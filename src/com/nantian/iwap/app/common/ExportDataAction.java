package com.nantian.iwap.app.common;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nantian.iwap.app.exp.ExpDataFactory;
import com.nantian.iwap.app.util.DateUtil;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;

/**
 * 数据导出
 * 
 * @author wjj
 *
 */
public class ExportDataAction extends TransactionBizAction {
	private static Logger log = Logger.getLogger(ImportDataAction.class);

	@SuppressWarnings("rawtypes")
	@Override
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			String exportFlag = dtbHelper.getStringValue("exportFlag");
			if (!"1".equals(exportFlag)) {
				return 1;
			}
			String filetype = dtbHelper.getStringValue("filetype");
			String txcode = dtbHelper.getStringValue("txcode");
			Map rst=null;
			if("punish".equals(txcode)){
				String beginT = dtbHelper.getStringValue("beginTime");
				String endT = dtbHelper.getStringValue("endTime");
				String userName="%" + URLDecoder.decode(dtbHelper.getStringValue("userName"),"utf-8") + "%";
				String departmentName="%" + URLDecoder.decode(dtbHelper.getStringValue("departmentName"),"utf-8")+ "%";	
				String userId = dtbHelper.getStringValue("userId");
				rst=exportDataPunish(userId,filetype,beginT,endT,userName,departmentName);
			}else if("mergeSummary".equals(txcode)){
				String branchNames = dtbHelper.getStringValue("branchNames");
				String names=new String(branchNames.getBytes("ISO-8859-1"),"utf-8");
				String branchs=assembleString(names);
				String payMonth = dtbHelper.getStringValue("payMonth");
				rst=exportBranchData(filetype,branchs,payMonth);
				
			}else{
				if (filetype == null || "".equals(filetype.trim())) {
					log.error("数据导出类型为空");
					dtbHelper.setError("exportdata-err-001", "数据导出类型为空");
					return flag;
				}

				ExpDataFactory edf = ExpDataFactory.getInstance();

				if (dtbHelper.getDataTransferBus().getElement("rows") == null) {
					dtbHelper.setError("exportdata-err-002", "数据总线缺少rows对象");
					return flag;
				} else if (!(dtbHelper.getDataTransferBus().getElement("rows").getAllValues()
						.get(0) instanceof java.util.List)) {
					dtbHelper.setError("exportdata-err-003", "数据总线rows对象不是java.util.List数据类型");
					return flag;
				}
				List dList = dtbHelper.getListValue("rows");

				String titleString = dtbHelper.getStringValue("titleString");
				List<Map<String, String>> titleList = new ArrayList<Map<String, String>>();
				if (titleString != null && !"".equals(titleString.trim())) {
					try {
						titleList = JSONObject.parseObject(titleString, new TypeReference<List<Map<String, String>>>() {
						});
					} catch (Exception e) {
						log.warn("格式化自定义表格列名出错", e);
					}
				}else{
					log.warn("前端未传入表格字段数据titleString对象");
				}

				rst = edf.expData(filetype, dList, titleList);
			}

			if (rst.get("msg") != null && !"".equals(rst.get("msg").toString().trim())) {
				log.error("导出处理类数据导出出错:" + rst.get("msg"));
				dtbHelper.setError("exportdata-err-004", "[导出处理类数据导出出错]" + rst.get("msg"));
			} else {
				dtbHelper.setRstData("info", rst.get("info"));
				dtbHelper.setRstData("fileName", rst.get("fileName"));
				flag = 1;
			}
		} catch (Exception e) {
			log.error("数据导出出错", e);
			dtbHelper.setError("exportdata-err-005", "[数据导出出错]" + e.getMessage());
		}
		return flag;
	}

	/**
	 * 处分停发台账导出
	 * @param list
	 * @return
	 */
	public Map exportDataPunish(String userId,String filetype,String beginT,String endT,String userName,String departmentName){
		ExpDataFactory edf = ExpDataFactory.getInstance();
		List<Map<String, Object>> dataList = null;
		if(userId!=null && !"".equals(userId)){
			dataList = getPunishDataOne(userId);
		}else{
			dataList = getPunishData(beginT, endT, userName, departmentName);
		}
		/*此处的key为每个sheet的名称，一个excel中可能有多个sheet页*/ /*此处key对应每一列的标题*//*该list为每个sheet页的数据*/
        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
        //表格key-value抬头
        Map<String,String> titleMap=new LinkedHashMap<String,String>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<Date> listb = new ArrayList<Date>();
        List<Date> liste = new ArrayList<Date>();
        //判断标题月份展示
        for(int i=0;i<dataList.size();i++){
        	Object beginDate=dataList.get(i).get("BEGIN_DATE"); //处分开始时间
        	Object endDate=dataList.get(i).get("END_DATE"); //处分结束时间
        	Date bdate=DateUtil.parseDate(beginDate.toString(), DateUtil.YYYY_MM_DD);
        	Date edate=DateUtil.parseDate(endDate.toString(), DateUtil.YYYY_MM_DD);
        	listb.add(bdate);
        	liste.add(edate);
        }
        String minYear=getMinYear(listb);
        String maxYear=getMaxYear(liste);
        //封装表头标题
        titleMap.put("department", "部门");
        titleMap.put("fmis", "FMIS员工号");
        titleMap.put("name", "姓名");
        titleMap.put("tfyq", "停发要求");
        titleMap.put("tfxm", "停发项目");
        titleMap.put("tfxm2", "停发项目2");
        if(minYear.equals(maxYear)){ //年份相同
        	titleMap.put(minYear+"NFJXGZZE", minYear+"年停发绩效工资总额");
	        titleMap.put(minYear+"YTFJXGZZE", minYear+"年已停发绩效工资总额");
	        titleMap.put(minYear+"DTFJXGZZE", minYear+"年待停发绩效工资总额");
	        for(int i=1;i<=12;i++){
	        	if(i<10){
	        		String key = minYear+"-0"+i;
	        		String value = i+"月";
	        		titleMap.put(key, value);
	        	}else{
	        		String key = minYear+"-"+i;
	        		String value = i+"月";
	        		titleMap.put(key, value);
	        	}
	        }
        }else{ //添加两年的头标题
        	titleMap.put(minYear+"NFJXGZZE", minYear+"年停发绩效工资总额");
	        titleMap.put(minYear+"YTFJXGZZE", minYear+"年已停发绩效工资总额");
	        titleMap.put(minYear+"DTFJXGZZE", minYear+"年待停发绩效工资总额");
	        for(int i=1;i<=12;i++){
	        	if(i<10){
	        		String key = minYear+"-0"+i;
	        		String value = i+"月";
	        		titleMap.put(key, value);
	        	}else{
	        		String key = minYear+"-"+i;
	        		String value = i+"月";
	        		titleMap.put(key, value);
	        	}
	        }
	        titleMap.put(maxYear+"NFJXGZZE", maxYear+"年停发绩效工资总额");
	        titleMap.put(maxYear+"YTFJXGZZE", maxYear+"年已停发绩效工资总额");
	        titleMap.put(maxYear+"DTFJXGZZE", maxYear+"年待停发绩效工资总额");
	        for(int i=1;i<=12;i++){
	        	if(i<10){
	        		String key = maxYear+"-0"+i;
	        		String value = i+"月";
	        		titleMap.put(key, value);
	        	}else{
	        		String key = maxYear+"-"+i;
	        		String value = i+"月";
	        		titleMap.put(key, value);
	        	}
	        }
        }
        //封装数据
        for(int i=0;i<dataList.size();i++){
        	Object id=dataList.get(i).get("USER_ID");
        	Object name=dataList.get(i).get("USER_NAME");
        	Object department=dataList.get(i).get("DEPARTMENT");
        	Object fmisNo=dataList.get(i).get("FMIS_NO");
        	Object pmatter=dataList.get(i).get("JTYQ");  //停发绩效具体要求
        	Object tingfa=dataList.get(i).get("NFJXGZZE"); //拟停发绩效工资总额
        	Object ytingfa=dataList.get(i).get("YTFJXGZZE"); //已停发绩效工资总额
        	Object dtingfa=dataList.get(i).get("DTFJXGZZE"); //待停发绩效工资总额
        	Object beginDate=dataList.get(i).get("BEGIN_DATE"); //处分开始时间
        	Object endDate=dataList.get(i).get("END_DATE"); //处分结束时间
        	Object stopjx=dataList.get(i).get("STOP_JX_FLAG"); //是否停发绩效工资月份
        	Object stopsalary=dataList.get(i).get("STOP_PAYMENT_SALARY"); //停发金额工资月份
        	Object stopnj=dataList.get(i).get("STOP_NJ_FLAG"); //是否暂停企业年金月份
        	
        	for(int j=0;j<3;j++){
	        	HashMap<String, String> mapval = new HashMap<String, String>();
	        	mapval.put("department", department==null?"":String.valueOf(department));
	        	mapval.put("fmis", fmisNo==null?"":String.valueOf(fmisNo));
	        	mapval.put("name", name==null?"":String.valueOf(name));
	        	mapval.put("tfyq", pmatter==null?"":String.valueOf(pmatter));
	        	if(j==0){
	        		mapval.put("tfxm", "停发绩效工资");
	        		mapval.put("tfxm2", "是否停发绩效工资");
	        		//是否停发绩效工资月份
	        		if(stopjx!=null){
	        			String stopjxStr=String.valueOf(stopjx);
	        			String[] arrjx=stopjxStr.split(",");
	        			for(int y=0;y<arrjx.length;y++){
	        				String[] arr=arrjx[y].split("/");
	        				String val=arr[1];
	        				if("0".equals(val)){
	        					val="是";
	        				}else{
	        					val="否";
	        				}
	        				mapval.put(arr[0], val);
	        			}
	        		}
	        	}else if(j==1){
	        		Map<String,Integer> jxmap=new HashMap<String,Integer>();
	        		mapval.put("tfxm", "停发绩效工资");
	        		mapval.put("tfxm2", "停发金额（元）");
	        		//201x年停发绩效工资总额  先不计算
	        		//停发金额
	        		if(stopsalary!=null){
	        			String stopsalaryStr=String.valueOf(stopsalary);
	        			String[] arrsalary=stopsalaryStr.split(",");
	        			for(int y=0;y<arrsalary.length;y++){
	        				String[] arr=arrsalary[y].split("/");
	        				mapval.put(arr[0], arr[1]);
	        				String dateStr=arr[0].substring(0, 4);
	        				if(jxmap.containsKey(dateStr)){
	        					Integer value=jxmap.get(dateStr)+Integer.parseInt(arr[1]);
	        					jxmap.put(dateStr, value);
	        				}else{
	        					jxmap.put(dateStr, Integer.parseInt(arr[1]));
	        				}
	        			}
	        		}
	        		for(Entry<String,Integer> entry : jxmap.entrySet()){
	        			System.out.println("key:::"+entry.getKey());
	        			String key=entry.getKey();
	        			mapval.put(key+"NFJXGZZE", String.valueOf(entry.getValue()));
	        		}
	        		
	        	}else if(j==2){
	        		mapval.put("tfxm", "是否暂停企业年金单位缴费部分");
	        		mapval.put("tfxm2", "是否暂停企业年金单位缴费部分");
	        		//是否停发绩效工资月份
	        		if(stopnj!=null){
	        			String stopnjStr=String.valueOf(stopnj);
	        			String[] arrnj=stopnjStr.split(",");
	        			for(int y=0;y<arrnj.length;y++){
	        				String[] arr=arrnj[y].split("/");
	        				String val=arr[1];
	        				if("0".equals(val)){
	        					val="是";
	        				}else{
	        					val="否";
	        				}
	        				mapval.put(arr[0], val);
	        			}
	        		}
	        	}
	        	list.add(mapval);
        	}
        }
        Map rst = edf.expDataPunish(filetype, list, titleMap,new int[]{0,1,2,3,4});
        return rst;
	}
	//判断最小的年份
	public String getMinYear(List<Date> list){
		if(list!=null && list.size()>0){
			Date minDate=list.get(0);
			for(int i=0;i<list.size();i++){
				if(list.get(i).getTime()<minDate.getTime()){
					minDate=list.get(i);
				}
			}
			String yyyy=DateUtil.format(minDate, DateUtil.YYYY);
			return yyyy;
		}
		return "";
	}
	
	// 判断最大的年份
	public String getMaxYear(List<Date> list) {
		if (list != null && list.size() > 0) {
			Date maxDate = list.get(0);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getTime() > maxDate.getTime()) {
					maxDate = list.get(i);
				}
			}
			String yyyy = DateUtil.format(maxDate, DateUtil.YYYY);
			return yyyy;
		}
		return "";
	}
	
	/**
	 * 组装字符串
	 */
	public String assembleString(String str){
		String branchNames="";
		if(!StringUtil.isBlank(str)){
			String[] arrStr=str.split(",");
			for(int i=0;i<arrStr.length;i++){
				branchNames+="'" + arrStr[i] +"',";
			}
			branchNames=branchNames.substring(0, branchNames.length()-1);
		}
		return branchNames;
	}
	
	/**
	 * 获取导出数据
	 */
	public List<Map<String, Object>> getPunishData(String beginT,String endT,String userName,String departmentName){
		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<Map<String, Object>> dataList = null;
			StringBuffer sbf=new StringBuffer();
			sbf.append("select USER_ID,USER_NAME, DEPARTMENT, FMIS_NO, PUNISH_MATTER, PUNISH_GRADE, to_char(DATE_ARRIVE,'yyyy-MM-dd') DATE_ARRIVE,STOP_PAYMENT_SALARY,STOP_PAYMENT_SUM,");
			sbf.append("PUNISH_MONTH_SUM, to_char(BEGIN_DATE,'yyyy-MM-dd') BEGIN_DATE, to_char(END_DATE,'yyyy-MM-dd') END_DATE, PUNISH_BASIS,STOP_JX_FLAG,STOP_NJ_FLAG, REMARK,EXECUTE_MONTH,");
			sbf.append("YFJXGZ,BYTFJE,SJYFJXGZ,JTYQ,NFJXGZZE from punishInfo where 1=1 and DEPARTMENT like ? and USER_NAME like ? and BEGIN_DATE>=to_date(?,'yyyy/MM/dd') and END_DATE<=to_date(?,'yyyy/MM/dd')");
			dataList = dbBean.queryForList(sbf.toString(),departmentName,userName,beginT,endT);
			System.out.println("dataList::"+dataList);
			return dataList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取单个导出数据
	 */
	public List<Map<String, Object>> getPunishDataOne(String userId){
		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<Map<String, Object>> dataList = null;
			StringBuffer sbf=new StringBuffer();
			sbf.append("select USER_ID,USER_NAME, DEPARTMENT, FMIS_NO, PUNISH_MATTER, PUNISH_GRADE, to_char(DATE_ARRIVE,'yyyy-MM-dd') DATE_ARRIVE,STOP_PAYMENT_SALARY,STOP_PAYMENT_SUM,");
			sbf.append("PUNISH_MONTH_SUM, to_char(BEGIN_DATE,'yyyy-MM-dd') BEGIN_DATE, to_char(END_DATE,'yyyy-MM-dd') END_DATE, PUNISH_BASIS,STOP_JX_FLAG,STOP_NJ_FLAG, REMARK,EXECUTE_MONTH,");
			sbf.append("YFJXGZ,BYTFJE,SJYFJXGZ,JTYQ,NFJXGZZE from punishInfo where 1=1 and USER_ID=?");
			dataList = dbBean.queryForList(sbf.toString(),userId);
			System.out.println("dataList::"+dataList);
			return dataList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 导出各分行五险一金数据汇总
	 */
	public Map exportBranchData(String filetype,String branchs,String payMonth){
		ExpDataFactory edf = ExpDataFactory.getInstance();
		List<Map<String, Object>> dataList = null;
		//通过ID和月份查询数据
		dataList=findDataByIdAndMonth(branchs,payMonth);
		/*此处的key为每个sheet的名称，一个excel中可能有多个sheet页*/ /*此处key对应每一列的标题*//*该list为每个sheet页的数据*/
        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
        //表格key-value抬头
        Map<String,String> titleMap=new LinkedHashMap<String,String>();
        Map<String,String> titleMap2=new LinkedHashMap<String,String>();
        //封装表头标题
        titleMap.put("BRANCH_NAME", "分行名称");
        titleMap.put("USER_NAME", "姓名");
        titleMap.put("FMIS_NO", "FMIS员工号");
        titleMap.put("GJJ_MS_COMPANY_MONTHPAY", "住房公积金");
        titleMap.put("GJJ_JS_COMPANY_MONTHPAY", "住房公积金");
        titleMap.put("GJJ_MS_PERSONAL_MONTHPAY", "住房公积金");
        titleMap.put("GJJ_JS_PERSONAL_MONTHPAY", "住房公积金");        
        titleMap.put("YLBX_COMPANY_MONTHPAY", "医疗保险");
        titleMap.put("YLBX_PERSONAL_MONTHPAY", "医疗保险");
        titleMap.put("SYBX_COMPANY_MONTHPAY", "失业保险");
        titleMap.put("SYBX_PERSONAL_MONTHPAY", "失业保险");       
        titleMap.put("YLAOBX_COMPANY_MONTHPAY", "养老保险");
        titleMap.put("YLAOBX_PERSONAL_MONTHPAY", "养老保险");
        titleMap.put("GSBX_COMPANY_MONTHPAY", "工伤保险");    
        titleMap.put("SYUBX_COMPANY_MONTHPAY", "生育保险");
        titleMap.put("COMPANY_COUNT", "合计");
        titleMap.put("PERSONAL_COUNT", "合计");
        titleMap.put("COMPANYANDPERSONAL_COUNT", "合计( 单位+个人）");
        
        titleMap2.put("BRANCH_NAME", "分行名称");
        titleMap2.put("USER_NAME", "姓名");
        titleMap2.put("FMIS_NO", "FMIS员工号");
        titleMap2.put("GJJ_MS_COMPANY_MONTHPAY", "单位月缴费金额（免税）");
        titleMap2.put("GJJ_JS_COMPANY_MONTHPAY", "单位月缴费金额(计税）");
        titleMap2.put("GJJ_MS_PERSONAL_MONTHPAY", "个人月缴费金额（免税）");
        titleMap2.put("GJJ_JS_PERSONAL_MONTHPAY", "个人月缴费金额(计税）");        
        titleMap2.put("YLBX_COMPANY_MONTHPAY", "单位月缴费金额");
        titleMap2.put("YLBX_PERSONAL_MONTHPAY", "个人月缴费金额");
        titleMap2.put("SYBX_COMPANY_MONTHPAY", "单位月缴费金额");
        titleMap2.put("SYBX_PERSONAL_MONTHPAY", "个人月缴费金额");       
        titleMap2.put("YLAOBX_COMPANY_MONTHPAY", "单位月缴费金额");
        titleMap2.put("YLAOBX_PERSONAL_MONTHPAY", "个人月缴费金额");
        titleMap2.put("GSBX_COMPANY_MONTHPAY", "单位月缴费金额");     
        titleMap2.put("SYUBX_COMPANY_MONTHPAY", "单位月缴费金额");
        titleMap2.put("COMPANY_COUNT", "单位");
        titleMap2.put("PERSONAL_COUNT", "个人");
        titleMap2.put("COMPANYANDPERSONAL_COUNT", "合计( 单位+个人）");
        
        Map rst = edf.expBranchData(filetype, dataList, titleMap,titleMap2,payMonth);
        //修改
        if (rst.get("msg") == null || "".equals(rst.get("msg").toString().trim())) {
        	try {
        		DBAccessBean dbBean = DBAccessPool.getDbBean();
            	String sqlStr="update fiveRisks_oneGold set SUMMARYFLAG='1' where status='1' and BRANCH_NAME in (" + branchs + ")";
            	dbBean.executeUpdate(sqlStr);
            	dbBean.executeCommit();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		return rst;
	}
	
	public List<Map<String, Object>> findDataByIdAndMonth(String branchs,String payMonth){
		DBAccessBean dbBean = DBAccessPool.getDbBean();
		List<Map<String, Object>> dataList = null;
		StringBuffer sbf=new StringBuffer();
		
		String sqlStr=" select ID,USER_NAME,FMIS_NO,BRANCH_NAME,to_char(PAY_MONTH,'yyyy-MM') PAY_MONTH,GJJ_MS_COMPANY_MONTHPAY,GJJ_JS_COMPANY_MONTHPAY,"+
				"GJJ_MS_PERSONAL_MONTHPAY,GJJ_JS_PERSONAL_MONTHPAY,YLBX_COMPANY_MONTHPAY,YLBX_PERSONAL_MONTHPAY,SYBX_COMPANY_MONTHPAY,SYBX_PERSONAL_MONTHPAY,"+
				"YLAOBX_COMPANY_MONTHPAY,YLAOBX_PERSONAL_MONTHPAY,GSBX_COMPANY_MONTHPAY,SYUBX_COMPANY_MONTHPAY,"+
				"COMPANY_COUNT,PERSONAL_COUNT,COMPANYANDPERSONAL_COUNT,STATUS,CHECKSTATUS from fiveRisks_oneGold where 1=1 and status='1' ";
		
		if (!"".equals(payMonth)) {
			sqlStr += " and PAY_MONTH= to_date('"+ payMonth + "','yyyy/MM') ";
		}
		
		if (!"".equals(branchs)) {
			sqlStr += " and BRANCH_NAME in (" + branchs + ")";
		}
		sqlStr +="order by BRANCH_NAME";
		dataList = dbBean.queryForList(sqlStr);
		if(dataList!=null && dataList.size()>0){
			Map<String, Object> mapObj = new HashMap<String, Object>();
			BigDecimal GJJ_MS_COMPANY_SUM = new BigDecimal("0");
			BigDecimal GJJ_JS_COMPANY_SUM = new BigDecimal("0");
			BigDecimal GJJ_MS_PERSONAL_SUM = new BigDecimal("0");
			BigDecimal GJJ_JS_PERSONAL_SUM = new BigDecimal("0");
			BigDecimal YLBX_COMPANY_SUM = new BigDecimal("0");
			BigDecimal YLBX_PERSONAL_SUM= new BigDecimal("0");
			BigDecimal SYBX_COMPANY_SUM = new BigDecimal("0");
			BigDecimal SYBX_PERSONAL_SUM = new BigDecimal("0");
			BigDecimal YLAOBX_COMPANY_SUM = new BigDecimal("0");
			BigDecimal YLAOBX_PERSONAL_SUM = new BigDecimal("0");
			BigDecimal GSBX_COMPANY_SUM = new BigDecimal("0");
			BigDecimal SYUBX_COMPANY_SUM = new BigDecimal("0");
			BigDecimal COMPANY_COUNT_SUM = new BigDecimal("0");
			BigDecimal PERSONAL_COUNT_SUM = new BigDecimal("0");
			BigDecimal COMPANYANDPERSONAL_COUNT_SUM = new BigDecimal("0");
			for(int i=0;i<dataList.size();i++){
				Map<String, Object> map = dataList.get(i);
				Set<Entry<String, Object>> set=map.entrySet();
				for(Entry<String, Object> entry : set){
					String key = entry.getKey();
					Object value = entry.getValue();
					if("GJJ_MS_COMPANY_MONTHPAY".equals(key)){	
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							GJJ_MS_COMPANY_SUM=GJJ_MS_COMPANY_SUM.add(bd);
						}
					}else if("GJJ_JS_COMPANY_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							GJJ_JS_COMPANY_SUM=GJJ_JS_COMPANY_SUM.add(bd);
						}
					}else if("GJJ_MS_PERSONAL_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							GJJ_MS_PERSONAL_SUM=GJJ_MS_PERSONAL_SUM.add(bd);
						}
					}else if("GJJ_JS_PERSONAL_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							GJJ_JS_PERSONAL_SUM=GJJ_JS_PERSONAL_SUM.add(bd);
						}
					}else if("YLBX_COMPANY_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							YLBX_COMPANY_SUM=YLBX_COMPANY_SUM.add(bd);
						}
					}else if("YLBX_PERSONAL_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							YLBX_PERSONAL_SUM=YLBX_PERSONAL_SUM.add(bd);
						}
					}else if("SYBX_COMPANY_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							SYBX_COMPANY_SUM=SYBX_COMPANY_SUM.add(bd);
						}
					}else if("SYBX_PERSONAL_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal count = new BigDecimal(String.valueOf(SYBX_PERSONAL_SUM));
							BigDecimal bd = new BigDecimal(value.toString());
							SYBX_PERSONAL_SUM=SYBX_PERSONAL_SUM.add(bd);
						}
					}else if("YLAOBX_COMPANY_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal count = new BigDecimal(String.valueOf(YLAOBX_COMPANY_SUM));
							BigDecimal bd = new BigDecimal(value.toString());
							YLAOBX_COMPANY_SUM=YLAOBX_COMPANY_SUM.add(bd);
						}
					}else if("YLAOBX_PERSONAL_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal count = new BigDecimal(String.valueOf(YLAOBX_PERSONAL_SUM));
							BigDecimal bd = new BigDecimal(value.toString());
							YLAOBX_PERSONAL_SUM=YLAOBX_PERSONAL_SUM.add(bd);
						}
					}else if("GSBX_COMPANY_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal count = new BigDecimal(String.valueOf(GSBX_COMPANY_SUM));
							BigDecimal bd = new BigDecimal(value.toString());
							GSBX_COMPANY_SUM=GSBX_COMPANY_SUM.add(bd);
						}
					}else if("SYUBX_COMPANY_MONTHPAY".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							SYUBX_COMPANY_SUM=SYUBX_COMPANY_SUM.add(bd);
						}
					}else if("COMPANY_COUNT".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							COMPANY_COUNT_SUM=COMPANY_COUNT_SUM.add(bd);
						}
					}else if("PERSONAL_COUNT".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							PERSONAL_COUNT_SUM=PERSONAL_COUNT_SUM.add(bd);
						}
					}else if("COMPANYANDPERSONAL_COUNT".equals(key)){
						if(value!=null){
							BigDecimal bd = new BigDecimal(value.toString());
							COMPANYANDPERSONAL_COUNT_SUM=COMPANYANDPERSONAL_COUNT_SUM.add(bd);
						}
					}
					
				}
			}
			mapObj.put("BRANCH_NAME", "合计");
			mapObj.put("USER_NAME", "");
			mapObj.put("FMIS_NO", "");
			mapObj.put("GJJ_MS_COMPANY_MONTHPAY", GJJ_MS_COMPANY_SUM);
			mapObj.put("GJJ_JS_COMPANY_MONTHPAY", GJJ_JS_COMPANY_SUM);
			mapObj.put("GJJ_MS_PERSONAL_MONTHPAY", GJJ_MS_PERSONAL_SUM);
			mapObj.put("GJJ_JS_PERSONAL_MONTHPAY", GJJ_JS_PERSONAL_SUM);
			mapObj.put("YLBX_COMPANY_MONTHPAY", YLBX_COMPANY_SUM);
			mapObj.put("YLBX_PERSONAL_MONTHPAY", YLBX_PERSONAL_SUM);
			mapObj.put("SYBX_COMPANY_MONTHPAY", SYBX_COMPANY_SUM);
			mapObj.put("SYBX_PERSONAL_MONTHPAY", SYBX_PERSONAL_SUM);
			mapObj.put("YLAOBX_COMPANY_MONTHPAY", YLAOBX_COMPANY_SUM);
			mapObj.put("YLAOBX_PERSONAL_MONTHPAY", YLAOBX_PERSONAL_SUM);
			mapObj.put("GSBX_COMPANY_MONTHPAY", GSBX_COMPANY_SUM);
			mapObj.put("SYUBX_COMPANY_MONTHPAY", SYUBX_COMPANY_SUM);
			mapObj.put("COMPANY_COUNT", COMPANY_COUNT_SUM);
			mapObj.put("PERSONAL_COUNT", PERSONAL_COUNT_SUM);
			mapObj.put("COMPANYANDPERSONAL_COUNT", COMPANYANDPERSONAL_COUNT_SUM);
			dataList.add(mapObj);
		}
		System.out.println("dataList::"+dataList);
		return dataList;
	}
}