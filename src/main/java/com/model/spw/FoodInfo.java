package com.model.spw;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/11/24.
 */
public class FoodInfo {


    private  long shopID;
    private  long reportDate;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    private String foodCode;
    private int foodCount;
    private String foodName;
    private BigDecimal foodPriceAmount;
    private BigDecimal foodRealAmount;



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

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
    }

    public BigDecimal getFoodPriceAmount() {
        return foodPriceAmount;
    }

    public void setFoodPriceAmount(BigDecimal foodPriceAmount) {
        this.foodPriceAmount = foodPriceAmount;
    }

    public BigDecimal getFoodRealAmount() {
        return foodRealAmount;
    }

    public void setFoodRealAmount(BigDecimal foodRealAmount) {
        this.foodRealAmount = foodRealAmount;
    }
}
