package com.nantian.iwap.app.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.nantian.iwap.common.util.DateUtil;

public class IWAPSessionListener implements HttpSessionListener,HttpSessionAttributeListener {
	private static Map<String,Map<String,String>> onlineUser=new HashMap<String,Map<String,String>>();
	private static Map<String,HttpSession> userSession=new HashMap<String,HttpSession>();
	
	/**
	 * listOnline:获取在线用户列表 <br/> 
	 * 
	 * @author weixiaohua 
	 * @param start
	 * @param limit
	 * @return 
	 * @since JDK 1.6
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map listOnline(int start,int limit){
		Map map=new HashMap();
		Object[] keys=onlineUser.keySet().toArray();
		List list=new ArrayList();
		for(int i=start;i<keys.length&&i<limit;i++){
			Map row=onlineUser.get(keys[i]);
			HttpSession session=userSession.get(row.get("ACCT_ID"));
			row.put("LAST_TIME", DateUtil.getDefaultDatePattern().format(new Date(session.getLastAccessedTime())));
			list.add(row);
		}
		map.put("rows", list);
		map.put("total", onlineUser.size());
		return map;
	}
	/**
	 * logout:注销指定用户<br/> 
	 * 
	 * @author weixiaohua 
	 * @param userId 
	 * @since JDK 1.6
	 */
	public static void logout(String userId){
		if(userSession.get(userId)!=null){
			userSession.get(userId).invalidate();
		}
	}
	
	public void sessionCreated(HttpSessionEvent sessionEvent) {
	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		if(sessionEvent.getSession().getAttribute("userInfo")!=null){//用户登录
			Map map=(Map)sessionEvent.getSession().getAttribute("userInfo");
			String acctId=(String)map.get("ACCT_ID");
			userSession.remove(acctId);
			onlineUser.remove(sessionEvent.getSession().getId());
		}
	}

	public void attributeAdded(HttpSessionBindingEvent sbe) {
		if(sbe.getName().equals("userInfo")){//用户登录
			Map map=(Map)sbe.getValue();
			String acctId=(String)map.get("ACCT_ID");
			if(userSession.get(acctId)!=null){//把之前的用户Session至成失效
				onlineUser.remove(userSession.get(acctId).getId());
				userSession.get(acctId).invalidate();
			}
			userSession.put(acctId, sbe.getSession());
			onlineUser.put(sbe.getSession().getId(), map);
		}
	}

	public void attributeRemoved(HttpSessionBindingEvent sbe) {
		if(sbe.getName().equals("userInfo")){//用户登录
			Map map=(Map)sbe.getValue();
			String acctId=(String)map.get("ACCT_ID");
			userSession.remove(acctId);
			onlineUser.remove(sbe.getSession().getId());
		}
	}

	public void attributeReplaced(HttpSessionBindingEvent sbe) {
	}

}
