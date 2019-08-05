<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/screen/comm/header.jsp" %>
<link href="<%=path %>/css/style.css" rel="stylesheet">
<link href="<%=path %>/css/zTreeStyle.css" rel="stylesheet">
<script type="text/javascript" src="<%=path %>/js/iwapui.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="<%=path %>/js/Tree.js"></script>
<script type='text/javascript' src="<%=path %>/js/dictionary.js"></script>
<script type='text/javascript' src="<%=path %>/js/public.js"></script>
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
<title>用户管理</title>
</head>
<body class="iwapui center_body">
	<input type="hidden" value="${userInfo.ORG_ID}" id="_deptid">
	<input type="hidden" value="${userInfo.ACCT_ID}" id="_userid">
	<!-- 对话框开始 -->
	<div id="divDialog" class="divDialog">
		<div class="bg"></div>
		<div class="dialog" id="myModal" style="width: 600px;">
			<div class="dialog-header">
				<button type="button" data-dialog-hidden="true" class="close">
					<span>×</span>
				</button>
				<h4 class="dialog-title">增加偏好</h4>
			</div>
			<div class="modal-body">
				<form method="post" onsubmit="return false" id="dialogarea">
					<!-- form开始 -->
					<div class="col-md-12">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="柜员ID非空且长度大于1" data-iwap-minlength="1">
							<span>用户ID:</span> 
							<input name="ACCT_ID"   disabled="disabled" type="text"  data-iwap-xtype="TextField" id="acct_id"
							 class="input_text" >
								<a href='javascript:void(0)' class='btn btn-primary' onclick='dialogModal("myModa2")'></a>
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="所属机构不能为空">
							<span>机构编号:</span> 
							<input name="ORG_ID"  disabled="disabled" type="text" data-iwap-xtype="TextField" id="org_id"
								class="input_text" style="width: 250" value=""  autocomplete="off"">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="机构简称不能为空">
							<span>机构简称:</span> 
							<input name="ORG_NM"  disabled="disabled" type="text" data-iwap-xtype="TextField" id="org_nm"
								class="input_text" style="width: 250" value=""  autocomplete="off"">
						</div>
						<div class="selectbox mr60 inputbox" id="ctx_iwap-gen-7">
							<span>偏好设置类别:</span> <select data-iwap-xtype="ListField"
								id="prefrc_category" name="PREFRC_CATEGORY" width="" class="select_btn ">
							</select>
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="设置名称不能为空" data-iwap-minlength="5">
							<span>设置名称:</span> <input name="SETTING_KEY" type="text"
								data-iwap-xtype="TextField" id="setting_key" class="input_text">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="设置值不能为空">
							<span>设置值:</span> <input name="SETTING_VAL" type="text"
								data-iwap-xtype="TextField" id="setting_val" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="true" >
							<span>备注1:</span> <input name="setting_val" type="text"
								data-iwap-xtype="TextField" id="remark1" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="true" >
							<span>备注2:</span> <input name="setting_val" type="text"
								data-iwap-xtype="TextField" id="remark2" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="true" >
							<span>备注3:</span> <input name="setting_val" type="text"
								data-iwap-xtype="TextField" id="remark3" class="input_text"
								style="width: 250" value="">
						</div>
					</div>
				</form>
				<!-- form END -->
				<div style="padding-left: 210px">
					<div class="buttonbox">
						<button data-iwap-xtype="ButtonField" id="save"
							class="btn false mr30" data-dialog-hidden="true"
							onclick="doSave()">保存</button>
					</div>
					<div id="" class="buttonbox">
						<button data-iwap-xtype="ButtonField" id="reset"
							class="btn false mr30">清空</button>
					</div>
					<div id="" class="buttonbox">
						<button data-iwap-xtype="ButtonField" id="resetDel"
							class="btn false mr30">清空</button>
					</div>
				</div>
			</div>
		</div>

		<!-- 第二个对话框 END-->
		<!-- 第三个对话框（对话框中打开对话框） -->
		<div class="bg"></div>
		<div class="dialog" id="myModa2" style="width: 830px;">
			<div class="dialog-header">
				<button type="button" class="close" id="btn_iwap-gen-10"
					data-dialog-hidden="true">
					<span>×</span>
				</button>
				<h4 class="modal-title">选择所属用户</h4>
			</div>
			<div class="modal-body">
				<iframe style="height: 600px; width: 800px"
					src="iwap.ctrl?txcode=usertList"></iframe>
			</div>
		</div>
		<!-- 第三个对话框 END-->
	</div>
	<!-- 对话框END -->
	<!-- 页面查询区域开始 -->
	<form id="Conditions" class="clearfix">
		<div class="col-md-6 fl">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>用户ID	:</span><input name="ACCT_ID" type="text"
					data-iwap-xtype="TextField" id="ACCT_ID" class="input_text" value="">
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="selectbox tl  pr inputbox">
					<span>偏好设置类别:</span><select data-iwap-xtype="ListField" name="PREFRC_CATEGORY"
						class="select_content" id="PREFRC_CATEGORY">
						<option value="%">--全部--</option>
					</select>
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>设置名称:</span><input name="SETTING_KEY" type="text"
					data-iwap-xtype="TextField" id="SETTING_KEY" class="input_text"
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
		<!-- 表格工具栏　开始 -->
		<div class="table_nav">
			<a id="selectmultidel" class="" onclick="del()"
				href="javaScript:void(0)">删除</a> <a href="javaScript:void(0)"
				data-iwap-dialog="myModal" id="add" onclick="add()">新增</a>
		</div>
		<!-- 表格工具栏　END -->
		<table id="iwapGrid"
			class="mygrid table table-bordered table-striped table-hover"
			data-iwap="grid" data-iwap-id="" data-iwap-param=""
			data-iwap-pagination="true">
			<tr>
				<th data-grid-name="ACCT_ID" primary="primary" data-order=""   data-grid-render="transDef"><input
					type="checkbox" name="selectname" selectmulti="selectmulti"
					value=""> <s><input type="checkbox"
						selectmulti="selectmulti" value="{{value}}"></s></th>
				<th data-grid-name="ACCT_ID" class="tl">用户ID</th>
				<th data-grid-name="PREFRC_CATEGORY" data-grid-render="transDef_pc" class="tl">偏好设置类别</th>
				<th data-grid-name="SETTING_KEY">设置名称</th>
				<th data-grid-name="SETTING_VAL">设置值</th>
				<th data-grid-name="ROLE_ID">角色</th>
				<th data-grid-name="ORG_ID">所属机构</th>
				<th data-grid-name="ORG_NM">机构简称</th>
				<th data-grid-name="ORG_FUL_NM">机构全称</th>
				<th data-grid-name="acct_id" option="option" option-html=''><span>操作</span><s>
						<a href="javaScript:void(0)" class="editId" onclick="edit(this)" id ="editOne">修改</a>&nbsp;|&nbsp;
					    <a href="javaScript:void(0)" class="editId" onclick="delOne(this)">删除</a>
				</s></th>
			</tr>
		</table>
	</div>
	<!-- 查询内容区域　END -->
	</body>
<script type="text/javascript">
var actionType="", iwapGrid=null,condionForm=null,operForm=null,grantTree=null;
$(document).ready(function() {
	initDict&&initDict("PREFRC_CATEGORY",function(){
		//重置按钮事件
		operForm=$.IWAP.Form({'id':'dialogarea'});
		condionForm=$.IWAP.Form({'id':'Conditions'});		
		$('#reset').click(function() {
			operForm.reset();
		});
		$('#resetDel').click(function() {
				$('#myModal input').not('#acct_id,#org_id,#org_nm,#role_id,#org_ful_nm').val(''); 
				$('select#prefrc_category').val('1');
		});
		/*查询表格初始化  设置默认查询条件*/
		var fData={'_deptid':$("#_deptid").val(),'actionId':'doBiz','start':'0','limit':'10','txcode':'prefrcMg'};
		iwapGrid = $.IWAP.iwapGrid({
			mode:'server',
			fData:fData,
			Url:'iwap.ctrl',
			grid:'grid',
			form:condionForm,
			renderTo:'iwapGrid'
		});	
	});

	// 初始化偏好设置类别（采用数据字典）
	initSelectKV('{"prefrc_category":"PREFRC_CATEGORY","PREFRC_CATEGORY":"PREFRC_CATEGORY"}');
});
//SYS_ACCT_PREFRC表的主键由：acct_id+prefrc_category+setting_key共同组成，获得它们的值。
function transDef(val,row_data){
	return row_data.acct_id+','+row_data.prefrc_category+','+row_data.setting_key;
	return getDict("PREFRC_CATEGORY",val);
}
//列表中偏好设置类别转换
function transDef_pc(val,row_data){
	return getDict("PREFRC_CATEGORY",val);
}
//增加
function add(){
	//每次点击增加按钮后：角色和状态设成默认值
	document.getElementById("reset").style.display = "block";
	document.getElementById("resetDel").style.display = "none";
	$('#myModal').dialog('新增偏好');
	actionType="add";
	operForm.reset();
 	operForm.enabledById("acct_id");
	$('select#prefrc_category').val('1');
};
//对话框
function dialogModal(id){
	$('#'+id).dialog();
};
//保存
function doSave(){
	var extParam={'option':actionType,'txcode':'prefrcMg','actionId':'doBiz'};
	var param=operForm.getData();
	$.IWAP.applyIf(param,extParam);
	iwapGrid.doSave(param,'#myModal');
}
//删除（可多个）
function del(){
	if(iwapGrid.getCheckValues()=="") {
		alert("请选择要删除的偏好!");
		return;
	}
	
	if (!confirm("确定要删除吗?请确定!"))
		return;
	//SYS_ACCT_PREFRC表的主键由：acct_id+prefrc_category+setting_key共同组成，获得它们的值。
	var ads="" , pcs="" ,sks="" ;
	$('input[selectmulti="selectmulti"]:checked').each(function(index, el) {
		var vals =$(el).val().split(',');
		ads+=vals[0]+',';
		pcs+=vals[1]+',';
		sks+=vals[2]+',';
	})
	ads=ads.substring(0,ads.length-1);
	pcs=pcs.substring(0,pcs.length-1);
	sks=sks.substring(0,sks.length-1);
	var param={'option':"remove",'txcode':"prefrcMg",
			'acct_ids': ads,
			'prefrc_categorys': pcs,
			'setting_keys': sks,
			'actionId':"doBiz"};
	$.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
		 if (rs['header']['msg']) {
		 	return alert("删除失败:"+rs['header']['msg']);
		 }else{
		 	alert("删除成功");
		 	iwapGrid.doQuery(condionForm.getData()); 
		 }
	 },function(){
		 alert("删除失败!");
	 });
};
//删除（单个）
function delOne(obj){
	if (!confirm("确定要删除吗?请确定!")){
		return;
	}
	var param={'option':"remove",'txcode':"prefrcMg",
			'acct_ids': iwapGrid.getCurrentRow()['acct_id'],
			'prefrc_categorys': iwapGrid.getCurrentRow()['prefrc_category'],
			'setting_keys': iwapGrid.getCurrentRow()['setting_key'],
			'actionId':"doBiz"};
	$.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
		 if (rs['header']['msg']) {
		 	return alert("删除失败:"+rs['header']['msg']);
		 }else{
		 	alert("删除成功");
		 	iwapGrid.doQuery(condionForm.getData()); 
		 }
	 },function(){
		 alert("删除失败!");
	 });
} 

//编辑
function edit(obj){
	var pianhao=iwapGrid.getCurrentRow()['PREFRC_CATEGORY'];
	alert("pianhao::"+pianhao);
	document.getElementById("reset").style.display = "none";
	document.getElementById("resetDel").style.display = "block";
	$('#myModal').dialog("修改柜员");
	actionType="save";
	operForm.reset();
	operForm.setData(iwapGrid.getCurrentRow());
};
</script>
</html>