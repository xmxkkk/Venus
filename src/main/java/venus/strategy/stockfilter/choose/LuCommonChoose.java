package venus.strategy.stockfilter.choose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;

import venus.dao.LuStrategyFilterMapper;
import venus.dao.LuStrategyStockfilterMapper;
import venus.dao.StockinfoMapper;
import venus.model.dao.LuStrategyFilter;
import venus.model.dao.LuStrategyStockfilter;
import venus.model.dao.Stockinfo;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class LuCommonChoose extends ApplicationObjectSupport{
	Logger logger=Logger.getLogger(LuCommonChoose.class);
	@Autowired LuStrategyFilterMapper luStrategyFilterMapper;
	@Autowired LuStrategyStockfilterMapper luStrategyStockfilterMapper;
	@Autowired StockinfoMapper stockinfoMapper;
	public List<String> choose(int id){
		logger.info("[start]id="+id);
		
		List<String> result=null;
		try{
			List<LuStrategyFilter> filters=luStrategyFilterMapper.findId(id);
			result=choose(filters);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+result);
		return result;
	}
	public List<String> choose(String strategy_json_str){
		logger.info("[start]");
		
		List<String> result=null;
		try{
			
			JSONArray filtersArray=JSONArray.parseArray(strategy_json_str);
			List<LuStrategyFilter> filters=new ArrayList<LuStrategyFilter>();
			
			Map<String, LuStrategyStockfilter> stockfilterMap=stockfilters();
			
			for(int i=0;i<filtersArray.size();i++){
				String filter=filtersArray.getJSONObject(i).getString("filter");
				String condition=filtersArray.getJSONObject(i).getString("condition");
				
				LuStrategyStockfilter luStrategyStockfilter=stockfilterMap.get(filter);
				
				
				LuStrategyFilter luStrategyFilter=new LuStrategyFilter();
				luStrategyFilter.setId(0);
				luStrategyFilter.setFilter(filter);
				luStrategyFilter.setCondition(condition);
				luStrategyFilter.setOrd(luStrategyStockfilter.getOrd());
					
				filters.add(luStrategyFilter);
			}

			Collections.sort(filters);
			
			result=choose(filters);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+result);
		return result;
	}
	
	private List<String> choose(List<LuStrategyFilter> filters){
		logger.info("[start]");
		
		List<String> result=new ArrayList<String>();
		try{
			if(filters==null||filters.size()==0){
				logger.info("[message]not have filters"+result);
				return result;
			}
			
			Map<String, LuStrategyStockfilter> stockfilterMap=stockfilters();
			
			
			List<Stockinfo> stocks=stockinfoMapper.findStop(0);
			for(Stockinfo stock:stocks){
				String code=stock.getCode();
				
				boolean pass=false;
				for(LuStrategyFilter filter:filters){
					StockFilter stockFilter=(StockFilter)getApplicationContext().getBean(filter.getFilter());

					LuStrategyStockfilter luStrategyStockfilter=stockfilterMap.get(filter.getFilter());
					
					if(luStrategyStockfilter.getUse_type().equals("filter")){
						if(!stockFilter.filter(code, filter.getCondition())){
							pass=true;
							break;
						}
					}else if(luStrategyStockfilter.getUse_type().equals("condition")){
						if(stockFilter.filter(code, filter.getCondition())){
							pass=false;
							break;
						}
					}
				}
				
				if(pass)continue;
				
				result.add(code);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+result);
		return result;
	}
	private Map<String, LuStrategyStockfilter> stockfilters(){
		Map<String, LuStrategyStockfilter> stockfilterMap=new HashMap<String, LuStrategyStockfilter>();
		List<LuStrategyStockfilter> stockfilterList=luStrategyStockfilterMapper.findAll();
		for(int i=0;i<stockfilterList.size();i++){
			stockfilterMap.put(stockfilterList.get(i).getName(), stockfilterList.get(i));
		}
		return stockfilterMap;
	}
}
