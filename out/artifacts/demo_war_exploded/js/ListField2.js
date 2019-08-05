/**
 * Created by Jackie on 2015/5/20 0020.
 */
(function(IWAP) {
	/**
	 * 初始化组件
	 * @param config
	 Label	文本
	 allowBlank	是否允许为空	默认true
	 disabled	是否禁用	默认false
	 data 下拉数据 modal为local时生效
	 Listeners	监听事件
	 readonly	是否只读	False
	 mode 数据来源 local/server
	 Value	默认值
	 url 请求url Mode 为server时有效
	 baseParam 过滤参数 Mode 为server时有效
	 displayValue 显示值字段
	 UseValue 使用值字段
	 isMultiSelect 是否多选 默认false
	 renderTo	渲染对象	Dom对象或者domID
	 name 名称
	 id 标识 如果设置并且在dom中存在则不去创建对象，只是渲染
	 */
	var ListField2 = function(config) {
		var allowBlank = config['allowBlank'] && true;
		var genId = config['id'] || IWAP.id();
		var readonly = config['readonly'] == true;
		var disabled = config['disabled'] == true;
		var hidden = config['hidden'] == true;
		var label = config['label'] || "";
		var mode = config['mode'];
		var data = config['data'];
		var renderTo = null;
		var listeners = config['listeners'] || [];
		var displayValue = config['displayValue'];
		var useValue = config['useValue'];
		var baseParam = config['baseParam'] || {};
		var url = config['url'] || '';//设置AJAX的url
		var isMultiSelect = config['isMultiSelect'] || false;//单选模式或者多选模式,false为多选模式，true为单选模式
		var setValue = config['setValue'] || '';//设置选中值
		var html = [];
		var ul = $(document.createElement("ul")),self, errself, ctxself;
		var loadedstate = false;//false表示未加载完成，true表示加载完毕
		var vals={};
		if (config['renderTo']) {
			if (IWAP.isString(config['renderTo'])) {
				renderTo = $("#" + config['renderTo']);
			} else {
				renderTo = $(config['renderTo']);
			}
		} else {//如果不设置渲染对象则默认为body
			renderTo = $(document.body);
		}

		html.push('<div class="selectbox mr60" id="ctx_' + genId + '">');
		html.push('<span>'+ label+ ': </span><input type="text" value="" id="'+ genId+ '" xtype="ListField2" class="select_content_in" autocomplete="false" data-value="" title="" />');
		allowBlank&&html.push('<span class="err_status pa" id="err_' + genId+ '">*</span>');
		html.push('</div>'); 
		
		 
		//写入代码
		renderTo.append(html.join(''));
		self = $("#" + genId);
		errself = $("#err_" + genId);
		ctxself = $("#ctx_" + genId);

		ul.addClass('select_list_box '+ (isMultiSelect ? "isMultiSelect" : ""));
		ul.css('display', 'none');
		ctxself.append(ul);
		
		/**
		 * 设置文本框的值
		 */
		var setval=function(){
			var text;
			var val;
			for ( var id in vals) {
				text = text ? (text + "," + id) : id;
				val = val ? (val + "," + vals[id]) : vals[id];
			}
			self.val(text);
			self.attr('data-value', val);
			self.attr('title', text);
		};
		
		/**
		 * 存储原始值
		 */
		var oldvals;
		/**
		 * 判断值是否改变
		 */
		var change=function(){
			var s=true;
			for ( var key in vals)if(oldvals[key]!=vals[key]){
				s=false;
				setTimeout(function(){
					listeners&&listeners.change&&listeners.change(self,vals);
				}, 1);
				break;
			};
			if(s){
				for ( var key in oldvals)if(oldvals[key]!=vals[key]){
					s=false;
					setTimeout(function(){
						listeners&&listeners.change&&listeners.change(self,vals);
					}, 1);
					break;
				};
			}
			
		};
		//初始化参数集
		var initialize=function (data) {
//			alert(JSON.stringify(data));
			data=data["body"]?(data["body"]["rows"]):data;
			//循环DATA数据
			for (var i = 0; i < data.length; i++)ul.append('<li data-value="' + data[i][useValue] + '">'+ data[i][displayValue] + '</li>');
			/*是否禁用*/ 
			disabled&&self.attr('disabled', true);
			disabled&&self.css('background', '#eeeeee'); 
			hidden&&self.hide(); 
			/*是否只读*/
			readonly&&self.attr('readonly', true);
			//点击input时展开下拉盒子
			readonly&&self.click(function() {ul.remove();});
			listeners&&self.on("click", listeners.expend);
			//设置选中值 
			setValue && ul.children('li').each(function() {
				var _self = $(this);
				var _datavalue = _self.attr('data-value');
				if (isMultiSelect) {//如果是单选框
					if (_datavalue == setValue) {
						vals[_self.text()] = _datavalue;
					}
				} else {//如果是复选框
					$(setValue.split(",")).each(function(key,value) {
						if(value==_datavalue){
							_self.addClass("active");
							vals[_self.text()] = _datavalue;
						} 
					}); 
				}
				setval();
			}); 
			//self.focus(function() {self.blur();});
			//input获取点击的值-下拉单选
			if(isMultiSelect)
				ul.children('li').click(function() {
					vals={};
					var _self=$(this);
					vals[_self.text()]=_self.attr('data-value');
					setTimeout(change, 1);//单选框改变时在此处判断
					setval();
				});
			else //input获取点击的值-下拉复选
				ul.children('li').click(function() {
				var _self=$(this);
				if(_self.hasClass("active")){
					_self.removeClass("active");
					delete vals[_self.text()];
				}else{ 
					_self.addClass("active"); 
					vals[_self.text()]=_self.attr('data-value');
				}
				setval();
			});
			//点击input时展开下拉盒子
			var clickd=false;
			self.mousedown(function() {
				clickd=true;
				oldvals={};
				for ( var key in vals)oldvals[key]=vals[key];
				ul.show(); 
				
				$("body").on("mouseup."+genId,function() {
				if(clickd)
					clickd=false;
				else{ 
					ul.hide(); 
					$("body").off("mouseup."+genId);
					isMultiSelect||setTimeout(change, 1);//复选框改变时在此处判断
					}
				});
			});
			isMultiSelect||ul.mousedown(function() {clickd=true;});
			loadedstate=true;
		};

		
		if (mode == 'local') {
			// 判断是多选下拉还是单选下拉
			setTimeout(function(){initialize(data);}, 1);
		} else if (mode == 'server') {
			listeners['beforeLoad']&& listeners['beforeLoad'].apply(listeners['beforeLoad'],arguments);
			$.ajax({
				url : url,
				type : 'post',
				data: JSON.stringify(baseParam),
				contentType: 'application/json',
				dataType:'json',
				success : initialize});
		}
		this.loadedstate=loadedstate; 
		this.XType = 'ListField2';
		this.options = config;
		this.disabled = disabled;
		this.hidden = hidden;
		this.setValue=function(val){
			var text;
			for ( var id in vals) {
				text = text ? (text + "," + id) : id;
				val = val ? (val + "," + vals[id]) : vals[id];
			}
			self.val(text);
			self.attr('data-value', val);
			self.attr('title', text);
		};
		/**
		 * 校验输入框
		 */
		this.getValue = function() {
			var val;
			for ( var id in vals) {
				val=val?(val+","+vals[id]):vals[id];
			}
			return val;
		};
		this.getValueText = function() {
			var text;
			for ( var id in vals) {
				text=text?(text+","+id):id;
			}
			return text;
		};
		this.disabled = function() {
			self.attr('disabled', true);
			ul.hide();
		};
		this.hidden = function() {
			self.hide();
		};
		this.enabled = function() {
			self.removeAttr('disabled');
			
		};
		this.getId = function() {
			return genId;
		};
		this.getSelf=function(){
			return self;
		};
		this.getErrself=function(){
			return errself;
		};
		this.load = function(data, append) {
			if (!append) {
				self.empty();
			}
			for (var i = 0; i < data.length; i++) {
				ul.append('<li data-value="' + data[i][useValue] + '">'+ data[i][displayValue] + '</li>');
			}
		};
		/* 设置查询参数，放入基础过滤参数中 */
		this.setParam = function(condition) {
			if (mode == 'server') {
				for ( var key in condition) {
					baseParam[key] = condition[key];
				}
			}
		};
	};
	IWAP.XType['ListField2'] = IWAP.ListField2 = function(config) {
		return new ListField2(config);
	};
})(jQuery.IWAP || (window.IWAP=jQuery.IWAP={}));
