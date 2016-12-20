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
import org.springframework.stereotype.Component;

import venus.model.dao.StockCompanyHolderTop;

@Component
@Mapper
public interface StockCompanyHolderTopMapper {
	@Insert("insert into stock_company_holder_top (code,type,name,time,stock_number,stock_number_change,stock_rate,stock_rate_change,category,update_time) " 
			+ "values (#{code},#{type},#{name},#{time},#{stock_number},#{stock_number_change},#{stock_rate},#{stock_rate_change},#{category},#{update_time})")
	int insert(StockCompanyHolderTop stockCompanyHolderOrg);
	
	@Delete("delete from stock_company_holder_top where code=#{code} and type=#{type} and name=#{name} and time=#{time}")
	int delete(@Param("code")String code,@Param("type")String type,@Param("name")String name,@Param("time")String time);
	
	@Select("select * from stock_company_holder_top where code=#{code} and type=#{type} and name=#{name} and time=#{time}")
	StockCompanyHolderTop find(@Param("code")String code,@Param("type")String type,@Param("name")String name,@Param("time")String time);
	
	@Select("select * from stock_company_holder_top where code=#{code}")
	List<StockCompanyHolderTop> findCode(@Param("code")String code);
	
	@Select("select * from stock_company_holder_top where code=#{code} and time<=#{time} order by time asc")
	List<StockCompanyHolderTop> findDtLt(@Param("code")String code,@Param("time")String time);
	
	@Select("select * from stock_company_holder_top where code=#{code} and type=#{type} and time=#{time}")
	List<StockCompanyHolderTop> findCodeTypeTime(@Param("code")String code,@Param("type")String type,@Param("time")String time);
	
	@Select("select * from stock_company_holder_top where code=#{code} and type=#{type} order by time desc,stock_rate desc limit #{length}")
	List<StockCompanyHolderTop> findTop(@Param("code")String code,@Param("type")String type,@Param("length")int length);
	
	@InsertProvider(type=StockCompanyHolderTopMapperProvider.class,method="insertAll")
	int insertAll(List<StockCompanyHolderTop> list);
	
	public static class StockCompanyHolderTopMapperProvider {
		public String insertAll(Map<String, List<StockCompanyHolderTop>> map) {
			List<StockCompanyHolderTop> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `stock_company_holder_top` (`code`,`type`,`name`,`time`,`stock_number`,`stock_number_change`,`stock_rate`,`stock_rate_change`,`category`,`update_time`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].type'}',#'{'list[{0}].name'}',#'{'list[{0}].time'}',#'{'list[{0}].stock_number'}',#'{'list[{0}].stock_number_change'}',#'{'list[{0}].stock_rate'}',#'{'list[{0}].stock_rate_change'}',#'{'list[{0}].category'}',#'{'list[{0}].update_time'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
}
