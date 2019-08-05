package com.nantian.iwap.app.imp.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.nantian.iwap.app.imp.SystemVariable;
import com.nantian.iwap.common.util.DateUtil;
import com.nantian.iwap.databus.DTBHelper;

public class SystemVariableImpl implements SystemVariable {
	private static Logger log = Logger.getLogger(SystemVariableImpl.class);
	private static SystemVariableImpl systemVariableImpl = null;

	private DTBHelper dtbHelper;

	private SystemVariableImpl() {

	}

	public DTBHelper getDtbHelper() {
		return dtbHelper;
	}

	public void setDtbHelper(DTBHelper dtbHelper) {
		this.dtbHelper = dtbHelper;
	}

	public static SystemVariableImpl getInstance() {
		if (systemVariableImpl == null) {
			systemVariableImpl = new SystemVariableImpl();
		}
		return systemVariableImpl;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String transVariable(String variable) {
		Map map = getSystemVariable();
		Pattern p = Pattern.compile("\\$\\{[^}]+}");
		Matcher m = p.matcher(variable);
		boolean isFound = false;
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				String varName = m.group(i);
				if (varName.length() < 3)
					continue;
				varName = varName.substring(2, varName.length() - 1);
				String varValue = (String) map.get(varName);
				if (varValue != null && !"".equals(varValue) && !varValue.startsWith("$")) {
					variable = variable.replaceAll("\\$\\{" + varName + "}", varValue);
					isFound = true;
					break;
				}
			}
			if (isFound) {
				break;
			}
		}
		return variable;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map getSystemVariable() {
		Map variable = new HashMap();
		try {
			if (dtbHelper != null) {
				variable.put("ACCT_ID", dtbHelper.getStringValue("userInfo.ACCT_ID"));
				variable.put("ORG_ID", dtbHelper.getStringValue("userInfo.ORG_ID"));
			}
		} catch (Exception e) {
			log.warn("初始化数据总线系统参数出错", e);
		}

		try {
			variable.put("sysdate", DateUtil.getCurrentDate());
			variable.put("systime", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			log.warn("初始化系统参数出错", e);
		}
		return variable;
	}

}
