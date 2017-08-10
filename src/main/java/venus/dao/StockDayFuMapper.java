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

import venus.model.dao.StockDayFu;

@Mapper
public interface StockDayFuMapper {
	
	@Select("select * from stock_day_fu")
	List<StockDayFu> findAll();

	@Insert("insert into stock_day_fu (code,dt,open_price,close_price,high_price,low_price,change_rate,change_price,trade_quty,trade_amt,week,weight) "
			+ "values (#{code},#{dt},#{open_price},#{close_price},#{high_price},#{low_price},#{change_rate},#{change_price},#{trade_quty},#{trade_amt},#{week},#{weight})")
	public int insertStockDayFu(StockDayFu stockDay);
	
	@Select("select * from stock_day_fu where code=#{code} order by dt asc")
	public List<StockDayFu> findCode(@Param("code")String code);

	@Select("select * from stock_day_fu where code=#{code} order by dt desc limit 1")
	StockDayFu findLast(@Param("code")String code);
	
	@Delete("delete from stock_day_fu where code=#{code} and dt>=#{startDt} and dt<=#{endDt}")
	int deleteCodeDt(@Param("code")String code,@Param("startDt")String startDt,@Param("endDt")String endDt);
	
	@Delete("delete from stock_day_fu where code=#{code}")
	int deleteCode(@Param("code")String code);
	
	@Select("select * from stock_day_fu where code=#{code} and dt=#{dt}")
	public StockDayFu findStockDayFu(@Param("code") String code, @Param("dt") String dt);
	
	@Select("select * from (select * from stock_day_fu where code=#{code} and dt<#{dt} order by dt desc limit #{day}) x order by x.dt")
	public List<StockDayFu> findStockDayFuLastN(@Param("code")String code,@Param("dt")String dt,@Param("day")int day);
	
	@Select("select * from stock_day_fu where code=#{code} order by dt desc limit #{day}")
	public List<StockDayFu> findLastN(@Param("code")String code,@Param("day")int day);
	
	@Select("select * from stock_day_fu where code=#{code} order by dt desc limit 1")
	public StockDayFu findStockDayFuLast(@Param("code")String code);
	
	@Select("select * from stock_day_fu where code=#{code} order by dt asc limit 1")
	public StockDayFu findFirst(@Param("code")String code);
	
	@Select("select * from stock_day_fu where code=#{code} and dt<#{dt}")
	public List<StockDayFu> findStockDayFuBefore(@Param("code")String code,@Param("dt")String dt);
	
	@Select("select * from stock_day_fu where code=#{code} and dt>=#{startTime} and dt<=#{endTime}")
	public List<StockDayFu> findStockDayFuStageLR(@Param("code")String code,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	@Select("select * from stock_day_fu where code=#{code} and dt>#{startTime} and dt<#{endTime}")
	public List<StockDayFu> findStockDayFuStage(@Param("code")String code,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	@Select("select * from stock_day_fu where code=#{code} and dt>=#{startTime} and dt<#{endTime}")
	public List<StockDayFu> findStockDayFuStageL(@Param("code")String code,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	@Select("select * from stock_day_fu where code=#{code} and dt>#{startTime} and dt<=#{endTime}")
	public List<StockDayFu> findStockDayFuStageR(@Param("code")String code,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	@Select("select * from stock_day_fu where code=#{code} order by abs(DATEDIFF(#{dt},dt)) asc limit 1")
	public StockDayFu findDtNear(@Param("code")String code,@Param("dt")String dt);
	
	@Update("update stock_day_fu set change_rate=#{change_rate},change_price=#{change_price} where code=#{code} and dt=#{dt}")
	public int updateChange(@Param("code")String code,@Param("dt")String dt,@Param("change_rate")double change_rate,@Param("change_price")double change_price);
	
	@Select("select count(1) as cnt from stock_day_fu where dt=#{dt}")
	public int findCountTotal(@Param("dt")String dt);
	
	@Select("select count(1) as cnt from stock_day_fu where dt=#{dt} and change_rate<0")
	public int findCountReduce(@Param("dt")String dt);
	
	@Select("select count(1) as cnt from stock_day_fu where dt=#{dt} and change_rate>0")
	public int findCountAdd(@Param("dt")String dt);
	
	@Select("select * from stock_day_fu where change_rate is null")
	List<StockDayFu> findChangeRateNull();
	
	@Select("select * from stock_day_fu where code=#{code} and change_rate is null")
	List<StockDayFu> findChangeRateNullCode(@Param("code")String code);
	
	@InsertProvider(type=StockDayFuMapperProvider.class,method="insertAll")
	int insertAll(List<StockDayFu> list);
	
	@UpdateProvider(type=StockDayFuMapperProvider.class,method="updateChangeAll")
	int updateChangeAll(List<StockDayFu> list);
	
	public static class StockDayFuMapperProvider {
		public String insertAll(Map<String, List<StockDayFu>> map) {
			List<StockDayFu> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(25600);
			stringBuilder.append("insert into `stock_day_fu` (`code`,`dt`,`open_price`,`close_price`,`high_price`,`low_price`,`change_rate`,`change_price`,`trade_quty`,`trade_amt`,`week`,`weight`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].dt'}',#'{'list[{0}].open_price'}',#'{'list[{0}].close_price'}',#'{'list[{0}].high_price'}',#'{'list[{0}].low_price'}',#'{'list[{0}].change_rate'}',#'{'list[{0}].change_price'}',#'{'list[{0}].trade_quty'}',#'{'list[{0}].trade_amt'}',#'{'list[{0}].week'}',#'{'list[{0}].weight'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
		public String updateChangeAll(Map<String, List<StockDayFu>> map) {
			List<StockDayFu> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(25600);
			MessageFormat messageFormat = new MessageFormat("update `stock_day_fu` set change_rate=#'{'list[{0}].change_rate'}',change_price=#'{'list[{0}].change_price'}' where code=#'{'list[{0}].code'}' and dt=#'{'list[{0}].dt'}';");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
			}
			return stringBuilder.toString();
		}
	}
	
	
	
}
