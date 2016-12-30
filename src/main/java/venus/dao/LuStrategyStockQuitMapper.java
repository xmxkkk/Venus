package venus.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.LuStrategyStockQuit;

@Component
@Mapper
public interface LuStrategyStockQuitMapper {

	@Insert("insert into lu_strategy_stock_quit (id,code,calc_date,join_date,quit_date,join_price,quit_price,join_price_fu,quit_price_fu,update_time)"
			+ " values (#{id},#{code},#{calc_date},#{join_date},#{quit_date},#{join_price},#{quit_price},#{join_price_fu},#{quit_price_fu},#{update_time})")
	int insert(LuStrategyStockQuit luStrategyStockQuit);

	@Update("update lu_strategy_stock_quit set join_date=#{join_date},quit_date=#{quit_date},join_price=#{join_price},quit_price=#{quit_price}"
			+ ",join_price_fu=#{join_price_fu},quit_price_fu=#{quit_price_fu},update_time=#{update_time} "
			+ "where id=#{id} and code=#{code} and calc_date=#{calc_date}")
	int update(LuStrategyStockQuit luStrategyStockQuit);
	
	@Delete("delete from lu_strategy_stock_quit where id=#{id} and code=#{code} and calc_date=#{calc_date}")
	int delete(@Param("id")int id,@Param("code")String code,@Param("calc_date")String calc_date);
	
	@Select("select * from lu_strategy_stock_quit where id=#{id} and code=#{code} and calc_date=#{calc_date}")
	LuStrategyStockQuit find(@Param("id")int id,@Param("code")String code,@Param("calc_date")String calc_date);
}
