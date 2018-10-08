package com.model.spw;

/**
 * Created by zhangnan on 2017/11/21.
 */


public class YamlInfo {

  private String openapiToken; // openapi token
  private String foodTimestamp; // 菜品时间戳,初始值为0
  private String orderTimestamp; // 订单时间戳,初始值为0
  private String taskTime;//定时时间
public String getOpenapiToken() {
	return openapiToken;
}
public void setOpenapiToken(String openapiToken) {
	this.openapiToken = openapiToken;
}
public String getFoodTimestamp() {
	return foodTimestamp;
}
public void setFoodTimestamp(String foodTimestamp) {
	this.foodTimestamp = foodTimestamp;
}
public String getOrderTimestamp() {
	return orderTimestamp;
}
public void setOrderTimestamp(String orderTimestamp) {
	this.orderTimestamp = orderTimestamp;
}
public String getTaskTime() {
	return taskTime;
}
public void setTaskTime(String taskTime) {
	this.taskTime = taskTime;
}
}
