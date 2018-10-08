package com.model.spw;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/1/10.
 */
public class PayInfoYouHui {

    private long groupID;
    private long shopID;
    private long reportDate;

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


    public String getPaySubjectCodeForZK() {
        return paySubjectCodeForZK;
    }

    public void setPaySubjectCodeForZK(String paySubjectCodeForZK) {
        this.paySubjectCodeForZK = paySubjectCodeForZK;
    }

    public String getPaySubjectNameForZK() {
        return paySubjectNameForZK;
    }

    public void setPaySubjectNameForZK(String paySubjectNameForZK) {
        this.paySubjectNameForZK = paySubjectNameForZK;
    }

    public BigDecimal getAmountForZK() {
        return amountForZK;
    }

    public void setAmountForZK(BigDecimal amountForZK) {
        this.amountForZK = amountForZK;
    }

    private String paySubjectCodeForZK;
    private String paySubjectNameForZK;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private BigDecimal amountForZK;
    private String deviceName;

}
