<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>机构管理-查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${ctx}/css/zTreeStyle.css">
<link href="${ctx}/css/font-awesomecss/font-awesome.css"
	rel="stylesheet" type="text/css">
<link href="${ctx}/css/font-awesomecss/font-awesome-ie7.min.css"
	rel="stylesheet" type="text/css">
<link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/style.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/css.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/iwapui-style.css" rel="stylesheet"
	type="text/css">
<!-- 公共 -->
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/UtilTool.js"></script>
<script src="${ctx}/js/bootstrap.min.js"></script>
<!-- 以下为组件JS -->
<script src="${ctx}/js/TextField.js"></script>
<script src="${ctx}/js/ButtonField.js"></script>
<script src="${ctx}/js/public.js"></script>
<!-- 对话框 -->
<script src="${ctx}/js/dialog.js"></script>
<!-- tree -->
<script type="text/javascript" src="${ctx}/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript"
	src="${ctx}/js/jquery.ztree.exhide-3.5.js"></script>
<script src="${ctx}/js/Tree.js"></script>
<!-- 文件上传 -->
<script type="text/javascript" src="${ctx}/js/FileUpload.js"></script>
<script type="text/javascript">
	var grid = null;
	var departmentid = null;
	var deptName = null;
	var deptHelp = null;
	var dept_tree = null;
	var IDMark_A = "_a";
	
	$(document).ready(function() {
		/* 普通文本框 */
		departmentid = $.IWAP.TextField({
			label:'机构编号',
			renderTo:'departmentid',
		});
		
		deptName = $.IWAP.TextField({
			label:'机构名称',
			renderTo:'deptName',
		});
		
		/** 清空数据 */
		$("#btn_clear").click(function(){
			$("input:not([type=hidden])").val("");
			$("#deptLevel")[0].selectedIndex = 0;
			$("#state")[0].selectedIndex = 0;
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
		
		$("#btn_query").click(function(){
			doQuery();
		});
		
		dataLoad('','','','');
		
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
	
	function doQuery(){
		dataLoad(departmentid.getValue(),deptName.getValue(),$('#deptLevel').val(),$('#state').val());
	}
	
	function onClick(event, treeId, treeNode){
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		zTree.expandNode(treeNode);
		//doEdit(treeNode.id);
		//dept_tree.Load(data,append);
	}
	
	
	function dataLoad(departmentid,deptName,deptLevel,state){
		var callFn = function(rs){
			//判断是否出错
			if(rs['header']['msg']){
				alert("查询无结果:"+rs['header']['msg']);
			}else{
				$('#tree').html("");
				var zNodes = rs['body']['zNodes'];
				zNodes = eval("("+zNodes+")");
				if(zNodes.length <=0){
					alert("权限内无对应数据！");
				}else{
					dept_tree = $.IWAP.Tree({
						onClick:onClick,
						//addDiyDom:addDiyDom,
						disabled:false,
						hidden:false,
						value:[],
						checked:false,
						data:zNodes,
						mode:'local',
						isOrg:true,
						btn2:true,
				        btn2Text:"修改",
				        btn2style:"b_blue",
				        btn3:true,
				        btn3Text:"删除",
				        btn3style:"b_red",
						renderTo:'tree'
			        });
			        dept_tree.hiddenRmenu();
			        $.IWAP.Tree.addbtn2= function(btnId,treeId, treeNode){
				         doEdit(treeNode.id);
				    }
				        
				    $.IWAP.Tree.addbtn3= function(btnId,treeId, treeNode){
				         doRemove(treeNode.id);
				    }
				}
			}
		}
		var form = {'departmentid':departmentid,'deptName':deptName,'deptLevel':deptLevel,'state':state,'_deptId':$('#_deptId').val()};
		sendAjax(form,'department','doBiz',callFn);
	}
	
	function doEdit(deptid){
		window.location="${ctx}/iwap.ctrl?txcode=showDepartment&deptid="+deptid;
	}
	
	function doRemove(deptid){
		if(!confirm("确定要删除吗?请确定!"))return;
		var callFn = function(rs){
			if(rs['header']['msg']){
				alert("机构显示出错:"+rs['header']['msg']);
			}else{
				alert("删除成功");
			}
		}
		var form = {'deptid':""+deptid,'option':'remove'};

		sendAjax(form,'department','doBiz',callFn);
		window.location="${ctx}/iwap.ctrl?txcode=department&deptid="+deptid;
	}
	
	window.onload = function(){
		var callFn = function(rs){
			var deptLevel = rs['body'].Dept_Level;
			var state = rs['body'].Dept_State; 	
			
			/** 填充状态 */
			$.each(deptLevel,function(){
				$("<option/>").val(this.id)
				  .text(this.name)
				  .appendTo("#deptLevel");
			});
			$.each(state,function(){
				$("<option/>").val(this.id)
				  .text(this.name)
				  .appendTo("#state");
			})
		}
		var form = {'dictNm':'Dept_Level,Dept_State'};
		sendAjax(form,'dictionary',1,callFn);
	}
</script>
</head>
<body class="iwapui center_body">
	<input type="hidden" id="_deptId" value="${userInfo.ORG_ID}" />
	<div class="">
		<ul class="linelist">
			<li class="clearfix">
				<table>
					<tr>
						<td>
							<div class="inputbox pr" id="departmentid"></div>
						</td>
						<td>
							<div class="inputbox pr" id="deptName"></div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="selectbox">
								<label class="select_label">级别:</label> <select name="deptLevel"
									class="select_content" id="deptLevel">
									<option value="">--全部--</option>
								</select>
							</div>
						</td>
						<td>
							<div class="selectbox">
								<label class="select_label">状态:</label> <select name="state"
									class="select_content" id="state">
									<option value="">--全部--</option>
								</select>
							</div>
						</td>
					<tr>
				</table>
			</li>
		</ul>
	</div>
	<div class="search_btn">
		<a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_query">查询</a>
		<a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_clear">清空</a>
		<a href="${ctx}/iwap.ctrl?txcode=addDepartment" class="btn btn-primary" id="btn_add">新增</a>
		<a href="javaScript:void(0)" id="uplaodfile"></a>
	</div>
	<hr>
	<div class="table_box">
		<br>
		<div id="tree"></div>
	</div>
</body>
</html>
