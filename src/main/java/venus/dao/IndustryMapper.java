package venus.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import venus.model.dao.Industry;
import venus.model.dao.IndustryStock;

@Component
@Mapper
public interface IndustryMapper {
	@Insert("insert into industry (type,subtype,type_code,subtype_code) values (#{type},#{subtype},#{type_code},#{subtype_code})")
	public int insertIndustry(Industry industry);
	
	@Insert("insert into industry_stock (type_code,subtype_code,code) values (#{type_code},#{subtype_code},#{code})")
	public int insertIndustryStock(IndustryStock industryStock);
	
	@Delete("delete from industry")
	public int deleteIndustries();
	
	@Delete("delete from industry_stock")
	public int deleteIndustryStocks();

}
