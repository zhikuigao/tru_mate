<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <link rel="stylesheet" href="jquery.mCustomScrollbar.css"/>
    <link rel="stylesheet" href="searchMap.css"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<title>TRU Mate搜索地图</title>
	<meta name="description" content="TRU Mate是一款面向工程师的工作辅助软件，基于Hook技术，帮用户打造了面向个性化的解决方案，随时随地触手可，为 用户的工作带来了便捷； 基于人工智能、自然语言处理等技术，为用户提供精准推荐，减轻用户在庞大的知识网络中搜索相关的数据；以及搜索地图，帮助用户记录搜索问题的轨迹，找到解决方案，分享给小伙伴，达成知识共享；是工程师的小秘书，好帮手。"> 
	<meta name="title" content="TRU Mate是一款面向工程师的工作辅助软件，基于Hook技术，帮用户打造了面向个性化的解决方案，随时随地触手可，为 用户的工作带来了便捷； 基于人工智能、自然语言处理等技术，为用户提供精准推荐，减轻用户在庞大的知识网络中搜索相关的数据；以及搜索地图，帮助用户记录搜索问题的轨迹，找到解决方案，分享给小伙伴，达成知识共享；是工程师的小秘书，好帮手。">
	<meta name="sharecontent" data-msg-content=""/>
</head>
<body>
<div id="popupMap">
    <div style="display:none"><img src="img/300.png"></div>
    <div id="popupMapBox">
        <div class="mapcenter"></div>
    </div>
</div>

<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.8.3/jquery.min.js"></script>
<script type="text/javascript" src="jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="html2canvas.js"></script>
<script type="text/javascript" src="share.js"></script>
<script type="text/javascript" src="../md5.js"></script>
<script>
var config = {
        url:"", /*获取URL，可加上来自分享到QQ标识，方便统计*/
        showcount:'1',/*是否显示分享总数,显示：'1'，不显示：'0' */
        desc:'快来分享吧!', /*分享理由(风格应模拟用户对话),支持多分享语随机展现（使用|分隔）*/
        title:'TRU Mate搜索地图', /*分享标题(可选)*/
        summary:'你的思维导图，你的搜索能手', /*分享摘要(可选)*/
        pics:'http://mate.jwis.cn/busisystem/searchMap/img/icon_100.png', /*分享图片(可选)*/
        flash: 'http://my.tv.sohu.com/us/245572917/84440945.shtml', /*视频地址(可选)*/
        style:'203',
        target : '_blank',
    };
	$(".qqzone").share(config,"qzone");
    $(".qq").share(config,"qq");
    function getNowFormatDate(){var date=new Date();var seperator1="-";var seperator2=":";var month=date.getMonth()+1;var strDate=date.getDate();if(month>=1&&month<=9){month="0"+month}if(strDate>=0&&strDate<=9){strDate="0"+strDate}var currentdate=date.getFullYear()+seperator1+month+seperator1+strDate+" "+date.getHours()+seperator2+date.getMinutes()+seperator2+date.getSeconds();
        return currentdate};
</script>
<script>
    $(function(){
        var time= getNowFormatDate();
        var m=hex_md5("busiSystemMate" + time);
        var id=window.location.href.split('?')[1].match(/id=[0-9]+/)[0].split('=')[1];
        var map=window.location.href.split('?')[1].match(/map=[a-zA-Z]+/)[0].split('=')[1];
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
                                            _html+='<li><i></i><a href="'+html.mapDatas[n].url+'" title="'+html.mapDatas[n].title+'">'+html.mapDatas[n].title.replace(/</g,"&lt;").replace(/>/g,"&gt;")+'</a></li>'
                                        }
                                    }
                                    return _html+'</ul>'
                                })(html.mapKeywords[i].id,html)+
                            '</div>\
                        </div>'
                    }
                    $('.mapcenter').html(_html);
                }
                const {ipcRenderer} =require('electron');
                $('.browse a').click(function(){
                    console.log(ipcRenderer)
                    if(window.parent.document.getElementsByTagName('iframe').length!=0){
                        let showmaindata={
                            url:$(this).attr('href'),
                            mapUrl:window.location.href,
                            showNav:"detail",
                            tid:-1
                        }

                        return false;
                    }
                })
                $('div.browse').mCustomScrollbar({
                    theme:"minimal-dark"
                });
                $("div.mapcenter").mCustomScrollbar({
                    theme:"minimal-dark"
                });
            }
        });
        $('.copy_a').attr('data-url',"http://"+window.location.host+window.location.pathname+"?id="+id+"&map=false");

        //微信二维码显示
        $('.weixin').toggle(function(){
           $(this).find('span').stop().slideDown(300);
            if(map=="true"||share=="true"){
                var saveFile = function(data){
                var url = encodeURIComponent(data);
                var data={
                busiBlock:"searchBlock",
                busiCode:"saveSearchShare",
                userId:$('.mapcenter').attr('data-userid'),
                mapId: $('.mapcenter').attr('data-id'),
                shareTo:"1",
                picUrl:url,
                device:"pc",
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
                };
                html2canvas(document.querySelector(".mapcenter"), {
                allowTaint: true,
                taintTest: false,
                onrendered: function(canvas) {
                var imgData = canvas.toDataURL();
                saveFile(imgData);
                }
                });
            }
        },function(){
            $(this).find('span').stop().slideUp(300);
        });
        //复制地址
        $('.copy_a').click(function(){
            var url=$(this).attr('data-url');
            $('body').append("<textarea>"+url+"</textarea>")
            $('textarea').select();
            document.execCommand("Copy");
            $('textarea').remove();
            $('body').append("<p id='spand' style='position: fixed; width: 300px; background: rgba(0,0,0,.6); color: #fff; line-height: 50px; text-align: center; border-radius: 10px; bottom: 30px;left: 50%; margin-left: -150px; display: none;'>复制成功</p>");
            $('#spand').fadeIn(200,function(){
                setTimeout(function(){
                    $('#spand').fadeOut(500,function(){$('#spand').remove()});
                },2000);
            })
        });
        $('div.mapcenter').on('click','div.keyword a',function(){
            if(map){
                var data={
                        firstkeyword:$('div.maptitle').attr('data-firstkeyword'),
                        mapId:$('div.maptitle').attr('data-id'),
                        keyword:$(this).text().replace(/^\([0-9]+\)[0-9]+、/,""),
                        keywordId:"00"+new Date().getTime(),
                        sourceId:$(this).attr('data-source')
                    }
                var ipcRenderer =window.parent.window.ipcRenderer;
                    console.log(ipcRenderer);
                ipcRenderer.send('showMainFrameWindow',data,"showWindow");
                window.showWindow=function(data){
                    console.log(data);
                }
                //window.dialog.showSearchKeyword('TRUSearchList?dataMap='+JSON.stringify(data));
            }
        });
    });
    $(window).resize(function(){
        var _h=$(this).height()-51-20-30;
        $('.mapcenter').height(_h);
    });
</script>
</body>
</html>