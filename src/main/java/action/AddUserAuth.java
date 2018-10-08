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
import com.service.MD5;

public class AddUserAuth extends ActionSupport {

	private static final long serialVersionUID=1L;
	public String execute(){
		
		HttpServletRequest request=ServletActionContext.getRequest();
		String clientID=request.getParameter("clientID");
		String groupID=request.getParameter("groupId");
		String shopID=request.getParameter("shopId");
		String isAll=request.getParameter("isAll");
		String interfaces=request.getParameter("interfaces");		
		String client_id=MD5.toMD5to16(clientID);
		String password=MD5.toMD5to32(clientID);		
		String authType="";
		if(isAll.equals("1")){
			authType="2";
		}else{
		if(!shopID.equals("")){
			authType="1";
		}else{
			authType="0";
		}
		}
		String authorities="auth_type:"+authType+";group:"+groupID+";shop:"+shopID+";isAll:"+isAll;
		String additional_information=clientID;
		UserAuthDetails userAuthDetails=new UserAuthDetails();
		userAuthDetails.setAdditional_information(additional_information);
		userAuthDetails.setAuthorities(authorities);
		userAuthDetails.setClientID(client_id);
		userAuthDetails.setPassword(password);
		
		UserInterface userInterface=new UserInterface();
		userInterface.setClient_id(clientID);
		userInterface.setInterfaces(interfaces);
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:beans.xml"); 
		IUserService service = (IUserService) ctx.getBean("aaa");  
		service.addUserAuth(userAuthDetails, userInterface);
		return Action.SUCCESS;
		
	}
}
