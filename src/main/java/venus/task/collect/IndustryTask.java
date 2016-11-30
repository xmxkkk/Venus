package venus.task.collect;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;

import venus.dao.IndustryMapper;
import venus.helper.util.URLUtil;
import venus.model.dao.Industry;
import venus.model.dao.IndustryStock;

@Component
public class IndustryTask {
	Logger logger=Logger.getLogger(IndexinfoTask.class);
	
	@Autowired IndustryMapper industryMapper;
	@Autowired URLUtil URLUtil;
	public void init(){
		logger.info("[start]IndustryTask.init:");
		try{
			industryMapper.deleteIndustries();
			industryMapper.deleteIndustryStocks();
			
			
			industries("新浪行业板块", "bkshy");
			industries("概念板块", "gainianbankuai");
			industries("地域板块", "diyu");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]IndustryTask.init:"+e.getMessage());
		}
		logger.info("[end]IndustryTask.init:");
	}
	private void industries(String type,String typename){
		logger.info("[start]"+type+","+typename);
		try{
			String url="http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=[[%22bknode%22,%22"+typename+"%22,%22%22,0]]";
	
			String jsonStr = URLUtil.url2str(url,false);
			
			JSONArray json = JSONArray.parseArray(jsonStr);
	
			json = json.getJSONObject(0).getJSONArray("items");
			
			for(int i=0;i<json.size();i++){
				String subtype=json.getJSONArray(i).getString(0);
				String name=json.getJSONArray(i).getString(1);
				
				Industry industry=new Industry();
				industry.setType(type);
				industry.setSubtype(subtype);
				industry.setType_code(typename);
				industry.setSubtype_code(name);
				
				industryMapper.insertIndustry(industry);
				
				int page=1;
				int pageSize=200;
				while(true){
					String newurl="http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=[[%22bkshy_node%22,%22"+name+"%22,%22%22,0,"+page+","+pageSize+"]]";
					
					logger.info(subtype+"/"+name+"/"+newurl);
					
					JSONArray temps=JSONArray.parseArray(URLUtil.url2str(newurl,false)).getJSONObject(0).getJSONArray("items");
					for(int j=0;j<temps.size();j++){
						String code=temps.getJSONArray(j).getString(1);
						
						IndustryStock industryStock=new IndustryStock();
						industryStock.setCode(code);
						industryStock.setType_code(typename);
						industryStock.setSubtype_code(name);
						
						industryMapper.insertIndustryStock(industryStock);
						
					}
					
					if(temps.size()<pageSize){
						break;
					}
					page++;
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+type+","+typename);
	}
}
