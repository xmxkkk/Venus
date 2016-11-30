package venus.task.analyse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
import venus.dao.StockCompanyFinanceRateMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.StockCompanyFinance;
import venus.model.dao.StockCompanyFinanceRate;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyFinanceRateTask {
	Logger logger=Logger.getLogger(StockCompanyFinanceRateTask.class);
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	@Autowired StockCompanyFinanceRateMapper stockCompanyFinanceRateMapper;
	public void init(){
		logger.info("[start]");
		try{
			List<StockCompanyFinanceRate> insertAll=new ArrayList<StockCompanyFinanceRate>();
			
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(Stockinfo stock:stocks){
				String code=stock.getCode();
				
				stockCompanyFinanceRateMapper.deleteCode(code);
				
				String type="simple";
				List<StockCompanyFinance> data=stockCompanyFinanceMapper.findCodeType(code, type);
				Set<String> menusSet=new HashSet<String>();
				for(int i=0;i<data.size();i++){
					menusSet.add(data.get(i).getMenu());
				}
				List<String> menus=new ArrayList<String>(menusSet);
				for(int i=0;i<menus.size();i++){
					String menu=menus.get(i);
					data=stockCompanyFinanceMapper.findCodeTypeMenu(code, type, menu);
					for(int j=5;j<data.size();j++){
						StockCompanyFinance temp=data.get(j);
						
						double start=totalValue(data.subList(j-1-4, j-1));
						double end=totalValue(data.subList(j-4, j));
						
						double rate=0;
						if(start>0.000000001){
							rate=NumUtil.format2(100.0*(end-start)/start);
						}
						
						StockCompanyFinanceRate stockCompanyFinanceRate=new StockCompanyFinanceRate();
						stockCompanyFinanceRate.setCode(code);
						stockCompanyFinanceRate.setMenu(menu);
						stockCompanyFinanceRate.setTime(temp.getTime());
						stockCompanyFinanceRate.setRate(rate);
						stockCompanyFinanceRate.setUpdate_time(DateUtil.datetime());
						
						insertAll.add(stockCompanyFinanceRate);
						
						if(insertAll.size()==1000){
							stockCompanyFinanceRateMapper.insertAll(insertAll);
							insertAll.clear();
						}
					}
				}
				if(insertAll.size()>0){
					stockCompanyFinanceRateMapper.insertAll(insertAll);
					insertAll.clear();
				}
				logger.info("[message]"+code);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
	private double totalValue(List<StockCompanyFinance> data){
		double total=0;
		for(int i=0;i<data.size();i++){
			total+=data.get(i).getValue();
		}
		return total;
	}
}
