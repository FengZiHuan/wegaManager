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
    <script src="js/jquery.min.js"></script>
    <style type="text/css">

        /*样式写在此处是因为此页面需要BODY高度单独设为高度百分百*/
        body,html{height: 100%; overflow-y: hidden;}
    </style>

</head>
<body class="iwapui login pr">


<select>
    <option value="感冒" v="常见病">感冒</option>
    <option value="发烧" v="常见病">发烧</option>
    <option value="腹泻" v="常见病">腹泻</option>
    <option value="肺炎" v="传染病">手足口病</option>
    <option value="肺炎" v="传染病">水痘</option>
    <option value="肺炎" v="传染病">红眼病</option>
</select>
<input name="type" type="text" id="type" >
<input type="submit">
</body>
<script>
    $(function(){
        $("select").change(function(){
            $("#type").val($("select>option:selected").attr("v"));
        });
    });
</script>
</html>