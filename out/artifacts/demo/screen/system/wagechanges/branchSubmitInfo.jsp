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
  width: 1000px;
  max-width: 100%;
  margin-bottom: 20px;
}

</style>
</head>
<body class="iwapui center_body">
	<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
	<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
	<!-- 对话框开始 -->
	<div id="divDialog" class="divDialog">
		<div class="bg"></div>
		<div class="dialog" id="myModal" style="width: 1100px;">
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
							<span>工伤保险个人月缴费金额:</span> <input name="GSBX_PERSONAL_MONTHPAY" type="text" 
								data-iwap-xtype="TextField" id="GSBX_PERSONAL_MONTHPAY" class="input_text"
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
					<div class="col-md-6">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="">
							<span>生育保险个人月缴费金额:</span> <input name="SYUBX_PERSONAL_MONTHPAY" 
								type="text" data-iwap-xtype="TextField" id="SYUBX_PERSONAL_MONTHPAY"
								class="input_text" style="width: 250" value="">
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
				<span>分行名称:</span><input name="branchName" type="text"
					data-iwap-xtype="TextField" id="branchName" class="input_text"
					value="">
			</div>
		</div>
		
		<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>统计月份:</span>
				<input data-iwap-xtype="TextField" id="payMonth" name="payMonth" class="Wdate" 
				onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy/MM'})"/>
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
		<a href="javaScript:void(0)" id="export" onclick="exportData()">合并汇总</a>
		</div>
		<!-- 表格工具栏　END -->
		<form method="post" action="iwap.ctrl" id="dialogarea2">
			<input type="hidden" id="txcode" name="txcode" size="15" value="mergeSummary">
        	<input type="hidden" id="actionId" name="actionId" size="15" value="doBiz">
        	<input type="hidden" id="option" name="option" size="15" value="edit">
        	<input type="hidden" id="countMonth" name="countMonth" size="15">
        	<input type="hidden" id="branchName" name="branchName" size="15"/>
		<table id="iwapGrid"
			class="mygrid table table-bordered table-striped table-hover" >
			<tr>
				<th data-grid-name="BRANCH_NAME" primary="primary" data-order=""><input
					type="checkbox" name="selectname" selectmulti="selectmulti"
					value=""> <s><input type="checkbox"
						selectmulti="selectmulti" value="{{value}}"></s></th>
				<th data-grid-name="BRANCH_NAME" class="tl">分行名称</th>
				<th data-grid-name="PAY_MONTH" class="tl">统计月份</th>
				<th data-grid-name="SUBMIT_DATE" class="tl">提交时间</th>
				<th data-grid-name="ROLE_ID" option="option" option-html=''><span>操作</span>
					<s>
					    <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id ="editOne">编辑</a>&nbsp;|&nbsp;
					    <a href="javaScript:void(0)" class="editId" onclick="giveBack(this)">退回</a>
					</s>
				</th>
			</tr>
		</table>
		</form>
	</div>
	<!-- 查询内容区域　END -->
</body>
<script type="text/javascript">
var actionType="", iwapGrid=null,condionForm=null,operForm=null,grantTree=null,orgTree= null;
var grantTreeData=null,orgTreeData=null;
$(document).ready(function() {
			debugger;
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
			function month(){
				var today=new Date();
				var y=today.getFullYear();
				var m=today.getMonth()+1;
				if(m<10){
					m='0'+m;
					}
				return y+"/"+m;
				}
			document.getElementById("payMonth").value = month();

			var dataform=condionForm.getData();
			/*查询表格初始化  设置默认查询条件*/
			var fData={'actionId':'doBiz','start':'0','limit':'10','txcode':'mergeSummary'};
			$.IWAP.apply(fData,dataform);
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

			


});


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


//编辑
function edit(obj){
	var payMonth=iwapGrid.getCurrentRow()['PAY_MONTH'];
	var branchName=iwapGrid.getCurrentRow()['BRANCH_NAME'];
	var today = new Date();
    var today_time =  FormatDate(today);
    if(today_time>payMonth){
        alert('不能修改当前月以前的记录');
        return false;
    }
    $("#branchName").val(branchName);
    $("#countMonth").val(payMonth);
    $("#dialogarea2").submit();

};
function FormatDate (sysDate) {
    var date = new Date(sysDate);
    return date.getFullYear()+"-"+(date.getMonth()+1);
}
//合并汇总导出五险一金划付核对表
function exportData(){
	if(iwapGrid.getCheckValues()=="") {
		alert("请选择要导出的数据记录!");
		return;
	}
	if (!confirm("确定要合并汇总吗?请确定!")){
		return;
	}
	var payMonth=$("#payMonth").val();
	var branchNames=iwapGrid.getCheckValues();
		
	var data = {'exportFlag':'1','filetype':'xlsx','txcode':'mergeSummary','payMonth':payMonth,'branchNames':branchNames,'actionId':'doBiz','start':'0','limit':'1000'};
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