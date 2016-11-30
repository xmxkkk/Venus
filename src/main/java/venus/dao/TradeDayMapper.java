package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.TradeDay;

@Component
@Mapper
public interface TradeDayMapper {
	@Select("select * from trade_day order by dt")
	List<TradeDay> find();

	@Select("select * from trade_day where dt>=#{start_time} and dt<=#{end_time} order by dt")
	List<TradeDay> findTime(@Param("start_time")String start_time,@Param("end_time")String end_time);
	
	@Delete("delete from trade_day")
	int delete();
	
	@Insert("insert into trade_day select DISTINCT dt from index_day")
	int insert();
	
	@Select("select * from trade_day where dt>#{dt} order by dt asc limit 1")
	TradeDay findTimeFirst(@Param("dt")String dt);
	
	@Select("select * from trade_day where dt<now() order by dt desc limit 1")
	TradeDay findLastDay();
	
	@Select("select * from trade_day where dt=#{dt}")
	TradeDay findDt(@Param("dt")String dt);
}
