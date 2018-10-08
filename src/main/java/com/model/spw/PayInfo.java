package com.model.spw;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/1/10.
 */
public class PayInfo {

    private BigDecimal foodPaidAmount;
    private long groupID;
    private long shopID;
    private long reportDate;

    public BigDecimal getFoodPaidAmount() {
        return foodPaidAmount;
    }

    public void setFoodPaidAmount(BigDecimal foodPaidAmount) {
        this.foodPaidAmount = foodPaidAmount;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public long getShopID() {
        return shopID;
    }

    public void setShopID(long shopID) {
        this.shopID = shopID;
    }

    public long getReportDate() {
        return reportDate;
    }

    public void setReportDate(long reportDate) {
        this.reportDate = reportDate;
    }


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private String deviceName;
    private String foodCategoryCode;
    private String foodCategoryKey;
    private String foodCategoryName;

    public String getFoodCategoryCode() {
        return foodCategoryCode;
    }

    public void setFoodCategoryCode(String foodCategoryCode) {
        this.foodCategoryCode = foodCategoryCode;
    }

    public String getFoodCategoryKey() {
        return foodCategoryKey;
    }

    public void setFoodCategoryKey(String foodCategoryKey) {
        this.foodCategoryKey = foodCategoryKey;
    }

    public String getFoodCategoryName() {
        return foodCategoryName;
    }

    public void setFoodCategoryName(String foodCategoryName) {
        this.foodCategoryName = foodCategoryName;
    }

}