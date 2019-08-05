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
     disabled	是否禁用	默认false
     data 树形数据
     checked	是否有选择框	默认false
     listeners	监听事件
     mode	数据来源	Local/server
     Label	文本
     Value	默认值
     url	请求URL	Mode 为server时有效
     baseParam	基础过虑参数	Mode 为server时有效
     isMultiSelect	是否多选	默认false
     id 标识 如果设置并且在dom中存在则不去创建对象，只是渲染
     hidden	是否隐藏
     renderTo	渲染对象	Dom对象或者domID
     */
    $.IWAP.Tree=function(config) {
    	var isOrg=config['isOrg']||false;
        var data = config['data'];
        var checkType=config['checkType']||{ "Y" : "ps", "N" : "ps" };
        var checked = config['checked']||false;
        var disabled= config['disabled']||false;
        var genId = config['id']|| $.IWAP.id();
        var mode =  config['mode']||'';
        var label = config['label'] || '';
        var value = config['value'] || '';
        var url = config['url'] || '';
        var baseParam=config['baseParam']||'';
        var setParam=config['SetParam']||'';
        var isMultiSelect=config['isMultiSelect']||false;
        var hidden=config['hidden']||false;
        var renderTo = null;
        var onClick=config['onClick']||IWAP.empty;
        var beforLoad=config['beforLoad']||IWAP.empty;
        var onExpand=config['onExpand']||IWAP.empty;
        var onCheck=config['onCheck']||IWAP.empty;
        var beforeClick=config['beforeClick']||IWAP.empty;
        var OnRightClick=config['OnRightClick']||IWAP.empty;
        var btn2=config['btn2']||false;//目前树后面小Btn2按钮
        var btn2Text=config['btn2Text']||"";
        var btn2style=config['btn2style']||"";//目前树后面小Btn按钮的样式有三个为b_blue, b_red, b_gray
        var btn3=config['btn3']||false;//目前树后面小Btn3按钮
        var btn3Text=config['btn3Text']||"";
        var btn3style=config['btn3style']||"";//目前树后面小Btn按钮的样式有三个为b_blue, b_red, b_gray
        var treeId = null;
        var events=config['listeners']||'';
        var rMenu=null;
        var treeObj=null;
        if(disabled){
            events=[];
        }
      //  var name = config['name'] || genId;
        if (config['renderTo']) {
            if ($.IWAP.isString(config['renderTo'])) {
                renderTo = $("#" + config['renderTo']);
            } else {
                renderTo = config['renderTo'];
            }
        } else {//如果不设置渲染对象则默认为body
            renderTo = document.body;
        }
    function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			/*for (var i=0, l=childNodes.length; i<l; i++) {
				if(typeof(childNodes[i].isParent)=='undefined'){
					childNodes[i].isParent=false;
				}else{
					if(childNodes[i].isParent.trim() =='true'){
						childNodes[i].isParent=true;
					}else{
						childNodes[i].isParent=false;
					}	
				}
			}*/
			return childNodes;
		}
        function beforeClick(treeId, treeNode, clickFlag) {
			if (!treeNode.isParent) {
				//alert("请选择父节点");
				return false;
			} else {
				return true;
			}
		}

		function beforLoad(treeId, treeNode) {
			//className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" beforeAsync ]&nbsp;&nbsp;&nbsp;&nbsp;" + ((!!treeNode && !!treeNode.name) ? treeNode.name : "root") );
			return true;
		}

        var html = [];
        /**
         * <div class="ztree pr">
         </div>
         */
        html.push('<div id="rMenu"> <ul> <li id="m_add">增加节点</li> <li id="m_del" >删除节点</li> </ul> </div>');
        html.push('<div class="tree pr"  >');
        html.push('<ul  id="'+genId+'"  class="ztree"></ul>')
        html.push('</div>');
        $(renderTo).append(html.join(''));

        var setting = {
                check: {
                            enable: checked,
                            chkboxType:checkType
                        },
                view: {
                         showIcon: true,
                         showLine: true,

                     },
                 data: {
                     simpleData: {
                         enable: true
                     }
                 },
                 callback: {
                    beforeAsync:events['beforLoad'],
                    onClick:onClick,
                    onExpand:events['eventsonExpand'],
                    onCheck:onCheck, 
                    beforeClick:events['beforeClick']
                }
	    };  
        //增加树后面的A标签用以添加小文本按钮-以数据DATA的flag判断
        setting.view.addDiyDom=function(treeId, treeNode) {
        	if(isOrg){
        		switch (treeNode.flag){
	                case 0: 
	                    $("#" + treeNode.tId + "_a").append('<a class="btn1_0">正常</a>');
	                break;
	                case 1: 
	                    $("#" + treeNode.tId + "_a").append('<a class="btn1_1">撤消</a>');
	                break;
	                case 2: 
	                    $("#" + treeNode.tId + "_a").append('<a class="btn1_2">其它</a>');
	                break;
        		};
        	}
            //增加树后面的A标签用以添加小文本按钮-自定义两个
            if(btn2){ 
                var btnid="btn2_"+treeNode.id;
                if ($("#"+btnid).length>0) return; 
                $("#" + treeNode.tId + "_a").append('<a id="'+btnid+'" class="'+btn2style+'">'+btn2Text+'</a>');
                //隐藏第一个节点的操作按钮
                $("#"+treeId+">li:first>a").children('a:last').hide();
                $("#"+btnid).on("click", function(){
                    ($.IWAP.Tree.addbtn2)&&($.IWAP.Tree.addbtn2(btnid,treeId, treeNode));
                });
            }; 
            if(btn3){ 
                var btnid="btn3_"+treeNode.id;
                if ($("#"+btnid).length>0) return; 
                $("#" + treeNode.tId + "_a").append('<a id="'+btnid+'" class="'+btn3style+'">'+btn3Text+'</a>');
                //隐藏第一个节点的操作按钮
                $("#"+treeId+">li:first>a").children('a:last').hide();
                $("#"+btnid).on("click", function(){
                    ($.IWAP.Tree.addbtn3)&&($.IWAP.Tree.addbtn3(btnid,treeId, treeNode));
                });
            };
        };

        setting.callback.onRightClick=function (event, treeId, treeNode) {     //右键事件方法
         
            if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
                treeObj.cancelSelectedNode();
                showRMenu("root", event.clientX, event.clientY);
            } else if (treeNode && !treeNode.noR) {
                treeObj.selectNode(treeNode);
                showRMenu("node", event.clientX, event.clientY);
                //console.log(treeNode)
                //右键菜单增加和删除节点
                $("#m_del").off("click").on("click" ,function(){
                    $.fn.zTree.getZTreeObj(treeId).removeNode(treeNode,true);  
                });
                $("#m_add").off("click").on("click" ,function(){
                    //alert(treeId);
                    var newNodes = {name:"新增节点"};
                    $.fn.zTree.getZTreeObj(treeId).addNodes(treeNode,newNodes); 
                });
            }
        };
        if(mode=='local'){
           // data['childs'].push({id:genId,pid: $.IWAP.id(),name:data['text'],open:true});
                 
                
        }else if(mode=='server'){
                        setting.callback.onAsyncSuccess=events['beforeClick']
                        setting.async= {
                                type:"get",
                                enable: true,
                                dataType: "text",
                                url:url,
                                autoParam:baseParam,
                                otherParam:setParam,
                                dataFilter: filter
                                } 
        }
        $.fn.zTree.init($("#"+genId), setting,data); 
        rMenu = $("#rMenu");
        treeId= genId;  
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        //treeObj.expandAll(true);
        function chkDisabled(treeObj,nodes,flag){
            for(var i=0;i<nodes.length;i++){
                if(typeof (nodes[i]["children"])=='undefined'){
                    treeObj.setChkDisabled(nodes[i],flag)
                }else{
                    treeObj.setChkDisabled(nodes[i],flag)
                    chkDisabled(treeObj,nodes[i]["children"],flag)
                }
            }
        }
       if(disabled){
        	var nodes = treeObj.getNodes();
            chkDisabled(treeObj,nodes,true);
        }
        if(hidden){
        	var nodes = treeObj.getNodes();
        	treeObj.hideNodes(nodes);
        }

        if(value!=''){
        	for(var j=0;j<value.length;j++){
        		var nodes = treeObj.getNodesByParam("id", value[j], null);
        			for(var i=0;i<nodes.length;i++){
        			treeObj.checkNode(nodes[i], true, true);	
        		}
        		
        	}
        }

        
        function showRMenu(type, x, y) {            //显示菜单
            $("#rMenu ul").show();
            if (type=="root") {
                $("#m_del").hide();
                $("#m_add").hide();
                //$("#m_unCheck").hide();
            } else {
                $("#m_del").show();
                $("#m_add").show();
                //$("#m_unCheck").show();
            }
            rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});
            $("body").bind("mouseup", onBodyMouseDown);
        }
        
     
        function hideRMenu() {                   //隐藏右键菜单
            if (rMenu) rMenu.css({"visibility": "hidden"});
            $("body").unbind("mousedown", onBodyMouseDown);
        }
        function onBodyMouseDown(event){        //绑定事件
            //if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
                rMenu.css({"visibility" : "hidden"});
            //}
        }

        /*返回对象*/
        return {
            XType:'Tree',
            options:config,
            getValue:function(){
            	var nodes=treeObj.getCheckedNodes(true);
            	var res = new Array();
            	 for(var i=0;i<nodes.length;i++){
            		 res[i]=nodes[i]["id"];
            	 }
            	 return res;
            },
            getChecked:function(){
            	return treeObj.getCheckedNodes(true);
            },
            isChecked:function(){
            	return treeObj.getCheckedNodes(true).length>0?true:false;
            },
            setCheck:function(leftid,bealean){
            	var nodes = treeObj.getNodesByParam("id", leftid, null);
                for (var i=0, l=nodes.length; i < l; i++) {
                	treeObj.checkNode(nodes[i], bealean, bealean);
                }
            },
            SetParam:function(param){
            	if(mode=='server'){
            		setParam=param;
            	}
            },
            Load:function(data,append){
                if(append){
                    for(var j=0;j<data.length;j++){
                        var nodes= treeObj.getNodesByParam("id", data[j]["pId"], null);
                        for(var i=0;i< nodes.length;i++){
                            treeObj.addNodes(nodes[i],data[j]);
                        }
                    }
                }else{
                    $.fn.zTree.init($("#"+genId), setting,data);
                }
            },
            disabled:function(){
            	var nodes = treeObj.getNodes();
                chkDisabled(treeObj,nodes,true)
            },
            hidden:function(){
            	var nodes = treeObj.getNodes();
            	treeObj.hideNodes(nodes);
            },
            enabled:function(){
            	var nodes = treeObj.getNodes();
                chkDisabled(treeObj,nodes,false)
            },
            getId:function(){
            	if($("#ctx_"+genId).val()==''){
                    return "ctx_"+genId;
               }else{
                  return genId;
               }
            },
            hiddenRmenu:function(){
                rMenu.css('display', 'none');
            },
            setCheckAll:function(isChecked){
            	treeObj.checkAllNodes(isChecked);
            }
        }
    }
    $.IWAP.XType['Tree']=$.IWAP.Tree;
}(jQuery))