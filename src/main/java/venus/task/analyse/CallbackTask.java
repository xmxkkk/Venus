package venus.task.analyse;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.IndexDayMapper;
import venus.dao.StockCompanyHangyeMapper;
import venus.dao.StockCompanySummaryMapper;
import venus.dao.StockDayFuMapper;
import venus.dao.StockDayMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.IndexDay;
import venus.model.dao.StockCompanyHangye;
import venus.model.dao.StockCompanySummary;
import venus.model.dao.StockDay;
import venus.model.dao.StockDayFu;
import venus.model.dao.Stockinfo;
import venus.strategy.stockfilter.filter.impl.JingzichanshouyilvStockFilter;

@Component
public class CallbackTask {
	Logger logger=Logger.getLogger(CallbackTask.class);
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired IndexDayMapper indexDayMapper;
	@Autowired JingzichanshouyilvStockFilter jingzichanshouyilvStockFilter;
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	@Autowired StockCompanyHangyeMapper stockCompanyHangyeMapper;
	@Autowired StockDayMapper stockDayMapper;
	public void init(){
		logger.info("[start]");
		try{
			String endDt="20161113";
			String startDt=DateUtil.date(endDt, -90);
			
			List<String> codes=new ArrayList<>();
			
			List<Stockinfo> stocks=stockinfoMapper.findNotStop();
			for(Stockinfo stock:stocks){
				String code=stock.getCode();
				double rateRelative=calcChangeRateRelative(code,startDt,endDt);
				if(rateRelative>-25){
					continue;
				}
				double rateAbsolute=calcChangeRateAbsolute(code,startDt,endDt);
				
				codes.add(code);
	
				logger.info("[message]"+code+"	,"+rateRelative+"	,"+rateAbsolute);
			}
			
			
			for(int i=0;i<codes.size();i++){
				String code=codes.get(i);
				Stockinfo stock=stockinfoMapper.findStockinfo(code);
				StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
				StockCompanyHangye stockCompanyHangye=stockCompanyHangyeMapper.findCode(code);
				StockDay stockDay=stockDayMapper.findLast(code);
				
				Double zongshizhi=stockCompanySummary.getZongshizhi();
				zongshizhi=zongshizhi==null?0:zongshizhi;
				
				logger.info(stock.getCode()+"="+stock.getName()+"	"+",市盈率="+stockCompanySummary.getShiyinglvttm()+",	净收="+stockCompanySummary.getJingzichanshouyilv()
				+",	总市值:"+NumUtil.format(zongshizhi/100000000,0)+",	行业:"+stockCompanyHangye.getLevel1()+",	价格:"+stockDay.getClose_price());
			}
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
	
	private double calcChangeRateRelative(String code,String startTime,String endTime){
		StockDayFu startStockDayFu=stockDayFuMapper.findDtNear(code, startTime);
		StockDayFu endStockDayFu=stockDayFuMapper.findDtNear(code, endTime);
		if(endStockDayFu.getClose_price()==0)return 0;
		double stockRate=NumUtil.format2(100.0*(endStockDayFu.getClose_price()-startStockDayFu.getClose_price())/startStockDayFu.getClose_price());

		String indexCode="sh000300";
		IndexDay startIndexDay=indexDayMapper.findDtNear(indexCode, startTime);
		IndexDay endIndexDay=indexDayMapper.findDtNear(indexCode, endTime);
		if(endIndexDay.getClose_price()==0)return 0;
		double indexRate=NumUtil.format2(100.0*(endIndexDay.getClose_price()-startIndexDay.getClose_price())/startIndexDay.getClose_price());
		
		return NumUtil.format2(stockRate-indexRate);
	}
	private double calcChangeRateAbsolute(String code,String startTime,String endTime){
		StockDayFu startStockDayFu=stockDayFuMapper.findDtNear(code, startTime);
		StockDayFu endStockDayFu=stockDayFuMapper.findDtNear(code, endTime);
		if(endStockDayFu.getClose_price()==0)return 0;
		double stockRate=NumUtil.format2(100.0*(endStockDayFu.getClose_price()-startStockDayFu.getClose_price())/startStockDayFu.getClose_price());

		return NumUtil.format2(stockRate);
	}
}
