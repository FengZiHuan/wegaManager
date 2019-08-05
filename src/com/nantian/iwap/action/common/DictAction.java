package com.nantian.iwap.action.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nantian.iwap.biz.flow.BaseBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.ofpiwap.common.DataMapDictionary;

/**
 * 
 * FileName:DictAction.java<br>
 * Description:解析多个数据字典名<br>
 * History:<br>
 * Date Author Desc<br>
 * -------------------------------------------------------<br>
 * 2016-1-29 10:24:58 wjj
 */
public class DictAction extends BaseBizAction {
	private static Logger logger = Logger.getLogger(DictAction.class);

	@SuppressWarnings("rawtypes")
	@Override
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		int flag = 0;
		try {
			String dictNm = dtbHelper.getStringValue("dictNm");
			String[] dictStr = dictNm.split(",");
			for (String dict : dictStr) {
				logger.info("准备获取数据字典：[" + dict + "]");
				if (dict == null || "".equals(dict.trim())) {
					logger.info("数据字典名称有误，跳过");
					continue;
				}
				Map mapName = DataMapDictionary.getInstance().getDataMap(dict);
				if (mapName == null) {
					logger.info("系统缺少该数据字典，跳过");
					continue;
				}
				JSONArray jsonArray = new JSONArray();
				Set keys = mapName.keySet();
				Iterator it = keys.iterator();
				while (it.hasNext()) {
					JSONObject jsonObject = new JSONObject();
					String key = it.next().toString();
					String value = mapName.get(key).toString();
					jsonObject.put("id", key);
					jsonObject.put("name", value);
					jsonArray.add(jsonObject);
					dtbHelper.getDataTransferBus().getResultContext().addData(dict, jsonArray.toArray());
				}
				logger.info("数据字典：" + dict + ",定义信息:" + mapName);
			}
			flag = 1;
		} catch (Exception e) {
			logger.error("查询数据字典出错", e);
			dtbHelper.setError("dict-error", "[查询数据字典出错]" + e.getMessage());
		}
		return flag;
	}
}
