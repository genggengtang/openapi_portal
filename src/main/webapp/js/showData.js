var showImage =function(){
	 var showImageData={};	
	 showImageData.initInfo=function(){	
		 var datetimepicker= {
			    	language:  'zh-CN',
			        autoclose: 1,
			        startView: 2,
			        minView: 2,
			        format:"yyyy-mm-dd",
			        forceParse: 0,
			        pickerPosition:'bottom-left'
			    }        
		 $("#byDate").datetimepicker(datetimepicker); 
		 var pageNo = getParameter('pno');
			if(!pageNo){
				pageNo = 1;
			}				
			$("#form_shop").off('click.submit').on('click.submit','#submit',function(){				 				 				
				var shopID=$("#shop").val();
				var sex=$("#sex").val();
				var age=$("#age").val();
				var date=$("#byDate").val();					
				$.ajax({
		             url:"test/queryImageData",
		             type : "post",
		             data: {shopID:shopID,sex:sex,age:age,date:date,pageNow:pageNo},
		             dataType: "json",
		             success:function(data){
		            	var records = data.rows;	
		            	var totalPage=data.pageCount;
		            	var totalRecords=data.totalCount;
		            	if(records!=""&&records!="undefined"){
		            		var getImageDataTemplate = Handlebars.compile($("#getImageDataTemplate").html());			
		    				$("#getImageData1").html(getImageDataTemplate(records)); 	            		
		            	}
		            	else{
		            		alert("查询无数据");		         
		            	}		            	
		            	var pageOptions = {
	                            pagerid:"kkpager",
								pno : pageNo,
								//总页码
								total : totalPage,
								//总数据条数
								totalRecords : totalRecords,
								mode : 'click',//默认值是link，可选link或者click
								click : function(n){
									// do something
									$.ajax({
							             url:"test/queryImageData",
							             type : "post",
							             data: {shopID:shopID,sex:sex,age:age,date:date,pageNow:n},
							             dataType: "json",
							             success:function(data){
							            	    var records = data.rows;
							            	    var getImageDataTemplate = Handlebars.compile($("#getImageDataTemplate").html());			
							    				$("#getImageData1").html(getImageDataTemplate(records)); 				
						    				
							             }
							             });																
									//手动选中按钮
									this.selectPage(n);
									return false;
								}
	                        };
	                        new Pager(null,pageOptions,true);		            	 		            	
		             }		             		             
		            });				
			 });   			 
	 }	 
 return{    	
        init:function(){  
        	showImageData.initInfo();        	
        }
      }  
}()
