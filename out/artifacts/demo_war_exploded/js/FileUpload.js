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
     label  文本  标题
     disabled  是否禁用    默认false
     isMultiSelect 是否多选文件 默认为false
     size  文件大小 M为单位
     fileType 文件类型  多个文件类型采用逗号隔开
     renderTo 渲染对象    Dom对象或者domID
     success:上传成功回调函数
     failed:上传失败回调函数
     hidden:是否隐藏 默认为false
     afterUpload:上传之后触发
     beforeUpload:上传之前触发
     url:上传地址
     */
    $.IWAP.FileUpload=function(config) {
        var label = config['label'] || "";
        var genId = config['id'] || $.IWAP.id();
        var isMultiSelect=typeof(config['isMultiSelect'])=='undefined'?false:config['isMultiSelect'];
        var disabled=typeof(config['disabled'])=='undefined'?false:config['disabled'];
        var hidden=typeof(config['hidden'])=='undefined'?false:config['hidden'];
        var _size=config['size']||'4';
        var fileType=config['fileType'];
		var param=config['param']||{};
        var renderTo = null;
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
        html.push('<div class="upfilebox" id="ctx_'+genId+'">');
        html.push('<div class="upfile_layout">');
        html.push('<span>'+label+'</span>');
        html.push('</div>');
        html.push('<input type="file" class="upfile" enctype="multipart/form-data"  id="'+genId+'"/>');
        html.push('</div>');
        $(renderTo).append(html.join(""));
        /*是否支持多选*/
        if(isMultiSelect){
            $('#'+genId).attr('multiple','multiple');
        }
        /*是否禁用*/
        if(disabled){
            $('#'+genId).attr('disabled',true);
            $('#'+genId).css('background','#eeeeee');
        }
        /*是否隐藏*/
        if(hidden){
            $('#ctx_'+genId).hide();
        }
        /**
         * 检查文件类型是否符合要求
         */
        var typeValid=function(){
             var _files=$('#'+genId)[0].files;
             for ( var i = 0; i < _files.length; i++) {
                var _fileName=_files[i].name;
                var _postfix=_fileName.substring(_fileName.lastIndexOf('.')+1).toLowerCase();
                for ( var j = 0; j < fileType.length; j++) {
                    if (_postfix!=fileType[j].toLowerCase()) {
                        continue;
                    } else {
                        return true;
                    }
                    
                }
            }
             return false;
        }
        /**
         * 检查文件大小是否符合要求
         */
        var sizeValid=function(){
             var _files=$('#'+genId)[0].files;
             for ( var i = 0; i < _files.length; i++) {
                 var _fileSize=(_files[i].size)/(1024*1024);
                 if (_fileSize>_size) {
                    return false;
                } else {
                    continue;
                }
            }
            return true;
        }
        /**
         * 检查是否选择了文件进行上传
         */
        var existValid=function(){
            var _files=$('#'+genId)[0].files;
            if (_files.length>0) {
                return true;
            } else {
                return false;
            }
        }
        /**
         * 上传文件
         */
        function uploadAttachment(){
            if (!existValid()) {
                alert('请选择文件上传!!!');
                return;
            }
            if(!typeValid()){
                alert('文件格式有误，请选择正确格式的文件上传!!!');
                return;
            }
            if(!sizeValid()){
                alert("文件大小超过指定的大小，请重新选择文件!!!");
                return;
            }
            var _formData=new FormData();
            var _files=$('#'+genId)[0].files;
            if (_files.length>1) {
                for (var i = 0; i <_files.length; i++) {
                    _formData.append('file'+i,_files[i]);
                };
            }else{
                _formData.append('file',_files[0]);
            };
            for(var i in param){
         	  _formData.append(i,param[i]);
            };
            var xhr = new XMLHttpRequest();
            if (config['beforeUpload']) {
               var flag=config['beforeUpload'].apply(config['beforeUpload'],arguments);
               if(flag){
                   xhr.open("post",config['url'], true);
                   xhr.upload.onprogress = function(ev){
                        var percent = 0;
                        if(ev.lengthComputable) {
                            percent = 100 * ev.loaded/ev.total;
                            document.getElementById('bar').style.width = percent + '%';
                        }
                    }
                   xhr.onreadystatechange=function(){
                       if (xhr.readyState==4 && xhr.status==200){
                           var retObj=JSON.parse(xhr.responseText);
                           if (config['success']) {
                                config['success'].apply(config['success'],arguments);
                           }
                           if(config['afterUpload']){
                               config['afterUpload'].apply(config['afterUpload'],arguments);
                           }
                       }else if(xhr.readyState==4 && xhr.status!=200){//返回错误
                           try{
                               var retObj=JSON.parse(xhr.responseText);
                               if(config['failed']){//已经定义失败回调函数
                                   config['failed'].apply(config['failed'],arguments);
                               }else{
                                   alert(xhr.responseText);
                               }
                           }catch(e){//返回非json格式
                               if(config['failed']){//已经定义失败回调函数
                                   config['failed'].apply(config['failed'],arguments);
                               }else{
                                   alert(xhr.responseText);
                               }
                           }finally{
                               if(config['afterUpload']){
                                   config['afterUpload'].apply(config['afterUpload'],arguments);
                               }
                           }
                       }
                   }
                   xhr.send(_formData);
               }
            };
           
        }
        
        
        /*返回对象*/
        return {
            XType:'FileUpload',
            /**
             * 返回所有的配置参数
             */
            options:config,
            disabled:disabled,
            hidden:hidden,
            /**
             * 获得文件大小信息，返回值为json数组
             */
            getFileSize:function(){
                var _files=$('#'+genId)[0].files;
                var _fileSize=[];
                for (var i = 0; i < _files.length; i++) {
                   _fileSize.push(JSON.parse('{"fileName":"'+_files[i].name+'","fileSize":"'+_files[i].size+'"}'));
                };
                return _fileSize;
            },
//            /**
//             * 获得文件名，返回值为json数组
//             */
//             getFileName:function(){
//                var _files=$('#'+genId)[0].files;
//                var _fileName=[];
//                for (var i = 0; i < _files.length; i++) {
//                   _fileSize.push(JSON.parse(_files[i].name));
//                };
//                return _fileName;
//            },
            /**
             * 移除所有的文件
             */
            removeFile:function(){
                $("#"+genId).val('');
            },
			/**
             * 增加参数
             */
			 addParam:function(obj){
            	for(var i in obj){
            		param[i]=obj[i];
            	}
            },
			/**
             * 移除所有的参数
             */
            removeParam:function(){
            	param={};
            },
            /**
             * 获取所有的文件名称，返回值为数组格式
             */
            getFileName:function(){
                var _files=$('#'+genId)[0].files;
                var _name=[];
                for (var i = 0; i < _files.length; i++) {
                         _name.push(_files[i].name);                    
                }
                return _name;
            },
            /**
             * 上传文件
             */
            upload:function(){
                uploadAttachment();
            },
            /**
             * 禁用文件上传
             */
            disabled:function(){
                $("#"+genId).attr('disabled',true);
                $('#'+genId).css('background','#eeeeee');
            },
            /**
             * 启用文件上传
             */
            enabled:function(){
                $("#"+genId).removeAttr('disabled');
                $('#'+genId).css('background','');
            },
            /**
             * 获得文件上传框的id值
             */
            getId:function(){
                return genId;
            }
        }
    }
    $.IWAP.XType['FileUpload']=$.IWAP.FileUpload;
}(jQuery))