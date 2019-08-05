package com.nantian.iwap.app.action.system;

import com.nantian.iwap.app.common.PubAction;
import com.nantian.iwap.biz.actions.BatisAction;
import com.nantian.iwap.biz.actions.BatisDaoAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.ibatis.IWAPBatisFactory;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class RoleBatisDaoAction extends BatisDaoAction
{
  private static Logger log = Logger.getLogger(RoleBatisDaoAction.class);
  private String querySqlId;
  private String deleteSqlId;
  private String insertSqlId;
  private String existSqlId;
  private String updateSqlId;
  private String dtlSqlId;
  private static List<String> actionType = new ArrayList();

  static { actionType.add("query");
    actionType.add("delete");
    actionType.add("update");
    actionType.add("insert");
    actionType.add("detail"); 
    actionType.add("query_grant");
    actionType.add("save_grant");
    actionType.add("query_sys_org");}

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

  public int insert(DTBHelper dtbHelper)
  {
	//改写
	Map map=IWAPBatisFactory.getInstance().getIwapBatisPool().queryForList(this.existSqlId, dtbHelper).get(0);
	if(String.valueOf(map.get("CNT")).equals("0")){
		return IWAPBatisFactory.getInstance().getIwapBatisPool().insert(this.insertSqlId, dtbHelper);
	}else{
		dtbHelper.setError("ADD-ROLE-ERR", "角色已存在!");
		return -1;
	}
  }

  public int update(DTBHelper dtbHelper) {
    return IWAPBatisFactory.getInstance().getIwapBatisPool().update(this.updateSqlId, dtbHelper);
  }

  public int delete(DTBHelper dtbHelper) {
	  //改写
    return IWAPBatisFactory.getInstance().getIwapBatisPool().delete(this.deleteSqlId, dtbHelper);
  }

  public int detail(DTBHelper dtbHelper)
  {
    Map map = IWAPBatisFactory.getInstance().getIwapBatisPool().queryForMap(this.dtlSqlId, dtbHelper);
    dtbHelper.setRstData(map);
    return map.size();
  }

  public int query(DTBHelper dtbHelper) {

    BatisAction action = new BatisAction();
    action.setSqlStr(this.querySqlId);
    return action.actionProcess(dtbHelper);
  }
  //查询模板设定
  public int query_grant(DTBHelper dtbHelper)  {
		int flag = 0;
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			String role_id = dtbHelper.getStringValue("role_id");
			
			String sqlStr = "select module_id  from sys_role_module  WHERE role_id =?";
			List<Map<String, Object>> dataList = dbBean.queryForList(sqlStr, role_id);
			dtbHelper.setRstData("grants", dataList);
			flag = 1;
		} catch (Exception e) {
			log.error("角色授权查询出错", e);
			dtbHelper.setError("rolemg-err-qg", "[角色授权查询出错]" + e.getMessage());
		} finally {
			DBAccessPool.releaseDbBean();
		}
		return flag;
	}

  	//保存模板设定
	public int save_grant(DTBHelper dtbHelper)  {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			DBAccessPool.createDbBean();
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			List<String> acct_module_list = dtbHelper.getListValue("acct_module_list");
			String role_id = dtbHelper.getStringValue("role_id");
			dbBean.executeUpdate("delete from sys_role_module where role_id = ?  ", role_id );
		String sqlStr = "INSERT INTO sys_role_module(role_id,module_id) VALUES (?,?)";
			for (String module : acct_module_list) {
				if (module == null || "".equals(module.trim())) {
					continue;
				}
				dbBean.executeUpdate(sqlStr, role_id, module);
			}
			dbBean.executeCommit();
			flag = 1;
		} catch (Exception e) {
			log.error("角色授权保存出错", e);
			dtbHelper.setError("rolemg-err-sg", "[角色授权保存出错]" + e.getMessage());
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
	//查询系统模板树
	public  int query_sys_module(DTBHelper dtbHelper) {
		String sqlStr = "SELECT module_id,  pmodule_id,  module_nm,module_valid FROM sys_module ";
		try {
			DBAccessPool.createDbBean();
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
				dtbHelper.setError("rolemg-err-qsm-zNodes", "[系统模板没有树]" );
				DBAccessPool.releaseDbBean();
				return 0;
			}
			dtbHelper.setRstData("flag", 1);
			dtbHelper.setRstData("zNodes", zNodes);
		} catch (Exception e) {
			log.error("查询页面：数据库访问异常!", e);
			dtbHelper.setRstData("flag", 0);
			dtbHelper.setError("rolemg-err-qsm", "[系统模板授权查询出错]" );
		}finally {
			DBAccessPool.releaseDbBean();
		}
		return 1;
	}
	
	//查询机构树
	public  int query_sys_org(DTBHelper dtbHelper) {
		String sqlStr = "SELECT org_id,  org_pid,  org_nm,org_status FROM sys_org ";
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<DataObject> resultList = dbBean.executeQuery(sqlStr );
			log.info("执行sql语句:" + sqlStr);
			String zNodes = "";
			for (DataObject tmp : resultList) {
				zNodes += String.format("{id:'%s', pId:'%s', name:'%s'},",
						tmp.getValue("org_id"), tmp.getValue("org_pid"), tmp
								.getValue("org_nm"));
			}
			if (zNodes.length() > 0) {
				zNodes = zNodes.substring(0, zNodes.length() - 1);
				zNodes = "[" + zNodes + "]";
			}else{
				dtbHelper.setError("rolemg-err-qso-OrgzNodes", "[机构没有树]" );
				DBAccessPool.releaseDbBean();
				return 0;
			}
			dtbHelper.setRstData("OrgzNodes", zNodes);
		} catch (Exception e) {
			log.error("查询页面：数据库访问异常!", e);
			dtbHelper.setError("rolemg-err-qso", "[机构授权查询出错]" );
		}finally {
			DBAccessPool.releaseDbBean();
		}
		return 1;
	}


  
  public String getQuerySqlId() {
    return this.querySqlId;
  }
  public void setQuerySqlId(String querySqlId) {
    this.querySqlId = querySqlId;
  }
  public String getDeleteSqlId() {
    return this.deleteSqlId;
  }
  public void setDeleteSqlId(String deleteSqlId) {
    this.deleteSqlId = deleteSqlId;
  }
  public String getInsertSqlId() {
    return this.insertSqlId;
  }
  public void setInsertSqlId(String insertSqlId) {
    this.insertSqlId = insertSqlId;
  }
  public String getUpdateSqlId() {
    return this.updateSqlId;
  }
  public void setUpdateSqlId(String updateSqlId) {
    this.updateSqlId = updateSqlId;
  }

public String getExistSqlId() {
	return existSqlId;
}

public void setExistSqlId(String existSqlId) {
	this.existSqlId = existSqlId;
}
  
}