package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.StockCompanyEvent;

@Component
@Mapper
public interface StockCompanyEventMapper {
	
	@Insert("insert into stock_company_event (code,date,title,update_time,type,md5) values (#{code},#{date},#{title},#{update_time},#{type},#{md5})")
	int insert(StockCompanyEvent stockCompanyEvent);

	@Select("select * from stock_company_event where md5=#{md5}")
	StockCompanyEvent find(@Param("md5")String md5);
	
	@Select("select * from stock_company_event where code=#{code}")
	List<StockCompanyEvent> findCode(@Param("code")String code);

	@Select("select * from stock_company_event where code=#{code} and date<=#{date} order by date asc")
	List<StockCompanyEvent> findDtLt(@Param("code")String code,@Param("date")String date);
	
}
