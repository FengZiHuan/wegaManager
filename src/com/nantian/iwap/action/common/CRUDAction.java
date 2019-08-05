package com.nantian.iwap.action.common;

import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;

public abstract class CRUDAction extends TransactionBizAction {
	protected  DBAccessBean dbBean = DBAccessPool.getDbBean();
	
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		String option = dtbHelper.getStringValue("option");
		System.out.println("option----"+option);
		if(StringUtil.isBlank(option)){
			return query(dtbHelper);	
		}else if ("add".equals(option)) {
			return add(dtbHelper);
		} else if ("save".equals(option)) {
			return save(dtbHelper);
		} else if ("show".equals(option)) {
			return show(dtbHelper);
		} else if ("remove".equals(option)) {
			return remove(dtbHelper);
		} else {
			return other(dtbHelper);
		}
	}

	/**
	 * 执行查询操作
	 * 
	 * @param dtbHelper
	 * @throws BizActionException
	 */
	protected abstract int query(DTBHelper dtbHelper) throws BizActionException;

	/**
	 * 执行新增操作
	 * 
	 * @param dtbHelper
	 * @throws BizActionException
	 */
	protected abstract int add(DTBHelper dtbHelper) throws BizActionException;

	/**
	 * 执行保存操作
	 * 
	 * @param dtbHelper
	 * @throws BizActionException
	 */
	protected abstract int save(DTBHelper dtbHelper) throws BizActionException;

	/**
	 * 执行明细操作
	 * 
	 * @param dtbHelper
	 * @throws BizActionException
	 */
	protected abstract int show(DTBHelper dtbHelper) throws BizActionException;

	/**
	 * 执行删除操作
	 * 
	 * @param dtbHelper
	 * @throws BizActionException
	 */
	protected abstract int remove(DTBHelper dtbHelper)
			throws BizActionException;

	/**
	 * 执行其他操作
	 * 
	 * @param dtbHelper
	 * @throws BizActionException
	 * @说明 可用option判断后使用相应方法，并不是只有一种其他操作的方法
	 */
	protected abstract int other(DTBHelper dtbHelper) throws BizActionException;
}
