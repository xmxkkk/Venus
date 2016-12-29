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
public class LuChoose4 implements LuChoose{
	Logger logger=Logger.getLogger(LuChoose4.class);
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
	public List<String> choose() {
		logger.info("[start]");
		
		List<String> result=new ArrayList<String>();
		try{
			List<Stockinfo> stocks=stockinfoMapper.findStop(0);
			for(Stockinfo stock:stocks){
				String code=stock.getCode();
				
				//传入合法条件
				if(!zongshizhiStockFilter.filter(code, "<10000000000D"))continue;
//				if(!hangyeStockFilter.filter(code, "1,!=医药生物"))continue;
//				if(!shiyinglvttmStockFilter.filter(code, "<50"))continue;
				if(!jingzichanshouyilvStockFilter.filter(code, ">10&&<35"))continue;
//				if(!top1holderrateStockFilter.filter(code, ">25"))continue;
	//			if(!orgholderrateStockFilter.filter(code, "<30"))continue;
				if(!xiaoshoumaolilvStockFilter.filter(code, ">35"))continue;
				if(!zichanfuzhailvStockFilter.filter(code, "<60&&>20"))continue;
	//			if(!cansellStockFilter.filter(code, "-30,30,>10"))continue;
//				if(!jinglirunstableStockFilter.filter(code, "true"))continue;
//				if(!jingzichanshouyilvhangyeStockFilter.filter(code, "<=0.2"))continue;
//				if(!dongshizhangStockFilter.filter(code, "true"))continue;
				if(!shangshiriqiStockFilter.filter(code, ">5"))continue;
				if(!priceStockFilter.filter(code, "<20"))continue;
				if(!financerateStockFilter.filter(code, "营业总收入,>10&&<55"))continue;
//				if(!betaStockFilter.filter(code, ">2.5"))continue;
//				if(!nameStockFilter.filter(code, "!INST"))continue;
//				if(!xiadieStockFilter.filter(code, "60,<-10"))continue;
				
//				if(!xianjinliuStockFilter.filter(code, "筹资现金,>0"))continue;
				if(!xianjinliuStockFilter.filter(code, "经营现金,>0"))continue;
//				if(!xianjinliuStockFilter.filter(code, "投资现金,<0"))continue;
//				if(!xianjinliuStockFilter.filter(code, "现金,>0"))continue;
				
				
				
				result.add(code);
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]"+result.size()+","+result);
		return result;
	}

}
