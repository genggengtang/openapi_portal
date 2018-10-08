package com.model.spw;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/1/10.
 */
public class PayInfoShouKuan {

    private long groupID;
    private long shopID;
    private long reportDate;
    private String saasOrderKey;

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

    public String getSaasOrderKey() {
        return saasOrderKey;
    }

    public void setSaasOrderKey(String saasOrderKey) {
        this.saasOrderKey = saasOrderKey;
    }

    public String getPaySubjectCodeForSK() {
        return paySubjectCodeForSK;
    }

    public void setPaySubjectCodeForSK(String paySubjectCodeForSK) {
        this.paySubjectCodeForSK = paySubjectCodeForSK;
    }

    public String getPaySubjectNameForSK() {
        return paySubjectNameForSK;
    }

    public void setPaySubjectNameForSK(String paySubjectNameForSK) {
        this.paySubjectNameForSK = paySubjectNameForSK;
    }

    public BigDecimal getAmountForSK() {
        return amountForSK;
    }

    public void setAmountForSK(BigDecimal amountForSK) {
        this.amountForSK = amountForSK;
    }

    private String paySubjectCodeForSK;
    private String paySubjectNameForSK;
    private BigDecimal amountForSK;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private String deviceName;

}
