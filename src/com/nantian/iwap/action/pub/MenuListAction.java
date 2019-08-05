package com.nantian.iwap.action.pub;

import java.util.List;

import org.apache.log4j.Logger;

import com.nantian.iwap.action.common.CRUDAction;
import com.nantian.iwap.app.action.system.RoleBatisDaoAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;

/**
 * 
* @Title: MenuListAction.java
* @Package com.nantian.iwap.action.pub
* @Description: TODO 菜单列表
* @author chun  
* @date 2016年3月11日 下午2:58:57
 */
public class MenuListAction extends CRUDAction{
	 private static Logger log = Logger.getLogger(MenuListAction.class);

	@Override
	protected int query(DTBHelper dtbHelper) throws BizActionException {
		String sqlStr = "SELECT module_id,  pmodule_id,  module_nm,module_valid FROM sys_module ";
		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<DataObject> resultList = dbBean.executeQuery(sqlStr );
			log.info("执行sql语句:" + sqlStr);
			String zNodes = "";
			for (DataObject tmp : resultList) {
				zNodes += String.format("{id:'%s', pId:'%s', name:'%s'},",
						tmp.getValue("module_id"), tmp.getValue("pmodule_id"), tmp
								.getValue("module_nm"));
			}
			if (zNodes.length() > 0) {
				zNodes = zNodes.substring(0, zNodes.length() - 1);
				zNodes = "[" + zNodes + "]";
			}else {
				dtbHelper.setError("menuList-err-zNodes", "[菜单内无对应数据!]" );
				return 0;
			}
			dtbHelper.setRstData("zNodes", zNodes);
		} catch (Exception e) {
			log.error("菜单列表查询出错", e);
			dtbHelper.setError("menuList-err-qry", "菜单列表查询出错");
			return 0;
		}
		return 1;
	}

	@Override
	protected int add(DTBHelper dtbHelper) throws BizActionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int save(DTBHelper dtbHelper) throws BizActionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int show(DTBHelper dtbHelper) throws BizActionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int remove(DTBHelper dtbHelper) throws BizActionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int other(DTBHelper dtbHelper) throws BizActionException {
		// TODO Auto-generated method stub
		return 0;
	}

}
