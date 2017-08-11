package venus.task.collect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import venus.dao.StockCompanyJingyingMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyJingying;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyJingyingTask {
	Logger logger=Logger.getLogger(StockCompanyJingyingTask.class);
	@Value("${stock-company-jingying-threadnum}")
	public int threadNum;
	@Autowired
	StockCompanyJingyingMapper stockCompanyJingyingMapper;
	@Autowired
	StockinfoMapper stockinfoMapper;
	
	@Autowired URLUtil URLUtil;
	public void init(String stockCode,int threadId){
		init(false,stockCode,threadId);
	}
	public void initCache(String stockCode,int threadId){
		init(true,stockCode,threadId);
	}
	private void init(boolean cacheParam,String stockCode,int threadId){
		logger.info("[start]"+cacheParam+","+stockCode);
		try{
			List<Stockinfo> stocks = null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				stocks=new ArrayList<Stockinfo>();
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks.add(stock);
			}
			
			List<StockCompanyJingying> insertAll=new ArrayList<StockCompanyJingying>();
			
			for(Stockinfo stock:stocks){
				String code=stock.getCode();
				
				if(stockCode==null){
					if (code.hashCode() % threadNum != threadId) {
						continue;
					}
				}
				
				String str=null;
				try{
					str=URLUtil.url2str("http://stockpage.10jqka.com.cn/"+code+"/operate/", cacheParam);
				}catch(Exception e){
					logger.error("[except]",e);
					continue;
				}
				if(StringUtil.isBlank(str))continue;
				Document document=Jsoup.parse(str);
				
				stockCompanyJingyingMapper.deleteCode(code);
				
				String jsonStr=document.select("#chartsData").text();
				JSONObject object=JSONObject.parseObject(jsonStr);
				
				Iterator<String> it=object.keySet().iterator();
				while(it.hasNext()){
					String date=it.next();
					JSONObject temp=object.getJSONObject(date);
					String[] categorys={"营业收入","营业成本","利润比例"};
					String[] categoryKeys={"YYSR","YYCB","LRBL"};
					
					for(int k=0;k<categoryKeys.length;k++){
						String category=categorys[k];
						
						JSONObject tempObj=temp.getJSONObject(categoryKeys[k]);
						
						String[] types={"行业","产品","市场"};
						String[] typeKeys={"1","2","3"};
						
						for(int i=0;i<typeKeys.length;i++){
							String type=types[i];
							JSONArray data=tempObj.getJSONArray(typeKeys[i]);
							if(data==null)continue;
							
							for(int j=0;j<data.size();j++){
								String menu=data.getJSONArray(j).getString(0);
								Double value= NumUtil.text2num(data.getJSONArray(j).getString(1));
								
								StockCompanyJingying stockCompanyJingying=new StockCompanyJingying();
								stockCompanyJingying.setCode(code);
								stockCompanyJingying.setDate(date);
								stockCompanyJingying.setType(type);
								stockCompanyJingying.setCategory(category);
								stockCompanyJingying.setMenu(menu);
								stockCompanyJingying.setValue(value);
								stockCompanyJingying.setUpdate_time(DateUtil.datetime2());
								
								insertAll.add(stockCompanyJingying);
								if (insertAll.size()==1000) {
									stockCompanyJingyingMapper.insertAll(insertAll);
									insertAll.clear();
								}
								
							}
						}
					}
				}
				
				if (insertAll.size()>0) {
					stockCompanyJingyingMapper.insertAll(insertAll);
					insertAll.clear();
				}
				
				logger.info("[message]"+code);
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
	
		logger.info("[end]");
	}
}
