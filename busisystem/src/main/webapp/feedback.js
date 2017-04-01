/**
 * Created by - on 2016/1/21.
 */

$(function(){
    var feedback  = {};
    feedback.files = new Array();

    //反馈内容  填写字数  实时更新
    $("#feedback").on("keyup", "#feedbackcontent textarea", function(){
        var len = $(this).val().split("").length;
        //实时动态显示字数
        if(200 <len){
            $("#feedbackcontent p>span").text(200);
        }else{
            $("#feedbackcontent p>span").text(len);
        };
        //控制发送按钮是否可用
        $("#sendFeedbackButton").css("color", 10 > len ? "#cccccc" : "#333333") ;
    });
    //rgb  转化为  六位数字符
    feedback.rgb2hex  = function (rgb) {
        rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
        function hex(x) {
            return ("0" + parseInt(x).toString(16)).slice(-2);
        }
        return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
    }
    $("#feedbackemail > input").bind("focus",function(){
        $("#feedbackemail > p").text("");
    });

    feedback.formatDate = function (b, g) {
        var a = {
            "M+": b.getMonth() + 1,
            "d+": b.getDate(),
            "h+": b.getHours(),
            "m+": b.getMinutes(),
            "s+": b.getSeconds(),
            "q+": Math.floor((b.getMonth() + 3) / 3),
            S: b.getMilliseconds()
        };
        g || (g = "yyyy-MM-dd hh:mm:ss");
        /(y+)/.test(g) && (g = g.replace(RegExp.$1, (b.getFullYear() + "").substr(4 - RegExp.$1.length)));
        for (var c in a)(new RegExp("(" + c + ")")).test(g) && (g = g.replace(RegExp.$1, 1 == RegExp.$1.length ? a[c] : ("00" + a[c]).substr(("" + a[c]).length)));
        return g
    };
    //提交反馈内容
    $("#feedback").on("click","#sendFeedbackButton",function(){
        var attitudes = $(".optionsicon");
        var imgurl;
        var attitude = "";
        $(attitudes).each(function(){
            imgurl = $(this).css("background-image");
            imgurl = imgurl.substring(imgurl.lastIndexOf("/"), imgurl.length);
            if(-1 != imgurl.indexOf("9")){
                attitude = $(this).next().text().trim();
            }
        });
        if("" == attitude){
            $.customPrompt("请亮出你的态度(例如:勾选'喜欢')");
            //$("#feedbackemail > p").text("请亮出你的态度(例如:勾选'喜欢')");
            return;
        }
        if(10 > $("#feedbackcontent textarea").val().split("").length){
            $.customPrompt("反馈文字不足10个,请再辛苦下");
            //$("#feedbackemail > p").text("输入的文本要大于10个字符");
            return;
        };
        var mailreg = new RegExp(/^[a-zA-Z0-9\+\.\_\%\-\+]{1,256}\@[a-zA-Z0-9][a-zA-Z0-9\-]{0,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{0,25})$/);
        var telreg = new RegExp(/^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/);
        var contactInfo = $("#feedbackemail > input").val().trim();
        if(!mailreg.test(contactInfo) && !telreg.test(contactInfo)){
            $.customPrompt("联系方式(邮箱/电话)格式错误!");
            //$("#feedbackemail > p").text("邮箱格式错误!");
            return;
        };

        var comment = $("#feedbackcontent > textarea").val().trim();
        //alert(comment.match(/`~!@#$%^&*()_+-=[]{\}\|;:\//));
        //上传图片到图片服务器 后 获取图片路径
        var time = feedback.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss");
        var data = {
            "busiBlock": "userBlock",
            "time": time,
            "Md5Str": hex_md5("busiSystemMate" + time),
            "busiCode": "userFeedback",
            "email" : contactInfo,
            "content": encodeURIComponent(comment.replace(/\"/g,"\\\"").replace(/\'/g,"\\\'").replace(/\n/g, "\\n")),
            "attitude": attitude
        };
        $("#uploadFiles").attr("action", "http://mate.jwis.cn/busisystem/entry/busiFileEntry.do?"+ "paramObject=" + JSON.stringify(data)).submit();
        //跳转前 控制发送按钮是否可用 禁止重复反馈
        $("#sendFeedbackButton").attr("onClick","return false;").css("color", "#cccccc") ;
        $.customPrompt("已发送，谢谢反馈", 1000,function(){
            $("#sendFeedbackButton").attr("onClick","").css("color","#333333") ;
            location.href = "/";
        });
    });

    //勾选  态度
    $("#feedback").on("click","#feedbackoptions .optionsicon",function(){
        //根据图片路径判断
        var imgurl = $(this).css("background-image");
        if(-1 != imgurl.indexOf("8")){
            $(this).css("background","url('imgs/9.png') center no-repeat");
            $(this).parent().siblings().find(".optionsicon").css("background","url('imgs/8.png') center no-repeat");
        }else if(-1 != imgurl.indexOf("9")){
            $(this).css("background","url('imgs/8.png') center no-repeat");
            $(this).parent().siblings().find(".optionsicon").css("background","url('imgs/8.png') center no-repeat");
        }
    });

    //选择本地图片
    $("#feedback").on("click","#feedbackupdategif",function(){
        $("<input type='file' name = 'fileKey' class='hiddenInfo'/>").appendTo($("#uploadFiles"));
        $("#feedbackphoto input[type=file]:last-child").click();
    });

    //选择本地图片
    $("#feedback").on("mouseup mousedown", "#feedbackupdategif",function(e){
        if("mouseup" == e.type) $(this).css("color","#333333");
        if("mousedown" == e.type) $(this).css("color","blue");
    });

    //显示  本地图片
    var imgTypeFlag = true;
    var imgNameFlag = true;
    $("#feedback").on("change", "#feedbackphoto input[type=file]",function(){
        if(9 == $("#feedbackphoto ol li").length) $.customPrompt("最多只能添加9张图片");
        feedback.checkFile(this.files);
        var $photoError =  $("#getPhotoError");
        if(!imgTypeFlag){
            $photoError.text("支持gif,jpg,jpeg,png(1M以内)");
            $("#feedbackphoto input[type=file]:last-child").remove();
        };
        if(!imgNameFlag){
            $photoError.text("选择的图片重复!");
            $("#feedbackphoto input[type=file]:last-child").remove();
        };
        if(imgTypeFlag && imgNameFlag){
            $photoError.text("");
        };
    });

    //删除选择的图片
    $(document).on("click","#feedbackphoto ol li p", function(){
        var $thisParent = $(this).parent();
        var delIndex = $("#feedbackphoto ol li").index($thisParent);
        $("#feedbackphoto input[type=file]").each(function(index,ele){
            if(delIndex ==  index){
                $(ele).remove();
                return false;
            };
        });
        $thisParent.remove();
    });

    var imgs = ["gif", "jpg", "jpeg", "png"];
    feedback.checkFile = function (files){
        var show = $("#feedbackphoto ol")[0];
        var imgsLen = 9 - $("#feedbackphoto ol li").length;
        var html = "", i=0;
        var filesLength = files.length;
        var func = function(){
            if(i >= filesLength || i >= imgsLen ){
                // 若已经读取完毕，则把html添加页面中
                show.innerHTML += html;
                imgTypeFlag = true;
                return true;
            }
            var file = files[i];
            var reader = new FileReader();

            // show表示<div id='show'></div>，用来展示图片预览的
            var suffixName = file.type.substring(file.type.lastIndexOf("/") + 1);
            var imgName = file.name;
            imgNameFlag = true;
            imgTypeFlag = true;
            $("#feedbackphoto ol li").each(function(index,ele){
                if( imgName == $(ele).attr("imgname").trim()){
                    imgNameFlag = false;
                    return false;
                };
            });
           if(!imgNameFlag) return false;
            if(-1 == imgs.indexOf(suffixName) || 1000 < file.size/1000){
                imgTypeFlag = false;
                return false;
            };
            reader.onload = function(e){
                html += "<li imgname='"+ imgName+"'><p>x</p><img src='"+e.target.result+" 'alt='img'></li>";
                i++;
                func(); //选取下一张图片
            }
            reader.readAsDataURL(files[i]);
        }
        func();
    };
});