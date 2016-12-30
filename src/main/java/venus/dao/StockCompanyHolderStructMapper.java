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

import venus.model.dao.StockCompanyHolderStruct;

@Component
@Mapper
public interface StockCompanyHolderStructMapper {

	@Insert("insert into stock_company_holder_struct (code,time,aguzongguben,liutongagu,xianshouagu,bguzongguben,liutongbgu,xianshoubgu,hguzongguben,liutonghgu,xianshouhgu,biandongyuanyin,update_time) values "
			+ "(#{code},#{time},#{aguzongguben},#{liutongagu},#{xianshouagu},#{bguzongguben},#{liutongbgu},#{xianshoubgu},#{hguzongguben},#{liutonghgu},#{xianshouhgu},#{biandongyuanyin},#{update_time})")
	int insert(StockCompanyHolderStruct stockCompanyHolderStruct);

	@Delete("delete from stock_company_holder_struct where code=#{code}")
	int deleteCode(String code);
	
	@Delete("delete from stock_company_holder_struct where code=#{code} and time=#{time}")
	int deleteCodeTime(@Param("code")String code,@Param("time")String time);
	
	@Select("select * from stock_company_holder_struct where code=#{code}")
	List<StockCompanyHolderStruct> findCode(String code);
	
	@Select("select * from stock_company_holder_struct where code=#{code} order by time desc limit 1")
	StockCompanyHolderStruct findCodeLast(String code);
	
	@Select("select * from stock_company_holder_struct where code=#{code} and time=#{time}")
	StockCompanyHolderStruct findCodeTime(@Param("code")String code,@Param("time")String time);
	
	@InsertProvider(type=StockCompanyHolderStructMapperProvider.class,method="insertAll")
	int insertAll(List<StockCompanyHolderStruct> list);
	
	public static class StockCompanyHolderStructMapperProvider {
		public String insertAll(Map<String, List<StockCompanyHolderStruct>> map) {
			List<StockCompanyHolderStruct> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `stock_company_holder_struct` (`code`,`time`,`zongguben`,`aguzongguben`,`liutongagu`,`xianshouagu`,`bguzongguben`,`liutongbgu`,`xianshoubgu`,`hguzongguben`,`liutonghgu`,`xianshouhgu`,`biandongyuanyin`,`update_time`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].time'}',#'{'list[{0}].zongguben'}',#'{'list[{0}].aguzongguben'}',#'{'list[{0}].liutongagu'}',#'{'list[{0}].xianshouagu'}',#'{'list[{0}].bguzongguben'}',#'{'list[{0}].liutongbgu'}',#'{'list[{0}].xianshoubgu'}',#'{'list[{0}].hguzongguben'}',#'{'list[{0}].liutonghgu'}',#'{'list[{0}].xianshouhgu'}',#'{'list[{0}].biandongyuanyin'}',#'{'list[{0}].update_time'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
}
