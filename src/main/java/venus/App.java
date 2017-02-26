package venus;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import venus.helper.middle.AlertMiddle;
import venus.helper.middle.Count;
import venus.helper.property.CommandProperty;
import venus.helper.util.CommonUtil;
import venus.helper.util.URLUtil;
import venus.task.analyse.LuStrategyTask;
import venus.task.collect.CheckTask;
import venus.task.collect.DayTask;
import venus.task.collect.StockCompanyHolderChangeTask;
import venus.task.collect.StockCompanyTask;

@SpringBootApplication
public class App {
	static Logger logger=Logger.getLogger(App.class);
	public static void main(String[] args) {
		final ConfigurableApplicationContext cxt = SpringApplication.run(App.class, args);
		
//		StockCompanyInfoTask stockCompanyInfoTask=cxt.getBean(StockCompanyInfoTask.class);
//		stockCompanyInfoTask.init(null);
		
//		StockCompanySummaryTask stockCompanySummaryTask=cxt.getBean(StockCompanySummaryTask.class);
//		stockCompanySummaryTask.init(null);
		
//		StockCompanyFinanceAnalyse stockCompanyFinanceAnalyse=cxt.getBean(StockCompanyFinanceAnalyse.class);
//		stockCompanyFinanceAnalyse.init();
		
//		StockCompanyHolderStructTask stockCompanyHolderStructTask=cxt.getBean(StockCompanyHolderStructTask.class);
//		stockCompanyHolderStructTask.initCache();
		
		
//		final StockCompanySummaryTask stockCompanySummaryTask=cxt.getBean(StockCompanySummaryTask.class);
//		stockCompanySummaryTask.updateAllOther(null,false);
		
		/*
		final Count count=new Count();
		count.init(stockCompanySummaryTask.threadNum);
		for (int i = 0; i < stockCompanySummaryTask.threadNum; i++) {
			final int threadId = i;
			new Thread() {
				public void run() {
					stockCompanySummaryTask.updateAllOther(null,threadId);
					synchronized (count) {
						count.reduce();
					}
				}
			}.start();
		}
		CommonUtil.wait2000(count);*/
		
//		StockCompanyFinanceTask stockCompanyFinanceTask=cxt.getBean(StockCompanyFinanceTask.class);
//		stockCompanyFinanceTask.init("002821", 0);
		
//		final StockCompanyHolderChangeTask stockCompanyHolderChangeTask=cxt.getBean(StockCompanyHolderChangeTask.class);
//		final Count count=new Count();
//		count.init(stockCompanyHolderChangeTask.threadNum);
//		for (int i = 0; i < stockCompanyHolderChangeTask.threadNum; i++) {
//			final int threadId = i;
//			new Thread() {
//				public void run() {
//					stockCompanyHolderChangeTask.init(threadId);
//					synchronized (count) {
//						count.reduce();
//					}
//				}
//			}.start();
//		}
//		CommonUtil.wait2000(count);
		
		
		
		CommandProperty startProperty=cxt.getBean(CommandProperty.class);
		String startDtCommand=startProperty.getCommandDt().trim();
		if(!startDtCommand.equals("none")&&startDtCommand.equals("start")){
			DayTask dayTask=cxt.getBean(DayTask.class);
			dayTask.init();
		}
		
		String startSctCommand=startProperty.getCommandSct().trim();
		if(!startSctCommand.equals("none")&&startSctCommand.equals("start")){
			String startSctCache=startProperty.getCommandSctCache();
			String startSctForce=startProperty.getCommandSctForce();
			
			StockCompanyTask stockCompanyTask=cxt.getBean(StockCompanyTask.class);
			
			if(startSctForce.equals("true")){
				stockCompanyTask.initForce();
			}else if(startSctCache.equals("true")){
				stockCompanyTask.initCache();
			}else if(startSctCache.equals("false")){
				stockCompanyTask.init();
			}
		}
		
		String startCtCommand=startProperty.getCommandCt().trim();
		if(!startCtCommand.equals("none")&&startCtCommand.equals("start")){
			CheckTask checkTask=cxt.getBean(CheckTask.class);
			checkTask.init();
		}
		
		String startLstCommand=startProperty.getCommandLst().trim();
		if(!startLstCommand.equals("none")&&startLstCommand.equals("start")){
			LuStrategyTask luStrategyTask=cxt.getBean(LuStrategyTask.class);
			int commandLstId=startProperty.getCommandLstId();
			String commandLstJson=startProperty.getCommandLstJson();
			String force=startProperty.getCommandLstForce();
			boolean forceParam=false;
			if(force.equals("true")){
				forceParam=true;
			}
			luStrategyTask.init(commandLstId,commandLstJson,forceParam);
		}
		
		if(URLUtil.errorNum>100){
			AlertMiddle alertMiddle=cxt.getBean(AlertMiddle.class);
			alertMiddle.init("抓取数据错误提醒邮件","抓取错误过多"); 
		}
		
		String startShutdownCommand=startProperty.getCommandShutdown().trim();
		if(!startShutdownCommand.equals("none")&&startShutdownCommand.equals("start")){
			CommonUtil.shutdown();
		}
		
		
//		StockCompanySummaryTask stockCompanySummaryTask=cxt.getBean(StockCompanySummaryTask.class);
//		stockCompanySummaryTask.init();
//		stockCompanySummaryTask.initOther();
		
//		StockCompanyFinanceTask stockCompanyFinanceTask=cxt.getBean(StockCompanyFinanceTask.class);
//		stockCompanyFinanceTask.init();
		
//		StockCompanyHolderNumberTask stockCompanyHolderNumberTask=cxt.getBean(StockCompanyHolderNumberTask.class);
//		stockCompanyHolderNumberTask.init();
		
//		StockCompanyHolderTopTask stockCompanyHolderTopTask=cxt.getBean(StockCompanyHolderTopTask.class);
//		stockCompanyHolderTopTask.init();
		
//		StockCompanyHolderCansellTask stockCompanyHolderCansellTask=cxt.getBean(StockCompanyHolderCansellTask.class);
//		stockCompanyHolderCansellTask.init();
		
//		StockCompanyHolderOrgTask stockCompanyHolderOrgTask = cxt.getBean(StockCompanyHolderOrgTask.class);
//		stockCompanyHolderOrgTask.init();
		
//		StockCompanyHangyeTask stockCompanyHangyeTask=cxt.getBean(StockCompanyHangyeTask.class);
//		stockCompanyHangyeTask.init();
		
//		StockCompanyHangyeDataTask stockCompanyHangyeDataTask=cxt.getBean(StockCompanyHangyeDataTask.class);
//		stockCompanyHangyeDataTask.init();
		
//		StockCompanyHoldingTask stockCompanyHoldingTask =cxt.getBean(StockCompanyHoldingTask.class);
//		stockCompanyHoldingTask.init();
		
//		StockCompanyInfoTask stockCompanyInfoTask=cxt.getBean(StockCompanyInfoTask.class);
//		stockCompanyInfoTask.init();
		
//		StockCompanyEventTask stockCompanyEventTask=cxt.getBean(StockCompanyEventTask.class);
//		stockCompanyEventTask.init();
		
//		LuStrategyTask luStrategyTask=cxt.getBean(LuStrategyTask.class);
//		luStrategyTask.init();
		
//		LuStrategyTestTask luStrategyTestTask=cxt.getBean(LuStrategyTestTask.class);
//		luStrategyTestTask.init();
		
//		ShiyinglvUnitScore shiyinglvUnitScore=cxt.getBean(ShiyinglvUnitScore.class);
//		shiyinglvUnitScore.getScore("000001");
		
		/*
		final StrategySimulation simulation=cxt.getBean(StrategySimulation.class);
		RandomChooseStock randomChooseStock=cxt.getBean(RandomChooseStock.class);
		
		//0.4#0.8
		final List<Stockinfo> list=randomChooseStock.randomChooseTime("1", 20,"20130101");
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30); 
		for(int i=0;i<list.size();i++){
			final String code=list.get(i).getCode();
			fixedThreadPool.execute(new Runnable() {
				public void run() {
					String strategy_class="stockStrategy011";
					StrategyResult result=simulation.simulate(code, strategy_class, "20000101", "20160101", "0.4#0.4");
					System.out.println(result);
				}
			});
		}
		fixedThreadPool.shutdown();
		*/
		/*
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30); 
		
		for(double i=0.1;i<=1.0;i+=0.1){
			for(double j=0.1;j<=1.0;j+=0.1){
				final double ii=i;
				final double jj=j;
				fixedThreadPool.execute(new Runnable() {
					public void run() {
						String code="600000";
						String strategy_class="stockStrategy011";
						StrategyResult result=simulation.simulate(code, strategy_class, "20000101", "20160101", ii+"#"+jj);
						System.out.println(result);
					}
				});
			}
		}
		fixedThreadPool.shutdown();*/
		
		/*
		RandomChooseStock randomChooseStock=cxt.getBean(RandomChooseStock.class);
		final List<Stockinfo> list=randomChooseStock.randomChooseTime("1", 200,"20130101");
		
		for(int i=0;i<list.size();i++){
			String code=list.get(i).getCode();
			String strategy_class="stockStrategy001";
			StrategyResult result=simulation.simulate(code, strategy_class, "20130101", "20160819", "30#20");
			System.out.println(result);
		}*/
		
		
		//30#20
		
		/*
		RandomChooseStock randomChooseStock=cxt.getBean(RandomChooseStock.class);
		final List<Stockinfo> list=randomChooseStock.randomChooseTime("5", 20,"20000101");
		
		final StrategySimulation simulation=cxt.getBean(StrategySimulation.class);
		
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30); 
		
		for(int i=20;i<=90;i+=10){
			for(int j=20;j<=90;j+=10){
				final int xx=i;
				final int yy=j;
				fixedThreadPool.execute(new Runnable() {
					public void run() {
						for(int x=0;x<list.size();x++){
							String code=list.get(x).getCode();
							String strategy_class="stockStrategy001";
							simulation.simulate(code, strategy_class, "20000101", "20160101", xx+"#"+yy);
							System.out.println("==============="+code+"=================");
						}
					}
				});
			}
		}
		fixedThreadPool.shutdown();*/
		

		//50#30#30#80
//		simulation.simulate("000001", "stockStrategy004", "20150101", "20160101", "50#30#30#80");
//		simulation.simulate("000002", "stockStrategy004", "20150101", "20160101", "50#30#30#80");
		
//		simulation.simulate("000001", "stockStrategy004", "20160101", "20160823", "50#30#30#80");
//		simulation.simulate("000002", "stockStrategy004", "20160101", "20160823", "50#30#30#80");
		
		/*
		for(int i=20;i<=90;i+=10){
			for(int j=20;j<=90;j+=10){
				for(int m=20;m<=90;m+=10){
					for(int n=20;n<=90;n+=10){
						String code="600000";
						String strategy_class="stockStrategy004";
						simulation.simulate(code, strategy_class, "20150101", "20160101", i+"#"+j+"#"+m+"#"+n);
					}
				}
			}
		}*/
		
		/*
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30); 
		for(int i=2;i<=15;i+=1){
			for(int j=2;j<=15;j+=1){
				final int ii=i;
				final int jj=j;
				fixedThreadPool.execute(new Runnable() {
					public void run() {
						String code="600000";
						String strategy_class="stockStrategy002";
						for(int m=2000;m<2016;m++){
							simulation.simulate(code, strategy_class, m+"0101", (m+1)+"0101", ii+"#"+jj);
							simulation.simulate(code, strategy_class, m+"0101", (m+1)+"0101", ii+"#"+jj);
						}
						
						simulation.simulate(code, strategy_class, "20000101", "20160101", ii+"#"+jj);
						simulation.simulate(code, strategy_class, "20000101", "20160101", ii+"#"+jj);
						
						code="000001";
						for(int m=2000;m<2016;m++){
							simulation.simulate(code, strategy_class, m+"0101", (m+1)+"0101", ii+"#"+jj);
							simulation.simulate(code, strategy_class, m+"0101", (m+1)+"0101", ii+"#"+jj);
						}
						
						simulation.simulate(code, strategy_class, "20000101", "20160101", ii+"#"+jj);
						simulation.simulate(code, strategy_class, "20000101", "20160101", ii+"#"+jj);
						
						code="000002";
						for(int m=2000;m<2016;m++){
							simulation.simulate(code, strategy_class, m+"0101", (m+1)+"0101", ii+"#"+jj);
							simulation.simulate(code, strategy_class, m+"0101", (m+1)+"0101", ii+"#"+jj);
						}
						
						simulation.simulate(code, strategy_class, "20000101", "20160101", ii+"#"+jj);
						simulation.simulate(code, strategy_class, "20000101", "20160101", ii+"#"+jj);
						
					}
				});
			}
		}
		fixedThreadPool.shutdown();
		*/
/*
		String code="600000";
		for(int i=2000;i<2016;i++){
			simulation.simulate(code, "stockStrategy010", i+"0101", (i+1)+"0101", "1");
			simulation.simulate(code, "stockStrategy010", i+"0101", (i+1)+"0101", "2");
		}
		
		simulation.simulate(code, "stockStrategy010", "20000101", "20160101", "1");
		simulation.simulate(code, "stockStrategy010", "20000101", "20160101", "2");
		
		code="000001";
		for(int i=2000;i<2016;i++){
			simulation.simulate(code, "stockStrategy010", i+"0101", (i+1)+"0101", "1");
			simulation.simulate(code, "stockStrategy010", i+"0101", (i+1)+"0101", "2");
		}
		
		simulation.simulate(code, "stockStrategy010", "20000101", "20160101", "1");
		simulation.simulate(code, "stockStrategy010", "20000101", "20160101", "2");
		
		code="000002";
		for(int i=2000;i<2016;i++){
			simulation.simulate(code, "stockStrategy010", i+"0101", (i+1)+"0101", "1");
			simulation.simulate(code, "stockStrategy010", i+"0101", (i+1)+"0101", "2");
		}
		
		simulation.simulate(code, "stockStrategy010", "20000101", "20160101", "1");
		simulation.simulate(code, "stockStrategy010", "20000101", "20160101", "2");
		*/
		
		
		
		
		
		
//		DayTask dayTask=cxt.getBean(DayTask.class);
//		dayTask.init();
		
//		System.out.println(new File("D:\\storage\\vip.stock.finance.sina.com.cn\\000048AC\\2E3A70FE\\24DA3926\\FE253410\\000048AC2E3A70FE24DA3926FE253410").getAbsolutePath());
		
		/*
		final StockDayTask stockDayTask=cxt.getBean(StockDayTask.class);
		final TaskUpdateMapper taskUpdateMapper=cxt.getBean(TaskUpdateMapper.class);
		final TaskUpdateTask taskUpdateTask=cxt.getBean(TaskUpdateTask.class);
		
		final TaskUpdate taskUpdate=taskUpdateMapper.findExist("StockDayTask.updateChangeAll");
		if(taskUpdate==null){
			final Count count=new Count();
			count.init(stockDayTask.threadNum);
			
			for (int i = 0; i < stockDayTask.threadNum; i++) {
				final int threadId = i;
				new Thread() {
					public void run() {
						stockDayTask.updateChangeAll(threadId);
						synchronized (count) {
							count.reduce();
						}
					}
				}.start();
			}
			while(true){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(count.count()==0){
					break;
				}
			}
			taskUpdateTask.update("StockDayTask.updateChangeAll", 24*60*60*30);
		}*/
		
//		DayTask dayTask=cxt.getBean(DayTask.class);
//		dayTask.init();
		
		
		/*
		final StrategySimulation strategySimulation=cxt.getBean(StrategySimulation.class);
		strategySimulation.clearInvalidSimulation();
		
//		60#40
		
		
		strategySimulation.simulate("300003", "stockStrategy001", "20100101", "20160823", "63#41");
		strategySimulation.simulate("300003", "stockStrategy001", "20110101", "20160823", "63#41");
		strategySimulation.simulate("300003", "stockStrategy001", "20120101", "20160823", "63#41");
		strategySimulation.simulate("300003", "stockStrategy001", "20130101", "20160823", "63#41");
		strategySimulation.simulate("300003", "stockStrategy001", "20140101", "20160823", "63#41");
		strategySimulation.simulate("300003", "stockStrategy001", "20150101", "20160823", "63#41");*/
		
		
		/*
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30); 
		for(int i=10;i<=120;i+=1){
			for(int j=10;j<=120;j+=1){
				final int ii=i;
				final int jj=j;
				fixedThreadPool.execute(new Runnable() {
					public void run() {
						strategySimulation.simulate("300003", "stockStrategy001", "20150601", "20160823", ii+"#"+jj);
					}
				});
			}
		}
		fixedThreadPool.shutdown();*/
		//63#41
		/*
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30); 
		StockinfoMapper stockinfoMapper=cxt.getBean(StockinfoMapper.class);
		List<Stockinfo> stockinfos=stockinfoMapper.findStockinfos();
		for(int i=0;i<stockinfos.size();i++){
			final Stockinfo stockinfo=stockinfos.get(i);
			fixedThreadPool.execute(new Runnable() {
				public void run() {
					strategySimulation.simulate(stockinfo.getCode(), "stockStrategy001", "20000101", "20160823", "63#41");
				}
			});
		}
		fixedThreadPool.shutdown();*/
		/*
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30); 
		
		for(int i=90;i>=20;i-=3){
			for(int j=20;j<=90;j+=3){
				final int ii=i;
				final int jj=j;
				fixedThreadPool.execute(new Runnable() {
					public void run() {
						strategySimulation.simulate("600000", "stockStrategy001", "20000101", "20160823", ii+"#"+jj);
					}
				});
				
			}
		}
		fixedThreadPool.shutdown();*/
		
		/*
		final StockDayTask stockDayTask=cxt.getBean(StockDayTask.class);
		
		for(int i=0;i<30;i++){
			final int ii=i;
			new Thread(){
				public void run(){
					stockDayTask.updateChangeRateReal(30,ii);
				}
			}.start();
		}*/
		
		
		/*
		final StrategySimulation strategySimulation=cxt.getBean(StrategySimulation.class);
		strategySimulation.simulate("600005", "stockStrategy009", "20160101", "20160823", null);
		
		for(int i=2;i<=15;i++){
			for(int j=2;j<=15;j++){
				strategySimulation.simulate("600005", "stockStrategy002", "20160101", "20160823", i+"#"+j);
			}
		}*/
		
		
		
		
//		StepSimulation stepSimulation=cxt.getBean(StepSimulation.class);
//		stepSimulation.simulate("300003","20160101","20160823");
		
		
		
//		final StrategySimulation strategySimulation=cxt.getBean(StrategySimulation.class);
//		strategySimulation.clearInvalidSimulation();
		
		
//		TestMapper testMapper=cxt.getBean(TestMapper.class);
//		Integer xInteger=new Integer(0);
//		int id=testMapper.insert("x");
//		System.out.println(id+"/"+xInteger);
		
//		strategySimulation.simulate("300003", "stockStrategy001", "20160101", "20160823", "6#2");
		//2#2
		/*
		strategySimulation.simulate("600000", "stockStrategy002", "20150101", "20160101", "2#2");
		strategySimulation.simulate("600000", "stockStrategy002", "20140101", "20150101", "2#2");
		strategySimulation.simulate("600000", "stockStrategy002", "20130101", "20140101", "2#2");
		strategySimulation.simulate("600000", "stockStrategy002", "20120101", "20130101", "2#2");
		strategySimulation.simulate("600000", "stockStrategy002", "20110101", "20120101", "2#2");
		strategySimulation.simulate("600000", "stockStrategy002", "20100101", "20110101", "2#2");
		strategySimulation.simulate("600000", "stockStrategy002", "20090101", "20100101", "2#2");
		strategySimulation.simulate("600000", "stockStrategy002", "20080101", "20090101", "2#2");
		*/
		
		/*
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30); 

		StockinfoMapper stockinfoMapper=cxt.getBean(StockinfoMapper.class);
		List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
		
		for(int x=0;x<stocks.size();x++){
			final Stockinfo stock=stocks.get(x);
			for(int i=2;i<=20;i++){
				for(int j=2;j<=20;j++){
					final int ii=i;
					final int jj=j;
					fixedThreadPool.execute(new Runnable() {
						public void run() {
							strategySimulation.simulate(stock.getCode(), "stockStrategy002", "20160101", "20160823", ii+"#"+jj);
						}
					});
				}
			}
		}
		fixedThreadPool.shutdown();*/

	
	}
}
