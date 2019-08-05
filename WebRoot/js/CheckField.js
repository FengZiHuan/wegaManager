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
   //  allowChecked	是否必须选择	默认true  不是必选
   //  Width	宽度
      disabled	是否禁用	默认false
      Value	值
   //  nullText	空文本提示
   //  MinLength	最小长度
  //   MaxLength	最大长度
    // validator	字段校验函数
   //  validateOnBlur	是否失去焦点校验	默认true
  //   validatorText	非法文本提示
     Listeners	监听事件
     Label	文本
     //name 名称
     id 标识 如果设置并且在dom中存在则不去创建对象，只是渲染
     readonly	是否只读	False
     renderTo	渲染对象	Dom对象或者domID
     checked    是否禁用    默认false
     */
    $.IWAP.CheckField=function(config) {

        var genId = config['id'] || $.IWAP.id();
        var readonly = (typeof(config['readonly'])=='undefined')?false:config['readonly'];
        var renderTo = null;
        var name=config['name']||$.IWAP.id()+"name";
        var value = config['value'] || '';
        var events=config['listeners']||[];
        var items=config['items']||[];
        var disabled=(typeof (config['disabled'])=='undefined')?false:config['disabled'];
        var hidden=(typeof (config['hidden'])=='undefined')?false:config['hidden'];
        var tipsOn = config['tipsOn'] || '';
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
         * <div class="checkboxbox pr">
         <input name="2" type="checkbox" value="11" class="checkbox_text" />
         <span class="err_status" style="display:none;">*</span>
         <div class="sm_tooltip_r"></div>
         <div class="tooltip right"></div>
         <div class="tooltip_arrow_r">单选框未选中，请选择</div>
         </div>
         */
        if (config['id']) {
            html.push('<div class="checkboxbox pr" id="ctx_'+genId+'">');

            html.push('<span></span>');
            //$('#'+genId).appendTo($('#ctx_'+genId));
            $(renderTo).append(html.join(''));
            $('#'+genId).attr('xtype','CheckField');
            $('#'+genId).parent().appendTo($('#ctx_'+genId));
        }else{
            html.push('<div class="checkboxbox pr" id="ctx_'+genId+'">');
            for(var p in items){
                var c= (items[p]['checked']==undefined)?false:items[p]['checked'];
                if(c){
                    html.push('<label><input  type="checkbox"  name="'+name+'"   checked='+c+'  value="'+items[p]['value']+'"   />'+items[p]['label']+'</label>');
                }
                else{
                    html.push('<label><input  type="checkbox"  name="'+name+'"     value="'+items[p]['value']+'"   />'+items[p]['label']+'</label>');
                }
            }

            $(renderTo).append(html.join(''));
        }


        /*点击事件*/
        var onCheck= function (e) {
                 var  checkbox = $('#'+this.getId()).find("input[type=checkbox]"),
                      flag  = checkbox.is(":checked");
                 var t=  this.isChecked();
                     if( !flag ){
                           checkbox.prop("checked",true);
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
         //设置禁用或只读控件
        if(disabled||readonly){
             if($("#ctx_"+genId).val()==''){
                      $("#ctx_"+genId).find('input:checkbox').attr("disabled","true");
                 }else{
                      $("#"+genId).find('input:checkbox').attr("disabled","true");
                 }
        }
        //隐藏控件
        if(hidden){
             if($("#ctx_"+genId).val()==''){
                      $("#ctx_"+genId).find('input:checkbox').hide();
                      $("#ctx_"+genId).find('input:checkbox').closest('label').hide();
                 }else{
                      $("#"+genId).find('input:checkbox').hide();
                     $("#"+genId).find('input:checkbox').closest('label').hide();
                 }
        }
        /*返回对象*/
        return {
            XType:'CheckField',
            options:config,

           /*
            *user:CheckField.isChecked()
            *判断控件是否选中
            *return {*}
             * */
            isChecked:function(){
                    var  checkbox = $('#'+this.getId()).find("input[type=checkbox]"),
                    flag  = checkbox.is(":checked");
                    return flag;
            },
             /*
             *user:CheckField.setValue(val)
             *param val 选中radio值
             *设置选中radio值
             * */
            setValue:function(val){
                if($("#ctx_"+genId).val()==''){
                   $("#ctx_"+genId).find('input:checkbox:checked').each(function(){
                                $(this).val(val) ;
                     });
                }else{
                         $("#"+genId).find('input:checkbox:checked').each(function(){
                             $(this).val(val) ;
                        });
                }

            },
            /*
             *user:CheckField.getValue()
             *获取控件的值
             *return {*}
             * */
            getValue:function(){
                var str='';
                if($("#ctx_"+genId).val()==''){
                    $("#ctx_"+genId).find('input:checkbox:checked').each(function(){
                              str += $(this).val()+",";
                     });
                    return str;
                }else{
                    $("#"+genId).find('input:checkbox:checked').each(function(){
                            str += $(this).val()+",";
                        });
                        return str;
                }

            },
              /*
             *user:CheckField.disabled()
             *禁用控件
             * */
            disabled:function(){
                 if($("#ctx_"+genId).val()==''){
                      $("#ctx_"+genId).find('input:checkbox').attr("disabled","true");
                 }else{
                      $("#"+genId).find('input:checkbox').attr("disabled","true");
                 }
            },
            /*
             *user:CheckField.hidden()
             *隐藏控件
             * */
            hidden:function(){
                 if($("#ctx_"+genId).val()==''){
                      $("#ctx_"+genId).find('input:checkbox').hide();
                      $("#ctx_"+genId).find('input:checkbox').closest('label').hide();
                 }else{
                      $("#"+genId).find('input:checkbox').hide();
                     $("#"+genId).find('input:checkbox').closest('label').hide();
                 }
            },
             /*
             *user:CheckField.enabled()
             *不禁用控件或启用控件
             * */
            enabled:function(){
                 if($("#ctx_"+genId).val()==''){
                      $("#ctx_"+genId).find('input:checkbox').removeAttr("disabled","true");
                 }else{
                      $("#"+genId).find('input:checkbox').removeAttr("disabled","true");
                 }

            },
            /*
             *user:CheckField.getId()
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
    $.IWAP.XType['CheckField']=$.IWAP.CheckField;
}(jQuery))