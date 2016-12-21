package venus.task.collect;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.TaskUpdateMapper;
import venus.helper.middle.Count;
import venus.helper.middle.TaskExecTimeMiddle;
import venus.helper.middle.TaskUpdateMiddle;
import venus.helper.util.CommonUtil;
import venus.helper.util.Constant;
import venus.model.dao.TaskUpdate;

@Component
public class StockCompanyTask {
	Logger logger=Logger.getLogger(StockCompanyTask.class);
	@Autowired StockCompanyEventTask stockCompanyEventTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/event/"
	@Autowired StockCompanyFinanceTask stockCompanyFinanceTask;//"http://stockpage.10jqka.com.cn/basic/" + stock.getCode() + "/"+dataType+".txt"
	@Autowired StockCompanyHangyeDataTask stockCompanyHangyeDataTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/field/"
	@Autowired StockCompanyHangyeTask stockCompanyHangyeTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/field/"
	@Autowired StockCompanyHolderCansellTask stockCompanyHolderCansellTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/holder/"
	@Autowired StockCompanyHolderNumberTask stockCompanyHolderNumberTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/holder/"
	@Autowired StockCompanyHolderTopTask stockCompanyHolderTopTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/holder/"
	@Autowired StockCompanyHolderChangeTask stockCompanyHolderChangeTask;
	@Autowired StockCompanyHolderStructTask stockCompanyHolderStructTask;
	@Autowired StockCompanyHolderOrgTask stockCompanyHolderOrgTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/position/"
	@Autowired StockCompanyHoldingTask stockCompanyHoldingTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/company/"
	@Autowired StockCompanyInfoTask stockCompanyInfoTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/company/"
	@Autowired StockCompanySummaryTask stockCompanySummaryTask;//"http://hq.sinajs.cn/?list="+StringUtil.toMarketName(stock)
	@Autowired StockCompanyJingyingTask stockCompanyJingyingTask;//"http://stockpage.10jqka.com.cn/"+stock.getCode()+"/operate/"
	@Autowired TaskUpdateMapper taskUpdateMapper;
	@Autowired TaskUpdateMiddle taskUpdateMiddle;
	@Autowired TaskExecTimeMiddle taskExecTimeMiddle;
	public void init(){
		logger.info("[start]");
		try{
			String idInit=taskExecTimeMiddle.start("stockCompanyTask.init");
			
			String id=null;
			TaskUpdate taskUpdate=null;
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHoldingTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHoldingTask.init");
				
				final Count count=new Count();
				count.init(stockCompanyHoldingTask.threadNum);
				for (int i = 0; i < stockCompanyHoldingTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHoldingTask.init(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHoldingTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyInfoTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyInfoTask.initCache");
				
				final Count count=new Count();
				count.init(stockCompanyInfoTask.threadNum);
				for (int i = 0; i < stockCompanyInfoTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyInfoTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyInfoTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanySummaryTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanySummaryTask.init");
			
				final Count count=new Count();
				count.init(stockCompanySummaryTask.threadNum);
				for (int i = 0; i < stockCompanySummaryTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanySummaryTask.init(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanySummaryTask.init",Constant.TIME$SECOND$1DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyEventTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyEventTask.init");
				final Count count=new Count();
				count.init(stockCompanyEventTask.threadNum);
				for (int i = 0; i < stockCompanyEventTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyEventTask.init(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyEventTask.init",Constant.TIME$SECOND$1DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyFinanceTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyFinanceTask.init");
				final Count count=new Count();
				count.init(stockCompanyFinanceTask.threadNum);
				for (int i = 0; i < stockCompanyFinanceTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyFinanceTask.init(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				
				taskUpdateMiddle.update("StockCompanyFinanceTask.init",Constant.TIME$SECOND$7DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHangyeDataTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHangyeDataTask.init");
				final Count count=new Count();
				count.init(stockCompanyHangyeDataTask.threadNum);
				for (int i = 0; i < stockCompanyHangyeDataTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHangyeDataTask.init(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHangyeDataTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHangyeTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHangyeTask.initCache");
				
				final Count count=new Count();
				count.init(stockCompanyHangyeTask.threadNum);
				for (int i = 0; i < stockCompanyHangyeTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHangyeTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				
				taskUpdateMiddle.update("StockCompanyHangyeTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderCansellTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderCansellTask.init");
				
				final Count count=new Count();
				count.init(stockCompanyHolderCansellTask.threadNum);
				for (int i = 0; i < stockCompanyHolderCansellTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderCansellTask.init(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				
				taskUpdateMiddle.update("StockCompanyHolderCansellTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderNumberTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderNumberTask.initCache");
				
				final Count count=new Count();
				count.init(stockCompanyHolderNumberTask.threadNum);
				for (int i = 0; i < stockCompanyHolderNumberTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderNumberTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderNumberTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderTopTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderTopTask.initCache");
				
				final Count count=new Count();
				count.init(stockCompanyHolderTopTask.threadNum);
				for (int i = 0; i < stockCompanyHolderTopTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderTopTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderTopTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderChangeTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderChangeTask.initCache");
				
				final Count count=new Count();
				count.init(stockCompanyHolderChangeTask.threadNum);
				for (int i = 0; i < stockCompanyHolderChangeTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderChangeTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderChangeTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderStructTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderStructTask.initCache");
				
				final Count count=new Count();
				count.init(stockCompanyHolderStructTask.threadNum);
				for (int i = 0; i < stockCompanyHolderStructTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderStructTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderStructTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderOrgTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderOrgTask.init");
				
				final Count count=new Count();
				count.init(stockCompanyHolderOrgTask.threadNum);
				for (int i = 0; i < stockCompanyHolderOrgTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderOrgTask.init(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderOrgTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}

			taskUpdate=taskUpdateMapper.findExist("StockCompanyJingyingTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyJingyingTask.init");
				
				final Count count=new Count();
				count.init(stockCompanyJingyingTask.threadNum);
				for (int i = 0; i < stockCompanyJingyingTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyJingyingTask.init(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyJingyingTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			
			taskExecTimeMiddle.end(idInit);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
	public void initCache(){
		logger.info("[start]");
		try{
			String idCache=taskExecTimeMiddle.start("stockCompanyTask.initCache");
			
			String id=null;
			TaskUpdate taskUpdate=null;
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHoldingTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHoldingTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHoldingTask.threadNum);
				for (int i = 0; i < stockCompanyHoldingTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHoldingTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHoldingTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyInfoTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyInfoTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyInfoTask.threadNum);
				for (int i = 0; i < stockCompanyInfoTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyInfoTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyInfoTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanySummaryTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanySummaryTask.initCache");
				final Count count=new Count();
				count.init(stockCompanySummaryTask.threadNum);
				for (int i = 0; i < stockCompanySummaryTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanySummaryTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanySummaryTask.init",Constant.TIME$SECOND$1DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyEventTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyEventTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyEventTask.threadNum);
				for (int i = 0; i < stockCompanyEventTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyEventTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyEventTask.init",Constant.TIME$SECOND$1DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyFinanceTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyFinanceTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyFinanceTask.threadNum);
				for (int i = 0; i < stockCompanyFinanceTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyFinanceTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyFinanceTask.init",Constant.TIME$SECOND$7DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHangyeDataTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHangyeDataTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHangyeDataTask.threadNum);
				for (int i = 0; i < stockCompanyHangyeDataTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHangyeDataTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHangyeDataTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHangyeTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHangyeTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHangyeTask.threadNum);
				for (int i = 0; i < stockCompanyHangyeTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHangyeTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHangyeTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderCansellTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderCansellTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderCansellTask.threadNum);
				for (int i = 0; i < stockCompanyHolderCansellTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderCansellTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderCansellTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderNumberTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderNumberTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderNumberTask.threadNum);
				for (int i = 0; i < stockCompanyHolderNumberTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderNumberTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderNumberTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderTopTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderTopTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderTopTask.threadNum);
				for (int i = 0; i < stockCompanyHolderTopTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderTopTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderTopTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderChangeTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderChangeTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderChangeTask.threadNum);
				for (int i = 0; i < stockCompanyHolderChangeTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderChangeTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderChangeTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderStructTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderStructTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderStructTask.threadNum);
				for (int i = 0; i < stockCompanyHolderStructTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderStructTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderStructTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderOrgTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderOrgTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderOrgTask.threadNum);
				for (int i = 0; i < stockCompanyHolderOrgTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderOrgTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyHolderOrgTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyJingyingTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyJingyingTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyJingyingTask.threadNum);
				for (int i = 0; i < stockCompanyJingyingTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyJingyingTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskUpdateMiddle.update("StockCompanyJingyingTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskExecTimeMiddle.end(idCache);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
	public void initForce(){
		logger.info("[start]");
		try{
			String idForce=taskExecTimeMiddle.start("stockCompanyTask.initForce");
			
			String id=null;
			
			{
				id=taskExecTimeMiddle.start("stockCompanyHoldingTask.init");
				final Count count=new Count();
				count.init(stockCompanyHoldingTask.threadNum);
				for (int i = 0; i < stockCompanyHoldingTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHoldingTask.init(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			{
				id=taskExecTimeMiddle.start("stockCompanyInfoTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyInfoTask.threadNum);
				for (int i = 0; i < stockCompanyInfoTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyInfoTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			{
				id=taskExecTimeMiddle.start("stockCompanySummaryTask.init");
				final Count count=new Count();
				count.init(stockCompanySummaryTask.threadNum);
				for (int i = 0; i < stockCompanySummaryTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanySummaryTask.init(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			
			{
				id=taskExecTimeMiddle.start("stockCompanyEventTask.init");
				final Count count=new Count();
				count.init(stockCompanyEventTask.threadNum);
				for (int i = 0; i < stockCompanyEventTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyEventTask.init(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			{
				id=taskExecTimeMiddle.start("stockCompanyFinanceTask.init");
				final Count count=new Count();
				count.init(stockCompanyFinanceTask.threadNum);
				for (int i = 0; i < stockCompanyFinanceTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyFinanceTask.init(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			{
				id=taskExecTimeMiddle.start("stockCompanyHangyeDataTask.init");
				final Count count=new Count();
				count.init(stockCompanyHangyeDataTask.threadNum);
				for (int i = 0; i < stockCompanyHangyeDataTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHangyeDataTask.init(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			{
				id=taskExecTimeMiddle.start("stockCompanyHangyeTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHangyeTask.threadNum);
				for (int i = 0; i < stockCompanyHangyeTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHangyeTask.initCache(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			{
				id=taskExecTimeMiddle.start("stockCompanyHolderCansellTask.init");
				final Count count=new Count();
				count.init(stockCompanyHolderCansellTask.threadNum);
				for (int i = 0; i < stockCompanyHolderCansellTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderCansellTask.init(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			{
				id=taskExecTimeMiddle.start("stockCompanyHolderNumberTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderNumberTask.threadNum);
				for (int i = 0; i < stockCompanyHolderNumberTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderNumberTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			{
				id=taskExecTimeMiddle.start("stockCompanyHolderTopTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderTopTask.threadNum);
				for (int i = 0; i < stockCompanyHolderTopTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderTopTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			
			{
				id=taskExecTimeMiddle.start("stockCompanyHolderChangeTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderChangeTask.threadNum);
				for (int i = 0; i < stockCompanyHolderChangeTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderChangeTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			{
				id=taskExecTimeMiddle.start("stockCompanyHolderStructTask.initCache");
				final Count count=new Count();
				count.init(stockCompanyHolderStructTask.threadNum);
				for (int i = 0; i < stockCompanyHolderStructTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderStructTask.initCache(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			
			{
				id=taskExecTimeMiddle.start("stockCompanyHolderOrgTask.init");
				final Count count=new Count();
				count.init(stockCompanyHolderOrgTask.threadNum);
				for (int i = 0; i < stockCompanyHolderOrgTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyHolderOrgTask.init(threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			
			{
				id=taskExecTimeMiddle.start("stockCompanyJingyingTask.init");
				final Count count=new Count();
				count.init(stockCompanyJingyingTask.threadNum);
				for (int i = 0; i < stockCompanyJingyingTask.threadNum; i++) {
					final int threadId = i;
					new Thread() {
						public void run() {
							stockCompanyJingyingTask.init(null,threadId);
							synchronized (count) {
								count.reduce();
							}
						}
					}.start();
				}
				CommonUtil.wait2000(count);
				taskExecTimeMiddle.end(id);
			}
			
			taskExecTimeMiddle.end(idForce);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
}
