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
import org.springframework.stereotype.Component;

import venus.model.dao.StockCompanyFinance;

@Component
@Mapper
public interface StockCompanyFinanceMapper {
	@Insert("insert into stock_company_finance (code,time,menu,type,`value`,update_time) " 
			+ "values (#{code},#{time},#{menu},#{type},#{value},#{update_time})")
	int insert(StockCompanyFinance stockCompanyFinance);
	
	@Update("update stock_company_finance set value=#{value} ,update_time=#{update_time} where code=#{code} and time=#{time} and menu=#{menu} and type=#{type}")
	int update(StockCompanyFinance stockCompanyFinance);
	
	@Delete("delete from stock_company_finance where code=#{code} and time=#{time} and menu=#{menu} and type=#{type}")
	int delete(@Param("code")String code,@Param("time")String time,@Param("menu")String menu,@Param("type")String type);
	
	@Select("select * from stock_company_finance where code=#{code} and time=#{time} and menu=#{menu} and type=#{type}")
	StockCompanyFinance find(@Param("code")String code,@Param("time")String time,@Param("menu")String menu,@Param("type")String type);
	
	@Select("select * from stock_company_finance where code=#{code} and time<=#{time} order by time asc")
	List<StockCompanyFinance> findDtLt(@Param("code")String code,@Param("time")String time);
	
	@Select("select * from stock_company_finance where code=#{code}")
	List<StockCompanyFinance> findCode(@Param("code")String code);
	
	@Select("select * from stock_company_finance where code=#{code} and type=#{type} order by time asc")
	List<StockCompanyFinance> findCodeType(@Param("code")String code,@Param("type")String type);
	
	@Select("select * from stock_company_finance where code=#{code} and type=#{type} and menu=#{menu} order by time asc")
	List<StockCompanyFinance> findCodeTypeMenu(@Param("code")String code,@Param("type")String type,@Param("menu")String menu);
	
	@Select("select * from stock_company_finance where code=#{code} and type=#{type} and menu=#{menu} order by time desc limit #{length}")
	List<StockCompanyFinance> findCodeTypeMenuLastLimit(@Param("code")String code,@Param("type")String type,@Param("menu")String menu,@Param("length")int length);
	
	@Select("select * from stock_company_finance where code=#{code} and type=#{type} and menu=#{menu} and time>=#{startTime} and time<=#{endTime}")
	List<StockCompanyFinance> findCodeTypeMenuTime(@Param("code")String code,@Param("type")String type,@Param("menu")String menu,@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	@Select("select * from stock_company_finance where code=#{code} order by time desc limit 1")
	StockCompanyFinance findLastTime(@Param("code")String code);
	
	@Select("select * from stock_company_finance where code=#{code} and type=#{type} order by time desc limit 1")
	StockCompanyFinance findCodeTypeLastTime(@Param("code")String code,@Param("type")String type);
	
	@Delete("delete from stock_company_finance where code=#{code}")
	int deleteCode(@Param("code")String code);
	
	@InsertProvider(type=StockCompanyFinanceMapperProvider.class,method="insertAll")
	int insertAll(List<StockCompanyFinance> list);
	
	public static class StockCompanyFinanceMapperProvider {
		public String insertAll(Map<String, List<StockCompanyFinance>> map) {
			List<StockCompanyFinance> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `stock_company_finance` (`code`,`time`,`menu`,`type`,`value`,`update_time`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].time'}',#'{'list[{0}].menu'}',#'{'list[{0}].type'}',#'{'list[{0}].value'}',#'{'list[{0}].update_time'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
}
