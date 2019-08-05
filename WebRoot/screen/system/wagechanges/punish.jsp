<%@ page language="java" import="com.nantian.iwap.web.WebEnv"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>处分管理-查询</title>
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
.divDialog .dialog {
    width: 50%;
    max-width: 1200px;
    z-index: 10;
    position: absolute;
    top: 3%;
    left: 50%;
    background: #fff;
    border: 1px solid rgba(0, 0, 0, .2);
    border-radius: 6px;
    outline: 0;
    -webkit-box-shadow: 0 3px 9px rgba(0, 0, 0, .5);
    box-shadow: 0 3px 9px rgba(0, 0, 0, .5);
    display: none;
}

</style>
</head>
<body class="iwapui center_body">
	<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
	<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
	<input type="hidden" value="${pInfo.haha}" name="username"  id="username">
	<!-- 对话框开始 -->
	<div id="divDialog" class="divDialog">
		<div class="bg"></div>
		<div class="dialog" id="myModal" style="width: 600px;">
			<div class="dialog-header">
				<button type="button" data-dialog-hidden="true" class="close">
					<span>×</span>
				</button>
				<h4 class="dialog-title">录入处分信息</h4>
			</div>
			<div class="modal-body">
				<form method="post" action="iwap.ctrl" id="dialogarea">
				  	<input type="hidden" id="txcode" name="txcode" size="15" value="punish">
        			<input type="hidden" id="actionId" name="actionId" size="15" value="doBiz">
        			<input type="hidden" id="actionType" name="actionType" size="15" value="input_info">
					<input type="hidden" id="USER_ID" name="USER_ID" size="15" value=""/>
					<!-- form开始 -->
					<div class="col-md-12">
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="部门不能为空">
							<span>部门:</span> <input name="DEPARTMENT" type="text"
								data-iwap-xtype="TextField" id="DEPARTMENT" class="input_text">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="姓名不能为空">
							<span>姓名:</span> <input name="USER_NAME" type="text"
								data-iwap-xtype="TextField" id="USER_NAME" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="FMIS员工号不能为空">
							<span>FMIS员工号:</span> <input name="FMIS_NO" type="text"
								data-iwap-xtype="TextField" id="FMIS_NO" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="处分事项不能为空">
							<span>处分事项:</span> <input name="PUNISH_MATTER" type="text"
								data-iwap-xtype="TextField" id="PUNISH_MATTER" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="selectbox mr60 inputbox" id="ctx_iwap-gen-7">
							<span>处分类型:</span> <select data-iwap-xtype="ListField"
								id="PUNISH_GRADE" name="PUNISH_GRADE" width="" class="select_btn ">
							</select>
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="处分下达日期不能为空">
							<span>处分下达日期:</span> <input class="Wdate" data-iwap-xtype="TextField" onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"    
        				type="text" name="DATE_ARRIVE" id="DATE_ARRIVE" />
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="停发绩效工资月数不能为空">
							<span>停发绩效工资月数:</span> <input name="PUNISH_MONTH_SUM" type="text"
								data-iwap-xtype="TextField" id="PUNISH_MONTH_SUM" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="执行处分起始月份不能为空">
							<span>执行处分起始月份:</span> <input data-iwap-xtype="TextField" id="BEGIN_DATE" name="BEGIN_DATE" class="Wdate" 
							onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM'})"/>
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="执行处分终止月份不能为空">
							<span>执行处分终止月份:</span> <input class="Wdate" data-iwap-xtype="TextField" onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM'})"    
        				type="text" name="END_DATE" id="END_DATE" />
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="处分依据不能为空">
							<span>处分依据:</span> <input name="PUNISH_BASIS" type="text"
								data-iwap-xtype="TextField" id="PUNISH_BASIS" class="input_text"
								style="width: 250" value="">
						</div>
						<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="true">
							<span>备注:</span> <input name="REMARK" type="text"
								data-iwap-xtype="TextField" id="REMARK" class="input_text"
								style="width: 250" value="">
						</div>

					</div>
				</form>
				<!-- form END -->
				<div style="padding-left: 210px">
					<div class="buttonbox">
						<button data-iwap-xtype="ButtonField" id="save"
							class="btn false mr30" data-dialog-hidden="true"
							onclick="doSave()">下一步</button>
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
				<span>用户部门:</span><input name="departmentName" type="text"
					data-iwap-xtype="TextField" id="departmentName" class="input_text" value="">
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>用户姓名:</span><input name="userName" type="text"
					data-iwap-xtype="TextField" id="userName" class="input_text"
					value="">
			</div>
		</div>
		<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>开始时间:</span>
					<input class="input_text"  data-iwap-xtype="TextField"
						onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')||\'new Date()\'}',minDate:'#F{$dp.$D(\'endTime\',{d:-732})}',onpicked:pickedFunc,oncleared:clearedFunc})"   
        				type="text" name="beginTime" id="beginTime" value="" />
			</div>
		</div>
		
		<div class="col-md-6 fl">
			<div class="inputbox tl pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>结束时间:</span>
					<input class="input_text" onfocus="picker2rule(this)" data-iwap-xtype="TextField"  
        				type="text" name="endTime" id="endTime" value="" />
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
		<div class="table_nav" style="display:inline">
				<a id="selectmultidel" class="" onclick="del()"
				href="javaScript:void(0)">删除</a> <a href="javaScript:void(0)"
				data-iwap-dialog="myModal" id="add" onclick="add()">新增</a>
				<a href="javaScript:void(0)" id="export" onclick="exportData()">明细导出</a>
				<a href="javaScript:void(0)" id="tzExport" onclick="tzExportData()">处分台账导出</a>
				
		</div>
		<div style="display:inline">
			<a href="javaScript:void(0)" id="uplaodfile"></a>
		</div>
		<!-- 表格工具栏　END -->
		<table id="iwapGrid"
			class="mygrid table table-bordered table-striped table-hover"
			>
			<tr>
				<th data-grid-name="USER_ID" primary="primary" data-order=""><input
					type="checkbox" name="selectname" selectmulti="selectmulti"
					value=""> <s><input type="checkbox"
						selectmulti="selectmulti" value="{{value}}"></s></th>
				<th data-grid-name="USER_ID" class="tl">序号</th>
				<th data-grid-name="USER_NAME" class="tl">姓名</th>
				<th data-grid-name="FMIS_NO" class="tl">FMIS员工号</th>
				<th data-grid-name="JTYQ" class="tl">停发绩效具体要求</th>
				<th data-grid-name="YFJXGZ" class="tl">当月预发绩效工资标准</th>
				<th data-grid-name="BYTFJE" class="tl">本月停发金额</th>
				<th data-grid-name="SJYFJXGZ" class="tl">实际预发绩效工资</th>
				<th data-grid-name="REMARK" class="tl">备注</th>
				<th data-grid-name="BEGIN_DATE" class="tl">执行工资月份</th>
				<th data-grid-name="ROLE_ID" option="option" option-html=''><span>操作</span>
					<s>
					    <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id ="editOne">修改</a>&nbsp;|&nbsp;
					    <a href="javaScript:void(0)" class="editId" onclick="delOne(this)">删除</a>&nbsp;|&nbsp;
					    <a href="javaScript:void(0)" class="editId" onclick="exportOneData(this)">台账导出</a>
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
			var fData={'actionId':'doBiz','start':'0','limit':'10','txcode':'punish'};
			iwapGrid = $.IWAP.iwapGrid({
				mode:'server',
				fData:fData,
				Url:'${ctx}/iwap.ctrl',
				grid:'grid',
				form:condionForm,
				renderTo:'iwapGrid'
			});	
			// 初始化角色是否启用（采用数据字典）
			initSelectKV('{"PUNISH_GRADE":"PunishGrade" }');

			/* 文件上传 */
			fileUpload = $.IWAP.FileUpload({
			'id':'import_file',
			'url':'iwap.ctrl',
			'fileType':['xlsx','xls'],
			'label':'导入',
			'afterUpload':function(){
				var field={'imp_id':'000010','files':fileUpload.getFileName(),'isOverWrite':'false','modelcode':'punish'};
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

var md = new Date(); //第二个输入框最大值的全局变量
//第一个输入框选择好日期的时候操作
function pickedFunc() {

    var Y = $dp.cal.getP('y'); //用内置方法获取到选中的年月日
    var M = $dp.cal.getP('M');
    var D = $dp.cal.getP('d');
    M = parseInt(M, 10) - 1;
    D = parseInt(D, 10) + 732; //字符串的数字转换成int再运算。并且如果超过30天，也能自动处理。
    var d = new Date()
    d.setFullYear(Y, M, D) //设置时间
    var nowDate = new Date();
    if (nowDate <= d) { //现在的时间比较，如果算出来的值大于现在时间，修改全局变量md为现在时间。
        md = nowDate;
    } else { //全局变量设置为算出来的值得 
        var month = d.getMonth() + 1; //月份的范围是（0到11）;
        md = d.getFullYear() + "-" + month + "-" + d.getDate(); //直接把d给过去会有问题，所以拼成字符串发过去
    }
}
//第一个清空的时候的操作
function clearedFunc() {
    md = new Date();
}
//给第二个输入框定义规则
function picker2rule(ele) {
    WdatePicker({minDate: '#F{$dp.$D(\'beginTime\')}', maxDate: md });
}

//增加
function add(){
	//每次点击增加按钮后：角色是否启用设成默认值
	document.getElementById("reset").style.display = "block";
	document.getElementById("resetDel").style.display = "none";
	$('#myModal').dialog('录入处分信息');
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
	if($('input#DEPARTMENT').val()==''){
		alert("部门名称不能为空");
		return ;
	}
	if($('input#USER_NAME').val()==''){
		alert("姓名不能为空");
		return ;
	}
	if($('input#FMIS_NO').val()==''){
		alert("fmisNo不能为空");
		return ;
	}
	if($('input#PUNISH_MATTER').val()==''){
		alert("处分事项不能为空");
		return ;
	}
	if($('input#DATE_ARRIVE').val()==''){
		alert("处分下达日期不能为空");
		return ;
	}
	if($('input#PUNISH_MONTH_SUM').val()==''){
		alert("停发绩效工资月数不能为空");
		return ;
	}
	if($('input#BEGIN_DATE').val()==''){
		alert("执行处分起始月份不能为空");
		return ;
	}
	if($('input#END_DATE').val()==''){
		alert("执行处分终止月份不能为空");
		return ;
	}
	if($('input#PUNISH_BASIS').val()==''){
		alert("处分依据不能为空");
		return ;
	}
	
	$("#dialogarea").submit();
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
	var param={'actionType':"delete",'txcode':"punish",'list':list,'actionId':"doBiz"};
	iwapGrid.doDelete(param);
};
//删除（单个）
function delOne(obj){
	if (!confirm("确定要删除吗?请确定!")){
		return;
	}
	var list = Array();
	list[0]=iwapGrid.getCurrentRow()['USER_ID'];
	var param={'actionType':"delete",'txcode':"punish",'list': list,'actionId':"doBiz"};
	iwapGrid.doDelete(param);
} 

//返回添加页面
function addEdit(){
	document.getElementById("reset").style.display = "none";
	document.getElementById("resetDel").style.display = "block";
	$('#myModal').dialog("修改角色");
	actionType="update";
	operForm.reset();
	var dapartment = "$(punishInfo.department)";
	var userName = "$(punishInfo.userName)";
	var fmisNo = "$(punishInfo.fmisNo)";
	var punishMatter = "$(punishInfo.punishMatter)";
	var punishGrade = "$(punishInfo.punishGrade)";
	var arriveDate = "$(punishInfo.arriveDate)";
	var monthSum = "$(punishInfo.monthSum)";
	var beginDate = "$(punishInfo.beginDate)";
	var endDate = "$(punishInfo.endDate)";
	var punishBasis = "$(punishInfo.punishBasis)";
	var remark = "$(punishInfo.remark)";
}

//编辑
function edit(obj){
	document.getElementById("reset").style.display = "none";
	document.getElementById("resetDel").style.display = "block";
	$('#myModal').dialog("修改角色");
	actionType="update";
	operForm.reset();
	operForm.setData(iwapGrid.getCurrentRow());
	var grade=iwapGrid.getCurrentRow()['PUNISH_GRADE'];	
	var userId=iwapGrid.getCurrentRow()['USER_ID'];	
	console.log("userId::"+userId);
	$('#USER_ID').val(userId);
	$('select#PUNISH_GRADE').val(grade);
};

function exportData(){
	var data = {'exportFlag':'1','filetype':'xlsx','txcode':'punish','actionId':'doBiz','start':'0','limit':'1000'};
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

function tzExportData(){
	debugger;
	var beginTime=$("#beginTime").val();
	var endTime=$("#endTime").val();
	if(beginTime==''|| endTime==''){
		alert("请您选择开始日期和结束日期！");
		return;
		}
	var name=$("#userName").val();
	var department=$("#departmentName").val();
	var userName = encodeURI(encodeURI(name));
	var departmentName = encodeURI(encodeURI(department));
	var data = {'exportFlag':'1','filetype':'xlsx','txcode':'punish','departmentName':departmentName,'userName':userName,'beginTime':beginTime,'endTime':endTime,'actionId':'doBiz','start':'0','limit':'1000'};
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
	var userId=iwapGrid.getCurrentRow()['USER_ID'];	
	var data = {'exportFlag':'1','filetype':'xlsx','txcode':'punish','userId':userId,'actionId':'doBiz','start':'0','limit':'1000'};
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