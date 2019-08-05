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
import java.util.Calendar;
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
public class TingFaMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(TingFaMgAction.class);
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
        if ("daochu".equals(option)) {
            return exportNow(dtbHelper);
        }
        if ("daochu2".equals(option)) {
            return exportByYear(dtbHelper);
        }
        if ("daochu3".equals(option)) {
            return exportByName(dtbHelper);
        }
        return 0;
    }


    private int query(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        try {
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            String year = dtbHelper.getStringValue("year");
            String yearyue = dtbHelper.getStringValue("yue");


            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sqlStr = null;
            if (year.equals("") && yearyue.equals("")) {
                //没输入年月不展示
                sqlStr = "select * from NH_HOLIDAY  where NH_BNXUTF>0  and NH_JNXUTF>0 or NH_GNXUTF>0  order by NH_NO";
                dataList = dbBean.queryForList(sqlStr, page);
                dtbHelper.setRstData("rows", dataList);
                dtbHelper.setRstData("total", page.getTotalCount());
                flag = 1;
            }
            else if (yearyue.equals("")) {
                sqlStr ="select * from NH_HOLIDAY  where  (NH_BNXUTF>0 or NH_JNXUTF>0 or NH_GNXUTF>0) and NH_XIUJIAYEAR=? order by NH_NO";
                dataList = dbBean.queryForList(sqlStr,year);
                dtbHelper.setRstData("rows", dataList);
                flag = 1;
            }
            else {
                  String[] split = yearyue.split("/");
                    String yue=split[1];
                    //输入的年月
                    String yearyu=split[0];
                    String nh_zny = yearyu+ "-" + yue;

                String yuefen = "";
                if (yue.equals("01")) {
                    yuefen = "NH_ONETGZ,NH_ONETJX,NH_ONETCHE";
                    String s = "select NH_ONETGZ,NH_ONETJX,NH_ONETCHE from nh_holiday";
                }
                if (yue.equals("02")) {
                    yuefen = "NH_TWOTGZ,NH_TWOTJX,NH_ONETCHE";
                }
                if (yue.equals("03")) {
                    yuefen = "NH_THTGZ,NH_THTJX,NH_THTCHE";
                }
                if (yue.equals("04")) {
                    yuefen = "NH_FOTGZ,NH_FOTJX,NH_FOTCHE";
                }
                if (yue.equals("05")) {
                    yuefen = "NH_FIVETGZ,NH_FIVETJX,NH_FIVETCHE";
                }
                if (yue.equals("06")) {
                    yuefen = "NH_SIXTGZ,NH_SIXTJX,NH_SIXTCHE";
                }
                if (yue.equals("07")) {
                    yuefen = "NH_SETGZ,NH_SETJX,NH_SETCHE";
                }
                if (yue.equals("08")) {
                    yuefen = "NH_AGETGZ,NH_AGETJX,NH_AGETCHE";
                }
                if (yue.equals("09")) {
                    yuefen = "NH_NITGZ,NH_NITJX,NH_NITCHE";
                }
                if (yue.equals("10")) {
                    yuefen = "NH_TENTGZ,NH_TENTJX,NH_TENTCHE";
                }
                if (yue.equals("11")) {
                    yuefen = "NH_ETGZ,NH_ETJX,NH_ETCHE";
                }
                if (yue.equals("12")) {
                    yuefen = "NH_TTGZ,NH_TTJX,NH_TTCHE";
                }

                else {
                    sqlStr = "select NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,NH_NAME,NH_FMIS,NH_XIUJIAYEAR,NH_GYTF,NH_GYITF,NH_GNXUTF,NH_JYTF," +
                            "NH_JYITF,NH_JNXUTF, NH_BYTF, NH_BYTIF,NH_BNXUTF," + yuefen + ",NH_NTGZ,NH_NTJX,NH_NTCHE,NH_YTGZ,NH_YTJX,NH_YTCHE from NH_HOLIDAY where  (NH_BNXUTF>0 or NH_JNXUTF>0 or NH_GNXUTF>0) and NH_XIUJIAYEAR=? order by NH_NO";
                    System.out.println(sqlStr);
                    List<Map<String, Object>> maps = dbBean.queryForList(sqlStr,yearyu);
                    String[] yues = yuefen.split(",");
                    String dtgz = yues[0];
                    String dtjx = yues[1];
                    String dtche = yues[2];
                    String nh_dtgz = null;
                    String nh_dtjx = null;
                    String nh_dtche = null;
                    String nh_dfgz = null;
                    String nh_dfjx = null;
                    String nh_dfche = null;
                    for (Map<String, Object> map : maps) {
                        String fmis = map.get("NH_FMIS").toString();
                        String gz = map.get("NH_ZGWGZ").toString();
                        Double zgz = Double.parseDouble(gz);
                        String jx = map.get("NH_ZJXGZ").toString();
                        Double zjx = Double.parseDouble(jx);
                        String che = map.get("NH_ZJTBT").toString();
                        Double zche = Double.parseDouble(che);
                        Double dtc;
                        Double dtg;
                        Double dtj;
                        if (map.get(dtche) != null) {
                            nh_dtche = map.get(dtche).toString();
                            dtc = Double.parseDouble(nh_dtche);
                            Double dfche = zche - dtc;
                            nh_dfche = dfche.toString();
                        } else {
                            nh_dtche = "0";
                            dtc = 0.0;
                            Double dfche = zche - dtc;
                            nh_dfche = dfche.toString();
                        }
                        if (map.get(dtgz) != null) {
                            nh_dtgz = map.get(dtgz).toString();
                            dtg = Double.parseDouble(nh_dtgz);
                            Double dfg = zgz - dtg;
                            nh_dfgz = dfg.toString();
                        } else {
                            nh_dtgz = "0";
                            dtg = 0.0;
                            Double dfg = zgz - dtg;
                            nh_dfgz = dfg.toString();
                        }
                        if (map.get(dtjx) != null) {
                            nh_dtjx = map.get(dtjx).toString();
                            dtj = Double.parseDouble(nh_dtjx);
                            Double dfj = zjx - dtj;
                            nh_dfjx = dfj.toString();

                        } else {
                            nh_dtjx = "0";
                            dtj = 0.0;
                            Double dfj = zjx - dtj;
                            nh_dfjx = dfj.toString();
                        }

                        String sql1 = "UPDATE nh_holiday set NH_DTGZ=?,NH_DTJX =?,NH_DTCHE=?," +
                                "NH_DFGZ=?,NH_DFJX=?,NH_DFCHE=? ,NH_ZNY=? where nh_fmis=?";
                        dbBean.executeUpdate(sql1, nh_dtgz, nh_dtjx, nh_dtche,
                                nh_dfgz, nh_dfjx, nh_dfche, nh_zny, fmis);
                        dbBean.executeCommit();
                    }
                    sqlStr = "select * from NH_HOLIDAY where  (NH_BNXUTF>0 or NH_JNXUTF>0 or NH_GNXUTF>0) and NH_XIUJIAYEAR=? order by NH_NO";
                    System.out.println(sqlStr);
                    dataList = dbBean.queryForList(sqlStr,yearyu);
                    dtbHelper.setRstData("rows", dataList);
                    flag = 1;

                }


            }

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
            String nh_dept=dtbHelper.getStringValue("NH_DEPT");
            String NH_NAME=dtbHelper.getStringValue("NH_NAME");
            String NH_FMIS=dtbHelper.getStringValue("NH_FMIS");
            String NH_QINGTYPE=dtbHelper.getStringValue("NH_QINGTYPE");
            String NH_ZGWGZ=dtbHelper.getStringValue("NH_ZGWGZ");
            String NH_ZJXGZ=dtbHelper.getStringValue("NH_ZJXGZ");
            String NH_ZJTBT=dtbHelper.getStringValue("NH_ZJTBT");
            String NH_DTGZ=dtbHelper.getStringValue("NH_DTGZ");
            String NH_DTJX=dtbHelper.getStringValue("NH_DTJX");
            String NH_DTCHE=dtbHelper.getStringValue("NH_DTCHE");
            String NH_DFGZ=dtbHelper.getStringValue("NH_DFGZ");
            String NH_DFJX=dtbHelper.getStringValue("NH_DFJX");
            String NH_DFCHE=dtbHelper.getStringValue("NH_DFCHE");
            String NH_BEIZHU=dtbHelper.getStringValue("NH_BEIZHU");
            String NH_ZNY=dtbHelper.getStringValue("NH_ZNY");
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

    protected int save(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String NH_DEPT=dtbHelper.getStringValue("NH_DEPT");
            String NH_NO=dtbHelper.getStringValue("NH_NO");
            String NH_NAME=dtbHelper.getStringValue("NH_NAME");
            String NH_FMIS=dtbHelper.getStringValue("NH_FMIS");
            String NH_QINGTYPE=dtbHelper.getStringValue("NH_QINGTYPE");
            String NH_ZGWGZ=dtbHelper.getStringValue("NH_ZGWGZ");
            String NH_ZJXGZ=dtbHelper.getStringValue("NH_ZJXGZ");
            String NH_ZJTBT=dtbHelper.getStringValue("NH_ZJTBT");
            String NH_DTGZ=dtbHelper.getStringValue("NH_DTGZ");
            String NH_DTJX=dtbHelper.getStringValue("NH_DTJX");
            String NH_DTCHE=dtbHelper.getStringValue("NH_DTCHE");
            String NH_DFGZ=dtbHelper.getStringValue("NH_DFGZ");
            String NH_DFJX=dtbHelper.getStringValue("NH_DFJX");
            String NH_DFCHE=dtbHelper.getStringValue("NH_DFCHE");
            String NH_BEIZHU=dtbHelper.getStringValue("NH_BEIZHU");
            String NH_ZNY=dtbHelper.getStringValue("NH_ZNY");
            String sql2 = " update NH_HOLIDAY set NH_DEPT=?, NH_NAME=?,NH_FMIS=?,NH_ZGWGZ=?,NH_ZJXGZ=?,NH_ZJTBT=?," +
                    "NH_DTGZ=?,NH_DTJX=?,NH_DTCHE=?,NH_DFGZ=?,NH_DFJX=?,NH_DFCHE=?,NH_BEIZHU=?,NH_ZNY=? where nh_no=?";
            dbBean.executeUpdate(sql2,NH_DEPT,NH_NAME,NH_FMIS,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,
                    NH_DTGZ,NH_DTJX,NH_DTCHE,NH_DFGZ,NH_DFJX,NH_DFCHE,NH_BEIZHU,NH_ZNY ,NH_NO);
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

    protected int exportNow(DTBHelper dtbHelper) {
        int flag = 0;
        Calendar now = Calendar.getInstance();
        int i = now.get(Calendar.YEAR);
        String year = String.valueOf(i);
        int a = now.get(Calendar.MONTH);
        String yue = (a + 1) + "";
        String yearmoth="";
        if (dtbHelper.getStringValue("yue") != "") {
            yearmoth =dtbHelper.getStringValue("yue");
            String[] split = yearmoth.split("/");
            year=split[0];
            yue=split[1];
        }
        int start = Integer.valueOf(dtbHelper.getStringValue("start"));
        int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));

        String nh_zny = yearmoth;
        String yuefen = "";
        String sqlStr;
        DBAccessBean dbBean = DBAccessPool.getDbBean();


        if (yue.equals("01")) {
            yuefen = "NH_ONETGZ,NH_ONETJX,NH_ONETCHE";
        }
        if (yue.equals("02")) {
            yuefen = "NH_TWOTGZ,NH_TWOTJX,NH_ONETCHE";
        }
        if (yue.equals("03")) {
            yuefen = "NH_THTGZ,NH_THTJX,NH_THTCHE";
        }
        if (yue.equals("04")) {
            yuefen = "NH_FOTGZ,NH_FOTJX,NH_FOTCHE";
        }
        if (yue.equals("05")) {
            yuefen = "NH_FIVETGZ,NH_FIVETJX,NH_FIVETCHE";
        }
        if (yue.equals("06")) {
            yuefen = "NH_SIXTGZ,NH_SIXTJX,NH_SIXTCHE";
        }
        if (yue.equals("07")) {
            yuefen = "NH_SETGZ,NH_SETJX,NH_SETCHE";
        }
        if (yue.equals("08")) {
            yuefen = "NH_AGETGZ,NH_AGETJX,NH_AGETCHE";
        }
        if (yue.equals("09")) {
            yuefen = "NH_NITGZ,NH_NITJX,NH_NITCHE";
        }
        if (yue.equals("10")) {
            yuefen = "NH_TENTGZ,NH_TENTJX,NH_TENTCHE";
        }
        if (yue.equals("11")) {
            yuefen = "NH_ETGZ,NH_ETJX,NH_ETCHE";
        }
        if (yue.equals("12")) {
            yuefen = "NH_TTGZ,NH_TTJX,NH_TTCHE";
        }
        sqlStr = "select * from NH_HOLIDAY where  (NH_BNXUTF>0 or NH_JNXUTF>0 or NH_GNXUTF>0) and NH_XIUJIAYEAR=? order by NH_NO";
        List<Map<String, Object>> maps = dbBean.queryForList(sqlStr,year);
        String[] yues = yuefen.split(",");
        String dtgz = yues[0];
        String dtjx = yues[1];
        String dtche = yues[2];
        String nh_dtgz = null;
        String nh_dtjx = null;
        String nh_dtche = null;
        String nh_dfgz = null;
        String nh_dfjx = null;
        String nh_dfche = null;
        for (Map<String, Object> map : maps) {
            String no = map.get("NH_NO").toString();
            String gz = map.get("NH_ZGWGZ").toString();
            Double zgz = Double.parseDouble(gz);
            String jx = map.get("NH_ZJXGZ").toString();
            Double zjx = Double.parseDouble(jx);
            String che = map.get("NH_ZJTBT").toString();
            Double zche = Double.parseDouble(che);
            Double dtc;
            Double dtg;
            Double dtj;
            if (map.get(dtche) != null) {
                nh_dtche = map.get(dtche).toString();
                dtc = Double.parseDouble(nh_dtche);
                Double dfche = zche - dtc;
                nh_dfche = dfche.toString();
            } else {
                nh_dtche = "0";
                dtc = 0.0;
                Double dfche = zche - dtc;
                nh_dfche = dfche.toString();
            }
            if (map.get(dtgz) != null) {
                nh_dtgz = map.get(dtgz).toString();
                dtg = Double.parseDouble(nh_dtgz);
                Double dfg = zgz - dtg;
                nh_dfgz = dfg.toString();
            } else {
                nh_dtgz = "0";
                dtg = 0.0;
                Double dfg = zgz - dtg;
                nh_dfgz = dfg.toString();
            }
            if (map.get(dtjx) != null) {
                nh_dtjx = map.get(dtjx).toString();
                dtj = Double.parseDouble(nh_dtjx);
                Double dfj = zjx - dtj;
                nh_dfjx = dfj.toString();

            } else {
                nh_dtjx = "0";
                dtj = 0.0;
                Double dfj = zjx - dtj;
                nh_dfjx = dfj.toString();
            }

            String sql1 = "UPDATE nh_holiday set NH_DTGZ=?,NH_DTJX =?,NH_DTCHE=?," +
                    "NH_DFGZ=?,NH_DFJX=?,NH_DFCHE=? ,NH_ZNY=? where nh_no=?";
            dbBean.executeUpdate(sql1, nh_dtgz, nh_dtjx, nh_dtche,
                    nh_dfgz, nh_dfjx, nh_dfche, nh_zny, no);
            dbBean.executeCommit();
        }


        String sql = "select NH_NO,NH_DEPT,NH_NAME,NH_FMIS,NH_QINGTYPE,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT," +
                "NH_DTGZ,NH_DTJX,NH_DTCHE,NH_DFGZ,NH_DFJX,NH_DFCHE,NH_BEIZHU,NH_FMIS,NH_ZNY from nh_holiday where nh_no=? and NH_XIUJIAYEAR=?";
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("holidayids");
            if (userids.equals("")||userids.contains("{{value}}"))  {
                sql = "select NH_NO,NH_DEPT,NH_NAME,NH_FMIS,NH_QINGTYPE,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT," +
                        "NH_DTGZ,NH_DTJX,NH_DTCHE,NH_DFGZ,NH_DFJX,NH_DFCHE,NH_BEIZHU,NH_FMIS,NH_ZNY from nh_holiday where (NH_BNXUTF>0 or NH_JNXUTF>0 or NH_GNXUTF>0) and NH_XIUJIAYEAR=?";
                dataList = dbBean.queryForList(sql,year);
                dataList.size();


            } else {
                String[] userarr = userids.split(",");
                for (String user : userarr) {
                    if (user == null || "".equals(user.trim())) {
                        continue;
                    }
                    int no=Integer.parseInt(user);
                    List<Map<String, Object>> maps1 = dbBean.queryForList(sql, no,year);
                    for (Map<String, Object> map : maps1) {
                        dataList.add(map);
                    }
                }
            }
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
            String s = "2";
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
//按年导出
    protected int exportByYear(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        String year=dtbHelper.getStringValue("year");

        String ye=dtbHelper.getStringValue("y");
        System.out.println(ye);

        String sql="select NH_NO,NH_FMIS,NH_DEPT,NH_NAME,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,NH_QINGTYPE,NH_XIUJIAYEAR,NH_SUMDAYS,NH_TGWGZ,NH_TJXGZ,NH_TJTBTD,NH_GYTF,NH_GYITF,NH_GNXUTF,NH_JYTF, \n" +
                "NH_JYITF,NH_JNXUTF, NH_BYTF, NH_BYTIF,NH_BNXUTF,NH_ONETGZ,NH_ONETJX,NH_ONETCHE,NH_TWOTGZ,NH_TWOTJX,\n" +
                "NH_TWOTCHE,NH_THTGZ,NH_THTJX,NH_THTCHE,NH_FOTGZ,NH_FOTJX,NH_FOTCHE,NH_FIVETGZ,NH_FIVETJX,NH_FIVETCHE,\n" +
                "NH_SIXTGZ,NH_SIXTJX,NH_SIXTCHE,NH_SETGZ,NH_SETJX,NH_SETCHE,NH_AGETGZ,NH_AGETJX,NH_AGETCHE,NH_NITGZ,\n" +
                "NH_NITJX,NH_NITCHE,NH_TENTGZ,NH_TENTJX,NH_TENTCHE,NH_ETGZ,NH_ETJX,NH_ETCHE,NH_TTGZ,NH_TTJX,\n" +
                "NH_TTCHE,NH_NTGZ,NH_NTJX,NH_NTCHE,NH_YTGZ,NH_YTJX,NH_YTCHE from nh_holiday where NH_XIUJIAYEAR=? and NH_NO=?"
                ;
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("holidayids");
            if (userids.equals("")||userids.contains("{{value}}"))  {
                sql= "select NH_NO,NH_FMIS,NH_DEPT,NH_NAME,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,NH_QINGTYPE,NH_XIUJIAYEAR,NH_SUMDAYS,NH_TGWGZ,NH_TJXGZ,NH_TJTBTD,NH_GYTF,NH_GYITF,NH_GNXUTF,NH_JYTF, \n" +
                        "NH_JYITF,NH_JNXUTF, NH_BYTF, NH_BYTIF,NH_BNXUTF,NH_ONETGZ,NH_ONETJX,NH_ONETCHE,NH_TWOTGZ,NH_TWOTJX,\n" +
                        "NH_TWOTCHE,NH_THTGZ,NH_THTJX,NH_THTCHE,NH_FOTGZ,NH_FOTJX,NH_FOTCHE,NH_FIVETGZ,NH_FIVETJX,NH_FIVETCHE,\n" +
                        "NH_SIXTGZ,NH_SIXTJX,NH_SIXTCHE,NH_SETGZ,NH_SETJX,NH_SETCHE,NH_AGETGZ,NH_AGETJX,NH_AGETCHE,NH_NITGZ,\n" +
                        "NH_NITJX,NH_NITCHE,NH_TENTGZ,NH_TENTJX,NH_TENTCHE,NH_ETGZ,NH_ETJX,NH_ETCHE,NH_TTGZ,NH_TTJX,\n" +
                        "NH_TTCHE,NH_NTGZ,NH_NTJX,NH_NTCHE,NH_YTGZ,NH_YTJX,NH_YTCHE from nh_holiday where (NH_BNXUTF>0 or NH_JNXUTF>0 or NH_GNXUTF>0) and NH_XIUJIAYEAR=? ";
                dataList = dbBean.queryForList(sql,year);
                dataList.size();


            } else {
                String[] userarr = userids.split(",");
                for (String user : userarr) {
                    if (user == null || "".equals(user.trim())) {
                        continue;
                    }
                    int no=Integer.parseInt(user);
                    List<Map<String, Object>> maps = dbBean.queryForList(sql, year,no);
                    for (Map<String, Object> map : maps) {
                        dataList.add(map);
                    }
                }
            }
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
            String s = "3";
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



    protected int exportByName(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;



        String sql="select NH_NO,NH_FMIS,NH_DEPT,NH_NAME,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,NH_QINGTYPE,NH_XIUJIAYEAR,NH_SUMDAYS,NH_TGWGZ,NH_TJXGZ,NH_TJTBTD,NH_GYTF,NH_GYITF,NH_GNXUTF,NH_JYTF, \n" +
                "NH_JYITF,NH_JNXUTF, NH_BYTF, NH_BYTIF,NH_BNXUTF,NH_ONETGZ,NH_ONETJX,NH_ONETCHE,NH_TWOTGZ,NH_TWOTJX,\n" +
                "NH_TWOTCHE,NH_THTGZ,NH_THTJX,NH_THTCHE,NH_FOTGZ,NH_FOTJX,NH_FOTCHE,NH_FIVETGZ,NH_FIVETJX,NH_FIVETCHE,\n" +
                "NH_SIXTGZ,NH_SIXTJX,NH_SIXTCHE,NH_SETGZ,NH_SETJX,NH_SETCHE,NH_AGETGZ,NH_AGETJX,NH_AGETCHE,NH_NITGZ,\n" +
                "NH_NITJX,NH_NITCHE,NH_TENTGZ,NH_TENTJX,NH_TENTCHE,NH_ETGZ,NH_ETJX,NH_ETCHE,NH_TTGZ,NH_TTJX,\n" +
                "NH_TTCHE,NH_NTGZ,NH_NTJX,NH_NTCHE,NH_YTGZ,NH_YTJX,NH_YTCHE from nh_holiday where NH_NAME like ? and NH_NO=?"
                ;
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            String name1 =  dtbHelper.getStringValue("name");
            String name ="%"+new String(name1.getBytes("ISO8859-1"), "UTF-8")+"%" ;
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("holidayids");
            if (userids.equals("")||userids.contains("{{value}}"))  {
                sql= "select NH_NO,NH_FMIS,NH_DEPT,NH_NAME,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,NH_QINGTYPE,NH_XIUJIAYEAR,NH_SUMDAYS,NH_TGWGZ,NH_TJXGZ,NH_TJTBTD,NH_GYTF,NH_GYITF,NH_GNXUTF,NH_JYTF, \n" +
                        "NH_JYITF,NH_JNXUTF, NH_BYTF, NH_BYTIF,NH_BNXUTF,NH_ONETGZ,NH_ONETJX,NH_ONETCHE,NH_TWOTGZ,NH_TWOTJX,\n" +
                        "NH_TWOTCHE,NH_THTGZ,NH_THTJX,NH_THTCHE,NH_FOTGZ,NH_FOTJX,NH_FOTCHE,NH_FIVETGZ,NH_FIVETJX,NH_FIVETCHE,\n" +
                        "NH_SIXTGZ,NH_SIXTJX,NH_SIXTCHE,NH_SETGZ,NH_SETJX,NH_SETCHE,NH_AGETGZ,NH_AGETJX,NH_AGETCHE,NH_NITGZ,\n" +
                        "NH_NITJX,NH_NITCHE,NH_TENTGZ,NH_TENTJX,NH_TENTCHE,NH_ETGZ,NH_ETJX,NH_ETCHE,NH_TTGZ,NH_TTJX,\n" +
                        "NH_TTCHE,NH_NTGZ,NH_NTJX,NH_NTCHE,NH_YTGZ,NH_YTJX,NH_YTCHE from nh_holiday where (NH_BNXUTF>0 or NH_JNXUTF>0 or NH_GNXUTF>0) and NH_NAME like ? ";
                dataList = dbBean.queryForList(sql,name);
                dataList.size();


            } else {
                String[] userarr = userids.split(",");
                for (String user : userarr) {
                    if (user == null || "".equals(user.trim())) {
                        continue;
                    }
                    int no=Integer.parseInt(user);
                    List<Map<String, Object>> maps = dbBean.queryForList(sql, name,no);
                    for (Map<String, Object> map : maps) {
                        dataList.add(map);
                    }
                }
            }
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
            String s = "3";
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
