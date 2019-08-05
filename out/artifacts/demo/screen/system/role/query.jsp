<%@ page language="java" import="com.nantian.iwap.web.WebEnv"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>角色管理-查询</title>
<meta name="description" content="">
<!-- 一般查询页面所引入的样式文件 -->
<link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/css.css" rel="stylesheet">
<link href="${ctx}/css/style.css" rel="stylesheet">
<link href="${ctx}/css/iwapui-style.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/zTreeStyle.css" rel="stylesheet">
<!-- JQ必须在最JS上面 -->
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/UtilTool.js"></script>
<script src="${ctx}/js/Form.js"></script>
<script src="${ctx}/js/TextField.js"></script>
<script src="${ctx}/js/ListField.js"></script>
<script type="text/javascript" src="${ctx}/js/iwapGrid.js"></script>
<script type="text/javascript" src="${ctx}/js/iwapui.js"></script>
<script type='text/javascript' src="${ctx}/js/String.js"></script>
<script type='text/javascript' src="${ctx}/js/dictionary.js"></script>
<script type='text/javascript' src="${ctx}/js/public.js"></script>
<!--Tree.js使用需同时引用   1、zTreeStyle.css  2、jquery.ztree.all-3.5.js  3、jquery.ztree.exhide-3.5.js  -->
<script type="text/javascript" src="${ctx}/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="${ctx}/js/Tree.js"></script>
</head>
<body class="iwapui center_body">
	<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
	<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
	<!-- 对话框开始 -->
	<div id="divDialog" class="divDialog">
		<div class="bg"></div>
		<div class="dialog" id="myModal" style="width: 600px;">
			<div class="dialog-header">
				<button type="button" data-dialog-hidden="true" class="close">
					<span>×</span>
				</button>
				<h4 class="dialog-title">增加角色</h4>
			</div>
			<div class="modal-body">
				<form method="post" onsubmit="return false" id="dialogarea">
					<!-- form开始 -->
					<div class="col-md-12">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="角色ID不能为空">
							<span>角色ID:</span> <input name="ROLE_ID" type="text"
								data-iwap-xtype="TextField" id="ROLE_ID" class="input_text">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="角色名称不能为空">
							<span>角色名称:</span> <input name="ROLE_NM" type="text"
								data-iwap-xtype="TextField" id="ROLE_NM" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="角色描述不能为空">
							<span>角色描述:</span> <input name="ROLE_DESC" type="text"
								data-iwap-xtype="TextField" id="ROLE_DESC" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="selectbox mr60 inputbox" id="ctx_iwap-gen-7">
							<span>是否启用:</span> <select data-iwap-xtype="ListField"
								id="ROLE_ENABLED" name="ROLE_ENABLED" width="" class="select_btn ">
							</select>
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="所属机构不能为空">
							<span>所属机构名称:</span> 
							<input name="ORG_NM" disabled="disabled" type="text" data-iwap-xtype="TextField" id="ORG_NM"
								class="input_text" style="width: 250" value=""  autocomplete="off"">
							<a href='javascript:void(0)' class='btn btn-primary' onclick='dialogModal("myModal4")'></a>
							<input name="ORG_ID"  type="hidden" type="text" data-iwap-xtype="TextField" id="ORG_NM"
								class="input_text" style="width: 250" value=""  autocomplete="off"">
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
							<div id="" class="buttonbox">
						<button data-iwap-xtype="ButtonField" id="resetDel"
							class="btn false mr30">清空</button>
					</div>
					</div>
				</div>
			</div>
		</div>
	<!-- 对话框END -->
	<!-- 第三个对话框 -->
	<div class="bg"></div>
	<div class="dialog" id="myModal3" style="width: 600px;">
		<div class="dialog-header">
			<button type="button" data-dialog-hidden="true" class="close">
				<span>×</span>
			</button>
			<h4 class="dialog-title">角色模块授权</h4>
		</div>
		<div class="modal-body">
		<!-- <iframe style="height: 300px; width: 560px"
					src="iwap.ctrl?txcode=menuList"></iframe> -->
			<div id="grant_tree"></div>
			<div style="padding-left: 210px">
				<div class="buttonbox">
					<button data-iwap-xtype="ButtonField" id="save_role"
						class="btn false mr30" data-dialog-hidden="true"
						onclick="doSaveRole()">保存</button>
				</div>
				<div id="" class="buttonbox">
					<button data-iwap-xtype="ButtonField" id="reset_role"
						class="btn false mr30">清空</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 第三个对话框 END-->
	
	<!-- 第四个对话框 -->
	<div class="bg"></div>
	<div class="dialog" id="myModal4" style="width: 600px;">
		<div class="dialog-header">
			<button type="button" data-dialog-hidden="true" class="close">
				<span>×</span>
			</button>
			<h4 class="dialog-title">机构授权</h4>
		</div>
		<div class="modal-body">
		    <div id="org_tree"></div> 
			<div style="padding-left: 210px">
				<div class="buttonbox">
					<button data-iwap-xtype="ButtonField" id="save_org"
						class="btn false mr30" data-dialog-hidden="true"
						onclick="doSaveOrg()">保存</button>
				</div>
				<div id="" class="buttonbox">
					<button data-iwap-xtype="ButtonField" id="reset_org"
						class="btn false mr30">清空</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 第四个对话框 END-->
	<!-- 页面查询区域开始 -->
	<form id="Conditions" class="clearfix">
		<div class="col-md-6 fl">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true" data-iwap-tooltext="输入不能为空且应为1至6位数"
				data-iwap-minlength="1" data-iwap-maxlength="12">
				<span>角色编号:</span><input name="roleid" type="text"
					data-iwap-xtype="TextField" id="roleid" class="input_text" value="">
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>角色名称:</span><input name="roleName" type="text"
					data-iwap-xtype="TextField" id="roleName" class="input_text"
					value="">
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>机构号码:</span><input name="org_id" type="text"
					data-iwap-xtype="TextField" id="org_id" class="input_text"
					value="">
			</div>
		</div>
		
	</form>
	<div class="tc mb14">
		<a href="javaScript:void(0)" class="btn btn-primary mr30" id="query"
			onclick="iwapGrid.doQuery()">查询</a> <a href="javaScript:void(0)"
			class="btn btn-primary mr30" id="btn_clear"
			onclick="iwapGrid.doReset();">清空</a>
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
				<th data-grid-name="ROLE_ID" primary="primary" data-order=""><input
					type="checkbox" name="selectname" selectmulti="selectmulti"
					value=""> <s><input type="checkbox"
						selectmulti="selectmulti" value="{{value}}"></s></th>
				<th data-grid-name="ROLE_ID" class="tl">角色编号</th>
				<th data-grid-name="ROLE_NM">角色名称</th>
				<th data-grid-name="ROLE_DESC">角色描述</th>
				<th data-grid-name="ORG_ID">机构号码</th>
				<th data-grid-name="ROLE_DESC">角色描述</th>
				<th data-grid-name="ORG_ID">机构号码</th>
				<th data-grid-name="acct_id" option="option" option-html=''><span>角色授权</span><s>
						<a href="javaScript:void(0)" class="editId" onclick="grant(this)" id ="grant">授权</a>
				</th>
				<th data-grid-name="ROLE_ID" option="option" option-html=''><span>操作</span>
					<s>
						<a class="editId" onclick="edit(this)">修改</a>
					    <a class="editId" onclick="delOne(this)">删除</a>
					</s>
				</th>
			</tr>
		</table>
	</div>
	<!-- 查询内容区域　END -->
</body>
<script type="text/javascript">
var actionType="", iwapGrid=null,condionForm=null,operForm=null,grantTree=null,orgTree= null;
var grantTreeData=null,orgTreeData=null;
$(document).ready(function() {
			//重置按钮事件
			operForm=$.IWAP.Form({'id':'dialogarea'});
			condionForm=$.IWAP.Form({'id':'Conditions'});
			$('#reset').click(function() {
				operForm.reset();
				$('select#ROLE_ENABLED').val('1');
			});
			$('#resetDel').click(function() {
				$('input').not('#ROLE_ID').val(''); 
 				$('select#ROLE_ENABLED').val('1');
			});
			//查出系统模块树
		   query_sys_module&&query_sys_module();
			//查询机构树
			query_sys_org&&query_sys_org();
			$("#reset_role").click( function(){
				grantTree.Load(grantTreeData,false);
			});
			 $('#reset_org').click(function() {
				 orgTree.Load(orgTreeData,false);
			}); 
			/*查询表格初始化  设置默认查询条件*/
			var fData={'actionId':'doBiz','start':'0','limit':'10','txcode':'roleMg'};
			iwapGrid = $.IWAP.iwapGrid({
				mode:'server',
				fData:fData,
				Url:'${ctx}/iwap.ctrl',
				grid:'grid',
				form:condionForm,
				renderTo:'iwapGrid'
			});	
			// 初始化角色是否启用（采用数据字典）
			initSelectKV('{"ROLE_ENABLED":"ROLE_ENABLED" }');

});
//增加
function add(){
	//每次点击增加按钮后：角色是否启用设成默认值
	document.getElementById("reset").style.display = "block";
	document.getElementById("resetDel").style.display = "none";
	$('#myModal').dialog('新增角色');
	actionType="insert";
	operForm.reset();
	$('select#ROLE_ENABLED').val('1');
};
//对话框
function dialogModal(id){
	$('#'+id).dialog();
};
//保存
function doSave(){
	var extParam={'actionType':actionType,'txcode':'roleMg','actionId':'doBiz'};
	var param=operForm.getData();
	$.IWAP.applyIf(param,extParam);
	iwapGrid.doSave(param,'#myModal');
}
//删除（可多个）
function del(){
	if(iwapGrid.getCheckValues()=="") {
		alert("请选择要删除的角色!");
		return;
	}
	
	if (!confirm("确定要删除吗?请确定!"))
		return;
	var list = iwapGrid.getCheckValues().split(",");
	var param={'actionType':"delete",'txcode':"roleMg",'list':list,'actionId':"doBiz"};
	iwapGrid.doDelete(param);
};
//删除（单个）
function delOne(obj){
	//alert(iwapGrid.getCurrentRow()['ROLE_ID']);
	if (!confirm("确定要删除吗?请确定!")){
		return;
	}
	var list = Array();
	list[0]=iwapGrid.getCurrentRow()['ROLE_ID'];
	var param={'actionType':"delete",'txcode':"roleMg",'list': list,'actionId':"doBiz"};
	iwapGrid.doDelete(param);
} 

//编辑
function edit(obj){
	document.getElementById("reset").style.display = "none";
	document.getElementById("resetDel").style.display = "block";
	$('#myModal').dialog("修改角色");
	actionType="update";
	operForm.reset();
	operForm.setData(iwapGrid.getCurrentRow());
	$('select#ROLE_ENABLED').val('1');
};

//查询模块授权
function grant(obj){
	var callFn = function(rs){
		//判断是否出错
		if(rs['header']['msg']){
			alert("模块授权出错:"+rs['header']['msg']);
		}else{
			zNodes= rs['body']['grants'];
			for(var i in zNodes){
				grantTree.setCheck(zNodes[i]['module_id'],true);
			}
			$('#myModal3').dialog();
		}
		
	}
	var field = {'actionType':'query_grant','role_id':iwapGrid.getCurrentRow()['ROLE_ID']};
	sendAjax(field,'roleMg','doBiz',callFn);
} 
//保存模块授权
function doSaveRole(){
	var callFn = function(rs){
		$('#myModal3').find('.close').click();
		//判断是否出错
		if(rs['header']['msg']){
			alert("授权保存出错:"+rs['header']['msg']);
		}else{
			alert("授权保存成功");
		}
	}
	var field = {'actionType':'save_grant','acct_module_list':grantTree.getValue(),'role_id':iwapGrid.getCurrentRow()['ROLE_ID']};
	sendAjax(field,'roleMg','doBiz',callFn);
} 
//查询系统模块树
function query_sys_module(){
	 var callFn = function(rs){
		//判断是否出错
		if(rs['header']['msg']){
			alert("查询系统模块出错:"+rs['header']['msg']);
		}else{
			$('#grant_tree').html("");
			var zNodes = rs['body']['zNodes'];
			zNodes = eval("("+zNodes+")");
			if(zNodes.length <=0){
				alert("权限内无对应数据！");
			}else{
				grantTree = $.IWAP.Tree({
					checkType:{ "Y" : "p", "N" : "s" },
					disabled:false,
					hidden:false,
					value:[],
					checked:true,
					data:zNodes,
					mode:'local',
					renderTo:'grant_tree'
		        });
			}
		}
	}
	var field = {'actionType':'query_sys_module'};
	sendAjax(field,'roleMg','doBiz',callFn); 
} 
//查询机构树
function query_sys_org(){
	var callFn = function(rs){
		//判断是否出错
		if(rs['header']['msg']){
			alert("查询机构树出错:"+rs['header']['msg']);
		}else{
			$('#org_tree').html("");
			orgTreeData = rs['body']['OrgzNodes'];
			orgTreeData = eval("("+orgTreeData+")");
			if(orgTreeData.length <=0){
				alert("机构内无对应数据！");
			}else{
				orgTree = $.IWAP.Tree({
					checkType:{ "Y" : "p", "N" : "s" },
					disabled:false,
					hidden:false,
					value:[],
					checked:true,
					data:orgTreeData,
					mode:'local',
					renderTo:'org_tree'
		        });
			}
		}
	}
	var field = {'actionType':'query_sys_org'};
	sendAjax(field,'roleMg','doBiz',callFn);
} 
//选择机构保存
function doSaveOrg(){
	$('#myModal4').find('.close').click();
	var org_id =orgTree.getChecked()[orgTree.getChecked().length-1].id;
	var org_name =orgTree.getChecked()[orgTree.getChecked().length-1].name;
	$("#ORG_ID").val(org_id);
	$("#ORG_NM").val(org_name);
	//机构树打勾全取消
	orgTree.Load(orgTreeData,false);
}
</script>
</html>