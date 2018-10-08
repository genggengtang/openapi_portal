package action;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.model.ImageData;
import com.model.UserAuthDetails;
import com.opensymphony.xwork2.ActionSupport;
import com.service.IUserService;
public class QueryImageData extends ActionSupport {	
	private static final long serialVersionUID=1L;
	//private List<String> imageDatas=new ArrayList<String>();
	private Map responseJson;;
	public Map getResponseJson() {
		return responseJson;
	}
	 public void setResponseJson(Map responseJson) {   
	        this.responseJson = responseJson;   
	    }   
	public String execute(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:beans.xml");  			
        IUserService service = (IUserService) ctx.getBean("aaa");         
		HttpServletRequest request = ServletActionContext. getRequest();
		String shopID = request.getParameter("shopID");
		String sex = request.getParameter("sex");
		String age = request.getParameter("age");
		String date = request.getParameter("date");		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date newDate=null;
		try {
			newDate = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		HashMap<String, Object> map=new HashMap<String,Object>();
		if(shopID!=null||shopID!=""){
			map.put("hllShopId", shopID);
		}
		map.put("gender", sex);
		int minAge=0;
		int maxAge=0;
		if(age!=null||age!=""){
			String[] ages=age.split("-");
			minAge=Integer.parseInt(ages[0]);
			maxAge=Integer.parseInt(ages[1]);
			map.put("minAge", minAge);
			map.put("maxAge", maxAge);
		}		
		map.put("date", newDate);
		
		String s_pageNow=request.getParameter("pageNow");
		int pageNow=1;
		if(s_pageNow!=null){
			pageNow=Integer.parseInt(s_pageNow);
		}
		int pageCount=0;
		int pageSize=24;
		int rowCount=0;	
		
		
		rowCount=service.queryImageDataCount(map);
		if(rowCount%pageSize==0){
			pageCount=rowCount/pageSize;
		}else{
			pageCount=rowCount/pageSize+1;
		}
		int offset=(pageNow-1)*pageSize;
		HashMap<String,Integer> offsetLimit=new HashMap<String,Integer>();
		offsetLimit.put("offset", offset);
		offsetLimit.put("limit", pageSize);		
		
		map.put("offset", offset);
		map.put("limit", pageSize);		
		List<ImageData> imageData=service.getImageData(map);
		Map<String, Object> map1 = new HashMap<String, Object>();
//		if(imageData.size()>0){
//			for(ImageData imageData1:imageData){
//				imageDatas.add(imageData1.getPicUrl());
//			}
//		}
		map1.put("rows", imageData);
		map1.put("totalCount", rowCount); 
	    map1.put("pageCount", pageCount); 
		this.setResponseJson(map1); 
		return SUCCESS;
	}
}
