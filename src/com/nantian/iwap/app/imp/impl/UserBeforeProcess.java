package com.nantian.iwap.app.imp.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.nantian.iwap.app.imp.IBeforeProcess;


public class UserBeforeProcess implements IBeforeProcess {

	private static Logger log = Logger.getLogger(UserBeforeProcess.class);

	@SuppressWarnings("rawtypes")
	@Override
	public boolean process(Map config, Object dataFile) {
		boolean flag = false;
		try {

			String defaultPwd = "123456";

			Sheet sheet = (Sheet) dataFile;
			Cell userCell = null;
			Cell pwdCell = null;
			short bgClr = -1;
			short border = 0;
			if ("0".equals(config.get("impTp"))) {
				int rowSum = sheet.getLastRowNum();
				int startIdx = Integer.valueOf(config.get("startIdx").toString());
				int startCol = Integer.valueOf(config.get("startCol").toString());
				for (; startIdx <= rowSum; startIdx++) {
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
					userCell = row.getCell(0);
					pwdCell = row.getCell(1);
					if (userCell != null && pwdCell != null) {
						String user = getCellValue(userCell, sheet.getWorkbook());
						String pwd = getCellValue(pwdCell, sheet.getWorkbook());
						if (pwd == null || "".equals(pwd.trim())) {
							pwd = defaultPwd;
						}

						pwdCell.setCellValue(pwd);
					}
				}
			} else {
				List cfgDtl = (List) config.get("dtl");
				for (int i = 0; i < cfgDtl.size(); i++) {
					Map fieldCfg = (Map) cfgDtl.get(i);
					if (fieldCfg.get("fldNm") != null && "acct_id".equals(fieldCfg.get("fldNm").toString())) {
						if (fieldCfg.get("defVal") != null) {
							String setVal = (String) fieldCfg.get("defVal");
							int r = Integer.parseInt(setVal.substring(1)) - 1;
							setVal = setVal.toUpperCase();
							Row row = sheet.getRow(r);
							int col = (int) setVal.charAt(0) - 65;
							userCell = row.getCell(col);
						}
					}
					if (fieldCfg.get("fldNm") != null && "acct_pwd".equals(fieldCfg.get("fldNm").toString())) {
						if (fieldCfg.get("defVal") != null) {
							String setVal = (String) fieldCfg.get("defVal");
							int r = Integer.parseInt(setVal.substring(1)) - 1;
							setVal = setVal.toUpperCase();
							Row row = sheet.getRow(r);
							int col = (int) setVal.charAt(0) - 65;
							pwdCell = row.getCell(col);
						}
					}
				}
				if (userCell != null && pwdCell != null) {
					String user = getCellValue(userCell, sheet.getWorkbook());
					String pwd = getCellValue(pwdCell, sheet.getWorkbook());
					if (pwd == null || "".equals(pwd.trim())) {
						pwd = defaultPwd;
					}

					pwdCell.setCellValue(pwd);
				}
			}
			flag = true;
		} catch (Exception e) {
			log.error("导入前处理出错", e);
		}
		return flag;
	}

	private static String getCellValue(Cell cell, Workbook workbook) {
		String rst = null;
		Date dateRst = null;// 日期格式取值
		int type = cell.getCellType();
		switch (type) {
		case Cell.CELL_TYPE_STRING:
			rst = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			// 添加日期格式判断 by lihl 2014-05-06 10:36:16
			if (HSSFDateUtil.isCellDateFormatted(cell)) {// 若是日期格式，则先获取日期再转换成字符串
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
			}
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
}
