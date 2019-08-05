/**
 * Created by Jackie on 2015/5/20 0020.
 */
(function($){
    if(!$.IWAP){
        $.extend(
            {
                IWAP: {}
            }
        );
    }

    /**
     * 初始化组件
     * @param config
     label  文本
     allowBlank 是否允许为空  默认true
     disabled   是否禁用    默认false
     data 下拉数据 当mode为local时生效
     //checked 是否选择  默认false  多选暂未实现
     listeners  监听事件
     readonly  是否只读    false
     mode 数据来源 local/server
     value  默认值
     url 请求URL Mode 为server时有效
     baseParam 基础过虑参数 mode 为server时有效
     displayValue 显示值字段
     UseValue 使用值字段
     //isMultiSelect 是否多选 默认false  多选暂未实现，目前默认单选
     renderTo	渲染对象	Dom对象或者domID
     */
    $.IWAP.ListField=function(config) {
        var allowBlank = typeof(config['allowBlank'])=='undefined'?true:config['allowBlank'];
        var genId = config['id'] || $.IWAP.id();
        var readonly = config['readonly'] == true;
        var disabled=config['disabled'] == true;
        var hidden=config['hidden'] == true;
        var label = config['label'] || "";
        var width = config['width'] || "";
        var mode=config['mode'];
        var data=config['data'];
        var renderTo = null;
        var value = config['value'] || '';
        var events=config['listeners']||[];
        var changes=config['changes']||[];
        var displayText=config['displayValue'];
        var useText=config['useValue'];
        var baseParam=config['baseParam']||{};
        if (config['renderTo']) {
            if ($.IWAP.isString(config['renderTo'])) {
                renderTo = $("#" + config['renderTo']);
            } else {
                renderTo = config['renderTo'];
            }
        } else {
            renderTo = document.body;
        }
        var html = [];
        html.push('<div class="selectbox mr60" id="ctx_'+genId+'">');
        var selectObj=null;
        if(config["id"]){
            selectObj=$("#"+config["id"]);
            selectObj.attr("xtype","ListField");
        }else{
            if(disabled){
                html.push('<span>'+label+':</span>&nbsp;&nbsp;<select xtype="ListField" id="'+genId+'" disabled="disabled" width="'+width+'" class="select_btn mr60"><select>');
            }else{
                html.push('<span>'+label+':</span>&nbsp;&nbsp;<select xtype="ListField" id="'+genId+'" width="'+width+'" class="select_btn "><select>');
            }
            if(!allowBlank){
                html.push('<i class="err_status" id="err_'+genId+'">*</i>');
            }
            $(renderTo).append(html.join(''));

        }
        create();
        //创建select和option下拉列表
        function create(){
            if(mode=='local') {
                for (var i = 0; i < data.length; i++) {
                    var tmp=[];
                    if(data[i][displayText]==value){
                        if(i==data.length-1){
                            tmp.push('<option value="' + data[i][useText] + '"  selected="selected">' + data[i][displayText] + '</option>');
                        }else{
                            tmp.push('<option value="' + data[i][useText] + '"  selected="selected">' + data[i][displayText] + '</option>');
                        }
                    }else{
                        if(i==data.length-1){
                            tmp.push('<option value="' + data[i][useText] + '">' + data[i][displayText] + '</option>');
                        }else{
                            tmp.push('<option value="' + data[i][useText] + '">' + data[i][displayText] + '</option>');
                        }

                    }
                    $('#'+genId).append(tmp.join(''));
                }
            }else if(mode=='server'){
                if(events['beforeLoad']){
                    events['beforeLoad'].apply(events['beforeLoad'],arguments);
                }
               query(baseParam);
            }
        }
        //从后台查询数据，后台返回值格式为json，使用值字段为useText,显示值字段为displayText
        function query(querydata){
             $.ajax({
            	 	type:'post',
                    url: config['url'],
                    //cache:false,
                    data: JSON.stringify(querydata),
                    contentType: 'application/json',
                    success: function (resData){
                        var temp=[];
                        resData=resData["body"]["rows"];
                        for (var i = 0; i < resData.length; i++) {
                            if(resData[i][displayText]==value){
                                if(i==resData.length-1){
                                    temp.push('<option value="' + resData[i][useText] + '"  selected="selected">' + resData[i][displayText] + '</option>');
                                }else{
                                    temp.push('<option value="' + resData[i][useText] + '"  selected="selected">' + resData[i][displayText] + '</option>');
                                }
                            }else{
                                if(i==resData.length-1){
                                    temp.push('<option value="' + resData[i][useText] + '">' + resData[i][displayText] + '</option>');
                                }else{
                                    temp.push('<option value="' + resData[i][useText] + '">' + resData[i][displayText] + '</option>');
                                }
                            }
                        }
                        $('#'+genId).append(temp.join(''));
                    },
                    dataType:'json'
                });
        };
        /*是否禁用*/
        if(disabled){
            $("#" + genId).attr('disabled',true);
            $('#'+genId).css('background','#eeeeee');
        }
        /*选定下拉值触发事件*/
        /*$("#" + genId).change(function(){
                change&&change(this);
        });*/
        if(changes['change']){
            $("#"+genId).on("change",changes['change']);
        }
         /*是否隐藏*/
        if(hidden){
            $("#" + genId).hide();
        }
        if (readonly){
            $("#" + genId).attr('readonly',true);
        };
        if(events['expend']){
            $("#"+genId).on("click",events['expend']);
        }
        /*返回对象*/
        return {
            //genId:genId,
            XType:'ListField',
            options:config,
            disabled:disabled,
            hidden:hidden,
            /**
             * 函数
             */

            /*取得选定使用值*/
            getValue:function(){
                return $("#"+genId).val();
            },
            /*设置默认显示值*/
            setValue:function(val){
                $("#"+genId+'  option').each(function(){
                    if($(this).html()==val){
                        $(this).attr('selected','selected');
                    }else{
                         $(this).removeAttr('selected');
                    }
                });
            },
            /*禁用下拉列表*/
            disabled:function(){
                $("#" + genId).attr('disabled',true);
                $('#'+genId).css('background','#eeeeee');
            },
            /*隐藏下拉列表*/
            hidden:function(){
                $("#" + genId).hide();
            },
            /*启用下拉列表*/
            enabled:function(){
                $("#" + genId).removeAttr('disabled');
                $('#'+genId).css('background','');
            },
            /*取得下拉列表的id值*/
            getId:function(){
                return genId;
            },
            /*追加下拉列表数据，如果mode为server，则先到服务端进行查询后再进行数据添加*/
            load:function(data,append) { 
                    if (mode=='server'&&append){
                        query(baseParam);
                    };
                    if (!append) {
                        $("#" + genId).empty();
                    }
                    var temp ='';
                    for (var i = 0; i < data.length; i++) {
                        temp += '<option value="' + data[i][useText] + '">' + data[i][displayText] + '</option>';
                    }
                    $("#" + genId).append(temp);
                },
             /*设置查询参数，放入基础过滤参数中*/
            setParam:function(condition){
               if(mode=='server'){
                   for(var key in condition){
                       baseParam[key]=condition[key];
                   }
               }
            }
        }
    }
    $.IWAP.XType['ListField']=$.IWAP.ListField;
}(jQuery))
