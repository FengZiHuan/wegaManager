package com.nantian.iwap.action.wagechanges;

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

/**
 * ClassName: UserMgAction <br/>
 * Function: 请假管理<br/>
 *
 * @author f
 * @version 1.0
 * @since JDK 1.7 Copyright (c) 2019, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class SeePerformanceHistoryMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(CheckPerformanceMgAction.class);
    private String encryptClazz = "com.nantian.iwap.app.util.DefaultEncrypt";// 默认加密方式


    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");
        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }


        return 0;
    }

    protected int query(DTBHelper dtbHelper) throws BizActionException {

        int flag = 0;
        try {
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            String time = dtbHelper.getStringValue("time");
            String name = dtbHelper.getStringValue("name");
            String fmis = dtbHelper.getStringValue("fmis");
            String brand = dtbHelper.getStringValue("brand");
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sqlStr = "select rc_name,rc_fmis,rc_brand,rc_year,rc_money,to_char(rc_times,'yyyy-MM') rc_times from NH_RECORD_PERFORMANCE where 1=1";
            if (!"".equals(time)) {
                sqlStr += " and rc_times = to_date( '" + time + ",'yyyy/MM')'";
            }
            if (!"".equals(name)) {
                sqlStr += " and rc_name like '%" + name + "%'";
            }
            if (!"".equals(fmis)) {
                sqlStr += " and rc_fmis = '" +fmis + "'";
            }
            if (!"".equals(brand)) {
                sqlStr += " and rc_brand = '" + brand + "'";
            }
            sqlStr += " order by rc_times desc";
            dataList = dbBean.queryForList(sqlStr);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("total", page.getTotalCount());
            flag = 1;
        } catch (Exception e) {
            log.error("查询出错", e);
            dtbHelper.setError("usermg-err-qry", "[查询出错]" + e.getMessage());
        }
        return flag;
    }


}
