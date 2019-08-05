<%@ page language="java" import="com.nantian.iwap.web.WebEnv"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>五险一金管理-查询</title>
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
<!-- 引入日期控件 -->
<script type="text/javascript" src="${ctx}/js/My97DatePicker/calendar.js"></script>
<script type="text/javascript" src="${ctx}/js/My97DatePicker/WdatePicker.js"></script>
<!-- 文件上传 -->
<script type="text/javascript" src="${ctx}/js/FileUpload.js"></script>
<style>
.table {
  width: 3000px;
  max-width: 100%;
  margin-bottom: 20px;
}
.divDialog .dialog {
    width: 50%;
    max-width: 1200px;
    z-index: 10;
    position: absolute;
    top: 10%;
    left: 50%;
    background: #fff;
    border: 1px solid rgba(0, 0, 0, .2);
    border-radius: 6px;
    outline: 0;
    -webkit-box-shadow: 0 3px 9px rgba(0, 0, 0, .5);
    box-shadow: 0 3px 9px rgba(0, 0, 0, .5);
    display: none;
}

.iwapui .inputbox>span{  vertical-align: top; height: 32px; line-height: 32px; width: 150px; display: inline-block; text-align: right; overflow: hidden; margin-right: 5px;}

</style>
</head>
<body class="iwapui center_body">
	<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
	<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
	<!-- 对话框开始 -->
	<div id="divDialog" class="divDialog">
		<div class="bg"></div>
		<div class="dialog" id="myModal" style="width: 1030px;">
			<div class="dialog-header">
				<button type="button" data-dialog-hidden="true" class="close">
					<span>×</span>
				</button>
				<h4 class="dialog-title">五险一金信息</h4>
			</div>
			<div class="modal-body">
				<form method="post" onsubmit="return false" id="dialogarea">
					<!-- form开始 -->
					<div class="col-md-6">
						<div class="inputbox tl pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="姓名不能为空">
							<span>用户姓名:</span> <input name="USER_NAME" 
								type="text" data-iwap-xtype="TextField" id="USER_NAME"
								class="input_text" style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="FMIS员工号不能为空">
							<span>FMIS员工号:</span> <input name="FMIS_NO" type="text"
								data-iwap-xtype="TextField" id="FMIS_NO" class="input_text"
								style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="分行名称不能为空">
							<span>分行名称:</span> <input name="BRANCH_NAME" 
								type="text" data-iwap-xtype="TextField" id="BRANCH_NAME"
								class="input_text" style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="缴费月份不能为空">
							<span>缴费月份:</span> <input data-iwap-xtype="TextField" id="PAY_MONTH" name="PAY_MONTH" class="Wdate" 
							onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy/MM'})"/>
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="" data-iwap-inputtype="mumber">
							<span>住房公积金单位月缴费金额:</span> <input name="GJJ_COMPANY_MONTHPAY" type="text"
								data-iwap-xtype="TextField" id="GJJ_COMPANY_MONTHPAY" class="input_text"
								style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox tl pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="住房公积金个人月缴费金额不能为空">
							<span>住房公积金个人月缴费金额:</span> <input name="GJJ_PERSONAL_MONTHPAY" 
								type="text" data-iwap-xtype="TextField" id="GJJ_PERSONAL_MONTHPAY"
								class="input_text" style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox tl pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="">
							<span>医疗保险单位月缴费金额:</span> <input name="YLBX_COMPANY_MONTHPAY" type="text" 
								data-iwap-xtype="TextField" id="YLBX_COMPANY_MONTHPAY" class="input_text"
								style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox tl pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="">
							<span>医疗保险个人月缴费金额:</span> <input name="YLBX_PERSONAL_MONTHPAY" type="text" 
								data-iwap-xtype="TextField" id="YLBX_PERSONAL_MONTHPAY" class="input_text"
								style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="">
							<span>失业保险单位月缴费金额:</span> <input name="SYBX_COMPANY_MONTHPAY" type="text" 
								data-iwap-xtype="TextField" id="SYBX_COMPANY_MONTHPAY" class="input_text"
								style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="">
							<span>失业保险个人月缴费金额:</span> <input name="SYBX_PERSONAL_MONTHPAY" type="text"
								data-iwap-xtype="TextField" id="SYBX_PERSONAL_MONTHPAY" class="input_text"
								style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="">
							<span>养老保险单位月缴费金额:</span> <input name="YLAOBX_COMPANY_MONTHPAY" 
								type="text" data-iwap-xtype="TextField" id="YLAOBX_COMPANY_MONTHPAY"
								class="input_text" style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="">
							<span>养老保险个人月缴费金额:</span> <input name="YLAOBX_PERSONAL_MONTHPAY" type="text" 
								data-iwap-xtype="TextField" id="YLAOBX_PERSONAL_MONTHPAY" class="input_text"
								style="width: 250" value="">
						</div>
					</div>
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="">
							<span>工伤保险单位月缴费金额:</span> <input name="GSBX_COMPANY_MONTHPAY" type="text" 
								data-iwap-xtype="TextField" id="GSBX_COMPANY_MONTHPAY" class="input_text"
								style="width: 250" value="">
						</div>
					</div>			
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="">
							<span>生育保险单位月缴费金额:</span> <input name="SYUBX_COMPANY_MONTHPAY" type="text"
								data-iwap-xtype="TextField" id="SYUBX_COMPANY_MONTHPAY" class="input_text"
								style="width: 250" value="">
						</div>
					</div>



					
				</form>
				<!-- form END -->
				<div style="padding-left: 490px;padding-top: 300px;">
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
	<!-- 第二个对话框（对话框中打开对话框） -->
		<div class="bg"></div>
		<div class="dialog" id="myModa2" style="width: 830px;">
			<div class="dialog-header">
				<button type="button" class="close" id="btn_iwap-gen-10"
					data-dialog-hidden="true">
					<span>×</span>
				</button>
				<h4 class="modal-title">选择姓名</h4>
			</div>
			<div class="modal-body">
				<iframe style="height: 600px; width: 800px"
					src="iwap.ctrl?txcode=staffList"></iframe>
			</div>
		</div>
	<!-- 第二个对话框 END-->
		
	<!-- 第三个对话框 -->
	<div class="bg"></div>
		<div class="dialog" id="myModa3" style="width: 830px;">
			<div class="dialog-header">
				<button type="button" class="close" id="btn_iwap-gen-10"
					data-dialog-hidden="true">
					<span>×</span>
				</button>
				<h4 class="modal-title">选择城市</h4>
			</div>
			<div class="modal-body">
				<iframe style="height: 600px; width: 800px"
					src="iwap.ctrl?txcode=cityList"></iframe>
			</div>
		</div>
	<!-- 第三个对话框 END-->
	
	<!-- 页面查询区域开始 -->
	<form id="Conditions" class="clearfix">

<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>姓名:</span><input name="userName" type="text"
					data-iwap-xtype="TextField" id="userName" class="input_text"
					value="">
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true" data-iwap-tooltext="输入不能为空且应为1至6位数"
				data-iwap-minlength="1" data-iwap-maxlength="12">
				<span>fmisNo:</span><input name="fmisNo" type="text"
					data-iwap-xtype="TextField" id="fmisNo" class="input_text" value="">
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>分行名称:</span><input name="branchName" type="text"
					data-iwap-xtype="TextField" id="branchName" class="input_text"
					value="">
			</div>
		</div>
		
		<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>缴费月份:</span><input data-iwap-xtype="TextField" id="payMonth" name="payMonth" class="Wdate" 
							onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy/MM'})"/>
			</div>
		</div>

		<div class="col-md-6 fl">
			<div class="selectbox tl  pr inputbox">
				<span>提交状态:</span><select data-iwap-xtype="ListField" name="state"
					class="select_content" id="state">
					<option value="%">全部</option>
				</select>
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="selectbox tl  pr inputbox">
				<span>审核状态:</span><select data-iwap-xtype="ListField" name="checkState"
					class="select_content" id="checkState">
					<option value="%">全部</option>
				</select>
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
	<div style="overflow: auto; width: 100%;">
		<!-- 表格工具栏　开始 -->
		<div class="table_nav" style="display:inline">
				<a id="selectmultidel" class="" onclick="del()"
				href="javaScript:void(0)">删除</a> <a href="javaScript:void(0)"
				data-iwap-dialog="myModal" id="add" onclick="add()">新增</a>
				<a id="selectmultisubmit" class="" onclick="submission()" href="javaScript:void(0)">提交</a>
				<a href="javaScript:void(0)" id="export" onclick="exportData()">导出</a>
		</div>
		<div style="display:inline">
			<a href="javaScript:void(0)" id="uplaodfile"></a>
		</div>
		<!-- 表格工具栏　END -->
		<table id="iwapGrid"
			class="mygrid table table-bordered table-striped table-hover" >
			<tr>
				<th data-grid-name="ID" primary="primary" data-order=""><input
					type="checkbox" name="selectname" selectmulti="selectmulti"
					value=""> <s><input type="checkbox"
						selectmulti="selectmulti" value="{{value}}"></s></th>
				<th data-grid-name="ID" class="tl">序号</th>
				<th data-grid-name="USER_NAME" class="tl">姓名</th>
				<th data-grid-name="FMIS_NO" class="tl">FMIS员工号</th>
				<th data-grid-name="BRANCH_NAME" class="tl">分行名称</th>
				<th data-grid-name="PAY_MONTH" class="tl">缴费月份</th>
				<th data-grid-name="GJJ_COMPANY_MONTHPAY" class="tl">住房公积金单位月缴费金额</th>				
				<th data-grid-name="GJJ_PERSONAL_MONTHPAY" class="tl">住房公积金个人月缴费金额</th>
				<th data-grid-name="YLBX_COMPANY_MONTHPAY" class="tl">医疗保险单位月缴费金额</th>
				<th data-grid-name="YLBX_PERSONAL_MONTHPAY" class="tl">医疗保险个人月缴费金额</th>
				<th data-grid-name="SYBX_COMPANY_MONTHPAY" class="tl">失业保险单位月缴费金额</th>
				<th data-grid-name="SYBX_PERSONAL_MONTHPAY" class="tl">失业保险个人月缴费金额</th>
				<th data-grid-name="YLAOBX_COMPANY_MONTHPAY" class="tl">养老保险单位月缴费金额</th>
				<th data-grid-name="YLAOBX_PERSONAL_MONTHPAY" class="tl">养老保险个人月缴费金额</th>
				<th data-grid-name="GSBX_COMPANY_MONTHPAY" class="tl">工伤保险单位月缴费金额</th>
				<th data-grid-name="SYUBX_COMPANY_MONTHPAY" class="tl">生育保险单位月缴费金额</th>
				<th data-grid-name="COMPANY_COUNT" class="tl">单位合计</th>
				<th data-grid-name="PERSONAL_COUNT" class="tl">个人合计</th>
				<th data-grid-name="COMPANYANDPERSONAL_COUNT" class="tl">单位和个人合计</th>
				<th data-grid-name="STATUS" data-grid-render="transDef_status" class="tl">单据状态</th>
				<th data-grid-name="CHECKSTATUS" data-grid-render="transDef_checkstatus" class="tl">审核状态</th>
				<th data-grid-name="ROLE_ID" option="option" option-html=''><span>操作</span>
					<s>
					    <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id ="editOne">修改</a>&nbsp;|&nbsp;
					    <a href="javaScript:void(0)" class="editId" onclick="delOne(this)">删除</a>
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

			/*查询表格初始化  设置默认查询条件*/
			var fData={'actionId':'doBiz','start':'0','limit':'10','txcode':'fiveRisksOneGold'};
			iwapGrid = $.IWAP.iwapGrid({
				mode:'server',
				fData:fData,
				Url:'${ctx}/iwap.ctrl',
				grid:'grid',
				form:condionForm,
				renderTo:'iwapGrid'
			});	
			
			initDict&&initDict("submitState",function(){});
			initDict&&initDict("checkState",function(){});
			// 初始化角色,状态（采用数据字典）
			initSelectKV('{"state":"submitState","checkState":"checkState"}');
			
			/* 文件上传 */
			fileUpload = $.IWAP.FileUpload({
			'id':'import_file',
			'url':'iwap.ctrl',
			'fileType':['xlsx','xls'],
			'label':'导入',
			'afterUpload':function(){
				var field={'imp_id':'000011','files':fileUpload.getFileName(),'isOverWrite':'false','modelcode':'exchangesubsidy'};
				var data=JSON.stringify({'body':field,'header':{'txcode':"importData",'actionId':"doBiz"}});
				$.ajax({
						timeout:0,
						data:data,
						cache:false,
						contentType:'application/json',
						dataType:'json',
						type:"POST",
						url:'iwap.ctrl',
						async:false,
						error:function(request){
							alert("Connection error");
						},
						success:function(data){
							if(data['header']['msg']){
								return alert("");
							}else if(data['body']['info']){
								return alert(""+data['body']['info']);
							}else {
								return alert("Inport error");
							}
						}

				});
				},//文件上传之后的触发事件，不管上传是否成功都会执行
			'beforeUpload':function(){return true},
			'size':1024000,  //允许上传的单个文件大小，是以byte为单位
			'success':function(res){
				//alert("文件上传成功");
				//var json = JSON.parse(res.target.response);
				//alert(json.msg);
				//console.log(json.uploadfilelist);
				
			},//上传成功后的回调函数
		    'failed':function(){alert('上传失败');},//上传失败后的回调函数
			'renderTo':'uplaodfile'
		});
			
		$("#uplaodfile").change(function(){
			fileUpload.upload();
		}); 


});


function transDefCompany(val,row_data){
	var gjjjs=row_data.GJJ_MS_COMPANY_MONTHPAY;
	var gjjms=row_data.GJJ_JS_COMPANY_MONTHPAY;
	gjjjs=parseFloat(gjjjs)?parseFloat(gjjjs):0;
	gjjms=parseFloat(gjjjs)?parseFloat(gjjms):0;
	return (gjjjs+gjjms).toFixed(1);

}
function transDefPersonal(val,row_data){
	var gjjjs=row_data.GJJ_MS_PERSONAL_MONTHPAY;
	var gjjms=row_data.GJJ_JS_PERSONAL_MONTHPAY;
	gjjjs=parseFloat(gjjjs)?parseFloat(gjjjs):0;
	gjjms=parseFloat(gjjjs)?parseFloat(gjjms):0;
	return (gjjjs+gjjms).toFixed(1);

}
function transDef_status(val,row_data){
	return getDict("submitState",val);
}
function transDef_checkstatus(val,row_data){

	return getDict("checkState",val);
}

//增加
function add(){
	//每次点击增加按钮后：角色是否启用设成默认值
	document.getElementById("reset").style.display = "block";
	document.getElementById("resetDel").style.display = "none";
	$('#myModal').dialog('五险一金信息');
	actionType="add";
	operForm.reset();
};
//对话框
function dialogModal(id){
	$('#'+id).dialog();
};
//保存
function doSave(){
	if($('input#USER_NAME').val()==''){
		alert("姓名不能为空");
		return ;
	}
	if($('input#FMIS_NO').val()==''){
		alert("fmisNo不能为空");
		return ;
	}
	if($('input#BRANCH_NAME').val()==''){
		alert("分行名称不能为空");
		return ;
	}
	if($('input#ACCOUNT_NUMBER').val()==''){
		alert("账号不能为空");
		return ;
	}
	if($('input#ACCOUNT_NAME').val()==''){
		alert("账户名称不能为空");
		return ;
	}
	if($('input#BANK_NAME').val()==''){
		alert("开户行名不能为空");
		return ;
	}
	if($('input#PAY_MONTH').val()==''){
		alert("缴费月份不能为空");
		return ;
	}
	var extParam={'option':actionType,'txcode':'fiveRisksOneGold','actionId':'doBiz'};
	var param=operForm.getData();
	$.IWAP.applyIf(param,extParam);
	$.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
		 $('#myModal').find('.close').click();
		 if (rs['header']['msg']) {
		 	return alert("保存失败:"+rs['header']['msg']);
		 }else{
		 	alert("保存成功");
		 	iwapGrid.doQuery(condionForm.getData()); 
		 }
	 },function(){
		 alert("保存失败!");
	 });
}

//提交员工五险一金（可多个）
function submission(){
	if(iwapGrid.getCheckValues()=="") {
		alert("请选择要提交的记录!");
		return;
	}
	
	if (!confirm("确定要提交吗?请确定!"))
		return;
	
	var param={'option':"submission",'txcode':"fiveRisksOneGold",'userids': iwapGrid.getCheckValues(),'actionId':"doBiz"};
	$.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
		 if (rs['header']['msg']) {
		 	return alert("提交失败:"+rs['header']['msg']);
		 }else{
		 	alert("提交成功");
		 	iwapGrid.doQuery(condionForm.getData()); 
		 }
	 },function(){
		 alert("提交失败!");
	 });
};
//删除（可多个）
function del(){
	if(iwapGrid.getCheckValues()=="") {
		alert("请选择要删除的记录!");
		return;
	}
	
	if (!confirm("确定要删除吗?请确定!"))
		return;
	
	var param={'option':"remove",'txcode':"fiveRisksOneGold",'userids': iwapGrid.getCheckValues(),'actionId':"doBiz"};
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
	var param={'option':"remove",'txcode':"fiveRisksOneGold",'userids': iwapGrid.getCurrentRow()['ID'],'actionId':"doBiz"};
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
	var checkStatus=iwapGrid.getCurrentRow()['CHECKSTATUS'];
	if(checkStatus==1){
		alert("该记录已经审核，不能修改！");
		return;
		}
	document.getElementById("reset").style.display = "none";
	document.getElementById("resetDel").style.display = "block";
	$('#myModal').dialog("修改五险一金");
	actionType="save";
	operForm.reset();
	operForm.setData(iwapGrid.getCurrentRow());

};

function exportData(){
	var data = {'exportFlag':'1','filetype':'xlsx','txcode':'exchangesubsidy','actionId':'doBiz','start':'0','limit':'1000'};
	var form = condionForm.getData();
	$.IWAP.apply(data,form);
	
	var titleString = [];
	$("table#iwapGrid tbody tr:eq(0) th").each(function(){
		if($(this).hasClass("tl")){
			var titleMap = {};
			titleMap[$(this).attr("data-grid-name")]=$(this).html();
			titleString.push(titleMap);
		}
	});
	titleString = JSON.stringify(titleString);
	var reg = /\[/g;
	var reg2 = /\]/g;
	var reg3 = /\{/g;
	var reg4 = /\}/g;
    titleString = titleString.replace(reg, '%5B');
    titleString = titleString.replace(reg2, '%5D');
    titleString = titleString.replace(reg3, '%7B');
    titleString = titleString.replace(reg4, '%7D');
	data.titleString=titleString;
	
	var param="";

	for (var key in data) {
		param += key + "=" + data[key] + "&";
	}
	param = param.substr(0,param.length-1);
	var iframe = $('<iframe name="iwapdownload">');
	iframe.css("display", "none");
	iframe.attr("src", "download.ctrl?" + param);
	$('body').prepend(iframe);
}


function exportOneData(obj){
	if (!confirm("确定要导出处分台账吗?请确定!")){
		return;
	}
	var userId=iwapGrid.getCurrentRow()['ID'];	
	var data = {'exportFlag':'1','filetype':'xlsx','txcode':'exchangesubsidy','userId':userId,'actionId':'doBiz','start':'0','limit':'1000'};
	debugger;
	var param="";
	for (var key in data) {
		param += key + "=" + data[key] + "&";
	}
	param = param.substr(0,param.length-1);
	var iframe = $('<iframe name="iwapdownload">');
	iframe.css("display", "none");
	iframe.attr("src", "download.ctrl?" + param);
	$('body').prepend(iframe);
}

</script>
</html>