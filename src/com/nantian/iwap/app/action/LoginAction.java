package com.nantian.iwap.app.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.app.util.PasswordEncrypt;
import com.nantian.iwap.biz.actions.TransactionBatisAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.DateUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.ibatis.IWAPBatisDBPool;
import com.nantian.iwap.ibatis.IWAPBatisFactory;
import com.nantian.iwap.security.cipher.impl.IwapMd5Encrypt;
/**
 * ClassName: LoginAction <br/> 
 * Function:登陆验证 <br/> 
 * date: 2015年12月22日 上午11:17:03 <br/> 
 * @author weixiaohua 
 * @version  
 * @since JDK 1.6 
 * Copyright (c) 2015, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class LoginAction extends TransactionBatisAction {
	private static Logger log=Logger.getLogger(LoginAction.class);
	private String checkPwd="true";//是否进行密码校验，默认是需要，为了支持单点登录
	private int pwdErrCnt=3;//密码最大允许错误次数
	private int pwdExpireDays=90;//密码过期天数
	private String encryptClazz="com.nantian.iwap.app.util.DefaultEncrypt";//默认加密方式
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int actionExecute(DTBHelper dtb) throws BizActionException {
		int flag=1;
		String user=dtb.getStringValue("userId");
		String pwd=dtb.getStringValue("password");
		dtb.getDataTransferBus().addElement("errCnt", pwdErrCnt);
		IWAPBatisDBPool dbpool=IWAPBatisFactory.getInstance().getIwapBatisPool();
		//获取用户信息
		Map map=dbpool.queryForMap("loginSql", dtb);
		if(map==null){//用户不存在
			dtb.setError("login-err-002","用户不存在!");
			log.warn("用户不存在!");
			flag=-1;
		}else{
			if("2".equals(map.get("ACCT_STATUS"))){
				dtb.setError("login-err-005","账户被锁定!");
				log.error("账户被锁定!");
				flag=-1;
			}
			if("0".equals(map.get("ACCT_STATUS"))&&flag==1){
				dtb.setError("login-err-005","账户无效!");
				log.error("账户无效!");
				flag=-1;
			}
			if("true".equalsIgnoreCase(checkPwd)&&flag==1){
				try {
					PasswordEncrypt encrypt=(PasswordEncrypt)Class.forName(encryptClazz).newInstance();
					pwd=encrypt.encryptPassword(user, pwd);
				} catch (Exception e) {
					dtb.setError("login-err-004","系统错误!");
					log.error("加密类["+encryptClazz+"]实例化错误!",e);
					flag=-1;
				}
				if(!pwd.equals(map.get("ACCT_PWD"))){
					dtb.setError("login-err-003","密码错误!");
					log.warn("密码错误!");
					dbpool.update("updateUserErrCnt", dtb);
					flag=-1;
				}
			}
			if(flag==1){
				if(map.get("LAST_MDF_PWD_DT")!=null){
					try {
						int day=DateUtil.getPeriodDays(DateUtil.getDefaultDatePattern().format(map.get("LAST_MDF_PWD_DT")), DateUtil.getCurrentDate());
						if(day>this.getPwdExpireDays()){
							dtb.setError("login-err-006","密码已过期!");
							log.error("密码已过期!");
						}
					} catch (Exception e) {
						log.warn("日期比较出错！",e);
					}
				}/*else{
					dtb.setError("login-err-006","首次登陆需修改密码!");
					log.error("首次登陆需修改密码!");
				}
				/*登陆成功后修改密码错误次数*/
				dbpool.update("clearUserErrCnt", dtb);
				List roleMap=dbpool.queryForList("queryUserRole", dtb);
				List roles=new ArrayList();
				for(int i=0;i<roleMap.size();i++){
					Object role=((Map)roleMap.get(i)).get("ROLE_ID");
					roles.add(role);
				}
				map.put("roles", roles);
				dtb.setRstData(map);
			}
		}
		return flag;
	}

	public boolean validator(DTBHelper dbHelper) {
		if(dbHelper.getObjectValue("password")==null||dbHelper.getObjectValue("userId")==null){
			dbHelper.setError("login-err-001","用户名或密码不允许为空!");
			log.warn("用户名或密码不允许为空!");
		}
		return dbHelper.getObjectValue("password")!=null&&dbHelper.getObjectValue("userId")!=null;
	}
	
	
	
	
	public String getCheckPwd() {
		return checkPwd;
	}

	public void setCheckPwd(String checkPwd) {
		this.checkPwd = checkPwd;
	}

	public int getPwdErrCnt() {
		return pwdErrCnt;
	}

	public void setPwdErrCnt(int pwdErrCnt) {
		this.pwdErrCnt = pwdErrCnt;
	}

	public int getPwdExpireDays() {
		return pwdExpireDays;
	}

	public void setPwdExpireDays(int pwdExpireDays) {
		this.pwdExpireDays = pwdExpireDays;
	}

	public String getEncryptClazz() {
		return encryptClazz;
	}

	public void setEncryptClazz(String encryptClazz) {
		this.encryptClazz = encryptClazz;
	}

	public static void main(String[] args){
		System.out.println(Boolean.valueOf("1"));
		System.out.println(IwapMd5Encrypt.instance().getMD5ofStr("admin", "admin"));
	}

}
