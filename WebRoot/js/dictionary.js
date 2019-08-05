window._dictJson = {};
/**
 * 初始化数据字典
 * 
 * @param dictLs
 *            必输项,数据字典列表，例: 'Dept_State' 或者 'Dept_Level,Dept_State'，
 *            因为数据字典请求到的数据是存储在window里的一个对象，可允许同一个页面多次使用此方法请求不同的数据字典，该对象的数据会自动叠加
 * @param callFn
 *            非必输项,通过设置callFn实现数据字典异步请求后再执行该函数，保证优先获得数据字典
 * 
 */
var initDict = function(dictLs, callFn) {
	if (dictLs) {
		getDictJson && getDictJson(dictLs, function(json) {
			if (json) {
				$.extend(window._dictJson,json);
				callFn && callFn();
			}
		});
	} else {
		alert("初始化数据字典参数有误");
	}
};

/**
 * 获取数据字典格式化文本，使用该方法必须先使用initDict函数初始化所需数据字典，使用对应数据字典把原始数据进行格式化
 * 
 * @param dictNm
 *            必输项,数据字典名称，例: 'Dept_State'
 * @param data
 *            必输项,原始数据
 * @return 返回对应数据字典格式化文本，若无对应值则返回原始数据
 * 
 */
var getDict = function(dicNm, data) {
	var result = data || "";
	if (dicNm && data) {
		var dictJson = window._dictJson;
		if (dictJson && dictJson[dicNm]) {
			$.each(dictJson[dicNm], function() {
				if (this && data == this.id) {
					result = this.name;
					return true;
				}
			});
		}
	}
	return result;
}

/**
 * 初始化数据字典下拉列表
 * 
 * @param selectKV
 *            必输项,下拉列表控件ID与数据字典的键值对json数据，例如:{"selectID1":"dictNm1","selectID2":"dictNm2"},
 *            其中selectID1、selectID2是下拉列表select控件的id，dictNm1、dictNm2是对应的数据字典名称
 * 
 */
var initSelectKV = function(selectKV) {
	if (selectKV) {
		var selectJson = $.IWAP.strToJson(selectKV);
		var dictLs = "";
		for ( var id in selectJson) {
			if (selectJson[id]) {
				dictLs += selectJson[id] + ',';
			}
		}
		if (dictLs.length > 0) {
			dictLs = dictLs.substr(0, dictLs.length - 1);
			getDictJson && getDictJson(dictLs, function(dictJson) {
				if (dictJson) {
					for ( var id in selectJson) {
						if (id && $.IWAP.trim(id).length>0 && selectJson[id]) {
							var dicNm = selectJson[id];
							if(dictJson[dicNm]){
								$.each(dictJson[dicNm], function() {
									if (this) {
										$("<option/>").val(this.id)
										  .text(this.name)
										  .appendTo("select#"+id);
									}
								});
							}
						}
					}
				}
			});
		}
	} else {
		alert("初始化数据字典下拉列表参数有误");
	}
}

/**
 * 发送异步请求获取数据字典
 * 
 * @param dictLs
 *            必输项,数据字典列表，例: 'Dept_State' 或者 'Dept_Level,Dept_State'
 * @param successCallFn
 *           必输项,成功时的回调函数，如果不传则只提示'获取数据字典完毕'，同时传入数据字典返回的json数据，格式如：
 *           {"Product_Type":[{"id":"001","name":"理财产品"},{"id":"002","name":"保险产品"}],"Card_Type":[{"id":"0","name":"身份证"},{"id":"1","name":"护照"}]}
 *
 */
var getDictJson = function(dictLs, successCallFn) {
	if (dictLs) {
		$.ajax({
			type : 'post',
			url : 'iwap.ctrl',
			timeout : 0,
			data : JSON.stringify({
				'body' : {
					'dictNm' : dictLs
				},
				'header' : {
					'txcode' : 'dict',
					'actionId' : 'doBiz'
				}
			}),
			cache : false,
			async : false,
			contentType : 'application/json',
			dataType : 'json',
			success : function(rs) {
				if (rs && rs['header']['msg']) {
					alert("获取数据字典失败:" + data['header']['msg']);
				} else if (rs && rs['body']) {
					if (successCallFn) {
						successCallFn(rs['body']);
					} else {
						alert("获取数据字典完毕!");
					}
				} else {
					alert("getDictJson error");
				}
			},
			error : function() {
				alert("数据字典数据加载失败!");
			}
		});
	} else {
		alert("请求获取数据字典参数有误!");
	}
};