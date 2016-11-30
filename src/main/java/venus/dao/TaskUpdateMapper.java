package venus.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.TaskUpdate;

@Mapper
@Component
public interface TaskUpdateMapper {
	
	@Insert("insert into task_update (name,update_time,interval_time) values (#{name},#{update_time},#{interval_time})")
	public int insert(@Param("name")String name,@Param("update_time")String update_time,@Param("interval_time")int interval_time);

	@Update("update task_update set update_time=#{update_time} where name=#{name}")
	public void update(@Param("name")String name,@Param("update_time")String update_time);
	
	@Select("select * from task_update where name=#{name}")
	public TaskUpdate find(@Param("name")String name);

	@Delete("delete from task_update where name=#{name}")
	public int delete(@Param("name")String name);
	
	@Select("select * from task_update where name=#{name} and DATE_ADD(update_time,INTERVAL interval_time SECOND)>now()")
	public TaskUpdate findExist(@Param("name")String name);
}
