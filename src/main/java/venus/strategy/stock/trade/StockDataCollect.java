package venus.strategy.stock.trade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockDayFuMapper;
import venus.dao.StockinfoMapper;
import venus.model.dao.StockDayFu;
import venus.model.strategy.StockData;

@Component
public class StockDataCollect {
	
	@Autowired
	StockDayFuMapper stockDayFuMapper;
	@Autowired
	StockinfoMapper stockinfoMapper;
	
	private int stockDayNum=120;
	public void initParam(String param){
		String[] params=param.split("#");
		stockDayNum=Integer.parseInt(params[0]);
	}
	
	public StockData collect(String code,String dt){
		List<StockDayFu> stockDays=stockDayFuMapper.findStockDayFuLastN(code, dt, stockDayNum);
		
//		Stockinfo stockinfo=stockinfoMapper.findStockinfo(code);
		
		//销毁过去不会知道的事情
//		stockinfo.setWeight(0);
		
		StockData stockData=new StockData();
		stockData.setStockDays(stockDays);
//		stockData.setStockinfo(stockinfo);
	
		return stockData;
	}
}
