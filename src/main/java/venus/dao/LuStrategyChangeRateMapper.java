package venus.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.LuStrategyChangeRate;

@Component
@Mapper
public interface LuStrategyChangeRateMapper {
	@Insert("insert into lu_strategy_change_rate (id,dt,day7_change_rate,day14_change_rate,day21_change_rate,month1_change_rate,month3_change_rate,month6_change_rate,year1_change_rate,year2_change_rate"
			+ ",year3_change_rate,year4_change_rate,year5_change_rate,year6_change_rate,year7_change_rate,year8_change_rate,year9_change_rate,year10_change_rate) values "
			+ "(#{id},#{dt},#{day7_change_rate},#{day14_change_rate},#{day21_change_rate},#{month1_change_rate},#{month3_change_rate},#{month6_change_rate},#{year1_change_rate},#{year2_change_rate}"
			+ ",#{year3_change_rate},#{year4_change_rate},#{year5_change_rate},#{year6_change_rate},#{year7_change_rate},#{year8_change_rate},#{year9_change_rate},#{year10_change_rate})")
	int insert(LuStrategyChangeRate luStrategyChangeRate);
	
	@Select("select * from lu_strategy_change_rate where id=#{id} and dt=#{dt}")
	LuStrategyChangeRate find(@Param("id")int id,@Param("dt")String dt);
	
	@Delete("delete from lu_strategy_change_rate where id=#{id} and dt=#{dt}")
	int delete(@Param("id")int id,@Param("dt")String dt);

	@Delete("delete from lu_strategy_change_rate where id=#{id}")
	int deleteId(int id);
}
