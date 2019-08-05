package com.nantian.iwap.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;

import org.apache.poi.ss.usermodel.DateUtil;






public class DateUtil2 {

	public static List<String> getMonthBetween(String minDate, String maxDate) {
	    ArrayList<String> result = new ArrayList<String>();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

	    Calendar min = Calendar.getInstance();
	    Calendar max = Calendar.getInstance();

	    try {
			min.setTime(sdf.parse(minDate));
		    min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
		    max.setTime(sdf.parse(maxDate));
		    max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    Calendar curr = min;
	    while (curr.before(max)) {
	     result.add(sdf.format(curr.getTime()));
	     curr.add(Calendar.MONTH, 1);
	    }

	    return result;
	  }
	
	public static void main(String[] args) throws Exception {
		/*String minDate="2018-10-01";
		String maxDate="2019-2-01";
		
		List<String> list=getMonthBetween(minDate, maxDate);
		System.out.println("list::"+list);*/
		String str="aa,cc，dd";

		String regets = "，|,|\\s+"; 
		String[] strarr=str.split(regets);
		for(int i=0;i<strarr.length;i++){
			System.out.println("strarr::"+strarr[i]);
		}
		
	}
}
