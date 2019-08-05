package com.nantian.iwap.action.pub;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.action.common.CRUDAction;
import com.nantian.iwap.app.common.PubAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.PaginationSupport;

public class DeptListAction extends CRUDAction {
	private static Logger logger = Logger.getLogger(DeptListAction.class);

	@Override
	protected int query(DTBHelper dtbHelper) {
		int start = (Integer) dtbHelper.getObjectValue("start");
		int limit = (Integer) dtbHelper.getObjectValue("limit");
		String departmentid = dtbHelper.getStringValue("departmentid");
		String deptName = dtbHelper.getStringValue("deptname");
		String deptLevel = dtbHelper.getStringValue("deptLevel");
		String _deptId = dtbHelper.getStringValue("_deptId");
		String _deptlevel = PubAction.getDeptlevel(_deptId);
		

		System.out.println(_deptlevel);
		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();

		String  sqlStr="select ORG_ID,ORG_PID,ORG_NM,ORG_LVL from sys_org  "+
				" where  org_status='1'  and org_lvl >= " + _deptlevel + " and org_path like '%" + _deptId + "%' ";
		//String sqlStr = "	SELECT   d.org_id, d.org_pid,  d.org_nm ,d.org_status from sys_org d  left join  sys_org m on d.org_pid=m.org_id"+
		//		" where  1=1  and d.org_lvl >= " + _deptlevel + " and d.org_path like '%" + _deptId + "%' ";
		
		if (!"".equals(departmentid)) {
			sqlStr += " and org_id like '%" + departmentid + "%'";
		}
		if (!"".equals(deptName)) {
			sqlStr += " and org_nm like '%"+deptName+ "%' ";
		}
		if (!"".equals(deptLevel)) {
			sqlStr += " and org_lvl = '" + deptLevel+"'";
		}
		
			System.out.println("-------"+sqlStr);
			
			PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String,Object>> dataList = dbBean.queryForList(sqlStr, page);
			dtbHelper.setRstData("rows", dataList);
			sqlStr="select count(*) from(" + sqlStr + ") tbl";
			int countI = dbBean.queryForInt(sqlStr);
			System.out.println(countI);
			dtbHelper.setRstData("total", countI);
			
		} catch (Exception e) {
			logger.error("查询页面：数据库访问异常!", e);
			dtbHelper.setRstData("flag", 1);
			dtbHelper.setRstData("ERROR", "数据库访问异常!");
		}
		return 1;
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
