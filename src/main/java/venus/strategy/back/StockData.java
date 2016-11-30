package venus.strategy.back;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyEventMapper;
import venus.dao.StockCompanyFinanceMapper;
import venus.dao.StockCompanyHangyeDataMapper;
import venus.dao.StockCompanyHolderCansellMapper;
import venus.dao.StockCompanyHolderNumberMapper;
import venus.dao.StockCompanyHolderOrgMapper;
import venus.dao.StockCompanyHolderTopMapper;
import venus.dao.StockDayFuMapper;
import venus.dao.StockDayMapper;
import venus.helper.util.DateUtil;
import venus.model.dao.StockCompanyEvent;
import venus.model.dao.StockCompanyFinance;
import venus.model.dao.StockCompanyHangyeData;
import venus.model.dao.StockCompanyHolderCansell;
import venus.model.dao.StockCompanyHolderNumber;
import venus.model.dao.StockCompanyHolderOrg;
import venus.model.dao.StockCompanyHolderTop;
import venus.model.dao.StockDay;
import venus.model.dao.StockDayFu;

@Component
public class StockData {
	public void collect(String code,String dt){
		String dt2=DateUtil.convert(dt);
//		stockCompanyEvents=stockCompanyEventMapper.findDtLt(code,dt2);
		stockCompanyFinances=stockCompanyFinanceMapper.findDtLt(code,dt2);
//		stockCompanyHangyeDatas=stockCompanyHangyeDataMapper.findDtLt(code,dt2);
//		stockCompanyHolderCansells=stockCompanyHolderCansellMapper.findDtLt(code,dt2);
//		stockCompanyHolderNumbers=stockCompanyHolderNumberMapper.findDtLt(code,dt2);
//		stockCompanyHolderOrgs=stockCompanyHolderOrgMapper.findDtLt(code,dt2);
//		stockCompanyHolderTops=stockCompanyHolderTopMapper.findDtLt(code,dt2);
//		stockDayFus=stockDayFuMapper.findStockDayFuLastN(code, dt, 180);
//		stockDays=stockDayMapper.findStockDayLastN(code, dt, 180);
	}
	@Autowired StockCompanyEventMapper stockCompanyEventMapper;
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	@Autowired StockCompanyHangyeDataMapper stockCompanyHangyeDataMapper;
	@Autowired StockCompanyHolderCansellMapper stockCompanyHolderCansellMapper;
	@Autowired StockCompanyHolderNumberMapper stockCompanyHolderNumberMapper;
	@Autowired StockCompanyHolderOrgMapper stockCompanyHolderOrgMapper;
	@Autowired StockCompanyHolderTopMapper stockCompanyHolderTopMapper;
	@Autowired StockDayMapper stockDayMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	
	List<StockCompanyEvent> stockCompanyEvents;
	List<StockCompanyFinance> stockCompanyFinances;
	List<StockCompanyHangyeData> stockCompanyHangyeDatas;
	List<StockCompanyHolderCansell> stockCompanyHolderCansells;
	List<StockCompanyHolderNumber> stockCompanyHolderNumbers;
	List<StockCompanyHolderOrg> stockCompanyHolderOrgs;
	List<StockCompanyHolderTop> stockCompanyHolderTops;
	List<StockDay> stockDays;
	List<StockDayFu> stockDayFus;
	public List<StockCompanyEvent> getStockCompanyEvents() {
		return stockCompanyEvents;
	}
	public void setStockCompanyEvents(List<StockCompanyEvent> stockCompanyEvents) {
		this.stockCompanyEvents = stockCompanyEvents;
	}
	public List<StockCompanyFinance> getStockCompanyFinances() {
		return stockCompanyFinances;
	}
	public void setStockCompanyFinances(List<StockCompanyFinance> stockCompanyFinances) {
		this.stockCompanyFinances = stockCompanyFinances;
	}
	public List<StockCompanyHangyeData> getStockCompanyHangyeDatas() {
		return stockCompanyHangyeDatas;
	}
	public void setStockCompanyHangyeDatas(List<StockCompanyHangyeData> stockCompanyHangyeDatas) {
		this.stockCompanyHangyeDatas = stockCompanyHangyeDatas;
	}
	public List<StockCompanyHolderCansell> getStockCompanyHolderCansells() {
		return stockCompanyHolderCansells;
	}
	public void setStockCompanyHolderCansells(List<StockCompanyHolderCansell> stockCompanyHolderCansells) {
		this.stockCompanyHolderCansells = stockCompanyHolderCansells;
	}
	public List<StockCompanyHolderNumber> getStockCompanyHolderNumbers() {
		return stockCompanyHolderNumbers;
	}
	public void setStockCompanyHolderNumbers(List<StockCompanyHolderNumber> stockCompanyHolderNumbers) {
		this.stockCompanyHolderNumbers = stockCompanyHolderNumbers;
	}
	public List<StockCompanyHolderOrg> getStockCompanyHolderOrgs() {
		return stockCompanyHolderOrgs;
	}
	public void setStockCompanyHolderOrgs(List<StockCompanyHolderOrg> stockCompanyHolderOrgs) {
		this.stockCompanyHolderOrgs = stockCompanyHolderOrgs;
	}
	public List<StockCompanyHolderTop> getStockCompanyHolderTops() {
		return stockCompanyHolderTops;
	}
	public void setStockCompanyHolderTops(List<StockCompanyHolderTop> stockCompanyHolderTops) {
		this.stockCompanyHolderTops = stockCompanyHolderTops;
	}
	public List<StockDay> getStockDays() {
		return stockDays;
	}
	public void setStockDays(List<StockDay> stockDays) {
		this.stockDays = stockDays;
	}
	public List<StockDayFu> getStockDayFus() {
		return stockDayFus;
	}
	public void setStockDayFus(List<StockDayFu> stockDayFus) {
		this.stockDayFus = stockDayFus;
	}
	
}
