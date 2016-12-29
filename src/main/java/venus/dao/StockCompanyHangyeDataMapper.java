package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.StockCompanyHangyeData;

@Component
@Mapper
public interface StockCompanyHangyeDataMapper {

	@Insert("insert into stock_company_hangye_data (code,date,meigushouyi,meigujingzichan,meiguxianjinliu,jinglirun,yingyeshouru"
			+ ",zongzichan,jingzichanshouyilv,gudongquanyibilv,xiaoshoumaolilv,zongguben,zichanfuzhailv,update_time) values "
			+ "(#{code},#{date},#{meigushouyi},#{meigujingzichan},#{meiguxianjinliu},#{jinglirun},#{yingyeshouru},#{zongzichan}"
			+ ",#{jingzichanshouyilv},#{gudongquanyibilv},#{xiaoshoumaolilv},#{zongguben},#{zichanfuzhailv},#{update_time})")
	int insert(StockCompanyHangyeData stockCompanyHangyeData);
	
	@Update("update stock_company_hangye_data set meigushouyi=#{meigushouyi},meigujingzichan=#{meigujingzichan},meiguxianjinliu=#{meiguxianjinliu}"
			+ ",jinglirun=#{jinglirun},yingyeshouru=#{yingyeshouru},zongzichan=#{zongzichan},jingzichanshouyilv=#{jingzichanshouyilv}"
			+ ",gudongquanyibilv=#{gudongquanyibilv},xiaoshoumaolilv=#{xiaoshoumaolilv},zongguben=#{zongguben}"
			+ ",zichanfuzhailv=#{zichanfuzhailv},update_time=#{update_time} where code=#{code} and date=#{date}")
	int update(StockCompanyHangyeData stockCompanyHangyeData);
	
	@Select("select * from stock_company_hangye_data where code=#{code} and date=#{date}")
	StockCompanyHangyeData find(@Param("code")String code,@Param("date")String date);
	
	@Delete("delete from stock_company_hangye_data where code=#{code} and date=#{date}")
	int delete(@Param("code")String code,@Param("date")String date);
	
	@Select("select * from stock_company_hangye_data where code=#{code}")
	List<StockCompanyHangyeData> findCode(@Param("code")String code);
	
	@Select("select * from stock_company_hangye_data where code=#{code} order by date desc limit 1")
	StockCompanyHangyeData findNew(@Param("code")String code);
	
	@Select("select * from stock_company_hangye_data where code in (select code from stock_company_hangye where level3=#{level3}) and date=#{date}")
	List<StockCompanyHangyeData> findLevel3(@Param("level3")String level3,@Param("date")String date);
	
	@Select("select * from stock_company_hangye_data where code=#{code} order by date desc limit 1")
	StockCompanyHangyeData findLastTime(@Param("code")String code);
	
	@Select("select * from stock_company_hangye_data where code=#{code} and date<=#{date} order by date asc")
	List<StockCompanyHangyeData> findDtLt(@Param("code")String code,@Param("date")String date);
	
}
