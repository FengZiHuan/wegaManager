package com.nantian.iwap.app.action;

import java.util.Map;

import com.nantian.iwap.app.listener.IWAPSessionListener;
import com.nantian.iwap.biz.flow.BaseBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;

public class OnlineUserAction extends BaseBizAction {

	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		int start=0;
		int limit=10;
		if(!StringUtil.isBlank(dtbHelper.getStringValue("start"))){
			start=Integer.parseInt(dtbHelper.getStringValue("start"));
		}
		if(!StringUtil.isBlank(dtbHelper.getStringValue("limit"))){
			limit=Integer.parseInt(dtbHelper.getStringValue("limit"));
		}
		Map rst=IWAPSessionListener.listOnline(start, limit);
		dtbHelper.setRstData(rst);
		return 1;
	}

}
