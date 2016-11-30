package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.StockCompanyHangye;

@Component
@Mapper
public interface StockCompanyHangyeMapper {
	
	@Insert("insert into stock_company_hangye (code,level1,level2,level3,level_num,update_time) values "
			+ "(#{code},#{level1},#{level2},#{level3},#{level_num},#{update_time})")
	int insert(StockCompanyHangye stockCompanyHangye);
	
	@Select("select * from stock_company_hangye where code=#{code}")
	StockCompanyHangye findCode(@Param("code")String code);
	
	@Delete("delete from stock_company_hangye where code=#{code}")
	int delete(@Param("code")String code);
	
	@Select("select * from stock_company_hangye where level1=#{level1}")
	List<StockCompanyHangye> findLevel1(@Param("level1")String level1);
	
	@Select("select * from stock_company_hangye where level2=#{level2}")
	List<StockCompanyHangye> findLevel2(@Param("level2")String level2);
	
	@Select("select * from stock_company_hangye where level3=#{level3}")
	List<StockCompanyHangye> findLevel3(@Param("level3")String level3);
	
	@Select("select level1 from stock_company_hangye group by level1")
	List<String> findLevel1Group();
	
	@Select("select level2 from stock_company_hangye group by level2")
	List<String> findLevel2Group();
	
	@Select("select level3 from stock_company_hangye group by level3")
	List<String> findLevel3Group();
	
	
	
	
}
