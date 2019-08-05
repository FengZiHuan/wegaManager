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
    <script type="text/javascript" src="${ctx}/js/My97DatePicker/calendar.js"></script>
    <script type="text/javascript" src="${ctx}/js/My97DatePicker/WdatePicker.js"></script>
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
            <h4 class="dialog-title">信息</h4>
        </div>
        <div class="modal-body">
            <form method="post" onsubmit="return false" id="dialogarea">
                <!-- form开始 -->
                <div class="col-md-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>部门:</span> <input name="D_DEPT" type="text"
                                                data-iwap-xtype="TextField" id="D_DEPT" class="input_text"
                                                value="" disabled="disabled" >
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>姓名:</span> <input name="D_NAME" type="text"
                                                data-iwap-xtype="TextField" id="O_NAME" class="input_text"
                                                value="" disabled="disabled" >
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>fmis员工号:</span> <input name="D_FMIS" type="text"
                                                     data-iwap-xtype="TextField" id="D_FMIS" class="input_text"
                                                     value="" disabled="disabled" >
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>项目:</span> <input name="D_PROJECT" type="text"
                                                data-iwap-xtype="TextField" id="D_PROJECT" class="input_text"
                                                value="" disabled="disabled" >
                    </div>


                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>分配绩效:</span> <input name="D_JX" type="text"
                                                  data-iwap-xtype="TextField" id="D_JX" class="input_text"
                                                  value="" disabled="disabled" >
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>分配奖励:</span> <input name="D_GZ" type="text"
                                                  data-iwap-xtype="TextField" id="D_GZ" class="input_text"
                                                  value="" disabled="disabled" >
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>待分配绩效:</span> <input name="D_XJX" type="text"
                                                  data-iwap-xtype="TextField" id="D_XJX" class="input_text"
                                                  value="" disabled="disabled" >
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>待分配奖励:</span> <input name="D_XGZ" type="text"
                                                  data-iwap-xtype="TextField" id="D_XGZ" class="input_text"
                                                  value="" disabled="disabled" >
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="缴费月份不能为空">
                        <span>发放月份:</span> <input data-iwap-xtype="TextField" id="D_SENDMONTH" name="D_SENDMONTH" class="Wdate"
                                                  onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM'})"/>
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>发放分配绩效:</span> <input name="D_SENDJX" type="text"
                                                  data-iwap-xtype="TextField" id="D_SENDJX" class="input_text"
                                                  value="" >
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>发放分配奖励:</span> <input name="D_SENDGZ" type="text"
                                                  data-iwap-xtype="TextField" id="D_SENDGZ" class="input_text"
                                                  value=""  >
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
<!-- 页面查询区域开始 -->
<form id="Conditions" class="clearfix">
    <div class="col-md-6 fl">
        <div class="inputbox tl pr" data-iwap="tooltipdiv"
             data-iwap-empty="true">
            <span>姓名:</span><input name="name" type="text"
                                   data-iwap-xtype="TextField" id="name" class="input_text"
                                   value="" >
        </div>
    </div>

    <div class="col-md-6 fl">
        <div class="inputbox tl pr" data-iwap="tooltipdiv"
             data-iwap-empty="true">
            <span>fmis号:</span><input name="fmis" type="text"
                                      data-iwap-xtype="TextField" id="fmis" class="input_text"
                                      value="">
        </div>
    </div>
    <div class="col-md-6 fl">
        <div class="inputbox tl pr" data-iwap="tooltipdiv"
             data-iwap-empty="true">
            <span>项目:</span><input name="project" type="text"
                                   data-iwap-xtype="TextField" id="project" class="input_text"
                                   value="">
        </div>
    </div>
    <div class="col-md-6">
        <div class="inputbox pr" data-iwap="tooltipdiv"
             data-iwap-empty="false" data-iwap-tooltext="年份不能为空">
            <span>发放时间:</span> <input data-iwap-xtype="TextField" id="time" name="time" class="Wdate"
                                      onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM'})"/>
        </div>
    </div>

</form>
</div>
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
           href="javaScript:void(0)">删除</a>
        <a href="javaScript:void(0)" id="export" onclick="exportData()">月份绩效明细导出</a>
    </div>
    <!-- 表格工具栏　END -->
    <table id="iwapGrid"
           class="mygrid table table-bordered table-striped table-hover"
           data-iwap="grid" data-iwap-id="" data-iwap-param=""
           data-iwap-pagination="true">
        <tr>
            <th data-grid-name="D_ID" primary="primary" data-order=""><input
                    type="checkbox" name="selectname" selectmulti="selectmulti"
                    value=""> <s><input type="checkbox"
                                        selectmulti="selectmulti" value="{{value}}"></s></th>
            <th data-grid-name="D_DEPT" class="tl">部门</th>
            <th data-grid-name="D_NAME" class="tl">姓名</th>
            <th data-grid-name="D_FMIS" class="tl">员工号</th>
            <th data-grid-name="D_PROJECT" class="tl">项目</th>
            <th data-grid-name="D_JX" class="tl">分配绩效</th>
            <th data-grid-name="D_GZ" class="tl">分配工资</th>
            <th data-grid-name="D_SENDMONTH" class="tl">发放月份</th>
            <th data-grid-name="D_XJX" class="tl">待发放分配绩效</th>
            <th data-grid-name="D_XGZ" class="tl">待发放奖励工资</th>
            <th data-grid-name="D_ID" option="option" option-html=''><span>操作</span>
                <s>
                    <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id ="editOne">发放绩效奖金</a>&nbsp;|&nbsp;
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
			var fData={'actionId':'doBiz','start':'0','limit':'10','txcode':'managerCheckPerformanceMg'};
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
			initSelectKV('{"sendState":"sendState"}');

			/* 文件上传 */
			fileUpload = $.IWAP.FileUpload({
			'id':'import_file',
			'url':'iwap.ctrl',
			'fileType':['xlsx','xls'],
			'label':'导入',
			'afterUpload':function(){
				var field={'imp_id':'000007','files':fileUpload.getFileName(),'isOverWrite':'false','option':'managerCheckPerformance','start':'1','limit':'10'};
				var data=JSON.stringify({'body':field,'header':{'txcode':"managerCheckPerformanceMg",'actionId':"doBiz"}});
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
								alert("");
							}else if(data['body']['info']){
								 alert(""+data['body']['info']);
							}else {
								 alert("Inport error");
							}
                              location.reload();
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

		$("#uplaod").change(function(){
			fileUpload.upload();
		});


})

//增加
function add(){
	//每次点击增加按钮后：角色是否启用设成默认值
	document.getElementById("reset").style.display = "block";
	document.getElementById("resetDel").style.display = "none";
	$('#myModal').dialog('省行本部绩效奖金');
	actionType="add";
	operForm.reset();
};
//对话框
function dialogModal(id){
	$('#'+id).dialog();
};
//保存
function doSave(){
	var extParam={'option':actionType,'txcode':'managerCheckPerformanceMg','actionId':'doBiz'};
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


//删除（可多个）
function del(){
	if(iwapGrid.getCheckValues()=="") {
		alert("请选择要删除的记录!");
		return;
	}

	if (!confirm("确定要删除吗?请确定!"))
		return;

	var param={'option':"remove",'txcode':"managerCheckPerformanceMg",'userids': iwapGrid.getCheckValues(),'actionId':"doBiz"};
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
	var param={'option':"remove",'txcode':"managerCheckPerformanceMg",'userids': iwapGrid.getCurrentRow()['D_ID'],'actionId':"doBiz"};
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
	$('#myModal').dialog("发放绩效奖金");
	actionType="save";
	operForm.reset();
	operForm.setData(iwapGrid.getCurrentRow());

};

function exportData(){
    var time=  $('#time').val();
    var data = {'exportFlag':'1','filetype':'xlsx','time':time,'option':"export",'txcode':'managerCheckPerformanceMg','holidayids': iwapGrid.getCheckValues(),'actionId':'doBiz','start':'0','limit':'1000'};
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






</script>
</html>