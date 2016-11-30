package venus.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.FeatureSimilar;

@Component
@Mapper
public interface FeatureSimilarMapper {
	
	@Insert("insert into feature_similar (code_a,code_b,feature_type,start_time,end_time,similar_type,similar) values (#{code_a},#{code_b},#{feature_type},#{start_time},#{end_time},#{similar_type},#{similar})")
	int insert(@Param("code_a")String code_a,@Param("code_b")String code_b,@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("similar_type")String similar_type,@Param("similar")double similar);

	@Delete("delete from feature_similar where code_a=#{code_a} and code_b=#{code_b} and feature_type=#{feature_type} and start_time=#{start_time} and end_time=#{end_time} and similar_type=#{similar_type}")
	int delete(@Param("code_a")String code_a,@Param("code_b")String code_b,@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("similar_type")String similar_type);
	
	@Select("select * from feature_similar where code_a=#{code_a} and code_b=#{code_b} and feature_type=#{feature_type} and start_time=#{start_time} and end_time=#{end_time} and similar_type=#{similar_type}")
	FeatureSimilar find(@Param("code_a")String code_a,@Param("code_b")String code_b,@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("similar_type")String similar_type);
	
	@Select("select * from feature_similar where ((code_a=#{code_a} and code_b=#{code_b}) or (code_a=#{code_b} and code_b=#{code_a})) and feature_type=#{feature_type} and start_time=#{start_time} and end_time=#{end_time} and similar_type=#{similar_type}")
	FeatureSimilar findDouble(@Param("code_a")String code_a,@Param("code_b")String code_b,@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("similar_type")String similar_type);
	
	@Delete("delete from feature_similar where feature_type=#{feature_type} and start_time=#{start_time} and end_time=#{end_time} and similar_type=#{similar_type}")
	int deleteType(@Param("feature_type")String feature_type,@Param("start_time")String start_time,@Param("end_time")String end_time,@Param("similar_type")String similar_type);
	
	@InsertProvider(type=StockFeatureSimilarMapperProvider.class,method="insertAll")
	int insertAll(List<FeatureSimilar> list);
	
	public static class StockFeatureSimilarMapperProvider {
		public String insertAll(Map<String, List<FeatureSimilar>> map) {
			List<FeatureSimilar> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder(256);
			stringBuilder.append("insert into `feature_similar` (`code_a`,`code_b`,`feature_type`,`start_time`,`end_time`,`similar_type`,`similar`) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].code_a'}',#'{'list[{0}].code_b'}',#'{'list[{0}].feature_type'}',#'{'list[{0}].start_time'}',#'{'list[{0}].end_time'}',#'{'list[{0}].similar_type'}',#'{'list[{0}].similar'}')");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{String.valueOf(i)}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
	}
}
