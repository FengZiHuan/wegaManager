<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>机构-查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 一般查询页面所引入的样式/js文件 -->
<link href="${ctx}/css/font-awesomecss/font-awesome.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/font-awesomecss/font-awesome-ie7.min.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/jquery.dataTables.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css">
<script src="${ctx}/js/jquery.min.js"></script>
<!-- dataTables -->
<script src="${ctx}/js/jquery.dataTables.js"></script>
<script src="${ctx}/js/UtilTool.js"></script>
<script src="${ctx}/js/Grid.js"></script>
<link href="${ctx}/css/style.css" rel="stylesheet" type="text/css">
<!-- 文本框 -->
<link href="${ctx}/css/css.css" rel="stylesheet">
<link href="${ctx}/css/iwapui-style.css" rel="stylesheet">
<script src="${ctx}/js/TextField.js"></script>
<!-- 对话框 -->
<script src="${ctx}/js/bootstrap.min.js"></script>
<script src="${ctx}/js/ButtonField.js"></script>
<script src="${ctx}/js/public.js"></script>
<script src="${ctx}/js/jquery.cookie.js"></script>
<style type="text/css">
.deptlist_Table table{width: 100%;}
body{width: 100%;}
body.center_body{padding-top: 15px; margin-bottom: 130px;}
</style>
<script type="text/javascript">
	var grid = null;
	var departmentid = null;
	var deptname = null;
	var deptHelp = null;
    var _table = null;
    var DEPT_ID=null;
    var ORG_NM=null;

    
 
	$(document).ready(function() {
	
	    getDictionary();
	
		getDeptValue=function(deptId,deptname){
				DEPT_ID=deptId;
				ORG_NM=deptname;
				//alert(DEPT_ID);
				document.getElementById("deptId_input").value=deptId;
				document.getElementById("deptName_input").value=deptname;
		}
		returnDept=function(){
			//parent.deptdialog1.hidden();
			//alert($('#default', window.parent.document).html()); 
			var deptIdinput = document.getElementById("deptId_input").value;
			var deptname = document.getElementById("deptName_input").value;
			if(deptIdinput==""){
			alert("请选择机构");
			return;
			}
			//$('#jgbh', parent.document).value=deptId_input;
			window.parent.document.getElementsByName("ORG_ID")[0].value=deptIdinput;
			window.parent.document.getElementsByName("ORG_NM")[0].value=deptname;
			var win=$(window.parent.document);
			var myModa2=win.find("#myModa2");
			
			//确定所属机构后，清空查询条件后并查询《选择所属机构》对话框
			setTimeout(function(){
					$("input:not([type=hidden])").val("");
					$("#deptLevel").val("");
					doQuery();
			});
			myModa2.find(".close").click();
			
		}

		/* 普通文本框 */
		departmentid = $.IWAP.TextField({
		    width:'100px',
			label:'机构编号',
			renderTo:'departmentid'
		});
		
		deptname = $.IWAP.TextField({
		    width:'100px',
			label:'机构名称',
			renderTo:'deptname'
		});
		
		
		/*查询表格初始化*/
		grid = $.IWAP.Grid({
			'txcode':'deptList',
			'param':{'departmentid':departmentid.getValue(),
				'deptname':deptname.getValue(),'deptLevel':$('#deptLevel').val(),'_deptId':$('#_deptId').val(),
				'actionId':'doBiz'},
			'beforeRequest':function(){//请求前的回调函数
				
			},
			'beforeSuccess':function(){//请求成功前的回调函数
				
			},
			'sUrl':'iwap.ctrl',
			//'width':'100%',
			'bAutoWidth': false,
			'bStateSave':true,
			lazyLoad:false,
			ordering:false,
			renderTo:'deptlist_Table',
			"aoColumns":[
			   {"mData":"ORG_ID",title:'机构号'},
			   {"mData":"ORG_NM",title:'机构名称',defaultContent:""},
	  		    {"mData":"ORG_LVL",title:'机构级别',
				"mRender":function(data,type,full){
					return doDict("Dept_Level",data);
				}   
			   },
	  		   {"mData":"OPER",title:'选择',
				"mRender":function(data,type,full){
					return ['<input type="radio"  name="radid" onClick="getDeptValue(\''+full.ORG_ID+'\',\''+full.ORG_NM+'\')">'].join('');}   
			   }
			]
		});
		
		
		
		/** 清空数据 */
		$("#btn_clear").click(function(){
			$("input:not([type=hidden])").val("");
			$("#deptLevel").val("");

		});
		
		//获取table对象
		_table = grid.getGrid();

		// 初始化角色,状态（采用数据字典）
		//initSelectKV('{"deptLevel":"Dept_Level"}');
	});
	
	function setString(str, len) {  
	    var strlen = 0;  
	    var s = "";  
	    for (var i = 0; i < str.length; i++) {  
	        if (str.charCodeAt(i) > 128) {  
	            strlen += 2;  
	        } else {  
	            strlen++;  
	        }  
	        s += str.charAt(i);  
	        if (strlen >= len) {  
	            return s+"...";  
	        }  
	    }  
	    return s;  
	}
	
	/** 查询操作员 */
	function doQuery(){
		var api = new jQuery.fn.dataTable.Api(_table.fnSettings());
		 var params = {'departmentid':departmentid.getValue(),
				'deptname':deptname.getValue(),'deptLevel':$('#deptLevel').val(),'_deptId':$('#_deptId').val(),
				'actionId':'doBiz'}
		 _table.fnSettings().ajax.data=params;
        api.ajax.reload();  
	}

	
	
	/*
	var oper_id = null;
	function deleteLog(){
		//获取所选中的行数据
		var obj = grid.getSelected();
		var len = obj.length;
		if(len == 0){
			alert("请选择要删除的日志信息!");
			return;
		}
		if(!confirm("确定要删除吗?请确定!"))return;
		
		for(var i=0; i < len; i++){
			var callFn = function(rs){
				window.location="${ctx}/iwap.ctrl?txcode=loginfo";
			}
			var field = {'id':obj[i].id};
			sendAjax(field,'deleteLoginfo',1,callFn);
		}
	}*/

    function getDictionary(){
		var callFn = function(rs){
			var levelObj = rs['body']['Dept_Level'];
			dictArray=rs['body'];
			/** 填充机构级别 */
			$.each(levelObj,function(){
				$("<option/>").val(this.id)
				  .text(this.name)
				  .appendTo("#deptLevel");
			});
		}
		var filed = {'dictNm':'Dept_Level'};
		sendAjax(filed,'dictionary',1,callFn);
	}
</script>
</head>
<body class="iwapui center_body">
<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
<div class="">
	<ul class="linelist">
		<li class="clearfix">

	  			<div width="50px" class="inputbox pr" id="departmentid"></div>

	  			<div class="inputbox pr" id="deptname"></div>

		  		<div class="selectbox inlineb">
		  			<label class="select_label">　级别：</label>
		  			<select name="deptLevel" class="select_content" id="deptLevel" style="width:100px;margin-left: 3px;">
		  				<option value="">--全部--</option>
		  			</select>
	      		</div>

		</li>
	</ul>
</div>
<div class="tc">
	<a href="javaScript:void(0)" class="btn btn-primary mr30" onclick="doQuery()">查询</a>
	<a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_clear">清空</a>
</div>
<div>
		<div class="fr mb5"><a id="s_ture" href="javaScript:void(0)" class="btn btn-success w80" onclick="returnDept()">确定</a></div>
		<input id="deptId_input" type="hidden" style="display:none;">
		<input id="deptName_input" type="hidden" style="display:none;">
</div> 
<div class="table_box">
	<div id="deptlist_Table"></div>
</div>
</body>
</html>
