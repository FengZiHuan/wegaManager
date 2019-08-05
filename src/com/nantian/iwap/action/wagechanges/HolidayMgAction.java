package com.nantian.iwap.action.wagechanges;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nantian.iwap.app.exp.ExpDataFactory;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
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
public class HolidayMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(HolidayMgAction.class);
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
        if ("query_grant".equals(option)) {
            return query_grant(dtbHelper);
        }
        if ("save_grant".equals(option)) {
            return save_grant(dtbHelper);
        }
        if ("daochu".equals(option)) {
            return daochu(dtbHelper);
        }

        return 0;
    }


    protected int query(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        try {
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sqlStr = "select * from NH_HOLIDAY where 1=1 ";
            String year= dtbHelper.getStringValue("year");
            String fmisNo= dtbHelper.getStringValue("fmisNo");
            String type= dtbHelper.getStringValue("type");
            String name= dtbHelper.getStringValue("userName");

            if (!"".equals(year)) {

                sqlStr += " and NH_XIUJIAYEAR = '" + year+ "'";
            }
            if (!"".equals(fmisNo)) {

                sqlStr += " and nh_fmis= '" + fmisNo+ "'";
            }
            if (!"".equals(type)&&!"0".equals(type)) {
                if ("1".equals(type)){
                    type="事假";
                }
                if ("2".equals(type)){
                    type="普通病假";
                }
                if ("3".equals(type)){
                    type="重疾病假";
                }

                if ("4".equals(type)){
                    type="普通长病假";
                }
                if ("5".equals(type)){
                    type="重疾长病假";
                }

                sqlStr += " and NH_QINGTYPE= '" + type+ "'";
            }
            if (!"".equals(name)) {
                sqlStr += " and NH_NAME like '%"+name+ "%' ";
            }


            sqlStr+=" order by NH_NO";
            dataList = dbBean.queryForList(sqlStr, page);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("total", page.getTotalCount());

            flag = 1;
        } catch (Exception e) {
            log.error("用户查询出错", e);
            dtbHelper.setError("usermg-err-qry", "[用户查询出错]" + e.getMessage());
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
                String nh_no="";
                String sql1 = "select * from NH_HOLIDAY " +
                        "WHERE NH_FMIS= ? AND NH_QINGTYPE= ? AND NH_XIUJIAYEAR=?";

                List<Map<String, Object>> maps = dbBean.queryForList(sql1, nh_fmis, nh_qingtype, nh_year);
                for (Map<String, Object> map : maps) {
                      nh_no= map.get("NH_NO").toString();
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
                            nh_times = times+ "," + nh_times ;
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
                dbBean.executeUpdate(sqlStr2,nh_times,nh_sumdays,nh_no);
                dbBean.executeCommit();

            }else {
                String nh_zgwgz = null;
                String nh_zjxgz = null;
                String nh_zjtbt = null;
                String seSql = "select W_GWGZ,W_JX,W_JBU from NH_DATASOURSE where w_fmis=?";
                List<Map<String, Object>> maps = dbBean.queryForList(seSql, nh_fmis);
                if (maps == null || maps.size() == 0) {
                    log.error("本人不存在");
                } else {
                    for (Map<String, Object> map : maps) {
                        nh_zgwgz = map.get("NH_ZGWGZ").toString();
                        nh_zjxgz = map.get("NH_ZJXGZ").toString();
                        nh_zjtbt = map.get("NH_ZJTBT").toString();
                    }
                    String sqlStr = "INSERT INTO NH_HOLIDAY(nh_cardid,nh_dept,nh_name,nh_qingtype,nh_xiujiayear,nh_times" +
                            ",nh_sumdays,nh_fmis,nh_beizhu,nh_zgwgz,nh_zjxgz,nh_zjtbt) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                    dbBean.executeUpdate(sqlStr, nh_cardid, nh_dept, nh_name, nh_qingtype,
                            nh_year, nh_times, nh_sumdays, nh_fmis, nh_beizhu,nh_zgwgz,nh_zjxgz,nh_zjtbt);
                    dbBean.executeCommit();
                  flag = 1;
            }
        }} catch (Exception e) {
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
                    nh_no= map.get("NH_NO").toString();
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
                            nh_times = times+ "," + nh_times ;
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
                dbBean.executeUpdate(sqlStr2,nh_times,nh_sumdays,nh_no);
                dbBean.executeCommit();

            }else {
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

    protected int query_grant(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        try {
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            String acct_id = dtbHelper.getStringValue("acct_id");
            String sqlStr = "select role_id from sys_acct_role where acct_id = ?";
            List<Map<String, Object>> dataList = dbBean.queryForList(sqlStr, acct_id);
            dtbHelper.setRstData("grants", dataList);
            flag = 1;
        } catch (Exception e) {
            log.error("用户授权查询出错", e);
            dtbHelper.setError("usermg-err-qg", "[用户授权查询出错]" + e.getMessage());
        }
        return flag;
    }

    @SuppressWarnings("unchecked")
    protected int save_grant(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            List<String> acct_role_list = dtbHelper.getListValue("acct_role_list");
            String acct_id = dtbHelper.getStringValue("acct_id");
            dbBean.executeUpdate("delete from sys_acct_role where acct_id = ?", acct_id);
            String sqlStr = "INSERT INTO sys_acct_role(role_id,acct_id) VALUES (?,?)";
            for (String role : acct_role_list) {
                if (role == null || "".equals(role.trim())) {
                    continue;
                }
                dbBean.executeUpdate(sqlStr, role, acct_id);
            }
            dbBean.executeCommit();
            flag = 1;
        } catch (Exception e) {
            log.error("用户授权保存出错", e);
            dtbHelper.setError("usermg-err-sg", "[用户授权保存出错]" + e.getMessage());
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


    protected int daochu(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        String sql = "select * from nh_holiday where nh_cardid=?";
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("holidayids");
            String[] userarr = userids.split(",");
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (String user : userarr) {
                if (user == null || "".equals(user.trim())) {
                    continue;
                }
                List<Map<String, Object>> maps = dbBean.queryForList(sql, user);
                for (Map<String, Object> map : maps) {
                    dataList.add(map);
                }
            }
            System.out.println(dataList.size());
            String filetype = "xlsx";
            String titleString = dtbHelper.getStringValue("titleString");
            ExpDataFactory edf = ExpDataFactory.getInstance();
            List<Map<String, String>> titleList = new ArrayList<Map<String, String>>();
            if (titleString != null && !"".equals(titleString.trim())) {
                try {
                    titleList = JSONObject.parseObject(titleString, new TypeReference<List<Map<String, String>>>() {
                    });
                } catch (Exception e) {
                    log.warn("格式化自定义表格列名出错", e);
                }
            } else {
                log.warn("前端未传入表格字段数据titleString对象");
            }
            String s = "1";
            Map rst = edf.expData1(filetype, dataList, titleList, s);
            if (rst.get("msg") != null && !"".equals(rst.get("msg").toString().trim())) {
                log.error("导出处理类数据导出出错:" + rst.get("msg"));
                dtbHelper.setError("exportdata-err-004", "[导出处理类数据导出出错]" + rst.get("msg"));
            } else {
                dtbHelper.setRstData("info", rst.get("info"));
                flag = 1;
            }
        } catch (Exception e) {
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
