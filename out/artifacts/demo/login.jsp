<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>广州南天智能Web开发平台</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="css/font-awesomecss/font-awesome.css" rel="stylesheet" type="text/css">
<link href="css/font-awesomecss/font-awesome-ie7.min.css" rel="stylesheet" type="text/css">
<link href="css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="css/iwapui-style.css" rel="stylesheet" type="text/css">
<link href="css/style.css" rel="stylesheet" type="text/css">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/UtilTool.js"></script>
<script src="js/TextField.js"></script>
<script src="js/ButtonField.js"></script>
<style type="text/css">
/*样式写在此处是因为此页面需要BODY高度单独设为高度百分百*/
 body,html{height: 100%; overflow-y: hidden;}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		var institutionsbox=null;
		var pswbox=null;
		institutionsbox=$.IWAP.TextField({
            label:'用户编号',
            allowBlank:false,
            validatorText:'请输入用户编号',
            minLength:5,
            name:'userId',
            value:'',
            renderTo:'institutionsbox'
        });
        pswbox=$.IWAP.TextField({
            label:'密　码',
            allowBlank:false,
            validatorText:'请输入登录密码',
            minLength:5,
            value:'',
            name:'password',
            type:'password',
            renderTo:'pswbox'
        });
        var buttonbox=null;
        buttonbox1=$.IWAP.ButtonField({
            label: '确　认',
            click:function (){if(institutionsbox.validate()&&pswbox.validate()){
            	$("#loginFrm").submit();
            }},
            renderTo:"buttonbox",
            disabled:false
        });
        buttonbox2=$.IWAP.ButtonField({
            label: '重置',
            click:function (){institutionsbox.setValue('');pswbox.setValue('');},
            renderTo:"buttonbox",
            disabled:false
        });
	});
</script>
</head>
<body class="iwapui login pr">
<!-- <div class="loginbody_bg"><img src="images/bg.png"></div> -->

<div class="center">
	<div class="logo login_logo"></div>
	<div class="opcity"></div>
	<div class="title_text">IWAP5.0开发平台</div>
	<div class="input_body">
		<form action="iwap.ctrl" method="post" id="loginFrm">
		 <input type="hidden" id="txcode" name="txcode" size="15" value="login">
         <input type="hidden" id="actionId" name="actionId" size="15" value="login">


		<ul>
			<li id="institutionsbox" class="institutions"></li>
			<li id="pswbox" class="pswbox"></li>
			<li id="msg" ><span style="color: red">${errorMsg}</span></li>
			
		</ul>
		</form>
	</div>
    <div id="buttonbox" class="marginauto clearfix buttonbody"></div>
</div>
<div class="copyright">版权所有：广州南天电脑系统有限公司</div>
</body>
</html>