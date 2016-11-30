package venus.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.StockCompanyHolderOrg;

@Component
@Mapper
public interface StockCompanyHolderOrgMapper {

	@Insert("insert into stock_company_holder_org (code,date,name,type,num,total_price,rate,num_change,update_time) values "
			+ "(#{code},#{date},#{name},#{type},#{num},#{total_price},#{rate},#{num_change},#{update_time})")
	int insert(StockCompanyHolderOrg stockCompanyHolderOrg);

	@Select("select * from stock_company_holder_org where code=#{code}")
	List<StockCompanyHolderOrg> findCode(@Param("code")String code);
	
	@Select("select sum(rate) as rate from stock_company_holder_org where code=#{code}")
	Double findCodeTotalRate(@Param("code")String code);
	
	@Select("select * from stock_company_holder_org where code=#{code} and date=#{date} and name=#{name} and type=#{type}")
	StockCompanyHolderOrg findCodeDtNameType(@Param("code")String code,@Param("date")String date,@Param("name")String name,@Param("type")String type);
	
	@Select("select * from stock_company_holder_org where code=#{code} and date<=#{date} order by date asc")
	List<StockCompanyHolderOrg> findDtLt(@Param("code")String code,@Param("date")String date);
	
	@InsertProvider(type=StockCompanyHolderOrgMapperProvider.class,method="insertAll")
	int insertAll(List<StockCompanyHolderOrg> list);
	
	public static class StockCompanyHolderOrgMapperProvider {
		public String insertAll(Map<String, List<StockCompanyHolderOrg>> map) {
			List<StockCompanyHolderOrg> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `stock_company_holder_org` (`code`,`date`,`name`,`type`,`num`,`total_price`,`rate`,`num_change`,`update_time`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code'}',#'{'list[{0}].date'}',#'{'list[{0}].name'}',#'{'list[{0}].type'}',#'{'list[{0}].num'}',#'{'list[{0}].total_price'}',#'{'list[{0}].rate'}',#'{'list[{0}].num_change'}',#'{'list[{0}].update_time'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
}
