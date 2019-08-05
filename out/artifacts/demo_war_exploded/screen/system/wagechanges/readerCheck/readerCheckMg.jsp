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
    <!-- 文件上传 -->
    <script type="text/javascript" src="${ctx}/js/FileUpload.js"></script>
    <style type="text/css" mce_bogus="1">

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

        .iwapui input, .iwapui select, .iwapui table.border1, .iwapui textarea {
            border: 1px #ccc solid;
            border-radius: 4px;
            higth: 28px;
        }
    </style>
    <title>用户管理</title>
</head>
<body class="iwapui center_body">
<!-- 第四个对话框 -->

<!-- 第四个对话框 END-->

<!-- 对话框开始 -->
<div id="divDialog" class="divDialog">
    <div class="bg"></div>
    <div class="dialog" id="myModal" style="width: 600px;height: 500px">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">新增</h4>
        </div>
        <div class="modal-body">
            <form method="post" onsubmit="return false" id="dialogarea">
                <!-- form开始 -->
                <div class="col-md-12 col-xs-12">
                    <div class="col-md-12 col-xs-12">
                        <div class="inputbox pr" data-iwap="tooltipdiv"
                             data-iwap-empty="false" data-iwap-tooltext="姓名不为空">
                            <span>申请人:</span> <input name="NAME" type="text"
                                                     data-iwap-xtype="TextField" id="NAME" class="input_text"
                                                     style="width: 250" value="">
                        </div>
                    </div>
                    <div class="col-md-12 col-xs-12">
                        <div class="inputbox pr" data-iwap="tooltipdiv"
                             data-iwap-empty="true">
                            <span>申请日期:</span> <input data-iwap-xtype="TextField" id="TIMES" name="TIMES" class="Wdate"
                                                      onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"/>
                        </div>
                    </div>
                    <div class="col-md-12 col-xs-12">
                        <div class="inputbox pr" data-iwap="tooltipdiv"
                             data-iwap-empty="false" data-iwap-tooltext="类型不能为空">
                            <span>类型:</span>

                            <select id="_select" onchange="setRuleContent();" style="height: 29px;">
                                <option value="1">工资标准</option>
                                <option value="1">福利</option>
                                <option value="1">年金</option>
                                <option value="1">社保</option>
                                <option value="1">休假</option>
                                <option value="1">处分</option>
                            </select>
                            <input name="TYPE" type="text"
                                   data-iwap-xtype="TextField" id="TYPE" class="input_text"
                                   style="width: 220px"   value="" onchange="setRuleContent1();"  placeholder="输入"
                            >
                        </div>
                    </div>
                    <div class="col-md-12 col-xs-12">
                        <div class="inputbox pr" data-iwap="tooltipdiv"
                             data-iwap-empty="false" data-iwap-tooltext="不能为空">
                            <span>申请审批事项:</span> <textarea name="REMARK" type="textarea"
                                                           data-iwap-xtype="TextField" id="REMARK" class="input_text"
                                                           value="" style="height: 200px"></textarea>
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
    <div class="dialog" id="myModal2" style="width: 900px;height:600px">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">查看照片</h4>
        </div>
        <div class="modal-body" id="big_img" style="display:flex;align-items:center;justify-content:center;width:800px;height:550px;">

        </div>
    </div>
</div>
<!-- 第二个对话框 END-->
<!-- 第三个对话框 -->

<!-- 第三个对话框 END-->
</div>
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
        <div class="inputbox pr" data-iwap="tooltipdiv"
             data-iwap-empty="true" data-iwap-tooltext="输入不能为空且应为1至6位数"
             data-iwap-minlength="1" data-iwap-maxlength="12">
            <span>编号:</span><input name="no" type="text"
                                   data-iwap-xtype="TextField" id="no" class="input_text" value="">
        </div>
    </div>
    <div class="col-md-6">
        <div class="inputbox pr" data-iwap="tooltipdiv"
             data-iwap-empty="false" data-iwap-tooltext="年份份不能为空">
            <span>申请日期:</span> <input data-iwap-xtype="TextField" id="time" name="time" class="Wdate"
                                      onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy-MM-dd'})"/>
        </div>
    </div>
    <div class="col-md-6">
        <div class="inputbox pr" data-iwap="tooltipdiv"
             data-iwap-empty="false" data-iwap-tooltext="类型不能为空">
            <span>类型:</span>

            <select id="_select1" onchange="setRuleContent1();" style="height: 29px;">
                <option value="1">工资标准</option>
                <option value="1">福利</option>
                <option value="1">年金</option>
                <option value="1">社保</option>
                <option value="1">休假</option>
                <option value="1">处分</option>
            </select>
            <input name="types" type="text"
                   data-iwap-xtype="TextField" id="types" class="input_text"
                   style="width: 220px"   value="" onchange="setRuleContent1();"  placeholder="输入"
            >
        </div>
    </div>

    <div class="col-md-6 fl">
        <div class="inputbox tl pr" data-iwap="tooltipdiv"
             data-iwap-empty="true">
            <span>申请审批事项:</span><input name="remarks" type="text"
                                       data-iwap-xtype="TextField" id="remarks" class="input_text"
                                       value="">
        </div>
    </div>
</form>
<div class="tc mb14">
    <a href="javaScript:void(0)" class="btn btn-primary mr30" id="query"
       onclick="iwapGrid.doQuery()">查询</a> <a href="javaScript:void(0)"
                                              class="btn btn-primary mr30" id="btn_clear"
                                              onclick="iwapGrid.doReset();">清空</a>
</div>
<!-- 页面查询区域　END -->
<!-- 查询内容区域　开始 -->


<div class="table_box">
    <!-- 表格工具栏　开始 -->
    <div class="table_nav" style="display:inline">
        <a id="selectmultidel" class="" onclick="del()" href="javaScript:void(0)">删除</a>
        <a href="javaScript:void(0)" data-iwap-dialog="myModal" id="add" onclick="add()">新增</a>
    </div>
    <div style="display:inline">
        <a href="javaScript:void(0)" id="uplaod">
            <div class="upfilebox" id="ctx_import_file">
                <div class="upfile_layout">
                    <span>附件导入</span></div>
                <input type="file" class="upfile" enctype="multipart/form-data" id="import_file"></div>
        </a>
    </div>
    <!-- 表格工具栏　END -->
    <table id="iwapGrid"
           class="mygrid table table-bordered table-striped table-hover"
           data-iwap="grid" data-iwap-id="" data-iwap-param=""
           data-iwap-pagination="true">
        <tr class="tr_css">
            <s>
                <th data-grid-name="CHECK_NO" primary="primary" data-order="">
                    <s><input type="checkbox" selectmulti="selectmulti" value="{{value}}"></s></th>
                <th data-grid-name="CHECK_NO" class="tab_css_1">审批事项编号</th>
                <th data-grid-name="TIMES" class="tab_css_1">申请日期</th>
                <th data-grid-name="NAME" class="tab_css_1">申请人</th>
                <th data-grid-name="TYPE" class="tab_css_1">类别</th>

                <th data-grid-name="CHECK_NO" option="option" option-html='' class="tab_css_1">
                    <span>操作</span>
                    <s>
                        <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id="editOne">编辑</a>&nbsp;|&nbsp;
                        <a href="javaScript:void(0)" class="export" onclick="exportData(this)">导出</a>&nbsp;|&nbsp;

                        <a href="javaScript:void(0)" class="" onclick="seePicture(this)"
                                id="see">查看附件&nbsp;|&nbsp;
                        </a>
                        <a href="javaScript:void(0)" class="delByID" onclick="delByID(this)">删除附件</a>&nbsp;|&nbsp;

                        <a href="javaScript:void(0)" class="editId" onclick="doSure(this)">确认归档</a>

                    </s>
                </th>
            </s>
        </tr>
    </table>
</div>
</div>

<!-- 查询内容区域　END -->
</body>
<script type="text/javascript">
    var actionType = "", iwapGrid = null, condionForm = null, operForm = null, roleForm = null, grantTree = null,
        grantTreeData = null;
    $(document).ready(function () {
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
                    'readerCheckMg'
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

        // 初始化角色,状态（采用数据字典）
        initSelectKV('{"sendState":"sendState"}');


        /* 文件上传 */
        fileUpload = $.IWAP.FileUpload({

            'id': 'import_file',
            'url': 'iwap.ctrl',
            'fileType': ['pdf', 'png',"jpg"],
            'label': '导入',
            'afterUpload': function () {
                var field = {'files': fileUpload.getFileName(), 'isOverWrite': 'false', 'start': '1', 'limit': '10'};
                var data = JSON.stringify({
                    'body': field,
                    'header': {
                        'txcode': "readerCheckMg",
                        'option': 'upload',
                        'actionId': "doBiz",
                        'checkNo': iwapGrid.getCurrentRow()['CHECK_NO'],
                    }
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
                            alert("");
                        } else if (data['body']['info']) {
                            alert("" + data['body']['info']);
                        } else {
                            alert("Inport error");
                        }
                        location.reload();
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
            'renderTo': 'uplaodfile'
        });

        $("#uplaod").change(function () {

            var checkStatus=iwapGrid.getCurrentRow()['STATUS'];
            if(checkStatus==1){
                alert("该记录已经归档，不能修改！");
                return;
            }

            if (iwapGrid.getCheckValues() == "") {
                alert("请选择要导入的记录!");
                return;
            }
            fileUpload.upload();
        });

    });

    //增加
    function add() {
        //每次点击增加按钮后：角色和状态设成默认值
        document.getElementById("reset").style.display = "block";
        document.getElementById("resetDel").style.display = "none";
        $('#myModal').dialog('新增');
        actionType = "add";
        operForm.reset();

        //  $('select#acct_status').val('1');
    };

    //对话框
    function dialogModal(id) {
        $('#' + id).dialog();
    };

    //保存
    function doSave() {
        var checkNo= iwapGrid.getCurrentRow()['CHECK_NO'];
        var extParam = {'option': actionType, 'txcode': 'readerCheckMg', 'actionId': 'doBiz','checkNo':checkNo};
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
            'txcode': "readerCheckMg",
            'userids': iwapGrid.getCheckValues(),
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
            'holidayids': iwapGrid.getCurrentRow()['NH_NO'],
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

    function delByID(obj) {
        var checkStatus=iwapGrid.getCurrentRow()['STATUS'];
        if(checkStatus==1){
            alert("该记录已经归档，不能修改！");
            return;
        }
        if (!confirm("确定要删除吗?请确定!")) {
            return;
        }
        var param = {
            'option': "deleteById",
            'txcode': "readerCheckMg",
            'checkNo': iwapGrid.getCurrentRow()['CHECK_NO'],
            'actionId': "doBiz"
        };
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {

            gridData = rs.body.rows;


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

    function doSure(obj) {
        var checkStatus=iwapGrid.getCurrentRow()['STATUS'];
        if(checkStatus==1){
            alert("该记录已经归档，不能修改！");
            return;
        }
        if (!confirm("确定要归档吗?请确定!")) {
            return;
        }
        var param = {
            'option': "sure",
            'txcode': "readerCheckMg",
            'checkNo': iwapGrid.getCurrentRow()['CHECK_NO'],
            'actionId': "doBiz"
        };
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {

            if (rs['header']['msg']) {
                return alert("归档失败:" + rs['header']['msg']);
            } else {
                alert("归档成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        }, function () {
            alert("归档失败!");
        });


    }







    function exportData() {
        if (!confirm("确定要导出审表吗?请确定!")) {
            return;
        }
        var data = {
            'exportFlag': '1',
            'filetype': 'xlsx',
            'option': "export",
            'txcode': 'readerCheckMg',
            'checkNo': iwapGrid.getCurrentRow()['CHECK_NO'],
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


    //编辑
    function edit(obj) {
        var checkStatus=iwapGrid.getCurrentRow()['STATUS'];
        if(checkStatus==1){
            alert("该记录已经归档，不能修改！");
            return;
        }
        document.getElementById("reset").style.display = "none";
        document.getElementById("resetDel").style.display = "block";
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


    function setRuleContent() {
        var selectValue = $('#_select option:selected').text();//选中select的内容
        //alert("selectValue" + selectValue);
        var inputValue = $("#TYPE").val(selectValue);//input获得select的内容并显示在输入框中
        //alert(inputValue);
    };

    function setRuleContent1() {
        var selectValue = $('#_select1 option:selected').text();//选中select的内容
        //alert("selectValue" + selectValue);
        var inputValue = $("#types").val(selectValue);//input获得select的内容并显示在输入框中
        //alert(inputValue);
    };


    function doRead() {//这是按钮的点击事件


        var img = document.getElementById('ele');//获取img对象
        img.src = "<%=path %>/images/bg.png"
    }

    var btObject = document.getElementById("bt");

    //为按钮注册点击事件,添加事件处理函数


    function seePicture() {
        $("#big_img").empty();
        $('#myModal2').dialog("照片详情");
        var param = {
            'option': "see",
            'txcode': "readerCheckMg",
            'checkNo': iwapGrid.getCurrentRow()['CHECK_NO'],
            'actionId': "doBiz"
        };
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            if (rs['header']['msg']) {
                return alert("查看失败:" + rs['header']['msg']);
            } else {
                debugger
                var gridData = rs.body.rows;
                var imgPath = "<%=path %>" + gridData;
                console.info("imgPath" + imgPath);
                if (console.log(gridData.search("pdf") != -1 )){
                    $("#big_img").append("<img alt=" + imgPath + " style='cursor:pointer;width:500px;height: 300px;' src=" + imgPath + ">");
                }
                $("#big_img").append("<iframe alt=" + imgPath + " style='cursor:pointer;width:500px;height: 300px;' src=" + imgPath + ">");


            }
        }, function () {
            alert("删除失败!");
        });
        //根据id获取图片标签,设置图片的src属性值


    };


</script>
</html>