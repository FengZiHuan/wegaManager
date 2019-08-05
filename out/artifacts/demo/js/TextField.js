/**
 * Created by weixiaohua on 15-5-14.
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
     Width	宽度
     disabled	是否禁用	默认false
     Value	值
     nullText	空文本提示
     MinLength	最小长度
     MaxLength	最大长度
     validator	字段校验函数
     validateOnBlur	是否失去焦点校验	默认true
     validatorText	非法文本提示
     Listeners	监听事件
     Label	文本
     name 名称
     id 标识 如果设置并且在dom中存在则不去创建对象，只是渲染
     readonly	是否只读	False
     renderTo	渲染对象	Dom对象或者domID
     */
    $.IWAP.TextField=function(config) {
    	var allowBlank=(config['allowblank']||config['allowBank']);
        allowBlank =allowBlank==undefined?true:new String(allowBlank).toString()==="true";
        var hidden =config['hidden']==undefined?false:new String(config['hidden']).toString()==="true";
        var nullText= config['nullText']
        var genId = config['id'] || $.IWAP.id();
        var readonly = config['readonly']==undefined?false:config['readonly'];
        var label = config['label'] || "";
        var width = config['width'] || 250;
        var tipText = config['validatorText'] || "输入无效,请重新输入";
        var validBlur = config['validateOnBlur'] ==undefined?true:config['validateOnBlur'];
        var renderTo = null;
        var name = config['name'] || genId;
        var disabled = config['disabled'] || false;
        var value = config['value'] || '';
        var tipsOn = config['tipsOn'] || '';
        var events=config['listeners']||[];
        var type=typeof (config['type'])=='undefined'?'text':config['type'];
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
         function tipsOnFunction(){
            function onposition(){
                if (tipsOn=='right') {
                    html.push('<div class="tooltip_arrow_r" id="err_tip_'+genId+'" style="display:none">'+tipText+'</div>');
                }else if (tipsOn=='bottom') {
                    //后续开发时增加上下左等提示框时用
                    html.push('<div class="tooltip_arrow_b" id="err_tip_'+genId+'" style="display:none">'+tipText+'</div>');
                };
            };
            if (tipsOn) {
                onposition()
            }else{
                html.push('<div class="tooltip_arrow_r" id="err_tip_'+genId+'" style="display:none">'+tipText+'</div>');
            };
        }
        if (config['id']) {
            $('#'+genId).val(value);
            $('#'+genId).addClass('input_text');
            $('#'+genId).attr('xtype','TextField');
        } else{
            html.push('<div class="inputbox pr">');
            html.push('<span>'+label+'：</span><input name="'+name+'" type="'+type+'"  xtype="TextField" id="'+genId+'" class="input_text" style="width:'+width+'" value="'+value+'"/>');
            if(!allowBlank){
                html.push('<i class="err_status" id＝"err_'+genId+'">*</i>');
            }
            html.push('<div class="sm_tooltip_r" id="sm_'+genId+'" style="display:none"></div>');
            html.push('<div class="tooltip right" id="right_'+genId+'" style="display:none"></div>');
            tipsOnFunction();
            $(renderTo).append(html.join(""));
         }
        if(disabled){
            $("#"+genId).attr("disabled","true");
        }
        if(readonly){
            $("#"+genId).attr("readonly",true);
        }
        if(hidden){
            $("#"+genId).parent().hide();
        }
        /*$("#err_tip_"+genId).hide();
        $("#sm_"+genId).hide();
        $("#right_"+genId).hide();*/
        var valid=function(){
            $("#sm_"+genId).show();
            $("#right_"+genId).show();
            $("#err_tip_"+genId).show();
            $("#"+genId).removeClass("input_text");
            $("#"+genId).addClass("input_text_valid");
        }
        var clearStyle=function(){
            $("#sm_"+genId).hide();
            $("#right_"+genId).hide();
            $("#err_tip_"+genId).hide();
            $("#"+genId).removeClass("input_text_valid");
            $("#"+genId).addClass("input_text");
        }
        /*校验函数*/
        var validatorFunc=function(){//input_text_valid
                if(events['blur']){
                    events['blur'].apply(events['blur'],arguments);
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
                        if(nullText){
                            $("#err_tip_"+genId).text(nullText);
                        }
                        valid();
                        return false;
                    }
                    $("#err_tip_"+genId).text(tipText);
                    if(config['minLength']){
                        if($("#"+genId).val().length<config['minLength']){
                            $("#err_tip_"+genId).text("输入长度小于"+config['minLength'])
                            valid();
                            return false;
                        }
                    }
                    if(config['maxLength']){
                        if($("#"+genId).val().length>config['maxLength']){
                            $("#err_tip_"+genId).text("输入长度大于"+config['maxLength']);
                            valid();
                            return false;
                        }
                    }
                    clearStyle();
                    return true;
                }

            }
        /*绑定事件*/
        if(validBlur){
            $("#"+genId).on("blur",validatorFunc);
            $("#"+genId).on("focus",function(){
                clearStyle();
                if(events['focus']){
                    events['focus'].apply(events['focus'],arguments);
                }
            });
        }else{
            if(events['focus']){
                $("#"+genId).on("focus",events['focus']);
            }
            if(events['blur']){
                $("#"+genId).on("blur",events['blur']);
            }
        }
        if(events['changeValue']){

            $("#"+genId).on("change",events['changeValue']);
        }
        /*返回对象*/
        return {
            XType:'TextField',
            options:config,
            disabled:disabled,
            hidden:false,
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
                return validatorFunc.apply(validatorFunc,[]);
            },
            /**
             * 设置文本框值
             * @param val
             */
            setValue:function(val){
                $("#"+genId).val(val);
            },
            /**
             * 得到文本框值
             * @returns {*|{}}
             */
            getValue:function(){
                return $("#"+genId).val();
            },
            /**
             * 禁用
             */
            disabled:function(){
                $("#"+genId).attr("disabled","true");
            },
            /**
             * 隐藏
             */
            hidden:function(){
                $("#"+genId).hidden();
            },
            /**
             * 启用
             */
            enabled:function(){
                $("#"+genId).removeAttr("disabled");
            },
            /**
             * 获取组建ID
             * @returns {*}
             */
            getId:function(){
                return genId;
            }
        }
    }
    $.IWAP.XType['TextField']=$.IWAP.TextField;
}(jQuery))