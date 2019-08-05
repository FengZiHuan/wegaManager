 <%@ page language="java" import="com.nantian.iwap.web.WebEnv" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>密码修改</title>
<meta name="description" content="">
<!-- 一般查询页面所引入的样式文件 -->
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/css.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/style.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/iwapui-style.css" rel="stylesheet" type="text/css">
<!-- JQ必须在最JS上面 -->
<script src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
<script src="<%=request.getContextPath() %>/js/UtilTool.js"></script>
<script src="<%=request.getContextPath() %>/js/Form.js"></script>
<script src="<%=request.getContextPath() %>/js/TextField.js"></script>
<script src="<%=request.getContextPath() %>/js/ListField.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/iwapGrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/iwapui.js"></script>
<style type="text/css">
table template{display: none;}
</style>
</head>
<body class="iwapui center_body">
<div class="subnav_box">
	<ul class="breadcrumb">
		<li class="active">密码修改</li>
	</ul>
</div>
<!-- 页面查询区域开始 -->
<form id="Conditions" class="clearfix" action="iwap.ctrl" method="post">
	<div class="col-md-8 fl col-sm-offset-4">
	  	<div class="inputbox pr" data-iwap="tooltipdiv"  data-iwap-empty="false" data-iwap-tooltext="输入不能为空且应为1至6位数" data-iwap-minlength="1" data-iwap-maxlength="12">
	  	<input name="srcCode" type="hidden"  id="srcCode" readOnly class="input_text" value="${errorCode==null?srcCode:errorCode}">
	  	<input name="txcode" type="hidden"  id="txcode" readOnly class="input_text" value="modifyPwd">
	  	<input name="actionId" type="hidden"  id="actionId" readOnly class="input_text" value="modify">
	  			<span>用户ID:</span><input name="acctId" type="text" data-iwap-xtype="TextField" id="acctId"  class="input_text" value="${userInfo.ACCT_ID}">
	  	</div>
	 </div>
	 <div class="col-md-8 fl col-sm-offset-4">
	  	<div class="inputbox tl pr" data-iwap="tooltipdiv" data-iwap-empty="true" >
		  		<span>用户名称:</span><input name="acctNm" type="text" data-iwap-xtype="TextField" id="acctNm"  class="input_text" value="${userInfo.ACCT_NM}">
		</div>
	 </div>
	 <div class="col-md-8 fl col-sm-offset-4">
	    <div class="inputbox pr" data-iwap="tooltipdiv" data-iwap-empty="true">
		  		<span>原密码:</span><input name="orgPwd" type="password" data-iwap-xtype="TextField" data-iwap-allowBlank=false id="orgPwd" class="input_text" value="">
		 </div>
	 </div>
	<div class="col-md-8 fl col-sm-offset-4">
	    <div class="inputbox pr" data-iwap="tooltipdiv" data-iwap-empty="true">
		  		<span>新密码:</span><input name="newPwd" type="password" data-iwap-xtype="TextField" data-iwap-allowBlank=false id="newPwd" class="input_text" value="">
		 </div>
	 </div>
	 <div class="col-md-8 fl col-sm-offset-4">
	    <div class="inputbox pr" data-iwap="tooltipdiv" data-iwap-empty="true">
		  		<span>确认密码:</span><input name="cfmPwd" type="password" data-iwap-xtype="TextField" data-iwap-allowBlank=false id="cfmPwd" class="input_text" value="">
		 </div>
	 </div>
	 <div class="col-md-8 fl col-sm-offset-4">
	    <div class="">
		  		<span style="color: red">${errorCode!='login-err-006'?errorMsg:''}</span>
		 </div>
	 </div>
</form>
<div class="tc mb14">
	<a href="javaScript:void(0)" class="btn btn-primary mr30" id="query" onclick="modifyPwd()">提交</a>
</div>
</body>
<script>
var condionForm=null;
$(document).ready(function() {
	condionForm=$.IWAP.Form({'id':'Conditions'});
});

function modifyPwd(){
	if(condionForm.validate()){
		$("#Conditions").submit();
	}
}


</script>
 <input type="hidden" id="sessionId" value="<%=session.getId() %>" name="sessionId">
</html>