package venus.task.analyse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.IndexDayMapper;
import venus.dao.StockCompanyFinanceMapper;
import venus.dao.StockDayFuMapper;
import venus.dao.StockinfoMapper;
import venus.helper.exception.BizException;
import venus.helper.util.NumUtil;
import venus.model.dao.IndexDay;
import venus.model.dao.StockCompanyFinance;
import venus.model.dao.StockDayFu;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyFinanceAnalyse {
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired IndexDayMapper indexDayMapper;
	public void init(){

		for(int i=2000;i<=2016;i++){
			int mo=i;
			
			String startTime1=(mo-2)+"-12-31";
			String endTime1=(mo-1)+"-09-30";
			
			String startTime2=(mo-1)+"-06-30";
			String endTime2=mo+"-03-31";
			
			String type="simple";
			
			String buyStartTime=mo+"-04-30";
			String buyEndTime=(mo+1)+"-04-30";
			
			String hushen300Code="sh000300";
			
			double hushen300Rate=m4(hushen300Code, buyStartTime, buyEndTime);
			
			double rateTotal=0;
			int iTotal=0;
			List<Stockinfo> stocks=stockinfoMapper.findNotStop();
			for(Stockinfo stock:stocks){
				String code=stock.getCode();
				
				double val1=0;
				try{
					val1=m2(code,type,startTime1,endTime1);
				}catch(BizException e){
					continue;
				}
				
				double val2=0;
				try{
					val2=m2(code,type,startTime2,endTime2);
				}catch(BizException e){
					continue;
				}
				
				//股东权益合计
				//净利润
				List<StockCompanyFinance> 净利润=stockCompanyFinanceMapper.findCodeTypeMenuTime(code, type, "净利润", startTime2, endTime2);
				double 净利润数值=m1(净利润);
				List<StockCompanyFinance> 股东权益合计=stockCompanyFinanceMapper.findCodeTypeMenuTime(code, type, "股东权益合计", startTime2, endTime2);
				double 股东权益合计数值=m1(股东权益合计);
				
				double roe=NumUtil.format2(净利润数值*100/股东权益合计数值);
				
				if(roe<10)continue;
				
				double rate=NumUtil.format2((val2-val1)*100/val1);
				if(rate<-30&&rate>-50){
					rateTotal+=m3(code, buyStartTime, buyEndTime, hushen300Rate);
					iTotal++;
				}
				
			}
			double avg=NumUtil.format2(rateTotal/iTotal);
			
			System.out.println("----------------------");
			System.out.println("year	"+ mo);
			System.out.println("iTotal	="+iTotal);
			System.out.println("avg		="+avg);
		}
	}
	private double m4(String code,String startTime,String endTime){
		IndexDay start=indexDayMapper.findDtNear(code, startTime.replaceAll("-", ""));
		IndexDay end=indexDayMapper.findDtNear(code, endTime.replaceAll("-", ""));
		return NumUtil.format2((end.getClose_price()-start.getClose_price())*100/start.getClose_price());
	}
	private double m3(String code,String startTime,String endTime,double hushen300){
		StockDayFu start=stockDayFuMapper.findDtNear(code, startTime.replaceAll("-", ""));
		StockDayFu end=stockDayFuMapper.findDtNear(code, endTime.replaceAll("-", ""));
		
		return NumUtil.format2((end.getClose_price()-start.getClose_price())*100/start.getClose_price())-hushen300;
	}
	private double m2(String code,String type,String startTime,String endTime){
		List<StockCompanyFinance> 应收账款=stockCompanyFinanceMapper.findCodeTypeMenuTime(code, type, "应收账款", startTime, endTime);
		
		if(应收账款.size()!=4)throw new BizException("9999", "");
		
		double 应收账款数值=m1(应收账款);
		
		List<StockCompanyFinance> 应收票据=stockCompanyFinanceMapper.findCodeTypeMenuTime(code, type, "应收票据", startTime, endTime);
		if(应收票据.size()!=4)throw new BizException("9999", "");
		double 应收票据数值=m1(应收票据);
		
		double 应收=应收账款数值+应收票据数值;
		
		return 应收;
	}
	private double m1(List<StockCompanyFinance> list){
		double val=0;
		for(int i=0;i<list.size();i++){
			val+=list.get(i).getValue();
		}
		return val;
	}
	
}
