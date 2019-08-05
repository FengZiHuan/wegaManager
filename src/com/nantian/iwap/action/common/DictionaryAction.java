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
 * FileName:DictionaryAction.java<br>
 * Description:解析多个数据字典名<br>
 * History:<br>
 * Date       Author       Desc<br>
 * -------------------------------------------------------<br>
 * 2015-6-9    chun
 */
public class DictionaryAction extends BaseBizAction{
	private static Logger logger = Logger.getLogger(DictionaryAction.class);

	@Override
	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		String dictNm = dtbHelper.getStringValue("dictNm");
		String[] dictStr = dictNm.split(",");
		
		for(String dict: dictStr){
			logger.info("获取数据字典："+ dict +"信息");
			Map mapName = DataMapDictionary.getInstance().getDataMap(dict);
			JSONArray jsonArray = new JSONArray();//[]
			Set keys = mapName.keySet();
			Iterator it = keys.iterator();
			while(it.hasNext()){
				JSONObject jsonObject = new JSONObject();//{}
				String key = it.next().toString();
				String value = mapName.get(key).toString();
				jsonObject.put("id", key);
				jsonObject.put("name", value);
				jsonArray.add(jsonObject);
				dtbHelper.getDataTransferBus().getResultContext().addData(dict, jsonArray.toArray());
			}
			logger.info("字典："+ dict + "定义信息:" + mapName);
		}
		return 1;
	}
}
