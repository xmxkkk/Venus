package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.Indexinfo;

@Component
@Mapper
public interface IndexinfoMapper {
	
	@Insert("insert into indexinfo (code,name) values (#{code},#{name})")
	int insert(Indexinfo indexinfo);
	
	@Delete("delete from indexinfo")
	int deleteAll();
	
	@Select("select * from indexinfo")
	List<Indexinfo> find();
	
	@Select("select * from indexinfo where flag=#{flag}")
	List<Indexinfo> findFlag(@Param("flag")int flag);
	
	@Update("update indexinfo set flag=#{flag} where code=#{code}")
	int updateFlag(@Param("code")String code,@Param("flag")int flag);
	
	@Select("select * from indexinfo where code=#{code}")
	Indexinfo findCode(@Param("code")String code);
}
