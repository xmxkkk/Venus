package venus.feature.impl;

import org.springframework.stereotype.Component;

import venus.feature.Feature;
import venus.model.dao.Price;

@Component
public class RateFeature3 implements Feature{

	public String feature(Price stockDay){
		if(stockDay==null){
			return "o";
		}
		if(stockDay.getChange_rate()>-0.005 && stockDay.getChange_rate()<0.005){
			return "a";
		}else if(stockDay.getChange_rate()<=-0.005){
			return "b";
		}else if(stockDay.getChange_rate()>=0.005){
			return "c";
		}
		return "0";
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
