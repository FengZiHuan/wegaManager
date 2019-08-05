package com.nantian.iwap.action.pub;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.action.common.CRUDAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.PaginationSupport;

public class CityListAction extends CRUDAction {
	private static Logger logger = Logger.getLogger(CityListAction.class);

	@Override
	protected int query(DTBHelper dtbHelper) {
		int flag = 0;
		try {
			int start = Integer.valueOf(dtbHelper.getStringValue("start"));
			int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
			String liveCity = dtbHelper.getStringValue("live_city");
			String workCity = dtbHelper.getStringValue("work_city");
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			PaginationSupport page = new PaginationSupport(start, limit, limit);
			List<Map<String, Object>> dataList = null;
			String sqlStr="select live_city,work_city,distance,(select s.subsidy_amount from subsidy_standard s "+
					" where s.start_distance<c.distance and s.end_distance>c.distance) as subsidy_amount from city_distance c where 1=1 ";
			
			if (!"".equals(liveCity)) {
				sqlStr += " and LIVE_CITY like '%"+liveCity+ "%' ";
			}
			
			if (!"".equals(workCity)) {
				sqlStr += " and WORK_CITY like '%"+workCity+ "%' ";
			}
			System.out.println("-------"+sqlStr);
			
			dataList = dbBean.queryForList(sqlStr, page);
			dtbHelper.setRstData("rows", dataList);
			dtbHelper.setRstData("total", page.getTotalCount());
			flag = 1;
		} catch (Exception e) {
			logger.error("交流补贴查询出错", e);
			dtbHelper.setError("subsidymg-err-qry", "[交流补贴查询出错]" + e.getMessage());
		}
		return flag;
	}

	@Override
	protected int add(DTBHelper dtbHelper) {
		return 0;
	}

	@Override
	protected int save(DTBHelper dtbHelper) {
		
		return 0;
	}

	@Override
	protected int remove(DTBHelper dtbHelper) {
		
		return 0;
	}

	@Override
	protected int show(DTBHelper dtbHelper) throws BizActionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int other(DTBHelper dtbHelper) throws BizActionException {
		
		return 0;
	}

}
