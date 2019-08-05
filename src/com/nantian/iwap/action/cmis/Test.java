package com.nantian.iwap.action.cmis;

import com.nantian.iwap.app.imp.DefaultValue;
import com.nantian.iwap.app.imp.ImpDataFactory;
import com.nantian.iwap.app.imp.SystemVariable;
import com.nantian.iwap.app.imp.impl.SystemVariableImpl;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;
import com.nantian.iwap.web.upload.UploadConfig;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;


public class Test extends TransactionBizAction {
    private Map config;

    public void setConfig(Map config) {
        this.config = config;
    }

    private static Logger log = Logger.getLogger(DeptUpdateMgAction.class);

    @Override
    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");

        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }
        if ("upload".equals(option)) {
            return upload(dtbHelper);
        }
        if ("remove".equals(option)) {
            return remove(dtbHelper);
        }
        if ("add".equals(option)) {
            return add(dtbHelper);
        }
        if ("save".equals(option)) {
            return save(dtbHelper);
        }
        if ("test".equals(option)) {
            return test(dtbHelper);
        }

        return 0;
    }

    private int test(DTBHelper dtbHelper) {

        dtbHelper.setRstData("rows", "sss");
        System.out.println("测试成功");
        return 1;
    }

    private int save(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String d_no = dtbHelper.getStringValue("D_NO");
            String d_name = dtbHelper.getStringValue("D_NAME");
            String seSql = "select count(*) from nh_dept where d_no=?";
            int i = dbBean.queryForInt(seSql, d_no);
            if (i == 1) {
                String sql = "update nh_dept set d_name=? where d_no=?";
                dbBean.executeUpdate(sql, d_name, d_no);
                dbBean.executeCommit();
                flag = 1;
            } else {
                log.error("部门号不能修改");
                dtbHelper.setError("usermg-err-sv", "部门号不能修改");


            }
        } catch (Exception e) {
            log.error("保存出错", e);
            dtbHelper.setError("usermg-err-sv", "[保存出错]" + e.getMessage());
        }
        return flag;
    }

    private int add(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            String d_name = dtbHelper.getStringValue("D_NAME");
            String d_no = dtbHelper.getStringValue("D_NO");

            if (d_name.equals("") || d_no.equals("")) {
                dtbHelper.setError("usermg-err-add-002", "名字或者序号不能为空");
            } else {
                String sql = "insert into nh_dept(d_name,d_no) values(?,?) ";
                dbBean.executeUpdate(sql, d_name, d_no);
                dbBean.executeCommit();
                flag = 1;
            }

        } catch (Exception e) {
            log.error("新增出错", e);
            dtbHelper.setError("usermg-err-add-002", "[新增出错]" + e.getMessage());
            try {
                if (dbBean != null) {
                    dbBean.executeRollBack();
                }
            } catch (Exception e2) {
                log.error("数据库回滚出错", e2);
            }
        }
        return flag;

    }


    protected int query(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        try {

            DBAccessBean dbAccessBean = DBAccessPool.getDbBean();
            List<Map<String, Object>> dataList = null;
            String sqlStr = "select * from nh_dept  ";
            dataList = dbAccessBean.queryForList(sqlStr);
            sqlStr += " order by d_id desc";
            System.out.println("-------" + sqlStr);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("hello", "test");
            flag = 1;
        } catch (Exception e) {
            log.error("查询出错", e);
            dtbHelper.setError("subsidymg-err-qry", "[查询出错]" + e.getMessage());
        }
        return flag;
    }


    protected int remove(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("userids");
            String[] userarr = userids.split(",");
            int u_cnt = 0;
            int s_cnt = 0;
            for (String user : userarr) {
                if (user == null || "".equals(user.trim())) {
                    continue;
                }
                u_cnt++;
                int id = Integer.parseInt(user);
                int i = dbBean.executeUpdate("delete from NH_dept where d_id = ?", id);
                if (i == 1) {
                    s_cnt++;
                }
            }
            dbBean.executeCommit();
            if (u_cnt != s_cnt) {
                log.warn("删除出错:删除失败" + (u_cnt - s_cnt) + "条");
                dtbHelper.setError("usermg-err-rm-001", "[删除出错]删除失败" + (u_cnt - s_cnt) + "条");
            } else {
                flag = 1;
            }
        } catch (Exception e) {
            log.error("删除出错", e);
            dtbHelper.setError("usermg-err-rm-002", "[删除出错]" + e.getMessage());
            try {
                if (dbBean != null) {
                    dbBean.executeRollBack();
                }
            } catch (Exception e2) {
                log.error("数据库回滚出错", e2);
            }
        }
        return flag;
    }

//导入数据

    private int upload(DTBHelper dtbHelper) {
        int flag = 0;
        try {
            List files = dtbHelper.getListValue("files");
            if (files == null || files.size() != 1) {
                log.error("导入数据的文件数量必须为1");
                dtbHelper.setError("importdata-err-001", "导入数据的文件数量必须为1");
                return flag;
            }
            SystemVariableImpl svi = SystemVariableImpl.getInstance();
            svi.setDtbHelper(dtbHelper);
            String fileName = files.get(0).toString();
            fileName = UploadConfig.tempPath + fileName;
            String imp_id = dtbHelper.getStringValue("imp_id");
            String modelcode = dtbHelper.getStringValue("option");
            if (imp_id == null || "".equals(imp_id.toString().trim())) {
                log.error("导入数据类型ID不能为空");
                dtbHelper.setError("importdata-err-002", "导入数据类型ID不能为空");
                return flag;
            }
            ImpDataFactory idf = ImpDataFactory.getInstance();
            Map config = idf.getConfig();
            String table = "nh_dept";
            StringBuffer msg = new StringBuffer(200);
            Map cfg = (Map) config.get(imp_id);

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
            Map rst = new HashMap();
            log.info("开始导入Excel" + fileName + " table=" + table + " ");
            rst.put("flag", true);
            Workbook workbook = null;
            try {
                DBAccessPool.createDbBean();
                DBAccessBean dbBean = DBAccessPool.getDbBean();
                if (fileName.endsWith("xls")) {
                    workbook = new HSSFWorkbook(new FileInputStream(fileName));
                } else {
                    workbook = new XSSFWorkbook(fileName);
                }
                Sheet sheet = workbook.getSheetAt(sheetIdx);
                short bgClr = -1;
                short border = 0;
                // 列表形式导入

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
                    List cfgDtl = (List) ((Map) config.get(imp_id)).get("dtl");
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
                    String d_no = value.get("d_no").toString();
                    String d_name = value.get("d_name").toString();

                    String secSql = "select count(*) from NH_DEPT where d_name=?";
                    Boolean exist = dbBean.queryForInt(secSql, d_name) > 0;
                    //满足条件覆盖
                    if (!exist) {
                        try {
                            String insertsql = "insert into NH_DEPT(d_name,d_no) values (?,?)";
                            dbBean.executeUpdate(insertsql, d_name, d_no);
                            dbBean.executeCommit();
                            rst.put("info", "成功导入数据");
                            log.info(imp_id + "成功导入数据");
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
                            String updateSql = "update  nh_dept set d_name=?  where d_no=?";
                            dbBean.executeUpdate(updateSql, d_no, d_no);
                            rst.put("info", "成功导入数据");
                            log.info(imp_id + "成功导入数据");
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

            } catch (Exception e) {
                log.warn("导入Excel", e);
                msg.append("文件未发现\r\n");
            }

            if (rst.get("msg") != null && !"".equals(rst.get("msg").toString().trim())) {
                dtbHelper.setError("importdata-err-003", "[数据导入出错]" + rst.get("msg"));
            } else {
                dtbHelper.setRstData("info", rst.get("info"));
                flag = 1;
            }
        } catch (Exception e) {
            log.error("数据导入出错", e);
            dtbHelper.setError("importdata-err-004", "[数据导入出错]" + e.getMessage());
        }
        return flag;
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
}
