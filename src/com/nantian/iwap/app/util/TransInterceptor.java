package com.nantian.iwap.app.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.common.util.DateUtil;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.SequenceHelper;
import com.nantian.iwap.persistence.sync.TransCacheFactory;
import com.nantian.iwap.processor.ProcessContext;
import com.nantian.iwap.processor.ProcessorInterceptor;

public class TransInterceptor implements ProcessorInterceptor {

	private static Logger log = Logger.getLogger(TransInterceptor.class);

	@Override
	public void preHandle(ProcessContext context) throws Exception {
		context.getDataTransferBus().getResultContext().addData("tx_dt", DateUtil.getCurrentDate("yyyy-MM-dd"));
		context.getDataTransferBus().getResultContext().addData("req_tm",
				DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));// 请求时间
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postHandle(ProcessContext context) throws Exception {
		String txcode = (String) context.getSession().getAttribute("_preTxcode");
		System.out.println("txcode--" + txcode);
		List<Map<String, String>> configList = TransCacheFactory.getTransCache().getCache(txcode);
		String param_opcode = null;
		if (configList != null && configList.size() > 0) {
			param_opcode = configList.get(0).get("param_opcode");
		} else {
			log.warn("交易码[" + txcode + "]无交易配置记录");
			return;
		}
		String op_code = context.getDTBHelper().getStringValue(param_opcode);
		Map<String, String> config = null;
		for (Map<String, String> cfg : configList) {
			if (op_code.equals(cfg.get("op_code"))) {
				config = cfg;
				break;
			}
		}

		List params = new ArrayList();

		boolean flag = config != null;
		if (flag && "1".equals(config.get("log_flag"))) {// 需要记录日志
			DBAccessPool.createDbBean();
			DBAccessBean dbBean = DBAccessPool.getDbBean();
			String tx_id = SequenceHelper.generate("tx_id");
			String tx_flag = "";
			String err_msg = "";

			String err_code = context.getDTBHelper().getErrorCode();
			if (err_code != null) {
				err_msg = context.getDTBHelper().getErrorMessage();
				tx_flag = "0";
			} else {
				err_msg = "";
				tx_flag = "1";
			}
			Map map = (Map) context.getSession().getAttribute("userInfo");
			String acct_id = (String) map.get("ACCT_ID");
			String tx_dt = (String) context.getDataTransferBus().getResultContext().getData("tx_dt");
			String req_tm = (String) context.getDataTransferBus().getResultContext().getData("req_tm");
			params.add(tx_id);
			params.add(txcode);
			params.add(config.get("op_code"));
			params.add(tx_flag);
			params.add(err_code);
			params.add(err_msg);
			params.add(config.get("tx_nm"));
			params.add(tx_dt);
			params.add(acct_id);
			params.add(req_tm);
			params.add(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			StringBuffer sqlsb=new StringBuffer();
			sqlsb.append("insert into sys_tx_dtl(tx_id,tx_cd,op_code,tx_flag,err_code,err_msg,tx_rmk,tx_dt,acct_id,req_tm,resp_tm) ");
			sqlsb.append("values(?,?,?,?,?,?,?,");
			sqlsb.append("to_date(?,'yyyy-MM-dd'),?,");
			sqlsb.append("to_date(?,'yyyy-MM-dd HH24:mi:ss'),");
			sqlsb.append("to_date(?,'yyyy-MM-dd HH24:mi:ss'))");
			//String sql = "insert into sys_tx_dtl(tx_id,tx_cd,op_code,tx_flag,err_code,err_msg,tx_rmk,tx_dt,acct_id,req_tm,resp_tm) values(?,?,?,?,?,?,?,?,?,?,?)";
			dbBean.executeUpdate(sqlsb.toString(), params.toArray());
			DBAccessPool.releaseDbBean();
		}

	}

}
