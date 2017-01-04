package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderOrgMapper;
import venus.dao.StockCompanyHolderStructMapper;
import venus.dao.StockCompanyHolderTopMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyHolderStruct;
import venus.model.dao.StockCompanyHolderTop;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class HolderStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(HolderStockFilter.class);
	@Autowired StockCompanyHolderTopMapper stockCompanyHolderTopMapper;
	@Autowired StockCompanyHolderStructMapper stockCompanyHolderStructMapper;
	@Autowired StockCompanyHolderOrgMapper stockCompanyHolderOrgMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		Double val=new Double(0);
		try{
			
			String[] paramss=params.split(",");
			
			if(paramss[0].equals("前十股东占比")){
				List<StockCompanyHolderTop> stockCompanyHolderTops=stockCompanyHolderTopMapper.findTop(code, "top10_gudong_in",10);
				if(stockCompanyHolderTops==null||stockCompanyHolderTops.size()==0)return false;
				for (StockCompanyHolderTop stockCompanyHolderTop2 : stockCompanyHolderTops) {
					val+=stockCompanyHolderTop2.getStock_rate();
				}
			}else if(paramss[0].equals("第一持股人占比")){
				List<StockCompanyHolderTop> stockCompanyHolderTops=stockCompanyHolderTopMapper.findTop(code, "top10_gudong_in",1);
				if(stockCompanyHolderTops==null||stockCompanyHolderTops.size()==0)return false;
				for (StockCompanyHolderTop stockCompanyHolderTop2 : stockCompanyHolderTops) {
					val+=stockCompanyHolderTop2.getStock_rate();
				}
			}else if(paramss[0].equals("限售股票")){
				StockCompanyHolderStruct stockCompanyHolderStruct=stockCompanyHolderStructMapper.findCodeLast(code);
				if(stockCompanyHolderStruct==null||stockCompanyHolderStruct.getZongguben()==null)return false;
				
				double xianshouagu=0;
				if(null!=stockCompanyHolderStruct.getXianshouagu()){
					xianshouagu=stockCompanyHolderStruct.getXianshouagu();
				}
				
				double xianshoubgu=0;
				if(null!=stockCompanyHolderStruct.getXianshoubgu()){
					xianshoubgu=stockCompanyHolderStruct.getXianshoubgu();
				}
				
				double xianshouhgu=0;
				if(null!=stockCompanyHolderStruct.getXianshouhgu()){
					xianshouhgu=stockCompanyHolderStruct.getXianshouhgu();
				}
				
				val=xianshouagu+xianshoubgu+xianshouhgu;
//				xianshou=xianshou*100/stockCompanyHolderStruct.getZongguben();
			}else if(paramss[0].equals("限售股票比率")){
				StockCompanyHolderStruct stockCompanyHolderStruct=stockCompanyHolderStructMapper.findCodeLast(code);
				if(stockCompanyHolderStruct==null||stockCompanyHolderStruct.getZongguben()==null)return false;
				
				double xianshouagu=0;
				if(null!=stockCompanyHolderStruct.getXianshouagu()){
					xianshouagu=stockCompanyHolderStruct.getXianshouagu();
				}
				
				double xianshoubgu=0;
				if(null!=stockCompanyHolderStruct.getXianshoubgu()){
					xianshoubgu=stockCompanyHolderStruct.getXianshoubgu();
				}
				
				double xianshouhgu=0;
				if(null!=stockCompanyHolderStruct.getXianshouhgu()){
					xianshouhgu=stockCompanyHolderStruct.getXianshouhgu();
				}
				
				val=xianshouagu+xianshoubgu+xianshouhgu;
				val=val*100/stockCompanyHolderStruct.getZongguben();
			}else if(paramss[0].equals("流通股票")){
				StockCompanyHolderStruct stockCompanyHolderStruct=stockCompanyHolderStructMapper.findCodeLast(code);
				if(stockCompanyHolderStruct==null||stockCompanyHolderStruct.getZongguben()==null)return false;
				
				double liutongagu=0;
				if(null!=stockCompanyHolderStruct.getLiutongagu()){
					liutongagu=stockCompanyHolderStruct.getLiutongagu();
				}
				
				double liutongbgu=0;
				if(null!=stockCompanyHolderStruct.getLiutongbgu()){
					liutongbgu=stockCompanyHolderStruct.getLiutongbgu();
				}
				
				double liutonghgu=0;
				if(null!=stockCompanyHolderStruct.getLiutonghgu()){
					liutonghgu=stockCompanyHolderStruct.getLiutonghgu();
				}
				
				val=liutongagu+liutongbgu+liutonghgu;
//				xianshou=xianshou*100/stockCompanyHolderStruct.getZongguben();
			}else if(paramss[0].equals("流通股票比率")){
				StockCompanyHolderStruct stockCompanyHolderStruct=stockCompanyHolderStructMapper.findCodeLast(code);
				if(stockCompanyHolderStruct==null||stockCompanyHolderStruct.getZongguben()==null)return false;
				
				double liutongagu=0;
				if(null!=stockCompanyHolderStruct.getLiutongagu()){
					liutongagu=stockCompanyHolderStruct.getLiutongagu();
				}
				
				double liutongbgu=0;
				if(null!=stockCompanyHolderStruct.getLiutongbgu()){
					liutongbgu=stockCompanyHolderStruct.getLiutongbgu();
				}
				
				double liutonghgu=0;
				if(null!=stockCompanyHolderStruct.getLiutonghgu()){
					liutonghgu=stockCompanyHolderStruct.getLiutonghgu();
				}
				
				val=liutongagu+liutongbgu+liutonghgu;
				val=val*100/stockCompanyHolderStruct.getZongguben();
			}else if(paramss[0].equals("机构持股")){
				String date=stockCompanyHolderOrgMapper.findLastTime(code);
				val=stockCompanyHolderOrgMapper.findCodeTotal(code,date);
				if(val==null)return false;
				
			}else if(paramss[0].equals("机构持股比率")){
				String date=stockCompanyHolderOrgMapper.findLastTime(code);
				val=stockCompanyHolderOrgMapper.findCodeTotalRate(code,date);
				if(val==null)return false;
			}else if(paramss[0].equals("总股本")){
				StockCompanyHolderStruct stockCompanyHolderStruct=stockCompanyHolderStructMapper.findCodeLast(code);
				if(stockCompanyHolderStruct==null||stockCompanyHolderStruct.getZongguben()==null)return false;
				
				val=stockCompanyHolderStruct.getZongguben();
			}

			result= CommonUtil.compareExpressionDouble(val, paramss[1]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+val+","+params+","+result);
		return result;
	}
}
