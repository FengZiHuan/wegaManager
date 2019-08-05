package com.nantian.iwap.action.wagechanges;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.nantian.iwap.app.util.DateUtil;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;


public class MergeSummary extends TransactionBizAction{

	private static Logger log = Logger.getLogger(MergeSummary.class);
	

	@Override
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		String option = dtbHelper.getStringValue("option");
		if (StringUtil.isBlank(option)) {
			return query(dtbHelper);
		}
		if ("queryInfo".equals(option)) {
			return queryInfo(dtbHelper);
		}
		if ("add".equals(option)) {
			return add(dtbHelper);
		}
		if ("edit".equals(option)) {
			return edit(dtbHelper);
		}
		if ("save".equals(option)) {
			return save(dtbHelper);
		}
		if ("remove".equals(option)) {
			return remove(dtbHelper);
		}
		if ("submission".equals(option)) {
			return submission(dtbHelper);
		}
		return 0;
	}
	
	protected int query(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			int start = Integer.valueOf(dtbHelper.getStringValue("start"));
			int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
			String branchName = dtbHelper.getStringValue("branchName");
			String payMonth = dtbHelper.getStringValue("payMonth");
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String, Object>> dataList = null;
			String sqlStr="select f.branch_name branch_name,to_char(f.pay_month,'yyyy/MM') pay_month,to_char(max(f.submit_date),'yyyy/MM/dd') submit_date from fiveRisks_oneGold f where 1=1 and status='1' ";

			if (!"".equals(branchName)) {
				sqlStr += " and BRANCH_NAME like '%"+branchName+ "%' ";
			}
			
			if (!"".equals(payMonth)) {
				sqlStr += " and PAY_MONTH= to_date('"+ payMonth + "','yyyy/MM') ";
			}
			sqlStr +="group by f.branch_name,f.pay_month";
			System.out.println("-------"+sqlStr);
			dataList = dbBean.queryForList(sqlStr, page);
			dtbHelper.setRstData("rows", dataList);
			dtbHelper.setRstData("total", page.getTotalCount());
			flag = 1;
		} catch (Exception e) {
			log.error("分行信息查询出错", e);
			dtbHelper.setError("subsidymg-err-qry", "[分行信息查询出错]" + e.getMessage());
		}
		return flag;
	}

	protected int queryInfo(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			int start = Integer.valueOf(dtbHelper.getStringValue("start"));
			int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
			String userName = dtbHelper.getStringValue("userName");
			String fmisNo = dtbHelper.getStringValue("fmisNo");
			String branchName = dtbHelper.getStringValue("branchName");
			String payMonth = dtbHelper.getStringValue("payMonth");
			String state = dtbHelper.getStringValue("state");
			String checkState = dtbHelper.getStringValue("checkState");

			DBAccessBean dbBean = DBAccessPool.getDbBean();
			PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String, Object>> dataList = null;
			String sqlStr=" select ID,USER_NAME,FMIS_NO,BRANCH_NAME,to_char(PAY_MONTH,'yyyy-MM') PAY_MONTH,(GJJ_MS_COMPANY_MONTHPAY+GJJ_JS_COMPANY_MONTHPAY) GJJ_COMPANY_MONTHPAY,"+
					"(GJJ_MS_PERSONAL_MONTHPAY+GJJ_JS_PERSONAL_MONTHPAY) GJJ_PERSONAL_MONTHPAY,YLBX_COMPANY_MONTHPAY,YLBX_PERSONAL_MONTHPAY,SYBX_COMPANY_MONTHPAY,SYBX_PERSONAL_MONTHPAY,"+
					"YLAOBX_COMPANY_MONTHPAY,YLAOBX_PERSONAL_MONTHPAY,GSBX_COMPANY_MONTHPAY,SYUBX_COMPANY_MONTHPAY,"+
					"COMPANY_COUNT,PERSONAL_COUNT,COMPANYANDPERSONAL_COUNT,STATUS,CHECKSTATUS from fiveRisks_oneGold where 1=1 ";
			
			if (!"".equals(userName)) {
				sqlStr += " and USER_NAME like '%" + userName + "%'";
			}

			if (!"".equals(fmisNo)) {
				sqlStr += " and FMIS_NO = '" + fmisNo+"'";
			}
			
			if (!"".equals(branchName)) {
				sqlStr += " and BRANCH_NAME like '%"+branchName+ "%' ";
			}
			
			if (!"".equals(payMonth)) {
				sqlStr += " and PAY_MONTH= to_date('"+ payMonth + "','yyyy/MM') ";
			}
			
			if (!"".equals(state)) {
				if("0".equals(state)){
					sqlStr += " and (STATUS = '" + state+"' or STATUS is null)" ;
				}else if("1".equals(state)){
					sqlStr += " and STATUS = '" + state+"'";
				}
				
			}
			
			if (!"".equals(checkState)) {
				if("0".equals(checkState)){
					sqlStr += " and (CHECKSTATUS = '" + checkState+"' or CHECKSTATUS is null)" ;
				}else if("1".equals(checkState)){
					sqlStr += " and CHECKSTATUS = '" + checkState+"'";
				}
				
			}
			System.out.println("-------"+sqlStr);
			dataList = dbBean.queryForList(sqlStr, page);
			dtbHelper.setRstData("rows", dataList);
			dtbHelper.setRstData("total", page.getTotalCount());
			flag = 1;
		} catch (Exception e) {
			log.error("交流补贴查询出错", e);
			dtbHelper.setError("subsidymg-err-qry", "[交流补贴查询出错]" + e.getMessage());
		}
		return flag;
	}
	protected int edit(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			String branchName = dtbHelper.getStringValue("branchName");
			String countMonth = dtbHelper.getStringValue("countMonth");
			Map<String,String> map = new HashMap<String, String>();
			map.put("branchName", branchName);
			map.put("countMonth", countMonth);
			dtbHelper.setRstData(map);
			flag = 2;
		} catch (Exception e) {
			log.error("查询出错", e);
			dtbHelper.setError("subsidymg-err-qry", "[查询出错]" + e.getMessage());
		}
		return flag;
	}

	protected int add(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String userName = dtbHelper.getStringValue("USER_NAME");
			String fmisNo = dtbHelper.getStringValue("FMIS_NO");
			String branchName = dtbHelper.getStringValue("BRANCH_NAME");
			String payMonth = dtbHelper.getStringValue("PAY_MONTH");
			String gjjcompay = dtbHelper.getStringValue("GJJ_COMPANY_MONTHPAY");
			String gjjperpay = dtbHelper.getStringValue("GJJ_PERSONAL_MONTHPAY");
			String ylbxcompay = dtbHelper.getStringValue("YLBX_COMPANY_MONTHPAY");
			String ylbxperpay = dtbHelper.getStringValue("YLBX_PERSONAL_MONTHPAY");
			String sybxcompay = dtbHelper.getStringValue("SYBX_COMPANY_MONTHPAY");
			String sybxperpay = dtbHelper.getStringValue("SYBX_PERSONAL_MONTHPAY");
			String ylaobxcompay = dtbHelper.getStringValue("YLAOBX_COMPANY_MONTHPAY");
			String ylaobxperpay = dtbHelper.getStringValue("YLAOBX_PERSONAL_MONTHPAY");
			String gsbxcompay = dtbHelper.getStringValue("GSBX_COMPANY_MONTHPAY");
			String gsbxperpay = dtbHelper.getStringValue("GSBX_PERSONAL_MONTHPAY");
			String syubxcompay = dtbHelper.getStringValue("SYUBX_COMPANY_MONTHPAY");
			String syubxperpay = dtbHelper.getStringValue("SYUBX_PERSONAL_MONTHPAY");
			/*String companyCount = dtbHelper.getStringValue("COMPANY_COUNT");
			String personalCount = dtbHelper.getStringValue("PERSONAL_COUNT");
			String companyAndPersonalCount = dtbHelper.getStringValue("COMPANYANDPERSONAL_COUNT");*/
			BigDecimal companyCount = null;	 //单位合计
			BigDecimal personalCount = null;	//个人合计
			BigDecimal companyAndPersonalCount = null;	 //单位和个人合计
			BigDecimal gjjcom = new BigDecimal(gjjcompay);
			BigDecimal gjjper = new BigDecimal(gjjperpay);
			BigDecimal ylbxcom = new BigDecimal(ylbxcompay);
			BigDecimal ylbxper = new BigDecimal(ylbxperpay);
			BigDecimal sybxcom = new BigDecimal(sybxcompay);
			BigDecimal sybxper = new BigDecimal(sybxperpay);
			BigDecimal ylaobxcom = new BigDecimal(ylaobxcompay);
			BigDecimal ylaobxper = new BigDecimal(ylaobxperpay);
			BigDecimal gsbxcom = new BigDecimal(gsbxcompay);
			BigDecimal gsbxper = new BigDecimal(gsbxperpay);
			BigDecimal syubxcom = new BigDecimal(syubxcompay);
			BigDecimal syubxper = new BigDecimal(syubxperpay);
			companyCount=gjjcom.add(ylbxcom).add(sybxcom).add(ylaobxcom).add(gsbxcom).add(syubxcom);
			personalCount=gjjper.add(ylbxper).add(sybxper).add(ylaobxper).add(gsbxper).add(syubxper);
			companyAndPersonalCount=companyCount.add(personalCount);
			//维护公积金最高免税金额 2500(免税）
			double gjj=2500.0;
			//单位月缴费金额(免税）
			double gjjmscompay=0.0;	
			//单位月缴费金额(计税）
			double gjjjscompay=0.0;			
			//个人月缴费金额(免税）
			double gjjmspersonal=0.0;
			//个人月缴费金额(计税）
			double gjjjspersonal=0.0;
			if(!StringUtil.isBlank(gjjcompay)){
				BigDecimal b1 = new BigDecimal(gjjcompay);
				BigDecimal b2 = new BigDecimal(gjj);
				if(b1.doubleValue()>gjjmscompay){
					gjjmscompay=gjj;
					gjjjscompay=b1.subtract(b2).doubleValue();
				}else{
					gjjmscompay=b1.doubleValue();
					gjjjscompay=0.0;
				}
			}
			if(!StringUtil.isBlank(gjjperpay)){
				BigDecimal b1 = new BigDecimal(gjjperpay);
				BigDecimal b2 = new BigDecimal(gjj);
				if(b1.doubleValue()>gjjmscompay){
					gjjmspersonal=gjj;
					gjjjspersonal=b1.subtract(b2).doubleValue();
				}else{
					gjjmspersonal=b1.doubleValue();
					gjjjspersonal=0.0;
				}
			}
			String sqlStr = "select USER_NAME from fiveRisks_oneGold where  FMIS_NO = ?";
			DataObject result = dbBean.executeSingleQuery(sqlStr, fmisNo);
			if (result != null) {
				dbBean.executeRollBack();
				log.warn("五险一金新增出错：该用户已存在!");
				dtbHelper.setError("usermg-err-add-001", "[五险一金新增出错]该用户已存在!");
				return flag;
			}
			StringBuffer sbf=new StringBuffer();
			sbf.append("INSERT INTO fiveRisks_oneGold(USER_NAME,FMIS_NO,BRANCH_NAME,PAY_MONTH,GJJ_MS_COMPANY_MONTHPAY,GJJ_JS_COMPANY_MONTHPAY,");
			sbf.append(" GJJ_MS_PERSONAL_MONTHPAY,GJJ_JS_PERSONAL_MONTHPAY, YLBX_COMPANY_MONTHPAY, YLBX_PERSONAL_MONTHPAY, SYBX_COMPANY_MONTHPAY,");
			sbf.append("SYBX_PERSONAL_MONTHPAY, YLAOBX_COMPANY_MONTHPAY,YLAOBX_PERSONAL_MONTHPAY, GSBX_COMPANY_MONTHPAY, GSBX_PERSONAL_MONTHPAY, SYUBX_COMPANY_MONTHPAY,");
			sbf.append("SYUBX_PERSONAL_MONTHPAY, COMPANY_COUNT, PERSONAL_COUNT,COMPANYANDPERSONAL_COUNT) ");
			sbf.append(" values (?, ?, ?, to_date(?,'yyyy/MM'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			dbBean.executeUpdate(sbf.toString(), userName, fmisNo, branchName, payMonth, gjjmscompay, gjjjscompay,gjjmspersonal,gjjjspersonal,ylbxcom.doubleValue(),
					ylbxper.doubleValue(),sybxcom.doubleValue(),sybxper.doubleValue(),ylaobxcom.doubleValue(),ylaobxper.doubleValue(),gsbxcom.doubleValue(),gsbxper.doubleValue(),
					syubxcom.doubleValue(),syubxper.doubleValue(),companyCount.doubleValue(),personalCount.doubleValue(),companyAndPersonalCount.doubleValue());

			dbBean.executeCommit();
			flag = 1;
		} catch (Exception e) {
			log.error("交流补贴新增出错", e);
			dtbHelper.setError("usermg-err-add-002", "[交流补贴新增出错]" + e.getMessage());
			try {
				if (dbBean != null) {
					dbBean.executeRollBack();
				}
			} catch (Exception e2) {
				log.error("数据库回滚出错", e2);
			}
		}
		return flag;
	}

	protected int save(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String userName = dtbHelper.getStringValue("USER_NAME");
			String fmisNo = dtbHelper.getStringValue("FMIS_NO");
			String branchName = dtbHelper.getStringValue("BRANCH_NAME");
			String payMonth = dtbHelper.getStringValue("PAY_MONTH");
			String gjjcompay = dtbHelper.getStringValue("GJJ_COMPANY_MONTHPAY");
			String gjjperpay = dtbHelper.getStringValue("GJJ_PERSONAL_MONTHPAY");
			String ylbxcompay = dtbHelper.getStringValue("YLBX_COMPANY_MONTHPAY");
			String ylbxperpay = dtbHelper.getStringValue("YLBX_PERSONAL_MONTHPAY");
			String sybxcompay = dtbHelper.getStringValue("SYBX_COMPANY_MONTHPAY");
			String sybxperpay = dtbHelper.getStringValue("SYBX_PERSONAL_MONTHPAY");
			String ylaobxcompay = dtbHelper.getStringValue("YLAOBX_COMPANY_MONTHPAY");
			String ylaobxperpay = dtbHelper.getStringValue("YLAOBX_PERSONAL_MONTHPAY");
			String gsbxcompay = dtbHelper.getStringValue("GSBX_COMPANY_MONTHPAY");
			String gsbxperpay = dtbHelper.getStringValue("GSBX_PERSONAL_MONTHPAY");
			String syubxcompay = dtbHelper.getStringValue("SYUBX_COMPANY_MONTHPAY");
			String syubxperpay = dtbHelper.getStringValue("SYUBX_PERSONAL_MONTHPAY");
			BigDecimal companyCount = null;	 //单位合计
			BigDecimal personalCount = null;	//个人合计
			BigDecimal companyAndPersonalCount = null;	 //单位和个人合计
			BigDecimal gjjcom = new BigDecimal(gjjcompay);
			BigDecimal gjjper = new BigDecimal(gjjperpay);
			BigDecimal ylbxcom = new BigDecimal(ylbxcompay);
			BigDecimal ylbxper = new BigDecimal(ylbxperpay);
			BigDecimal sybxcom = new BigDecimal(sybxcompay);
			BigDecimal sybxper = new BigDecimal(sybxperpay);
			BigDecimal ylaobxcom = new BigDecimal(ylaobxcompay);
			BigDecimal ylaobxper = new BigDecimal(ylaobxperpay);
			BigDecimal gsbxcom = new BigDecimal(gsbxcompay);
			BigDecimal gsbxper = new BigDecimal(gsbxperpay);
			BigDecimal syubxcom = new BigDecimal(syubxcompay);
			BigDecimal syubxper = new BigDecimal(syubxperpay);
			companyCount=gjjcom.add(ylbxcom).add(sybxcom).add(ylaobxcom).add(gsbxcom).add(syubxcom);
			personalCount=gjjper.add(ylbxper).add(sybxper).add(ylaobxper).add(gsbxper).add(syubxper);
			companyAndPersonalCount=companyCount.add(personalCount);
			//维护公积金最高免税金额 2500(免税）
			double gjj=2500.0;
			//单位月缴费金额(免税）
			double gjjmscompay=0.0;	
			//单位月缴费金额(计税）
			double gjjjscompay=0.0;			
			//个人月缴费金额(免税）
			double gjjmspersonal=0.0;
			//个人月缴费金额(计税）
			double gjjjspersonal=0.0;
			if(!StringUtil.isBlank(gjjcompay)){
				BigDecimal b1 = new BigDecimal(gjjcompay);
				BigDecimal b2 = new BigDecimal(gjj);
				if(b1.doubleValue()>gjjmscompay){
					gjjmscompay=gjj;
					gjjjscompay=b1.subtract(b2).doubleValue();
				}else{
					gjjmscompay=b1.doubleValue();
					gjjjscompay=0.0;
				}
			}
			if(!StringUtil.isBlank(gjjperpay)){
				BigDecimal b1 = new BigDecimal(gjjperpay);
				BigDecimal b2 = new BigDecimal(gjj);
				if(b1.doubleValue()>gjjmscompay){
					gjjmspersonal=gjj;
					gjjjspersonal=b1.subtract(b2).doubleValue();
				}else{
					gjjmspersonal=b1.doubleValue();
					gjjjspersonal=0.0;
				}
			}
			StringBuffer sbf=new StringBuffer();
			sbf.append("UPDATE fiveRisks_oneGold set USER_NAME=?,FMIS_NO=?,BRANCH_NAME=?,PAY_MONTH=to_date(?,'yyyy/MM'),GJJ_MS_COMPANY_MONTHPAY=?,GJJ_JS_COMPANY_MONTHPAY=?,");
			sbf.append(" GJJ_MS_PERSONAL_MONTHPAY=?,GJJ_JS_PERSONAL_MONTHPAY=?, YLBX_COMPANY_MONTHPAY=?, YLBX_PERSONAL_MONTHPAY=?, SYBX_COMPANY_MONTHPAY=?,");
			sbf.append("SYBX_PERSONAL_MONTHPAY=?, YLAOBX_COMPANY_MONTHPAY=?,YLAOBX_PERSONAL_MONTHPAY=?, GSBX_COMPANY_MONTHPAY=?, GSBX_PERSONAL_MONTHPAY=?, SYUBX_COMPANY_MONTHPAY=?,");
			sbf.append("SYUBX_PERSONAL_MONTHPAY=?, COMPANY_COUNT=?, PERSONAL_COUNT=?,COMPANYANDPERSONAL_COUNT=? ");
			sbf.append(" where FMIS_NO=?");
			
			dbBean.executeUpdate(sbf.toString(), userName, fmisNo, branchName, payMonth, gjjmscompay, gjjjscompay,gjjmspersonal,gjjjspersonal,ylbxcom.doubleValue(),
					ylbxper.doubleValue(),sybxcom.doubleValue(),sybxper.doubleValue(),ylaobxcom.doubleValue(),ylaobxper.doubleValue(),gsbxcom.doubleValue(),gsbxper.doubleValue(),
					syubxcom.doubleValue(),syubxper.doubleValue(),companyCount.doubleValue(),personalCount.doubleValue(),companyAndPersonalCount.doubleValue(),fmisNo);
			dbBean.executeCommit();
			flag = 1;
		} catch (Exception e) {
			log.error("交流补贴保存出错", e);
			dtbHelper.setError("usermg-err-sv", "[交流补贴保存出错]" + e.getMessage());
		}
		return flag;
	}

	protected int remove(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String userids = dtbHelper.getStringValue("userids");
			String[] idarr = userids.split(",");
			int u_cnt = 0;
			int s_cnt = 0;
			for (String id : idarr) {
				if (id == null || "".equals(id.trim())) {
					continue;
				}
				u_cnt++;
				int i = dbBean.executeUpdate("delete from exchange_subsidy_info where ID = ?", id);
				if (i == 1) {
					s_cnt++;
				}
			}
			dbBean.executeCommit();
			if (u_cnt != s_cnt) {
				log.warn("交流补贴删除出错:删除失败" + (u_cnt - s_cnt) + "条");
				dtbHelper.setError("usermg-err-rm-001", "[交流补贴删除出错]删除失败" + (u_cnt - s_cnt) + "条");
			} else {
				flag = 1;
			}
		} catch (Exception e) {
			log.error("交流补贴删除出错", e);
			dtbHelper.setError("usermg-err-rm-002", "[交流补贴删除出错]" + e.getMessage());
			try {
				if (dbBean != null) {
					dbBean.executeRollBack();
				}
			} catch (Exception e2) {
				log.error("数据库回滚出错", e2);
			}
		}
		return flag;
	}
	/**
	 * 提交员工五险一金
	 * @param dtbHelper
	 * @return
	 * @throws BizActionException
	 */
	protected int submission(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String userids = dtbHelper.getStringValue("userids");
			String[] idarr = userids.split(",");
			int u_cnt = 0;
			int s_cnt = 0;
			String sysdate=DateUtil.format(new Date(), "yyyy/MM/dd");
			for (String id : idarr) {
				if (id == null || "".equals(id.trim())) {
					continue;
				}
				if (id == null || "{{value}}".equals(id.trim())) {
					continue;
				}
				u_cnt++;
				int i = dbBean.executeUpdate("update fiveRisks_oneGold set STATUS='1',SUBMIT_DATE=to_date(?,'yyyy/MM/dd')  where ID = ?",sysdate, id);
				if (i == 1) {
					s_cnt++;
				}
			}
			dbBean.executeCommit();
			if (u_cnt != s_cnt) {
				log.warn("五险一金提交出错:提交失败" + (u_cnt - s_cnt) + "条");
				dtbHelper.setError("usermg-err-rm-001", "[五险一金提交出错]提交失败" + (u_cnt - s_cnt) + "条");
			} else {
				flag = 1;
			}
		} catch (Exception e) {
			log.error("五险一金提交出错", e);
			dtbHelper.setError("usermg-err-rm-002", "[五险一金提交出错]" + e.getMessage());
			try {
				if (dbBean != null) {
					dbBean.executeRollBack();
				}
			} catch (Exception e2) {
				log.error("数据库回滚出错", e2);
			}
		}
		return flag;
	}

	
}
