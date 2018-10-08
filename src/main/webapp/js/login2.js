var redistribution =function(){
	 var InfoRedistribution={};	
	 InfoRedistribution.initInfo=function(){		 
		 var pageNo = getParameter('pno');
			if(!pageNo){
				pageNo = 1;
			}		
		 $.ajax({
             url:"test/Action1",
             type : "post",
             data: "{pageNow:pageNo}",
             dataType: "json",
             success:function(data){	 
            	var records = data.rows;
            	var totalPage=data.pageCount;
            	var totalRecords=data.totalCount;
				var checkPendingGroupListTemplate = Handlebars.compile($("#checkPendingGroupListTemplate").html());			
				$("#checkPendingGroupList").html(checkPendingGroupListTemplate(records)); 				
				$("#checkPendingGroupList").find("tr").each(function(trindex,tritem){					
				     var records=data.rows;
					 var tdArr = $(this).children();
					 var flag=false;
					 $(tritem).find("td").each(function(tdindex,tditem){						
						 if(tdindex==3){
							 var $select= $(tditem).children();
							 var isAll=records[trindex].all;							
							 if(isAll==1){								
								 $select.find("option[value='1']").attr("selected", true);
								 flag=true;
							 }else{
								 $select.find("option[value='0']").attr("selected", true);
							 }							 							
						 }							 
						 if(tdindex==4&&flag!=true){
							 var $exampleMulti= $(tditem).children().select2();
							 var arr=records[trindex].interfaces;
							 var arr1=arr.split(",");							
							 $exampleMulti.val(arr1).trigger("change");							 
							 $(".js-example-basic-multiple").select2();
							 var $exampleMulti = $(".js-example-programmatic-multi").select2();
						 }	
						 
					 });					
				});					
				 $("#kkpager").Pager(
						 {
								pno : pageNo,
								//总页码
								total : totalPage,
								//总数据条数
								totalRecords : totalRecords,
								mode : 'click',//默认值是link，可选link或者click
						 		click : function(n){
									// do something	
									 $.ajax({
							             url:"test/Action1",
							             type : "post",
							             data: {pageNow:n},
							             dataType: "json",
							             success:function(data){
							            	    var records = data.rows;
							            		var checkPendingGroupListTemplate = Handlebars.compile($("#checkPendingGroupListTemplate").html());			
							    				$("#checkPendingGroupList").html(checkPendingGroupListTemplate(records)); 				
							    				$("#checkPendingGroupList").find("tr").each(function(trindex,tritem){					
							    				     var records=data.rows;
							    					 var tdArr = $(this).children();
							    					 var flag=false;
							    					 $(tritem).find("td").each(function(tdindex,tditem){
							    						 if(tdindex==3){
							    							 var $select= $(tditem).children();
							    							 var isAll=records[trindex].all;							
							    							 if(isAll==1){
							    								 $select.find("option[value='1']").attr("selected", true);
							    								 //$("#bankCode option[value="+bankCode+"]").prop("selected","selected");
							    								 flag=true;
							    							 }else{
							    								 $select.find("option[value='0']").attr("selected", true);
							    							 }							 							
							    						 }	
							    						 if(tdindex==4&&flag!=true){
							    							 var $exampleMulti= $(tditem).children().select2();
							    							 var arr=records[trindex].interfaces;
							    							 var arr1=arr.split(",");
							    							 $exampleMulti.val(arr1).trigger("change");							 
							    							 $(".js-example-basic-multiple").select2();
							    							 var $exampleMulti = $(".js-example-programmatic-multi").select2();
							    						 }						 
							    					 });					
							    				});
							             }
							             });							
									//手动选中按钮
									this.selectPage(n);
									return false;
								}
						}	 				 
				 );				 
             }
             });		 
	 }	 
	 InfoRedistribution.selectInterface=function(){
		 $(document).off("change").on("change","select.admin",function(){			
			 if($(this).val()=='1'){				 
				 $(this).parent().parent().find("td").each(function(tdindex,tditem){					 
					 if(tdindex==4){								
						 var $exampleMulti=$(tditem).children().select2();
						 $exampleMulti.val(null).trigger("change");
					 }
				 })
			 }
		 })		 
		 $("#checkPendingList").off('click.submit').on('click.submit','#submit',function(){
			 var clientID;
			 var groupId;
			 var shopId;
			 var interfaces=new Array();
			 var isAll;
			 $(this).parent().parent().find("td").each(function(tdindex,tditem){
				 if(tdindex==0){
					 clientID=$(tditem).text();
					 
				 }
				 if(tdindex==1){
					 groupId=$(tditem).text();
					 
				 }
				 if(tdindex==2){
					 shopId=$(tditem).text();
					 
				 }
				 if(tdindex==3){
					 isAll=$(tditem).children().val();	 
				 }
				 if(tdindex==4){
					 var $exampleMulti=$(tditem).children().select2();
					 interfaces=$exampleMulti.val(); 					 
				 }
			 });
			 var interfaceNew="";
			if(interfaces!=null){
			 interfaceNew=interfaces.join(",");	
			}
			 $.ajax({
	             url:"test/updateQueryAuth",
	             type : "post",
	             data: {clientID:clientID,groupId:groupId,shopId:shopId,interfaces:interfaceNew,isAll:isAll},
	             dataType: "json",
	             success:function(data){	 	            	
					alert("修改成功！！");							 			 
	             }
	             });
			
			
		 });   
		 $("#checkPendingList").off('click.checkSettleUnitList').on('click.checkSettleUnitList','#checkSettleUnitList',function(){			 
			 $("#interfaceID").select2();
			 $("#checkSettleUnit").modal("show");	
			 
			 $("#modalContent").off('click.addUserAuth').on('click.addUserAuth','#addUserAuth',function(){			 			
				 var clientID=$("#clientID").val();
				 if(clientID==null||clientID==""){
					 return;
				 }
				 var groupId=$("#groupId").val();				 
				 var shopId=$("#shopId").val();						
				 var interfaces=new Array();
				 $("#checkSettleUnit").modal("show");
				 var $exampleMulti = $("#interfaceID").select2();
				 interfaces=$exampleMulti.val();
				 if(interfaces==null||interfaces==""){
					 return;
				 }
				 var isAll;
				 if(interfaces.length==6){
					 isAll=1;					 
				 }else {
					 isAll=0
				 }
				 var interfaceNew=interfaces.join(",");				 
				 console.log(clientID);
				 console.log(groupId);
				 console.log(shopId);
				 console.log(interfaceNew);
				 console.log(isAll);				 
				 $.ajax({
		             url:"test/addAuth",
		             type : "post",
		             data: {clientID:clientID,groupId:groupId,shopId:shopId,interfaces:interfaceNew,isAll:isAll},
		             dataType: "json",
		             success:function(data){	 	            	
						alert("添加成功！！");		
						//InfoRedistribution.initInfo();
						location.reload();
		             }
		             });				 				 
			 });
		 });		 
     };  
 return{    	
        init:function(){  
        	InfoRedistribution.initInfo();
        	InfoRedistribution.selectInterface();
        }
      }  
}()
