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

import venus.dao.StockCompanyHolderStructMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyHolderStruct;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyHolderStructTask {
	Logger logger=Logger.getLogger(StockCompanyHolderStructTask.class);
	@Value("${stock-company-holder-struct-threadnum}")
	public int threadNum;
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockCompanyHolderStructMapper stockCompanyHolderStructMapper;
	
	@Autowired URLUtil URLUtil;
	public void init(int threadId){
		init(false,threadId);
	}
	public void initCache(int threadId){
		init(true,threadId);
	}
	private void init(boolean cacheParam,int threadId){
		logger.info("[start]"+cacheParam);
		try{
			List<StockCompanyHolderStruct> insertAll=new ArrayList<StockCompanyHolderStruct>();
			
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(Stockinfo stock:stocks){
				//http://stockpage.10jqka.com.cn/000001/position/
				if (stock.getCode().hashCode() % threadNum != threadId) {
					continue;
				}
				
				String code=stock.getCode();
				String str=null;
				try{
					str=URLUtil.url2str("http://stockpage.10jqka.com.cn/"+stock.getCode()+"/holder/", cacheParam);
				}catch(Exception e){
					logger.error("[except]",e);
					continue;
				}
				if(StringUtil.isBlank(str))continue;
				Document document=Jsoup.parse(str);
				
				Elements elements=document.select("#stockcapit tr");
				if(elements==null||elements.size()==0){
					continue;
				}

				int len=elements.get(0).children().size()-1;
				
				List<StockCompanyHolderStruct> list=new ArrayList<StockCompanyHolderStruct>();
				for(int i=0;i<len;i++){
					StockCompanyHolderStruct temp=new StockCompanyHolderStruct();
					temp.setCode(code);
					list.add(temp);
				}
				
				for(int i=0;i<elements.size();i++){
					Elements children=elements.get(i).children();
					for(int j=1;j<children.size();j++){
						if(i==0){
							String time=children.get(j).text();
							list.get(j-1).setTime(time);
						}else if(i==1){
							double zongguben=0;
							String zonggubenStr=children.get(j).text();
							if(!zonggubenStr.equals("")&&!zonggubenStr.equals("-")){
//								zongguben=Double.parseDouble(zonggubenStr)*10000;
								zongguben=NumUtil.text2num(zonggubenStr);
							}
							list.get(j-1).setZongguben(zongguben);
						}else if(i==2){
							double aguzongguben=0;
							String aguzonggubenStr=children.get(j).text();
							if(!aguzonggubenStr.equals("")&&!aguzonggubenStr.equals("-")){
//								aguzongguben=Double.parseDouble(aguzonggubenStr)*10000;
								aguzongguben=NumUtil.text2num(aguzonggubenStr);
							}
							list.get(j-1).setAguzongguben(aguzongguben);
						}else if(i==3){
							double liutongagu=0;
							String liutongaguStr=children.get(j).text();
							if(!liutongaguStr.equals("")&&!liutongaguStr.equals("-")){
//								liutongagu=Double.parseDouble(liutongaguStr)*10000;
								liutongagu=NumUtil.text2num(liutongaguStr);
							}
							list.get(j-1).setLiutongagu(liutongagu);
						}else if(i==4){
							double xianshouagu=0;
							String xianshouaguStr=children.get(j).text();
							if(!xianshouaguStr.equals("")&&!xianshouaguStr.equals("-")){
//								xianshouagu=Double.parseDouble(xianshouaguStr)*10000;
								xianshouagu=NumUtil.text2num(xianshouaguStr);
							}
							list.get(j-1).setXianshouagu(xianshouagu);
						}
						
						if(elements.size()>6){
							if(i==5){
								double bguzongguben=0;
								String bguzonggubenStr=children.get(j).text();
								if(!bguzonggubenStr.equals("")&&!bguzonggubenStr.equals("-")){
//									bguzongguben=Double.parseDouble(bguzonggubenStr)*10000;
									bguzongguben=NumUtil.text2num(bguzonggubenStr);
								}
								list.get(j-1).setBguzongguben(bguzongguben);
							}else if(i==6){
								double liutongbgu=0;
								String liutongbguStr=children.get(j).text();
								if(!liutongbguStr.equals("")&&!liutongbguStr.equals("-")){
//									liutongbgu=Double.parseDouble(liutongbguStr)*10000;
									liutongbgu=NumUtil.text2num(liutongbguStr);
								}
								list.get(j-1).setLiutongbgu(liutongbgu);
							}else if(i==7){
								double xianshoubgu=0;
								String xianshoubguStr=children.get(j).text();
								if(!xianshoubguStr.equals("")&&!xianshoubguStr.equals("-")){
//									xianshoubgu=Double.parseDouble(xianshoubguStr)*10000;
									xianshoubgu=NumUtil.text2num(xianshoubguStr);
								}
								list.get(j-1).setXianshoubgu(xianshoubgu);
							}
						}
						if(elements.size()>9){
							if(i==8){
								double hguzongguben=0;
								String hguzonggubenStr=children.get(j).text();
								if(!hguzonggubenStr.equals("")&&!hguzonggubenStr.equals("-")){
//									hguzongguben=Double.parseDouble(hguzonggubenStr)*10000;
									hguzongguben=NumUtil.text2num(hguzonggubenStr);
								}
								list.get(j-1).setHguzongguben(hguzongguben);
							}else if(i==9){
								double liutonghgu=0;
								String liutonghguStr=children.get(j).text();
								if(!liutonghguStr.equals("")&&!liutonghguStr.equals("-")){
//									liutonghgu=Double.parseDouble(liutonghguStr)*10000;
									liutonghgu=NumUtil.text2num(liutonghguStr);
								}
								list.get(j-1).setLiutonghgu(liutonghgu);
							}else if(i==10){
								double xianshouhgu=0;
								String xianshouhguStr=children.get(j).text();
								if(!xianshouhguStr.equals("")&&!xianshouhguStr.equals("-")){
//									xianshouhgu=Double.parseDouble(xianshouhguStr)*10000;
									xianshouhgu=NumUtil.text2num(xianshouhguStr);
								}
								list.get(j-1).setXianshouhgu(xianshouhgu);
							}
						}
						
						String biandongyuanyin=children.get(j).text();
						list.get(j-1).setBiandongyuanyin(biandongyuanyin);
					}
				}
				for(int i=0;i<list.size();i++){
					String code1=list.get(i).getCode();
					String time=list.get(i).getTime();
					
					StockCompanyHolderStruct existStockCompanyHolderStruct=stockCompanyHolderStructMapper.findCodeTime(code1, time);
					if(existStockCompanyHolderStruct==null){
						insertAll.add(list.get(i));
					}
					
					if(insertAll.size()==1000){
						stockCompanyHolderStructMapper.insertAll(insertAll);
						logger.info(stock.getCode()+":"+insertAll.size());
						insertAll.clear();
					}
				}
				list.clear();
				
			}
			if(insertAll.size()>0){
				stockCompanyHolderStructMapper.insertAll(insertAll);
				logger.info(insertAll.size());
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]"+cacheParam);
	}
	
	
}
