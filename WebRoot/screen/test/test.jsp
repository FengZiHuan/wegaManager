<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2019/7/25
  Time: 10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>angularJS 测试</title>
    <script src="${ctx}/js/angular.min.js"></script>
    <script type="text/javascript">
        //创建 angular 模块
        //参数 1：模块的名称
        //参数 2：要加载的其它模块列表，如果没有也得加这个空数组，不加表示获取一个模块
        var app = angular.module("app", []);
        app.controller("myController", function ($scope, $http) {
               //查询所有列表数据并绑定到 list 对象
            $scope.findAll = function () {
                debugger
                $http.post("../demo_war_exploded/iwap.ctrl?txcode=testMg").success(function (response) {
                    $scope.list = response;
                    alert(response)



                });
            };
        });

    </script>
</head>
<body ng-app="app" ng-controller="myController" ng-init="findAll()">
<table>
    <thead>
    <tr id="testTable">
        <td id="op">id</td>
        <td>姓名</td>
        <td>性别</td>
    </tr>
    </thead>
    <tr ng-repeat="p in persons">
        <td>{{p.id}}</td>
        <td>{{p.name}}</td>
        <td>{{p.gender}}</td>
        <td>{{p.saa}}</td>

    </tr>
</table>
</body>
</html>
