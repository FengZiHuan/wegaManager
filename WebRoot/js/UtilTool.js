function UtilTool(){
    var documentId=0;
	this.encodeHtml=function(value){
        return !value ? value : String(value).replace(/&/g, "&amp;").replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
    };
    this.decodeHtml=function(value){
        return !value ? value : String(value).replace(/&gt;/g, ">").replace(/&lt;/g, "<").replace(/&quot;/g, '"').replace(/&amp;/g, "&");
    };
    this.encodeUrl=function(url){
    	return encodeURIComponent(url);
    };
    this.decodeUrl=function(url){
    	return decodeURIComponent(url);
    };
    /**
     * 把字符串转换成Json格式
     */
	this.strToJson = function(json){
        return eval("(" + json + ')');
    };
    this.alert=function(title,msg){
      alert(msg||title);
    };
    /**
     * 判断参数是否是对象
     * @param obj
     * @returns {boolean} true
     */
    this.isObject=function(obj){
        return Object.prototype.toString.call(obj) === '[object Object]';
    };
    /**
     * 判断对象是否是数组
     */
    this.isArray=function(obj){
        return Object.prototype.toString.call(obj) === '[object Array]';
    }
    /**
     * 判断对象是否函数
     */
    this.isFunction=function(obj){
        return Object.prototype.toString.call(obj) === '[object Function]';
    }
    /**
     * 判断对象是否是日期
     */
    this.isDate=function(o){
    	return o && typeof o.getFullYear == "function";
    };
    /**
     * 判断对象是否是字符串
     */
    this.isString=function(o){
    	return typeof(o)=="string"&&typeof(o.sub)=="function";
    };
    /**
     * 把Json串转换成字符格式
     */
    this.jsonToStr= function(o){
        if(typeof o == "undefined" || o === null){
            return "null";
        }else if(this.isArray(o)){
            return function(o){
            	var a = ["["], flag=false, i, l = o.length, v;
                for (i = 0; i < l; i += 1) {
                    v = o[i];
                    switch (typeof v) {
                        case "undefined":
                        case "function":
                        case "unknown":
                            break;
                        default:
                            if (flag) {
                                a.push(',');
                            }
                            a.push(v === null ? "null" : this.jsonToStr(v));
                            flag = true;
                    }
                }
                a.push("]");
                return a.join("");
            }();
        }else if(this.isDate(o)){
            return function(o){
            	return '"' + o.getFullYear() + "-" +
                pad(o.getMonth() + 1) + "-" +
                pad(o.getDate()) + "T" +
                pad(o.getHours()) + ":" +
                pad(o.getMinutes()) + ":" +
                pad(o.getSeconds()) + '"';
            }();
        }else if(typeof o == "string"){
            return "\""+o+"\"";
        }else if(typeof o == "number"){
            return String(o);
        }else if(typeof o == "boolean"){
            return String(o);
        }else {
            var a = ["{"], b=false, i, v;
            for (i in o) {
                if(o.hasOwnProperty(i)) {
                    v = o[i];
                    switch (typeof v) {
                    case "undefined":
                    case "function":
                    case "unknown":
                        break;
                    default:
                        if(b){
                            a.push(',');
                        }
                        a.push(this.jsonToStr(i), ":",
                                v === null ? "null" : this.jsonToStr(v));
                        b = true;
                    }
                }
            }
            a.push("}");
            return a.join("");
        }
    };
    this.trim=function(str){
    	var reg= /^\s+|\s+$/g;
    	return str.replace(reg, "");
    };
    this.toLowerCase=function(str){
    	return new String(str).toLowerCase();
    };
    this.toUpperCase=function(str){
    	return new String(str).toUpperCase();
    };
    this.subStr=function(str,start,len){
    	return new String(str).subStr();
    };
    /**
     * 实现srcObj属性方法克隆给tagetObj，如果tagetObj中存在则用srcObj覆盖targetObj中属性方法
     */
    this.apply=function(tagetObj,srcObj){
    	for(var p in srcObj){
    		tagetObj[p]=srcObj[p];
    	}
    };
    /**
     * 实现srcObj属性方法克隆给tagetObj，如果tagetObj中存在则用srcObj中属性方法将被忽略
     */
    this.applyIf=function(targetObj,srcObj){
    	for(var p in srcObj){
    		if(!targetObj[p]){
    			targetObj[p]=srcObj[p];
    		}
    	}
    };
    this.iwapRequest=function(url,jsonParam,sucessCallBack,failCallBack){
        failCallBack=failCallBack||function(obj){
                alert("交易:["+jsonParam['txcode']+"]出错!错误信息:"+obj['responseJSON']['msg']);
            };
        $.ajax({type:'post',
            url:url,
            timeout:0,
            data:JSON.stringify(jsonParam),
            cache:false,
            contentType: 'application/json',
            dataType:'json',
            success:sucessCallBack,
            error:failCallBack});
    }
    /**
     * 判断对象是否是正常值
     */
    this.isNormal = function(o) {
    	if(o != null && o != undefined && o != '') {
    		return true;
    	}else {
    		return false;
    	}
    }
    /**
     * 空函数
     */
    this.emptyFn=function(){};
    /**
     * 格式化日期函数
     * @param date
     * @returns {*}
     */
    this.getFomatDate = function (date)
        {
            if (date == "NaN") return null;
            if (typeof date == "string") return date;
            var format = 'yyyy-MM-dd hh:mm:ss';
            var o = {
                "M+": date.getMonth() + 1,
                "d+": date.getDate(),
                "h+": date.getHours(),
                "m+": date.getMinutes(),
                "s+": date.getSeconds(),
                "q+": Math.floor((date.getMonth() + 3) / 3),
                "S": date.getMilliseconds()
            }
            if (/(y+)/.test(format))
            {
                format = format.replace(RegExp.$1, (date.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
            }
            for (var k in o)
            {
                if (new RegExp("(" + k + ")").test(format))
                {
                    format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
                }
            }
            return format;
        }
    this.id=function(){
        documentId++;
        return "iwap-gen-"+documentId;
    }
}

$(document).ready(function(){
    $(document.body).addClass('iwapui');
});

IWAP=new UtilTool();
if(!IWAP.XType){
    IWAP.XType={};
}
$.extend(
    {
        IWAP: IWAP
    }
);