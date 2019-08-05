package com.nantian.iwap.app.imp;

import java.util.Map;

/**
 * IAllowRow接口定义是否允许该行数据
 * @author stormhua
 */
public interface IAllowRow {
	/**
	 * 实现是否忽略当前行记录，如果忽略则丢失该条数据 
	 * 如果不允许则返回false否则返回true
	 * @param rowRecord 记录
	 * @param
	 * @return
	 */
	public boolean allow(Map<String, String> rowRecord);
}
