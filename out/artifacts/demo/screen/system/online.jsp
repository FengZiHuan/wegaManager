 <%@ page language="java" import="com.nantian.iwap.web.WebEnv" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>在线用户管理</title>
<meta name="description" content="">
<!-- 一般查询页面所引入的样式文件 -->
<link href="<%=request.getContextPath()%>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/iwapui-style.css" rel="stylesheet" type="text/css">
<!-- JQ必须在最JS上面 -->
<script src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/js/UtilTool.js"></script>
<script src="<%=request.getContextPath()%>/js/Form.js"></script>
<script src="<%=request.getContextPath()%>/js/TextField.js"></script>
<script src="<%=request.getContextPath()%>/js/ListField.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/iwapGrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/iwapui.js"></script>
</head>
<body class="iwapui center_body">
<div class="subnav_box">
	<ul class="breadcrumb">
		<li><a href="javaScript:void(0);">首页</a></li>
		<li><a href="javaScript:void(0);">系统维护</a></li>
		<li><a href="javaScript:void(0);">在线管理</a></li>
		<li class="active">查询</li>
	</ul>
</div>
<!-- 页面查询区域开始 -->
<form id="Conditions" class="clearfix">
	<div class="col-md-6 fl">
	  	<div class="inputbox pr" data-iwap="tooltipdiv"  data-iwap-empty="false" data-iwap-tooltext="输入不能为空且应为1至6位数" data-iwap-minlength="1" data-iwap-maxlength="12">
	  			<span>用户ID:</span><input name="operid" type="text" data-iwap-xtype="TextField" id="operid" class="input_text" value="">
	  	</div>
	 </div>
	 <div class="col-md-6 fl">
	  	<div class="inputbox tl pr" data-iwap="tooltipdiv" data-iwap-empty="true" >
		  		<span>用户名称:</span><input name="operName" type="text" data-iwap-xtype="TextField" id="operName" class="input_text" value="">
		</div>
	 </div>
</form>
<div class="tc mb14">
	<a href="javaScript:void(0)" class="btn btn-primary mr30" id="query" onclick="iwapGrid.doQuery()">查询</a>
	<a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_clear" onclick="iwapGrid.doReset();">清空</a>
</div>
<!-- 页面查询区域　END -->
<!-- 查询内容区域　开始 -->
<div class="table_box">
	<!-- 表格工具栏　END -->
	<table id="iwapGrid" class="iwapGrid table table-bordered table-striped table-hover" data-iwap="grid" data-iwap-id="" data-iwap-param="" data-iwap-pagination="true">
		<tr>
			<th data-grid-name="ACCT_ID" primary="primary" data-order="">
			<input type="checkbox" name="selectname" selectmulti="selectmulti" value="">
			<s><input type="checkbox" selectmulti="selectmulti" value="{{value}}"></s></th>
			<th data-grid-name="ACCT_ID" class="tl">用户ID</th>
			<th data-grid-name="ACCT_NM">用户名称</th>
			<th data-grid-name="LAST_TIME">最后访问时间</th>
			<th data-grid-name="IP_ADDRESS">IP地址</th>
			<th data-grid-name="OPER_ID" option="option" option-html=''><span>操作</span><s><a class="editId" onclick="edit(this)">注销</a></s></th>
		</tr>
	</table>
</div>
<!-- 查询内容区域　END -->
</body>
<script type="text/javascript">
var actionType="", iwapGrid=null,condionForm=null,operForm=null;

$(document).ready(function() {
	//重置按钮事件
	operForm=$.IWAP.Form({'id':'dialogarea'});
	condionForm=$.IWAP.Form({'id':'Conditions'});
	$('#reset').click(function() {
		operForm.reset();
	});
	/*查询表格初始化  设置默认查询条件*/
	var fData={'operid':'%','operName':'%','actionId':'doBiz','start':0,limit:10,'txcode':'online'};
	iwapGrid = $.IWAP.iwapGrid({
		mode:'server',
		fData:fData,
		Url:'<%=request.getContextPath()%>/iwap.ctrl',
		grid:'grid',
		form:condionForm,
		renderTo:'iwapGrid'
	});
});
//注销
function edit(obj){
	$('#myModal').dialog("修改柜员");
	actionType="save";
	operForm.reset();
	operForm.setData(iwapGrid.getCurrentRow());
	operForm.disabledById("OPER_ID,ORG_ID,ORG_NM");
};
</script>
</html>