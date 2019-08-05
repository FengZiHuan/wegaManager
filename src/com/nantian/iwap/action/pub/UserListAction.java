package com.nantian.iwap.action.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.action.common.CRUDAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.app.common.PubAction;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;

public class UserListAction extends CRUDAction {
	private static Logger logger = Logger.getLogger(UserListAction.class);

	@Override
	protected int query(DTBHelper dtbHelper) {
		int flag = 0;
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			int start = Integer.valueOf(dtbHelper.getStringValue("start"));
			int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
			String acct_id = dtbHelper.getStringValue("acct_id");
			String acct_nm = dtbHelper.getStringValue("acct_nm");
			String org_id = dtbHelper.getStringValue("org_id");
			String org_nm = dtbHelper.getStringValue("org_nm");
			String _deptid = dtbHelper.getStringValue("_deptid");
			List params = new ArrayList();
			String sqlStr = "SELECT a.role_id,a.acct_id,sa.acct_nm, "
					        + " r.org_id,o.org_nm,o.org_ful_nm,o.org_pid "
							+ " FROM sys_acct_role a, sys_role r ,sys_org o ,sys_account sa "
							+ " WHERE a.role_id = r.role_id  and o.org_id=r.org_id  and a.acct_id=sa.acct_id"
							+ " AND o.org_path like ? ";
			  params.add("%"+_deptid+"%");
				if(!"".equals(acct_id)&& !"%".equals(acct_id)){
					sqlStr += "and a.acct_id like ? ";
					params.add("%"+acct_id+"%");
				}
				if(!"".equals(acct_nm)&& !"%".equals(acct_nm)){
					sqlStr += "and sa.acct_nm like ? ";
					params.add("%"+acct_nm+"%");
				}
				if(!"".equals(org_id)&& !"%".equals(org_id)){
					sqlStr += "and r.org_id like ? ";
					params.add("%"+org_id+"%");
				}
				if(!"".equals(org_nm)&& !"%".equals(org_nm)){
					sqlStr += "and o.org_nm like ? ";
					params.add("%"+org_nm+"%");
				}
			System.out.println("-------"+_deptid);
            System.out.println("-------"+sqlStr);
            PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String,Object>> dataList = dbBean.queryForList(sqlStr,page,params.toArray());
			dtbHelper.setRstData("rows", dataList);
			sqlStr="select count(*) from(" + sqlStr + ") tbl";
			int countI = dbBean.queryForInt(sqlStr,params.toArray());
			System.out.println(countI);
			dtbHelper.setRstData("total", countI);
			DBAccessPool.releaseDbBean();
			flag = 1;
		} catch (Exception e) {
			logger.error("用户授权查询出错", e);
			dtbHelper.setError("UserListAction-err-q", "[用户查询出错]" + e.getMessage());
		} finally {
			DBAccessPool.releaseDbBean();
		}
		return flag;
	}

	@Override
	protected int add(DTBHelper dtbHelper) {
		return 0;
	}

	@Override
	protected int save(DTBHelper dtbHelper) {
		
		return 0;
	}

	@Override
	protected int remove(DTBHelper dtbHelper) {
		
		return 0;
	}

	@Override
	protected int show(DTBHelper dtbHelper) throws BizActionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int other(DTBHelper dtbHelper) throws BizActionException {
		
		return 0;
	}

}
