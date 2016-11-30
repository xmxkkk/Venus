package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.Stockinfo;

@Component
@Mapper
public interface StockinfoMapper {
	@Insert("insert into stockinfo (code,market,name,weight,ipo_start_time,ipo_price,create_time,beta,ipo_days,trade_days,first_trade_day,stop) "
			+ "values (#{code},#{market},#{name},#{weight},#{ipo_start_time},#{ipo_price},#{create_time},#{beta},#{ipo_days},#{trade_days},#{first_trade_day},#{stop})")
	public int insertStockinfo(Stockinfo stockinfo);

	@Select("select * from stockinfo")
	public List<Stockinfo> findStockinfos();
	
	@Select("select * from stockinfo where stop>=0")
	List<Stockinfo> findNotStop();

	@Update("update stockinfo set market=#{market},name=#{name},weight=#{weight},ipo_start_time=#{ipo_start_time},ipo_price=#{ipo_price},"
			+ "create_time=#{create_time},beta=#{beta},ipo_days=#{ipo_days},trade_days=#{trade_days},first_trade_day=#{first_trade_day},stop=#{stop} where code=#{code}")
	public int updateStockinfo(Stockinfo stockinfo);

	@Select("select * from stockinfo where code=#{code}")
	public Stockinfo findStockinfo(String code);
	
	@Select("select * from stockinfo where first_trade_day<=#{first_trade_day}")
	public List<Stockinfo> findFirstTradeDay(@Param("first_trade_day") String first_trade_day);
	
	@Update("update stockinfo set flag=#{flag} where code=${code}")
	int updateFlag(@Param("code")String code,@Param("flag")int flag);
	
	@Select("select * from stockinfo where flag=#{flag}")
	List<Stockinfo> findFlag(@Param("flag")int flag);
	
	@Update("update stockinfo set flag=#{flag}")
	int updateAllFlag(@Param("flag")int flag);
	
	@Select("select * from stockinfo where stop=#{stop}")
	List<Stockinfo> findStop(@Param("stop")int stop);
	
}
