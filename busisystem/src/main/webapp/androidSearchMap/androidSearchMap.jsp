	<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1, maximum-scale=3, minimum-scale=1, user-scalable=no">
<title>TRU Mate搜索地图</title>
<script>new function(){var _self=this;_self.width=750;_self.fontSize=100;_self.widthProportion=function(){var p=(document.body&&document.body.clientWidth||document.getElementsByTagName("html")[0].offsetWidth)/_self.width;return p>1?1:p};_self.changePage=function(){document.getElementsByTagName("html")[0].setAttribute("style","font-size:"+_self.widthProportion()*_self.fontSize+"px !important")};_self.changePage();window.addEventListener("resize",function(){_self.changePage()},false)};
</script>
<style>
* {
	margin: 0px;
	padding: 0px;
}
div, span {
	box-sizing: border-box;
}
body {
	-webkit-tap-highlight-color: rgba(0,0,0,0);
}
.centerList {
	width: 7.5rem;
	padding: 0.62rem 0.1rem 0.44rem;
}
.centerBox {
	overflow: hidden;
}
.centerBoxLeft .keyword, .centerBoxLeft>i, .centerBoxLeft .browsBox ul li i, .centerBoxLeft .browsBox ul li a {
	float: left;
}
.centerBoxLeft .browse {
	float: left;
	border-left: 1px solid #e0e0e0;
}
.centerBoxLeft .browsBox ul {
	border-left: 1px solid #e0e0e0;
}
.centerBoxRight .keyword, .centerBoxRight>i, .centerBoxRight .browsBox ul li i, .centerBoxRight .browsBox ul li a {
	float: right;
}
.centerBoxRight .browse {
	float: right;
	border-right: 1px solid #e0e0e0;
}
.centerBoxRight .browsBox {
	text-align: right;
	margin-left: -1.2rem;
}
.centerBoxRight .browsBox ul {
	border-right: 1px solid #e0e0e0;
}
.centerBoxRight .browsBox ul li i {
	margin-right: 0;
	margin-left: 0.1rem;
}
.centerBoxRight.centerBox>i em {
	float: left;
	margin-right: 0;
	margin-left: -0.08rem;
}
.centerBoxRight .browse ul li {
	text-align: right;
	width:5rem;
}
.keyword {
	width: 1.2rem;
	margin-top: 0.9rem;
}
.keyword span {
	display: table-cell;
	width: 1.2rem;
	height: 1.2rem;
	border-radius: 0.6rem;
	padding: 0 0.12rem;
	font-size: 0.2rem;
	color: #fff;
	vertical-align: middle;
	text-align: center;
	line-height: 0.26rem;
}
.keyword span em {
	display: inline-block;
	max-width: 0.96rem;
	font-style: normal;
	font-size: 0.24rem;
	word-break: break-all;
}
.centerBox>i {
	height: 1px;
	min-height: 1px;
	background: #e0e0e0;
	width: 0.25rem;
	margin-top: 1.5rem;
}
.centerBox>i em {
	float: right;
	width: 0.1rem;
	height: 0.1rem;
	border-radius: 0.05rem;
	margin: -0.06rem -0.08rem 0 0;
	border: 0.03rem solid #fac864;
	background: #FFF;
	position: relative;
	z-index: 3;
}
.browse {
	width: 4.4rem;
	border-bottom: 1px solid #e0e0e0;
	min-height: 3.08rem;
}
.browsBox {
	width: 5.6rem;
	overflow: hidden;
	font-size: 0;
}
.browsBox>i {
	display: inline-block;
	vertical-align: middle;
	width: 0.35rem;
	height: 1px;
	background: #e0e0e0;
}
.browsBox ul {
	display: inline-block;
	vertical-align: middle;
	list-style: none;
	margin: 0.6rem 0;
	min-height:1px;
}
.browsBox ul.browsBoxUl1{
	height:1px;
}
.browsBox ul.browsBoxUl2{
	height: 0.88rem;
}
.browsBox ul.browsBoxUl3{
	height: 1.78rem;
}
.browsBox ul li {
	width: 5.2rem;
	margin-bottom: 0.3rem;
	display: block;
	height: 0.6rem;
}
.browsBox ul li:first-child {
	margin-top: -0.29rem;
}
.browsBox ul li:last-child {
	margin-bottom: -0.3rem;
}
.browsBox ul li a {
	display: inline-block;
	max-width: 4.16rem;
	padding: 0 0.22rem;
	height: 0.58rem;
	font-size: 0.24rem;
	color: #606060;
	text-decoration: none;
	border: 1px solid #e0e0e0;
	line-height: 0.58rem;
	border-radius: 0.3rem;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
.browsBox ul li a em{display: inline-block;width: .4rem;height: .4rem; vertical-align: top; margin:.1rem .1rem 0 0;position: relative; z-index:1;}
.browsBox ul li a em.curr0{ background:url(cu00.png) no-repeat center center; background-size:100%;}
.browsBox ul li a em.curr1{ background:url(cu01.png) no-repeat center center; background-size:100%;}
.centerBoxRight .browsBox ul li a em{ float:right; margin:.1rem 0 0 .1rem;}
.browsBox ul li a:active, .more a {
	color: #3d9bff;
	border-color: #3d9bff
}
.browsBox ul li i {
	width: 0.22rem;
	height: 1px;
	background: #e0e0e0;
	margin: 0.29rem 0.1rem 0 0;
}
.more {
	padding-left: 0.7rem;
}
.more a {
	display: inline-block;
	padding: 0 0.22rem;
	height: 0.58rem;
	font-size: 0.24rem;
	color: #606060;
	text-decoration: none;
	border: 1px solid #e0e0e0;
	line-height: 0.58rem;
	border-radius: 0.3rem;
	vertical-align: top;
}
.centerBoxRight .more {
	text-align: right;
	padding-right: 0.7rem;
}
.bgcolor0 .keyword span {
	background: #fac864;
}
.bgcolor1 .keyword span {
	background: #fa8cb4;
}
.bgcolor1>i em {
	border-color: #fa8cb4;
}
.bgcolor2 .keyword span {
	background: #64bee6;
}
.bgcolor2>i em {
	border-color: #64bee6;
}
.bgcolor3 .keyword span {
	background: #beaa82;
}
.bgcolor3>i em {
	border-color: #beaa82;
}
.bgcolor4 .keyword span {
	background: #82c85a;
}
.bgcolor4>i em {
	border-color: #82c85a;
}
.bgcolor5 .keyword span {
	background: #eb7d5f;
}
.bgcolor5>i em {
	border-color: #eb7d5f;
}
.bgcolor6 .keyword span {
	background: #6ec8be;
}
.bgcolor6>i em {
	border-color: #6ec8be;
}
</style>
</head>

<body>
<div style="display:none"><img src="../searchMap/img/300.png"></div>
<div class="centerList"></div>
<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.8.3/jquery.min.js"></script> 
<script type="text/javascript" src="../md5.js"></script> 
<script type="text/javascript" src="../searchMap/html2canvas.js"></script> 
<script>
        function getNowFormatDate(){var date=new Date();var seperator1="-";var seperator2=":";var month=date.getMonth()+1;var strDate=date.getDate();if(month>=1&&month<=9){month="0"+month}if(strDate>=0&&strDate<=9){strDate="0"+strDate}var currentdate=date.getFullYear()+seperator1+month+seperator1+strDate+" "+date.getHours()+seperator2+date.getMinutes()+seperator2+date.getSeconds();
        return currentdate};
        var time= getNowFormatDate();
        var m=hex_md5("busiSystemMate" + time);
        var id=window.location.href.split('?')[1].match(/id=[0-9]+/)[0].split('=')[1];
        var map=window.location.href.split('?')[1].match(/map=[a-zA-Z]+/)[0].split('=')[1];
		var isRn=window.location.href.split('?')[1].match(/isRn=[a-zA-Z]+/)[0].split('=')[1];
        if(/share/.test(window.location.search)){
            var share=true;
        }
        var data={
            busiBlock:"searchBlock",
            Md5Str:m,
            time:time,
            busiCode:"querySearchMapsDatas",
            id:id
        }
        $.ajax({
            type : "get",
            dataType:'json',
            url: "../entry/busiEntry.do",
            data: {
            paramObject:JSON.stringify(data)
            },
            success:function(data){
                if(data.result.code==1){
                    var _html="";
                    var dataHtml=data.resultData;
                    $('div.centerList').attr('data-id',dataHtml.maps.id).attr('firstKeyword',dataHtml.maps.firstKeyword);
                    for(var i=0;i<dataHtml.mapKeywords.length;i++){
                        if(i%2==0){
                           var box_html='<div class="centerBox centerBoxLeft bgcolor'+i%7+'">'
                        }else{
                           var box_html='<div class="centerBox centerBoxRight bgcolor'+i%7+'">';
                        }
                        _html+=box_html+'<div class="keyword" data-id="'+dataHtml.mapKeywords[i].id+'" data-source="'+dataHtml.mapKeywords[i].source+'"><span><em data-mapKeywords="'+dataHtml.mapKeywords[i].keyword.replace(/</g,"&lt;").replace(/>/g,"&gt;")+'">'+dataHtml.mapKeywords[i].keyword.replace(/</g,"&lt;").replace(/>/g,"&gt;").substring(0,20)+'...</em><em>('+(function(id,i,dataHtml){var ss=0;dataHtml.mapDatas.forEach(function(o){if(o.keywordId==id){ss++}});return ss})(dataHtml.mapKeywords[i].id,i,dataHtml)+')</em></span></div>'+
                                            '<i><em></em></i>'+
                                            '<div class="browse">'+
                                                '<div class="browsBox">'+
                                                    (function(i){
                                                        if(i%2==0)return "<i></i>";
                                                        else return "";
                                                        })(i)+
                                                    '<ul>'+(function(id,i,dataHtml){
                                                                var li_html="";
                                                                var list_index=0;
                                                                for(var n=0;n<dataHtml.mapDatas.length;n++){
                                                                    if(dataHtml.mapDatas[n].keywordId==id){
                                                                        list_index++;
                                                                        if(list_index<4){
																			if(i%2==0){
                                                                           		li_html+='<li><i></i><a href="'+dataHtml.mapDatas[n].url+'" data-id="'+dataHtml.mapDatas[n].id+'"><em class="curr'+dataHtml.mapDatas[n].keepFlag+'"></em>'+dataHtml.mapDatas[n].title.replace(/</g,"&lt;").replace(/>/g,"&gt;")+'</a></li>';
																			}else{
																				li_html+='<li><i></i><a href="'+dataHtml.mapDatas[n].url+'" data-id="'+dataHtml.mapDatas[n].id+'"><em class="curr'+dataHtml.mapDatas[n].keepFlag+'"></em>'+dataHtml.mapDatas[n].title.replace(/</g,"&lt;").replace(/>/g,"&gt;")+'</a></li>';
																			}
                                                                        }
                                                                    }
                                                                    if(list_index==4){
                                                                        li_html+='</ul>'+(function(i){
                                                                        if(i%2==1)return "<i></i>";
                                                                        else return "";
                                                                        })(i)+
                                                                        '</div><div class="more"><a href="javascript:;">更多 &gt;&gt;</a></div>';
                                                                        break;
                                                                    }
                                                                    if(dataHtml.mapDatas.length-1==n){
                                                                        li_html+='</ul>'+(function(i){
                                                                        if(i%2==1)return "<i></i>";
                                                                        else return "";
                                                                        })(i)+
                                                                        '</div>';
                                                                    }
                                                                }
                                                                return li_html;
                                                    })(dataHtml.mapKeywords[i].id,i,dataHtml)+
                                            '</div>'+
                                        '</div>';
                    }
                    $('div.centerList').html(_html);
					$('.browsBox ul').each(function(){
						$(this).attr('class','browsBoxUl'+$(this).find('li').length);
					});
                }
            }
        });
    $('body').on('touchstart','.keyword span',function(){
        if(map=="true"&&isRn=="true"){
			WebViewBridge.send("intentSearchList,"+$(this).find('em').attr('data-mapKeywords')+","+$(this).parents('.keyword').attr('data-source'));
        }else if(map=="true"){
			client.intentSearchList($(this).find('em').attr('data-mapKeywords'),$(this).parents('.keyword').attr('data-source'));
		}
    });
    $('body').on('touchstart','.more a',function(){
        var keyworddom=$(this).parents('.browse').siblings('.keyword');
		if(isRn=="true"){
			WebViewBridge.send("loadMoreList,"+keyworddom.attr('data-id')+","+keyworddom.find('em').attr('data-mapKeywords'));
		}else{
			client.loadMoreList(keyworddom.attr('data-id'),keyworddom.find('em').attr('data-mapKeywords'));
		}
	});
    function saveSearchShare(userId,shareTo){
        var saveFile = function(data){
        var url = encodeURIComponent(data);
        var data={
        busiBlock:"searchBlock",
        busiCode:"saveSearchShare",
        userId:userId,
        mapId: $('div.centerList').attr('data-id'),
        shareTo:shareTo,
        picUrl:url,
        device:"android",
        language:"ZH"
        }
        $.ajax({
        type: "post",
        url: "../saveCanvas.do",
        data: data,
        dataType:"text",
        success: function(result){
        //console.log(result);
        },
        error:function(result){
        //console.log(result);
        }
        });
        }
        html2canvas(document.querySelector(".centerList"), {
        allowTaint: true,
        taintTest: false,
        onrendered: function(canvas) {
        var imgData = canvas.toDataURL();
        saveFile(imgData);
        }
        });
    };
	$('body').on('touchstart','.browsBox ul li a em',function(){
		WebViewBridge.send("mapMark,"+($(this).parent('a').attr('data-id'))+","+($(this).hasClass('curr0')?0:1));
		return false;
	});
	if (WebViewBridge) {
		WebViewBridge.onMessage = function (message) {
			$('.browsBox ul li a').each(function(){
				if(message==$(this).attr('data-id')){
					var _class=$(this).find('em').hasClass('curr0')?"curr1":"curr0";
					$(this).find('em').attr('class',_class);
					var _thisLi=$(this).parent('li');
					var c=_thisLi.clone(true);
					if(_class=="curr1"){
						_thisLi.parent('ul').prepend(c);
					}else{
						if(_thisLi.parent('ul').find('em.curr1:last').length>0){
							_thisLi.parent('ul').find('em.curr1:last').parents('li').after(c);
						}else{
							_thisLi.parent('ul').prepend(c);
						}
					}
					_thisLi.remove();
				}
			});
		};
	}
	$('body').on('touchstart',function(){});
</script>
</body>
</html>
