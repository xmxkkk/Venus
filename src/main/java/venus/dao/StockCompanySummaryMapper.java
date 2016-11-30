package venus.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import venus.model.dao.StockCompanySummary;

@Component
@Mapper
public interface StockCompanySummaryMapper {
	@Insert("insert into stock_company_summary (code,open_price,close_price,curr_price,high_price,low_price,chengjiaoliang,chengjiaoe,update_time,zongguben,liutongguben,meigujingzichan,zuijinniandujinglirun,zuijinsigejidujinglirun,zuijinsigejidumeigushouyi) " 
			+ "values (#{code},#{open_price},#{close_price},#{curr_price},#{high_price},#{low_price},#{chengjiaoliang},#{chengjiaoe},#{update_time},#{zongguben},#{liutongguben},#{meigujingzichan},#{zuijinniandujinglirun},#{zuijinsigejidujinglirun},#{zuijinsigejidumeigushouyi})")
	int insert(StockCompanySummary stockCompanySummary);
	
	@Update("update stock_company_summary set zongguben=#{zongguben},liutongguben=#{liutongguben},meigujingzichan=#{meigujingzichan},zuijinniandujinglirun=#{zuijinniandujinglirun},zuijinsigejidujinglirun=#{zuijinsigejidujinglirun}"
			+ ",zuijinsigejidumeigushouyi=#{zuijinsigejidumeigushouyi},zuijinniandumeigushouyi=#{zuijinniandumeigushouyi},zhuceziben=#{zhuceziben},faxingjiage=#{faxingjiage}"
			+ ",chengliriqi=#{chengliriqi},shangshiriqi=#{shangshiriqi} where code=#{code}")
	int update(StockCompanySummary stockCompanySummary);
	
	@Delete("delete from stock_company_summary where code=#{code}")
	int delete(@Param("code")String code);
	
	@Select("select * from stock_company_summary")
	List<StockCompanySummary> finds();
	
	@Update("update stock_company_summary set zongshizhi=#{zongshizhi},liutongshizhi=#{liutongshizhi},zhenfu=#{zhenfu},"+
"huanshoulv=#{huanshoulv},shijinglv=#{shijinglv},shiyinglvttm=#{shiyinglvttm},"+
"shiyinglvjing=#{shiyinglvjing},jingzichanshouyilv=#{jingzichanshouyilv} where code=#{code}")
	int updateOther(StockCompanySummary stockCompanySummary);
	
	@Select("select * from stock_company_summary where code=#{code}")
	StockCompanySummary findCode(@Param("code")String code);
	
	@Select("select * from stock_company_summary where code=#{code} and chengjiaoliang=0.0 and open_price=0.0")
	StockCompanySummary findStop(@Param("code")String code);
	
	
}
