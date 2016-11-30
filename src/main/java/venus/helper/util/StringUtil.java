package venus.helper.util;

import java.util.List;
import java.util.Map;

import venus.model.dao.Stockinfo;

public class StringUtil {
	@SuppressWarnings("rawtypes")
	public static boolean isBlank(Object obj){
		if(obj==null){
			return true;
		}
		
		if(obj instanceof List){
			return ((List) obj).size()==0;
		}else if(obj instanceof Map){
			return ((Map) obj).size()==0;
		}else if(obj instanceof String){
			return obj.equals("");
		}
		return false; 
	}
	
	public static String toMarketName(Stockinfo stock){
		if(stock.getMarket().equals(Constant.MARKET$HUSHIAGU)){
			return "sh"+stock.getCode();
		}else{
			return "sz"+stock.getCode();
		}
	}
}
