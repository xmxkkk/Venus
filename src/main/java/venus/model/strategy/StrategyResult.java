package venus.model.strategy;

import java.util.List;

public class StrategyResult {
	private int id;
	private String strategy_class;
	private String code;
	private String start_time;
	private String end_time;
	private double mar;
	private double sharpe_ratio;
	private int buy_times;
	private int sell_times;
	private double profit_rate;
	private int state;
	private double max_down;
	private String param;
	private double profit_rate_year;
	private double profit_rate_standard;
	private double profit_rate_standard_year;
	
	public double getProfit_rate_standard_year() {
		return profit_rate_standard_year;
	}
	public void setProfit_rate_standard_year(double profit_rate_standard_year) {
		this.profit_rate_standard_year = profit_rate_standard_year;
	}
	public double getProfit_rate_standard() {
		return profit_rate_standard;
	}
	public void setProfit_rate_standard(double profit_rate_standard) {
		this.profit_rate_standard = profit_rate_standard;
	}
	public double getProfit_rate_year() {
		return profit_rate_year;
	}
	public void setProfit_rate_year(double profit_rate_year) {
		this.profit_rate_year = profit_rate_year;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public double getMax_down() {
		return max_down;
	}
	public void setMax_down(double max_down) {
		this.max_down = max_down;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	private List<StrategyProcess> strategyProcesses;
	public List<StrategyProcess> getStrategyProcesses() {
		return strategyProcesses;
	}
	public void setStrategyProcesses(List<StrategyProcess> strategyProcesses) {
		this.strategyProcesses = strategyProcesses;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStrategy_class() {
		return strategy_class;
	}
	public void setStrategy_class(String strategy_class) {
		this.strategy_class = strategy_class;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public double getMar() {
		return mar;
	}
	public void setMar(double mar) {
		this.mar = mar;
	}
	public double getSharpe_ratio() {
		return sharpe_ratio;
	}
	public void setSharpe_ratio(double sharpe_ratio) {
		this.sharpe_ratio = sharpe_ratio;
	}
	public int getBuy_times() {
		return buy_times;
	}
	public void setBuy_times(int buy_times) {
		this.buy_times = buy_times;
	}
	public int getSell_times() {
		return sell_times;
	}
	public void setSell_times(int sell_times) {
		this.sell_times = sell_times;
	}
	public double getProfit_rate() {
		return profit_rate;
	}
	public void setProfit_rate(double profit_rate) {
		this.profit_rate = profit_rate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "StrategyResult [id=" + id + ", strategy_class=" + strategy_class + ", code=" + code + ", start_time="
				+ start_time + ", end_time=" + end_time + ", mar=" + mar + ", sharpe_ratio=" + sharpe_ratio
				+ ", buy_times=" + buy_times + ", sell_times=" + sell_times + ", profit_rate=" + profit_rate
				+ ", state=" + state + ", max_down=" + max_down + ", param=" + param + ", profit_rate_year="
				+ profit_rate_year + ", profit_rate_standard=" + profit_rate_standard + ", profit_rate_standard_year="
				+ profit_rate_standard_year + ", strategyProcesses=" + strategyProcesses + "]";
	}
	
}
