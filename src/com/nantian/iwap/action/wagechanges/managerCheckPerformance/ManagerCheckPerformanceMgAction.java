package com.nantian.iwap.action.wagechanges.managerCheckPerformance;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nantian.iwap.app.exp.ExpDataFactory;
import com.nantian.iwap.app.imp.ImpDataFactory;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


public class ManagerCheckPerformanceMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(ManagerCheckPerformanceMgAction.class);


    @Override
    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");

        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }
        if ("managerCheckPerformance".equals(option)) {
            return managerCheckPerformance(dtbHelper);
        }
        if ("save".equals(option)) {
            return save(dtbHelper);
        }
        if ("summ".equals(option)) {
            return summ(dtbHelper);
        }
        if ("remove".equals(option)) {
            return remove(dtbHelper);
        }
        if ("export".equals(option)) {
            return export(dtbHelper);
        }

        return 0;
    }

    private int summ(DTBHelper dtbHelper) {
        DBAccessBean dbBean = DBAccessPool.getDbBean();
        int flag = 0;
        try {
            dbBean.setAutoCommit(false);
            String seSql = "select * from nh_overtime where o_status='0'";
            List<Map<String, Object>> maps = dbBean.queryForList(seSql);
            for (Map<String, Object> map : maps) {
                String jiReason = "";
                String jiaTimes = "";
                String o_idcard = map.get("O_IDCARD").toString();
                String o_id = map.get("O_ID").toString();
                //判断重复
                String sql = "select count(*) from NH_GOVERMONEY where g_idcard=? and g_status='0'";
                int i = dbBean.queryForInt(sql, o_idcard);
                if (i == 0) {
                    String o_name = map.get("O_NAME").toString();
                    //加班原因
                    String o_reason = map.get("O_REASON").toString();
                    String o_gz = map.get("O_GZ").toString();
                    String o_overgz = map.get("O_OVERGZ").toString();
                    String selectSql = "select o_reason ,o_otimes from nh_overtime where o_status = '0' and O_IDCARD=?";
                    List<Map<String, Object>> stringMap = dbBean.queryForList(selectSql, o_idcard);
                    int j = 0;
                    for (Map<String, Object> stringObjectMap : stringMap) {
                        if (stringObjectMap.get("O_REASON") == null || stringObjectMap.get("O_OTIMES") == null) {
                            dtbHelper.setError("", "具体时间或者原因不能为空");

                        } else {
                            if (j < stringMap.size() - 1) {
                                jiReason += stringObjectMap.get("O_REASON").toString() + ",";
                                jiaTimes += stringObjectMap.get("O_OTIMES").toString() + ",";
                                j++;
                            } else {
                                jiReason += stringObjectMap.get("O_REASON").toString();
                                jiaTimes += stringObjectMap.get("O_OTIMES").toString();
                                break;
                            }
                        }

                    }
                    System.out.println(jiReason);
                    System.out.println(jiaTimes);

                    seSql = "SELECT sum(O_DOUBLEDAYS),sum(O_THDAYS),SUM(O_DAYMONEY) FROM nh_overtime WHERE o_status = '0' and O_IDCARD=?";
                    List<Map<String, Object>> dataMaps = dbBean.queryForList(seSql, o_idcard);
                    String g_dayGzs = "";
                    String g_doDays = "";
                    String g_thDays = "";

                    for (Map<String, Object> objectMap : dataMaps) {
                        g_dayGzs = objectMap.get("SUM(O_DAYMONEY)").toString();
                        if (objectMap.get("SUM(O_THDAYS)") == null) {
                            g_thDays = "";
                        } else {
                            g_thDays = objectMap.get("SUM(O_THDAYS)").toString();
                        }
                        if (objectMap.get("SUM(O_DOUBLEDAYS)") == null) {
                            g_doDays = "";
                        } else {
                            g_doDays = objectMap.get("SUM(O_DOUBLEDAYS)").toString();
                        }
                    }
                    String g_no = "0";
                    String selectFmisSQl = "select w_fmis from NH_DATASOURSE where w_idcard=?";
                    DataObject fmisObject = dbBean.executeSingleQuery(selectFmisSQl, o_idcard);
                    String g_fims = fmisObject.getValue("w_fmis").toString();
                    //这里设总工资为加班工资导入值班工资时在加上；
                    String insSql = "insert into NH_GOVERMONEY(G_IDCARD,G_NAME,G_NO,G_FMIS,G_REASON,G_TIMES,G_THDAYS,G_DODAYS,G_GZ" +
                            ",G_OVERGZ,G_SDAYGZ,G_SGZ,G_STATUS) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    dbBean.executeUpdate(insSql, o_idcard, o_name, g_no, g_fims, jiReason, jiaTimes, g_thDays, g_doDays, o_gz
                            , o_overgz, g_dayGzs, g_dayGzs, "0");
                    dbBean.executeCommit();
                    flag = 1;

                } else {
                    String updateSql = " update  nh_overtime  set o_status='1' where o_id=?";
                    dbBean.executeUpdate(updateSql, o_id);
                    dbBean.executeCommit();
                    flag = 1;
                    continue;
                }

                String updateSql = " update  nh_overtime  set o_status='1' where o_id=?";
                dbBean.executeUpdate(updateSql, o_id);
                dbBean.executeCommit();
                flag = 1;
            }

        } catch (Exception e) {
            log.error("合并出错", e);
            dtbHelper.setError("usermg-err-add-002", "[合并出错]" + e.getMessage());
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
            String time = dtbHelper.getStringValue("time");
            String name = dtbHelper.getStringValue("name");
            String fmis = dtbHelper.getStringValue("fmis");
            String project = dtbHelper.getStringValue("project");
            String sqlStr = "select * from nh_gjx where (d_xjx>0 or d_xgz>0)";

            if (!"".equals(time)) {
                sqlStr += " and D_SENDMONTH='" + time + "'";
            }
            if (!"".equals(name)) {
                sqlStr += " and d_name like '%" + name + "%'";
            }
            if (!"".equals(fmis)) {
                sqlStr += " and d_fmis = '" + fmis + "'";
            }
            if (!"".equals(project)) {
                sqlStr += " and d_project like '%" + project + "%'";
            }
            sqlStr += " order by d_id desc";
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

    protected int save(DTBHelper dtbHelper) throws BizActionException {
        //身份证不能修改
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String d_fmis = dtbHelper.getStringValue("D_FMIS");
            String d_project = dtbHelper.getStringValue("D_PROJECT");
            String d_jx = dtbHelper.getStringValue("D_JX");
            String d_gz = dtbHelper.getStringValue("D_GZ");
            String d_sendjx = dtbHelper.getStringValue("D_SENDJX");
            String d_sendgz = dtbHelper.getStringValue("D_SENDGZ");
            String d_sendmonth = dtbHelper.getStringValue("D_SENDMONTH");
            String d_xgz = dtbHelper.getStringValue("D_XGZ");
            String d_xjx = dtbHelper.getStringValue("D_XJX");
            BigDecimal xgz = null;
            BigDecimal xjx = null;
            if (d_sendmonth.equals("")) {
                log.error("发放月份不能为空");
                dtbHelper.setError("usermg-err-sv", "发放月份不能为空");

            } else {

                if (d_jx.equals("")) {
                    d_jx = "0.0";
                }
                if (d_gz.equals("")) {
                    d_gz = "0.0";
                }
                if (d_sendgz.equals("")) {
                    d_sendgz = "0.0";
                }
                if (d_sendjx.equals("")) {
                    d_sendjx = "0.0";
                }
                if (d_xjx.equals("")) {
                    xjx = new BigDecimal(Double.parseDouble(d_jx));
                } else {
                    xjx = new BigDecimal(Double.parseDouble(d_xjx));
                }

                if (d_xgz.equals("")) {
                    xgz = new BigDecimal(Double.parseDouble(d_gz));
                } else {
                    xgz = new BigDecimal(Double.parseDouble(d_xgz));
                }


                BigDecimal sendGz = new BigDecimal(Double.parseDouble(d_sendgz));
                BigDecimal sendJx = new BigDecimal(Double.parseDouble(d_sendjx));
                //还需发
                xgz = xgz.subtract(sendGz).setScale(2, BigDecimal.ROUND_HALF_UP);
                xjx = xjx.subtract(sendJx).setScale(2, BigDecimal.ROUND_HALF_UP);
                if (xgz.compareTo(new BigDecimal(0.0)) < 0 || xjx.compareTo(new BigDecimal(0.0)) < 0) {
                    log.error("绩效或者奖励金额超出，请重新分配");
                    dtbHelper.setError("usermg-err-sv", "绩效或者奖励金额超出，请重新分配");

                } else {
                    d_xgz = xgz + "";
                    d_xjx = xjx + "";
                    String updateSql = " update nh_gjx set d_sendmonth=?,d_xgz=?,d_xjx=?,d_sendjx=?,d_sendgz=? where d_fmis=? and d_project=? ";
                    dbBean.executeUpdate(updateSql, d_sendmonth, d_xgz, d_xjx, d_sendjx, d_sendgz, d_fmis, d_project);
                    dbBean.executeCommit();
                    flag = 1;
                }
            }

        } catch (Exception e) {
            log.error("保存出错", e);
            dtbHelper.setError("usermg-err-sv", "[保存出错]" + e.getMessage());
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
                int i = dbBean.executeUpdate("delete from NH_GJX   where d_id = ?", user);
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

    private int managerCheckPerformance(DTBHelper dtbHelper) {
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
            String fileNm = files.get(0).toString();
            fileNm = UploadConfig.tempPath + fileNm;
            String imp_id = dtbHelper.getStringValue("imp_id");
            String modelcode = dtbHelper.getStringValue("option");
            if (imp_id == null || "".equals(imp_id.toString().trim())) {
                log.error("导入数据类型ID不能为空");
                dtbHelper.setError("importdata-err-002", "导入数据类型ID不能为空");
                return flag;
            }
            ImpDataFactory idf = ImpDataFactory.getInstance();
            Map rst = idf.impData(fileNm, imp_id, false, modelcode);
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

    private int export(DTBHelper dtbHelper) {

        int flag = 0;
        DBAccessBean dbBean = null;
        String time = dtbHelper.getStringValue("time");
        String sumFjx = "";
        String djx = "";
        String sumFgz = "";
        String dgz = "";
        String sumSendgz = "";
        String dSendgz = "";
        String sumSendjx = "";
        String dSendjx = "";
        String dxgz = "";
        String sumXgz = "";
        String dxjx = "";
        String sumXjx = "";
        if (time.equals("")) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            time = sdf.format(d);
        }
        String sqlStr = "select d_dept,d_fmis,d_name,d_project,D_SENDMONTH,d_jx,d_gz,D_SENDJX,D_SENDGZ,d_xjx,d_xgz from nh_gjx where (d_xjx>0 or d_xgz>0) and D_SENDMONTH=?";
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("holidayids");
            if (userids.equals("") || userids.contains("{{value}}")) {
                dataList = dbBean.queryForList(sqlStr, time);
                dataList.size();

            } else {
                String[] userarr = userids.split(",");
                for (String user : userarr) {
                    if (user == null || "".equals(user.trim())) {
                        continue;
                    }
                    sqlStr = "select d_dept,d_fmis,d_name,d_project,D_SENDMONTH,d_jx,d_gz,D_SENDJX,D_SENDGZ,d_xjx,d_xgz from nh_gjx where (d_xjx>0 or d_xgz>0) and D_SENDMONTH=? and D_ID=? ";
                    int no = Integer.parseInt(user);
                    List<Map<String, Object>> maps = dbBean.queryForList(sqlStr, time, no);

                    for (Map<String, Object> map : maps) {
                        dataList.add(map);
                    }
                }
            }
            BigDecimal sdgzMoney = new BigDecimal("0.0");
            BigDecimal sdjxMoney = new BigDecimal("0.0");
            BigDecimal sSendgzMoney = new BigDecimal("0.0");
            BigDecimal sSendjxMoney = new BigDecimal("0.0");
            BigDecimal sxgzMoney = new BigDecimal("0.0");
            BigDecimal sxjxMoney = new BigDecimal("0.0");
            for (Map<String, Object> map : dataList) {
                if (map.get("D_GZ") == null) {
                    dgz = "0.0";
                }
                dgz = map.get("D_GZ").toString();
                //计算总钱
                BigDecimal dgzMoney = new BigDecimal(Double.parseDouble(dgz));
                sdgzMoney = sdgzMoney.add(dgzMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                //计算总绩效
                if (map.get("D_JX") == null) {
                    djx = "0.0";
                }
                djx = map.get("D_JX").toString();
                BigDecimal djxMoney = new BigDecimal(Double.parseDouble(djx));
                sdjxMoney = sdjxMoney.add(djxMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                //计算当月发放工资
                if (map.get("D_SENDGZ") == null) {
                    dSendgz = "0.0";
                }
                dSendgz = map.get("D_SENDGZ").toString();
                BigDecimal sgzMoney = new BigDecimal(Double.parseDouble(dSendgz));
                sSendgzMoney = sSendgzMoney.add(sgzMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
                //计算当月发放绩效
                if (map.get("D_SENDJX") == null) {
                    dSendjx = "0.0";
                }
                dSendjx = map.get("D_SENDJX").toString();
                BigDecimal sjxMoney = new BigDecimal(Double.parseDouble(dSendjx));
                sSendjxMoney = sSendjxMoney.add(sjxMoney).setScale(2, BigDecimal.ROUND_HALF_UP);

                //计算须发工资
                if (map.get("D_XGZ") == null) {
                    dxgz = "0.0";
                }
                dxgz = map.get("D_XGZ").toString();
                BigDecimal dxgzMoney = new BigDecimal(Double.parseDouble(dxgz));
                sxgzMoney = sxgzMoney.add(dxgzMoney).setScale(2, BigDecimal.ROUND_HALF_UP);

                //须发绩效
                if (map.get("D_XJX") == null) {
                    dxjx = "0.0";
                }
                dxjx = map.get("D_XGZ").toString();
                BigDecimal dxjxMoney = new BigDecimal(Double.parseDouble(dxjx));
                sxjxMoney = sxjxMoney.add(dxjxMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            sumFgz = sdgzMoney + "";
            sumFjx = sdjxMoney + "";
            sumSendgz = sSendgzMoney + "";
            sumSendjx = sSendjxMoney + "";
            sumXgz = sxgzMoney + "";
            sumXjx = sxjxMoney + "";
            String[] end = new String[]{"合计", "", "", "","", sumFjx, sumFgz, sumSendjx, sumSendgz, sumXjx, sumXgz};

            String filetype = "xlsx";
            String titleString = dtbHelper.getStringValue("titleString");
            //导出表格顺序
            List<String> titleId = new ArrayList<>();
            String[] t = new String[]{"D_DEPT", "D_FMIS", "D_NAME", "D_PROJECT","D_SENDMONTH", "D_JX", "D_GZ", "D_SENDJX", "D_SENDGZ", "D_XJX", "D_XGZ"};
            for (int i = 0; i < t.length; i++) {
                titleId.add(t[i]);
            }
            String s = "overTime";
            String[] head0 = new String[]{"部门", "员工号", "姓名", "项目","发放时间", "分配绩效工资", "分配奖励工资", "当月发放分配绩效工资", "当月发放奖励工资", "待发放分配绩效工资", "待发放奖励工资"};
            String[] head1 = new String[]{"部门", "员工号", "姓名", "项目","发放时间", "分配绩效工资", "分配奖励工资", "当月发放分配绩效工资", "当月发放奖励工资", "待发放分配绩效工资", "待发放奖励工资"};
            String[] headnum0 = new String[]{"0,1,0,0", "0,1,1,1", "0,1,2,2", "0,1,3,3", "0,1,4,4", "0,1,5,5", "0,1,6,6", "0,1,7,7",
                    "0,1,8,8", "0,1,9,9","0,1,10,10"};

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
                DBAccessPool.createDbBean();
                dbBean = DBAccessPool.getDbBean();
                Sheet sheet = workbook.createSheet();
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                int maxLine = ExpDataFactory.MAX_LINE > dataList.size() ? dataList.size() : ExpDataFactory.MAX_LINE;
                if (dataList.size() > 0) {
                    // 写入标题
                    Row rowTitle = sheet.createRow(0);
                    Cell cell1 = rowTitle.createCell(0);
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
                            sheet.setColumnWidth(colNum, (columnWidth) * 256);
                        } else {
                            sheet.setColumnWidth(colNum, (columnWidth + 5) * 256);
                        }
                    }
                    // 写入数据
                    int rowCnt = 2;
                    for (Map<String, Object> tmp : dataList) {
                        if (rowCnt > maxLine + 1) {
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

                    //处理尾页excel
                    Row foot = sheet.createRow(rowCnt++);
                    for (int i = 0; i < end.length; i++) {
                        Cell endCell = foot.createCell(i);
                        endCell.setCellValue(end[i]);
                        endCell.setCellStyle(cellStyle);
                        sheet.addMergedRegion(new CellRangeAddress(maxLine+2,maxLine+2 ,
                                0, 4));
                    }
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
        } catch (Exception e) {
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

}
