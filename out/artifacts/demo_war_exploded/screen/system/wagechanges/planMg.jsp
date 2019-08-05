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

        .iwapui .inputbox .input_text {
            width: 100px;
            height: 28px;
            padding: 1px 5px;
            background-color: #fff;
        }

        .iwapui .inputbox > span {
            vertical-align: top;
            height: 32px;
            line-height: 32px;
            width: 150px;
            display: inline-block;
            text-align: right;
            overflow: hidden;
            margin-right: 5px;
        }

    </style>
    <title>用户管理</title>
</head>
<body class="iwapui center_body">

<input type="hidden" value="${userInfo.ACCT_ID}" id="_roleid">
<input type="hidden" value="${userInfo.ORG_ID}" id="_deptId">
<div id="divDialog" class="divDialog">
    <div class="bg"></div>
    <div class="dialog" id="myModal" style="width: 995px ;height: 800px">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">信息</h4>
        </div>
        <div class="modal-body">
            <form method="post" onsubmit="return false" id="dialogarea">

                <!-- form开始 -->
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>姓名:</span> <input name="NH_NAME" type="text"
                                                  data-iwap-xtype="TextField" id="NH_NAME" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>fmis员工号:</span> <input name="NH_FMIS" type="text"
                                                  data-iwap-xtype="TextField" id="NH_FMIS" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>休假年份:</span> <input name="NH_XIUJIAYEAR" type="text"
                                                  data-iwap-xtype="TextField" id="NH_XIUJIAYEAR" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>1月停发岗位工资:</span> <input name="NH_ONETGZ" type="text"
                                                  data-iwap-xtype="TextField" id="NH_ONETGZ" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>1月停发绩效工资:</span> <input name="NH_ONETJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_ONETJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>1月停发交通补贴:</span> <input name="NH_ONETCHE" type="text"
                                                  data-iwap-xtype="TextField" id="NH_ONETCHE" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>2月停发岗位工资:</span> <input name="NH_TWOTGZ" type="text"
                                                  data-iwap-xtype="TextField" id="NH_TWOTGZ" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>2月停发绩效工资:</span> <input name="NH_TWOTJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_TWOTJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>2月停发交通补贴:</span> <input name="NH_TWOTCHE" type="text"
                                                  data-iwap-xtype="TextField" id="NH_TWOTCHE" class="input_text"
                                                  style="width: 250" value="">
                </div>


                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>3月停发岗位工资:</span> <input name="NH_THTGZ" type="text"
                                                  data-iwap-xtype="TextField" id="NH_THTGZ" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>3月停发绩效工资:</span> <input name="NH_THTJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_THTJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>3月停发交通补贴:</span> <input name="NH_THTCHE" type="text"
                                                  data-iwap-xtype="TextField" id="NH_THTCHE" class="input_text"
                                                  style="width: 250" value="">
                </div>


                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>4月停发岗位工资:</span> <input name="NH_FOTGZ" type="text"
                                                  data-iwap-xtype="TextField" id="NH_FOTGZ" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>4月停发绩效工资:</span> <input name="NH_FOTJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_FOTJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>4月停发交通补贴:</span> <input name="NH_FOTCHE" type="text"
                                                  data-iwap-xtype="TextField" id="NH_FOTCHE" class="input_text"
                                                  style="width: 250" value="">
                </div>


                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>5月停发岗位工资:</span> <input name="NH_FIVETGZ" type="text"
                                                  data-iwap-xtype="TextField" id="NH_FIVETGZ" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>5月停发绩效工资:</span> <input name="NH_FIVETJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_FIVETJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>5月停发交通补贴:</span> <input name="NH_FIVETCHE" type="text"
                                                  data-iwap-xtype="TextField" id="NH_FIVETCHE" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>6月停发岗位工资:</span> <input name="NH_SIXTGZ" type="text"
                                                  data-iwap-xtype="TextField" id="NH_SIXTGZ" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>6月停发绩效工资:</span> <input name="NH_SIXTJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_SIXTJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>6月停发交通补贴:</span> <input name="NH_SIXTCHE" type="text"
                                                  data-iwap-xtype="TextField" id="NH_SIXTCHE" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>7月停发岗位工资:</span> <input name="NH_SETGZ" type="text"
                                                  data-iwap-xtype="TextField" id="NH_SETGZ" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>7月停发绩效工资:</span> <input name="NH_SETJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_SETJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>7月停发交通补贴:</span> <input name="NH_SETCHE" type="text"
                                                  data-iwap-xtype="TextField" id="NH_SETCHE" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>8月停发岗位工资:</span> <input name="NH_AGETGZ" type="text"
                                                  data-iwap-xtype="TextField" id="NH_AGETGZ" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>8月停发绩效工资:</span> <input name="NH_AGETJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_AGETJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>8月停发交通补贴:</span> <input name="NH_AGETCHE" type="text"
                                                  data-iwap-xtype="TextField" id="NH_AGETCHE" class="input_text"
                                                  style="width: 250" value="">
                </div>

                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>9月停发岗位工资:</span> <input name="NH_NITGZ" type="text"
                                                  data-iwap-xtype="TextField" id="NH_NITGZ" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>9月停发绩效工资:</span> <input name="NH_NITJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_NITJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>9月停发交通补贴:</span> <input name="NH_NITCHE" type="text"
                                                  data-iwap-xtype="TextField" id="NH_NITCHE" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>10月停发岗位工资:</span> <input name="NH_TENTGZ" type="text"
                                                   data-iwap-xtype="TextField" id="NH_TENTGZ" class="input_text"
                                                   style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>10月停发绩效工资:</span> <input name="NH_TENTJX" type="text"
                                                   data-iwap-xtype="TextField" id="NH_TENTJX" class="input_text"
                                                   style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>10月停发交通补贴:</span> <input name="NH_TENTCHE" type="text"
                                                   data-iwap-xtype="TextField" id="NH_TENTCHE" class="input_text"
                                                   style="width: 250" value="">
                </div>

                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>11月停发岗位工资:</span> <input name="NH_ETGZ" type="text"
                                                   data-iwap-xtype="TextField" id="NH_ETGZ" class="input_text"
                                                   style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>11月停发绩效工资:</span> <input name="NH_ETJX" type="text"
                                                   data-iwap-xtype="TextField" id="NH_ETJX" class="input_text"
                                                   style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>11月停发交通补贴:</span> <input name="NH_ETCHE" type="text"
                                                   data-iwap-xtype="TextField" id="NH_ETCHE" class="input_text"
                                                   style="width: 250" value="">
                </div>


                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>12月停发岗位工资:</span> <input name="NH_TTGZ" type="text"
                                                   data-iwap-xtype="TextField" id="NH_TTGZ" class="input_text"
                                                   style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>12月停发绩效工资:</span> <input name="NH_TTJX" type="text"
                                                   data-iwap-xtype="TextField" id="NH_TTJX" class="input_text"
                                                   style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>12月停发交通补贴:</span> <input name="NH_T<TCHE" type="text"
                                                   data-iwap-xtype="TextField" id="NH_TTCHE" class="input_text"
                                                   style="width: 250" value="">
                </div>

                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>年终奖停发岗位工资:</span> <input name="NH_NTGZ" type="text"
                                                   data-iwap-xtype="TextField" id="NH_NTGZ" class="input_text"
                                                   style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>年终奖发绩效工资:</span> <input name="NH_NTJX" type="text"
                                                  data-iwap-xtype="TextField" id="NH_NTJX" class="input_text"
                                                  style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>年终奖停发交通补贴:</span> <input name="NH_NTCHE" type="text"
                                                   data-iwap-xtype="TextField" id="NH_NTCHE" class="input_text"
                                                   style="width: 250" value="">
                </div>


                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>以后年度停发岗位工资:</span> <input name="NH_YTGZ" type="text"
                                                    data-iwap-xtype="TextField" id="NH_YTGZ" class="input_text"
                                                    style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>以后年度发绩效工资:</span> <input name="NH_YTJX" type="text"
                                                   data-iwap-xtype="TextField" id="NH_YTJX" class="input_text"
                                                   style="width: 250" value="">
                </div>
                <div class="inputbox pr" data-iwap="tooltipdiv"
                     data-iwap-empty="true">
                    <span>以后年度停发交通补贴:</span> <input name="NH_YTCHE" type="text"
                                                    data-iwap-xtype="TextField" id="NH_YTCHE" class="input_text"
                                                    style="width: 250" value="">
                </div>
                </div>



        </form>
        <!-- form END -->
        <div style="padding-left:430px;padding-top: 50px;">
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
        <div class="col-md-6">
            <div class="inputbox pr" data-iwap="tooltipdiv"
                 data-iwap-empty="false" data-iwap-tooltext="年份份不能为空">
                <span>年份:</span> <input data-iwap-xtype="TextField" id="year" style="width: 250px" name="year" class="Wdate"
                                        onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy'})"/>
            </div>
        </div>
        <div class="col-md-6">
            <div class="inputbox pr" data-iwap="tooltipdiv"
                 data-iwap-empty="false" data-iwap-tooltext="月份不能为空">
                <span>月份:</span> <input data-iwap-xtype="TextField" style="width: 250px" id="yue" name="yue" class="Wdate"
                                        onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy/MM'})"/>
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
        <a href="javaScript:void(0)" id="export" onclick="exportData()">审核导出</a>
        <a id="summary" class="" onclick="summ()"
           href="javaScript:void(0)">导入工资表</a>

    </div>
    <div style="display:inline">
        <a href="javaScript:void(0)" id="uplaod">
            <div class="upfilebox" id="ctx_import_file"><div class="upfile_layout">
                <span>值班导入</span></div>
                <input type="file" class="upfile" enctype="multipart/form-data" id="import_file"></div></a>
    </div>

    <!-- 表格工具栏　END -->
    <table id="iwapGrid"
           class="mygrid table table-bordered table-striped table-hover"
           data-iwap="grid" data-iwap-id="" data-iwap-param=""
           data-iwap-pagination="true">
        <tr>
            <th data-grid-name="NH_FMIS" primary="primary" data-order="">
                <s><input type="checkbox" selectmulti="selectmulti" value="{{value}}"></s></th>

            <th data-grid-name="NH_NAME" class="tab_css_1">姓名</th>
            <th data-grid-name="NH_FMIS" class="tab_css_1">FMIS员工号</th>
            <th data-grid-name="NH_XIUJIAYEAR"class="tab_css_1">休假年份</th>
            <th data-grid-name="NH_GYTF" class="tab_css_1">当年停发岗位工资(应停发数)</th>
            <th data-grid-name="NH_GYITF" class="tab_css_1">当年停发岗位工资(已停发数)</th>
            <th data-grid-name="NH_GNXUTF" class="tab_css_1">当年停发岗位工资(年末需停发）</th>
            <th data-grid-name="NH_JYTF" class="tab_css_1">当年停发绩效工资（应停发数）</th>
            <th data-grid-name="NH_JYITF" class="tab_css_1">当年停发绩效工资（已停发数）</th>
            <th data-grid-name="NH_JNXUTF"class="tab_css_1">当年停发绩效工资（年末需停发）</th>
            <th data-grid-name="NH_BYTF" class="tab_css_1">当年停发交通补贴（应停发数）</th>
            <th data-grid-name="NH_BYTIF" class="tab_css_1">当年停发交通补贴已停发数）</th>
            <th data-grid-name="NH_BNXUTF" class="tab_css_1">当年停发交通补贴（年末需停发）</th>
            <th data-grid-name="NH_ONETGZ" class="tab_css_1">1月停发岗位工资</th>
            <th data-grid-name="NH_ONETJX" class="tab_css_1">1月停发绩效工资</th>
            <th data-grid-name="NH_ONETCHE" class="tab_css_1">1月停发交通补贴</th>
            <th data-grid-name="NH_TWOTGZ" class="tab_css_1">2月停发岗位工资</th>
            <th data-grid-name="NH_TWOTJX" class="tab_css_1">2月停发绩效工资</th>
            <th data-grid-name="NH_TWOTCHE" class="tab_css_1">2月停发交通补贴</th>
            <th data-grid-name="NH_THTGZ" class="tab_css_1">3月停发岗位工资</th>
            <th data-grid-name="NH_THTJX" class="tab_css_1">3月停发绩效工资</th>
            <th data-grid-name="NH_THTCHE" class="tab_css_1">3月停发交通补贴</th>
            <th data-grid-name="NH_FOTGZ" class="tab_css_1">4月停发岗位工资</th>
            <th data-grid-name="NH_FOTJX" class="tab_css_1">4月停发绩效工资</th>
            <th data-grid-name="NH_FOTCHE" class="tab_css_1">4月停发交通补贴</th>
            <th data-grid-name="NH_FIVETGZ" class="tab_css_1">5月停发岗位工资</th>
            <th data-grid-name="NH_FIVETJX" class="tab_css_1">5月停发绩效工资</th>
            <th data-grid-name="NH_FIVETCHE" class="tab_css_1">5月停发交通补贴</th>
            <th data-grid-name="NH_SIXTGZ" class="tab_css_1">6月停发岗位工资</th>
            <th data-grid-name="NH_SIXTJX" class="tab_css_1">6月停发绩效工资</th>
            <th data-grid-name="NH_SIXTCHE" class="tab_css_1">6月停发交通补贴</th>
            <th data-grid-name="NH_SETGZ" class="tab_css_1">7月停发岗位工资</th>
            <th data-grid-name="NH_SETJX" class="tab_css_1">7月停发绩效工资</th>
            <th data-grid-name="NH_SETCHE" class="tab_css_1">7月停发交通补贴</th>
            <th data-grid-name="NH_AGETGZ" class="tab_css_1">8月停发岗位工资</th>
            <th data-grid-name="NH_AGETJX" class="tab_css_1">8月停发绩效工资</th>
            <th data-grid-name="NH_AGETCHE" class="tab_css_1">8月停发交通补贴</th>
            <th data-grid-name="NH_NITGZ" class="tab_css_1">9月停发岗位工资补贴</th>
            <th data-grid-name="NH_NITJX" class="tab_css_1">9月停发绩效工资</th>
            <th data-grid-name="NH_NITCHE" class="tab_css_1">9月停发交通补贴</th>
            <th data-grid-name="NH_TENTGZ" class="tab_css_1">10月停发岗位工资</th>
            <th data-grid-name="NH_TENTJX" class="tab_css_1">10月停发绩效工资</th>
            <th data-grid-name="NH_TENTCHE" class="tab_css_1">10月停发交通补贴</th>
            <th data-grid-name="NH_ETGZ" class="tab_css_1">11月停发岗位工资</th>
            <th data-grid-name="NH_ETJX" class="tab_css_1">11月停发绩效工资</th>
            <th data-grid-name="NH_ETCHE" class="tab_css_1">11月停发交通补贴</th>
            <th data-grid-name="NH_TTGZ" class="tab_css_1">12月停发岗位工资</th>
            <th data-grid-name="NH_TTJX" class="tab_css_1">12月停发绩效工资</th>
            <th data-grid-name="NH_TTCHE" class="tab_css_1">12月停发交通补贴</th>
            <th data-grid-name="NH_NTGZ" class="tab_css_1">年终奖停发岗位工资</th>
            <th data-grid-name="NH_NTJX" class="tab_css_1">年终奖停发绩效工资</th>
            <th data-grid-name="NH_NTCHE" class="tab_css_1">年终奖停发交通补贴</th>
            <th data-grid-name="NH_YTGZ" class="tab_css_1">以后年度停发岗位工资</th>
            <th data-grid-name="NH_YTJX" class="tab_css_1">以后年度停发绩效工资</th>
            <th data-grid-name="NH_YTCHE" class="tab_css_1">以后年度停发交通补贴</th>
            <th data-grid-name="NH_ZGZ" class="tab_css_1">输入的总停发岗位工资</th>
            <th data-grid-name="NH_ZJX" class="tab_css_1">输入的总停发绩效工资</th>
            <th data-grid-name="NH_ZCHE" class="tab_css_1">输入的总停发交通补贴</th>
            <th data-grid-name="NH_JYGZ" class="tab_css_1">总数与年末须发岗位工资对比</th>
            <th data-grid-name="NH_JYZX" class="tab_css_1">总数与年末须发绩效工资对比</th>
            <th data-grid-name="NH_JYCHE" class="tab_css_1">总数与年末须发交通补贴对比</th>


            <th data-grid-name="NH_FMIS" option="option" option-html=''><s>
                <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id ="editOne">计划发放</a>&nbsp;|&nbsp;
                <a href="javaScript:void(0)" class="editId" onclick="delOne(this)">删除</a>


            </s></th>
        </tr>
    </table>
</div>
</div>

<!-- 查询内容区域　END -->
</body>
<script type="text/javascript">
    var actionType="", iwapGrid=null,condionForm=null,operForm=null,roleForm=null,grantTree=null,grantTreeData=null;
    $(document).ready(function() {
        initDict&&initDict("Role",function(){
            //重置按钮事件
            operForm=$.IWAP.Form({'id':'dialogarea'});
            roleForm=$.IWAP.Form({'id':'dialogarea2'});
            condionForm=$.IWAP.Form({'id':'Conditions'});

            grantTreeData=_dictJson['Role'];
            grantTree = $.IWAP.Tree({
                disabled:false,
                hidden:false,
                value:[],
                isMultiSelect:true,
                checked:true,
                data:grantTreeData,
                mode:'local',
                renderTo:'grant_tree'
            });

            $('#reset').click(function() {
                operForm.reset();
                $('#myModal select#acct_status').val('1');
            });
            $('#resetDel').click(function() {
                if($('#myModal input#acct_id').val()==$('#_userid').val()){
                    $('#myModal input').not('#acct_id,#org_id,#org_nm').val('');
                }else{
                    $('#myModal input').not('#acct_id').val('');
                }
                $('#myModal select#acct_status').val('1');
            });
            $('#reset_role').click(function() {
                roleForm.reset();
                grantTree.Load(grantTreeData,false);
            });
            /*查询表格初始化  设置默认查询条件	var fData={'actionId':'doBiz','start':'0','limit':'10','txcode':'roleMg'};*/
            var fData={'actionId':'doBiz','start':'0','limit':'10','txcode':'' +
                    'jihuaMg'};
            iwapGrid = $.IWAP.iwapGrid({
                mode:'server',
                fData:fData,
                Url:'iwap.ctrl',
                grid:'grid',
                form:condionForm,
                renderTo:'iwapGrid'
            });
        });
        // 初始化角色,状态（采用数据字典）
        initSelectKV('{"userStatus":"ACCT_STATUS","state":"WorkState","acct_status":"ACCT_STATUS"}');

    });

    //增加
    function add(){
        //每次点击增加按钮后：角色和状态设成默认值
        document.getElementById("reset").style.display = "block";
        document.getElementById("resetDel").style.display = "none";
        $('#myModal').dialog('新增请假');
        actionType="add";
        operForm.reset();

        //  $('select#acct_status').val('1');
    };
    //对话框
    function dialogModal(id){
        $('#'+id).dialog();
    };
    //保存
    function doSave(){
        var extParam={'option':actionType,'txcode':'jihuaMg','actionId':'doBiz'};
        var param=operForm.getData();


        /*if($('input#org_nm').val()==''){
            alert("所属机构输入框不能为空");
            return ;
        }*/
        $.IWAP.applyIf(param,extParam);
        $.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
            $('#myModal').find('.close').click();
            if (rs['header']['msg']) {
                return alert("保存失败:"+rs['header']['msg']);
            }else{
                alert("保存成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        },function(){
            alert("保存失败!");
        });
    }
    //删除（可多个）
    function del(){
        if(iwapGrid.getCheckValues()=="") {
            alert("请选择要删除的柜员!");
            return;
        }

        if (!confirm("确定要删除吗?请确定!"))
            return;

        var param={'option':"remove",'txcode':"holidayMg",'holidayids': iwapGrid.getCheckValues(),'actionId':"doBiz"};
        $.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
            if (rs['header']['msg']) {
                return alert("删除失败:"+rs['header']['msg']);
            }else{
                alert("删除成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        },function(){
            alert("删除失败!");
        });
    };
    //删除（单个）
    function delOne(obj){
        if (!confirm("确定要删除吗?请确定!")){
            return;
        }
        var param={'option':"remove",'txcode':"holidayMg",'holidayids': iwapGrid.getCurrentRow()['NH_CARDID'],'actionId':"doBiz"};
        $.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
            if (rs['header']['msg']) {
                return alert("删除失败:"+rs['header']['msg']);
            }else{
                alert("删除成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        },function(){
            alert("删除失败!");
        });
    }


    //编辑
    function edit(obj){
        document.getElementById("reset").style.display = "none";
        document.getElementById("resetDel").style.display = "block";
        $('#myModal').dialog("修改柜员");
        actionType="save";
        operForm.reset();
        operForm.setData(iwapGrid.getCurrentRow());
        operForm.disabledById("acct_id,org_id,org_nm");
        $('select#acct_status').val(iwapGrid.getCurrentRow()['acct_status']);
        $('#show_org').hide();
        if(iwapGrid.getCurrentRow()['acct_id']!=$('#_userid').val()){
            $('#show_org').show();
        }
    };

    //授权
    function grant(obj){
        var callFn = function(rs){
            if(rs['header']['msg']){
                return alert("授权查询出错:"+rs['header']['msg']);
            }
            roleForm.reset();
            var grants = rs['body']['grants'];
            grantTree.Load(grantTreeData,false);
            for(var i in grants){
                grantTree.setCheck(grants[i]['role_id'],true);
            }
            $('#myModal3').dialog();
        }
        var field = {'option':'query_grant','acct_id':iwapGrid.getCurrentRow()['acct_id']};
        sendAjax(field,'userMg','doBiz',callFn);
    }

    //授权保存
    function doSaveRole(){
        var callFn = function(rs){
            $('#myModal3').find('.close').click();
            if(rs['header']['msg']){
                alert("授权保存出错:"+rs['header']['msg']);
            }else{
                alert("授权保存成功");
            }
        }
        var field = {'option':'save_grant','acct_role_list':grantTree.getValue(),'acct_id':iwapGrid.getCurrentRow()['acct_id']};
        sendAjax(field,'userMg','doBiz',callFn);
    }
    function daochu(){

        var param={'option':"daochu",'txcode':"tingMg",'holidayids': iwapGrid.getCheckValues(),'actionId':"doBiz"};
        $.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
            if (rs['header']['msg']) {
                return alert("导出失败:"+rs['header']['msg']);
            }else{
                alert("导出成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        },function(){
            alert("导出失败!");
        });

    }
    function setRuleContent(){
        var selectValue = $('#_select option:selected').text();//选中select的内容
        //alert("selectValue" + selectValue);
        var inputValue = $("#NH_QINGTYPE").val(selectValue);//input获得select的内容并显示在输入框中
        //alert(inputValue);
    };

    function exportData(){

        var data = {'exportFlag':'1','filetype':'xlsx','option':"daochu",'txcode':'tingMg','holidayids': iwapGrid.getCheckValues(),'actionId':'doBiz','start':'0','limit':'1000'};
        var form = condionForm.getData();
        $.IWAP.apply(data,form);

        var titleString = [];
        $("table#iwapGrid tbody tr:eq(0) th").each(function(){
            if($(this).hasClass("tl")){
                var titleMap = {};
                titleMap[$(this).attr("data-grid-name")]=$(this).html();
                titleString.push(titleMap);
            }
        });

        titleString = JSON.stringify(titleString);
        data.titleString=titleString;

        var param="";

        for (var key in data) {
            param += key + "=" + data[key] + "&";
        }
        param = param.substr(0,param.length-1);
        var iframe = $('<iframe name="iwapdownload">');
        iframe.css("display", "none");
        iframe.attr("src", "download.ctrl?" + param);
        $('body').prepend(iframe);
    }
    function exportData1(){
        var data = {'exportFlag':'1','filetype':'xlsx','txcode':'userMg','actionId':'doBiz','start':'0','limit':'1000'};
        var form = condionForm.getData();
        $.IWAP.apply(data,form);

        var titleString = [];
        $("table#iwapGrid tbody tr:eq(0) th").each(function(){
            if($(this).hasClass("tl")){
                var titleMap = {};
                titleMap[$(this).attr("data-grid-name")]=$(this).html();
                titleString.push(titleMap);
            }
        });

        titleString = JSON.stringify(titleString);
        data.titleString=titleString;

        var param="";

        for (var key in data) {
            param += key + "=" + data[key] + "&";
        }
        param = param.substr(0,param.length-1);
        var iframe = $('<iframe name="iwapdownload">');
        iframe.css("display", "none");
        iframe.attr("src", "download.ctrl?" + param);
        $('body').prepend(iframe);
    }

    /* 文件上传 */
    fileUpload = $.IWAP.FileUpload({
        'id':'import_file',
        'url':'iwap.ctrl',
        'fileType':['xls','xlsx'],
        'label':'导入',
        'afterUpload':function(){
            var field={'imp_id':'000005','files':fileUpload.getFileName(),'isOverWrite':'true'};
            var data=JSON.stringify({'body':field,'header':{'txcode':"importData",'option':"daoru",'actionId':"doBiz"}});
            $.ajax({
                timeout:0,
                data:data,
                cache:false,
                contentType:'application/json',
                dataType:'json',
                type:"POST",
                url:'iwap.ctrl',
                async:false,
                error:function(request){
                    alert("Connection error");
                },
                success:function(data){
                    if(data['header']['msg']){
                        return alert("");
                    }else if(data['body']['info']){
                        return alert(""+data['body']['info']);
                    }else {
                        return alert("Inport error");
                    }
                }

            });
        },//文件上传之后的触发事件，不管上传是否成功都会执行
        'beforeUpload':function(){return true},
        'size':1024000,  //允许上传的单个文件大小，是以byte为单位
        'success':function(res){
            //alert("文件上传成功");
            //var json = JSON.parse(res.target.response);
            //alert(json.msg);
            //console.log(json.uploadfilelist);

        },//上传成功后的回调函数
        'failed':function(){alert('上传失败');},//上传失败后的回调函数
        'renderTo':'upfile'
    });

    $("#upfile").change(function(){

        fileUpload.upload();
    });


    function chaxun(){
        var extParam={'option':"chaxun",'txcode':'jihuaMg','actionId':'doBiz'};
        var param=operForm.getData();


        /*if($('input#org_nm').val()==''){
            alert("所属机构输入框不能为空");
            return ;
        }*/
        $.IWAP.applyIf(param,extParam);
        $.IWAP.iwapRequest("iwap.ctrl",param,function(rs){
            $('#myModal').find('.close').click();
            if (rs['header']['msg']) {
                return alert("查询失败:"+rs['header']['msg']);
            }else{
                alert("查询成功");
                iwapGrid.doQuery(condionForm.getData());
            }
        },function(){
            alert("查询失败!");
        });
    }


</script>
</html>