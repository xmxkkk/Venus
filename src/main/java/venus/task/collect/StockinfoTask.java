package venus.task.collect;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;

import venus.dao.StockDayFuMapper;
import venus.dao.StockDayMapper;
import venus.dao.StockinfoMapper;
import venus.dao.TradeDayMapper;
import venus.helper.util.Constant;
import venus.helper.util.NumUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockDay;
import venus.model.dao.StockDayFu;
import venus.model.dao.Stockinfo;

@Component
public class StockinfoTask {
	Logger logger=Logger.getLogger(StockinfoTask.class);
	
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockDayMapper stockDayMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired TradeDayMapper tradeDayMapper;
	@Autowired URLUtil URLUtil;
	public void init() {
		logger.info("[start]");
		try{
			int page = 1;
			while (true) {
				String jsonStr = URLUtil.url2str(
						"http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=[[%22hq%22,%22hs_a%22,%22%22,0,"
								+ page + ",80]]",false);
	
				JSONArray json = JSONArray.parseArray(jsonStr);
	
				json = json.getJSONObject(0).getJSONArray("items");
	
				for (int i = 0; i < json.size(); i++) {
					JSONArray tempArr = json.getJSONArray(i);
	//				String codeStr = tempArr.getString(0);
					String market = null;
					String code = tempArr.getString(1);
					//000是深交所开头数字，002是中小板，300创业板块，600、601都是上交所上市的股票开头数字.
					if (code.startsWith("000") || code.startsWith("001")) {
						market = Constant.MARKET$SHENSHIAGU;
					} else if (code.startsWith("600")||code.startsWith("601")
							||code.startsWith("603")) {
						market = Constant.MARKET$HUSHIAGU;
					} else if (code.startsWith("002")){
						market = Constant.MARKET$ZHONGXIAOBAN;
					} else if (code.startsWith("300")){
						market = Constant.MARKET$CHUANGYEBAN;
					}else {
						throw new RuntimeException();
					}
	
					String name = tempArr.getString(2);
	
					int stop=0;
					if(0.0==tempArr.getDouble(4)){
						stop=1;
					}
					
					Stockinfo stockinfo = new Stockinfo();
					stockinfo.setCode(code);
					stockinfo.setMarket(market);
					stockinfo.setName(name);
					stockinfo.setStop(stop);
	
					StockDayFu stockDay=stockDayFuMapper.findStockDayFuLast(code);
					if(stockDay!=null){
						stockinfo.setWeight(stockDay.getWeight());
					}
					
					Stockinfo existStockinfo = stockinfoMapper.findStockinfo(code);
					if (existStockinfo == null) {
						stockinfoMapper.insertStockinfo(stockinfo);
					} else {
						stockinfoMapper.updateStockinfo(stockinfo);
					}
				}
	
				if (json.size() < 80) {
					break;
				}
				logger.info("stockinfo=["+page+"]");
				page++;
			}
//			updateWeight();
//			updateTradeDays();
//			updateFirstTradeDay();
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
	public void updateBeta(String stockCode){
		logger.info("[start]"+stockCode);
		try{
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				stocks=new ArrayList<Stockinfo>();
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks.add(stock);
			}
			for(int i=0;i<stocks.size();i++){
				Stockinfo stock=stocks.get(i);
				Double beta=stockDayMapper.findBetaAvg(stock.getCode(), stock.getTrade_days()/10);
				if(beta==null){
					beta=new Double(0);
				}
				beta=NumUtil.format4(beta*100);
				stock.setBeta(beta);
				stockinfoMapper.updateStockinfo(stock);
				logger.info(stock);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+stockCode);
	}
	
	public void updateWeight(String stockCode){
		logger.info("[start]"+stockCode);
		try{
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks=new ArrayList<Stockinfo>();
				stocks.add(stock);
			}
			for (Stockinfo stock : stocks) {
				StockDayFu lastStockDay=stockDayFuMapper.findStockDayFuLast(stock.getCode());
				if(lastStockDay==null){
					stock.setWeight(1);
				}else{
					stock.setWeight(lastStockDay.getWeight());
				}
				
				stockinfoMapper.updateStockinfo(stock);
				logger.info(stock);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+stockCode);
	}
	public void updateTradeDays(String stockCode){
		logger.info("[start]"+stockCode);
		try{
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks=new ArrayList<Stockinfo>();
				stocks.add(stock);
			}
			for (Stockinfo stock : stocks) {
				List<StockDay> stockDays=stockDayMapper.findCode(stock.getCode());
				if(stockDays==null){
					stock.setTrade_days(0);
				}else{
					stock.setTrade_days(stockDays.size());
				}
				
				stockinfoMapper.updateStockinfo(stock);
				logger.info(stock);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+stockCode);
	}
	public void updateFirstTradeDay(String stockCode){
		logger.info("[start]"+stockCode);
		try{
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks=new ArrayList<Stockinfo>();
				stocks.add(stock);
			}
			for (Stockinfo stock : stocks) {
				StockDay stockDay=stockDayMapper.findFirst(stock.getCode());
				if(stockDay==null){
					stock.setFirst_trade_day(null);
				}else{
					stock.setFirst_trade_day(stockDay.getDt());
					stockinfoMapper.updateStockinfo(stock);
				}
				logger.info(stock);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+stockCode);
	}
	
}
