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

import venus.model.dao.StockCompanyFinanceRate;

@Component
@Mapper
public interface StockCompanyFinanceRateMapper {
	
	@Insert("insert into stock_company_finance_rate (code,time,menu,rate,update_time) values "
			+ "(#{code},#{time},#{menu},#{rate},#{update_time})")
	int insert();
	
	@Select("select * from stock_company_finance_rate where code=#{code} and time=#{time} and menu=#{menu}")
	StockCompanyFinanceRate find(@Param("code")String code,@Param("time")String time,@Param("menu")String menu);
	
	@Delete("delete from stock_company_finance_rate where code=#{code} and time=#{time} and menu=#{menu}")
	int delete(@Param("code")String code,@Param("time")String time,@Param("menu")String menu);
	
	@Delete("delete from stock_company_finance_rate where code=#{code}")
	int deleteCode(@Param("code")String code);
	
	@InsertProvider(type=StockCompanyFinanceRateMapperProvider.class,method="insertAll")
	int insertAll(List<StockCompanyFinanceRate> list);
	
	public static class StockCompanyFinanceRateMapperProvider {
		public String insertAll(Map<String, List<StockCompanyFinanceRate>> map) {
			List<StockCompanyFinanceRate> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `stock_company_finance_rate` (`code`,`time`,`menu`,`rate`,`update_time`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].time'}',#'{'list[{0}].menu'}',#'{'list[{0}].rate'}',#'{'list[{0}].update_time'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
	
}
