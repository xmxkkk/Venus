package venus.feature.impl;

import org.springframework.stereotype.Component;

import venus.feature.Feature;
import venus.model.dao.Price;

@Component
public class RateFeature implements Feature{

	public String feature(Price stockDay){
		
		//A开盘价减去收盘价特征
		//B最高价减去最低价特征
		//C成交量特征
		StringBuffer sb=new StringBuffer();
		
		double rate=(stockDay.getOpen_price()-stockDay.getClose_price())/stockDay.getClose_price();
		sb.append(rateFeature1("A",rate));
		
//		rate=(stockDay.getHigh_price()-stockDay.getLow_price())/stockDay.getClose_price();
//		sb.append(rateFeature1("B",rate));
//		
//		rate=(stockDay.getHigh_price()-stockDay.getClose_price())/stockDay.getClose_price();
//		sb.append(rateFeature1("C",rate));
//		
//		rate=(stockDay.getLow_price()-stockDay.getClose_price())/stockDay.getClose_price();
//		sb.append(rateFeature1("D",rate));
		
		
		return sb.toString();
	}
	public String rateFeature1(String prefix,double rate){
		String feature=null;
		if(rate>.105){
			feature="a";
		}else if(rate>.095&&rate<=.105){
			feature="b";
		}else if(rate>.055 && rate<=.095){
			feature="c";
		}else if(rate>.045 && rate<=.055){
			feature="d";
		}else if(rate>.03 && rate<=.045){
			feature="e";
		}else if(rate>.01 && rate<=.03){
			feature="f";
		}else if(rate>0 && rate<=.01){
			feature="g";
		}else if(rate==0){
			feature="h";
		}else if(rate>=-.01 && rate<0){
			feature="i";
		}else if(rate>=-.03 && rate<-.01){
			feature="j";
		}else if(rate>=-.045 && rate<-.03){
			feature="k";
		}else if(rate>=-.055 && rate<-.045){
			feature="l";
		}else if(rate>=-.095 && rate<-.055){
			feature="m";
		}else if(rate>=-.105 && rate<-.095){
			feature="n";
		}else if(rate<.105){
			feature="o";
		}
		return prefix+feature;
	}
}
