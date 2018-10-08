package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.model.spw.ShopInfo;
import com.service.OrderService;
import com.util.Util;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.opensymphony.xwork2.ActionSupport;
import com.service.IUserService;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 用户登录
 *
 * @author Administrator
 */
@SuppressWarnings("serial")
public class LoginAction extends ActionSupport {

    private static final long serialVersionUID = 1L;
    private static final String JIANGBIAN_ACCOUNT = "jiangbian";
    private static final String JIANGBIAN_PASSWORD = "jb1234";

    public String loginUser() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:beans.xml");
        IUserService service = (IUserService) ctx.getBean("aaa");
        HttpServletRequest request = ServletActionContext.getRequest();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String retcode = service.login(username, password);
        return retcode;


    }

    public String JiangBianLogin() {

        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpSession session = request.getSession();

            String username = request.getParameter("account");
            username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
            String password = request.getParameter("password");
            session.setAttribute("account", username);
            session.setAttribute("password", password);

            if (null == username || username.isEmpty()) {
                ServletActionContext.getRequest().setAttribute("message", "请输入用户名");
                return "input";
            } else if (null == password || password.isEmpty()) {
                ServletActionContext.getRequest().setAttribute("message", "请输入密码");
                return "input";
            } else if (!username.equals(JIANGBIAN_ACCOUNT)) {
                ServletActionContext.getRequest().setAttribute("message", "用户名错误");
                return "loginError";
            } else if (!password.equals(JIANGBIAN_PASSWORD)) {
                ServletActionContext.getRequest().setAttribute("message", "密码错误");
                return "loginError";
            } else {
                return "loginOk";
            }

        } catch (Exception e) {
            e.printStackTrace();
            ServletActionContext.getRequest().setAttribute("message", "服务器错误");
            return "loginError";
        }

    }

    public String SpwLogin() {
        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            HttpSession session = request.getSession();

            String shopID = request.getParameter("shopID");
            System.out.println("****************"+shopID);
            String reportDate_new = request.getParameter("reportDate");
            if (null == reportDate_new || reportDate_new.isEmpty()) {
                ServletActionContext.getRequest().setAttribute("message", "请输入日期，格式为：2018-01-01");
                return "input";
            }
            String[] reportDates = reportDate_new.split("-");

            if(reportDates == null || reportDates.length != 3){
                ServletActionContext.getRequest().setAttribute("message", "日期格式不对，格式为：2018-01-01");
                return "input";
            }
            String month = reportDates[1];
            String day = reportDates[2];
            if(reportDates[1].length() == 1){
                    month ="0"+reportDates[1];
            }
            if(reportDates[2].length() == 1){
                   day ="0"+reportDates[2];
            }
            String reportDateParam = reportDates[0]+month+day;


//            if(reportDate_new.split("-"))
//            String reportDate = Util.convertDate(reportDate_new);
            System.out.println("****************"+reportDate_new);
            session.setAttribute("shopID", shopID);
            session.setAttribute("reportDate", reportDate_new);

            if (null == shopID || shopID.isEmpty()) {
                ServletActionContext.getRequest().setAttribute("message", "请输入门店号");
                return "input";
            }  else {

                ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:beans.xml");
                OrderService service = (OrderService) ctx.getBean("orderService");
                HttpServletRequest httpServletRequest = ServletActionContext.getRequest();
                //验证shopID
                List<ShopInfo> shopInfoList = service.queryShopIds(63402);
                boolean flag = false;
                for(ShopInfo shopInfo : shopInfoList){
                    if(shopID.equals(String.valueOf(shopInfo.getShopID()))){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    ServletActionContext.getRequest().setAttribute("message", "无此shopID");
                    return "loginError";
                }
                try {
                    int count = service.importSalesNumForFood(63402, Long.valueOf(shopID), Long.valueOf(reportDateParam));
                   service.importPayInfo(63402, Long.valueOf(shopID), Long.valueOf(reportDateParam));
//
                    service.importYuanGongCanInfo(63402, Long.valueOf(shopID), Long.valueOf(reportDateParam));
                    service.importSalesNumForCategory(63402, Long.valueOf(shopID), Long.valueOf(reportDateParam), count);
                }catch (Exception e){
                    ServletActionContext.getRequest().setAttribute("message", "导入数据发生异常");
                    return "loginError";
                }
                return "loginOk";
            }
        } catch (Exception e) {
            e.printStackTrace();
            ServletActionContext.getRequest().setAttribute("message", "服务器错误");
            return "loginError";
        }

    }


}
