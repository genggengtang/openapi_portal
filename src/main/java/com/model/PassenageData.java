package com.model;

import java.util.Date;

public class PassenageData {
	
	 private String cameraId;
     private String areaName;
     private String flowId;
     private String groupId;
     private int age;
     private int gender;
     private Date appearTime;
     private Date leaveTime;
     private String picUrl;
     private String areaId;
     public String getCameraId() {
		return cameraId;
	}
	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public Date getAppearTime() {
		return appearTime;
	}
	public void setAppearTime(Date appearTime) {
		this.appearTime = appearTime;
	}
	public Date getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(Date leaveTime) {
		this.leaveTime = leaveTime;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public int getFloorName() {
		return floorName;
	}
	public void setFloorName(int floorName) {
		this.floorName = floorName;
	}
	public int getAreaType() {
		return areaType;
	}
	public void setAreaType(int areaType) {
		this.areaType = areaType;
	}
	public int getAreaBuiness() {
		return areaBuiness;
	}
	public void setAreaBuiness(int areaBuiness) {
		this.areaBuiness = areaBuiness;
	}
	private int floorName;
     private int areaType;
     private int areaBuiness;


}
