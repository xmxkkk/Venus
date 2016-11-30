package venus.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface RunStatusMapper {
	
	@Insert("insert into run_status (id,status) values (#{id},#{status})")
	int insert(@Param("id")String id,@Param("status")int status);
	
	@Update("update run_status set status=#{status} where id=#{id}")
	int update(@Param("id")String id,@Param("status")int status);
}
