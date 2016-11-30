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
import venus.model.dao.StockCompanyHolding;

@Component
@Mapper
public interface StockCompanyHoldingMapper {
	
	@Insert("insert into stock_company_holding (code,name,relation,rate,money,profit,is_combine,yewu,update_time) values "
			+ "(#{code},#{name},#{relation},#{rate},#{money},#{profit},#{is_combine},#{yewu},#{update_time})")
	int insert(StockCompanyHolding stockCompanyHolding);
	
	@Select("select * from stock_company_holding where code=#{code} and name=#{name} and relation=#{relation} and rate=#{rate}")
	StockCompanyHolding find(@Param("code")String code,@Param("name")String name,@Param("relation")String relation,@Param("rate")double rate);
	
	@Select("select * from stock_company_holding where code=#{code}")
	List<StockCompanyHolding> findCode(@Param("code")String code);
	
	@Delete("delete from stock_company_holding where code=#{code} and name=#{name} and relation=#{relation} and rate=#{rate}")
	int delete(@Param("code")String code,@Param("name")String name,@Param("relation")String relation,@Param("rate")double rate);
	
	@Delete("delete from stock_company_holding where code=#{code}")
	int deletes(@Param("code")String code);
	
	@InsertProvider(type=StockCompanyHoldingMapperProvider.class,method="insertAll")
	int insertAll(List<StockCompanyHolding> list);
	
	public static class StockCompanyHoldingMapperProvider {
		public String insertAll(Map<String, List<StockCompanyHolding>> map) {
			List<StockCompanyHolding> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `stock_company_holding` (`code`,`name`,`relation`,`rate`,`money`,`profit`,`is_combine`,`yewu`,`update_time`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].name'}',#'{'list[{0}].relation'}',#'{'list[{0}].rate'}',#'{'list[{0}].money'}',#'{'list[{0}].profit'}',#'{'list[{0}].is_combine'}',#'{'list[{0}].yewu'}',#'{'list[{0}].update_time'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
}
