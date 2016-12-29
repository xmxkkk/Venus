package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.LuStrategyStock;

@Component
@Mapper
public interface LuStrategyStockMapper {
	
	@Insert("insert into lu_strategy_stock (id,code,market,name,addtime,quittime,status,score,change_rate,curr_price,zongshizhi,shiyinglvttm,update_time) values "
			+ "(#{id},#{code},#{market},#{name},#{addtime},#{quittime},#{status},#{score},#{change_rate},#{curr_price},#{zongshizhi},#{shiyinglvttm},#{update_time})")
	int insert(LuStrategyStock luStrategyStock);
	
	@Update("update lu_strategy_stock set market=#{market},name=#{name},addtime=#{addtime},quittime=#{quittime},status=#{status},score=#{score}"
			+ ",change_rate=#{change_rate},curr_price=#{curr_price},zongshizhi=#{zongshizhi},shiyinglvttm=#{shiyinglvttm},update_time=#{update_time} where id=#{id} and code=#{code}")
	int update(LuStrategyStock luStrategyStock);
	
	@Select("select * from lu_strategy_stock where id=#{id} and code=#{code}")
	LuStrategyStock find(@Param("id")int id,@Param("code")String code);
	
	@Select("select * from lu_strategy_stock where id=#{id}")
	List<LuStrategyStock> findId(@Param("id")int id);
	
	@Select("select * from lu_strategy_stock where id=#{id} and status=#{status}")
	List<LuStrategyStock> findIdStatus(@Param("id")int id,@Param("status")int status);
	
	@Delete("delete from lu_strategy_stock where id=#{id}")
	int deleteId(@Param("id")int id);
}
