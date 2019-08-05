package com.nantian.iwap.app.action.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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

/**
 * ClassName: UserMgAction <br/>
 * Function: 获取用户菜单<br/>
 * date: 2016年3月2日15:18:49 <br/>
 * 
 * @author wjj
 * @version
 * @since JDK 1.7 Copyright (c) 2016, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class UserMgAction extends TransactionBizAction {

	private static Logger log = Logger.getLogger(UserMgAction.class);
	private String encryptClazz = "com.nantian.iwap.app.util.DefaultEncrypt";// 默认加密方式
	private String insertSqlId;

	private static List<String> actionType = new ArrayList();

	static {
		actionType.add("insert");
	}

	  
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
		if ("query_grant".equals(option)) {
			return query_grant(dtbHelper);
		}
		if ("save_grant".equals(option)) {
			return save_grant(dtbHelper);
		}
		return 0;
	}

	protected int query(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			int start = Integer.valueOf(dtbHelper.getStringValue("start"));
			int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
			String deptId = "%" + dtbHelper.getStringValue("deptId") + "%";
			String userId = "%" + dtbHelper.getStringValue("userId") + "%";
			String userNm = "%" + dtbHelper.getStringValue("userNm") + "%";
			String userStatus = "%" + dtbHelper.getStringValue("userStatus") + "%";
			String _deptid = dtbHelper.getStringValue("_deptid");
			String deptlevel = null;
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			String sqlStr = "select org_lvl from sys_org where org_id='" + _deptid + "'";
			DataObject result = dbBean.executeSingleQuery(sqlStr);
			if (result != null) {
				deptlevel = result.getValue("org_lvl");
			}

			PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String, Object>> dataList = null;
			if (deptlevel == null || "".equals(deptlevel)) {
				/*sqlStr = "select * from (select a.acct_id, a.acct_status, a.acct_nm,a.org_id,o.org_nm, a.last_login_tm,a.acct_phone,a.acct_addr,a.acct_zipcode,a.acct_email,a.acct_ver_nm from sys_account a,sys_org o "
						+ "where a.org_id=o.org_id and a.org_id like ? and a.acct_id like ? and a.acct_nm like ? and a.acct_status like ? and o.org_path like ?) t_usermgtb";*/
				sqlStr = "select a.acct_id, a.acct_status, a.acct_nm,a.org_id,o.org_nm, a.last_login_tm,a.acct_phone,a.acct_addr,a.acct_zipcode,a.acct_email,a.acct_ver_nm from sys_account a,sys_org o "
						+ "where a.org_id=o.org_id and a.org_id like ? and a.acct_id like ? and a.acct_nm like ? and a.acct_status like ? and o.org_path like ?";
				dataList = dbBean.queryForList(sqlStr, page, deptId, userId, userNm, userStatus, "%" + _deptid + "%");
			} else {
				sqlStr = "select * from (select a.acct_id, a.acct_status, a.acct_nm,a.org_id,o.org_nm, a.last_login_tm,a.acct_phone,a.acct_addr,a.acct_zipcode,a.acct_email,a.acct_ver_nm from sys_account a,sys_org o "
						+ "where a.org_id=o.org_id and a.org_id like ? and a.acct_id like ? and a.acct_nm like ? and a.acct_status like ? and o.org_path like ? and o.org_lvl >= ?) t_usermgtb";
				dataList = dbBean.queryForList(sqlStr, page, deptId, userId, userNm, userStatus, "%" + _deptid + "%",
						deptlevel);
			}
			/*Map<String,Object> map = new HashMap<String,Object>();
			map.put("xiaoqiang", "xixi");
			dtbHelper.setRstData(map);*/
			
			dtbHelper.setRstData("rows", dataList);
			dtbHelper.setRstData("total", page.getTotalCount());
			flag = 1;
		} catch (Exception e) {
			log.error("用户查询出错", e);
			dtbHelper.setError("usermg-err-qry", "[用户查询出错]" + e.getMessage());
		}
		return flag;
	}

	protected int add(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String acct_id = dtbHelper.getStringValue("ACCT_ID");
			String acct_nm = dtbHelper.getStringValue("ACCT_NM");
			String org_id = dtbHelper.getStringValue("ORG_ID");
			String acct_phone = dtbHelper.getStringValue("ACCT_PHONE");
			String acct_addr = dtbHelper.getStringValue("ACCT_ADDR");
			String acct_zipcode = dtbHelper.getStringValue("ACCT_ZIPCODE");
			String acct_email = dtbHelper.getStringValue("ACCT_EMAIL");
			String acct_ver_nm = dtbHelper.getStringValue("ACCT_VER_NM");
			String acct_status = dtbHelper.getStringValue("ACCT_STATUS");
			PasswordEncrypt encrypt = (PasswordEncrypt) Class.forName(encryptClazz).newInstance();
			String acct_pwd = encrypt.encryptPassword(acct_id, "123456");// 默认密码
			String acct_crt = dtbHelper.getStringValue("_userid");
			dtbHelper.setObjectValue("acct_id", acct_id);
			dtbHelper.setObjectValue("acct_nm", acct_nm);
			dtbHelper.setObjectValue("org_id", org_id);
			dtbHelper.setObjectValue("acct_phone", acct_phone);
			dtbHelper.setObjectValue("acct_addr", acct_addr);
			dtbHelper.setObjectValue("acct_zipcode", acct_zipcode);
			dtbHelper.setObjectValue("acct_email", acct_email);
			dtbHelper.setObjectValue("acct_ver_nm", acct_ver_nm);
			dtbHelper.setObjectValue("acct_status", acct_status);
			dtbHelper.setObjectValue("acct_pwd", acct_pwd);
			dtbHelper.setObjectValue("acct_crt", acct_crt);
			dtbHelper.setObjectValue("acct_ver_nm", acct_ver_nm);
			
			String sqlStr = "select acct_id from sys_account where acct_id = ?";
			DataObject result = dbBean.executeSingleQuery(sqlStr, acct_id);
			if (result != null) {
				dbBean.executeRollBack();
				log.warn("用户新增出错：该用户ID已存在!");
				dtbHelper.setError("usermg-err-add-001", "[用户新增出错]该用户ID已存在!");
				return flag;
			}
			
			int insertResult=IWAPBatisFactory.getInstance().getIwapBatisPool().insert(this.insertSqlId, dtbHelper);
			System.out.println("insertResult::"+insertResult);
			/*sqlStr = "INSERT INTO sys_account(acct_id,acct_pwd,acct_status,acct_nm,org_id,acct_phone,acct_addr,acct_zipcode,acct_email,acct_ver_nm,acct_crt_tm,acct_crt) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
			dbBean.executeUpdate(sqlStr, acct_id, acct_pwd, acct_status, acct_nm, org_id, acct_phone, acct_addr,
					acct_zipcode, acct_email, acct_ver_nm, DateUtil.getDate("2019-05-12","yyyy-MM-dd"), acct_crt);
*/
			sqlStr = "delete from sys_acct_role where acct_id=?";
			dbBean.executeUpdate(sqlStr, acct_id);

			dbBean.executeCommit();
			flag = 1;
		} catch (Exception e) {
			log.error("用户新增出错", e);
			dtbHelper.setError("usermg-err-add-002", "[用户新增出错]" + e.getMessage());
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
			String acct_id = dtbHelper.getStringValue("acct_id");
			String acct_nm = dtbHelper.getStringValue("acct_nm");
			String org_id = dtbHelper.getStringValue("org_id");
			String acct_phone = dtbHelper.getStringValue("acct_phone");
			String acct_addr = dtbHelper.getStringValue("acct_addr");
			String acct_zipcode = dtbHelper.getStringValue("acct_zipcode");
			String acct_email = dtbHelper.getStringValue("acct_email");
			String acct_ver_nm = dtbHelper.getStringValue("acct_ver_nm");
			String acct_status = dtbHelper.getStringValue("acct_status");
			String acct_mdf = dtbHelper.getStringValue("_userid");

			String sqlStr = "UPDATE sys_account set acct_status=?,acct_nm=?,org_id=?,acct_phone=?,acct_addr=?,acct_zipcode=?,acct_email=?,acct_ver_nm=?,acct_mdf_tm=?,acct_mdf=? where acct_id=?";
			dbBean.executeUpdate(sqlStr, acct_status, acct_nm, org_id, acct_phone, acct_addr, acct_zipcode, acct_email,
					acct_ver_nm, DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"), acct_mdf, acct_id);

			dbBean.executeCommit();
			flag = 1;
		} catch (Exception e) {
			log.error("用户保存出错", e);
			dtbHelper.setError("usermg-err-sv", "[用户保存出错]" + e.getMessage());
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
			String _userid = dtbHelper.getStringValue("_userid");
			String[] userarr = userids.split(",");
			int u_cnt = 0;
			int s_cnt = 0;
			for (String user : userarr) {
				if (user == null || "".equals(user.trim())) {
					continue;
				}
				if (_userid.trim().equals(user.trim())) {
					continue;
				}
				u_cnt++;
				int i = dbBean.executeUpdate("delete from sys_account where acct_id = ?", user);
				dbBean.executeUpdate("delete from sys_acct_role where acct_id = ?", user);
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

	protected int query_grant(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			String acct_id = dtbHelper.getStringValue("acct_id");
			String sqlStr = "select role_id from sys_acct_role where acct_id = ?";
			List<Map<String, Object>> dataList = dbBean.queryForList(sqlStr, acct_id);
			dtbHelper.setRstData("grants", dataList);
			flag = 1;
		} catch (Exception e) {
			log.error("用户授权查询出错", e);
			dtbHelper.setError("usermg-err-qg", "[用户授权查询出错]" + e.getMessage());
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	protected int save_grant(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			List<String> acct_role_list = dtbHelper.getListValue("acct_role_list");
			String acct_id = dtbHelper.getStringValue("acct_id");
			dbBean.executeUpdate("delete from sys_acct_role where acct_id = ?", acct_id);
			String sqlStr = "INSERT INTO sys_acct_role(role_id,acct_id) VALUES (?,?)";
			for (String role : acct_role_list) {
				if (role == null || "".equals(role.trim())) {
					continue;
				}
				dbBean.executeUpdate(sqlStr, role, acct_id);
			}
			dbBean.executeCommit();
			flag = 1;
		} catch (Exception e) {
			log.error("用户授权保存出错", e);
			dtbHelper.setError("usermg-err-sg", "[用户授权保存出错]" + e.getMessage());
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

	public String getInsertSqlId() {
		return insertSqlId;
	}

	public void setInsertSqlId(String insertSqlId) {
		this.insertSqlId = insertSqlId;
	}

}
