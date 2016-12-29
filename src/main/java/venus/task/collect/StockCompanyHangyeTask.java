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

import venus.dao.StockCompanyHangyeMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyHangye;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyHangyeTask {
	Logger logger=Logger.getLogger(StockCompanyHangyeTask.class);
	@Value("${stock-company-hangye-threadnum}")
	public int threadNum;
	@Autowired
	StockCompanyHangyeMapper stockCompanyHangyeMapper;
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
			for(Stockinfo stock:stocks){
				//stockpage.10jqka.com.cn/000001/field/
				
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
				//<p class="threecate fl">三级行业分类：<span class="tip f14">金融服务 -- 银行 -- 银行Ⅲ （共<strong>23</strong>家）</span></p>
				
				Document document=Jsoup.parse(str);
				Elements elements=document.select(".threecate span");
				String text=elements.text();
				if(text.equals("")){
					continue;
				}
				String[] hangyes=text.substring(0, text.indexOf("（")).split(" -- ");
				
				int level_num=Integer.parseInt(elements.get(0).child(0).text());
	
				StockCompanyHangye stockCompanyHangye = new StockCompanyHangye();
				stockCompanyHangye.setCode(stock.getCode());
				stockCompanyHangye.setLevel1(hangyes[0].trim());
				stockCompanyHangye.setLevel2(hangyes[1].trim());
				stockCompanyHangye.setLevel3(hangyes[2].trim());
				stockCompanyHangye.setLevel_num(level_num);
				stockCompanyHangye.setUpdate_time(DateUtil.datetime());
				
				StockCompanyHangye existStockCompanyHangye=stockCompanyHangyeMapper.findCode(stock.getCode());
				if(existStockCompanyHangye!=null){
					stockCompanyHangyeMapper.delete(stock.getCode());
				}
				
				stockCompanyHangyeMapper.insert(stockCompanyHangye);
				logger.info(stockCompanyHangye);
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]"+cacheParam);
	}
}
