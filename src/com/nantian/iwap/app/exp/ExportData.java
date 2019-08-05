package com.nantian.iwap.app.exp;

import java.util.List;
import java.util.Map;

/**
 * 导出数据接口，实现该类须实现某一类型文件数据导出
 * 
 * @author stormhua
 *
 */
@SuppressWarnings("rawtypes")
public interface ExportData {

	/**
	 * 实现数据导出功能
	 * 
	 * @param fileExt
	 *            导出文件类型
	 * @param dataList
	 *            数据数列
	 * @param titleList
	 *            导出数据列名
	 * @return
	 */
	public Map exportData1(String fileExt, List dataList, List<Map<String, String>> titleLis,String s);
	public Map exportData(String fileExt, List dataList, List<Map<String, String>> titleLis);

	public Map exportDataPunish(String fileExt, List<Map<String, String>> list, Map<String,String> title, int[] mergeIndex);

	public Map exportBranchData(String fileExt, List<Map<String, Object>> list, Map<String,String> title, Map<String,String> title2,String payMonth);

	public Map exportManage(String filetype, List<Map<String, Object>> dataList, List<Map<String, String>> titleList, List<String> titleId, String s, String[] head0, String[] head1, String[] headnum0);
}
