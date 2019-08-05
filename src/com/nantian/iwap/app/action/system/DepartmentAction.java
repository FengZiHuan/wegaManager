package com.nantian.iwap.app.action.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.action.common.CRUDAction;
import com.nantian.iwap.app.common.PubAction;


public class DepartmentAction extends CRUDAction {
	private static Logger logger = Logger.getLogger(DepartmentAction.class);

	@Override
	protected int query(DTBHelper dtbHelper) {
		String departmentid = dtbHelper.getStringValue("departmentid");
		String deptName = dtbHelper.getStringValue("deptName");
		String deptLevel = dtbHelper.getStringValue("deptLevel");
		String state = dtbHelper.getStringValue("state");
		String _deptId = dtbHelper.getStringValue("_deptId");
		String _deptlevel = PubAction.getDeptlevel(_deptId);
		
		List params = new ArrayList();
		String sqlStr = "	SELECT   d.org_id, d.org_pid,  d.org_nm ,d.org_status from sys_org d  left join  sys_org m on d.org_pid=m.org_id"+
				" where  1=1  and d.org_lvl >= " + _deptlevel + " and d.org_path like '%" + _deptId + "%' ";
		
		if (!"".equals(departmentid)) {
			sqlStr += " and d.org_id like  ? ";
			params.add("%" + departmentid + "%");
		}
		if (!"".equals(deptName)) {
			sqlStr += " and d.org_nm like ? ";
			params.add("%" + deptName + "%");
		}
		if (!"".equals(state)) {
			sqlStr += " and d.org_status =  ? " ;
			params.add(state);
		}
		if (!"".equals(deptLevel)) {
			sqlStr += " and d.org_lvl >= ? " ;
			params.add(deptLevel);
		}
		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<DataObject> resultList = dbBean.executeQuery(sqlStr ,params.toArray());
			logger.info("执行sql语句:" + sqlStr);
			String zNodes = "";
			for (DataObject tmp : resultList) {
				zNodes += String.format("{id:'%s', pId:'%s', name:'%s',flag:%s},",
						tmp.getValue("org_id"), tmp.getValue("org_pid"), tmp
								.getValue("org_nm"), "1".equals(tmp.getValue("org_status"))?"0":"1");
			}
			if (zNodes.length() > 0) {
				zNodes = zNodes.substring(0, zNodes.length() - 1);
				zNodes = "[" + zNodes + "]";
			}else {
				dtbHelper.setError("depmg-err-q-depzNodes", "[机构没有树]" );
				DBAccessPool.releaseDbBean();
				return 0;
			}
			dtbHelper.setRstData("zNodes", zNodes);
		} catch (Exception e) {
			logger.error("查询页面：数据库访问异常!", e);
			dtbHelper.setError("depmg-err-q-depzNodes", "[数据库访问异常!]" );
		}finally {
			DBAccessPool.releaseDbBean();
		}
		return 1;
	}

	@Override
	protected int add(DTBHelper dtbHelper) {
		String deptid = dtbHelper.getStringValue("deptid");
		String deptName = dtbHelper.getStringValue("name");
		String deptfullName = dtbHelper.getStringValue("fullname");
		String deptLevel = dtbHelper.getStringValue("deptlevel");
		String parentid = dtbHelper.getStringValue("parentid");
		String state = dtbHelper.getStringValue("state");
		String _deptlevel = PubAction.getDeptlevel(parentid);
		String orgpath ="";
		//String sqlStr = "insert into sys_org(org_id,org_nm,org_ful,nm,org_lvl,org_pid,org_path,org_status) values (?,?,?,?,?,?)";
		try {
			if ("4".equals(_deptlevel)) {
				dtbHelper.setError("depmg-err-a", "[网点没有下属机构!]" );
				DBAccessPool.releaseDbBean();
				return 0;
			}
			if (Integer.valueOf(deptLevel) <= Integer.valueOf(_deptlevel)) {
				dtbHelper.setError("depmg-err-a", "[机构等级不能高于或等于上级机构!]" );
				DBAccessPool.releaseDbBean();
				return 0;
			}
	
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<DataObject> resultList = dbBean.executeQuery(
					"select org_lvl from sys_org where org_id=?", deptid);
			if (resultList.size() > 0) {
				dtbHelper.setError("depmg-err-a", "[该机构号已存在!]" );

				return 1;
			}
			if(Integer.valueOf(deptLevel)==0){//增加省行机构
				parentid = "";
			}
			if(""==parentid&&"".equals(parentid)){
				orgpath=deptid;
			}else{
				DataObject result = dbBean.executeSingleQuery("select org_path from sys_org where org_id=?",parentid);
				if (result != null) {
					String org_path = result.getValue("org_path");
					orgpath = org_path+"."+deptid;
				}
			}
			
			String sqlstr = "insert into sys_org(org_id,org_nm,org_ful_nm,org_lvl,org_pid,org_path,org_status) values (?,?,?,?,?,?,?)";

			int i = dbBean.executeUpdate(sqlstr, deptid, deptName, deptfullName, deptLevel,
					parentid, orgpath, state);
			logger.info("执行sql语句:" + sqlstr);
			if (i == 1) {
			} else {
				dtbHelper.setError("depmg-err-a", "[数据库添加失败!]" );
				DBAccessPool.releaseDbBean();
				return 0;
			}
		} catch (Exception e) {
			logger.error("数据库访问异常!", e);
			dtbHelper.setError("depmg-err-a", "[数据库添加失败!]" );
		}finally {
			DBAccessPool.releaseDbBean();
		}
		return 1;
	}

	@Override
	protected int save(DTBHelper dtbHelper) {
		String deptid = dtbHelper.getStringValue("deptid");
		String deptName = dtbHelper.getStringValue("name");
		String deptfullName = dtbHelper.getStringValue("fullname");
		String ipaddress = dtbHelper.getStringValue("ipaddress");
		String state = dtbHelper.getStringValue("state");
		
		String sqlStr = "update sys_org set org_nm=?,org_ful_nm=?,org_status=? where org_id=?";

		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			int i = dbBean.executeUpdate(sqlStr, deptName, deptfullName,state,deptid);

			logger.info("执行sql语句:" + sqlStr);
			if (i == 1) {
			} else {
				dtbHelper.setError("depmg-err-s", "[数据库更新失败!]" );
				DBAccessPool.releaseDbBean();
				return 0;
			}
		} catch (Exception e) {
			logger.error("数据库访问异常!", e);
			dtbHelper.setError("depmg-err-s", "[数据库访问异常!]" );
		}finally {
			DBAccessPool.releaseDbBean();
		}
		return 1;
	}

	@Override
	protected int remove(DTBHelper dtbHelper) {
		String deptid = dtbHelper.getStringValue("deptid");
		String sqlStr = "delete from sys_org where org_id=?";
		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			int i = dbBean.executeUpdate(sqlStr, deptid);
			logger.info("执行sql语句:" + sqlStr);
			if (i == 1) {
			} else {
				dtbHelper.setError("depmg-err-r", "[数据库删除失败!]" );
				DBAccessPool.releaseDbBean();
				return 0;
			}
		} catch (Exception e) {
			logger.error("数据库访问异常!", e);
			dtbHelper.setError("depmg-err-r", "[数据库访问异常!]" );

		}finally {
			DBAccessPool.releaseDbBean();
		}
		return 1;
	}

	@Override
	protected int show(DTBHelper dtbHelper) throws BizActionException {
		// TODO Auto-generated method stub
		String deptid = dtbHelper.getStringValue("deptid");
		String sqlStr = "select * from(select m.ORG_ID,m.ORG_NM,m.ORG_FUL_NM,m.ORG_LVL,m1.ORG_NM as ORG_PNM,m.ORG_STATUS "+
						" from sys_org m,sys_org m1 " +
						" where  m.org_pid=m1.org_id and m.org_id= '"+ deptid +"') _tbl";
		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<Map<String,Object>> dataList = dbBean.queryForList(sqlStr);
			
			logger.info("执行sql语句:" + sqlStr);
			
			if (dataList.size() > 0) {
				dtbHelper.setRstData("rows", dataList.get(0));
			} else {
				dtbHelper.setError("depmg-err-s", "[数据库显示失败!]" );
				DBAccessPool.releaseDbBean();
				return 0;
			}
		} catch (Exception e) {
			logger.error("数据库访问异常!", e);
			dtbHelper.setError("depmg-err-s", "[数据库访问异常!]" );
		}finally {
			DBAccessPool.releaseDbBean();
		}
		return 0;
	}

	@Override
	protected int other(DTBHelper dtbHelper) throws BizActionException {
		String option = dtbHelper.getStringValue("option");
		System.out.println("option----"+option);
		if ("list".equals(option)) {
			return list(dtbHelper);	
		}else if("listOrg".equals(option)){
			return listOrg(dtbHelper);
		}
			
		return 0;
	}
	
	protected int list(DTBHelper dtbHelper) throws BizActionException {
		String name = dtbHelper.getStringValue("name");
		String org_id = "%"+dtbHelper.getStringValue("org_id")+"%";
		String org_id1 = dtbHelper.getStringValue("org_id1");
		String sqlStr = "SELECT  d.ORG_ID, d.ORG_PID, d.ORG_NM " +
						" from sys_org d  " +
						" left join  sys_org m on d.org_pid=m.org_id " +
						" where d.org_nm like '"+name+"'  and d.org_path  LIKE '"+org_id+"'" +
						" and d.org_pid="+org_id1+" or d.org_id="+org_id1+ 
						" order by org_id asc" ;
		try {
			System.out.println(sqlStr);
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<Map<String,Object>> dataList = dbBean.queryForList(sqlStr);
			dtbHelper.setRstData("rows", dataList);
			dtbHelper.setRstData("total", dataList.size());
			logger.info("执行sql语句:" + sqlStr);
		} catch (Exception e) {
			logger.error("查询页面：数据库访问异常!", e);
			dtbHelper.setError("depmg-err-l", "[数据库访问异常!]" );

		}finally {
			DBAccessPool.releaseDbBean();
		}
		return 0;
	}
	
	protected int listOrg(DTBHelper dtbHelper) throws BizActionException {
		String name = dtbHelper.getStringValue("name");
		String org_id = dtbHelper.getStringValue("org_id");
		String org_id1 = dtbHelper.getStringValue("org_id1");
		String sqlStr = "SELECT  d.ORG_ID, d.ORG_PID, d.ORG_NM " +
						" from sys_org d  " +
						" left join  sys_org m on d.org_pid=m.org_id" +
						" where d.org_nm like '"+name+"'  and d.org_path  LIKE '"+org_id+"' and d.org_pid="+org_id1+
						" order by org_id asc" ;
		try {
			System.out.println(sqlStr);
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<Map<String,Object>> dataList = dbBean.queryForList(sqlStr);
			dtbHelper.setRstData("rows", dataList);
			dtbHelper.setRstData("total", dataList.size());
			logger.info("执行sql语句:" + sqlStr);
			dtbHelper.setRstData("flag", 1);
		} catch (Exception e) {
			logger.error("查询页面：数据库访问异常!", e);
			dtbHelper.setError("depmg-err-lo", "[数据库访问异常!]" );
		}finally {
			DBAccessPool.releaseDbBean();
		}
		return 0;
	}

}
