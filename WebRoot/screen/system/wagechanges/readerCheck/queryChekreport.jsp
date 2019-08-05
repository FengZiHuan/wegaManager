<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/screen/comm/header.jsp" %>
<link href="<%=path %>/css/style.css" rel="stylesheet">
<link href="<%=path %>/css/zTreeStyle.css" rel="stylesheet">
<script type="text/javascript" src="<%=path %>/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=path %>/js/iwapui.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="<%=path %>/js/Tree.js"></script>
<script type="text/javascript" src="<%=path %>/js/FileUpload.js"></script>
<script type='text/javascript' src="<%=path %>/js/dictionary.js"></script>
<script type='text/javascript' src="<%=path %>/js/public.js"></script>
<!-- 现场核查查询 -->
<title>现场核查查询</title>
<meta name="description" content="">
<meta name="keywords" content="">
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

.button_caozuo{
    border-radius:2px;
    padding: 1px 5px;
    background-color: #008CBA;
    color: white;
    margin-left:4px;
    margin-reight:4px;

}
</style>
</head>
<body class="iwapui center_body">
	<input type="hidden" value="${userInfo.ORG_ID}" id="_deptid">

	<!-- 标题 -->
	<!-- <div class="pageTitle">现场核查</div> -->
    <div class="h3" style="text-align:center;">现场核查</div>
    <!-- 页面查询区域开始 -->
    <!-- ，公司名称，访问地点，谈访主题，经办人，创建时间（按区间）企业类型 -->
	<form id="Conditions" class="clearfix">
	    <div class="col-md-4 fl" style="margin-bottom: 5px">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>公司名称:</span><input name="chnName" type="text"
					data-iwap-xtype="TextField" id="chnName" class="input_text" value="" style="width:200px" >
			</div>
		</div>
		
		<div class="col-md-4 fl" style="margin-bottom: 5px">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>访问地点:</span><input name="crPlace" type="text"
					data-iwap-xtype="TextField" id="crPlace" class="input_text" value="" style="width:200px" >
			</div>
		</div>
				<div class="col-md-4 fl" style="margin-bottom: 5px">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>谈访主题:</span><input name="crTheme" type="text"
					data-iwap-xtype="TextField" id="crTheme" class="input_text" value="" style="width:200px" >
			</div>
		</div>
				<div class="col-md-4 fl" style="margin-bottom: 5px">
			<div class="inputbox pr" data-iwap="tooltipdiv"
				data-iwap-empty="true">
				<span>经办人:</span><input name="crOperator" type="text"
					data-iwap-xtype="TextField" id="crOperator" class="input_text" value="" style="width:200px" >
			</div>
		</div>
		 <div class="col-md-4 fl" style="margin-bottom: 5px">
			<div class="selectbox tl  pr inputbox">
				<span>企业类型:</span>
					<select data-iwap-xtype="ListField" name="cpType"
					class="select_content" id="cpType" style="width:200px" >
					<option value="">==全部==</option>
				</select> 
			</div>
		</div>
		
				
		<div class="col-md-4 fl" style="margin-bottom: 5px">
			<div class="inputbox pr" data-iwap="tooltipdiv" data-iwap-empty="true">
				<span>创建时间:</span><input type="text" name="crStartCreateDate"
						data-iwap-xtype="TextField" id="crStartCreateDate"
						class="input_text" style="width: 95px;" 
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'1900-01-01',skin:'default'})"> -
					<input type="text" name="crEndCreateDate" 
						data-iwap-xtype="TextField" id="crEndCreateDate"
						class="input_text" style="width: 95px;" 
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'1900-01-01',skin:'default'})">
			</div>
		</div>
		
	</form>
	
	<div class="tc mb14">
		<a href="javaScript:void(0)" class="btn btn-primary mr30" id="query" onclick="iwapGrid.doQuery();">查询</a>
	    <a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_clear" onclick="iwapGrid.doReset();">清空</a>
	</div>
	<!-- 查询内容区域　开始 -->
	<div class="table_box">
		<!-- 表格工具栏　开始 -->
		<!-- <div class="table_nav">
			
			<a href="javaScript:void(0)" class="editId" onclick="edit(this)" id ="editOne">修改</a>
			<a href="javaScript:void(0)" class="" onclick="del()" id="selectmultidel">删除</a> 
			<a href="javaScript:void(0)" data-iwap-dialog="myModal" id="add" onclick="add()">新增</a>
			<a href="javaScript:void(0)" class="editId" onclick="grant(this)" id ="grant">授权</a>
			<a href="javaScript:void(0)" id="export" onclick="exportData()">导出</a>
			
			<a href="javaScript:void(0)" class="editId" onclick="detail(this)" id ="editOne">查看详情</a>
		</div> -->
		<!-- 表格工具栏　END -->
		<table id="iwapGrid"
			class="mygrid table table-bordered table-striped table-hover "
			data-iwap="grid" data-iwap-id="" data-iwap-param=""
			data-iwap-pagination="true">
			<tr><!-- 公司名称，访问地点，谈访主题，经办人，创建时间，详情（点开按模板展示，02需求中的贷后现场检查工作日志） -->
				<th data-grid-name="CHN_NAME" class="tl col-md-2">公司名称</th>
				<th data-grid-name="CR_PLACE" class="tl col-md-2">访问地点</th>
				<th data-grid-name="CR_THEME" class="tl col-md-2">谈访主题</th>									
				<th data-grid-name="CR_OPERATOR" class="tl col-md-2">经办人</th>
				<th data-grid-name="CREATEDATE" class="tl col-md-2">创建时间</th>
				<th data-grid-name="CR_PDF" class="tl col-md-2">操作
				   <s><button href="javaScript:void(0)" class="editId button_caozuo"  onclick="setPdfPath()"  id ="editOne">生成PDF</button>
				   <button href="javaScript:void(0)" class="editId button_caozuo" onclick="seePicture()"  id ="editOne">查看图片</a></button>
				</th>
				<!-- <th data-grid-name="MP_INFO" class="tl">信息内容
				   <s><a href="javaScript:void(0)" class="editId" onclick="detail(this)"  id ="editOne">查看</a></s>
				</th> -->
			</tr>
		</table>
	</div>
	<!-- 查询内容区域　结束 -->
		
<!-- 对话框区域开始 -->	
<div id="divDialog" class="divDialog">
		<div class="bg"></div>
		<div class="dialog" id="myModal" style="width: 600px;height:400px;">
			<div class="dialog-header">
				<button type="button" data-dialog-hidden="true" class="close">
					<span>×</span>
				</button>
				<h4 class="dialog-title">增加用户</h4>
			</div>
			<div class="modal-body">
					
					<div id="picture1" style="display:block;width:100%;height:320px;overflow:auto;padding-left:48px;padding-right:auto">
                     
					</div>	
		</div>
	</div>
	<div class="bg" style="position:fixed;"></div>
		<div class="dialog" id="myModal2" style="width: 400px;height:600px;position: fixed;left: 50%;top: 50%;transform: translate(40px,-350px);">
			<div class="dialog-header">
				<button type="button" data-dialog-hidden="true" class="close">
					<span>×</span>
				</button>
				<h4 class="dialog-title">查看照片</h4>
			</div>
			<div class="modal-body" id="big_img" style="display:flex;width:300px;height:300px;">
		        
			</div>
		</div>
</div>
<!-- 对话框结束 -->
<script type="text/javascript">
var actionType="", iwapGrid=null,pwdForm=null,condionForm=null,operForm=null,roleForm=null;
$(document).ready(function() {
	condionForm=$.IWAP.Form({'id':'Conditions'});
	afterInitSelectKV();
	initSelectKV('{"cpType":"CP_TYPE" }');
	
	
	
	
	
});


function afterInitSelectKV(){
	/*查询表格初始化  设置默认查询条件*/
	var fData={'actionId':'doBiz','start':'0','limit':'10','txcode':'chekreport'};
	iwapGrid = $.IWAP.iwapGrid({
		mode:'server',
		fData:fData,
		Url:'iwap.ctrl',
		grid:'grid',
		form:condionForm,
		renderTo:'iwapGrid',
		noCheckbox:true
	});	
}

//获取pdf地址
//var cr_id =null;
function setPdfPath(msg){	
	var cr_pdf = iwapGrid.getCurrentRow()['CR_PDF'];
	var cr_id = iwapGrid.getCurrentRow()['CR_ID'];
	doExport();
	//var o = queryById(cr_id);
	//console.info("****obj****"+o);
		//window.location.href = "screen/clns/queryInfo/pdfShowPage.jsp?cr_id="+cr_id;
		//window.location.href = cr_pdf;
		//window.open(cr_pdf,cr_pdf);

}

//生成PDF文件
function doExport(){
	var cr_id = iwapGrid.getCurrentRow()['CR_ID'];
	var cp_type = iwapGrid.getCurrentRow()['CP_TYPE'];
	var extParam = {'txcode':'expCheckLog','actionId':'doBiz','cr_id':cr_id,'cp_type':cp_type}
	$.IWAP.iwapRequest("iwap.ctrl",extParam,function(rs){
		if(rs["header"]["msg"]){
			return alert("导出失败:"+rs['header']['msg']);
		}else{
			//alert("导出成功");
			iwapGrid.doQuery(condionForm.getData());
		}
		queryById(cr_id);
		
	},function(){
		alert("导出失败");
	});
	
}
 //查寻地址
function queryById(crId){
	var param={'actionId':'doBiz','txcode':'chekreport','crId':crId};
	
	$.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
		var obj = rs.body.rows[0];
		
		window.open(obj["CR_PDF"],obj["CR_PDF"]);
	
		
		//票据类隐藏于展示  name="note"    $("input[name='newsletter']").attr("checked", true);
	
	},function(accpStatus){
		alert("[editBkoCustomCompInfo]获取修改客户信息失败!");
	});
}
 
function seePicture(){
	
	$("#picture1").empty();
	$('#myModal').dialog("照片");
	var pagramName = new Array();
	pagramName = window.location.pathname.split("/");
	actionType="show";	
	//operForm.setData(iwapGrid.getCurrentRow());
	var photos1 = iwapGrid.getCurrentRow()['CR_IMAGE'];
	if(!(photos1 == "" || photos1 == null)){
		var picture1 = new Array();
		console.info("picture1"+photos1);
		console.info("picture1[0]"+photos1[0]);
		console.info("picture1[1]"+photos1[1]);
		photos1 = photos1.replace("[","");
		photos1 = photos1.replace("]","");
		 picture1=photos1.split(',');					
	    for(var i=0;i<picture1.length;i++){
			console.info("SC_PHOTOREC_IMG["+i+"]"+picture1[i]);
			$("#picture1").append("<img id='image_"+i+"' onclick='seeBigPicture("+i+")' alt="+picture1[i]+" style='cursor:pointer;width: 150px;height: 150px;' src=/"+pagramName[1]+"/"+picture1[i]+">&nbsp;");
			if((i+1)%3 == 0){
				$("#picture1").append("<br/><br/>");
			}
		}  
		//$("#mpInfo1").html(iwapGrid.getCurrentRow()['CR_IMAGE']);
	}else{
		
		$("#picture1").append("<span>暂无图片</span>&nbsp;");
		}
}

function seeBigPicture(i){
	$("#big_img").empty();
	$('#myModal2').dialog("照片详情");
	actionType="show";
	console.info(i);
   var imgPath = $("#image_"+i)[0].src;
   console.info("imgPath"+imgPath);
   $("#big_img").append("<img alt="+imgPath+" style='width:auto;height:500px;' src="+imgPath+">&nbsp;");
}
 
</script>
</body>
</html>
