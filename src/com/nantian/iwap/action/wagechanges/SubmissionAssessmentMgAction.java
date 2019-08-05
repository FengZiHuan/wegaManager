package com.nantian.iwap.action.wagechanges;

import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: UserMgAction <br/>
 * Function: 请假管理<br/>
 *
 * @author f
 * @version 1.0
 * @since JDK 1.7 Copyright (c) 2019, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class SubmissionAssessmentMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(SubmissionAssessmentMgAction.class);
    private String encryptClazz = "com.nantian.iwap.app.util.DefaultEncrypt";// 默认加密方式


    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");
        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }
        if ("add".equals(option)) {
            return add(dtbHelper);
        }
        if ("submission".equals(option)) {
            return submission(dtbHelper);
        }
        if ("save".equals(option)) {
            return save(dtbHelper);
        }
        if ("remove".equals(option)) {
            return remove(dtbHelper);
        }



        return 0;
    }

    //提交数据生成，按照项目汇总（同一月份同一月份）
    private int submission(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            String ordId = dtbHelper.getStringValue("userInfo.ORG_ID");
            String sq = "select org_nm from SYS_ORG where org_id =?";
            DataObject dataObject = dbBean.executeSingleQuery(sq, ordId);
            String st_brand = dataObject.getValue("org_nm");
            String sqlStr = "SELECT SUM (ST_REMONEYS),ST_PROJECT,ST_TIMES FROM NH_BRANDSTAFF  where ST_SATUS = '0' and ST_BRAND = '" + st_brand + "' GROUP BY ST_BRAND, ST_PROJECT,ST_TIMES";
            List<Map<String, Object>> maps = dbBean.queryForList(sqlStr);
            for (Map<String, Object> map : maps) {
                //省行下发本次项目总金额
                BigDecimal p_gmoney = new BigDecimal(0);
                //省行与分行提交本次项目差别
                BigDecimal p_cmoney=new BigDecimal(0);
                //分行提交项目总金额
                String p_money = map.get("SUM(ST_REMONEYS)").toString();
                Double p_moneys = Double.parseDouble(p_money);
                BigDecimal pmoney = new BigDecimal(p_moneys);
                pmoney= pmoney.setScale(2, BigDecimal.ROUND_HALF_UP);
                String pmoney1=pmoney+"";
                //分行
                String p_brand = st_brand;
                //项目名字
                String p_name = map.get("ST_PROJECT").toString();
                //执行时间
                String p_times = map.get("ST_TIMES").toString();
                String[] split = p_times.split("-");
                String jx_year = split[0];
                String sql = "select jx_name1,jx_sum1,jx_name2,jx_sum2,jx_name3,jx_sum3, jx_name4,jx_sum4,jx_name5,jx_sum5,jx_name6 ,jx_sum6 from nh_jxjj where jx_year=?" +
                        " and jx_brand=?";
                DataObject dataObject1 = dbBean.executeSingleQuery(sql, jx_year,p_brand);
                String jx_name1 = dataObject1.getValue("jx_name1");
                String jx_name2 = dataObject1.getValue("jx_name2");
                String jx_name3 = dataObject1.getValue("jx_name3");
                String jx_name4 = dataObject1.getValue("jx_name4");
                String jx_name5 = dataObject1.getValue("jx_name5");
                String jx_name6 = dataObject1.getValue("jx_name6");
                String jx_sum1 = dataObject1.getValue("jx_sum1");
                Double jx_money1=Double.parseDouble(jx_sum1);
                String jx_sum2 = dataObject1.getValue("jx_sum2");
                Double jx_money2=Double.parseDouble(jx_sum2);
                String jx_sum3 = dataObject1.getValue("jx_sum3");
                Double jx_money3=Double.parseDouble(jx_sum3);
                String jx_sum4 = dataObject1.getValue("jx_sum4");
                Double jx_money4=Double.parseDouble(jx_sum4);
                String jx_sum5 = dataObject1.getValue("jx_sum5");
                Double jx_money5=Double.parseDouble(jx_sum5);
                String jx_sum6 = dataObject1.getValue("jx_sum6");
                Double jx_money6=Double.parseDouble(jx_sum6);
                //匹配项目
                String pgmoney="";
                String pmmoney="";
                String pcmoney="";
                if (p_name.equals(jx_name1)) {
                    p_name = jx_name1;
                    p_gmoney=new BigDecimal(jx_money1).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pgmoney=p_gmoney+"";
                    p_cmoney=p_gmoney.subtract(pmoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pcmoney=p_cmoney+"";


                }
                if (p_name.equals(jx_name2)) {
                    p_name = jx_name2;
                    p_gmoney=new BigDecimal(jx_money2).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pgmoney=p_gmoney+"";
                    System.out.println(p_gmoney);

                    p_cmoney=p_gmoney.subtract(pmoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pcmoney=p_cmoney+"";

                }
                if (p_name.equals(jx_name3)) {
                    p_name = jx_name3;
                    p_gmoney=new BigDecimal(jx_money3).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pgmoney=p_gmoney+"";
                    p_cmoney=p_gmoney.subtract(pmoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pcmoney=p_cmoney+"";
                }
                if (p_name.equals(jx_name4)) {
                    p_name = jx_name4;
                    p_gmoney=new BigDecimal(jx_money4).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pgmoney=p_gmoney+"";
                    p_cmoney=p_gmoney.subtract(pmoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pcmoney=p_cmoney+"";

                }
                if (p_name.equals(jx_name5)) {
                    p_name = jx_name5;
                    p_gmoney=new BigDecimal(jx_money5).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pgmoney=p_gmoney+"";
                    p_cmoney=p_gmoney.subtract(pmoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pcmoney=p_cmoney+"";

                }
                if (p_name.equals(jx_name6)) {
                    p_name = jx_name6;
                    p_gmoney=new BigDecimal(jx_money6).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pgmoney=p_gmoney+"";
                    p_cmoney=p_gmoney.subtract(pmoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                    pcmoney=p_cmoney+"";

                }

                String insql = "INSERT INTO NH_PROJECT (p_money,p_brand,p_name,p_gmoney,p_times,p_cmoney) VALUES (?,?,?,?,?,?)";
                dbBean.executeUpdate(insql, pmoney1,p_brand,p_name,pgmoney,p_times,pcmoney);
                dbBean.executeCommit();
            }
            //更新字段，每次只更新没提交的数据。
            String upsql="update NH_BRANDSTAFF set st_satus='1',st_status='0' where st_BRAND = '" + st_brand + "'";
            dbBean.executeUpdate(upsql);
            dbBean.executeCommit();
            flag = 1;
        } catch (Exception e) {
            log.error("提交错误出错", e);
            dtbHelper.setError("usermg-err-add-002", "[提交出错]" + e.getMessage());
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
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            String year = dtbHelper.getStringValue("year");
            String project = dtbHelper.getStringValue("project");
            String ordId = dtbHelper.getStringValue("userInfo.ORG_ID");
            String sq = "select org_nm from SYS_ORG where org_id =?";
            DataObject dataObject = dbBean.executeSingleQuery(sq, ordId);
            String st_brand = dataObject.getValue("org_nm");
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sqlStr = "select * from  NH_BRANDSTAFF where ST_BRAND = '" + st_brand + "'";
            if (!"".equals(year)) {

                sqlStr += " and st_times like '%" + year + "%'";
            }
            if (!"".equals(project)) {
                sqlStr += " and st_project like '%" + project + "%' ";
            }
            sqlStr += " order by st_id";
            dataList = dbBean.queryForList(sqlStr, page);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("total", page.getTotalCount());
            flag = 1;
        } catch (Exception e) {
            log.error("查询出错", e);
            dtbHelper.setError("usermg-err-qry", "[查询出错]" + e.getMessage());
        }
        return flag;
    }

    protected int add(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean=DBAccessPool.getDbBean();
            String st_times = dtbHelper.getStringValue("ST_TIMES");
            String st_brand = dtbHelper.getStringValue("ST_BRAND");
            String st_project = dtbHelper.getStringValue("ST_PROJECT");
            String st_fmis = dtbHelper.getStringValue("ST_FMIS");
            String st_name = dtbHelper.getStringValue("ST_NAME");
            String st_post = dtbHelper.getStringValue("ST_POST");
            String st_remoney = dtbHelper.getStringValue("ST_REMONEYS");
            String st_satus ="0";
            String st_status ="0";
            String sql ="INSERT INTO NH_BRANDSTAFF (st_brand,st_project,st_times,st_fmis,st_name,st_post,st_remoneys,st_satus,st_status) VALUES (?,?,?,?,?,?,?,?,?)";
            dbBean.executeUpdate(sql,st_brand,st_project,st_times,st_fmis,st_name,st_post,st_remoney,st_satus,st_status);
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

    protected int save(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String st_times = dtbHelper.getStringValue("ST_TIMES");
            String st_brand = dtbHelper.getStringValue("ST_BRAND");
            String st_project = dtbHelper.getStringValue("ST_PROJECT");
            String st_fmis = dtbHelper.getStringValue("ST_FMIS");
            String st_id = dtbHelper.getStringValue("ST_ID");
            String st_name = dtbHelper.getStringValue("ST_NAME");
            String st_post = dtbHelper.getStringValue("ST_POST");
            String st_remoney = dtbHelper.getStringValue("ST_REMONEYS");

            String sql = "update NH_BRANDSTAFF set st_project=?, ST_TIMES=?,ST_fmis=?,ST_BRAND=?,ST_NAME=?,ST_POST=?,ST_REMONEYS=? where st_id=?";

            dbBean.executeUpdate(sql,st_project, st_times,st_fmis, st_brand,  st_name, st_post, st_remoney,st_id);
            dbBean.executeCommit();
            flag = 1;

        } catch (Exception e) {
            log.error("保存出错", e);
            dtbHelper.setError("usermg-err-sv", "[用户保存出错]" + e.getMessage());
        }
        return flag;
    }

    protected int remove(DTBHelper dtbHelper) throws BizActionException {
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
                int i = dbBean.executeUpdate("delete from NH_BRANDSTAFF  where st_id = ?", user);
                if (i == 1) {
                    s_cnt++;
                }
            }
            dbBean.executeCommit();
            if (u_cnt != s_cnt) {
                log.warn("用户删除出错:删除失败" + (u_cnt - s_cnt) + "条");
                dtbHelper.setError("usermg-err-rm-001", "[用户删除出错]删除失败" + (u_cnt - s_cnt) + "条");
            } else {
                flag = 1;
            }
        } catch (Exception e) {
            log.error("用户删除出错", e);
            dtbHelper.setError("usermg-err-rm-002", "[用户删除出错]" + e.getMessage());
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
