 <%@ page language="java" import="com.nantian.iwap.web.WebEnv" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>日志查询</title>
<meta name="description" content="">
<%@include file="/screen/comm/header.jsp" %>
<link href="<%=path %>/css/style.css" rel="stylesheet">
<script type="text/javascript" src="<%=path %>/js/iwapui.js"></script>
<script type='text/javascript' src="<%=path %>/js/dictionary.js"></script>
<style type="text/css">
.t_string{
	text-align: left
}
.t_number{
	text-align: right
}
.t_date{
	text-align: center
}
</style>
</head>
<body class="iwapui center_body">
<form id="Conditions" class="clearfix">
		<div class="col-md-6 fl">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>交易码:</span><input name="tx_cd" type="text"
					data-iwap-xtype="TextField" id="tx_cd" class="input_text" value="">
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>用户ID:</span><input name="usr_id" type="text"
					data-iwap-xtype="TextField" id="usr_id" class="input_text"
					value="">
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>错误码:</span><input name="err_code" type="text"
					data-iwap-xtype="TextField" id="err_code" class="input_text"
					value="">
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
		<table id="iwapGrid"
			class="mygrid table table-bordered table-striped table-hover"
			data-iwap="grid" data-iwap-id="" data-iwap-param=""
			data-iwap-pagination="true">
			<tr>
				<th data-grid-name="TX_CD" class="tl">交易码</th>
				<th data-grid-name="OP_CODE">操作码</th>
				<th data-grid-name="TX_FLAG">交易标志</th>
				<th data-grid-name="ACCT_ID">用户ID</th>
				<th data-grid-name="TX_DT"  data-grid-render="dateFormat">交易时间</th>
				<th data-grid-name="REQ_TM" data-grid-render="dateTimeFormat">请求时间</th>
				<th data-grid-name=ERR_CODE>错误码</th>
				<th data-grid-name=ERR_MSG>错误信息</th>
			</tr>
		</table>
	</div>
</body>
<script>
var actionType="", iwapGrid=null,condionForm=null,operForm=null;
$(document).ready(function() {
		//重置按钮事件
		operForm=$.IWAP.Form({'id':'dialogarea'});
		condionForm=$.IWAP.Form({'id':'Conditions'});
		$('#reset').click(function() {
			operForm.reset();
		});
		$('#resetDel').click(function() {
			$('input').not('#acct_id').val(''); 
			$('select#acct_status').val('1');
		});
		
		/*查询表格初始化  设置默认查询条件*/
		var fData={'actionId':'logQuery','start':'0','limit':'10','txcode':'logQry'};
		iwapGrid = $.IWAP.iwapGrid({
			mode:'server',
			fData:fData,
			Url:'iwap.ctrl',
			grid:'grid',
			form:condionForm,
			renderTo:'iwapGrid'
		});	
});
function dateFormat(val,row_data){
	var d=new Date(val);
	return d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
}
function dateTimeFormat(val,row_data){
	var d=new Date(val);
	return d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
}
</script>

</html>