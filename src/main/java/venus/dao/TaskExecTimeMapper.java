package venus.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.TaskExecTime;

@Component
@Mapper
public interface TaskExecTimeMapper {

	@Insert("insert into task_exec_time (id,name,start_time,end_time,time) values "
			+ "(#{id},#{name},#{start_time},#{end_time},#{time})")
	int insert(TaskExecTime taskExecTime);
	
	@Update("update task_exec_time set name=#{name},start_time=#{start_time},end_time=#{end_time},time=#{time} where id=#{id}")
	int update(TaskExecTime taskExecTime);
	
	@Select("select * from task_exec_time where id=#{id}")
	TaskExecTime find(@Param("id")String id);
	
}
