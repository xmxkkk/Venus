package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.StockCompanyHolderCansell;

@Component
@Mapper
public interface StockCompanyHolderCansellMapper {

	@Insert("insert into stock_company_holder_cansell (code,date,num,rate,type,price,total_price,update_time) values"
			+ " (#{code},#{date},#{num},#{rate},#{type},#{price},#{total_price},#{update_time})")
	int insert(StockCompanyHolderCansell stockCompanyHolderCansell);
	
	@Update("update stock_company_holder_cansell set num=#{num},rate=#{rate},type=#{type},price=#{price},total_price=#{total_price},update_time=#{update_time}"
			+ " where code=#{code} and date=#{date}")
	int update(StockCompanyHolderCansell stockCompanyHolderCansell);
	
	@Select("select * from stock_company_holder_cansell where code=#{code}")
	List<StockCompanyHolderCansell> findCode(@Param("code")String code);
	
	@Delete("delete from stock_company_holder_cansell where code=#{code} and date=#{date}")
	int delete(@Param("code")String code,@Param("date")String date);
	
	@Select("select * from stock_company_holder_cansell where code=#{code} and date=#{date}")
	StockCompanyHolderCansell findCodeDate(@Param("code")String code,@Param("date")String date);
	
	@Select("select * from stock_company_holder_cansell where code=#{code} and DATEDIFF(`date`,date_format(now(),'%Y-%m-%d'))>#{day1} and DATEDIFF(`date`,date_format(now(),'%Y-%m-%d'))<#{day2}")
	List<StockCompanyHolderCansell> findCodeDay(@Param("code")String code,@Param("day1")int day1,@Param("day2")int day2);

	@Select("select * from stock_company_holder_cansell where code=#{code} and date<=#{date} order by date asc")
	List<StockCompanyHolderCansell> findDtLt(@Param("code")String code,@Param("date")String date);
}
