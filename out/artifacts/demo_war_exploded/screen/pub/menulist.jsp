<%@ page language="java" import="com.nantian.iwap.web.WebEnv"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>菜单列表</title>
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
		<div class="modal-body">
			<div id="menu_tree"></div>
			<div style="padding-left: 210px">
				<div class="buttonbox">
					<button data-iwap-xtype="ButtonField" id="save_role"
						class="btn false mr30" data-dialog-hidden="true"
						onclick="doSave()">保存</button>
				</div>
				<div id="" class="buttonbox">
					<button data-iwap-xtype="ButtonField" id="reset_menu"
						class="btn false mr30">清空</button>
				</div>
			</div>
		</div>
</body>
<script type="text/javascript">
var menuTree=null;
$(document).ready(function() {
			//查出系统模块树
			query_sys_module&&query_sys_module();
			$("#reset_menu").click( function(){
				menuTree.setCheckAll(false);
			});
	});

/** 查出系统模块树 */
function query_sys_module(){
	var callFn = function(rs){
		if(!rs.header.msg){
			//$('#tree').empty(); ie报错，改用html
			$('#menu_tree').html("");
			var zNodes = rs['body']['zNodes'];
			zNodes = eval("("+zNodes+")");
			if(zNodes.length <=0){
				alert("权限内无对应数据！");
			}else{
				menuTree = $.IWAP.Tree({
					disabled:false,
					hidden:false,
					value:[],
					checked:true,
					data:zNodes,
					mode:'local',
					renderTo:'menu_tree'
		        });
			}
        }else{
        	alert(rs.header.msg);
        }
	}
	var field = {};
	sendAjax(field,'menuList','doBiz',callFn);
} 

function doSave(){
	debugger;
	var pmodule_id = "";
	var nodes = $.fn.zTree.getZTreeObj(menuTree.getId()).getNodes();
	if(nodes[0]['check_Child_State'] == "2"){
		pmodule_id = menuTree.getValue()[0];
	}else{
		pmodule_id = menuTree.getValue()[1];
	}
	/* if(pmodule_id == ""){
	alert("请选择机构");
	return;
	} */
	if(pmodule_id == undefined){pmodule_id = ""};
	window.parent.document.getElementsByName("pmodule_id")[0].value = pmodule_id;
	var win=$(window.parent.document);
	var myModa2=win.find("#myModal3");
	
	myModa2.find(".close").click();
}
</script>
</html>