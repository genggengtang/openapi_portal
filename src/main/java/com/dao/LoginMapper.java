package com.dao;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.model.AccountInfo;
import com.model.DianpuUser;
import com.model.ImageData;
import com.model.JituanUser;
import com.model.User;
import com.model.UserAuthDetails;
import com.model.UserInterface;
public interface LoginMapper {	
	public User loadByUsername(String username);
	public String queryInterfaceByUser(String username);
	public List<UserAuthDetails> queryUserAuth(HashMap<String,Integer> map);
	public void updateUserAuth(UserAuthDetails userAuthDetails);
	public void updateInterfaces(UserInterface userInterface);
	public void addUserAuth(UserAuthDetails userAuthDetails);
	public void addInterfaces(UserInterface userInterface);
	public int queryUserAuthCount();
	public List<ImageData> getImageData(HashMap<String,Object> map);	
	public int queryImageDataCount(HashMap<String,Object> map);
}

