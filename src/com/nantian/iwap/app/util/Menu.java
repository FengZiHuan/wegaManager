package com.nantian.iwap.app.util;

import java.util.ArrayList;
import java.util.List;

public class Menu {
	private String moduleId;//Id
	private String moduleNm;//名称
	private String moduleOpCode;//操作码
	private String pModuleId;//上级id
	private String moduleType;//类型
	private String moduleUrl;//链接地址
	private String moduleValid;//是否失效
	private String bigIcon;//大图标
	private String smallIcon;//小图标
	private String helpPage;//帮助页面
	private String helpTitle;//帮助标题
	private String moduleHide;//是否隐藏
	private String moduleTarget;//打开位置
	private String paramOpCode;//操作码参数
	private List<Menu> child=new ArrayList<Menu>();//子菜单
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleNm() {
		return moduleNm;
	}
	public void setModuleNm(String moduleNm) {
		this.moduleNm = moduleNm;
	}
	public String getModuleOpCode() {
		return moduleOpCode;
	}
	public void setModuleOpCode(String moduleOpCode) {
		this.moduleOpCode = moduleOpCode;
	}
	public String getpModuleId() {
		return pModuleId;
	}
	public void setpModuleId(String pModuleId) {
		this.pModuleId = pModuleId;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getModuleUrl() {
		return moduleUrl;
	}
	public void setModuleUrl(String moduleUrl) {
		this.moduleUrl = moduleUrl;
	}
	public String getModuleValid() {
		return moduleValid;
	}
	public void setModuleValid(String moduleValid) {
		this.moduleValid = moduleValid;
	}
	public String getBigIcon() {
		return bigIcon;
	}
	public void setBigIcon(String bigIcon) {
		this.bigIcon = bigIcon;
	}
	public String getSmallIcon() {
		return smallIcon;
	}
	public void setSmallIcon(String smallIcon) {
		this.smallIcon = smallIcon;
	}
	public String getHelpPage() {
		return helpPage;
	}
	public void setHelpPage(String helpPage) {
		this.helpPage = helpPage;
	}
	public String getHelpTitle() {
		return helpTitle;
	}
	public void setHelpTitle(String helpTitle) {
		this.helpTitle = helpTitle;
	}
	public String getModuleHide() {
		return moduleHide;
	}
	public void setModuleHide(String moduleHide) {
		this.moduleHide = moduleHide;
	}
	public String getModuleTarget() {
		return moduleTarget;
	}
	public void setModuleTarget(String moduleTarget) {
		this.moduleTarget = moduleTarget;
	}
	public String getParamOpCode() {
		return paramOpCode;
	}
	public void setParamOpCode(String paramOpCode) {
		this.paramOpCode = paramOpCode;
	}
	
	public void addChild(Menu m){
		this.child.add(m);
	}
	
	public List<Menu> getChild(){
		return this.child;
	}
}
