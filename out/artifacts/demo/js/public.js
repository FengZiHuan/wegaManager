/**
 * 发送异步请求
 * @param field 传递的参数
 * @param txcode 交易码
 * @param actionId 交易配置文件中的顺序
 * @param successCallFn 成功时的回调函数，如果不传，则调用默认函数
 * @param field 传递的地址
 * @returns
 */
function sendAjax(field,txcode,actionId,successCallFn,url){
	if(url == undefined){
		url='iwap.ctrl';
	}
	  data=JSON.stringify({'body':field,'header':{'txcode':txcode,'actionId':actionId}});
	$.ajax({
		type:'post',
		url:url,
		timeout:0,
		data:data,
		cache:false,
		async:false,
		contentType:'application/json',
		dataType:'json',
		success:function(rs){
			if(successCallFn == undefined){
				alert("操作成功!");
			}else{
				successCallFn(rs);
			}
		},//请求成功时回调的函数
		error:function(){
			alert("数据加载失败!");
		}
	});
}

/**
 检查日期格式是否为yyyy-mm-dd
*/
function checkDateFormat(date){
	var reg = /^(\d{4})-(\d{2})-(\d{2})$/;
	return reg.test(date);
}

//数据字典解析
var dictArray=null;
function doDict(dic,data){ 
           var result=data;     
           // alert(dic);
//'body':{"Dept_Level":[{"id":"0","name":"省行"},{"id":"1","name":"分行"},{"id":"2","name":"支行"},{"id":"3","name":"网点"}]}}
	        $.each(dictArray,function(name,value) {
	           if(dic==name){   
	               $.each(value,function(){
	                    if(data==this.id){
	                    result=this.name;
	                    }
				   });
		          
		         }
			});
		 return result;
	}

/**
 * 序列化form表单为json
 */
$.fn.serializeObject = function(){
	var obj = {};
	var arr = this.serializeArray();
	$.each(arr,function(){
		if(obj[this.name]){
			if(!obj[this.name].push){
				obj[this.name] = [obj[this.name]];
			}
			obj[this.name].push(this.value || '');
		}else{
			obj[this.name] = this.value || '';
		}
	});
	return obj;
 }
