package com.nantian.iwap.app.imp;

import java.util.Map;

/**
 * @Description 导入数据之前的处理逻辑接口，
 * 实现该接口并在sys_imp_cfg增加配置
 * @author stormhua
 * @date 2014-7-15
 * @Copy right by 广州南天电脑系统有限公司 2011-2016
 */
public interface IBeforeProcess {
	/**
	 * @Description 执行导入执行处理逻辑
	 * @param config 配置信息
	 * @param dataFile 导入文件,带路径
	 * @return true 处理成功,false处理失败
	 * 2014-7-15
	 */
	@SuppressWarnings("rawtypes")
	boolean process(Map config,Object dataFile);
}
