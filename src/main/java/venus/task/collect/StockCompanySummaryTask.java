package venus.task.collect;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
import venus.dao.StockCompanySummaryMapper;
import venus.dao.StockDayMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.Constant;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyFinance;
import venus.model.dao.StockCompanySummary;
import venus.model.dao.StockDay;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanySummaryTask {
	Logger logger=Logger.getLogger(StockCompanySummaryTask.class);
	@Value("${stock-company-summary-threadnum}")
	public int threadNum;
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockDayMapper stockDayMapper;
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	@Autowired URLUtil URLUtil;
	public void init(String stockCode,int threadId){
		init(false,stockCode,threadId);
	}
	public void initCache(String stockCode,int threadId){
		init(true,stockCode,threadId);
	}
	private void init(boolean cacheParam,String stockCode,int threadId){
		//http://hq.sinajs.cn/?list=sh600000,sh600004,sh600005,sh600006,sh600007,sh600008,sh600009,sh600010,sh600011,sh600012,sh600015,sh600016,sh600017,sh600018,sh600019,sh600020,sh600021,sh600022,sh600023,sh600026,sh600027,sh600028,sh600029,sh600030,sh600031,sh600033,sh600035,sh600036,sh600037,sh600038,sh600039,sh600048,sh600050,sh600051,sh600052,sh600053,sh600054,sh600055,sh600056,sh600057
		logger.info("[start]"+cacheParam+","+stockCode);
		try{
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks=new ArrayList<Stockinfo>();
				stocks.add(stock);
			}
			for(int i=0;i<stocks.size();i++){
				Stockinfo stock=stocks.get(i);
				
				if(stockCode==null){
					if (stock.getCode().hashCode() % threadNum != threadId) {
						continue;
					}
				}

				String str=null;
				try{
					str=URLUtil.url2str("http://hq.sinajs.cn/?list="+StringUtil.toMarketName(stock),Constant.CHARSET$GB2312, cacheParam);
				}catch(Exception e){
					logger.error("[except]",e);
					continue;
				}
				
				if(StringUtil.isBlank(str))continue;
				
				int start=str.indexOf("=\"");
				int end=str.lastIndexOf("\";");
				str=str.substring(start+2, end);
				
				String[] datas=str.split(",");
				
				if(datas.length<5){
					stock.setStop(-1);
					stockinfoMapper.updateStockinfo(stock);
					continue;
				}
				
				double open_price=NumUtil.text2num(datas[1]);
				double close_price=NumUtil.text2num(datas[2]);
				double curr_price=NumUtil.text2num(datas[3]);
				double high_price=NumUtil.text2num(datas[4]);
				double low_price=NumUtil.text2num(datas[5]);
				
				double chengjiaoliang=NumUtil.text2num(datas[8]);
				double chengjiaoe=NumUtil.text2num(datas[9]);
				
				StockCompanySummary stockCompanySummary=new StockCompanySummary();
				stockCompanySummary.setCode(stock.getCode());
				stockCompanySummary.setOpen_price(open_price);
				stockCompanySummary.setClose_price(close_price);
				stockCompanySummary.setCurr_price(curr_price);
				stockCompanySummary.setHigh_price(high_price);
				stockCompanySummary.setLow_price(low_price);
				stockCompanySummary.setChengjiaoliang(chengjiaoliang);
				stockCompanySummary.setChengjiaoe(chengjiaoe);
				
				stockCompanySummary.setUpdate_time(DateUtil.datetime());
				
				logger.info(stockCompanySummary);
				
				StockCompanySummary stockCompanySummaryDB=stockCompanySummaryMapper.findCode(stock.getCode());
				if(stockCompanySummaryDB==null){
					stockCompanySummaryMapper.insert(stockCompanySummary);
				}else{
					stockCompanySummaryMapper.updateBase(stockCompanySummary);
				}
				
//				stockCompanySummaryMapper.delete(stock.getCode());
//				stockCompanySummaryMapper.insert(stockCompanySummary);
			}
			
			initOther(cacheParam, stockCode);
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]"+cacheParam);
	}
	
	public void initOther(boolean cacheParam,String stockCode){
		logger.info("[start]"+cacheParam);
		try{
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks=new ArrayList<Stockinfo>();
				stocks.add(stock);
			}
			for(int i=0;i<stocks.size();i++){
				Stockinfo stock=stocks.get(i);
				String code=stock.getCode();
				
				String str=null;
				try{
					str=URLUtil.url2str("http://finance.sina.com.cn/realstock/company/"+StringUtil.toMarketName(stock)+"/nc.shtml",Constant.CHARSET$GB2312, cacheParam);
				}catch(Exception e){
					logger.error("[except]",e);
					continue;
				}
	//			private double zongguben;
	//			private double liutongguben;
	//			private double meigujingzichan;
	//			private double zuijinniandujinglirun;
	//			private double zuijinsigejidujinglirun;
	//			private double zuijinsigejidumeigushouyi;
	//			private double zuijinniandumeigushouyi;
	
				StockCompanySummary stockCompanySummary=new StockCompanySummary();
				stockCompanySummary.setCode(stock.getCode());
				
				Matcher m=Pattern.compile("var totalcapital = ([0-9\\.-]+);").matcher(str);
				while(m.find()){
					double zongguben=Double.parseDouble(m.group(1))*10000;
					stockCompanySummary.setZongguben(zongguben);
				}
				m=Pattern.compile("var currcapital = ([0-9\\.-]+);").matcher(str);
				while(m.find()){
					double liutongguben=Double.parseDouble(m.group(1))*10000;
					stockCompanySummary.setLiutongguben(liutongguben);
				}
				if(stockCompanySummary.getLiutongguben()<0.0001){
					double liutongguben=0;
					m=Pattern.compile("var curracapital = ([0-9\\.-]+);").matcher(str);
					while(m.find()){
						double liutongagu=Double.parseDouble(m.group(1))*10000;
						liutongguben+=liutongagu;
					}
					m=Pattern.compile("var currbcapital = ([0-9\\.-]+);").matcher(str);
					while(m.find()){
						double liutongbgu=Double.parseDouble(m.group(1))*10000;
						liutongguben+=liutongbgu;
					}
					stockCompanySummary.setLiutongguben(liutongguben);
				}
				
				m=Pattern.compile("var mgjzc = ([0-9\\.-]+);").matcher(str);
				while(m.find()){
					double meigujingzichan=Double.parseDouble(m.group(1));
					stockCompanySummary.setMeigujingzichan(meigujingzichan);
				}
				
				m=Pattern.compile("var fourQ_mgsy = ([0-9\\.-]+);").matcher(str);
				while(m.find()){
					double zuijinsigejidumeigushouyi=Double.parseDouble(m.group(1));
					stockCompanySummary.setZuijinsigejidumeigushouyi(zuijinsigejidumeigushouyi);
				}
				
				m=Pattern.compile("var lastyear_mgsy = ([0-9\\.-]+);").matcher(str);
				while(m.find()){
					double zuijinniandumeigushouyi=Double.parseDouble(m.group(1));
					stockCompanySummary.setZuijinniandumeigushouyi(zuijinniandumeigushouyi);
				}
				
				double zuijinniandujinglirun=0;
				List<StockCompanyFinance> zuijinniandujingliruns=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "year", "净利润",1);
				if(zuijinniandujingliruns.size()==1){
					zuijinniandujinglirun=zuijinniandujingliruns.get(0).getValue();
					zuijinniandujinglirun*=10000;
				}
				stockCompanySummary.setZuijinniandujinglirun(zuijinniandujinglirun);
				
				
				List<StockCompanyFinance> zuijinsigejidujingliruns=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "simple", "净利润",4);
				double zuijinsigejidujinglirun=0;
				if(zuijinsigejidujingliruns.size()==4){
					for (StockCompanyFinance stockCompanyFinance : zuijinsigejidujingliruns) {
						zuijinsigejidujinglirun+=stockCompanyFinance.getValue();
					}
					zuijinsigejidujinglirun *= 10000;
				}
				stockCompanySummary.setZuijinsigejidujinglirun(zuijinsigejidujinglirun);
				
				m=Pattern.compile("<p><b>注册资本：</b>([0-9\\.-]+)万元</p>").matcher(str);
				while(m.find()){
					double zhuceziben=Double.parseDouble(m.group(1))*10000;
					stockCompanySummary.setZhuceziben(zhuceziben);
				}
				
				m=Pattern.compile("<p><b>发行价格：</b>([0-9\\.-]+)元</p>").matcher(str);
				while(m.find()){
					double faxingjiage=Double.parseDouble(m.group(1));
					stockCompanySummary.setFaxingjiage(faxingjiage);
				}
				m=Pattern.compile("<p><b>成立日期：</b>([0-9\\.-]+)</p>").matcher(str);
				while(m.find()){
					String chengliriqi=m.group(1);
					stockCompanySummary.setChengliriqi(chengliriqi);
				}
				m=Pattern.compile("<p><b>上市日期：</b>([0-9\\.-]+)</p>").matcher(str);
				while(m.find()){
					String shangshiriqi=m.group(1);
					stockCompanySummary.setShangshiriqi(shangshiriqi);
				}
				
					
	//			<p><b>成立日期：</b>1987-12-22</p>
	//			<p><b>上市日期：</b>1991-04-03</p>
	//			<p><b>发行价格：</b>40元</p>
	//			<p><b>注册资本：</b>101166万元</p>
				
				stockCompanySummary.setUpdate_time(DateUtil.datetime());
				
				stockCompanySummaryMapper.update(stockCompanySummary);
				logger.info(stockCompanySummary);
				
			}
			
			updateAllOther(stockCode);
			
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[info]"+cacheParam);
	}
	
	public void updateAllOther(String stockCode){
		logger.info("[start]");
		try{
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				stocks=stockinfoMapper.findNotStop();
			}else{
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks=new ArrayList<Stockinfo>();
				stocks.add(stock);
			}
			for(Stockinfo stock:stocks){
				String code=stock.getCode();
//				if(code.compareTo("601958")<0){
//					continue;
//				}
//				if(code.equals("601958")){
//					System.out.println();
//				}
				logger.info(code);
				StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
				if(stockCompanySummary==null){
					continue;
				}
				StockDay lastStockDay=stockDayMapper.findLast(code);
				if(lastStockDay==null){
					continue;
				}
				
				double zongshizhi=NumUtil.format2(lastStockDay.getClose_price()*stockCompanySummary.getZongguben());
				double liutongshizhi=NumUtil.format2(lastStockDay.getClose_price()*stockCompanySummary.getLiutongguben());
				double zhenfu=0;
				if(stockCompanySummary.getOpen_price()==0.0){
					zhenfu=0;
				}else{
					zhenfu=NumUtil.format2((stockCompanySummary.getHigh_price()-stockCompanySummary.getLow_price())*100.0/stockCompanySummary.getHigh_price());
				}
				double huanshoulv=0;
				if(stockCompanySummary.getOpen_price()==0.0){
					huanshoulv=0;
				}else{
					if(stockCompanySummary.getLiutongguben()==0.0){
						huanshoulv=0;
					}else{
						huanshoulv=NumUtil.format2(stockCompanySummary.getChengjiaoliang()*100.0/stockCompanySummary.getLiutongguben());
					}
				}
				
				double shijinglv=0.0;
				if(stockCompanySummary.getMeigujingzichan()!=0.0){
					shijinglv=NumUtil.format2(stockCompanySummary.getClose_price()/stockCompanySummary.getMeigujingzichan());
				}
				double shiyinglvttm=0;
				
				List<StockCompanyFinance> stockCompanyFinancess=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "simple", "基本每股收益", 4);
				if(stockCompanyFinancess!=null&&stockCompanyFinancess.size()==4){
					double lirun=0;
					for(int i=0;i<stockCompanyFinancess.size();i++){
						lirun+=stockCompanyFinancess.get(i).getValue();
					}
					lirun=NumUtil.format4(lirun);
					if(lirun!=0){
						shiyinglvttm=NumUtil.format4(lastStockDay.getClose_price()/lirun);
					}
				}

				double shiyinglvjing=0;
				List<StockCompanyFinance> stockCompanyFinance1=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "year", "基本每股收益", 1);
				if(stockCompanyFinance1!=null&&stockCompanyFinance1.size()==1){
					double lirunyear=stockCompanyFinance1.get(0).getValue();
					lirunyear=NumUtil.format4(lirunyear);
					if(lirunyear!=0){
						shiyinglvjing=lastStockDay.getClose_price()/lirunyear;
					}
				}
				
				double jingzichanshouyilv=0;
				List<StockCompanyFinance> stockCompanyFinance2=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "year", "净资产收益率", 1);
				if(stockCompanyFinance2!=null&&stockCompanyFinance2.size()==1){
					jingzichanshouyilv=stockCompanyFinance2.get(0).getValue();
				}
				
				String update_time=DateUtil.datetime();
				
				stockCompanySummary.setZongshizhi(zongshizhi);
				stockCompanySummary.setLiutongshizhi(liutongshizhi);
				stockCompanySummary.setZhenfu(zhenfu);
				stockCompanySummary.setHuanshoulv(huanshoulv);
				stockCompanySummary.setShijinglv(shijinglv);
				stockCompanySummary.setShiyinglvttm(shiyinglvttm);
				stockCompanySummary.setShiyinglvjing(shiyinglvjing);
				stockCompanySummary.setJingzichanshouyilv(jingzichanshouyilv);
				stockCompanySummary.setUpdate_time(update_time);
				
				stockCompanySummaryMapper.updateOther(stockCompanySummary);
			}
			
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]");
	}
	
}
