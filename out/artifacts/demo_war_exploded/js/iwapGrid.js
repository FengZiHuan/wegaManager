(function($) {
    if (!$.IWAP) {
        $.extend(
            {
                IWAP: {}
            }
        );
    }
    
    //var deletelist=;
    $.IWAP.iwapGrid=function(config) {
    	var checkValues='';
        var Url=config['Url'];

        var fData=config['fData'];
        var grid = config['grid'];
        var importType = config['importType'];
        var importFunc= config['importFunc']||$.IWAP.empty;
        var mode =config['mode'];//只支持两种mode：local,server
        var total=config['total']||'';
        var form=config['form'];
        var _thead = config['thead'];
        var _td = config['td'];
        var orgParam=$.IWAP.jsonToStr(fData);
        var currentRow=null;
        var renderTo=config['renderTo'];
        if (config['renderTo']) {
            if ($.IWAP.isString(config['renderTo'])) {
                renderTo = $("#" + config['renderTo']);
            } else {
                renderTo = config['renderTo'];
            }
        } else {//如果不设置渲染对象则默认为body
            renderTo = document.body;
        };

        
        var html=[];
        var _start = fData.start;
        var prevmore=0;
        var pagemun= $('.pagination').attr('data-onpage')||'';
        
        //遍历TH的DATA属性为渲染TD做准备
        var tdName=[];
        $(renderTo).find('th').each(function() {
            tdName.push({
                dataName:$(this).attr('data-grid-name'),
                render:$(this).attr('data-grid-render'),
                tdclass:$(this).prop('class'),
                template:$(this).find('s').html()
            });
            //
            /*if ($(this).children('template').length>0) {
                var _template=$(this).find('template').html();
                var thisText=$(this).find('span').html();
                $(this).html(_template+'<template>'+_template+'</template>');
            }else{
                $(this).html(thisText); 
            } */
           
        }); 
          
        if($(".pagenav").length==0 && $(renderTo).attr("data-iwap-pagination")=="true") {
        	var toolHtml=[];
        	toolHtml.push('<div class="pagenav"><div class="fl f14" id="pagemun">当前第<i href="javascript:void(0)" class="c00" id="onhow">&nbsp;1&nbsp;');
        	toolHtml.push('</i>页，</div><div class="fl f14">每页显示<select id="on_state" class="mageinx5 c00"><option value="10">');
        	toolHtml.push(fData.limit+'</option><option value="15">15</option><option value="25">25</option><option value="50">50</option>');
        	toolHtml.push('</select>条数据</div>');
        	if(importType){
        		var types=importType.split(",");
        		toolHtml.push('&nbsp;&nbsp;<div class="fl f14 importData">');
        		for(var i=0;i<types.length;i++){
        			toolHtml.push('<a aria-label="Previous">&nbsp;&nbsp;<span aria-hidden="true">'+types[i]+'</span>');
        		}
        		toolHtml.push('</div>');
        	}
        	toolHtml.push('<div class="fr"><nav><ul class="pagination margin0"></ul></nav></div></div>');
        	
            $(renderTo).after(toolHtml.join(''));
            $(renderTo).next().find(".importData a span").on('click',importFunc);
        }

        //遍历
        //最重要
        function tdloding(){        	 
                //ajax请求
            debugger
                $.post(Url, fData,function(data) {
                	$(renderTo).find('tr:first').siblings().remove();


                	//获取数据。

                    gridData=data.body.rows;
                    //
                    name1=data.body.name1;
                    name2=data.body.name2;
                    name3=data.body.name3;
                    name4=data.body.name4;
                    name5=data.body.name5;
                    name6=data.body.name6;
                    name7=data.body.name7;
                    $("#a1").text(name1);
                    $("#a2").text(name2);
                    $("#a3").text(name3);
                    $("#a4").text(name4);
                    $("#a5").text(name5);
                    $("#a6").text(name6);
                    $("#a7").text(name7);




                        //
                    total=Math.ceil((data.body.total)/fData.limit);
                    //$(renderTo).find('tr:eq(0)').siblings('tr').remove();
                    //遍历
                    $(gridData).each(function(index, el) {
                        var tdData=[]
                        tdData.push('<tr>');
                        for (var i=0;i<tdName.length;i++) {
                            //var eldata=el.tdName[i];
                            var _value=el[tdName[i].dataName]?el[tdName[i].dataName]:'';
                            var tdclass_=tdName[i].tdclass;
                            var template_= tdName[i].template;
                            if(tdName[i].render&&window[tdName[i].render]){
                            	_value=window[tdName[i].render].apply(this,[_value,el]);
                            }
                            if(template_){
                               tdData.push('<td class="'+tdclass_+'"">'+(template_.replace("{{value}}",_value)) +'</td>'); 
                            }else{
                               tdData.push('<td class="'+tdclass_+'"">'+(_value) +'</td>');
                            }
                        }; 
                        tdData.push('</tr>');
                        $(tdData.join('')).appendTo($(renderTo));
                        //$(renderTo).append(tdData.join('')); 
                        $(renderTo).find("tbody td:last")[0]['row_data']=el;
                        
                    });       
                    
                    $('.pagination').html('<li>\
                                  <a aria-label="Previous">\
                                    <span aria-hidden="true">&laquo;</span>\
                                  </a>\
                                </li>\
                                <li>\
                                  <a aria-label="Next">\
                                    <span aria-hidden="true">&raquo;</span>\
                                  </a>\
                                </li>');
                    //判断是否一页，或者一页以上的分页
                    pagemun= $('.pagination').attr('data-onpage')||'';
                    if (total==1) {
                        $('.pagination').html('');
                    }else if(total<=7&&total>1) { 
	                        for (var i = 0; i < total; i++) {
	                            var pageHtml = '<li><a>'+(i+1)+'</a></li>';
	                            $('.pagination').find('li:last').before(pageHtml);
	                        };
                    }else{
                    	//var pageHtml = '<li><a>1</a></li>';
                    	//$('.pagination').find('li:last').before(pageHtml);
//                        for (var i = 1; i < 6; i++) {
//                            var pageHtml = '<li><a>'+(i+(prevmore))+'</a></li>';
//                            $('.pagination').find('li:last').before(pageHtml);
//                        };
                    	if(pagemun){
                    		if((pagemun)<=3){
                        		//$('.pagination').find('li:last').before('<li id="prevmore"><span>……</span></li>');
                        		for(var i=2;i<6;i++){
                            		$('.pagination').find('li:last').before('<li id="prevmore"><a>'+i+'</a></li>');
                            	}
                        		$('.pagination').find('li:last').before('<li id="prevmore"><span>…</span></li>');
                        	}else{
                        		$('.pagination').find('li:last').before('<li id="prevmore"><span>…</span></li>');
                        		if(total-parseInt(pagemun)<=3){
                        			for(var i=(total-4);i<total;i++){
                        				$('.pagination').find('li:last').before('<li id="prevmore"><a>'+i+'</a></li>');
                        			}
                        		}else{
                        			for(var i=(parseInt(pagemun)-1);i<=(parseInt(pagemun)+1);i++){
                                		$('.pagination').find('li:last').before('<li id="prevmore"><a>'+i+'</a></li>');
                                	}
                        			$('.pagination').find('li:last').before('<li id="prevmore"><span>…</span></li>');
                        		}
                        	}
                    	}
                    	
                        
                        //$('.pagination').find('li:last').before('<li><a>'+total+'</a></li>');

                    };
                    //点击到某页
                    var goToHtml='<nav class="gotopage">到第<input type="text" value="1">页</nav>';
                    //判断是否添加GOTO到某页
                    if ( $(renderTo).next('.pagenav').find('.gotopage').length<=0) {
                        $(renderTo).next('.pagenav').find('.fr').append(goToHtml);
                    };
                    $(renderTo).next('.pagenav').find('.gotopage').on('blur keyup', 'input', function(event) {
                        if(event.keyCode==13||!event.keyCode){
                            var gotopageMum = Math.ceil($(this).val())
                            if (gotopageMum>=1&&gotopageMum<=total) {
                                fData.start=(gotopageMum-1)*fData.limit;
                                $('.pagination').attr('data-onpage',gotopageMum);
                                $('#pagemun').find('i').html('&nbsp;'+gotopageMum+'&nbsp;');
                                tdloding();
                            }else{
                                if($('.pagination').attr('data-onpage')){
                                    $(this).val($('.pagination').attr('data-onpage'));
                                }else{
                                    $(this).val(1);
                                };
                                return false;
                            }
                        }
                        
                    });
                    //给当前页面添加选中样式
                    var onpageshow=null
                    $('.pagination li').find('a').each(function() {
                        if ($(this).text()==pagemun) {
                            $(this).addClass('on').siblings('a').removeClass('on');
                        };
                    });

                        
                    //点击分页按钮
                    $(renderTo).next().find('li').on('click', function(event) {
                        //获取当前选中页面
                        //console.log($('.pagination li a.on').closest('li').index())
                        //往前
                        pagemun= $('.pagination').attr('data-onpage')||'';
                        if ($(this).index()==0) {
                            if (pagemun==null||pagemun==''||pagemun==1) {
                                pagemun=1;
                                $('.pagination').attr('data-onpage',pagemun);
                                //alert('已经是第一页了')
                            }else{
                                pagemun=parseInt(pagemun)-1;
                                $('.pagination').attr('data-onpage',pagemun);
                                //点击下一页时候，改变分页显示数值
                                if (pagemun>4&&pagemun<(total-1)){
                                    prevmore-=1;
                                }
                            }
                            onpageshow=$('.pagination li a.on').closest('li').index();
                                
                        }else if ($(this).index()==($(renderTo).next().find('li').length-1)) {
                        //往后
                            //console.log($('.pagination li a.on').closest('li').index());
                            onpageshow=$('.pagination li a.on').closest('li').index();
                            var pagemun= $('.pagination').attr('data-onpage')||'';
                            if (pagemun==null||pagemun==''||pagemun==1) {
                                pagemun=1;
                                pagemun=pagemun+1;
                                $('.pagination').attr('data-onpage',pagemun);
                            }else if (pagemun==total){
                                pagemun=total;
                                $('.pagination').attr('data-onpage',total);
                               // alert('已经是最后一页了')
                            }else{
                                pagemun=parseInt(pagemun)+1;
                                $('.pagination').attr('data-onpage',pagemun);
                                //点击下一页时候，改变分页显示数值
                                if ((pagemun+5-onpageshow)<total){
                                    prevmore+=1;
                                }else{
                                    $('#prevmore').remove();
                                }
                            }
                                
                        }else{
                            var pagemun=$(this).text().replace(/[^0-9]/ig,"");
                            if(pagemun)
                            	$('.pagination').attr('data-onpage',pagemun);

                        };
                        //点击分页时，GOTO输入框清空
                        $(renderTo).next('.pagenav').find('.gotopage>input').val('');

                        if (pagemun!='…') {
                            fData.start=(pagemun-1)*fData.limit;
                        }else{
                            //默认5个分页按钮,加5是因为原来有五个分页，加1是因为
                            if ((prevmore+5-onpageshow+1)<total){
                                prevmore+=5;
                            }
                        }

                                              
                        if(pagemun){
                        	$('#pagemun').find('i').html('&nbsp;'+pagemun+'&nbsp;');  
                        	tdloding();
                        }
                        
                        
                    });
                    
                    
                    //点击checkbox多选
                    $(renderTo).find('td input[selectmulti="selectmulti"]').on('click', function() {
                    	checkValues='';
                        $(renderTo).find('input[selectmulti="selectmulti"]:checked').each(function(index, el) {
                        	checkValues+=$(this).val()+',';
                        });
                        
                    });
                    $(renderTo).find('td').closest('tr').on('mousedown',function(){
                    	//alert(this);
                    	currentRow=$(this).find("td:last")[0].row_data;
                    });

                });

        }
        tdloding();
        
        //点击分页选择下拉框
        $(renderTo).next('.pagenav').on('change', '#on_state', function() {
            var thisval = $(this).val();
            fData.limit=thisval;
            $('.pagination').attr('data-onpage',1);
            $('#pagemun').find('i').html('&nbsp;1&nbsp;');  
            $(renderTo).next('.pagenav').find('.gotopage>input').val('');
            tdloding();
        }); 
        //选择多选  
        $(renderTo).find('th input[selectmulti="selectmulti"]').click(function(){
            var _thisselect = $(renderTo).find('input[selectmulti="selectmulti"]');
            if($(this).prop('checked')){
                _thisselect.prop('checked', true);
            }else{
                _thisselect.removeAttr('checked');
            }
                 
            checkValues='';
            $(renderTo).find('input[selectmulti="selectmulti"]:checked').each(function(index, el) {
            	checkValues+=$(this).val()+',';
            	
            });
            //全选后，再取消个别TD里的checked后的判断
            $(renderTo).find('td').on('click', 'input[selectmulti="selectmulti"]', function() {
            	if ($(renderTo).find('th input[selectmulti="selectmulti"]').prop('checked')) {
            		$(renderTo).find('input[selectmulti="selectmulti"]:checked').each(function(index, el) {
		            	if (index<=fData.limit) {
		            		$(renderTo).find('th input[selectmulti="selectmulti"]').prop('checked',false);
		            	}
		            });
            	}else{
            		$(renderTo).find('input[selectmulti="selectmulti"]:checked').each(function(index, el) {
		            	if ((index+1)>=fData.limit) {
		            		$(renderTo).find('th input[selectmulti="selectmulti"]').prop('checked',true);
		            	}
		            });
            	}

            });
            
        });
        return {
        	options:config,
        	getCheckValues:function(){
        		return checkValues;
        	},
        	doQuery:function(param){
        		if(param){
        			$.IWAP.applyIf(fData,param);
        		}else{
        			if(form){
        				param=form.getData();
        				$.IWAP.apply(fData,param);
        			}
        		}
        		tdloding();
        	},
        	getCurrentRow:function(){
        		return currentRow;
        	},
        	doDelete:function(param){
        		$.IWAP.iwapRequest("iwap.ctrl",param,function(data){
        			 if (data.body.ERROR) {
        			 	return alert("删除失败:"+data.body.ERROR);
        			 }else{
        			 	alert("删除成功");
        			 	tdloding();
        			 }
        		 },function(){
        			 alert("删除失败!");
        		 });
        	},
        	doReset:function(){
        		if(form){
        			form.reset();
        			fData=$.IWAP.strToJson(orgParam);
        		}
        	},
        	doSave:function(param,obj,callBack){
        		//判断input设定不能为空，但实际没填入的次数
        		var num =0;
        		$('.divDialog div[data-iwap-empty="false"]').each(function(){
        			var spanText=$(this).find('span').text();
        			var inputVal=$(this).find('input').val();
        			var textareaVal=$(this).find('textarea').val();
        			
        			if(inputVal!=undefined){
        				if(inputVal==""||inputVal== null){
            				alert(spanText+"不能为空");
            				num++
            			}
        			}
        			if(textareaVal!=undefined){
        				if(textareaVal==""||textareaVal== null){
            				alert(spanText+"不能为空");
            				num++
            			}
        			}
        			
        		});
        		if(num==0){//当input次数为0时，进入保存方法
        			$.IWAP.iwapRequest("iwap.ctrl",param,function(){
           		 	 var error = arguments[0]["header"].msg;
           			 if(error != null){
           				 alert("保存失败:"+error);
           			 }else{
           				 alert("保存成功");
           				 tdloding();
                            if(obj){$(obj).find('.close').click()};
           				 if(callBack){callBack.apply(this,[this])};
           			 }
           		 },function(){
           			 alert("保存失败!");
           		 });
        		}
        		
        	}
        };


    }
}(jQuery))