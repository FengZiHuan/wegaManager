<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加机构</title>
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
<!-- 对话框 -->
<script src="${ctx}/js/bootstrap.min.js"></script>
<script src="${ctx}/js/dialog.js"></script>
<link rel="stylesheet" href="${ctx}/css/zTreeStyle.css">
<script type="text/javascript" src="${ctx}/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript"
	src="${ctx}/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript">
	var deptid = null;
	var deptname = null;
	var deptfullname = null;
	var parentid = null;
	var deptPName=null;
	var address = null;
	//下拉框树的配置		
	var settingT = {//ztree配置
			check: {
				enable: true,
				chkStyle: "radio",
				radioType: "all"
			},
			view: {
				dblClickExpand: false
			},
			data: {
				simpleData: {
					enable: true,
					idKey: "id",
					pIdKey: "pid",
					rootPId: 0
				}
			},
			callback: {
				onClick: function(e, treeId, treeNode) {
							var zTree = jQuery.fn.zTree.getZTreeObj("tree");
							zTree.checkNode(treeNode, !treeNode.checked, null, true);
							return false;
						},
				onCheck: function(e, treeId, treeNode) {
							var zTree = jQuery.fn.zTree.getZTreeObj("tree"),
							nodes = zTree.getCheckedNodes(true);
							var name=parentName.getId();
							var id=parentid.getId();
							if(nodes.length>0){
								var nodeName = nodes[0].name,nodeId=nodes[0].id;
								
								$('#'+name).val(nodeName);
								$('#'+id).val(nodeId);
							}else{
								$('#'+name).val(nodeName);
								$('#'+id).val(nodeId);
							}
					  }
						
				}
			};
	$(document).ready(function(){
		deptid=$.IWAP.TextField({
			label:'机构编号&nbsp;',
			allowBlank:false,
			validatorText:'机构编号为9位数字且不为空',
			validator:function(){
				var number = deptid.getValue();
				if(number.length>9){
					return false;
				}
				if(/^[0-9]{9}$/.test(number)){
	        		deptid.clearInvalid();
	        		return true;
	        	}else{return false;}
			},
			renderTo:'deptid'
		});
		
		deptname = $.IWAP.TextField({
			label:'机构名称&nbsp;',
			allowBlank:false,
			validatorText:'机构名称长度最大为20且不为空',
			maxLength:20,
			renderTo:'deptname'
		});
		
		deptfullname = $.IWAP.TextField({
			label:'机构全称',
			allowBlank:true,
			validatorText:'机构全称长度最大为40',
			maxLength:40,
			renderTo:'deptfullname'
		});
		parentid = $.IWAP.TextField({
			label:'上级机构编号&nbsp;',
			allowBlank:false,
			validatorText:'上级机构编号不能为空',
			disabled:true,
			renderTo:'parentid'
		});
		
		parentName= $.IWAP.TextField({
			label:'上级机构名称',
			allowBlank:false,
			nullText:'机构不能为空',
			renderTo:'parentName1'
		}); 
		
		address = $.IWAP.TextField({
			label:'地址',
			allowBlank:true,
			renderTo:'address',
			validatorText:'地址长度最大为42',
			maxLength:42
		});
	    
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
        		$("input").val("");
        		$("#deptlevel")[0].selectedIndex = 0;
        		$("#state")[0].selectedIndex = 0;
        	},
            renderTo:"buttonbox",
            disabled:false
        });
        buttonbox1=$.IWAP.ButtonField({
            label: '取　消',
            click:function cancle(){
        		window.location="${ctx}/iwap.ctrl?txcode=department";
        	},
            renderTo:"buttonbox",
            disabled:false
        });
        
        $("#deptHelp").click(function(){
			/* 初始化对话框 */
			deptHelp = $.IWAP.Dialog({
				title:'请选择机构',
				buttons:['查询','确定'],
				height:400,
				width:500,
				listeners:{
					afterClose:function(){//关闭后回调的函数
						
					}
				}
			});
			
		});
        $( "#"+parentName.getId()).on('keyup',searchOrg) ;
	});
	
	var callFnOrg=function(rs){
		/*机构数初始化*/
		var zNodes=[];
		var rv=rs['body']['rows'];
		if(rv.length==undefined){
			var obj = rv;
  			var vPid =obj['org_pid'];
  			var vNode={id:obj['org_id'],pid:vPid,name:obj['org_nm'],prop:obj };
  			zNodes.push(vNode);
			jQuery.fn.zTree.init(jQuery("#tree"), settingT, zNodes);
		}else{
			for(var i =0; i<rv.length;i++){
	  			var obj = rv[i];
	  			var vPid =obj['org_pid'];
	  			if(vPid==null || vPid=='null'||vPid==''){
	  				vPid='0';
	  			}
	  			var vNode={id:obj['org_id'],pid:vPid,name:obj['org_nm'],prop:obj };
	  			zNodes.push(vNode);
			}
			jQuery.fn.zTree.init(jQuery("#tree"), settingT, zNodes);
		}
		
	}
	
	function searchOrg(txobj){
		//alert("org");
		var name=this.value;
		if(name.trim()==''){
			var form = {'name':'%%','org_id':${userInfo.ORG_ID},'org_id':'${userInfo.ORG_ID}','option':'list'};
			sendAjax(form,'departmentList','doBiz',callFnOrg);
		}else{
			var form = {'name':"%"+name+"%",'org_id':${userInfo.ORG_ID},'org_id1':'${userInfo.ORG_ID}','option':'listOrg'};
			//var form ={'name':name,'org_id':'${userInfo.ORG_ID}'} ;
			sendAjax(form,'departmentListQuery','doBiz',callBackOrg);
		}
		
	}
	 var t;
		var callBackOrg = function (rs){
			 var zNodes=[];
			 var rv=rs['body']['rows'];
			 if(rv.length==undefined){
					var obj = rv;
		  			var vPid =obj['ORG_PID'];
		  			var vNode={id:obj['ORG_ID'],pid:vPid,name:obj['ORG_NM'],prop:obj };
		  			zNodes.push(vNode);
					jQuery.fn.zTree.init(jQuery("#tree"), settingT, zNodes);
			}else{
					for(var i =0; i<rv.length;i++){
				  		var obj = rv[i];
				  		var vPid =obj['ORG_PID'];
				  		if(vPid==null || vPid=='null'||vPid==''){
				  			vPid='0';
				  		}
				  		var vNode={id:obj['ORG_ID'],pid:vPid,name:obj['ORG_NM'],prop:obj };
				  		zNodes.push(vNode);
					}
					jQuery.fn.zTree.init(jQuery("#tree"), settingT, zNodes);
				}
			
	}
	function save(){
		if(deptid.validate()&&deptname.validate()){
			if($("#deptlevel").val()!='0'){
				if(parentid.validate()){
					var callFn = function(rs){
						if(rs['header']['msg']){
							alert("保存出错:"+rs['header']['msg']);
						}else{
							window.location="${ctx}/iwap.ctrl?txcode=department";

						}
					}
					var form = {'deptid':deptid.getValue(),'name':deptname.getValue(),'fullname':deptfullname.getValue(),
							'deptlevel':$("#deptlevel").val(),'parentid':parentid.getValue(),
							'state':$("#state").val(),'option':'add'};
				sendAjax(form,'addDepartment',"doBiz",callFn);
				}
			}else{
				parentid.clearInvalid();
				var callFn = function(rs){
					if(rs['header']['msg']){
						alert("保存出错:"+rs['header']['msg']);
					}else{
						window.location="${ctx}/iwap.ctrl?txcode=department";

					}
				}
				var form = {'deptid':deptid.getValue(),'name':deptname.getValue(),'fullname':deptfullname.getValue(),
						'deptlevel':$("#deptlevel").val(),'parentid':parentid.getValue(),
						'state':$("#state").val(),'option':'add'};
			sendAjax(form,'addDepartment',"doBiz",callFn);
			}

		}
	}
	
	function showMenu() {
		//var productCode = $("#ORG_NAME");
		var id=parentName.getId();
		var codeOffset = $("#"+id).offset();
		jQuery("#menuContent").slideDown("fast");
		jQuery("body").bind("mousedown", onBodyDown);
		
	}
	function hideMenu() {
		jQuery("#menuContent").fadeOut("fast");
		jQuery("body").unbind("mousedown", onBodyDown);
	}
	function onBodyDown(event) {
		if (!(event.target.id == "menuBtn" || event.target.id == "citySel" || event.target.id == "menuContent" || jQuery(event.target).parents("#menuContent").length>0)) {
			hideMenu();
		}
	} 
	window.onload = function(){
		var callFn = function(rs){
			var deptLevel = rs['body'].Dept_Level;
			var state = rs['body'].Dept_State; 	
			
			/** 填充状态 */
			$.each(deptLevel,function(){
				$("<option/>").val(this.id)
				  .text(this.name)
				  .appendTo("#deptlevel");
			});
			$.each(state,function(){
				$("<option/>").val(this.id)
				  .text(this.name)
				  .appendTo("#state");
			});
			$("#state").val(1);
		}
		var form = {'dictNm':'Dept_Level,Dept_State'};
		sendAjax(form,'dictionary',1,callFn);
		
		//alert(${userInfo.ORG_ID});
		var form = {'name':'%%','org_id':"%"+${userInfo.ORG_ID}+"%",'org_id1':'${userInfo.ORG_ID}','option':'list'};
		sendAjax(form,'departmentList','doBiz',callFnOrg);
		
		
	}
</script>
</head>
<body class="iwapui center_body">
<div class="subnav_box">
	<ul class="breadcrumb">
		<li class="active">新增机构</li>
	</ul>
</div>
	<div class="center">
		<div class="w500mauto">
			<ul>
				<li id="deptid" name="deptid"></li>
				<li id="deptname" name="deptname"></li>
				<li id="deptfullname" name="deptfullname"></li>
				<li class="clearfix">
					<div class="selectbox inputbox">
						<label class="select_label">机构等级&nbsp;:&nbsp;</label> <select
							name="deptlevel" class="select_content" id="deptlevel"
							style="margin-left: 3px;"></select>
					</div>
				</li>
				<li>
					<div id="parentid"></div>
				</li>
				<li>
					<div id="parentName1"></div>
				</li>
				<li style="margin-left: 4px;">
					<div id="menuContent" class="menuContent">
						<ul id="tree" class="ztree"></ul>
					</div>
				</li>
				<li 　class="selectbox">
					<div class="selectbox">
						<label class="select_label">机构状态&nbsp;:&nbsp;</label> <select
							name="state" class="select_content" id="state"></select>
					</div>
				</li>
				<li id="parent_li">
					<div class="inputbox pr" id="address"></div>
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