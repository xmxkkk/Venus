package venus.strategy.stockfilter.filter;

public interface StockFilter {
	boolean filter(String code,String params);
}
