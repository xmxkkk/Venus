package venus.task.collect;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockinfoMapper;
import venus.dao.TaskUpdateMapper;
import venus.helper.middle.Count;
import venus.helper.middle.TaskExecTimeMiddle;
import venus.helper.middle.TaskUpdateMiddle;
import venus.helper.util.CommonUtil;
import venus.helper.util.Constant;
import venus.model.dao.TaskUpdate;

@Component
public class DayTask {
	Logger logger=Logger.getLogger(DayTask.class);
	@Autowired StockinfoTask stockinfoTask;
	@Autowired IndexDayTask indexDayTask;
	@Autowired StockDayFuTask stockDayFuTask;
	@Autowired StockDayTask stockDayTask;
	@Autowired TradeDayTask tradeDayTask;
	@Autowired TaskUpdateMapper taskUpdateMapper;
	@Autowired TaskUpdateMiddle taskUpdateMiddle;
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired CheckTask checkTask;
//	@Autowired HangyeChangeRateTask hangyeChangeRateTask;
	@Autowired TaskExecTimeMiddle taskExecTimeMiddle;
	public void init(){
		logger.info("[start]");
		try{
			String idInit=taskExecTimeMiddle.start("dayTask.init");
			
			String id=taskExecTimeMiddle.start("stockinfoTask.init");
			stockinfoTask.init();
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("indexDayTask.init");
			final Count count=new Count();
			count.init(indexDayTask.threadNum);
			for (int i = 0; i < indexDayTask.threadNum; i++) {
				final int threadId = i;
				new Thread() {
					public void run() {
						indexDayTask.init(threadId,true,true);
						synchronized (count) {
							count.reduce();
						}
					}
				}.start();
			}
			CommonUtil.wait2000(count);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("tradeDayTask.init");
			tradeDayTask.init();
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("tradeDayTask.updateChange");
			indexDayTask.updateChange(0, null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockDayFuTask.init");
			final Count count2=new Count();
			count2.init(stockDayFuTask.threadNum);
			for (int i = 0; i < stockDayFuTask.threadNum; i++) {
				final int threadId = i;
				new Thread() {
					public void run() {
						stockDayFuTask.init(threadId,true,true,null);
						synchronized (count2) {
							count2.reduce();
						}
					}
				}.start();
			}
			CommonUtil.wait2000(count2);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockDayFuTask.updateChange");
			stockDayFuTask.updateChange(0,null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockinfoTask.updateWeight");
			stockinfoTask.updateWeight(null);
			taskExecTimeMiddle.end(id);
			
//			id=taskExecTimeMiddle.start("hangyeChangeRateTask.init");
//			hangyeChangeRateTask.init();
//			taskExecTimeMiddle.end(id);
			
			
			id=taskExecTimeMiddle.start("stockDayTask.init");
			final Count count3=new Count();
			count3.init(stockDayTask.threadNum);
			for (int i = 0; i < stockDayTask.threadNum; i++) {
				final int threadId = i;
				new Thread() {
					public void run() {
						stockDayTask.init(threadId,true,true,null);
						synchronized (count3) {
							count3.reduce();
						}
					}
				}.start();
			}
			CommonUtil.wait2000(count3);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockDayTask.updateChange");
			stockDayTask.updateChange(0,null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockinfoTask.updateTradeDays");
			stockinfoTask.updateTradeDays(null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockinfoTask.updateBeta");
			stockinfoTask.updateBeta(null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockinfoTask.updateFirstTradeDay");
			stockinfoTask.updateFirstTradeDay(null);
			taskExecTimeMiddle.end(id);
				
//			id=taskExecTimeMiddle.start("checkTask.init");
//			checkTask.init();
//			taskExecTimeMiddle.end(id);
			
			
			taskExecTimeMiddle.end(idInit);
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]");
	}
	public void initAll(){
		logger.info("[start]DayTask.initAll:");
		try{
			//stockDay初始化
			TaskUpdate taskUpdate=taskUpdateMapper.findExist("StockDayFuTask.initAll");
			if(taskUpdate==null){
				stockinfoMapper.updateAllFlag(0);
				final Count count=new Count();
				count.init(stockDayFuTask.threadNum);
				
				for (int i = 0; i < stockDayFuTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockDayFuTask.init(threadId,false,false,null);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				stockDayFuTask.updateChange(0,null);
				stockinfoTask.updateWeight(null);
				
//				hangyeChangeRateTask.init();
				
				taskUpdateMiddle.update("StockDayFuTask.initAll", Constant.TIME$SECOND$30DAY);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockDayTask.initAll");
			if(taskUpdate==null){
				stockinfoMapper.updateAllFlag(0);
				final Count count=new Count();
				count.init(stockDayTask.threadNum);
				
				for (int i = 0; i < stockDayTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockDayTask.init(threadId,false,false,null);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				stockDayTask.updateChange(0,null);
				stockinfoTask.updateTradeDays(null);
				stockinfoTask.updateBeta(null);
				stockinfoTask.updateFirstTradeDay(null);
				taskUpdateMiddle.update("StockDayTask.initAll", Constant.TIME$SECOND$30DAY);
			}
			
			taskUpdate=taskUpdateMapper.findExist("IndexDayTask.initAll");
			if(taskUpdate==null){
				final Count count=new Count();
				count.init(indexDayTask.threadNum);
				
				for (int i = 0; i < indexDayTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							indexDayTask.init(threadId,false,false);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("IndexDayTask.initAll", Constant.TIME$SECOND$1DAY);
			}
			
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]");
	}
}
