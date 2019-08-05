package com.nantian.iwap.app.util;



import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 常量工具类
 * 
 * @Version 1.0
 * @Remark
 * @author <a href="mailto:liaozhiyong@foreveross.com">liaozhiyong</a>
 * @since 2013-8-20
 */
public final class Constant {

	private Constant() {
		super();
	}

	public static final String key = "rostNEW*20170427";
	
	public static final String miStr = "184625";
	
	/**
	 * cxf webservice 客户端请求服务端的验证
	 */
	public final static String CXF_OPERATING_CHECK = "sessionINFOR";
	/**
	 * cxf webservice 客户端请求服务端的验证 默认值
	 */
	public final static String CXF_OPERATING_CHECK_DEFAULT = "sessionINFOR"; // 默认值

	public final static String YES = "Y";
	public final static String NO = "N";
	public final static String Y = "Y";
	public final static String N = "N";
	/**
	 * 是否引用
	 */
	public final static String QUOTE = "true";
	public final static String NON_QUOTE = "false";

	public final static String Flag = "Flag";
	public final static String Brief  = "Brief";
	public final static String Flop = "Flop";
	public final static String Code = "Code";
	public final static String Error = "Error";
	
	public final static String Message = "Message";
	public final static String Message_F = "Message_F";
	public final static String Message_Success = "操作成功!";
	public final static String Message_Failure = "操作失败!";
	
	//整数值数据
	public final static String INT_COUNT = "INT_COUNT"; 
	
	
	/**
	 * 导出最大记录数
	 */
	public final static int EXPORT_MAX_SIZE = 2000;

	/**
	 * 算法app id
	 */
	public final static String APP_ID_ROSTER_OPTIMIZER = "ROSTER_OPTIMIZER";

	/**
	 * 算法operator
	 */
	public final static String APP_GRD_OPT = "GRD_OPT";

	/**
	 * 时间轴区间大小
	 */
	public final static int interval = 15;
	/**
	 * 
	 * 一天开始分钟
	 * */
	public final static int STR_TIMENODE = 240;
	/**
	 * 一天开始小时
	 * */
	public final static String STR_TIME="04:00";
	/**
     * 一天开始小时
     * */
    public final static int STR_HOURS=4;
	/**
	 * 用户登录时是否启用验证码
	 */
	public static String enabledCaptcha = YES;//NO;
	/**
	 * 是否监控http日志
	 */
	public static String flagOutHttpLog = YES;

	/**
	 * 是否监控jdbc日志
	 */
	public static String flagOutJdbcLog = YES;

	/**
	 * 是否监控webService日志
	 */
	public static String flagOutWebserviceLog = YES;

	/**
	 * http正常耗时1秒
	 */
	public static long processTimeNormalHttp = 1000L;

	/**
	 * jdbc正常耗时1秒
	 */
	public static long processTimeNormalJdbc = 1000L;

	/**
	 * webservice正常耗时2秒
	 */
	public static long processTimeNormalWebservice = 2000L;

	/**
	 * 赋值
	 */
	public final static String Equal = "=";

	public final static String RCPoperator = "operator";

	// 让表数据查询不到
	public final static String whereSqlNOData = "@#@!@@@@@@*qro";

	// 旅客行为，典型周，航班号，起飞站，到达站通用占位符
	public final static String DEFAULTSTR = "#";

	/**
	 * 标题
	 */
	public final static String TITLE = "title";

	public final static String WEBoperator = "admin";

	public final static String GSMS_APP = "gsms_app";
	/**
	 * Admin角色
	 */
	public final static String ADMIN_ROLE = "admin_role";
	/**
	 * 总部管理员角色(具有所有数据的操作权限)
	 */
	public final static String ROLE_HEAD_ADMIN = "headquarters_admin_role";

	/**
	 * 数据表字段名称
	 */
	public final static String id = "ID";
	public final static String sf_id = "sf_id";
	public final static String dept_id = "dept_id";
	public final static String name = "NAME";
	public final static String NAMELIKE = "NAMELIKE";
	public final static String staffNum = "STAFF_NUM";
	public final static String STAFFNUM = "STAFFNUM";
	public final static String stuffNum = "stuffNum";
	/**
	 * 员工姓名
	 */
	public final static String STAFF_NAME = "staffName";
	public final static String cell_phone = "CELL_PHONE";
	public final static String departmentName = "DEPARTMENT_NAME";
	public final static String rankCd = "RANK_CD";
	public final static String qual_cd = "qual_cd";
	public final static String quaTypeDisplayNm = "quaTypeDisplayNm"; // DISPLAY_NM
	public final static String SKILLLEVEL = "skillLevel";																	// (GO_QUALIFICATION_TYPES_T地服排班资格信息表)
	public final static String rankTypeDisplayNm = "rankTypeDisplayNm";// DISPLAY_NM
																		// (GO_RANK_TYPES_T
																		// 地服排班员工级别(职位)信息表)
	public final static String strDt = "STR_DT";
	public final static String endDt = "END_DT";
	public final static String actStrDt = "ACT_STR_DT";
	public final static String actEndDt = "ACT_END_DT";

	public final static String DELETE_IND = "deleteInd";

	/**
	 * 员工状态:在岗
	 */
	public final static Long ON_GUARD = 0L;
	/**
	 * 员工状态:离职
	 */
	public final static Long DIMISSION = 1L;

	/**
	 * 实际开始时间
	 */
	public final static String ACT_STR_DT = "actStrDt";
	/**
	 * 实际结束时间
	 */
	public final static String ACT_END_DT = "actEndDt";
	/**
	 * 已排人数
	 */
	public final static String NUM_FIL = "numFil";
	public final static String comments = "COMMENTS";
	public final static String display_nm = "display_nm"; // DISPLAY_NM
															// (GO_QUALIFICATION_TYPES_T地服排班资格信息表)
	public final static String issue_date = "issue_date";
	public final static String exp_dt = "exp_dt";
	public final static String effDt = "effDt";
	public final static String expDt = "expDt";

	/**
	 * GMS_STAFF 坤翔用户信息表
	 */
	public final static String GMS_STAFF = "GMS_STAFF";

	/**
	 * 表示WEB系统
	 */
	public final static String RES_PLAN = "RES_PLAN";

	/**
	 * GMS_DEPARTMENT 坤翔组织结构表
	 */
	public final static String GMS_DEPARTMENT = "GMS_DEPARTMENT";

	/**
	 * GMS_STAFF 193测试数据库坤翔用户信息表
	 */
	public final static String GMS_STAFF_T = "GO_STAFF_T";

	/**
	 * GMS_DEPARTMENT 193测试数据库坤翔组织结构表
	 */
	public final static String GMS_DEPARTMENT_T = "GMS_DEPARTMENT";

	/**
	 * whereMap查询key值
	 */
	public final static String STAFFNUMS_LIKE = "staffNumsLike"; // like查询
	public final static String STAFFNUMS = "staffNums"; //
	public final static String STAFFNAMES = "staffNames";
	public final static String STAFFNAME_LIKE = "staffNameLike"; // like查询
	/**
	 * 员工号列表
	 */
	public final static String STAFF_NUM_LIST="staffNumList";

	public static String DEPT_IDS_HQL = "deptIdHql";

	/** 人员分组管理 **/
	public final static String GROUPSCD = "groupsCd";
	public final static String DEPTNAME = "deptName";
	public final static String GROUPSNM = "groupNm";
	public final static String GROUPSID = "groupId";
	public final static String EQUALDEPTNAME = "equalDeptName";
	/**
	 * 班组成员
	 */
	public final static String GROUP_MEMBERS = "groupMembers";

	/** 资格技能 **/
	public final static String QUALCD = "qualCd";
	public final static String QUALCDS = "qualCds";
	public final static String QUALCDLIKE = "qualCdlike";
	public final static String QUATYPEDISPLAYNM = "quaTypeDisplayNm";
	public final static String QUATYPEDISPLAYNMLIKE = "quaTypeDisplayNmlike";
	public final static String QUALGRPLIKE = "qualGrplike";
	public final static String QUALDESCLIKE = "qualDesclike";
	public final static String QUALCDSNOTIN = "qualCdsNotIn";
	public final static String QUALIDSNOTIN = "qualIdsNotIn";
	/**
	 * 无数据
	 */
	public final static String NODATA = "nodata";
	
	/** 定时任务管理 **/
	public final static String QRTZTRIGGERSTATE = "triggerState";
	public final static String QRTZSTARTTIME = "startTime";
	public final static String QRTZENDTIME = "endTime";
	
	/** 动态定时任务管理 **/
	public final static String JOBSTATUS = "jobStatus";
	public final static String JOBGROUP = "jobGroup";
	public final static String JOBNAME = "jobName";
	public final static String JOBBEANCLASS = "beanClass";
	public final static String JOBMETHODNAME = "methodName";
	/** 定时任务绑定 **/
	public final static String TRIGGERNAME = "triggerName";          //绑定的定时任务名称
	public final static String BINDPACKAGENAME = "bindPackageName";  //绑定的包名
	public final static String BINDCLASSNAME = "bindClassName";      //绑定的类名
	public final static String BINDMETHODNAME = "bindMethodName";    //绑定的方法名
	
	public static final int REPEAT_COUNT = 9999999;//简单定时器的重复次数	

	/** 部门科室 **/
	public final static String DEPARTMENT_NAME = "departmentNm";
	/**
	 * 科室ID
	 */
	public final static String DEPT_ID = "deptId";
	public final static String DEPT_IDS = "deptIds";
	public final static String DEPARTMENT_NAMELIKE = "departmentNmlike";
	public final static String DEPT_IDLIKE = "deptIdlike";
	public final static String DEPT_LEVELTYPE = "levelType";
	/**
	 * 查询类型
	 */
	public final static String QUERY_TYPE = "queryType";
	/**
	 * 所有
	 */
	public final static String ALL = "all";
	/**
	 * 当前登录用户查看的日志操作类型
	 */
	public final static String LOG_OPER_TYPE = "logOperType";
	/**
	 * 当前登录用户有权查看的所有第4级(科室级别)部门
	 */
	public final static String FOUR_LEVEL_DEPTS = "fourLevelDepts";

	/** 职位级别 **/
	public final static String RANK_CD = "rankCd";
	public final static String RANK_CDS = "rankCds";
	public final static String RANKTYPES_DISPLAYNM = "rankTypesDisplayNm";
	public final static String RANKTYPES_DISPLAYNMLIKE = "rankTypesDisplayNmLike";

	/** 任务代码 **/
	public final static String TASKCD = "taskCd1";// 基础数据任务资格关系
	/**
	 * 任务代码字符串，用,分隔，用于sql in子句
	 */
	public final static String TASKCDS = "taskCds";
	public final static String TASKCODENM = "taskCdNm";
	public final static String TASKCODENMLIKE = "taskCdNmlike";
	public final static String TASKCODENMS = "taskCdNms";
	public final static String STDWEEKFLAG = "stdWeekFlag";
	/**
	 * 任务类型代码
	 */
	public final static String TASK_TYPE_CD = "taskTypeCd";
	/**
	 * 任务代码
	 */
	public final static String TASK_CD = "taskCd";
	/**
	 * 航空公司代码
	 */
	public final static String ALNCD = "alnCd";
	/**
	 * 航班号
	 */
	public final static String FLTNUM = "fltNum";

	public final static String RELATION = "relation";
	/** 行政级别 **/
	public final static String RANKLEVEL = "ranklevel";
	/** 行政级别 所有级别**/
	public final static String RANKLEVEL_ALL = "-1";
	/** 英語等級**/
	public final static String ENGLISHLEVEL = "englishlevel";
	/** 英語等級 所有等級**/
	public final static String ENGLISHLEVEL_ALL = "-1";

	/** 成本信息 **/
	public final static String COST_NAME = "costNm";
	public final static String COST_DESC = "costDesc";
	public final static String MESG_DESC = "mesgDesc";
	public final static String COST_VALUE = "costValue";
	public final static String IS_SHARED = "isShared";

	/**
	 * 界面tab标识
	 */
	public final static String TAB_01 = "TAB_01"; // 服务标准界面标识01
	public final static String TAB_02 = "TAB_02"; // 服务标准界面 标识02
	public final static String TAB_03 = "TAB_03"; // 服务标准界面标识03
	public final static String TAB_04 = "TAB_04"; // 服务标准界面标识04
	public final static String TAB_05 = "TAB_05"; // 服务标准界面标识05

	public final static String CAN = "CAN";// CAN 机场
	public final static String PEK = "PEK";// PEK 机场
	public final static String D = "D";// 国内
	public final static String I = "I";// 国际
	
	public final static String DEPART = "D";// 出港
	public final static String ARRIVE = "A";// 进港
	
	// 质技级别
	/**
	 * 高级
	 */
	public final static String HIGHLEVEL = "H";
	/**
	 * 中级
	 */
	public final static String MIDLEVEL = "M";
	/**
	 * 初级
	 */
	public final static String LOWLEVEL = "L";
	// jpql
	/**
	 * jpql占位符
	 */
	public final static String PLACE_HOLDER_ONE = "%{0}%";
	/**
	 * 方案ID
	 */
	public final static String SCENARIO_ID = "scenarioId";
	/**
	 * 默认方案ID
	 */
	public final static Long OFFICIAL_SCENARIO_ID = 999999999l;
	/**
	 * 排班次生成工作量的方案ID
	 */
	public final static Long OFFICIAL_SHIFT_SCENARIO_ID = 999999998l;
	/**
	 * 排班结果未发布
	 */
	public final static Integer UN_PUBLISHED = 0;
	/**
	 * 排班结果已发布
	 */
	public final static Integer PUBLISHED = 1;
	
	/**
	 * 发布任务
	 */
	public final static Integer PUBLISHTASK = 3;
	
	/**
	 * 发布选班
	 */
	public final static Integer PUBLISHSELECTSHIFT = 4;
	
	/**
	 * 发布班次
	 */
	public final static Integer PUBLISHSHIFT = 2;
	/**
	 * 某员工某日第一次发布排班结果
	 */
	public final static Integer PUBLISH_FIRST = 1;

	/**
	 * 此次发布成功
	 */
	public final static String PUBLISH_RESULT_FLAG_SUCCEED = "1";
	/**
	 * 此次发布失败
	 */
	public final static String PUBLISH_RESULT_FLAG_FAIL = "0";
	/**
	 * 排班覆盖率报表导出总数
	 */
	public final static String EXPORT_COUNT = "exportCount";

	/**
	 * 国际服务科排任务
	 */
	public final static int GJFW_SCHEDULE_TASK = 1;
	/**
	 * 国际值机科计划排班
	 */
	public final static int GJZJ_SCHEDULE_SHIFT = 2;
	/**
	 * 国际值机科二次优化
	 */
	public final static int GJZJ_RE_SCHEDULE = 3;

	/**
	 * 中转科计划排班
	 */
	public final static int ZZK_SCHEDULE_SHIFT = 4;
	
	
	/**
	 * 国内值机科计划排班
	 */
	public final static int GNZJ_SCHEDULE_SHIFT = 5;
	
	/**
	 * 国内服务科计划排班
	 */
	public final static int GNFW_SCHEDULE_SHIFT = 6;
	
	/**
	 * 预测排班
	 */
	public final static int YUCE_SCHEDULE_SHIFT = 7;
	
	/**
	 * 国际服务科排班次
	 */
	public final static int GJFW_SCHEDULE_SHIFT = 8;
	
	/**
	 * 国内服务科排任务
	 */
	public final static int GNFW_SCHEDULE_TASK = 9;

	/**
	 * 取消自动排班
	 */
	public final static int CANCEL_SCHEDULE = 101;
	
	/**
	 * 正在删除工作量
	 */
	public final static int AS_DELETE_WORKPATTERN = 103;
	/**
	 * 排班状态刷新时间（单位毫秒）
	 */
	public final static int SCHEDULE_STATE_REFRESH_TIME = 100000;

	/**
	 * 将要添加到数据库中
	 */
	public final static String ADD = "add";
	/**
	 * 将要更新到数据库中
	 */
	public final static String UPDATE = "update";
	/**
	 * 将要从数据库中删除
	 */
	public final static String DELETE = "delete";
	/********** 自动排班状态常量 **********/

	/**
	 * 没有工作状态
	 */
	public static final Integer AS_NOWORK_TYPE = 99;
	/**
	 * 初始化启动
	 */
	public static final Integer AS_STARTED_TYPE = 0;
	
	/**
	 * 正在生成工作量
	 */
	public static final Integer AS_WORK_PATTERN = 205;
	
	/**
	 * 工作量已完成
	 */
	public static final Integer AS_WORK_PATTERN_SUCCESS = 215;
	
	/**
	 * 生成工作量出错
	 */
	public static final Integer AS_WORK_PATTERN_ERROR = 210;
	
	/**
	 * 加载数据
	 */
	public static final Integer AS_LOADDATA_TYPE = 1;
	/**
	 * 列生成
	 */
	public static final Integer AS_COLGEN_TYPE = 2;
	/**
	 * 求解
	 */
	public static final Integer AS_SOLVE_TYPE = 3;
	/**
	 * 解析及保存结果
	 */
	public static final Integer AS_PARSEANDSAVERESULT_TYPE = 4;
	/**
	 * 完成
	 */
	public static final Integer AS_COMPLETED_TYPE = 5;
	/**
	 * 等待
	 */
	public static final Integer AS_WAITING_TYPE = 6;
	/**
	 * 被中止
	 */
	public static final Integer AS_STOPPED_TYPE = 7;
	/**
	 * 错误
	 */
	public static final Integer AS_ERROR_TYPE = 8;
	/**
	 * 没有对应的算法服务
	 */
	public static final Integer AS_NOCALCSERVER = 100;
	/**
	 * 排队中
	 */
	public static final Integer AS_INLIST = 101;

	/**
	 * 正在等待
	 */
	public static final Integer AS_WAIT_TYPE = 105;
	
	/**
	 * 已取消
	 */
	public static final Integer AS_CANCEL = 102;

	/**
	 * 算法服务无响应(Socket连接异常)
	 */
	public static final Integer AS_SERVICE_NO_RESPONSE = 200;

	/**
	 * 系统异常
	 */
	public static final Integer AS_SYSTEM_ERROR = 201;
	
	//当前审批状态(S：未提交；I：审批中；P：审批通过；F：审批未通过；U：无需审批；R：已撤销；O：已过期)
	@SuppressWarnings("serial")
	public static final Map<String, String> CHANGE_SHIFT_RESULt = new HashMap<String, String>() {
		{
			put("N", "未提交");
			put("I", "审批中");
			put("P", "审批通过");
			put("F", "审批未通过");
			put("U", "无需审批");
			put("R", "已撤销");
			put("O", "已过期");
		}
	};
	/**
	 * 自动排班状态文字描述(界面显示用)
	 */
	@SuppressWarnings("serial")
	public static final Map<Integer, String> AUTO_SCH_RESULt = new HashMap<Integer, String>() {
		{

			put(AS_STARTED_TYPE, "加载数据");//0
			put(AS_LOADDATA_TYPE, "加载数据");//1
			put(AS_COLGEN_TYPE, "正在排班");//2
			put(AS_SOLVE_TYPE, "正在排班");//3
			put(AS_PARSEANDSAVERESULT_TYPE, "正在排班");//4
			put(AS_COMPLETED_TYPE, "排班结束");//5
			/*
			 * put(AS_WAITING_TYPE,"等待"); put(AS_STOPPED_TYPE,"被中止");
			 */
			put(AS_ERROR_TYPE, "排班出错");//8
			put(AS_NOWORK_TYPE, "排班出错");//99
			put(AS_NOCALCSERVER, "正在排队");//100
			put(AS_INLIST, "正在排队");//101
			put(AS_CANCEL, "已取消");//102
			put(AS_SERVICE_NO_RESPONSE, "算法服务无响应");//200
			put(AS_SYSTEM_ERROR, "系统错误");//201
			
			put(AS_WAIT_TYPE, "正在等待");//105
			put(AS_WORK_PATTERN, "正在生成工作量");//205
			put(AS_WORK_PATTERN_ERROR, "生成工作量出错");//210
			put(AS_WORK_PATTERN_SUCCESS, "工作量已完成");//215
			put(AS_DELETE_WORKPATTERN, "正在删除工作量");//103
		}
	};
	/**
	 * 休假管理界面需要的班次类型
	 */
	@SuppressWarnings("serial")
	public static final Map<String, String> VACATION_SHIFT_TYPE = new LinkedHashMap<String, String>() {
		{
			put("ANNUAL", "年休假");
			put("HOME", "探亲假");
			put("MARRIAGE", "婚假");
			put("PREGNANCY", "孕期病假");
			put("MATERNITY", "产假");

			put("FEEDING", "哺乳期假");
			put("WORKSICK", "工伤假");
			put("SICK", "病假");
			put("AFFAIR", "事假");
			put("FUNERAL", "丧假");
			put("REST", "休息");
			put("BIRTH","生日假");
			put("BACKUP", "备份假");
			put("11", "普通班次");
		}
	};
	/**
	 * 外勤管理界面需要的班次类型
	 */
	@SuppressWarnings("serial")
	public static final Map<String, String> TRAINING_SHIFT_TYPE = new LinkedHashMap<String, String>() {
		{
			put("BUSINESSTR", "公差");
			put("TRAINING", "培训");
			put("CONFERENCE", "会议");
			put("SUPPORT", "支援主任");
		}
	};

	/********** 自动排班状态常量****结束 ******/

	/**
	 * 排班编辑AppId
	 */
	public static final String ROSTER_EDITOR_APPID = "ROSTER_EDITOR";
	/**
	 * 班次新增标志
	 */
	public static final String SHIFT_ADD = "shiftAdd";
	/**
	 * 班次更新标志
	 */
	public static final String SHIFT_UPDATE = "shiftUpdate";
	/**
	 * 任务新增标志
	 */
	public static final String TASK_ADD = "taskAdd";
	/**
	 * 任务更新标志
	 */
	public static final String TASK_UPDATE = "taskUpdate";

	/**
	 * 盐值
	 */
	public static final String ROST_APP_SALT = "rost-web-salt";

	/**
	 * 密码
	 */
	public static final String ROST_APP_PW = "tsoR@@2018";

	/**
	 * 锁班
	 */
	public static final String LOCKED = "Y";
	/**
	 * 未锁班
	 */
	public static final String UN_LOCKED = "N";
	/**
	 * 任务类型名第一部分
	 */
	public static final String TYPE_NM_PART_ONE="typeNmPart1";
	/**
	 * 任务类型名第二部分
	 */
	public static final String TYPE_NM_PART_TWO="typeNmPart2";
	
	/**
	 * 报表map title key
	 */
	public static final String REPORT_MAPKEY_TITLE = "title";
	
	/**
     * 报表map content key
     */
	public static final String REPORT_MAPKEY_CONTENT = "content";
	
	/**
	 * web端登录标识(写日志用)
	 */
	public static final String WEB_LOGIN = "WEB_LOGIN";
	/**
	 * 航班计划
	 */
	public static final String  FLIGHT_PLAN = "flightPlan";
	/**
	 * 典型周
	 */
	public static final String  TYPICAL_WEEK = "typicalWeek";
	/**
	 * 航班动态
	 */
	public static final String  FLIGHT_ACTIVITY = "flightActivity";
	
	/**
	 * 航班动态-预计
	 */
	public static final String  FLIGHT_ACTIVITY_PRE = "flightActivityPre";
	
	/**
	 * 国内服务科在数据库时间点配置
	 */
	public static final String  GNFWK_TIME_TYPE = "GNFWK_TIME_TYPE";
	
	/**
	 * 国际服务科在数据库时间点配置
	 */
	public static final String  GJFWK_TIME_TYPE = "GJFWK_TIME_TYPE";
	/**
	 * 航班服务标准
	 */
	public static final String FLIGHT_SERVICE_STANDARD = "FLIGHT_SERVICE_STANDARD";
	/**
	 * 固定任务标准
	 */
	public static final String FIXED_SERVICE_STANDARD = "FIXED_SERVICE_STANDARD";
	/**
	 * 旅客服务标准
	 */
	public static final String PASSENGER_SERVICE_STANDARD = "PASSENGER_SERVICE_STANDARD";
	/**
	 * 班次分类
	 */
	public static final String ROSTER_CATEGORY = "ROSTER_CATEGORY";
	/**
	 * 班次分类-上班
	 */
	public static final String ROSTER_CATEGORY_WORK = "ROSTER_CATEGORY_WORK";
	/**
	 * 班次分类-休假
	 */
	public static final String ROSTER_CATEGORY_VACATION = "ROSTER_CATEGORY_VACATION";
	/**
	 * 班次分类-外勤
	 */
	public static final String ROSTER_CATEGORY_LEGWORK = "ROSTER_CATEGORY_LEGWORK";
	/**
	 * 班次分类-休息
	 */
	public static final String ROSTER_CATEGORY_REST = "ROSTER_CATEGORY_REST";
	/**
	 * 无效停桥
	 */
	public static final String INVALID_STAND_BRIDGE = "INVALID_STAND_BRIDGE";
	/**
	 * 无效登机口
	 */
	public static final String INVALID_GATENOS = "INVALID_GATENOS";
	/**
	 * 无效停机位
	 */
	public static final String INVALID_STANDNOS = "INVALID_STANDNOS";
	/**
	 * 允许访问灵活报表状态
	 */
	public static final Long ACCESS_ALLOWED = 1l;
	/**
	 * 不允许访问灵活报表状态
	 */
	public static final Long ACCESS_FORBIDDEN = 0l;
	/**
	 * ROST会话注销状态
	 */
	public static final Long ACCESS_LOGOUT = 2l;
	public static final String OPER_TYPE_FREEREPORT = "REPORT_LOGIN";
	public static final String OPER_DESC_FREEREPORT = "\u7075\u6d3b\u62a5\u8868\u5355\u70b9\u767b\u5f55\u65e5\u5fd7";
	public static final String DEFAULT_SPLITER = ",";
	/**
	 * 当前审批状态(F：审批未通过；R：已撤销；O：已过期);
	 */
	public static final String []APPROVAL_FLAG = {"R","O","F"};
	
	//-------------考勤查询，修正模板
	/** 修正状态 */
	public static final String CORRECT_STATUS_TEMPLATE = "修正状态:{0}->{1}|";
	/** 修正时间 */
	public static final String CORRECT_TIME_TEMPLATE = "修正时间:{0}->{1}|";
	/** 缺卡补录 */
	public static final String MISSCARD_MAKEUP_TEMPLATE = "缺卡补录:{0}|";
	//-------------考勤审批，修正模板
	/** 申请修正 */
	public static final String APPLY_CORRECT_TEMPLATE = "申请修正:{0}->{1}|";
	/** 加班审批 */
	public static final String OVERTIME_EXAMINE_TEMPLATE = "加班审批:加班{0}小时|";
	/** 考勤异常审批 */
	public static final String ABNORMAL_ATTENDANCE_TEMPLATE = "考勤异常审批:{0}打卡{1}小时|";
	/** 申请补录 */
	public static final String APPLY_MAKEUP_TEMPLATE = "申请补录:{0}|";
	//--------------审批参数模板
	public static final String SHIFT_CONTENT_TEMPLATE = "{0}-{1}({2})";
	
	public static final String BATCH_TIP_TEMPLATE = "员工{0}，{1}审批处理失败：{2}";
	//-------------打卡时间
	public static final String FIRST_STR_TIME = "firstStrTime";
	public static final String FIRST_END_TIME = "firstEndTime";
	
	public static final String SECOND_STR_TIME = "secondStrTime";
	public static final String SECOND_END_TIME = "secondEndTime";
	
	public static final String THREE_STR_TIME = "threeStrTime";
	public static final String THREE_END_TIME = "threeEndTime";
	
	
	
	/** 工时转换 */
	public static String getHoursAMinutes(double seconds) {
		String hr = (long)(seconds/(60*60))+"小时";
		String mt = (long)(seconds%(60*60))==0?"":(long)(seconds%(60*60))/60+"分钟";
		String hAm = hr+mt;
		return hAm;
	}
	
	/**
	 * 考勤查询 start
	 */
	public static final String WORK_STATISTICS_STATUS = "WORK_STATISTICS_STATUS";//考勤状态
	
	public static final String NORMAL = "NORMAL";//正常
	public static final String LATE_FOR_WORK = "LATE_FOR_WORK";//迟到
	public static final String LEAVE_EARLY = "LEAVE_EARLY";    //早退
	public static final String NO_CARD_STR_WORK = "NO_CARD_STR_WORK";//上班缺卡
	public static final String NO_CARD_END_WORK = "NO_CARD_END_WORK";//下班缺卡
	public static final String ABSENT_HALF_DAY = "ABSENT_HALF_DAY";  //旷工半天
	public static final String ABSENT = "ABSENT";//旷工
	public static final String OUTSIDE_OFFICE = "OUTSIDE_OFFICE";//外勤
	public static final String REST_FOR_WORK = "REST_FOR_WORK";  //休息
	public static final String VACATION_FOR_WORK = "VACATION_FOR_WORK";//休假
	public static final String NO_CARD = "NO_CARD";//未打卡
	public static final String NO_SCHEDULING = "NO_SCHEDULING";//未排班
	public static final String ABNORMAL_ATTENDANCE = "ABNORMAL_ATTENDANCE";//考勤异常
	public static final String OVERTIME_FOR_WORK = "OVERTIME_FOR_WORK";    //加班
	//新维护的状态
	public static final String ABNORMAL_ATTENDANCE_LEAVE_EARLY = "ABNORMAL_ATTENDANCE_LEAVE_EARLY";//考勤异常-早退
	public static final String ABNORMAL_ATTENDANCE_OVERTIME_FOR_WORK = "ABNORMAL_ATTENDANCE_OVERTIME_FOR_WORK";//考勤异常-加班
	public static final String LATE_FOR_WORK_ABSENT_HALF_DAY = "LATE_FOR_WORK_ABSENT_HALF_DAY";//迟到-旷工半天
	public static final String LATE_FOR_WORK_LEAVE_EARLY = "LATE_FOR_WORK_LEAVE_EARLY";//迟到-早退
	public static final String LATE_FOR_WORK_OVERTIME_FOR_WORK = "LATE_FOR_WORK_OVERTIME_FOR_WORK";//迟到-加班
	public static final String LATE_FOR_WORK_ABNORMAL_ATTENDANCE = "LATE_FOR_WORK_ABNORMAL_ATTENDANCE";//迟到-考勤异常
	public static final String OVERTIME_FOR_WORK_ABSENT_HALF_DAY = "OVERTIME_FOR_WORK_ABSENT_HALF_DAY";//加班-旷工半天
	public static final String OVERTIME_FOR_WORK_LEAVE_EARLY = "OVERTIME_FOR_WORK_LEAVE_EARLY";//加班-早退
	public static final String OVERTIME_FOR_WORK_ABNORMAL_ATTENDANCE = "OVERTIME_FOR_WORK_ABNORMAL_ATTENDANCE";//加班-考勤异常
	public static final String ABSENT_HALF_DAY_LEAVE_EARLY = "ABSENT_HALF_DAY_LEAVE_EARLY";//旷工半天-早退
	public static final String ABSENT_HALF_DAY_OVERTIME_FOR_WORK = "ABSENT_HALF_DAY_OVERTIME_FOR_WORK";//旷工半天-加班
	
	public static final String MODIFY_FLAG_STR = "0";//修改了上班状态
	public static final String MODIFY_FLAG_END = "1";//修改了下班状态
	public static final String MODIFY_FLAG_ALL = "2";//上班、下班都修改了
	
	public static final String VERY_EARLY_SHIFT = "VERY_EARLY_SHIFT";//极早班
	public static final String VERY_LATE_SHIFT = "VERY_LATE_SHIFT";//极晚班
	public static final String OVER_NIGHT_SHIFT = "OVER_NIGHT_SHIFT";//通宵班
	
	public static final String WORK_STATISTICS_DEPT = "WORK_STATISTICS_DEPT";//考勤统计部门
	
	public static final String FIRST_SHIFT = "1";//第一班次
	public static final String SECOND_SHIFT = "2";//第二班次
	public static final String THREE_SHIFT = "3";//第三班次
	
	//考勤参数
	public static final String A_LATE_START_TIME = "A";          //迟到开始时间
	public static final String B_LATE_END_TIME = "B";            //迟到结束时间
	public static final String C_LEAVE_EARLY_START_TIME = "C";   //早退开始时间
	public static final String D_LEAVE_EARLY_END_TIME = "D";     //早退结束时间
	public static final String E_BEFORE_ABNORMAL_WORK_TIME = "E";//班前异常时间
	public static final String F_AFTER_ABNORMAL_WORK_TIME = "F"; //班后异常时间
	
	
	/**********************************************   考勤查询 start   *******************************************/
	
	/**********************************************   典型周对接-实时派工 start   *******************************************/
	public static final String STANDARD_WEEKS_SERVICE = "S";//服务标准
	public static final String APP_TASK_ASSIGN_INFO = "A";//实时派工
	
	public static final String FLT_DOMESTIC = "D";      //国内
	public static final String FLT_INTERNATIONAL = "I"; //国际
	public static final String FLT_DOMESTIC_INTERNATIONAL = "DI";//生成两条
	public static final String FLT_INTERNATIONAL_DOMESTIC = "ID";//生成两条
	public static final String FLT_UNFILTERED = "#";    //不过滤
	
	
	
	/**********************************************   典型周对接-实时派工 end   *********************************************/
	
	/**
	 * 值机工作量自动下载的科室ID
	 */
	public static final String CHECK_WORK_LOAD_DEPT = "CHECK_WORK_LOAD_DEPT";
	
	/**********************************************   中转接机任务统计 start   *********************************************/
	/** 敲门任务 */
	public static final String PU_QM = "PU_QM";
	/** 过站任务 */
	public static final String PU_GZ = "PU_GZ";
	/** 无伴任务 */
	public static final String PU_TF_UM_AR = "PU_TF_UM_AR";
	/** 临时任务 */
	public static final String PU_LS = "PU_LS";
	/** 统计日期 */
	public static final String STATISTIC_DATE = "statisticDate";
	/** 员工号 */
	public static final String STAFF_NUM = "staffNum";
	
	/**********************************************   中转接机任务统计 start   *********************************************/
	
	/**********************************************   考勤设备监控 start   *********************************************/
	public static final String WA_ABNORMAL_WARNED_TIME_RANGE = "WA_ABNORMAL_WARNED_TIME_RANGE";
	public static final String WARNED_TIME_START_TIME = "abnormal_warned_start_time";
	public static final String WARNED_TIME_END_TIME = "abnormal_warned_end_time";
	/**********************************************   考勤设备监控 end   *********************************************/

}
