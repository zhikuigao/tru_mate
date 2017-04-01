/**
 * Created by Administrator on 2016/9/14 0014.
 */
const {ipcRenderer} =require('electron');
var us=() => {
    const url = window.location.href;
    var data = url.split('#')[1];
    return JSON.parse(data);
};
let _Window = (data) => {
    ipcRenderer.send("system", {
        tacticBlock : data,
		_window:"contextFrameWindow"
    });
}
ipcRenderer.on('accessMasterCallback', (event, arg, callback) => {
	if(callback){
		try {
			window[callback](arg);
		} catch(error) {
			window.main[callback](arg);
		}
	}
});
var bindZ=()=>{
	$('.navcenter iframe:last').contents().find('.browse ul li i').click(function(){
		var _this=$(this);
		var datas={
			"busiBlock":"searchBlock",
			"busiCode":"flagMapData",
			"id":_this.attr('data-id'),
			"flag":_this.hasClass('curr0')?1:0
		}
		ipcRenderer.send('accessMaster',datas,"flagMapData");
		window.flagMapData=(datas)=>{
			if(datas.result.code==1){
				_this.attr('class',_this.hasClass('curr0')?"curr1":"curr0");
				var _h=_this.parents('li').clone(true);
				if(_this.hasClass('curr1')){
					_this.parents('.browse').find('ul').prepend(_h);
				}else{
					if(_this.parents('.browse').find('ul li i.curr1:last').length>0){
						_this.parents('.browse').find('ul li i.curr1:last').parents('li').after(_h);
					}else{
						_this.parents('.browse').find('ul').prepend(_h);
					}
				}
				_this.parents('li').remove();
			}
		}
	});
}
$(function () {
    //窗口最小、最大、还原、关闭
    $('.conls_a a').click(function(){
        var i=$(this).index();
        if(i==0)_Window("miniWindow");
        else if(i==1){
            if($(this).hasClass('maxwindow')){
                $(this).removeClass("maxwindow");
                _Window("unmaximize")
            }else{
                $(this).addClass("maxwindow");
                _Window("maxWindow")
            }
        }
        else _Window("closeWindow");
    });
	//初始化加载
	var datas=us();
	if(datas.url!==""){
		$('.navcenter').append('<webview src="'+datas.url+'" nodeintegration allowpopups autosize="on" disablewebsecurity partition="map" width="100%" height="470"></webview>');
		//webviews加载完成
		$('.navcenter webview:first')[0].addEventListener('new-window', (e) => {
			const protocol = require('url').parse(e.url).protocol
			if (protocol === 'http:' || protocol === 'https:') {
				$('.navcenter webview:first')[0].loadURL(e.url);
			}
		});
		$('.navcenter webview:first')[0].addEventListener('dom-ready', function(e){
			let searchsimilar={
				"url":$('.navcenter webview:first').attr('src'),
				"type":datas.typeid
			}
			ipcRenderer.send('truSearchBlock', searchsimilar,"searchsimilar");
			window.searchsimilar=function(data){
				if(data.result.code==1){
					var _html="<li>相关推荐</li>";
					for(var i=0;i<data.resultData.list.length;i++){
						_html+='<li><i>'+(i+1)+'</i><a href="'+data.resultData.list[i].url+'">'+data.resultData.list[i].title+'</a></li>'
					}
					$('.tBox').remove();
					$('.navcenter').append('<div class="tBox"><ul>'+_html+'</ul><span> T R U 推荐</span></div>');
					if($('.nav a.curr').index()==1)$('.tBox').css('display','none');
				}
			}
		});
	}
	$('.navcenter iframe:last').attr('src',datas.mapUrl);
	//导航切换
    $('.nav a').click(function(){
		var $iframe=$('.navcenter iframe:last'),$webview=$('.navcenter webview:first');
        var i=$(this).index();
        $(this).addClass('curr').siblings('a').removeClass('curr');
        var _left=$(this).position().left;
        var _width=$(this).outerWidth()
        $('.nav i').stop().animate({left:_left,width:_width},400);
		if(i==0){
			$iframe.hide().attr('src','');
			$webview.css({width:"auto",height:$iframe.height()});
			$('.centRight').hide();
			$('.tBox').css('display','block');
		}else{
			$('.centRight').show();
			$iframe.show().attr('src',datas.mapUrl);
			$webview.css({width:"0px",height:"0px"});
			$('.tBox').css('display','none');
		}
    });
    if(datas.showNav=="detail"){
        $('.nav a:first').trigger('click');
    }else{
        $('.nav a:last').trigger('click');
    }
	$('.navcenter iframe:last').load(function(){
		//复制地址
		$('.copy_a').click(function(){
			var url=$('#main02').get(0).contentWindow.location.href;
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
		//微信二维码
		var img_src="http://www.2d-code.cn/2dcode/api.php?key=c_2b44ZMfMX727o9uqDRYEM0ojTgRsNgvckpGB2BzO4SM&url="+$('#main02').get(0).contentWindow.location.href;
		$('.weixin span img').attr('src',img_src);
		
		//微信二维码显示
		var Wx_click=$._data($('.weixin')[0],"events");
		if(!Wx_click){
			$('.weixin').on('click',function(){
				if($(this).find('span').is(':hidden')){
					$(this).find('span').stop().slideDown(300);
					var saveFile = function(data){
						var url = encodeURIComponent(data);
						var data={
							busiBlock:"searchBlock",
							busiCode:"saveSearchShare",
							userId:$(window.frames["main02"].document).find('.mapcenter').attr('data-userid'),
							mapId: $(window.frames["main02"].document).find('.mapcenter').attr('data-id'),
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
					html2canvas($(window.frames["main02"].document).find('.mapcenter').get(0), {
						allowTaint: true,
						taintTest: false,
						onrendered: function(canvas) {
							var imgData = canvas.toDataURL();
							saveFile(imgData);
						}
					});
				}else {
					$(this).find('span').stop().slideUp(300);
				}
				return false;
			});
		}
		// 分享
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
	});
	//小美推荐
	$('body').on('click','.tBox span',function(){
		$(this).animate({'right':-24},150,function(){
			$(this).siblings('ul').animate({'right':0},300).parent('.tBox').animate({'width':'240px'},300);
		});
	});
	$('body').on('mouseleave','.tBox ul',function(){
		$(this).animate({'right':'-240px'},200).parent('.tBox').animate({'width':'24px'},200,function(){
			$(this).find('span').animate({'right':'0'},300);
		});
	});
	$('body').on('click','.tBox a',function(){
		var _url=$(this).attr('href');
		$('.navcenter webview:first').attr('src',_url);
		return false;
	});
	$(window).resize(function(){
        var _h=$(window).height()-60-20-60;
        $('.navcenter iframe').css('height',_h);
		if($('.navBox a.curr').index()===0)$('.navcenter webview:first').css('height',_h);
    });
	$('.centRight a:not(:first)').click(function(){
		if($('.weixin span').is(':visible'))$('.weixin span').stop().slideUp(300);
	});
});