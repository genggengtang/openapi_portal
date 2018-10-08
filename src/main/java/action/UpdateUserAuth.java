package action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.model.UserAuthDetails;
import com.model.UserInterface;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.service.IUserService;

public class UpdateUserAuth extends ActionSupport {

	private static final long serialVersionUID=1L;
	public String execute(){
		HttpServletRequest request=ServletActionContext.getRequest();
		String clientID=request.getParameter("clientID");
		String groupID=request.getParameter("groupId");
		String shopID=request.getParameter("shopId");
		String isAll=request.getParameter("isAll");
		String interfaces=request.getParameter("interfaces");
		System.out.println("interfaces "+interfaces);		
		String auth_type=null;
		String authorities=null;
		if(isAll.equals("1")&&shopID.equals("")&&groupID.equals("")){
			auth_type="2";
			authorities="auth_type:"+auth_type+";group:"+groupID+";shop:"+shopID+";isAll:"+isAll;
		}
		else{
		if(shopID.equals("")||shopID==null){
			auth_type="0";
			authorities="auth_type:"+auth_type+";group:"+groupID+";shop:;isAll:"+isAll;
			
		}
		else{
			auth_type="1";
			authorities="auth_type:"+auth_type+";group:"+groupID+";shop:"+shopID+";isAll:"+isAll;
		}
		}
		
		UserAuthDetails userAuthDetails=new UserAuthDetails();
		userAuthDetails.setAuthorities(authorities);
		userAuthDetails.setAdditional_information(clientID);		
		UserInterface userInterface=new UserInterface();
	    userInterface.setClient_id(clientID);
	    userInterface.setInterfaces(interfaces);	    
	    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:beans.xml"); 
		IUserService service = (IUserService) ctx.getBean("aaa");  
		service.updateUserAuth(userAuthDetails, userInterface);
		return Action.SUCCESS;
	}
}
