package com.dao;

import java.util.HashMap;

import com.model.AccountInfo;
import com.model.DianpuUser;
import com.model.JituanUser;

public interface LoginDianpuMapper {
	public DianpuUser loadByDianpuUsername(String username);
	
	public AccountInfo getAccountInfo(String childAccount);
	public String getShopID(HashMap<String,Integer> map);
	public String getShopName(HashMap<String,Integer> map);


}
