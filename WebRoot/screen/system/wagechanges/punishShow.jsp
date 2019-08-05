<%@ page language="java" import="com.nantian.iwap.web.WebEnv"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>角色管理-查询</title>
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
<script src="${ctx}/js/Form.js"></script>
<script src="${ctx}/js/TextField.js"></script>
<script src="${ctx}/js/ListField.js"></script>
<script type="text/javascript" src="${ctx}/js/iwapGrid.js"></script>
<script type="text/javascript" src="${ctx}/js/iwapui.js"></script>
<script type='text/javascript' src="${ctx}/js/String.js"></script>
<script type='text/javascript' src="${ctx}/js/dictionary.js"></script>
<script type='text/javascript' src="${ctx}/js/public.js"></script>
<!--Tree.js使用需同时引用   1、zTreeStyle.css  2、jquery.ztree.all-3.5.js  3、jquery.ztree.exhide-3.5.js  -->
<script type="text/javascript" src="${ctx}/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.ztree.exhide-3.5.js"></script>
<script type="text/javascript" src="${ctx}/js/Tree.js"></script>
<!-- 引入日期控件 -->
<script type="text/javascript" src="${ctx}/js/My97DatePicker/calendar.js"></script>
<script type="text/javascript" src="${ctx}/js/My97DatePicker/WdatePicker.js"></script>

<style type="text/css">
.t_string{
	text-align: left
}
.title_text{width: 405px; height: 56px; position: absolute; z-index: 2; left: 50%; top: 10px; 
margin-left: -200px; font-size: 26px; font-weight: bold; text-align: center; text-shadow: 2px 0 2px #fff, 0 2px 2px #fff, 0 -2px 2px #fff, -2px 0 2px #fff;}

</style>
<script type="text/javascript">
	$(document).ready(function() {
debugger;
		var userId="${punishInfo.userId}"
		if(userId!=null && userId!=""){
			<c:forEach items="${punishInfo.ths}" var="th" varStatus="loopStatus">

			var thvla="${th}";
			var t=new Date(thvla);
			var tt=new Date();
			if(t<tt){
				$('#${th}-je').attr("readonly","readonly");
				$('#${th}-je').css({"background": "gray"});
				$('#${th}-jxgz').attr("disabled",true);
				$('#${th}-qynj').attr("disabled",true);
			}else{
				
				}
			
			</c:forEach>
		}

       
	});

	//保存
	function doSave(){
		var jesum=0;
		<c:forEach items="${punishInfo.ths}" var="th" varStatus="loopStatus">
			$('#${th}-jxgz').attr("disabled",false);
			$('#${th}-qynj').attr("disabled",false);
			jesum=jesum+parseFloat($('#${th}-je').val());
		</c:forEach>
		var nfje=$("#NFJXGZZE").val();
		if(jesum!=nfje){
			if(!confirm("拟停发绩效工资总额与填写金额总和不相等,确定要提交吗?请确定!"))return;
			$("#stopHairForm").submit();
		}else{
			$("#stopHairForm").submit();
			}
		
	}
	//返回
	function comeBack(){
		window.location="${ctx}/iwap.ctrl?txcode=punish";
	}
</script>
</head>
<body class="iwapui center_body">
	<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
	<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
	<input type="hidden" value="${punishInfo.xiaoqiang}" name="username"  id="username">
	<div class="title_text">处分录入</div>
<div style="overflow: auto; width: 100%;">
	<form action="iwap.ctrl" method="post" id="stopHairForm">
			<input type="hidden" id="txcode" name="txcode" size="15" value="punish">
        	<input type="hidden" id="actionId" name="actionId" size="15" value="doBiz">
        	<input type="hidden" id="actionType" name="actionType" size="15" value="insert">
    <table id="cform"
		class="mygrid table table-bordered table-striped table-hover">
        <thead>
           
            <tr>
                <th >部门</th>
				<th >姓名</th>
				<th >FMIS员工号</th>
				<th >停发要求</th>
				<th >拟停发绩效工资总额</th>    
				<th colspan="2">停发项目</th>      
                <c:forEach var="th" items="${punishInfo.ths}" varStatus="status"> <%--   循环动态列名 --%>         
                    <th>${th}</th>
                </c:forEach> 
            </tr>
        </thead>
        <tbody>
            <c:forEach var="result" items="${punishInfo.list}" varStatus="status">
                <tr>
                     <%--   机构列数据显示--%>    
					<c:if test="${status.index == 0}">  
  						<td style="vertical-align:middle;" rowspan="3">${result.DEPARTMENT}</td>
						<td style="vertical-align:middle;" rowspan="3">${result.USER_NAME}</td>
						<td style="vertical-align:middle;" rowspan="3">${result.FMIS_NO}</td>

						<td style="vertical-align:middle;" rowspan="3"><textarea rows="5" cols="30" name="JTYQ" id="JTYQ">${result.JTYQ}</textarea></td>					
						<td style="vertical-align:middle;" rowspan="3"><input type="text" id="NFJXGZZE" name="NFJXGZZE" value="${result.NFJXGZZE}"></td>
						<td style="vertical-align:middle;" rowspan="2">${result.tingfajixiao}</td>
					</c:if>	
					<c:choose>
       					<c:when test="${status.index == 2}">
							<td colspan="2">${result.tingfa}</td>
       					</c:when>
       					<c:otherwise>
                    		 <td>${result.tingfa}</td>
       					</c:otherwise>
					</c:choose>

                    <%--   动态列数据显示 --%>    
                    <c:forEach var="td" items="${punishInfo.tds}" varStatus="loopStatus">
						<c:if test="${status.index == 0}">  
  							<td><select id="${td}-jxgz" name="${td}-jxgz">
  								<option value="0">是</option>
  								<option value="1" <c:if test="${'1' eq result[td]}">selected</c:if>>否</option>
  							</td>
						</c:if>
                         
						<c:if test="${status.index == 1}">  
  							<td><input type="text" id="${td}-je" name="${td}-je" value="${result[td]}"></td>
						</c:if>
						
						<c:if test="${status.index == 2}">  
  							<td><select id="${td}-qynj" name="${td}-qynj">
  								<option value="0">是</option>
  								<option value="1" <c:if test="${'1' eq result[td]}">selected</c:if>>否</option>
  							</td>
						</c:if>
                    </c:forEach> 
                </tr>
            </c:forEach>
        </tbody>
    </table>
</form>

	<div class="tc mb14">
		<a href="javaScript:void(0)" class="btn btn-primary mr30" id="save" onclick="doSave()">保存</a>
	    <a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_clear" onclick="comeBack();">返回</a>
	</div>
 
</div>
</div>
<body>
</html>