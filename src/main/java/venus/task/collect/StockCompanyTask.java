package venus.task.collect;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.TaskUpdateMapper;
import venus.helper.middle.TaskExecTimeMiddle;
import venus.helper.middle.TaskUpdateMiddle;
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
				stockCompanyHoldingTask.init();
				taskUpdateMiddle.update("StockCompanyHoldingTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyInfoTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyInfoTask.initCache");
				stockCompanyInfoTask.initCache(null);
				taskUpdateMiddle.update("StockCompanyInfoTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanySummaryTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanySummaryTask.init");
				stockCompanySummaryTask.init(null);
				taskUpdateMiddle.update("StockCompanySummaryTask.init",Constant.TIME$SECOND$1DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyEventTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyEventTask.init");
				stockCompanyEventTask.init();
				taskUpdateMiddle.update("StockCompanyEventTask.init",Constant.TIME$SECOND$1DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyFinanceTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyFinanceTask.init");
				stockCompanyFinanceTask.init(null);
				taskUpdateMiddle.update("StockCompanyFinanceTask.init",Constant.TIME$SECOND$7DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHangyeDataTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHangyeDataTask.init");
				stockCompanyHangyeDataTask.init(null);
				taskUpdateMiddle.update("StockCompanyHangyeDataTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHangyeTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHangyeTask.initCache");
				stockCompanyHangyeTask.initCache(null);
				taskUpdateMiddle.update("StockCompanyHangyeTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderCansellTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderCansellTask.init");
				stockCompanyHolderCansellTask.init();
				taskUpdateMiddle.update("StockCompanyHolderCansellTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderNumberTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderNumberTask.initCache");
				stockCompanyHolderNumberTask.initCache();
				taskUpdateMiddle.update("StockCompanyHolderNumberTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderTopTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderTopTask.initCache");
				stockCompanyHolderTopTask.initCache();
				taskUpdateMiddle.update("StockCompanyHolderTopTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderOrgTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderOrgTask.init");
				stockCompanyHolderOrgTask.init();
				taskUpdateMiddle.update("StockCompanyHolderOrgTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}

			taskUpdate=taskUpdateMapper.findExist("StockCompanyJingyingTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyJingyingTask.init");
				stockCompanyJingyingTask.init(null);
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
				stockCompanyHoldingTask.initCache();
				taskUpdateMiddle.update("StockCompanyHoldingTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyInfoTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyInfoTask.initCache");
				stockCompanyInfoTask.initCache(null);
				taskUpdateMiddle.update("StockCompanyInfoTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanySummaryTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanySummaryTask.initCache");
				stockCompanySummaryTask.initCache(null);
				taskUpdateMiddle.update("StockCompanySummaryTask.init",Constant.TIME$SECOND$1DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyEventTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyEventTask.initCache");
				stockCompanyEventTask.initCache();
				taskUpdateMiddle.update("StockCompanyEventTask.init",Constant.TIME$SECOND$1DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyFinanceTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyFinanceTask.initCache");
				stockCompanyFinanceTask.initCache(null);
				taskUpdateMiddle.update("StockCompanyFinanceTask.init",Constant.TIME$SECOND$7DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHangyeDataTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHangyeDataTask.initCache");
				stockCompanyHangyeDataTask.initCache(null);
				taskUpdateMiddle.update("StockCompanyHangyeDataTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHangyeTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHangyeTask.initCache");
				stockCompanyHangyeTask.initCache(null);
				taskUpdateMiddle.update("StockCompanyHangyeTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderCansellTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderCansellTask.initCache");
				stockCompanyHolderCansellTask.initCache();
				taskUpdateMiddle.update("StockCompanyHolderCansellTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderNumberTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderNumberTask.initCache");
				stockCompanyHolderNumberTask.initCache();
				taskUpdateMiddle.update("StockCompanyHolderNumberTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderTopTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderTopTask.initCache");
				stockCompanyHolderTopTask.initCache();
				taskUpdateMiddle.update("StockCompanyHolderTopTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyHolderOrgTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyHolderOrgTask.initCache");
				stockCompanyHolderOrgTask.initCache();
				taskUpdateMiddle.update("StockCompanyHolderOrgTask.init",Constant.TIME$SECOND$3DAY);
				taskExecTimeMiddle.end(id);
			}
			
			taskUpdate=taskUpdateMapper.findExist("StockCompanyJingyingTask.init");
			if(taskUpdate==null){
				id=taskExecTimeMiddle.start("stockCompanyJingyingTask.initCache");
				stockCompanyJingyingTask.initCache(null);
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
			
			id=taskExecTimeMiddle.start("stockCompanyHoldingTask.init");
			stockCompanyHoldingTask.init();
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyInfoTask.initCache");
			stockCompanyInfoTask.initCache(null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanySummaryTask.init");
			stockCompanySummaryTask.init(null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyEventTask.init");
			stockCompanyEventTask.init();
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyFinanceTask.init");
			stockCompanyFinanceTask.init(null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyHangyeDataTask.init");
			stockCompanyHangyeDataTask.init(null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyHangyeTask.initCache");
			stockCompanyHangyeTask.initCache(null);
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyHolderCansellTask.init");
			stockCompanyHolderCansellTask.init();
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyHolderNumberTask.initCache");
			stockCompanyHolderNumberTask.initCache();
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyHolderTopTask.initCache");
			stockCompanyHolderTopTask.initCache();
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyHolderOrgTask.init");
			stockCompanyHolderOrgTask.init();
			taskExecTimeMiddle.end(id);
			
			id=taskExecTimeMiddle.start("stockCompanyJingyingTask.init");
			stockCompanyJingyingTask.init(null);
			taskExecTimeMiddle.end(id);
			
			taskExecTimeMiddle.end(idForce);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
}
