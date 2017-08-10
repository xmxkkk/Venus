package venus.task.analyse;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.LuStrategyMapper;
import venus.dao.LuStrategyStockMapper;
import venus.dao.LuStrategyStockQuitMapper;
import venus.dao.StockDayFuMapper;
import venus.dao.StockDayMapper;
import venus.helper.util.NumUtil;
import venus.model.dao.LuStrategy;
import venus.model.dao.LuStrategyStockQuit;
import venus.model.dao.StockDay;
import venus.model.dao.StockDayFu;

@Component
public class TotalRateChangeFixTask {
	Logger logger=Logger.getLogger(TotalRateChangeFixTask.class);
	@Autowired StockDayMapper stockDayMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired LuStrategyStockQuitMapper luStrategyStockQuitMapper;
	@Autowired LuStrategyStockMapper luStrategyStockMapper;
	@Autowired LuStrategyMapper luStrategyMapper;
	public void init(){
		logger.info("[start]");
		try{
			List<LuStrategyStockQuit> data=luStrategyStockQuitMapper.findNull();
			for(int i=0;i<data.size();i++){
				LuStrategyStockQuit stock=data.get(i);
				String code=stock.getCode();
				String dt=stock.getQuit_date();
				dt=dt.replaceAll("-", "");
				
				StockDayFu lastStockDayFu=stockDayFuMapper.findDtNear(code, dt);
				
				StockDay lastStockDay=stockDayMapper.findDtNear(code,dt);
				
				if(lastStockDayFu==null||lastStockDay==null){
					logger.error("[error]strategy_id="+stock.getId()+",code="+code+",calc_date="+dt);
					logger.error("[error]lastStockDay="+lastStockDay);
					logger.error("[error]lastStockDayFu="+lastStockDayFu);
					continue;
				}
				
				double quit_price=0;
				if(lastStockDay!=null){
					quit_price=lastStockDay.getClose_price();
				}
				
				double quit_price_fu=0;
				if(lastStockDayFu!=null){
					quit_price_fu=lastStockDayFu.getClose_price();
				}
				
				stock.setQuit_price(quit_price);
				stock.setQuit_price_fu(quit_price_fu);
				
				Double change_rate=null;
				if(stock.getJoin_price_fu()!=null&&stock.getJoin_price_fu()!=0){
					change_rate=NumUtil.format2((stock.getQuit_price_fu()-stock.getJoin_price_fu())*100.0/stock.getJoin_price_fu());
				}
				
				stock.setChange_rate(change_rate);
				
				luStrategyStockQuitMapper.update(stock);
		
			}
			
			List<LuStrategy> luStrategys=luStrategyMapper.findStatus(1);
			for(int i=0;i<luStrategys.size();i++){
				LuStrategy luStrategy=luStrategys.get(i);
				
				Double changeRateQuitDouble=luStrategyStockQuitMapper.findTotalChangeRate(luStrategy.getId());
				Double changeRateDouble=luStrategyStockMapper.findTotalChangeRate(luStrategy.getId());
				double changeRateQuit=changeRateQuitDouble==null?0:changeRateQuitDouble;
				double changeRate=changeRateDouble==null?0:changeRateDouble;
				changeRate=NumUtil.format4(changeRate+changeRateQuit);
				luStrategy.setTotal_change_rate(changeRate);
				
				luStrategyMapper.updateLuStrategy(luStrategy);
			}
		
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]");
		
	}
}
