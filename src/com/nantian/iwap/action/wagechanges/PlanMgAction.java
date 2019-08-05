package com.nantian.iwap.action.wagechanges;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.nantian.iwap.app.exp.ExpDataFactory;
import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.PaginationSupport;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: UserMgAction <br/>
 * Function: 请假管理<br/>
 *
 * @author f
 * @version 1.0
 * @since JDK 1.7 Copyright (c) 2019, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class PlanMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(PlanMgAction.class);
    private String encryptClazz = "com.nantian.iwap.app.util.DefaultEncrypt";// 默认加密方式

    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");
        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }
        if ("add".equals(option)) {
            return add(dtbHelper);
        }
        if ("save".equals(option)) {
            return save(dtbHelper);
        }
        if ("remove".equals(option)) {
            return remove(dtbHelper);
        }
        if ("chaxun".equals(option)) {
            return chaxun(dtbHelper);
        }
        return 0;
    }

    private int chaxun(DTBHelper dtbHelper) {
        int flag = 0;
        try {

            String NH_XIUJIAYEAR = "%" + dtbHelper.getStringValue("NH_CARDID") + "%";
            String userId = "%" + dtbHelper.getStringValue("userId") + "%";
            DBAccessBean dbBean = DBAccessPool.getDbBean();

            List<Map<String, Object>> dataList = null;

            String sqlStr = "select * from NH_HOLIDAY  where NH_BNXUTF>0  or NH_JNXUTF>0 or NH_GNXUTF>0  order by NH_NO";
            dataList = dbBean.queryForList(sqlStr);
            dtbHelper.setRstData("rows", dataList);
            flag = 1;
        } catch (Exception e) {
            log.error("用户查询出错", e);
            dtbHelper.setError("usermg-err-qry", "[用户查询出错]" + e.getMessage());
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
            String sqlStr = "select * from NH_HOLIDAY where (NH_BNXUTF>0  or NH_JNXUTF>0 or NH_GNXUTF>0 ) ";
            String year= dtbHelper.getStringValue("year");
            String fmisNo= dtbHelper.getStringValue("fmisNo");
            String name= dtbHelper.getStringValue("userName");

            if (!"".equals(year)) {

                sqlStr += " and NH_XIUJIAYEAR = '" + year+ "'";
            }
            if (!"".equals(fmisNo)) {

                sqlStr += " and nh_fmis= '" + fmisNo+ "'";
            }
            if (!"".equals(name)) {
                sqlStr += " and NH_NAME like '%"+name+ "%' ";
            }


            sqlStr+=" order by NH_NO";
            dataList = dbBean.queryForList(sqlStr, page);
            dtbHelper.setRstData("rows", dataList);
            dtbHelper.setRstData("total", page.getTotalCount());

            flag = 1;
        } catch (Exception e) {
            log.error("用户查询出错", e);
            dtbHelper.setError("usermg-err-qry", "[用户查询出错]" + e.getMessage());
        }
        return flag;
    }

    protected int add(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        /*
         "NH_NAME" "NH_FMIS" "NH_XIUJIAYEAR" "NH_GYTF""NH_GYITF" "NH_GNXUTF"
           "NH_JYTF "NH_JYITF" "NH_JNXUTF" "NH_BYTF" "NH_BYTIF"  "NH_BNXUTF" >
            <th data-grid-name="NH_ONETGZ"
            <th data-grid-name="NH_ONETJX"
            <th data-grid-name="NH_ONETCHE"
           */
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String NH_NAME = dtbHelper.getStringValue("NH_NAME");
            String NH_FMIS = dtbHelper.getStringValue("NH_FMIS");
            String NH_XIUJIAYEAR = dtbHelper.getStringValue("NH_XIUJIAYEAR");
            String NH_GYTF = dtbHelper.getStringValue("NH_GYTF");
            String NH_GYITF = dtbHelper.getStringValue("NH_GYITF");
            String NH_GNXUTF = dtbHelper.getStringValue("NH_GNXUTF");
            String NH_JYTF = dtbHelper.getStringValue("NH_JYTF");
            String NH_JYITF = dtbHelper.getStringValue("NH_JYITF");
            String NH_JNXUTF = dtbHelper.getStringValue("NH_JNXUTF");
            String NH_BYTF = dtbHelper.getStringValue("NH_BYTF");
            String NH_BYTIF = dtbHelper.getStringValue("NH_BYTIF");
            String NH_BNXUTF = dtbHelper.getStringValue("NH_BNXUTF");
            String NH_ONETGZ = dtbHelper.getStringValue("NH_ONETGZ");
            String NH_ONETJX = dtbHelper.getStringValue("NH_ONETJX");
            String NH_ONETCHE = dtbHelper.getStringValue("NH_ONETCHE");
            String NH_TWOTGZ = dtbHelper.getStringValue("NH_TWOTGZ");
            String NH_TWOTJX = dtbHelper.getStringValue("NH_TWOTJX");
            String NH_TWOTCHE = dtbHelper.getStringValue("NH_TWOTCHE");
            String NH_THTGZ = dtbHelper.getStringValue("NH_THTGZ");
            String NH_THTJX = dtbHelper.getStringValue("NH_THTJX");
            String NH_THTCHE = dtbHelper.getStringValue("NH_THTCHE");
            String NH_FOTGZ = dtbHelper.getStringValue("NH_FOTGZ");
            String NH_FOTJX = dtbHelper.getStringValue("NH_FOTJX");
            String NH_FOTCHE = dtbHelper.getStringValue("NH_FOTCHE");
            String NH_FIVETGZ = dtbHelper.getStringValue("NH_FIVETGZ");
            String NH_FIVETJX = dtbHelper.getStringValue("NH_FIVETJX");
            String NH_FIVETCHE = dtbHelper.getStringValue("NH_FIVETCHE");
            String NH_SIXTGZ = dtbHelper.getStringValue("NH_SIXTGZ");
            String NH_SIXTJX = dtbHelper.getStringValue("NH_SIXTJX");
            String NH_SIXTCHE = dtbHelper.getStringValue("NH_SIXTCHE");
            String NH_SETGZ = dtbHelper.getStringValue("NH_SETGZ");
            String NH_SETJX = dtbHelper.getStringValue("NH_SETJX");
            String NH_SETCHE = dtbHelper.getStringValue("NH_SETCHE");
            String NH_AGETGZ = dtbHelper.getStringValue("NH_AGETGZ");
            String NH_AGETJX = dtbHelper.getStringValue("NH_AGETJX");
            String NH_AGETCHE = dtbHelper.getStringValue("NH_AGETCHE");
            String NH_NITGZ = dtbHelper.getStringValue("NH_NITGZ");
            String NH_NITJX = dtbHelper.getStringValue("NH_NITJX");
            String NH_NITCHE = dtbHelper.getStringValue("NH_NITCHE");
            String NH_TENTGZ = dtbHelper.getStringValue("NH_TENTGZ");
            String NH_TENTJX = dtbHelper.getStringValue("NH_TENTJX");
            String NH_TENTCHE = dtbHelper.getStringValue("NH_TENTCHE");
            String NH_ETGZ = dtbHelper.getStringValue("NH_ETGZ");
            String NH_ETJX = dtbHelper.getStringValue("NH_ETJX");
            String NH_ETCHE = dtbHelper.getStringValue("NH_ETCHE");
            String NH_TTGZ = dtbHelper.getStringValue("NH_TTGZ");
            String NH_TTJX = dtbHelper.getStringValue("NH_TTJX");
            String NH_TTCHE = dtbHelper.getStringValue("NH_TTCHE");
            String NH_NTGZ = dtbHelper.getStringValue("NH_NTGZ");
            String NH_NTJX = dtbHelper.getStringValue("NH_NTJX");
            String NH_NTCHE = dtbHelper.getStringValue("NH_NTCHE");
            String NH_YTGZ = dtbHelper.getStringValue("NH_YTGZ");
            String NH_YTJX = dtbHelper.getStringValue("NH_YTJX");
            String NH_YTCHE = dtbHelper.getStringValue("NH_YTCHE");


            String sqlStr = "INSERT INTO NH_HOLIDAY(NH_NAME,NH_FMIS,NH_XIUJIAYEAR,NH_GYTF,NH_GYITF,NH_GNXUTF,NH_JYTF, \n" +
                    "NH_JYITF,NH_JNXUTF, NH_BYTF, NH_BYTIF,NH_BNXUTF,NH_ONETGZ,NH_ONETJX,NH_ONETCHE,NH_TWOTGZ,NH_TWOTJX,\n" +
                    "NH_TWOTCHE,NH_THTGZ,NH_THTJX,NH_THTCHE,NH_FOTGZ,NH_FOTJX,NH_FOTCHE,NH_FIVETGZ,NH_FIVETJX,NH_FIVETCHE,\n" +
                    "NH_SIXTGZ,NH_SIXTJX,NH_SIXTCHE,NH_SETGZ,NH_SETJX,NH_SETCHE,NH_AGETGZ,NH_AGETJX,NH_AGETCHE,NH_NITGZ,\n" +
                    "NH_NITJX,NH_NITCHE,NH_TENTGZ,NH_TENTJX,NH_TENTCHE,NH_ETGZ,NH_ETJX,NH_ETCHE,NH_TTGZ,NH_TTJX,\n" +
                    "NH_TTCHE,NH_NTGZ,NH_NTJX,NH_NTCHE,NH_YTGZ,NH_YTJX,NH_YTCHE)\n" +
                    " VALUES (\n" +
                    "?,?,?,?,?,?,?,?,?,?,\n" +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?\n" +
                    ",?,?,?,?,?,?,?,?,?,?\n" +
                    ",?,?,?,?,?,?,?,?,?,?,\n" +
                    "?,?,?,?)";
            dbBean.executeUpdate(sqlStr, NH_NAME, NH_FMIS, NH_XIUJIAYEAR, NH_GYTF, NH_GYITF, NH_GNXUTF, NH_JYTF, NH_JYITF, NH_JNXUTF, NH_BYTF, NH_BYTIF, NH_BNXUTF
                    , NH_ONETGZ, NH_ONETJX, NH_ONETCHE, NH_TWOTGZ, NH_TWOTJX, NH_TWOTCHE, NH_THTGZ, NH_THTJX, NH_THTCHE, NH_FOTGZ, NH_FOTJX, NH_FOTCHE,
                    NH_FIVETGZ, NH_FIVETJX, NH_FIVETCHE, NH_SIXTGZ, NH_SIXTJX, NH_SIXTCHE, NH_SETGZ, NH_SETJX, NH_SETCHE, NH_AGETGZ, NH_AGETJX, NH_AGETCHE,
                    NH_NITGZ, NH_NITJX, NH_NITCHE, NH_TENTGZ, NH_TENTJX, NH_TENTCHE, NH_ETGZ, NH_ETJX, NH_ETCHE, NH_TTGZ, NH_TTJX, NH_TTCHE,
                    NH_NTGZ, NH_NTJX, NH_NTCHE, NH_YTGZ, NH_YTJX, NH_YTCHE);
            dbBean.executeCommit();
            flag = 1;
        } catch (Exception e) {
            log.error("用户新增出错", e);
            dtbHelper.setError("usermg-err-add-002", "[用户新增出错]" + e.getMessage());
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

    protected int save(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String NH_NO = dtbHelper.getStringValue("NH_NO");
            String NH_NAME = dtbHelper.getStringValue("NH_NAME");
            String NH_FMIS = dtbHelper.getStringValue("NH_FMIS");
            String NH_XIUJIAYEAR = dtbHelper.getStringValue("NH_XIUJIAYEAR");
            String NH_GYTF = dtbHelper.getStringValue("NH_GYTF");
            String NH_GYITF = dtbHelper.getStringValue("NH_GYITF");
            String NH_GNXUTF = dtbHelper.getStringValue("NH_GNXUTF");
            String NH_JYTF = dtbHelper.getStringValue("NH_JYTF");
            String NH_JYITF = dtbHelper.getStringValue("NH_JYITF");
            String NH_JNXUTF = dtbHelper.getStringValue("NH_JNXUTF");
            String NH_BYTF = dtbHelper.getStringValue("NH_BYTF");
            String NH_BYTIF = dtbHelper.getStringValue("NH_BYTIF");
            String NH_BNXUTF = dtbHelper.getStringValue("NH_BNXUTF");
            String NH_ONETGZ = dtbHelper.getStringValue("NH_ONETGZ");
            String NH_ONETJX = dtbHelper.getStringValue("NH_ONETJX");
            String NH_ONETCHE = dtbHelper.getStringValue("NH_ONETCHE");
            String NH_TWOTGZ = dtbHelper.getStringValue("NH_TWOTGZ");
            String NH_TWOTJX = dtbHelper.getStringValue("NH_TWOTJX");
            String NH_TWOTCHE = dtbHelper.getStringValue("NH_TWOTCHE");
            String NH_THTGZ = dtbHelper.getStringValue("NH_THTGZ");
            String NH_THTJX = dtbHelper.getStringValue("NH_THTJX");
            String NH_THTCHE = dtbHelper.getStringValue("NH_THTCHE");
            String NH_FOTGZ = dtbHelper.getStringValue("NH_FOTGZ");
            String NH_FOTJX = dtbHelper.getStringValue("NH_FOTJX");
            String NH_FOTCHE = dtbHelper.getStringValue("NH_FOTCHE");
            String NH_FIVETGZ = dtbHelper.getStringValue("NH_FIVETGZ");
            String NH_FIVETJX = dtbHelper.getStringValue("NH_FIVETJX");
            String NH_FIVETCHE = dtbHelper.getStringValue("NH_FIVETCHE");
            String NH_SIXTGZ = dtbHelper.getStringValue("NH_SIXTGZ");
            String NH_SIXTJX = dtbHelper.getStringValue("NH_SIXTJX");
            String NH_SIXTCHE = dtbHelper.getStringValue("NH_SIXTCHE");
            String NH_SETGZ = dtbHelper.getStringValue("NH_SETGZ");
            String NH_SETJX = dtbHelper.getStringValue("NH_SETJX");
            String NH_SETCHE = dtbHelper.getStringValue("NH_SETCHE");
            String NH_AGETGZ = dtbHelper.getStringValue("NH_AGETGZ");
            String NH_AGETJX = dtbHelper.getStringValue("NH_AGETJX");
            String NH_AGETCHE = dtbHelper.getStringValue("NH_AGETCHE");
            String NH_NITGZ = dtbHelper.getStringValue("NH_NITGZ");
            String NH_NITJX = dtbHelper.getStringValue("NH_NITJX");
            String NH_NITCHE = dtbHelper.getStringValue("NH_NITCHE");
            String NH_TENTGZ = dtbHelper.getStringValue("NH_TENTGZ");
            String NH_TENTJX = dtbHelper.getStringValue("NH_TENTJX");
            String NH_TENTCHE = dtbHelper.getStringValue("NH_TENTCHE");
            String NH_ETGZ = dtbHelper.getStringValue("NH_ETGZ");
            String NH_ETJX = dtbHelper.getStringValue("NH_ETJX");
            String NH_ETCHE = dtbHelper.getStringValue("NH_ETCHE");
            String NH_TTGZ = dtbHelper.getStringValue("NH_TTGZ");
            String NH_TTJX = dtbHelper.getStringValue("NH_TTJX");
            String NH_TTCHE = dtbHelper.getStringValue("NH_TTCHE");
            String NH_NTGZ = dtbHelper.getStringValue("NH_NTGZ");
            String NH_NTJX = dtbHelper.getStringValue("NH_NTJX");
            String NH_NTCHE = dtbHelper.getStringValue("NH_NTCHE");
            String NH_YTGZ = dtbHelper.getStringValue("NH_YTGZ");
            String NH_YTJX = dtbHelper.getStringValue("NH_YTJX");
            String NH_YTCHE = dtbHelper.getStringValue("NH_YTCHE");

            //校验车补数据
           if (NH_ONETCHE.equals("")){
              NH_ONETCHE="0.0";
           }
           Double onec=Double.parseDouble( NH_ONETCHE);

            if (NH_TWOTCHE.equals("")){
                NH_TWOTCHE="0.0";
            }
            Double twc=Double.parseDouble( NH_TWOTCHE);
            if (NH_THTCHE.equals("")){
                NH_THTCHE="0.0";
            }
            Double thc=Double.parseDouble( NH_THTCHE);
            if (NH_FOTCHE.equals("")){
                NH_FOTCHE="0.0";
            }
            Double foc=Double.parseDouble( NH_FOTCHE);
            if (NH_FIVETCHE.equals("")){
                NH_FIVETCHE="0.0";
            }
            Double fivec=Double.parseDouble( NH_FIVETCHE);
            if (NH_SIXTCHE.equals("")){
                NH_SIXTCHE="0.0";
            }
            Double sixc=Double.parseDouble( NH_SIXTCHE);
            if (NH_SETCHE.equals("")){
                NH_SETCHE="0.0";
            }
            Double sec=Double.parseDouble( NH_SETCHE);

            if (NH_AGETCHE.equals("")){
                NH_AGETCHE="0.0";
            }
            Double agc=Double.parseDouble( NH_AGETCHE);

            if (NH_NITCHE.equals("")){
                NH_NITCHE="0.0";
            }
            Double nic=Double.parseDouble( NH_NITCHE);

            if (NH_TENTCHE.equals("")){
                NH_TENTCHE="0.0";
            }
            Double tenc=Double.parseDouble( NH_TENTCHE);
            if (NH_ETCHE.equals("")){
                NH_ETCHE="0.0";
            }
            Double ec=Double.parseDouble( NH_ETCHE);
            if (NH_TTCHE.equals("")){
                NH_TTCHE="0.0";
            }
            Double tc=Double.parseDouble( NH_TTCHE);
            if (NH_NTCHE.equals("")){
                NH_NTCHE="0.0";
            }
            Double nc=Double.parseDouble( NH_NTCHE);
            if (NH_YTCHE.equals("")){
                NH_YTCHE="0.0";
            }
            Double yc=Double.parseDouble( NH_YTCHE);

            if (NH_BNXUTF.equals("")){
                NH_BNXUTF="0.0";
            }
            Double che1=Double.parseDouble(NH_BNXUTF);
            BigDecimal che=new BigDecimal(che1);


            Double a=onec+twc+thc+foc+fivec+sixc+sec+ agc+nic+tenc+ec+tc+nc+yc;
            BigDecimal nhzc=new BigDecimal(a);
            nhzc.setScale(2,BigDecimal.ROUND_HALF_UP);
            String nh_zche=nhzc+"";
            BigDecimal jyche=che.subtract(nhzc).setScale(2,BigDecimal.ROUND_HALF_UP);
            String nh_jyche=jyche+"";



            //校验gz


            if (NH_ONETGZ.equals("")){
                NH_ONETGZ="0.0";
            }
            Double oneg=Double.parseDouble( NH_ONETGZ);

            if (NH_TWOTGZ.equals("")){
                NH_TWOTGZ="0.0";
            }
            Double twg=Double.parseDouble( NH_TWOTGZ);
            if (NH_THTGZ.equals("")){
                NH_THTGZ="0.0";
            }
            Double thg=Double.parseDouble( NH_THTGZ);
            if (NH_FOTGZ.equals("")){
                NH_FOTGZ="0.0";
            }
            Double fog=Double.parseDouble( NH_FOTGZ);
            if (NH_FIVETGZ.equals("")){
                NH_FIVETGZ="0.0";
            }
            Double fiveg=Double.parseDouble( NH_FIVETGZ);
            if (NH_SIXTGZ.equals("")){
                NH_SIXTGZ="0.0";
            }
            Double sixg=Double.parseDouble( NH_SIXTGZ);
            if (NH_SETGZ.equals("")){
                NH_SETGZ="0.0";
            }
            Double seg=Double.parseDouble( NH_SETGZ);

            if (NH_AGETGZ.equals("")){
                NH_AGETGZ="0.0";
            }
            Double agg=Double.parseDouble( NH_AGETGZ);

            if (NH_NITGZ.equals("")){
                NH_NITGZ="0.0";
            }
            Double nig=Double.parseDouble( NH_SETGZ);

            if (NH_TENTGZ.equals("")){
                NH_TENTGZ="0.0";
            }
            Double teng=Double.parseDouble( NH_TENTGZ);
            if (NH_ETGZ.equals("")){
                NH_ETGZ="0.0";
            }
            Double eg=Double.parseDouble( NH_ETGZ);
            if (NH_TTGZ.equals("")){
                NH_TTGZ="0.0";
            }
            Double tg=Double.parseDouble( NH_TTCHE);
            if (NH_NTGZ.equals("")){
                NH_NTGZ="0.0";
            }
            Double ng=Double.parseDouble( NH_NTGZ);
            if (NH_YTGZ.equals("")){
                NH_YTGZ="0.0";
            }

            Double yg=Double.parseDouble( NH_YTGZ);
            if (NH_GNXUTF.equals("")){
                NH_GNXUTF="0.0";
            }
            Double nh_nxfgz1=Double.parseDouble(NH_GNXUTF);
            BigDecimal nh_nxfgz=new BigDecimal(nh_nxfgz1);
            Double a11=oneg+twg+thg+fog+fiveg+sixg+seg+agg+nig+teng+eg+tg+ng+yg;
            BigDecimal a1=new BigDecimal(a11).setScale(2,BigDecimal.ROUND_HALF_UP);;
            BigDecimal jygz=nh_nxfgz.subtract(a1) .setScale(2,BigDecimal.ROUND_HALF_UP);
            String nh_zgz=a1+"";
            String nh_jygz=jygz+"";

            //校验jx
            if (NH_ONETJX.equals("")){
                NH_ONETJX="0.0";
            }
            Double onej=Double.parseDouble( NH_ONETJX);

            if (NH_TWOTJX.equals("")){
                NH_TWOTJX="0.0";
            }
            Double twj=Double.parseDouble( NH_TWOTJX);
            if (NH_THTJX.equals("")){
                NH_THTJX="0.0";
            }
            Double thj=Double.parseDouble( NH_THTJX);
            if (NH_FOTJX.equals("")){
                NH_FOTJX="0.0";
            }
            Double foj=Double.parseDouble( NH_FOTJX);
            if (NH_FIVETJX.equals("")){
                NH_FIVETJX="0.0";
            }
            Double fivej=Double.parseDouble( NH_FIVETJX);
            if (NH_SIXTJX.equals("")){
                NH_SIXTJX="0.0";
            }
            Double sixj=Double.parseDouble( NH_SIXTJX);
            if (NH_SETJX.equals("")){
                NH_SETJX="0.0";
            }
            Double sej=Double.parseDouble( NH_SETJX);

            if (NH_AGETJX.equals("")){
                NH_AGETJX="0.0";
            }
            Double agj=Double.parseDouble( NH_AGETJX);

            if (NH_NITJX.equals("")){
                NH_NITJX="0.0";
            }
            Double nij=Double.parseDouble( NH_SETJX);

            if (NH_TENTJX.equals("")){
                NH_TENTJX="0.0";
            }
            Double tenj=Double.parseDouble( NH_TENTJX);
            if (NH_ETJX.equals("")){
                NH_ETJX="0.0";
            }
            Double ej=Double.parseDouble( NH_ETJX);
            if (NH_TTJX.equals("")){
                NH_TTJX="0.0";
            }
            Double tj=Double.parseDouble( NH_TTJX);
            if (NH_NTJX.equals("")){
                NH_NTJX="0.0";
            }
            Double nj=Double.parseDouble( NH_ETJX);
            if (NH_YTJX.equals("")){
                NH_YTJX="0.0";
            }
            Double yj=Double.parseDouble( NH_ETJX);

            if (NH_JNXUTF.equals("")){
                NH_JNXUTF="0.0";
            }
            Double nh_xjx1=Double.parseDouble( NH_ETJX);
            BigDecimal nh_xjx=new BigDecimal(nh_xjx1);

            Double a2=onej+twj+thj+foj+fivej+sixj+sej+agj+nij+tenj+ej+tj+nj+yj;
            String nh_zjx=a+"";

            String nh_jyjx="";
            String sql = " update NH_HOLIDAY set NH_NAME=?,NH_XIUJIAYEAR=?,NH_GYTF=?,NH_GYITF=?,NH_GNXUTF=?,NH_JYTF=?," +
                    "NH_JYITF=?,NH_JNXUTF=?, NH_BYTF=?, NH_BYTIF=?,NH_BNXUTF=?,NH_ONETGZ=?,NH_ONETJX=?," +
                    "NH_ONETCHE=?,NH_TWOTGZ=?,NH_TWOTJX=?, NH_TWOTCHE=?,  NH_THTGZ=?,NH_THTJX=?,NH_THTCHE=?," +
                    "NH_FOTGZ=?,NH_FOTJX=?,NH_FOTCHE=?,NH_FIVETGZ=?,NH_FIVETJX=?, NH_FIVETCHE=?,NH_SIXTGZ=?," +
                    "NH_SIXTJX=?, NH_SIXTCHE=?,NH_SETGZ=?,NH_SETJX=?,NH_SETCHE=?,NH_AGETGZ=?,NH_AGETJX=?, NH_AGETCHE=?," +
                    "NH_NITGZ=?,NH_NITJX=?,NH_NITCHE=?,NH_TENTGZ=?,NH_TENTJX=?,NH_TENTCHE=?,NH_ETGZ=?,NH_ETJX=?,NH_ETCHE=?, " +
                    "NH_TTGZ=?,NH_TTJX=?,NH_TTCHE=?,NH_NTGZ=?,NH_NTJX=?,NH_NTCHE=?,NH_YTGZ=?,NH_YTJX=?,NH_YTCHE=? ,nh_fmis=?,nh_jygz=? " +
                    ",nh_jyjx=?,nh_jyche=?,nh_zgz=?,nh_zjx=?,nh_zche=? where nh_no=?";

            dbBean.executeUpdate(sql, NH_NAME, NH_XIUJIAYEAR, NH_GYTF, NH_GYITF, NH_GNXUTF, NH_JYTF, NH_JYITF, NH_JNXUTF, NH_BYTF, NH_BYTIF, NH_BNXUTF
                    , NH_ONETGZ, NH_ONETJX, NH_ONETCHE, NH_TWOTGZ, NH_TWOTJX, NH_TWOTCHE, NH_THTGZ, NH_THTJX, NH_THTCHE, NH_FOTGZ, NH_FOTJX, NH_FOTCHE,
                    NH_FIVETGZ, NH_FIVETJX, NH_FIVETCHE, NH_SIXTGZ, NH_SIXTJX, NH_SIXTCHE, NH_SETGZ, NH_SETJX, NH_SETCHE, NH_AGETGZ, NH_AGETJX, NH_AGETCHE,
                    NH_NITGZ, NH_NITJX, NH_NITCHE, NH_TENTGZ, NH_TENTJX, NH_TENTCHE, NH_ETGZ, NH_ETJX, NH_ETCHE, NH_TTGZ, NH_TTJX, NH_TTCHE,
                    NH_NTGZ, NH_NTJX, NH_NTCHE, NH_YTGZ, NH_YTJX, NH_YTCHE, NH_FMIS,
                  nh_jygz,nh_jyjx,nh_jyche,nh_zgz,nh_zjx,nh_zche,NH_NO);
            dbBean.executeCommit();
            flag = 1;
        } catch (Exception e) {
            log.error("保存出错", e);
            dtbHelper.setError("usermg-err-sv", "[用户保存出错]" + e.getMessage());
        }
        return flag;
    }

    protected int remove(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("holidayids");
            String[] userarr = userids.split(",");
            int u_cnt = 0;
            int s_cnt = 0;
            for (String user : userarr) {
                if (user == null || "".equals(user.trim())) {
                    continue;
                }
                u_cnt++;
                int i = dbBean.executeUpdate("delete from nh_holiday where nh_cardid = ?", user);
                if (i == 1) {
                    s_cnt++;
                }
            }
            dbBean.executeCommit();
            if (u_cnt != s_cnt) {
                log.warn("用户删除出错:删除失败" + (u_cnt - s_cnt) + "条");
                dtbHelper.setError("usermg-err-rm-001", "[用户删除出错]删除失败" + (u_cnt - s_cnt) + "条");
            } else {
                flag = 1;
            }
        } catch (Exception e) {
            log.error("用户删除出错", e);
            dtbHelper.setError("usermg-err-rm-002", "[用户删除出错]" + e.getMessage());
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
