(function($){
    $.fn.share = function ($options,sites) {
        var $image = $(document).find('img:first').attr('src');
        var $config = {
            url: $options["url"] || window.location.href,
            desc: $options["desc"] || document.title,
            title: $options["title"] || document.title,
            summary: $(document.head).find('[name="title"]').attr('content'),
            image: $image ? $image : '',
            pics: $options["pics"] || "",
            showcount : $options["showcount"] || 1,
            target : $options["target"] || '_blank'
        };
        var windowParams = (function(){
            var screenWidth = screen.width;
            var screenHeight = screen.height;
            var commonParams = "toolbar=no,menubar=no,depended=yes,scrollbars=no,resizable=no,location=no,status=no";
            return {
                qzone: commonParams + ",height=430,width=600,top="+ (screenHeight - 400)/2 +",left=" + (screenWidth - 600)/2,
                qq: commonParams + ",height=600,width=800,top="+ (screenHeight - 600)/2 +",left=" + (screenWidth - 800)/2
            };
        })();
        $(this).bind("click",function(){
            var saveFile = function(data){
                var url = encodeURIComponent(data);
				console.log(sites);
                var data={
                    busiBlock:"searchBlock",
                    busiCode:"saveSearchShare",
                    userId:$('.mapcenter').attr('data-userid'),
                    mapId: $('.mapcenter').attr('data-id'),
                    shareTo:sites == "qq" ? 3 : 4,
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
                        console.log(result);
                    },
                    error:function(result){
                        console.log(result);
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
            var s = [];
            for(var i in $config){
                s.push(i + '=' + encodeURIComponent($config[i]||''));
            };
            var $urls = {
                qzone: "http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?" + s.join("&"),
                qq: "http://connect.qq.com/widget/shareqq/index.html?" + s.join("&")
            };
            window.open($urls[sites],sites + "shareWin",windowParams[sites]);
        });
    };
})(jQuery);