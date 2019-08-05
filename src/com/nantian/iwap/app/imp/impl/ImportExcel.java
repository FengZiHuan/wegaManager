package com.nantian.iwap.app.imp.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nantian.iwap.action.wagechanges.OverTimeMgAction;
import com.nantian.iwap.app.imp.*;
import com.nantian.iwap.databus.DTBHelper;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;

/**
 * 实现Excel依照配置文件导入
 *
 * @author stormhua modify by stormhua 20140604 增加是否允许当前行数据处理
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class ImportExcel implements ImportData {
    private Map config;
    private static Logger log = Logger.getLogger(ImportExcel.class);

    public void setConfig(Map config) {
        this.config = config;
    }

    /**
     * 构建更新SQL语句
     *
     * @param importId
     * @return
     */
    private String buildUpdateSql(String importId) {
        StringBuffer sql = new StringBuffer(200);
        List pk = getPkField(importId);
        List cfgDtl = (List) ((Map) config.get(importId)).get("dtl");
        String table = getTable(importId);
        sql.append("update ").append(table).append(" set ");
        for (int i = 0; i < cfgDtl.size(); i++) {
            Map row = (Map) cfgDtl.get(i);
            if (!pk.contains(row.get("fldNm").toString())) {
                sql.append(row.get("fldNm")).append("=?,");
            }
        }
        sql.delete(sql.length() - 1, sql.length()).append(" where ");
        for (int j = 0; j < pk.size(); j++) {
            sql.append(pk.get(j)).append("=?");
            if (j >= 0 && (j + 1) < pk.size()) {
                sql.append(" and ");
            }
        }
        return sql.toString();
    }

    private String buildInsertSql1(String importId) {
        String sql = "insert into NH_BRANDSTAFF (st_brand,st_project,st_times,st_fmis,st_name,st_post,st_remoneys,st_satus,st_status) VALUES(?,?,?,?,?,?,?,'0','0')";
        return sql;
    }


    /**
     * 构建插入SQL语句
     *
     * @param importId
     * @return
     */
    private String buildInsertSql(String importId) {
        StringBuffer sql = new StringBuffer(200);
        StringBuffer param = new StringBuffer();
        List cfgDtl = (List) ((Map) config.get(importId)).get("dtl");
        String table = getTable(importId);
        sql.append("insert into ").append(table).append("(");
        for (int i = 0; i < cfgDtl.size(); i++) {
            Map row = (Map) cfgDtl.get(i);
            sql.append(row.get("fldNm")).append(",");
//			param.append("?,");
            if ("acct_crt_tm".equals(row.get("fldNm"))) {
                param.append("to_date(?,'yyyy-MM-dd HH24:mi:ss'),");
            } else if ("BEGIN_DATE".equals(row.get("fldNm")) || "END_DATE".equals(row.get("fldNm"))) {
                param.append("to_date(?,'yyyy-MM-dd'),");
            } else {
                param.append("?,");
            }
        }
        sql.delete(sql.length() - 1, sql.length()).append(") values(");
        sql.append(param.delete(param.length() - 1, param.length()).toString()).append(")");
        return sql.toString();
    }


    /**
     * 获取配置信息对应表
     *
     * @param importId
     * @return
     */
    private String getTable(String importId) {
        return (String) ((Map) config.get(importId)).get("tblNm");
    }

    /**
     * 判断记录是否存在
     *
     * @param dbBean
     * @param importId
     * @param row
     * @return
     */
    private boolean exitsRecord(DBAccessBean dbBean, String importId, Map row) {
        StringBuffer sql = new StringBuffer(200);
        String table = getTable(importId);
        List param = new ArrayList();
        sql.append("select count(*) from ").append(table);
        sql.append(" where ");
        List pk = getPkField(importId);
        for (int j = 0; j < pk.size(); j++) {
            sql.append(pk.get(j)).append("=?");
            if ((j + 1) < pk.size()) {// j>0&&去掉此判断条件 2014-05-06 by liyuan
                sql.append(" and ");
            }
            param.add(row.get(pk.get(j)));
        }
        return dbBean.queryForInt(sql.toString(), param.toArray()) > 0;
    }

    /**
     * 获取导入配置主键
     *
     * @param importId
     * @return
     */
    private List getPkField(String importId) {
        List pkList = new ArrayList();
        List cfgDtl = (List) ((Map) config.get(importId)).get("dtl");
        for (int i = 0; i < cfgDtl.size(); i++) {
            Map row = (Map) cfgDtl.get(i);
            if ("0".equals(row.get("pkFlg"))) {
                pkList.add(row.get("fldNm"));
            }
        }
        return pkList;
    }

    /**
     * 检查配置信息
     *
     * @param importId
     * @param rst
     */
    private void checkConfig(String importId, Map rst) {
        Map cfg = (Map) config.get(importId);
        if (cfg.get("impTp") == null) {
            rst.put("flag", false);
            rst.put("msg", "配置信息错误,未设置导入类型\r\n");
        }
    }

    /**
     * 导入Excel2007之前版本
     *
     * @param fileName
     * @param importId
     * @return
     */
    private Map xlsToDb(String fileName, String importId, boolean isOverWrite, String modelcode) {
        String table = getTable(importId);
        StringBuffer msg = new StringBuffer(200);
        Map rst = new HashMap();
        log.info("开始导入Excel" + fileName + " table=" + table + " isOverWrite=" + isOverWrite);
        rst.put("flag", true);
        // String tmpNm=fileName.substring(fileName.lastIndexOf("/")+1);
        String sepatator = System.getProperties().getProperty("file.separator");
        String tmpNm = fileName.substring(fileName.lastIndexOf(sepatator) + 1);
        tmpNm = tmpNm.substring(0, tmpNm.lastIndexOf("."));
        Map cfg = (Map) config.get(importId);
        checkConfig(importId, rst);
        if (rst.get("flag").equals("false")) {
            return rst;
        }
        String impTp = (String) cfg.get("impTp");
        int startIdx = 0;
        int startCol = 0;
        int sheetIdx = 0;
        if (cfg.get("startIdx") != null) {
            //startIdx = (Integer) cfg.get("startIdx");
            startIdx = Integer.parseInt((String) cfg.get("startIdx"));
        }
        if (cfg.get("startCol") != null) {
            //startCol = (Integer) cfg.get("startCol");
            String startColStr = String.valueOf(cfg.get("startCol"));
            startCol = Integer.parseInt(startColStr);
        }
        if (cfg.get("sheetIdx") != null) {
            //sheetIdx = (Integer) cfg.get("sheetIdx");
            String sheetIdxStr = String.valueOf(cfg.get("sheetIdx"));
            sheetIdx = Integer.parseInt(sheetIdxStr);
        }
        try {
            DBAccessPool.createDbBean();
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileName));
            Sheet sheet = workbook.getSheetAt(sheetIdx);
            if (modelcode == "1") {
                rst = this.impData1(dbBean, impTp, sheet, startIdx, startCol, importId, isOverWrite, modelcode);
            } else {
                rst = this.impData(dbBean, impTp, sheet, startIdx, startCol, importId, isOverWrite, modelcode);
            }
        } catch (FileNotFoundException e) {
            log.warn("导入Excel", e);
            msg.append("文件未发现\r\n");
        } catch (IOException e) {
            log.warn("导入ExcelIo异常", e);
            msg.append("读文件异常\r\n");
        } catch (InstantiationException e) {
            log.warn("服务器异常", e);
            msg.append("服务器异常\r\n");
        } catch (IllegalAccessException e) {
            log.warn("访问异常", e);
            msg.append("访问异常\r\n");
        } catch (ClassNotFoundException e) {
            log.warn("类加载失败", e);
            msg.append("类找不到\r\n");
        } finally {
            DBAccessPool.releaseDbBean();
        }
        rst.put("msg", msg);
        return rst;
    }

    /**
     * 导入2007之后的版本
     *
     * @param fileName
     * @param importId
     * @return
     */
    @SuppressWarnings("deprecation")
    private Map xlsxToDb(String fileName, String importId, boolean isOverWrite, String modelcode) {
        String table = getTable(importId);
        StringBuffer msg = new StringBuffer(200);
        Map rst = new HashMap();
        log.info("开始导入Excel" + fileName + " table=" + table + " isOverWrite=" + isOverWrite);
        rst.put("flag", true);
        Map cfg = (Map) config.get(importId);
        checkConfig(importId, rst);
        String sepatator = System.getProperties().getProperty("file.separator");
        String tmpNm = fileName.substring(fileName.lastIndexOf(sepatator) + 1);
        tmpNm = tmpNm.substring(0, tmpNm.lastIndexOf("."));
        if (rst.get("flag").equals("false")) {
            return rst;// ${fileNm} //${sysdate}
        }
        String impTp = (String) cfg.get("impTp");
        int startIdx = 0;
        int startCol = 0;
        int sheetIdx = 0;
        if (cfg.get("startIdx") != null) {
            //startIdx = (Integer) cfg.get("startIdx");
            startIdx = Integer.parseInt((String) cfg.get("startIdx"));
        }
        if (cfg.get("startCol") != null) {
            //startCol = (Integer) cfg.get("startCol");
            String startColStr = String.valueOf(cfg.get("startCol"));
            startCol = Integer.parseInt(startColStr);
        }
        if (cfg.get("sheetIdx") != null) {
            //sheetIdx = (Integer) cfg.get("sheetIdx");
            String sheetIdxStr = String.valueOf(cfg.get("sheetIdx"));
            sheetIdx = Integer.parseInt(sheetIdxStr);
        }
        try {
            DBAccessPool.createDbBean();
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            XSSFWorkbook workbook = new XSSFWorkbook(fileName);
            Sheet sheet = workbook.getSheetAt(sheetIdx);
            if (modelcode == "1") {
                rst = this.impData1(dbBean, impTp, sheet, startIdx, startCol, importId, isOverWrite, modelcode);
            } else if (modelcode.equals("overtime")) {
                rst = this.impDataOverTime(dbBean, impTp, sheet, startIdx, startCol, importId, isOverWrite, modelcode);
            } else if (modelcode.equals("govertime")) {
                rst = this.impDataGoverTime(dbBean, impTp, sheet, startIdx, startCol, importId, isOverWrite, modelcode);
            }
            else if (modelcode.equals("managerCheckPerformance")) {
                rst = this.impDataGjx(dbBean, impTp, sheet, startIdx, startCol, importId, isOverWrite, modelcode);
            }

            else {
                rst = this.impData(dbBean, impTp, sheet, startIdx, startCol, importId, isOverWrite, modelcode);
            }
        } catch (FileNotFoundException e) {
            log.warn("导入Excel", e);
            msg.append("文件未发现\r\n");
        } catch (IOException e) {
            log.warn("导入ExcelIo异常", e);
            msg.append("读文件异常\r\n");
        } catch (InstantiationException e) {
            log.warn("服务器异常", e);
            msg.append("服务器异常\r\n");
        } catch (IllegalAccessException e) {
            log.warn("访问异常", e);
            msg.append("访问异常\r\n");
        } catch (ClassNotFoundException e) {
            log.warn("类加载失败", e);
            msg.append("类找不到\r\n");
        } finally {
            DBAccessPool.releaseDbBean();
        }
        rst.put("msg", msg);
        return rst;
    }


    private Map impData(DBAccessBean dbBean, String impTp, Sheet sheet, int startIdx, int startCol, String importId,
                        boolean isOverWrite, String modelcode) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map rst = new HashMap();
        if (((Map) config.get(importId)).get("beforeProc") != null
                && !"".equals(((Map) config.get(importId)).get("beforeProc").toString().trim())) {
			/*String bProc = (String) ((Map) config.get(importId)).get("beforeProc");
			IBeforeProcess beforeProc = (IBeforeProcess) Class.forName(bProc).newInstance();
			if (!beforeProc.process((Map) config.get(importId), sheet)) {// 处理失败
				log.warn(importId + "导入前处理失败");
				rst.put("msg", "导入前处理失败");
				return rst;
			}*/
        }
        String insertSql = "";
        String modifySql = "";
        if (modelcode.equals("brandStaff")) {
            insertSql = this.buildInsertSql1(importId);

        } else {
            insertSql = this.buildInsertSql(importId);
            modifySql = this.buildUpdateSql(importId);
        }
        final List updateParam = new ArrayList();
        List<List> insertParam = new ArrayList<List>();
        //收集fmisno
        List<String> fmisnoList = new ArrayList<String>();
        //收集LIVE_CITY和WORK_CITY
        Map<String, String> cityMap = new HashMap<String, String>();
        Object allow = ((Map) config.get(importId)).get("allowRow");
        IAllowRow allowRow = null;
        if (allow != null && !"".equals(allow.toString().trim())) {
            try {
                allowRow = (IAllowRow) Class.forName(allow.toString()).newInstance();
            } catch (Exception e) {
                log.warn(importId + "配置[" + allow.toString() + "]出错", e);
                rst.put("msg", "配置[" + allow.toString() + "]出错");
                return rst;
            }
        }
        short bgClr = -1;
        short border = 0;
        if ("0".endsWith(impTp)) {// 列表形式导入
            int rowSum = sheet.getLastRowNum();
            for (; startIdx <= rowSum; startIdx++) {// 循环数据域
                Row row = sheet.getRow(startIdx);
                if (bgClr == -1) {
                    bgClr = row.getCell(startCol).getCellStyle().getFillBackgroundColor();
                    border = row.getCell(startCol).getCellStyle().getBorderBottom();
                } else {// 判断样式和边框是否相等
                    if (row.getCell(startCol).getCellStyle().getFillBackgroundColor() != bgClr
                            || row.getCell(startCol).getCellStyle().getBorderBottom() != border) {
                        break;
                    }
                }
                List cfgDtl = (List) ((Map) config.get(importId)).get("dtl");
                int seq = 0;
                Map value = new HashMap();
                List p = new ArrayList();
                boolean blankCheck = true;
                //

                for (int i = 0; i < cfgDtl.size(); i++) {// 循环字段
                    String val = null;
                    Map fieldCfg = (Map) cfgDtl.get(i);
                    if (fieldCfg.get("defVal") != null && !"".equals(fieldCfg.get("defVal").toString())) {// 设置默认值
                        String settingDefVal = (String) fieldCfg.get("defVal");
                        if ("1".equals(fieldCfg.get("defValTp"))) {
                            Cell cell = row.getCell(startCol + seq);
                            if (cell == null) {
                                val = null;
                            } else {

                                val = getCellValue(cell, sheet.getWorkbook());

                            }
                            value.put(fieldCfg.get("fldNm"), val);
                            DefaultValue defValGen = (DefaultValue) Class.forName(settingDefVal).newInstance();
                            val = defValGen.generateValue(value, (String) fieldCfg.get("fldNm"));
                            seq++;
                        } else {
                            SystemVariable var = SystemVariableImpl.getInstance();
                            val = var.transVariable(settingDefVal);
                        }

                    } else {// 直接从excel中读取
                        Cell cell = row.getCell(startCol + seq);
                        if (cell == null) {
                            val = null;
                        } else {
                            val = getCellValue(cell, sheet.getWorkbook());
                        }
                        if (isMergedRegion(sheet, startIdx, startCol + seq)) {
                            int num = isMergedRegionColumn(sheet, startIdx, startCol + seq);
                            for (int j = 0; j < num; j++) {
                                seq++;
                            }
                        }
                        seq++;
                    }

                    // 判断当前字段是否主键且非空
                    if ("0".equals(fieldCfg.get("pkFlg"))) {
                        if (StringUtil.isBlank(val)) {
                            blankCheck = false;
                            break;
                        }
                    }
                    // 判断当前字段是否允许非空且合法
                    if ("0".equals(fieldCfg.get("allowBlank"))) {
                        if (StringUtil.isBlank(val)) {
                            blankCheck = false;
                            break;
                        }
                    }
                    value.put(fieldCfg.get("fldNm"), val);
                    //为导入台账做前期收集fmisNo
                    if ("punish".equals(modelcode)) {
                        if ("FMIS_NO".equals(fieldCfg.get("fldNm"))) {
                            fmisnoList.add(val);
                        }
                    }
                    p.add(val);
                }


                // 判断当前行是否通过非空判断
                if (!blankCheck) {
                    continue;
                }

                // 增加当前行是否有效判断
                if (allowRow != null) {
                    if (!allowRow.allow(value)) {
                        continue;
                    }
                }
                boolean exist;
                //绩效重复判断
                if (modelcode.equals("performace")) {
                    exist = this.exitsjx(dbBean, importId, value);
                } else if (modelcode.equals("brandStaff")) {
                    exist = this.exitbrandStaff(dbBean, importId, value);
                } else if (modelcode.equals("brandStaff")) {
                    exist = this.exitsOverTime(dbBean, importId, value);
                } else {
                    exist = this.exitsRecord(dbBean, importId, value);
                }
                if (exist && isOverWrite) {// 如果存在记录并允许覆盖
                    // 目的是为了匹配update时参数与值能对应上 by ly
                    List pk = getPkField(importId);
                    List p_updateParam = new ArrayList();
                    List cfgDtl_param = (List) ((Map) config.get(importId)).get("dtl");
                    for (int i = 0; i < cfgDtl_param.size(); i++) {
                        Map row_update_list = (Map) cfgDtl_param.get(i);
                        if (!pk.contains(row_update_list.get("fldNm").toString())) {
                            p_updateParam.add(value.get(row_update_list.get("fldNm").toString()));
                        }
                    }
                    for (int j = 0; j < pk.size(); j++) {
                        p_updateParam.add(value.get(pk.get(j).toString()));
                    }
                    updateParam.add(p_updateParam);
                } else if (!exist) {// 不存在
                    int sum = 0;
                    List cfgDtl_param = (List) ((Map) config.get(importId)).get("dtl");
                    for (int i = 0; i < cfgDtl_param.size(); i++) {
                        Map row_update_list = (Map) cfgDtl_param.get(i);
                        if (value.get(row_update_list.get("fldNm").toString()) == null
                                || value.get(row_update_list.get("fldNm").toString()).equals("")) {
                            sum++;
                        }
                    }
                    insertParam.add(p);
					/*if (sum < (Float.parseFloat(String.valueOf(cfgDtl_param.size()))) / 2) {
						insertParam.add(p);
					}*/
                } // 需要把重复记录提示的话需要在这里实现
            }
        } else {// 单表形式导入
            List cfgDtl = (List) ((Map) config.get(importId)).get("dtl");
            Map value = new HashMap();
            List p = new ArrayList();
            boolean blankCheck = true;
            for (int i = 0; i < cfgDtl.size(); i++) {// 循环字段
                Map fieldCfg = (Map) cfgDtl.get(i);
                String val = null;
                if (fieldCfg.get("defValTp") != null && !"".equals(fieldCfg.get("defVal").toString())) {// 设置默认值
                    String settingDefVal = (String) fieldCfg.get("defVal");

                    if ("1".equals(fieldCfg.get("defValTp"))) {
                        DefaultValue defValGen = (DefaultValue) Class.forName(settingDefVal).newInstance();
                        val = defValGen.generateValue(value, (String) fieldCfg.get("fldNm"));
                    } else {
                        SystemVariable var = SystemVariableImpl.getInstance();
                        val = var.transVariable(settingDefVal);
                    }
                } else {// 直接从excel中读取
                    if (fieldCfg.get("defVal") != null) {
                        String setVal = (String) fieldCfg.get("defVal");// getCustomID;Cont_cust_id
                        int r = Integer.parseInt(setVal.substring(1)) - 1;
                        setVal = setVal.toUpperCase();
                        Row row = sheet.getRow(r);
                        int col = (int) setVal.charAt(0) - 65;
                        val = getCellValue(row.getCell(col), sheet.getWorkbook());
                    } else {
                        val = "";
                    }
                }
                // 判断当前字段是否主键且非空
                if ("0".equals(fieldCfg.get("pkFlg"))) {
                    if (StringUtil.isBlank(val)) {
                        blankCheck = false;
                        break;
                    }
                }
                // 判断当前字段是否允许非空且合法
                if ("0".equals(fieldCfg.get("allowBlank"))) {
                    if (StringUtil.isBlank(val)) {
                        blankCheck = false;
                        break;
                    }
                }
                value.put(fieldCfg.get("fldNm"), val);
                p.add(val);

            }

            // 判断当前行是否通过非空判断
            if (!blankCheck) {
                rst.put("msg", "导入数据未能通过非空校验");
                return rst;
            }

            if (allowRow != null) {// 需要对记录进行检查
                if (!allowRow.allow(value)) {// 当前记录需要舍去
                    rst.put("msg", "导入数据未能通过校验");
                    return rst;
                }
            }
            boolean exist = this.exitsRecord(dbBean, importId, value);
            if (exist && isOverWrite) {// 如果存在记录并允许覆盖
                // 目的是为了匹配update时参数与值能对应上 by ly
                List pk = getPkField(importId);
                List p_updateParam = new ArrayList();
                List cfgDtl_param = (List) ((Map) config.get(importId)).get("dtl");
                for (int i = 0; i < cfgDtl_param.size(); i++) {
                    Map row_update_one = (Map) cfgDtl_param.get(i);
                    if (!pk.contains(row_update_one.get("fldNm").toString())) {
                        p_updateParam.add(value.get(row_update_one.get("fldNm").toString()));
                    }
                }
                for (int j = 0; j < pk.size(); j++) {
                    p_updateParam.add(value.get(pk.get(j).toString()));
                }
                updateParam.add(p_updateParam);
            } else if (!exist) {// 不存在
                //// 添加row中，主键值都为空的情况下不给插入数据
                insertParam.add(p);
            } // 需要把重复记录提示的话需要在这里实现
        }

        // 在覆盖情况下如果新增和修改条数为0，则提示错误 by lihl 2014-05-28 15:21:14
        if (insertParam.size() + updateParam.size() == 0) {
            throw new RuntimeException("插入参数为空");
        }
        int succCnt = 0;
        int upCnt = 0;
        if (!"exchangesubsidy".equals(modelcode)) {
            if (insertParam.size() > 0) {
                System.out.println(insertSql);
                System.out.println(insertParam);
                succCnt += dbBean.batchUpdate(insertSql, insertParam);
            }

            if (updateParam.size() > 0) {
                System.out.println(modifySql);
                System.out.println(updateParam);
                upCnt += dbBean.batchUpdate(modifySql, updateParam);
            }
        }

        //通过页面传递的modelcode做相应的逻辑操作
        if (!StringUtil.isBlank(modelcode)) {
            int scount = 0;
            if ("punish".equals(modelcode)) { //通过导入的数据给punishinfo表添加数据
                scount = ImportExcelByModule.insertDataByPunishmentAccount(dbBean, fmisnoList);
            }
            //为导入交流补贴前期收集居住城市和工作城市
            if ("exchangesubsidy".equals(modelcode)) {
                if (insertParam != null && insertParam.size() > 0) {
                    for (int i = 0; i < insertParam.size(); i++) {
                        List pList = new ArrayList();
                        pList.add(insertParam.get(0).get(0));
                        pList.add(insertParam.get(0).get(1));
                        pList.add(insertParam.get(0).get(2));
                        pList.add(insertParam.get(0).get(3));
                        Object live = insertParam.get(0).get(2);
                        Object work = insertParam.get(0).get(3);
                        if (live != null && work != null) {
                            List<Map<String, Object>> dataList = null;
                            String sqlStr = "select live_city,work_city,distance,(select s.subsidy_amount from subsidy_standard s "
                                    + " where s.start_distance<c.distance and s.end_distance>c.distance) as subsidy_amount from city_distance c where 1=1 "
                                    + " and LIVE_CITY ='" + live.toString() + "'"
                                    + " and WORK_CITY ='" + work.toString() + "'";
                            dataList = dbBean.queryForList(sqlStr);
                            if (dataList != null && dataList.size() > 0) {
                                Object distance = dataList.get(0).get("DISTANCE");
                                Object subsidyAmount = dataList.get(0).get("SUBSIDY_AMOUNT");
                                if (distance != null && subsidyAmount != null) {
                                    int lucheng = Integer.parseInt(distance.toString());
                                    int sa = Integer.parseInt(subsidyAmount.toString());
                                    pList.add(lucheng);
                                    pList.add(sa);
                                }
                            }

                        }
                        pList.add(insertParam.get(0).get(6));
                        pList.add(insertParam.get(0).get(7));
                        insertParam.remove(0);
                        insertParam.add(pList);
                    }
                }
                System.out.println("insertParam::::" + insertParam);
                if (insertParam.size() > 0) {
                    scount = dbBean.batchUpdate(insertSql, insertParam);

                }
            }
            rst.put("info", "成功导入数据");
        } else {
            if (upCnt > 0) {
                rst.put("info", "成功导入数据" + succCnt + "条,成功修改数据" + upCnt + "条");
            } else {
                rst.put("info", "成功导入数据");
            }

            log.info(importId + "成功导入数据");
        }
        return rst;
    }

    private String getCellValue(Cell cell, Workbook workbook) {
        String rst = null;
        Date dateRst = null;// 日期格式取值
        int type = cell.getCellType();
        switch (type) {
            case Cell.CELL_TYPE_STRING:
                rst = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                short format = cell.getCellStyle().getDataFormat();
                SimpleDateFormat sdf = null;
                if (format == 14 || format == 31 || format == 57 || format == 58) {
                    //日期
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    rst = sdf.format(date);
                } else if (format == 20 || format == 32) {
                    //时间
                    sdf = new SimpleDateFormat("HH:mm");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    rst = sdf.format(date);
                } else {// 非日期格式
                    Double d = cell.getNumericCellValue();
                    if (d.intValue() == d.doubleValue()) {
                        rst = String.valueOf(d.intValue());
                    } else {
                        rst = d.toString();
                    }
                }
			/*if (HSSFDateUtil.isCellDateFormatted(cell)) {// 若是日期格式，则先获取日期再转换成字符串
				try {
					dateRst = cell.getDateCellValue();
					rst = new SimpleDateFormat("yyyy-MM-dd").format(dateRst);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {// 非日期格式
				Double d = cell.getNumericCellValue();
				if (d.intValue() == d.doubleValue()) {
					rst = String.valueOf(d.intValue());
				} else {
					rst = d.toString();
				}
			}*/
                break;
            case Cell.CELL_TYPE_FORMULA:
                if (cell instanceof HSSFCell) {
                    HSSFFormulaEvaluator f = new HSSFFormulaEvaluator((HSSFWorkbook) workbook);
                    try {
                        rst = f.evaluate(cell).formatAsString();
                    } catch (Exception e) {
                        try {
                            rst = String.valueOf(cell.getNumericCellValue());
                        } catch (IllegalStateException e1) {
                            rst = String.valueOf(cell.getRichStringCellValue());
                        }
                    }
                } else {
                    try {
                        Double d = cell.getNumericCellValue();
                        if (d.intValue() == d.doubleValue()) {
                            rst = String.valueOf(d.intValue());
                        } else {
                            rst = d.toString();
                        }
                    } catch (Exception e) {
                        rst = String.valueOf(cell.getRichStringCellValue());
                    }
                }
                break;
            default:
                rst = "";
        }
        return rst;
    }

    /*
     * (non-Javadoc)
     *
     * @see load.ImportData#importData(java.lang.String, java.lang.String,
     * boolean)
     */
    public Map importData(String fileName, String importId, boolean isOverWrite, String modelcode) {
        if (fileName.endsWith("xls")) {
            return xlsToDb(fileName, importId, isOverWrite, modelcode);
        } else {
            return xlsxToDb(fileName, importId, isOverWrite, modelcode);
        }
    }

    /**
     * 判断是否有单元格合并
     *
     * @param sheet
     * @param
     * @param column
     * @return
     */
    public boolean isMergedRegion(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {

                    return true;
                }
            }
        }

        return false;
    }

    public int isMergedRegionColumn(Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {

                    return lastColumn - firstColumn;
                }
            }
        }

        return 0;
    }


    //导入之前处理
    //请假模块判断情况
    private boolean exitsRecord1(DBAccessBean dbBean, String importId, Map row) {

        String table = getTable(importId);
        List pk = getPkField(importId);
        if (row.get("nh_fmis") == null) {
            return false;
        }
        String nh_fmis = row.get("nh_fmis").toString();
        if (row.get("nh_xiujiayear") == null) {
            return false;
        }
        String nh_xiujiayear = row.get("nh_xiujiayear").toString();
        if (row.get("nh_qingtype") == null) {
            return false;
        }
        String nh_qingtype = row.get("nh_qingtype").toString();

        String sql = "select count(*) from NH_HOLIDAY WHERE NH_FMIS= ? AND NH_QINGTYPE=? AND NH_XIUJIAYEAR=?";
        return dbBean.queryForInt(sql, nh_fmis, nh_qingtype, nh_xiujiayear) > 0;


    }


    //加班导入判断重复
    private boolean exitsOverTime(DBAccessBean dbBean, String importId, Map row) {
        String table = getTable(importId);
        List pk = getPkField(importId);
        if (row.get("o_idcard") == null) {
            return false;
        }
        String o_idcard = row.get("o_idcard").toString();
        if (row.get("o_reason") == null) {
            return false;
        }
        String o_reason = row.get("o_reason").toString();
        if (row.get("o_otimes") == null) {
            return false;
        }
        String o_otimes = row.get("o_otimes").toString();

        String sql = "select count(*) from NH_OVERTIME  WHERE o_idcard= ? AND o_reason=? AND o_otimes=?";
        return dbBean.queryForInt(sql, o_idcard, o_reason, o_otimes) > 0;


    }

    //导入之前处理
    //绩效导入判断情况
    private boolean exitsjx(DBAccessBean dbBean, String importId, Map row) {

        String table = getTable(importId);
        List pk = getPkField(importId);
        if (row.get("jx_year") == null) {
            return false;
        }
        String jx_year = row.get("jx_year").toString();
        if (row.get("jx_brand") == null) {
            return false;
        }
        String jx_brand = row.get("jx_brand").toString();

        String sql = "select count(*) from nh_jxjj WHERE jx_brand= ? AND jx_year=? ";
        return dbBean.queryForInt(sql, jx_brand, jx_year) > 0;


    }

    //判断分行导入绩效
    private boolean exitbrandStaff(DBAccessBean dbBean, String importId, Map row) {

        String table = getTable(importId);
        List pk = getPkField(importId);
        if (row.get("st_fmis") == null) {
            return false;
        }
        String st_fmis = row.get("st_fmis").toString();
        if (row.get("st_times") == null) {
            return false;
        }
        String st_times = row.get("st_times").toString();
        if (row.get("st_brand") == null) {
            return false;
        }
        String st_brand = row.get("st_brand").toString();

        String sql = "select count(*) from NH_BRANDSTAFF  WHERE st_brand= ? AND st_times=? and st_fmis=?  ";
        return dbBean.queryForInt(sql, st_brand, st_times, st_fmis) > 0;
    }

    private Map impData1(DBAccessBean dbBean, String impTp, Sheet sheet, int startIdx, int startCol, String importId,
                         boolean isOverWrite, String str) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map rst = new HashMap();
        if (((Map) config.get(importId)).get("beforeProc") != null
                && !"".equals(((Map) config.get(importId)).get("beforeProc").toString().trim())) {
            String bProc = (String) ((Map) config.get(importId)).get("beforeProc");
            IBeforeProcess beforeProc = (IBeforeProcess) Class.forName(bProc).newInstance();
            if (!beforeProc.process((Map) config.get(importId), sheet)) {// 处理失败
                log.warn(importId + "导入前处理失败");
                rst.put("msg", "导入前处理失败");
                return rst;
            }
        }
        String insertSql = this.buildInsertSql(importId);
        String modifySql = this.buildUpdateSql(importId);
        final List updateParam = new ArrayList();
        final List insertParam = new ArrayList();
        Object allow = ((Map) config.get(importId)).get("allowRow");
        IAllowRow allowRow = null;
        if (allow != null && !"".equals(allow.toString().trim())) {
            try {
                allowRow = (IAllowRow) Class.forName(allow.toString()).newInstance();
            } catch (Exception e) {
                log.warn(importId + "配置[" + allow.toString() + "]出错", e);
                rst.put("msg", "配置[" + allow.toString() + "]出错");
                return rst;
            }
        }
        short bgClr = -1;
        short border = 0;
        if ("0".endsWith(impTp)) {// 列表形式导入
            int rowSum = sheet.getLastRowNum();
            for (; startIdx <= rowSum; startIdx++) {// 循环数据域
                Row row = sheet.getRow(startIdx);
                if (bgClr == -1) {
                    bgClr = row.getCell(startCol).getCellStyle().getFillBackgroundColor();
                    border = row.getCell(startCol).getCellStyle().getBorderBottom();
                } else {// 判断样式和边框是否相等
                    if (row.getCell(startCol).getCellStyle().getFillBackgroundColor() != bgClr
                            || row.getCell(startCol).getCellStyle().getBorderBottom() != border) {
                        break;
                    }
                }
                List cfgDtl = (List) ((Map) config.get(importId)).get("dtl");
                int seq = 0;
                Map value = new HashMap();
                List p = new ArrayList();
                boolean blankCheck = true;
                for (int i = 0; i < cfgDtl.size(); i++) {// 循环字段
                    Map fieldCfg = (Map) cfgDtl.get(i);
                    String val = null;
                    if (fieldCfg.get("defVal") != null && !"".equals(fieldCfg.get("defVal").toString())) {// 设置默认值
                        String settingDefVal = (String) fieldCfg.get("defVal");
                        if ("1".equals(fieldCfg.get("defValTp"))) {
                            Cell cell = row.getCell(startCol + seq);
                            if (cell == null) {
                                val = null;
                            } else {
                                val = getCellValue(cell, sheet.getWorkbook());
                            }
                            value.put(fieldCfg.get("fldNm"), val);
                            DefaultValue defValGen = (DefaultValue) Class.forName(settingDefVal).newInstance();
                            val = defValGen.generateValue(value, (String) fieldCfg.get("fldNm"));
                            seq++;
                        } else {
                            SystemVariable var = SystemVariableImpl.getInstance();
                            val = var.transVariable(settingDefVal);
                        }

                    } else {// 直接从excel中读取
                        Cell cell = row.getCell(startCol + seq);
                        if (cell == null) {
                            val = null;
                        } else {
                            val = getCellValue(cell, sheet.getWorkbook());
                        }
                        if (isMergedRegion(sheet, startIdx, startCol + seq)) {
                            seq++;
                        }
                        seq++;
                    }

                    // 判断当前字段是否主键且非空
                    if ("0".equals(fieldCfg.get("pkFlg"))) {
                        if (StringUtil.isBlank(val)) {
                            blankCheck = false;
                            break;
                        }
                    }
                    // 判断当前字段是否允许非空且合法
                    if ("0".equals(fieldCfg.get("allowBlank"))) {
                        if (StringUtil.isBlank(val)) {
                            blankCheck = false;
                            break;
                        }
                    }
                    value.put(fieldCfg.get("fldNm"), val);
                    p.add(val);
                }

                // 判断当前行是否通过非空判断
                if (!blankCheck) {
                    continue;
                }

                // 增加当前行是否有效判断
                if (allowRow != null) {
                    if (!allowRow.allow(value)) {
                        continue;
                    }
                }

                boolean exist = this.exitsRecord1(dbBean, importId, value);
                isOverWrite = true;
                //满足条件覆盖

                if (exist && isOverWrite) {// 如果存在记录并允许覆盖
                    // 目的是为了匹配update时参数与值能对应上 by ly

                    String nh_fmis = value.get("nh_fmis").toString();
                    String nh_qingtype = value.get("nh_qingtype").toString();
                    String nh_year = value.get("nh_xiujiayear").toString();
                    String nh_times;
                    String nh_sumdays;
                    String nh_no = "";
                    if (value.get("nh_times") != null) {
                        nh_times = value.get("nh_times").toString();
                    } else {
                        nh_times = "不存在的，真是的";
                    }
                    if (value.get("nh_sumdays") != null) {
                        nh_sumdays = value.get("nh_sumdays").toString();
                    } else {
                        nh_sumdays = "0";
                    }
                    String sql1 = "select * from NH_HOLIDAY " +
                            "WHERE NH_FMIS= ? AND NH_QINGTYPE= ? AND NH_XIUJIAYEAR=?";

                    List<Map<String, Object>> maps = dbBean.queryForList(sql1, nh_fmis, nh_qingtype, nh_year);

                    for (Map<String, Object> map : maps) {
                        nh_no = map.get("NH_NO").toString();
                        if (map.get("NH_TIMES") != null) {
                            String[] nh_times1 = map.get("NH_TIMES").toString().split(",");
                            List<String> list = new ArrayList<>();
                            for (String s : nh_times1) {
                                list.add(s);
                            }
                            if (list.contains(nh_times)) {
                                System.out.println("不需要合并");
                            } else {
                                Double sum = Double.parseDouble(nh_sumdays);
                                Double sum1;
                                if (map.get("NH_SUMDAYS") != null) {
                                    String sumdays = map.get("NH_SUMDAYS").toString();
                                    sum1 = Double.parseDouble(sumdays);
                                } else {
                                    sum1 = 0.0;

                                }
                                sum += sum1;
                                nh_sumdays = sum + "";
                                //时间
                                String times = map.get("NH_TIMES").toString();
                                nh_times = times + "," + nh_times;
                            }
                        } else {
                            Double sum = Double.parseDouble(nh_sumdays);
                            Double sum1;
                            if (map.get("NH_SUMDAYS") != null) {
                                String sumdays = map.get("NH_SUMDAYS").toString();
                                sum1 = Double.parseDouble(sumdays);
                            } else {
                                sum1 = 0.0;
                            }
                            sum += sum1;
                            nh_sumdays = sum + "";
                        }
                        String sqlStr2 = "update nh_holiday set NH_TIMES=?,NH_SUMDAYS=? where NH_NO=?";
                        dbBean.executeUpdate(sqlStr2, nh_times, nh_sumdays, nh_no);
                        dbBean.executeCommit();
                        rst.put("info", "成功导入数据");
                        log.info(importId + "成功导入数据");

                    }

                } else if (!exist) {
                    dbBean = DBAccessPool.getDbBean();
                    dbBean.setAutoCommit(false);
                    String nh_fmis = value.get("nh_fmis").toString();
                    String nh_cardid = value.get("nh_cardid").toString();
                    String nh_year = value.get("nh_xiujiayear").toString();
                    String nh_sumdays = value.get("nh_sumdays").toString();
                    String nh_times = value.get("nh_times").toString();
                    String nh_name = value.get("nh_name").toString();
                    String nh_dept = value.get("nh_dept").toString();
                    String nh_qingtype = value.get("nh_qingtype").toString();
                    String nh_beizhu = value.get("nh_beizhu").toString();
                    String nh_zgwgz = null;
                    String nh_zjxgz = null;
                    String nh_zjtbt = null;
                    String seSql = "select W_GWGZ,W_JX,W_JBU from NH_DATASOURSE where w_fmis=?";
                    List<Map<String, Object>> maps = dbBean.queryForList(seSql, nh_fmis);
                    if (maps == null || maps.size() == 0) {
                        log.error("本人不存在");
                    } else {
                        for (Map<String, Object> map : maps) {
                            nh_zgwgz = map.get("NH_ZGWGZ").toString();
                            nh_zjxgz = map.get("NH_ZJXGZ").toString();
                            nh_zjtbt = map.get("NH_ZJTBT").toString();
                        }
                        String sqlStr = "INSERT INTO NH_HOLIDAY(nh_cardid,nh_dept,nh_name,nh_qingtype,nh_xiujiayear,nh_times" +
                                ",nh_sumdays,nh_fmis,nh_beizhu,nh_zgwgz,nh_zjxgz,nh_zjtbt) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                        dbBean.executeUpdate(sqlStr, nh_cardid, nh_dept, nh_name, nh_qingtype,
                                nh_year, nh_times, nh_sumdays, nh_fmis, nh_beizhu,nh_zgwgz,nh_zjxgz,nh_zjtbt);
                        dbBean.executeCommit();
                        rst.put("info", "成功导入数据");
                        log.info(importId + "成功导入数据");


                    }
                }
            }}
        return rst;
    }

    //加班导入
    private Map impDataOverTime(DBAccessBean dbBean, String impTp, Sheet sheet, int startIdx, int startCol, String importId,
                                boolean isOverWrite, String str) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map rst = new HashMap();
        Object allow = ((Map) config.get(importId)).get("allowRow");
        IAllowRow allowRow = null;
        if (allow != null && !"".equals(allow.toString().trim())) {
            try {
                allowRow = (IAllowRow) Class.forName(allow.toString()).newInstance();
            } catch (Exception e) {
                log.warn(importId + "配置[" + allow.toString() + "]出错", e);
                rst.put("msg", "配置[" + allow.toString() + "]出错");
                return rst;
            }
        }
        short bgClr = -1;
        short border = 0;
        if ("0".endsWith(impTp)) {// 列表形式导入
            int rowSum = sheet.getLastRowNum();
            for (; startIdx <= rowSum; startIdx++) {// 循环数据域
                Row row = sheet.getRow(startIdx);
                if (bgClr == -1) {
                    bgClr = row.getCell(startCol).getCellStyle().getFillBackgroundColor();
                    border = row.getCell(startCol).getCellStyle().getBorderBottom();
                } else {// 判断样式和边框是否相等
                    if (row.getCell(startCol).getCellStyle().getFillBackgroundColor() != bgClr
                            || row.getCell(startCol).getCellStyle().getBorderBottom() != border) {
                        break;
                    }
                }
                List cfgDtl = (List) ((Map) config.get(importId)).get("dtl");
                int seq = 0;
                Map value = new HashMap();
                List p = new ArrayList();
                boolean blankCheck = true;
                for (int i = 0; i < cfgDtl.size(); i++) {// 循环字段
                    Map fieldCfg = (Map) cfgDtl.get(i);
                    String val = null;
                    if (fieldCfg.get("defVal") != null && !"".equals(fieldCfg.get("defVal").toString())) {// 设置默认值
                        String settingDefVal = (String) fieldCfg.get("defVal");
                        if ("1".equals(fieldCfg.get("defValTp"))) {
                            Cell cell = row.getCell(startCol + seq);
                            if (cell == null) {
                                val = null;
                            } else {
                                val = getCellValue(cell, sheet.getWorkbook());
                            }
                            value.put(fieldCfg.get("fldNm"), val);
                            DefaultValue defValGen = (DefaultValue) Class.forName(settingDefVal).newInstance();
                            val = defValGen.generateValue(value, (String) fieldCfg.get("fldNm"));
                            seq++;
                        } else {
                            SystemVariable var = SystemVariableImpl.getInstance();
                            val = var.transVariable(settingDefVal);
                        }

                    } else {// 直接从excel中读取
                        Cell cell = row.getCell(startCol + seq);
                        if (cell == null) {
                            val = null;
                        } else {
                            val = getCellValue(cell, sheet.getWorkbook());
                        }
                        if (isMergedRegion(sheet, startIdx, startCol + seq)) {
                            seq++;
                        }
                        seq++;
                    }
                    value.put(fieldCfg.get("fldNm"), val);
                    p.add(val);
                }
                boolean exist = this.exitsOverTime(dbBean, importId, value);

                //满足条件覆盖
                if (exist) {// 如果存在记录并允许覆盖
                    try {
                        dbBean = DBAccessPool.getDbBean();
                        dbBean.setAutoCommit(false);
                        String o_name = value.get("o_name").toString();
                        String o_reason = value.get("o_reason").toString();
                        String o_otimes = value.get("o_otimes").toString();
                        String o_days = value.get("o_days").toString();
                        String o_idcard = value.get("o_idcard").toString();
                        String o_gz = "";
                        String minMony = "";
                        String o_daymoney = "";
                        String o_thdays = "";
                        String o_doubledays = "";
                        Double thdays = null;
                        //日期还有钱需要判断累加
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(o_otimes);
                        String seSql = "select money,to_char(STARTIMES,'yyyy-MM-dd') STARTIMES,to_char(ENDTIMES,'yyyy-MM-dd') ENDTIMES from  NH_MINWEGA";
                        List<Map<String, Object>> maps = dbBean.queryForList(seSql);
                        for (Map<String, Object> map : maps) {
                            //2018-06-30 23:59:59之前
                            if (map.get("STARTIMES") == null) {
                                if (map.get("ENDTIMES") == null) {
                                    rst.put("msg", "广州最低工资标准出错");
                                } else {
                                    String endtimes = map.get("ENDTIMES").toString();
                                    Date endtime = format.parse(endtimes);
                                    if (endtime.after(date) || date.equals(endtime)) {
                                        minMony = map.get("MONEY").toString();
                                    }
                                }
                            } else {
                                //2018-06-30 23:59:59之后
                                String startimes = map.get("STARTIMES").toString();
                                Date startimes_ = format.parse(startimes);
                                //最新
                                if (map.get("ENDTIMES") == null) {
                                    if (date.after(startimes_) || date.equals(startimes_)) {
                                        minMony = map.get("MONEY").toString();
                                    }
                                } else {//中间区域
                                    String endtimes1 = map.get("ENDTIMES").toString();
                                    Date endtime1 = format.parse(endtimes1);
                                    if (date.after(startimes_) && date.before(endtime1)) {
                                        minMony = map.get("MONEY").toString();
                                    } else if (date.equals(startimes_) || date.equals(endtime1)) {
                                        minMony = map.get("MONEY").toString();
                                    }
                                }
                            }

                        }
                        //生成数据：拿到工资
                        Double gz = 0.0;
                        String sql = "select w_gz from NH_DATASOURSE where w_idcard=?";
                        List<Map<String, Object>> maps1 = dbBean.queryForList(sql, o_idcard);
                        if (maps1.size() <= 0 || maps1 == null) {
                            log.error("数据库没有这个人");
                            rst.put("msg", "数据库没有这个人");
                        } else {
                            for (Map<String, Object> map : maps1) {
                                String w_gz = map.get("W_GZ").toString();
                                gz = Double.parseDouble(w_gz);
                                o_gz = gz + "";
                            }
                            Double o_minMoney = Double.parseDouble(minMony);
                            Double overmoney = gz > o_minMoney ? gz : o_minMoney;
                            String o_overmoney = overmoney + "";
                            BigDecimal omoneys = new BigDecimal(overmoney);
                            BigDecimal days = new BigDecimal(21.75);
                            BigDecimal daymoney = omoneys.divide(days, 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                            //倍数2倍或者3倍
                            if (o_reason.contains("节")) {
                                thdays = Double.parseDouble(o_days);
                                o_thdays = thdays + "";
                                daymoney = daymoney.multiply(new BigDecimal(3.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                o_daymoney = daymoney + "";
                            } else {
                                o_doubledays = o_days;
                                daymoney = daymoney.multiply(new BigDecimal(2.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                o_daymoney = daymoney + "";
                            }
                            String upsql = "update nh_overtime  set o_name=?,o_days=?,o_thdays=?,o_doubledays=?,o_gz=?,O_MINGZ=?,O_OVERGZ=?,O_DAYMONEY=? where  o_idcard=? and o_reason=? and o_otimes=?";
                            dbBean.executeUpdate(upsql, o_name, o_days, o_thdays, o_doubledays, o_gz, minMony, o_overmoney, o_daymoney, o_idcard, o_reason, o_otimes);
                            dbBean.executeCommit();
                            rst.put("info", "成功导入数据");
                            log.info(importId + "成功导入数据");
                        }

                    } catch (Exception e) {
                        log.error("导入出错", e);
                        rst.put("msg", "导入出错");
                        try {
                            if (dbBean != null) {
                                dbBean.executeRollBack();
                            }
                        } catch (Exception e2) {
                            log.error("数据库回滚出错", e2);
                        }
                    }

                    return rst;


                } else if (!exist) {
                    try {
                        dbBean = DBAccessPool.getDbBean();
                        dbBean.setAutoCommit(false);
                        String o_name = value.get("o_name").toString();
                        String o_reason = value.get("o_reason").toString();
                        String o_otimes = value.get("o_otimes").toString();
                        String o_days = value.get("o_days").toString();
                        String o_idcard = value.get("o_idcard").toString();
                        String o_gz = "";
                        String minMony = "";
                        String o_daymoney = "";
                        String o_thdays = "";
                        String o_doubledays = "";
                        Double thdays = null;
                        //日期还有钱需要判断累加
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = format.parse(o_otimes);
                        String seSql = "select money,to_char(STARTIMES,'yyyy-MM-dd') STARTIMES,to_char(ENDTIMES,'yyyy-MM-dd') ENDTIMES from  NH_MINWEGA";
                        List<Map<String, Object>> maps = dbBean.queryForList(seSql);
                        for (Map<String, Object> map : maps) {
                            //2018-06-30 23:59:59之前
                            if (map.get("STARTIMES") == null) {
                                if (map.get("ENDTIMES") == null) {
                                    rst.put("msg", "广州最低工资标准出错");
                                } else {
                                    String endtimes = map.get("ENDTIMES").toString();
                                    Date endtime = format.parse(endtimes);
                                    if (endtime.after(date) || date.equals(endtime)) {
                                        minMony = map.get("MONEY").toString();
                                    }
                                }
                            } else {
                                //2018-06-30 23:59:59之后
                                String startimes = map.get("STARTIMES").toString();
                                Date startimes_ = format.parse(startimes);
                                //最新
                                if (map.get("ENDTIMES") == null) {
                                    if (date.after(startimes_) || date.equals(startimes_)) {
                                        minMony = map.get("MONEY").toString();
                                    }
                                } else {//中间区域
                                    String endtimes1 = map.get("ENDTIMES").toString();
                                    Date endtime1 = format.parse(endtimes1);
                                    if (date.after(startimes_) && date.before(endtime1)) {
                                        minMony = map.get("MONEY").toString();
                                    } else if (date.equals(startimes_) || date.equals(endtime1)) {
                                        minMony = map.get("MONEY").toString();
                                    }
                                }
                            }

                        }
                        //生成数据：拿到工资
                        Double gz = 0.0;
                        String sql = "select w_gz from NH_DATASOURSE where w_idcard=?";
                        List<Map<String, Object>> maps1 = dbBean.queryForList(sql, o_idcard);
                        if (maps1.size() <= 0 || maps1 == null) {
                            log.error("数据库没有这个人");
                            rst.put("msg", "数据库没有这个人");
                        } else {
                            for (Map<String, Object> map : maps1) {
                                String w_gz = map.get("W_GZ").toString();
                                gz = Double.parseDouble(w_gz);
                                o_gz = gz + "";
                            }
                            Double o_minMoney = Double.parseDouble(minMony);
                            Double overmoney = gz > o_minMoney ? gz : o_minMoney;
                            String o_overmoney = overmoney + "";
                            BigDecimal omoneys = new BigDecimal(overmoney);
                            BigDecimal days = new BigDecimal(21.75);
                            BigDecimal daymoney = omoneys.divide(days, 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                            //倍数2倍或者3倍
                            if (o_reason.contains("节")) {
                                thdays = Double.parseDouble(o_days);
                                o_thdays = thdays + "";
                                daymoney = daymoney.multiply(new BigDecimal(3.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                o_daymoney = daymoney + "";
                            } else {
                                o_doubledays = o_days;
                                daymoney = daymoney.multiply(new BigDecimal(2.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                o_daymoney = daymoney + "";
                            }
                            //插入sql       String o_daymoney="";
                            //            String o_thdays="";
                            //            String o_doubledays="";
                            String sesql = "select count(*) from nh_overtime where o_idcard=? and o_reason=? and o_otimes=? ";
                            int i = dbBean.queryForInt(sesql, o_idcard, o_reason, o_otimes);

                            String o_status = "0";
                            String insql = "insert into nh_overtime(o_name,o_reason,o_otimes,o_days,o_idcard,o_thdays,o_doubledays,o_gz,O_MINGZ,O_OVERGZ,O_DAYMONEY,o_status) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                            dbBean.executeUpdate(insql, o_name, o_reason, o_otimes, o_days, o_idcard, o_thdays, o_doubledays, o_gz, minMony, o_overmoney, o_daymoney, o_status);
                            dbBean.executeCommit();
                            rst.put("info", "成功导入数据");
                            log.info(importId + "成功导入数据");
                        }

                    } catch (Exception e) {
                        log.error("导入出错", e);
                        rst.put("msg", "导入出错");
                        try {
                            if (dbBean != null) {
                                dbBean.executeRollBack();
                            }
                        } catch (Exception e2) {
                            log.error("数据库回滚出错", e2);
                        }
                    }

                }
            }
        }
        return rst;
    }

    //值班导入
    private Map impDataGoverTime(DBAccessBean dbBean, String impTp, Sheet sheet, int startIdx, int startCol, String importId,
                                 boolean isOverWrite, String str) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map rst = new HashMap();
        short bgClr = -1;
        short border = 0;
        if ("0".endsWith(impTp)) {// 列表形式导入
            int rowSum = sheet.getLastRowNum();
            for (; startIdx <= rowSum; startIdx++) {// 循环数据域
                Row row = sheet.getRow(startIdx);
                if (bgClr == -1) {
                    bgClr = row.getCell(startCol).getCellStyle().getFillBackgroundColor();
                    border = row.getCell(startCol).getCellStyle().getBorderBottom();
                } else {// 判断样式和边框是否相等
                    if (row.getCell(startCol).getCellStyle().getFillBackgroundColor() != bgClr
                            || row.getCell(startCol).getCellStyle().getBorderBottom() != border) {
                        break;
                    }
                }
                List cfgDtl = (List) ((Map) config.get(importId)).get("dtl");
                int seq = 0;
                Map value = new HashMap();
                List p = new ArrayList();
                boolean blankCheck = true;
                for (int i = 0; i < cfgDtl.size(); i++) {// 循环字段
                    Map fieldCfg = (Map) cfgDtl.get(i);
                    String val = null;
                    if (fieldCfg.get("defVal") != null && !"".equals(fieldCfg.get("defVal").toString())) {// 设置默认值
                        String settingDefVal = (String) fieldCfg.get("defVal");
                        if ("1".equals(fieldCfg.get("defValTp"))) {
                            Cell cell = row.getCell(startCol + seq);
                            if (cell == null) {
                                val = null;
                            } else {
                                val = getCellValue(cell, sheet.getWorkbook());
                            }
                            value.put(fieldCfg.get("fldNm"), val);
                            DefaultValue defValGen = (DefaultValue) Class.forName(settingDefVal).newInstance();
                            val = defValGen.generateValue(value, (String) fieldCfg.get("fldNm"));
                            seq++;
                        } else {
                            SystemVariable var = SystemVariableImpl.getInstance();
                            val = var.transVariable(settingDefVal);
                        }

                    } else {// 直接从excel中读取
                        Cell cell = row.getCell(startCol + seq);
                        if (cell == null) {
                            val = null;
                        } else {
                            val = getCellValue(cell, sheet.getWorkbook());
                        }
                        if (isMergedRegion(sheet, startIdx, startCol + seq)) {
                            seq++;
                        }
                        seq++;
                    }
                    value.put(fieldCfg.get("fldNm"), val);
                    p.add(val);
                }
                String g_idcard = value.get("g_idcard").toString();
                String secSql = "select count(*) from NH_GOVERMONEY where g_idcard=?";
                Boolean exist = dbBean.queryForInt(secSql, g_idcard) > 0;
                //满足条件覆盖
                if (!exist) {
                    try {

                        String g_name = value.get("g_name").toString();
                        String g_ztimes = value.get("g_ztimes").toString();
                        String g_zdays = value.get("g_zdays").toString();
                        String g_zsgz = value.get("g_zsgz").toString();
                        String g_status = "0";
                        String g_sgz = g_zsgz;
                        String insertsql = "insert into NH_GOVERMONEY(g_name,g_ztimes" +
                                ",g_zdays,g_zsgz,g_idcard,g_status,g_sgz) values(?,?,?,?,?,?,?)";
                        dbBean.executeUpdate(insertsql,g_name,g_ztimes,g_zdays,g_zsgz,g_idcard,g_status,g_sgz);
                        dbBean.executeCommit();
                        rst.put("info", "成功导入数据");
                        log.info(importId + "成功导入数据");
                    } catch (Exception e) {
                        log.error("导入出错", e);
                        rst.put("msg", "导入出错");
                        try {
                            if (dbBean != null) {
                                dbBean.executeRollBack();
                            }
                        } catch (Exception e2) {
                            log.error("数据库回滚出错", e2);
                        }
                    }
                    return rst;
                } else if (exist) {
                    try {
                        String g_name = value.get("g_name").toString();
                        String g_ztimes = value.get("g_ztimes").toString();
                        String g_zdays = value.get("g_zdays").toString();
                        String g_zsgz = value.get("g_zsgz").toString();
                        //判断之前是否导入
                        String checkSql="select count(*) from NH_GOVERMONEY where g_idcard=? and g_ztimes=? and g_zdays=? and g_zsgz=? and g_status='0' ";
                        int i = dbBean.queryForInt(checkSql, g_idcard, g_ztimes, g_zdays, g_zsgz);
                        if (i>0){
                            rst.put("info", "成功导入数据");
                            log.info(importId + "成功导入数据");
                            continue;
                        }else {
                        String sql="select g_times ,g_sgz from NH_GOVERMONEY where g_idcard=? and g_status='0' ";
                        List<Map<String, Object>> maps = dbBean.queryForList(sql, g_idcard);
                        String g_times="";
                        String g_sgz="";
                        for (Map<String, Object> map : maps) {
                           String time = map.get("G_TIMES").toString();
                           if (time.contains(g_ztimes)){
                               g_times=time;
                           }else {
                               g_times=time+","+g_ztimes;
                           }
                            String money = map.get("G_SGZ").toString();
                           if (money.equals(g_zsgz)){
                               g_sgz=money;
                            }else{
                               BigDecimal smoney=new BigDecimal(Double.parseDouble(money));
                               BigDecimal zmoney=new BigDecimal(Double.parseDouble(g_zsgz));
                               BigDecimal sgz=smoney.add(zmoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                               g_sgz=sgz+"";
                            }

                        }
                        String updateSql="update NH_GOVERMONEY set g_name=?,g_ztimes=?" +
                                ",g_zdays=?,g_zsgz=? ,g_times=? ,g_sgz=? where g_idcard=? and g_status='0' ";
                        dbBean.executeUpdate(updateSql,g_name,g_ztimes,g_zdays,g_zsgz,g_times,g_sgz,g_idcard);
                        dbBean.executeCommit();
                        rst.put("info", "成功导入数据");
                        log.info(importId + "成功导入数据");


                    }} catch (Exception e) {
                        log.error("导入出错", e);
                        rst.put("msg", "导入出错");
                        try {
                            if (dbBean != null) {
                                dbBean.executeRollBack();
                            }
                        } catch (Exception e2) {
                            log.error("数据库回滚出错", e2);
                        }
                    }

                }
            }
        }
        return rst;
    }


//本部省行绩效导入
    private Map impDataGjx(DBAccessBean dbBean, String impTp, Sheet sheet, int startIdx, int startCol, String importId,
                                 boolean isOverWrite, String str) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map rst = new HashMap();
        short bgClr = -1;
        short border = 0;
        if ("0".endsWith(impTp)) {// 列表形式导入
            int rowSum = sheet.getLastRowNum();
            for (; startIdx <= rowSum; startIdx++) {// 循环数据域
                Row row = sheet.getRow(startIdx);
                if (bgClr == -1) {
                    bgClr = row.getCell(startCol).getCellStyle().getFillBackgroundColor();
                    border = row.getCell(startCol).getCellStyle().getBorderBottom();
                } else {// 判断样式和边框是否相等
                    if (row.getCell(startCol).getCellStyle().getFillBackgroundColor() != bgClr
                            || row.getCell(startCol).getCellStyle().getBorderBottom() != border) {
                        break;
                    }
                }
                List cfgDtl = (List) ((Map) config.get(importId)).get("dtl");
                int seq = 0;
                Map value = new HashMap();
                List p = new ArrayList();
                boolean blankCheck = true;
                for (int i = 0; i < cfgDtl.size(); i++) {// 循环字段
                    Map fieldCfg = (Map) cfgDtl.get(i);
                    String val = null;
                    if (fieldCfg.get("defVal") != null && !"".equals(fieldCfg.get("defVal").toString())) {// 设置默认值
                        String settingDefVal = (String) fieldCfg.get("defVal");
                        if ("1".equals(fieldCfg.get("defValTp"))) {
                            Cell cell = row.getCell(startCol + seq);
                            if (cell == null) {
                                val = null;
                            } else {
                                val = getCellValue(cell, sheet.getWorkbook());
                            }
                            value.put(fieldCfg.get("fldNm"), val);
                            DefaultValue defValGen = (DefaultValue) Class.forName(settingDefVal).newInstance();
                            val = defValGen.generateValue(value, (String) fieldCfg.get("fldNm"));
                            seq++;
                        } else {
                            SystemVariable var = SystemVariableImpl.getInstance();
                            val = var.transVariable(settingDefVal);
                        }

                    } else {// 直接从excel中读取
                        Cell cell = row.getCell(startCol + seq);
                        if (cell == null) {
                            val = null;
                        } else {
                            val = getCellValue(cell, sheet.getWorkbook());
                        }
                        if (isMergedRegion(sheet, startIdx, startCol + seq)) {
                            seq++;
                        }
                        seq++;
                    }
                    value.put(fieldCfg.get("fldNm"), val);
                    p.add(val);
                }
                String d_fmis = value.get("d_fmis").toString();
                String d_project = value.get("d_project").toString();
                String d_name = value.get("d_name").toString();
                String d_dept= value.get("d_dept").toString();
                String d_jx=null;
                String d_gz =null;
               if  (value.get("d_jx")==null){
                   d_jx="0.0";
               }
               d_jx= value.get("d_jx").toString();
                if  (value.get("d_gz")==null){
                    d_gz="0.0";
                }
                d_gz= value.get("d_gz").toString();
                String secSql = "select count(*) from NH_GJX  where d_fmis=? and d_project=?";
                Boolean exist = dbBean.queryForInt(secSql, d_fmis,d_project) > 0;
                //满足条件覆盖
                if (!exist) {
                    try {

                        String d_status = "0";
                        String insertsql = "insert into NH_GJX(d_name,d_dept,d_jx,d_gz,d_fmis,d_project,d_xjx,d_xgz ) values (?,?,?,?,?,?,?,?)";
                        dbBean.executeUpdate(insertsql,d_name,d_dept,d_jx,d_gz,d_fmis,d_project,d_jx,d_gz);
                        dbBean.executeCommit();
                        rst.put("info", "成功导入数据");
                        log.info(importId + "成功导入数据");
                    } catch (Exception e) {
                        log.error("导入出错", e);
                        rst.put("msg", "导入出错");
                        try {
                            if (dbBean != null) {
                                dbBean.executeRollBack();
                            }
                        } catch (Exception e2) {
                            log.error("数据库回滚出错", e2);
                            rst.put("msg", "导入出错");
                        }
                    }
                } else if (exist) {
                    try {
                        String upsql = "update  NH_GJX set d_name=?,d_dept=?,d_jx=?,d_gz=?,d_xjx=?,d_xgz=? where d_fmis=? and d_project=?";
                        dbBean.executeUpdate(upsql,d_name,d_dept,d_jx,d_gz,d_jx,d_gz,d_fmis,d_project);
                        dbBean.executeCommit();

                            rst.put("info", "成功导入数据");
                            log.info(importId + "成功导入数据");


                        } catch (Exception e) {
                        log.error("导入出错", e);
                        rst.put("msg", "导入出错");
                        try {
                            if (dbBean != null) {
                                dbBean.executeRollBack();
                            }
                        } catch (Exception e2) {
                            log.error("数据库回滚出错", e2);
                            rst.put("msg", "导入出错");
                        }
                    }

                }
            }
        }
        return rst;
    }

}