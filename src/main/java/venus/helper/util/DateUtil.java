package venus.helper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {
	public static List<String> months(String startTime,String endTime){
		List<String> result=new ArrayList<String>();
		
		
		int startYear=Integer.parseInt(startTime.substring(0, 4));
		int endYear=Integer.parseInt(endTime.substring(0, 4));

		int startMonth=Integer.parseInt(startTime.substring(4, 6));
		int endMonth=Integer.parseInt(endTime.substring(4, 6));
		
		result.add(startTime);
		
		for(int i=startMonth+1;i<=12;i++){
			String istr=String.valueOf(i);
			if(i<10){
				istr="0"+i;
			}
			if(!result.contains(startYear+istr+"01")){
				result.add(startYear+istr+"01");
			}
		}
		
		for(int year=startYear+1;year<endYear;year++){
			for(int i=1;i<=12;i++){
				String istr=String.valueOf(i);
				if(i<10){
					istr="0"+i;
				}
				result.add(year+istr+"01");
			}
		}
		
		for(int i=1;i<=endMonth;i++){
			String istr=String.valueOf(i);
			if(i<10){
				istr="0"+i;
			}
			
			if(!result.contains(endYear+istr+"01")){
				result.add(endYear+istr+"01");
			}
		}
		
		if(!result.contains(endTime)){
			result.add(endTime);
		}
		
		return result;
	}
	public static int datediff(String startTime,String endTime){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		try {
			Date endDate=sdf.parse(endTime);
			Date startDate=sdf.parse(startTime);
			return (int) ((endDate.getTime()-startDate.getTime())/(24L*60*60*1000));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public static int datediff2(String startTime,String endTime){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date endDate=sdf.parse(endTime);
			Date startDate=sdf.parse(startTime);
			return (int) ((endDate.getTime()-startDate.getTime())/(24L*60*60*1000));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public static int datediff3(String startTime,String endTime){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date endDate=sdf.parse(endTime);
			Date startDate=sdf.parse(startTime);
			return (int)((endDate.getTime()-startDate.getTime())/1000);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public static String currentYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(new Date());
	}
	public static String currentYear(String date){
		try {
			Date d=new SimpleDateFormat("yyyyMMdd").parse(date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			return sdf.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int currentSeason() {
		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH) + 1;
		int season = 0;

		if (currentMonth >= 1 && currentMonth <= 3)
			season = 1;
		else if (currentMonth >= 4 && currentMonth <= 6)
			season = 2;
		else if (currentMonth >= 7 && currentMonth <= 9)
			season = 3;
		else if (currentMonth >= 10 && currentMonth <= 12)
			season = 4;
		return season;
	}
	
	public static int currentSeason(String date){
		try {
			Date d=new SimpleDateFormat("yyyyMMdd").parse(date);
			SimpleDateFormat sdf = new SimpleDateFormat("MM");
			int season = 0;
			
			int currentMonth=Integer.parseInt(sdf.format(d));
			if (currentMonth >= 1 && currentMonth <= 3)
				season = 1;
			else if (currentMonth >= 4 && currentMonth <= 6)
				season = 2;
			else if (currentMonth >= 7 && currentMonth <= 9)
				season = 3;
			else if (currentMonth >= 10 && currentMonth <= 12)
				season = 4;
			
			return season;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String date2week(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date2 = sdf.parse(date);
			SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");

			return dateFm.format(date2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String datetime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
	public static String datetime2(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	public static String date(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}
	public static String date2(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	public static String date2(int day){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date=sdf.format(new Date());
			long d=sdf.parse(date).getTime()+day*24L*60*60*1000;
			return sdf.format(new Date(d));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String date2(String startDay,int day){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long d=sdf.parse(startDay).getTime()+day*24L*60*60*1000;
			return sdf.format(new Date(d));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String date(String startDay,int day){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			long d=sdf.parse(startDay).getTime()+day*24L*60*60*1000;
			return sdf.format(new Date(d));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String convert(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(date.length()==8){
				return sdf2.format(sdf.parse(date));
			}else if(date.length()==10){
				return sdf.format(sdf2.parse(date));
			}else if(date.length()==14){
				return sdf4.format(sdf3.parse(date));
			}else if(date.length()==19){
				return sdf3.format(sdf4.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> financeDay(List<String> result,String day,int num){
		if(result==null){
			result=new ArrayList<String>();
		}
		if(num==0){
			return result;
		}
		
		//"2016-09-30"
		int year=Integer.parseInt(day.substring(0,4));
		int month=Integer.parseInt(day.substring(5, 7));

		String mm=day.substring(5);
		
		String ixString=null;
		if(mm.compareTo("12-31")<0 && mm.compareTo("09-30")>=0){
			ixString=year+"-09-30";
		}else if(mm.compareTo("09-30")<0 && mm.compareTo("06-30")>=0){
			ixString=year+"-06-30";
		}else if(mm.compareTo("06-30")<0 && mm.compareTo("03-31")>=0){
			ixString=year+"-03-31";
		}else if(mm.compareTo("03-31")<0 && mm.compareTo("01-01")>=0){
			ixString=(year-1)+"-12-31";
		}else if(mm.compareTo("12-31")==0){
			ixString=year+"-12-31";
		}
		result.add(ixString);
		
		return financeDay(result,DateUtil.date2(ixString, -1),--num);
	}
	
}
