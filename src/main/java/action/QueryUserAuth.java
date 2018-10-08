package action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.model.UserAuthDetails;
import com.opensymphony.xwork2.ActionSupport;
import com.service.IUserService;
public class QueryUserAuth extends ActionSupport  {
	private static final long serialVersionUID=1L;
	private Map responseJson;;
	public Map getResponseJson() {
		return responseJson;
	}
	 public void setResponseJson(Map responseJson) {   
	        this.responseJson = responseJson;   
	    }   
	public String executeAjax(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:beans.xml"); 
		IUserService service = (IUserService) ctx.getBean("aaa");  		
		HttpServletRequest request=ServletActionContext.getRequest();
		String s_pageNow=request.getParameter("pageNow");
		int pageNow=1;
		if(s_pageNow!=null){
			pageNow=Integer.parseInt(s_pageNow);
		}
		int pageCount=0;
		int pageSize=8;
		int rowCount=0;		
		rowCount=service.queryUserAuthCount();
		if(rowCount%pageSize==0){
			pageCount=rowCount/pageSize;
		}else{
			pageCount=rowCount/pageSize+1;
		}
		int offset=(pageNow-1)*pageSize;
		HashMap<String,Integer> offsetLimit=new HashMap<String,Integer>();
		offsetLimit.put("offset", offset);
		offsetLimit.put("limit", pageSize);		
		List<UserAuthDetails> userAuthDetails=service.queryUserAuth(offsetLimit);
	    Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
	  for(UserAuthDetails authDetails:userAuthDetails){
		  Map<String, Object> data = new HashMap<String, Object>();  
		String authority=authDetails.getAuthorities();
		String clientID=authDetails.getAdditional_information();			
	    String[] split = authority.split(";");
        if (split.length != 4) {
            throw new IllegalArgumentException();
        }
        String authorityType = split[0];
        String group = split[1];
        String shop = split[2];
        String isAll= split[3];
        String[] authorityTypeValue = authorityType.split(":");
        String[] groupValue = group.split(":");
        String[] shopValue = shop.split(":");
        String[] isAllValue= isAll.split(":");
        String type=authorityTypeValue[1];
        String groupId="";
        if(!type.equals("2")){
        	  groupId = groupValue[1] ;
        }   
        String shopId="";
        String all=isAllValue[1];
        if (type.equals("1") ) {
           shopId =shopValue[1];
        }
        data.put("clientID", clientID);
		data.put("groupId", groupId);
		data.put("shopId", shopId);
		data.put("all", all);
		if(clientID!=null){
			String interfaces=service.queryInterfaceByUser(clientID);
			data.put("interfaces", interfaces);
		}
		
		list.add(data);
	  }	
	  map.put("rows", list);   
      map.put("totalCount", rowCount); 
      map.put("pageCount", pageCount); 
      this.setResponseJson(map); 
	  return SUCCESS;	
	}
}
