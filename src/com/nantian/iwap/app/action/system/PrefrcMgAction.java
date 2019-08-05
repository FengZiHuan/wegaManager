package com.nantian.iwap.app.action.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.app.util.PasswordEncrypt;
import com.nantian.iwap.biz.flow.BaseBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;

import javassist.bytecode.LineNumberAttribute.Pc;

/**
 * ClassName: UserMgAction <br/>
 * Function: 获取用户菜单<br/>
 * date: 2016年3月2日15:18:49 <br/>
 * 
 * @author wjj
 * @version
 * @since JDK 1.7 Copyright (c) 2016, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class PrefrcMgAction extends BaseBizAction {

	private static Logger log = Logger.getLogger(PrefrcMgAction.class);

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
			String acct_id = dtbHelper.getStringValue("ACCT_ID");
			String prefrc_category = dtbHelper.getStringValue("PREFRC_CATEGORY");
			String setting_key = dtbHelper.getStringValue("SETTING_KEY");
			String _deptid = "%"+dtbHelper.getStringValue("_deptid")+"%";
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			String sqlStr ="";

			PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String, Object>> dataList = null; 
			List params = new ArrayList();
		    sqlStr = "select * from ( SELECT p.acct_id ,p.prefrc_category ,p.setting_key ,p.setting_val ,p.remark1 ,p.remark2 ,p.remark3 , "
					+ " a.role_id , "
					+ " r.org_id , "
					+ " o.org_nm ,o.org_ful_nm "
					+ " from SYS_ACCT_PREFRC p ,sys_acct_role a ,sys_role r , sys_org o "
					+ " WHERE p.acct_id = a.acct_id and  a.role_id= r.role_id  and o.org_id=r.org_id "
					+ " AND o.org_path like ? " ;
            params.add(_deptid);
			if(!"".equals(acct_id)&& !"%".equals(acct_id)){
				sqlStr += "and p.acct_id like ? ";
				params.add("%"+acct_id+"%");
			}
			if(!"".equals(prefrc_category)&& !"%".equals(prefrc_category)){
				sqlStr += "and p.prefrc_category like ? ";
				params.add("%"+prefrc_category+"%");
			}
			if(!"".equals(setting_key)&& !"%".equals(setting_key)){
				sqlStr += "and p.setting_key like ? ";
				params.add("%"+setting_key+"%");
			}
			sqlStr += ") t_prefrcMgtb";
			System.out.println(sqlStr);
			
			dataList = dbBean.queryForList(sqlStr, page , params.toArray());
			log.info(sqlStr);
			dtbHelper.setRstData("rows", dataList);
			dtbHelper.setRstData("total", page.getTotalCount());
			flag = 1;
		} catch (Exception e) {
			log.error("用户偏好查询出错", e);
			dtbHelper.setError("prefrcMg-err-qry", "[用户偏好查询出错]" + e.getMessage());
		} finally {
			DBAccessPool.releaseDbBean();
		}
		return flag;
	}

	protected int add(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			DBAccessPool.createDbBean();
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String acct_id = dtbHelper.getStringValue("acct_id");
			String prefrc_category = dtbHelper.getStringValue("prefrc_category");
			String setting_key = dtbHelper.getStringValue("setting_key");
			String setting_val = dtbHelper.getStringValue("setting_val");
			String remark1 = dtbHelper.getStringValue("remark1");
			String remark2 = dtbHelper.getStringValue("remark2");
			String remark3 = dtbHelper.getStringValue("remark3");
			String sqlStr = "select 1 from sys_acct_prefrc where acct_id = ? and prefrc_category=? and setting_key=? ";
			DataObject result = dbBean.executeSingleQuery(sqlStr, acct_id ,prefrc_category ,setting_key);
			if (result != null) {
				dbBean.executeRollBack();
				log.warn("偏好新增出错：该偏好已存在!");
				dtbHelper.setError("prefrcmg-err-add-001", "[偏好新增出错]该偏好已存在!");
				DBAccessPool.releaseDbBean();
				return flag;
			}
			sqlStr = "INSERT INTO sys_acct_prefrc(acct_id,prefrc_category,setting_key,setting_val,remark1,remark2,remark3) VALUES (?,?,?,?,?,?,?)";
			dbBean.executeUpdate(sqlStr, acct_id,prefrc_category,setting_key,setting_val,remark1,remark2,remark3);
			dbBean.executeCommit();
			flag = 1;
		} catch (Exception e) {
			log.error("偏好新增出错", e);
			dtbHelper.setError("prefrcmg-err-add-002", "[偏好新增出错]" + e.getMessage());
			try {
				if (dbBean != null) {
					dbBean.executeRollBack();
				}
			} catch (Exception e2) {
				log.error("数据库回滚出错", e2);
			}
		} finally {
			DBAccessPool.releaseDbBean();
		}
		return flag;
	}

	protected int save(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			DBAccessPool.createDbBean();
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String acct_id = dtbHelper.getStringValue("acct_id");
			String prefrc_category = dtbHelper.getStringValue("prefrc_category");
			String setting_key = dtbHelper.getStringValue("setting_key");
			String setting_val = dtbHelper.getStringValue("setting_val");
			String remark1 = dtbHelper.getStringValue("remark1");
			String remark2 = dtbHelper.getStringValue("remark2");
			String remark3 = dtbHelper.getStringValue("remark3");
			//SYS_ACCT_PREFRC表的主键由：acct_id+prefrc_category+setting_key共同组成
				String sqlStr = "select 1 from sys_acct_prefrc where acct_id = ? and prefrc_category=? ";
				DataObject result1 = dbBean.executeSingleQuery(sqlStr, acct_id ,prefrc_category);
				sqlStr = "select 2 from sys_acct_prefrc where acct_id = ? and setting_key=? ";
				DataObject result2 = dbBean.executeSingleQuery(sqlStr,acct_id, setting_key );
				 sqlStr = "select 3 from sys_acct_prefrc where acct_id = ? and prefrc_category=? and setting_key=? ";
				 DataObject	 result3 = dbBean.executeSingleQuery(sqlStr, acct_id ,prefrc_category ,setting_key);
			
				 if (result3 != null) {
					 sqlStr = "UPDATE sys_acct_prefrc set setting_val=?,remark1=?,remark2=?,remark3=? where acct_id = ? and prefrc_category=? and setting_key=? ";
						dbBean.executeUpdate(sqlStr,setting_val,remark1,remark2,remark3,acct_id,prefrc_category,setting_key);
						dbBean.executeCommit();
						return flag;
				 }else {
					 if (result1 == null) {
						 if (result2 == null) {
								sqlStr = "UPDATE sys_acct_prefrc set prefrc_category=?,setting_key=?,setting_val=?,remark1=?,remark2=?,remark3=? where acct_id = ? ";
								dbBean.executeUpdate(sqlStr,prefrc_category,setting_key,setting_val,remark1,remark2,remark3,acct_id);
								dbBean.executeCommit();
								flag = 1 ;
					      }else {
					    	    sqlStr = "UPDATE sys_acct_prefrc set prefrc_category=?,setting_val=?,remark1=?,remark2=?,remark3=? where acct_id = ? and setting_key=? ";
								dbBean.executeUpdate(sqlStr,prefrc_category,setting_val,remark1,remark2,remark3,acct_id,setting_key);
								dbBean.executeCommit();
								flag = 1 ;
					    	  
						}
					}else {
						sqlStr = "UPDATE sys_acct_prefrc set setting_key=?,setting_val=?,remark1=?,remark2=?,remark3=? where acct_id=? and prefrc_category=? ";
						dbBean.executeUpdate(sqlStr,setting_key,setting_val,remark1,remark2,remark3,acct_id,prefrc_category);
						dbBean.executeCommit();
						flag = 1;
					} 
				 }
					 
					 
		} catch (Exception e) {
			log.error("偏好保存出错", e);
			dtbHelper.setError("prefrcmg-err-sv", "[偏好保存出错]" + e.getMessage());
			try {
				if (dbBean != null) {
					dbBean.executeRollBack();
				}
			} catch (Exception e2) {
				log.error("数据库回滚出错", e2);
			}
		} finally {
			DBAccessPool.releaseDbBean();
		}
		return flag;
	}

	protected int remove(DTBHelper dtbHelper) throws BizActionException {
		String acct_id	= dtbHelper.getStringValue("acct_ids");
		String[] ads = acct_id.split(",");
		String prefrc_category	= dtbHelper.getStringValue("prefrc_categorys");
		String[] pcs = prefrc_category.split(",");
		String setting_key	= dtbHelper.getStringValue("setting_keys");
		String[] sks = setting_key.split(",");
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			DBAccessPool.createDbBean();
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			for (int i = 0; i < ads.length; i++) {  
				  String ad = ads[i];  
				  String pc = pcs[i];
				  String sk = sks[i];
				String sql = "delete from sys_acct_prefrc where acct_id = ? and prefrc_category =? and setting_key =?";
				dbBean.executeUpdate(sql, ad, pc ,sk);
			}
			dbBean.executeCommit();
			flag = 1;
		} catch (Exception e) {
			log.error("偏好删除出错", e);
			dtbHelper.setError("prefrcmg-err-rm-001", "[偏好删除出错]" + e.getMessage());
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
