package com.nantian.iwap.action.wagechanges;


import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class PerformanceManagementMgAction extends TransactionBizAction{

	private static Logger log = Logger.getLogger(PerformanceManagementMgAction.class);
	

	@Override
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		String option = dtbHelper.getStringValue("option");
		if (StringUtil.isBlank(option)) {
			return query(dtbHelper);
		}
		if ("add".equals(option)) {
			return add(dtbHelper);
		}
		if ("save".equals(option)) {
			return save(dtbHelper);
		}
		if ("remove".equals(option)) {
			return remove(dtbHelper);
		}
		if ("send".equals(option)) {
			return send(dtbHelper);
		}
		return 0;
	}
//分发或者取消分发
	private int send(DTBHelper dtbHelper) {
		int flag = 0;
		DBAccessBean dbBean = null;
		String sql="";
		List<Map<String, Object>> dataList = new ArrayList<>();
		try {
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String userids = dtbHelper.getStringValue("holidayids");
			String status = dtbHelper.getStringValue("status");
			if (userids.equals("")||userids.contains("{{value}}"))  {
				sql= "update NH_JXJJ set jx_status=?";
				 dbBean.executeUpdate(sql,status);
				 dbBean.executeCommit();
				 flag=1;

			} else {
				String[] userarr = userids.split(",");
				for (String user : userarr) {
					if (user == null || "".equals(user.trim())) {
						continue;
					}

					sql="update NH_JXJJ set jx_status=? where jx_id=?";
					dbBean.executeUpdate(sql,status,user);
					flag=1;
				}
			}
		} catch (Exception e) {
			log.error("分发出错", e);
			dtbHelper.setError("usermg-err-add-002", "[分发出错]" + e.getMessage());
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

	protected int query(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			int start = Integer.valueOf(dtbHelper.getStringValue("start"));
			int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
			String sendState = dtbHelper.getStringValue("sendState");
			String branchName = dtbHelper.getStringValue("branchName");
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String, Object>> dataList = null;
			String sqlStr="select * from nh_jxjj";
			if (!"".equals(sendState)) {
				sqlStr += " where  jx_status = '" +sendState + "'";
			}
			if (!"".equals(branchName)&&!"".equals(sendState)) {
				sqlStr += " and JX_BRAND like '%"+branchName+ "%' ";
			}
			else if (!"".equals(branchName)){
				sqlStr += " where  JX_BRAND like '%"+branchName+ "%' ";

			}
			sqlStr+=" order by jx_id desc";

			System.out.println("-------"+sqlStr);
			dataList = dbBean.queryForList(sqlStr, page);
			dtbHelper.setRstData("rows", dataList);
			dtbHelper.setRstData("total", page.getTotalCount());
			flag = 1;
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
			String jx_year = dtbHelper.getStringValue("JX_YEAR");
			String jx_brand = dtbHelper.getStringValue("JX_BRAND");
			String jx_name1 = dtbHelper.getStringValue("JX_NAME1");
			String jx_hz1 = dtbHelper.getStringValue("JX_HZ1");
			String jx_fhz1 = dtbHelper.getStringValue("JX_FHZ1");
			String jx_sum1 = dtbHelper.getStringValue("JX_SUM1");
			String jx_name2 = dtbHelper.getStringValue("JX_NAME2");
			String jx_hz2 = dtbHelper.getStringValue("JX_HZ2");
			String jx_fhz2 = dtbHelper.getStringValue("JX_FHZ2");
			String jx_sum2= dtbHelper.getStringValue("JX_SUM2");
			String jx_name3 = dtbHelper.getStringValue("JX_NAME3");
			String jx_hz3 = dtbHelper.getStringValue("JX_HZ3");
			String jx_fhz3 = dtbHelper.getStringValue("JX_FHZ3");
			String jx_sum3 = dtbHelper.getStringValue("JX_SUM3");
			String jx_name4 = dtbHelper.getStringValue("JX_NAME4");
			String jx_hz4= dtbHelper.getStringValue("JX_HZ4");
			String jx_fhz4 = dtbHelper.getStringValue("JX_FHZ4");
			String jx_sum4 = dtbHelper.getStringValue("JX_SUM4");
			String jx_name5 = dtbHelper.getStringValue("JX_NAME5");
			String jx_hz5 = dtbHelper.getStringValue("JX_HZ5");
			String jx_fhz5 = dtbHelper.getStringValue("JX_FHZ5");
			String jx_sum5 = dtbHelper.getStringValue("JX_SUM5");
			String jx_name6 = dtbHelper.getStringValue("JX_NAME6");
			String jx_hz6 = dtbHelper.getStringValue("JX_HZ6");
			String jx_fhz6 = dtbHelper.getStringValue("JX_FHZ6");
			String jx_sum6 = dtbHelper.getStringValue("JX_SUM6");
			String sql= "insert into nh_jxjj(jx_year,jx_brand,jx_name1,jx_hz1,jx_fhz1,jx_sum1,jx_name2,jx_hz2,jx_fhz2,jx_sum2, " +
					"jx_name3,jx_hz3,jx_fhz3,jx_sum3,jx_name4,jx_hz4,jx_fhz4,jx_sum4,jx_name5,jx_hz5,jx_fhz5,jx_sum5, " +
					"jx_name6,jx_hz6,jx_fhz6,jx_sum6) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			dbBean.executeUpdate(sql,jx_year,jx_brand,jx_name1,jx_hz1,jx_fhz1,jx_sum1,jx_name2,jx_hz2,jx_fhz2,jx_sum2,
					jx_name3,jx_hz3,jx_fhz3,jx_sum3,jx_name4,jx_hz4,jx_fhz4,jx_sum4,jx_name5,jx_hz5,jx_fhz5,jx_sum5,
					jx_name6,jx_hz6,jx_fhz6,jx_sum6);
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

			String jx_year = dtbHelper.getStringValue("JX_YEAR");
			String jx_brand = dtbHelper.getStringValue("JX_BRAND");
			String jx_name1 = dtbHelper.getStringValue("JX_NAME1");
			String jx_hz1 = dtbHelper.getStringValue("JX_HZ1");
			String jx_fhz1 = dtbHelper.getStringValue("JX_FHZ1");
			String jx_sum1 = dtbHelper.getStringValue("JX_SUM1");
			String jx_name2 = dtbHelper.getStringValue("JX_NAME2");
			String jx_hz2 = dtbHelper.getStringValue("JX_HZ2");
			String jx_fhz2 = dtbHelper.getStringValue("JX_FHZ2");
			String jx_sum2= dtbHelper.getStringValue("JX_SUM2");
			String jx_name3 = dtbHelper.getStringValue("JX_NAME3");
			String jx_hz3 = dtbHelper.getStringValue("JX_HZ3");
			String jx_fhz3 = dtbHelper.getStringValue("JX_FHZ3");
			String jx_sum3 = dtbHelper.getStringValue("JX_SUM3");
			String jx_name4 = dtbHelper.getStringValue("JX_NAME4");
			String jx_hz4= dtbHelper.getStringValue("JX_HZ4");
			String jx_fhz4 = dtbHelper.getStringValue("JX_FHZ4");
			String jx_sum4 = dtbHelper.getStringValue("JX_SUM4");
			String jx_name5 = dtbHelper.getStringValue("JX_NAME5");
			String jx_hz5 = dtbHelper.getStringValue("JX_HZ5");
			String jx_fhz5 = dtbHelper.getStringValue("JX_FHZ5");
			String jx_sum5 = dtbHelper.getStringValue("JX_SUM5");
			String jx_name6 = dtbHelper.getStringValue("JX_NAME6");
			String jx_hz6 = dtbHelper.getStringValue("JX_HZ6");
			String jx_fhz6 = dtbHelper.getStringValue("JX_FHZ6");
			String jx_sum6 = dtbHelper.getStringValue("JX_SUM6");
			String sql= "update nh_jxjj set jx_year=?,jx_brand=?,jx_name1=?,jx_hz1=?,jx_fhz1=?,jx_sum1=?,jx_name2=?,jx_hz2=?,jx_fhz2=?,jx_sum2=?, " +
					"jx_name3=?,jx_hz3=?,jx_fhz3=?,jx_sum3=?,jx_name4=?,jx_hz4=?,jx_fhz4=?,jx_sum4=?,jx_name5=?,jx_hz5=?,jx_fhz5=?,jx_sum5=?, " +
					"jx_name6=?,jx_hz6=?,jx_fhz6=?,jx_sum6=? where jx_year=? and jx_brand=? ";
			dbBean.executeUpdate(sql,jx_name1,jx_hz1,jx_fhz1,jx_sum1,jx_name2,jx_hz2,jx_fhz2,jx_sum2,
					jx_name3,jx_hz3,jx_fhz3,jx_sum3,jx_name4,jx_hz4,jx_fhz4,jx_sum4,jx_name5,jx_hz5,jx_fhz5,jx_sum5,
					jx_name6,jx_hz6,jx_fhz6,jx_sum6,jx_year,jx_brand);
			dbBean.executeCommit();


			flag = 1;
		} catch (Exception e) {
			log.error("保存出错", e);
			dtbHelper.setError("usermg-err-sv", "[保存出错]" + e.getMessage());
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
			String[] userarr = userids.split(",");
			int u_cnt = 0;
			int s_cnt = 0;
			for (String user : userarr) {
				if (user == null || "".equals(user.trim())) {
					continue;
				}
				u_cnt++;
				int i = dbBean.executeUpdate("delete from NH_JXJJ  where jx_id = ?", user);
				if (i == 1) {
					s_cnt++;
				}
			}
			dbBean.executeCommit();
			if (u_cnt != s_cnt) {
				log.warn("用户删除出错:删除失败" + (u_cnt - s_cnt) + "条");
				dtbHelper.setError("usermg-err-rm-001", "[用户删除出错]删除失败" + (u_cnt - s_cnt) + "条");
			} else {
				flag = 1;
			}
		} catch (Exception e) {
			log.error("用户删除出错", e);
			dtbHelper.setError("usermg-err-rm-002", "[用户删除出错]" + e.getMessage());
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

	
}
