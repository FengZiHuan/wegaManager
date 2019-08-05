package com.nantian.iwap.action.wagechanges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.app.action.system.UserMgAction;
import com.nantian.iwap.app.util.PasswordEncrypt;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.DateUtil;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.ibatis.IWAPBatisFactory;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;

public class ExchangeSubsidy extends TransactionBizAction{

	private static Logger log = Logger.getLogger(ExchangeSubsidy.class);
	

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
		
		return 0;
	}
	
	protected int query(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			int start = Integer.valueOf(dtbHelper.getStringValue("start"));
			int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
			String userName = dtbHelper.getStringValue("userName");
			String fmisNo = dtbHelper.getStringValue("fmisNo");
			String liveCity = dtbHelper.getStringValue("liveCity");
			String workCity = dtbHelper.getStringValue("workCity");
			String state = dtbHelper.getStringValue("state");
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String, Object>> dataList = null;
			String sqlStr=" select ID,USER_NAME,FMIS_NO,LIVE_CITY,WORK_CITY,DISTANCE,SUBSIDY_AMOUNT,"+
					"to_char(BEGIN_DATE,'yyyy-MM-dd') BEGIN_DATE, to_char(END_DATE,'yyyy-MM-dd') END_DATE from exchange_subsidy_info where 1=1 ";
			
			if (!"".equals(userName)) {
				sqlStr += " and USER_NAME like '%" + userName + "%'";
			}

			if (!"".equals(fmisNo)) {
				sqlStr += " and FMIS_NO = '" + fmisNo+"'";
			}
			
			if (!"".equals(liveCity)) {
				sqlStr += " and LIVE_CITY like '%"+liveCity+ "%' ";
			}
			
			if (!"".equals(workCity)) {
				sqlStr += " and WORK_CITY like '%"+workCity+ "%' ";
			}
			
			if (!"".equals(state)) {
				if("0".equals(state)){
					
				}else if("1".equals(state)){
					sqlStr += " and END_DATE is null";
				}else if("2".equals(state)){
					sqlStr += " and END_DATE is not null";
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

	protected int add(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String userName = dtbHelper.getStringValue("USER_NAME");
			String fmisNo = dtbHelper.getStringValue("FMIS_NO");
			String liveCity = dtbHelper.getStringValue("LIVE_CITY");
			String workCity = dtbHelper.getStringValue("WORK_CITY");
			String distance = dtbHelper.getStringValue("DISTANCE");
			String subsidyAmount = dtbHelper.getStringValue("SUBSIDY_AMOUNT");
			String beginDate = dtbHelper.getStringValue("BEGIN_DATE");
			String endDate = dtbHelper.getStringValue("END_DATE");
			
			
			String sqlStr = "select USER_NAME from exchange_subsidy_info where USER_NAME = ? and FMIS_NO = ?";
			DataObject result = dbBean.executeSingleQuery(sqlStr, userName, fmisNo);
			if (result != null) {
				dbBean.executeRollBack();
				log.warn("交流补贴新增出错：该用户已存在!");
				dtbHelper.setError("usermg-err-add-001", "[交流补贴新增出错]该用户已存在!");
				return flag;
			}
			
			sqlStr = "INSERT INTO exchange_subsidy_info(USER_NAME,FMIS_NO,LIVE_CITY,WORK_CITY,DISTANCE,SUBSIDY_AMOUNT,BEGIN_DATE,END_DATE) VALUES (?,?,?,?,?,?,to_date(?,'yyyy/MM/dd'),to_date(?,'yyyy/MM/dd'))";
			dbBean.executeUpdate(sqlStr, userName, fmisNo, liveCity, workCity, distance, subsidyAmount,beginDate,endDate);

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
			String endDate = dtbHelper.getStringValue("END_DATE");

			String sqlStr = "UPDATE exchange_subsidy_info set END_DATE=to_date(?,'yyyy/MM/dd') where USER_NAME=? and FMIS_NO=?";
			dbBean.executeUpdate(sqlStr, endDate, userName, fmisNo);

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
				if (id == null || "{{value}}".equals(id.trim())) {
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

	
	
}
