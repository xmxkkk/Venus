package venus.task.collect;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import venus.dao.IndexinfoMapper;
import venus.helper.util.Constant;
import venus.helper.util.URLUtil;
import venus.model.dao.Indexinfo;

@Component
public class IndexinfoTask {
	Logger logger=Logger.getLogger(IndexinfoTask.class);
	@Autowired IndexinfoMapper indexinfoMapper;
	@Autowired URLUtil URLUtil;
	public void init(){
		logger.info("[start]");
		try{
			String url="http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeDataSimple?page=1&num=40&sort=symbol&asc=1&node=dpzs&_s_r_a=init";
			
			String jsonStr = URLUtil.url2str(url,Constant.CHARSET$GB2312,false);
			
			JSONArray json=JSONArray.parseArray(jsonStr);
			
			if(json.size()>0){
				indexinfoMapper.deleteAll();
			}
			
			for(int i=0;i<json.size();i++){
				JSONObject object=json.getJSONObject(i);
				String code=object.getString("symbol");
				String name=object.getString("name");
			
				Indexinfo indexinfo=new Indexinfo();
				indexinfo.setCode(code);
				indexinfo.setName(name);
				
				indexinfoMapper.insert(indexinfo);
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]");
	}
}
