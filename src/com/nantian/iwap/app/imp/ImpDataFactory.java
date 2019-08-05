package com.nantian.iwap.app.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;

/**
 * 数据导入工厂
 * 
 * @author stormhua
 *
 */
public class ImpDataFactory {
	private static ImpDataFactory impDataFactory = null;
	private static Logger log = Logger.getLogger(ImpDataFactory.class);
	private Map<String, ImportData> impProcess = new HashMap<String, ImportData>();// 导入实现类与类型配置

	@SuppressWarnings("rawtypes")
	private Map config = new HashMap();// 导入配置信息
	private String impSqlStr;// 查询配置信息SQL
	private Object[] args;

	private ImpDataFactory() {
	}

	public synchronized static ImpDataFactory getInstance() {
		if (impDataFactory == null) {
			impDataFactory = new ImpDataFactory();
		}
		return impDataFactory;
	}

	public void init(String impSqlStr, String... process) {
		try {
			Map<String, ImportData> imp = new HashMap<String, ImportData>();
			for (String p : process) {
				String[] array = p.split("_");
				if (array.length == 2) {
					Class<?> onwClass = Class.forName(array[1]);
					imp.put(array[0], (ImportData) onwClass.newInstance());
				}
			}
			ImpDataFactory impDataFactory = getInstance();
			impDataFactory.setImpProcess(imp);
			impDataFactory.setImpSqlStr(impSqlStr);
			impDataFactory.initConfig();
		} catch (Exception e) {
			log.error("初始化导入工厂出错", e);
		}
	}

	public void setImpSqlStr(String impSqlStr, Object... args) {
		this.impSqlStr = impSqlStr;
		this.args = args;
	}

	public void setImpProcess(Map<String, ImportData> imp) {
		this.impProcess = imp;
	}

	/**
	 * 初始化配置信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initConfig() {
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();

			List<Map<String, Object>> impData = null;
			if (args.length > 0) {
				impData = dbBean.queryForList(impSqlStr, args);
			} else {
				impData = dbBean.queryForList(impSqlStr);
			}
			Iterator<Map<String, Object>> it = impData.iterator();
			while (it.hasNext()) {
				Map<String, Object> row = it.next();
				String id = row.get("IMP_ID").toString();
				if (config.containsKey(id)) {
					Map cfg = (Map) config.get(id);
					List<Map<String, String>> dtl = (List<Map<String, String>>) cfg.get("dtl");
					Map cfgDtl = new HashMap();
					cfgDtl.put("fldNm", row.get("FLD_NM"));
					cfgDtl.put("pkFlg", row.get("PK_FLG"));
					cfgDtl.put("sortVal", row.get("SORT_VAL"));
					cfgDtl.put("dataTp", row.get("DATA_TP"));
					cfgDtl.put("defVal", row.get("DEF_VAL"));
					cfgDtl.put("defValTp", row.get("DEF_VAL_TP"));
					cfgDtl.put("allowBlank", row.get("ALLOW_ROW"));
					dtl.add(cfgDtl);
				} else {
					Map cfg = new HashMap();
					List<Map<String, String>> dtl = new ArrayList<Map<String, String>>();
					cfg.put("tblNm", row.get("TBL_EN_NM"));
					cfg.put("impTp", row.get("IMP_TP"));
					cfg.put("startIdx", row.get("START_PT"));
					cfg.put("sheetIdx", row.get("SHEET_IDX"));
					cfg.put("startCol", row.get("START_COL"));
					cfg.put("allowRow", row.get("ALLOW_ROW"));
					cfg.put("beforeProc", row.get("BEFORE_PROC"));
					cfg.put("afterProc", row.get("AFTER_PROC"));
					Map cfgDtl = new HashMap();
					cfgDtl.put("fldNm", row.get("FLD_NM"));
					cfgDtl.put("pkFlg", row.get("PK_FLG"));
					cfgDtl.put("sortVal", row.get("SORT_VAL"));
					cfgDtl.put("dataTp", row.get("DATA_TP"));
					cfgDtl.put("defVal", row.get("DEF_VAL"));
					cfgDtl.put("defValTp", row.get("DEF_VAL_TP"));
					cfgDtl.put("allowBlank", row.get("ALLOW_ROW"));
					dtl.add(cfgDtl);
					cfg.put("dtl", dtl);
					config.put(id, cfg);
				}
			}
			Iterator proc = impProcess.keySet().iterator();
			while (proc.hasNext()) {
				Object key = proc.next();
				ImportData impProc = impProcess.get(key);
				impProc.setConfig(config);
			}
		} catch (Exception e) {
			log.error("初始化配置信息出错", e);
		} finally {
			DBAccessPool.releaseDbBean();
		}

	}

	/**
	 * 数据导入调用方法
	 * 
	 * @param fileNm
	 *            文件名称
	 * @param impId
	 *            导入配置标识
	 * @param isOverWrite
	 *            是否允许覆盖
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map impData(String fileNm, String impId, boolean isOverWrite, String modelcode) {
		Map rst = new HashMap();
		String extNm = fileNm.substring(fileNm.lastIndexOf(".") + 1);
		ImportData impProc = impProcess.get(extNm);
		if (impProc != null) {
			rst = impProc.importData(fileNm, impId, isOverWrite, modelcode);
		} else {
			rst.put("msg", "无对应导入处理");
		}
		return rst;
	}

//自定义获取

	public Map getConfig() {
        try {
             Map config = new HashMap();
            DBAccessPool.createDbBean();
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            List<Map<String, Object>> impData = null;
            Iterator<Map<String, Object>> it = impData.iterator();
            while (it.hasNext()) {
                Map<String, Object> row = it.next();
                String id = row.get("IMP_ID").toString();
                if (config.containsKey(id)) {
                    Map cfg = (Map) config.get(id);
                    List<Map<String, String>> dtl = (List<Map<String, String>>) cfg.get("dtl");
                    Map cfgDtl = new HashMap();
                    cfgDtl.put("fldNm", row.get("FLD_NM"));
                    cfgDtl.put("pkFlg", row.get("PK_FLG"));
                    cfgDtl.put("sortVal", row.get("SORT_VAL"));
                    cfgDtl.put("dataTp", row.get("DATA_TP"));
                    cfgDtl.put("defVal", row.get("DEF_VAL"));
                    cfgDtl.put("defValTp", row.get("DEF_VAL_TP"));
                    cfgDtl.put("allowBlank", row.get("ALLOW_ROW"));
                    dtl.add(cfgDtl);
                } else {
                    Map cfg = new HashMap();
                    List<Map<String, String>> dtl = new ArrayList<Map<String, String>>();
                    cfg.put("tblNm", row.get("TBL_EN_NM"));
                    cfg.put("impTp", row.get("IMP_TP"));
                    cfg.put("startIdx", row.get("START_PT"));
                    cfg.put("sheetIdx", row.get("SHEET_IDX"));
                    cfg.put("startCol", row.get("START_COL"));
                    cfg.put("allowRow", row.get("ALLOW_ROW"));
                    cfg.put("beforeProc", row.get("BEFORE_PROC"));
                    cfg.put("afterProc", row.get("AFTER_PROC"));
                    Map cfgDtl = new HashMap();
                    cfgDtl.put("fldNm", row.get("FLD_NM"));
                    cfgDtl.put("pkFlg", row.get("PK_FLG"));
                    cfgDtl.put("sortVal", row.get("SORT_VAL"));
                    cfgDtl.put("dataTp", row.get("DATA_TP"));
                    cfgDtl.put("defVal", row.get("DEF_VAL"));
                    cfgDtl.put("defValTp", row.get("DEF_VAL_TP"));
                    cfgDtl.put("allowBlank", row.get("ALLOW_ROW"));
                    dtl.add(cfgDtl);
                    cfg.put("dtl", dtl);
                    config.put(id, cfg);
                }
            }
            Iterator proc = impProcess.keySet().iterator();
            while (proc.hasNext()) {
                Object key = proc.next();
                ImportData impProc = impProcess.get(key);
                impProc.setConfig(config);
            }
        } catch (Exception e) {
            log.error("初始化配置信息出错", e);
        } finally {
            DBAccessPool.releaseDbBean();
        }
        return config;

    }

}
