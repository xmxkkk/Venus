package venus.helper.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NumUtil {
	public static double format4(double d){
		Double double1=new Double(d);
		if(double1.isInfinite()||double1.isNaN()){
			d=0;
		}
		return new BigDecimal(d).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static double format2(double d){
		Double double1=new Double(d);
		if(double1.isInfinite()||double1.isNaN()){
			d=0;
		}
		return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static double format1(double d){
		Double double1=new Double(d);
		if(double1.isInfinite()||double1.isNaN()){
			d=0;
		}
		return new BigDecimal(d).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static String format(double d,int wei){
		Double double1=new Double(d);
		if(double1.isInfinite()||double1.isNaN()){
			d=0;
		}
		return new BigDecimal(d).setScale(wei, BigDecimal.ROUND_HALF_UP).toString();
	}
	public static double text2num(String text){
		return text2num(text,2);
	}
	public static double text2num(String text,int dic){
		text=text.replaceAll("元", "").replaceAll("手", "").replaceAll("股", "").replaceAll(" ", "").trim();
		if(text.equals("")||text.equals("&nbsp;")||text.equals(" ")){
			return 0;
		}
		double unit=1;
		
		if(text.contains("万")){
			text=text.replaceAll("万", "");
			unit=10000;
		}
		
		if(text.contains("亿")){
			text=text.replaceAll("亿", "");
			unit=100000000;
		}
		
		if(text.contains("%")){
			text=text.replace("%", "");
		}
		try{
			double result=new BigDecimal(text).multiply(new BigDecimal(unit)).setScale(dic, BigDecimal.ROUND_HALF_UP).doubleValue();
			return result;
		}catch(Exception e){
			return 0;
		}
	}
	public static boolean compares(Double d,Map<String, Double> params){
		if(d==null)return false;
		List<Boolean> results=new ArrayList<Boolean>();
		
		Iterator<String> it=params.keySet().iterator();
		while(it.hasNext()){
			String key=it.next();
			Double val=params.get(key);
			
			if(key.equals("=")){
				if(val==d){
					results.add(true);
				}else{
					return false;
				}
			}else if(key.equals("<")){
				if(d<val){
					results.add(true);
				}else{
					return false;
				}
			}else if(key.equals(">")){
				if(d>val){
					results.add(true);
				}else{
					return false;
				}
			}else if(key.equals("<=")){
				if(d<=val){
					results.add(true);
				}else{
					return false;
				}
			}else if(key.equals(">=")){
				if(d>=val){
					results.add(true);
				}else{
					return false;
				}
			}
		}
		if(results.size()==params.size())return true;
		
		return false;
		
	}
	public static double calcRate(double one,double two){
		double result=0;
		if(one==.0){
			result= 0;
		}else if(one>0){
			result=NumUtil.format4(100.0*(two/one-1));
		}else if(one<0){
			result=NumUtil.format4((1-(two/one))*100.0);
		}
		return result;
	}
}
