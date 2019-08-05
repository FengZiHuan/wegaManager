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
     text	文本	标题
     icon 图标  默认false
     items	子菜单数据
     click	点击菜单事件
     tip	提示信息
     url	链接地址 False
     disabled 是否禁用 默认 false
     hidden   是否隐藏 默认false
     renderTo	渲染对象	Dom对象或者domID
     */
    $.IWAP.Menu=function(config) {
        var items= config['items']||[]
        var genId = config['id']|| $.IWAP.id();
        var click =  config['click']||IWAP.emptyFn;
        var renderTo = null;
        var treeId = null;
        var menustyle=config['menustyle']||[];
        var disabled=(typeof (config['disabled'])=='undefined')?false:config['disabled'];
        var hidden=(typeof (config['hidden'])=='undefined')?false:config['hidden'];
        var events=config['listeners']||[];
        var landscape=config['landscape']||'column';                 //设置菜单column还是row
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
         * <div class="ztree pr">
         </div>
         */
        var setting = {

            view: {
                showLine: false,
                showIcon: false,
                selectedMulti: false,
                dblClickExpand: false,
                showTitle: true,
                nameIsHTML: true,
                addDiyDom: addDiyDom
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeClick: beforeClick

            }
        };
        if(!config['callback']){
        	config['callback']={
                beforeClick: beforeClick
            }
        }else{
        	$.IWAP.apply(config['callback'],{beforeClick: beforeClick});	
        }
        $.IWAP.apply(setting,config);
        //$.IWAP.apply(setting,config["key"]);

            if (menustyle) {
                if (menustyle=='menustyle') {
                    html.push('<div class="tree_menubox">');
                }else if (menustyle=='menustyle2') {
                    html.push('<div class="tree_menubox2">');
                }else{
                	 html.push('<div class="tree_menubox2">');
                }
            }else{html.push('<div class="tree_menubox">')};
            
            html.push('<ul id="'+genId+'" xtype="Menu" class="ztree '+landscape+'"  ></ul>')
            //html.push('<ul id="'+genId+'"  class="ztree cross"></ul>')
            html.push('</div>');
            $(renderTo).append(html.join(''));
            $.fn.zTree.init($("#"+genId), setting,items);              //初始化菜单
            treeId= genId;
            function leafOnExpand(treeId,treeNode){
                alert("expand");
            }
           //初始化菜单之前执行方法
           function addDiyDom(treeId, treeNode) {
                var spaceWidth = 5;
                var switchObj = $("#" + treeNode.tId + "_switch"),
                icoObj = $("#" + treeNode.tId + "_ico");
                switchObj.remove();
                icoObj.before(switchObj);
                if (treeNode.level > 1) {
                      var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level)+ "px'></span>";
                      switchObj.before(spaceStr);
                }
           }

          var treeObj = $.fn.zTree.getZTreeObj(treeId);
          //再点击菜单之前触发事件 该方法是点击菜单时候把提示信息显示出来
          function beforeClick(treeId, treeNode) {
              if(!disabled){
                  var zTree = $.fn.zTree.getZTreeObj(treeId);
                  if(typeof (treeNode.tip)!='undefined'){
                      $("#"+treeNode.tId +"  a").attr("title",treeNode.tip)
                  }
                  var childrens=treeNode.children;
                  if (childrens) {
                        for(var i=0;i<childrens.length;i++){
                          zTree.expandNode(childrens[i], false, false, true,true);   //把点击子节点都关闭
                        }
                  }
                      
                  expandFlag=!treeNode.open;
                  zTree.expandNode(treeNode, expandFlag, false, true,true);     //打开点击节点
                  return true;
              }else{
                  var zTree = $.fn.zTree.getZTreeObj(treeId);
                  if(typeof (treeNode.tip)!='undefined'){
                      $("#"+treeNode.tId +"  a").attr("title",treeNode.tip)
                  }
                  return true;
              }

          }

        //是否隐藏菜单
        if(hidden){
            var nodes = treeObj.getNodes();
            treeObj.hideNodes(nodes);
        }


        var treeHover=$("#"+genId);
        //鼠标移动到菜单时候改变背景颜色
        　treeHover.hover(function () {
             
            if (!treeHover.hasClass("showIcon")) {
                treeHover.addClass("showIcon");
            }
        }, function() {
            treeHover.removeClass("showIcon");
        });　


        /*返回对象*/
        return {
            XType:'Menu',
            options:config,
           /*
           *user:Menu.getText(id)
           *获取指定id的菜单的菜单项名称
           *param id菜单的id
           *return {*}
            * */
            getText:function(id){
                var nodes = treeObj.getNodesByParam("id", id, null);
            	var res ;
            	 for(var i=0;i<nodes.length;i++){
            		 res+=nodes[i]["name"];
            	 }
            	 return res;
            },
            /*
             *user:Menu.setText(id,text)
             *设置指定id的菜单中菜单项的名称
             *param id菜单中菜单项id text 菜单项的名称
             * */
            setText:function(id,text){
                var nodes = treeObj.getNodesByParam("id", id, null);
                for(var i=0;i<nodes.length;i++) {
                    nodes[i].name = text;
                    treeObj.updateNode(nodes[i]);
                }
            },
            /*
             *user:Menu.addCls(id,css)
             *设置整个菜单的样式
             *param css 样式名称
             * */
            addCls:function(id,css){
                var nodes = treeObj.getNodesByParam("id", id, null);
                $("#"+nodes[0]['tId']).addClass(css);
            	//return treeObj.getCheckedNodes(true).length>0?true:false;
            },
            /*
             *user:Menu.getHref(id)
             *获取指定id的菜单的菜单项链接
             *param id菜单的id
             *return {*}
             * */
            getHref:function(id){
                var href="";
                var nodes = treeObj.getNodesByParam("id", id, null);
                for(var i=0;i<nodes.length;i++){
                   href += $("#"+nodes[i]["tId"]+ " a").attr("href");
                }
                return href;
            },
            /*
             *user:Menu.getHref(id,href)
             *设置指定id的菜单的菜单项链接
             *param id菜单的id href菜单项链接
             * */
            setHref:function(id,href){
                var nodes = treeObj.getNodesByParam("id", id, null);
                for(var i=0;i<nodes.length;i++){
                    $("#"+nodes[i]["tId"]+ " a").attr("href",href);
                }
            },
            /*
             *user:Menu.addMenu(data,append)
             *菜单增加菜单项
             *param data菜单项信息 格式和items格式一样 append为true 菜单项是增加在之前菜单上 append 为false 清空之前的菜单，生成新的菜单
             *return {*}
              * */
            addMenu:function(data,append){
                if(append){
                	for(var j=0;j<data.length;j++){
                        var nodes= treeObj.getNodesByParam("id", data[j]["pId"], null);
                        for(var i=0;i< nodes.length;i++){
                            treeObj.addNodes(nodes[i],data[j]);
                        }
                	}
                    return true;
                }else{
                	$.fn.zTree.init($("#"+genId), setting,data);
                    return true;
                }
            },
            /*
             *user:Menu.hidden()
             *隐藏菜单
             * */
            hidden:function(){
            	var nodes = treeObj.getNodes();
            	treeObj.hideNodes(nodes);
            },
            /*
             *user:Menu.getId()
             *获取菜单id，不是菜单项id
             * */
            getId:function(){
            	if($("#ctx_"+genId).val()==''){
                    return "ctx_"+genId;
               }else{
                  return genId;
               }
            }
        }
    }
    $.IWAP.XType['Menu']=$.IWAP.Menu;
}(jQuery))