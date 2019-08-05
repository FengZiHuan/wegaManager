 
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
    disabled  是否禁用 默认false
    hidden   是否隐藏  默认false
    title 文本 标题
    items 数据 数组格式
    close 关闭动作触发  hidden隐藏对话框/close销毁对话框
    width  宽度
    height  高度
    listeners   监听事件,beforeClose和afterClose
    buttons  按钮组  数组格式
    */
    $.IWAP.Dialog=function(config) {
        var disabled=typeof(config['disabled'])=='undefined'?false:config['disabled'];
        var hidden=typeof(config['hidden'])=='undefined'?false:config['hidden'];
        var genId = config['id']|| $.IWAP.id();
        var title=config['title']||'';
        var content=config['html']||'';
        var height=config['height'];
        var width=config['width'];
        var buttons=config['buttons'];
        var events=config['listeners']||[];
        var close=config['close'];
        var renderTo = null;
        if (config['renderTo']) {
            if ($.IWAP.isString(config['renderTo'])) {
                renderTo = $("#" + config['renderTo']);
            } else {
                renderTo = config['renderTo'];
            }
        } else {
            renderTo = document.body;
        }

        var html=[];
        html.push('<div class="modal" role="dialog" aria-labelledby="myModalLabel" id="'+genId+'">');
        html.push('<div class="modal-dialog">');
        html.push('<div class="modal-content">')
        html.push('<div class="modal-header">');
        html.push('<button type="button" class="close" id="btn_'+genId+'" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>');
        html.push('<h4 class="modal-title">'+title+'</h4>');
        html.push('</div>');
        html.push('<div class="modal-body">');
        html.push(content);
        html.push('</div>');
        html.push('<div class="modal-footer" id="footer_'+genId+'">');
        html.push('</div>');
        html.push('</div></div></div>');
        $(renderTo).append(html.join(""));
        if (buttons) {
            for (var i = 0; i < buttons.length; i++) {
                $.IWAP.ButtonField({label:buttons[i],renderTo:'footer_'+genId});
            };
            $('#footer_'+genId+' button').addClass('btn-primary');
        }
        $("#btn_"+genId).click(function(){
           closeModal();
        });
        /*关闭对话框*/
        var closeModal=function(){
            if (events['beforeClose']) {
                events['beforeClose'].apply(events['beforeClose'],arguments);
            };
            $('#'+genId).hide();
            if (events['afterClose']) {
                events['afterClose'].apply(events['afterClose'],arguments);
            };
        }
        /*设置对话框高度*/
        if (height) {
            $('#'+genId).css('height',height);
        };
        /*设置对话框宽度*/
        if (width) {
            $('#'+genId).css('width',width);
        };
        /*是否隐藏对话框*/
        if (hidden) {
            $('#'+genId).hide();
        };
        /*是否停用对话框*/
        if (disabled){
            $('#'+genId).append('<div class="disabled_bg" id="cover_'+genId+'"></div>');
        };
        /*hidden隐藏对话框/close销毁对话框*/
        if (close) {
            if (close=='hidden') {
                $('#'+genId).hide();
            };
            if (close=='close') {
                $('#'+genId).remove();
            };
        };
        /**
        *移动对话框的处理函数,点击对话框头部可以移动
        */
        $('#'+genId+' .modal-header').on('mousedown',function(event){ 
            var divX=$('#'+genId).position().left;
            var divY=$('#'+genId).position().top;
            $(renderTo)[0].style.cursor = "move";
            var old=event;
            $('#'+genId+' .modal-header').on('mousemove',function(e){
                var left=e.pageX-old.pageX+divX;
                var top=e.pageY-old.pageY+divY;
                $('#'+genId).css({"position": "absolute","top":top,"left":left}); 
            });
        });
        $('body').on('mouseup',function(event){ 
                $('#'+genId+' .modal-header').off('mousemove');
                $(renderTo)[0].style.cursor = "default";
        });

        return  {
            XType:'Dialog',
            disabled:disabled,
            hidden:hidden,
            /*显示对话框*/
            show:function(){
                $('#'+genId).show('normal');
            },
             /*隐藏对话框*/
            hidden:function(){
                $('#'+genId).hide();
            },
            /*设置对话框高度*/
            setHeight:function(value){
                $('#'+genId).css('height',value);
            },
            /*设置对话框宽度*/
            setWidth:function(value){
                $('#'+genId).css('width',value);
            },
            /*设置对话框标题*/
            setTitle:function(value){
                $('#'+genId+' .modal-title').html(value);
            },
            /*关闭对话框*/
            close:function(){
                closeModal();
            },
            /*停用对话框*/
            disabled:function(){
                $('#'+genId).append('<div class="disabled_bg" id="cover_'+genId+'"></div>');
            },
            /*启用对话框*/
            enabled:function(){
                $('#cover_'+genId).removeClass('disabled_bg');
            },
            /*获取对话框的id值*/
            getId:function(){
                return genId;
            }
        }
  }
  $.IWAP.XType['Dialog']=$.IWAP.Dialog;
}(jQuery))
