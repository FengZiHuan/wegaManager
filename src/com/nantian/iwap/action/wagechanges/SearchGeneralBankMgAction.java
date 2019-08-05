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
public class SearchGeneralBankMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(SearchGeneralBankMgAction.class);
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

        return 0;
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

    //用户登录角色
    protected int query(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        try {
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            String year = dtbHelper.getStringValue("year");
            String ordId = dtbHelper.getStringValue("userInfo.ORG_ID");
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            String sq = "select org_nm from SYS_ORG where org_id =?";
            DataObject dataObject = dbBean.executeSingleQuery(sq, ordId);
            String jx_brand = dataObject.getValue("org_nm");
            System.out.println(jx_brand);
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sqlStr = "select * from nh_jxjj  where jx_status='1' and JX_BRAND = '" + jx_brand + "'";
            if (!"".equals(year)) {

                sqlStr += " and JX_YEAR =  '" + year + "'";
            }
            sqlStr += " order by JX_ID ";
            System.out.println("-------" + sqlStr);
            dataList = dbBean.queryForList(sqlStr, page);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("total", page.getTotalCount());
            flag = 1;
        } catch (Exception e) {
            log.error("查询出错", e);
            dtbHelper.setError("subsidymg-err-qry", "[查询出错]" + e.getMessage());
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
