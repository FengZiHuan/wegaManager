package com.nantian.iwap.app.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nantian.iwap.common.sequence.SequenceService;
import com.nantian.iwap.common.util.DateUtil;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessException;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.ofpiwap.common.DataMapDictionary;

/**
 * FileName:PubAction.java<br>
 * Description:交易的共用方法<br>
 * History:<br>
 * Date Author Desc<br>
 * -------------------------------------------------------<br>
 * 2015-07-21 wjj
 */
public class PubAction {
	private static final Log logger = LogFactory.getLog(PubAction.class);

	/**
	 * 向稽核日志流水表插入记录
	 * 库表auditflow中的各字段简解：flowno，流水表编号，表的主键，由程序自动生成；flowdate，流水记录插入日期；
	 * bizid，这个就应时而变，如果是对机顶盒进行操作，那么bizid就是机顶盒的标识(deviceid)，如果是对播放单进行编辑，那么bizid
	 * 就是播放单编号，如此类推；biztype，该流水操作所属类型，参看数据字典“稽核类型”；flag，流水操作的标志，亦可参照数据字典
	 * “稽核标志”；auditor，操作员；reason，如果操作员操作失败，其原因；remark，备注；
	 *
	 * @param
	 * param：将auditflow表中的字段bizid,biztype,flag,auditor,reason,remark依次封装在数组param[6]中，
	 * 即param[0]=auditflow.bizid, param[1]=auditflow.biztype, ...,
	 * param[5]=auditflow.remark,
	 * 要说明的是调用该方法前必须得在需要调用的Action中为该6个字段获值，然后再依次赋值到数组param[6]中。
	 * @return
	 */
	public static boolean AddAuditFlow(String param[]) {
		boolean flag = false;
		try {
			String flowno = SequenceService.getServiceInstance().getSequence("016");
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			String sql = "insert into auditflow (flowno,flowdate,bizid,biztype,flag,auditor,reason,remark) values (?,?,?,?,?,?,?,?)";
			int rows = dbBean.executeUpdate(sql, flowno, DateUtil
					.getCurrentDate(), param[0], param[1], param[2], param[3],
					param[4], param[5]);
			if (rows == 1) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("插入稽核流水表出错", e);
		}
		return flag;
	}

	/**
	 * 获取机构等级
	 *
	 * @param deptid
	 *            机构号
	 * @return String
	 * @author wjj
	 */
	public static String getDeptlevel(String deptid) {
		String deptlevel = null;
		try {
			DBAccessPool.createDbBean();
			String sqlStr = "select org_lvl from sys_org where org_id='"
					+ deptid + "'";
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			DataObject result = dbBean.executeSingleQuery(sqlStr);
			if (result != null) {
				deptlevel = result.getValue("org_lvl");
			}
		} catch (DBAccessException e) {
			logger.error("数据库访问异常!", e);
		}
		return deptlevel;
	}

	/**
	 * 获取某个机构下所属的所有机构，如果是省行机构，则返回所有机构
	 *
	 * @param deptid
	 *            登录系统的操作员所在机构号
	 * @return
	 */
	public static String getDepartment(String deptid) {
		try {
			String deptlevel = getDeptlevel(deptid);
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			List<DataObject> resultList = null;
			int level = Integer.parseInt(deptlevel);
			String params = deptid;
			int totalDeptLevel = getTotalDeptLevel();// 获取总的机构级别数

			for (int i = level; i < totalDeptLevel; i++) {
				String sql = "select org_id from sys_org where org_pid in (?)";
				resultList = dbBean.executeQuery(sql, params);
				if (resultList.size() != 0) {
					StringBuffer temp = new StringBuffer();
					for (DataObject obj : resultList) {
						String deptID = obj.getValue("org_id");
						if (!params.contains(deptID)) {
							temp.append(deptID+",");
						}
					}
					if (temp.length() != 0) {
						params = new String(temp);
						deptid = params + deptid;
					} else {
						break;
					}
				} else {
					break;
				}
			}

		} catch (Exception e) {
			logger.error("获取某机构下所有所属机构出现异常!", e);
		}
		return deptid;

	}

	/**
	 * 获取登陆系统操作员所在机构的级别
	 *
	 * @param deptid
	 *            登录系统操作员所在机构号
	 * @return 登录系统操作员所在机构的级别
	 */
	public static String getOperDeptLevel(String deptid) {
		String deptlevel = "";
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			String sqlstr = "select org_lvl from sys_org where org_id=?";
			DataObject deptLevelObj = dbBean.executeSingleQuery(sqlstr, deptid);
			deptlevel = deptLevelObj.getValue("org_lvl");

		} catch (Exception e) {
			logger.error(e);
		}
		return deptlevel;
	}

	/**
	 * 获取总的机构级别数
	 *
	 * @return
	 */
	private static int getTotalDeptLevel() {
		int TotalDeptLevel = 3;
		try {
			// 从数据字典中获取总的机构级别数
			Map<String, String> deptLevel = DataMapDictionary.getInstance()
					.getDataMap("Dept_Level");
			if (deptLevel != null) {
				TotalDeptLevel = deptLevel.size();
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return TotalDeptLevel;
	}

	/**
	 * @param deviceid
	 * @return
	 */
	public static boolean isDeviceidExist(String deviceid) {
		boolean flag = false;
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			String sqlstr = "select deviceid from terminal where deviceid =?";
			List<DataObject> result = dbBean.executeQuery(sqlstr, deviceid);
			if (result.size() != 0) {
				return true;
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return flag;
	}

	/**
	 * 获取某机构下所属的所有机构，如果是省行机构，则返回所有机构
	 *
	 * @param departmentid
	 *            登录系统的操作员所在机构号
	 * @return String
	 */
	public static String DepartmentQuerySql(String departmentid) {
		try {
			String deptlevel = getOperDeptLevel(departmentid);
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			int totalDeptLevel = getTotalDeptLevel();
			List<DataObject> resultList = null;
			int level = Integer.parseInt(deptlevel);
			departmentid = "'" + departmentid + "'";
			String params = departmentid;
			// 获取总的机构级别数
			for (int i = level; i < totalDeptLevel; i++) {
				resultList = dbBean
						.executeQuery("select org_id from sys_org where org_pid in ("
								+ params + ")");
				if (resultList.size() != 0) {
					StringBuffer temp = new StringBuffer();
					for (DataObject obj : resultList) {
						String depID = obj.getValue("org_id");
						if (!params.contains(depID)) {
							temp.append("'" + depID + "',");
						}
					}
					if (temp.length() != 0) {
						temp.deleteCharAt(temp.length() - 1);
						params = new String(temp);
						departmentid = departmentid + "," + params;
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return departmentid;
	}

	/**
	 * 随机生成发送ID
	 *
	 * @return String
	 */
	public static String makeSendId() {
		int iRandom = new Random().nextInt();
		String sendid = Integer.toString(Math.abs(iRandom)).substring(0, 4);
		return sendid;
	}
	public static void getLog(String operid, String type,String bizname,String remark,String bizid){
			try {
				DBAccessBean dbBean = DBAccessPool.getDbBean();
				String sql = "insert into log_info(id,operid,modify_date,type,bizname,remark,bizid) values(?,?,?,?,?,?,?)";
				List params =new ArrayList();
				String id = SequenceService.getServiceInstance().getSequence("005");
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				params.add(id);
				params.add(operid);
				params.add(sdf.format(date));
				params.add(type);
				params.add(bizname);
				params.add(remark);
				params.add(bizid);
				dbBean.executeUpdate(sql, params.toArray());
			} catch (Exception e) {
				logger.error("添加日志记录失败", e);
			}

		}
	
	/**
	 * 获取登陆系统操作员角色的级别
	 *
	 * @param roleid
	 *            登录系统操作员角色编号
	 * @return 登录系统操作员角色的级别
	 */
	public static String getOperRoleLevel(String roleid) {
		String rolelevel = "";
		try {
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			String sqlstr = "select org_lvl from sys_roledef where role_id=?";
			DataObject deptLevelObj = dbBean.executeSingleQuery(sqlstr, roleid);
			rolelevel = deptLevelObj.getValue("org_lvl");

		} catch (Exception e) {
			logger.error(e);
		}
		return rolelevel;
	}
}
