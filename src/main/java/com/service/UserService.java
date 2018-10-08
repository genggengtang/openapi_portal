package com.service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.model.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;


import com.dao.LoginDianpuMapper;
import com.dao.LoginMapper;
import com.dld.hualala.util.CipherUtil;


@Service("aaa")
public class UserService implements IUserService {





    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private LoginMapper userDao;

    @Resource
    private LoginDianpuMapper userDianpuDao;

    private static String pathForToken = "Oauth2/accessToken";
    private static String hostForToken = "open.mmyeah.com/";
    private static String pathForFlowData = "open/getFlowList";


    @Override
    public String login(String username, String password) {
        User u = userDao.loadByUsername(username);
        if (u == null) return "input";
        if (!u.getPassword().equals(password)) return "loginError";
        return "loginOk";
    }


    @Override
    public String queryInterfaceByUser(String username) {
        String interfaceList = userDao.queryInterfaceByUser(username);

        return interfaceList;


    }


    @Override
    public List<UserAuthDetails> queryUserAuth(HashMap<String, Integer> map) {


        List<UserAuthDetails> userAuthDetails = userDao.queryUserAuth(map);
        for (UserAuthDetails a : userAuthDetails) {
            System.out.println(a.getAuthorities());
            System.out.println(a.getAdditional_information());
        }

        return userAuthDetails;
    }


    @Override
    public void updateUserAuth(UserAuthDetails userAuthDetails,
                               UserInterface userInterface) {

        userDao.updateUserAuth(userAuthDetails);
        userDao.updateInterfaces(userInterface);

    }


    @Override
    public void addUserAuth(UserAuthDetails userAuthDetails,
                            UserInterface userInterface) {
        // TODO Auto-generated method stub
        userDao.addUserAuth(userAuthDetails);
        if (userInterface != null) {
            userDao.addInterfaces(userInterface);
        }
    }


    @Override
    public int queryUserAuthCount() {
        // TODO Auto-generated method stub
        int count = userDao.queryUserAuthCount();
        return count;
    }


    @Override
    public String loginDianpu(String account, String childAccount,
                              String password) {
        // TODO Auto-generated method stub

        DianpuUser dianpuUser = userDianpuDao.loadByDianpuUsername(childAccount);
        if (dianpuUser == null) return "input";
        if (dianpuUser != null && dianpuUser.getLoginPWD() != null) {
            if (!CipherUtil.cipherPassword("4", childAccount, password).equals(dianpuUser.getLoginPWD())) {
                return "loginError";
            }
        }


        return "loginOk";
    }


    @Override
    public AccountInfo getAccountInfo(String childAccount) {
        // TODO Auto-generated method stub
        AccountInfo info = userDianpuDao.getAccountInfo(childAccount);
        System.out.println(info.getAccountID() + "" + info.getGroupID());
        return info;
    }


    @Override
    public List<ShopDes> getShops(String accessToken) {
        // TODO Auto-generated method stub
        String childAccountID = GeneratorToken.decrypt(accessToken, "12345678");
        System.out.println("childAccountID=" + childAccountID);
        AccountInfo info = userDianpuDao.getAccountInfo(childAccountID);
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("accountID", 137);
        map.put("groupID", 2);
        String shopIDs = userDianpuDao.getShopID(map);
        System.out.println("shopIDs=" + shopIDs);
        if (shopIDs != null) {
            String[] shopIDArray = shopIDs.split(",");
            List<ShopDes> shopInfoArray = new ArrayList<ShopDes>();
            for (String s : shopIDArray) {
                HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
                hashmap.put("shopID", Integer.parseInt(s));
                String shopName = userDianpuDao.getShopName(hashmap);
                ShopDes shopInfo = new ShopDes();
                shopInfo.setShopID(Integer.parseInt(s));
                shopInfo.setShopName(shopName);
                shopInfoArray.add(shopInfo);
            }
            for (ShopDes shopInfo : shopInfoArray) {
                System.out.println("shopID=" + shopInfo.getShopID() + " shopName=" + shopInfo.getShopName());
            }

            return shopInfoArray;
        }
        return null;
    }


    @Override
    public List<ImageData> getImageData(HashMap<String, Object> map) {
        // TODO Auto-generated method stub


        List<ImageData> data = userDao.getImageData(map);

        return data;
    }


    @Override
    public int queryImageDataCount(HashMap<String, Object> map) {
        // TODO Auto-generated method stub
        int count = userDao.queryImageDataCount(map);
        return count;
    }

    @Override
    public String getAccessTokenForMmkj(String client_id, String client_secret, String grant_type, String code, String redirect_uri) {

        JSONObject responseBody = null;
        CloseableHttpClient client = null;

        try {
            client = HttpClients.createDefault();

            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(hostForToken)
                    .setPath(pathForToken)
                    .build();

            HttpPost httpPost = new HttpPost(uri);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader("Cache-Control", "no-cache");

            List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();

            postParams.add(new BasicNameValuePair("client_id", client_id));
            postParams.add(new BasicNameValuePair("client_secret", client_secret));
            postParams.add(new BasicNameValuePair("grant_type", grant_type));
            postParams.add(new BasicNameValuePair("code", code));
            postParams.add(new BasicNameValuePair("redirect_uri", redirect_uri));


            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(postParams, "UTF-8");

            httpPost.setEntity(urlEncodedFormEntity);

            HttpResponse response = client.execute(httpPost);

            responseBody = JSON.parseObject(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            logger.error("UserService getAccessTokenForMmkj error", e);

        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("UserService getAccessTokenForMmkj io error", e);
            }
        }
        String accessToken = null;
        if(responseBody != null){
            accessToken = responseBody.getString("access_token");
        }
        System.out.println("accessToken=" + accessToken);
        return accessToken;
    }

    @Override
    public List<FlowData> getFlowList(String accessToken, String synTime) {
        JSONObject responseBody = null;
        CloseableHttpClient client = null;

        try {
            client = HttpClients.createDefault();

            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(hostForToken)
                    .setPath(pathForFlowData)
                    .build();

            HttpPost httpPost = new HttpPost(uri);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader("Cache-Control", "no-cache");

            List<BasicNameValuePair> postParams = new ArrayList<BasicNameValuePair>();

            postParams.add(new BasicNameValuePair("access_token", accessToken));
            postParams.add(new BasicNameValuePair("synTime", synTime));


            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(postParams, "UTF-8");

            httpPost.setEntity(urlEncodedFormEntity);

            HttpResponse response = client.execute(httpPost);

            responseBody = JSON.parseObject(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            logger.error("UserService getFlowList error", e);

        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("UserService getFlowList io error", e);
            }
        }
        JSONObject data = null;
        JSONArray list = null;
        List<FlowData> flowDataList = new ArrayList<>();
        if(responseBody != null){
            data = responseBody.getJSONObject("data");
            list = data.getJSONArray("list");
            if(list != null ||list.size()!= 0){
                for(int i=0 ; i< list.size() ; i++){
                    FlowData flowData = new FlowData();
                   JSONObject jo = list.getJSONObject(i);
                   flowData.setFlowId(jo.getString("flowId"));
                   flowData.setAge(jo.getIntValue("age"));
                   flowData.setGender(jo.getIntValue("gender"));
                   flowData.setAppearTime(jo.getString("appearTime"));
                   flowData.setLeaveTime(jo.getString("leaveTime"));
                   flowData.setShopId(jo.getIntValue("shopId"));
                   flowData.setAreaId(jo.getIntValue("areaId"));
                   flowData.setAreaType(jo.getIntValue("areaType"));
                   flowData.setUploadTime(jo.getString("uploadTime"));
                   flowData.setPicUrl(jo.getString("picUrl"));
                   flowDataList.add(flowData);
                }
            }
        }
        return flowDataList;
    }


}
