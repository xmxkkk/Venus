package venus.helper.middle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.TaskExecTimeMapper;
import venus.helper.util.CommonUtil;
import venus.helper.util.DateUtil;
import venus.model.dao.TaskExecTime;

@Component
public class TaskExecTimeMiddle {
	@Autowired TaskExecTimeMapper taskExecTimeMapper;
	
	public String start(String name){
		String id=CommonUtil.randMd5();
		TaskExecTime taskExecTime=new TaskExecTime();
		taskExecTime.setId(id);
		taskExecTime.setName(name);
		taskExecTime.setStart_time(DateUtil.datetime2());
		taskExecTimeMapper.insert(taskExecTime);
		return id;
	}
	public void end(String id){
		TaskExecTime taskExecTime=taskExecTimeMapper.find(id);
		taskExecTime.setEnd_time(DateUtil.datetime2());
		int time=DateUtil.datediff3(taskExecTime.getStart_time(), taskExecTime.getEnd_time());
		taskExecTime.setTime(time);
		
		taskExecTimeMapper.update(taskExecTime);
	}
}
