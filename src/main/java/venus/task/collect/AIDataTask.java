package venus.task.collect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceRateMapper;
import venus.dao.StockDayFuMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.NumUtil;
import venus.model.dao.StockCompanyFinanceRate;
import venus.model.dao.StockDayFu;
import venus.model.dao.Stockinfo;

@Component
public class AIDataTask {
	@Autowired
	StockDayFuMapper stockDayFuMapper;
	@Autowired
	StockinfoMapper stockinfoMapper;
	
	@Autowired
	StockCompanyFinanceRateMapper stockCompanyFinanceRateMapper;
	
	private List<Stockinfo> random_choose(List<Stockinfo> list,int num){
		List<Stockinfo> result=new ArrayList<Stockinfo>();
		while(result.size()<num){
			int rand=(int) Math.round(Math.random()*list.size());
			Stockinfo stock=list.get(rand);
			result.add(stock);
			list.remove(stock);
		}
		
		return result;
	}
	public void fin(){
		List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
		
//		stocks=random_choose(stocks, 100);
		
		String startTime="20140331";
		String endTime="20140425";
		
		String[] menus=new String[]{
				"净利润同比增长率",
				"扣非净利润",
				"每股资本公积金",
				"净资产收益率",
				"资产负债比率",
				"每股未分配利润",
				"营业总收入",
				"净资产收益率-摊薄",
				"净利润",
				"每股经营现金流",
				"营业总收入同比增长率",
				"每股净资产",
				"基本每股收益",
				"存货周转率",
				"每股收益同比增长率",
				"营业收入同比增长率",
				"净资产收益率同比增长率",
				"流动比率",
				"速动比率",
				"产权比率",
				"保守速动比率",
				"存货周转天数",
				"营业周期",
				"应收账款周转天数",
				"营业利润",
				"营业总成本",
				"利润总额",
				"所得税",
				"资产减值损失",
				"销售毛利率",
				"投资收益",
				"财务费用",
				"营业成本",
				"管理费用",
				"综合收益总额",
				"营业收入",
				"营业外支出",
				"营业外收入",
				"营业税金及附加",
				"归属于母公司股东的综合收益总额",
				"销售费用",
				"固定资产",
				"股东权益合计",
				"未分配利润",
				"无形资产",
				"货币资金",
				"归属于母公司股东权益合计",
				"盈余公积金",
				"资产总计",
				"负债合计",
				"应交税费",
				"股本",
				"资本公积金",
				"非流动资产合计",
				"流动负债合计",
				"其他应付款",
				"应收账款",
				"流动资产合计",
				"负债和股东权益总计",
				"其他应收款",
				"应付账款",
				"预收账款",
				"预付账款",
				"存货",
				"非流动负债合计",
				"在建工程",
				"短期借款",
				"递延所得税资产",
				"其他流动资产",
				"少数股东权益",
				"归属于少数股东的综合收益总额",
				"长期股权投资",
				"长期待摊费用",
				"应收票据",
				"投资现金流入",
				"经营现金流入",
				"投资现金流出",
				"支付的各项税费",
				"经营现金流出",
				"投资现金流量净额",
				"筹资现金流量净额",
				"现金及现金等价物净增加额",
				"支付给职工以及为职工支付的现金",
				"经营现金流量净额",
				"筹资现金流出",
				"筹资现金流入",
				"应付票据",
				"应付股利",
				"购建固定资产和其他支付的现金",
				"销售商品、提供劳务收到的现金",
				"分配股利、利润或偿付利息支付的现金",
				"处置固定资产、无形资产的现金",
				"长期借款",
				"支付其他与筹资的现金",
				"偿还债务支付现金",
				"取得借款的现金",
				"投资支付的现金",
				"吸收投资收到现金",
				"其中：联营企业和合营企业的投资收益",
				"一年内到期的非流动负债",
				"收到的税费与返还",
				"应付利息",
				"收到其他与筹资的现金",
				"其他非流动负债",
				"支付其他与投资的现金",
				"可供出售金融资产"
		};
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(new File("stock_data_fin"+startTime+".txt"),true);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<stocks.size();i++){
			String code=stocks.get(i).getCode();
			
			StringBuffer temp=new StringBuffer();
			boolean isConti=false;
			
			for(int j=0;j<menus.length;j++){
				String menu=menus[j];
				StockCompanyFinanceRate rateVO=stockCompanyFinanceRateMapper.find(code, "2016-12-31", menu);
				if(rateVO==null){
					isConti=true;
					break;
				}
				double rate=rateVO.getRate();
				temp.append(rate+" ");
			}
			
			if(isConti)continue;

			
//			20170228
			
			StockDayFu startFu=stockDayFuMapper.findStockDayFu(code, startTime);
			StockDayFu endFu=stockDayFuMapper.findStockDayFu(code, endTime);
			
			if(startFu==null||endFu==null){
				continue;
			}
			
			double changeRate=(endFu.getClose_price()-startFu.getClose_price())/startFu.getClose_price();
			
			temp.append(changeRate+"\r\n");
			
			sb.append(temp);
			
			System.out.println(code);
			System.out.println(sb.length());
			
		}
		
		try {
			fos.write(sb.toString().getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void init(){
		
		List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
		
		stocks=random_choose(stocks, 100);
		
		for(int i=0;i<stocks.size();i++){
			String code=stocks.get(i).getCode();
			
			FileOutputStream fos=null;
			try {
				fos=new FileOutputStream(new File("stock_data_"+code+".txt"),true);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			
			List<StockDayFu> stockDayFus=stockDayFuMapper.findCode(code);
			
			for(int j=120;j<stockDayFus.size();j++){
				//250
				//125
				//60
				//20
				//5
				//4
				//3
				//2
				//1
				
//				StockDayFu stock250=stockDayFus.get(j-250);
//				StockDayFu stock125=stockDayFus.get(j-125);
//				StockDayFu stock60=stockDayFus.get(j-60);
//				StockDayFu stock20=stockDayFus.get(j-20);
//				StockDayFu stock10=stockDayFus.get(j-10);
//				StockDayFu stock5=stockDayFus.get(j-5);
//				StockDayFu stock4=stockDayFus.get(j-4);
//				StockDayFu stock3=stockDayFus.get(j-3);
//				StockDayFu stock2=stockDayFus.get(j-2);
//				StockDayFu stock1=stockDayFus.get(j-1);
//
//				StockDayFu stock0=stockDayFus.get(j);
				
				StringBuffer sb=new StringBuffer();
				for(int y=0;y<5;y++){
					for(int x=60;x>=2;x--){
						StockDayFu stockN=stockDayFus.get(j-x);
						StockDayFu stock1=stockDayFus.get(j-1);
						double rate=0.;
						/*
						if(y==0){
							 rate=NumUtil.format4((stockN.getOpen_price()-stockN.getClose_price())/stockN.getClose_price());
						}else if(y==1){
							rate=NumUtil.format4((stockN.getClose_price()-stockN.getLow_price())/stockN.getClose_price());
						}else if(y==2){
							rate=NumUtil.format4((stockN.getClose_price()-stockN.getHigh_price())/stockN.getClose_price());
						}else */
						if(y==3){
							rate=NumUtil.format4((stock1.getTrade_amt()-stockN.getTrade_amt())/stockN.getTrade_amt());
						}else if(y==4){
							rate=NumUtil.format4((stock1.getClose_price()-stockN.getClose_price())/stockN.getClose_price());
						}
						if(y==3 || y==4)
							sb.append(rate+" ");
					}
				}
				StockDayFu stock1=stockDayFus.get(j-1);
				StockDayFu stock0=stockDayFus.get(j);
				
				double rate=NumUtil.format4((stock0.getClose_price()-stock1.getClose_price())/stock1.getClose_price());
				sb.append(rate+" ");
				
				String data=sb.toString().trim()+"\r\n";
				

				try {
					/*
					fos.write((
							priceChangeRate250+" "+priceChangeRate125+" "+priceChangeRate60+" "+priceChangeRate20+" "+priceChangeRate10+" "+priceChangeRate5+" "+priceChangeRate4+" "+priceChangeRate3+" "+priceChangeRate2+" "+
							//amtChangeRate250+" "+amtChangeRate125+" "+amtChangeRate60+" "+amtChangeRate20+" "+amtChangeRate10+" "+amtChangeRate5+" "+amtChangeRate4+" "+amtChangeRate3+" "+amtChangeRate2+" "+amtChangeRate1+" "+
							amtChangeRate5+" "+amtChangeRate4+" "+amtChangeRate3+" "+amtChangeRate2+" "+amtChangeRate1+" "+
							highPriceChangeRate250+" "+highPriceChangeRate125+" "+highPriceChangeRate60+" "+highPriceChangeRate20+" "+highPriceChangeRate10+" "+highPriceChangeRate5+" "+highPriceChangeRate4+" "+highPriceChangeRate3+" "+highPriceChangeRate2+" "+highPriceChangeRate1+" "+
							lowPriceChangeRate250+" "+lowPriceChangeRate125+" "+lowPriceChangeRate60+" "+lowPriceChangeRate20+" "+lowPriceChangeRate10+" "+lowPriceChangeRate5+" "+lowPriceChangeRate4+" "+lowPriceChangeRate3+" "+lowPriceChangeRate2+" "+lowPriceChangeRate1+" "+
							openCloseChangeRate250+" "+openCloseChangeRate125+" "+openCloseChangeRate60+" "+openCloseChangeRate20+" "+openCloseChangeRate10+" "+openCloseChangeRate5+" "+openCloseChangeRate4+" "+openCloseChangeRate3+" "+openCloseChangeRate2+" "+openCloseChangeRate1+" "+priceChangeRate1+"\r\n"
							).getBytes());*/
					
					fos.write(data.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					
				}
				
			}
			
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
		}
		
	}
}
