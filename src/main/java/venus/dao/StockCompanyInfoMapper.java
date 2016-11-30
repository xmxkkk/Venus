package venus.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import venus.model.dao.StockCompanyInfo;

@Component
@Mapper
public interface StockCompanyInfoMapper {
	
	@Insert("insert into stock_company_info (code,suoshudiyu,suoshuhangye1,suoshuhangye2,cengyongming,zhuyingyewu"
			+ ",chanpinmingcheng,konggugudong,shijikongzhiren,zuizhongkongzhiren,dongshizhang,dongmi,farendaibiao"
			+ ",zongjingli,zhucezijin,yuangongrenshu,cangukonggu_num,hebingbaobiao_num,update_time) values "
			+ "(#{code},#{suoshudiyu},#{suoshuhangye1},#{suoshuhangye2},#{cengyongming},#{zhuyingyewu}"
			+ ",#{chanpinmingcheng},#{konggugudong},#{shijikongzhiren},#{zuizhongkongzhiren},#{dongshizhang},#{dongmi}"
			+ ",#{farendaibiao},#{zongjingli},#{zhucezijin},#{yuangongrenshu},#{cangukonggu_num},#{hebingbaobiao_num},#{update_time})")
	int insert(StockCompanyInfo stockCompanyInfo);

	@Delete("delete from stock_company_info where code=#{code}")
	int delete(@Param("code")String code);
	
	@Select("select * from stock_company_info where code=#{code}")
	StockCompanyInfo findCode(@Param("code")String code);
}
