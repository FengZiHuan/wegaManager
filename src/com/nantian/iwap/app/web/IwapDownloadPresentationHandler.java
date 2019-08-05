package com.nantian.iwap.app.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.ResultContext;
import com.nantian.iwap.presentation.BasePresentationHandler;
import com.nantian.iwap.processor.ProcessContext;

public class IwapDownloadPresentationHandler extends BasePresentationHandler {
	private static Logger log = Logger.getLogger(IwapDownloadPresentationHandler.class);

	@Override
	public void merge(ProcessContext context) throws Exception {
		context.getResponse().reset();
		context.getResponse().setHeader("iwapVersion", "5.0");
		if (context.getDTBHelper().isFormRequest()) {// 表单请求
			if (context.getDTBHelper().getViewName() != null) {
				if (context.getDTBHelper().getErrorCode() != null) {
					context.getRequest().setAttribute("errorCode", context.getDTBHelper().getErrorCode());
					context.getRequest().setAttribute("errorMsg", context.getDTBHelper().getErrorMessage());
				} else {
					context.getRequest().setAttribute("auth", context.getDTBHelper().getResultContext().getAuth());
				}
				context.getRequest()
						.getRequestDispatcher(
								context.getResponse().encodeRedirectURL(context.getDTBHelper().getViewName()))
						.forward(context.getRequest(), context.getResponse());
			} else {
				jsonProcess(context);
			}
		} else {
			jsonProcess(context);
		}
	}

	private void jsonProcess(ProcessContext context) throws IOException {
		ResultContext ctx = context.getDataTransferBus().getResultContext();
		Cookie[] cookies = context.getRequest().getCookies();
		Cookie cookie = new Cookie("JSESSIONID", context.getSession().getId());
		boolean hasSession = false;
		for (int i = 0; cookies != null && i < cookies.length; i++) {
			Cookie clientCookie = cookies[i];
			if ("JSESSIONID".equals(clientCookie.getName())) {
				hasSession = true;
			}
		}
		/**
		 * 如果客户端请求没带cookie或者cookie中没有JSESSIONID，则响应中设置cookies
		 */
		if (cookies == null || cookies.length == 0 || !hasSession) {
			context.getResponse().addCookie(cookie);
		}


			String fileName = "Export-" + UUID.randomUUID().toString().replace("-", "");
			OutputStream out = null;
			try {
				context.getResponse().setContentType("application/octet-stream");
				if ("xls".equals(context.getDTBHelper().getStringValue("filetype"))) {
					fileName += ".xls";
					HSSFWorkbook workbook = (HSSFWorkbook) ctx.getData("info");
					context.getResponse().setHeader("Content-disposition",
							"attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
					out = context.getResponse().getOutputStream();
					workbook.write(out);
				} else if ("xlsx".equals(context.getDTBHelper().getStringValue("filetype"))) {
					fileName += ".xlsx";
					XSSFWorkbook workbook = (XSSFWorkbook) ctx.getData("info");
					String name=(String) ctx.getData("fileName");
					if(!StringUtil.isBlank(name)){
						fileName=name+".xlsx";
					}
					context.getResponse().setHeader("Content-disposition",
							"attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
					out = context.getResponse().getOutputStream();
					workbook.write(out);
				}
				log.info("下发导出文件流:" + fileName);
			} catch (Exception e) {
				log.error("下发导出文件流出错:", e);
			} finally {
				out.close();
			}


	}

}
