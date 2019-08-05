/**
 * Created by on 15-9-24.
 */
//data-iwap ="tooltipdiv"启用静态data-iwap ="tooltipdiv"的JS
//data-empty 值为true　and false启用判断输入框是否可以为空的JS　默认为false
//对话框的modal-header和modal-body的button添加属性aria-hidden="true"时，点击可关闭对话框
//data-iwap-maxlength 最大输入　data-iwap-minlength 最小输入

$(function() {
	//延时是因为bootstrap的对话框JS会先覆盖一次原生的HTML代码里的modal-dialog，执行需要延时一点才能全部each遍历
	setTimeout(function(){
		//输入框tooltip
		$('body').find('div[data-iwap ="tooltipdiv"]').each(function(index, val) {
			var _this = $(this);
			var _thisName=_this.find('input').attr('id');
			//_this.find('input').attr('id', 'err_tip_'+_thisName);
			var tooltext= _this.attr('data-iwap-tooltext');
		    var _html = '<i class="err_status" id="err_'+_thisName+'">*</i><div class="sm_tooltip_r"  style="display:none" id="sm_'+_thisName+'"></div><div class="tooltip right" id="right_'+_thisName+'" style="display:none"></div><div class="tooltip_arrow_r" id="err_tip_'+_thisName+'" style="display:none">'+tooltext+'</div>';
		    //输入框字数限制遍历
	    	var maxMun=$(this).attr('data-iwap-maxlength');
	    	var minMun=$(this).attr('data-iwap-minlength');
	    	if (_this.find('i').length<=0) {
    			_this.append(_html);
    		};
    		//_this.find('i, .sm_tooltip_r, .tooltip, .tooltip_arrow_r').hide();
		    _this.find('input').blur(function() {
		    	if (this.value.length<=0||this.value.length>maxMun||this.value.length<minMun) {
		    		
			    	//显示tooltips
			    	_this.find('i, .sm_tooltip_r, .tooltip, .tooltip_arrow_r').show();
			    }else{
			    	_this.find('i, .sm_tooltip_r, .tooltip, .tooltip_arrow_r').hide();
			    };
			    //值为空时隐藏tooltips
			    $('body').find('div[data-iwap-empty="true"]').each(function() {
			    	$(this).find('i, .sm_tooltip_r, .tooltip, .tooltip_arrow_r').hide();
			    });
			    
		    });
		    //禁用遍历
		    if ($(this).attr('data-disabled')){
		    	$(this).find('input').attr('disabled', 'disabled');
		    };
		});
		//隐藏遍历
		$('body').find('div[data-hidden="hide"]').each(function() {
			$(this).css('display', 'none');
		});
		//禁用遍历
	    /*$('body').find('select[data-disabled="true"]').each(function() {
	    	$(this).attr('disabled', 'disabled');
	    });
	    $('body').find('a[data-disabled="true"]').each(function() {
	    	$(this).attr('disabled', 'disabled');
	    });
	    $('body').find('div[data-disabled="true"]').each(function() {
	    	$(this).attr('disabled', 'disabled');
	    });*/
	    
		//对话框显示隐藏
		//对话框模拟关闭和显示
		$('.dialog .dialog-header').find('button[data-dialog-hidden="true"]').click(function() {
			$(this).closest('.dialog').prev('.bg').hide();
			$(this).closest('.dialog').hide();
			

		});
		$('.dialog .dialog-body').find('button[data-dialog-hidden="true"]').click(function(){
			$(this).closest('.dialog').prev('.bg').hide();
			$(this).closest('.dialog').hide();
			

		});
		
		$('.dialog').each(function(index, val) {
			var dialogW = $(this).width();
			$(this).css('margin-left', -(dialogW/2));
		});
		$('body').find('a[data-iwap-dialog]').each(function() {
			$(this).on('click', function() {
				var diaId=$(this).attr('data-iwap-dialog');
				$('.dialog[id="'+diaId+'"]').css('display', 'block');
				$('.dialog[id="'+diaId+'"]').prev('div[class="bg"]').css('display', 'block');
				//$('#divDialog').css('display', 'block');
			});	
		});	

	},150);	 

});

(function($){
	$.fn.extend({   
        dialog: function(text,width) {
            return this.each(function() {
				$(this).css('display', 'block');
				$(this).prev('div[class="bg"]').css('display', 'block');
				$(this).find('.dialog-header h4').html(text)
				$(this).width(width);
            });  
        } 
    }); 
	//序列化表单
	$.fn.serializeJson=function(){var initialObj={}, array=this.serializeArray(); $(array).each(function(){var thisName = initialObj[this.name]; if(initialObj[this[name]]){if($.isArray(thisName)){thisName.push(this.value);}else{thisName=[thisName,this.value];}}else{initialObj[this.name]=this.value;}});return initialObj};
})(jQuery);