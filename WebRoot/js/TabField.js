/**
 * Created by zzm   on 2015-6-5 14:36:50
 * 选项卡控件
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
     label	文本	标题
     disabled	是否禁用	默认false
     items	选项卡数据
     dblClickToClose	双击关闭 false
     listeners	监听事件
     contextmenu	是否有右键菜单 false
     renderTo	渲染对象	Dom对象或者domID
     */
    $.IWAP.TabField=function(config) {
        var disabled = config['disabled']||false;
        var genId = config['id'] ||$.IWAP.id();
        var items = config['items'] ||[];
        var dblClickToClose = config['dblClickToClose'] || false;
        var listeners = config['listeners'] || [];
        var contextmenu = config['contextmenu'] || false;
        var notClose = config['notClose']||false;
        var hidden=config['hidden']||false;
        var renderTo = null;
        var isAdd=config['isAdd']||false;     //判断items数据是通过addTabItem增加还是初始化话items，初始化isAdd为false，isAdd为true为通过addTabItem方法增加选项卡
        if (config['renderTo']) {
            if ($.IWAP.isString(config['renderTo'])) {
                renderTo = $("#" + config['renderTo']);
            } else {
                renderTo = config['renderTo'];
            }
        } else {//如果不设置渲染对象则默认为body
            renderTo = document.body;
        }
        var Ihtml = '<i>×</i>';
        var Ihtmlhide = '<i style="display:none">×</i>';
        var html = [];
        /**if(config['isAdd']){
            genId = config['renderTo'];
        }*/
        /**
         *  <div role="tabpanel">

         <!-- Nav tabs -->
         <ul class="nav nav-tabs" role="tablist">
         <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab">Home</a></li>
         <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">Profile</a></li>
         <li role="presentation"><a href="#messages" aria-controls="messages" role="tab" data-toggle="tab">Messages</a></li>
         <li role="presentation"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab">Settings</a></li>
         </ul>

         <!-- Tab panes -->
         <div class="tab-content">
         <div role="tabpanel" class="tab-pane active" id="home">.home..</div>
         <div role="tabpanel" class="tab-pane" id="profile">profile</div>
         <div role="tabpanel" class="tab-pane" id="messages">message</div>
         <div role="tabpanel" class="tab-pane" id="settings">setting</div>
         </div>

         </div>
         */
            /*
             *增加选项卡  addSelect(obj)
             *@param items 选项卡初始化数据
             */
            function addSelect(items){
                var hrefid=genId+'-'+items["id"];
                var ul="#"+items['renderTo'];
                ul+=">ul"
                var div="#"+items['renderTo'];
                div+=">div";
                var event="click";

                var beforeSelectTabItem = items['listeners']['beforeSelectTabItem']||IWAP.emptyFn;
                var afterSelectTabItem = items['listeners']['afterSelectTabItem']||IWAP.emptyFn;
                var li=$('<li id="'+genId+'" role="presentation" class="'+hrefid+'" ><a href="#'+hrefid+'" aria-controls="'+hrefid+'"  xtype="TabField" role="tab" data-toggle="tab">'+items["label"]+'</a></li>');
                $(ul).append(li);
                if(items['dbclick']==true){
                    $('#'+genId).on('dblclick', function() {
                        if ($(this).closest('li').siblings().length<=1) {
                            alert('不能删除唯一的Tab页');
                            return false;
                        }else{
                            if (notClose==false) {
                                $(this).closest('li').remove();
                                $('.tab-content').find('.active').remove();
                                $('.tab-content>div:first').addClass('active');
                                activeOne();
                            }else{
                                return
                            }     
                        }
                    });
                }
                $('#'+genId).on('show.bs.tab', beforeSelectTabItem);
                $('#'+genId).on('shown.bs.tab', afterSelectTabItem);
                 $('#'+genId).click(function() {
                    if ($(this).find('i').length<=0) {
                        if (notClose==false) {
                            $(this).append(Ihtml).siblings().children('i').remove();
                            $(this).siblings().children('.Rclick_menu').remove();
                        }else{
                            $(this).append(Ihtmlhide).siblings().children('i').remove();
                            $(this).siblings().children('.Rclick_menu').remove();
                        }
                    }
                });
                 //右键菜单和自定义菜单
                var R_menu = '<div class="Rclick_menu"><a id="closeON">关闭</a><a id="closeAll">关闭其它</a></div>';
                if(items['contextmenu']==true){
                    $('#'+genId).on('contextmenu',function(e){
                        //选中的TAB才能追加自定义菜单
                        if ($(this).find('i').length>0 && $(this).find('.Rclick_menu').length<=0 && notClose==false) {
                            $(this).append(R_menu);
                        };
                        //右键时把上面隐藏了的Rclick_menu自定义菜单再展示出来
                        $(this).find('.Rclick_menu').show();
                        return false;
                     });
                }

                //右键菜单和自定义菜单点击事件
                $('#'+genId).on('click', '#closeAll',function() {
                    var ThisSiblings = $(this).closest('li').siblings();
                    ThisSiblings.remove();
                    $('.Rclick_menu').remove();
                    return false;
                });
                $('#'+genId).on('click', '#closeON',function() {
                    if ($(this).closest('li').siblings().length<1) {
                        alert('不能删除唯一的Tab页');
                        $('.Rclick_menu').remove();
                        return false;
                    }else{
                        $(this).closest('li').remove();
                        $('.Rclick_menu').remove();
                        $('.tab-content').find('.active').remove();
                        $('.tab-content>div:first').addClass('active');
                        activeOne();
                        return false;
                    }
                });
                //选项卡的右上角的关闭点击事件
                $('#'+genId).on('click', 'i', function() {
                    if ($(this).closest('li').siblings().length<1) {
                        alert('不能删除唯一的Tab页');
                        return false;
                    }else{
                        $(this).closest('li').remove();
                        $('.tab-content').find('.active').remove();
                        $('.tab-content>div:first').addClass('active');
                        activeOne();
                    }

                });
                //隐藏选项卡
                if(items["hidden"]==true){
                    $('#'+genId).hide();
                };
                //禁用选项卡
                if(items["disabled"]==true){
                    $('#'+genId).unbind('click');
                    $('#'+genId).unbind('dblclick');
                    $('#'+genId+ ' a').attr('data-toggle','');
                    $('#'+genId).addClass('disable');
                }
                    $(div).append('<div role="tabpanel" class="tab-pane" id="'+hrefid+'">'+items["html"]+'</div>');
            }
            //选择第一个选项卡被激活
            function activeOne(){
                $('.nav-tabs li:first').addClass('active');
                if ($('.nav-tabs li:first').find('i').length<=0) {
                    //判断关闭铵钮是否显示
                    if (notClose==false) {
                        $('.nav-tabs li:first').append(Ihtml).siblings().children('i').remove();
                    }else{
                        $('.nav-tabs li:first').append(Ihtmlhide).siblings().children('i').remove();
                    }
                };
            }
            //第一次初始化时候生成div
            if(!config['isAdd']){
                html.push('<div role="tabpanel" id="'+genId+'">');
                html.push(' <ul class="nav nav-tabs" role="tablist">');
                html.push(' </ul>');
                html.push('<div class="tab-content clearfix" id="'+genId+'-tab">');
                html.push('</div>');
                $(renderTo).append(html.join(''));
                for(var p in items){
                    items[p]['renderTo']=genId;
                    items[p]['xtype']="TabField";
                    items[p]['isAdd']=true;
                    items[p]['contextmenu']=config['contextmenu'];
                    items[p]['dbclick']=config['dblClickToClose'];
                    items[p]['listeners']=config['listeners'];
                    items[p]['disabled']=config['disabled'];
                    $.IWAP.XType[items[p]['xtype']].apply(this,[items[p]]);
                }

            }else{
                addSelect(config);
            }



          $('.nav-tabs a:first').tab('show');
          //$('.nav-tabs li').append('<i>×</i>');
          if ($('.nav-tabs li:first').find('i').length<=0) {
            //判断关闭铵钮是否显示
            if (notClose==false) {
                $('.nav-tabs li:first').append(Ihtml).siblings().children('i').remove();
            }else{
                $('.nav-tabs li:first').append(Ihtmlhide).siblings().children('i').remove();
            }
          };

           var afterAddTabItem=listeners['afterAddTabItem']||IWAP.emptyFn;
           var beforeAddTabItem= listeners['beforeAddTabItem']||IWAP.emptyFn;
           var beforeRemoveTabItem=listeners['beforeRemoveTabItem']||IWAP.emptyFn;
           var afterRemoveTabItem=listeners['afterRemoveTabItem']||IWAP.emptyFn;

           //点击空白位置，隐藏右键菜单
            $('html').mouseup(function() {
                $('.Rclick_menu').hide();
            });
            /*返回对象*/
            return {
            XType:'TabField',
            options:config,
            /*
             user:TabField.isTabItemExist(tabid)
             判断tabid选项卡是否存在
             @param tabid  选项卡id
             @return {*}
            */
            isTabItemExist:function(tabid){
                if($('#'+tabid).length>0){
                    return true;
                }
                return false;
            },
            /*
             user:TabField.selectTabItem(tabid)
             选中id=tabid的选项卡
             @param tabid  选项卡id
            */
            selectTabItem:function(tabid){
                $('#'+tabid).tab('show');
            }  ,
            /*
             user:TabField.getTabItemCount()
             获取选项卡的个数
             @return {*}
             */
            getTabItemCount:function(){
              return  $('#'+genId+ ' li').length;
            },
            /*
             user:TabField.removeSelectedTabItem()
             移除选中选项卡
             */
            removeSelectedTabItem:function(){
                $('#'+genId+' .active').remove()
                $('#'+genId+' a:first').tab('show');
            },
            /*
             user:TabField.getSelectedTabItemID()
             获取选中选项卡的id
             @return {*}
             */
            getSelectedTabItemID:function(){
              var sid=  $('#'+genId+'  li.active ').attr('id');
              return sid;
            },
            /*
             user:TabField.removeTabItem(tabid)
             移除指定（tabid）选项卡
             @param tabid 选项卡id
             */
            removeTabItem:function(tabid){
                beforeRemoveTabItem(this,tabid);
                $('#'+tabid).remove();
                $('#'+genId+' a:first').tab('show');
                afterRemoveTabItem(this,tabid);
            },
            /*
             user:TabField.addTabItem(items1)
             增加选项卡
             @param items1
             */
            addTabItem:function(items1){
                  beforeAddTabItem(this,items1) ;
                   $.each(items1,function(i,item){
                    $.IWAP.applyIf(item,{xtype:'TabField'});
                    item['isAdd']=true;
                    item['renderTo']=genId;
                    item['contextmenu']=config['contextmenu'];
                    item['dbclick']=config['dblClickToClose'];
                    item['listeners']=config['listeners'];
                   var vv= $.IWAP.XType[item['xtype']].apply(this,[item]);
                });
                 afterAddTabItem(this,items1);
            },
            /*
             user:TabField.getId()
             获取选项卡id
             @return {*}
             */
            getId:function(){
                return genId;
            }
        }
    }
    $.IWAP.XType['TabField']=$.IWAP.TabField;
}(jQuery))