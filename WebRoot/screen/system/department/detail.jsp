<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修改机构信息</title>
<link href="${ctx}/css/font-awesomecss/font-awesome.css"
	rel="stylesheet" type="text/css">
<link href="${ctx}/css/font-awesomecss/font-awesome-ie7.min.css"
	rel="stylesheet" type="text/css">
<link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css">
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/UtilTool.js"></script>
<link href="${ctx}/css/style.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/css.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/iwapui-style.css" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="${ctx}/js/UtilTool.js"></script>
<script src="${ctx}/js/TextField.js"></script>
<script src="${ctx}/js/ButtonField.js"></script>
<script src="${ctx}/js/public.js"></script>
<!-- 表单 -->
<script src="${ctx}/js/Form.js"></script>
<script type="text/javascript">

	var deptid_show = null;
	var deptname = null;
	var deptfullname = null;
	var parentname = null;
	var deptlevel_key = null;
	var state_key = null;
	
	$(document).ready(function(){
		deptid_show=$.IWAP.TextField({
			label:'机构编号',
			allowBlank:false,
			renderTo:'deptid_show'
		});
		$("#"+deptid_show.getId()).attr("disabled",true);
		deptid_show.clearInvalid();
		
		deptname = $.IWAP.TextField({
			label:'机构名称',
			allowBlank:false,
			validatorText:'机构名称长度最大为20且不为空',
			maxLength:20,
			renderTo:'deptname'
		});
		
		deptfullname = $.IWAP.TextField({
			label:'机构全称',
			allowBlank:true,
			renderTo:'deptfullname',
			validatorText:'机构全称长度最大为40',
			maxLength:40
		});
		
		parentname = $.IWAP.TextField({
			label:'上级机构',
			renderTo:'parentname'
		});
		$("#"+parentname.getId()).attr("disabled",true);

		/*初始化按钮*/
		var buttonbox=null;
        buttonbox1=$.IWAP.ButtonField({
            label: '确　定',
            click:save,
            renderTo:"buttonbox",
            disabled:false
        });
        buttonbox1=$.IWAP.ButtonField({
            label: '清　空',
            click:function clear(){
        		$("input:not(:disabled)").val("");
        		$("#state")[0].selectedIndex = 0;
        	},
            renderTo:"buttonbox",
            disabled:false
        });
        buttonbox1=$.IWAP.ButtonField({
            label: '返　回',
            click:function back(){
        		window.location="${ctx}/iwap.ctrl?txcode=department";
        	},
            renderTo:"buttonbox",
            disabled:false
        });
        
       	var callFn = function(rs){
			var dataObj = rs['body'].rows;
			deptid_show.setValue(dataObj['ORG_ID']);
			deptname.setValue(dataObj['ORG_NM']);
			deptfullname.setValue(dataObj['ORG_FUL_NM']);
			parentname.setValue(dataObj['ORG_PNM']);
			deptlevel_key = dataObj['ORG_LVL'];
			state_key = dataObj['ORG_STATUS'];
			$("#"+parentname.getId()).attr("disabled",true);
			getDictionary();
		}
		var form = {'deptid':$("#deptid").val(),'option':'show'};
		sendAjax(form,'showDepartment','doBiz',callFn);
        
	});
	//保存
	function save(){
		if(deptname.validate()){
			var callFn = function(rs){
				if(rs['header']['msg']){
					alert("机构显示出错:"+rs['header']['msg']);
				}else{
					alert("保存成功");
					window.location="${ctx}/iwap.ctrl?txcode=department";
				}
			}
			
			var form = {'deptid':deptid_show.getValue(),'name':deptname.getValue(),
						'fullname':deptfullname.getValue(),'state':$("#state").val(),
				     	'option':'save'};
			sendAjax(form,'showDepartment','doBiz',callFn);
		}
	}
	
	function getDictionary(){
		var callFn = function(rs){
			var deptLevel = rs['body'].Dept_Level;
			var state = rs['body'].Dept_State; 	
			
			/** 填充状态 */
			$.each(deptLevel,function(){
				$("<option/>").val(this.id)
				  .text(this.name)
				  .attr("selected",this.id == deptlevel_key)
				  .appendTo("#deptlevel");
			});
			$.each(state,function(){
				$("<option/>").val(this.id)
				  .text(this.name)
				  .attr("selected",this.id == state_key)
				  .appendTo("#state");
			});	
			$("#deptlevel").attr("disabled",true);
			if(deptlevel_key==1){
				$("#state").attr("disabled",false);
			}
		}
		var form = {'dictNm':'Dept_Level,Dept_State'};
		sendAjax(form,'dictionary',1,callFn);

	}
</script>
</head>
<body class="iwapui center_body">
	<% String deptid = request.getParameter("deptid"); %>

	<div class="subnav_box">
		<ul class="breadcrumb">
			<li class="active">修改机构</li>
		</ul>
	</div>
	<div class="center">
		<input type="hidden" id="deptid" value="<%=deptid %>"
			disabled="disabled">
		<div class="input_body"
			style="margin-top: 50px; font-size: 14px; font-weight: normal; line-height: 20px;">
			<ul>
				<li id="deptid_show" name="deptid_show"></li>
				<li id="deptname" name="deptname"></li>
				<li id="deptfullname" name="deptfullname"></li>
				<li>
					<div class="selectbox">
						<label class="select_label" style="margin-left: -5px;">机构等级:</label>
						<select name="deptlevel" class="select_content" id="deptlevel"
							style="margin-left: 8px;"></select>
					</div>
				</li>
				<li id="parent_li">
					<div class="inputbox pr" id="parentname"></div>
				</li>
				<li>
					<div class="selectbox">
						<label class="select_label" style="margin-left: -5px;">机构状态:</label>
						<select name="state" class="select_content" id="state"
							style="margin-left: 8px;"></select>
					</div>
				</li>
				<li style="margin-top: 40px; margin-left: 45px;">
					<div id="buttonbox" class="marginauto clearfix buttonbody"
						style="display: inline;"></div>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>