package com.nantian.iwap.app.action;

import org.apache.log4j.Logger;

import com.nantian.iwap.app.util.PasswordEncrypt;
import com.nantian.iwap.biz.actions.BatisAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.ibatis.IWAPBatisFactory;

public class ModifyPasswordAction extends BatisAction {
	private static Logger log=Logger.getLogger(ModifyPasswordAction.class);
	private String encryptClazz="com.nantian.iwap.app.util.DefaultEncrypt";//默认加密方式
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		IWAPBatisFactory.getInstance().getIwapBatisPool().update("updatePwd", dtbHelper);
		return 1;
	}

	public boolean validator(DTBHelper dtb) {
		if(dtb.getObjectValue("orgPwd")==null){
			dtb.setError("modifyPwd-err-001","原密码不允许为空!");
			log.warn("原密码为空!");
			return false;
		}
		if(dtb.getObjectValue("newPwd")==null){
			dtb.setError("modifyPwd-err-001","新密码不允许为空!");
			log.warn("新密码为空!");
			return false;
		}
		if(dtb.getObjectValue("cfmPwd")==null){
			dtb.setError("modifyPwd-err-001","确认密码不允许为空!");
			log.warn("确认密码为空!");
			return false;
		}
		if(!dtb.getObjectValue("cfmPwd").equals(dtb.getObjectValue("newPwd"))){
			dtb.setError("modifyPwd-err-002","输入密码两次不一致!");
			log.warn("输入密码两次不一致!");
			return false;
		}
		try {
			String user=dtb.getStringValue("userInfo.ACCT_ID");
			String pwd="";
			PasswordEncrypt encrypt=(PasswordEncrypt)Class.forName(encryptClazz).newInstance();
			pwd=encrypt.encryptPassword(user, dtb.getStringValue("orgPwd"));
			String orgPwd=dtb.getStringValue("userInfo.ACCT_PWD");//userInfo
			if(!pwd.equals(orgPwd)){
				dtb.setError("modifyPwd-err-004","旧密码不正确!");
				log.warn("旧密码不正确!");
				return false;
			}
			dtb.setRstData("userId", user);
			dtb.setRstData("currDate",new java.sql.Date(System.currentTimeMillis()));
			dtb.setRstData("targetPwd", encrypt.encryptPassword(user, dtb.getStringValue("newPwd")));
		} catch (Exception e) {
			dtb.setError("modifyPwd-err-003","系统错误!");
			return false;
		}
		return true;
	}

	public String getEncryptClazz() {
		return encryptClazz;
	}

	public void setEncryptClazz(String encryptClazz) {
		this.encryptClazz = encryptClazz;
	}
	
}
