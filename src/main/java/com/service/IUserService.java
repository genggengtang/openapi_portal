package com.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.model.*;


public interface IUserService {

	public String login(String username,String password);
	public String queryInterfaceByUser(String username);
	public List<UserAuthDetails> queryUserAuth(HashMap<String,Integer> map);
	public void updateUserAuth(UserAuthDetails userAuthDetails,UserInterface userInterface);
	public void addUserAuth(UserAuthDetails userAuthDetails,UserInterface userInterface);
	public int queryUserAuthCount();
	
	public String loginDianpu(String account,String childAccount,String password);
	
	public AccountInfo getAccountInfo(String childAccount);
	
	public List<ShopDes> getShops(String accessToken);

	public List<ImageData> getImageData(HashMap<String,Object> map);
	
	public int queryImageDataCount(HashMap<String,Object> map);

	public String getAccessTokenForMmkj(String client_id,String client_secret,String grant_type,String code,String redirect_uri);

	public List<FlowData> getFlowList(String accessToken , String synTime);



}
