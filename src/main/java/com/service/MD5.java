package com.service;

import java.security.MessageDigest;

public class MD5 {

	public static String toMD5to32(String plainText) {
	     try {
	        //����ʵ��ָ��ժҪ�㷨�� MessageDigest ����
	        MessageDigest md = MessageDigest.getInstance("MD5");  
	        //ʹ��ָ�����ֽ��������ժҪ��
	        md.update(plainText.getBytes());
	        //ͨ��ִ���������֮������ղ�����ɹ�ϣ���㡣
	        byte b[] = md.digest();
	        //���ɾ����md5���뵽buf����
	        int i;
	        StringBuffer buf = new StringBuffer("");
	        for (int offset = 0; offset < b.length; offset++) {
	          i = b[offset];
	          if (i < 0)
	            i += 256;
	          if (i < 16)
	            buf.append("0");
	          buf.append(Integer.toHexString(i));
	        }
	        return buf.toString();
	       
	        
	     } 
	     catch (Exception e) {
	       e.printStackTrace();
	     }
		return plainText;
	   }
	   
	
	public static String toMD5to16(String plainText) {
	     try {
	        //����ʵ��ָ��ժҪ�㷨�� MessageDigest ����
	        MessageDigest md = MessageDigest.getInstance("MD5");  
	        //ʹ��ָ�����ֽ��������ժҪ��
	        md.update(plainText.getBytes());
	        //ͨ��ִ���������֮������ղ�����ɹ�ϣ���㡣
	        byte b[] = md.digest();
	        //���ɾ����md5���뵽buf����
	        int i;
	        StringBuffer buf = new StringBuffer("");
	        for (int offset = 0; offset < b.length; offset++) {
	          i = b[offset];
	          if (i < 0)
	            i += 256;
	          if (i < 16)
	            buf.append("0");
	          buf.append(Integer.toHexString(i));
	        }
	        return buf.toString().substring(8, 24);
	        
	     } 
	     catch (Exception e) {
	       e.printStackTrace();
	     }
		return plainText;
	   }
	   
}
