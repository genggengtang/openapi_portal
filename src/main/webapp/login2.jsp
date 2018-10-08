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
	<link rel="stylesheet" href="css/bootstrap.min.css" />

	<link rel="stylesheet" type="text/css" href="css/styles.css"/>
	<link rel="stylesheet" type="text/css" href="css/select2.css"/>
	<link rel="stylesheet" type="text/css" href="css/plugin.css"/>
	<!--[if IE]>
		<script src="http://libs.useso.com/js/html5shiv/3.7/html5shiv.min.js"></script>
	<![endif]-->
</head>

<body>
	<div class="con-contain col-md-12" id="creatShop" >
    <div class="con-header page-header">
        <div class="left creatshop-left">
            <h4 class="h4">集团店铺信息授权</h4>
        </div>
        <div class="shop-right my-right">
        </div>
    </div>

    <div class="table-responsive col-md-12" id="checkPendingList">
			<table class="table table-bordered  ix-data-report table-hover">
				<thead>
					<tr>
					    <th class="text" >
							用户ID
						</th>
						<th class="text" >
							集团ID
						</th>
						
						<th class="text" >
							店铺ID
						</th>
						
						<th class="text" >
							是否全部接口权限
						</th>
						<th class="text" >							
							接口列表
						</th>
						<th class="text" >
							操作
						</th>
					</tr>
				</thead>
				<tbody id="checkPendingGroupList">

				</tbody>
			</table>
			<script  id ="checkPendingGroupListTemplate" type="text/x-handlbars-template">
				{{#each this}}
					<tr>
                        <td class="text" >{{clientID}}</td> 
						<td class="text" contenteditable=true>{{groupId}}</td>                      
						<td class="text" contenteditable=true>{{shopId}}</td>
                        <td class="text" contenteditable=true>
                            <select class="form-control admin" >
                                    <option role="option" value='0'>否</option>
                                    <option role="option" value='1'>是</option>                                    
                            </select>
                        </td>
                        <td class="text" contenteditable=true>
						 <select class="js-example-programmatic-multi form-control" multiple="multiple" data-select={{interfaces}}>
                                   <option value="getFinanceBaseInfo">getFinanceBaseInfo</option>   
                                   <option value="getFinanceTransData">getFinanceTransData</option>
                                   <option value="getCloudOrderLst">getCloudOrderLst</option>                                   
                                   <option value="createOrder">createOrder</option>
                                   <option value="submitOrder">submitOrder</option>
                                   <option value="rejectOrder">rejectOrder</option>
                         </select>	
                       </td>
                       					
					   <td class="text"><button class=" btn btn-primary" id="submit">确认</button></td>
					</tr>
				{{/each}}
				</script>
		<div class="flex flex-row" style="width:50%">
		  <button type="button" class="btn btn-primary" data-toggle="modal" id="checkSettleUnitList">
		       添加
		  </button>
		</div>	
		
		 <div style=" position: relative;display: flex;justify-content: center">
		 		<div id="kkpager" class="pagination"></div>
		</div>
	  </div>
	 
	  
	  <div class="modal fade checkSettleUnit" id="checkSettleUnit" tabindex="-1" role="dialog"
                   aria-labelledby="myModalLabel" aria-hidden="true">
       <div class="modal-dialog modal-500">
          <div class="modal-content" id="modalContent">
                <div class="modal-header">
                    <button type="button" class="close"
                       data-dismiss="modal" aria-hidden="true">
                          &times;
                     </button>
                    <h4 class="modal-title h4" id="myModalLabel">
                       增加客户信息
                    </h4>
                </div>

                 <div class="modal-body" id="modal_checkSettleUnit">
                     <div class="form-horizontal" >
                        <div class="form-group">
                             <label for="client" class="col-md-2 control-label">用户名：</label>
                             <div class="col-md-4">
                               <input type="text" class="form-control" placeholder="" id="clientID" >
                             </div>
                        </div>                       
                        <div class="form-group">
                            <label for="group" class="col-md-2 control-label">集团ID：</label>
                            <div class="col-md-4">
                               <input type="text" class="form-control" placeholder="" id="groupId" >
                            </div>
                        </div>
                        <div class="form-group">
                           <label for="shop" class="col-md-2 control-label">店铺ID：</label>
                           <div class="col-md-4">
                            <input type="text" class="form-control"  id="shopId" >
                           </div>
                        </div>
                        <div class="form-group">
                          <label for="interfaces" class="col-md-2 control-label">接口列表：</label>
                          <div class="col-md-8">
                            <select class="js-example-programmatic-multi1  form-control" id="interfaceID" multiple="multiple" data-select={{interfaces}}>
                                   <option value="getFinanceBaseInfo">getFinanceBaseInfo</option>   
                                   <option value="getFinanceTransData">getFinanceTransData</option>
                                   <option value="getCloudOrderLst">getCloudOrderLst</option>                                   
                                   <option value="createOrder">createOrder</option>
                                   <option value="submitOrder">submitOrder</option>
                                   <option value="rejectOrder">rejectOrder</option>
                           </select>	
                         </div>
                       </div>
                  </div>
                  
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                       data-dismiss="modal">取消
                    </button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal" id="addUserAuth">
                                               确定
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
</div>


</div>
<script src="js/common/jquery-2.1.1.min.js" type="text/javascript"></script>
<script src="js/common/bootstrap.min.js" type="text/javascript"></script>
<script src="js/common/kkpager.js" type="text/javascript"></script>
<script src="js/common/handlebars-1.0.0.beta.6.js" type="text/javascript"></script>
<script type="text/javascript" src="js/common/select2.min.js"></script>
<script src="js/login2.js" type="text/javascript"></script>
<script>
function getParameter(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r!=null) return unescape(r[2]); return null;
	}
$(document).ready(function(){
	redistribution.init();
	 });
</script>
</body>
</html>

