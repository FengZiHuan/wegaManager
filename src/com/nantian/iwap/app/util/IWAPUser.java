package com.nantian.iwap.app.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * ClassName: User <br/> 
 * Function: session中存放的用户对象 <br/> 
 * Reason: 方便获取用户信息<br/> 
 * date: 2015年9月19日 上午10:18:28 <br/> 
 * @author weixiaohua 
 * @version  
 * @since JDK 1.6 
 * Copyright (c) 2015, 广州南天电脑系统有限公司 All Rights Reserved.
 */
@SuppressWarnings("rawtypes")
public class IWAPUser implements Serializable {
	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.6 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;//用户编号
	private String userNm;//用户名称
	private String orgId;//部门编号
	private String orgNm;//部门名称
	private List dataAuth;//数据权限
	private List roles;//用户所属角色
	private List roleNm;//用户所属角色名称
	private Map attribute;//自定义属性
	private List funcAuth;//功能权限
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	
	public List getDataAuth() {
		return dataAuth;
	}
	public void setDataAuth(List dataAuth) {
		this.dataAuth = dataAuth;
	}
	public List getRoles() {
		return roles;
	}
	public void setRoles(List roles) {
		this.roles = roles;
	}
	public List getRoleNm() {
		return roleNm;
	}
	public void setRoleNm(List roleNm) {
		this.roleNm = roleNm;
	}
	public Map getAttribute() {
		return attribute;
	}
	public void setAttribute(Map attribute) {
		this.attribute = attribute;
	}
	
	public Object getAttrVal(String key){
		return this.attribute.get(key);
	}
	public List getFuncAuth() {
		return funcAuth;
	}
	public void setFuncAuth(List funcAuth) {
		this.funcAuth = funcAuth;
	}
	
}
