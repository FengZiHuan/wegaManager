package com.nantian.iwap.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.databus.DataTransferBus;
import com.nantian.iwap.ibatis.IWAPBatisDBPool;
import com.nantian.iwap.ibatis.IWAPBatisFactory;
import com.nantian.iwap.persistence.DBAccessConfig;
import com.nantian.iwap.web.WebEnv;
/**
 * ClassName: MenuFactory <br/> 
 * Function:菜单工程，实现菜单缓存，菜单查找，角色菜单，人员菜单 <br/>
 * 
 * @author weixiaohua 
 * @version  
 * @since JDK 1.6 
 * Copyright (c) 2016, 广州南天电脑系统有限公司 All Rights Reserved.
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class MenuFactory {
	private static Logger log=Logger.getLogger(MenuFactory.class);
	private static MenuFactory menu=null;
	private List<Menu> menuTreeList=new ArrayList<Menu>();
	private Map txcodeMap=new HashMap();
	
	
	private Map menuMap=new HashMap();
	
	public static synchronized  MenuFactory getInstance(){
		if(menu==null){
			menu=new MenuFactory();
			menu.init();
		}
		return menu;
	}
	
	
	public synchronized void init(){
		menuMap.clear();
		menuTreeList.clear();
		txcodeMap.clear();
		IWAPBatisDBPool pool=IWAPBatisFactory.getInstance().getIwapBatisPool();
		List<Map> menuList=pool.queryForList("queryAllMenu", null);
		IWAPBatisFactory.getInstance().closeIwapBatisPool();
		for(int i=0;i<menuList.size();i++){
			Map row=menuList.get(i);
			if(row.get("PMODULE_ID")==null){
				Menu menu=mapToMenu(row);
				menuTreeList.add(menu);
				buildMenuTree(menuList,menu,true);
			}
			txcodeMap.put(row.get("MODULE_OPCODE")==null?row.get("MODULE_ID"):row.get("MODULE_OPCODE"), row);
			menuMap.put(row.get("MODULE_ID"), row);
		}
		
	}
	
	public Menu getMenuByTxcode(String txcode,List<Menu> menus){
		for(int i=0;i<menus.size();i++){
			Menu menu=menus.get(i);
			if(menu.getModuleOpCode()!=null&&txcode.equals(menu.getModuleOpCode())){
				return menu;
			}else{
				Menu m=getMenuByTxcode(txcode,menu.getChild());
				if(m!=null){
					return m;
				}
			}
		}
		return null;
	}
	
	private Menu mapToMenu(Map<String,String> map){
		Menu m=new Menu();
		m.setBigIcon(map.get("BIG_ICON"));
		m.setHelpPage(map.get("HELP_PAGE"));
		m.setHelpTitle(map.get("HELP_TITLE"));
		m.setModuleHide(map.get("MODULE_HIDE"));
		m.setModuleId(map.get("MODULE_ID"));
		m.setModuleNm(map.get("MODULE_NM"));
		m.setModuleOpCode(map.get("MODULE_OPCODE"));
		m.setModuleTarget(map.get("MODULE_TARGET"));
		m.setModuleType(map.get("MODULE_TYPE"));
		m.setModuleUrl(map.get("MODULE_URL"));
		m.setModuleValid(map.get("MODULE_VALID"));
		m.setParamOpCode(map.get("PARAM_OPCODE"));
		m.setpModuleId(map.get("PMODULE_ID"));
		m.setSmallIcon(map.get("SMALL_ICON"));
		return m;
	}
	
	private void buildMenuTree(List<Map> list,Menu menu,boolean allowFunc){
		for(int i=0;i<list.size();i++){
			Map row=list.get(i);
			if(menu.getModuleId().equals(row.get("PMODULE_ID"))){
				Menu child=mapToMenu(row);
				if(allowFunc){
					menu.addChild(child);
				}else{
					if(!"2".equals(child.getModuleType())){//操作功能
						menu.addChild(child);
					}
				}
				if(child.getModuleType()!=null&&"2".equals(child.getModuleType())){//操作功能
					continue;
				}else{
					buildMenuTree(list,child,allowFunc);
				}
			}
		}
	}
	/**
	 * 
	 * findMenuByRole:通过角色获取权限<br/>  
	 * @author weixiaohua 
	 * @param roleId 
	 * @since JDK 1.6
	 */
	public List<Menu> findMenuByRole(String roleId){
		IWAPBatisDBPool pool=IWAPBatisFactory.getInstance().getIwapBatisPool();
		DataTransferBus dtb=new DataTransferBus();
		dtb.addElement("role_id", roleId);
		DTBHelper dtbHelp=new DTBHelper(dtb);
		List<Map> roleMenus=pool.queryForList("queryRoleMenu", dtbHelp);
		IWAPBatisFactory.getInstance().closeIwapBatisPool();
		List authMenu=new ArrayList();
		for(int i=0;i<roleMenus.size();i++){
			Map map=roleMenus.get(i);
			Map row=(Map)this.menuMap.get(map.get("MODULE_ID"));
			authMenu.add(row);
		}
		List authMenuTree=new ArrayList();
		for(int i=0;i<authMenu.size();i++){
			Map row=(Map)authMenu.get(i);
			if(row.get("PMODULE_ID")==null){
				Menu menu=mapToMenu(row);
				authMenuTree.add(menu);
				buildMenuTree(authMenu,menu,false);
			}
		}
		return authMenuTree;
		
	}
	/**
	 * findMenuByUser:通过用户ID获取用户菜单<br/> 
	 * 
	 * @author weixiaohua 
	 * @param userId 
	 * @since JDK 1.6
	 */
	public List<Menu> findMenuByUser(String userId){
		return findAllMenuByUser(userId,false);
	}
	/**
	 * findMenuByUser:通过用户ID获取用户功能<br/> 
	 * 
	 * @author weixiaohua 
	 * @param userId 
	 * @since JDK 1.6
	 */
	public List<Menu> findFuncByUser(String userId){
		return findAllMenuByUser(userId,true);
	}
	/**
	 * findAllMenuByUser:根据用户获取功能权限 <br/> 
	 * @author weixiaohua 
	 * @param userId
	 * @param allowFunc
	 * @return 
	 * @since JDK 1.6
	 */
	private List<Menu> findAllMenuByUser(String userId,boolean allowFunc){
		IWAPBatisDBPool pool=IWAPBatisFactory.getInstance().getIwapBatisPool();
		DataTransferBus dtb=new DataTransferBus();
		dtb.addElement("ACCT_ID", userId);
		DTBHelper dtbHelp=new DTBHelper(dtb);
		List<Map> list=pool.queryForList("queryUserAuth", dtbHelp);
		IWAPBatisFactory.getInstance().closeIwapBatisPool();
		List authMenu=new ArrayList();
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			Map row=(Map)this.menuMap.get(map.get("MODULE_ID"));
			if(!authMenu.contains(row))//去重
				authMenu.add(row);
		}
		List<Menu> authMenuTree=new ArrayList<Menu>();
		for(int i=0;i<authMenu.size();i++){
			Map row=(Map)authMenu.get(i);
			if(row.get("PMODULE_ID")==null){
				Menu menu=mapToMenu(row);
				authMenuTree.add(menu);
				buildMenuTree(authMenu,menu,allowFunc);
			}
		}
		return authMenuTree;
	}
	/**
	 * findMenuByUser:通过用户权限获取用户菜单<br/> 
	 * 
	 * @author weixiaohua 
	 * @param userId 
	 * @since JDK 1.6
	 */
	public List<Menu> findMenuByUserAuth(List userAuth){
		List authMenu=new ArrayList();
		for(int i=0;i<userAuth.size();i++){
			String mid=(String)userAuth.get(i);
			Map row=(Map)this.menuMap.get(mid);
			if(!authMenu.contains(row))//去重
				authMenu.add(row);
		}
		List authMenuTree=new ArrayList();
		for(int i=0;i<authMenu.size();i++){
			Map row=(Map)authMenu.get(i);
			if(row.get("PMODULE_ID")==null){
				Menu menu=mapToMenu(row);
				authMenuTree.add(menu);
				buildMenuTree(authMenu,menu,false);
			}
		}
		return authMenuTree;
	}
	
	
	public List<Menu> getMenuTreeList() {
		return menuTreeList;
	}


	public Map getMenuMap() {
		return menuMap;
	}


	public Map getTxcodeMap() {
		return txcodeMap;
	}


	public static void main(String[] args){
		WebEnv.webappPath="/Users/weixiaohua/app/iwap-app/WebContent/";
		com.nantian.iwap.persistence.DBAccessConfig config=DBAccessConfig.getDBAccessConfig();
		config.init("WEB-INF/config/dbsource.xml");
		com.nantian.iwap.ibatis.IWAPBatisFactory factory=IWAPBatisFactory.getInstance();
		factory.init("mysql", "conf/systemMapper.xml");
		MenuFactory menuFactory=MenuFactory.getInstance();
		menuFactory.init();
		
		
	}
	
}
