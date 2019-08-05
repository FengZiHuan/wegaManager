package com.nantian.iwap.action.wagechanges;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.app.util.DateUtil2;
import com.nantian.iwap.app.util.PasswordEncrypt;
import com.nantian.iwap.biz.actions.BatisAction;
import com.nantian.iwap.biz.actions.TransactionBatisAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.DateUtil;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.ibatis.IWAPBatisDBPool;
import com.nantian.iwap.ibatis.IWAPBatisFactory;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.security.cipher.impl.IwapMd5Encrypt;
import com.nantian.ofpiwap.common.DataMapDictionary;
/**
 * 处分情况
 */
public class PunishAction extends TransactionBatisAction {
	private static Logger log=Logger.getLogger(PunishAction.class);
	private String querySqlId;
	private String deleteSqlId;
	private String insertSqlId;
	private String queryStopSalarySqlId;
	private String updateSqlId;
	private String dtlSqlId;
	private static List<String> actionType = new ArrayList();
	private static Map<String,String> gradeMap = new HashMap<String,String>();
	static {
		actionType.add("query");
		actionType.add("delete");
		actionType.add("update");
		actionType.add("insert");
		actionType.add("detail");
		actionType.add("input_info");
		//添加等级
		gradeMap.put("0", "其它");
		gradeMap.put("1", "违规经济处理");
		gradeMap.put("2", "警告");
		gradeMap.put("3", "记过");
		gradeMap.put("4", "记大过");
		gradeMap.put("5", "降级");
		gradeMap.put("6", "撤职");
		gradeMap.put("7", "留用察看");
		gradeMap.put("8", "开除");
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
	    String actionType = dtbHelper.getStringValue("actionType");
	    if (StringUtil.isBlank(actionType)) {
	      actionType = "query";
	      log.warn("actionType not set then default [query]");
	    }
	    actionType = actionType.toLowerCase();
	    if (actionType.contains(actionType)) {
	      try {
	        Method method = getClass().getMethod(actionType, new Class[] { DTBHelper.class });
	        Object ret = method.invoke(this, new Object[] { dtbHelper });
	        return Integer.parseInt(ret.toString());
	      } catch (NoSuchMethodException e) {
	        log.error("unknow actionType [" + actionType + "]", e);
	      } catch (SecurityException e) {
	        log.error("SecurityException:", e);
	      } catch (IllegalAccessException e) {
	        log.error("IllegalAccessException:", e);
	      } catch (IllegalArgumentException e) {
	        log.error("IllegalArgumentException:", e);
	      } catch (InvocationTargetException e) {
	        log.error("execute " + actionType + " method error!", e.getTargetException());
	      }
	    } else {
	      log.error("unknow actionType [" + actionType + "]");
	      dtbHelper.setError("SYSERROR-91002", "错误参数请求!");
	    }
	    return 0;
	}

	  public int input_info(DTBHelper dtbHelper)
	  {
		String userId = dtbHelper.getStringValue("USER_ID");
		String department = dtbHelper.getStringValue("DEPARTMENT");
		String userName = dtbHelper.getStringValue("USER_NAME");
		String fmisNo = dtbHelper.getStringValue("FMIS_NO");
		String punishMatter = dtbHelper.getStringValue("PUNISH_MATTER");
		String punishGrade = dtbHelper.getStringValue("PUNISH_GRADE");
		String arriveDate = dtbHelper.getStringValue("DATE_ARRIVE");
		String monthSum = dtbHelper.getStringValue("PUNISH_MONTH_SUM");
		String beginDate = dtbHelper.getStringValue("BEGIN_DATE");
		String endDate = dtbHelper.getStringValue("END_DATE");
		String punishBasis = dtbHelper.getStringValue("PUNISH_BASIS");
		String remark = dtbHelper.getStringValue("REMARK");

		//停发绩效具体要求
		String jtyq=requestSplicing(punishGrade, punishMatter, monthSum, beginDate, punishBasis);
		List<String> ths = new ArrayList<String>(); 
		List<String> tds = new ArrayList<String>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ths=DateUtil2.getMonthBetween(beginDate, endDate);
		for(int i=0;i<ths.size();i++){
			tds.add(ths.get(i));
		}
		Map smap=null;
		String nfjxgzze="";
		Map<String,String> jemap=new HashMap<String,String>();
		Map<String,String> jxmap=new HashMap<String,String>();
		Map<String,String> njmap=new HashMap<String,String>();
		if(!StringUtil.isBlank(userId)){ //查询停发绩效工资对应月份详情
			smap=IWAPBatisFactory.getInstance().getIwapBatisPool().queryForList(this.queryStopSalarySqlId, dtbHelper).get(0);
			Object stopSalary=smap.get("STOP_PAYMENT_SALARY");
			Object stopjx=smap.get("STOP_JX_FLAG");
			Object stopnj=smap.get("STOP_NJ_FLAG");
			nfjxgzze=smap.get("NFJXGZZE")==null? "0" : smap.get("NFJXGZZE").toString();
			if(stopSalary!=null){
				String[] sarr=String.valueOf(stopSalary).split(",");
				for(int i=0;i<sarr.length;i++){
					String[] salaryArr=sarr[i].split("/");
					jemap.put(salaryArr[0],salaryArr[1]);
				}
			}
			if(stopjx!=null){
				String[] jarr=String.valueOf(stopjx).split(",");
				for(int i=0;i<jarr.length;i++){
					String[] jxArr=jarr[i].split("/");
					jxmap.put(jxArr[0],jxArr[1]);
				}
			}
			if(stopnj!=null){
				String[] narr=String.valueOf(stopnj).split(",");
				for(int i=0;i<narr.length;i++){
					String[] njArr=narr[i].split("/");
					njmap.put(njArr[0],njArr[1]);
				}
			}
			
		}
		
		for(int i=0;i<3;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("DEPARTMENT", department);
			map.put("USER_NAME", userName);
			map.put("FMIS_NO", fmisNo);
			map.put("JTYQ", jtyq);
			if(smap==null){
				map.put("NFJXGZZE", "");
			}else{
				map.put("NFJXGZZE", nfjxgzze);
			}
			map.put("tingfajixiao", "停发绩效工资");
			if(i==0){
				map.put("tingfa", "是否停发绩效工资");
				for(int j=0;j<ths.size();j++){
					map.put(ths.get(j), jxmap.get(ths.get(j)));
				}
			}else if(i==1){
				map.put("tingfa", "停发金额（元）");
				for(int j=0;j<ths.size();j++){
					map.put(ths.get(j), jemap.get(ths.get(j)));
				}
			}else if(i==2){
				map.put("tingfa", "是否暂停企业年金单位缴费部分");
				for(int j=0;j<ths.size();j++){
					map.put(ths.get(j), njmap.get(ths.get(j)));
				}
			}
			list.add(map);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("department", department);
		map.put("userName", userName);
		map.put("fmisNo", fmisNo);
		map.put("jtyq", jtyq);
		map.put("punishMatter", punishMatter);
		map.put("punishGrade", punishGrade);
		map.put("arriveDate", arriveDate);
		map.put("monthSum", monthSum);
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("punishBasis", punishBasis);
		map.put("remark", remark);
		map.put("ths", ths);
		map.put("tds", tds);
		map.put("list", list);
		dtbHelper.setRstData(map);
		return 2;
	  }
	  
	  public int insert(DTBHelper dtbHelper)
	  {
		String userId = dtbHelper.getStringValue("punishInfo.userId");
		String department = dtbHelper.getStringValue("punishInfo.department");
		String userName = dtbHelper.getStringValue("punishInfo.userName");
		String fmisNo = dtbHelper.getStringValue("punishInfo.fmisNo");
		String punishMatter = dtbHelper.getStringValue("punishInfo.punishMatter");
		String punishGrade = dtbHelper.getStringValue("punishInfo.punishGrade");
		String monthSum = dtbHelper.getStringValue("punishInfo.monthSum");
		String arriveDate = dtbHelper.getStringValue("punishInfo.arriveDate");
		String beginDate = dtbHelper.getStringValue("punishInfo.beginDate");
		String endDate = dtbHelper.getStringValue("punishInfo.endDate");
		String punishBasis = dtbHelper.getStringValue("punishInfo.punishBasis");
		String remark = dtbHelper.getStringValue("punishInfo.remark");
		String jtyq = dtbHelper.getStringValue("punishInfo.jtyq");
		//拟停发绩效工资总额
		Double ntfjxgzje = Double.parseDouble(dtbHelper.getStringValue("NFJXGZZE"));
		List<String> ths = new ArrayList<String>();
		List<String> jeList = new ArrayList<String>();
		String tfjeStr="";
		String tfjxgzStr="";
		String qynjStr="";
		//本月停发金额
		Double bytfje=0.0;
		ths=dtbHelper.getListValue("punishInfo.ths");
		if(ths!=null && ths.size()>0){
			for(int i=0;i<ths.size();i++){
				String je=dtbHelper.getStringValue(ths.get(i)+"-je");
				String tfjxgz=dtbHelper.getStringValue(ths.get(i)+"-jxgz");
				String qynj=dtbHelper.getStringValue(ths.get(i)+"-qynj");
				tfjeStr+=(ths.get(i)+"/"+je+",");
				tfjxgzStr+=(ths.get(i)+"/"+tfjxgz+",");
				qynjStr+=(ths.get(i)+"/"+qynj+",");
				bytfje+=Double.parseDouble(je);
			}
		}
		//停发绩效具体要求
		//String jtyq=requestSplicing(punishGrade, punishMatter, monthSum, beginDate, punishBasis);
	/*	StringBuffer jtyq = new StringBuffer();
		jtyq.append(punishGrade+"，处分期"+ths.size()+"个月，停"+ths.size()+"个月绩效，");
		jtyq.append(ths.get(0)+"-"+ths.get(ths.size()-1));*/
		
		dtbHelper.setObjectValue("DEPARTMENT",department);
		dtbHelper.setObjectValue("USER_NAME",userName);
		dtbHelper.setObjectValue("FMIS_NO",fmisNo);
		dtbHelper.setObjectValue("PUNISH_MATTER",punishMatter);
		dtbHelper.setObjectValue("PUNISH_GRADE",punishGrade);
		dtbHelper.setObjectValue("DATE_ARRIVE",arriveDate);
		dtbHelper.setObjectValue("PUNISH_MONTH_SUM",Integer.parseInt(monthSum));
		dtbHelper.setObjectValue("BEGIN_DATE",beginDate.substring(0, 7));
		dtbHelper.setObjectValue("END_DATE",endDate.substring(0, 7));
		dtbHelper.setObjectValue("PUNISH_BASIS",punishBasis);
		dtbHelper.setObjectValue("REMARK",remark);
		//执行工资月份
		//当月预发绩效工资标准
		//实际预发绩效工资
		dtbHelper.setObjectValue("STOP_PAYMENT_SALARY", tfjeStr.length()==0? "" : tfjeStr.substring(0, tfjeStr.length()-1));
		dtbHelper.setObjectValue("STOP_JX_FLAG", tfjxgzStr.length()==0? "" : tfjxgzStr.substring(0, tfjxgzStr.length()-1));
		dtbHelper.setObjectValue("STOP_NJ_FLAG", qynjStr.length()==0? "" : qynjStr.substring(0, qynjStr.length()-1));
		dtbHelper.setObjectValue("BYTFJE",bytfje);
		dtbHelper.setObjectValue("JTYQ",jtyq);
		dtbHelper.setObjectValue("NFJXGZZE",ntfjxgzje);
		//已停发绩效工资总额
		dtbHelper.setObjectValue("YTFJXGZZE",0.0);
		//待停发绩效工资总额
		dtbHelper.setObjectValue("DTFJXGZZE",0.0);
		List<String> list = new ArrayList<String>();
		list.add(userId);
		dtbHelper.setObjectValue("list",list);
		if(!StringUtil.isBlank(userId)){
			IWAPBatisFactory.getInstance().getIwapBatisPool().delete(this.deleteSqlId, dtbHelper);
		}
		IWAPBatisFactory.getInstance().getIwapBatisPool().insert(this.insertSqlId, dtbHelper);
		return 3;
		
	  }
	  
	  public int query(DTBHelper dtbHelper) {
			dtbHelper.setObjectValue("userName", dtbHelper.getStringValue("userName"));
			dtbHelper.setObjectValue("department", dtbHelper.getStringValue("departmentName"));
			dtbHelper.setObjectValue("beginDate", dtbHelper.getStringValue("beginTime"));
			dtbHelper.setObjectValue("endDate", dtbHelper.getStringValue("endTime"));
			String departmentName=dtbHelper.getStringValue("departmentName");
			String userName=dtbHelper.getStringValue("userName");
			String beginT=dtbHelper.getStringValue("beginTime");
			String endT=dtbHelper.getStringValue("endTime");
		    BatisAction action = new BatisAction();
		    action.setSqlStr(this.querySqlId);
		    return action.actionProcess(dtbHelper);
		  }

	  public int delete(DTBHelper dtbHelper) {
		  //改写
	    return IWAPBatisFactory.getInstance().getIwapBatisPool().delete(this.deleteSqlId, dtbHelper);
	  }

		
	  //要求拼接
	  public String requestSplicing(String grade,String punishMatter,String monthSum,
			  String beginDate,String punishBasis ){
		if("0".equals(grade)){
			return "";
		}else if("1".equals(grade)){
			return "违规经济处理("+punishMatter+")";
		}else{
			String gradeStr=gradeMap.get(grade); //处理类型
			return gradeStr+",停发"+monthSum+"个月绩效工资,执行时间"+beginDate+","+punishBasis;
		}
	  }

	public String getQuerySqlId() {
		return querySqlId;
	}

	public void setQuerySqlId(String querySqlId) {
		this.querySqlId = querySqlId;
	}

	public String getInsertSqlId() {
		return insertSqlId;
	}

	public void setInsertSqlId(String insertSqlId) {
		this.insertSqlId = insertSqlId;
	}

	public String getQueryStopSalarySqlId() {
		return queryStopSalarySqlId;
	}

	public void setQueryStopSalarySqlId(String queryStopSalarySqlId) {
		this.queryStopSalarySqlId = queryStopSalarySqlId;
	}

	public static List<String> getActionType() {
		return actionType;
	}

	public static void setActionType(List<String> actionType) {
		PunishAction.actionType = actionType;
	}

	public String getDeleteSqlId() {
		return deleteSqlId;
	}

	public void setDeleteSqlId(String deleteSqlId) {
		this.deleteSqlId = deleteSqlId;
	}

	public String getUpdateSqlId() {
		return updateSqlId;
	}

	public void setUpdateSqlId(String updateSqlId) {
		this.updateSqlId = updateSqlId;
	}

}
