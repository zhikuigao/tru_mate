<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/jquery.mCustomScrollbar.css"/>
    <link rel="stylesheet" href="css/searchMap.css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<title>TRU Mate搜索地图</title>
	<meta name="description" content="TRU Mate是一款面向工程师的工作辅助软件，基于Hook技术，帮用户打造了面向个性化的解决方案，随时随地触手可，为 用户的工作带来了便捷； 基于人工智能、自然语言处理等技术，为用户提供精准推荐，减轻用户在庞大的知识网络中搜索相关的数据；以及搜索地图，帮助用户记录搜索问题的轨迹，找到解决方案，分享给小伙伴，达成知识共享；是工程师的小秘书，好帮手。">
	<meta name="title" content="TRU Mate是一款面向工程师的工作辅助软件，基于Hook技术，帮用户打造了面向个性化的解决方案，随时随地触手可，为 用户的工作带来了便捷； 基于人工智能、自然语言处理等技术，为用户提供精准推荐，减轻用户在庞大的知识网络中搜索相关的数据；以及搜索地图，帮助用户记录搜索问题的轨迹，找到解决方案，分享给小伙伴，达成知识共享；是工程师的小秘书，好帮手。">
	<meta name="sharecontent" data-msg-content=""/>
</head>
<body>
<div id="popupMap">
    <div style="position: absolute;opacity: 0;filter: alpha(opacity=0);"><img src="images/300.png"></div>
    <div id="popupMapBox">
        <div class="mapcenter"></div>
    </div>
</div>

<script type="text/javascript" src="javascript/jquery.min.js"></script>
<script type="text/javascript" src="javascript/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="javascript/html2canvas.js"></script>
<script type="text/javascript" src="javascript/share.js"></script>
<script type="text/javascript" src="../md5.js"></script>
<script>
function getNowFormatDate(){var date=new Date();var seperator1="-";var seperator2=":";var month=date.getMonth()+1;var strDate=date.getDate();if(month>=1&&month<=9){month="0"+month}if(strDate>=0&&strDate<=9){strDate="0"+strDate}var currentdate=date.getFullYear()+seperator1+month+seperator1+strDate+" "+date.getHours()+seperator2+date.getMinutes()+seperator2+date.getSeconds();return currentdate};
</script>
<script>
    $(function(){
		var time= getNowFormatDate();
        var m=hex_md5("busiSystemMate" + time);
        var id=window.location.href.split('?')[1].match(/id=[0-9]+/)[0].split('=')[1];
		if(window.parent.window.require)var ipcRenderer =window.parent.window.require('electron').ipcRenderer;
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
                console.log('asas   '+data);
                if(data.result.code==1){
                    var html=data.resultData;
                    _html="";
                    $('meta[name=description]').attr('content','关于“'+html.maps.firstKeyword+'”的搜索分享，让搜索更加有趣');
                    $('meta[name=sharecontent]').attr('data-msg-content','关于“'+html.maps.firstKeyword+'”的搜索分享，让搜索更加有趣');
                    $('.mapcenter').attr('data-id',html.maps.id).attr('data-userid',html.maps.userId);
                    for(var i=0;i<html.mapKeywords.length;i++) {
                        if(i==0){
                            var fir_html='<div class="maplistbox fir">\
                                            <div class="maptitle" data-firstkeyword="'+html.maps.firstKeyword+'" data-id="'+html.maps.id+'">\
                                                <span>搜索地图</span><i class="heng"></i>\
                                            </div>';
                        }else{
                            var fir_html='<div class="maplistbox">';
                        }
                        _html+=fir_html+'<div class="keyword">\
                                            <a href="javascript:;" title="'+html.mapKeywords[i].keyword+'" data-id="'+html.mapKeywords[i].id+'" data-source="'+html.mapKeywords[i].source+'"><span>('+(function(id,i,dataHtml){var ss=0;dataHtml.mapDatas.forEach(function(o){if(o.keywordId==id){ss++}});return ss})(html.mapKeywords[i].id,i,html)+')</span>'+(i+1)+'、'+html.mapKeywords[i].keyword.replace(/</g,"&lt;").replace(/>/g,"&gt;")+'</a><i class="shu"></i>\
                                        </div>\
                                        <div class="browse">\
                                            <i class="heng"></i>\
                                            <ul>'+(function(id,html){
                                    var _html=""
                                    for(var n=0;n<html.mapDatas.length;n++){
                                        if(html.mapDatas[n].keywordId==id){
                                            _html+='<li><i class="curr'+html.mapDatas[n].keepFlag+'" data-id="'+html.mapDatas[n].id+'"></i><a href="'+html.mapDatas[n].url+'" title="'+html.mapDatas[n].title+'">'+html.mapDatas[n].title.replace(/</g,"&lt;").replace(/>/g,"&gt;")+'</a></li>'
                                        }
                                    }
                                    return _html+'</ul>'
                                })(html.mapKeywords[i].id,html)+
                            '</div>\
                        </div>'
                    }
                    $('.mapcenter').html(_html);
                    if(window.parent.document.getElementsByTagName('iframe').length!=0)window.parent.bindZ();
                    resize();
                }
                $('.browse a').click(function(){
                    if(window.parent.document.getElementsByTagName('iframe').length!=0){
                        var showmaindata={
                            url:$(this).attr('href'),
                            mapUrl:$('.mapcenter').attr('data-id'),
                            showNav:"detail",
                            tid:-1
                        }
						ipcRenderer.send('showContextFrameWindow',showmaindata);
                    }else{
						window.open($(this).attr('href'));
					}
					return false;
                })
                $('div.browse').mCustomScrollbar({
                    theme:"minimal-dark"
                });
                $("div.mapcenter").mCustomScrollbar({
                    theme:"minimal-dark"
                });
            }
        });
        $('div.mapcenter').on('click','div.keyword a',function(){
			var data={
					firstkeyword:$('div.maptitle').attr('data-firstkeyword'),
					mapId:$('div.maptitle').attr('data-id'),
					keyword:$(this).text().replace(/^\([0-9]+\)[0-9]+、/,""),
					keywordId:"00"+new Date().getTime(),
					sourceId:$(this).attr('data-source')
				}
			ipcRenderer.send('showMainFrameWindow',data);
        });
        $(window).resize(function(){
            resize();
        });
    });
    function resize(){
        $('.browse ul li a').css('width',$(window).width()-360);
        $('.mapcenter').height($(window).height()-20);
    }
</script>
</body>
</html>