<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/screen/comm/header.jsp" %>
    <!-- 一般查询页面所引入的样式文件 -->
    <link rel="stylesheet" href="${ctx}/css/jquery.bigautocomplete.css" type="text/css"/>
    <link href="${ctx}/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/css/css.css" rel="stylesheet">
    <link href="${ctx}/css/style.css" rel="stylesheet">
    <link href="${ctx}/css/iwapui-style.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/css/zTreeStyle.css" rel="stylesheet">
    <!-- JQ必须在最JS上面 -->
    <script type="text/javascript" src="${ctx}/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.bigautocomplete.js"></script>
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
    <!-- 文件上传 -->
    <script type="text/javascript" src="${ctx}/js/FileUpload.js"></script>
    <style type="text/css" mce_bogus="1">

        .span {
            vertical-align: top;
            height: 32px;
            line-height: 32px;
            width: 200px;
            display: inline-block;
            overflow: hidden;
            margin-right: 5px;
        }

        table th {
            white-space: nowrap;
        }

        table td {
            white-space: nowrap;
        }

        body, table {
            font-size: 12px;
        }

        table {
            empty-cells: show;
            border-collapse: collapse;
            margin: 0 auto;
        }

        h1, h2, h3 {
            font-size: 12px;
            margin: 0;
            padding: 0;
        }

        table.tab_css_1 {
            border: 1px solid #cad9ea;
            color: #666;
        }

        table.tab_css_1 th {
            background-repeat: repeat-x;
            height: 40px;
        }

        table.tab_css_1 td, table.tab_css_1 th {
            border: 1px solid #cad9ea;
            padding: 0 1em 0;
            height: 40px;
        }

        table.tab_css_1 tr.tr_css {
            background-color: #f5fafe;
            height: 40px;
        }


    </style>
    <title>用户管理</title>
</head>
<body class="iwapui center_body">

<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
<div id="divDialog" class="divDialog">
    <div class="bg"></div>
    <div class="dialog" id="myModal" style="height:600px">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">信息</h4>
        </div>
        <div class="modal-body">
            <form method="post" onsubmit="return false" id="dialogarea">

                <!-- form开始 -->

                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false">
                        <span class="span" id="test"></span>
                        <select style="height: 28px" id="_select" onchange="setRuleContent();"> 
                            <option value="1">总行</option>
                            <option value="1">省行</option>
                            <option value="1">二级分行</option>
                            <option value="1">外省分行</option>
                        </select>
                        <input name="P_UNIT_LEVEL" style="width: 223px" type="text"
                               data-iwap-xtype="TextField" id="P_UNIT_LEVEL" class="input_text"
                                 value="" onchange="setRuleContent1();"  placeholder="输入"
                        >
                    </div>
                </div>
                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>姓名:</span> <input type="text" id="tt" data-iwap-xtype="TextField" name="P_NAME"
                                                class="input_text"

                                                autocomplete="off"/>
                    </div>
                </div>
                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false">
                        <span class="span">	现单位层级:</span>
                        <select style="height: 28px" id="_select1" onchange="setValue();"> 
                            <option value="1">总行</option>
                            <option value="1">省行</option>
                            <option value="1">二级分行</option>
                            <option value="1">外省分行</option>
                        </select>
                        <input name="P_NOW_UNIT_LEVEL" style="width: 223px" type="text"
                               data-iwap-xtype="TextField" id="P_NOW_UNIT_LEVEL" class="input_text"
                                 value="" onchange="setValue1();"  placeholder="输入"
                        >
                    </div>
                </div>
                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>现单位名称:</span> <input name="P_DEPT" type="text"
                                                   data-iwap-xtype="TextField" id="P_DEPT" class="input_text"
                                                   style="width: 250" value=""></div>
                </div>
                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>现科室单元:</span> <input name="P_SECTION_UNIT" type="text"
                                                   data-iwap-xtype="TextField" id="P_SECTION_UNIT" class="input_text"
                                                   style="width: 250" value=""></div>
                </div>
                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>现岗位名称:</span> <input name="P_POST" type="text"
                                                   data-iwap-xtype="TextField" id="P_POST" class="input_text"
                                                   style="width: 250" value=""></div>
                </div>
                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>现执行等级:</span> <input name="P_EXECUTION_LEVEL" type="text"
                                                   data-iwap-xtype="TextField" id="P_EXECUTION_LEVEL" class="input_text"
                                                   style="width: 250" value=""></div>
                </div>
                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>现执行层级:</span> <input name="P_EXECUTION_RANK" type="text"
                                                   data-iwap-xtype="TextField" id="P_EXECUTION_RANK" class="input_text"
                                                   style="width: 250" value=""></div>
                </div>

                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>变动日期:</span> <input data-iwap-xtype="TextField" id="G_ZTIMES" name="P_CHANGE_TIMES"
                                                  class="Wdate"
                                                  onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"/>
                    </div>

                </div>
                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>发单日期:</span> <input data-iwap-xtype="TextField" id="sendTimes" name="P_SEND_TIMES"
                                                  class="Wdate"
                                                  onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"/>
                    </div>

                </div>

                <div class="col-md-12 col-xs-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>备注:</span> <input name="P_REMARK" type="text"
                                                data-iwap-xtype="TextField" id="P_REMARK" class="input_text"
                                                style="width: 250" value=""></div>
                </div>


            </form>
            <!-- form END -->
            <div style="padding-left:200px;padding-top: 50px;">
                <div id="" class="buttonbox">
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
                <div id="" class="buttonbox" style="">
                    <button data-iwap-xtype="ButtonField" id="resetDel"
                            class="btn false mr30">清空
                    </button>
                </div>
            </div>
        </div>
    </div>
    <!-- 第二个对话框（对话框中打开对话框） -->


    <!-- 对话框END -->
    <!-- 页面查询区域开始 -->

    <form id="Conditions" class="clearfix">
        <div class="col-md-6 fl">
            <div class="inputbox tl pr" data-iwap="tooltipdiv"
                 data-iwap-empty="true">
                <span>姓名:</span><input name="userName" type="text"
                                       data-iwap-xtype="TextField" id="userName" class="input_text"
                                       value="">
            </div>
        </div>
        <div class="col-md-6 fl">
            <div class="inputbox tl pr" data-iwap="tooltipdiv"
                 data-iwap-empty="true">
                <span>部门:</span><input name="deptName" type="text"
                                       data-iwap-xtype="TextField" id="deptName" class="input_text"
                                       value="">
            </div>
        </div>
        <div class="col-md-6">
            <div class="inputbox pr" data-iwap="tooltipdiv"
                 data-iwap-empty="false" data-iwap-tooltext="年份份不能为空">
                <span>变动日期:</span> <input data-iwap-xtype="TextField" id="year" name="changeTimes" class="Wdate"
                                          onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"/>
            </div>
        </div>
        <div class="col-md-6">
            <div class="inputbox pr" data-iwap="tooltipdiv"
                 data-iwap-empty="false" data-iwap-tooltext="月份不能为空">
                <span>发单日期:</span> <input data-iwap-xtype="TextField" id="yue" name="sendTimes" class="Wdate"
                                          onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"/>
            </div>
        </div>

    </form>
    <div class="tc mb14">
        <a href="javaScript:void(0)" class="btn btn-primary mr30" id="query" onclick="iwapGrid.doQuery()">查询</a>
        <a href="javaScript:void(0)" class="btn btn-primary mr30" id="btn_clear" onclick="iwapGrid.doReset();">清空</a>
    </div>
</div>

<!-- 页面查询区域　END -->
<!-- 查询内容区域　开始 -->


<div style="overflow-x:auto;overflow-y:hidden;">
    <!-- 表格工具栏　开始 -->
    <div class="table_nav" style="display:inline">
        <a id="selectmultidel" class="" onclick="del()"
           href="javaScript:void(0)">删除</a> <a href="javaScript:void(0)"
                                                data-iwap-dialog="myModal" id="add" onclick="add()">新增</a>
        <a href="javaScript:void(0)" id="export" onclick="exportData()">模板导出</a>
    </div>
    <div style="display:inline">
        <a href="javaScript:void(0)" id="uplaod">
            <div class="upfilebox" id="ctx_import_file">
                <div class="upfile_layout">
                    <span>导入维护</span></div>
                <input type="file" class="upfile" enctype="multipart/form-data" id="import_file"></div>
        </a>
    </div>

    <!-- 表格工具栏　END -->
    <table id="iwapGrid"
           class="mygrid table table-bordered table-striped table-hover"
           data-iwap="grid" data-iwap-id="" data-iwap-param=""
           data-iwap-pagination="true">

        <tr id="dataName">
            <th data-grid-name="P_ID" primary="primary" data-order="">
                <s><input type="checkbox" selectmulti="selectmulti" value="{{value}}"></s></th>
            <th data-grid-name="P_UNIT_LEVEL" class="tab_css_1">原单位层级</th>
            <th data-grid-name="P_NAME" class="tab_css_1">姓名</th>
            <th data-grid-name="W_IDCARD" class="tab_css_1">身份证号</th>
            <th data-grid-name="W_SEX" class="tab_css_1">性别</th>
            <th data-grid-name="W_DEPT" class="tab_css_1">原单位名称</th>
            <th data-grid-name="SECTIONT_UNIT" class="tab_css_1">原内设科室单元</th>
            <th data-grid-name="W_POST" class="tab_css_1">原岗位名称</th>
            <th data-grid-name="POST_GRADE" class="tab_css_1">原岗位等级</th>
            <th data-grid-name="POST_RANK" class="tab_css_1">原岗位层级</th>
            <th data-grid-name="W_EXECUTIVE_POST_LEVEL" class="tab_css_1">原执行等级</th>
            <th data-grid-name="W_EXECUTIVE_POST_RANK" class="tab_css_1">原执行层次</th>
            <th data-grid-name="P_NOW_UNIT_LEVEL" class="tab_css_1">现单位层次</th>
            <th data-grid-name="P_DEPT" class="tab_css_1">现单位名称</th>
            <th data-grid-name="P_SECTION_UNIT" class="tab_css_1">现内设科室单元</th>
            <th data-grid-name="P_POST" class="tab_css_1">现岗位名称</th>
            <th data-grid-name="P_POST_GRADE" class="tab_css_1">现岗位等级</th>
            <th data-grid-name="P_POST_RANK" class="tab_css_1">现岗位层级</th>
            <th data-grid-name="P_EXECUTION_LEVEL" class="tab_css_1">现执行等级</th>
            <th data-grid-name="P_EXECUTION_RANK" class="tab_css_1">现执行层次</th>
            <th data-grid-name="P_CHANGE_TIMES" class="tab_css_1">变动日期</th>
            <th data-grid-name="P_SEND_TIMES" class="tab_css_1">发单日期</th>
            <th data-grid-name="P_NOTICE_NUMBER" class="tab_css_1">通知书文号</th>
            <th data-grid-name="P_REMARK" class="tab_css_1">备注</th>


            <th data-grid-name="P_ID" option="option" option-html=''>具体操作<s>
                <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id="editOne">修改</a>&nbsp;|&nbsp;
                <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id="editOn">上传附件</a>&nbsp;|&nbsp;
                <a href="javaScript:void(0)" class="editId" onclick="delOne(this)">删除</a>
            </s></th>
        </tr>
    </table>
</div>
</div>

<!-- 查询内容区域　END -->
</body>
<script type="text/javascript">
    document.getElementById("test").innerHTML = "原单位层级";
    var idDatas=null;
    var actionType = "", iwapGrid = null, condionForm = null, operForm = null, roleForm = null, grantTree = null,
        grantTreeData = null;
    $(document).ready(function () {

        var param = {'option': "", 'txcode': "changeDataMg", 'actionId': "doBiz", 'start': '0', 'limit': '10',};

        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            if (rs['header']['msg']) {
                return alert("删除失败:" + rs['header']['msg']);
            } else {
                debugger
                var mapList = rs.body.datas;
                var myArr = new Array()
                var i = 0;
                //填充框数据
                $.each(mapList, function (i, data) {

                    var dataValue = "";


                    var ke = data["W_SECTION_UNIT"];
                    var name = data["W_NAME"];
                    var dept = data["W_DEPT"];
                    var post = data["W_POST"];
                    value = name + "-" + dept + "-" + ke + "-" + post;//key所对应的value
                    dataValue = dataValue + value;
                    myArr[i] = {name: dataValue};
                    i++;
                });


                iwapGrid.doQuery(condionForm.getData());

                //核心
                $("#tt").bigAutocomplete({
                    data: myArr
                });
            }
        }, function () {
            alert("失败!");
        });

        initDict && initDict("Role", function () {
            //重置按钮事件
            operForm = $.IWAP.Form({'id': 'dialogarea'});
            roleForm = $.IWAP.Form({'id': 'dialogarea2'});
            condionForm = $.IWAP.Form({'id': 'Conditions'});

            grantTreeData = _dictJson['Role'];
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
            /*查询表格初始化  设置默认查询条件	var fData={'actionId':'doBiz','start':'0','limit':'10','txcode':'roleMg'};*/
            var fData = {

                'actionId': 'doBiz', 'start': '0', 'limit': '10', 'txcode': '' +
                    'changeDataMg'
            };
            iwapGrid = $.IWAP.iwapGrid({
                mode: 'server',
                fData: fData,
                Url: 'iwap.ctrl',
                grid: 'grid',
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
        $('#myModal').dialog('新增变动信息');
        actionType = "add";
        idDatas = "";
        operForm.reset();

        //  $('select#acct_status').val('1');
    };

    //对话框
    function dialogModal(id) {
        $('#' + id).dialog();
    };

    //保存
    function doSave() {
        var extParam = {'id': idDatas, 'option': actionType, 'txcode': 'changeDataMg', 'actionId': 'doBiz'};
        var param = operForm.getData();


        /*if($('input#org_nm').val()==''){
            alert("所属机构输入框不能为空");
            return ;
        }*/
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
            'txcode': "holidayMg",
            'holidayids': iwapGrid.getCheckValues(),
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

    //删除（单个）
    function delOne(obj) {
        if (!confirm("确定要删除吗?请确定!")) {
            return;
        }
        var param = {
            'option': "remove",
            'txcode': "holidayMg",
            'holidayids': iwapGrid.getCurrentRow()['NH_CARDID'],
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
        idDatas = iwapGrid.getCurrentRow()['P_ID'],
            $('#myModal').dialog("修改");
        actionType = "save";
        operForm.reset();
        operForm.setData(iwapGrid.getCurrentRow());
        operForm.disabledById("acct_id,org_id,org_nm");
        $('select#acct_status').val(iwapGrid.getCurrentRow()['acct_status']);
        $('#show_org').hide();
        if (iwapGrid.getCurrentRow()['acct_id'] != $('#_userid').val()) {
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
                grantTree.setCheck(grants[i]['role_id'], true);
            }
            $('#myModal3').dialog();
        }
        var field = {'option': 'query_grant', 'acct_id': iwapGrid.getCurrentRow()['acct_id']};
        sendAjax(field, 'userMg', 'doBiz', callFn);
    }

    //授权保存
    function doSaveRole() {
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
            'acct_role_list': grantTree.getValue(),
            'acct_id': iwapGrid.getCurrentRow()['acct_id']
        };
        sendAjax(field, 'userMg', 'doBiz', callFn);
    }

    function daochu() {

        var param = {
            'option': "daochu",
            'txcode': "tingMg",
            'holidayids': iwapGrid.getCheckValues(),
            'actionId': "doBiz"
        };
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            if (rs['header']['msg']) {
                return alert("导出失败:" + rs['header']['msg']);
            } else {
                alert("导出成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        }, function () {
            alert("导出失败!");
        });

    }


    function exportData() {

        var data = {
            'exportFlag': '1',
            'filetype': 'xlsx',
            'option': "daochu",
            'txcode': 'tingMg',
            'holidayids': iwapGrid.getCheckValues(),
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

    function exportData1() {
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

    /* 文件上传 */
    fileUpload = $.IWAP.FileUpload({
        'id': 'import_file',
        'url': 'iwap.ctrl',
        'fileType': ['xls', 'xlsx'],
        'label': '导入',
        'afterUpload': function () {
            var field = {'imp_id': '000005', 'files': fileUpload.getFileName(), 'isOverWrite': 'true'};
            var data = JSON.stringify({
                'body': field,
                'header': {'txcode': "importData", 'option': "daoru", 'actionId': "doBiz"}
            });
            $.ajax({
                timeout: 0,
                data: data,
                cache: false,
                contentType: 'application/json',
                dataType: 'json',
                type: "POST",
                url: 'iwap.ctrl',
                async: false,
                error: function (request) {
                    alert("Connection error");
                },
                success: function (data) {
                    if (data['header']['msg']) {
                        return alert("");
                    } else if (data['body']['info']) {
                        return alert("" + data['body']['info']);
                    } else {
                        return alert("Inport error");
                    }
                }

            });
        },//文件上传之后的触发事件，不管上传是否成功都会执行
        'beforeUpload': function () {
            return true
        },
        'size': 1024000,  //允许上传的单个文件大小，是以byte为单位
        'success': function (res) {
            //alert("文件上传成功");
            //var json = JSON.parse(res.target.response);
            //alert(json.msg);
            //console.log(json.uploadfilelist);

        },//上传成功后的回调函数
        'failed': function () {
            alert('上传失败');
        },//上传失败后的回调函数
        'renderTo': 'upfile'
    });

    $("#upfile").change(function () {

        fileUpload.upload();
    });


    function chaxun() {
        var extParam = {'option': "chaxun", 'txcode': 'jihuaMg', 'actionId': 'doBiz'};
        var param = operForm.getData();


        /*if($('input#org_nm').val()==''){
            alert("所属机构输入框不能为空");
            return ;
        }*/
        $.IWAP.applyIf(param, extParam);
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            $('#myModal').find('.close').click();
            if (rs['header']['msg']) {
                return alert("查询失败:" + rs['header']['msg']);
            } else {
                alert("查询成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        }, function () {
            alert("查询失败!");
        });
    }

    function setRuleContent() {
        var selectValue = $('#_select option:selected').text();//选中select的内容
        //alert("selectValue" + selectValue);
        var inputValue = $("#P_UNIT_LEVEL").val(selectValue);//input获得select的内容并显示在输入框中
        //alert(inputValue);
    };

    function setRuleContent1() {
        var selectValue = $('#_select1 option:selected').text();//选中select的内容
        //alert("selectValue" + selectValue);
        var inputValue = $("#P_UNIT_LEVEL").val(selectValue);//input获得select的内容并显示在输入框中
        //alert(inputValue);
    };

    function setValue() {
        var selectValue = $('#_select1 option:selected').text();//选中select的内容
        //alert("selectValue" + selectValue);
        var inputValue = $("#P_NOW_UNIT_LEVEL").val(selectValue);//input获得select的内容并显示在输入框中
        //alert(inputValue);
    };

    function setValue1() {
        var selectValue = $('#_select1 option:selected').text();//选中select的内容
        //alert("selectValue" + selectValue);
        var inputValue = $("#P_NOW_UNIT_LEVEL").val(selectValue);//input获得select的内容并显示在输入框中
        //alert(inputValue);
    };

</script>
</html>