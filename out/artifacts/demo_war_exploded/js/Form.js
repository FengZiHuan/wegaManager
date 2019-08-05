/**
 * Created by weixiaohua on 15-5-17.
 */
(function($) {
    if (!$.IWAP) {
        $.extend(
            {
                IWAP: {}
            }
        );
    }
    /**
     * @param config
     * 配置项
     配置项	名称	描述
     method	请求方法	Get/post 默认post
     url	请求地址
     baseParam	请求基础参数
     txCode     交易码
     inputWidth	输入框宽度
     labelWidth	文本宽度
     Listeners	监听事件
     fileUpload	是否文件上传	默认false
     Items	表单显示字段
     buttons	按钮组
     Timeout	超时时间
     dataType	数据传输格式	Json/form
     renderTo	渲染对象	Dom对象或者domID
     属性
     属性	名称	描述
     dataType	数据传输格式	Json/form，只读
     Items	表单显示字段	[]只读
     方法
     方法	名称	描述
     clearInvalid ()	清除非法格式提醒
     Add(fields)	增加显示字段
     validate	验证表单是否合法	False验证不通过，true通过
     setData(Json)	设置表单字段值	表单名称key值为value
     getData()	获取表单字段值	表单名称key值为value
     Reset()	重置表单
     Submit()	提交表单
     Form(config)	返回一个表单对象
     GetId()	获取对象标识
     事件
     事件	名称	描述
     beforSubmit(form)	提交之前触发	如果返回false则无法提交
     Success(rst)	成功触发
     Failed(rst)	失败触发	False验证不通过，true通过
     * @constructor
     */
    $.IWAP.Form=function(config) {
        var mehhod=config['mehhod']||'post';
        var txCode=config['txcode'];
        var column=config['column']||12;
        var url=config['url'];
        var baseParam=config['baseParam']||{};
        var widthDef=config['inputWidth'];
        var labelWidth=config['labelWidth'];
        var fileUpload=config['fileUpload']==true;
        var timeOut=config['timeout']||0;
        var dataType=config['dataType']||'form';
        var renderTo = null;
        var genId = config['id'] || $.IWAP.id();
        var name = config['name'] || genId;
        var events=config['listeners']||[];
        var items=config['items']||[];
        var sucessFun=config['sucess']|| $.IWAP.emptyFn;
        var failFun=config['fail']|| $.IWAP.emptyFn;
        var targetItem=[];
        if(config['id']&&$("#"+config['id']).length>0){//存在的form
        	var formItems=$("#"+config['id']).find("[data-iwap-xtype]");
        	 $.each(formItems,function(i,val){//如果不设置XType则默认TextField
        		 var obj={};
        		 for(var x=0;x<val.attributes.length;x++){
        			 var name=val.attributes.item(x)['name'];
        			 obj[name]=val.attributes.item(x)['value'];
        			 if(name.indexOf("data-iwap-")!=-1){
        				 var targetName=name.substr(10);
        				 obj[targetName]=val.attributes.item(x)['value'];
        			 }
        		 }
                 var target=$.IWAP.XType[obj['xtype']].apply(this,[obj]);
                 targetItem.push(target);
        	 });
        	
        }else{
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
            html.push("<div class='col-md-12'>");
            html.push("<form method='"+mehhod+"' onsubmit='return false' id='"+genId+"'></form>");
            html.push("</div>");
            
            $(renderTo).append(html.join(""));
            $.each(items,function(i,val){//如果不设置XType则默认TextField

                var item=$("<div class='col-md-"+column+"'></div>");
                $.IWAP.applyIf(val,{xtype:'TextField',renderTo:item});
                $("#"+genId).append(item);
                var target=$.IWAP.XType[val['xtype']].apply(this,[val]);
                targetItem.push(target);
            });
        }
        
        var validatorFun=function(){
            var flag=true;
            $.each(targetItem,function(i,item){
                if(!item.validate()){
                    flag=false;
                }
            });
            return flag;
        };
        var form={
            XType:'Form',
            options:config,
            clearInvalid:function(){
                $.each(targetItem,function(i,item){
                    item.clearInvalid();
                });
            },
            validate:function(){
                return validatorFun.apply(validatorFun,[]);
            },
            reset:function(){
                $(targetItem).each(function(i,item){
                	item&&item.clearInvalid&&item.clearInvalid();
                	item&&item.setValue&&item.setValue("");
                });
            },
            submit:function(){
                if(this.validate()){
                    var param={header:{txcode:txCode}};
                    var reqPar=this.getData();
                    $.IWAP.applyIf(reqPar,baseParam);
                    param['body']=reqPar;
                    $.IWAP.iwapRequest(url,param,sucessFun,failFun);
                }
            },
            enabled:function(){
            	$(targetItem).each(function(i,item){
                	item&&item.clearInvalid&&item.clearInvalid();
                	item&&item.enabled&&item.enabled();
                });
            },
            enabledById:function(id){
            	if(!id){
            		return;
            	}
            	$(targetItem).each(function(i,item){
                	if(id.indexOf(",")!=-1){
                		var ids=id.split(",");
                		for(var x=0;x<ids.length;x++){
                			if(item['options']['id']==ids[x]){
                    			item&&item.clearInvalid&&item.clearInvalid();
                            	item&&item.enabled&&item.enabled();
                    		}
                		}
                	}else{
                		if(item['options']['id']==id){
                			item&&item.clearInvalid&&item.clearInvalid();
                        	item&&item.enabled&&item.enabled();
                		}
                	}
                });
            },
            disabled:function(){
            	$(targetItem).each(function(i,item){
                	item&&item.clearInvalid&&item.clearInvalid();
                	item&&item.disabled&&item.disabled();
                });
            },
            disabledById:function(id){
            	if(!id){
            		return;
            	}
            	$(targetItem).each(function(i,item){
                	if(id.indexOf(",")!=-1){
                		var ids=id.split(",");
                		for(var x=0;x<ids.length;x++){
                			if(item['options']['id']==ids[x]){
                    			item&&item.clearInvalid&&item.clearInvalid();
                            	item&&item.disabled&&item.disabled();
                    		}
                		}
                	}else{
                		if(item['options']['id']==id){
                			item&&item.clearInvalid&&item.clearInvalid();
                        	item&&item.disabled&&item.disabled();
                		}
                	}
                });
            },
            getItem:function(){
                return targetItem;
            },
            setData:function(json){
                for(var p in json){
                    $.each(targetItem,function(i,item){
                        if(item.options['name']==p||item.getId()==p){
                            item.setValue(json[p]);
                        }
                    });
                }
            },
            getData:function(){
                var rst={};
                $.each(targetItem,function(i,item){
                	if(item.getValue()){
                		rst[item.options['name']||item.getId()]=item.getValue();
                	}
                });
                return rst;
            },

            getId:function(){
                return genId;
            }
        };
        if((!config['id'])||$("#"+config['id']).length==0){//存在的form
        	var buttons=config['buttons']||[{xtype:'ButtonField',label:'重置',click:function(){
                form.reset();
                //return false;
            }},{xtype:'ButtonField',label:'提交',click:function(){
                form.submit();
                // return false;
            }}];
	        var item=$("<div class='col-md-12' style='padding-left: 40%;' id='button_"+genId+"'></div>");
	        $(renderTo).append(item);
	        $.each(buttons,function(i,button){
	            $.IWAP.applyIf(button,{xtype:'ButtonField',renderTo:'button_'+genId});
	            $.IWAP.XType[button['xtype']].apply(this,[button]);
	        });
        }
        return form;
    }
    $.IWAP.XType['Form']=$.IWAP.Form;
}(jQuery))