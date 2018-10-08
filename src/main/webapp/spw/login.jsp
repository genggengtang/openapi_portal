<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%
    String path = request.getContextPath();
    pageContext.setAttribute("path", path);
%>
<%@ page session="true" %>

<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <!--[if IE]>
    <script src="http://libs.useso.com/js/html5shiv/3.7/html5shiv.min.js"></script>
    <![endif]-->
</head>
<style>

</style>
<body>
<div style="margin:auto;width:800px;height:840px;border:1px solid #ddd">
    <div class="login-top" align="center" style="padding-top: 100px;">
        <h1 align="center" > 三品王POS数据上传系统</h1>
        <%--<p></p>--%>
    </div>
    <div class="htmleaf-content clearfix">

        <div id="msform">
            <fieldset>
                <form action="SpwLoginAction" method="post">
                    <div style="width:350px;margin:10px auto;padding:0 30px">
                        <div class="form-group  has-feedback">
				  <span class="form-control-feedback gly" aria-hidden="true">
				  	<i class="glyphicon glyphicon-user"> </i>  <span>|</span>
				  </span>
                            <input type="text" class="form-control logins" placeholder="请输入门店号" name="shopID"
                                   value=<%=session.getAttribute("shopID")==null?"":session.getAttribute("shopID")%>>
                        </div>
                        <div class="form-group  has-feedback">
				  <span class="form-control-feedback gly" aria-hidden="true">
				  	<i class="glyphicon glyphicon-lock"></i>  <span>|</span>
				  </span>
                            <input type="text" class="form-control logins" placeholder="请输入日期,格式为：2018-01-01" name="reportDate"
                                   value=<%=session.getAttribute("reportDate")==null?"":session.getAttribute("reportDate")%>>
                        </div>
                    </div>
                    <input type="submit" class="next action-button" value="导入"/>
                </form>
            </fieldset>
            <span style="color: red">${message}</span><br/>

        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/common/jquery-2.1.1.min.js" type="text/javascript"></script>
<script>window.jQuery || document.write('<script src="${pageContext.request.contextPath}/js/jquery-2.1.1.min.js"><\/script>')</script>
<script src="${pageContext.request.contextPath}/js/common/jquery.easing.min.js" type="text/javascript"></script>
</body>
</html>