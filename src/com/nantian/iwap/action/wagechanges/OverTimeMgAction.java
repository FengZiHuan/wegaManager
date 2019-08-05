package com.nantian.iwap.action.wagechanges;

import com.nantian.iwap.app.imp.ImpDataFactory;
import com.nantian.iwap.app.imp.impl.SystemVariableImpl;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;
import com.nantian.iwap.web.upload.UploadConfig;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class OverTimeMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(OverTimeMgAction.class);


    @Override
    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");


        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }
        if ("add".equals(option)) {
            return add(dtbHelper);
        }
        if ("overtime".equals(option)) {
            return importOverTime(dtbHelper);
        }
        if ("save".equals(option)) {
            return save(dtbHelper);
        }
        if ("summ".equals(option)) {
            return summ(dtbHelper);
        }
        if ("remove".equals(option)) {
            return remove(dtbHelper);
        }

        return 0;
    }

    private int summ(DTBHelper dtbHelper) {
        DBAccessBean dbBean = DBAccessPool.getDbBean();
        int flag = 0;
        try {
            dbBean.setAutoCommit(false);
            String seSql = "select * from nh_overtime where o_status='0'";
            List<Map<String, Object>> maps = dbBean.queryForList(seSql);
            for (Map<String, Object> map : maps) {
                String jiReason = "";
                String jiaTimes = "";
                String o_idcard = map.get("O_IDCARD").toString();
                String o_id = map.get("O_ID").toString();
                //判断重复
                String sql = "select count(*) from NH_GOVERMONEY where g_idcard=? and g_status='0'";
                int i = dbBean.queryForInt(sql, o_idcard);
                if (i == 0) {
                    String o_name = map.get("O_NAME").toString();
                    //加班原因
                    String o_reason = map.get("O_REASON").toString();
                    String o_gz = map.get("O_GZ").toString();
                    String o_overgz = map.get("O_OVERGZ").toString();
                    String selectSql = "select o_reason ,o_otimes from nh_overtime where o_status = '0' and O_IDCARD=?";
                    List<Map<String, Object>> stringMap = dbBean.queryForList(selectSql, o_idcard);
                    int j = 0;
                    for (Map<String, Object> stringObjectMap : stringMap) {
                        if (stringObjectMap.get("O_REASON") == null || stringObjectMap.get("O_OTIMES") == null) {
                            dtbHelper.setError("", "具体时间或者原因不能为空");

                        } else {
                            if (j < stringMap.size() - 1) {
                                jiReason += stringObjectMap.get("O_REASON").toString() + ",";
                                jiaTimes += stringObjectMap.get("O_OTIMES").toString() + ",";
                                j++;
                            } else {
                                jiReason += stringObjectMap.get("O_REASON").toString();
                                jiaTimes += stringObjectMap.get("O_OTIMES").toString();
                                break;
                            }
                        }

                    }
                    System.out.println(jiReason);
                    System.out.println(jiaTimes);

                    seSql = "SELECT sum(O_DOUBLEDAYS),sum(O_THDAYS),SUM(O_DAYMONEY) FROM nh_overtime WHERE o_status = '0' and O_IDCARD=?";
                    List<Map<String, Object>> dataMaps = dbBean.queryForList(seSql, o_idcard);
                    String g_dayGzs = "";
                    String g_doDays = "";
                    String g_thDays = "";

                    for (Map<String, Object> objectMap : dataMaps) {
                        g_dayGzs = objectMap.get("SUM(O_DAYMONEY)").toString();
                        if (objectMap.get("SUM(O_THDAYS)") == null) {
                            g_thDays = "";
                        } else {
                            g_thDays = objectMap.get("SUM(O_THDAYS)").toString();
                        }
                        if (objectMap.get("SUM(O_DOUBLEDAYS)") == null) {
                            g_doDays = "";
                        } else {
                            g_doDays = objectMap.get("SUM(O_DOUBLEDAYS)").toString();
                        }
                    }
                    String g_no = "0";
                    String selectFmisSQl = "select w_fmis from NH_DATASOURSE where w_idcard=?";
                    DataObject fmisObject = dbBean.executeSingleQuery(selectFmisSQl, o_idcard);
                    String g_fims = fmisObject.getValue("w_fmis").toString();
                    //这里设总工资为加班工资导入值班工资时在加上；
                    String insSql = "insert into NH_GOVERMONEY(G_IDCARD,G_NAME,G_NO,G_FMIS,G_REASON,G_TIMES,G_THDAYS,G_DODAYS,G_GZ" +
                            ",G_OVERGZ,G_SDAYGZ,G_SGZ,G_STATUS) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    dbBean.executeUpdate(insSql, o_idcard, o_name, g_no, g_fims, jiReason, jiaTimes, g_thDays, g_doDays, o_gz
                            , o_overgz, g_dayGzs, g_dayGzs, "0");
                    dbBean.executeCommit();
                    flag = 1;

                } else {
                    String updateSql = " update  nh_overtime  set o_status='1' where o_id=?";
                    dbBean.executeUpdate(updateSql, o_id);
                    dbBean.executeCommit();
                    flag = 1;
                    continue;
                }

                String updateSql = " update  nh_overtime  set o_status='1' where o_id=?";
                dbBean.executeUpdate(updateSql, o_id);
                dbBean.executeCommit();
                flag = 1;
            }

        } catch (Exception e) {
            log.error("合并出错", e);
            dtbHelper.setError("usermg-err-add-002", "[合并出错]" + e.getMessage());
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
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String time = dtbHelper.getStringValue("time");
            String name = dtbHelper.getStringValue("name");
            String fmis = dtbHelper.getStringValue("fmis");
            String sqlStr = "select * from nh_overtime where O_STATUS ='0' ";
            sqlStr += " order by o_id desc";
            if (!"".equals(time)) {
                sqlStr += " and o_times like '%" + time + "%'";
            }
            if (!"".equals(name)) {
                sqlStr += " and o_name like '%" + name + "%'";
            }
            if (!"".equals(fmis)) {
                sqlStr += " and o_fmis = '" + fmis + "'";
            }
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
            String o_name = dtbHelper.getStringValue("O_NAME");
            String o_reason = dtbHelper.getStringValue("O_REASON");
            String o_otimes = dtbHelper.getStringValue("O_OTIMES");
            String o_days = dtbHelper.getStringValue("O_DAYS");
            String o_idcard = dtbHelper.getStringValue("O_IDCARD");
            String o_gz = "";
            String minMony = "";
            String o_daymoney = "";
            String o_thdays = "";
            String o_doubledays = "";
            Double thdays = null;
            //日期还有钱需要判断累加
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(o_otimes);
            String seSql = "select money,to_char(STARTIMES,'yyyy-MM-dd') STARTIMES,to_char(ENDTIMES,'yyyy-MM-dd') ENDTIMES from  NH_MINWEGA";
            List<Map<String, Object>> maps = dbBean.queryForList(seSql);
            for (Map<String, Object> map : maps) {
                //2018-06-30 23:59:59之前
                if (map.get("STARTIMES") == null) {
                    if (map.get("ENDTIMES") == null) {
                        dtbHelper.setError("usermg-err-add-002", "[广州最低工资出错]");
                    } else {
                        String endtimes = map.get("ENDTIMES").toString();
                        Date endtime = format.parse(endtimes);
                        if (endtime.after(date) || date.equals(endtime)) {
                            minMony = map.get("MONEY").toString();
                        }
                    }
                } else {
                    //2018-06-30 23:59:59之后
                    String startimes = map.get("STARTIMES").toString();
                    Date startimes_ = format.parse(startimes);
                    //最新
                    if (map.get("ENDTIMES") == null) {
                        if (date.after(startimes_) || date.equals(startimes_)) {
                            minMony = map.get("MONEY").toString();
                        }
                    } else {//中间区域
                        String endtimes1 = map.get("ENDTIMES").toString();
                        Date endtime1 = format.parse(endtimes1);
                        if (date.after(startimes_) && date.before(endtime1)) {
                            minMony = map.get("MONEY").toString();
                        } else if (date.equals(startimes_) || date.equals(endtime1)) {
                            minMony = map.get("MONEY").toString();
                        }
                    }
                }

            }
            //生成数据：拿到工资
            Double gz = 0.0;

            String sql = "select w_gz from NH_DATASOURSE where w_idcard=?";
            List<Map<String, Object>> maps1 = dbBean.queryForList(sql, o_idcard);
            if (maps1.size() <= 0 || maps1 == null) {
                dtbHelper.setError("usermg-err-add-002", "没有这个人");
            } else {
                for (Map<String, Object> map : maps1) {
                    String w_gz = map.get("W_GZ").toString();
                    gz = Double.parseDouble(w_gz);
                    o_gz = gz + "";
                }

                Double o_minMoney = Double.parseDouble(minMony);
                Double overmoney = gz > o_minMoney ? gz : o_minMoney;
                String o_overmoney = overmoney + "";
                BigDecimal omoneys = new BigDecimal(overmoney);
                BigDecimal days = new BigDecimal(21.75);
                BigDecimal daymoney = omoneys.divide(days, 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                //倍数2倍或者3倍
                if (o_reason.contains("节")) {
                    thdays = Double.parseDouble(o_days);
                    o_thdays = thdays + "";
                    daymoney = daymoney.multiply(new BigDecimal(3.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    o_daymoney = daymoney + "";
                } else {
                    o_doubledays = o_days;
                    daymoney = daymoney.multiply(new BigDecimal(2.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    o_daymoney = daymoney + "";
                }
                //插入sql       String o_daymoney="";
                //            String o_thdays="";
                //            String o_doubledays="";
                String sesql = "select count(*) from nh_overtime where o_idcard=? and o_otimes=? ";
                int i = dbBean.queryForInt(sesql, o_idcard, o_otimes);
                if (i > 0) {
                    log.error("已有记录");
                    dtbHelper.setError("usermg-err-add-002", "[已存在记录]");
                } else {
                    String stutas = "0";
                    String insql = "insert into nh_overtime(o_name,o_reason,o_otimes,o_days,o_idcard,o_thdays,o_doubledays,o_gz,O_MINGZ,O_OVERGZ,O_DAYMONEY,o_status) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                    dbBean.executeUpdate(insql, o_name, o_reason, o_otimes, o_days, o_idcard, o_thdays, o_doubledays, o_gz, minMony, o_overmoney, o_daymoney, stutas);
                    dbBean.executeCommit();
                    flag = 1;
                }
            }
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
        //身份证不能修改
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String o_name = dtbHelper.getStringValue("O_NAME");
            String o_reason = dtbHelper.getStringValue("O_REASON");
            String o_otimes = dtbHelper.getStringValue("O_OTIMES");
            String o_days = dtbHelper.getStringValue("O_DAYS");
            String o_idcard = dtbHelper.getStringValue("O_IDCARD");
            String o_gz = "";
            String minMony = "";
            String o_daymoney = "";
            String o_thdays = "";
            String o_doubledays = "";
            Double thdays = null;
            //日期还有钱需要判断累加
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(o_otimes);
            String seSql = "select money,to_char(STARTIMES,'yyyy-MM-dd') STARTIMES,to_char(ENDTIMES,'yyyy-MM-dd') ENDTIMES from  NH_MINWEGA";
            List<Map<String, Object>> maps = dbBean.queryForList(seSql);
            for (Map<String, Object> map : maps) {
                //2018-06-30 23:59:59之前
                if (map.get("STARTIMES") == null) {
                    if (map.get("ENDTIMES") == null) {
                        dtbHelper.setError("usermg-err-add-002", "[广州最低工资出错]");
                    } else {
                        String endtimes = map.get("ENDTIMES").toString();
                        Date endtime = format.parse(endtimes);
                        if (endtime.after(date) || date.equals(endtime)) {
                            minMony = map.get("MONEY").toString();
                        }
                    }
                } else {
                    //2018-06-30 23:59:59之后
                    String startimes = map.get("STARTIMES").toString();
                    Date startimes_ = format.parse(startimes);
                    //最新
                    if (map.get("ENDTIMES") == null) {
                        if (date.after(startimes_) || date.equals(startimes_)) {
                            minMony = map.get("MONEY").toString();
                        }
                    } else {//中间区域
                        String endtimes1 = map.get("ENDTIMES").toString();
                        Date endtime1 = format.parse(endtimes1);
                        if (date.after(startimes_) && date.before(endtime1)) {
                            minMony = map.get("MONEY").toString();
                        } else if (date.equals(startimes_) || date.equals(endtime1)) {
                            minMony = map.get("MONEY").toString();
                        }
                    }
                }

            }
            //生成数据：拿到工资
            Double gz = 0.0;

            String sql = "select w_gz from NH_DATASOURSE where w_idcard=?";
            List<Map<String, Object>> maps1 = dbBean.queryForList(sql, o_idcard);
            if (maps1.size() <= 0 || maps1 == null) {
                dtbHelper.setError("usermg-err-add-002", "没有这个人");
            } else {
                for (Map<String, Object> map : maps1) {
                    String w_gz = map.get("W_GZ").toString();
                    gz = Double.parseDouble(w_gz);
                    o_gz = gz + "";
                }
                Double o_minMoney = Double.parseDouble(minMony);
                Double overmoney = gz > o_minMoney ? gz : o_minMoney;
                String o_overmoney = overmoney + "";
                BigDecimal omoneys = new BigDecimal(overmoney);
                BigDecimal days = new BigDecimal(21.75);
                BigDecimal daymoney = omoneys.divide(days, 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                //倍数2倍或者3倍
                if (o_reason.contains("节")) {
                    thdays = Double.parseDouble(o_days);
                    o_thdays = thdays + "";
                    daymoney = daymoney.multiply(new BigDecimal(3.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    o_daymoney = daymoney + "";
                } else {
                    o_doubledays = o_days;
                    daymoney = daymoney.multiply(new BigDecimal(2.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    o_daymoney = daymoney + "";
                }
                //不允许修改身份证号码
                String upSQl = "select count(*) from nh_overtime where o_idcard=? and o_otimes=?";
                int i = dbBean.queryForInt(upSQl, o_idcard, o_otimes);
                if (i == 0) {
                    log.error("身份证或者具体时间不能修改");
                    dtbHelper.setError("usermg-err-add-002", "[身份证或者具体时间不能修改");

                } else {
                    String sesql = "select count(*) from nh_overtime where o_idcard=? and o_reason=? and o_otimes=? ";
                    int i1 = dbBean.queryForInt(sesql, o_idcard, o_reason, o_otimes);
                    if (i1 > 1) {
                        log.error("已有记录");
                        dtbHelper.setError("usermg-err-add-002", "[已存在记录]");
                    } else {
                        String updSql = "update nh_overtime set o_name=?,o_reason=?,o_days=?,o_thdays=?,o_doubledays=?,o_gz=?,O_MINGZ=?,O_OVERGZ=?,O_DAYMONEY=? where o_idcard=? and o_otimes=?";
                        dbBean.executeUpdate(updSql, o_name, o_reason, o_days, o_thdays, o_doubledays, o_gz, minMony, o_overmoney, o_daymoney, o_idcard, o_otimes);
                        dbBean.executeCommit();
                        flag = 1;
                    }
                }
            }
        } catch (Exception e) {
            log.error("保存出错", e);
            dtbHelper.setError("usermg-err-sv", "[保存出错]" + e.getMessage());
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
                int i = dbBean.executeUpdate("delete from NH_OVERTIME   where o_id = ?", user);
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

//导入数据

    private int importOverTime(DTBHelper dtbHelper) {
        int flag = 0;
        try {
            List files = dtbHelper.getListValue("files");
            if (files == null || files.size() != 1) {
                log.error("导入数据的文件数量必须为1");
                dtbHelper.setError("importdata-err-001", "导入数据的文件数量必须为1");
                return flag;
            }
            SystemVariableImpl svi = SystemVariableImpl.getInstance();
            svi.setDtbHelper(dtbHelper);
            String fileNm = files.get(0).toString();
            fileNm = UploadConfig.tempPath + fileNm;
            String imp_id = dtbHelper.getStringValue("imp_id");
            String modelcode = dtbHelper.getStringValue("option");
            if (imp_id == null || "".equals(imp_id.toString().trim())) {
                log.error("导入数据类型ID不能为空");
                dtbHelper.setError("importdata-err-002", "导入数据类型ID不能为空");
                return flag;
            }
            ImpDataFactory idf = ImpDataFactory.getInstance();
            Map rst = idf.impData(fileNm, imp_id, false, modelcode);
            if (rst.get("msg") != null && !"".equals(rst.get("msg").toString().trim())) {
                dtbHelper.setError("importdata-err-003", "[数据导入出错]" + rst.get("msg"));
            } else {
                dtbHelper.setRstData("info", rst.get("info"));
                flag = 1;
            }
        } catch (Exception e) {
            log.error("数据导入出错", e);
            dtbHelper.setError("importdata-err-004", "[数据导入出错]" + e.getMessage());
        }
        return flag;
    }

}
