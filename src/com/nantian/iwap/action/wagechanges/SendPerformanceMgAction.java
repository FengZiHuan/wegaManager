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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SendPerformanceMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(SendPerformanceMgAction.class);
    private String encryptClazz = "com.nantian.iwap.app.util.DefaultEncrypt";// 默认加密方式


    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");
        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
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
                String ssm = "0.0";
                String apSql = "";

                //查询总表里同一分行同一年份fmis号码数是不是大于0，有就更新，没有就插入；
                String sql = "select count(bd_fmis) from nh_bdata where bd_brand=? and bd_year=? and bd_fmis=?";
                int i = dbBean.queryForInt(sql, bd_brand, bd_year, bd_fmis);
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
                    DataObject dataObject = dbBean.executeSingleQuery(sSql, bd_fmis);
                    //查询记录上面的总数
                    String b_ssMoney = dataObject.getValue("bd_ssm");
                    Double bd_ssMoney = Double.parseDouble(b_ssMoney);
                    BigDecimal smoney = new BigDecimal(bd_ssMoney);
                    //本次项目的钱
                    Double bd_money1 = Double.parseDouble(bd_money);
                    BigDecimal bMoney = new BigDecimal(bd_money1);
                    //查询记录的总钱
                    BigDecimal gmoney = smoney.add(bMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                    ssm = gmoney + "";
                    String inSql = "update nh_bdata set bd_ssm=?," + apSql + " where bd_fmis=?";
                    dbBean.executeUpdate(inSql, ssm, bd_project, bd_money, bd_fmis);

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
                    //总钱等于第一次录入项目的钱
                    Double gmoney = Double.parseDouble(bd_money);
                    ssm = gmoney + "";
                    String inSql = "insert into nh_bdata(bd_year,bd_brand,bd_fmis,bd_name,bd_ssm," + apSql + ") values(?,?,?,?,?,?,?)";
                    dbBean.executeUpdate(inSql, bd_year, bd_brand, bd_fmis, bd_name, ssm, bd_project, bd_money);
                }
            }
            String upsql = "update NH_BRANDSTAFF set st_status='1'";
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
        DBAccessBean dbBean;
        try {
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            List<Map<String, Object>> dataList = null;
            String sql = "select * from nh_bdata where BD_XFS>0 ";
            dataList = dbBean.queryForList(sql);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("total", page.getTotalCount());
            flag = 1;
        } catch (Exception e) {
            log.error("查询出错", e);
            dtbHelper.setError("usermg-err-qry", "[查询出错]" + e.getMessage());
        }
        return flag;
    }

    //操作两张表
    protected int save(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
            String times = df.format(new Date());
            String bd_year = dtbHelper.getStringValue("BD_YEAR");
            String bd_name = dtbHelper.getStringValue("BD_NAME");
            String bd_brand = dtbHelper.getStringValue("BD_BRAND");
            String bd_fmis = dtbHelper.getStringValue("BD_FMIS");
            String sMoney = dtbHelper.getStringValue("BD_SSM");
            String nMoney = dtbHelper.getStringValue("BD_NOW");
            //插入记录表
            //避免重复发放


            String sql = " select count(*) from NH_RECORD_PERFORMANCE where rc_fmis=? and rc_year=? and RC_TIMES= to_date(?,'yyyy/MM')";
            int i = dbBean.queryForInt(sql, bd_fmis, bd_year, times);

            if (i == 0) {
                //计算已发数.
                String bd_yfs = "";
                String seSql = "select bd_yfs from nh_bdata  where bd_fmis=? and bd_year=?";
                List<Map<String, Object>> maps = dbBean.queryForList(seSql, bd_fmis, bd_year);
                if (maps == null || maps.size() == 0) {
                    bd_yfs = "0";
                } else {
                    for (Map<String, Object> map : maps) {
                        bd_yfs = map.get("BD_YFS").toString();
                    }
                }

                //已发的钱
                Double yiMoney = Double.parseDouble(bd_yfs);
                BigDecimal yifaMoney = new BigDecimal(yiMoney).setScale(2, BigDecimal.ROUND_HALF_UP);

                //总钱
                Double zMoney = Double.parseDouble(sMoney);
                BigDecimal zMoneys = new BigDecimal(zMoney);
                //本次发
                BigDecimal nowMoney = new BigDecimal(Double.parseDouble(nMoney));
                //已发钱等于之前的钱+本次的钱
                yifaMoney = yifaMoney.add(nowMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                bd_yfs = yifaMoney + "";
                //还需要发的钱
                BigDecimal xmoney = zMoneys.subtract(yifaMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                String bd_xfs = xmoney + "";
                //判断是否大于零
                Double mny = Double.parseDouble(bd_xfs);
                if (mny < 0) {
                    dtbHelper.setError("usermg-err-sv", "[发放失败，本次发放金额超出]");
                } else {
                    //更新总表，页面可以显示已发金额，还需发金额。
                    String inssql = "update nh_bdata set bd_yfs=?,bd_xfs=? where bd_fmis=? and bd_year=?";
                    dbBean.executeUpdate(inssql, bd_yfs, bd_xfs, bd_fmis, bd_year);
                    dbBean.executeCommit();
                    String inSql = "insert into NH_RECORD_PERFORMANCE (rc_fmis,rc_year,rc_name,rc_money,rc_brand,rc_times) values (?,?,?,?,?,to_date(?,'yyyy/MM'))";
                    dbBean.executeUpdate(inSql, bd_fmis, bd_year, bd_name, nMoney, bd_brand, times);
                    dbBean.executeCommit();
                    flag = 1;
                }
            } else {
                dtbHelper.setError("usermg-err-sv", "[本月已发过绩效]");
            }
        } catch (Exception e) {
            log.error("分发绩效保存出错", e);
            dtbHelper.setError("usermg-err-sv", "[分发绩效保存出错]" + e.getMessage());
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
