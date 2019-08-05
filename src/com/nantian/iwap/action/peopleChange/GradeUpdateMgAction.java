package com.nantian.iwap.action.peopleChange;

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
import com.nantian.iwap.persistence.PaginationSupport;
import com.nantian.iwap.web.upload.UploadConfig;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;


public class GradeUpdateMgAction extends TransactionBizAction {
    private Map config;

    public void setConfig(Map config) {
        this.config = config;
    }

    private static Logger log = Logger.getLogger(GradeUpdateMgAction.class);


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


        return 0;
    }

    private int save(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            int num = dtbHelper.getIntValue("NUM");
            int postNum = dtbHelper.getIntValue("POST_NUM");
            String dept = dtbHelper.getStringValue("DEPT");
            String post = dtbHelper.getStringValue("POST");
            postNum=postNum-num;
            if (postNum>=0){
                String updateSql="update CHAGE_POST set post_num =? where dept=?and post=?";
                dbBean.executeUpdate(updateSql,postNum,dept,post);
                dbBean.executeCommit();
                flag=1;

            }else {
                log.error("超过存在岗位数量");
                dtbHelper.setError("usermg-err-sv", "超过存在岗位数量" );

            }


        } catch (Exception e) {
            log.error("减少岗位出错", e);
            dtbHelper.setError("usermg-err-sv", "减少岗位数量出错" + e.getMessage());
        }
        return flag;
    }

    private int add(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            String dept = dtbHelper.getStringValue("dept");
            String post = dtbHelper.getStringValue("post");
            String postNum = dtbHelper.getStringValue("postNum");
            String postGrade = dtbHelper.getStringValue("postGrade");
            String postRank = dtbHelper.getStringValue("postRank");
            String status = dtbHelper.getStringValue("status");
            String sc_unit = dtbHelper.getStringValue("sc_unit");
            String remark = dtbHelper.getStringValue("remark");
            String sql="insert into CHAGE_POST(dept,post,post_num,post_grade,post_rank,status,SECTIONT_UNIT,remark) values(?,?,?,?,?,?,?,?)";
            dbBean.executeUpdate(sql,dept,post,postNum,postGrade,postRank,status,sc_unit,remark);
            dbBean.executeCommit();
            flag = 1;


        } catch (Exception e) {
            log.error("部门新增出错", e);
            dtbHelper.setError("usermg-err-add-002", "部门新增出错" + e.getMessage());
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
        List<Map<String, Object>> dataList = null;
        try {
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            String sqlStr = "select * from CHAGE_POST where 1=1 ";
            String postName = dtbHelper.getStringValue("postName");
            String deptName = dtbHelper.getStringValue("deptName");
            if (!"".equals(postName)) {
                sqlStr += " and post like '%" + postName + "%'";
            }
            if (!"".equals(deptName)) {
                sqlStr += " and dept like '%" + deptName + "%'";
            }
            sqlStr += " order by id ";
            dataList = dbBean.queryForList(sqlStr, page);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("total", page.getTotalCount());
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
            String userids = dtbHelper.getStringValue("ids");
            String[] userarr = userids.split(",");
            int u_cnt = 0;
            int s_cnt = 0;
            for (String user : userarr) {
                if (user == null || "".equals(user.trim())) {
                    continue;
                }
                u_cnt++;
                int id = Integer.parseInt(user);
                int i = dbBean.executeUpdate("delete from CHAGE_POST where id = ?", id);
                if (i == 1) {
                    s_cnt++;
                }
            }
            dbBean.executeCommit();
            if (u_cnt != s_cnt) {
                log.warn("删除失败" + (u_cnt - s_cnt) + "条");
                dtbHelper.setError("usermg-err-rm-001", "删除出错" + (u_cnt - s_cnt) + "条");
            } else {
                flag = 1;
            }
        } catch (Exception e) {
            log.error("删除出错", e);
            dtbHelper.setError("usermg-err-rm-002", "删除出错" + e.getMessage());
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
                log.error("导入文件数量必须为1");
                dtbHelper.setError("importdata-err-001", "导入数据的文件数量必须为1");
                return flag;
            }
            SystemVariableImpl svi = SystemVariableImpl.getInstance();
            svi.setDtbHelper(dtbHelper);
            String fileName = files.get(0).toString();
            fileName = UploadConfig.tempPath + fileName;
            String imp_id = dtbHelper.getStringValue("imp_id");
            if (imp_id == null || "".equals(imp_id.toString().trim())) {
                log.error("导入数据类型不能为空");
                dtbHelper.setError("importdata-err-002", "导入数据类型ID不能为空");
                return flag;
            }
            ImpDataFactory idf = ImpDataFactory.getInstance();
            Map config = idf.getConfig();
            String table = "CHAGE_POST";
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
                String sheetIdxStr = String.valueOf(cfg.get("sheetIdx"));
                sheetIdx = Integer.parseInt(sheetIdxStr);
            }
            Map rst = new HashMap();
            log.info("开始导入" + fileName + " table=" + table + " ");
            rst.put("flag", true);
            Workbook workbook = null;
            try {
                DBAccessPool.createDbBean();
                DBAccessBean dbBean = DBAccessPool.getDbBean();
                if (fileName.endsWith("xls")) {
                    workbook = new HSSFWorkbook(new FileInputStream(fileName));
                } else {
                    workbook = new XSSFWorkbook(new FileInputStream(fileName));
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

                    String dept = value.get("DEPT").toString();
                    String post = value.get("POST").toString();
                    String postNum = value.get("POST_NUM").toString();
                    String postGrade = value.get("POST_GRADE").toString();
                    String postRank = value.get("POST_RANK").toString();
                    String remark ="";
                    if (value.get("REMARK")!=null){
                     remark = value.get("REMARK").toString();
                    }
                    String status = value.get("STATUS").toString();
                    String sectiontUnit = value.get("SECTIONT_UNIT").toString();

                    String secSql = "select count(*) from CHAGE_POST where dept=? and post=?";
                    Boolean exist = dbBean.queryForInt(secSql, dept,post) > 0;
                    //满足条件覆盖
                    if (!exist) {
                        try {
                            String insertsql = "insert into CHAGE_POST(dept,post,post_num,post_grade,post_rank,remark,status,sectiont_unit) values (?,?,?,?,?,?,?,?)";
                            dbBean.executeUpdate(insertsql,dept,post,postNum,postGrade,postRank,remark,status,sectiontUnit);
                            dbBean.executeCommit();
                            rst.put("info", "成功导入岗位数据");
                            log.info(imp_id + "成功导入岗位数据");
                        } catch (Exception e) {
                            log.error("导入岗位数据出错", e);
                            rst.put("msg", "导入岗位数据出错");
                            try {
                                if (dbBean != null) {
                                    dbBean.executeRollBack();
                                }
                            } catch (Exception e2) {
                                log.error("数据库出错", e2);
                                rst.put("msg", "导入出错");
                            }
                        }
                    } else if (exist) {
                        try {
                            String updateSql = "update CHAGE_POST set post_num=?,post_grade=?,post_rank=?,remark=?,status=?,sectiont_unit=? where dept=? and post=?";
                            dbBean.executeUpdate(updateSql,postNum,postGrade,postRank,remark,status,sectiontUnit,dept,post);
                            rst.put("info", "成功导入数据");
                            log.info(imp_id + "成功导入数据");
                        } catch (Exception e) {
                            log.error("导入岗位数据出错", e);
                            rst.put("msg", "导入岗位出错");
                            try {
                                if (dbBean != null) {
                                    dbBean.executeRollBack();
                                }
                            } catch (Exception e2) {
                                log.error("数据库出错", e2);
                                rst.put("msg", "导入岗位出错");
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
                    Date date = DateUtil.getJavaDate(value);
                    rst = sdf.format(date);
                } else if (format == 20 || format == 32) {
                    //时间
                    sdf = new SimpleDateFormat("HH:mm");
                    double value = cell.getNumericCellValue();
                    Date date = DateUtil.getJavaDate(value);
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
