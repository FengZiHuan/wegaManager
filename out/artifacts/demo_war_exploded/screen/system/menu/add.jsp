<%@ page language="java" import="com.nantian.iwap.web.WebEnv"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>添加菜单</title>
<link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/css.css" rel="stylesheet">
<link href="${ctx}/css/style.css" rel="stylesheet">
<link href="${ctx}/css/iwapui-style.css" rel="stylesheet" type="text/css">
<!-- JQ必须在最JS上面 -->
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/UtilTool.js"></script>
<script src="${ctx}/js/TextField.js"></script>
<script src="${ctx}/js/ListField.js"></script>
<script type="text/javascript" src="${ctx}/js/iwapGrid.js"></script>
<script type="text/javascript" src="${ctx}/js/iwapui.js"></script>
<script type='text/javascript' src="${ctx}/js/String.js"></script>
<script type='text/javascript' src="${ctx}/js/dictionary.js"></script>
<script src="${ctx}/js/public.js"></script>
<script type="text/javascript">
	var menuTree=  null;
	$(document).ready(function(){
		// 初始化模块类型,有效性,模块状态 （采用数据字典）
	    initSelectKV('{"module_type":"MODULE_TYPE","module_valid":"IS_VALID","module_hide":"IS_HIDE"}');
		
		//清空
		$("#reset").click(function(){
			$("input:not([type=hidden])").val("");
			$("#module_type")[0].selectedIndex = 0;
			$("#module_valid")[0].selectedIndex = 0;
			$("#module_hide")[0].selectedIndex = 0;
		});
		$("#back").click(function(){
			window.location = "${ctx}/iwap.ctrl?txcode=menuMg";
		});
	});
	
	//新增
	function doSave(){
		if(validate()){
			var callFn = function(rs){
				if(!rs.header.msg){
					window.location="${ctx}/iwap.ctrl?txcode=menuMg";
				}else{
					alert(rs.header.msg);
				}
			}
			var form = $("form").serializeObject();
			form['option'] = "add";
			form['pmodule_id'] = $("#pmodule_id").val();
			console.log(form);
			sendAjax(form,'menuMg',"doBiz",callFn);
		}
	}
	
	function validate(){
		$('#area div[data-iwap-empty="false"]').each(function(){
			var spanText=$(this).attr("data-iwap-tooltext");
			var inputVal=$(this).find('input').val();
			var type = $(this).find('input').attr("name");
			
			if(inputVal!=undefined){
				if($("#module_type").val() == "0"){
					if(inputVal==""||inputVal== null){
						if(type == "pmodule_id"){
							return;
						}else{
							alert(spanText);
						}
	    				
	    				num++
	    			}
					
				}else{
					if(inputVal==""||inputVal== null){
	    				alert(spanText);
	    				num++
	    			}
				} 
				
			}
		});
		
		//模块排序值：需填正整数
		if(!/^\d+(?=\.{0,1}\d+$|$)/.test($('#module_order').val())){
    		alert($('#module_order').prev('span').text()+"只能为正整数");
    		return false;
    	}
		 //操作码请求参数
		if($("#module_type").val() == "2" && $("#param_opcode").val() == "" || $("#param_opcode").val() == null){
			alert($('#param_opcode').prev('span').text()+"不能为空");
			return false;
		}
		 return true;
	}
	
	//对话框
	function dialogModal(id){
		$('#'+id).dialog();
	};
	
</script>
</head>
<body class="iwapui center_body">
<div class="subnav_box">
	<ul class="breadcrumb">
		<li class="active">新增菜单</li>
	</ul>
</div>
	<!-- 对话框开始 -->
	<div id="divDialog" class="divDialog">
	<div class="bg"></div>
	<div class="dialog" id="myModal3" style="width: 600px;">
		<div class="dialog-header">
			<button type="button" data-dialog-hidden="true" class="close">
				<span>×</span>
			</button>
			<h4 class="dialog-title">选择上级模块</h4>
		</div>
		<div class="modal-body">
				<iframe style="height: 300px; width: 560px"
					src="iwap.ctrl?txcode=menuList"></iframe>
		</div>
	</div>
	</div>
	<!-- 对话框END -->
	<div class="center">
		<div class="w500mauto">
		  <form method="post" onsubmit="return false" id="area">
			<ul>
				<li>
					<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="true" data-iwap-tooltext="模块操作码不能为空">
							<span>模块操作码:</span> <input name="opcode" type="text"
								data-iwap-xtype="TextField" id="opcode" class="input_text">
					</div>
				</li>
				<li>
					<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="模块名称不能为空">
							<span>模块名称:</span> <input name="module_nm" type="text"
								data-iwap-xtype="TextField" id="module_nm" class="input_text"
								style="width: 250" value="">
					</div>
				</li>
				<li>
					<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="true" data-iwap-tooltext="上级模块编号不能为空">
							<span>上级模块编号:</span> <input name="pmodule_id" type="text"  disabled="disabled"
								type="text" data-iwap-xtype="TextField" id="pmodule_id" class="input_text" 
								style="width: 250" value="" onkeyup="suggestWord(this)" autocomplete="off"
								onclick="showMenu();"> <a href='javascript:void(0)'
								class='btn btn-primary' onclick='dialogModal("myModal3")' id="show_menu"></a>
					</div>
				</li>
				<li>
					<div class="selectbox mr60 inputbox">
							<span>模块类型:</span> <select data-iwap-xtype="ListField"
								id="module_type" name="module_type" width="" class="select_btn ">
							</select>
					</div>
				</li>
				<li>
					<div class="selectbox mr60 inputbox">
							<span>是否有效:</span> <select data-iwap-xtype="ListField"
								id="module_valid" name="module_valid" width="" class="select_btn ">
							</select>
					</div>
				</li>
				<li>
					<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="true" data-iwap-tooltext="">
							<span>帮助标题:</span> <input name="help_title" type="text"
								data-iwap-xtype="TextField" id="help_title" class="input_text"
								style="width: 250" value="">
					</div>
				</li>
				<li>
					<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="true" data-iwap-tooltext="">
							<span>帮助页面:</span> <input name="help_page" type="text"
								data-iwap-xtype="TextField" id="help_page" class="input_text"
								style="width: 250" value="">
					</div>
				</li>
				<li>
					<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="false" data-iwap-tooltext="模块排序值只能为正整数">
							<span>模块排序值:</span> <input name="module_order" type="text"
								data-iwap-xtype="TextField" id="module_order" class="input_text"
								style="width: 250" value="">
					</div>
				</li>
				<li>
					<div class="selectbox mr60 inputbox">
							<span>模块状态:</span> <select data-iwap-xtype="ListField"
								id="module_hide" name="module_hide" width="" class="select_btn ">
							</select>
					</div>
				</li>
				<li>
					<div class="inputbox pr" data-iwap="tooltipdiv"
							data-iwap-empty="true" data-iwap-tooltext="操作码请求参数不能为空">
							<span>操作码请求参数:</span> <input name="param_opcode" type="text"
								data-iwap-xtype="TextField" id="param_opcode" class="input_text"
								style="width: 250" value="">
					</div>
				</li>
				<li>
					<div style="padding-left: 150px">
						<div class="buttonbox">
							<button data-iwap-xtype="ButtonField" id="save"
								class="btn false mr30" onclick="doSave()">保存</button>
						</div>
						<div id="" class="buttonbox">
							<button data-iwap-xtype="ButtonField" id="reset"
								class="btn false mr30">清空</button>
						</div>
						<div id="" class="buttonbox">
							<button data-iwap-xtype="ButtonField" id="back"
								class="btn false mr30">返回</button>
						</div>
					</div>
				</li>
			</ul>
		  </form>
		</div>
	</div>
	
</body>
</html>