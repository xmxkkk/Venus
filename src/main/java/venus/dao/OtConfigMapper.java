package venus.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.OtConfig;

@Component
@Mapper
public interface OtConfigMapper {

	@Select("select * from ot_config where name=#{name}")
	OtConfig find(String name);

	@Insert("insert into ot_config (id,name,title,`value`,status) values (#{id},#{name},#{title},#{value},#{status})")
	int insert(OtConfig otConfig);
	
	@Insert("update ot_config set name=#{name},title=#{title},`value`=#{value},status=#{status} where id=#{id}")
	int update(OtConfig otConfig);
}
