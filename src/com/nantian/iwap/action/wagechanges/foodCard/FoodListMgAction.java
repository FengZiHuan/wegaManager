package com.nantian.iwap.action.wagechanges.foodCard;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nantian.iwap.app.exp.ExpDataFactory;
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
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.poi.poifs.crypt.EncryptionMode.standard;

/**
 * @since JDK 1.8 Copyright (c) 2016, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class FoodListMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(FoodListMgAction.class);
    private String encryptClazz = "com.nantian.iwap.app.util.DefaultEncrypt";// 默认加密方式
    private String insertSqlId;

    private static List<String> actionType = new ArrayList();

    static {
        actionType.add("insert");
    }


    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");
        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }
        if ("add".equals(option)) {
            return add(dtbHelper);
        }
        if ("save".equals(option) || "add".equals(option)) {
            return save(dtbHelper);
        }
        if ("import".equals(option) || "add".equals(option)) {
            return upload(dtbHelper);
        }
        if ("exportByPeople".equals(option)) {
            return exportByPeople(dtbHelper);
        }
        if ("exportByDept".equals(option)) {
            return exportByDept(dtbHelper);
        }

        if ("remove".equals(option)) {
            return remove(dtbHelper);
        }

        return 0;
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
                int no=Integer.parseInt(user);
                int i = dbBean.executeUpdate("delete from food_card   where id = ?", no);
                if (i == 1) {
                    s_cnt++;
                }
            }
            dbBean.executeCommit();
            if (u_cnt != s_cnt) {
                log.warn("删除失败" + (u_cnt - s_cnt) + "条");
                dtbHelper.setError("usermg-err-rm-001", "删除失败" + (u_cnt - s_cnt) + "条");
            } else {
                flag = 1;
            }
        } catch (Exception e) {
            log.error("删除数据出错", e);
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

    private int exportByDept(DTBHelper dtbHelper) {


        int flag = 0;
        DBAccessBean dbBean = DBAccessPool.getDbBean();
        List<Map<String, Object>> dataList = new ArrayList<>();
        //往期
        String batch = dtbHelper.getStringValue("batch");
        //本期
        if (batch.equals("")) {
            String selectSql = "select max(batch) from food_card";
            DataObject dataObject = dbBean.executeSingleQuery(selectSql);
            batch = dataObject.getValue("max(batch)");
        }

        String filetype = "xlsx";
        String titleString = dtbHelper.getStringValue("titleString");
        String selectSql = "SELECT name from FOOD_PROJECT ";
        List<Map<String, Object>> maps = dbBean.queryForList(selectSql);
        String name1 = "凉茶甜品";
        String name2 = "项目二";
        String name3 = "项目三";
        String name4 = "项目四";
        String name5 = "项目五";
        String name6 = "项目六";
        String name7 = "项目七";
        if (maps.get(0).get("NAME") != null) {
            name1 = maps.get(0).get("NAME").toString();
        }
        if (maps.get(1).get("NAME") != null) {
            name2 = maps.get(1).get("NAME").toString();
        }
        if (maps.get(2).get("NAME") != null) {
            name3 = maps.get(2).get("NAME").toString();
        }
        if (maps.get(3).get("NAME") != null) {
            name4 = maps.get(3).get("NAME").toString();
        }
        if (maps.get(4).get("NAME") != null) {
            name5 = maps.get(4).get("NAME").toString();
        }
        if (maps.get(5).get("NAME") != null) {
            name6 = maps.get(5).get("NAME").toString();
        }
        if (maps.get(6).get("NAME") != null) {
            name7 = maps.get(6).get("NAME").toString();
        }




        String strSql = "SELECT DEPT ,count(fmis) C,sum(tea_free) ST,sum(wash_free) SW,sum(free3) SF3,sum(free4) SF4,sum(free5) SF5,sum(free6) SF6,sum(free7) SF7 from (select * from FOOD_CARD where BATCH=?) GROUP BY dept   ";
        dataList = dbBean.queryForList(strSql, batch);
        List<String> titleId = new ArrayList<>();
        //导出顺序
        String[] t = new String[]{"NO","DEPT", "C", "ST", "SW", "SF3",  "SF4", "SF5", "SF6", "SF7"};
        for (int i = 0; i < t.length; i++) {
            titleId.add(t[i]);
        }
        int no = 1;
        for (Map<String, Object> map : dataList) {
            map.put("NO", no);
            no++;
        }
        //获取合计数据
        String num="";
        String sum1="";
        String sum2="";
        String sum3="";
        String sum4="";
        String sum5="";
        String sum6="";
        String sum7="";
        String dataSql="SELECT count(fmis) C,sum(tea_free) ST,sum(wash_free) SW,sum(free3) SF3,sum(free4) SF4,sum(free5) SF5,sum(free6) SF6,sum(free7) SF7 from (select * from FOOD_CARD where BATCH=?) ";
        List<Map<String, Object>> maps1 = dbBean.queryForList(dataSql, batch);
        for (Map<String, Object> map : maps1) {
            if (map.get("C")!=null){
                num=map.get("C").toString();
            }
            if (map.get("ST")!=null){
                sum1=map.get("ST").toString();
            } if (map.get("SW")!=null){
                sum2=map.get("SW").toString();
            } if (map.get("SF3")!=null){
                sum3=map.get("SF3").toString();
            } if (map.get("SF4")!=null){
                sum4=map.get("SF4").toString();
            } if (map.get("SF5")!=null){
                sum5=map.get("SF5").toString();
            } if (map.get("SF6")!=null){
                sum6=map.get("SF6").toString();
            }
            if (map.get("SF7")!=null){
                sum7=map.get("SF7").toString();
            }

        }

        String s = "overTime";
        String[] end = new String[]{"合计", "", num,sum1,sum2,sum3,sum4,sum5,sum6,sum7};
        String[] head0 = new String[]{"序号", "部门", "人数",name1,name2,name3,name4,name5,name6,name7};
        String[] head1 = new String[]{"序号", "部门", "人数",name1,name2,name3,name4,name5,name6,name7};

        String[] headnum0 = new String[]{"2,3,0,0", "2,3,1,1", "2,3,2,2", "2,3,3,3", "2,3,4,4", "2,3,5,5", "2,3,6,6", "2,3,7,7",
                "2,3,8,8", "2,3,9,9", "2,3,10,10", "2,3,11,11", };

        //对应excel中的和列，下表从0开始{"开始行,结束行,开始列,结束列"}
        ExpDataFactory edf = ExpDataFactory.getInstance();
        List<Map<String, String>> titleList = new ArrayList<Map<String, String>>();
        if (titleString != null && !"".equals(titleString.trim())) {
            try {
                titleList = JSONObject.parseObject(titleString, new TypeReference<List<Map<String, String>>>() {
                });
            } catch (Exception e) {
                log.warn("格式化自定义表格列名出错", e);
            }
        } else {
            log.warn("前端未传入表格字段数据titleString对象");
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        Map rst = new HashMap();
        try {
            Sheet sheet = workbook.createSheet();


            XSSFFont font = workbook.createFont();
            font.setFontName("仿宋_GB2312");
            font.setFontHeightInPoints((short) 15);
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

            XSSFFont font1 = workbook.createFont();
            font1.setFontName("仿宋_GB2312");
            font1.setFontHeightInPoints((short) 12);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font1);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            CellStyle fontcellStyle = workbook.createCellStyle();
            fontcellStyle.setFont(font);
            fontcellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            fontcellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);


            int maxLine = ExpDataFactory.MAX_LINE > dataList.size() ? dataList.size() : ExpDataFactory.MAX_LINE;
            if (dataList.size() > 0) {
                // 写入标题
                String[] split = batch.split("-");
                String year = split[0];
                String month = split[1];

                Row rowTitle = sheet.createRow(0);
                String[] th = new String[]{"省行本部在岗人员" + year + "年" + month + "月份汇总名单"};
                for (int i = 0; i < th.length; i++) {
                    Cell endCell = rowTitle.createCell(i);
                    endCell.setCellValue(th[i]);
                    sheet.addMergedRegion(new CellRangeAddress(0, 1,
                            0, 9));
                    endCell.setCellStyle(fontcellStyle);
                }


                rowTitle = sheet.createRow(2);
                Cell cell1 = rowTitle.createCell(0);
                for (int i = 0; i < head0.length; i++) {
                    cell1 = rowTitle.createCell(i);
                    cell1.setCellValue(head0[i]);
                    cell1.setCellStyle(cellStyle);
                }
                rowTitle = sheet.createRow(3);
                for (int i = 0; i < head1.length; i++) {
                    cell1 = rowTitle.createCell(i);
                    cell1.setCellValue(head1[i]);
                    cell1.setCellStyle(cellStyle);
                }
                for (int i = 0; i < headnum0.length; i++) {
                    String[] temp = headnum0[i].split(",");
                    Integer startrow = Integer.parseInt(temp[0]);
                    Integer overrow = Integer.parseInt(temp[1]);
                    Integer startcol = Integer.parseInt(temp[2]);
                    Integer overcol = Integer.parseInt(temp[3]);
                    sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                            startcol, overcol));
                }

                //列宽自动

                // 写入数据
                int rowCnt = 4;
                for (Map<String, Object> tmp : dataList) {
                    if (rowCnt > maxLine + 3) {
                        break;
                    }
                    Row row = sheet.createRow(rowCnt);
                    int cellCnt = 0;
                    for (String tId : titleId) {
                        Cell cell = row.createCell(cellCnt);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(tmp.get(tId) == null ? "" : tmp.get(tId).toString());
                        cellCnt++;
                    }
                    rowCnt++;
                }
                System.out.println(maxLine);
                System.out.println(rowCnt);

                //处理尾页excel
                Row foot = sheet.createRow(rowCnt);
                for (int i = 0; i < end.length; i++) {
                    Cell endCell = foot.createCell(i);
                    endCell.setCellValue(end[i]);
                    endCell.setCellStyle(cellStyle);
                    sheet.addMergedRegion(new CellRangeAddress(maxLine + 4, maxLine + 4,
                            0, 1));
                }


                Row last = sheet.createRow(rowCnt+1);
                System.out.println(rowCnt);
                Row trueEnd = sheet.createRow(rowCnt+2);
                String[] endArr = new String[]{"制表:", "", "审核:", "", "", "",  "审批:", "", "日期:", ""};
                for (int i = 0; i < endArr.length; i++) {
                    Cell endCell = trueEnd.createCell(i);
                    endCell.setCellValue(endArr[i]);
                    endCell.setCellStyle(cellStyle);
                }
                System.out.println(rowCnt);


            }
            rst.put("info", workbook);
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

    private int exportByPeople(DTBHelper dtbHelper) {

        int flag = 0;
        DBAccessBean dbBean = DBAccessPool.getDbBean();
        List<Map<String, Object>> dataList = new ArrayList<>();
        //往期
        String batch = dtbHelper.getStringValue("batch");
        //本期
        if (batch.equals("")) {
            String selectSql = "select max(batch) from food_card";
            DataObject dataObject = dbBean.executeSingleQuery(selectSql);
            batch = dataObject.getValue("max(batch)");
        }
        String sqlStr = "select DEPT,NAME,PEOPLENO,TEA_FREE,WASH_FREE,FREE3,FREE4,FREE5,FREE6,FREE7,REMARK from food_card where batch=?";
        dataList = dbBean.queryForList(sqlStr, batch);
        //添加序号
        int no = 1;
        for (Map<String, Object> map : dataList) {
            map.put("NO", no);
            no++;
        }
        String filetype = "xlsx";
        String titleString = dtbHelper.getStringValue("titleString");
        //导出表格顺序
        List<String> titleId = new ArrayList<>();
        String[] t = new String[]{"NO", "DEPT", "NAME", "PEOPLENO", "TEA_FREE", "WASH_FREE", "FREE3", "FREE4", "FREE5", "FREE6", "FREE7", "REMARK"};
        for (int i = 0; i < t.length; i++) {
            titleId.add(t[i]);
        }
        //获取项目名字。
        String selectSql = "SELECT name from FOOD_PROJECT ";
        List<Map<String, Object>> maps = dbBean.queryForList(selectSql);
        String name1 = "凉茶甜品";
        String name2 = "项目二";
        String name3 = "项目三";
        String name4 = "项目四";
        String name5 = "项目五";
        String name6 = "项目六";
        String name7 = "项目七";
        if (maps.get(0).get("NAME") != null) {
            name1 = maps.get(0).get("NAME").toString();
        }
        if (maps.get(1).get("NAME") != null) {
            name2 = maps.get(1).get("NAME").toString();
        }
        if (maps.get(2).get("NAME") != null) {
            name3 = maps.get(2).get("NAME").toString();
        }
        if (maps.get(3).get("NAME") != null) {
            name4 = maps.get(3).get("NAME").toString();
        }
        if (maps.get(4).get("NAME") != null) {
            name5 = maps.get(4).get("NAME").toString();
        }
        if (maps.get(5).get("NAME") != null) {
            name6 = maps.get(5).get("NAME").toString();
        }
        if (maps.get(6).get("NAME") != null) {
            name7 = maps.get(6).get("NAME").toString();
        }
        //获取统计
        String sumTea_free = "";
        String sumWash_free = "";
        String sumFree3 = "";
        String sumFree4 = "";
        String sumFree5 = "";
        String sumFree6 = "";
        String sumFree7 = "";

        String strSql = "select sum(tea_free) ST,sum(wash_free) SW,sum(free3) SF3,sum(free4) SF4,sum(free5) SF5,sum(free6) SF6,sum(free7) SF7  from food_card where batch=? ";
        List<Map<String, Object>> mapList = dbBean.queryForList(strSql, batch);
        for (Map<String, Object> map : mapList) {
            if (map.get("ST") != null) {
                sumTea_free = map.get("ST").toString();
            }
            if (map.get("SW") != null) {
                sumWash_free = map.get("SW").toString();
            }
            if (map.get("SF3") != null) {
                sumFree3 = map.get("SF3").toString();
            }

            if (map.get("SF4") != null) {
                sumFree4 = map.get("SF4").toString();
            }
            if (map.get("SF5") != null) {
                sumFree5 = map.get("SF5").toString();
            }
            if (map.get("SF6") != null) {
                sumFree3 = map.get("SF6").toString();
            }
            if (map.get("SF7") != null) {
                sumFree3 = map.get("SF7").toString();
            }


        }

        String s = "overTime";
        String[] end = new String[]{"合计", "", "", "", sumTea_free, sumWash_free, sumFree3, sumFree4, sumFree5, sumFree6, sumFree7};
        String[] head0 = new String[]{"序号", "部门", "姓名", "人员编号", name1, name2, name3, name4, name5, name6, name7, "备注"};
        String[] head1 = new String[]{"序号", "部门", "姓名", "人员编号", name1, name2, name3, name4, name5, name6, name7, "备注"};

        String[] headnum0 = new String[]{"2,3,0,0", "2,3,1,1", "2,3,2,2", "2,3,3,3", "2,3,4,4", "2,3,5,5", "2,3,6,6", "2,3,7,7",
                "2,3,8,8", "2,3,9,9", "2,3,10,10", "2,3,11,11", "2,3,12,12"};

        //对应excel中的和列，下表从0开始{"开始行,结束行,开始列,结束列"}
        ExpDataFactory edf = ExpDataFactory.getInstance();
        List<Map<String, String>> titleList = new ArrayList<Map<String, String>>();
        if (titleString != null && !"".equals(titleString.trim())) {
            try {
                titleList = JSONObject.parseObject(titleString, new TypeReference<List<Map<String, String>>>() {
                });
            } catch (Exception e) {
                log.warn("格式化自定义表格列名出错", e);
            }
        } else {
            log.warn("前端未传入表格字段数据titleString对象");
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        Map rst = new HashMap();
        try {
            Sheet sheet = workbook.createSheet();


            XSSFFont font = workbook.createFont();
            font.setFontName("仿宋_GB2312");
            font.setFontHeightInPoints((short) 15);
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

            XSSFFont font1 = workbook.createFont();
            font1.setFontName("仿宋_GB2312");
            font1.setFontHeightInPoints((short) 12);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font1);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            CellStyle fontcellStyle = workbook.createCellStyle();
            fontcellStyle.setFont(font);
            fontcellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            fontcellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);


            int maxLine = ExpDataFactory.MAX_LINE > dataList.size() ? dataList.size() : ExpDataFactory.MAX_LINE;
            if (dataList.size() > 0) {
                // 写入标题
                String[] split = batch.split("-");
                String year = split[0];
                String month = split[1];

                Row rowTitle = sheet.createRow(0);
                String[] th = new String[]{"省行本部在岗人员" + year + "年" + month + "月份充值名单"};
                for (int i = 0; i < th.length; i++) {
                    Cell endCell = rowTitle.createCell(i);
                    endCell.setCellValue(th[i]);
                    sheet.addMergedRegion(new CellRangeAddress(0, 1,
                            0, 11));
                    endCell.setCellStyle(fontcellStyle);
                }


                rowTitle = sheet.createRow(2);
                Cell cell1 = rowTitle.createCell(0);
                for (int i = 0; i < head0.length; i++) {
                    cell1 = rowTitle.createCell(i);
                    cell1.setCellValue(head0[i]);
                    cell1.setCellStyle(cellStyle);
                }
                rowTitle = sheet.createRow(3);
                for (int i = 0; i < head1.length; i++) {
                    cell1 = rowTitle.createCell(i);
                    cell1.setCellValue(head1[i]);
                    cell1.setCellStyle(cellStyle);
                }
                for (int i = 0; i < headnum0.length; i++) {
                    String[] temp = headnum0[i].split(",");
                    Integer startrow = Integer.parseInt(temp[0]);
                    Integer overrow = Integer.parseInt(temp[1]);
                    Integer startcol = Integer.parseInt(temp[2]);
                    Integer overcol = Integer.parseInt(temp[3]);
                    sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                            startcol, overcol));
                }

                //列宽自动

                // 写入数据
                int rowCnt = 4;
                for (Map<String, Object> tmp : dataList) {
                    if (rowCnt > maxLine + 3) {
                        break;
                    }
                    Row row = sheet.createRow(rowCnt);
                    int cellCnt = 0;
                    for (String tId : titleId) {
                        Cell cell = row.createCell(cellCnt);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(tmp.get(tId) == null ? "" : tmp.get(tId).toString());
                        cellCnt++;
                    }
                    rowCnt++;
                }
                System.out.println(maxLine);
                System.out.println(rowCnt);

                //处理尾页excel
                Row foot = sheet.createRow(rowCnt);
                for (int i = 0; i < end.length; i++) {
                    Cell endCell = foot.createCell(i);
                    endCell.setCellValue(end[i]);
                    endCell.setCellStyle(cellStyle);
                    sheet.addMergedRegion(new CellRangeAddress(maxLine + 4, maxLine + 4,
                            0, 3));
                }


                Row last = sheet.createRow(rowCnt+1);
                System.out.println(rowCnt);
                Row trueEnd = sheet.createRow(rowCnt+2);
                String[] endArr = new String[]{"制表:", "", "审核:", "", "", "", "", "", "审批:", "", "日期:", ""};
                for (int i = 0; i < endArr.length; i++) {
                    Cell endCell = trueEnd.createCell(i);
                    endCell.setCellValue(endArr[i]);
                    endCell.setCellStyle(cellStyle);
                }
                System.out.println(rowCnt);


            }
            rst.put("info", workbook);
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


    private int add(DTBHelper dtbHelper) {

        int flag = 0;
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
            String selectSql = "SELECT name from FOOD_PROJECT ";
            List<Map<String, Object>> maps = dbBean.queryForList(selectSql);
            String name1 = "项目1";
            String name2 = "项目2";
            String name3 = "项目3";
            String name4 = "项目4";
            String name5 = "项目5";
            String name6 = "项目6";
            String name7 = "项目7";
            if (maps.get(0).get("NAME") != null) {
                name1 = maps.get(0).get("NAME").toString();
            }
            if (maps.get(1).get("NAME") != null) {
                name2 = maps.get(1).get("NAME").toString();
            }
            if (maps.get(2).get("NAME") != null) {
                name3 = maps.get(2).get("NAME").toString();
            }
            if (maps.get(3).get("NAME") != null) {
                name4 = maps.get(3).get("NAME").toString();
            }
            if (maps.get(4).get("NAME") != null) {
                name5 = maps.get(4).get("NAME").toString();
            }
            if (maps.get(5).get("NAME") != null) {
                name6 = maps.get(5).get("NAME").toString();
            }
            if (maps.get(6).get("NAME") != null) {
                name7 = maps.get(6).get("NAME").toString();
            }
            dtbHelper.setRstData("name1", name1);
            dtbHelper.setRstData("name2", name2);
            dtbHelper.setRstData("name3", name3);
            dtbHelper.setRstData("name4", name4);
            dtbHelper.setRstData("name5", name5);
            dtbHelper.setRstData("name6", name6);
            dtbHelper.setRstData("name7", name7);
            String name = dtbHelper.getStringValue("userName");
            String depts = dtbHelper.getStringValue("depts");
            String batch = dtbHelper.getStringValue("batch");
            if (batch.equals("")) {
                String strSql = "select max(batch) from food_card";
                DataObject dataObject = dbBean.executeSingleQuery(strSql);
                batch = dataObject.getValue("max(batch)");
            }
            String sql = "select * from FOOD_CARD where batch=? ";


            if (!"".equals(name)) {
                sql += " and name like '%" + name + "%'";
            }
            if (!"".equals(depts)) {
                sql += " and dept = '" + depts + "'";
            }
            sql += " order by id ";
            dataList = dbBean.queryForList(sql, batch);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("total", page.getTotalCount());
            flag = 1;
        } catch (Exception e) {
            log.error("用户查询出错", e);
            dtbHelper.setError("usermg-err-qry", "[用户查询出错]" + e.getMessage());
        }
        return flag;
    }

    protected int save(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String id = dtbHelper.getStringValue("id");
            String tea_free = dtbHelper.getStringValue("TEA_FREE");
            String wash_free = dtbHelper.getStringValue("WASH_FREE");
            String free3 = dtbHelper.getStringValue("FREE3");
            String free4 = dtbHelper.getStringValue("FREE4");
            String free5 = dtbHelper.getStringValue("FREE5");
            String free6 = dtbHelper.getStringValue("FREE6");
            String free7 = dtbHelper.getStringValue("FREE7");
            String updateSql = "update FOOD_CARD set TEA_FREE=?,WASH_FREE=?,FREE3=?,FREE4=?,FREE5=?,FREE6=?,FREE7=?  where id=?";
            dbBean.executeUpdate(updateSql, tea_free, wash_free, free3, free4, free5, free6, free7, id);
            dbBean.executeCommit();
            flag = 1;
        } catch (Exception e) {
            log.error("修改出错", e);
            dtbHelper.setError("usermg-err-add-002", "修改出错" + e.getMessage());
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
            if (imp_id == null || "".equals(imp_id.toString().trim())) {
                log.error("导入数据类型ID不能为空");
                dtbHelper.setError("importdata-err-002", "导入数据类型ID不能为空");
                return flag;
            }
            ImpDataFactory idf = ImpDataFactory.getInstance();
            Map config = idf.getConfig();
            String table = "FOOD_CARD";
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
                    String fmis = value.get("fmis").toString();
                    String name = value.get("name").toString();
                    String dept = value.get("dept").toString();
                    String tea_free = null;
                    if (value.get("tea_free") != null) {
                        tea_free = value.get("tea_free").toString();
                    }
                    String wash_free = null;
                    if (value.get("wash_free") != null) {
                        wash_free = value.get("wash_free").toString();
                    }
                    String remark = null;
                    if (value.get("remark") != null) {
                        remark = value.get("remark").toString();
                    }
                    String free3 = null;
                    if (value.get("free3") != null) {
                        free3 = value.get("free3").toString();
                    }
                    String free4 = null;
                    if (value.get("free4") != null) {
                        free4 = value.get("free4").toString();
                    }
                    String free5 = null;
                    if (value.get("free5") != null) {
                        free5 = value.get("free5").toString();
                    }
                    String free6 = null;
                    if (value.get("free6") != null) {
                        free6 = value.get("free6").toString();
                    }
                    String free7 = null;
                    if (value.get("free7") != null) {
                        free4 = value.get("free7").toString();
                    }


                    String secSql = "select count(*) from FOOD_CARD where fmis=?";
                    Boolean exist = dbBean.queryForInt(secSql, fmis) > 0;
                    //满足条件覆盖
                    if (!exist) {
                        try {
                            String insertsql = "insert into FOOD_CARD(name,fmis,dept,tea_free,wash_free,remark,free3,free4,free5,free6,free7) values (?,?,?,?,?,?,?,?,?,?,?)";
                            dbBean.executeUpdate(insertsql, name, fmis, dept, tea_free, wash_free, remark, free3, free4, free5, free6, free7);
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
                            String updateSql = "update  FOOD_CARD set name=?,dept=?,tea_free=?,wash_free=?,remark=?,free3=?,free4=?,free5=?,free6=?,free7=?  where fmis=?";
                            dbBean.executeUpdate(updateSql, name, dept, tea_free, wash_free, remark, free3, free4, free5, free6, free7, fmis);
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
