package venus.task.collect;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import venus.dao.StockCompanyFinanceMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyFinance;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyFinanceTask {
	Logger logger = Logger.getLogger(StockCompanyFinanceTask.class);

	@Autowired
	private StockCompanyFinanceMapper stockCompanyFinanceMapper;
	@Autowired
	private StockinfoMapper stockinfoMapper;
	@Autowired URLUtil URLUtil;
	public void init(String stockCode) {
		init(false,stockCode);
	}

	public void initCache(String stockCode) {
		init(true,stockCode);
	}

	private void init(boolean cacheParam,String stockCode) {

		// http://stockpage.10jqka.com.cn/basic/000001/main.txt
		// http://stockpage.10jqka.com.cn/basic/000001/debt.txt
		// http://stockpage.10jqka.com.cn/basic/000001/benefit.txt
		// http://stockpage.10jqka.com.cn/basic/000001/cash.txt
		logger.info("[start]" + cacheParam);
		try {
			
			/* "main", */// 002815:cash:1000
			List<Stockinfo> stocks = null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				stocks=new ArrayList<Stockinfo>();
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks.add(stock);
			}
			
			//"report", "simple", "year"

			String currentYear=String.valueOf(Integer.parseInt(DateUtil.currentYear())-1);
			int currentSeason=DateUtil.currentSeason();
			
			String yearStr=DateUtil.currentYear();
			String newOne=null;
			if(currentSeason==1){
				newOne=String.valueOf(Integer.parseInt(yearStr)-1)+"-12-31";
			}else if(currentSeason==2){
				newOne=yearStr+"-03-31";
			}else if(currentSeason==3){
				newOne=yearStr+"-06-30";
			}else if(currentSeason==4){
				newOne=yearStr+"-09-30";
			}
			
			for (int i = 0; i < stocks.size(); i++) {
				
				List<StockCompanyFinance> insertAll = new ArrayList<StockCompanyFinance>();
				Stockinfo stock = stocks.get(i);
				
//				stockCompanyFinanceMapper.deleteCode(stock.getCode());
				
				String[] dataTypes = { "debt", "benefit", "cash" };
				
				for (String dataType : dataTypes) {
					String code=stock.getCode();
					
					List<String> typeList=new ArrayList<String>();
					typeList.add("report");
					typeList.add("simple");
					typeList.add("year");
					
					StockCompanyFinance existStockCompanyFinance=stockCompanyFinanceMapper.findCodeTypeLastTime(code, "year");
					if(existStockCompanyFinance!=null&&existStockCompanyFinance.getTime().equals(currentYear)){
						typeList.remove("year");
					}
					
					existStockCompanyFinance=stockCompanyFinanceMapper.findCodeTypeLastTime(code, "report");
					if(existStockCompanyFinance!=null&&existStockCompanyFinance.getTime().equals(newOne)){
						typeList.remove("report");
					}
					
					existStockCompanyFinance=stockCompanyFinanceMapper.findCodeTypeLastTime(code, "simple");
					if(existStockCompanyFinance!=null&&existStockCompanyFinance.getTime().equals(newOne)){
						typeList.remove("simple");
					}
					
					if(typeList.size()==0){
						logger.info("[message]pass:"+code);
						continue;
					}
					
					String[] types=new String[typeList.size()];
					for(int m=0;m<typeList.size();m++){
						types[m]=typeList.get(m);
					}
					
					String url="http://stockpage.10jqka.com.cn/basic/" + code + "/" + dataType + ".txt";
					String str=null;
					try{
						str = URLUtil.url2str(url,cacheParam);
					}catch(Exception e){
						e.printStackTrace();
						logger.error("[except]"+e.getMessage());
						continue;
					}
					
					if (StringUtil.isBlank(str)){
						logger.error("[message]"+url);
						continue;
					}
					JSONObject object = JSONObject.parseObject(str);

					JSONArray title = object.getJSONArray("title");

					
					for (String type : types) {
						JSONArray report = object.getJSONArray(type);
						for (int k = 1; k < report.size(); k++) {

							String menu = "", unit = "";
							if (title.get(k).toString().startsWith("[")) {
								menu = title.getJSONArray(k).getString(0);
								unit = title.getJSONArray(k).getString(1);
							} else {
								menu = title.getString(k);
							}

							for (int x = 0; x < report.getJSONArray(k).size(); x++) {
								String time = report.getJSONArray(0).getString(x);
								double value = NumUtil.text2num(report.getJSONArray(k).getString(x) + unit);

								StockCompanyFinance stockCompanyFinance = new StockCompanyFinance();
								stockCompanyFinance.setCode(stock.getCode());
								stockCompanyFinance.setTime(time);
								stockCompanyFinance.setMenu(menu);
								stockCompanyFinance.setType(type);
								stockCompanyFinance.setValue(value);
								stockCompanyFinance.setUpdate_time(DateUtil.datetime());
								
								StockCompanyFinance stockCompanyFinanceDB=stockCompanyFinanceMapper.find(code, time, menu, type);
								
								if(stockCompanyFinanceDB!=null){
									stockCompanyFinanceMapper.delete(code, time, menu, type);
								}
								
								insertAll.add(stockCompanyFinance);

								if (insertAll.size() == 1000) {
									stockCompanyFinanceMapper.insertAll(insertAll);
									logger.info(stock.getCode() + ":" + dataType + ":" + insertAll.size());
									insertAll.clear();
								}
							}
						}
					}
				}
				if (insertAll.size() > 0) {
					stockCompanyFinanceMapper.insertAll(insertAll);
					logger.info(stock.getCode() + ":" + insertAll.size());
					insertAll.clear();
				}
				stockinfoMapper.updateFlag(stock.getCode(), 1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[except]" + e.getMessage());
		}
		logger.info("[end]" + cacheParam);
	}

}
