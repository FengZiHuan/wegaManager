package com.nantian.iwap.action.wagechanges.managerCheckPerformance;


import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.PaginationSupport;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Map;


public class ManagerCheckDataMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(ManagerCheckDataMgAction.class);


    @Override
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
            String time = dtbHelper.getStringValue("time");
            String name = dtbHelper.getStringValue("name");
            String fmis = dtbHelper.getStringValue("fmis");
            String project = dtbHelper.getStringValue("project");
            String sqlStr = "select * from nh_gjx";
            if (!"".equals(time)) {
                sqlStr += " and D_SENDMONTH='" + time + "'";
            }
            if (!"".equals(name)) {
                sqlStr += " and d_name like '%" + name + "%'";
            }
            if (!"".equals(fmis)) {
                sqlStr += " and d_fmis = '" + fmis + "'";
            }
            if (!"".equals(project)) {
                sqlStr += " and d_project like '%" + project + "%'";
            }
            sqlStr += " order by d_id desc";
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

    protected int save(DTBHelper dtbHelper) throws BizActionException {
        //身份证不能修改
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String d_fmis = dtbHelper.getStringValue("D_FMIS");
            String d_project = dtbHelper.getStringValue("D_PROJECT");
            String d_jx = dtbHelper.getStringValue("D_JX");
            String d_gz = dtbHelper.getStringValue("D_GZ");
            String d_name = dtbHelper.getStringValue("D_NAME");
            String d_dept = dtbHelper.getStringValue("D_DEPT");
            String xjx =d_jx;
            String xgz=d_gz;
            if (d_jx.equals("")&&d_gz.equals("")){
                log.error("绩效和奖金不能都为空");
                dtbHelper.setError("usermg-err-add-002", "绩效和奖金不能都为空" );
            }else {
                String sql="update nh_gjx set d_jx=?, d_gz=? ,d_name=?,d_dept=?,d_xjx=?,d_xgz=? where d_fmis=? and d_project=？ ";
                dbBean.executeUpdate(sql,d_jx,d_gz,d_name,d_dept,xjx,xgz,d_fmis,d_project);
                dbBean.executeCommit();
                flag=1;
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
                int no=Integer.parseInt(user);
                int i = dbBean.executeUpdate("delete from NH_GJX   where d_id = ?", no);
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
            String d_fmis = dtbHelper.getStringValue("D_FMIS");
            String d_project = dtbHelper.getStringValue("D_PROJECT");
            String d_jx = dtbHelper.getStringValue("D_JX");
            String d_gz = dtbHelper.getStringValue("D_GZ");
            String d_name = dtbHelper.getStringValue("D_NAME");
            String d_dept = dtbHelper.getStringValue("D_DEPT");
            String xjx =d_jx;
            String xgz=d_gz;
            if (d_jx.equals("")&&d_gz.equals("")){
                log.error("绩效和奖金不能都为空");
                dtbHelper.setError("usermg-err-add-002", "绩效和奖金不能都为空" );
            }else {
            String sql="insert into nh_gjx(d_fmis,d_project,d_jx,d_gz,d_name,d_dept,d_xjx,d_xgz) values(?,?,?,?,?,?) ";
            dbBean.executeUpdate(sql,d_fmis,d_project,d_jx,d_gz,d_name,d_dept,xjx,xgz);
            dbBean.executeCommit();
            flag=1;
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

}
