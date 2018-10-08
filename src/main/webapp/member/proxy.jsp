<%@ page import="java.net.*,java.util.*,java.lang.*,com.google.gson.*,com.util.*"%>
<%@ page import="java.io.*" %>
<%@ page contentType="text/xml;charset=UTF-8"%>
<%

   try {
       String url = null;
       StringBuffer params = new StringBuffer();
       Enumeration enu = request.getParameterNames();
       String phone="";
       String userKey="QNOzGirvv";//openapi的用户的key
       String key="B279f5F2jdj2xB6u5RBfsQyw1AaYDg7AA9c4VuvU";//万达接口的key
       String str="";
       int total = 0;
       while (enu.hasMoreElements()) {
           String paramName=(String)enu.nextElement();
           if(paramName.equals("url")){
               url=request.getParameter(paramName);
           }else if(paramName.equals("str")){
               str=request.getParameter(paramName);
           }else{
               if(paramName.equals("customerMobile")){
                   phone=request.getParameter(paramName);
               }
               if(total==0){
                   params.append(paramName).append("=").append(URLEncoder.encode(request.getParameter(paramName), "UTF-8"));
               } else {
                   params.append("&").append(paramName).append("=").append(URLEncoder.encode(request.getParameter(paramName), "UTF-8"));
               }
               ++total;
           }
       }

       //TODO 修改url
       if(url.startsWith("http://open-api.hualala.com/")){
           params.append("&token=").append(Util.getSha1(url+phone+userKey));
       }else{
           String sign=Util.getMD5(str+key);
           params.append("&sign=").append(sign);
       }

       //out.println(url);

       if(null !=url){
           PrintWriter printWriter = null;
           //使用POST方式向目的服务器发送请求
           URL connect = new URL(url.toString());
           URLConnection connection = connect.openConnection();
           // 发送POST请求必须设置如下两行
           connection.setDoOutput(true);
           connection.setDoInput(true);

           // 获取URLConnection对象对应的输出流
           printWriter = new PrintWriter(connection.getOutputStream());
           // 发送请求参数
           printWriter.print(params.toString());
           // flush输出流的缓冲
           printWriter.flush();

           BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
           String line;
           while((line = reader.readLine()) != null){
               out.println(line);
           }
           reader.close();
       }
   }catch (Exception e){
       e.printStackTrace();
       Map<String,Object> json=new HashMap();
       json.put("code","888");
       json.put("msg","出错了");
       out.println(new Gson().toJson(json));
   }

%>