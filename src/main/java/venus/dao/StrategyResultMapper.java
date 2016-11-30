package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.strategy.StrategyResult;

@Component
@Mapper
public interface StrategyResultMapper {

	@Select("select case when max(id) is null then 1 else max(id)+1 end from strategy_result")
	public int findNextId();
	
	@Insert("insert into strategy_result (id,strategy_class,code,start_time,end_time,param,mar,sharpe_ratio,buy_times,sell_times,profit_rate,state,max_down,profit_rate_year,profit_rate_standard,profit_rate_standard_year)"
			+ " values (#{id},#{strategy_class},#{code},#{start_time},#{end_time},#{param},#{mar},#{sharpe_ratio},#{buy_times},#{sell_times},#{profit_rate},#{state},#{max_down},#{profit_rate_year},#{profit_rate_standard},#{profit_rate_standard_year})")
	public int insert(StrategyResult StrategyResult);
	
	@Update("update strategy_result set strategy_class=#{strategy_class},code=#{code},start_time=#{start_time},end_time=#{end_time},param=#{param},mar=#{mar},"
			+ "sharpe_ratio=#{sharpe_ratio},buy_times=#{buy_times},sell_times=#{sell_times},profit_rate=#{profit_rate},state=#{state},max_down=#{max_down},profit_rate_year=#{profit_rate_year}"
			+ ",profit_rate_standard=#{profit_rate_standard},profit_rate_standard_year=#{profit_rate_standard_year}"
			+ " where id=#{id}")
	public int update(StrategyResult strategyResult);
	
	@Select("select * from strategy_result where code=#{code} and strategy_class=#{strategy_class} and start_time=#{startTime} and end_time=#{endTime} and param=#{param}")
	public StrategyResult find(@Param("code")String code,@Param("strategy_class")String strategy_class,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("param")String param);
	
	@Select("select * from strategy_result where state=#{state}")
	public List<StrategyResult> findState(@Param("state")int state);
	
	@Delete("delete from strategy_result where id=#{id}")
	public int delete(@Param("id")int id);
	
	@Select("select * from strategy_result")
	public List<StrategyResult> findAll();
	
	@Update("update strategy_result set profit_rate_standard=#{profit_rate_standard},profit_rate_standard_year=#{profit_rate_standard_year} where id=#{id}")
	public int updateProfitRateStandard(@Param("id")int id,@Param("profit_rate_standard") double profit_rate_standard,@Param("profit_rate_standard_year")double profit_rate_standard_year);

	@Select("select * from strategy_result where code=#{code} and strategy_class=#{strategy_class} and start_time=#{startTime} and end_time=#{endTime} order by profit_rate_year desc limit 1")
	public StrategyResult findMaxProfit(@Param("code")String code,@Param("strategy_class")String strategy_class,@Param("startTime")String startTime,@Param("endTime")String endTime);

	@Select("select count(1) as cnt from strategy_result where code=#{code} and strategy_class=#{strategy_class} and start_time=#{startTime} and end_time=#{endTime} ")
	public int findCount(@Param("code")String code,@Param("strategy_class")String strategy_class,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	@Select("select * from (select * from strategy_result where code=#{code} and strategy_class=#{strategy_class} and start_time=#{startTime} and end_time=#{endTime} order by profit_rate_year desc limit 98) x order by x.profit_rate_year asc limit 1")
	public StrategyResult findMiddelProfit(@Param("code")String code,@Param("strategy_class")String strategy_class,@Param("startTime")String startTime,@Param("endTime")String endTime);
}
