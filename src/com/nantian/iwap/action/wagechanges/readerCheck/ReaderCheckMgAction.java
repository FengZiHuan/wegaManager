package com.nantian.iwap.action.wagechanges.readerCheck;


import com.nantian.iwap.app.exp.ExpDataFactory;
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
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


public class ReaderCheckMgAction extends TransactionBizAction {
    private Map config;

    public void setConfig(Map config) {
        this.config = config;
    }

    private static Logger log = Logger.getLogger(ReaderCheckMgAction.class);


    @Override
    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");

        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }
        if ("deleteById".equals(option)) {
            return deleteById(dtbHelper);
        }
        if ("upload".equals(option)) {
            return upload(dtbHelper);
        }
        if ("remove".equals(option)) {
            return remove(dtbHelper);
        }
        if ("see".equals(option)) {
            return see(dtbHelper);
        }
        if ("export".equals(option)) {
            return export(dtbHelper);
        }
        if ("add".equals(option)) {
            return add(dtbHelper);
        }
        if ("save".equals(option)) {
            return save(dtbHelper);
        }
        if ("sure".equals(option)) {
            return doSure(dtbHelper);
        }


        return 0;
    }

    private int doSure(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String checkNo = dtbHelper.getStringValue("checkNo");

            String sql = "update  READERCHECK set status='1' where CHECK_NO = ?";
            dbBean.executeUpdate(sql, checkNo);
            dbBean.executeCommit();
            dbBean.executeCommit();
            flag=1;
        } catch (Exception e) {
            log.error("归档出错", e);
            dtbHelper.setError("usermg-err-rm-002", "归档出错" + e.getMessage());
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

    private int see(DTBHelper dtbHelper) {
        int flag = 0;
        try {
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            List<Map<String, Object>> dataList = null;
            String checkNo = dtbHelper.getStringValue("checkNo");
            String file_url = "";
            String sqlStr = "select file_url from READERCHECK where check_no=? ";
            dataList = dbBean.queryForList(sqlStr, checkNo);
            if (dataList != null && dataList.size() > 0) {
                for (Map<String, Object> map : dataList)
                    file_url = map.get("FILE_URL").toString();
            }
            dtbHelper.setRstData("rows", file_url);
            flag = 1;
        } catch (Exception e) {
            log.error("附件没导入", e);
            dtbHelper.setError("file-err", "附件没导入" + e.getMessage());
        }
        return flag;
    }

    private int deleteById(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String checkNo = dtbHelper.getStringValue("checkNo");
            //先删除图片
            String file_url = "";
            String sqlStr = "select file_url from READERCHECK where check_no=? ";
            List<Map<String, Object>> dataList = dbBean.queryForList(sqlStr, checkNo);
            if (dataList != null && dataList.size() > 0) {
                for (Map<String, Object> map : dataList)
                    file_url = map.get("FILE_URL").toString();
              }
            String[] split = file_url.split("/");
            file_url=split[2];

            try{
            file_url = UploadConfig.tempPath + file_url;
            File file=new File(file_url);
            file.delete();
            }catch (Exception e){
                log.error("删除附件出错,文件不存在", e);
                dtbHelper.setError("usermg-err-rm-002", "删除附件出错，文件不存在" + e.getMessage());


            }
            //在删除url
            String sql = "update  READERCHECK set file_url=null where CHECK_NO = ?";
            dbBean.executeUpdate(sql, checkNo);
            dbBean.executeCommit();
            dbBean.executeCommit();
            flag=1;
        } catch (Exception e) {
            log.error("删除附件出错", e);
            dtbHelper.setError("usermg-err-rm-002", "删除附件出错" + e.getMessage());
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

    private int save(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String checkNo = dtbHelper.getStringValue("checkNo");
            String name = dtbHelper.getStringValue("NAME");
            String type = dtbHelper.getStringValue("TYPE");
            String remark = dtbHelper.getStringValue("REMARK");
            String times = dtbHelper.getStringValue("TIMES");
            String seSql = "update READERCHECK set name=?,type=?,remark=?,times=? where check_no=?";
            dbBean.executeUpdate(seSql,name,type,remark,times,checkNo);
            dbBean.executeCommit();
            flag=1;
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
            String times = dtbHelper.getStringValue("TIMES");
            String type = dtbHelper.getStringValue("TYPE");
            String name = dtbHelper.getStringValue("NAME");
            String remark = dtbHelper.getStringValue("REMARK");
            String checkNo = null;
            //生成流水号算法生成2019-001类型流水号。
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            SimpleDateFormat fortimes = new SimpleDateFormat("yyyy-MM-dd");
            String forTimes = fortimes.format(date);
            if (times.equals("")) {
                times = forTimes;
            }
            String year = format.format(date);
            int num;
            //获取数据库中最大的num值
            String sql = "select max(NUM) from READERCHECK";
            num = dbBean.queryForInt(sql);
            //查询数据库如果数据库不存在年份代表新的年份流水号变为2020-001这样。
            String seLectSql = "select count(*) from READERCHECK where year=? ";
            int i = dbBean.queryForInt(seLectSql, year);
            //重置流水号
            if (i == 0) {
                //设num=1;
                num = 1;
                checkNo = year + "-" + "00" + num;

            } else {
                if (num == 0) {
                    num = 1;
                }
                num++;
                if (num < 10) {
                    checkNo = year + "-" + "00" + num;
                } else if (num >= 10 && num < 100) {
                    checkNo = year + "-" + "0" + i;
                } else {
                    checkNo = year + "-" + i;
                }
            }

            String insertSql = "insert into READERCHECK(CHECK_NO,TIMES,NAME,TYPE,YEAR,NUM,REMARK) values(?,?,?,?,?,?,?)";
            dbBean.executeUpdate(insertSql, checkNo, times, name, type, year, num, remark);
            dbBean.executeCommit();
            flag = 1;

        } catch (Exception e) {
            log.error("新增出错", e);
            dtbHelper.setError("usermg-err-add-002", "新增出错" + e.getMessage());
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

            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String types = dtbHelper.getStringValue("types");
            String userName = dtbHelper.getStringValue("userName");
            String no = dtbHelper.getStringValue("no");
            String remarks = dtbHelper.getStringValue("remarks");
            String time = dtbHelper.getStringValue("time");
            String sqlStr = "select * from READERCHECK where 1=1 ";
            if (!"".equals(userName)) {
                sqlStr += " and NAME like '%" + userName + "%'";
            }
            if (!"".equals(remarks)) {
                sqlStr += " and REMARK like '%" + remarks + "%'";
            }
            if (!"".equals(no)) {
                sqlStr += " and check_no = '" + no + "'";
            }
            if (!"".equals(time)) {
                sqlStr += " and TIMES = '" + time + "'";
            }
            if (!"".equals(types)) {
                sqlStr += " and TYPE = '" + types + "'";
            }
            sqlStr += " order by CHECK_NO desc";
            System.out.println("-------" + sqlStr);
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
            String userids = dtbHelper.getStringValue("userids");
            String[] userarr = userids.split(",");
            int u_cnt = 0;
            int s_cnt = 0;
            for (String user : userarr) {
                if (user == null || "".equals(user.trim())) {
                    continue;
                }
                u_cnt++;

                int i = dbBean.executeUpdate("delete from READERCHECK  where CHECK_NO = ?", user);
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

    //导入图片
    private int upload(DTBHelper dtbHelper) {
        int flag = 0;
        Map rst = new HashMap();
        DBAccessBean dbBean = null;

        List files = dtbHelper.getListValue("files");
        if (files == null || files.size() != 1) {
            log.error("导入数据的文件数量必须为1");
            dtbHelper.setError("importdata-err-001", "导入数据的文件数量必须为1");
            return flag;
        }
        SystemVariableImpl svi = SystemVariableImpl.getInstance();
        svi.setDtbHelper(dtbHelper);
        String fileName = files.get(0).toString();
        fileName = "/upload/" + fileName;

        try {
            dbBean = DBAccessPool.getDbBean();
            System.out.println("恭喜你成功");
            String checkNo = dtbHelper.getStringValue("checkNo");
            String insertSql = "update READERCHECK set file_url=? where check_no=?";
            dbBean.executeUpdate(insertSql, fileName, checkNo);
            dbBean.executeCommit();
            rst.put("info", "成功导入附件");
            log.info("成功导入附件");
            dtbHelper.setRstData("info", rst.get("info"));
            flag = 1;

        } catch (Exception e) {
            log.error("导入失败，找不到文件");
            dtbHelper.setError("importdata-err-002", "导入数据失败，io流出错");
            return flag;
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

    private int export(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;
        List<Map<String, Object>> dataList = null;
        Map rst = new HashMap();
        try {
            dbBean = DBAccessPool.getDbBean();
            String filetype = "xlsx";
            String titleString = dtbHelper.getStringValue("titleString");
            //导出表格顺序
            String checkNo = dtbHelper.getStringValue("checkNo");
            String times = null;
            String remark = null;
            String sql = "select TIMES,REMARK from READERCHECK where CHECK_NO=? ";
            dataList = dbBean.queryForList(sql, checkNo);
            for (Map<String, Object> map : dataList) {
                times = map.get("TIMES").toString();
                remark = map.get("REMARK").toString();
            }
            List<String> titleId = new ArrayList<>();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFFont font = workbook.createFont();
            font.setFontName("仿宋_GB2312");
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            Sheet sheet = workbook.createSheet();
            sheet.setColumnWidth(0, 256 * 3);
            font.setFontHeightInPoints((short) 15);


            XSSFFont font1 = workbook.createFont();
            font1.setFontName("仿宋_GB2312");
            font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            font1.setFontHeightInPoints((short) 10);
            //字体样式居中
            CellStyle fontcellStyle = workbook.createCellStyle();
            fontcellStyle.setFont(font);
            fontcellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            fontcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);


            //小字体样式居中
            CellStyle xfontcellStyle = workbook.createCellStyle();
            xfontcellStyle.setFont(font1);
            xfontcellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            xfontcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

            //设定样式
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            sheet.setDisplayGridlines(false);
            cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            cellStyle.setBorderBottom((short) 2);

            cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
            //设定样式右边框
            CellStyle rigthStyle = workbook.createCellStyle();
            rigthStyle.setFont(font);
            rigthStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            rigthStyle.setBorderRight((short) 2);
            rigthStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            rigthStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);


            CellStyle leftStype = workbook.createCellStyle();
            leftStype.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            leftStype.setBorderLeft((short) 2);
            leftStype.setFont(font1);
            leftStype.setAlignment(XSSFCellStyle.ALIGN_LEFT);
            leftStype.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);


            CellStyle checkStype = workbook.createCellStyle();

            checkStype.setFont(font1);
            checkStype.setAlignment(XSSFCellStyle.ALIGN_LEFT);
            checkStype.setAlignment(XSSFCellStyle.VERTICAL_TOP);
            checkStype.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);


            //设定样式审批时间样式1
            CellStyle timeStype = workbook.createCellStyle();
            timeStype.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            timeStype.setFont(font1);
            timeStype.setBorderBottom((short) 2);
            timeStype.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            timeStype.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

            //设定样式审批时间左边样式
            CellStyle timeStypeLeft = workbook.createCellStyle();
            timeStypeLeft.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            timeStypeLeft.setFont(font1);
            timeStypeLeft.setBorderLeft((short) 2);
            timeStypeLeft.setBorderBottom((short) 2);
            timeStypeLeft.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            timeStypeLeft.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

            //设定样式审批时间左边样式
            CellStyle timeStypeRight = workbook.createCellStyle();
            timeStypeRight.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            timeStypeRight.setFont(font1);
            timeStypeRight.setBorderRight((short) 2);
            timeStypeRight.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            timeStypeRight.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
            timeStypeRight.setBorderBottom((short) 2);

            CellStyle cellStyle1 = workbook.createCellStyle();
            cellStyle1.setFont(font1);
            cellStyle1.setBorderLeft((short) 2);
            cellStyle1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
            cellStyle1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);


            int maxLine = ExpDataFactory.MAX_LINE > dataList.size() ? dataList.size() : ExpDataFactory.MAX_LINE;
            if (dataList.size() > 0) {
                // 写入标题第一行

                String[] frist = new String[]{"", "", "", "", "", "", "", "", "", ""};
                Row rowTitle = sheet.createRow(0);
                Cell startCell0 = rowTitle.createCell(0);

                for (int i = 1; i < frist.length; i++) {
                    Cell endCell = rowTitle.createCell(i);
                    endCell.setCellValue(frist[i]);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0,
                            1, 9));
                }

                String[] sec = new String[]{"", "", "", "", "", "", "", "", "", ""};
                rowTitle = sheet.createRow(1);
                Cell endCell0 = rowTitle.createCell(0);
                Cell endCell11 = rowTitle.createCell(1);
                for (int i = 3; i < sec.length - 1; i++) {
                    Cell endCell = rowTitle.createCell(i);
                    endCell.setCellValue(frist[i]);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1,
                            3, 8));
                }
                Cell endCell = rowTitle.createCell(9);


                rowTitle = sheet.createRow(2);
                String[] th = new String[]{"", "", "中国农业银行广东省分行薪酬事项审批表", "", "", "", "", "", "", ""};
                Cell cell20 = rowTitle.createCell(0);
                Cell cell21 = rowTitle.createCell(1);
                for (int i = 2; i < th.length - 1; i++) {
                    Cell endCell2 = rowTitle.createCell(i);
                    endCell2.setCellValue(th[i]);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2,
                            2, 8));
                    endCell2.setCellStyle(fontcellStyle);
                }
                Cell endCell29 = rowTitle.createCell(9);


                rowTitle = sheet.createRow(3);
                Cell cell30 = rowTitle.createCell(0);
                Cell cell31 = rowTitle.createCell(1);
                Cell cell32 = rowTitle.createCell(2);
                cell32.setCellValue("(" + checkNo + ")");
                cell32.setCellStyle(xfontcellStyle);
                sheet.addMergedRegion(new CellRangeAddress(3, 3,
                        2, 8));
                Cell endCell39 = rowTitle.createCell(9);

                String[] th1 = new String[]{"", "申请日期：", times, "", "", "", "", "", "", ""};
                rowTitle = sheet.createRow(4);
                Cell cell40 = rowTitle.createCell(0);
                Cell cell41 = rowTitle.createCell(1);
                cell41.setCellValue("申请日期：");
                cell41.setCellStyle(timeStype);
                for (int i = 2; i < th1.length; i++) {
                    Cell endCell1 = rowTitle.createCell(i);
                    endCell1.setCellValue(th1[i]);
                    endCell1.setCellStyle(timeStype);
                    sheet.addMergedRegion(new CellRangeAddress(4, 4,
                            2, 3));
                }
                Cell endCell49 = rowTitle.createCell(9);
                endCell49.setCellStyle(timeStype);


                rowTitle = sheet.createRow(5);
                Cell cell5 = rowTitle.createCell(0);
                Cell cell51 = rowTitle.createCell(01);
                cell51.setCellStyle(leftStype);
                cell5.setCellValue("");
                cell5.setCellStyle(cellStyle1);
                sheet.addMergedRegion(new CellRangeAddress(5, 5,
                        2, 8));
                Cell cell59 = rowTitle.createCell(9);
                cell59.setCellStyle(rigthStyle);

                String[] six = new String[]{"", "申请审批事项：", "", "", "", "", "", ""};
                rowTitle = sheet.createRow(6);
                Cell endCell60 = rowTitle.createCell(0);
                for (int i = 1; i < 3; i++) {
                    Cell endCell2 = rowTitle.createCell(i);
                    endCell2.setCellValue(six[i]);
                    endCell2.setCellStyle(cellStyle1);
                    sheet.addMergedRegion(new CellRangeAddress(6, 6,
                            1, 2));
                    endCell2.setCellStyle(leftStype);

                }
                Cell endCell69 = rowTitle.createCell(9);
                endCell69.setCellStyle(rigthStyle);

                String[] seven = new String[]{"", "", remark, "", "", "", "", ""};
                //设边框
                rowTitle = sheet.createRow(7);
                Cell remarkCell00 = rowTitle.createCell(0);
                Cell remarkCell01 = rowTitle.createCell(1);
                Cell remarkCell09 = rowTitle.createCell(9);
                remarkCell01.setCellStyle(leftStype);
                remarkCell09.setCellStyle(rigthStyle);
                for (int i = 2; i < seven.length; i++) {
                    Cell endCell1 = rowTitle.createCell(i);
                    endCell1.setCellValue(seven[i]);
                    endCell1.setCellStyle(checkStype);
                    sheet.addMergedRegion(new CellRangeAddress(7, 16,
                            2, 8));
                }
                //边框
                for (int i = 8; i < 17; i++) {
                    rowTitle = sheet.createRow(i);
                    Cell remarkCell = rowTitle.createCell(1);
                    Cell remarkCellend = rowTitle.createCell(9);
                    remarkCell.setCellStyle(leftStype);
                    remarkCellend.setCellStyle(rigthStyle);
                }


                String[] check = new String[]{"", "", "", "", "复核: ", "", "", ""};
                rowTitle = sheet.createRow(17);
                Cell peopelCell = rowTitle.createCell(1);
                peopelCell.setCellValue("经办人: ");
                peopelCell.setCellStyle(leftStype);
                for (int i = 2; i < check.length; i++) {
                    Cell endCell3 = rowTitle.createCell(i);
                    endCell3.setCellValue(check[i]);
                    endCell3.setCellStyle(xfontcellStyle);
                }
                Cell peopelCellend = rowTitle.createCell(9);
                peopelCellend.setCellStyle(rigthStyle);


                rowTitle = sheet.createRow(18);
                Cell cellspace = rowTitle.createCell(1);

                cellspace.setCellStyle(timeStypeLeft);
                for (int i = 2; i < th1.length; i++) {
                    Cell endCell1 = rowTitle.createCell(i);
                    endCell1.setCellStyle(timeStype);
                }
                Cell cellspaceEnd = rowTitle.createCell(9);
                cellspaceEnd.setCellStyle(timeStypeRight);

                rowTitle = sheet.createRow(19);
                Cell space = rowTitle.createCell(1);
                space.setCellStyle(leftStype);
                Cell spaceEnd = rowTitle.createCell(9);
                spaceEnd.setCellStyle(rigthStyle);

                String[] str = new String[]{"", "人力资源部意见：", "", "", "", "", "", ""};
                rowTitle = sheet.createRow(20);
                for (int i = 1; i < 3; i++) {
                    Cell endCell2 = rowTitle.createCell(i);
                    endCell2.setCellValue(str[i]);
                    endCell2.setCellStyle(cellStyle1);
                    sheet.addMergedRegion(new CellRangeAddress(20, 20,
                            1, 2));
                    endCell2.setCellStyle(leftStype);

                }
                Cell fenCell = rowTitle.createCell(9);
                fenCell.setCellStyle(rigthStyle);

                for (int i = 21; i < 24; i++) {
                    rowTitle = sheet.createRow(i);
                    Cell remarkCell = rowTitle.createCell(1);
                    Cell remarkCellend = rowTitle.createCell(9);
                    remarkCell.setCellStyle(leftStype);
                    remarkCellend.setCellStyle(rigthStyle);
                }


                rowTitle = sheet.createRow(24);
                Cell cellspace1 = rowTitle.createCell(1);

                cellspace1.setCellStyle(timeStypeLeft);
                for (int i = 2; i < th1.length; i++) {
                    Cell endCell1 = rowTitle.createCell(i);
                    endCell1.setCellStyle(timeStype);
                }
                Cell cellspaceEnd1 = rowTitle.createCell(9);
                cellspaceEnd1.setCellStyle(timeStypeRight);


                rowTitle = sheet.createRow(25);
                Cell space1 = rowTitle.createCell(1);
                space1.setCellStyle(leftStype);
                Cell spaceEnd1 = rowTitle.createCell(9);
                spaceEnd1.setCellStyle(rigthStyle);

                String[] str1 = new String[]{"", "省行行领导审批意见：", "", "", "", "", "", ""};
                rowTitle = sheet.createRow(26);
                for (int i = 1; i < 3; i++) {
                    Cell endCell2 = rowTitle.createCell(i);
                    endCell2.setCellValue(str1[i]);
                    endCell2.setCellStyle(cellStyle1);
                    sheet.addMergedRegion(new CellRangeAddress(26, 26,
                            1, 2));
                    endCell2.setCellStyle(leftStype);

                }
                Cell fenCell1 = rowTitle.createCell(9);
                fenCell1.setCellStyle(rigthStyle);


                for (int i = 27; i < 30; i++) {
                    rowTitle = sheet.createRow(i);
                    Cell remarkCell = rowTitle.createCell(1);
                    Cell remarkCellend = rowTitle.createCell(9);
                    remarkCell.setCellStyle(leftStype);
                    remarkCellend.setCellStyle(rigthStyle);
                }

                rowTitle = sheet.createRow(30);
                Cell cellspace2 = rowTitle.createCell(1);

                cellspace2.setCellStyle(timeStypeLeft);
                for (int i = 2; i < th1.length; i++) {
                    Cell endCell1 = rowTitle.createCell(i);
                    endCell1.setCellStyle(timeStype);
                }
                Cell cellspaceEnd2 = rowTitle.createCell(9);
                cellspaceEnd2.setCellStyle(timeStypeRight);

                System.out.println(maxLine);
                //处理尾页excel
                rst.put("info", workbook);
            }
        } catch (Exception e) {
            log.error("导出数据出错", e);
            rst.put("msg", "导出数据出错:" + e.getMessage());
        } finally {
            DBAccessPool.releaseDbBean();
        }
        if (rst.get("msg") != null && !"".equals(rst.get("msg").toString().trim())) {
            log.error("导出处理类数据导出出错:" + rst.get("msg"));
            dtbHelper.setError("exportdata-err-004", "[导出处理类数据导出出错]" + rst.get("msg"));
        } else {
            dtbHelper.setRstData("info", rst.get("info"));
            flag = 1;
        }

        return flag;
    }

}
