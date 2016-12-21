package venus.task.analyse;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import venus.dao.IndexDayMapper;
import venus.dao.LuStrategyChangeRateMapper;
import venus.dao.LuStrategyFilterMapper;
import venus.dao.LuStrategyMapper;
import venus.dao.LuStrategyStockMapper;
import venus.dao.RunStatusMapper;
import venus.dao.StockCompanyHangyeDataMapper;
import venus.dao.StockCompanyHangyeMapper;
import venus.dao.StockCompanySummaryMapper;
import venus.dao.StockDayFuMapper;
import venus.dao.StockDayMapper;
import venus.dao.StockinfoMapper;
import venus.dao.TradeDayMapper;
import venus.helper.util.CommonUtil;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.model.dao.IndexDay;
import venus.model.dao.LuStrategy;
import venus.model.dao.LuStrategyChangeRate;
import venus.model.dao.LuStrategyFilter;
import venus.model.dao.LuStrategyStock;
import venus.model.dao.StockCompanyHangye;
import venus.model.dao.StockCompanySummary;
import venus.model.dao.StockDay;
import venus.model.dao.StockDayFu;
import venus.model.dao.Stockinfo;
import venus.model.dao.TradeDay;
import venus.strategy.stockfilter.choose.LuChoose;
import venus.strategy.stockfilter.choose.LuCommonChoose;

@Component
public class LuStrategyTask extends ApplicationObjectSupport{
	Logger logger=Logger.getLogger(LuStrategyTask.class);
	@Autowired LuStrategyMapper luStrategyMapper;
	@Autowired LuStrategyFilterMapper luStrategyFilterMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired StockDayMapper stockDayMapper;
	@Autowired StockCompanyHangyeDataMapper stockCompanyHangyeDataMapper;
	@Autowired IndexDayMapper indexDayMapper;
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	@Autowired LuStrategyStockMapper luStrategyStockMapper;
	@Autowired TradeDayMapper tradeDayMapper;
	@Autowired StockCompanyHangyeMapper stockCompanyHangyeMapper;
	@Autowired LuStrategyChangeRateMapper luStrategyChangeRateMapper;
	@Autowired RunStatusMapper runStatusMapper;
	@Autowired LuCommonChoose luCommonChoose;
	public void init(int id,String strategy_json_str){
		logger.info("[start]id="+id);
		try{
			String endDt=DateUtil.date2();
			
			int months[]={-7,-14,-21,-30,-90,-180,-365,-365*2,-365*3,-365*4,-365*5,-365*6,-365*7,-365*8,-365*9,-365*10};
			
			String[] startDtMonths=new String[months.length];
			for(int i=0;i<months.length;i++){
				startDtMonths[i]=DateUtil.date2(months[i]);
			}
			
			String runStatusId=null;
					
			List<LuStrategy> luStrategies=new ArrayList<LuStrategy>();
			if(id==-1){
				JSONObject obj=JSONObject.parseObject(strategy_json_str);
				runStatusId=obj.getString("id");
				strategy_json_str=CommonUtil.decode(obj.getString("filters"));

				runStatusMapper.insert(runStatusId, 0);
				
				LuStrategy luStrategy=new LuStrategy();
				luStrategy.setId(-1);
				luStrategies.add(luStrategy);
			}else if(id==0){
				luStrategies=luStrategyMapper.findStatus(1);
			}else{
				LuStrategy luStrategy=luStrategyMapper.findId(id);
				if(luStrategy==null){
					logger.info("[message]not exist id:"+id);
				}else{
					luStrategies.add(luStrategy);
				}
			}
			
			for(LuStrategy luStrategy:luStrategies){
				
//				String end_date=DateUtil.date2(luStrategy.getModify_date(), luStrategy.getInterval_day());
//				if(end_date.compareTo(DateUtil.date2())>=0){
//					continue;
//				}
			
				List<String> codes=null;
				if(!StringUtil.isBlank(luStrategy.getStrategy_class())){
					LuChoose luChoose=(LuChoose)getApplicationContext().getBean(luStrategy.getStrategy_class());
					codes=luChoose.choose();
				}else{
					if(luStrategy.getId()>0){
						List<LuStrategyFilter> luStrategyFilters=luStrategyFilterMapper.findId(luStrategy.getId());
						if(luStrategyFilters.size()==0)continue;
					}
					if(luStrategy.getId()==-1){
						codes=luCommonChoose.choose(strategy_json_str);
					}else{
						codes=luCommonChoose.choose(luStrategy.getId());
					}
				}
				
				double[] rateAvgMonths=new double[months.length];
				int[] iAvgMonths=new int[months.length];
				for(int i=0;i<months.length;i++){
					rateAvgMonths[i]=0;
					iAvgMonths[i]=0;
				}
				
				int down=0;
				int up=0;
				int flat=0;
				
				if(luStrategy.getId()==-1){
					luStrategyStockMapper.deleteId(luStrategy.getId());
					luStrategyChangeRateMapper.deleteId(luStrategy.getId());
				}
				
				for(int i=0;i<codes.size();i++){
					String code=codes.get(i);
					
					StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
					if(stockCompanySummary==null)continue;
					
					StockDayFu endStockDay=stockDayFuMapper.findDtNear(code, endDt.replaceAll("-", ""));
					if(endStockDay==null)continue;
					double weight=endStockDay.getWeight();
					
					for(int j=0;j<months.length;j++){
						if(stockCompanySummary.getShangshiriqi().compareTo(startDtMonths[j])<0){
							//计算一个月的收益
							StockDayFu startMonthStockDay=stockDayFuMapper.findDtNear(code, startDtMonths[j].replaceAll("-", ""));
							if(startMonthStockDay==null)continue;
							double rateMonth=((endStockDay.getClose_price()/weight) - (startMonthStockDay.getClose_price()/weight)) / (startMonthStockDay.getClose_price()/weight) * 100.0;
							rateAvgMonths[j]+=NumUtil.format2(rateMonth);
							iAvgMonths[j]++;
						}
					}
					
					Stockinfo stock=stockinfoMapper.findStockinfo(code);
					
					TradeDay lastTradeDay=tradeDayMapper.findLastDay();
					StockDay currentStockDay=stockDayMapper.find(code, lastTradeDay.getDt());
					double change_rate=0.;
					if(stock.getStop()==0){
						if(currentStockDay==null){
							change_rate=0.;
						}else{
							change_rate=currentStockDay.getChange_rate()*100;
						}
					}
					
					StockDay lastStockDay=stockDayMapper.findLast(code);
					
					String market=StringUtil.toMarketName(stock).substring(0, 2).toUpperCase();
					
					//更新数据
					LuStrategyStock luStrategyStockDb=luStrategyStockMapper.find(luStrategy.getId(), code);
					if(luStrategyStockDb==null){
						luStrategyStockDb=new LuStrategyStock();
						luStrategyStockDb.setAddtime(DateUtil.datetime2());
						
						
						luStrategyStockDb.setId(luStrategy.getId());
						luStrategyStockDb.setMarket(market);
						luStrategyStockDb.setName(stock.getName());
						luStrategyStockDb.setChange_rate(change_rate);
						luStrategyStockDb.setCode(code);
						luStrategyStockDb.setCurr_price(NumUtil.format2(lastStockDay.getClose_price()));
						luStrategyStockDb.setQuittime("");
						luStrategyStockDb.setScore(1);
						luStrategyStockDb.setShiyinglvttm(stockCompanySummary.getShiyinglvttm());
						luStrategyStockDb.setStatus(1);
						luStrategyStockDb.setZongshizhi(stockCompanySummary.getZongshizhi());
						luStrategyStockDb.setUpdate_time(DateUtil.datetime());
						
						luStrategyStockMapper.insert(luStrategyStockDb);
					}
					
					luStrategyStockDb.setMarket(market);
					luStrategyStockDb.setName(stock.getName());
					luStrategyStockDb.setChange_rate(change_rate);
					luStrategyStockDb.setCode(code);
					luStrategyStockDb.setCurr_price(NumUtil.format2(lastStockDay.getClose_price()));
					luStrategyStockDb.setQuittime("");
					luStrategyStockDb.setScore(luStrategyStockDb.getScore()+1);
					luStrategyStockDb.setShiyinglvttm(stockCompanySummary.getShiyinglvttm());
					luStrategyStockDb.setStatus(1);
					luStrategyStockDb.setZongshizhi(stockCompanySummary.getZongshizhi());
					luStrategyStockDb.setUpdate_time(DateUtil.datetime());
					
					luStrategyStockMapper.update(luStrategyStockDb);
					
					if(change_rate==0.0){
						flat++;
					}else if(change_rate>0){
						up++;
					}else{
						down++;
					}
					
				}
				
				luStrategy.setDown(down);
				luStrategy.setUp(up);
				luStrategy.setFlat(flat);
				luStrategy.setModify_date(DateUtil.date2());
				luStrategy.setUpdate_time(DateUtil.datetime());
				
				luStrategyMapper.updateLuStrategy(luStrategy);
				
				//更新score
				List<LuStrategyStock> stocks=luStrategyStockMapper.findIdStatus(luStrategy.getId(), 1);
				for(int i=0;i<stocks.size();i++){
					LuStrategyStock temp=stocks.get(i);
					if(!codes.contains(temp.getCode())){
						temp.setScore(0);
						temp.setStatus(0);
						luStrategyStockMapper.update(temp);
					}
				}
				
				logger.info("时间	:7天	:14天	:21天	:1月	:3月	:6月	:1年	:2年	:3年	:4年	:5年	:6年	:7年	:8年	:9年	:10年");
				
				for(int i=0;i<months.length;i++){
					iAvgMonths[i]=iAvgMonths[i]==0?1:iAvgMonths[i];
				}
				
				StringBuffer sb=new StringBuffer();
				for(int i=0;i<months.length;i++){
					rateAvgMonths[i]=NumUtil.format2(rateAvgMonths[i]/iAvgMonths[i]);
					sb.append("	:"+rateAvgMonths[i]);
				}

				//sh000300
				String sh000300="sh000300";
				IndexDay endIndexDay=indexDayMapper.findDtNear(sh000300, endDt);
				
				double[] rateMongths=new double[months.length];
				
				StringBuffer sb1=new StringBuffer();
				StringBuffer sb2=new StringBuffer();
				
				for(int i=0;i<months.length;i++){
					IndexDay startMonthIndexDay=indexDayMapper.findDtNear(sh000300, startDtMonths[i]);
					double rateMongth=NumUtil.format2((endIndexDay.getClose_price() - startMonthIndexDay.getClose_price()) / startMonthIndexDay.getClose_price() * 100.0);
					sb2.append("	:"+rateMongth);
					rateMongths[i]=NumUtil.format2(rateAvgMonths[i]-rateMongth);
					sb1.append("	:"+rateMongths[i]);
				}
				logger.info("组合"+sb.toString());
				logger.info("沪深"+sb2.toString());
				logger.info("去沪"+sb1.toString());
				
				String dt=DateUtil.date2();
				
				LuStrategyChangeRate dbLuStrategyChangeRate=luStrategyChangeRateMapper.find(luStrategy.getId(), dt);
				if(dbLuStrategyChangeRate!=null){
					luStrategyChangeRateMapper.delete(luStrategy.getId(), dt);
				}
				
				LuStrategyChangeRate luStrategyChangeRate=new LuStrategyChangeRate();
				luStrategyChangeRate.setId(luStrategy.getId());
				luStrategyChangeRate.setDt(dt);
				luStrategyChangeRate.setDay7_change_rate(rateMongths[0]);
				luStrategyChangeRate.setDay14_change_rate(rateMongths[1]);
				luStrategyChangeRate.setDay21_change_rate(rateMongths[2]);
				luStrategyChangeRate.setMonth1_change_rate(rateMongths[3]);
				luStrategyChangeRate.setMonth3_change_rate(rateMongths[4]);
				luStrategyChangeRate.setMonth6_change_rate(rateMongths[5]);
				luStrategyChangeRate.setYear1_change_rate(rateMongths[6]);
				luStrategyChangeRate.setYear2_change_rate(rateMongths[7]);
				luStrategyChangeRate.setYear3_change_rate(rateMongths[8]);
				luStrategyChangeRate.setYear4_change_rate(rateMongths[9]);
				luStrategyChangeRate.setYear5_change_rate(rateMongths[10]);
				luStrategyChangeRate.setYear6_change_rate(rateMongths[11]);
				luStrategyChangeRate.setYear7_change_rate(rateMongths[12]);
				luStrategyChangeRate.setYear8_change_rate(rateMongths[13]);
				luStrategyChangeRate.setYear9_change_rate(rateMongths[14]);
				luStrategyChangeRate.setYear10_change_rate(rateMongths[15]);
				luStrategyChangeRateMapper.insert(luStrategyChangeRate);
				
				logger.info(luStrategy.getId()+":"+codes.size());
				for(int i=0;i<codes.size();i++){
					String code=codes.get(i);
					Stockinfo stock=stockinfoMapper.findStockinfo(code);
					StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
					StockCompanyHangye stockCompanyHangye=stockCompanyHangyeMapper.findCode(code);
					StockDay stockDay=stockDayMapper.findLast(code);
					
					double zongshizhi=stockCompanySummary.getZongshizhi()==null?0:stockCompanySummary.getZongshizhi();
					double shiyinglvttm=stockCompanySummary==null||stockCompanySummary.getShiyinglvttm()==null?0:stockCompanySummary.getShiyinglvttm();
					double jingzichanshouyilv=stockCompanySummary==null||stockCompanySummary.getJingzichanshouyilv()==null?0:stockCompanySummary.getJingzichanshouyilv();
					
					String level1=stockCompanyHangye==null||stockCompanyHangye.getLevel1()==null?"":stockCompanyHangye.getLevel1();
					String level2=stockCompanyHangye==null||stockCompanyHangye.getLevel2()==null?"":stockCompanyHangye.getLevel2();
					String level3=stockCompanyHangye==null||stockCompanyHangye.getLevel3()==null?"":stockCompanyHangye.getLevel3();
					
					double close_price=stockDay==null||stockDay.getClose_price()==null?0:stockDay.getClose_price();
					
					
					logger.info(stock.getCode()+"="+stock.getName()+"	"+",市盈率="+shiyinglvttm+",	净收="+jingzichanshouyilv
					+",	总市值:"+NumUtil.format(zongshizhi/100000000,0)+",	行业:"+level1+"	,"+level2+"	,"
							+level3+",	价格:"+close_price);
				}
				
				luStrategy.setRun_status(1);
				luStrategyMapper.updateLuStrategy(luStrategy);
			}
			
			if(runStatusId!=null){
				runStatusMapper.update(runStatusId, 1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]id="+id);
	}

}
