/**
 * Created by zzm on 2015-5-20 09:27:47.
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
     allowBlank	是否允许为空	默认true
     width	宽度
     disabled	是否禁用	默认false
     Value	值
     nullText	空文本提示
     minLength	最小长度
     maxLength	最大长度
     validator	字段校验函数
     validateOnBlur	是否失去焦点校验	默认true
     validatorText	非法文本提示
     listeners	监听事件
     label	文本
     id 标识 如果设置并且在dom中存在则不去创建对象，只是渲染
     readonly	是否只读	False
     renderTo	渲染对象	Dom对象或者domID
     */
    $.IWAP.MobileField=function(config) {
        var allowBlank = (typeof(config['allowBlank'])=='undefined')?true:config['allowBlank'];
        var width = config['width']||'';
        var genId = config['id'] || $.IWAP.id();
        var readonly = (typeof(config['readonly'])=='undefined')?false:config['readonly'];
        var disabled=(typeof(config['disabled'])=='undefined')?false:config['disabled'];
        var nullText=config['nullText'];
        var label = config['label'] || "";
        var minLength=config['minLength']||'';
        var maxLength=config['maxLength']||'';
        var tipText = config['validatorText'] || "输入无效,请重新输入";
        var validBlur = config['validateOnBlur'] || true;
        var validator=config['validator']||IWAP.emptyFn;
        var renderTo = null;
        var hidden=(typeof(config['hidden'])=='undefined')?false:config['hidden'];
        var value = config['value'] || '';
        var events=config['listeners']||[];
        var beforeChangeValue="";                             //控件值改变之前的值保存变量
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
         * <div class="inputbox pr">
         <input name="2" type="text" size="11" class="input_text" />
         <span class="err_status" style="display:none;">*</span>
         <div class="sm_tooltip_r"></div>
         <div class="tooltip right"></div>
         <div class="tooltip_arrow_r">输入框未填写，请正确填写</div>
         </div>
         */
         if(config["id"]){
             html.push('<div class="inputbox pr">');
             html.push('<span>'+label+':</span>&nbsp;&nbsp;<input  placeholder="'+nullText+'"  type="text"  xtype="TextField" id="'+genId+'" style="width:'+width+'px" class="input_text" value="'+value+'"/>');
             if(!allowBlank){
                 html.push('<span class="err_status" id＝"err_'+genId+'">*</span>');
             }
             html.push('<div class="tooltip_arrow_r" id="err_tip_'+genId+'">'+tipText+'</div></div>');
             $(renderTo).append(html.join(""));
         }
        $("#err_tip_"+genId).hide();
        //是否隐藏
        if(hidden) {
            //$('#'+genId).hide();
            $('#'+genId).closest("div").hide();
        }
        //判断控件是否为禁用
        if(disabled==true){
            $('#'+genId).attr('disabled',true);
            $('#'+genId).css('background','#eeeeee')
        }
        //判断控件是否只读
        if(readonly==true){
            $('#'+genId).attr("readonly",true)
        }
        var valid=function(){
            $("#err_tip_"+genId).show();
            $("#"+genId).removeClass("input_text");
            $("#"+genId).addClass("input_text_valid");
        }
        var clearStyle=function(){
            $("#err_tip_"+genId).hide();
            $("#"+genId).removeClass("input_text_valid");
            $("#"+genId).addClass("input_text");
        }
        /*校验手机号码格式函数*/

       var mobileFunc=function(mobile){
           var reg=/^1[3|4|5|8][0-9]\d{4,8}$/;
           if(reg.test(mobile)){
               return true;
           }
            return false;
       }
        /*校验函数*/
        var validatorFunc=function(){//input_text_valid
                if(events['onBlur']){
                    events['onBlur'].apply(events['onBlur'],arguments);
                }
                if(config['validator']){
                    var flag=config['validator'].apply(this,[]);
                    if(!flag){
                        valid();
                    }else{
                        clearStyle();
                    }
                    return flag;
                }else{
                        if(!$("#"+genId).val()&&!allowBlank){
                            valid();
                            return false;
                        }
                        if(config['minLength']){
                            if(allowBlank){
                                    if ($("#"+genId).val().length!=0) {
                                        if($("#"+genId).val().length<config['minLength']){
                                        $("#err_tip_"+genId).text("输入手机号码长度大于"+config['minLength']);
                                        valid();
                                        return false;
                                    }
                            }
                        }else{
                            if($("#"+genId).val().length<config['minLength']){
                                         $("#err_tip_"+genId).text("输入手机号码长度大于"+config['minLength']);
                                         valid();
                                         return false;
                            }
                        }
                    }
                    if(config['maxLength']){
                        if(allowBlank){
                            if($("#"+genId).val().length!=0){
                                 if($("#"+genId).val().length>config['maxLength']){
                                     $("#err_tip_"+genId).text("输入手机号码长度小于"+config['maxLength']);
                                     valid();
                                    return false;
                                 } 
                            }
                        }else{
                                 if($("#"+genId).val().length>config['minLength']){
                                    $("#err_tip_"+genId).text("输入手机号码长度小于"+config['maxLength']);
                                     valid();
                                    return false;
                                 } 
                        }
                       
                    } else{
                            if(allowBlank){
                                  if ($("#"+genId).val().length!=0) {
                                     /*手机号码不为空，进行验证*/
                                     if(!mobileFunc($("#"+genId).val())){
                                             $("#err_tip_"+genId).text("输入正确手机号码");
                                             valid();
                                             return false;
                                      }
                            }
                        }  if(!allowBlank){
                               if ($("#"+genId).val().length!=0) {
                                        /*手机号码不为空，进行验证*/
                                        if(!mobileFunc($("#"+genId).val())){
                                          $("#err_tip_"+genId).text("输入正确手机号码");
                                             valid();
                                             return false;
                                        }
                               }
                         }
                    }
                    clearStyle();
                    return true;
                }
            }
        //onchange事件调用方法
        var onChange=function(){
            events['onChangeValue'].apply(events['onChangeValue'],[beforeChangeValue]);
        }
       //绑定焦点校验
        var onFocusFunc=function(){
            beforeChangeValue=this.value;
            events['onFocus'].apply(events['onFocus'],[])
        }
        /*绑定事件*/
        if(validBlur){
            $("#"+genId).on("blur",validatorFunc);
            $("#"+genId).on("focus",function(){
                clearStyle();
                beforeChangeValue=this.value;
                if(events['onFocus']){
                    events['onFocus'].apply(events['onFocus'],arguments);
                }
            });
        }else{
            if(events['onFocusFunc']){
                $("#"+genId).on("focus",onFocusFunc);
            }
            if(events['onBlur']){
                $("#"+genId).on("blur",events['onBlur']);
            }
        }
        if(events['onChangeValue']){

            $("#"+genId).on("change",onChange);
        }
        /*返回对象*/
        return {
            XType:'MobileField',
            options:config,
            /**
             * user:MobileField.clearInvalid()
             * 清除非法提醒格式
             */
            clearInvalid:function(){
                clearStyle();
            },
            /**
             *user:MobileField.validate()
             * 校验输入框
             */
            validate:function(){
                validatorFunc.apply(validatorFunc,[]);
            },
            /*
             *user:MobileField.setValue(val)
             *设置控件值
             */
            setValue:function(val){
                $("#"+genId).val(val);
            },
            /*
            *user:MobileField.getValue()
            *获取控件值
            *return {*}
            * */
            getValue:function(){
                return $("#"+genId).val();
            },
            /*
            *user:MobileField.disabled()
            *禁用控件
            * */
            disabled:function(){
                $("#"+genId).attr("disabled",true);
                $('#'+genId).css('background','#eeeeee')
            },
            /*
            *user:MobileField.hidden()
            *隐藏控件
            * */
            hidden:function(){
                $('#'+genId).closest("div").hide();
            },
            /*
            *user:MobileField.enabled()
            *启用控件/不禁用控件
            * */
            enabled:function(){
                $("#"+genId).removeAttr("disabled");
                $('#'+genId).css('background','')
            },
            /*
             *user:MobileField.getId()
             *获取控件id
             * */
            getId:function(){
                return genId;
            }
        }
    }
    $.IWAP.XType['MobileField']=$.IWAP.MobileField;
}(jQuery))