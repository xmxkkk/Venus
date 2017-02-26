package venus.helper.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class URLUtil {
	@Value("${url-file-basepath}")
	String urlFileBasepath;
	static public int errorNum=0;
	static CloseableHttpClient httpclient;// = HttpClients.createDefault();
	public URLUtil(){
		RequestConfig config=RequestConfig.custom().setConnectTimeout(Constant.TIMEOUT).setConnectionRequestTimeout(Constant.TIMEOUT).setSocketTimeout(Constant.TIMEOUT).build();
		httpclient = HttpClients.custom().setDefaultRequestConfig(config)  
		         .setMaxConnTotal(200)  
		         .setMaxConnPerRoute(100).build();  
	}
	public String url2str(String url,boolean cache) throws IOException{
		return url2str(url, Constant.CHARSET$UTF8, cache);
	}
	public String url2str(String url,String charset,boolean cache) throws IOException{
		String filepath=url2path(url);
		if(cache){
			if(new File(filepath).exists()){
				String result=file2str(filepath,charset);
				if(StringUtil.isBlank(result)){
					clearCache(url);
				}else{
					return result;
				}
			}
		}
		String result=null;
		HttpGet httpGet=new HttpGet(url);
		CloseableHttpResponse response=null;
		try {
//			httpGet.setConfig(config);
			response=httpclient.execute(httpGet);
			if(response.getStatusLine().getStatusCode()==200){
				result=is2str(response.getEntity().getContent(),charset);
			}else{
				if(!httpGet.isAborted()){
					httpGet.abort();
				}
				result= null;
			}
		} catch (ClientProtocolException e) {
			errorNum++;
			throw e;
		} catch (IOException e) {
			errorNum++;
			throw e;
		}finally {
			try {  
                if (response != null) {  
                    response.getEntity().getContent().close();
                }  
            } catch (IllegalStateException e) {  
            	throw e;
            } catch (IOException e) {  
            	throw e;
            }  
			if(!httpGet.isAborted()){
				httpGet.abort();
			}
		}
		
		if(!StringUtil.isBlank(result)){
			str2file(filepath, result, charset);
		}
		
		return result;
	}
	private static String is2str(InputStream is,String charset) throws IOException{
		BufferedReader br=new BufferedReader(new InputStreamReader(is, charset));
		return is2str(br);
	}
	
	private static String is2str(BufferedReader br) throws IOException{
		StringBuffer sb = new StringBuffer();
		char[] buf = new char[102400];
		int len = 0;
		while ((len = br.read(buf, 0, buf.length)) > 0) {
			sb.append(new String(buf, 0, len));
		}
		br.close();
		return sb.toString();
	}
	private String url2path(String url){
		String path=null;
		try {
			String host=new URL(url).getHost();
			String md5=CommonUtil.md5(url);
			String dir=urlFileBasepath+"/"+host+"/"+md5.substring(0, 2)+"/"+md5.substring(2, 4)+"/"+md5.substring(4, 6)+"/"+md5.substring(6, 8)+"/"+md5.substring(8, 10);
			if(!new File(dir).exists()){
				new File(dir).mkdirs();
			}
			path=dir+"/"+md5;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return path;
	}
	private static String file2str(String filepath,String charset){
		String result=null;
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(filepath), charset));
			result= is2str(br);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void str2file(String filepath,String str,String charset){
		try {
			if(new File(filepath).exists()){
				new File(filepath).delete();
			}
			new File(filepath).createNewFile();
			
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath),charset));
			bw.write(str);
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void clearCache(String url){
		String filepath=url2path(url);
		new File(filepath).delete();
	}
	public String getUrlFileBasepath() {
		return urlFileBasepath;
	}
	public void setUrlFileBasepath(String urlFileBasepath) {
		this.urlFileBasepath = urlFileBasepath;
	}
	
	
	/*
	public static String urlFileBasepath="d:/storage";
	
	public static String getUrlFileBasepath() {
		return urlFileBasepath;
	}
	public static void setUrlFileBasepath(String urlFileBasepath) {
		URLUtil.urlFileBasepath = urlFileBasepath;
	}
	public static String url2path(String url){
		String path=null;
		try {
			String host=new URL(url).getHost();
			String md5=CommonUtil.md5(url);
			String dir=urlFileBasepath+"/"+host+"/"+md5.substring(0, 2)+"/"+md5.substring(2, 4)+"/"+md5.substring(4, 6)+"/"+md5.substring(6, 8)+"/"+md5.substring(8, 10);
			if(!new File(dir).exists()){
				new File(dir).mkdirs();
			}
			path=dir+"/"+md5;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return path;
	}
	public static String file2str(String filepath){
		return file2str(filepath, Constant.CHARSET$UTF8);
	}
	public static String file2str(String filepath,String charset){
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(filepath), charset));
			return is2str(br);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void str2file(String filepath,String str){
		try {
			if(new File(filepath).exists()){
				new File(filepath).delete();
			}
			new File(filepath).createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(new File(filepath));
			fileOutputStream.write(str.getBytes());
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String is2str(BufferedReader is){
		String result = null;
		try {
			StringBuffer sb = new StringBuffer();
			char[] buf = new char[102400];
			int len = 0;
			while ((len = is.read(buf, 0, 102400)) > 0) {
				sb.append(new String(buf, 0, len));
			}
			is.close();
			result = sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String url2str2cache(String url,String charset){
		return url2str(url,charset,true);
	}
	public static String url2str2cache(String url){
		return url2str2cache(url,Constant.CHARSET$UTF8);
	}
	
	public static String url2str(String url,boolean cache){
		return url2str(url, Constant.CHARSET$UTF8, cache);
	}
	public static void clearCache(String url){
		String filepath=url2path(url);
		new File(filepath).delete();
	}
	public static String url2str(String url,String charset,boolean cache) {
		String filepath=url2path(url);
		if(cache){
			if(new File(filepath).exists()){
				return (filepath);
			}
		}
		
		try {
			InputStream is = new URL(url).openStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(is, charset));
			
			String result=is2str(br);
			if(StringUtil.isBlank(result)){
				return null;
			}
			if(cache){
				str2file(filepath, result);
			}
			return result;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}*/
}
