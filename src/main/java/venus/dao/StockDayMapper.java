package venus.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Component;

import venus.model.dao.StockDay;

@Component
@Mapper
public interface StockDayMapper {
	
	@Insert("insert into stock_day (code,dt,open_price,close_price,high_price,low_price,change_rate,change_price,trade_quty,trade_amt,week) values "
			+ "(#{code},#{dt},#{open_price},#{close_price},#{high_price},#{low_price},#{change_rate},#{change_price},#{trade_quty},#{trade_amt},#{week})")
	int insert(StockDay stockDay);
	
	@Select("select * from stock_day where code=#{code} and dt=#{dt}")
	StockDay find(@Param("code")String code,@Param("dt")String dt);
	
	@Select("select * from stock_day where code=#{code} order by dt asc limit 1")
	StockDay findFirst(@Param("code")String code);
	
	@Select("select * from stock_day where code=#{code}")
	List<StockDay> findCode(@Param("code")String code);
	
	@Select("select * from stock_day where code=#{code} order by dt desc limit 1")
	StockDay findLast(@Param("code")String code);
	
	@Delete("delete from stock_day where code=#{code} and dt=#{dt}")
	int delete(@Param("code")String code,@Param("dt")String dt);
	
	@Delete("delete from stock_day where code=#{code}")
	int deleteCode(@Param("code")String code);
	
	@Delete("delete from stock_day where code=#{code} and dt>=#{startDt} and dt<=#{endDt}")
	int deleteCodeDt(@Param("code")String code,@Param("startDt")String startDt,@Param("endDt")String endDt);
	
	@Select("select * from (select * from stock_day where code=#{code} and dt<#{dt} order by dt desc limit #{day}) x order by x.dt")
	public List<StockDay> findStockDayLastN(@Param("code")String code,@Param("dt")String dt,@Param("day")int day);
	
	@Select("truncate table stock_day")
	void deleteAll();
	
	@Update("update stock_day set change_rate=#{change_rate},change_price=#{change_price} where code=#{code} and dt=#{dt}")
	int updateChange(@Param("code")String code, @Param("dt")String dt, @Param("change_rate")double change_rate,@Param("change_price")double change_price);
	
	@Select("select * from stock_day where change_rate is null")
	List<StockDay> findChangeRateNull();
	
	@InsertProvider(type=StockDayMapperProvider.class,method="insertAll")
	int insertAll(List<StockDay> list);
	
	@UpdateProvider(type=StockDayMapperProvider.class,method="updateChangeAll")
	int updateChangeAll(List<StockDay> list);
	
	@Select("select avg(abs(change_rate)) from stock_day where code=#{code} order by abs(change_rate) desc limit #{num}")
	Double findBetaAvg(@Param("code")String code,@Param("num")int num);
	
	@Select("select avg(abs(change_rate)) from stock_day where code=#{code}")
	Double findBeta(@Param("code")String code);
	
	public static class StockDayMapperProvider {
		public String insertAll(Map<String, List<StockDay>> map) {
			List<StockDay> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(25600);
			stringBuilder.append("insert into `stock_day` (`code`,`dt`,`open_price`,`close_price`,`high_price`,`low_price`,`change_rate`,`change_price`,`trade_quty`,`trade_amt`,`week`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].dt'}',#'{'list[{0}].open_price'}',#'{'list[{0}].close_price'}',#'{'list[{0}].high_price'}',#'{'list[{0}].low_price'}',#'{'list[{0}].change_rate'}',#'{'list[{0}].change_price'}',#'{'list[{0}].trade_quty'}',#'{'list[{0}].trade_amt'}',#'{'list[{0}].week'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
		public String updateChangeAll(Map<String, List<StockDay>> map) {
			List<StockDay> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(25600);
			MessageFormat messageFormat = new MessageFormat("update `stock_day` set change_rate=#'{'list[{0}].change_rate'}',change_price=#'{'list[{0}].change_price'}' where code=#'{'list[{0}].code'}' and dt=#'{'list[{0}].dt'}';");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
			}
			return stringBuilder.toString();
		}
	}
}
