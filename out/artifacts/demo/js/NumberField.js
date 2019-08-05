/**
 * Created by Jackie on 2015/5/18 0018.
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
     allowBlank 是否允许为空  默认true
     width  宽度
     disabled   是否禁用    默认false
     value  值
     nullText   空文本提示
     minNumber	最小数字
     maxNumber	最大数字
     validator  字段校验函数  返回boolean值
     validateOnBlur 是否失去焦点校验    默认true
     validatorText 非法文本提示
     listeners  监听事件
     label  文本
     id 标识 给定指定的id值而不是使用生成的id值
     readonly   是否只读    默认false
     renderTo   渲染对象    Dom对象或者domID
     */
    $.IWAP.NumberField=function(config) {
        var allowBlank = typeof(config['allowBlank'])=='undefined'?true:config['allowBlank'];
        var width = config['width'];
        var disabled=config['disabled']||false;
        var hidden=config['hidden']||false;
        var nullText=config['nullText']||'';
        var genId = config['id'] || $.IWAP.id();
        var readonly = config['readonly'] == true;
        var label = config['label'] || "";
        var tipText = config['validatorText'] || "输入无效,请重新输入";
        var validBlur = (typeof(config['validateOnBlur'])=='undefined')?true:config['validateOnBlur'];
        var renderTo = null;
        var value = config['value'] || '';
        var events=config['listeners']||[];
        var beforeChangeValue=null;
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
        if(!config["id"]) {
            html.push('<div class="inputbox pr">');
            html.push('<span>' + label + ':</span>&nbsp;&nbsp;<input type="text"  placeholder="' + nullText + '"  xtype="NumberField" id="' + genId + '" class="input_text" style="width:' + width + 'px"' + ' value="' + value + '"/>');
            if (!allowBlank) {
                html.push('<i class="err_status" id=err_' + genId + '">*</i>');
            }
            html.push('<div class="sm_tooltip_r" id="sm_' + genId + '"></div>');
            html.push('<div class="tooltip right" id="right_' + genId + '"></div>');
            html.push('<div class="tooltip_arrow_r" id="err_tip_' + genId + '">' + tipText + '</div></div>');
            $(renderTo).append(html.join(""));
        }
        $("#err_tip_"+genId).hide();
        $("#sm_"+genId).hide();
        $("#right_"+genId).hide();
        $("#err_tip_"+genId).hide();
         /**
        * 非法格式提醒
        */
        var valid=function(){
            $("#sm_"+genId).show();
            $("#right_"+genId).show();
            $("#err_tip_"+genId).show();
            $("#"+genId).removeClass("input_text");
            $("#"+genId).addClass("input_text_valid");
        }
        /**
        * 清除非法格式提醒
        */
        var clearStyle=function(){
            $("#sm_"+genId).hide();
            $("#right_"+genId).hide();
            $("#err_tip_"+genId).hide();
            $("#"+genId).removeClass("input_text_valid");
            $("#"+genId).addClass("input_text");
        }

        /**
        *校验是否是数字的函数
        */
         var isNumber=function(str){
            var reg=/^\d+$/ ;
            return reg.test(str);
        }
        /**
        *失去焦点的校验函数
        */
        var validatorFunc=function(){//input_text_valid
            if(events['blur']){
                events['blur'].apply(events['blur'],arguments);
            }
            /**
            *如果配置了字段校验函数，则执行配置的，否则执行默认的校验函数
            */
            if(config['validator']){
                var flag=config['validator'].apply(config['validator'],arguments);
                if(!flag){
                    valid();
                }else{
                    clearStyle();
                }
                return flag;
            }else{
                if(allowBlank){
                        if($("#"+genId).val()){
                             if(!isNumber($("#"+genId).val())){
                                 valid();
                                return false;
                            }
                            if(config['minNumber']){
                                  if(parseFloat($("#"+genId).val())<parseFloat(config['minNumber'])){
                                    valid();
                                    return false;
                                }
                            }
                            if(config['maxNumber']){
                                 if(parseFloat($("#"+genId).val())>parseFloat(config['maxNumber'])){
                                    valid();
                                    return false;
                                }
                            }
                            clearStyle();
                            return true;
                        } 
                    }else{
                            if(!isNumber($("#"+genId).val())){
                                valid();
                                return false;
                            }
                            if(!$("#"+genId).val()){
                                valid();
                                return false;
                            }
                            if(config['minNumber']){
                                  if(parseFloat($("#"+genId).val())<parseFloat(config['minNumber'])){
                                    valid();
                                    return false;
                                }
                            }
                            if(config['maxNumber']){
                                 if(parseFloat($("#"+genId).val())>parseFloat(config['maxNumber'])){
                                    valid();
                                    return false;
                                }
                            }
                            clearStyle();
                            return true;
                        }
            }

        }
        /*校验函数*/

        //onchange事件调用方法
         var onChange=function(){
              events['changeValue'].apply(events['changeValue'],[beforeChangeValue]);
          }

        if(validBlur){
            $("#"+genId).on("blur",validatorFunc);
            $("#"+genId).on("focus",function(){
                clearStyle();
                beforeChangeValue=this.value;
                if(events['focus']){
                    events['focus'].apply(events['focus'],arguments);
                }
            });
        }else{
            if(events['focus']){
                beforeChangeValue=this.value;
                $("#"+genId).on("focus",events['focus']);
            }
            if(events['blur']){
                $("#"+genId).on("blur",events['blur']);
            }
        }
        if(events['changeValue']){
            $("#"+genId).on("change",onChange);
        }
        if(disabled){
            $("#"+genId).attr("disabled",true);
            $('#'+genId).css('background','#eeeeee')
        }
        if(hidden){
            $("#"+genId).hide();
        }
        if (readonly) {
             $("#"+genId).attr("readonly",true);
        };
        /*返回对象*/
        return {
            XType:'NumberField',
            options:config,
            disabled:disabled,
            hidden:hidden,
            /**
             * 清除非法提醒格式
             */
            clearInvalid:function(){
                clearStyle();
            },
            /**
             * 校验输入框
             */
            validate:function(){
                validatorFunc.apply(validatorFunc,[]);
            },
            getValue:function(){
                $("#"+genId).val();
            },
            setValue:function(val){
                return $("#"+genId).val(val);
            },
            disabled:function(){
                $("#" + genId).attr('disabled',true);
                $('#'+genId).css('background','#eeeeee')
            },
            hidden:function(){
                $("#"+genId).hide();
            },
            enabled:function(){
                $("#" + genId).removeAttr('disabled');
                $('#'+genId).css('background','')
            },
            getId:function(){
                return genId;
            }
        }
    }
    $.IWAP.XType['NumberField']=$.IWAP.NumberField;
}(jQuery))