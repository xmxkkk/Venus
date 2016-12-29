package venus.task.collect;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderTopMapper;
import venus.dao.StockCompanySummaryMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyHolderTop;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyHolderTopTask {
	Logger logger=Logger.getLogger(StockCompanyHolderTopTask.class);
	@Value("${stock-company-holder-top-threadnum}")
	public int threadNum;
	@Autowired StockCompanyHolderTopMapper stockCompanyHolderTopMapper;
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	@Autowired URLUtil URLUtil;
	public void init(int threadId){
		init(false,threadId);
	}
	public void initCache(int threadId){
		init(true,threadId);
	}
	private void init(boolean cacheParam,int threadId){
		logger.info("[start]"+cacheParam);
		try{
			List<StockCompanyHolderTop> insertAll=new ArrayList<StockCompanyHolderTop>();
			
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(Stockinfo stock:stocks){
				if (stock.getCode().hashCode() % threadNum != threadId) {
					continue;
				}
				
				if(stockCompanySummaryMapper.findCode(stock.getCode())==null)continue;
				if(stockCompanySummaryMapper.findStop(stock.getCode())!=null)continue;
				
				//stockpage.10jqka.com.cn/600000/holder/
				String str=null;
				try{
					str=URLUtil.url2str("http://stockpage.10jqka.com.cn/"+stock.getCode()+"/holder/", cacheParam);
				}catch(Exception e){
					logger.error("[except]",e);
					continue;
				}
				
				if(StringUtil.isBlank(str))continue;
				Document document=Jsoup.parse(str);
				
				
				//fher_1
	//			bd_list1
	//			bd_list0
				
	//			top10_liutonggu_in
	//			top10_liutonggu_out
	//			top10_gudong_in
	//			top10_gudong_out
				
				Elements id1elements=document.select("#bd_list1 .m_tab ul li");
				Elements id2elements=document.select("#bd_list0 .m_tab ul li");
				
				List<String> times=new ArrayList<String>();
				List<String> ids1=new ArrayList<String>();
				List<String> ids2=new ArrayList<String>();
				for(int i=0;i<id1elements.size();i++){
					times.add(id1elements.get(i).text());
					ids1.add(id1elements.get(i).attr("name"));
					ids2.add(id2elements.get(i).attr("name"));
				}
				
				String[] types={"top10_liutonggu_in","top10_liutonggu_out","top10_gudong_in","top10_gudong_out"};
				String[] features={".ggintro tr","table[style=\"table-layout:fixed;\"] tr",".ggintro tr","table[style=\"table-layout: fixed;\"] tr"};
				for(int m=0;m<types.length;m++){
					String type=types[m];
					for(int i=0;i<ids1.size();i++){
						List<String> ids=null;
						if(m<2){
							ids=ids1;
						}else{
							ids=ids2;
						}
						Elements elements2=document.select("#"+ids.get(i));
						Element element2=elements2.get(0);
		
						Elements rows=element2.select(features[m]);
						for(int j=1;j<rows.size();j++){
							Elements elements3=rows.get(j).children();
							if(elements3.size()<7){
								continue;
							}
							String name=elements3.get(0).text();
							double stock_number=NumUtil.text2num(elements3.get(1).text());
							String stock_number_change=elements3.get(2).text();
							double stock_number_change_d=0;
							if(stock_number_change.equals("不变")){
								stock_number_change_d=0;//
							}else if(stock_number_change.equals("新进")){
								stock_number_change_d=100;
							}else if(stock_number_change.equals("退出")){
								stock_number_change_d=-100;
							}else{
								stock_number_change_d=NumUtil.text2num(stock_number_change);
							}
							
							double stock_rate=NumUtil.text2num(elements3.get(3).text());
							
							String stock_rate_change=elements3.get(4).text();
							double stock_rate_change_d=0;
							if(stock_rate_change.equals("不变")){
								stock_rate_change_d=0;//
							}else if(stock_rate_change.equals("新进")){
								stock_rate_change_d=100;
							}else if(stock_rate_change.equals("退出")){
								stock_rate_change_d=-100;
							}else{
								stock_rate_change_d=NumUtil.text2num(stock_rate_change);
							}
							
							if(stock_number_change.equals("新进")){
								stock_rate_change_d=100;
							}else if(stock_number_change.equals("退出")){
								stock_rate_change_d=-100;
							}
							
							String category=elements3.get(5).text();
							
							StockCompanyHolderTop stockCompanyHolderTop=new StockCompanyHolderTop();
							stockCompanyHolderTop.setCode(stock.getCode());
							stockCompanyHolderTop.setType(type);
							stockCompanyHolderTop.setName(name);
							stockCompanyHolderTop.setTime(times.get(i));
							stockCompanyHolderTop.setCategory(category);
							stockCompanyHolderTop.setStock_number(stock_number);
							stockCompanyHolderTop.setStock_number_change(stock_number_change_d);
							stockCompanyHolderTop.setStock_rate(stock_rate);
							stockCompanyHolderTop.setStock_rate_change(stock_rate_change_d);
							stockCompanyHolderTop.setUpdate_time(DateUtil.datetime());
							
							StockCompanyHolderTop existStockCompanyHolderTop=stockCompanyHolderTopMapper.find(stock.getCode(), type, name, times.get(i));
							if(existStockCompanyHolderTop==null){
								insertAll.add(stockCompanyHolderTop);
							}
							if(insertAll.size()==1000){
								stockCompanyHolderTopMapper.insertAll(insertAll);
								logger.info(insertAll.size());
								insertAll.clear();
							}
						}
					}
				}
				
			}
			if(insertAll.size()>0){
				stockCompanyHolderTopMapper.insertAll(insertAll);
				logger.info(insertAll.size());
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]"+cacheParam);
	}
}
