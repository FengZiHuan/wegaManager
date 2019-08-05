package com.nantian.iwap.action.wagechanges.foodCard;

import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;
import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @since JDK 1.8 Copyright (c) 2016, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class SureFoodPeopleMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(SureFoodPeopleMgAction.class);
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
        if ("dosure".equals(option)) {
            return doSure(dtbHelper);
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

        if ("save_grant".equals(option)) {
            return save_grant(dtbHelper);
        }

        return 0;
    }

    private int remove(DTBHelper dtbHelper) {

        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("userids");
            String[] userarr = userids.split(",");
            int u_cnt = 0;
            int s_cnt = 0;
            for (String user : userarr) {
                if (user == null || "".equals(user.trim())) {
                    continue;
                }
                u_cnt++;
                int id = Integer.parseInt(user);
                int i = dbBean.executeUpdate("delete from food_card  where id = ?", id);
                if (i == 1) {
                    s_cnt++;
                }
            }
            dbBean.executeCommit();
            if (u_cnt != s_cnt) {
                log.warn("删除出错:删除失败" + (u_cnt - s_cnt) + "条");
                dtbHelper.setError("usermg-err-rm-001", "[删除出错]删除失败" + (u_cnt - s_cnt) + "条");
            } else {
                flag = 1;
            }
        } catch (Exception e) {
            log.error("删除出错", e);
            dtbHelper.setError("usermg-err-rm-002", "[删除出错]" + e.getMessage());
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


    protected int add(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String fmis = dtbHelper.getStringValue("FMIS");
            String name = dtbHelper.getStringValue("NAME");
            String dept = dtbHelper.getStringValue("DEPT");
            String brithday = dtbHelper.getStringValue("BRITHDAY");
            String remark = dtbHelper.getStringValue("REMARK");

            String sql = "insert into food_card(fmis,name,dept,brithday,remark) values(?,?,?,?,?)";
            dbBean.executeUpdate(sql, fmis, name, dept, brithday, remark);
            dbBean.executeCommit();
            flag = 1;

        } catch (Exception e) {
            log.error("新增出错", e);
            dtbHelper.setError("usermg-err-add-002", "[新增出错]" + e.getMessage());
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


    private int doSure(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = DBAccessPool.getDbBean();
        List<Map<String, Object>> dataList = null;
        try {
            String batchs = dtbHelper.getStringValue("batchs");
            String sql = "select max(batch) from food_card";
            DataObject dataObject = dbBean.executeSingleQuery(sql);
            String batch1 = dataObject.getValue("max(batch)");
            //生成流水号
            String tea_free = "";
            String free3 = "";
            String wash_free = "";
            String free4 = "";
            String free5 = "";
            String free6 = "";
            String free7 = "";
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            String time = simpleDateFormat.format(date);
            //批次号码。
            String batch = time + "-01";
            //已经存在就是本月第二次发
            if (batch.equals(batch1)) {
                batch = time + "-02";
            }
            //第一步，根据选择批次的人员查出来有关人员的fmis号码 等信息,插入批号
            String fmis = "";
            String dept = "";
            String name = "";
            String foodcardno = "";
            String peopleno = "";
            String remark = "";
            String brithday = "";
            if (!batchs.equals("")) {
                String insertSql = "insert into food_card(batch,fmis,dept,name,foodcardno,remark,brithday,peopleno) values(?,?,?,?,?,?,?,?) ";
                String selectSql = "select * from food_card where batch=?";
                List<Map<String, Object>> maps = dbBean.queryForList(selectSql, batchs);
                for (Map<String, Object> map : maps) {
                    fmis = map.get("FMIS").toString();
                    name = map.get("NAME").toString();
                    if (map.get("FOODCARDNO")!=null){
                    foodcardno = map.get("FOODCARDNO").toString();
                    }
                    if (map.get("PEOPLENO")!=null){
                        peopleno = map.get("PEOPLENO").toString();
                    }
                    if (map.get("REMARK") != null) {
                        remark = map.get("REMARK").toString();
                    }
                    if (brithday != null) {
                        brithday = map.get("BRITHDAY").toString();
                    }
                    dept = map.get("DEPT").toString();
                    if (dept.equals("其他")){
                        tea_free=dtbHelper.getStringValue("TEA_FREE");
                        wash_free=dtbHelper.getStringValue("WASH_FREE");
                        free3=dtbHelper.getStringValue("FREE3");
                        free3=dtbHelper.getStringValue("FREE4");
                        free3=dtbHelper.getStringValue("FREE5");
                        free3=dtbHelper.getStringValue("FREE6");
                        free3=dtbHelper.getStringValue("FREE7");
                        insertSql= "insert into food_card(batch,fmis,dept,name,foodcardno,remark,brithday,peopleno,tea_free,wash_free,free3" +
                                ",free4,free5,free6,free7) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                        dbBean.executeUpdate(insertSql, batch, fmis, dept, name, foodcardno, remark, brithday,peopleno
                        ,tea_free,wash_free,free3,free4,free5,free6,free7);
                        dbBean.executeCommit();
                    }else {
                    //新增
                    dbBean.executeUpdate(insertSql, batch, fmis, dept, name, foodcardno, remark, brithday,peopleno);
                    dbBean.executeCommit();
                    }
                }

            }
            //第二步为新增数据添加编号
                String updateNoSql="update food_card set batch=? where batch is null";
                dbBean.executeUpdate(updateNoSql,batch);
                dbBean.executeCommit();

            //第3步查询部门名字,根据名字插入对应的项目价格等信息。
            String selectDeptSql = " select name, id from food_dept";
            List<Map<String, Object>> maps1 = dbBean.queryForList(selectDeptSql);
            for (Map<String, Object> map1 : maps1) {
                //根据名字取得对应的id
                String name1 = map1.get("NAME").toString();
                //部门为其他的不更新数据。
                if (map1.get("NAME").toString().equals("其他")){
                    continue;
                }
                else{
                String id = map1.get("ID").toString();
                String mainSql = " select p.id,p.STANDARD from FOOD_PROJECT p WHERE id in (SELECT p_id from FOOD_PROJECT_DEPT d where d.d_id=? and d.times=? and d.status='1')";
                List<Map<String, Object>> mapsList = dbBean.queryForList(mainSql, id, time);
                if (mapsList != null && mapsList.size() > 0) {
                    for (Map<String, Object> map : mapsList) {
                        String index = map.get("ID").toString();
                        if (index.equals("1")) {
                            tea_free = map.get("STANDARD").toString();
                        }
                        if (index.equals("2")) {
                            wash_free = map.get("STANDARD").toString();
                        }
                        if (index.equals("3")) {
                            if (map.get("STANDARD") != null) {
                                free3 = map.get("STANDARD").toString();
                            }
                        }
                        if (index.equals("4")) {
                            if (map.get("STANDARD") != null) {
                                free4 = map.get("STANDARD").toString();
                            }
                        }
                        if (index.equals("5")) {
                            if (map.get("STANDARD") != null) {
                                free5 = map.get("STANDARD").toString();
                            }
                        }
                        if (index.equals("6")) {
                            if (map.get("STANDARD") != null) {
                                free6 = map.get("STANDARD").toString();
                            }
                        }
                        if (index.equals("7")) {
                            if (map.get("STANDARD") != null) {
                                free7 = map.get("STANDARD").toString();
                            }
                        }
                    }

                    }
                    //最后更新数据
                    String updateSql = "update food_card set tea_free=?,wash_free=?,free3=?,free4=?,free5=?,free6=?,free7=? where dept=? and batch=?";
                    dbBean.executeUpdate(updateSql, tea_free, wash_free, free3, free4, free5, free6, free7, name1, batch);
                    dbBean.executeCommit();
                }

                flag = 1;


            }


            int a = 2;
            flag = 1;
        } catch (Exception e) {
            log.error("确认名单出错", e);
            dtbHelper.setError("usermg-err-add-002", "确认名单出错" + e.getMessage());
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
            String dept = dtbHelper.getStringValue("depts");
            String batch = dtbHelper.getStringValue("batchs");
            String name = dtbHelper.getStringValue("userName");
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sql = "select * from FOOD_CARD where 1=1";
            if (!"".equals(batch)) {
                sql += " and batch = '" + batch + "'";
            }
            if (!"".equals(name)) {
                sql += " and name like '%" + name + "%'";
            }
            if (!"".equals(dept)) {
                sql += " and dept = '" + dept + "'";
            }

            sql += " order by id desc ";
            dataList = dbBean.queryForList(sql);

            int i=1;
            for (Map<String, Object> map : dataList) {
              map.put("NO",i);
                i++;
            }
            dataList.size();

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
            String ids = dtbHelper.getStringValue("id");
            String fmis = dtbHelper.getStringValue("FMIS");
            String dept = dtbHelper.getStringValue("DEPT");
            String name = dtbHelper.getStringValue("NAME");
            String remark = dtbHelper.getStringValue("REMARK");
            String brithday = dtbHelper.getStringValue("BRITHDAY");
            int id = Integer.parseInt(ids);
            String updateSql = "update FOOD_CARD set fmis=?,dept=?,name=?,remark=? ,brithday=? where id=?";
            dbBean.executeUpdate(updateSql, fmis, dept, name, remark, brithday, id);
            dbBean.executeCommit();
            flag = 1;
        } catch (Exception e) {
            log.error("名单新增出错", e);
            dtbHelper.setError("usermg-err-add-002", "名单新增出错" + e.getMessage());
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
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String times = simpleDateFormat.format(date);
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            List<String> deptList = dtbHelper.getListValue("deptList");
            //不勾选默认全选
            if (deptList.size() == 0) {
                String sql = "select id from food_dept";
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
                dbBean.executeUpdate(sqlStr, p_id, d_id, times);
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
