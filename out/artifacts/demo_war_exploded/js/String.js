
    String.prototype.isString=true;
    
	String.prototype.toFile=function(){
	   return this	
					.replace(/[\/\\]+/g, "/")
					.replace(/\/ +\//g, "/")
	   				.replace(/[\/\s]+$/, "")
	   				.replace(/^\s+/, "")
	   				.replace(/^\s+/, ""); ; 
	};
	 
	String.prototype.getFileName=function(){
		var file=this.toFile() ; 
		var index=file.lastIndexOf("/");
		if(index==-1)
			return file;
		else
			return file.substring(index+1);
	};
	
	String.prototype.getParentPath=function(){
		var file=this.toFile() ; 
		var index=file.lastIndexOf("/");
		if(index==0)
			return "/";
		if(index==-1)
			return ".";
		else
			return file.substring(0,index);
	};
	
    String.prototype.times=function   (count) {
        return count < 1 ? '': new Array(count + 1).join(this);
    };
    String.prototype.isJSON= function () {
        var str = this;
        if (str.blank()) return false;
        str = this.replace(/\\./g, '@').replace(/"[^"\\\n\r]*"/g, '');
        return (/^[,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]*$/).test(str)
    }
    String.prototype.toJSON= function  (sanitize) {
        var json = this;
        try {
            if (json.isJSON()) return eval('(' + json + ')');
        } catch(e) {}
        return  this;
    }
    String.prototype.include= function (pattern) {
        return this.indexOf(pattern) > -1;
    };
    String.prototype.startsWith= function (pattern) {
        return this.indexOf(pattern) === 0;
    };
    String.prototype.endsWith= function  (pattern) {
        var d = this.length - pattern.length;
        return d >= 0 && this.lastIndexOf(pattern) === d;
    };
    String.prototype.empty= function () {
        return this == '';
    };
    String.prototype.blank= function  () {
        return /^\s*$/.test(this);
    };
    
  
	/**
	 * "2015/08/07/22:33:18".toDate("yyyy/MM/dd/hh:mm:ss")
	 * 字符串转时间对象
	 * @param fmt 字符串的格式
	 * @returns {Date} 返回的时间对象
	 */
	String.prototype.toDate=function(fmt){
		  var ret=new Date();
		  var o = {
				  	"y+" : function(year){ret.setFullYear(year);},                 //月份 
				    "M+" : function(month){ret.setMonth(month&&(month-1));},                 //月份 
				    "d+" : function(date){ret.setDate(date);},                    //日 
				    "h+" : function(hours){ret.setHours(hours);},                   //小时 
				    "m+" : function(minutes){ret.setMinutes(minutes);},                //分 
				    "s+" : function(seconds){ret.setSeconds(seconds);},                //秒  
				    "S"  : function(milliseconds){ret.setMilliseconds(milliseconds);}            //毫秒 
				  };
		 for ( var k in o) {
			var r = new RegExp("("+k+")");
			if(r.test(fmt))
				o[k](Number(this.substring(fmt.indexOf(RegExp.$1), fmt.indexOf(RegExp.$1)+RegExp.$1.length)));
			else
				o[k](0);
		 } 
		 return ret;  
	};
	/**
	 * 去掉首尾的空格
	 */
	String.prototype.trim=function(){
		return this.replace(/(^ +)|( +$)/g, "");
	};
	
	/**
	 * 判断一个字符串是否是IP地址
	 * @returns
	 */
	String.prototype.isIP=function(){
	        return !!this.match(/^((25[0-5]|2[0-4]\d|[01]?\d\d?)($|(?!\.$)\.)){4}$/);
	};
	/**
	 * "127.0.0.1".mateIP("127.*.*.*")
	 * 匹配一个IP规则
	 * */
	String.prototype.mateIP=function(style){
		style=style.replace(/\./g, "\\.").replace(/\*/g, "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)");
        return !!this.match(new RegExp(style));
	};
	/**
	 * "www.baidu.com".mateStr("*.*.com")
	 * 匹配一个字符串
	 */
	String.prototype.mateStr=function(style){
		style=style.replace(/\./g, "\\.").replace(/\*/g, "(.+)");
        return !!this.match(new RegExp(style));
	};
	