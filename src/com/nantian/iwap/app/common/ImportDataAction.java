package com.nantian.iwap.app.common;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.app.imp.ImpDataFactory;
import com.nantian.iwap.app.imp.impl.SystemVariableImpl;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.web.upload.UploadConfig;

/**
 * 数据导入
 *
 * @author wjj
 */
public class ImportDataAction extends TransactionBizAction {
    private static Logger log = Logger.getLogger(ImportDataAction.class);

    @SuppressWarnings("rawtypes")
    @Override
    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        try {
            String option = dtbHelper.getStringValue("option");

            if ("daoru".equals(option)) {
                return daoru(dtbHelper);
            }

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
            String modelcode = dtbHelper.getStringValue("modelcode");
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
            log.error("用户数据导入出错", e);
            dtbHelper.setError("importdata-err-004", "[数据导入出错]" + e.getMessage());
        }
        return flag;
    }



    private int daoru(DTBHelper dtbHelper) {
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
            if (imp_id == null || "".equals(imp_id.toString().trim())) {
                log.error("导入数据类型ID不能为空");
                dtbHelper.setError("importdata-err-002", "导入数据类型ID不能为空");
                return flag;
            }
            ImpDataFactory idf = ImpDataFactory.getInstance();
            String s = "1";

            Map rst = idf.impData(fileNm, imp_id, false, s);
            if (rst.get("msg") != null && !"".equals(rst.get("msg").toString().trim())) {
                dtbHelper.setError("importdata-err-003", "[数据导入出错]" + rst.get("msg"));
            } else {
                dtbHelper.setRstData("info", rst.get("info"));
                flag = 1;
            }
        } catch (Exception e) {
            log.error("用户数据导入出错", e);
            dtbHelper.setError("importdata-err-004", "[数据导入出错]" + e.getMessage());
        }
        return flag;
    }


}