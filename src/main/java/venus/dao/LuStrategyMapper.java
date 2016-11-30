package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.LuStrategy;

@Component
@Mapper
public interface LuStrategyMapper {

	@Insert("insert into lu_strategy (id,title,attr,rate_3month,rate_1month,update_time,strategy_class,up,down,flat,img,type,modify_date,interval_day) values "
			+ "(#{id},#{title},#{attr},#{rate_3month},#{rate_1month},#{update_time},#{strategy_class},#{up},#{down},#{flat},#{img},#{type},#{modify_date},#{interval_day})")
	int insertLuStrategy(LuStrategy luStrategy);
	
	@Update("update lu_strategy set title=#{title},attr=#{attr},rate_3month=#{rate_3month},rate_1month=#{rate_1month}"
			+ ",update_time=#{update_time},strategy_class=#{strategy_class},up=#{up},down=#{down},flat=#{flat},img=#{img},type=#{type},modify_date=#{modify_date}"
			+ ",interval_day=#{interval_day},status=#{status},run_status=#{run_status} where id=#{id}")
	int updateLuStrategy(LuStrategy luStrategy);
	
	@Select("select * from lu_strategy")
	List<LuStrategy> find();
	
	@Select("select * from lu_strategy where id=#{id}")
	LuStrategy findId(int id);

	@Select("select * from lu_strategy where status=#{status}")
	List<LuStrategy> findStatus(@Param("status")int status);
	
}
