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

import venus.model.dao.StockCompanyHolderChange;

@Component
@Mapper
public interface StockCompanyHolderChangeMapper {

	@Insert("insert into stock_company_holder_change (code,time,aguzongguben,liutongagu,xianshouagu,biandongyuanyin,update_time) values "
			+ "(#{code},#{time},#{aguzongguben},#{liutongagu},#{xianshouagu},#{biandongyuanyin},#{update_time})")
	int insert(StockCompanyHolderChange stockCompanyHolderStruct);

	@Delete("delete from stock_company_holder_change where code=#{code}")
	int deleteCode(String code);
	
	@Delete("delete from stock_company_holder_change where code=#{code} and time=#{time}")
	int deleteCodeTime(@Param("code")String code,@Param("time")String time);
	
	@Select("select * from stock_company_holder_change where code=#{code}")
	List<StockCompanyHolderChange> findCode(String code);
	
	@Select("select * from stock_company_holder_change where code=#{code} and time=#{time}")
	StockCompanyHolderChange findCodeTime(@Param("code")String code,@Param("time")String time);
	
	@InsertProvider(type=StockCompanyHolderChangeMapperProvider.class,method="insertAll")
	int insertAll(List<StockCompanyHolderChange> list);
	
	public static class StockCompanyHolderChangeMapperProvider {
		public String insertAll(Map<String, List<StockCompanyHolderChange>> map) {
			List<StockCompanyHolderChange> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `stock_company_holder_change` (`code`,`time`,`aguzongguben`,`liutongagu`,`xianshouagu`,`biandongyuanyin`,`update_time`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].time'}',#'{'list[{0}].aguzongguben'}',#'{'list[{0}].liutongagu'}',#'{'list[{0}].xianshouagu'}',#'{'list[{0}].biandongyuanyin'}',#'{'list[{0}].update_time'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
}
