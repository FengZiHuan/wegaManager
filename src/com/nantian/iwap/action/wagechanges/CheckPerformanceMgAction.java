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
import java.text.Bidi;
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
public class CheckPerformanceMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(CheckPerformanceMgAction.class);
    private String encryptClazz = "com.nantian.iwap.app.util.DefaultEncrypt";// 默认加密方式


    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");
        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
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

        if ("send".equals(option)) {
            return send(dtbHelper);
        }
        if ("submission".equals(option)) {
            return submission(dtbHelper);
        }

        return 0;
    }

    private int submission(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            //查询总表里同一分行同一年份有没有fmis号码有就更新，没有就插入；
            String sqlStr = "SELECT * FROM NH_BRANDSTAFF  where ST_SATUS = '1'and st_status ='0'";
            List<Map<String, Object>> maps = dbBean.queryForList(sqlStr);
            for (Map<String, Object> map : maps) {
                String bd_brand = map.get("ST_BRAND").toString();
                String p_times = map.get("ST_TIMES").toString();
                String bd_name = map.get("ST_NAME").toString();
                String bd_fmis = map.get("ST_FMIS").toString();
                String bd_project = map.get("ST_PROJECT").toString();
                String bd_money = map.get("ST_REMONEYS").toString();

                String[] split = p_times.split("-");
                String bd_year = split[0];
                String selSql = "select jx_name1,jx_name2,jx_name3,jx_name4,jx_name5,jx_name6 from nh_jxjj where jx_year=? and jx_brand=?";
                DataObject name = dbBean.executeSingleQuery(selSql, bd_year, bd_brand);
                String bd_pname1 = name.getValue("jx_name1");
                String bd_pname2 = name.getValue("jx_name2");
                String bd_pname3 = name.getValue("jx_name3");
                String bd_pname4 = name.getValue("jx_name4");
                String bd_pname5 = name.getValue("jx_name5");
                String bd_pname6 = name.getValue("jx_name6");
                //总钱
                String ssm="0.0";
                String apSql = "";

                //查询总表里同一分行同一年份fmis号码数是不是大于0，有就更新，没有就插入；
                String sql = "select count(bd_fmis) from nh_bdata where bd_brand=? and bd_year=? and bd_fmis=?";
                int i = dbBean.queryForInt(sql, bd_brand, bd_year,bd_fmis);
                //更新
                if (i > 0) {
                    if (bd_project.equals(bd_pname1)) {
                        apSql = "bd_pname1=?,bd_sum1=?";
                    }
                    if (bd_project.equals(bd_pname2)) {
                        apSql = "bd_pname2=?,bd_sum2=?";
                    }
                    if (bd_project.equals(bd_pname3)) {
                        apSql = "bd_pname3=?,bd_sum3=?";
                    }
                    if (bd_project.equals(bd_pname4)) {
                        apSql = "bd_pname4=?,bd_sum4=?";
                    }
                    if (bd_project.equals(bd_pname5)) {
                        apSql = "bd_pname5=?,bd_sum5=?";
                    }
                    if (bd_project.equals(bd_pname6)) {
                        apSql = "bd_pname6=?,bd_sum6=?";
                    }
                    String sSql = "select bd_ssm  from nh_bdata where bd_fmis=?";
                    DataObject dataObject = dbBean.executeSingleQuery(sSql,bd_fmis);
                    //查询记录上面的总数
                    String b_ssMoney=dataObject.getValue("bd_ssm");
                    Double bd_ssMoney = Double.parseDouble(b_ssMoney);
                    BigDecimal smoney=new BigDecimal(bd_ssMoney);
                    //本次项目的钱
                    Double bd_money1 = Double.parseDouble(bd_money);
                    BigDecimal bMoney=new BigDecimal(bd_money1);
                    //查询记录的总钱
                    BigDecimal gmoney=smoney.add(bMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                    ssm=gmoney+"";
                    String inSql = "update nh_bdata set bd_ssm=?,bd_xfs=?, "+apSql+" where bd_fmis=?";
                    dbBean.executeUpdate(inSql, ssm,ssm, bd_project, bd_money,bd_fmis);

                } else {
                    if (bd_project.equals(bd_pname1)) {
                        apSql = "bd_pname1,bd_sum1";
                    }
                    if (bd_project.equals(bd_pname2)) {
                        apSql = "bd_pname2,bd_sum2";
                    }
                    if (bd_project.equals(bd_pname3)) {
                        apSql = "bd_pname3,bd_sum3";
                    }
                    if (bd_project.equals(bd_pname4)) {
                        apSql = "bd_pname4,bd_sum4";
                    }
                    if (bd_project.equals(bd_pname5)) {
                        apSql = "bd_pname5,bd_sum5";
                    }
                    if (bd_project.equals(bd_pname6)) {
                        apSql = "bd_pname6,bd_sum6";
                    }
                    //总钱等于第一次录入项目的钱,还需发等于总钱
                    Double gmoney = Double.parseDouble(bd_money);
                    ssm=gmoney+"";
                    String inSql = "insert into nh_bdata(bd_year,bd_brand,bd_fmis,bd_name,bd_ssm,bd_xfs," + apSql + ") values(?,?,?,?,?,?,?,?)";
                    dbBean.executeUpdate(inSql, bd_year, bd_brand, bd_fmis, bd_name, ssm,ssm, bd_project, bd_money);
                }
            }
            String upsql="update NH_BRANDSTAFF set st_status='1'";
            dbBean.executeUpdate(upsql);
            dbBean.executeCommit();
            flag = 1;
        } catch (Exception e) {
            log.error("提交出错", e);
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

    // 选择分发或不分发
    private int send(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        String status = dtbHelper.getStringValue("status");
        String sql = "";
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("holidayids");
            if (userids.equals("") || userids.contains("{{value}}")) {
                sql = "update nh_jxjj set jx_status=?";
                dbBean.executeUpdate(sql, status);
                dbBean.executeCommit();
                flag = 1;
            } else {

                String[] userarr = userids.split(",");
                int u_cnt = 0;
                int s_cnt = 0;
                for (String user : userarr) {
                    if (user == null || "".equals(user.trim())) {
                        continue;
                    }
                    u_cnt++;
                    int a = Integer.parseInt(user);

                    sql = "update nh_jxjj set jx_status=? where jx_id =?";


                    int i = dbBean.executeUpdate(sql, status, a);
                    if (i == 1) {
                        s_cnt++;
                    }
                }
                dbBean.executeCommit();
                if (u_cnt != s_cnt) {
                    log.warn("分发出错:分发失败" + (u_cnt - s_cnt) + "条");
                    dtbHelper.setError("usermg-err-rm-001", "[分发出错]分发失败" + (u_cnt - s_cnt) + "条");
                } else {
                    flag = 1;
                }
            }
        } catch (Exception e) {
            log.error("分发出错", e);
            dtbHelper.setError("usermg-err-rm-002", "[分发出错]" + e.getMessage());
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

    //一开始就生成核对数据展现在页面上面
    protected int query(DTBHelper dtbHelper) throws BizActionException {

        int flag = 0;
        try {
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            String year = dtbHelper.getStringValue("year");
            String project = dtbHelper.getStringValue("project");
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            String ordId = dtbHelper.getStringValue("userInfo.ORG_ID");
            String sq = "select org_nm from SYS_ORG where org_id =?";
            DataObject dataObject = dbBean.executeSingleQuery(sq, ordId);
            String p_brand = dataObject.getValue("org_nm");
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sqlStr = "select P_ID,P_MONEY,P_BRAND ,P_NAME,P_GMONEY, P_TIMES,P_CMONEY from  NH_PROJECT where P_BRAND = '" + p_brand + "'";
            if (!"".equals(year)) {

                sqlStr += " and p_times = '" + year + "'";
            }
            if (!"".equals(project)) {
                sqlStr += " and p_name like '%" + project + "%' ";
            }
            sqlStr += " order by p_name";
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
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);

            String nh_cardid = dtbHelper.getStringValue("NH_CARDID");
            String nh_dept = dtbHelper.getStringValue("NH_DEPT");
            String nh_name = dtbHelper.getStringValue("NH_NAME");
            String nh_qingtype = dtbHelper.getStringValue("NH_QINGTYPE");
            String nh_year = dtbHelper.getStringValue("NH_XIUJIAYEAR");
            String nh_times = dtbHelper.getStringValue("NH_TIMES");
            String nh_sumdays = dtbHelper.getStringValue("NH_SUMDAYS");
            String nh_fmis = dtbHelper.getStringValue("NH_FMIS");
            String nh_beizhu = dtbHelper.getStringValue("NH_BEIZHU");
            //合并
            if (nh_fmis == "" || nh_qingtype == "" || nh_year == "") {
                log.error("用户新增出错");
                return 0;
            }

            String sql = "select count(*) from NH_HOLIDAY WHERE NH_FMIS= ? AND NH_QINGTYPE=? AND NH_XIUJIAYEAR=?";
            int i = dbBean.queryForInt(sql, nh_fmis, nh_qingtype, nh_year);
            //有记录合并
            if (i > 0) {
                String nh_no = "";
                String sql1 = "select * from NH_HOLIDAY " +
                        "WHERE NH_FMIS= ? AND NH_QINGTYPE= ? AND NH_XIUJIAYEAR=?";

                List<Map<String, Object>> maps = dbBean.queryForList(sql1, nh_fmis, nh_qingtype, nh_year);
                for (Map<String, Object> map : maps) {
                    nh_no = map.get("NH_NO").toString();
                    if (map.get("NH_TIMES") != null) {
                        String[] nh_times1 = map.get("NH_TIMES").toString().split(",");
                        List<String> list = new ArrayList<>();
                        for (String s : nh_times1) {
                            list.add(s);
                        }
                        if (list.contains(nh_times)) {
                            System.out.println("不需要合并");
                        } else {
                            Double sum = Double.parseDouble(nh_sumdays);
                            Double sum1;
                            if (map.get("NH_SUMDAYS") != null) {
                                String sumdays = map.get("NH_SUMDAYS").toString();
                                sum1 = Double.parseDouble(sumdays);
                            } else {
                                sum1 = 0.0;

                            }
                            sum += sum1;
                            nh_sumdays = sum + "";
                            //时间
                            String times = map.get("NH_TIMES").toString();
                            nh_times = times + "," + nh_times;
                        }
                    } else {
                        Double sum = Double.parseDouble(nh_sumdays);
                        Double sum1;
                        if (map.get("NH_SUMDAYS") != null) {
                            String sumdays = map.get("NH_SUMDAYS").toString();
                            sum1 = Double.parseDouble(sumdays);
                        } else {
                            sum1 = 0.0;
                        }
                        sum += sum1;
                        nh_sumdays = sum + "";
                    }


                }
                String sqlStr2 = "update nh_holiday set NH_TIMES=?,NH_SUMDAYS=? where NH_NO=?";
                dbBean.executeUpdate(sqlStr2, nh_times, nh_sumdays, nh_no);
                dbBean.executeCommit();

            } else {
                String sqlStr = "INSERT INTO NH_HOLIDAY(nh_cardid,nh_dept,nh_name,nh_qingtype,nh_xiujiayear,nh_times" +
                        ",nh_sumdays,nh_fmis,nh_beizhu) VALUES (?,?,?,?,?,?,?,?,?)";
                dbBean.executeUpdate(sqlStr, nh_cardid, nh_dept, nh_name, nh_qingtype,
                        nh_year, nh_times, nh_sumdays, nh_fmis, nh_beizhu);
                dbBean.executeCommit();
                flag = 1;
            }
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

    protected int save(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String nh_no = dtbHelper.getStringValue("NH_NO");
            String nh_cardid = dtbHelper.getStringValue("NH_CARDID");
            String nh_dept = dtbHelper.getStringValue("NH_DEPT");
            String nh_name = dtbHelper.getStringValue("NH_NAME");
            String nh_qingtype = dtbHelper.getStringValue("NH_QINGTYPE");
            String nh_year = dtbHelper.getStringValue("NH_XIUJIAYEAR");
            String nh_times = dtbHelper.getStringValue("NH_TIMES");
            String nh_sumdays = dtbHelper.getStringValue("NH_SUMDAYS");
            String nh_fmis = dtbHelper.getStringValue("NH_FMIS");
            String nh_beizhu = dtbHelper.getStringValue("NH_BEIZHU");
            //合并
            if (nh_fmis == "" || nh_qingtype == "" || nh_year == "") {
                log.error("用户新增出错");
                return 0;
            }

            String sql = "select count(*) from NH_HOLIDAY WHERE NH_FMIS= ? AND NH_QINGTYPE=? AND NH_XIUJIAYEAR=?";
            int i = dbBean.queryForInt(sql, nh_fmis, nh_qingtype, nh_year);
            //有记录合并
            if (i > 0) {

                String sql1 = "select * from NH_HOLIDAY " +
                        "WHERE NH_FMIS= ? AND NH_QINGTYPE= ? AND NH_XIUJIAYEAR=?";

                List<Map<String, Object>> maps = dbBean.queryForList(sql1, nh_fmis, nh_qingtype, nh_year);
                for (Map<String, Object> map : maps) {
                    nh_no = map.get("NH_NO").toString();
                    if (map.get("NH_TIMES") != null) {
                        String[] nh_times1 = map.get("NH_TIMES").toString().split(",");
                        List<String> list = new ArrayList<>();
                        for (String s : nh_times1) {
                            list.add(s);
                        }
                        if (list.contains(nh_times)) {
                            System.out.println("不需要合并");
                        } else {
                            Double sum = Double.parseDouble(nh_sumdays);
                            Double sum1;
                            if (map.get("NH_SUMDAYS") != null) {
                                String sumdays = map.get("NH_SUMDAYS").toString();
                                sum1 = Double.parseDouble(sumdays);
                            } else {
                                sum1 = 0.0;

                            }
                            sum += sum1;
                            nh_sumdays = sum + "";
                            //时间
                            String times = map.get("NH_TIMES").toString();
                            nh_times = times + "," + nh_times;
                        }
                    } else {
                        Double sum = Double.parseDouble(nh_sumdays);
                        Double sum1;
                        if (map.get("NH_SUMDAYS") != null) {
                            String sumdays = map.get("NH_SUMDAYS").toString();
                            sum1 = Double.parseDouble(sumdays);
                        } else {
                            sum1 = 0.0;
                        }
                        sum += sum1;
                        nh_sumdays = sum + "";
                    }


                }
                String sqlStr2 = "update nh_holiday set NH_TIMES=?,NH_SUMDAYS=? where NH_NO=?";
                dbBean.executeUpdate(sqlStr2, nh_times, nh_sumdays, nh_no);

            } else {
                String sqlStr = "UPDATE nh_holiday set  NH_CARDID=?,nh_dept =?,nh_name=?,nh_qingtype=?,nh_xiujiayear=?,nh_times=?,nh_sumdays=?,nh_fmis=?,nh_beizhu=? where nh_no=?";
                dbBean.executeUpdate(sqlStr, nh_cardid, nh_dept, nh_name, nh_qingtype, nh_year, nh_times,
                        nh_sumdays, nh_fmis, nh_beizhu, nh_no);

                dbBean.executeCommit();
                flag = 1;
            }
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
            String userids = dtbHelper.getStringValue("holidayids");
            String[] userarr = userids.split(",");
            int u_cnt = 0;
            int s_cnt = 0;
            for (String user : userarr) {
                if (user == null || "".equals(user.trim())) {
                    continue;
                }
                u_cnt++;

                int i = dbBean.executeUpdate("delete from nh_holiday where nh_no = ?", user);
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
