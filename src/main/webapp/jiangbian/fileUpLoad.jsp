<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@taglib uri="/struts-tags" prefix="s"%>   
 <%
 		String path =request.getContextPath();
 		pageContext.setAttribute("path",	path);
 %>
<!doctype html>
<html lang="zh">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />

	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
	<!--[if IE]>
		<script src="http://libs.useso.com/js/html5shiv/3.7/html5shiv.min.js"></script>
	<![endif]-->
</head>
<style>
	form {
	    display: inline-flex;
    }
	span {
		color: red;
		display: flex;
		margin-left: 30px;
	}
</style>
<body>
	<div style="margin:auto;width:800px;height:840px;border:1px solid #ddd">
		<div class="login-top">
			<h1> 哗啦啦江边数据上传界面</h1>
			<p></p>
		</div>
		<div class="htmleaf-content clearfix">
		<div id="msform" style="width: 350px;">
			<fieldset>
				<form action="FileUploadAction" method="post" enctype="multipart/form-data">
					<s:token></s:token>
					<input type="file" name="file">
					<input type="submit" value="提交">
				</form>
			</fieldset>

			  <span>* 日期格式必须为"01-Mar-2016"</span><br/>
			  <span>${message}</span>

		</div>
	</div>
	</div>
	<script src="${pageContext.request.contextPath}/js/common/jquery-2.1.1.min.js" type="text/javascript"></script>
	<script>window.jQuery || document.write('<script src="${pageContext.request.contextPath}/js/jquery-2.1.1.min.js"><\/script>')</script>
	<script src="${pageContext.request.contextPath}/js/common/jquery.easing.min.js" type="text/javascript"></script>
</body>
</html>