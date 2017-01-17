package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyInfoMapper;
import venus.model.dao.StockCompanyInfo;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class GuoqiStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(GuoqiStockFilter.class);
	@Autowired StockCompanyInfoMapper stockCompanyInfoMapper;
	@Override
	public boolean filter(String code, String params) {
		//select * from stock_company_info where konggugudong like '%国有资产%' or shijikongzhiren like '%国有资产%' or zuizhongkongzhiren like '%国有资产%'
		
		
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanyInfo stockCompanyInfo=stockCompanyInfoMapper.findCode(code);
			String kongzhiren=stockCompanyInfo.getKonggugudong()+stockCompanyInfo.getShijikongzhiren()+stockCompanyInfo.getZuizhongkongzhiren();
			
			if(kongzhiren.contains("国有资产")||kongzhiren.contains("国资委")||kongzhiren.contains("人民政府")||kongzhiren.contains("中央汇金")){
				result=true;
			}
		
			if(params.equals("false")){
				result=!result;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		
		return result;
	}

}
