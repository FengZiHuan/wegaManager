<%@ page language="java" import="com.nantian.iwap.web.WebEnv"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>菜单管理-查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${ctx}/css/zTreeStyle.css">
<link href="${ctx}/css/font-awesomecss/font-awesome.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/font-awesomecss/font-awesome-ie7.min.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/style.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/css.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/iwapui-style.css" rel="stylesheet" type="text/css">
<!-- 公共 -->
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/UtilTool.js"></script>
<script src="${ctx}/js/bootstrap.min.js"></script>
<!-- 以下为组件JS -->
<script src="${ctx}/js/TextField.js"></script>
<script src="${ctx}/js/ButtonField.js"></script>
<script src="${ctx}/js/public.js"></script>
<!-- tree -->
<script type="text/javascript" src="${ctx}/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.ztree.exhide-3.5.js"></script>
<script src="${ctx}/js/Tree.js"></script>
<script type="text/javascript">
	var menuName = null;
	var menu_tree = null;
	
	$(document).ready(function() {
		/* 普通文本框 */
		menuName = $.IWAP.TextField({
			label:'菜单名称',
			renderTo:'menuName',
		});
		
		/** 清空数据 */
		$("#btn_clear").click(function(){
			$("input").val("");
		});
		
		$("#btn_query").click(function(){
			doQuery();
		});
		
		dataLoad('%');
		
	});
	
	function doQuery(){
		dataLoad(menuName.getValue());
	}
	
	function onClick(event, treeId, treeNode){
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		zTree.expandNode(treeNode);
	}
	
	
	function dataLoad(menuName){
		var callFn = function(rs){
			if(!rs.header.msg){
				//$('#tree').empty(); ie报错，改用html
				$('#tree').html("");
				var zNodes = rs['body']['zNodes'];
				zNodes = eval("("+zNodes+")");
				if(zNodes.length <=0){
					alert("菜单内无对应数据！");
				}else{
					menu_tree = $.IWAP.Tree({
						onClick:onClick,
						//addDiyDom:addDiyDom,
						disabled:false,
						hidden:false,
						value:[],
						checked:false,
						data:zNodes,
						mode:'local',
						btn2:true,
				        btn2Text:"修改",
				        btn2style:"b_blue",
				        btn3:true,
				        btn3Text:"删除",
				        btn3style:"b_red",
						renderTo:'tree'
			        });
			        menu_tree.hiddenRmenu();
			        $.IWAP.Tree.addbtn2= function(btnId,treeId, treeNode){
				         doEdit(treeNode.id);
				    }
				        
				    $.IWAP.Tree.addbtn3= function(btnId,treeId, treeNode){
				         doRemove(treeNode.id);
				    }
				}
	        }else{
	        	alert(rs.header.msg);
	        }
		}
		var form = {'menuName':'%'+menuName+'%'};
		sendAjax(form,'menuMg','doBiz',callFn);
	}
	
	function doEdit(module_id){
		window.location="${ctx}/iwap.ctrl?txcode=menuMg&option=save&moduleId="+module_id;
	}
	
	function doRemove(module_id){
		if(!confirm("确定要删除吗?请确定!"))return;
		var callFn = function(rs){
			if(!rs.header.msg){
				doQuery();
				alert("删除成功");
	        }else{
	        	alert(rs.header.msg);
	        }
		}
		var form = {'module_id':""+module_id,'option':'remove'};

		sendAjax(form,'menuMg','doBiz',callFn);
	}
	
</script>
</head>
<body class="iwapui center_body">
	<input type="hidden" id="_deptId" value="${userInfo.ORG_ID}" />
	<div class="">
		<ul class="linelist">
			<li class="clearfix">
				<div class="inputbox pr" id="menuName"></div>
			</li>
		</ul>
	</div>
	<div class="search_btn">
		<a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_query">查询</a> 
		<a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_clear">清空</a> 
		<a href="${ctx}/iwap.ctrl?txcode=menuMg&option=add" class="btn btn-primary" id="btn_add">新增</a> 
	</div>
	<hr>
	<div class="table_box">
		<br>
		<div id="tree"></div>
	</div>
</body>
</html>
