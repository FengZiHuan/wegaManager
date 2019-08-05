package com.nantian.iwap.app.action;

import java.util.List;
import java.util.Map;

import com.nantian.iwap.app.util.MenuFactory;
import com.nantian.iwap.biz.flow.BaseBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.databus.DTBHelper;
/**
 * ClassName: UserMenu <br/> 
 * Function: 获取用户菜单<br/> 
 * date: 2016年2月2日 下午5:07:59 <br/> 
 * @author weixiaohua 
 * @version  
 * @since JDK 1.6 
 * Copyright (c) 2016, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class UserMenuAction extends BaseBizAction {

	@SuppressWarnings("rawtypes")
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		String userId=(String)dtbHelper.getObjectValue("userInfo.ACCT_ID");
		//String userId=(String)user.get("ACCT_ID");
		List userAuth=MenuFactory.getInstance().findMenuByUser(userId);
		dtbHelper.setRstData("rows", userAuth);
		return 1;
	}

}
