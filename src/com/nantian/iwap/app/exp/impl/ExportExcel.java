package com.nantian.iwap.app.exp.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;


import com.nantian.iwap.app.exp.ExpDataFactory;
import com.nantian.iwap.app.exp.ExportData;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 实现Excel依照数据库查询语句导出
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExportExcel implements ExportData {
	private static Logger log = Logger.getLogger(ExportExcel.class);
	/**
	 * 获取标题数据
	 *
	 * @param dbBean
	 * @param dataList
	 * @param titleList
	 *            [{key1:val1},{key2:val2}]
	 * @return {"titleId":List<String>,"titleNm":String[]}
	 */
	public Map getTitles(DBAccessBean dbBean, List<Map<String, Object>> dataList, List<Map<String, String>> titleList)
			throws Exception {
		Map titles = null;
		String sqlStr = "select di_id,di_title from sys_data_item where di_id in";
		List<String> titleId = new ArrayList<String>();
		String[] titleNm = null;
		try {
			StringBuffer titleBf = new StringBuffer();
			Map tnm_tl = new HashMap();
			if (titleList.size() > 0) {
				titleNm = new String[titleList.size()];
				for (Map<String, String> tMap : titleList) {
					Set keys = tMap.keySet();
					Iterator iterator = keys.iterator();
					while (iterator.hasNext()) {
						String key = iterator.next().toString();
						titleId.add(key);
						titleBf.append("?,");
						String value=tMap.get(key);
						String titleName=new String(value.getBytes("ISO-8859-1"),"utf-8");
						tnm_tl.put(key, titleName);
					}
				}
			} else {
				Map<String, Object> tMap = dataList.get(0);
				titleNm = new String[tMap.size()];
				Set keys = tMap.keySet();
				Iterator iterator = keys.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					titleId.add(key);
					titleBf.append("?,");
				}
			}

			sqlStr += "(" + titleBf.substring(0, titleBf.length() - 1) + ")";
			List<Map<String, Object>> dList = dbBean.queryForList(sqlStr, titleId.toArray());
			Map tnm_dl = new HashMap();
			for (Map<String, Object> dMap : dList) {
				tnm_dl.put(dMap.get("di_id"), dMap.get("di_title"));
			}
			int i = 0;
			for (String t : titleId) {
				titleNm[i] = (tnm_dl.get(t) != null && !"".equals(tnm_dl.get(t).toString().trim()))
						? tnm_dl.get(t).toString()
						: (tnm_tl.get(t) != null && !"".equals(tnm_tl.get(t).toString().trim()))
								? tnm_tl.get(t).toString() : t;
				i++;
			}
			titles = new HashMap();
			titles.put("titleId", titleId);
			titles.put("titleNm", titleNm);
		} catch (Exception e) {
			log.error("获取标题数据出错", e);
		}
		return titles;

	}

	/**
	 * 导出2007之前的版本
	 *
	 * @param dataList
	 * @return
	 */
	private Map dbToXls(List dataList, List<Map<String, String>> titleList) {
		Map rst = new HashMap();
		try {
			Workbook workbook = new HSSFWorkbook();
			rst = expData(workbook, dataList, titleList);
		} catch (Exception e) {
			log.error("导出Xls文件出错", e);
			rst.put("msg", "导出Xls文件出错:" + e.getMessage());
		}
		return rst;
	}

	/**
	 * 导出2007之后的版本
	 * 
	 * @param dataList
	 * @return
	 */
	private Map dbToXlsx(List dataList, List<Map<String, String>> titleList) {
		Map rst = new HashMap();
		try {
			Workbook workbook = new XSSFWorkbook();
			rst = expData(workbook, dataList, titleList);
		} catch (Exception e) {
			log.error("导出Xlsx文件出错", e);
			rst.put("msg", "导出Xlsx文件出错:" + e.getMessage());
		}
		return rst;
	}




	/**
	 * 导出数据
	 *
	 * @param workbook
	 * @param dataList
	 * @param titleList
	 * @return
	 */



    private Map expData(Workbook workbook, List<Map<String, Object>> dataList, List<Map<String, String>> titleList) {
        Map rst = new HashMap();
        try {
        	DBAccessPool.createDbBean();
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            Sheet sheet = workbook.createSheet();
            //设置标题和单元格样式
            XSSFCellStyle columnTopStyle = getColumnTopStyleAfter(workbook);  //获取列头样式对象
            XSSFCellStyle style = getStyleAfter(workbook);                    //单元格样式对象
			int maxLine = ExpDataFactory.MAX_LINE > dataList.size() ? dataList.size() : ExpDataFactory.MAX_LINE;
            if (dataList.size() > 0) {
                Map titles = this.getTitles(dbBean, dataList, titleList);
                String[] titleNm = null;
                List<String> titleId = null;
                if (titles == null || titles.get("titleNm") == null || titles.get("titleId") == null) {
                    rst.put("msg", "导出数据出错:获取标题数据出错");
                    return rst;
                } else {
                    titleNm = (String[]) titles.get("titleNm");
                    titleId = (List<String>) titles.get("titleId");
                }

                // 写入标题

                int cellCnt = 0;
                Row rowTitle = sheet.createRow(0);
                for (String tNm : titleNm) {
                    Cell cell = rowTitle.createCell(cellCnt);
                    cell.setCellStyle(columnTopStyle);
                    cell.setCellValue(tNm);
                    cellCnt++;
                }

                // 写入数据
                int rowCnt = 1;
                for (Map<String, Object> tmp : dataList) {
                    if (rowCnt > maxLine) {
                        break;
                    }

                    Row row = sheet.createRow(rowCnt);
                    cellCnt = 0;
                    for (String tId : titleId) {
                        Cell cell = row.createCell(cellCnt);
                        cell.setCellStyle(style);
                        cell.setCellValue(tmp.get(tId) == null ? "" : tmp.get(tId).toString());
                        cellCnt++;
                    }
                    rowCnt++;
                }
            }

            rst.put("info", workbook);
        } catch (Exception e) {
            log.error("导出数据出错", e);
            rst.put("msg", "导出数据出错:" + e.getMessage());
        } finally {
            DBAccessPool.releaseDbBean();
        }
        return rst;
    }


	@Override
	public Map exportData(String fileExt, List dataList, List<Map<String, String>> titleLis) {
		if (fileExt.endsWith("xls")) {
			return dbToXls(dataList,titleLis);
		} else {
			return dbToXlsx(dataList, titleLis);
		}
	}



	@Override
	public Map exportData1(String fileExt, List dataList, List<Map<String, String>> titleList,String s) {
		if (fileExt.endsWith("xls")) {
			return dbToXls(dataList, titleList);
		} else {
			return dbToXlsx1(dataList, titleList,s);
		}
	}

	private Map dbToXlsx1(List dataList, List<Map<String, String>> titleList ,String s) {
		Map rst = new HashMap();
		try {
			Workbook workbook = new XSSFWorkbook();
			if (s.equals("1")){
				rst = daochu(workbook, dataList, titleList);
			}

			if (s.equals("2")){
				rst = ygz(workbook, dataList, titleList);
			}
			if (s.equals("3")){
				rst = year(workbook, dataList, titleList);
			}

		} catch (Exception e) {
			log.error("导出Xlsx文件出错", e);
			rst.put("msg", "导出Xlsx文件出错:" + e.getMessage());
		}
		return rst;
	}


	private Map daochu(Workbook workbook, List<Map<String, Object>> dataList, List<Map<String, String>> titleList) {
		Map rst = new HashMap();
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			Sheet sheet = workbook.createSheet();
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
			int maxLine = ExpDataFactory.MAX_LINE > dataList.size() ? dataList.size() : ExpDataFactory.MAX_LINE;
			if (dataList.size() > 0) {
				Map titles = this.getTitles(dbBean, dataList, titleList);
				String[] titleNm = null;
				List<String> titleId = new ArrayList<>();
				String[] t= new String[] {
						"NH_DEPT","NH_FMIS", "NH_NAME",  "NH_ZGWGZ", "NH_ZJXGZ", "NH_ZJTBT",  "NH_QINGTYPE" , "NH_XIUJIAYEAR", "NH_SUMDAYS", "NH_TGWGZ" ,
						"NH_TJXGZ", "NH_TJTBTD", "NH_GYTF", "NH_GYITF", "NH_GNXUTF" , "NH_JYTF", "NH_JYITF", "NH_JNXUTF", "NH_BYTF", "NH_BYTIF", "NH_BNXUTF" };

				for (int i = 0; i < t.length; i++) {
					titleId.add(t[i]);
				}

				if (titles == null || titles.get("titleNm") == null || titles.get("titleId") == null) {
					rst.put("msg", "导出数据出错:获取标题数据出错");
					return rst;
				} else {
					titleNm = (String[]) titles.get("titleNm");
					//titleId = (List<String>) titles.get("titleId");
				}
				// 写入标题
				Row rowTitle = sheet.createRow(0);
				Cell cell1 =  rowTitle.createCell(0);
				String[] head0 = new String[] { "部门", "FMIS员工号",
						"姓名","正常标准","正常标准","正常标准", "休假类型", "休假年份","休假天数(累计)",
						"停发天数（工作日）","当年停发岗位工资","当年停发岗位工资","当年停发岗位工资",
						"当年停发绩效工资","当年停发绩效工资","当年停发绩效工资","当年停发交通补贴","当年停发交通补贴","当年停发交通补贴"};
				String[] head1 = new String[] { "部门", "FMIS员工号",
						"姓名","岗位工资", "预发绩效", "交通补贴", "休假类型", "休假年份","休假天数(累计)",
						"岗位工资", "绩效工资", "交通补贴应扣天数","应停发数", "已停发数", "年末还需停发",
						"应停发数", "已停发数", "年末还需停发","应停发数", "已停发数","年末还需停发"};
				String[] headnum0 = new String[] { "0,1,0,0", "0,1,1,1", "0,1,2,2",
						"0,0,3,5", "0,1,6,6", "0,1,7,7" ,"0,1,8,8","0,0,9,11","0,0,12,14",
						"0,0,15,17","0,0,18,20"};//对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}

				for (int i = 0; i < head0.length; i++) {
					cell1 = rowTitle.createCell(i);
					cell1.setCellValue(head0[i]);
					cell1.setCellStyle(cellStyle);
				}
				rowTitle = sheet.createRow(1);
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

				//让列宽随着导出的列长自动适应
				for (int colNum = 0; colNum < head1.length; colNum++) {
					int columnWidth = sheet.getColumnWidth(colNum) / 256;
					for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
						Row currentRow;
						//当前行未被使用过
						if (sheet.getRow(rowNum) == null) {
							currentRow = sheet.createRow(rowNum);
						} else {
							currentRow = sheet.getRow(rowNum);
						}
						if (currentRow.getCell(colNum) != null) {
							//取得当前的单元格
							Cell currentCell = currentRow.getCell(colNum);
							//如果当前单元格类型为字符串
							if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
								int length = currentCell.getStringCellValue().getBytes().length;
								if (columnWidth < length) {
									//将单元格里面值大小作为列宽度
									columnWidth = length;
								}
							}
						}
					}
					//再根据不同列单独做下处理
					if (colNum == 0) {
						sheet.setColumnWidth(colNum, (columnWidth ) * 256);
					} else {
						sheet.setColumnWidth(colNum, (columnWidth + 5) * 256);
					}
				}
				// 写入数据
				int rowCnt = 2;
				for (Map<String, Object> tmp : dataList) {
					if (rowCnt > maxLine+1) {
						break;
					}

					Row row= sheet.createRow(rowCnt);

					int cellCnt = 0;
					for (String tId : titleId) {
						Cell cell = row.createCell(cellCnt);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(tmp.get(tId) == null ? "" : tmp.get(tId).toString());
						cellCnt++;
					}
					rowCnt++;
				}
			}

			rst.put("info", workbook);
		} catch (Exception e) {
			log.error("导出数据出错", e);
			rst.put("msg", "导出数据出错:" + e.getMessage());
		} finally {
			DBAccessPool.releaseDbBean();
		}
		return rst;
	}





	private Map ygz(Workbook workbook, List<Map<String, Object>> dataList, List<Map<String, String>> titleList) {
		Map rst = new HashMap();
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			Sheet sheet = workbook.createSheet();
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
			int maxLine = ExpDataFactory.MAX_LINE > dataList.size() ? dataList.size() : ExpDataFactory.MAX_LINE;
			if (dataList.size() > 0) {
				Map titles = this.getTitles(dbBean, dataList, titleList);
				String[] titleNm = null;
				List<String> titleId = new ArrayList<>();
				String[] t= new String[] {
						"NH_NO","NH_DEPT","NH_NAME","NH_FMIS","NH_QINGTYPE","NH_ZGWGZ","NH_ZJXGZ","NH_ZJTBT",
						"NH_DTGZ","NH_DTJX","NH_DTCHE","NH_DFGZ","NH_DFJX","NH_DFCHE","NH_BEIZHU","NH_ZNY" };
				for (int i = 0; i < t.length; i++) {
					titleId.add(t[i]);
				}
				if (titles == null || titles.get("titleNm") == null || titles.get("titleId") == null) {
					rst.put("msg", "导出数据出错:获取标题数据出错");
					return rst;
				} else {
					titleNm = (String[]) titles.get("titleNm");
					//titleId = (List<String>) titles.get("titleId");
				}
				// 写入标题
				Row rowTitle = sheet.createRow(0);
				Cell cell1 =  rowTitle.createCell(0);
				String[] head0 = new String[] {"序号","部门","姓名","FMIS员工号","休假类型","正常情况","正常情况","正常情况","本月扣发","本月扣发","本月扣发",
						"本月发放情况","本月发放情况","本月发放情况","备注","执行月份"};
				String[] head1 = new String[] {"序号","部门","姓名","FMIS员工号","休假类型","岗位工资","预发绩效","交通补贴"
						,"岗位工资","预发绩效","交通补贴","岗位工资","预发绩效","交通补贴","备注","执行月份"};
				String[] headnum0 = new String[] { "0,1,0,0", "0,1,1,1", "0,1,2,2","0,1,3,3","0,1,4,4",
						"0,0,5,7", "0,0,8,10", "0,0,11,13" ,"0,1,14,14","0,1,15,15"
				};//对应excel中的行和列，下表从0开始{"开始行,结束行,开始列,结束列"}
				for (int i = 0; i < head0.length; i++) {
					cell1 = rowTitle.createCell(i);
					cell1.setCellValue(head0[i]);
					cell1.setCellStyle(cellStyle);
				}
				rowTitle = sheet.createRow(1);
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
				//让列宽随着导出的列长自动适应
				for (int colNum = 0; colNum < head1.length; colNum++) {
					int columnWidth = sheet.getColumnWidth(colNum) / 256;
					for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
						Row currentRow;
						//当前行未被使用过
						if (sheet.getRow(rowNum) == null) {
							currentRow = sheet.createRow(rowNum);
						} else {
							currentRow = sheet.getRow(rowNum);
						}


						if (currentRow.getCell(colNum) != null) {
							//取得当前的单元格
							Cell currentCell = currentRow.getCell(colNum);
							//如果当前单元格类型为字符串
							if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
								int length = currentCell.getStringCellValue().getBytes().length;
								if (columnWidth < length) {
									//将单元格里面值大小作为列宽度
									columnWidth = length;
								}
							}
						}
					}
					//再根据不同列单独做下处理
					if (colNum == 0) {
						sheet.setColumnWidth(colNum, (columnWidth ) * 256);
					} else {
						sheet.setColumnWidth(colNum, (columnWidth + 5) * 256);
					}
				}
				// 写入数据
				int rowCnt = 2;
				for (Map<String, Object> tmp : dataList) {
					if (rowCnt > maxLine+1) {
						break;
					}

					Row row= sheet.createRow(rowCnt);

					int cellCnt = 0;
					for (String tId : titleId) {
						Cell cell = row.createCell(cellCnt);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(tmp.get(tId) == null ? "" : tmp.get(tId).toString());
						cellCnt++;
					}
					rowCnt++;
				}
			}

			rst.put("info", workbook);
		} catch (Exception e) {
			log.error("导出数据出错", e);
			rst.put("msg", "导出数据出错:" + e.getMessage());
		} finally {
			DBAccessPool.releaseDbBean();
		}
		return rst;
	}



	//按年导出
	private Map year(Workbook workbook, List<Map<String, Object>> dataList, List<Map<String, String>> titleList) {
		Map rst = new HashMap();
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			Sheet sheet = workbook.createSheet();
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
			int maxLine = ExpDataFactory.MAX_LINE > dataList.size() ? dataList.size() : ExpDataFactory.MAX_LINE;

            if (dataList.size() > 0) {
				Map titles = this.getTitles(dbBean, dataList, titleList);
				String[] titleNm = null;
				List<String> titleId = new ArrayList<>();

				String[] t= new String[] {
						"NH_NO","NH_FMIS","NH_DEPT","NH_NAME","NH_ZGWGZ","NH_ZJXGZ","NH_ZJTBT","NH_QINGTYPE","NH_XIUJIAYEAR","NH_SUMDAYS","NH_TGWGZ","NH_TJXGZ","NH_TJTBTD","NH_GYTF","NH_GYITF","NH_GNXUTF","NH_JYTF",
						"NH_JYITF","NH_JNXUTF","NH_BYTF", "NH_BYTIF","NH_BNXUTF","NH_ONETGZ","NH_ONETJX","NH_ONETCHE","NH_TWOTGZ","NH_TWOTJX",
						"NH_TWOTCHE","NH_THTGZ","NH_THTJX","NH_THTCHE","NH_FOTGZ","NH_FOTJX","NH_FOTCHE","NH_FIVETGZ","NH_FIVETJX","NH_FIVETCHE",
						"NH_SIXTGZ","NH_SIXTJX","NH_SIXTCHE","NH_SETGZ","NH_SETJX","NH_SETCHE","NH_AGETGZ","NH_AGETJX","NH_AGETCHE","NH_NITGZ",
						"NH_NITJX","NH_NITCHE","NH_TENTGZ","NH_TENTJX","NH_TENTCHE","NH_ETGZ","NH_ETJX","NH_ETCHE","NH_TTGZ","NH_TTJX",
						"NH_TTCHE","NH_NTGZ","NH_NTJX","NH_NTCHE","NH_YTGZ","NH_YTJX","NH_YTCHE" };


				for (int i = 0; i < t.length; i++) {
					titleId.add(t[i]);
				}
				if (titles == null || titles.get("titleNm") == null || titles.get("titleId") == null) {
					rst.put("msg", "导出数据出错:获取标题数据出错");
					return rst;
				} else {
					titleNm = (String[]) titles.get("titleNm");
					//titleId = (List<String>) titles.get("titleId");
				}
				// 写入标题
				Row rowTitle = sheet.createRow(0);
				Cell cell1 =  rowTitle.createCell(0);
				String[] head0 = new String[] {"序号","FMIS员工号","部门","姓名","正常标准","正常标准","正常标准","休假类型","休假年份","休假天数（累计）","停发天数（工作日）","停发天数（工作日）","停发天数（工作日）",
						"当年停发岗位工资","当年停发岗位工资","当年停发岗位工资","当年停发绩效工资","当年停发绩效工资","当年停发绩效工资","当年停发车补","当年停发车补","当年停发车补",
						"年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细",
						"年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细",
						"年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细",
						"年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细","年实际已停发明细",
						"年实际已停发明细","年实际已停发明细"};
				String[] head1 = new String[] {"序号","FMIS员工号","部门","姓名","岗位工资","预发绩效","交通补贴","休假类型","休假年份","休假天数（累计）","岗位工资","预发绩效","交通补贴",
						"应停发数","已停发数","年末还需停发",  "应停发数","已停发数","年末还需停发",  "应停发数","已停发数","年末还需停发",
						"1月停发岗位工资","1月停发预发绩效工资","1月停发交通补贴","2月停发岗位工资","2月停发预发绩效工资","2月停发交通补贴","3月停发岗位工资","3月停发预发绩效工资","3月停发交通补贴",
						"4月停发岗位工资","4月停发预发绩效工资","4月停发交通补贴","5月停发岗位工资","5月停发预发绩效工资","5月停发交通补贴","6月停发岗位工资","6月停发预发绩效工资","6月停发交通补贴",
						"7月停发岗位工资","7月停发预发绩效工资","7月停发交通补贴","8月停发岗位工资","8月停发预发绩效工资","8月停发交通补贴","9月停发岗位工资","9月停发预发绩效工资","9月停发交通补贴",
						"10月停发岗位工资","10月停发预发绩效工资","10月停发交通补贴","11月停发岗位工资","11月停发预发绩效工资","11月停发交通补贴","12月停发岗位工资","12月停发预发绩效工资","12月停发交通补贴",
						"年终奖停发岗位工资","年终奖停发预发绩效工资","年终奖停发交通补贴","以后年度停发岗位工资","以后年度停发预发绩效工资","以后年度停发交通补贴",
				};
				String[] headnum0 = new String[] { "0,1,0,0", "0,1,1,1", "0,1,2,2","0,1,3,3","0,1,4,6",
						"0,1,7,7","0,1,8,8","0,1,9,9","0,0,10,12","0,0,13,15","0,0,16,18","0,0,19,21",
						"0,0,22,63"
				};//对应excel中的和列，下表从0开始{"开始行,结束行,开始列,结束列"}

				for (int i = 0; i < head0.length; i++) {
					cell1 = rowTitle.createCell(i);
					cell1.setCellValue(head0[i]);
					cell1.setCellStyle(cellStyle);
				}
				rowTitle = sheet.createRow(1);
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

				//让列宽随着导出的列长自动适应
				for (int colNum = 0; colNum < head1.length; colNum++) {
					int columnWidth = sheet.getColumnWidth(colNum) / 256;
					for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
						Row currentRow;
						//当前行未被使用过
						if (sheet.getRow(rowNum) == null) {
							currentRow = sheet.createRow(rowNum);
						} else {
							currentRow = sheet.getRow(rowNum);
						}
						if (currentRow.getCell(colNum) != null) {
							//取得当前的单元格
							Cell currentCell = currentRow.getCell(colNum);
							//如果当前单元格类型为字符串
							if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
								int length = currentCell.getStringCellValue().getBytes().length;
								if (columnWidth < length) {
									//将单元格里面值大小作为列宽度
									columnWidth = length;
								}
							}
						}
					}
					//再根据不同列单独做下处理
					if (colNum == 0) {
						sheet.setColumnWidth(colNum, (columnWidth ) * 256);
					} else {
						sheet.setColumnWidth(colNum, (columnWidth + 5) * 256);
					}
				}
				// 写入数据
				int rowCnt = 2;
				for (Map<String, Object> tmp : dataList) {
					if (rowCnt > maxLine+1) {
						break;
					}

					Row row= sheet.createRow(rowCnt);

					int cellCnt = 0;
					for (String tId : titleId) {
						Cell cell = row.createCell(cellCnt);
						cell.setCellStyle(cellStyle);
						cell.setCellValue(tmp.get(tId) == null ? "" : tmp.get(tId).toString());
						cellCnt++;
					}
					rowCnt++;
				}
			}
			rst.put("info", workbook);
		} catch (Exception e) {
			log.error("导出数据出错", e);
			rst.put("msg", "导出数据出错:" + e.getMessage());
		} finally {
			DBAccessPool.releaseDbBean();
		}
		return rst;
	}

	@Override
	public Map exportDataPunish(String fileExt, List<Map<String, String>> list,
			Map<String, String> title, int[] mergeIndex) {

		return dbToXlsxPunish(list, title, mergeIndex);
	}
	
	private Map dbToXlsxPunish(List<Map<String, String>> list, Map<String, String> title, int[] mergeIndex) {
		Map rst = new HashMap();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
	        /*此处的key为每个sheet的名称，一个excel中可能有多个sheet页*/ /*此处key对应每一列的标题*//*该list为每个sheet页的数据*/
	        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
	        map.put("（A4）处分停发台账", list);
	        rst =createExcel(workbook,title, map, mergeIndex);
			
		} catch (Exception e) {
			log.error("导出Xls文件出错", e);
			rst.put("msg", "导出Xls文件出错:" + e.getMessage());
		}
		return rst;
	}
	
    /**
 * @param title 标题集合 tilte的长度应该与list中的model的属性个数一致
 * @param maps 内容集合
 * @param mergeIndex 合并单元格的列
 */
public Map createExcel(XSSFWorkbook workbook,Map<String, String> title, Map<String/*sheet名*/, List<Map<String/*对应title的值*/, String>>> maps, int[] mergeIndex){
	Map rst = new HashMap();
    Sheet sheet = null;
    int n = 0;
    //设置标题和单元格样式
    XSSFCellStyle columnTopStyle = getColumnTopStyleAfter(workbook);  //获取列头样式对象
    XSSFCellStyle style = getStyleAfter(workbook);                    //单元格样式对象
    /*循环sheet页*/
    for(Map.Entry<String, List<Map<String/*对应title的值*/, String>>> entry : maps.entrySet()){
        /*实例化sheet对象并且设置sheet名称，book对象*/
        try {
            sheet = workbook.createSheet();
            workbook.setSheetName(n, entry.getKey());
            workbook.setSelectedTab(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        /*初始化head，填值标题行（第一行）*/
        Row row0 = sheet.createRow(0);
        /*for(int i = 0; i<title.length; i++){
            创建单元格，指定类型
            Cell cell_1 = row0.createCell(i, Cell.CELL_TYPE_STRING);
            cell_1.setCellValue(title[i]);
        }*/
    	int c=0;
        for (Map.Entry<String, String> entrymap : title.entrySet()) { 
        	 /*创建单元格，指定类型*/
            Cell cell_1 = row0.createCell(c, Cell.CELL_TYPE_STRING);
            cell_1.setCellStyle(columnTopStyle);
            cell_1.setCellValue(entrymap.getValue());
            c++;
        }
        
        CellRangeAddress titlecra=new CellRangeAddress(0, 0, 4, 5);
        //在sheet里增加合并单元格
        sheet.addMergedRegion(titlecra);
        /*得到当前sheet下的数据集合*/
        List<Map<String/*对应title的值*/, String>> list = entry.getValue();
        /*遍历该数据集合*/
        List<PoiModel> poiModels = new ArrayList<PoiModel>();
        if(null!=workbook){
            Iterator iterator = list.iterator();
            int index = 1;/*这里1是从excel的第二行开始，第一行已经塞入标题了*/
            while (iterator.hasNext()){
                Row row = sheet.createRow(index);
                /*取得当前这行的map，该map中以key，value的形式存着这一行值*/
                Map<String, String> map = (Map<String, String>)iterator.next();
                /*循环列数，给当前行塞值*/
                int i=0; //从第一列开始
                for(Entry<String, String> entrytitle : title.entrySet()){
                //for(int i = 0; i<title.length; i++){
                    String old = "";
                    /*old存的是上一行统一位置的单元的值，第一行是最上一行了，所以从第二行开始记*/
                    if(index > 1){
                        old = poiModels.get(i)==null?"":poiModels.get(i).getContent();
                    }
                    /*循环需要合并的列*/
                    for(int j = 0; j < mergeIndex.length; j++){
                        if(index == 1){
                            /*记录第一行的开始行和开始列*/
                            PoiModel poiModel = new PoiModel();
                            poiModel.setOldContent(map.get(entrytitle.getKey()));
                            poiModel.setContent(map.get(entrytitle.getKey()));
                            poiModel.setRowIndex(1);
                            poiModel.setCellIndex(i);
                            poiModels.add(poiModel);
                            break;
                        }else if(i > 3 && mergeIndex[j] == i){/*这边i>0也是因为第一列已经是最前一列了，只能从第二列开始*/
                            /*当前同一列的内容与上一行同一列不同时，把那以上的合并, 或者在当前元素一样的情况下，前一列的元素并不一样，这种情况也合并*/
                            /*如果不需要考虑当前行与上一行内容相同，但是它们的前一列内容不一样则不合并的情况，把下面条件中||poiModels.get(i).getContent().equals(map.get(title[i])) && !poiModels.get(i - 1).getOldContent().equals(map.get(title[i-1]))去掉就行*/
                            if(!poiModels.get(i).getContent().equals(map.get(entrytitle.getKey())) ){
                                /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                                CellRangeAddress cra=new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                                //在sheet里增加合并单元格
                                sheet.addMergedRegion(cra);
                                
                                if(mergeIndex[j] == 4 && (poiModels.get(i).getRowIndex()!=(index - 1))){
                                    CellRangeAddress nianjincra=new CellRangeAddress(poiModels.get(i).getRowIndex()+2, poiModels.get(i).getRowIndex()+2, poiModels.get(i).getCellIndex(), poiModels.get(i).getCellIndex()+1);
                                    //在sheet里增加合并单元格
                                    sheet.addMergedRegion(nianjincra);
                                }
                                /*重新记录该列的内容为当前内容，行标记改为当前行标记，列标记则为当前列*/
                                poiModels.get(i).setContent(map.get(entrytitle.getKey()));
                                poiModels.get(i).setRowIndex(index);
                                poiModels.get(i).setCellIndex(i);
                            }
                        }
                        /*处理第一列的情况*/
                        if(mergeIndex[j] == i && (0<=i && i<=3) && (index-1)%3 == 0){
                            /*当前行的当前列与上一行的当前列的内容不一致时，则把当前行以上的合并*/
                            CellRangeAddress cra=new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index - 1/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                            //在sheet里增加合并单元格
                            sheet.addMergedRegion(cra);
                            /*重新记录该列的内容为当前内容，行标记改为当前行标记*/
                            poiModels.get(i).setContent(map.get(entrytitle.getKey()));
                            poiModels.get(i).setRowIndex(index);
                            poiModels.get(i).setCellIndex(i);
                        }
                       
                        /*最后一行没有后续的行与之比较，所有当到最后一行时则直接合并对应列的相同内容*/
                        if(mergeIndex[j] == i && index == list.size()){
                            CellRangeAddress cra=new CellRangeAddress(poiModels.get(i).getRowIndex()/*从第二行开始*/, index/*到第几行*/, poiModels.get(i).getCellIndex()/*从某一列开始*/, poiModels.get(i).getCellIndex()/*到第几列*/);
                            //在sheet里增加合并单元格
                            sheet.addMergedRegion(cra);
                        }
                    }
                    Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                    cell.setCellStyle(style);
                    cell.setCellValue(map.get(entrytitle.getKey()));
                    /*在每一个单元格处理完成后，把这个单元格内容设置为old内容*/
                    poiModels.get(i).setOldContent(old);
                    i++;
                }
                index++;
            }
        }
        n++;
    }
    rst.put("fileName", "处分停发台账");
    rst.put("info", workbook);
	return rst;
}

@Override
public Map exportBranchData(String fileExt, List<Map<String, Object>> list,
		Map<String, String> title, Map<String, String> title2, String payMonth) {
	// TODO Auto-generated method stub
	return dbToXlsxBranchData(list, title, title2, payMonth);
}

	@Override
	public Map exportManage(String filetype, List<Map<String, Object>> dataList, List<Map<String, String>> titleList, List<String> titleId, String s, String[] head0, String[] head1, String[] headnum0) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		Map rst = new HashMap();
		try {
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			Sheet sheet = workbook.createSheet();
			XSSFCellStyle cellStyle = getColumnTopStyleAfter(workbook);  //获取列头样式对象
			XSSFCellStyle style = getStyleAfter(workbook);
			int maxLine = ExpDataFactory.MAX_LINE > dataList.size() ? dataList.size() : ExpDataFactory.MAX_LINE;
			if (dataList.size() > 0) {
				// 写入标题
				Row rowTitle = sheet.createRow(0);
				Cell cell1 =  rowTitle.createCell(0);
				for (int i = 0; i < head0.length; i++) {
					cell1 = rowTitle.createCell(i);
					cell1.setCellValue(head0[i]);
					cell1.setCellStyle(cellStyle);
				}
				rowTitle = sheet.createRow(1);
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
				for (int colNum = 0; colNum < head1.length; colNum++) {
					int columnWidth = sheet.getColumnWidth(colNum) / 256;
					for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
						Row currentRow;
						//当前行未被使用过
						if (sheet.getRow(rowNum) == null) {
							currentRow = sheet.createRow(rowNum);
						} else {
							currentRow = sheet.getRow(rowNum);
						}
						if (currentRow.getCell(colNum) != null) {
							//取得当前的单元格
							Cell currentCell = currentRow.getCell(colNum);
							//如果当前单元格类型为字符串
							if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
								int length = currentCell.getStringCellValue().getBytes().length;
								if (columnWidth < length) {
									//将单元格里面值大小作为列宽度
									columnWidth = length;
								}
							}
						}
					}
					//再根据不同列单独做下处理
					if (colNum == 0) {
						sheet.setColumnWidth(colNum, (columnWidth ) * 256);
					} else {
						sheet.setColumnWidth(colNum, (columnWidth + 5) * 256);
					}
				}
				// 写入数据
				int rowCnt = 2;
				for (Map<String, Object> tmp : dataList) {
					if (rowCnt > maxLine+1) {
						break;
					}
					Row row= sheet.createRow(rowCnt);
					int cellCnt = 0;
					for (String tId : titleId) {
						Cell cell = row.createCell(cellCnt);
						cell.setCellStyle(style);
						cell.setCellValue(tmp.get(tId) == null ? "" : tmp.get(tId).toString());
						cellCnt++;
					}
					rowCnt++;
				}

				//处理尾页excel


			}
			rst.put("info", workbook);
		} catch (Exception e) {
			log.error("导出数据出错", e);
			rst.put("msg", "导出数据出错:" + e.getMessage());
		} finally {
			DBAccessPool.releaseDbBean();
		}
		return rst;
	}

	private Map dbToXlsxBranchData(List<Map<String, Object>> list, Map<String, String> title, Map<String, String> title2, String payMonth) {
	Map rst = new HashMap();
	try {
		XSSFWorkbook workbook = new XSSFWorkbook();
        /*此处的key为每个sheet的名称，一个excel中可能有多个sheet页*/ /*此处key对应每一列的标题*//*该list为每个sheet页的数据*/
        Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
        map.put("合并分行数据", list);
        rst =createBranchExcel(workbook,title,title2, map, payMonth);
		
	} catch (Exception e) {
		log.error("导出Xls文件出错", e);
		rst.put("msg", "导出Xls文件出错:" + e.getMessage());
	}
	return rst;
}

public Map createBranchExcel(XSSFWorkbook workbook,Map<String, String> title, Map<String, String> title2,
		Map<String, List<Map<String, Object>>> maps, String payMonth){
	
	Map rst = new HashMap();
    Sheet sheet = null;
    int n = 0;
    String[] headnum1 = new String[] { "1,1,3,6", "1,1,7,8", "1,1,9,10", "1,1,11,12", "1,1,16,17"};
    String[] headnum2 = new String[] { "1,2,0,0", "1,2,1,1", "1,2,2,2"};
    //设置标题和单元格样式
    XSSFCellStyle columnTopStyle = getColumnTopStyleAfter(workbook);  //获取列头样式对象
    XSSFCellStyle style = getStyleAfter(workbook);                    //单元格样式对象
    /*循环sheet页*/
    for(Map.Entry<String, List<Map<String/*对应title的值*/, Object>>> entry : maps.entrySet()){
        /*实例化sheet对象并且设置sheet名称，book对象*/
        try {
            sheet = workbook.createSheet();
            workbook.setSheetName(n, entry.getKey());
            workbook.setSelectedTab(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        /*初始化head，填值标题行（第一行）*/
        Row row0 = sheet.createRow(0);
        /*创建单元格，指定类型*/
        Cell cell_0 = row0.createCell(0, Cell.CELL_TYPE_STRING);
        cell_0.setCellStyle(columnTopStyle);
        cell_0.setCellValue("二级分行行级干部五险一金划付核对表("+payMonth+")");
        CellRangeAddress titlecra=new CellRangeAddress(0, 0, 0, title.size()-1);
        //在sheet里增加合并单元格
        sheet.addMergedRegion(titlecra);
        
        /*初始化head，填值标题行（第二行）*/
        Row row1 = sheet.createRow(1);
    	int c=0;
        for (Map.Entry<String, String> entrymap : title.entrySet()) { 
        	 /*创建单元格，指定类型*/
            Cell cell_1 = row1.createCell(c, Cell.CELL_TYPE_STRING);
            cell_1.setCellStyle(columnTopStyle);
            cell_1.setCellValue(entrymap.getValue());
            c++;
        }
        //动态合并单元格
        for (int i = 0; i < headnum1.length; i++) {
            String[] temp = headnum1[i].split(",");
            Integer startrow = Integer.parseInt(temp[0]);
            Integer overrow = Integer.parseInt(temp[1]);
            Integer startcol = Integer.parseInt(temp[2]);
            Integer overcol = Integer.parseInt(temp[3]);
            sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                    startcol, overcol));
        }
        /*初始化head，填值标题行（第二行）*/
        Row row2 = sheet.createRow(2);
    	int c2=0;
        for (Map.Entry<String, String> entrymap2 : title2.entrySet()) { 
        	 /*创建单元格，指定类型*/
            Cell cell_2 = row2.createCell(c2, Cell.CELL_TYPE_STRING);
            cell_2.setCellStyle(columnTopStyle);
            cell_2.setCellValue(entrymap2.getValue());
            c2++;
        }
        //动态合并单元格
        for (int i = 0; i < headnum2.length; i++) {
            String[] temp = headnum2[i].split(",");
            Integer startrow = Integer.parseInt(temp[0]);
            Integer overrow = Integer.parseInt(temp[1]);
            Integer startcol = Integer.parseInt(temp[2]);
            Integer overcol = Integer.parseInt(temp[3]);
            sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                    startcol, overcol));
        }
        /*得到当前sheet下的数据集合*/
        List<Map<String/*对应title的值*/, Object>> list = entry.getValue();
        /*遍历该数据集合*/
        if(null!=workbook){
            Iterator iterator = list.iterator();
            int index = 3;/*这里1是从excel的第二行开始，第一行已经塞入标题了*/
            while (iterator.hasNext()){
                Row row = sheet.createRow(index);
                /*取得当前这行的map，该map中以key，value的形式存着这一行值*/
                Map<String, Object> map = (Map<String, Object>)iterator.next();
                /*循环列数，给当前行塞值*/
                int i=0; //从第一列开始
                for(Entry<String, String> entrytitle : title.entrySet()){

                    Cell cell = row.createCell(i, Cell.CELL_TYPE_STRING);
                    cell.setCellStyle(style);
                    Object obj=map.get(entrytitle.getKey());
                    String value=String.valueOf(obj);
                    cell.setCellValue(value);
                    i++;
                }
                index++;
            }
        }
        n++;
    }
    rst.put("info", workbook);
    rst.put("fileName", "二级分行行级干部五险一金划付核对表");
   	return rst;
}

/**
 * @param
 * @return
 * @author jiaqing.xu@hand-china.com
 * @date 2017/10/19 13:31
 * @description 标题行的单元格样式
 */
public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

    // 设置字体
    HSSFFont font = workbook.createFont();
    //设置字体大小
    font.setFontHeightInPoints((short) 12);
    //字体加粗
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    //设置字体名字
    font.setFontName("Courier New");
    //设置样式;
    HSSFCellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
    //设置底边框;
    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    //设置底边框颜色;
    style.setBottomBorderColor(HSSFColor.BLACK.index);
    //设置左边框;
    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    //设置左边框颜色;
    style.setLeftBorderColor(HSSFColor.BLACK.index);
    //设置右边框;
    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    //设置右边框颜色;
    style.setRightBorderColor(HSSFColor.BLACK.index);
    //设置顶边框;
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    //设置顶边框颜色;
    style.setTopBorderColor(HSSFColor.BLACK.index);
    //在样式用应用设置的字体;
    style.setFont(font);
    //设置自动换行;
    style.setWrapText(true);
    //设置水平对齐的样式为居中对齐;
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    //设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    return style;
}

public XSSFCellStyle getColumnTopStyleAfter(Workbook workbook) {

    // 设置字体
    XSSFFont font = (XSSFFont) workbook.createFont();
    //设置字体大小
    font.setFontHeightInPoints((short) 12);
    //字体加粗
    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    //设置字体名字
    font.setFontName("Courier New");
    //设置样式;
    XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
    style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
    //设置底边框;
    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    //设置底边框颜色;
    style.setBottomBorderColor(HSSFColor.BLACK.index);
    //设置左边框;
    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    //设置左边框颜色;
    style.setLeftBorderColor(HSSFColor.BLACK.index);
    //设置右边框;
    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    //设置右边框颜色;
    style.setRightBorderColor(HSSFColor.BLACK.index);
    //设置顶边框;
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    //设置顶边框颜色;
    style.setTopBorderColor(HSSFColor.BLACK.index);
    //在样式用应用设置的字体;
    style.setFont(font);
    //设置自动换行;
    style.setWrapText(true);
    //设置水平对齐的样式为居中对齐;
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    //设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    return style;
}
/**
 * @param
 * @return
 * @author jiaqing.xu@hand-china.com
 * @date 2017/10/19 13:31
 * @description 列数据信息单元格样式
 */
public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
    // 设置字体
    HSSFFont font = workbook.createFont();
    //设置字体大小
    font.setFontHeightInPoints((short)12);
    //字体加粗
    //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    //设置字体名字
    font.setFontName("Courier New");
    //设置样式;
    HSSFCellStyle style = workbook.createCellStyle();
    //设置底边框;
    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    //设置底边框颜色;
    style.setBottomBorderColor(HSSFColor.BLACK.index);
    //设置左边框;
    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    //设置左边框颜色;
    style.setLeftBorderColor(HSSFColor.BLACK.index);
    //设置右边框;
    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    //设置右边框颜色;
    style.setRightBorderColor(HSSFColor.BLACK.index);
    //设置顶边框;
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    //设置顶边框颜色;
    style.setTopBorderColor(HSSFColor.BLACK.index);
    //在样式用应用设置的字体;
    style.setFont(font);
    //设置自动换行;
    style.setWrapText(true);
    //设置水平对齐的样式为居中对齐;
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    //设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    return style;
}

public XSSFCellStyle getStyleAfter(Workbook workbook) {
    // 设置字体
    XSSFFont font = (XSSFFont) workbook.createFont();
    //设置字体大小
    font.setFontHeightInPoints((short)12);
    //字体加粗
    //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    //设置字体名字
    font.setFontName("Courier New");
    //设置样式;
    XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
    //设置底边框;
    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    //设置底边框颜色;
    style.setBottomBorderColor(HSSFColor.BLACK.index);
    //设置左边框;
    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    //设置左边框颜色;
    style.setLeftBorderColor(HSSFColor.BLACK.index);
    //设置右边框;
    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    //设置右边框颜色;
    style.setRightBorderColor(HSSFColor.BLACK.index);
    //设置顶边框;
    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    //设置顶边框颜色;
    style.setTopBorderColor(HSSFColor.BLACK.index);
    //在样式用应用设置的字体;
    style.setFont(font);
    //设置自动换行;
    style.setWrapText(true);
    //设置水平对齐的样式为居中对齐;
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    //设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    return style;
}
}
