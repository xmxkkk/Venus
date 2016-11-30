package venus.task.analyse;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.IndexDayMapper;
import venus.dao.StockCompanyHangyeMapper;
import venus.dao.StockCompanySummaryMapper;
import venus.dao.StockDayFuMapper;
import venus.helper.util.NumUtil;
import venus.model.dao.IndexDay;
import venus.model.dao.StockCompanyHangye;
import venus.model.dao.StockCompanySummary;
import venus.model.dao.StockDayFu;

@Component
public class HangyeTimeTask {
	Logger logger=Logger.getLogger(HangyeTimeTask.class);
	@Autowired StockCompanyHangyeMapper stockCompanyHangyeMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired IndexDayMapper indexDayMapper;
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	
	public void init(){
		List<StockCompanyHangye> stockCompanyHangyes=stockCompanyHangyeMapper.findLevel3("仪器仪表");
		
		String[] years={"2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017"};
		String startDt="0101";
		String endDt="1231";
		
		for(int j=0;j<years.length-1;j++){
			int cnt=0;
			double avgRate=0.0;
			for(int i=0;i<stockCompanyHangyes.size();i++){
				String code=stockCompanyHangyes.get(i).getCode();
			
				StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
				
				String ssYear=stockCompanySummary.getShangshiriqi().substring(0, 4);
				if(years[j].compareTo(ssYear)<0){
					continue;
				}
				
				StockDayFu startStockDayFu=stockDayFuMapper.findDtNear(code, years[j]+startDt);
				StockDayFu endStockDayFu=stockDayFuMapper.findDtNear(code, years[j+1]+endDt);
				double rate=NumUtil.format2((endStockDayFu.getClose_price()-startStockDayFu.getClose_price())*100.0/startStockDayFu.getClose_price());
				
				String indexCode="sh000300";
				IndexDay startIndexDay=indexDayMapper.findDtNear(indexCode, years[j]+startDt);
				IndexDay endIndexDay=indexDayMapper.findDtNear(indexCode, years[j+1]+endDt);
				double indexRate=NumUtil.format2((endIndexDay.getClose_price()-startIndexDay.getClose_price())*100.0/startIndexDay.getClose_price());
				
				rate-=indexRate;
				avgRate+=rate;
				cnt++;
			}
			
			avgRate=NumUtil.format2(avgRate/cnt);
			
			logger.info(years[j]+"	:"+avgRate);
		}
		
	}
}
