package venus.task.collect;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import venus.dao.StockCompanyHangyeDataMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyHangyeData;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyHangyeDataTask {
	Logger logger=Logger.getLogger(StockCompanyHangyeDataTask.class);
	@Value("${stock-company-hangye-data-threadnum}")
	public int threadNum;
	@Autowired
	StockCompanyHangyeDataMapper stockCompanyHangyeDataMapper;
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
		logger.info("[start]"+cacheParam);
		try{
			List<String> exists=new ArrayList<String>();
			
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks=new ArrayList<Stockinfo>();
				stocks.add(stock);
			}
			
			for(Stockinfo stock:stocks){
				if(stockCode==null){
					if (stock.getCode().hashCode() % threadNum != threadId) {
						continue;
					}
				}
				
				String str=null;
				try{
					str=URLUtil.url2str("http://stockpage.10jqka.com.cn/"+stock.getCode()+"/field/", cacheParam);
				}catch(Exception e){
					logger.error("[except]",e);
					continue;
				}
				if(StringUtil.isBlank(str))continue;
				Document document=Jsoup.parse(str);
				
				Elements elements=document.select("#fieldsChartData");
				if(elements.size()==0){
					continue;
				}
				String value=elements.get(0).attr("value");
				
				JSONObject object=JSONObject.parseObject(value);
				Set<String> timesSet=object.keySet();
				
				List<String> times=new ArrayList<String>(timesSet);
				
				for(int i=0;i<times.size();i++){
					String date=times.get(i);
					JSONArray array=object.getJSONArray(date);
					for(int j=0;j<array.size();j++){
						JSONArray tempArr=array.getJSONArray(j);
						String code=tempArr.getString(0);
						if(exists.contains(code+times.get(i))){
							continue;
						}
						exists.add(code+times.get(i));
						double meigushouyi=(tempArr.getString(2)==null||tempArr.getString(2).equals("false"))?0:tempArr.getDouble(2);
						double meigujingzichan=(tempArr.getString(3)==null||tempArr.getString(3).equals("false"))?0:tempArr.getDouble(3);
						double meiguxianjinliu=(tempArr.getString(4)==null||tempArr.getString(4).equals("false"))?0:tempArr.getDouble(4);
						double jinglirun=(tempArr.getString(5)==null||tempArr.getString(5).equals("false"))?0:tempArr.getDouble(5);
						double yingyeshouru=(tempArr.getString(6)==null||tempArr.getString(6).equals("false"))?0:tempArr.getDouble(6);
						double zongzichan=(tempArr.getString(7)==null||tempArr.getString(7).equals("false"))?0:tempArr.getDouble(7);
						double jingzichanshouyilv=(tempArr.getString(8)==null||tempArr.getString(8).equals("false"))?0:tempArr.getDouble(8);
						
						
						double gudongquanyibilv=(tempArr.getString(9)==null||tempArr.getString(9).equals("false"))?0:tempArr.getDouble(9);
						
						double xiaoshoumaolilv=(tempArr.getString(10)==null||tempArr.getString(10).equals("false"))?0:tempArr.getDouble(10);
						
						double zongguben=(tempArr.getString(11)==null||tempArr.getString(11).equals("false"))?0:tempArr.getDouble(11);
						double zichanfuzhailv=(tempArr.getString(9)==null||tempArr.getString(9).equals("false"))?0:100-tempArr.getDouble(9);
						
						StockCompanyHangyeData stockCompanyHangyeData=new StockCompanyHangyeData();
						stockCompanyHangyeData.setCode(code);
						stockCompanyHangyeData.setDate(date);
						stockCompanyHangyeData.setMeigushouyi(meigushouyi);
						stockCompanyHangyeData.setMeigujingzichan(meigujingzichan);
						stockCompanyHangyeData.setMeiguxianjinliu(meiguxianjinliu);
						stockCompanyHangyeData.setJinglirun(jinglirun);
						stockCompanyHangyeData.setYingyeshouru(yingyeshouru);
						stockCompanyHangyeData.setZongzichan(zongzichan);
						stockCompanyHangyeData.setJingzichanshouyilv(jingzichanshouyilv);
						stockCompanyHangyeData.setGudongquanyibilv(gudongquanyibilv);
						stockCompanyHangyeData.setXiaoshoumaolilv(xiaoshoumaolilv);
						stockCompanyHangyeData.setZongguben(zongguben);
						stockCompanyHangyeData.setZichanfuzhailv(zichanfuzhailv);
						stockCompanyHangyeData.setUpdate_time(DateUtil.datetime());
						
						StockCompanyHangyeData dbStockCompanyHangyeData=stockCompanyHangyeDataMapper.find(code, date);
						if(dbStockCompanyHangyeData!=null){
							stockCompanyHangyeDataMapper.update(stockCompanyHangyeData);
						}else{
							stockCompanyHangyeDataMapper.insert(stockCompanyHangyeData);
						}
								
						
						logger.info(stockCompanyHangyeData);
					}
				}
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]"+cacheParam);
	}
}
