package venus.helper.middle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockinfoMapper;
import venus.dao.TradeDayMapper;
import venus.model.dao.Stockinfo;
import venus.model.dao.TradeDay;

@Component
public class RandomChooseStock {
	
	@Autowired
	StockinfoMapper stockinfoMapper;
	@Autowired
	TradeDayMapper tradeDayMapper;
	
	public List<Stockinfo> randomChoose(String seed,int num){
		List<Stockinfo> base=stockinfoMapper.findStockinfos();
		return randomChoose(base, seed, num);
	}
	private List<Stockinfo> randomChoose(List<Stockinfo> base,String seed,int num){
		Set<Stockinfo> result=new HashSet<Stockinfo>();
		int i=0;
		while(result.size()<num){
			Stockinfo stockinfo=base.get(Math.abs(seed.hashCode())%base.size());
			if(!result.contains(stockinfo)){
				result.add(stockinfo);
			}
			i++;
			seed+=i;
		}
		return new ArrayList<Stockinfo>(result);
	}
	public List<Stockinfo> randomChooseTime(String seed,int num,String time){
		TradeDay firstTradeDay=tradeDayMapper.findTimeFirst(time);
		List<Stockinfo> base=stockinfoMapper.findFirstTradeDay(firstTradeDay.getDt());
		return randomChoose(base, seed, num);
	}
	
}
