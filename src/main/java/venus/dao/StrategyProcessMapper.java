package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.strategy.StrategyProcess;

@Component
@Mapper
public interface StrategyProcessMapper {
	
	@Insert("insert into strategy_process (id,type,dt,price)"
			+ " values (#{id},#{type},#{dt},#{price})")
	public int insertStrategyProcess(StrategyProcess strategyProcess);
	
	@Insert("insert into strategy_process (id,type,dt,price)"
			+ " values (#{id},#{type},#{dt},#{price})")
	public int insert(@Param("id")int id,@Param("type")String type,@Param("dt")String dt,@Param("price")double price);
	
	@Select("select * from strategy_process where id=#{id} order by dt asc")
	public List<StrategyProcess> findById(@Param("id")int id);
	
	@Delete("delete from strategy_process where id=#{id}")
	public int delete(@Param("id")int id);
}
