package com.nantian.iwap.action.wagechanges.foodCard;



import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.PaginationSupport;
import org.apache.log4j.Logger;
import java.util.*;

/**
 *
 * @since JDK 1.8 Copyright (c) 2016, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class AddFoodCardMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(AddFoodCardMgAction.class);
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


        return 0;
    }



    protected int query(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        try {
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            String dept = dtbHelper.getStringValue("depts");
            String name = dtbHelper.getStringValue("userName");
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sql = "select * from FOOD_CARD where FOODCARDNO is null ";
            if (!"".equals(name)) {
                sql += " and name like '%" + name + "%'";
            }
            if (!"".equals(dept)) {
                sql += " and DEPT = '" + dept + "'";
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
            String ID = dtbHelper.getStringValue("id");
            String foodcardno = dtbHelper.getStringValue("FOODCARDNO");
            String peopleno = dtbHelper.getStringValue("PEOPLENO");
            int id= Integer.parseInt(ID);
            String updateSql="update FOOD_CARD set FOODCARDNO=?, PEOPLENO=? where id=?";
            dbBean.executeUpdate(updateSql,foodcardno ,peopleno,id);
            dbBean.executeCommit();
            flag = 1;
        } catch (Exception e) {
            log.error("添加出错", e);
            dtbHelper.setError("usermg-err-add-002", "添加出错" + e.getMessage());
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
