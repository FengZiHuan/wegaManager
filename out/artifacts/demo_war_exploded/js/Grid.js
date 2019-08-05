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
     txCode 交易码
     param 传入参数
     beforeRequest 在进行ajax请求前触发
     beforeSuccess  成功返回前触发
     id 标识 如果设置并且在dom中存在则不去创建对象，只是渲染
     lazyLoad 是否延迟加载 默认为true
     renderTo   渲染对象    Dom对象或者domID
     isMultiSelect 是否可以多选  默认为true
     */
    $.IWAP.Grid=function(config) {
        var _html=[];
        var txcode=config['txcode'];
        var url=config['sUrl'];
        var multiSelect=typeof(config['isMultiSelect'])=='undefined'?true:config['isMultiSelect'];
        var genId=config['id']||$.IWAP.id();
        var lazyLoad=typeof(config['lazyLoad'])=='undefined'?true:config['lazyLoad'];
        var renderTo = null;
        var _table=null;
        var _selected=[];
        config['bFilter']=false;
        config['ajax']={"data" :config['param']};
        config['bRetrieve']=true;
        config['serverSide']=true;
        config['sServerMethod']='POST';
        config['fnServerData']=function(sSource, aoData, fnCallback,settions) {
                        opt=settions;
                        var param={};
                        for(var i=0;i<aoData.length;i++){
                            param[aoData[i]['name']]=aoData[i]['value'];
                        }
                        if(!lazyLoad){
                            if(config['beforeRequest']){
                                config['beforeRequest'].apply(config['beforeRequest'],arguments);
                            }
                            var p={};
                            p['header']={'txcode':txcode};
                            p['body']=param;
                                p['body']['start']=settions['_iDisplayStart'];
                                p['body']['limit']=settions['_iDisplayLength'];
                            var fnCallBackDef=function(resp){
                                    if(config['beforeSuccess']){
                                        config['beforeSuccess'].apply(config['beforeSuccess'],arguments);
                                    }
                                    fnCallback({"iTotalDisplayRecords":resp.body['total']||0,"aaData":resp.body['rows']||[]});
                                    if(!resp.body['total']){
                                        $("#"+genId+"_info").html("抱歉,没有搜索到符合条件的数据");
                                    }
                                    $("#"+genId+" tbody tr").bind("click",function(){
                                        //判断是否支持多选
                                        if(multiSelect){//多选
                                            if($(this).hasClass("selected")){
                                                $(this).removeClass("selected");
                                                for(var i=0;i<_selected.length;i++){
                                                    if(_selected[i]==_table.fnSettings().aoData[this.rowIndex-1]._aData){
                                                        _selected.splice(i,1);
                                                    }
                                                }
                                            }else{
                                                $(this).addClass("selected");
                                                _selected.push(_table.fnSettings().aoData[this.rowIndex-1]._aData);
                                            }
                                        }else{//单选
                                            $("#"+genId+" tbody tr").removeClass("selected");
                                            $(this).addClass("selected");
                                             _selected.splice(0, _selected.length);
                                             _selected.push(_table.fnSettings().aoData[this.rowIndex-1]._aData);
                                        }
                                    });
                                }
                            $.IWAP.iwapRequest(url,p,fnCallBackDef);
                            settions.ajax.data=settions.oAjaxData;
                        }
                    
                };
        config['oLanguage']={//下面是一些汉语翻译
                    "sSearch": "搜索",
                    "sLengthMenu": "每页显示 _MENU_ 条记录",
                    "sZeroRecords": "没有检索到数据",
                    "sInfo": "显示 _START_ 至 _END_ 条 &nbsp;&nbsp;共 _TOTAL_ 条",
                    "sInfoFiltered": "(筛选自 _MAX_ 条数据)",
                    "sInfoEmtpy": "没有数据",
                    "sProcessing": "正在加载数据...",
                    "sProcessing": "<img src='{{rootUrl}}global/img/ajaxLoader/loader01.gif' />", //这里是给服务器发请求后到等待时间显示的 加载gif
                            "oPaginate":
                            {
                                "sFirst": "首页",
                                "sPrevious": "前一页",
                                "sNext": "后一页",
                                "sLast": "末页"
                            }
                    };

         if (config['renderTo']) {
            if ($.IWAP.isString(config['renderTo'])) {
                renderTo = $("#" + config['renderTo']);
            } else {
                renderTo = config['renderTo'];
            }
        } else {//如果不设置渲染对象则默认为body
            renderTo = document.body;
        }
        _html.push('<table class="display" id="'+genId+'">');
        _html.push('<thead><tr>');
        var _header=[];
        _header=config['aoColumns'];
        if (_header.length>0) {
            for (var i=0.; i<_header.length>0; i++) {
                if (_header[i]['mData']) {
                    _html.push('<th>'+_header[i]['mData']+'</th>');
                };
            };
        }else{
            return null;
        };
        _html.push('</tr></thead>');
        _html.push('</table>');
        $(renderTo).append(_html.join(''));
         _table=$('#'+genId).dataTable(config);

        $("#"+genId+"_length").before($("#"+genId));
        //$("#"+genId+"_length").append("<span>&nbsp;&nbsp;</span>");
        $("#"+genId+"_length").css("padding-top","0.755em");
        $("#"+genId+"_info").css("clear","none");
        $("#"+genId+"_info").addClass("custom_iwap_info");
        $("#"+genId+"_paginate").addClass("mt8");
        return{
             /**
             *返回table对象
             */
            getGrid:function(){
                return _table;
            },
            /**
             *查询数据
             *param 为传入的json格式条件
             */
            doQuery:function(param){
                lazyLoad=false;
                var p={};
                p['header']={'txcode':txcode};
                p['body']=param;
                var api = new jQuery.fn.dataTable.Api(_table.fnSettings());
                _table.fnSettings().ajax.data=p;
                api.ajax.reload();    
            },
            XType:'Grid',
            options:config,
            /**
             * JSON数组
             * @returns 返回所有选中行的行数据，不管是单选还是多选
             */
            getSelected:function(){
                return _selected;
            }
        }
    $.IWAP.XType['Grid']=$.IWAP.Grid;
  }
}(jQuery))
