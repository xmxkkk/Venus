package venus.feature.impl;

import org.springframework.stereotype.Component;

import venus.feature.Feature;
import venus.model.dao.Price;

@Component
public class RateFeature4 implements Feature{

	public String feature(Price stockDay){
		if(stockDay==null){
			return "o";
		}
		if(stockDay.getChange_rate()>0){
			return "a";
		}else{
			return "b";
		}
	}
}
