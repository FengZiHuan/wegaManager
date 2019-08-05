/**
 * Created by Jackie on 2015/5/27 14:04:11
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
     *初始化组件
     * @param config
     disabled	是否禁用	默认false
     Label	文本
     hidden 是否隐藏	默认false
    // id 标识 如果设置并且在dom中存在则不去创建对象，只是渲染
     renderTo	渲染对象	Dom对象或者domID
     click 单击事件
     */
    $.IWAP.ButtonField=function(config) {
        var genId = config['id'] || $.IWAP.id();
        var label = config['label'] || "";
        var renderTo = null;
        //  var name = config['name'] || genId;
        var events=config['click']||[];
        var disabled=config['disabled']||false;
        var hidden=config['hidden']||false;
        if (config['renderTo']) {
            if ($.IWAP.isString(config['renderTo'])) {
                renderTo = $("#" + config['renderTo']);
            } else {
                renderTo = config['renderTo'];
            }
        } else {//�����������Ⱦ������Ĭ��Ϊbody
            renderTo = document.body;
        }
        var html = [];
        /**
         * <div class="radiobox pr">
         <input name="2" type="radio" value="11" class="radio_text" />
         <span class="err_status" style="display:none;">*</span>
         <div class="sm_tooltip_r"></div>
         <div class="tooltip right"></div>
         <div class="tooltip_arrow_r">��ѡ��δѡ�У���ѡ��</div>
         </div>
         */
        if (config['id']) {
            html.push('<div id="ctx_'+genId+'">');
            $('#'+config['id']).append(html.join(''));
            $('#'+genId).html(label);
            $('#'+genId).attr('xtype','ButtonField');
            $('#'+genId).appendTo($('#ctx_'+genId));
        } else{
            html.push('<div id="ctx_'+genId+'" class="buttonbox">');
            html.push('<button  xtype="ButtonField" id="'+genId+'"class="btn btn-primary mr30">'+label+'</button>');
            $(renderTo).append(html.join(""));
        }
        if(events){
                $("#"+genId).on("click",events);
        }
        if(disabled){
           $("#"+genId).attr("disabled","true");
        }
        if(hidden){
                $("#"+genId).hide();
        }

        /*���ض���*/
        return {
            XType:'ButtonField',
            options:config,
            disabled:disabled,
            hidden:hidden,
            setText:function(text){
                    $("#"+genId).html(text);
            },
            getText:function(){
                    return $("#"+genId).html();
            },
            disabled:function(){
                    $("#"+genId).attr('disabled',true);

            },
            hidden:function(){
                    $("#"+genId).hide();
            },
            enabled:function(){
                $("#" + genId).removeAttr('disabled');
            },
            getId:function(){
                    return genId;
            }
        }
    }
    $.IWAP.XType['ButtonField']=$.IWAP.ButtonField;
}(jQuery))
