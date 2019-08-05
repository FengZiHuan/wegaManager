/**
 * Created by zzm on 2015-5-18 11:21:34.
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
     readonly	是否只读	默认False
     hidden    是否隐藏     默认false
     renderTo	渲染对象	Dom对象或者domID
     */
    $.IWAP.IdCardField=function(config) {
        var allowBlank = (typeof(config['allowBlank'])=='undefined')?true:config['allowBlank'];
        var width = config['width']||'';
        var genId = config['id'] || $.IWAP.id();
        var readonly = (typeof(config['readonly'])=='undefined')?false:config['readonly'];
        var label = config['label'] || "";
        var tipText = config['validatorText'] || "输入无效,请重新输入";
        var validBlur = typeof(config['validateOnBlur']=='undefined')?true:config['validateOnBlur'] ;
        var renderTo = null;
        var hidden=(typeof(config['hidden'])=='undefined')?false:config['hidden'];
        var value = config['value'] || '';
        var events=config['listeners']||[];
        var disabled =(typeof (config['disabled'])=='undefined')?false:config['disabled'];
        var nullText=config['nullText']||'';
        var validator=config['validator']|| IWAP.emptyFn;
        var beforeChangeValue="";                              //输入框之前数据
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
        if(!config["id"]){
            html.push('<div class="inputbox pr">');
            html.push('<span>' + label + ':</span>&nbsp;&nbsp;<input  type="text"  style="width:'+width+'px" placeholder="'+nullText+'" xtype="IdCardField" id="' + genId + '" class="input_text"    value="' + value + '"/>');
            if (!allowBlank) {
                html.push('<span class="err_status" id＝"err_' + genId + '">*</span>');
            }
            html.push('<div class="tooltip_arrow_r" id="err_tip_' + genId + '">' + tipText + '</div></div>');
            $(renderTo).append(html.join(""));
        }
        //是否隐藏
        if(hidden) {
            $('#'+genId).hidden();
            //$('#'+genId).closest("div").hide();
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
        $("#err_tip_"+genId).hide();
        /*
         校验之后样式改变
         */
        var valid=function(){
            $("#err_tip_"+genId).show();
            $("#"+genId).removeClass("input_text");
            $("#"+genId).addClass("input_text_valid");
        }
        /*
         清除样式
         */
        var clearStyle=function(){
            $("#err_tip_"+genId).hide();
            $("#"+genId).removeClass("input_text_valid");
            $("#"+genId).addClass("input_text");
        }
        /*校验身份证函数*/
    	function isTrueValidateCodeBy18IdCard(a_idCard) {   
                    var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子;
                    var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];// 身份证验证位值，10代表X;
					var sum = 0; // 声明加权求和变量   
					if (a_idCard[17].toLowerCase() == 'x') {   
						a_idCard[17] = 10;// 将最后位为x的验证码替换为10方便后续操作   
					}   
					for ( var i = 0; i < 17; i++) {   
						sum += Wi[i] * a_idCard[i];// 加权求和   
					}   
					valCodePosition = sum % 11;// 得到验证码所位置   
					if (a_idCard[17] == ValideCode[valCodePosition]) {   
						return true;   
					}
					return false;   
		}
		/*
		校验身份证18位身份证号
		@param idCard18 身份证号
		*/
    	function isValidityBrithBy18IdCard(idCard18){
					var year = idCard18.substring(6,10);   
					var month = idCard18.substring(10,12);   
					var day = idCard18.substring(12,14);   
					var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
					// 这里用getFullYear()获取年份，避免千年虫问题   
					if(temp_date.getFullYear()!=parseFloat(year) || temp_date.getMonth()!=parseFloat(month)-1 || temp_date.getDate()!=parseFloat(day)){   
						return false;   
					}
					return true;   
		}
        /*
         校验身份证15位身份证号
         @param idCard15 身份证号
         */
    	function isValidityBrithBy15IdCard(idCard15){   
			var year =  idCard15.substring(6,8);   
			var month = idCard15.substring(8,10);   
			var day = idCard15.substring(10,12);
			var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
			// 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
			if(temp_date.getYear()!=parseFloat(year) || temp_date.getMonth()!=parseFloat(month)-1 || temp_date.getDate()!=parseFloat(day)){   
				return false;   
			}
			return true;
		}
        /*
         校验身份证
         @param values 用户输入身份证号
         @return {*}
         */
        var validatorID=function(values){
			if (values.length == 15) {
				return isValidityBrithBy15IdCard(values);   
			}else if (values.length == 18){   
				var a_idCard = values.split("");// 得到身份证数组   
				if (isValidityBrithBy18IdCard(values)&&isTrueValidateCodeBy18IdCard(a_idCard)) {   
					return true;   
				}   
				return false;
			}
			return false;
        }
        /*校验函数*/
        var validatorFunc=function(){//input_text_valid
                if(events['onBlur']){
                    events['onBlur'].apply(events['onBlur'],arguments);
                }
                if(!$("#"+genId).val()&&!allowBlank){
                        valid();
                        return false;
                 }
                if(config['minLength']){
                        if(allowBlank){
                                  if ($("#"+genId).val().length!=0) {
                                      if($("#"+genId).val().length<config['minLength']){
                                                  $("#err_tip_"+genId).text("输入身份证大于"+config['minLength']);
                                                    valid();
                                                  return false;
                                      }
                                  }
                        }if (!allowBlank) {
                                 if($("#"+genId).val().length<config['minLength']){
                                     $("#err_tip_"+genId).text("输入身份证大于"+config['minLength']);
                                        valid();
                                        return false;
                                  }
                        }
                }
                if(config['maxLength']){
                        if(allowBlank){
                            if($("#"+genId).val().length!=0){
                                if($("#"+genId).val().length>config['maxLength']){
                                            $("#err_tip_"+genId).text("输入身份证小于"+config['maxLength']);
                                             valid();
                                             return false;
                                 }
                            }
                        }else{
                                 if($("#"+genId).val().length>config['maxLength']){
                                         $("#err_tip_"+genId).text("输入身份证小于"+config['maxLength']);
                                         valid();
                                         return false;
                                 }
                               }
                }
                         if(allowBlank){
                            if ($("#"+genId).val().length!=0) {
                              /*身份证号不为空，进行验证*/
                              if(!validatorID($("#"+genId).val())){
                                          $("#err_tip_"+genId).text("输入正确身份证");
                                          valid();
                                          return false;
                              }
                            }
                         }
                    	 if(!allowBlank){
                           if ($("#"+genId).val().length!=0) {
                            /*身份证号不为空，进行验证*/
                              if(!validatorID($("#"+genId).val())){
                                   $("#err_tip_"+genId).text("输入正确身份证");
                                   valid();
                                   return false;
                              }
                            }

                }
                    clearStyle();
                  if(config['validator']){
                      var flag=config['validator'].apply(this,[]);
                      if(!flag){
                          valid();
                      }else{
                          clearStyle();
                      }
                  }
                    return true;
              //  }
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
        if(validBlur==true){
            $("#"+genId).on("blur",validatorFunc);
            $("#"+genId).on("focus",function(){
                clearStyle();
                beforeChangeValue=this.value;
                if(events['onFocus']){
                    events['onFocus'].apply(events['onFocus'],arguments);
                }
            });
        }else{
            if(events['onFocus']){
                $("#"+genId).on("focus",onFocusFunc);
            }
            if(events['onBlur']){
                $("#"+genId).on("blur",events['onBlur']);
            }
        }
        //绑定onchange事件
        if(events['onChangeValue']){
            $("#"+genId).on("change",onChange);
        }
        /*返回对象*/
        return {
            XType:'IdCardField',
            options:config,
            /**
             *user:IdCardField.clearInvalid()
             * 清除非法提醒格式
             */
            clearInvalid:function(){
                clearStyle();
            },
            /**
             * user:IdCardField.validate()
             * 校验输入框
             */
            validate:function(){
                validatorFunc.apply(validatorFunc,[]);
            },
            /*
             *user:IdCardField.setValue(val)
             *设置控件值
             * param val 设置控件值
             */
            setValue:function(val){
                $("#"+genId).val(val);
            },
            /*
             *user:IdCardField.getValue()
             *返回控件值
             * return {*}
             */
            getValue:function(){
                return $("#"+genId).val();
            },
            /*
             *user:IdCardField.disabled()
             *禁用控件
             */
            disabled:function(){
                $("#"+genId).attr("disabled",true);
                $('#'+genId).css('background','#eeeeee')
            },
            /*
             *user:IdCardField.hidden()
             *隐藏控件
             */
            hidden:function(){
                $('#'+genId).closest("div").hide();
            },
            /*
             *user:IdCardField.enabled()
             *启用控件
             */
            enabled:function(){
                $("#"+genId).removeAttr("disabled");
                $('#'+genId).css('background','')
            },
            /*
             *user:IdCardField.getId()
             *获取控件id
              *return {*}
             */
            getId:function(){
                return genId;
            }
        }
    }
    $.IWAP.XType['IdCardField']=$.IWAP.IdCardField;
}(jQuery))