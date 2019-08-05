/**
 * Created by Jackie on 2015/5/20 0018.
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
     minLength  最小长度
     maxLength  最大长度
     validator  字段校验函数 返回boolean值
     validateOnBlur 是否失去焦点校验    默认true
     validatorText  非法文本提示
     listeners  监听事件
     label  文本
     id 标识 给定指定的id值而不是使用生成的id值
     readonly   是否只读    默认false
     renderTo   渲染对象    Dom对象或者domID
     */
    $.IWAP.EmailField=function(config) {
        var allowBlank = typeof(config['allowBlank'])=='undefined'?true:config['allowBlank'];
        var width = config['width'];
        var genId = config['id'] || $.IWAP.id();
        var disabled=config['disabled']||false;
        var hidden=config['hidden']||false;
        var readonly = config['readonly'] == true;
        var nullText=config['nullText']||'';
        var label = config['label'] || "";
        var tipText = config['validatorText'] || "输入无效,请重新输入";
        var validBlur = (typeof(config['validateOnBlur'])=='undefined')?true:config['validateOnBlur'];
        var renderTo = null;
        var name = config['name'] || genId;
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
        html.push('<div class="inputbox pr">');
        if(!config["id"]){
            html.push('<span>'+label+':</span>&nbsp;&nbsp;<input  type="text" placeholder="'+nullText+'" xtype="EmailField" id="'+genId+'" class="input_text" style="width:'+width+'px;"'+' value="'+value+'"/>');
            html.push('<div class="sm_tooltip_r" id="sm_'+genId+'"></div>');
            html.push('<div class="tooltip right" id="right_'+genId+'"></div>');
            html.push('<div class="tooltip_arrow_r" id="err_tip_'+genId+'">'+tipText+'</div></div>');
            $(renderTo).append(html.join(""));
        }
        if(!allowBlank){
               html.push('<i class="err_status" id＝"err_'+genId+'">*</i>');
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
        *校验是否是邮箱的函数
        */
        var isEmail=function(str){
            var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
            return reg.test(str);
        }

        /*失去焦点的校验函数*/
        var validatorFunc=function(){//input_text_valid
            if(events['blur']){
                events['blur'].apply(events['blur'],arguments);
            }
            /**
            *如果配置了字段校验函数，则执行配置的，否则执行默认的校验函数
            */
            if(config['validator']){
                var flag=config['validator'].apply(config['validator'],arguments);;
                if(!flag){
                    valid();
                }else{
                    clearStyle();
                }
                return flag;
            }else{
                    if(allowBlank){
                        if($("#"+genId).val()){
                            if(config['minLength']){
                                if($("#"+genId).val().trim().length<config['minLength']){
                                    valid();
                                    return false;
                                }
                            }
                            if(config['maxLength']){
                                if($("#"+genId).val().trim().length>config['maxLength']){
                                    valid();
                                    return false;
                                }
                            }
                            if(!isEmail($("#"+genId).val().trim())){
                                 valid();
                                return false;
                            }
                            clearStyle();
                            return true;
                        }   
                    }else{
                            if(!$("#"+genId).val()){
                                valid();
                                return false;
                            }
                            if(config['minLength']){
                                if($("#"+genId).val().trim().length<config['minLength']){
                                    valid();
                                    return false;
                                }
                            }
                            if(config['maxLength']){
                                if($("#"+genId).val().trim().length>config['maxLength']){
                                    valid();
                                    return false;
                                }
                            }
                            if(!isEmail($("#"+genId).val().trim())){
                                valid();
                                return false;
                            }
                            clearStyle();
                            return true;
                        }
                }
        }
         //onchange事件调用方法
         var onChange=function(){
              events['changeValue'].apply(events['changeValue'],[beforeChangeValue]);
          }

        /*绑定事件*/
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
        if (readonly){
            $("#"+genId).attr("readonly",true);
        };
        /*返回对象*/
        return {
            XType:'EmailField',
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
                $('#'+genId).css('background','#eeeeee');
            },
            hidden:function(){
                $("#"+genId).hide();
            },
            enabled:function(){
                $("#" + genId).removeAttr('disabled');
                $('#'+genId).css('background','');
            },
            getId:function(){
                return genId;
            }
        }
    }
    $.IWAP.XType['EmailField']=$.IWAP.EmailField;
}(jQuery))