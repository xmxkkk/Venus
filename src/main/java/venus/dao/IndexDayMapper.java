package venus.dao;

import java.math.BigDecimal;
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

import venus.model.dao.IndexDay;

@Component
@Mapper
public interface IndexDayMapper {
	@Insert("insert into index_day (code,dt,open_price,high_price,low_price,close_price,change_price,change_rate,trade_quty,trade_amt,week) values ("
			+ "#{code},#{dt},#{open_price},#{high_price},#{low_price},#{close_price},#{change_price},#{change_rate},#{trade_quty},#{trade_amt},#{week})")
	public int insertIndexDay(IndexDay indexDay);
	
	@Update("update index_day set open_price=#{open_price},high_price=#{high_price},low_price=#{low_price},close_price=#{close_price},"
			+ "change_price=#{change_price},change_rate=#{change_rate},trade_quty=#{trade_quty},trade_amt=#{trade_amt},week=#{week}"
			+ " where code=#{code} and dt=#{dt}")
	public int updateIndexDay(IndexDay indexDay);
	
	@Select("select * from index_day where code=#{code} order by dt desc limit 1")
	IndexDay findLast(@Param("code")String code);
	
	@Delete("delete from index_day where code=#{code} and dt>=#{startDt} and dt<=#{endDt}")
	int deleteCodeDt(@Param("code")String code,@Param("startDt")String startDt,@Param("endDt")String endDt);

	@Select("select * from index_day where code=#{code} and dt=#{dt}")
	public IndexDay findIndexDay(@Param("code") String code, @Param("dt") String dt);

	@Select("select avg(close_price) from ( select * from index_day where code=#{code} and dt<=#{dt} order by dt desc limit #{day}) x")
	public BigDecimal findClosePriceMa(@Param("code") String code, @Param("dt") String dt, @Param("day") int day);

	@Select("select avg(middle_price) from ( select * from index_day where code=#{code} and dt<=#{dt} order by dt desc limit #{day}) x")
	public BigDecimal findMiddlePriceMa(@Param("code") String code, @Param("dt") String dt, @Param("day") int day);

	@Select("select * from index_day where code=#{code} order by dt")
	public List<IndexDay> findIndexDayMarket(@Param("code") String code);

	@Select("select * from (select * from index_day where code=#{code} and dt<#{dt} order by dt desc limit #{day}) x order by x.dt")
	public List<IndexDay> findIndexDayLastN(@Param("code") String code, @Param("dt") String dt,
			@Param("day") int day);

	@Select("select * from index_day where code=#{code} and dt>=#{startDt} and dt<#{endDt}")
	public List<IndexDay> findIndexDayStage(@Param("code") String code, @Param("startDt") String startDt,
			@Param("endDt") String endDt);
	
	@Select("select * from index_day where code=#{code} order by dt desc limit #{day}")
	public List<IndexDay> findLastN(@Param("code")String code,@Param("day")int day);
	
	@Select("select * from index_day where code=#{code} order by dt asc")
	public List<IndexDay> findCode(@Param("code")String code);
	
	@Update("update index_day set change_rate=#{change_rate},change_price=#{change_price} where code=#{code} and dt=#{dt}")
	public int updateChange(@Param("code")String code,@Param("dt")String dt,@Param("change_rate")double change_rate,@Param("change_price")double change_price);

	@Select("select * from index_day where code=#{code} order by abs(DATEDIFF(#{dt},dt)) asc limit 1")
	public IndexDay findDtNear(@Param("code")String code,@Param("dt")String dt);
	
	@InsertProvider(type=IndexDayMapperProvider.class,method="insertAll")
	int insertAll(List<IndexDay> list);
	
	@UpdateProvider(type=IndexDayMapperProvider.class,method="updateChangeAll")
	int updateChangeAll(List<IndexDay> list);
	
	public static class IndexDayMapperProvider {
		public String insertAll(Map<String, List<IndexDay>> map) {
			List<IndexDay> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(25600);
			stringBuilder.append("insert into `index_day` (`code`,`dt`,`open_price`,`close_price`,`high_price`,`low_price`,`change_rate`,`change_price`,`trade_quty`,`trade_amt`,`week`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].dt'}',#'{'list[{0}].open_price'}',#'{'list[{0}].close_price'}',#'{'list[{0}].high_price'}',#'{'list[{0}].low_price'}',#'{'list[{0}].change_rate'}',#'{'list[{0}].change_price'}',#'{'list[{0}].trade_quty'}',#'{'list[{0}].trade_amt'}',#'{'list[{0}].week'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
		public String updateChangeAll(Map<String, List<IndexDay>> map) {
			List<IndexDay> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(25600);
			MessageFormat messageFormat = new MessageFormat("update `index_day` set change_rate=#'{'list[{0}].change_rate'}',change_price=#'{'list[{0}].change_price'}' where code=#'{'list[{0}].code'}' and dt=#'{'list[{0}].dt'}';");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
			}
			return stringBuilder.toString();
		}
	}
}
