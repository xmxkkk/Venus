package venus.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.StageRate;

@Component
@Mapper
public interface StageRateMapper {
	@Insert("insert into stage_rate (stage,up,down,total,level) " + "values (#{stage},#{up},#{down},#{total},#{level})")
	public int insertStageRate(StageRate stageRate);

	@Select("select * from stage_rate where stage=#{stage}")
	public StageRate findStageRate(String stage);
}
