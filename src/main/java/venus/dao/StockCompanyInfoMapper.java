package venus.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
	
	@Update("update stock_company_info set suoshudiyu=#{suoshudiyu},suoshuhangye1=#{suoshuhangye1},suoshuhangye2=#{suoshuhangye2}"
			+ ",cengyongming=#{cengyongming},zhuyingyewu=#{zhuyingyewu},chanpinmingcheng=#{chanpinmingcheng},konggugudong=#{konggugudong}"
			+ ",shijikongzhiren=#{shijikongzhiren},zuizhongkongzhiren=#{zuizhongkongzhiren},dongshizhang=#{dongshizhang},dongmi=#{dongmi}"
			+ ",farendaibiao=#{farendaibiao},zongjingli=#{zongjingli},zhucezijin=#{zhucezijin},yuangongrenshu=#{yuangongrenshu}"
			+ ",cangukonggu_num=#{cangukonggu_num},hebingbaobiao_num=#{hebingbaobiao_num},update_time=#{update_time} where code=#{code}")
	int update(StockCompanyInfo stockCompanyInfo);
}
