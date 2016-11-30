package venus.helper.middle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.TaskUpdateMapper;
import venus.helper.util.DateUtil;

@Component
public class TaskUpdateMiddle {
	Logger logger=Logger.getLogger(TaskUpdateMiddle.class);
	@Autowired
	TaskUpdateMapper taskUpdateMapper;
	
	public void update(String name,int intervalTime){
		logger.info("[start]"+name+","+intervalTime);
		try{
			taskUpdateMapper.delete(name);
			taskUpdateMapper.insert(name, DateUtil.datetime(),intervalTime);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
}
