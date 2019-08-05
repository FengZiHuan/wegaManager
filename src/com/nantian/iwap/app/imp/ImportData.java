package com.nantian.iwap.app.imp;

import java.util.Map;

/**
 * 导入数据接口，实现该类须实现某一类型文件数据导入
 * 
 * @author stormhua
 *
 */
public interface ImportData {

	/**
	 * 实现文件导入功能
	 * 
	 * @param fileName
	 *            文件名
	 * @param importId
	 *            配置标识
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map importData(String fileName, String importId, boolean isOverWrite, String modelcode);

	/**
	 * 设置配置信息
	 * 
	 * @param map
	 */
	@SuppressWarnings("rawtypes")
	void setConfig(Map map);

}
