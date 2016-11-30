package venus.strategy.stock.choose.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.helper.middle.RandomChooseStock;
import venus.model.dao.Stockinfo;
import venus.strategy.stock.choose.StockChooseStrategy;

@Component
public class StockChooseStrategy001 implements StockChooseStrategy{
	
	@Autowired
	RandomChooseStock randomChooseStock;
	private String startTime;
	private int num;
	public void initParam(String param) {
		num=Integer.parseInt(param.split("#")[0]);
		startTime=param.split("#")[1];
	}
	public List<Stockinfo> choose() {
		return randomChooseStock.randomChooseTime("123456", num,startTime);
	}
}
