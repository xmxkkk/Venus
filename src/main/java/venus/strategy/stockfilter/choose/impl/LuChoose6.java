package venus.strategy.stockfilter.choose.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHangyeDataMapper;
import venus.dao.StockCompanySummaryMapper;
import venus.dao.StockinfoMapper;
import venus.model.dao.Stockinfo;
import venus.strategy.stockfilter.choose.LuChoose;
import venus.strategy.stockfilter.filter.impl.BetaStockFilter;
import venus.strategy.stockfilter.filter.impl.CansellStockFilter;
import venus.strategy.stockfilter.filter.impl.DongshizhangStockFilter;
import venus.strategy.stockfilter.filter.impl.FinancerateStockFilter;
import venus.strategy.stockfilter.filter.impl.GuoqiStockFilter;
import venus.strategy.stockfilter.filter.impl.HangyeStockFilter;
import venus.strategy.stockfilter.filter.impl.JinglirunstableStockFilter;
import venus.strategy.stockfilter.filter.impl.JingzichanshouyilvStockFilter;
import venus.strategy.stockfilter.filter.impl.JingzichanshouyilvhangyeStockFilter;
import venus.strategy.stockfilter.filter.impl.NameStockFilter;
import venus.strategy.stockfilter.filter.impl.OrgholderrateStockFilter;
import venus.strategy.stockfilter.filter.impl.PriceStockFilter;
import venus.strategy.stockfilter.filter.impl.ShangshiriqiStockFilter;
import venus.strategy.stockfilter.filter.impl.ShiyinglvttmStockFilter;
import venus.strategy.stockfilter.filter.impl.Top1holderrateStockFilter;
import venus.strategy.stockfilter.filter.impl.XiadieStockFilter;
import venus.strategy.stockfilter.filter.impl.XianjinliuStockFilter;
import venus.strategy.stockfilter.filter.impl.XiaoshoumaolilvStockFilter;
import venus.strategy.stockfilter.filter.impl.YewuStockFilter;
import venus.strategy.stockfilter.filter.impl.ZichanfuzhailvStockFilter;
import venus.strategy.stockfilter.filter.impl.ZongshizhiStockFilter;

@Component
public class LuChoose6 implements LuChoose{
	Logger logger=Logger.getLogger(LuChoose6.class);
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	@Autowired StockCompanyHangyeDataMapper stockCompanyHangyeDataMapper;
	@Autowired ZongshizhiStockFilter zongshizhiStockFilter;
	@Autowired HangyeStockFilter hangyeStockFilter;
	@Autowired ShiyinglvttmStockFilter shiyinglvttmStockFilter;
	@Autowired JingzichanshouyilvStockFilter jingzichanshouyilvStockFilter;
	@Autowired Top1holderrateStockFilter top1holderrateStockFilter;
	@Autowired OrgholderrateStockFilter orgholderrateStockFilter;
	@Autowired XiaoshoumaolilvStockFilter xiaoshoumaolilvStockFilter;
	@Autowired ZichanfuzhailvStockFilter zichanfuzhailvStockFilter;
	@Autowired CansellStockFilter cansellStockFilter;
	@Autowired JinglirunstableStockFilter jinglirunstableStockFilter;
	@Autowired JingzichanshouyilvhangyeStockFilter jingzichanshouyilvhangyeStockFilter;
	@Autowired DongshizhangStockFilter dongshizhangStockFilter;
	@Autowired ShangshiriqiStockFilter shangshiriqiStockFilter;
	@Autowired PriceStockFilter priceStockFilter;
	@Autowired FinancerateStockFilter financerateStockFilter;
	@Autowired BetaStockFilter betaStockFilter;
	@Autowired NameStockFilter nameStockFilter;
	@Autowired YewuStockFilter yewuStockFilter;
	@Autowired XiadieStockFilter xiadieStockFilter;
	@Autowired XianjinliuStockFilter xianjinliuStockFilter;
	@Autowired GuoqiStockFilter guoqiStockFilter;
	public List<String> choose() {
		logger.info("[start]");
		
		List<String> result=new ArrayList<String>();
		try{
			List<Stockinfo> stocks=stockinfoMapper.findStop(0);
			for(Stockinfo stock:stocks){
				String code=stock.getCode();
				
			
//				if(!zongshizhiStockFilter.filter(code, "<100"))continue;
				if(!hangyeStockFilter.filter(code, "2,=新材料||=化工新材料||=化工合成材料"))continue;
//				if(!shiyinglvttmStockFilter.filter(code, "<18"))continue;
				if(!jingzichanshouyilvStockFilter.filter(code, ">0"))continue;
//				if(!top1holderrateStockFilter.filter(code, ">25"))continue;
	//			if(!orgholderrateStockFilter.filter(code, "<30"))continue;
//				if(!xiaoshoumaolilvStockFilter.filter(code, ">30"))continue;
//				if(!zichanfuzhailvStockFilter.filter(code, "<60&&>20"))continue;
	//			if(!cansellStockFilter.filter(code, "-30,30,>10"))continue;
//				if(!jinglirunstableStockFilter.filter(code, null))continue;
//				if(!jingzichanshouyilvhangyeStockFilter.filter(code, "<=0.2"))continue;
//				if(!dongshizhangStockFilter.filter(code, null))continue;
//				if(!shangshiriqiStockFilter.filter(code, ">3"))continue;
//				if(!priceStockFilter.filter(code, "<20"))continue;
//				if(!financerateStockFilter.filter(code, "应收账款,>-30&&<-20"))continue;
//				if(!financerateStockFilter.filter(code, "营业收入,>0"))continue;
//				if(!betaStockFilter.filter(code, ">2.5"))continue;
//				if(!nameStockFilter.filter(code, "!INST"))continue;
//				if(!xiadieStockFilter.filter(code, "60,<-10"))continue;
				
//				if(!xianjinliuStockFilter.filter(code, "筹资现金,>0"))continue;
//				if(!xianjinliuStockFilter.filter(code, "经营现金,>0"))continue;
//				if(!xianjinliuStockFilter.filter(code, "投资现金,<0"))continue;
//				if(!xianjinliuStockFilter.filter(code, "现金,>0"))continue;
				
				if(!guoqiStockFilter.filter(code, "false"))continue;
								
				result.add(code);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+result.size()+","+result);
		return result;
	}

}
