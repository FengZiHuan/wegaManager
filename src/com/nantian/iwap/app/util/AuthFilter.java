package com.nantian.iwap.app.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.core.Validation;
import com.nantian.iwap.databus.DTBHelper;
@SuppressWarnings({ "rawtypes","unchecked" })
public class AuthFilter implements Validation{
	private String url="/index.jsp";
	private boolean hasAuth(List<Menu> list,String txcode,DTBHelper dtbHelper){
		MenuFactory factory=MenuFactory.getInstance();
		if(factory.getTxcodeMap().containsKey(txcode)){//需要做权限控制
			Menu m=factory.getMenuByTxcode(txcode, factory.getMenuTreeList());//获取指定交易码下菜单及操作权限
			List<Menu> allFuns=m.getChild();//获取功能操作
			Menu authMenu=factory.getMenuByTxcode(txcode,list);//获取用户交易码下操作权限
			if(authMenu==null){//用户没有菜单权限
				return false;
			}else{
				for(int i=0;i<allFuns.size();i++){
					Menu func=allFuns.get(i);
					if(allowAuth(dtbHelper,func.getModuleOpCode(),m)){//判断请求是否需要做权限控制
						//判断用户是否有权限
						List<Menu> authFuncs=authMenu.getChild();
						boolean authFlag=false;
						for(int x=0;x<authFuncs.size();x++){
							Menu authFunc=authFuncs.get(x);
							if(func.getModuleOpCode().equals(authFunc.getModuleOpCode())){
								authFlag=true;
								break;
							}
						}
						return authFlag;
					}
				}
			}
			return true;
		}else{//不需要做权限控制
			return true;
		}
		
	}
	
	private boolean allowAuth(DTBHelper dtbHelper,String opcode,Menu menu){
		boolean auth=false;
		for(int j=0;j<menu.getChild().size();j++){
			Menu child=menu.getChild().get(j);
			String paramOpCode=child.getParamOpCode();
			if(!StringUtil.isBlank(dtbHelper.getStringValue(paramOpCode))&&child.getModuleOpCode().equals(opcode)&&
					dtbHelper.getStringValue(paramOpCode).equals(child.getModuleOpCode())){
				auth=true;
			}
		}
		return auth;
	}
	/**
	 * getCurrentTxcodeAuth:获取当前用户，当前交易权限 <br/>  
	 * 
	 * @author weixiaohua 
	 * @return 
	 * @since JDK 1.6
	 */
	private  Map getCurrentTxcodeAuth(DTBHelper dtbHelper){
		Map rst=new HashMap();
		MenuFactory factory=MenuFactory.getInstance();
		Map txcodeMap=factory.getTxcodeMap();
		String txcode=dtbHelper.getStringValue("_txcode");
		String tagetTxcode=dtbHelper.getStringValue(txcode);
		if(txcodeMap.containsKey(tagetTxcode)){//权限控制范围交易码
			if(dtbHelper.getObjectValue("userInfo.ACCT_ID")==null){//未登陆
				rst.put("auth", "none");
			}else{
				String userId=(String)dtbHelper.getObjectValue("userInfo.ACCT_ID");
				//String userId=(String)user.get("ACCT_ID");
				List<Menu> userAuth=factory.findFuncByUser(userId);
				Menu m=factory.getMenuByTxcode(tagetTxcode, factory.getMenuTreeList());//获取指定交易码下菜单及操作权限
				List<Menu> allFuns=m.getChild();//获取功能操作
				Menu authMenu=factory.getMenuByTxcode(tagetTxcode,userAuth);//获取用户交易码下操作权限
				if(authMenu==null){//用户没有菜单权限
					rst.put("auth", "none");
				}else{
					List<Menu> authFuncs=authMenu.getChild();
					for(int i=0;i<allFuns.size();i++){
						Menu func=allFuns.get(i);
						for(int x=0;x<authFuncs.size();x++){
							Menu authFunc=authFuncs.get(x);
							if(func.getModuleOpCode().equals(authFunc.getModuleOpCode())){
								if(rst.get("auth")!=null){
									rst.put("auth", rst.get("auth")+","+func.getModuleOpCode());
								}else{
									rst.put("auth",func.getModuleOpCode());
								}
							}
						}
					}
				}
			}
		}else{
			rst.put("auth", "all");
		}
		return rst;
	}

	/**
	 * TODO 校验逻辑
	 * @see com.nantian.iwap.core.Validation#validation(com.nantian.iwap.databus.DTBHelper)
	 */
	public boolean validation(DTBHelper dtbHelper) {
		boolean authFlag=true;
		Map txcodeMap=MenuFactory.getInstance().getTxcodeMap();
		String txcode=dtbHelper.getStringValue("_txcode");
		String tagetTxcode=dtbHelper.getStringValue(txcode);
		if(txcodeMap.containsKey(tagetTxcode)){//权限控制范围交易码
			if(dtbHelper.getObjectValue("userInfo.ACCT_ID")==null){//未登陆
				authFlag=false;
			}else{
				String userId=(String)dtbHelper.getObjectValue("userInfo.ACCT_ID");
				//String userId=(String)user.get("ACCT_ID");
				List<Menu> userAuth=MenuFactory.getInstance().findFuncByUser(userId);
				//_RequestDispatcher
				authFlag=hasAuth(userAuth,tagetTxcode,dtbHelper);
				if(!authFlag){
					if(dtbHelper.isFormRequest()&&!authFlag){
						dtbHelper.setViewName(url);
					}
					dtbHelper.setError("SYS_ERR_AUTH", "用户没有权限!");
				}
				
			}
		}
		Map rst=getCurrentTxcodeAuth(dtbHelper);
		dtbHelper.getResultContext().setAuth((String)rst.get("auth"));
		return authFlag;
	}

}
