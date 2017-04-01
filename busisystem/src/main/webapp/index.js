/**
 * Created by - on 2016/4/1.
 */
(function($){
    $(function(){
        var index = {};
        //同步昵称
        index.nickName = $("#nickName").text().trim();
        if("" != index.nickName){
            $("#qq_nickname").text(index.nickName);
            $("#qq_nickname").show();
            $("#qq_login_btn").remove();
        };
        //跳转到反馈  http://mate.jwis.cn/busisystem/feedback.jsp
        $("#feedback").bind("click",function(){
            location.href = "http://mate.jwis.cn/busisystem/feedback.jsp";
        });
        //图片轮播
        var can = true;
        index.imgPlay =function(){
          if(can){
              var $firstImg = $("#contain .img:eq(0)");
              if("none" == $firstImg.css("display")){
                  $firstImg.css("display","block");
                  $firstImg.next().css("display","none");
              }else{
                  $firstImg.css("display","none");
                  $firstImg.next().css("display","block");
              }
          };
        };
        setInterval(index.imgPlay,5000);
        $("#contain").bind("mouseout",function(){
            can = true;
        });
        $("#contain").bind("mouseover",function(){
            can = false;
        });

        //QQ 第三方登录
        $(document).on("click", "#qq_login_btn",function(){
            //应用的APPID
            var appID = "101291757";
            //登录授权后的回调地址，设置为当前url
            //在成功授权后回调时location.hash将带有access_token信息，开始获取openid
            var redirectURI = "http://mate.jwis.cn/busisystem/entry/qqcallback.do";
            var path = 'https://graph.qq.com/oauth2.0/authorize?';
            var queryParams = ['client_id=' + appID,
                'redirect_uri=' + redirectURI,
                'scope=' + 'get_user_info','response_type=token'];

            var query = queryParams.join('&');
            var url = path + query;
            location.href= url;
        });
    });
})(jQuery);