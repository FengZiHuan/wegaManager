/**
 * Created by zzm on 2015-5-20 14:04:11
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
      disabled	是否禁用	默认false
      Value	值
      listeners	监听事件
      label	文本
      id 标识 如果设置并且在dom中存在则不去创建对象，只是渲染
      readonly	是否只读	False
      renderTo	渲染对象	Dom对象或者domID
      checked    是否禁用    默认false
     */
    $.IWAP.RadioField=function(config) {

        var genId = config['id'] || $.IWAP.id();
        var readonly = (typeof(config['readonly'])=='undefined')?false:config['readonly'];
        var label = config['label'] || "";
        var checked = config['checked'] || true;
        var renderTo = null;
        var name=config['name']||$.IWAP.id()+"name";
        var value = config['value'] || '';
        var events=config['listeners']||[];
        var items=config['items']||[];
        var disabled=(typeof (config['disabled'])=='undefined')?false:config['disabled'];
        var hidden=(typeof (config['hidden'])=='undefined')?false:config['hidden'];
       var obj=eval(items);
        if (config['renderTo']) {
            if ($.IWAP.isString(config['renderTo'])) {
                renderTo = $("#" + config['renderTo']);
            } else {
                renderTo = config['renderTo'];
            }
        } else {//如果不设置渲染对象则默认为body
            renderTo = document.body;
        }
        var html = [];
        /**
         * <div class="radiobox pr">
         <input name="2" type="radio" value="11" class="radio_text" />
         <span class="err_status" style="display:none;">*</span>
         <div class="sm_tooltip_r"></div>
         <div class="tooltip right"></div>
         <div class="tooltip_arrow_r">单选框未选中，请选择</div>
         </div>
         */
        if(!config["id"]) {
            html.push('<div class="radiobox pr" id="ctx_' + genId + '">');
            for (var p in items) {
                var c = (items[p]['checked'] == undefined || items[p]['checked'] == false) ? false : true;
                if (c) {
                    html.push('<label><input  type="radio"  xtype="RadioField" name="' + name + '"   checked=' + c + '  value="' + items[p]['value'] + '"   />' + items[p]['label'] + '</label>');
                } else {
                    html.push('<label><input  type="radio"  xtype="RadioField"  name="' + name + '"     value="' + items[p]['value'] + '"   />' + items[p]['label'] + '</label>');
                }
            }
            $(renderTo).append(html.join(''));
            //判断控件是否禁用和只读
            if (disabled || readonly) {
                if ($("#ctx_" + genId).val() == '') {
                    $("#ctx_" + genId).find('input:radio').attr("disabled", "true");
                } else {
                    $("#" + genId).find('input:radio').attr("disabled", "true");
                }
            }
        }
        //判断控件是否隐藏
        if(hidden){
            if($("#ctx_"+genId).val()==''){
                $("#ctx_"+genId).find('input:radio').hide();
                $("#ctx_"+genId).find('input:radio').closest('label').hide();
            }else{
                $("#"+genId).find('input:radio').hide();
                $("#"+genId).find('input:radio').closest('label').hide();
            }
        }

        /*点击事件*/
        var onCheck= function (e) {
                     var radio = $('#'+this.getId()).find("input[type=radio]"),
                     flag  = radio.is(":checked");
                     var t=  this.isChecked();
                     if( !flag ){
                           radio.prop("checked",true);
                    }
        }
         /*绑定事件*/
        if(events['onCheck']){
                 if($("#ctx_"+genId).val()==''){
                      $("#ctx_"+genId).on("click",events['onCheck']);
                 }else{
                      $("#"+genId).on("click",events['onCheck']);
                 }
            }


        /*返回对象*/
        return {
            XType:'RadioField',
            options:config,
            /*
            *user:RadioField.isChecked()
            *判断控件是否选中
            *return {*}
             * */
            isChecked:function(){
                    var id=this.getId();
                    var  radio = $('#'+id).find("input[type=radio]") ,
                    flag  = radio.is(":checked");
                    return flag;
            },
            /*
             *user:RadioField.setValue(val)
             *param val 选中radio值
             *设置选中radio值
             * */
            setValue:function(val){
                if($("#ctx_"+genId).val()==''){
                    $("#ctx_"+genId).find('input:radio:checked').val(val);
                }else{
                        $("#"+genId).find('input:radio:checked').val(val);
                }

            },
            /*
             *user:RadioField.getValue()
             *获取控件的值
             *return {*}
             * */
            getValue:function(){
                if($("#ctx_"+genId).val()==''){
                     return $("#ctx_"+genId).find('input:radio:checked').val();
                }else{
                    return $("#"+genId).find('input:radio:checked').val();
                }

            },
            /*
             *user:RadioField.disabled()
             *禁用控件
             * */
            disabled:function(){
                if($("#ctx_"+genId).val()==''){
                    $("#ctx_"+genId).find('input:radio').attr("disabled","true");
                }else{
                    $("#"+genId).find('input:radio').attr("disabled","true");
                }
            },
            /*
             *user:RadioField.hidden()
             *隐藏控件
             * */
            hidden:function(){
                if($("#ctx_"+genId).val()==''){
                    $("#ctx_"+genId).find('input:radio').hide();
                    $("#ctx_"+genId).find('input:radio').closest('label').hide();
                }else{
                    $("#"+genId).find('input:radio').hide();
                    $("#"+genId).find('input:radio').closest('label').hide();
                }
            },
            /*
             *user:RadioField.enabled()
             *不禁用控件或启用控件
             * */
            enabled:function(){
                if($("#ctx_"+genId).val()==''){
                    $("#ctx_"+genId).find('input:radio').removeAttr("disabled","true");
                }else{
                    $("#"+genId).find('input:radio').removeAttr("disabled","true");
                }

            },
            /*
             *user:RadioField.getId()
             *获取控件id
             * */
            getId:function(){
                if($("#ctx_"+genId).val()==''){
                     return "ctx_"+genId;
                }else{
                   return genId;
                }

            }
        }
    }
    $.IWAP.XType['RadioField']=$.IWAP.RadioField;
}(jQuery))