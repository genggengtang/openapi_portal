package action;

import java.util.List;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import com.model.ShopDes;

import com.opensymphony.xwork2.ActionSupport;
import com.service.GeneratorToken;
import com.service.IUserService;
import com.service.RedisService;

public class DianpuLoginAction extends ActionSupport {	
	
	@Resource  
    private RedisService redisService; 

	private String token;	
	private List<ShopDes> shopInfos;
	public List<ShopDes> getShopInfos() {
		return shopInfos;
	}
	public void setShopInfos(List<ShopDes> shopInfos) {
		this.shopInfos = shopInfos;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String loginDianpu() throws Exception{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:beans.xml");  			
        IUserService service = (IUserService) ctx.getBean("aaa");         
		HttpServletRequest request = ServletActionContext. getRequest();
		String account = request.getParameter("account");
		String childAccount = request.getParameter("child_account");	
		String password = request.getParameter("password");		
        String retcode=service.loginDianpu(account, childAccount,password);	
        if(retcode=="loginOk"){                	       	        	
        	token=GeneratorToken.encrypt(childAccount, "12345678") ;                             
            if(!redisService.exists("openApi_dianpu_"+childAccount)){
            	redisService.set("openApi_dianpu_"+childAccount, token);
            }   
            
        }        
	    return retcode;
}			
	public String getShops(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:beans.xml");  			
        IUserService service = (IUserService) ctx.getBean("aaa");  
        HttpServletRequest request = ServletActionContext. getRequest();
		String accessToken = request.getParameter("token");
		System.out.println("token "+accessToken);
        
       List<ShopDes> shopInfos=service.getShops(accessToken);
       if(shopInfos!=null){
		return "loginOk";
		
       }
       return "";		
	}
}
