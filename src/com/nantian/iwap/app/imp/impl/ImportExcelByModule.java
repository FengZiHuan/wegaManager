package com.nantian.iwap.app.imp.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.nantian.iwap.app.util.DateUtil;
import com.nantian.iwap.app.util.DateUtil2;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.databus.DataTransferBus;
import com.nantian.iwap.ibatis.IWAPBatisFactory;
import com.nantian.iwap.persistence.DBAccessBean;

/**
 * 根据模块代码导入Excel数据
 * @author shuzhiqiang
 *
 */
public class ImportExcelByModule {

	public static int insertDataByPunishmentAccount(DBAccessBean dbBean,List<String> fmisnoList){
		int sum=0;
		//通过fmisno查询导入的数据
		if(fmisnoList!=null && fmisnoList.size()>0){
			for(int y=0;y<fmisnoList.size();y++){
				List<Map<String, Object>> dataList = null;
				StringBuffer sbf=new StringBuffer();
				sbf.append("select USER_ID,USER_NAME,DEPARTMENT,FMIS_NO,TFYQ,to_char(BEGIN_DATE,'yyyy-MM-dd') BEGIN_DATE,to_char(END_DATE,'yyyy-MM-dd') END_DATE,STOP_JX_FLAG,STOP_NJ_FLAG,STOP_YEAR,NFJXGZZE,YTFJXGZZE,DTFJXGZZE,");
				sbf.append("January,February,March,April,May,June,July,August,September,October,November,December,NEXT_STOP_YEAR,NEXT_NFJXGZZE,NEXT_YTFJXGZZE,NEXT_DTFJXGZZE,");
				sbf.append("NEXT_January,NEXT_February,NEXT_March,NEXT_April,NEXT_May,NEXT_June,NEXT_July,NEXT_August,NEXT_September,NEXT_October,NEXT_November,NEXT_December ");
				sbf.append(" from sys_punishment_account where FMIS_NO=?");
				dataList = dbBean.queryForList(sbf.toString(),fmisnoList.get(y));
				if(dataList!=null && dataList.size()>0){
					Map<String, Object> mapvla=dataList.get(0);
					//开始时间到结束时间之间具体日期
					List<String> dateList = new ArrayList<String>();
					Map<String,String> monthMap=getMonthMap();
					Map<String,String> nextMonthMap=getNextMonthMap();
					//停发绩效工资对应月份
					String stopSalary ="";
					for(Entry<String, String> entry : monthMap.entrySet()){
						//开始处分年份
						String syear="";
						Object stopYear=mapvla.get("STOP_YEAR");
						if(stopYear==null){
							break;
						}else{
							syear=stopYear.toString().substring(0,4);
						}
						Object val = mapvla.get(entry.getKey());
						if(val!=null){
							stopSalary+=(syear+"-"+entry.getValue()+"/"+val.toString()+",");
						}
					}
					for(Entry<String, String> entry : nextMonthMap.entrySet()){
						//开始处分年份
						String nextyear="";
						Object nextstopYear=mapvla.get("NEXT_STOP_YEAR");
						if(nextstopYear==null){
							break;
						}else{
							nextyear=nextstopYear.toString().substring(0,4);
						}
						Object val = mapvla.get(entry.getKey());
						if(val!=null){
							stopSalary+=(nextyear+"-"+entry.getValue()+"/"+val.toString()+",");
						}
					}
					//处分开始日期
					Object beginTime=mapvla.get("BEGIN_DATE");
					//处分结束日期
					Object endTime=mapvla.get("END_DATE");
					//停发绩效对应月份
					String stopjx="";
					//停发年金对应月份
					String stopnj="";
					Object stopjxflag=mapvla.get("STOP_JX_FLAG");
					Object stopnjflag=mapvla.get("STOP_NJ_FLAG");
					List<String> jxList=new ArrayList<String>();
					List<String> njList=new ArrayList<String>();
					Map<String,String> jxMap=new HashMap<String,String>();
					Map<String,String> njMap=new HashMap<String,String>();
					if(stopjxflag!=null){
						String regets = "，|,|\\s+";
						String[] jxarr=stopjxflag.toString().split(regets);
						for(int j=0;j<jxarr.length;j++){
							String jxstr=jxarr[j];
							jxstr=jxstr.replace("年", "-");
							jxstr=jxstr.replace("月", "");
							jxList.add(jxstr);
						}
					}
					if(stopnjflag!=null){
						String regets = "，|,|\\s+";
						String[] njarr=stopnjflag.toString().split(regets);
						for(int j=0;j<njarr.length;j++){
							String njstr=njarr[j];
							njstr=njstr.replace("年", "-");
							njstr=njstr.replace("月", "");
							njList.add(njstr);
						}
					}
					int monthsum=0;
					if(beginTime!=null && endTime!=null){
						dateList=DateUtil2.getMonthBetween(beginTime.toString(), endTime.toString());
						if(dateList!=null && dateList.size()>0){
							for(int t=0;t<dateList.size();t++){
								monthsum++;
								String d=dateList.get(t);
								Date date1=DateUtil.parseStrToDate2(d);
								for(int j=0;j<jxList.size();j++){
									String jdate=jxList.get(j);
									Date date2=DateUtil.parseStrToDate2(jdate);
									if(date1.getTime()==date2.getTime()){
										jxMap.put(d, "0");
									}
								}
								for(int j=0;j<njList.size();j++){
									String jdate=njList.get(j);
									Date date2=DateUtil.parseStrToDate2(jdate);
									if(date1.getTime()==date2.getTime()){
										njMap.put(d, "0");
									}
								}
							}
							for(int t=0;t<dateList.size();t++){
								String d=dateList.get(t);
								d=d.replaceAll("/", "-");
								if(jxMap!=null){
									if(jxMap.containsKey(d)){
										stopjx+=(d+"/0,");
										continue;
									}
								}
								stopjx+=(d+"/1,");
							}
							for(int t=0;t<dateList.size();t++){
								String d=dateList.get(t);
								d=d.replaceAll("/", "-");
								if(njMap!=null){
									if(njMap.containsKey(d)){
										stopnj+=(d+"/0,");
										continue;
									}
								}
								stopnj+=(d+"/1,");
							}
							System.out.println("stopjx值::"+stopjx+"stopnj值::"+stopnj);
							
							
						}
					}
					//准备数据，插入punishinfo表
					Object name=mapvla.get("USER_NAME");
					Object department=mapvla.get("DEPARTMENT");
					Object fmisno=mapvla.get("FMIS_NO");
					Object tfyq=mapvla.get("TFYQ");
					Object nfgzze=mapvla.get("NFJXGZZE");
					Object nextnfgzze=mapvla.get("NEXT_NFJXGZZE");
					Object yfgzze=mapvla.get("YTFJXGZZE");
					Object nyfgzze=mapvla.get("NEXT_YTFJXGZZE");
					Object dfgzze=mapvla.get("DTFJXGZZE");
					Object ndfgzze=mapvla.get("NEXT_DTFJXGZZE");
					Double nfgz=0.0; //拟停发绩效工资总额
					Double n=0.0;
					Double nn=0.0;
					Double yfgz=0.0; //已停发绩效工资总额
					Double yf=0.0;
					Double nyf=0.0;
					Double dfgz=0.0; //待停发绩效工资总额
					Double df=0.0;
					Double ndf=0.0;
					if(nfgzze!=null){
						n=Double.parseDouble(nfgzze.toString());
					}
					if(nextnfgzze!=null){
						nn=Double.parseDouble(nextnfgzze.toString());
					}
					nfgz=n+nn; //拟发工资总额
					if(yfgzze!=null){
						yf=Double.parseDouble(yfgzze.toString());
					}
					if(nyfgzze!=null){
						nyf=Double.parseDouble(nyfgzze.toString());
					}
					if(dfgzze!=null){
						df=Double.parseDouble(dfgzze.toString());
					}
					if(ndfgzze!=null){
						ndf=Double.parseDouble(ndfgzze.toString());
					}
					nfgz=n+nn; //拟发工资总额
					yfgz=yf+nyf; //已停发绩效工资总额
					dfgz=df+ndf;  //待停发绩效工资总额
					//停发工资对应月份
					String spsalary=stopSalary.length()>0?stopSalary.substring(0, stopSalary.length()-1) : "";
					String sjxf=stopjx.length()>0?stopjx.substring(0, stopjx.length()-1) : "";
					String snjf=stopnj.length()>0?stopnj.substring(0, stopnj.length()-1) : "";
					
					DataTransferBus dtb=new DataTransferBus();
					dtb.addElement("DEPARTMENT", department);
					dtb.addElement("USER_NAME", name);
					dtb.addElement("FMIS_NO", fmisno);
					dtb.addElement("PUNISH_MATTER", "");
					dtb.addElement("PUNISH_GRADE", "");
					dtb.addElement("DATE_ARRIVE", "");
					dtb.addElement("PUNISH_MONTH_SUM", monthsum);
					dtb.addElement("BEGIN_DATE", String.valueOf(beginTime).substring(0, 7));
					dtb.addElement("END_DATE", String.valueOf(endTime).substring(0, 7));
					dtb.addElement("PUNISH_BASIS", "");
					dtb.addElement("REMARK", "");
					dtb.addElement("STOP_PAYMENT_SALARY", spsalary);
					dtb.addElement("STOP_JX_FLAG", sjxf);
					dtb.addElement("STOP_NJ_FLAG", snjf);
					dtb.addElement("BYTFJE", 0.0);
					dtb.addElement("JTYQ", tfyq==null?"":tfyq.toString());
					dtb.addElement("NFJXGZZE", nfgz);
					dtb.addElement("YTFJXGZZE", yfgz);
					dtb.addElement("DTFJXGZZE", dfgz);
					DTBHelper dtbHelp=new DTBHelper(dtb);
					try {
					//删除指定的fmisno记录
					IWAPBatisFactory.getInstance().getIwapBatisPool().delete("delPunishByFmisNo", dtbHelp);
					//插入数据
					int result = IWAPBatisFactory.getInstance().getIwapBatisPool().insert("insertPunishInfo", dtbHelp);
					sum+=result;
					} catch (Exception e) {
						e.printStackTrace();
						DataTransferBus dt=new DataTransferBus();
						dtb.addElement("FMIS_NO", fmisno.toString());
						DTBHelper dtbh=new DTBHelper(dtb);
						IWAPBatisFactory.getInstance().getIwapBatisPool().delete("delPunishmentAccount", dtbh);
					}
					
				}
				
			}
		}
		return sum;
	}
	
	//封装年份月份map
	public static Map<String,String> getMonthMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("JANUARY", "01");
		map.put("FEBRUARY", "02");
		map.put("MARCH", "03");
		map.put("APRIL", "04");
		map.put("MAY", "05");
		map.put("JUNE", "06");
		map.put("JULY", "07");
		map.put("AUGUST", "08");
		map.put("SEPTEMBER", "09");
		map.put("OCTOBER", "10");
		map.put("NOVEMBER", "11");
		map.put("DECEMBER", "12");
		return map;
	}
	//封装下一年份月份map
	public static Map<String,String> getNextMonthMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("NEXT_JANUARY", "01");
		map.put("NEXT_FEBRUARY", "02");
		map.put("NEXT_MARCH", "03");
		map.put("NEXT_APRIL", "04");
		map.put("NEXT_MAY", "05");
		map.put("NEXT_JUNE", "06");
		map.put("NEXT_JULY", "07");
		map.put("NEXT_AUGUST", "08");
		map.put("NEXT_SEPTEMBER", "09");
		map.put("NEXT_OCTOBER", "10");
		map.put("NEXT_NOVEMBER", "11");
		map.put("NEXT_DECEMBER", "12");
		return map;
	}

}
