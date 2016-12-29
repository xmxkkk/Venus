package venus.task.collect;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import venus.dao.IndexDayMapper;
import venus.dao.IndexinfoMapper;
import venus.helper.exception.BizException;
import venus.helper.util.Constant;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.IndexDay;
import venus.model.dao.Indexinfo;

@Component
public class IndexDayTask {
	Logger logger=Logger.getLogger(IndexDayTask.class);
	@Autowired IndexinfoMapper indexinfoMapper;
	@Autowired IndexDayMapper indexDayMapper;
	@Autowired URLUtil URLUtil;
	@Value("${index-day-task-threadnum}")
	public int threadNum;
	
	public void init(int threadId,boolean clearCache,boolean updateNew){
		logger.info("[start]threadId="+threadId+",clearCache="+clearCache+",updateNew="+updateNew);
		try{
			List<Indexinfo> stocks=null;
			if(updateNew){
				stocks=indexinfoMapper.find();
			}else{
				stocks=indexinfoMapper.findFlag(0);
			}
			
			int currentSeason=DateUtil.currentSeason();
			String currentYear=DateUtil.currentYear();
			
			for(int i=0;i<stocks.size();i++){
				if (i % threadNum != threadId) {
					continue;
				}
				
				Indexinfo stock = stocks.get(i);
				String code=stock.getCode();
				String codeStr=stock.getCode().substring(2);

				IndexDay lastIndexDay=indexDayMapper.findLast(code);
				
				List<IndexDay> insertAll=new ArrayList<IndexDay>();
				
				String str=null;
				try{
					str=URLUtil.url2str("http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/"+codeStr+"/type/S.phtml",Constant.CHARSET$GB2312, false);
				}catch(Exception e){
					logger.error("[except]StockCompanyEventTask.init:",e);
					continue;
				}
				
				if(StringUtil.isBlank(str)){
					logger.error("[message]"+stock);
					continue;
				}
				Document document=Jsoup.parse(str);
				
				boolean conti=true;

				List<String> yearsList=new ArrayList<String>();
				Elements yearsElements=document.select("#center select[name=year] option");
				for(int j=yearsElements.size()-1;j>=0;j--){
					yearsList.add(yearsElements.get(j).text());
				}
				
				for(int j=0;j<yearsList.size();j++){
					if(!conti)break;
					
					String year=yearsList.get(j);
					for(int k=1;k<=4;k++){
						
						if(lastIndexDay!=null){
							String lastYear=DateUtil.currentYear(lastIndexDay.getDt());
							int lastSeason=DateUtil.currentSeason(lastIndexDay.getDt());
							
							if((year+k).compareTo(lastYear+lastSeason)<0){
								continue;
							}
						}
						
						boolean cache=true;
						if(year.equals(currentYear)&&k==currentSeason){
							cache=false;
						}
						String html=null;
						try{
							String url="http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/"+codeStr+"/type/S.phtml?year="+year+"&jidu="+k;
							if(clearCache){
								URLUtil.clearCache(url);
							}
							html=URLUtil.url2str(url,Constant.CHARSET$GB2312, cache);
						}catch(Exception e){
							logger.error("[message]"+code+","+year+","+k);
							logger.error("[message]",e);
							conti=false;
							break;
						}
						if(StringUtil.isBlank(html)){
							conti=false;
							break;
						}
						if(k==1){
							indexDayMapper.deleteCodeDt(code, year+"0101", year+"0331");
						}else if(k==2){
							indexDayMapper.deleteCodeDt(code, year+"0401", year+"0630");
						}else if(k==3){
							indexDayMapper.deleteCodeDt(code, year+"0701", year+"0930");
						}else if(k==4){
							indexDayMapper.deleteCodeDt(code, year+"1001", year+"1231");
						}
						
						Document doc=Jsoup.parse(html);
						Elements rows=doc.select("#FundHoldSharesTable tr");
						for(int m=2;m<rows.size();m++){
							Elements cells=rows.get(m).children();
							if(cells.size()!=7){
								continue;
							}
							String dt=cells.get(0).text().replaceAll("-", "");
							double open_price=NumUtil.format4(Double.parseDouble(cells.get(1).text()));
							double high_price=NumUtil.format4(Double.parseDouble(cells.get(2).text()));
							double close_price=NumUtil.format4(Double.parseDouble(cells.get(3).text()));
							double low_price=NumUtil.format4(Double.parseDouble(cells.get(4).text()));
							double trade_quty=NumUtil.format4(Double.parseDouble(cells.get(5).text()));
							double trade_amt=NumUtil.format4(Double.parseDouble(cells.get(6).text()));
							
							IndexDay stockDay=new IndexDay();
							stockDay.setCode(code);
							stockDay.setDt(dt);
							stockDay.setWeek(DateUtil.date2week(dt));
							stockDay.setOpen_price(open_price);
							stockDay.setClose_price(close_price);
							stockDay.setHigh_price(high_price);
							stockDay.setLow_price(low_price);
							stockDay.setTrade_amt(trade_amt);
							stockDay.setTrade_quty(trade_quty);
							stockDay.setChange_rate(null);
							stockDay.setChange_price(null);
							
							insertAll.add(stockDay);
							
							if(insertAll.size()==1000){
								indexDayMapper.insertAll(insertAll);
								logger.info(code+":"+insertAll.size());
								insertAll.clear();
							}
						}
					}
				}
				if(insertAll.size()>0){
					indexDayMapper.insertAll(insertAll);
					logger.info(code+":"+insertAll.size());
					insertAll.clear();
				}
				
				indexinfoMapper.updateFlag(code, 1);
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]threadId="+threadId+",clearCache="+clearCache+",updateNew="+updateNew);
	}
	public void updateChange(int threadId,String stockCode){
		logger.info("[start]"+threadId);
		try{
			List<Indexinfo> indexs=null;
			if(stockCode==null){
				indexs=indexinfoMapper.find();
			}else{
				Indexinfo index=indexinfoMapper.findCode(stockCode);
				indexs=new ArrayList<Indexinfo>();
				indexs.add(index);
			}
			
			for (int i=0;i<indexs.size();i++) {
				if (i % threadNum != threadId) {
					continue;
				}
				
				Indexinfo index=indexs.get(i);
				
				IndexDay lastOne=indexDayMapper.findLast(index.getCode());
				
				/**/
				if(lastOne!=null){
					if(lastOne.getChange_rate()!=null){
						logger.info(index);
						continue;
					}
				}
				
				List<IndexDay> updateChangeAll=new ArrayList<IndexDay>();
				
				List<IndexDay> indexDays=indexDayMapper.findCode(index.getCode());
				for(int j=0;j<indexDays.size();j++){
					IndexDay indexDay=indexDays.get(j);
					
					double change_rate=0;
					double change_price=0;
					if(j==0){
						change_rate=(indexDay.getClose_price()-indexDay.getOpen_price())/indexDay.getOpen_price();
						if(Double.isNaN(change_rate) || Double.isInfinite(change_rate)){
							change_rate=0;
						}
						change_rate=NumUtil.format4(change_rate);
						change_price=NumUtil.format4(indexDay.getClose_price()-indexDay.getOpen_price());
					}else{
						IndexDay leftDay=indexDays.get(j-1);
						change_rate=(indexDay.getClose_price()-leftDay.getClose_price())/leftDay.getClose_price();
						if(Double.isNaN(change_rate) || Double.isInfinite(change_rate)){
							change_rate=0;
						}
						change_rate=NumUtil.format4(change_rate);
						change_price=NumUtil.format4(indexDay.getClose_price()-leftDay.getClose_price());
					}
					indexDay.setChange_rate(change_rate);
					indexDay.setChange_price(change_price);
					
					updateChangeAll.add(indexDay);
					
					if(updateChangeAll.size()==1000){
						indexDayMapper.updateChangeAll(updateChangeAll);
						updateChangeAll.clear();
					}
				}
				if(updateChangeAll.size()>0){
					indexDayMapper.updateChangeAll(updateChangeAll);
					updateChangeAll.clear();
				}
				
				logger.info(index);
			}
		} catch (Exception e) {
			logger.error("[except]",e);
		}
		logger.info("[end]"+threadId);
	}
}
