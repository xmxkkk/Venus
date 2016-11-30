package venus.task.collect;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.HangyeChangeRateMapper;
import venus.dao.StockCompanyHangyeMapper;
import venus.dao.StockDayFuMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.HangyeChangeRate;
import venus.model.dao.StockCompanyHangye;
import venus.model.dao.StockDayFu;

@Component
public class HangyeChangeRateTask {
	Logger logger=Logger.getLogger(HangyeChangeRateTask.class);
	@Autowired HangyeChangeRateMapper hangyeChangeRateMapper;
	@Autowired StockCompanyHangyeMapper stockCompanyHangyeMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	public void init(){
		String[] levelStrs={"level1","level2","level3"};
		for(String levelStr:levelStrs){
			hangyeChangeRateMapper.deleteLevel(levelStr);
			
			List<String> levels=null;
			if(levelStr.equals("level1")){
				levels=stockCompanyHangyeMapper.findLevel1Group();
			}else if(levelStr.equals("level2")){
				levels=stockCompanyHangyeMapper.findLevel2Group();
			}else if(levelStr.equals("level3")){
				levels=stockCompanyHangyeMapper.findLevel3Group();
			}
			
			int[] months={-7,-14,-21,-30,-90,-180,-365,-365*2,-365*3,-365*4,-365*5,-365*6,-365*7,-365*8,-365*9,-365*10};
			String currentDate=DateUtil.date();
			int[] monthIs=new int[months.length];
			double[] mongthAvgRates=new double[months.length];
			String[] monthDates=new String[months.length];
			for(int i=0;i<months.length;i++){
				monthDates[i]=DateUtil.date(currentDate, months[i]);
				monthIs[i]=0;
				mongthAvgRates[i]=0;
			}
			
			for(String level:levels){
				List<StockCompanyHangye> stockCompanyHangyes=null;
				if(levelStr.equals("level1")){
					stockCompanyHangyes=stockCompanyHangyeMapper.findLevel1(level);
				}else if(levelStr.equals("level2")){
					stockCompanyHangyes=stockCompanyHangyeMapper.findLevel2(level);
				}else if(levelStr.equals("level3")){
					stockCompanyHangyes=stockCompanyHangyeMapper.findLevel3(level);
				}
				
				for(StockCompanyHangye stockCompanyHangye:stockCompanyHangyes){
					String code=stockCompanyHangye.getCode();
					StockDayFu endStockDayFu=stockDayFuMapper.findDtNear(code, currentDate);
					
					for(int i=0;i<months.length;i++){
						StockDayFu monthStartStockDayFu=stockDayFuMapper.findDtNear(code, monthDates[i]);
						double monthRate=(endStockDayFu.getClose_price()-monthStartStockDayFu.getClose_price())/monthStartStockDayFu.getClose_price();
						mongthAvgRates[i]+=monthRate;
						monthIs[i]++;
					}
				}
				StringBuffer sb=new StringBuffer();
				for(int i=0;i<months.length;i++){
					if(monthIs[i]==0){
						mongthAvgRates[i]=0;
					}else{
						mongthAvgRates[i]=NumUtil.format2(mongthAvgRates[i]*100/monthIs[i]);
					}
					sb.append("	:"+mongthAvgRates[i]);
				}
				
				HangyeChangeRate hangyeChangeRate=new HangyeChangeRate();
				hangyeChangeRate.setLevel(levelStr);
				hangyeChangeRate.setName(level);
				hangyeChangeRate.setNum(stockCompanyHangyes.size());
				hangyeChangeRate.setDay7_change_rate(mongthAvgRates[0]);
				hangyeChangeRate.setDay14_change_rate(mongthAvgRates[1]);
				hangyeChangeRate.setDay21_change_rate(mongthAvgRates[2]);
				hangyeChangeRate.setMonth1_change_rate(mongthAvgRates[3]);
				hangyeChangeRate.setMonth3_change_rate(mongthAvgRates[4]);
				hangyeChangeRate.setMonth6_change_rate(mongthAvgRates[5]);
				hangyeChangeRate.setYear1_change_rate(mongthAvgRates[6]);
				hangyeChangeRate.setYear2_change_rate(mongthAvgRates[7]);
				hangyeChangeRate.setYear3_change_rate(mongthAvgRates[8]);
				hangyeChangeRate.setYear4_change_rate(mongthAvgRates[9]);
				hangyeChangeRate.setYear5_change_rate(mongthAvgRates[10]);
				hangyeChangeRate.setYear6_change_rate(mongthAvgRates[11]);
				hangyeChangeRate.setYear7_change_rate(mongthAvgRates[12]);
				hangyeChangeRate.setYear8_change_rate(mongthAvgRates[13]);
				hangyeChangeRate.setYear9_change_rate(mongthAvgRates[14]);
				hangyeChangeRate.setYear10_change_rate(mongthAvgRates[15]);
				
				hangyeChangeRateMapper.insert(hangyeChangeRate);
				
				logger.info(levelStr+"	,"+level+"			"+"	("+stockCompanyHangyes.size()+")"+sb.toString());
			}
		}
	}
}
