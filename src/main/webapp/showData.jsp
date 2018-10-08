<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
 <%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
	<link rel="stylesheet" href="css/bootstrap.min.css" />

	<link rel="stylesheet" type="text/css" href="css/styles.css">
	<link rel="stylesheet" type="text/css" href="css/plugin.css">
	<!--[if IE]>
		<script src="http://libs.useso.com/js/html5shiv/3.7/html5shiv.min.js"></script>
	<![endif]-->
</head>

<body>
	<div style="margin:auto;width:800px;height:840px;border:1px solid #ddd">
		
		<div class="htmleaf-content clearfix">
		
		<div id="msform">
		
		 <div class="row" id="getImageData">
                    <form action=""  class="form-horizontal col-md-7 col-lg-7 ol-sm-8"  role="form" id="form_shop">
                        <fieldset class="fill-traderinfo " id="">
                            <div class="form-group">
                              <label  class="col-md-3 col-lg-3 col-sm-3 control-label" for="">分店：</label>
                              <div class="col-md-9 col-lg-9 col-sm-9 ">
                                   <select class="form-control" name="shop" id="shop">
                                     <option value="759732">豆捞坊(浦东店)</option>
                                   </select>
                              </div>
                             </div> 
                             <div class="form-group " > 
                              <label  class="col-md-3 col-lg-3 col-sm-3 control-label" for="">性别：</label>
                              <div class="col-md-9 col-lg-9 col-sm-9 ">
                                   <select class="form-control" name="sex" id="sex">
                                     <option value="1">男</option>
			                         <option value="0">女</option>
                                   </select>
                              </div>
                            </div>
                            <div class="form-group " >
                                <label  class="col-md-3 col-lg-3 col-sm-3 control-label" for="">年龄：</label>
                                <div class="col-md-9 col-lg-9 col-sm-9  ">
                                      <select class="form-control" name="age" id="age">
                                        <option value="0-150">全部</option>
			                            <option value="0-10">10岁以下</option>
			                            <option value="11-20">11-20岁</option>
			                            <option value="21-30">21-30岁</option>
			                            <option value="31-40">31-40岁</option>
			                            <option value="41-50">41-50岁</option>
			                            <option value="51-60">51-60岁</option>
			                            <option value="61-150">61岁以上</option>
                                      </select>
                                </div>
                              </div>
                              <div class="form-group " >
                                <label class="col-md-3 col-lg-3 col-sm-3 control-label" for="contractExpireTime1">日期：</label>
                                   <div class="col-md-5 col-lg-5 col-sm-5">
                                     <div class="input-group date" id="byDate1" >
                                     <input class="form-control " type="text" value="" name="byDate1"  id="byDate" placeholder="">
                                       <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                     </div>
                             </div>                    
                             </div>
                        </fieldset>
                        <div class="form-group ">
                          <div class="col-md-9 col-lg-9 col-sm-9 col-md-offset-3 col-lg-offset-3 flex">
                            <button type="button" class="btn btn-primary pull-right" id="submit">确定</button>
                          </div>
                        </div>
                    </form>
		</div >
		<div class="row" id="getImageData1">
		
		</div>
		<div style=" position: relative;display: flex;justify-content: center">
		 		<div id="kkpager" class="pagination"></div>
		</div>
		<script  id ="getImageDataTemplate" type="text/x-handlbars-template">
				{{#each this}}				
                        <img id="image" height="100" width="100" src="{{picUrl}}"/>		
				{{/each}}
	    </script>
	</div>
	</div>
	</div>	
	<script src="js/common/jquery-2.1.1.min.js" type="text/javascript"></script>
	<script>window.jQuery || document.write('<script src="js/jquery-2.1.1.min.js"><\/script>')</script>
	<script src="js/common/jquery.easing.min.js" type="text/javascript"></script>
	<script src="js/common/bootstrap-datetimepicker.min.js"></script>
	<script src="js/common/kkpager.js" type="text/javascript"></script>
    <script src="js/common/handlebars-1.0.0.beta.6.js" type="text/javascript"></script>
	<script type="text/javascript"></script>
	<script src="js/showData.js" type="text/javascript"></script>
    <script>
      function getParameter(name) {
	         var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
             var r = window.location.search.substr(1).match(reg);
         	if (r!=null) return unescape(r[2]); return null;
	   }
     $(document).ready(function(){
    	 showImage.init();
	    });
    </script>
</body>
</html>