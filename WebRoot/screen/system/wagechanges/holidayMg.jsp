<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
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

        table th{white-space: nowrap;}
        table td{white-space: nowrap;}
        body,table{font-size:12px;}
        table{empty-cells:show;border-collapse: collapse;margin:0 auto;}
        h1,h2,h3{font-size:12px;margin:0;padding:0;}
        table.tab_css_1{border:1px solid #cad9ea;color:#666;}
        table.tab_css_1 th{background-repeat:repeat-x;height:40px;}
        table.tab_css_1 td,table.tab_css_1 th{border:1px solid #cad9ea;padding:0 1em 0;height:40px;}
        table.tab_css_1 tr.tr_css{background-color:#f5fafe;height:40px;}
    </style>
    <title>用户管理</title>
</head>
<body class="iwapui center_body">


<!-- 对话框开始 -->
<div id="divDialog" class="divDialog">
    <div class="bg"></div>
    <div class="dialog" id="myModal" style="width: 600px;">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">新增请假</h4>
        </div>
        <div class="modal-body">
            <form method="post" onsubmit="return false" id="dialogarea">
                <!-- form开始 -->


                <div class="col-md-12" >
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" >
                        <span>序号:</span>
                        <input name="NH_NO" type="text" data-iwap-xtype="TextField" id="NH_NO" class="input_text"style="">
                    </div>

                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" >
                        <span>FMIS员工号:</span>
                        <input name="NH_FMIS" type="text" data-iwap-xtype="TextField" id="NH_FMIS" class="input_text"style="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false">
                        <span>身份证号:</span> <input name="NH_CARDID" type="text"
                                                  data-iwap-xtype="TextField" id="NH_CARDID" class="input_text">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="部门名称不能为空">
                        <span>部门:</span> <input name="NH_DEPT" type="text"
                                                  data-iwap-xtype="TextField" id="NH_DEPT" class="input_text"
                                                  style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="柜员名称不能为空">
                        <span>姓名:</span> <input name="NH_NAME" type="text"
                                                data-iwap-xtype="TextField" id="NH_NAME" class="input_text"
                                                style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="false" data-iwap-tooltext="柜员名称不能为空">
                        <span>请假类型:</span>
                           <select id="_select"  onchange="setRuleContent();"> 
                             <option value="1">探亲假</option>
                             <option value="1">事假</option>
                             <option value="1">普通病假</option>
                             <option value="1">重疾病假</option>
                             <option value="1">普通长病休</option>
                             <option value="1">普通重病休</option>
                           </select>
                        <input name="NH_QINGTYPE" type="text"
                               data-iwap-xtype="TextField" id="NH_QINGTYPE" class="input_text"
                               style="width: 200px"   value="" onchange="setRuleContent1();"  placeholder="输入"
                        >
                    </div>

                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>休假年份:</span> <input name="NH_XIUJIAYEAR" type="text"
                                                  data-iwap-xtype="TextField" id="NH_XIUJIAYEAR" class="input_text"
                                                  style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>起止时间:</span> <input name="NH_TIMES" type="text"
                                                  data-iwap-xtype="TextField" id="NH_TIMES" class="input_text"
                                                  style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>本次休假天数(工作日):</span> <input name="NH_SUMDAYS" type="text"
                                                  data-iwap-xtype="TextField" id="NH_SUMDAYS" class="input_text"
                                                  style="width: 250" value="">
                    </div>
                    <div class="inputbox pr" data-iwap="tooltipdiv"
                         data-iwap-empty="true">
                        <span>备注:</span> <input name="NH_BEIZHU" type="text"
                                                         data-iwap-xtype="TextField" id="NH_BEIZHU" class="input_text"
                                                         style="width: 250" value="">
                    </div>
                </div>
            </form>
            <!-- form END -->
            <div style="padding-left: 210px">
                <div class="buttonbox" >
                    <button data-iwap-xtype="ButtonField" id="save"
                            class="btn false mr30" data-dialog-hidden="true"
                            onclick="doSave()">保存</button>
                </div>
                <div id="" class="buttonbox">
                    <button data-iwap-xtype="ButtonField" id="reset"
                            class="btn false mr30">清空</button>
                </div>
                <div id="" class="buttonbox" style="display: none">
                    <button data-iwap-xtype="ButtonField" id="resetDel"
                            class="btn false mr30" >清空</button>
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
    <div class="dialog" id="myModal3" style="width: 600px;">
        <div class="dialog-header">
            <button type="button" data-dialog-hidden="true" class="close">
                <span>×</span>
            </button>
            <h4 class="dialog-title">用户授权</h4>
        </div>
        <div class="modal-body">
            <div id="grant_tree"></div>
            <div style="padding-left: 210px">
                <div class="buttonbox">
                    <button data-iwap-xtype="ButtonField" id="save_role"
                            class="btn false mr30" data-dialog-hidden="true"
                            onclick="doSave()">保存</button>
                </div>
                <div id="" class="buttonbox">
                    <button data-iwap-xtype="ButtonField" id="reset_role"
                            class="btn false mr30">清空</button>
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
            <span>fmisNo:</span><input name="fmisNo" type="text"
                                       data-iwap-xtype="TextField" id="fmisNo" class="input_text" value="">
        </div>
    </div>
    <div class="col-md-6">
        <div class="inputbox pr" data-iwap="tooltipdiv"
             data-iwap-empty="false" data-iwap-tooltext="年份份不能为空">
            <span>年份:</span> <input data-iwap-xtype="TextField" id="year" name="year" class="Wdate"
                                    onFocus="WdatePicker({lang:'zh-cn',dateFmt:'yyyy'})"/>
        </div>
    </div>
    <div class="col-md-6 fl">
        <div class="selectbox mr60 inputbox">
            <label class="select_label">请假类型:</label>
            <select data-iwap-xtype="ListField" name="type" class="select_content" id="type">
                <option value="0">全部</option>
                <option value="1">事假</option>
                <option value="2">普通病假</option>
                <option value="3">重疾病假</option>
                <option value="4">普通长病假</option>
                <option value="5">重疾长病假</option>
            </select>
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
        <a id="selectmultidel" class="" onclick="del()"href="javaScript:void(0)">删除</a>
        <a href="javaScript:void(0)" data-iwap-dialog="myModal" id="add" onclick="add()">新增</a>

    </div>
    <div  style="display:inline">

        <a href="javaScript:void(0) "id="upfile"  ></a>
    </div>
    <!-- 表格工具栏　END -->
        <table id="iwapGrid"
               class="mygrid table table-bordered table-striped table-hover"
               data-iwap="grid" data-iwap-id="" data-iwap-param=""
               data-iwap-pagination="true">
        <tr class="tr_css">
            <s>
            <th data-grid-name="NH_NO" primary="primary" data-order="">
                <s><input type="checkbox" selectmulti="selectmulti" value="{{value}}"></s></th>
            <th data-grid-name="NH_NO" class="tab_css_1" >序号</th>
            <th data-grid-name="NH_FMIS" class="tab_css_1">FMIS员工号</th>
            <th data-grid-name="NH_CARDID" class="tab_css_1">身份证号</th>
            <th data-grid-name="NH_DEPT" class="tab_css_1">部门</th>
            <th data-grid-name="NH_NAME" class="tab_css_1">姓名</th>
            <th data-grid-name="NH_QINGTYPE" class="tab_css_1">请休假类型</th>
            <th data-grid-name="NH_XIUJIAYEAR"class="tab_css_1">休假年份</th>
            <th data-grid-name="NH_TIMES" class="tab_css_1">起止时间</th>
            <th data-grid-name="NH_SUMDAYS" class="tab_css_1">本次休假天数(工作日)</th>
            <th data-grid-name="NH_BEIZHU" class="tab_css_1">备注</th>
            <th data-grid-name="NH_NO" option="option" option-html='' class="tab_css_1">
                <s>
                <a href="javaScript:void(0)" class="editId" onclick="edit(this)" id ="editOne">修改</a>&nbsp;|&nbsp;
                <a href="javaScript:void(0)" class="editId" onclick="delOne(this)">删除</a>
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
                    'holidayMg'};
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

        // 初始化角色,状态（采用数据字典）
        initSelectKV('{"sendState":"sendState"}');

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
        var extParam={'option':actionType,'txcode':'holidayMg','actionId':'doBiz'};
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
        var param={'option':"remove",'txcode':"holidayMg",'holidayids': iwapGrid.getCurrentRow()['NH_NO'],'actionId':"doBiz"};
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

        if(iwapGrid.getCheckValues()=="") {
            alert("请选择要导出的数据!");
            return;
        }

        if (!confirm("确定要导出吗?请确定!"))
            return;

        var param={'option':"daochu",'txcode':"holidayMg",'holidayids': iwapGrid.getCheckValues(),'actionId':"doBiz"};
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

        var data = {'exportFlag':'1','filetype':'xlsx','option':"daochu",'txcode':'holidayMg','holidayids': iwapGrid.getCheckValues(),'actionId':'doBiz','start':'0','limit':'1000'};
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


</script>
</html>