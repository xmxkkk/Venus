package venus.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.HangyeChangeRate;

@Component
@Mapper
public interface HangyeChangeRateMapper {
	
	@Insert("insert into hangye_change_rate (level,name,day7_change_rate,day14_change_rate,day21_change_rate,month1_change_rate,month3_change_rate,month6_change_rate,year1_change_rate,year2_change_rate"
			+ ",year3_change_rate,year4_change_rate,year5_change_rate,year6_change_rate,year7_change_rate,year8_change_rate,year9_change_rate,year10_change_rate,num) values "
			+ "(#{level},#{name},#{day7_change_rate},#{day14_change_rate},#{day21_change_rate},#{month1_change_rate},#{month3_change_rate},#{month6_change_rate},#{year1_change_rate},#{year2_change_rate}"
			+ ",#{year3_change_rate},#{year4_change_rate},#{year5_change_rate},#{year6_change_rate},#{year7_change_rate},#{year8_change_rate},#{year9_change_rate},#{year10_change_rate},#{num})")
	int insert(HangyeChangeRate hangyeChangeRate);
	
	@Select("select * from hangye_change_rate where level=#{level} and name=#{name}")
	HangyeChangeRate find(@Param("level")String level,@Param("name")String name);
	
	@Delete("delete from hangye_change_rate where level=#{level}")
	int deleteLevel(@Param("level")String level);
	
}
