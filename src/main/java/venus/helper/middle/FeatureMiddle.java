package venus.helper.middle;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import venus.dao.IndexinfoMapper;
import venus.dao.StockDayFuMapper;
import venus.dao.FeatureMapper;
import venus.dao.FeatureSimilarMapper;
import venus.dao.IndexDayMapper;
import venus.dao.StockinfoMapper;
import venus.dao.TradeDayMapper;
import venus.feature.Feature;
import venus.helper.util.CommonUtil;
import venus.helper.util.Constant;
import venus.helper.util.NumUtil;
import venus.model.dao.Indexinfo;
import venus.model.dao.StockDayFu;
import venus.model.dao.FeatureSimilar;
import venus.model.dao.IndexDay;
import venus.model.dao.Stockinfo;
import venus.model.dao.TradeDay;
import venus.task.collect.IndexinfoTask;
import venus.task.collect.TradeDayTask;

@Component
public class FeatureMiddle extends ApplicationObjectSupport{
	Logger logger=Logger.getLogger(IndexinfoTask.class);
	
	@Autowired
	FeatureMapper featureMapper;
	@Autowired
	StockinfoMapper stockinfoMapper;
	@Autowired
	StockDayFuMapper stockDayFuMapper;
	@Autowired
	IndexDayMapper indexDayMapper;
	@Autowired
	FeatureSimilarMapper featureSimilarMapper;
	@Autowired
	TradeDayMapper tradeDayMapper;
	@Autowired
	TradeDayTask tradeDayTask;
	@Autowired
	IndexinfoMapper indexinfoMapper;
	
	public void initIndexFeature(String featureStr,String start_time,String end_time){
		logger.info("[start]"+featureStr+","+start_time+","+end_time);
		try{
			tradeDayTask.init();
			Feature feature=(Feature)getApplicationContext().getBean(featureStr);
			List<TradeDay> tradeDays=tradeDayMapper.findTime(start_time,end_time);
			
			List<Indexinfo> indexinfos=indexinfoMapper.find();
			for(int i=0;i<indexinfos.size();i++){
				Indexinfo indexinfo=indexinfos.get(i);
				String code=indexinfo.getCode();
				StringBuffer sb=new StringBuffer();
				for(int j=0;j<tradeDays.size();j++){
					String dt=tradeDays.get(j).getDt();
					IndexDay stockDay=indexDayMapper.findIndexDay(code, dt);
					sb.append(feature.feature(stockDay));
				}
				
				featureMapper.delete(code, featureStr,start_time,end_time,Constant.CODE$INDEX);
				featureMapper.insert(code, featureStr,start_time,end_time,Constant.CODE$INDEX, sb.toString());
				
				logger.info(sb.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]",e);
		}
		logger.info("[end]"+featureStr+","+start_time+","+end_time);
	}
	
	public void initStockFeature(String featureStr,String start_time,String end_time){
		logger.info("[start]"+featureStr+","+start_time+","+end_time);
		try{
			tradeDayTask.init();
			Feature feature=(Feature)getApplicationContext().getBean(featureStr);
			List<TradeDay> tradeDays=tradeDayMapper.findTime(start_time,end_time);
			
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(int i=0;i<stocks.size();i++){
				String code=stocks.get(i).getCode();
				StringBuffer sb=new StringBuffer();
				for(int j=0;j<tradeDays.size();j++){
					String dt=tradeDays.get(j).getDt();
					StockDayFu stockDay=stockDayFuMapper.findStockDayFu(code, dt);
					sb.append(feature.feature(stockDay));
				}
				
				featureMapper.delete(code, featureStr,start_time,end_time,Constant.CODE$STOCK);
				featureMapper.insert(code, featureStr,start_time,end_time,Constant.CODE$STOCK, sb.toString());
				
				logger.info(sb.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]",e);
		}
		logger.info("[end]"+featureStr+","+start_time+","+end_time);
	}
	
	public void compareStockStock(String feature_type,String start_time,String end_time){
		logger.info("[start]"+feature_type+","+start_time+","+end_time);
		try{
			featureSimilarMapper.deleteType(feature_type, start_time, end_time,Constant.CODE$STOCK);
	
			List<FeatureSimilar> result=new ArrayList<FeatureSimilar>();
			int  m=0;
			List<venus.model.dao.Feature> stockFeatures=featureMapper.findTypeO(feature_type,start_time,end_time,Constant.CODE$STOCK);
			for(int i=0;i<stockFeatures.size();i++){
				for(int j=i;j<stockFeatures.size();j++){
					String code_a=stockFeatures.get(i).getCode();
					String code_b=stockFeatures.get(j).getCode();
					
					if(code_a.equals(code_b)){
						continue;
					}
					
					String feature_a=stockFeatures.get(i).getFeature();
					String feature_b=stockFeatures.get(j).getFeature();
					
					double rate=CommonUtil.compare(feature_a, feature_b);
					if(Double.isInfinite(rate) || Double.isNaN(rate)){
						rate=0;
					}
					rate=NumUtil.format4(rate);
					
					FeatureSimilar temp=new FeatureSimilar();
					temp.setCode_a(code_a);
					temp.setCode_b(code_b);
					temp.setSimilar(rate);
					temp.setFeature_type(feature_type);
					temp.setStart_time(start_time);
					temp.setEnd_time(end_time);
					temp.setSimilar_type(Constant.CODE$STOCK);
					result.add(temp);
					
					if(result.size()%100000==0){
						featureSimilarMapper.insertAll(result);
						result.clear();
						m++;
						logger.info(m*100000);
					}
				}
			}
			featureSimilarMapper.insertAll(result);
			result.clear();
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]"+feature_type+","+start_time+","+end_time);
	}
	public void compareStockIndex(String feature_type,String start_time,String end_time){
		logger.info("[start]"+feature_type+","+start_time+","+end_time);
		try{
			featureSimilarMapper.deleteType(feature_type, start_time, end_time,Constant.CODE$INDEX);
	
			List<FeatureSimilar> result=new ArrayList<FeatureSimilar>();
			int  m=0;
			List<venus.model.dao.Feature> indexFeatures=featureMapper.findTypeO(feature_type,start_time,end_time,Constant.CODE$INDEX);
			List<venus.model.dao.Feature> stockFeatures=featureMapper.findTypeO(feature_type,start_time,end_time,Constant.CODE$STOCK);
			
			for(int i=0;i<stockFeatures.size();i++){
				for(int j=0;j<indexFeatures.size();j++){
					String code_a=stockFeatures.get(i).getCode();
					String code_b=indexFeatures.get(j).getCode();
					
					if(code_a.equals(code_b)){
						continue;
					}
					
					String feature_a=stockFeatures.get(i).getFeature();
					String feature_b=indexFeatures.get(j).getFeature();
					
					double rate=CommonUtil.compare(feature_a, feature_b);
					if(Double.isInfinite(rate) || Double.isNaN(rate)){
						rate=0;
					}
					rate=NumUtil.format4(rate);
					
					FeatureSimilar temp=new FeatureSimilar();
					temp.setCode_a(code_a);
					temp.setCode_b(code_b);
					temp.setSimilar(rate);
					temp.setFeature_type(feature_type);
					temp.setStart_time(start_time);
					temp.setEnd_time(end_time);
					temp.setSimilar_type(Constant.CODE$INDEX);
					result.add(temp);
					
					if(result.size()%100000==0){
						featureSimilarMapper.insertAll(result);
						result.clear();
						m++;
						logger.info(m*100000);
					}
				}
			}
			featureSimilarMapper.insertAll(result);
			result.clear();
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]"+feature_type+","+start_time+","+end_time);
	}
}
