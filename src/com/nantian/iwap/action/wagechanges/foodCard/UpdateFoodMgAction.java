package com.nantian.iwap.action.wagechanges.foodCard;

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
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @since JDK 1.8 Copyright (c) 2016, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class UpdateFoodMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(UpdateFoodMgAction.class);
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

        if ("save".equals(option)||"add".equals(option)) {
            return save(dtbHelper);
        }
        if ("dosure".equals(option)) {
            return doSure(dtbHelper);
        }
        if ("save_grant".equals(option)) {
            return save_grant(dtbHelper);
        }

        return 0;
    }

    private int doSure(DTBHelper dtbHelper) {

        int flag = 0;
        DBAccessBean dbBean = DBAccessPool.getDbBean();
        List<Map<String, Object>> dataList = null;
        try {
            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
            String times = simpleDateFormat.format(date);
            String ids = dtbHelper.getStringValue("ids");
            String updateSql="update FOOD_PROJECT_DEPT set times=?,status='1'  where p_id = ?";
            if (ids.contains("{{value}}")) {
                String[] split = ids.split("}},");
                ids=split[1];
            }
            String[] idList = ids.split(",");
            for (String id : idList) {
                if (id == null || "".equals(id.trim())) {
                    continue;
                }
                int no=Integer.parseInt(id);
                dbBean.executeUpdate("update FOOD_PROJECT_DEPT set times=?,status='1'  where p_id = ?",times, no);
                dbBean.executeCommit();
            }
            flag = 1;
        } catch (Exception e) {
            log.error("确认出错", e);
            dtbHelper.setError("usermg-err-add-002", "确认出错" + e.getMessage());
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
            String stand = dtbHelper.getStringValue("stand");
            String name = dtbHelper.getStringValue("userName");
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sql = "select * from FOOD_PROJECT where STATUS='1'";
            if (!"".equals(name)) {
                sql += " and name like '%" + name + "%'";
            }
            if (!"".equals(stand)) {
                sql += " and STANDARD = '" + stand + "'";
            }
            sql += " order by id ";
            dataList = dbBean.queryForList(sql);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("total", page.getTotalCount());
            flag = 1;
        } catch (Exception e) {
            log.error("用户查询出错", e);
            dtbHelper.setError("usermg-err-qry", "[用户查询出错]" + e.getMessage());
        }
        return flag;
    }

    protected int save(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String p_id = dtbHelper.getStringValue("ID");
            String name = dtbHelper.getStringValue("NAME");
            String standard = dtbHelper.getStringValue("STANDARD");
            String unit = dtbHelper.getStringValue("UNIT");
            String times = dtbHelper.getStringValue("TIMES");
            int id= Integer.parseInt(p_id);
            String updateSql="update FOOD_PROJECT set name=?,standard=?,unit=?,times=? where id=?";
            dbBean.executeUpdate(updateSql, name, standard, unit, times, id);
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
    protected int save_grant(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
            String times = simpleDateFormat.format(date);
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            List<String> deptList = dtbHelper.getListValue("deptList");
            //不勾选默认全选
            if (deptList.size()==0){
                String sql="select id from food_dept";
                List<Map<String, Object>> maps = dbBean.queryForList(sql);
                for (Map<String, Object> map : maps) {
                    String pid = map.get("ID").toString();
                    deptList.add(pid);
                }

            }

            String p_id = dtbHelper.getStringValue("id");
            dbBean.executeUpdate("delete from  FOOD_PROJECT_DEPT where p_id = ?", p_id);
            String sqlStr = "INSERT INTO FOOD_PROJECT_DEPT(p_id,d_id,times) VALUES (?,?,?)";

            for (String d_id : deptList) {
                if (d_id == null || "".equals(d_id.trim())) {
                    continue;
                }
                dbBean.executeUpdate(sqlStr, p_id, d_id,times);
            }
            dbBean.executeCommit();
            flag = 1;
        } catch (Exception e) {
            log.error("保存出错", e);
            dtbHelper.setError("usermg-err-sg", "保存出错" + e.getMessage());
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
