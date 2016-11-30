package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.LuStrategyFilter;

@Component
@Mapper
public interface LuStrategyFilterMapper {
	
	@Insert("insert into lu_strategy_filter (id,filter,condition,update_time) values "
			+ "(#{id},#{filter},#{condition},#{update_time})")
	int insert(LuStrategyFilter luStrategyFilter);
	
	@Select("select * from lu_strategy_filter where id=#{id}")
	List<LuStrategyFilter> findId(int id);
	
	@Delete("delete from lu_strategy_filter where id=#{id}")
	int deleteId(int id);
	
	
}
