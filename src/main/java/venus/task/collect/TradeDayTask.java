package venus.task.collect;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.TradeDayMapper;

@Component
public class TradeDayTask {
	Logger logger=Logger.getLogger(TradeDayTask.class);
	@Autowired
	TradeDayMapper tradeDayMapper;
	public void init(){
		logger.info("[start]");
		try{
			tradeDayMapper.delete();
			tradeDayMapper.insert();
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]");
	}
}
