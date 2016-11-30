package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHangyeDataMapper;
import venus.dao.StockCompanyHangyeMapper;
import venus.helper.util.CommonUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.StockCompanyHangye;
import venus.model.dao.StockCompanyHangyeData;
import venus.strategy.stockfilter.filter.StockFilter;

/**
 * 行业净资产收益率
 * @author Administrator
 *
 */
@Component
public class JingzichanshouyilvhangyeStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(JingzichanshouyilvhangyeStockFilter.class);
	@Autowired StockCompanyHangyeDataMapper stockCompanyHangyeDataMapper;
	@Autowired StockCompanyHangyeMapper stockCompanyHangyeMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanyHangye stockCompanyHangye=stockCompanyHangyeMapper.findCode(code);
			StockCompanyHangyeData stockCompanyHangyeData=stockCompanyHangyeDataMapper.findNew(code);
			if(stockCompanyHangye==null||stockCompanyHangyeData==null)return false;
				
			List<StockCompanyHangyeData> stockCompanyHangyeDatas=stockCompanyHangyeDataMapper.findLevel3(stockCompanyHangye.getLevel3(), stockCompanyHangyeData.getDate());
			
			double rankd=0.0;
			if(stockCompanyHangyeDatas.size()==1){
				
			}else{
				int rank=stockCompanyHangyeDatas.size();
				for(int i=0;i<stockCompanyHangyeDatas.size();i++){
					if(stockCompanyHangyeDatas.get(i).getJingzichanshouyilv()<=stockCompanyHangyeData.getJingzichanshouyilv()){
						rank--;
					}
				}
				rankd=NumUtil.format2(1.0*rank/(stockCompanyHangyeDatas.size()-1));
			}
			result= CommonUtil.compareExpressionDouble(rankd, params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}

}
