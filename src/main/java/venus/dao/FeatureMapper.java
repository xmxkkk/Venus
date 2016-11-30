package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.Feature;

@Mapper
@Component
public interface FeatureMapper {

	@Insert("insert into feature (code,feature_type,start_time,end_time,type,feature) values (#{code},#{feature_type},#{start_time},#{end_time},#{type},#{feature})")
	public int insert(@Param("code")String code,@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("type")String type,@Param("feature")String feature);
	
	@Update("update feature set feature=#{feature} where code=#{code} and type=#{type} and start_time=#{start_time} and end_time=#{end_time}")
	public int update(@Param("code")String code,@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("type")String type,@Param("feature")String feature);

	@Delete("delete from feature where code=#{code} and feature_type=#{feature_type} and start_time=#{start_time} and end_time=#{end_time} and type=#{type}")
	public int delete(@Param("code")String code,@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("type")String type);
	
	@Select("select * from feature where code=#{code} and feature_type=#{feature_type} and start_time=#{start_time} and end_time=#{end_time} and type=#{type}")
	public Feature find(@Param("code")String code,@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("type")String type);
	
	@Select("select * from feature where feature_type=#{feature_type} and start_time=#{start_time} and end_time=#{end_time} and type=#{type}")
	public List<Feature> findType(@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("type")String type);
	
	@Select("select * from feature where feature_type=#{feature_type} and start_time=#{start_time} and end_time=#{end_time} and type=#{type} and feature not like '%o%'")
	public List<Feature> findTypeO(@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("type")String type);
}
