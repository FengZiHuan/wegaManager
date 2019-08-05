package com.nantian.iwap.action.wagechanges;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.nantian.iwap.app.util.DateUtil;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;


public class MergeSummaryCheck extends TransactionBizAction{

	private static Logger log = Logger.getLogger(MergeSummaryCheck.class);
	

	@Override
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		String option = dtbHelper.getStringValue("option");
		if (StringUtil.isBlank(option)) {
			return query(dtbHelper);
		}
		if ("viewDetail".equals(option)) {
			return viewDetail(dtbHelper);
		}
		if("giveBack".equals(option)){
			return giveBack(dtbHelper);
		}
		return 0;
	}
	
	protected int query(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			int start = Integer.valueOf(dtbHelper.getStringValue("start"));
			int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
			String payMonth = dtbHelper.getStringValue("payMonth");
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String, Object>> dataList = null;
			String sqlStr="select to_char(f.pay_month,'yyyy/MM') PAY_MONTH,count(DISTINCT f.branch_name) BRANCH_TOTAL, sum(f.companyandpersonal_count) TOTAL_SUM from fiveRisks_oneGold f where 1=1 and f.summaryflag='1' ";

			
			if (!"".equals(payMonth)) {
				sqlStr += " and PAY_MONTH= to_date('"+ payMonth + "','yyyy') ";
			}
			sqlStr +="group by f.pay_month";
			System.out.println("-------"+sqlStr);
			dataList = dbBean.queryForList(sqlStr, page);
			dtbHelper.setRstData("rows", dataList);
			dtbHelper.setRstData("total", page.getTotalCount());
			flag = 1;
		} catch (Exception e) {
			log.error("分行信息查询出错", e);
			dtbHelper.setError("subsidymg-err-qry", "[分行信息查询出错]" + e.getMessage());
		}
		return flag;
	}

	protected int viewDetail(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			String payMonth = dtbHelper.getStringValue("month");
			Map<String,String> map = new HashMap<String, String>();
			map.put("payMonth", payMonth);
			dtbHelper.setRstData(map);
			flag = 2;
		} catch (Exception e) {
			log.error("查看出错", e);
			dtbHelper.setError("subsidymg-err-qry", "[查看出错]" + e.getMessage());
		}
		return flag;
	}

	protected int giveBack(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		DBAccessBean dbBean = null;
		try {
			dbBean = DBAccessPool.getDbBean();
			dbBean.setAutoCommit(false);
			String payMonth = dtbHelper.getStringValue("payMonth");
			StringBuffer sbf=new StringBuffer();
			sbf.append("UPDATE fiveRisks_oneGold set SUMMARYFLAG='0' where PAY_MONTH=to_date("+payMonth+",'yyyy/MM')");
			dbBean.executeUpdate(sbf.toString());
			dbBean.executeCommit();
			flag = 1;
		} catch (Exception e) {
			log.error("修改出错", e);
			dtbHelper.setError("subsidymg-err-qry", "[修改出错]" + e.getMessage());
		}
		return flag;
	}
	
}
