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

import venus.model.dao.StockCompanyJingying;

@Component
@Mapper
public interface StockCompanyJingyingMapper {

	@Insert("insert into stock_company_jingying (code,date,type,category,menu,value,update_time) values "
			+ "(#{code},#{date},#{type},#{category},#{menu},#{value},#{update_time})")
	int insert(StockCompanyJingying stockCompanyJingying);
	
	@Delete("delete from stock_company_jingying where code=#{code} and date=#{date} and type=#{type} and menu=#{menu}")
	int deleteCodeDateTypeMenu(@Param("code")String code,@Param("date")String date,@Param("type")String type,@Param("menu")String menu);
	
	@Select("select * from stock_company_jingying where code=#{code} and date=#{date} and type=#{type} and menu=#{menu}")
	StockCompanyJingying find(@Param("code")String code,@Param("date")String date,@Param("type")String type,@Param("menu")String menu);
	
	@Delete("delete from stock_company_jingying where code=#{code}")
	int deleteCode(String code);
	
	@InsertProvider(type=StockCompanyJingyingMapperProvider.class,method="insertAll")
	int insertAll(List<StockCompanyJingying> list);
	
	public static class StockCompanyJingyingMapperProvider {
		public String insertAll(Map<String, List<StockCompanyJingying>> map) {
			List<StockCompanyJingying> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `stock_company_jingying` (`code`,`date`,`type`,`category`,`menu`,`value`,`update_time`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].date'}',#'{'list[{0}].type'}',#'{'list[{0}].category'}',#'{'list[{0}].menu'}',#'{'list[{0}].value'}',#'{'list[{0}].update_time'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
}
