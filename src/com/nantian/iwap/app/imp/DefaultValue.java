package com.nantian.iwap.app.imp;

import java.util.Map;
/**
 * 生成默认值
 * @author stormhua
 *
 */
public interface DefaultValue {
	/**
	 * 实现生成值业务规则，例如传入的记录<id:123,value:456> 
	 * 可以让id value的值连接在一起返回123456
	 * @param rowRecord 记录
	 * @param field 字段名称
	 * @return
	 */
	public String generateValue(Map<String, String> rowRecord,String field);
}
