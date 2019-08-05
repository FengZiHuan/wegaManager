<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户-查询</title>
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
	var live_city = null;
	var work_city = null;
    var _table = null;


    
 
	$(document).ready(function() {
		getUserValue=function(LIVE_CITY,WORK_CITY,DISTANCE,SUBSIDY_AMOUNT){
				document.getElementById("live_city_input").value=LIVE_CITY;
				document.getElementById("work_city_input").value=WORK_CITY;
				document.getElementById("distance_input").value=DISTANCE;
				document.getElementById("subsidyamount_input").value=SUBSIDY_AMOUNT;
			
		}
		returnDept=function(){
			debugger;
			var live_city_input = document.getElementById("live_city_input").value;
			var work_city_input = document.getElementById("work_city_input").value;
			var distance_input = document.getElementById("distance_input").value;
			var subsidyamount_input = document.getElementById("subsidyamount_input").value;
			if(live_city_input==""){
			alert("请选择行程");
			return;
			}
			window.parent.document.getElementsByName("LIVE_CITY")[0].value=live_city_input;
			window.parent.document.getElementsByName("WORK_CITY")[0].value=work_city_input;
			window.parent.document.getElementsByName("DISTANCE")[0].value=distance_input;
			window.parent.document.getElementsByName("SUBSIDY_AMOUNT")[0].value=subsidyamount_input;
			var win=$(window.parent.document);
			var myModa2=win.find("#myModa3");
			
			//确定所属用户后，清空查询条件后并查询《选择所属用户》对话框
			setTimeout(function(){
					$("input:not([type=hidden])").val("");
					doQuery();
			});
			myModa2.find(".close").click();
			
		}

		/* 普通文本框 */		
		live_city = $.IWAP.TextField({
		    width:'100px',
			label:'居住城市',
			renderTo:'live_city'
		});	

		work_city = $.IWAP.TextField({
		    width:'100px',
			label:'工作城市',
			renderTo:'work_city'
		});	
		/*查询表格初始化*/
		
		grid = $.IWAP.Grid({
			'txcode':'cityList',
			'param':{'live_city':live_city.getValue(),'work_city':work_city.getValue(),'actionId':'doBiz'},
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
			renderTo:'citylist_Table',
			"aoColumns":[
			   {"mData":"LIVE_CITY",title:'居住城市'},
			   {"mData":"WORK_CITY",title:'工作城市'},
	  		   {"mData":"DISTANCE",title:'路程距离'},
			   {"mData":"SUBSIDY_AMOUNT",title:'交流补贴标准'},
	  		   {"mData":"OPER",title:'选择',
				"mRender":function(data,type,full){
					return ['<input type="radio"  name="radid" onClick="getUserValue(\''+full.LIVE_CITY+'\',\''+full.WORK_CITY+'\',\''+full.DISTANCE+'\',\''+full.SUBSIDY_AMOUNT+'\')">'].join('');}   
			   }
			]
		});
		
		
		
		/** 清空数据 */
		$("#btn_clear").click(function(){
			$("input:not([type=hidden])").val("");
		});
		
		//获取table对象
		_table = grid.getGrid();
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
	
	/** 查询 */
	function doQuery(){
		var api = new jQuery.fn.dataTable.Api(_table.fnSettings());
		 var params = {'live_city':live_city.getValue(),'work_city':work_city.getValue(),'actionId':'doBiz'}
		 _table.fnSettings().ajax.data=params;
        api.ajax.reload();  
	}


</script>
</head>
<body class="iwapui center_body">
<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
<div class="">
	<ul class="linelist">
		<li class="clearfix">
	  			<div class="inputbox pr" id="live_city"></div>
	  			<div class="inputbox pr" id="work_city"></div>
		</li>
	</ul>
</div>
<div class="tc">
	<a href="javaScript:void(0)" class="btn btn-primary mr30" onclick="doQuery()">查询</a>
	<a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_clear">清空</a>
</div>
<div>
		<div class="fr mb5"><a id="s_ture" href="javaScript:void(0)" class="btn btn-success w80" onclick="returnDept()">确定</a></div>
		<input id="live_city_input" type="hidden" style="display:none;">
		<input id="work_city_input" type="hidden" style="display:none;">
		<input id="distance_input" type="hidden" style="display:none;">
		<input id="subsidyamount_input" type="hidden" style="display:none;">
</div> 
<div class="table_box">
	<div id="citylist_Table"></div>
</div>
</body>
</html>
