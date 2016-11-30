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

import venus.model.dao.StockCompanyHolderNumber;

@Component
@Mapper
public interface StockCompanyHolderNumberMapper {
	
	@Insert("insert into stock_company_holder_number (code,time,menu,value,update_time) values ("
			+ "#{code},#{time},#{menu},#{value},#{update_time})")
	int insert(StockCompanyHolderNumber stockCompanyHolderNumber);

	@Update("update stock_company_holder_number set value=#{value},update_time=#{update_time} "
			+ "where code=#{code} and time=#{time} and menu=#{menu}")
	int update(StockCompanyHolderNumber stockCompanyHolderNumber);
	
	@Delete("delete from stock_company_holder_number where code=#{code} and time=#{time} and menu=#{menu}")
	int delete(@Param("code")String code,@Param("menu")String menu,@Param("time")String time);
	
	@Select("select * from stock_company_holder_number where code=#{code} and menu=#{menu}")
	List<StockCompanyHolderNumber> findCodeMenu(@Param("code")String code,@Param("menu")String menu);
	
	@Select("select * from stock_company_holder_number where code=#{code} and menu=#{menu} and time=#{time}")
	StockCompanyHolderNumber findCodeMenuTime(@Param("code")String code,@Param("menu")String menu,@Param("time")String time);
	
	@Select("select * from stock_company_holder_number where code=#{code} and time<=#{time} order by time asc")
	List<StockCompanyHolderNumber> findDtLt(@Param("code")String code,@Param("time")String time);
	
	@InsertProvider(type=StockCompanyHolderNumberMapperProvider.class,method="insertAll")
	int insertAll(List<StockCompanyHolderNumber> list);
	
	public static class StockCompanyHolderNumberMapperProvider {
		public String insertAll(Map<String, List<StockCompanyHolderNumber>> map) {
			List<StockCompanyHolderNumber> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `stock_company_holder_number` (`code`,`time`,`menu`,`value`,`update_time`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].time'}',#'{'list[{0}].menu'}',#'{'list[{0}].value'}',#'{'list[{0}].update_time'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
	
}
