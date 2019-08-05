<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/screen/comm/header.jsp" %>
    <link href="<%=path %>/css/style.css" rel="stylesheet">
    <link href="<%=path %>/css/zTreeStyle.css" rel="stylesheet">
    <script type="text/javascript" src="<%=path %>/js/iwapui.js"></script>
    <script type="text/javascript" src="<%=path %>/js/jquery.ztree.all-3.5.js"></script>
    <script type="text/javascript" src="<%=path %>/js/jquery.ztree.exhide-3.5.js"></script>
    <script type="text/javascript" src="<%=path %>/js/Tree.js"></script>
    <script type='text/javascript' src="<%=path %>/js/dictionary.js"></script>
    <script type='text/javascript' src="<%=path %>/js/public.js"></script>
    <!-- 引入日期控件 -->
    <script type="text/javascript" src="${ctx}/js/My97DatePicker/calendar.js"></script>
    <script type="text/javascript" src="${ctx}/js/My97DatePicker/WdatePicker.js"></script>
    <style type="text/css">
        .divDialog .dialog {
            width: 50%;
            max-width: 1200px;
            z-index: 10;
            position: absolute;
            top: 10%;
            left: 50%;
            background: #fff;
            border: 1px solid rgba(0, 0, 0, .2);
            border-radius: 6px;
            outline: 0;
            -webkit-box-shadow: 0 3px 9px rgba(0, 0, 0, .5);
            box-shadow: 0 3px 9px rgba(0, 0, 0, .5);
            display: none;
        }
    </style>
    <title>用户管理</title>
</head>
<body class="iwapui center_body">
<input type="hidden" value="${userInfo.ORG_ID}" id="_deptid">
<input type="hidden" value="${userInfo.ACCT_ID}" id="_userid">
<!-- 对话框开始 -->
<div id="divDialog" class="divDialog">
    <div class="bg"></div>
    <div class="dialog" id="myModal" style="width: 600px;height: 600px">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">增加柜员</h4>
        </div>
        <div class="modal-body">
            <form method="post" onsubmit="return false" id="dialogarea">
                <!-- form开始 -->
                <div class="col-md-12">
                    <div class="col-md-12 col-xs-12">
                        <div class="inputbox pr" data-iwap="tooltipdiv"
                             data-iwap-empty="false" data-iwap-tooltext="柜员名称不能为空">
                            <span>项目序号:</span> <input name="ID" type="text"
                                                      data-iwap-xtype="TextField" id="ID" class="input_text"
                                                      style="width: 250" value="">
                        </div>
                    </div>
                    <div class="col-md-12 col-xs-12">
                        <div class="inputbox pr" data-iwap="tooltipdiv"
                             data-iwap-empty="false" data-iwap-tooltext="柜员名称不能为空">
                            <span>项目名称:</span> <input name="NAME" type="text"
                                                      data-iwap-xtype="TextField" id="NAME" class="input_text"
                                                      style="width: 250" value="">
                        </div>
                    </div>
                    <div class="col-md-12 col-xs-12">
                        <div class="inputbox pr" data-iwap="tooltipdiv"
                             data-iwap-empty="false" data-iwap-tooltext="柜员名称不能为空">
                            <span>补贴标准:</span> <input name="STANDARD" type="text"
                                                      data-iwap-xtype="TextField" id="STANDARD" class="input_text"
                                                      style="width: 250" value="">
                        </div>
                    </div>
                    <div class="col-md-12 col-xs-12">
                        <div class="inputbox pr" data-iwap="tooltipdiv"
                             data-iwap-empty="false" data-iwap-tooltext="柜员名称不能为空">
                            <span>补贴单位:</span> <input name="UNIT" type="text"
                                                      data-iwap-xtype="TextField" id="UNIT" class="input_text"
                                                      style="width: 250" value="">
                        </div>
                    </div>

                    <div class="col-md-12 col-xs-12">
                        <div class="inputbox pr" data-iwap="tooltipdiv"
                             data-iwap-empty="true">
                            <span>申请日期:</span> <input data-iwap-xtype="TextField" id="TIMES" name="TIMES" class="Wdate"
                                                      onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM'})"/>
                        </div>
                    </div>


                </div>
            </form>
            <!-- form END -->
            <div style="padding-left: 210px">
                <div class="buttonbox">
                    <button data-iwap-xtype="ButtonField" id="save"
                            class="btn false mr30" data-dialog-hidden="true"
                            onclick="doSave()">保存
                    </button>
                </div>
                <div id="" class="buttonbox">
                    <button data-iwap-xtype="ButtonField" id="reset"
                            class="btn false mr30">清空
                    </button>
                </div>
                <div id="" class="buttonbox">
                    <button data-iwap-xtype="ButtonField" id="resetDel"
                            class="btn false mr30">清空
                    </button>
                </div>
            </div>
        </div>
    </div>
    <!-- 第二个对话框（对话框中打开对话框） -->
    <div class="bg"></div>
    <div class="dialog" id="myModa2" style="width: 830px;">
        <div class="dialog-header">
            <button type="button" class="close" id="btn_iwap-gen-10"
                    data-dialog-hidden="true">
                <span>×</span>
            </button>
            <h4 class="modal-title">选择所属机构</h4>
        </div>
        <div class="modal-body">
            <iframe style="height: 600px; width: 800px"
                    src="iwap.ctrl?txcode=deptList"></iframe>
        </div>
    </div>
    <!-- 第二个对话框 END-->
    <!-- 第三个对话框 -->
    <div class="bg"></div>
    <div class="dialog" id="myModal3" style="width: 600px;height: 1200px">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">请选择部门</h4>
        </div>
        <div class="modal-body">
            <div id="grant_tree"></div>
            <div style="padding-left: 210px">
                <div class="buttonbox">
                    <button data-iwap-xtype="ButtonField" id="save_role"
                            class="btn false mr30" data-dialog-hidden="true"
                            onclick="doSaveRole()">保存
                    </button>
                </div>
                <div id="" class="buttonbox">
                    <button data-iwap-xtype="ButtonField" id="reset_role"
                            class="btn false mr30">清空
                    </button>
                </div>
            </div>
        </div>
    </div>
    <!-- 第三个对话框 END-->
</div>
<!-- 对话框END -->
<!-- 页面查询区域开始 -->
<form id="Conditions" class="clearfix">

    <div class="col-md-6 fl">
        <div class="inputbox pr" data-iwap="tooltipdiv"
             data-iwap-empty="true">
            <span>项目名称:</span><input name="userName" type="text"
                                     data-iwap-xtype="TextField" id="userName" class="input_text"
                                     value="">
        </div>
    </div>
    <div class="col-md-6 fl">
        <div class="inputbox pr" data-iwap="tooltipdiv"
             data-iwap-empty="true">
            <span>补贴标准:</span><input name="stand" type="text"
                                     data-iwap-xtype="TextField" id="stand" class="input_text"
                                     value="">
        </div>
    </div>
    <div class="col-md-12 col-xs-12">
        <div class="inputbox pr" data-iwap="tooltipdiv"
             data-iwap-empty="true">
            <span>申请日期:</span> <input data-iwap-xtype="TextField" id="date" name="date" class="Wdate"
                                      onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM'})"/>
        </div>
    </div>
</form>
<div class="tc mb14">
    <a href="javaScript:void(0)" class="btn btn-primary mr30" id="query" onclick="iwapGrid.doQuery()">查询</a>
    <a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_clear" onclick="iwapGrid.doReset();">清空</a>
</div>
<!-- 页面查询区域　END -->
<!-- 查询内容区域　开始 -->
<div class="table_box">
    <!-- 表格工具栏　开始 -->
    <div class="table_nav">
        <a href="javaScript:void(0)"
           data-iwap-dialog="myModal" id="add" onclick="add()">新增</a>
        <a href="javaScript:void(0)" id="doSure" onclick="doSure()">确认项目</a>
    </div>
    <!-- 表格工具栏　END -->
    <table id="iwapGrid"
           class="mygrid table table-bordered table-striped table-hover"
           data-iwap="grid" data-iwap-id="" data-iwap-param=""
           data-iwap-pagination="true">
        <tr>
            <th data-grid-name="ID" primary="primary" data-order=""><input
                    type="checkbox" name="selectname" selectmulti="selectmulti"
                    value=""> <s><input type="checkbox"
                                        selectmulti="selectmulti" value="{{value}}"></s></th>

            <th data-grid-name="ID" class="tl">项目序号</th>
            <th data-grid-name="NAME" class="tl">补贴项目</th>
            <th data-grid-name="STANDARD" class="tl">补贴标准</th>
            <th data-grid-name="UNIT" class="tl">补贴单位（元或次）</th>
            <th data-grid-name="TIMES" class="tl">修改时间</th>
            <th data-grid-name="ID" option="option" option-html=''><span>请选择部门</span><s>
                <a href="javaScript:void(0)" class="editId" onclick="grant(this)" id="grant">请选择部门</a>
            </s></th>
            <th data-grid-name="ID" option="option" option-html=''><span>操作</span><s>
                <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id="editOne">修改</a>

            </s></th>
        </tr>
    </table>
</div>
<!-- 查询内容区域　END -->
</body>
<script type="text/javascript">
    var actionType = "", iwapGrid = null, condionForm = null, operForm = null, roleForm = null, grantTree = null,
        grantTreeData = null;
    $(document).ready(function () {
        initDict && initDict("foodDept", function () {
            //重置按钮事件
            operForm = $.IWAP.Form({'id': 'dialogarea'});
            roleForm = $.IWAP.Form({'id': 'dialogarea2'});
            condionForm = $.IWAP.Form({'id': 'Conditions'});
            grantTreeData = _dictJson['foodDept'];
            grantTree = $.IWAP.Tree({
                disabled: false,
                hidden: false,
                value: [],
                isMultiSelect: true,
                checked: true,
                data: grantTreeData,
                mode: 'local',
                renderTo: 'grant_tree'
            });

            $('#reset').click(function () {
                operForm.reset();
                $('#myModal select#acct_status').val('1');
            });
            $('#resetDel').click(function () {
                if ($('#myModal input#acct_id').val() == $('#_userid').val()) {
                    $('#myModal input').not('#acct_id,#org_id,#org_nm').val('');
                } else {
                    $('#myModal input').not('#acct_id').val('');
                }
                $('#myModal select#acct_status').val('1');
            });
            $('#reset_role').click(function () {
                roleForm.reset();
                grantTree.Load(grantTreeData, false);
            });
            /*查询表格初始化  设置默认查询条件*/
            var fData = {
                'userId': '%',
                'userNm': '%',
                'deptId': '%',
                'state': '%',
                '_deptid': '%' + $("#_deptid").val() + '%',
                'actionId': 'doBiz',
                'start': '0',
                'limit': '10',
                'txcode': 'updateFoodMg'
            };
            iwapGrid = $.IWAP.iwapGrid({
                mode: 'server',
                fData: fData,
                Url: 'iwap.ctrl',
                grid: 'grid',
                importFunc: function () {
                    alert(1)
                },
                form: condionForm,
                renderTo: 'iwapGrid'
            });
        });
        // 初始化角色,状态（采用数据字典）
        initSelectKV('{"userStatus":"ACCT_STATUS","state":"WorkState","acct_status":"ACCT_STATUS"}');

    });

    //增加
    function add() {
        //每次点击增加按钮后：角色和状态设成默认值
        document.getElementById("reset").style.display = "block";
        document.getElementById("resetDel").style.display = "none";
        $('#myModal').dialog('新增柜员');
        actionType = "add";
        operForm.reset();
        operForm.enabledById("acct_id");
        operForm.disabledById("org_id,org_nm");
        $('select#acct_status').val('1');
    };

    //对话框
    function dialogModal(id) {
        $('#' + id).dialog();
    };

    //保存
    function doSave() {
        var extParam = {'option': actionType, 'txcode': 'updateFoodMg', 'actionId': 'doBiz'};
        var param = operForm.getData();

        if ($('input#ID').val() == '') {
            alert("项目序号不能为空");
            return;
        }
        if ($('input#NAME').val() == '') {
            alert("项目名字不能为空");
            return;
        }
        if ($('input#DEPT').val() == '') {
            alert("部门不能为空");
            return;
        }
        if ($('input#STANDARD').val() == '') {
            alert("名字不能为空");
            return;
        }
        $.IWAP.applyIf(param, extParam);
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            $('#myModal').find('.close').click();
            if (rs['header']['msg']) {
                return alert("保存失败:" + rs['header']['msg']);
            } else {
                alert("保存成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        }, function () {
            alert("保存失败!");
        });
    }

    //删除（可多个）
    function del() {
        if (iwapGrid.getCheckValues() == "") {
            alert("请选择要删除的柜员!");
            return;
        }

        if (!confirm("确定要删除吗?请确定!"))
            return;

        var param = {
            'option': "remove",
            'txcode': "userMg",
            'userids': iwapGrid.getCheckValues(),
            '_userid': $('#_userid').val(),
            'actionId': "doBiz"
        };
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            if (rs['header']['msg']) {
                return alert("删除失败:" + rs['header']['msg']);
            } else {
                alert("删除成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        }, function () {
            alert("删除失败!");
        });
    };


    //确定项目

    function doSure() {
        if (iwapGrid.getCheckValues() == "") {
            alert("请选择本月要补贴的项目!");
            return;
        }

        if (!confirm("确定要选择?请确定!"))
            return;

        var param = {
            'option': "dosure",
            'txcode': "updateFoodMg",
            'ids': iwapGrid.getCheckValues(),
            'actionId': "doBiz"
        };
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            if (rs['header']['msg']) {
                return alert(rs['header']['msg']);
            } else {
                alert("确认成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        }, function () {
            alert("确认失败!");
        });
    };

    //删除（单个）
    function delOne(obj) {
        if (!confirm("确定要删除吗?请确定!")) {
            return;
        }
        var param = {
            'option': "remove",
            'txcode': "userMg",
            'userids': iwapGrid.getCurrentRow()['ACCT_ID'],
            '_userid': $('#_userid').val(),
            'actionId': "doBiz"
        };
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            if (rs['header']['msg']) {
                return alert("删除失败:" + rs['header']['msg']);
            } else {
                alert("删除成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        }, function () {
            alert("删除失败!");
        });
    }

    //编辑
    function edit(obj) {
        document.getElementById("reset").style.display = "none";
        document.getElementById("resetDel").style.display = "block";
        $('#myModal').dialog("修改柜员");
        actionType = "save";
        operForm.reset();
        console.log("ACCT_STATUS::" + iwapGrid.getCurrentRow()['ACCT_STATUS']);
        debugger;
        operForm.setData(iwapGrid.getCurrentRow());
        operForm.disabledById("acct_id,org_id,org_nm");
        $('select#acct_status').val(iwapGrid.getCurrentRow()['ACCT_STATUS']);
        $('#show_org').hide();
        if (iwapGrid.getCurrentRow()['ACCT_ID'] != $('#_userid').val()) {
            $('#show_org').show();
        }
    };

    //授权
    function grant(obj) {


        var callFn = function (rs) {
            if (rs['header']['msg']) {
                return alert("授权查询出错:" + rs['header']['msg']);
            }
            roleForm.reset();
            var grants = rs['body']['grants'];
            grantTree.Load(grantTreeData, false);
            for (var i in grants) {
                grantTree.setCheck(grants[i]['id'], true);
            }
            $('#myModal3').dialog();
        }
        var field = {'option': 'query_grant'};
        sendAjax(field, 'updateFoodMg', 'doBiz', callFn);
    }

    //授权保存
    function doSaveRole() {

        if (grantTree.getValue() == "") {
            if (!confirm("确定要选择全部部门吗?请确定!")) {
                return;
            }
        }
        if (!confirm("确定要选择吗?请确定!"))
            return;

        var callFn = function (rs) {
            $('#myModal3').find('.close').click();
            if (rs['header']['msg']) {
                alert("授权保存出错:" + rs['header']['msg']);
            } else {
                alert("授权保存成功");
            }
        }
        var field = {
            'option': 'save_grant',
            'deptList': grantTree.getValue(),
            'id': iwapGrid.getCurrentRow()['ID']
        };
        sendAjax(field, 'updateFoodMg', 'doBiz', callFn);
    }

    function exportData() {
        var data = {
            'exportFlag': '1',
            'filetype': 'xlsx',
            'txcode': 'userMg',
            'actionId': 'doBiz',
            'start': '0',
            'limit': '1000'
        };
        var form = condionForm.getData();
        $.IWAP.apply(data, form);

        var titleString = [];
        $("table#iwapGrid tbody tr:eq(0) th").each(function () {
            if ($(this).hasClass("tl")) {
                var titleMap = {};
                titleMap[$(this).attr("data-grid-name")] = $(this).html();
                titleString.push(titleMap);
            }
        });
        titleString = JSON.stringify(titleString);
        var reg = /\[/g;
        var reg2 = /\]/g;
        var reg3 = /\{/g;
        var reg4 = /\}/g;
        titleString = titleString.replace(reg, '%5B');
        titleString = titleString.replace(reg2, '%5D');
        titleString = titleString.replace(reg3, '%7B');
        titleString = titleString.replace(reg4, '%7D');
        data.titleString = titleString;

        var param = "";

        for (var key in data) {
            param += key + "=" + data[key] + "&";
        }
        param = param.substr(0, param.length - 1);
        var iframe = $('<iframe name="iwapdownload">');
        iframe.css("display", "none");
        iframe.attr("src", "download.ctrl?" + param);
        $('body').prepend(iframe);
    }
</script>
</html>