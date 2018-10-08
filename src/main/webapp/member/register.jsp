<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
    String groupID=request.getParameter("groupID");
    String imageUrl=request.getContextPath()+"/image/"+groupID+".jpg";
    String realPath=application.getRealPath("/image/"+groupID+".jpg");
    File file=new File(realPath);
    if(!file.exists()){
        imageUrl=request.getContextPath()+"/image/logo.jpg";
    }

%>
<%@ page session="true" %>

<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="blank"/>
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <title></title>
    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />--%>

    <%--<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">--%>
    <!--[if IE]>
    <script src="http://libs.useso.com/js/html5shiv/3.7/html5shiv.min.js"></script>
    <![endif]-->
</head>
<style>
    .section{
        width: 100%
    }
    .header{
        background: #FFFFFF;
        width: 100%;
        height: auto;
    }
    .logo{
        margin: 0 auto;
        width: 75px;
        height: 75px;
        background-image: url(<%= imageUrl %>);
        background-repeat: round
    }
    .into{
        margin: 0 auto;
        width: 6em;
        height: auto;
        text-align: center;
    }
    .center{
        margin: 0 auto;
        border-top: 1px solid silver;
    }
    #submit{
        /*width: 200px;*/
        height: 40px;
        background: green;
        text-align: center;
        line-height: 40px;
        color: #FFFFFF;
        margin: 50px auto;
        border-radius: 5px;
        cursor:pointer;
    }
</style>
<body>
<div class="header">
    <div class="logo">
        <img src="" alt="">
    </div>
    <div class="into">
        <h2>哗啦啦</h2>
    </div>
    <br>
</div>
<div class="section">
    <div class="center">
        <p>网页由哗啦啦开发，请确定授权以下信息</p>
        <ul style="margin: 0 auto">
            <li style="color: silver">
                获得你的公开信息
            </li>
        </ul>
        <div id="submit">
            注册为会员
        </div>
        <div class="into">
            <span id="telephonenameTip"></span>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/common/jquery-2.1.1.min.js" type="text/javascript"></script>
<script>window.jQuery || document.write('<script src="${pageContext.request.contextPath}/js/jquery-2.1.1.min.js"><\/script>')</script>
<script src="${pageContext.request.contextPath}/js/common/jquery.easing.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/member/main.js" type="text/javascript"></script>
</body>
<script>
    window.onload = function () {
        //提交注册按钮
        $("#submit").click(function () {
            //获取参数
            var groupID=GetQueryString("groupID")
            var customerMobile=GetQueryString("customerMobile")
            var sourceWay= GetQueryString("sourceWay")
            var sourceType= GetQueryString("sourceType")
           // var cardTypeID= GetQueryString("cardTypeID")
            var notifyUrl= GetQueryString("notifyUrl")
            var backUrl=  GetQueryString("backUrl")

            if(groupID=="47037"){
                clientId="jf000116";
            }else{
                clientId="jf000138"
            }

            // 向后台发送处理数据
            $.ajax({
                url:"${pageContext.request.contextPath}"+api_host+"member/openCard",
                data: { groupID: groupID,
                        customerMobile: customerMobile,
                        sourceType: sourceType,
                        sourceWay:sourceWay,
                       // cardTypeID: cardTypeID,
                        shopID:shopID,
                        isNeedCode:isNeedCode,
                        version:1
                },
                type: "POST",
                dataType: "json",
                error: function (msg) {
                    console.log(msg);
                },
                success: function (data) {
//                    console.log(data)
                    if (data.code == "000") {
                        //回调接口
                        var date=new Date().Format("yyyyMMddhhmmss");

                        var str=
                            "clientId"+clientId+
                            "telNo"+customerMobile+
                            "timestamp"+date+
//                            "token"+data.data.token+
                            "uid"+ data.data.cardID;

//                        console.log(str)

                        $.ajax({
                            url:"${pageContext.request.contextPath}/member/proxy.jsp?url="+notifyUrl+"&str="+str,
                            data: { uid: data.data.cardID,
                                    telNo: customerMobile,
                                    clientId: clientId,
                                    token:data.data.token,
                                    timestamp: date
                                  },
                            type: "POST",
                            dataType: "json",
                            error: function (msg) {
                                console.log(msg);
                            },
                            success: function (data) {
//                                console.log(data)
                                if (data.code =="00") {
                                    window.location.href= backUrl;
                                } else {
                                    $("#telephonenameTip").html("<font color='red'>"+data.message+"</font>");
                                    return;
                                }
                            }
                        });
                    } else {
                        $("#telephonenameTip").html("<font color='red'>"+data.msg+"</font>");
                        return;
                    }
                }
            });
        });
    }
</script>
</html>
