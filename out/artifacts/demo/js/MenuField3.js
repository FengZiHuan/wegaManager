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
     Label	文本
     allowBlank	是否允许为空	默认true
     disabled	是否禁用	默认false
     data 下拉数据 modal为local时生效
     Listeners	监听事件
     readonly	是否只读	False
     mode 数据来源 local/server
     Value	默认值
     url 请求url Mode 为server时有效
     baseParam 过滤参数 Mode 为server时有效
     displayValue 显示值字段
     UseValue 使用值字段
     isMultiSelect 是否多选 默认false
     renderTo	渲染对象	Dom对象或者domID


     name 名称
     id 标识 如果设置并且在dom中存在则不去创建对象，只是渲染


     */
    $.IWAP.menuField2=function(config) {
        var allowBlank = config['allowBlank'] || '';
        var genId = config['id'] || $.IWAP.id();
        var readonly = config['readonly'] == true;
        var disabled=config['disabled'] == true;
        var hidden=config['hidden'] == true;
        var label = config['label'] || "";
        var mode=config['mode'];
        var data=config['data'];
        var renderTo = null;
        var value = config['value'] || '';
        var listeners=config['listeners']||[];
        var displayValue=config['displayValue'];
        var useValue=config['useValue'];
        var baseParam=config['baseParam']||{};
        var url=config['url']||'';//设置AJAX的url
        var isMultiSelect=config['isMultiSelect']||'';//多选单选下拉
        var setValue=config['setValue']||'';//设置选中值
        var name=config['name']||'name';
        var children=config['children']||'children';
        var indId=config['idKey']||'id';
        if (config['renderTo']) {
            if ($.IWAP.isString(config['renderTo'])) {
                renderTo = $("#" + config['renderTo']);
            } else {
                renderTo = config['renderTo'];
            }
        } else {//如果不设置渲染对象则默认为body
            renderTo = document.body;
        }
        var menuData=[];
        var renderHtml=function(data){
        	for (var i = 0; typeof (data)==='object' && i < data.length; i++) {
                temp.push('<li><a href="javascript:void(0)">'+data[i][name]+'<i></i></a>');
                menuData.push(data[i]);
                //2级//使用typeof是因为需要判断循环的对象是一个对象非空时才循环
                temp.push('<ul>')
                for (var n = 0; typeof (data[i][children])==='object' && n < data[i][children].length; n++) {
                    temp.push('<li><a href="javascript:void(0)">'+data[i][children][n][name]+'<i></i></a>');
                    menuData.push(data[i][children][n]);
                    //3级
                    temp.push('<ul>');
                    for (var h = 0; typeof (data[i][children][n][children])==='object' && h < data[i][children][n][children].length; h++) {
                        temp.push('<li><a href="javascript:void(0)">'+data[i][children][n][children][h][name]+'<i></i></a>');
                        menuData.push(data[i][children][n][children][h]);
                        //4级
                        temp.push('<ul>');
                        for (var j = 0; typeof (data[i][children][n][children][h][children])==='object' && j < data[i][children][n][children][h][children].length; j++) {
                            temp.push('<li><a href="javascript:void(0)">'+data[i][children][n][children][h][children][j][name]+'<i></i></a>');
                            menuData.push(data[i][children][n][children][h][children][j]);
                        }
                        temp.push('</ul></li>');
                    }
                    temp.push('</ul></li>')
                };
                temp.push('</ul>')
            };

            temp.push('</li>');
            
            //菜单HTML代码添加结束
            $('#'+genId).find('ul').append(temp.join(''));
            $.each($('#'+genId).find('a'),function(i,v){
            	$(v)[0].data=menuData[i];
            	$(v).click(config['click']||function(data){});
            });
            
        }
        var html = [];
       
        //菜单HTML代码添加开始
        html.push('<div class="menubox" id="'+genId+'"><ul>');
        html.push('</ul></div>');
        
        //写入代码
        $(renderTo).append(html.join(''));

        //判断是本地数据还是服务端数据
        if(mode=='local') {
            var temp = []
            //1级
            renderHtml(data);
        }
        else if(mode=='server') {
            if(listeners['beforeLoad']){
                listeners['beforeLoad'].apply(listeners['beforeLoad'],arguments);
            }
            query(baseParam);
            //query();
        };
              
        
        //server的AJAX数据
        function query(querydata){
            $.ajax({
                url: config.url,
                type: 'get',
                data: querydata,
                dataType: 'json',
                success: function success(resdata) {
                    var temp = []
                    renderHtml(resdata);
                    //模拟下拉等各项点击和选择事件-AJAX数据
                    //start();
                    //setfunction();
                }
            });
            
        };

        //配置项函数
        /*是否禁用*/
        if(disabled){
            $("#" + genId).attr('disabled',true);
            $('#'+genId).css('background','#eeeeee');
        }
        if(hidden){
            $("#" + genId).hide();
        }
        /*是否只读*/
        if (readonly){
            $("#" + genId).attr('readonly',true);
            //点击input时展开下拉盒子
            $("#" + genId).click(function() {
                $(this).next('ul').remove();
            });
        };
        if(listeners){
            $("#"+genId).on("click",listeners.expend);
        };
        //设置选中值
        if (setValue) {
            $("#"+genId).next("ul").find('li').each(function() {
                if ($(this).text()==setValue) {
                    var _datavalue = $(this).attr('data-value');
                    $("#"+genId).val(setValue);
                    $("#"+genId).attr('data-value', _datavalue);
                    $("#"+genId).attr('title', setValue);
                };
                
            });
        };
      
            

        /*返回对象*/
        return {
            XType:'MenuField',
            options:config,
            disabled:disabled,
            hidden:hidden,
            /**
             * 校验输入框
             */
            /*getText:function(id){
                var nodes = treeObj.getNodesByParam("id", id, null);
                var res ;
                 for(var i=0;i<nodes.length;i++){
                     res+=nodes[i]["name"];
                 }
                 return res;
            },*/
            //
            setText:function(column,Ftext,text){
                var inNumber=0;
                var setInF = setInterval(function(){
                    var c1 = $("#"+genId+">ul>li>a");
                    var c2 = $("#"+genId+">ul>li>ul>li>a");
                    var c3 = $("#"+genId+">ul>li>>ul>li>ul>li>a");
                    var c4 = $("#"+genId+">ul>li>>ul>li>ul>li>ul>li>a");
                    console.log(inNumber)
                    if (c1.length==0) {
                        inNumber++
                        if (inNumber>800) {
                            clearInterval(setInF);
                            alert('找不到要设置的数据！');
                        };
                        return;
                    };
                    clearInterval(setInF);
                    switch(column){
                        case 1:
                            $(c1).each(function() {
                                 if ($(this).text()==Ftext) {
                                    $(this).text(text)
                                 };
                            });
                        case 2:
                            $(c2).each(function() {
                                 if ($(this).text()==Ftext) {
                                    $(this).text(text)
                                 };
                            });
                        case 3:
                            $(c3).each(function() {
                                 if ($(this).text()==Ftext) {
                                    $(this).text(text)
                                 };
                            });
                        case 4:
                            $(c4).each(function() {
                                 if ($(this).text()==text) {
                                    $(this).text(Ftext)
                                 };
                            });

                    }
                },10);
    
            },          
            disabled:function(){
                $("#" + genId).attr('disabled',true);
            },
            hidden:function(){
                $("#" + genId).hide();
            },
            enabled:function(){
                $("#" + genId).removeAttr('disabled');
            },
            getId:function(){
                return genId;
            },
            load:function(data,append) {
                /*if (mode=='server'&& append) {
                    query(baseParam);
                };*/
                if (!append) {
                    $("#" + genId).empty();
                } 
                for (var i = 0; i < data.length; i++) {
                       $(ul).append('<li data-value="'+data[i][useValue]+'">'+data[i][displayValue]+'</li>');
                } 
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


        //模拟下拉等各项点击和选择事件
        function start(){
        
        }
    }
    $.IWAP.XType['MenuField']=$.IWAP.MenuField3;

    
        
}(jQuery))
