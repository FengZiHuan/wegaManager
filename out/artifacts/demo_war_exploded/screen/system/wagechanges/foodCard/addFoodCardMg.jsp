<%@ page language="java" import="com.nantian.iwap.web.WebEnv"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>省行本部-查询</title>
    <meta name="description" content="">
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
    <style>


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
</head>
<body class="iwapui center_body">
<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
<div id="divDialog" class="divDialog">
    <div class="bg"></div>
    <div class="dialog" id="myModal" style="width: 600px;">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">信息</h4>
        </div>
        <div class="modal-body">
            <form method="post" onsubmit="return false" id="dialogarea">
                <!-- form开始 -->
                <div class="col-md-12">
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>姓名:</span> <input name="NAME" type="text"
                                                data-iwap-xtype="TextField" id="NAME" class="input_text"
                                                value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>测试:</span> <input type="text" id="tt"   data-iwap-xtype="TextField" class="input_text"

                                                autocomplete="off"/>
                    </div>

                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>部门:</span> <input name="DEPT" type="text"
                                                data-iwap-xtype="TextField" id="DEPT" class="input_text"
                                                value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>出生日期:</span> <input name="BRITHDAY" type="text"
                                                  data-iwap-xtype="TextField" id="BRITHDAY" class="input_text"
                                                  value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>饭卡号码:</span> <input name="FOODCARDNO" type="text"
                                                  data-iwap-xtype="TextField" id="FOODCARDNO" class="input_text"
                                                  value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="不能为空">
                        <span>人员编号:</span> <input name="PEOPLENO" type="text"
                                                  data-iwap-xtype="TextField" id="PEOPLENO" class="input_text"
                                                  value="">
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
                    <div id="" class="buttonbox">
                        <button data-iwap-xtype="ButtonField" id="resetDel"
                                class="btn false mr30">清空
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 第三个对话框 END-->

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
                <span>部门:</span><input name="depts" type="text"
                                       data-iwap-xtype="TextField" id="depts" class="input_text"
                                       value="">
            </div>
        </div>


    </form>
</div>
<div class="tc mb14">
    <a href="javaScript:void(0)" class="btn btn-primary mr30" id="query"
       onclick="iwapGrid.doQuery()">查询</a> <a href="javaScript:void(0)"
                                              class="btn btn-primary mr30" id="btn_clear"
                                              onclick="iwapGrid.doReset();">清空</a>
</div>
<!-- 页面查询区域　END -->
<!-- 查询内容区域　开始 -->

<!-- 表格工具栏　开始 -->

<div class="table_nav" style="display:inline">


</div>

<!-- 表格工具栏　END -->
<!-- 表格工具栏　开始 -->

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

        <th data-grid-name="NAME" class="tl">姓名</th>
        <th data-grid-name="DEPT" class="tl">部门</th>
        <th data-grid-name="BRITHDAY" class="tl">出生日期</th>
        <th data-grid-name="FOODCARDNO" class="tl">饭卡号</th>
        <th data-grid-name="PEOPLENO" class="tl">人员编号</th>
        <th data-grid-name="ID" option="option" option-html=''><span>操作</span>
            <s>
                <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id="editOne">输入编号</a>
            </s>
        </th>
    </tr>
</table>
</div>
</div>
<!-- 查询内容区域　END -->
</body>
<script type="text/javascript">
    var actionType = "", iwapGrid = null, condionForm = null, operForm = null, grantTree = null, orgTree = null;
    var grantTreeData = null, orgTreeData = null;


    $(document).ready(function () {

        var param = {'option': "", 'txcode': "testMg", 'actionId': "doBiz"};

        $.IWAP.iwapRequest("iwap.ctrl", param, function (rs) {
            if (rs['header']['msg']) {
                return alert("删除失败:" + rs['header']['msg']);
            } else {

                var mapList = rs.body.rows;
                var myArr = new Array()
                var i = 0;
                //填充框数据
                $.each(mapList, function (i, data) {

                    var  dataValue = null;
                    var j=0;
                    for (var item in data) {
                        value = data[item];//key所对应的value
                        j++;
                        if (j>=4) {
                            dataValue=dataValue+value;
                        }
                        else {
                            dataValue=dataValue+value+"-";
                        }

                    }
                    myArr[i] ={name :dataValue};
                    i++;
                });


                iwapGrid.doQuery(condionForm.getData());

           //核心
                $("#tt").bigAutocomplete({
                    data: myArr
                });
            }
        }, function () {
            alert("删除失败!");
        });


        //重置按钮事件
        operForm = $.IWAP.Form({'id': 'dialogarea'});
        condionForm = $.IWAP.Form({'id': 'Conditions'});
        $('#reset').click(function () {
            operForm.reset();
            $('select#ROLE_ENABLED').val('1');
        });
        $('#resetDel').click(function () {
            $('input').not('#ROLE_ID').val('');
            $('select#ROLE_ENABLED').val('1');
        });

        /*查询表格初始化  设置默认查询条件*/
        var fData = {'actionId': 'doBiz', 'start': '0', 'limit': '10', 'txcode': 'addFoodCardMg'};
        iwapGrid = $.IWAP.iwapGrid({
            mode: 'server',
            fData: fData,
            Url: '${ctx}/iwap.ctrl',
            grid: 'grid',
            form: condionForm,
            renderTo: 'iwapGrid'
        });

        initDict && initDict("submitState", function () {
        });
        initDict && initDict("checkState", function () {
        });
        // 初始化角色,状态（采用数据字典）
        initSelectKV('{"sendState":"sendState"}');

        /* 文件上传 */
        fileUpload = $.IWAP.FileUpload({
            'id': 'import_file',
            'url': 'iwap.ctrl',
            'fileType': ['xlsx', 'xls'],
            'label': '导入',
            'afterUpload': function () {
                var field = {
                    'imp_id': '000021',
                    'files': fileUpload.getFileName(),
                    'isOverWrite': 'false',
                    'option': 'import',
                    'start': '1',
                    'limit': '10'
                };
                var data = JSON.stringify({'body': field, 'header': {'txcode': "sendFoodMg", 'actionId': "doBiz"}});
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
            fileUpload.upload();
        });


    })

    //增加
    function add() {
        //每次点击增加按钮后：角色是否启用设成默认值
        document.getElementById("reset").style.display = "block";
        document.getElementById("resetDel").style.display = "none";
        $('#myModal').dialog('省行本部绩效奖金');
        actionType = "add";
        operForm.reset();

    };

    //对话框
    function dialogModal(id) {
        $('#' + id).dialog();
    };

    //保存
    function doSave() {
        var id = iwapGrid.getCurrentRow()['ID']
        var extParam = {'option': actionType, 'txcode': 'addFoodCardMg', 'actionId': 'doBiz', 'id': id};
        var param = operForm.getData();
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
            alert("请选择要删除的记录!");
            return;
        }

        if (!confirm("确定要删除吗?请确定!"))
            return;

        var param = {
            'option': "remove",
            'txcode': "deptUpdateMg",
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
            'txcode': "deptUpdateMg",
            'userids': iwapGrid.getCurrentRow()['D_ID'],
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
        var checkStatus = iwapGrid.getCurrentRow()['CHECKSTATUS'];
        if (checkStatus == 1) {
            alert("该记录已经审核，不能修改！");
            return;
        }
        document.getElementById("reset").style.display = "none";
        document.getElementById("resetDel").style.display = "block";
        $('#myModal').dialog("修改");
        actionType = "save";
        operForm.reset();
        operForm.setData(iwapGrid.getCurrentRow());

    };


</script>
</html>