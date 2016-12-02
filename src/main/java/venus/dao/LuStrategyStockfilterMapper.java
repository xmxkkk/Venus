package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.LuStrategyStockfilter;

@Component
@Mapper
public interface LuStrategyStockfilterMapper {
	
	@Insert("insert into lu_strategy_stockfilter (name,type,title,attr,use_type,ord) values "
			+ "(#{name},#{type},#{title},#{attr},#{use_type},#{ord})")
	int insert(LuStrategyStockfilter luStrategyStockfilter);
	
	@Select("select * from lu_strategy_stockfilter where name=#{name}")
	LuStrategyStockfilter findName(String name);
	
	@Select("select * from lu_strategy_stockfilter")
	List<LuStrategyStockfilter> findAll();
	
}
