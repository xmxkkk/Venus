package venus.task.collect;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyInfoMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyInfo;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyInfoTask {
	Logger logger=Logger.getLogger(StockCompanyInfoTask.class);
	@Value("${stock-company-info-threadnum}")
	public int threadNum;
	@Autowired
	StockCompanyInfoMapper stockCompanyInfoMapper;
	@Autowired
	StockinfoMapper stockinfoMapper;
	
	@Autowired URLUtil URLUtil;
	public void init(String stockCode,int threadId){
		init(false,stockCode,threadId);
	}
	public void initCache(String stockCode,int threadId){
		init(true,stockCode,threadId);
	}
	private void init(boolean cacheParam,String stockCode,int threadId){
		logger.info("[start]"+cacheParam+","+stockCode);
		try{
			List<Stockinfo> stocks = null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				stocks=new ArrayList<Stockinfo>();
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks.add(stock);
			}
			for(Stockinfo stock:stocks){
				if(stockCode==null){
					if (stock.getCode().hashCode() % threadNum != threadId) {
						continue;
					}
				}
				String str=null;
				try{
					str=URLUtil.url2str("http://stockpage.10jqka.com.cn/"+stock.getCode()+"/company/", cacheParam);
				}catch(Exception e){
					logger.error("[except]",e);
					continue;
				}
				if(StringUtil.isBlank(str))continue;
				Document document=Jsoup.parse(str);
				
				Elements detail1=document.select("#detail .m_table").get(0).select("tr");

				
				String suoshudiyu=detail1.get(0).select("td").get(2).child(1).text();
				String suoshuhangye=detail1.get(1).select("td").get(1).child(1).text();
				String suoshuhangye1="";
				String suoshuhangye2="";
				if(!suoshuhangye.equals("-")){
					suoshuhangye1=suoshuhangye.split(" — ")[0];
					suoshuhangye2=suoshuhangye.split(" — ")[1];
				}
				String cengyongming=detail1.get(2).select("td").get(0).child(1).text();
				
				detail1=document.select("#detail .m_table").get(1).child(0).children();

				
				String zhuyingyewu=detail1.get(0).select("td").get(0).child(1).text();
				String chanpinmingcheng=detail1.get(1).select("td").get(0).child(1).text();
				
				String konggugudong="";
				if(detail1.get(2).select("td span").size()>0){
					konggugudong=detail1.get(2).select("td span").get(0).text();
				}
				
				String shijikongzhiren="";
				if(detail1.get(3).select("td span").size()>=2){
					shijikongzhiren=detail1.get(3).select("td span").get(0).text()+detail1.get(3).select("td span").get(1).text();
				}
				String zuizhongkongzhiren="";
				if(detail1.get(4).select("td span").size()>=2){
					zuizhongkongzhiren=detail1.get(4).select("td span").get(0).text()+detail1.get(4).select("td span").get(1).text();
				}
				
				String dongshizhang=detail1.get(5).select("td").get(0).child(1).text();
				String dongmi=detail1.get(5).select("td").get(1).child(1).text();
				String farendaibiao=detail1.get(5).select("td").get(2).child(1).text();
				String zongjingli=detail1.get(6).select("td").get(0).child(1).text();
				double zhucezijin=NumUtil.text2num(detail1.get(6).select("td").get(1).child(1).text());
				int yuangongrenshu=Integer.parseInt(detail1.get(6).select("td").get(2).child(1).text());
				
				
				Elements cangugongsishu=document.select("#share .m_table caption span").select("strong");
				int cangukonggu_num=0;
				if(cangugongsishu.size()>0){
					cangukonggu_num=Integer.parseInt(cangugongsishu.get(0).text());
				}
				int hebingbaobiao_num=0;
				if(cangugongsishu.size()>1){
					hebingbaobiao_num=Integer.parseInt(cangugongsishu.get(1).text());
				}

				
				StockCompanyInfo stockCompanyInfo=new StockCompanyInfo();
				stockCompanyInfo.setCode(stock.getCode());
				stockCompanyInfo.setCangukonggu_num(cangukonggu_num);
				stockCompanyInfo.setCengyongming(cengyongming);
				stockCompanyInfo.setChanpinmingcheng(chanpinmingcheng);
				stockCompanyInfo.setDongmi(dongmi);
				stockCompanyInfo.setDongshizhang(dongshizhang);
				stockCompanyInfo.setFarendaibiao(farendaibiao);
				stockCompanyInfo.setHebingbaobiao_num(hebingbaobiao_num);
				stockCompanyInfo.setKonggugudong(konggugudong);
				stockCompanyInfo.setShijikongzhiren(shijikongzhiren);
				stockCompanyInfo.setSuoshudiyu(suoshudiyu);
				stockCompanyInfo.setSuoshuhangye1(suoshuhangye1);
				stockCompanyInfo.setSuoshuhangye2(suoshuhangye2);
				stockCompanyInfo.setYuangongrenshu(yuangongrenshu);
				stockCompanyInfo.setZhucezijin(zhucezijin);
				stockCompanyInfo.setZhuyingyewu(zhuyingyewu);
				stockCompanyInfo.setZongjingli(zongjingli);
				stockCompanyInfo.setZuizhongkongzhiren(zuizhongkongzhiren);
				stockCompanyInfo.setUpdate_time(DateUtil.datetime());
				
				StockCompanyInfo existStockCompanyInfo=stockCompanyInfoMapper.findCode(stock.getCode());
				if(existStockCompanyInfo!=null){
					stockCompanyInfoMapper.update(stockCompanyInfo);
				}else{
					stockCompanyInfoMapper.insert(stockCompanyInfo);
				}
				
				
				logger.info(stockCompanyInfo);
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]");
	}
}
