package com.nantian.iwap.action.cmis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nantian.iwap.app.exp.ExpDataFactory;
import com.nantian.iwap.app.imp.ImpDataFactory;
import com.nantian.iwap.app.imp.impl.SystemVariableImpl;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.PaginationSupport;
import com.nantian.iwap.web.upload.UploadConfig;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

;


public class PostSalaryChangeMgAction extends TransactionBizAction {
    private Map config;

    public void setConfig(Map config) {
        this.config = config;
    }

    private static Logger log = Logger.getLogger(PostSalaryChangeMgAction.class);



    @Override
    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");


        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }
        if ("add".equals(option)) {
            return add(dtbHelper);
        }
        if ("govertime".equals(option)) {
            return importOverTime(dtbHelper);
        }
        if ("save".equals(option)) {
            return save(dtbHelper);
        }

        if ("remove".equals(option)) {
            return remove(dtbHelper);
        }
        if ("export".equals(option)) {
            return export(dtbHelper);
        }

        return 0;
    }

    private int export(DTBHelper dtbHelper) {

        int flag = 0;
        DBAccessBean dbBean = null;

        String sqlStr="SELECT P_NAME,P_CMIS,P_POST,P_POSTTIMES,P_POSTTYPE,P_POSTS,P_WEGATYPE,P_WEGAGRADE,P_CHANGETIMES," +
                "P_REASON,P_REMARK,P_STATUS,P_IDCARD,P_SENDDEPT FROM PEOPLE_POST_SALARY_CHANGES ";
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("holidayids");
            if (userids.equals("") || userids.contains("{{value}}")) {
                dataList = dbBean.queryForList(sqlStr);
                dataList.size();
            } else {
                String[] userarr = userids.split(",");
                for (String user : userarr) {
                    if (user == null || "".equals(user.trim())) {
                        continue;
                    }
                    sqlStr="SELECT P_NAME,P_CMIS,P_POST,P_POSTTIMES,P_POSTTYPE,P_POSTS,P_WEGATYPE,P_WEGAGRADE,P_CHANGETIMES," +
                            "P_REASON,P_REMARK,P_STATUS,P_IDCARD,P_SENDDEPT FROM PEOPLE_POST_SALARY_CHANGES where p_id=? ";
                    int no = Integer.parseInt(user);
                    List<Map<String, Object>> maps = dbBean.queryForList(sqlStr, no);
                    for (Map<String, Object> map : maps) {
                        dataList.add(map);
                    }
                }
            }
            String filetype = "xlsx";
            String titleString = dtbHelper.getStringValue("titleString");
            //导出表格顺序
            List<String> titleId = new ArrayList<>();

            String[] t = new String[]{"P_CMIS", "P_NAME","P_POST", "P_POSTTIMES", "P_POSTTYPE", "P_POSTS", "P_WEGATYPE"
                    , "P_WEGAGRADE", "P_CHANGETIMES", "P_REASON", "P_REMARK", "P_STATUS","P_IDCARD","P_SENDDEPT"};
            for (int i = 0; i < t.length; i++) {
                titleId.add(t[i]);
            }
            String s = "overTime";
           
            String[] head0 = new String[]{"员工号","姓名","发薪岗位","岗位聘任时间","岗位等级","岗位族群","工资等级","工资档次","变动时间","变动原因","备注","执行状态","身份证号","发薪机构"};
            String[] head1 = new String[]{"员工号","姓名","发薪岗位","岗位聘任时间","岗位等级","岗位族群","工资等级","工资档次","变动时间","变动原因","备注","执行状态","身份证号","发薪机构"};
            String[] headnum0 = new String[]{"0,1,0,0", "0,1,1,1", "0,1,2,2", "0,1,3,3", "0,1,4,4", "0,1,5,5", "0,1,6,6", "0,1,7,7",
                    "0,1,8,8", "0,1,9,9", "0,1,10,10","0,1,11,11","0,1,12,12","0,1,13,13"};
            //对应excel中的和列，下表从0开始{"开始行,结束行,开始列,结束列"}
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

            Map rst = edf.exportManage(filetype, dataList, titleList, titleId, s, head0, head1, headnum0);
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

            String sqlStr="SELECT P_ID,P_NAME,P_CMIS,P_POST,P_POSTTIMES,P_POSTTYPE,P_POSTS,P_WEGATYPE,P_WEGAGRADE,P_CHANGETIMES," +
                    "P_REASON,P_REMARK,P_STATUS,P_IDCARD,P_SENDDEPT FROM PEOPLE_POST_SALARY_CHANGES where 1=1";


            if (!"".equals(name)) {
                sqlStr += " and p_name like '%" + name + "%'";
            }
            if (!"".equals(fmis)) {
                sqlStr += " and p_cmis = '" +fmis + "'";
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
            String g_name = dtbHelper.getStringValue("G_NAME");
            String g_zsgz = dtbHelper.getStringValue("G_ZSGZ");
            String g_ztimes = dtbHelper.getStringValue("G_ZTIMES");
            String g_zdays = dtbHelper.getStringValue("G_ZDAYS");
            String g_idcard = dtbHelper.getStringValue("G_IDCARD");
            String g_sgz="";
            String g_status = "0";
            String secSql = "select count(*) from NH_GOVERMONEY where g_idcard=?";
            Boolean exist = dbBean.queryForInt(secSql, g_idcard) > 0;
            if (!exist){
                g_sgz=g_zsgz;
                String insertsql = "insert into NH_GOVERMONEY(g_name,g_ztimes" +
                        ",g_zdays,g_zsgz,g_idcard,g_status,g_sgz) values(?,?,?,?,?,?,?)";
                dbBean.executeUpdate(insertsql,g_name,g_ztimes,g_zdays,g_zsgz,g_idcard,g_status,g_sgz);
                dbBean.executeCommit();
            }else {

                //判断之前是否导入
                String checkSql="select count(*) from NH_GOVERMONEY where g_idcard=? and g_ztimes=? and g_zdays=? and g_zsgz=? and g_status='0' ";
                int i = dbBean.queryForInt(checkSql, g_idcard, g_ztimes, g_zdays, g_zsgz);
                if (i>0){
                    log.error("新增出错已经存在记录");
                    dtbHelper.setError("usermg-err-add-002", "[新增出错]");
                    dtbHelper.setError(","," ");
                }else {
                    String sql="select g_times ,g_sgz from NH_GOVERMONEY where g_idcard=? and g_status='0' ";
                    List<Map<String, Object>> maps = dbBean.queryForList(sql, g_idcard);
                    String g_times="";
                    for (Map<String, Object> map : maps) {
                        String time = map.get("G_TIMES").toString();
                        if (time.contains(g_ztimes)){
                            g_times=time;
                        }else {
                            g_times=time+","+g_ztimes;
                        }
                        String money = map.get("G_SGZ").toString();
                        if (money.equals(g_zsgz)){
                            g_sgz=money;
                        }else{
                            BigDecimal smoney=new BigDecimal(Double.parseDouble(money));
                            BigDecimal zmoney=new BigDecimal(Double.parseDouble(g_zsgz));
                            BigDecimal sgz=smoney.add(zmoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                            g_sgz=sgz+"";
                        }

                    }
                    String updateSql="update NH_GOVERMONEY set g_name=?,g_ztimes=?" +
                            ",g_zdays=?,g_zsgz=? ,g_times=? ,g_sgz=? where g_idcard=? and g_status='0' ";
                    dbBean.executeUpdate(updateSql,g_name,g_ztimes,g_zdays,g_zsgz,g_times,g_sgz,g_idcard);
                    dbBean.executeCommit();
                }}
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
            String g_name = dtbHelper.getStringValue("G_NAME");
            String g_zsgz = dtbHelper.getStringValue("G_ZSGZ");
            String g_ztimes = dtbHelper.getStringValue("G_ZTIMES");
            String g_zdays = dtbHelper.getStringValue("G_ZDAYS");
            String g_idcard = dtbHelper.getStringValue("G_IDCARD");

            dbBean.executeCommit();
            flag = 1;


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
                int g_id=   Integer.parseInt(user);
                int i = dbBean.executeUpdate("delete from NH_GOVERMONEY   where g_id = ?", g_id);
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
