package com.nantian.iwap.app.action.nongheng;

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
 * ClassName: TingMgAction <br/>
 * Function: 请假管理<br/>
 *
 * @author f
 * @version 1.0
 * @since JDK 1.7 Copyright (c) 2019, 广州南天电脑系统有限公司 All Rights Reserved.
 */
public class TingMgAction extends TransactionBizAction {

    private static Logger log = Logger.getLogger(TingMgAction.class);


    public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
        String option = dtbHelper.getStringValue("option");
        if (StringUtil.isBlank(option)) {
            return query(dtbHelper);
        }
        if ("daochu".equals(option)) {
            return daochu(dtbHelper);
        }
        if ("add".equals(option)) {
            return add(dtbHelper);
        }
        if ("shengc".equals(option)) {
            return shengc(dtbHelper);
        }
        if ("save".equals(option)) {
            return save(dtbHelper);
        }

        if ("remove".equals(option)) {
            return remove(dtbHelper);
        }




        return 0;
    }



    protected int query(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        try {
            int start = Integer.valueOf(dtbHelper.getStringValue("start"));
            int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
            DBAccessBean dbBean = DBAccessPool.getDbBean();
            PaginationSupport page = new PaginationSupport(start, limit, limit);
            List<Map<String, Object>> dataList = null;
            String sqlStr = "select * from NH_HOLIDAY order by NH_NO";
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

    protected int shengc(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        try {

            DBAccessBean dbBean = DBAccessPool.getDbBean();

            List<Map<String, Object>> dataList = null;
            String sqlStr = "select * from NH_HOLIDAY";
            dataList = dbBean.queryForList(sqlStr);
            for (Map<String, Object> map : dataList) {

                BigDecimal bday=new BigDecimal(30.00);
                String sumdays = map.get("NH_SUMDAYS").toString();
                Double su = Double.parseDouble(sumdays);
                BigDecimal sums=new BigDecimal(su);
                map.put("NH_SUMDAYS",sums+"");
                //交通补贴
                map.put("NH_TJTBTD", sums+"");
                //交通补贴标准
                String cheMoney = null;
                try {
                    cheMoney = map.get("NH_ZJTBT").toString();
                    map.get("NH_ZJXGZ").toString();
                    map.get("NH_ZGWGZ").toString();
                } catch (Exception e) {
                    log.error("补贴标准不能为空", e);
                    dtbHelper.setError("usermg-err-add-002", "[生成出错]" + e.getMessage());
                }
                Double fc = Double.parseDouble(cheMoney);
                BigDecimal fcm=new BigDecimal(fc);
                fcm.setScale(2,BigDecimal.ROUND_HALF_UP);
                map.put("NH_ZJTBT",fcm+"");
                BigDecimal a=new BigDecimal(20.83);
                BigDecimal zkouMoney;
                // BigDecimal  zgzMoney=(gzMoney.multiply(gsum.setScale(2)).divide(bday,2,BigDecimal.ROUND_HALF_UP))
                //                                .setScale(2,BigDecimal.ROUND_HALF_UP);
                zkouMoney=  sums.multiply(fcm.setScale(2)).divide(a,2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
                zkouMoney=(zkouMoney.compareTo(fcm)>0)?fcm:zkouMoney;
                //矫正应该扣交通总费用无论是否存在记录
                map.put("NH_BYTF", zkouMoney+"");
                Object o=map.get("NH_BYTIF");

                //当年停发交通补贴已停发数是否为空，空年末应方数为总数；
                if ( o!= null ) {
                    String yfm = map.get("NH_BYTIF").toString();
                    Double yiMoney = Double.parseDouble(yfm);
                    BigDecimal yifaMoney=new BigDecimal(yiMoney);
                    map.put("NH_BYTIF",yifaMoney+"");
                    //年末需要发的钱
                    BigDecimal haimoney;
                    haimoney = zkouMoney.compareTo(yifaMoney) >= 0 ? (zkouMoney.subtract(yifaMoney)) : (yifaMoney.subtract(zkouMoney));
                    map.put("NH_BNXUTF", haimoney+"");
                } else {
                    map.put("NH_BYTIF",0);
                    map.put("NH_BNXUTF", zkouMoney+"");
                }
                if (map.get("NH_QINGTYPE").equals("普通病假")) {

                    //请假时间小于等于6扣车补该员工车补标准*（应扣天数/20.83），如当月应扣金额大于当月发放金额，则取当月发放金额
                    if (su <= 6) {
                        //扣矫正应该扣交通总费用无论是否存在记录
                        continue;

                    } else if (su> 6 && su <= 22) {
                        //"NH_TJXGZ"停发天数（工作日）预发绩效</th>
                        /*
                           "NH_ZGWGZ" 正常标准(岗位工资), "NH_ZJXGZ" 正常标准(预发绩效)"NH_ZJTBT"正常标准(交通补贴)
            "NH_QINGTYPE" 请休假类型",NH_XIUJIAYEAR"休假年份,"NH_SUMDAYS" 休假天数(累计)
            "NH_TGWGZ" 停发天数（工作日）岗位工资 "NH_TJXGZ"停发天数（工作日）预发绩效</th>
            "NH_TJTBTD" 停发天数（工作日）交通补贴 "NH_GYTF" 当年停发岗位工资(应停发数)</th>
            "NH_GYITF" 当年停发岗位工资(已停发数)<"NH_GNXUTF"当年停发岗位工资(年末需停发）</th>
            "NH_JYTF" 当年停发绩效工资（应停发数）</th>       "NH_JYITF" 当年停发绩效工资（已停发数）</th>
            "NH_JNXUTF"当年停发绩效工资（年末需停发）</th> "NH_BYTF" 当年停发交通补贴（应停发数）</th>
            "NH_BYTIF" 当年停发交通补贴已停发数）</th>"NH_BNXUTF" 当年停发交通补贴（年末需停发）</th>
             */
                       BigDecimal i=new BigDecimal(6);
                        BigDecimal sum=sums.subtract(i);
                        map.put("NH_TJXGZ", sum+"");
                        //绩效应该扣的钱//绩效标准
                        String jx = (String) map.get("NH_ZJXGZ");
                        Double jxM=Double.parseDouble(jx);

                        BigDecimal jxMoney=new BigDecimal(jxM);
                        map.put("NH_ZJXGZ",jxMoney+"");
                        BigDecimal zkjxMoney;
                        BigDecimal day=new BigDecimal(30);
                        //zkouMoney=  sums.multiply(fcm.setScale(2)).divide(a,2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);

                        zkjxMoney=jxMoney.multiply(sum.setScale(2)).divide(day,2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
                        map.put("NH_JYTF",zkjxMoney+"");
                        if (map.get("NH_JYITF") !=null) {
                            //已发
                            String yxf = map.get("NH_JYITF").toString();
                            Double yifa1Money = Double.parseDouble(yxf);
                            BigDecimal yifaMoney=new BigDecimal(yifa1Money);
                            //年末需要发的钱
                            BigDecimal nianxMoney;
                            nianxMoney=zkjxMoney.subtract(yifaMoney);
                            map.put("NH_JNXUTF", nianxMoney+"");
                        } else  {
                            map.put("NH_JYITF",0);
                            map.put("NH_JNXUTF", zkjxMoney+"");
                        }

                    }else if(su>=23){
                        //扣绩效
                        BigDecimal num=new BigDecimal("6");
                        BigDecimal jsum=sums.subtract(num);
                        map.put("NH_TJXGZ",jsum+"");
                        //绩效应该扣的钱//绩效标准
                        String jx = (String) map.get("NH_ZJXGZ");
                        Double jMoney=Double.parseDouble(jx);
                        BigDecimal jxMoney=new BigDecimal(jMoney);
                        map.put("NH_ZJXGZ",jxMoney+"");
                        BigDecimal zkjxMoney;
                        //setScale(2,BigDecimal.ROUND_HALF_UP)
                        zkjxMoney=jxMoney.multiply(jsum.setScale(2)).divide(bday,2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
                        map.put("NH_JYTF",zkjxMoney+"");
                        if (map.get("NH_JYITF") != null) {
                            //已发
                            String jyxf =  map.get("NH_JYITF").toString();
                            Double jyfaMoney = Double.parseDouble(jyxf);
                            BigDecimal jyifaMoney=new BigDecimal(jyfaMoney);
                            map.put("NH_JYITF",jyifaMoney+"");
                            //年末需要发的钱
                            BigDecimal  nianxMoney;
                            nianxMoney=zkjxMoney.subtract(jyifaMoney);
                            map.put("NH_JNXUTF", nianxMoney+"");
                        } else  {
                            map.put("NH_JYITF" ,0);
                            map.put("NH_JNXUTF", zkjxMoney+"");
                        }
                        //扣工资"NH_GYTF" 当年停发岗位工资(应停发数)</th>
                        //"NH_ZGWGZ" 正常标准(岗位工资),
                        // "NH_GYITF" 当年停发岗位工资(已停发数)<"NH_GNXUTF"当年停发岗位工资(年末需停发）</th>
                        BigDecimal gsum=sums.subtract(new BigDecimal(22));
                        map.put("NH_TGWGZ",gsum+"");
                        String gz = (String) map.get("NH_ZGWGZ");
                        Double gMoney=Double.parseDouble(jx);
                        BigDecimal gzMoney=new BigDecimal(gMoney);
                        map.put("NH_ZGWGZ",gzMoney+"");
                      //a = a.divide(b,2,BigDecimal.ROUND_HALF_UP).multiply(c.setScale(2,BigDecimal.ROUND_HALF_UP)).
                        //setScale(2,BigDecimal.ROUND_HALF_UP)
                        BigDecimal  zgzMoney=(gzMoney.multiply(gsum.setScale(2)).divide(bday,2,BigDecimal.ROUND_HALF_UP))
                                .setScale(2,BigDecimal.ROUND_HALF_UP);
                        System.out.println(zgzMoney);
                        map.put("NH_GYTF",zgzMoney+"");

                        if (map.get("NH_GYITF")!=null){
                            String gzf =  map.get("NH_JYITF").toString();
                            Double gfMoney = Double.parseDouble(gzf);
                            BigDecimal gzfMoney=new BigDecimal(gfMoney);
                            map.put("NH_GYITF",gzfMoney+"");
                            BigDecimal gnyfMoney=zgzMoney.subtract(gzfMoney);
                            map.put("NH_GNXUTF",gnyfMoney+"");
                        }else {
                            map.put("NH_GYITF",0);
                            map.put("NH_GNXUTF",zgzMoney+"");

                        }

                    }


                } else if (map.get("NH_QINGTYPE").equals("重疾病假")) {
                    if (su <= 6) {
                        //扣矫正应该扣交通总费用无论是否存在记录
                        continue;
                    }else if (su>6&&su<=126){
                        BigDecimal i=new BigDecimal(6);
                        BigDecimal sum=sums.subtract(i);
                        map.put("NH_TJXGZ", sum+"");
                        //绩效应该扣的钱//绩效标准
                        String jx = (String) map.get("NH_ZJXGZ");
                        Double jxM=Double.parseDouble(jx);

                        BigDecimal jxMoney=new BigDecimal(jxM);
                        map.put("NH_ZJXGZ",jxMoney+"");
                        BigDecimal zkjxMoney;
                        BigDecimal day=new BigDecimal(30);
                        //zkouMoney=  sums.multiply(fcm.setScale(2)).divide(a,2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);

                        zkjxMoney=jxMoney.multiply(sum.setScale(2)).divide(day,2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
                        map.put("NH_JYTF",zkjxMoney+"");
                        if (map.get("NH_JYITF") !=null) {
                            //已发
                            String yxf = map.get("NH_JYITF").toString();
                            Double yifa1Money = Double.parseDouble(yxf);
                            BigDecimal yifaMoney=new BigDecimal(yifa1Money);
                            //年末需要发的钱
                            BigDecimal nianxMoney;
                            nianxMoney=zkjxMoney.subtract(yifaMoney);
                            map.put("NH_JNXUTF", nianxMoney+"");
                        } else  {
                            map.put("NH_JYITF",0);
                            map.put("NH_JNXUTF", zkjxMoney+"");
                        }
                    }



                } else if (map.get("NH_QINGTYPE").equals("事假")) {



                    //扣绩效
                    map.put("NH_TJXGZ",sums+"");
                    //绩效应该扣的钱//绩效标准
                    String jx = (String) map.get("NH_ZJXGZ");
                    Double jMoney=Double.parseDouble(jx);
                    BigDecimal jxMoney=new BigDecimal(jMoney);
                    map.put("NH_ZJXGZ",jxMoney+"");
                    BigDecimal zkjxMoney;
                    //setScale(2,BigDecimal.ROUND_HALF_UP)
                    zkjxMoney=jxMoney.multiply(sums.setScale(2)).divide(bday,2,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
                    map.put("NH_JYTF",zkjxMoney+"");
                    if (map.get("NH_JYITF") != null) {
                        //已发
                        String jyxf =  map.get("NH_JYITF").toString();
                        Double jyfaMoney = Double.parseDouble(jyxf);
                        BigDecimal jyifaMoney=new BigDecimal(jyfaMoney);
                        map.put("NH_JYITF",jyifaMoney+"");
                        //年末需要发的钱
                        BigDecimal  nianxMoney;
                        nianxMoney=zkjxMoney.subtract(jyifaMoney);
                        map.put("NH_JNXUTF", nianxMoney+"");
                    } else  {
                        map.put("NH_JYITF" ,0);
                        map.put("NH_JNXUTF", zkjxMoney+"");
                    }
                    //扣工资"NH_GYTF"
                   map.put("NH_TGWGZ",sums+"");
                    String gz = (String) map.get("NH_ZGWGZ");
                    Double gMoney=Double.parseDouble(jx);
                    BigDecimal gzMoney=new BigDecimal(gMoney);
                    map.put("NH_ZGWGZ",gzMoney+"");
                    BigDecimal  zgzMoney=(gzMoney.multiply(sums.setScale(2)).divide(bday,2,BigDecimal.ROUND_HALF_UP))
                            .setScale(2,BigDecimal.ROUND_HALF_UP);
                    System.out.println(zgzMoney);
                    map.put("NH_GYTF",zgzMoney+"");

                    if (map.get("NH_GYITF")!=null){
                        String gzf =  map.get("NH_JYITF").toString();
                        Double gfMoney = Double.parseDouble(gzf);
                        BigDecimal gzfMoney=new BigDecimal(gfMoney);
                        map.put("NH_GYITF",gzfMoney+"");
                        BigDecimal gnyfMoney=zgzMoney.subtract(gzfMoney);
                        map.put("NH_GNXUTF",gnyfMoney+"");
                    }else {
                        map.put("NH_GYITF",0);
                        map.put("NH_GNXUTF",zgzMoney+"");

                    }

                } else if (map.get("NH_QINGTYPE").equals("普通长病休")) {

                } else if (map.get("NH_QINGTYPE").equals("重病长病休")) {


                }
                //更改数据库内容：
                String nh_dept =  map.get("NH_DEPT").toString();
                String nh_name =  map.get("NH_NAME").toString();
                String nh_fmis =  map.get("NH_FMIS").toString();
                String NH_ZGWGZ =  map.get("NH_ZGWGZ").toString();
                String NH_ZJXGZ =  map.get("NH_ZJXGZ").toString();
                String NH_ZJTBT =  map.get("NH_ZJTBT").toString();
                String NH_QINGTYPE =  map.get("NH_QINGTYPE").toString();
                String NH_XIUJIAYEAR= map.get("NH_XIUJIAYEAR").toString();
                String NH_SUMDAYS= (String) map.get("NH_SUMDAYS");
                String NH_TGWGZ = (String) map.get("NH_TGWGZ");
                String NH_TJXGZ = (String) map.get("NH_TJXGZ");
                String NH_TJTBTD =null;
                if (map.get("NH_TJTBTD")!=null){
                    NH_TJTBTD =map.get("NH_TJTBTD").toString();
                }
                String NH_GYTF = null;
                if(map.get("NH_GYTF")!=null){
                    NH_GYTF=map.get("NH_GYTF").toString();
                }
                String NH_GYITF = null;
                if (map.get("NH_GYITF")!=null){
                    NH_GYITF=  map.get("NH_GYITF").toString();
                }
                String NH_GNXUTF = null;
                if (map.get("NH_GNXUTF")!=null){
                    NH_GNXUTF =  map.get("NH_GNXUTF").toString();
                }
                String NH_JYTF=null;
                if (map.get("NH_JYTF")!=null){
                    NH_JYTF =  map.get("NH_JYTF").toString();
                }
                String NH_JYITF =null;
                if (map.get("NH_JYITF")!=null){
                    NH_JYITF =  map.get("NH_JYITF").toString();
                }
                String NH_JNXUTF = null;
                if (map.get("NH_JNXUTF")!=null){
                    NH_JNXUTF=map.get("NH_JNXUTF").toString();
                }
                String NH_BYTF =null;
                if (map.get("NH_BYTF")!=null){
                    NH_BYTF=map.get("NH_BYTF").toString();
                }

                String NH_BYTIF = null;
                if (map.get("NH_BYTIF")!=null){
                    NH_BYTIF=map.get("NH_BYTIF").toString();
                }
                String NH_BNXUTF =null;
                if(map.get("NH_BNXUTF")!=null){
                    NH_BNXUTF=map.get("NH_BNXUTF").toString();
                }
                String sqlStr1 = "UPDATE nh_holiday set NH_DEPT=?,NH_NAME=?,NH_ZGWGZ=?,NH_ZJXGZ=?,NH_ZJTBT=?" +
                        ",NH_QINGTYPE=?,NH_XIUJIAYEAR=?,NH_SUMDAYS=?,NH_TGWGZ=?,NH_TJXGZ=?, NH_TJTBTD=?, NH_GYTF=?, NH_GYITF=?, NH_GNXUTF=?, NH_JYTF=?, NH_JYITF=?," +
                        " NH_JNXUTF=?, NH_BYTF=?, NH_BYTIF=?,NH_BNXUTF=?  where nh_fmis=?";
               dbBean.executeUpdate(sqlStr1,nh_dept,nh_name,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,NH_QINGTYPE,
                       NH_XIUJIAYEAR,NH_SUMDAYS,NH_TGWGZ,NH_TJXGZ,NH_TJTBTD,NH_GYTF,NH_GYITF,NH_GNXUTF,NH_JYTF,NH_JYITF,
                       NH_JNXUTF,NH_BYTF, NH_BYTIF,NH_BNXUTF,nh_fmis);
                dbBean.executeCommit();

            }

            flag = 1;
        } catch (Exception e) {
            log.error("生成出错", e);
            dtbHelper.setError("usermg-err-qry", "[生成出错]" + e.getMessage());
        }
        return flag;
    }

    protected int add(DTBHelper dtbHelper) throws BizActionException {
        int flag = 0;
        DBAccessBean dbBean = null;
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String nh_dept = dtbHelper.getStringValue("NH_DEPT");
            String nh_name = dtbHelper.getStringValue("NH_NAME");
            String nh_fmis = dtbHelper.getStringValue("NH_FMIS");
            String NH_ZGWGZ = dtbHelper.getStringValue("NH_ZGWGZ");
            String NH_ZJXGZ = dtbHelper.getStringValue("NH_ZJXGZ");
            String NH_ZJTBT = dtbHelper.getStringValue("NH_ZJTBT");
            String NH_QINGTYPE = dtbHelper.getStringValue("NH_QINGTYPE");
            String NH_XIUJIAYEAR = dtbHelper.getStringValue("NH_XIUJIAYEAR");
            String NH_SUMDAYS = dtbHelper.getStringValue("NH_SUMDAYS");
            String NH_TGWGZ = dtbHelper.getStringValue("NH_TGWGZ");
            String NH_TJXGZ = dtbHelper.getStringValue("NH_TJXGZ");
            String NH_TJTBTD = dtbHelper.getStringValue("NH_TJTBTD");
            String NH_GYTF = dtbHelper.getStringValue("NH_GYTF");
            String NH_GYITF = dtbHelper.getStringValue("NH_GYITF");
            String NH_GNXUTF = dtbHelper.getStringValue("NH_GNXUTF");
            String NH_JYTF = dtbHelper.getStringValue("NH_JYTF");
            String NH_JYITF = dtbHelper.getStringValue("NH_JYITF");
            String NH_JNXUTF = dtbHelper.getStringValue("NH_JNXUTF");
            String NH_BYTF = dtbHelper.getStringValue("NH_BYTF");
            String NH_BYTIF = dtbHelper.getStringValue("NH_BYTIF");
            String NH_BNXUTF = dtbHelper.getStringValue("NH_BNXUTF");

            String sqlStr1 = "INSERT into nh_holiday ( NH_DEPT,NH_NAME,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT" +
                    ",NH_QINGTYPE,NH_XIUJIAYEAR,NH_SUMDAYS,NH_TGWGZ,NH_TJXGZ, NH_TJTBTD, NH_GYTF, NH_GYITF, NH_GNXUTF, NH_JYTF, NH_JYITF," +
                    " NH_JNXUT, NH_BYTF, NH_BYTIF,NH_BNXUTF,nh_fmis)VALUES(?,?,?,?,?,?," +
                    "?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?)";
            dbBean.executeUpdate(sqlStr1,nh_dept,nh_name,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,NH_QINGTYPE,
                    NH_XIUJIAYEAR,NH_SUMDAYS,NH_TGWGZ,NH_TJXGZ,NH_TJTBTD,NH_GYTF,NH_GYITF,NH_GNXUTF,NH_JYTF,NH_JYITF,
                    NH_JNXUTF,NH_BYTF, NH_BYTIF,NH_BNXUTF,nh_fmis);
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
            String nh_dept = dtbHelper.getStringValue("NH_DEPT");
            String nh_name = dtbHelper.getStringValue("NH_NAME");
            String nh_fmis = dtbHelper.getStringValue("NH_FMIS");
            String NH_ZGWGZ = dtbHelper.getStringValue("NH_ZGWGZ");
            String NH_ZJXGZ = dtbHelper.getStringValue("NH_ZJXGZ");
            String NH_ZJTBT = dtbHelper.getStringValue("NH_ZJTBT");
            String nh_qingtype = dtbHelper.getStringValue("NH_QINGTYPE");
            String nh_year = dtbHelper.getStringValue("NH_XIUJIAYEAR");
            String nh_sumdays = dtbHelper.getStringValue("NH_SUMDAYS");
            String NH_TGWGZ = dtbHelper.getStringValue("NH_TGWGZ");
            String NH_TJXGZ = dtbHelper.getStringValue("NH_TJXGZ");
            String NH_TJTBTD = dtbHelper.getStringValue("NH_TJTBTD");
            String NH_GYTF = dtbHelper.getStringValue("NH_GYTF");
            String NH_GYITF = dtbHelper.getStringValue("NH_GYITF");
            String NH_GNXUTF = dtbHelper.getStringValue("NH_GNXUTF");
            String NH_JYTF = dtbHelper.getStringValue("NH_JYTF");
            String nh_no=dtbHelper.getStringValue("NH_NO");
            String NH_JYITF = dtbHelper.getStringValue("NH_JYITF");
            String NH_JNXUTF = dtbHelper.getStringValue("NH_JNXUTF");
            String NH_BYTF = dtbHelper.getStringValue("NH_BYTF");
            String NH_BYTIF = dtbHelper.getStringValue("NH_BYTIF");
            String NH_BNXUTF = dtbHelper.getStringValue("NH_BNXUTF");
            String sqlStr1 = "UPDATE nh_holiday set NH_DEPT=?,NH_NAME=?,NH_ZGWGZ=?,NH_ZJXGZ=?,NH_ZJTBT=?" +
                    ",NH_QINGTYPE=?,NH_XIUJIAYEAR=?,NH_SUMDAYS=?,NH_TGWGZ=?,NH_TJXGZ=?, NH_TJTBTD=?, NH_GYTF=?, NH_GYITF=?, NH_GNXUTF=?, NH_JYTF=?, NH_JYITF=?," +
                    " NH_JNXUTF=?, NH_BYTF=?, NH_BYTIF=?,NH_BNXUTF=? ,nh_fmis=? where nh_no=?";
            dbBean.executeUpdate(sqlStr1, nh_dept, nh_name,NH_ZGWGZ, NH_ZJXGZ, NH_ZJTBT, nh_qingtype, nh_year, nh_sumdays
                    , NH_TGWGZ, NH_TJXGZ, NH_TJTBTD, NH_GYTF, NH_GYITF, NH_GNXUTF, NH_JYTF, NH_JYITF, NH_JNXUTF, NH_BYTF, NH_BYTIF
                    , NH_BNXUTF,nh_fmis,nh_no);

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
                int i = dbBean.executeUpdate("delete from nh_holiday where nh_no = ?", user);
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


    protected int daochu(DTBHelper dtbHelper) {
        int flag = 0;
        DBAccessBean dbBean = null;


        String sql = "select NH_DEPT,NH_NAME,NH_FMIS,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,NH_QINGTYPE,NH_XIUJIAYEAR," +
                "NH_SUMDAYS,NH_TGWGZ, NH_TJTBTD, NH_GYTF, NH_GYITF, NH_GNXUTF, NH_JYTF, NH_JYITF,NH_JNXUTF, " +
                "NH_BYTF, NH_BYTIF,NH_BNXUTF from nh_holiday where nh_fmis=?";
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            dbBean = DBAccessPool.getDbBean();
            dbBean.setAutoCommit(false);
            String userids = dtbHelper.getStringValue("holidayids");
            if (userids.equals("")) {
                sql = "select NH_DEPT,NH_NAME,NH_FMIS,NH_ZGWGZ,NH_ZJXGZ,NH_ZJTBT,NH_QINGTYPE,NH_XIUJIAYEAR," +
                        "NH_SUMDAYS,NH_TGWGZ,NH_TJXGZ,NH_TJTBTD, NH_GYTF, NH_GYITF, NH_GNXUTF, NH_JYTF, NH_JYITF,NH_JNXUTF, " +
                        "NH_BYTF, NH_BYTIF,NH_BNXUTF from nh_holiday ";
                dataList = dbBean.queryForList(sql);
                dataList.size();


            } else {
                String[] userarr = userids.split(",");
                for (String user : userarr) {
                    if (user == null || "".equals(user.trim())) {
                        continue;
                    }
                    List<Map<String, Object>> maps = dbBean.queryForList(sql, user);
                    for (Map<String, Object> map : maps) {
                        dataList.add(map);
                    }
                }
            }
            String filetype = "xlsx";
            String titleString = dtbHelper.getStringValue("titleString");
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
            String s = "1";
            Map rst = edf.expData1(filetype, dataList, titleList, s);
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
