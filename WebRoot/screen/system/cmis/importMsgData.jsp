<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/screen/comm/header.jsp" %>
    <link href="<%=path%>/css/style.css" rel="stylesheet">
    <link href="<%=path%>/css/zTreeStyle.css" rel="stylesheet">
    <script type="text/javascript" src="<%=path%>/js/iwapui.js"></script>
    <script type="text/javascript"
            src="<%=path%>/js/jquery.ztree.all-3.5.js"></script>
    <script type="text/javascript"
            src="<%=path%>/js/jquery.ztree.exhide-3.5.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Tree.js"></script>
    <script type="text/javascript" src="<%=path%>/js/FileUpload.js"></script>
    <script type='text/javascript' src="<%=path%>/js/dictionary.js"></script>
    <script type='text/javascript' src="<%=path%>/js/public.js"></script>
    <style type="text/css">
        .t_string {
            text-align: left
        }

        .t_number {
            text-align: right
        }

        .t_date {
            text-align: center
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
    <div class="dialog" id="myModal"
         style="width: 600px; position: absolute; top: 20px;">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">添加数据</h4>
        </div>
        <div class="modal-body">
            <form method="post" onsubmit="return false" id="dialogarea">
                <!-- form开始 -->
                <div class="col-md-12">
                    <input name="MPT_ID" type="text" data-iwap-xtype="TextField" id="MPT_ID" class="input_text"
                           hidden="hidden">

                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="客户ID非空且长度大于4"
                         data-iwap-minlength="5">
                        <span>客户ID:</span> <input name="CUST_ID" type="text"
                                                  data-iwap-xtype="TextField" id="CUST_ID" class="input_text">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="客户中文名全称不能为空">
                        <span>客户中文名全称:</span> <input name="CHN_NAME" type="text"
                                                     data-iwap-xtype="TextField" id="CHN_NAME" class="input_text"
                                                     style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="统一社会信用代码不能为空">
                        <span>统一社会信用代码:</span> <input name="MPT_CREDIT_CODE" type="text"
                                                      data-iwap-xtype="TextField" id="MPT_CREDIT_CODE"
                                                      class="input_text" style="width: 250" value="123456">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>组织机构代码:</span> <input name="MPT_ORG_CODE" type="text"
                                                    data-iwap-xtype="TextField" id="MPT_ORG_CODE" class="input_text"
                                                    style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>标签:</span> <input name="MPT_LABEL" type="text"
                                                data-iwap-xtype="TextField" id="MPT_LABEL" class="input_text"
                                                style="width: 250" value="">
                    </div>
                    <div class="selectbox mr60 inputbox" id="ctx_iwap-gen-7">
                        <span>分类:</span> <select data-iwap-xtype="ListField"
                                                 id="MPT_TYPE" name="MPT_TYPE" class="select_btn ">
                    </select>
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>预警指标:</span> <input name="MPT_WARNTARGET" type="text"
                                                  data-iwap-xtype="TextField" id="MPT_WARNTARGET" class="input_text"
                                                  style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>风险分类:</span> <input name="MPT_RISKKIND" type="text"
                                                  data-iwap-xtype="TextField" id="MPT_RISKKIND" class="input_text"
                                                  style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>内容:</span> <input name="MPT_INFO" type="text"
                                                data-iwap-xtype="TextField" id="MPT_INFO" class="input_text"
                                                style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>来源:</span> <input name="MPT_SOURCE" type="text"
                                                data-iwap-xtype="TextField" id="MPT_SOURCE" class="input_text"
                                                style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <!-- <span >发起者所在机构:</span> -->
                        <input name="MPT_ORG_ID" type="text" data-iwap-xtype="TextField"
                               id="MPT_ORG_ID" class="input_text" style="width: 250"
                               value="" hidden="hidden">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>发布日期:</span> <input name="MPT_PUBDATE" type="text"
                                                  data-iwap-xtype="TextField" id="MPT_PUBDATE" class="input_text"
                                                  style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>结办日期:</span> <input name="ENDDATE" type="text"
                                                  data-iwap-xtype="TextField" id="ENDDATE" class="input_text"
                                                  style="width: 250" value="">
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
    <!-- 第四个对话框 -->
    <div class="bg"></div>
    <div class="dialog" id="myModal4" style="width: 600px;">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">数据导入</h4>
        </div>
        <div class="modal-body">
            <div id="import_div"
                 style="width: 250px; margin: 20px auto 0 auto; text-align: center">
                <div class="f14 c00">
                    选择文件：<span id="fileName">(未选中文件)</span>
                </div>
            </div>
            <div style="padding-left: 210px">
                <div class="buttonbox">
                    <button data-iwap-xtype="ButtonField" id="import_data"
                            class="btn false mr30" data-dialog-hidden="true"
                            onclick="doImport()">导入
                    </button>
                    <button data-iwap-xtype="ButtonField" id="reset_data"
                            class="btn false mr30" data-dialog-hidden="true"
                            onclick="doResetFile()">重置
                    </button>
                </div>
            </div>
        </div>
    </div>
    <!-- 第四个对话框 END-->
</div>
<!-- 对话框END -->
<!-- 页面查询区域开始 -->
<form id="Conditions" class="clearfix">
    <div class="col-md-6 fl">
        <div class="inputbox pr" data-iwap="tooltipdiv"
             data-iwap-empty="true">
            <span>客户中文名全称:</span><input name="SCHN_NAME" type="text"
                                        data-iwap-xtype="TextField" id="SCHN_NAME" class="input_text" value="">
        </div>
    </div>
    <div class="col-md-6 fl">
        <div class="inputbox tl pr" data-iwap="tooltipdiv"
             data-iwap-empty="true">
            <span>信息内容:</span><input name="SMPT_INFO" type="text"
                                     data-iwap-xtype="TextField" id="SMPT_INFO" class="input_text" value="">
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
        <a id="selectmultidel" class="" onclick="del()" href="javaScript:void(0)">删除</a>
        <a href="javaScript:void(0)" data-iwap-dialog="myModal" id="add" onclick="add()">新增</a>
        <a href="javaScript:void(0)" id="export" onclick="exportData()">导出</a>
        <a href="javaScript:void(0)" id="fileUpload" onclick="importData()">导入</a>
    </div>
    <!-- 表格工具栏　END -->
    <table id="iwapGrid"
           class="mygrid table table-bordered table-striped table-hover"
           data-iwap="grid" data-iwap-id="" data-iwap-param=""
           data-iwap-pagination="true">
        <tr>
            <th data-grid-name="MPT_ID" primary="primary" data-order="">
                <input type="checkbox" id="allchecked2" name="selectname" selectmulti="selectmulti" value="">
                <s><input type="checkbox" id="allchecked1" selectmulti="selectmulti" value="{{value}}"></s>
            </th>
            <th data-grid-name="cust_id" class="tl">客户ID</th>
            <th data-grid-name="chn_name" class="tl">客户中文名全称</th>
            <th data-grid-name="mpt_credit_code" class="tl">统一社会信用代码</th>
            <th data-grid-name="mpt_org_code" class="tl">组织机构代码</th>
            <th data-grid-name="mpt_label" class="tl">信息标签</th>
            <th data-grid-name="MPT_TYPE" class="tl">信息分类</th>
            <th data-grid-name="mpt_warntarget" class="tl">预警指标</th>
            <th data-grid-name="mpt_riskkind" class="tl">风险分类</th>
            <th data-grid-name="mpt_info" class="tl">信息内容</th>
            <th data-grid-name="mpt_source" class="tl">信息来源</th>
            <th data-grid-name="mpt_org_id" class="tl">发起者所在机构</th>
            <th data-grid-name="mpt_pubdate" class="tl">发布日期</th>
            <th data-grid-name="enddate" class="tl">结办日期</th>

            <th data-grid-name="acct_id" option="option" option-html=''>
                <span>操作</span>
                <s>
                    <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id="editOne">修改</a>&nbsp;|&nbsp;
                    <a href="javaScript:void(0)" class="editId" onclick="delOne(this)">删除</a>
                </s></th>
        </tr>
    </table>
</div>
<!-- 查询内容区域　END -->
</body>
<script type="text/javascript">
    var actionType = "", iwapGrid = null, condionForm = null, operForm = null, roleForm = null, grantTree = null,
        grantTreeData = null, fileUpload = null;

    $(document).ready(
        function () {

            //重置按钮事件
            operForm = $.IWAP.Form({
                'id': 'dialogarea'
            });
            /* roleForm = $.IWAP.Form({
                'id' : 'dialogarea2'
            }); */
            condionForm = $.IWAP.Form({
                'id': 'Conditions'
            });

            /* getDictJson && getDictJson("EnableRole", function(json) {
                grantTreeData = json['EnableRole'];
                grantTree = $.IWAP.Tree({
                    disabled : false,
                    hidden : false,
                    value : [],
                    isMultiSelect : true,
                    checked : true,
                    data : grantTreeData,
                    mode : 'local',
                    renderTo : 'grant_tree'
                });
            }); */
            // 初始化角色,状态（采用数据字典）
            initSelectKV('{"MPT_TYPE":"MPT_TYPE","mpt_org_id":"ORG_CODE"}');
            //afterInitSelectKV();
            $('#reset').click(function () {
                operForm.reset();
            });

            /*查询表格初始化  设置默认查询条件*/
            var fData = {
                'chn_name': '%' || $('#SCHN_NAME').val,
                'mpt_label': '%',
                'mpt_type': '%',
                'mpt_info': '%' || $('#SMPT_INFO').val,
                'actionId': 'doBiz',
                'start': '0',
                'limit': '10',
                'txcode': 'importPushMsg'
            };
            iwapGrid = $.IWAP.iwapGrid({
                mode: 'server',
                fData: fData,
                Url: 'iwap.ctrl',
                grid: 'grid',
                form: condionForm,
                renderTo: 'iwapGrid'
            });

            //可选择多张图片
            fileUpload = $.IWAP.FileUpload({
                'id': 'import_file',
                'url': 'iwap.ctrl',
                'fileType': ['xls', 'xlsx'],
                'size': 100,
                'renderTo': 'import_div',
                'label': '请选择导入文件',
                'afterUpload': function () {
                    var field = {
                        'imp_id': '000003',
                        'files': fileUpload.getFileName(),
                        'isOverWrite': 'false'
                    };
                    var data = JSON.stringify({
                        'body': field,
                        'header': {
                            'txcode': "importData",
                            'actionId': "doBiz"
                        }
                    });
                    $
                        .ajax({
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
                                    return alert("导入失败:"
                                        + data['header']['msg']);
                                } else if (data['body']['info']) {
                                    return alert("导入成功:" + data['body']['info']) || $('#myModal4').find('.close').click();
                                } else {
                                    return alert("Import error");
                                }
                            }
                        });
                },
                'beforeUpload': function () {
                    return true;
                }

            });

            $('#import_file').on(
                'change',
                function () {
                    if (fileUpload.getFileName()
                        && fileUpload.getFileName().length > 0) {
                        $('#myModal4 #fileName').html(
                            fileUpload.getFileName()[0]);
                    } else {
                        $('#myModal4 #fileName').html('(未选中文件)');
                    }
                });

        });

    //增加
    function add() {
        //每次点击增加按钮后：角色和状态设成默认值
        document.getElementById("reset").style.display = "block";
        document.getElementById("resetDel").style.display = "none";
        $('#myModal').dialog('添加数据');
        actionType = "add";
        operForm.reset();
        var orgId = "${userInfo.ORG_ID}";
        $('#MPT_ORG_ID').val(orgId);
        $('select#MPT_TYPE').val('');
    };

    //对话框
    function dialogModal(id) {
        $('#' + id).dialog();
    };

    //保存
    function doSave() {
        var extParam = {
            'option': actionType,
            'txcode': 'importPushMsg',
            'actionId': 'doBiz',
        };
        var param = operForm.getData();

        /* if ($('input#acct_nm').val() == '') {
            alert("柜员名称不能为空");
            return;
        }
        if ($('input#org_nm').val() == '') {
            alert("所属机构输入框不能为空");
            return;
        } */
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
        console.log(iwapGrid.getCheckValues());
        var param = {
            'option': "remove",
            'txcode': "importPushMsg",
            'MPT_IDS': iwapGrid.getCheckValues(),
            'actionId': "doBiz"
        };
        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            $("#allchecked2").prop("checked", false);
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
            'txcode': "importPushMsg",
            'MPT_IDS': iwapGrid.getCurrentRow()['MPT_ID'],
            'MPT_ID': $('#MPT_ID').val,
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
        $('#myModal').dialog("修改数据");
        actionType = "save";
        operForm.reset();
        operForm.setData(iwapGrid.getCurrentRow());
        $('select#MPT_TYPE').val(iwapGrid.getCurrentRow()['mpt_type']);
    };

    function importData() {
        fileUpload.removeFile();
        $('#myModal4 #fileName').html('(未选中文件)');
        $('#myModal4').dialog();
    }


    function doImport() {
        fileUpload.upload();
    }

    function doResetFile() {
        fileUpload.removeFile();
        $('#myModal4 #fileName').html('(未选中文件)');
    }

    function exportData() {
        var data = {
            'exportFlag': '1',
            'filetype': 'xls',
            'txcode': 'importPushMsg',
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
        data['titleString'] = titleString;

        var param = "";
        for (var key in data) {
            param += key + "=" + data[key] + "&";
        }
        param = param.substr(0, param.length - 1);
        var dl_iframe = $('iframe[name="iwapdownload_iframe"]');
        var dl_form = $('form[name="iwapdownload_form"]');
        if (dl_iframe.length > 0 && dl_form.length > 0) {
            dl_form.attr("action", "download.ctrl?" + param);
            dl_form.submit();
        } else {
            dl_iframe = $('<iframe name="iwapdownload_iframe">');
            dl_iframe.css("display", "none");
            $('body').prepend(dl_iframe);

            dl_form = $('<form name="iwapdownload_form" target="iwapdownload_iframe" method="post"><input type="submit" id="dl_form_submit" /></form>');
            dl_form.attr("action", "download.ctrl?" + param);
            dl_form.css("display", "none");
            $('body').prepend(dl_form);

            $('#dl_form_submit').click();
        }

    }
</script>
</html>