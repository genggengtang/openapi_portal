package com.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.model.spw.*;
import com.service.OrderService;
import com.util.Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.sql.DriverManager.getConnection;

/**
 * Created by zhangnan on 2017/11/21.
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    public String getToken() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("grant_type", "client_credentials");
        map.put("scope", "read");

        Map<String, String> mapHeader = new HashMap<String, String>();
        mapHeader.put("Authorization", "Basic " + Base64.getEncoder().encodeToString("gxspw:XrhaOUdcSHbv"
                .getBytes()));
        //gxspw:XrhaOUdcSHbv
        ResponseInfo response = null;
        String token = null;
        try {
            response = Util.sendHttpPost("http://auth.hualala.com/oauth/token", "UTF-8", map, mapHeader);
        } catch (Exception e) {
            if (response == null || !response.getHttpStatus().equals("200")) {
                try {
                    response = Util.sendHttpPost("http://auth.hualala.com/oauth/token", "UTF-8", map, mapHeader);
                } catch (Exception e1) {
                    if (response == null || !response.getHttpStatus().equals("200")) {
                        logger.info("请求auth 失败");
                        return "";
                    }
                }
            }
        }
        try {
            token = response.getResponse().split(",")[0].split(":")[1].replaceAll("\"", "");
            System.out.println("token=" + token);
            return token;
        } catch (Exception e) {
            logger.error("get token failed", e);
        }
        return "";
    }


    public void importSalesNumForCategory(long groupID, long shopID, long reportDate, int count) {
        // 获取db连接
        PreparedStatement pstmt = null;
        ResultSet res = null;
        Connection conn = null;
        try {
            // 读取yml文件中时间戳
            Map<String, Object> foodData = queryFoood(groupID, shopID, reportDate);
            Map<String, Object> yuanGongCanData = queryYuanGongCanInfo(groupID, shopID, reportDate);
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = getConnection("jdbc:Oracle:thin:@192.168.1.133:1521:JDEDB", "POSPROD", "POSPROD");
            //conn = getConnection("jdbc:Oracle:thin:@localhost:1521:orcl", "C##RF", "123456");
            conn.setAutoCommit(false);
            String sql1 = "INSERT INTO TEST(ID,FREE) VALUES (?,?)";
            String sqlForNine = "INSERT INTO F58Q508(PSCTLF,PSE58PSSN,PSE58SLD,PSE58BANO,PSLITM,PSTRDJ,PSUORG,PSE58UDRC,PSURAT,PSURRF,PSUPMT,PSUPMJ,PSUOM,PSURCD,PSURDT,PSUSER,PSPID,PSJOBN,PSR1" +
                    ",PSR2" +
                    ",PSR3" +
                    ",PSEV01" +
                    ",PSEV02" +
                    ",PSEV03" +
                    ",PSINDATE" +
                    ",PSINRMK" +
                    ",PSREDATE" +
                    ",PSRERMK" +
                    ",PSUKID" +
                    ",PSSTATUS" +
                    ",PSFLAG" +
                    ",PSSTRUPMT" +
                    ",PSEFTA,PSURAB) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pstmt = conn.prepareStatement(sqlForNine);
            String shopIDForSpwNew = (String) foodData.get("shopIDForSpw");
            if (foodData != null && foodData.size() != 0 && !StringUtils.isBlank(shopIDForSpwNew)) {
                pstmt.setString(1, "9");
                String shopIDForSpw = String.valueOf(foodData.get("shopIDForSpw"));
                pstmt.setString(2, (StringUtils.isBlank(shopIDForSpw) ? "null" : shopIDForSpw));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                Date date = null;
                try {
                    date = (Date) formatter.parse(String.valueOf(reportDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pstmt.setInt(3, Util.dateToJuLian(date));
                pstmt.setString(4, String.valueOf(reportDate) + "01");
                pstmt.setString(5, " ");
                pstmt.setInt(6, Util.dateToJuLian(date));
                pstmt.setInt(7, 0);
                //count为传入的参数值
                int sum = count + ((int) yuanGongCanData.get("count"));
                if (sum < 0) {
                    sum = 0;
                }
                pstmt.setInt(8, sum);
                pstmt.setInt(9, 0);
                pstmt.setInt(10, 0);
                pstmt.setInt(11, Util.dateToInt());
                pstmt.setInt(12, Util.dateToJuLian(new Date()));
                pstmt.setString(13, "EA");

                pstmt.setString(14, " ");
                pstmt.setInt(15, 0);
                pstmt.setString(16, " ");
                pstmt.setString(17, " ");
                pstmt.setString(18, " ");

                pstmt.setString(19, " ");
                pstmt.setString(20, " ");
                pstmt.setString(21, " ");
                pstmt.setString(22, " ");
                pstmt.setString(23, " ");
                pstmt.setString(24, " ");

                pstmt.setDate(25, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setString(26, " ");
                pstmt.setDate(27, new java.sql.Date(System.currentTimeMillis()));

                pstmt.setString(28, " ");
                pstmt.setInt(29, 0);
                pstmt.setString(30, " ");
                pstmt.setString(31, " ");
                pstmt.setString(32, " ");
                pstmt.setString(33, " ");
                pstmt.setInt(34, 0);
                pstmt.execute();
                conn.commit();
            }
        } catch (Exception e) {
            try {
                logger.error("importSalesNumForCategory F58Q508 insert rollback", e);
                conn.rollback();
            } catch (Exception e1) {
                logger.error("importSalesNumForCategory F58Q508 insert rollback failed", e1);
            }
        } finally {
            try {
                if (res != null) {
                    res.close();
                    res = null;
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                logger.error("importSalesNumForCategory F58Q508 close resource failed", e);
            }
        }
    }

    @Override
    public int importSalesNumForFood(long groupID, long shopID, long reportDate) {
        // 获取db连接
        PreparedStatement pstmt = null;
        ResultSet res = null;
        Connection conn = null;
        int count = 0;
        try {
            // 读取yml文件中时间戳
            Map<String, Object> foodData = queryFoood(groupID, shopID, reportDate);
            List<FoodInfo> lists = (List) foodData.get("foodList");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = getConnection("jdbc:Oracle:thin:@192.168.1.133:1521:JDEDB", "POSPROD", "POSPROD");
            //conn = getConnection("jdbc:Oracle:thin:@localhost:1521:orcl", "C##RF", "123456");
            conn.setAutoCommit(false);
            String sqlForOne = "INSERT INTO F58Q508(PSCTLF,PSE58PSSN,PSE58SLD,PSE58BANO,PSLITM,PSTRDJ,PSUORG,PSE58UDRC,PSURAT,PSURRF,PSUPMT,PSUPMJ,PSUOM,PSURCD,PSURDT,PSUSER,PSPID,PSJOBN,PSR1" +
                    ",PSR2" +
                    ",PSR3" +
                    ",PSEV01" +
                    ",PSEV02" +
                    ",PSEV03" +
                    ",PSINDATE" +
                    ",PSINRMK" +
                    ",PSREDATE" +
                    ",PSRERMK" +
                    ",PSUKID" +
                    ",PSSTATUS" +
                    ",PSFLAG" +
                    ",PSSTRUPMT" +
                    ",PSEFTA,PSURAB) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sqlForOne);
            String shopIDForSpwNew = (String) foodData.get("shopIDForSpw");
            Map<String, Object> mapFoodPriceAmount = new HashMap<>();
            Map<String, Object> mapFoodRealAmount = new HashMap<>();
            Map<String, Object> mapFoodNumber = new HashMap<>();
            if (foodData != null && foodData.size() != 0 && !StringUtils.isBlank(shopIDForSpwNew)) {
                for (int i = 0; i < lists.size(); i++) {
                    if (StringUtils.isBlank(lists.get(i).getFoodCode())) {
                        mapFoodPriceAmount.put("8050006", lists.get(i).getFoodPriceAmount());
                        mapFoodRealAmount.put("8050006", lists.get(i).getFoodRealAmount());
                        mapFoodNumber.put("8050006", (lists.get(i).getFoodPriceAmount().floatValue()) / 1.5);
                        continue;
                    }
                    pstmt.setString(1, "1");
                    String shopIDForSpw = String.valueOf(foodData.get("shopIDForSpw"));
                    pstmt.setString(2, (StringUtils.isBlank(shopIDForSpw) ? "null" : shopIDForSpw));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(String.valueOf(reportDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    pstmt.setInt(3, Util.dateToJuLian(date));
                    pstmt.setString(4, String.valueOf(reportDate) + "01");
                    String foodName = lists.get(i).getFoodName();

                    String foodCode = lists.get(i).getFoodCode();

                    System.out.println("foodCode=" + foodCode);
                    pstmt.setString(5, (StringUtils.isBlank(foodCode) ? "" : foodCode));
                    pstmt.setString(6, String.valueOf(Util.dateToJuLian(date)));
                    int result = (lists.get(i).getFoodCount()) * 1000;
                    //String code = "8050006";
                    if (lists.get(i).getFoodCode().equals("8050006") && mapFoodNumber != null && mapFoodNumber.containsKey("8050006")) {
                        double s = (double) mapFoodNumber.get("8050006");
                        int a = (int) s;
                        int count1 = lists.get(i).getFoodCount() + a;
                        result = (int) (count1 * 1000);
                    }
                    pstmt.setInt(7, result);
                    pstmt.setInt(8, 1);
                    float foodRealAmount = lists.get(i).getFoodRealAmount().floatValue();
                    if (lists.get(i).getFoodCode().equals("8050006") && mapFoodRealAmount != null && mapFoodRealAmount.containsKey("8050006")) {
                        BigDecimal b = (BigDecimal) mapFoodRealAmount.get("8050006");
                        float bb = b.floatValue();
                        foodRealAmount = lists.get(i).getFoodRealAmount().floatValue() + bb;
                    }
                    pstmt.setFloat(9, foodRealAmount);
                    float foodPriceAmount = lists.get(i).getFoodPriceAmount().floatValue();
                    if (lists.get(i).getFoodCode().equals("8050006") && mapFoodPriceAmount != null && mapFoodPriceAmount.containsKey("8050006")) {
                        BigDecimal b = (BigDecimal) mapFoodPriceAmount.get("8050006");
                        float bb = b.floatValue();
                        foodPriceAmount = lists.get(i).getFoodPriceAmount().floatValue() + bb;
                    }
                    pstmt.setFloat(10, foodPriceAmount);
                    pstmt.setInt(11, Util.dateToInt());
                    pstmt.setInt(12, Util.dateToJuLian(new Date()));
                    pstmt.setString(13, "EA");

                    pstmt.setString(14, " ");
                    pstmt.setInt(15, 0);
                    pstmt.setString(16, " ");
                    pstmt.setString(17, " ");
                    pstmt.setString(18, " ");

                    pstmt.setString(19, " ");
                    pstmt.setString(20, " ");
                    pstmt.setString(21, " ");
                    pstmt.setString(22, " ");
                    pstmt.setString(23, " ");
                    pstmt.setString(24, " ");

                    pstmt.setDate(25, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setString(26, " ");
                    pstmt.setDate(27, new java.sql.Date(System.currentTimeMillis()));

                    pstmt.setString(28, " ");
                    pstmt.setInt(29, 0);
                    pstmt.setString(30, " ");
                    pstmt.setString(31, " ");
                    pstmt.setString(32, " ");
                    pstmt.setString(33, " ");
                    pstmt.setInt(34, 0);
                    //累加插入记录的数量
                    count++;
                    System.out.println("************281");
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                conn.commit();
                pstmt.clearBatch();
                return count;
            }
        } catch (Exception e) {
            try {
                logger.error("importSalesNumForFood F58Q508 insert failed rollback", e);
                //回滚 count置0
                count = 0;
                conn.rollback();
            } catch (Exception e1) {
                logger.error("importSalesNumForFood F58Q508 rollback failed", e1);
            }
        } finally {
            try {
                if (res != null) {
                    res.close();
                    res = null;
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                logger.error("importSalesNumForFood F58Q508 close resource failed", e);
            }
            return count;
        }
    }

    @Override
    public Map<String, Object> queryFoood(long groupID, long shopID, long reportDate) {
        logger.info("groupID=" + groupID + " shopID=" + shopID + " reportDate=" + reportDate);
        try {
            String token = getToken();
            Map<String, String> mapForData = new HashMap<String, String>();
            mapForData.put("groupID", String.valueOf(groupID));
            mapForData.put("shopID", String.valueOf(shopID));
            mapForData.put("reportDate", String.valueOf(reportDate));
            Map<String, String> mapHeaderForData = new HashMap<String, String>();
            mapHeaderForData.put("Authorization", "bearer " + token);
            ResponseInfo responseForData = null;
            try {
                responseForData = Util.sendHttpPost("http://open-api.hualala.com/rlj/getOrderFoodAndPayInfoByReportDate", "UTF-8", mapForData, mapHeaderForData);
            } catch (Exception e) {
                if (responseForData == null || !responseForData.getHttpStatus().equals("200")) {
                    try {
                        responseForData = Util.sendHttpPost("http://open-api.hualala.com/rlj/getOrderFoodAndPayInfoByReportDate", "UTF-8", mapForData, mapHeaderForData);
                    } catch (Exception e1) {
                        if (responseForData == null || !responseForData.getHttpStatus().equals("200")) {
                            logger.error("请求open-api getOrderFoodAndPayInfoByReportDate失败");
                            return null;
                        }
                    }
                }
            }
            JSONObject jsonForData = JSONObject.parseObject(responseForData.getResponse());
            int foodItemSum = 0;
            Map<String, Object> resultMap = new HashMap<>();
            if (jsonForData.getString("code").equals("000")) {
                JSONObject data = jsonForData.getJSONObject("data");
                String shopIDForSpw = (String) data.getString("shopIDForSpw");
                resultMap.put("shopIDForSpw", shopIDForSpw);
                foodItemSum = (int) data.getIntValue("foodItemSum");
                resultMap.put("foodItemSum", foodItemSum);
                JSONArray foodData = data.getJSONArray("foodSum");
                List<FoodInfo> foodList = new ArrayList<>();
                if (foodData.size() > 0) {
                    for (int i = 0; i < foodData.size(); i++) {
                        JSONObject json = foodData.getJSONObject(i);
                        FoodInfo foodInfo = new FoodInfo();
                        foodInfo.setFoodPriceAmount(json.getBigDecimal("foodPriceAmount"));
                        foodInfo.setReportDate(json.getLongValue("reportDate"));
                        foodInfo.setFoodRealAmount(json.getBigDecimal("foodRealAmount"));
                        foodInfo.setFoodCount(json.getIntValue("foodNumber"));
                        foodInfo.setShopID(json.getLongValue("shopID"));
                        foodInfo.setFoodCode(json.getString("foodCode"));
                        // foodInfo.setFoodName(json.getString("foodName"));
                        foodList.add(foodInfo);
                    }
                }

                Collections.sort(foodList, new Comparator<FoodInfo>() {
                    @Override
                    public int compare(FoodInfo o1, FoodInfo o2) {

                        String s1 = o1.getFoodCode();
                        String s2 = o2.getFoodCode();
                        int len1 = s1.length();
                        int len2 = s2.length();
                        int n = Math.min(len1, len2);
                        char v1[] = s1.toCharArray();
                        char v2[] = s2.toCharArray();
                        int pos = 0;

                        while (n-- != 0) {
                            char c1 = v1[pos];
                            char c2 = v2[pos];
                            if (c1 != c2) {
                                return c1 - c2;
                            }
                            pos++;
                        }
                        return len1 - len2;
                    }
                });
                resultMap.put("foodList", foodList);
            }
            return resultMap;
        } catch (Exception e) {
            logger.error("queryFoood failed", e);
        }
        return null;
    }

    @Override
    public Map<String, Object> queryPayInfo(long groupID, long shopID, long reportDate) {
        logger.info("\"groupID=\"+groupID+\" shopID=\"+shopID+\" reportDate=\"+reportDate");
        try {
            String token = getToken();
            Map<String, String> mapForData = new HashMap<String, String>();
            mapForData.put("groupID", String.valueOf(groupID));
            mapForData.put("shopID", String.valueOf(shopID));
            mapForData.put("reportDate", String.valueOf(reportDate));
            Map<String, String> mapHeaderForData = new HashMap<String, String>();
            mapHeaderForData.put("Authorization", "bearer " + token);
            ResponseInfo responseForData = null;
            try {
                responseForData = Util.sendHttpPost("http://open-api.hualala.com/rlj/getPayInfoByReportDate", "UTF-8", mapForData, mapHeaderForData);
            } catch (Exception e1) {
                if (responseForData != null || !responseForData.getHttpStatus().equals("200")) {
                    try {
                        responseForData = Util.sendHttpPost("http://open-api.hualala.com/rlj/getPayInfoByReportDate", "UTF-8", mapForData, mapHeaderForData);
                    } catch (Exception e2) {
                        if (responseForData == null || !responseForData.getHttpStatus().equals("200")) {
                            logger.error("访问open-api getPayInfoByReportDate 失败");
                            return null;
                        }
                    }
                }
            }
            JSONObject jsonForData = JSONObject.parseObject(responseForData.getResponse());
            int foodCategorySum = 0;
            Map<String, Object> resultMap = new HashMap<>();
            if (jsonForData.getString("code").equals("000")) {
                JSONObject data = jsonForData.getJSONObject("data");
                String shopIDForSpw = (String) data.getString("shopIDForSpw");
                //  BigDecimal foodPaidAmountByMeiTuan =(BigDecimal)data.getBigDecimal("foodPaidAmountByMeiTuan");
                resultMap.put("shopIDForSpw", shopIDForSpw);
                // resultMap.put("foodPaidAmountByMeiTuan",foodPaidAmountByMeiTuan);
                JSONArray payInfoListForJson = data.getJSONArray("payInfoList");
                List<PayInfo> payInfoList = new ArrayList<>();
                if (payInfoListForJson.size() > 0) {
                    Map<String, BigDecimal> foodCodeValueMap = new HashMap<String, BigDecimal>();
                    for (int i = 0; i < payInfoListForJson.size(); i++) {
                        JSONObject json = payInfoListForJson.getJSONObject(i);
                        PayInfo payInfo = JSONObject.toJavaObject(json, PayInfo.class);
                        if (!foodCodeValueMap.containsKey(payInfo.getFoodCategoryKey())) {
                            foodCodeValueMap.put(payInfo.getFoodCategoryKey(), payInfo.getFoodPaidAmount());
                            System.out.println("448:" + payInfo.getFoodPaidAmount());
                            payInfoList.add(payInfo);
                        } else {
                            foodCodeValueMap.put(payInfo.getFoodCategoryKey(), foodCodeValueMap.get(payInfo.getFoodCategoryKey() + payInfo.getFoodPaidAmount()));
                            for (PayInfo payInfo1 : payInfoList) {
                                if (payInfo1.getFoodCategoryCode().equals(payInfo.getFoodCategoryKey())) {
                                    BigDecimal newValue = (BigDecimal) foodCodeValueMap.get(payInfo.getFoodCategoryKey());
                                    //  System.out.println("newValue="+newValue);
                                    payInfo1.setFoodPaidAmount(newValue);
                                }
                            }
                        }
                    }
                }
                resultMap.put("payInfoList", payInfoList);

                JSONArray payInfoYouHuiListForJson = data.getJSONArray("payInfoYouHuiList");
                List<PayInfoYouHui> payInfoYouHuiList = new ArrayList<>();
                BigDecimal meituanShouXuFei = new BigDecimal(0);
                BigDecimal elemeShouXuFei = new BigDecimal(0);
                if (payInfoYouHuiListForJson.size() > 0) {
                    Map<String, BigDecimal> foodCodeValueMap = new HashMap<String, BigDecimal>();
                    for (int i = 0; i < payInfoYouHuiListForJson.size(); i++) {
                        JSONObject json = payInfoYouHuiListForJson.getJSONObject(i);
                        PayInfoYouHui payInfoYouHui = JSONObject.toJavaObject(json, PayInfoYouHui.class);
						
						if(payInfoYouHui != null){
							String paySubjectCodeForZK = payInfoYouHui.getPaySubjectCodeForZK();
							if(paySubjectCodeForZK.equals("C004")) {
								meituanShouXuFei = meituanShouXuFei.add(payInfoYouHui.getAmountForZK());
								continue;
							}else if(paySubjectCodeForZK.equals("C005")) {
								elemeShouXuFei = elemeShouXuFei.add(payInfoYouHui.getAmountForZK());
								continue;
							}
							
//							if(shopIDForSpw.startsWith("9009")) {
//								if(paySubjectCodeForZK.equals("K002")) {
//									payInfoYouHui.setPaySubjectCodeForZK("K012");
//									payInfoYouHui.setPaySubjectNameForZK("微信收款-加盟店");
//								}else if(paySubjectCodeForZK.equals("K003")) {
//									payInfoYouHui.setPaySubjectCodeForZK("K019");
//									payInfoYouHui.setPaySubjectNameForZK("支付宝收款-加盟店");
//								}
//							}
							
							String paySubjectNameForZK = payInfoYouHui.getPaySubjectNameForZK();
							if(paySubjectNameForZK.equals("6560981583157794730") || paySubjectNameForZK.equals("第二杯半价")
									|| paySubjectNameForZK.equals("机场88折") || paySubjectNameForZK.endsWith("元半价饮料")){
	                            payInfoYouHui.setPaySubjectCodeForZK("J001");
	                        }else if(paySubjectNameForZK.equals("开业5折优惠")) {
	                        	payInfoYouHui.setPaySubjectCodeForZK("J002");
	                        }
                        
//                        if (payInfoYouHui.getPaySubjectNameForZK().equals("账单赠送")) {
//                            continue;
//                        }
							String paySbjCodeForZK = payInfoYouHui.getPaySubjectCodeForZK();
	                        if (!foodCodeValueMap.containsKey(paySbjCodeForZK)) {
	                            foodCodeValueMap.put(paySbjCodeForZK, payInfoYouHui.getAmountForZK());
	                            payInfoYouHuiList.add(payInfoYouHui);
	                        } else {
	                            foodCodeValueMap.put(paySbjCodeForZK, payInfoYouHui.getAmountForZK().add((BigDecimal) foodCodeValueMap.get(paySbjCodeForZK)));
	                            for (PayInfoYouHui payInfoYouHui1 : payInfoYouHuiList) {
	                                if (payInfoYouHui1.getPaySubjectCodeForZK().equals(paySbjCodeForZK)) {
	                                    BigDecimal newValue = (BigDecimal) foodCodeValueMap.get(paySbjCodeForZK);
	                                    System.out.println("youhui de newValue=" + newValue);
	                                    payInfoYouHui1.setAmountForZK(newValue);
	                                }
	                            }
	                        }
	                    }
                    }
                }
                resultMap.put("payInfoYouHuiList", payInfoYouHuiList);
                resultMap.put("foodPaidAmountByMeiTuan",meituanShouXuFei);
                resultMap.put("foodPaidAmountByEleme",elemeShouXuFei);

                JSONArray payInfoShoukuanListForJson = data.getJSONArray("payInfoShoukuanList");
                List<PayInfoShouKuan> payInfoShouKuanList = new ArrayList<>();
                Map<String, BigDecimal> foodCodeValueMap = new HashMap<String, BigDecimal>();
                if (payInfoShoukuanListForJson.size() > 0) {
                    for (int i = 0; i < payInfoShoukuanListForJson.size(); i++) {
                        JSONObject json = payInfoShoukuanListForJson.getJSONObject(i);
                        PayInfoShouKuan payInfoShouKuan = JSONObject.toJavaObject(json, PayInfoShouKuan.class);
                        
						if(payInfoShouKuan != null){
							String paySubjectCodeForSK = payInfoShouKuan.getPaySubjectCodeForSK();
							String paySubjectNameForSK = payInfoShouKuan.getPaySubjectNameForSK();
                        	if(paySubjectNameForSK.equals("绘画3元券"))
                        		payInfoShouKuan.setPaySubjectCodeForSK("J001");
                        	else if(paySubjectNameForSK.equals("会员现金卡值")) {
                        		payInfoShouKuan.setPaySubjectCodeForSK("K009");
                        	}else if(paySubjectNameForSK.equals("会员赠送卡值")) {
                        		payInfoShouKuan.setPaySubjectCodeForSK("J011");
                        	}else if(paySubjectNameForSK.equals("会员积分抵扣")) {
                        		payInfoShouKuan.setPaySubjectCodeForSK("J004");
                        	}else if(paySubjectNameForSK.equals("会员折扣")) {
                        		payInfoShouKuan.setPaySubjectCodeForSK("J005");
                        	}else if(paySubjectNameForSK.equals("会员卡挂账")) {
                        		payInfoShouKuan.setPaySubjectCodeForSK("K999");
                        	}else if(paySubjectNameForSK.equals("充值现金卡值")) {
                        		payInfoShouKuan.setPaySubjectCodeForSK("Y001");
                        	}else if(paySubjectNameForSK.equals("充值赠送卡值")) {
                        		payInfoShouKuan.setPaySubjectCodeForSK("Y002");
                        	}
                        	
                        	if(shopIDForSpw.startsWith("9009")) {
								if(paySubjectCodeForSK.equals("K002")) {
									payInfoShouKuan.setPaySubjectCodeForSK("K012");
									payInfoShouKuan.setPaySubjectNameForSK("微信收款-加盟店");
								}else if(paySubjectCodeForSK.equals("K003")) {
									payInfoShouKuan.setPaySubjectCodeForSK("K019");
									payInfoShouKuan.setPaySubjectNameForSK("支付宝收款-加盟店");
								}
							}
                        }
//                        //把美团手续费加入到美团外卖下面
//                        if(payInfoShouKuan != null && payInfoShouKuan.getPaySubjectCodeForSK().equals("K004")){
//                            payInfoShouKuan.setAmountForSK(payInfoShouKuan.getAmountForSK().add(meituanShouXuFei));
//                        }
                        if (!foodCodeValueMap.containsKey(payInfoShouKuan.getPaySubjectCodeForSK())) {
                            foodCodeValueMap.put(payInfoShouKuan.getPaySubjectCodeForSK(), payInfoShouKuan.getAmountForSK());
                            payInfoShouKuanList.add(payInfoShouKuan);
                        } else {
                            foodCodeValueMap.put(payInfoShouKuan.getPaySubjectCodeForSK(), payInfoShouKuan.getAmountForSK().add((BigDecimal) foodCodeValueMap.get(payInfoShouKuan.getPaySubjectCodeForSK())));
                            for (PayInfoShouKuan payInfoShouKuan1 : payInfoShouKuanList) {
                                if (payInfoShouKuan1.getPaySubjectCodeForSK().equals(payInfoShouKuan.getPaySubjectCodeForSK())) {
                                    BigDecimal newValue = (BigDecimal) foodCodeValueMap.get(payInfoShouKuan.getPaySubjectCodeForSK());
                                    System.out.println("newValue=" + newValue);
                                    payInfoShouKuan1.setAmountForSK(newValue);
                                }
                            }
                        }
                    }
                }
                for(PayInfoShouKuan payInfoShouKuan2 :payInfoShouKuanList){
                    
					if(payInfoShouKuan2 != null){
						String paySubjectCodeForSK = payInfoShouKuan2.getPaySubjectCodeForSK();
						if(paySubjectCodeForSK.equals("K004"))
							payInfoShouKuan2.setAmountForSK(payInfoShouKuan2.getAmountForSK().add(meituanShouXuFei));
						else if(paySubjectCodeForSK.equals("K005"))
							payInfoShouKuan2.setAmountForSK(payInfoShouKuan2.getAmountForSK().add(elemeShouXuFei));
                    }
                }
                resultMap.put("payInfoShoukuanList", payInfoShouKuanList);
            }
            return resultMap;
        } catch (Exception e) {
            logger.error("queryPayInfo failed", e);
        }
        return null;
    }

    @Override
    public void importPayInfo(long groupID, long shopID, long reportDate) {
        // 获取db连接
        PreparedStatement pstmt = null;
        ResultSet res = null;
        Connection conn = null;
        try {
            // 读取yml文件中时间戳
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = getConnection("jdbc:Oracle:thin:@192.168.1.133:1521:JDEDB", "POSPROD", "POSPROD");
            //conn = getConnection("jdbc:Oracle:thin:@localhost:1521:orcl", "C##RF", "123456");
            conn.setAutoCommit(false);
            Map<String, Object> payData = queryPayInfo(groupID, shopID, reportDate);
            List<PayInfo> payInfoList = (List) payData.get("payInfoList");
            List<PayInfoYouHui> payInfoYouHuiList = (List) payData.get("payInfoYouHuiList");
            List<PayInfoShouKuan> payInfoShoukuanList = (List) payData.get("payInfoShoukuanList");
            BigDecimal foodPaidAmountByMeiTuan = (BigDecimal)payData.get("foodPaidAmountByMeiTuan");
            BigDecimal foodPaidAmountByEleme = (BigDecimal)payData.get("foodPaidAmountByEleme");
            String sqlForOne = "INSERT INTO F58B091Z(PREDUS,PREDTN,PREDLN,PREDDT,PRTRDJ,PREDSP,PRMCU,PRDCTO,PRE58KDTY,PRGLC,PRTXR1,PRGL01,PRAA,PRINDATE,PRREDATE, PRCO,PRE58PSSN,PRCRCD,PRE58KSMSS,PRFYR" +
                    ",PRMT" +
                    ",PRDGJ" +
                    ",PRANI" +
                    ",PRAID" +
                    ",PROBJ" +
                    ",PRSUB" +
                    ",PRSBL" +
                    ",PRSBLT" +
                    ",PRICU" +
                    ",PRDCT" +
                    ",PRDOC" +
                    ",PRJELN" +
                    ",PREXA" +
                    ",PREXR" +
                    ",PRR1" +
                    ",PRR2" +
                    ",PRR3" +
                    ",PRURCD" +
                    ",PRURDT" +
                    ",PRURAT" +
                    ",PRURAB" +
                    ",PRURRF" +
                    ",PREV01" +
                    ",PREV02" +
                    ",PREV03" +
                    ",PRUSER" +
                    ",PRPID" +
                    ",PRJOBN" +
                    ",PRUPMJ" +
                    ",PRUPMT" +
                    ",PRINRMK" +
                    ",PRRERMK" +
                    ",PRUKID" +
                    ",PRSTATUS" +
                    ",PRFLAG" +
                    ",PRE58KABT1" +
                    ",PRE58KABR1" +
                    ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sqlForOne);
            if (payInfoList != null && payInfoList.size() != 0) {
                for (int i = 0; i < payInfoList.size(); i++) {
                    pstmt.setString(1, "POS");
                    pstmt.setString(2, String.valueOf(payData.get("shopIDForSpw")) + String.valueOf(reportDate) + "01");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(String.valueOf(reportDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    pstmt.setInt(3, (i + 1) * 1000);
                    pstmt.setInt(4, Util.dateToJuLian(new Date()));
                    pstmt.setInt(5, Util.dateToJuLian(date));
                    pstmt.setString(6, "0");
                    String shopIDForSpw = (String) payData.get("shopIDForSpw");
                    StringBuilder sb = new StringBuilder();
                    if (shopIDForSpw != null && shopIDForSpw.length() != 12) {
                        for (int j = 0; j < 12 - shopIDForSpw.length(); j++) {
                            sb.append(" ");
                        }
                        sb.append(shopIDForSpw);
                    }
                    pstmt.setString(7, sb.toString());
                    pstmt.setString(8, "DB");
                    pstmt.setString(9, "10");
                    String foodCategoryCode;
                    String foodCategoryKey = payInfoList.get(i).getFoodCategoryKey();
                    if (!StringUtils.isBlank(foodCategoryKey) && foodCategoryKey.equals("FCK_PACK_FEE")) {
                        foodCategoryCode = "805";
                    } else if (!StringUtils.isBlank(foodCategoryKey) && foodCategoryKey.equals("FCK_WS_SERVICE_FEE")) {
                        foodCategoryCode = "805";
                    } else {
                        foodCategoryCode = payInfoList.get(i).getFoodCategoryCode();
                    }
                    pstmt.setString(10, (StringUtils.isBlank(foodCategoryCode) ? " " : foodCategoryCode));
                    System.out.println("618:" + (payInfoList.get(i).getFoodPaidAmount()));
                    BigDecimal bg = new BigDecimal("100");
                    BigDecimal paidAmount = bg.multiply((BigDecimal) payInfoList.get(i).getFoodPaidAmount());
                    System.out.println("630:" + paidAmount);
                    int paidAmount1 = paidAmount.intValue();
                    //int paidAmount = (int) (payInfoList.get(i).getFoodPaidAmount() * 100);
                    System.out.println("633:" + paidAmount1);
                    pstmt.setInt(11, 6000);
                    pstmt.setString(12, " ");
                    pstmt.setInt(13, paidAmount1);
                    pstmt.setDate(14, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));
                    if (shopIDForSpw.startsWith("1009")) {
                        pstmt.setString(16, "00100");
                    } else {
                        pstmt.setString(16, "00900");
                    }
                    pstmt.setString(17, " ");
                    pstmt.setString(18, " ");
                    pstmt.setString(19, " ");
                    pstmt.setInt(20, 0);
                    pstmt.setInt(21, 0);
                    pstmt.setInt(22, 0);

                    pstmt.setString(23, " ");
                    pstmt.setString(24, " ");
                    pstmt.setString(25, " ");
                    pstmt.setString(26, " ");
                    pstmt.setString(27, " ");
                    pstmt.setString(28, " ");

                    pstmt.setInt(29, 0);
                    pstmt.setString(30, " ");
                    pstmt.setInt(31, 0);
                    pstmt.setInt(32, 0);

                    pstmt.setString(33, " ");
                    pstmt.setString(34, " ");
                    pstmt.setString(35, " ");
                    pstmt.setString(36, " ");
                    pstmt.setString(37, " ");
                    pstmt.setString(38, " ");

                    pstmt.setInt(39, 0);
                    pstmt.setInt(40, 0);
                    pstmt.setInt(41, 0);

                    pstmt.setString(42, " ");
                    pstmt.setString(43, " ");
                    pstmt.setString(44, " ");
                    pstmt.setString(45, " ");
                    pstmt.setString(46, " ");
                    pstmt.setString(47, " ");
                    pstmt.setString(48, " ");

                    pstmt.setInt(49, 0);
                    pstmt.setInt(50, 0);

                    pstmt.setString(51, " ");
                    pstmt.setString(52, " ");

                    pstmt.setInt(53, 0);

                    pstmt.setString(54, " ");
                    pstmt.setString(55, " ");
                    pstmt.setString(56, " ");
                    pstmt.setString(57, " ");


                    System.out.println("************");
                    pstmt.addBatch();
                }
                for (int i = 0; i < payInfoYouHuiList.size(); i++) {
                    System.out.println("702:" + payInfoYouHuiList.get(i).getPaySubjectNameForZK());
                    // if (!payInfoYouHuiList.get(i).getPaySubjectNameForZK().equals("账单赠送")) //把账单赠送去掉
                    // {
                    pstmt.setString(1, "POS");
                    pstmt.setString(2, String.valueOf(payData.get("shopIDForSpw")) + String.valueOf(reportDate) + "01");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(String.valueOf(reportDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    pstmt.setInt(3, (payInfoList.size() + i + 1) * 1000);
                    pstmt.setInt(4, Util.dateToJuLian(new Date()));
                    pstmt.setInt(5, Util.dateToJuLian(date));
                    pstmt.setString(6, "0");
                    String shopIDForSpw = (String) payData.get("shopIDForSpw");
                    StringBuilder sb = new StringBuilder();
                    if (shopIDForSpw != null && shopIDForSpw.length() != 12) {
                        for (int j = 0; j < 12 - shopIDForSpw.length(); j++) {
                            sb.append(" ");
                        }
                        sb.append(shopIDForSpw);
                    }
                    pstmt.setString(7, sb.toString());
                    pstmt.setString(8, "DB");
                    pstmt.setString(9, "30");
                    pstmt.setString(10, (StringUtils.isBlank(payInfoYouHuiList.get(i).getPaySubjectCodeForZK()) ? " " : payInfoYouHuiList.get(i).getPaySubjectCodeForZK()));
                    BigDecimal bg = new BigDecimal("100");
                    BigDecimal paidAmount1 = bg.multiply((BigDecimal) payInfoYouHuiList.get(i).getAmountForZK());
                    int paidAmount = paidAmount1.intValue();
                    pstmt.setInt(11, 6000);
                    pstmt.setString(12, " ");
                    pstmt.setInt(13, paidAmount);
                    pstmt.setDate(14, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));

                    if (shopIDForSpw.startsWith("1009")) {
                        pstmt.setString(16, "00100");
                    } else {
                        pstmt.setString(16, "00900");
                    }
                    pstmt.setString(17, " ");
                    pstmt.setString(18, " ");
                    pstmt.setString(19, " ");
                    pstmt.setInt(20, 0);
                    pstmt.setInt(21, 0);
                    pstmt.setInt(22, 0);

                    pstmt.setString(23, " ");
                    pstmt.setString(24, " ");
                    pstmt.setString(25, " ");
                    pstmt.setString(26, " ");
                    pstmt.setString(27, " ");
                    pstmt.setString(28, " ");

                    pstmt.setInt(29, 0);
                    pstmt.setString(30, " ");
                    pstmt.setInt(31, 0);
                    pstmt.setInt(32, 0);

                    pstmt.setString(33, " ");
                    pstmt.setString(34, " ");
                    pstmt.setString(35, " ");
                    pstmt.setString(36, " ");
                    pstmt.setString(37, " ");
                    pstmt.setString(38, " ");

                    pstmt.setInt(39, 0);
                    pstmt.setInt(40, 0);
                    pstmt.setInt(41, 0);

                    pstmt.setString(42, " ");
                    pstmt.setString(43, " ");
                    pstmt.setString(44, " ");
                    pstmt.setString(45, " ");
                    pstmt.setString(46, " ");
                    pstmt.setString(47, " ");
                    pstmt.setString(48, " ");

                    pstmt.setInt(49, 0);
                    pstmt.setInt(50, 0);

                    pstmt.setString(51, " ");
                    pstmt.setString(52, " ");

                    pstmt.setInt(53, 0);

                    pstmt.setString(54, " ");
                    pstmt.setString(55, " ");
                    pstmt.setString(56, " ");
                    pstmt.setString(57, " ");
                    System.out.println("************");
                    pstmt.addBatch();
                    // }
                }
//
                for (int i = 0; i < payInfoShoukuanList.size(); i++) {

                    pstmt.setString(1, "POS");

                    //pstmt.setString(1, payInfoShoukuanList.get(i).getDeviceName());
                    pstmt.setString(2, String.valueOf(payData.get("shopIDForSpw")) + String.valueOf(reportDate) + "01");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(String.valueOf(reportDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    pstmt.setInt(3, (payInfoList.size() + payInfoYouHuiList.size() + i + 1) * 1000);
                    pstmt.setInt(4, Util.dateToJuLian(new Date()));
                    pstmt.setInt(5, Util.dateToJuLian(date));
                    pstmt.setString(6, "0");
                    String shopIDForSpw = (String) payData.get("shopIDForSpw");
                    StringBuilder sb = new StringBuilder();
                    if (shopIDForSpw != null && shopIDForSpw.length() != 12) {
                        for (int j = 0; j < 12 - shopIDForSpw.length(); j++) {
                            sb.append(" ");
                        }
                        sb.append(shopIDForSpw);
                    }
                    pstmt.setString(7, sb.toString());
                    pstmt.setString(8, "DB");
                    pstmt.setString(9, "20");
                    pstmt.setString(10, (StringUtils.isBlank(payInfoShoukuanList.get(i).getPaySubjectCodeForSK()) ? " " : payInfoShoukuanList.get(i).getPaySubjectCodeForSK()));
                    BigDecimal bg = new BigDecimal("100");
                    BigDecimal paidAmount1 = bg.multiply((BigDecimal) payInfoShoukuanList.get(i).getAmountForSK());
                    int paidAmount = paidAmount1.intValue();
                    pstmt.setInt(11, 0);
                    pstmt.setString(12, " ");
                    pstmt.setInt(13, paidAmount);
                    pstmt.setDate(14, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));

                    if (shopIDForSpw.startsWith("1009")) {
                        pstmt.setString(16, "00100");
                    } else {
                        pstmt.setString(16, "00900");
                    }
                    pstmt.setString(17, " ");
                    pstmt.setString(18, " ");
                    pstmt.setString(19, " ");
                    pstmt.setInt(20, 0);
                    pstmt.setInt(21, 0);
                    pstmt.setInt(22, 0);

                    pstmt.setString(23, " ");
                    pstmt.setString(24, " ");
                    pstmt.setString(25, " ");
                    pstmt.setString(26, " ");
                    pstmt.setString(27, " ");
                    pstmt.setString(28, " ");

                    pstmt.setInt(29, 0);
                    pstmt.setString(30, " ");
                    pstmt.setInt(31, 0);
                    pstmt.setInt(32, 0);

                    pstmt.setString(33, " ");
                    pstmt.setString(34, " ");
                    pstmt.setString(35, " ");
                    pstmt.setString(36, " ");
                    pstmt.setString(37, " ");
                    pstmt.setString(38, " ");

                    pstmt.setInt(39, 0);
                    pstmt.setInt(40, 0);
                    pstmt.setInt(41, 0);

                    pstmt.setString(42, " ");
                    pstmt.setString(43, " ");
                    pstmt.setString(44, " ");
                    pstmt.setString(45, " ");
                    pstmt.setString(46, " ");
                    pstmt.setString(47, " ");
                    pstmt.setString(48, " ");

                    pstmt.setInt(49, 0);
                    pstmt.setInt(50, 0);

                    pstmt.setString(51, " ");
                    pstmt.setString(52, " ");

                    pstmt.setInt(53, 0);

                    pstmt.setString(54, " ");
                    pstmt.setString(55, " ");
                    pstmt.setString(56, " ");
                    pstmt.setString(57, " ");
                    System.out.println("************");
                    pstmt.addBatch();
                }

                List<String> foodCodes = new ArrayList<String>();
                foodCodes.add("K002");
                foodCodes.add("K003");
//                foodCodes.add("K005");
                foodCodes.add("K007");


                int shouXuFeiCount = 0;
                for (int i = 0; i < payInfoShoukuanList.size(); i++) {
                    String paySubjectCodeForSK = payInfoShoukuanList.get(i).getPaySubjectCodeForSK();
					if (foodCodes.contains(paySubjectCodeForSK)) {
                        pstmt.setString(1, "POS");
                        //pstmt.setString(1, payInfoShoukuanList.get(i).getDeviceName());
                        pstmt.setString(2, String.valueOf(payData.get("shopIDForSpw")) + String.valueOf(reportDate) + "01");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        Date date = null;
                        try {
                            date = (Date) formatter.parse(String.valueOf(reportDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        pstmt.setInt(3, (payInfoList.size() + payInfoYouHuiList.size() + payInfoShoukuanList.size() + shouXuFeiCount + 1) * 1000);
                        pstmt.setInt(4, Util.dateToJuLian(new Date()));
                        pstmt.setInt(5, Util.dateToJuLian(date));
                        pstmt.setString(6, "0");
                        String shopIDForSpw = (String) payData.get("shopIDForSpw");
                        StringBuilder sb = new StringBuilder();
                        if (shopIDForSpw != null && shopIDForSpw.length() != 12) {
                            for (int j = 0; j < 12 - shopIDForSpw.length(); j++) {
                                sb.append(" ");
                            }
                            sb.append(shopIDForSpw);
                        }
                        pstmt.setString(7, sb.toString());
                        pstmt.setString(8, "DB");
                        pstmt.setString(9, "40");
                        String chargeCode = "";
                        Double chargeValue = 0.00;
                        Double chargeRate = 0.00;
                        if (paySubjectCodeForSK.equals("K002")) {
                            chargeCode = "C002";
                            chargeValue = 0.00;
                            chargeRate = 0.00;
                        } else if (paySubjectCodeForSK.equals("K003")) {
                            chargeCode = "C003";
                            chargeValue = 0.00;
                            chargeRate = 0.00;
                        }
                        /**
                         * 在这里去掉美团
                         */
//                        else if (payInfoShoukuanList.get(i).getPaySubjectCodeForSK().equals("K004")) {
//                            if (payInfoShoukuanList.get(i).getAmountForSK().doubleValue() < 23.07) {
//                                chargeValue = 3.00;
//                                chargeRate = 0.00;
//                            } else {
//                                chargeValue = 0.00;
//                                chargeRate = 0.13;
//                            }
//                            chargeCode = "C004";
//                        }
                        
                        // 去掉饿了么
                        /*else if (payInfoShoukuanList.get(i).getPaySubjectCodeForSK().equals("K005")) {
                            if (payInfoShoukuanList.get(i).getAmountForSK().doubleValue() < 23.07) {
                                chargeValue = 3.00;
                                chargeRate = 0.00;
                            } else {
                                chargeValue = 0.00;
                                chargeRate = 0.13;
                            }
                            chargeCode = "C005";
                        }*/
                        
                        else if (paySubjectCodeForSK.equals("K007")) {
                            if (payInfoShoukuanList.get(i).getAmountForSK().doubleValue() < 23.6) {
                                chargeValue = 3.00;
                                chargeRate = 0.00;
                            } else {
                                chargeValue = 0.00;
                                chargeRate = 0.13;
                            }
                            chargeCode = "C007";
                        }
                        pstmt.setString(10, StringUtils.isBlank(chargeCode) ? " " : chargeCode);
                        BigDecimal bg = new BigDecimal("100");
                        BigDecimal bg1 = new BigDecimal(chargeRate);
                        BigDecimal bg2 = new BigDecimal(chargeValue);
                        BigDecimal paidAmount1 = bg.multiply(bg2.add(payInfoShoukuanList.get(i).getAmountForSK().multiply(bg1)));
                        int paidAmount = paidAmount1.intValue();
                        ;
                        pstmt.setInt(11, 0);
                        pstmt.setString(12, " ");
                        pstmt.setInt(13, paidAmount);
                        pstmt.setDate(14, new java.sql.Date(System.currentTimeMillis()));
                        pstmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));
                        if (shopIDForSpw.startsWith("1009")) {
                            pstmt.setString(16, "00100");
                        } else {
                            pstmt.setString(16, "00900");
                        }
                        pstmt.setString(17, " ");
                        pstmt.setString(18, " ");
                        pstmt.setString(19, " ");
                        pstmt.setInt(20, 0);
                        pstmt.setInt(21, 0);
                        pstmt.setInt(22, 0);
                        pstmt.setString(23, " ");
                        pstmt.setString(24, " ");
                        pstmt.setString(25, " ");
                        pstmt.setString(26, " ");
                        pstmt.setString(27, " ");
                        pstmt.setString(28, " ");

                        pstmt.setInt(29, 0);
                        pstmt.setString(30, " ");
                        pstmt.setInt(31, 0);
                        pstmt.setInt(32, 0);

                        pstmt.setString(33, " ");
                        pstmt.setString(34, " ");
                        pstmt.setString(35, " ");
                        pstmt.setString(36, " ");
                        pstmt.setString(37, " ");
                        pstmt.setString(38, " ");

                        pstmt.setInt(39, 0);
                        pstmt.setInt(40, 0);
                        pstmt.setInt(41, 0);

                        pstmt.setString(42, " ");
                        pstmt.setString(43, " ");
                        pstmt.setString(44, " ");
                        pstmt.setString(45, " ");
                        pstmt.setString(46, " ");
                        pstmt.setString(47, " ");
                        pstmt.setString(48, " ");

                        pstmt.setInt(49, 0);
                        pstmt.setInt(50, 0);

                        pstmt.setString(51, " ");
                        pstmt.setString(52, " ");

                        pstmt.setInt(53, 0);

                        pstmt.setString(54, " ");
                        pstmt.setString(55, " ");
                        pstmt.setString(56, " ");
                        pstmt.setString(57, " ");
                        System.out.println("************");
                        pstmt.addBatch();
                        shouXuFeiCount++;
                    }
                }


                /**
                 * 插入美团手续费
                 */
                pstmt.setString(1, "POS");
                //pstmt.setString(1, payInfoShoukuanList.get(i).getDeviceName());
                pstmt.setString(2, String.valueOf(payData.get("shopIDForSpw")) + String.valueOf(reportDate) + "01");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                Date date = null;
                try {
                    date = (Date) formatter.parse(String.valueOf(reportDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pstmt.setInt(3, (payInfoList.size() + payInfoYouHuiList.size() + payInfoShoukuanList.size()  + shouXuFeiCount+1) * 1000);
                pstmt.setInt(4, Util.dateToJuLian(new Date()));
                pstmt.setInt(5, Util.dateToJuLian(date));
                pstmt.setString(6, "0");
                String shopIDForSpw = (String) payData.get("shopIDForSpw");
                StringBuilder sb = new StringBuilder();
                if (shopIDForSpw != null && shopIDForSpw.length() != 12) {
                    for (int j = 0; j < 12 - shopIDForSpw.length(); j++) {
                        sb.append(" ");
                    }
                    sb.append(shopIDForSpw);
                }
                pstmt.setString(7, sb.toString());
                pstmt.setString(8, "DB");
                pstmt.setString(9, "40");

                Double chargeValue = 0.00;

                pstmt.setString(10,"C004");
                BigDecimal bg = new BigDecimal("100");
//                    BigDecimal bg2 = new BigDecimal(chargeValue);
//                    if(foodPaidAmountByMeiTuan.multiply(new BigDecimal(0.13)).doubleValue()>3){
//                        bg2= bg2.add(foodPaidAmountByMeiTuan.multiply(new BigDecimal(0.13)));
//                    }else{
//                        bg2= bg2.add(new BigDecimal(3));
//                    }
                BigDecimal paidAmount1 = bg.multiply(foodPaidAmountByMeiTuan);
                int paidAmount = paidAmount1.intValue();
                pstmt.setInt(11, 0);
                pstmt.setString(12, " ");
                pstmt.setInt(13, paidAmount);
                pstmt.setDate(14, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));
                if (shopIDForSpw.startsWith("1009")) {
                    pstmt.setString(16, "00100");
                } else {
                    pstmt.setString(16, "00900");
                }
                pstmt.setString(17, " ");
                pstmt.setString(18, " ");
                pstmt.setString(19, " ");
                pstmt.setInt(20, 0);
                pstmt.setInt(21, 0);
                pstmt.setInt(22, 0);
                pstmt.setString(23, " ");
                pstmt.setString(24, " ");
                pstmt.setString(25, " ");
                pstmt.setString(26, " ");
                pstmt.setString(27, " ");
                pstmt.setString(28, " ");

                pstmt.setInt(29, 0);
                pstmt.setString(30, " ");
                pstmt.setInt(31, 0);
                pstmt.setInt(32, 0);

                pstmt.setString(33, " ");
                pstmt.setString(34, " ");
                pstmt.setString(35, " ");
                pstmt.setString(36, " ");
                pstmt.setString(37, " ");
                pstmt.setString(38, " ");

                pstmt.setInt(39, 0);
                pstmt.setInt(40, 0);
                pstmt.setInt(41, 0);

                pstmt.setString(42, " ");
                pstmt.setString(43, " ");
                pstmt.setString(44, " ");
                pstmt.setString(45, " ");
                pstmt.setString(46, " ");
                pstmt.setString(47, " ");
                pstmt.setString(48, " ");

                pstmt.setInt(49, 0);
                pstmt.setInt(50, 0);

                pstmt.setString(51, " ");
                pstmt.setString(52, " ");

                pstmt.setInt(53, 0);

                pstmt.setString(54, " ");
                pstmt.setString(55, " ");
                pstmt.setString(56, " ");
                pstmt.setString(57, " ");
                System.out.println("************");
                pstmt.addBatch();
                
                
                /**
                 * 插入饿了么手续费
                 */
                pstmt.setString(1, "POS");
                //pstmt.setString(1, payInfoShoukuanList.get(i).getDeviceName());
                pstmt.setString(2, String.valueOf(payData.get("shopIDForSpw")) + String.valueOf(reportDate) + "01");
                try {
                    date = (Date) formatter.parse(String.valueOf(reportDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pstmt.setInt(3, (payInfoList.size() + payInfoYouHuiList.size() + payInfoShoukuanList.size()  + shouXuFeiCount+2) * 1000);
                pstmt.setInt(4, Util.dateToJuLian(new Date()));
                pstmt.setInt(5, Util.dateToJuLian(date));
                pstmt.setString(6, "0");
                String shopIDForSpw2 = (String) payData.get("shopIDForSpw");
                StringBuilder sb2 = new StringBuilder();
                if (shopIDForSpw != null && shopIDForSpw2.length() != 12) {
                    for (int j = 0; j < 12 - shopIDForSpw2.length(); j++) {
                        sb2.append(" ");
                    }
                    sb2.append(shopIDForSpw2);
                }
                pstmt.setString(7, sb.toString());
                pstmt.setString(8, "DB");
                pstmt.setString(9, "40");

                Double chargeValue2 = 0.00;

                pstmt.setString(10,"C005");
                BigDecimal bg2 = new BigDecimal("100");
//                    BigDecimal bg2 = new BigDecimal(chargeValue);
//                    if(foodPaidAmountByMeiTuan.multiply(new BigDecimal(0.13)).doubleValue()>3){
//                        bg2= bg2.add(foodPaidAmountByMeiTuan.multiply(new BigDecimal(0.13)));
//                    }else{
//                        bg2= bg2.add(new BigDecimal(3));
//                    }
                BigDecimal paidAmount2 = bg2.multiply(foodPaidAmountByEleme);
                int paidAmountEleme = paidAmount2.intValue();
                pstmt.setInt(11, 0);
                pstmt.setString(12, " ");
                pstmt.setInt(13, paidAmountEleme);
                pstmt.setDate(14, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setDate(15, new java.sql.Date(System.currentTimeMillis()));
                if (shopIDForSpw.startsWith("1009")) {
                    pstmt.setString(16, "00100");
                } else {
                    pstmt.setString(16, "00900");
                }
                pstmt.setString(17, " ");
                pstmt.setString(18, " ");
                pstmt.setString(19, " ");
                pstmt.setInt(20, 0);
                pstmt.setInt(21, 0);
                pstmt.setInt(22, 0);
                pstmt.setString(23, " ");
                pstmt.setString(24, " ");
                pstmt.setString(25, " ");
                pstmt.setString(26, " ");
                pstmt.setString(27, " ");
                pstmt.setString(28, " ");

                pstmt.setInt(29, 0);
                pstmt.setString(30, " ");
                pstmt.setInt(31, 0);
                pstmt.setInt(32, 0);

                pstmt.setString(33, " ");
                pstmt.setString(34, " ");
                pstmt.setString(35, " ");
                pstmt.setString(36, " ");
                pstmt.setString(37, " ");
                pstmt.setString(38, " ");

                pstmt.setInt(39, 0);
                pstmt.setInt(40, 0);
                pstmt.setInt(41, 0);

                pstmt.setString(42, " ");
                pstmt.setString(43, " ");
                pstmt.setString(44, " ");
                pstmt.setString(45, " ");
                pstmt.setString(46, " ");
                pstmt.setString(47, " ");
                pstmt.setString(48, " ");

                pstmt.setInt(49, 0);
                pstmt.setInt(50, 0);

                pstmt.setString(51, " ");
                pstmt.setString(52, " ");

                pstmt.setInt(53, 0);

                pstmt.setString(54, " ");
                pstmt.setString(55, " ");
                pstmt.setString(56, " ");
                pstmt.setString(57, " ");
                System.out.println("************");
                pstmt.addBatch();

                pstmt.executeBatch();
                conn.commit();
                pstmt.clearBatch();
            }
        } catch (Exception e) {
            try {
                logger.error("importPayInfo F58B091Z insert failed rollback", e);
                conn.rollback();
            } catch (Exception e1) {
                logger.error("importPayInfo F58B091Z insert rollback failed", e1);
            }
        } finally {
            try {
                if (res != null) {
                    res.close();
                    res = null;
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                 }
            } catch (Exception e) {
                logger.error("importPayInfo F58B091Z insert close resources failed", e);
            }
        }
    }

    @Override
    public Map<String, Object> queryYuanGongCanInfo(long groupID, long shopID, long reportDate) {
        try {
            String token = getToken();
            Map<String, String> mapForData = new HashMap<String, String>();
            mapForData.put("groupID", String.valueOf(groupID));
            mapForData.put("shopID", String.valueOf(shopID));
            mapForData.put("reportDate", String.valueOf(reportDate));

            Map<String, String> mapHeaderForData = new HashMap<String, String>();

            mapHeaderForData.put("Authorization", "bearer " + token);
            ResponseInfo responseForData = null;
            try {
                responseForData = Util.sendHttpPost("http://open-api.hualala.com/rlj/getYuanGongCanInfoByReportDate", "UTF-8", mapForData, mapHeaderForData);
            } catch (Exception e1) {
                if (responseForData != null || !responseForData.getHttpStatus().equals("200")) {
                    try {
                        responseForData = Util.sendHttpPost("http://open-api.hualala.com/rlj/getYuanGongCanInfoByReportDate", "UTF-8", mapForData, mapHeaderForData);
                    } catch (Exception e2) {
                        if (responseForData == null || !responseForData.getHttpStatus().equals("200")) {
                            logger.error("访问open-api getYuanGongCanInfoByReportDate 失败");
                            return null;
                        }
                    }
                }
            }
            JSONObject jsonForData = JSONObject.parseObject(responseForData.getResponse());
            int foodItemSum = 0;
            Map<String, Object> resultMap = new HashMap<>();
            if (jsonForData.getString("code").equals("000")) {
                JSONObject data = jsonForData.getJSONObject("data");
                String shopIDForSpw = (String) data.getString("shopIDForSpw");
                resultMap.put("shopIDForSpw", shopIDForSpw);
                JSONArray foodData = data.getJSONArray("foodList");
                List<FoodInfo> foodList = new ArrayList<>();
                int count = 0;
                if (foodData.size() > 0) {
                    for (int i = 0; i < foodData.size(); i++) {
                        JSONObject json = foodData.getJSONObject(i);
                        FoodInfo foodInfo = new FoodInfo();
                        foodInfo.setFoodPriceAmount(json.getBigDecimal("foodPriceAmount"));
                        foodInfo.setReportDate(json.getLongValue("reportDate"));
                        foodInfo.setFoodRealAmount(json.getBigDecimal("foodRealAmount"));
                        foodInfo.setFoodCount(json.getIntValue("foodNumber"));
                        foodInfo.setShopID(json.getLongValue("shopID"));
                        foodInfo.setFoodCode(json.getString("foodCode"));
                        foodInfo.setFoodName(json.getString("foodName"));
                        foodList.add(foodInfo);
                    }
                    count = foodData.size();
                }
                resultMap.put("foodList", foodList);
                resultMap.put("count", count);
            }
            return resultMap;
        } catch (Exception e) {
            logger.error("queryYuanGongCanInfo failed", e);
        }
        return null;
    }

    @Override
    public void importYuanGongCanInfo(long groupID, long shopID, long reportDate) {
        // 获取db连接
        PreparedStatement pstmt = null;
        ResultSet res = null;
        Connection conn = null;
        try {
            // 读取yml文件中时间戳
            System.out.println("196************");
            Map<String, Object> foodData = queryYuanGongCanInfo(groupID, shopID, reportDate);
            List<FoodInfo> lists = (List) foodData.get("foodList");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("202************");
            conn = getConnection("jdbc:Oracle:thin:@192.168.1.133:1521:JDEDB", "POSPROD", "POSPROD");
            //conn = getConnection("jdbc:Oracle:thin:@localhost:1521:orcl", "C##RF", "123456");
            conn.setAutoCommit(false);
            String sqlForOne = "INSERT INTO F58Q508(PSCTLF,PSE58PSSN,PSE58SLD,PSE58BANO,PSLITM,PSTRDJ,PSUORG,PSE58UDRC,PSURAT,PSURRF,PSUPMT,PSUPMJ,PSUOM,PSURCD,PSURDT,PSUSER,PSPID,PSJOBN,PSR1" +
                    ",PSR2" +
                    ",PSR3" +
                    ",PSEV01" +
                    ",PSEV02" +
                    ",PSEV03" +
                    ",PSINDATE" +
                    ",PSINRMK" +
                    ",PSREDATE" +
                    ",PSRERMK" +
                    ",PSUKID" +
                    ",PSSTATUS" +
                    ",PSFLAG" +
                    ",PSSTRUPMT" +
                    ",PSEFTA,PSURAB) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sqlForOne);
            if (foodData != null && foodData.size() != 0) {
                for (int i = 0; i < lists.size(); i++) {
                    pstmt.setString(1, "2");
                    String shopIDForSpw = String.valueOf(foodData.get("shopIDForSpw"));
                    pstmt.setString(2, (StringUtils.isBlank(shopIDForSpw) ? "null" : shopIDForSpw));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    Date date = null;
                    try {
                        date = (Date) formatter.parse(String.valueOf(reportDate));
                    } catch (ParseException e) {
                        logger.error("importYuanGongCanInfo date parse failed", e);
                    }
                    pstmt.setInt(3, Util.dateToJuLian(date));
                    pstmt.setString(4, String.valueOf(reportDate) + "01");
                    String foodCode = lists.get(i).getFoodCode();
                    int result = (lists.get(i).getFoodCount()) * 1000;
                    if (StringUtils.isBlank(lists.get(i).getFoodCode())) {
                        double count1 = (lists.get(i).getFoodPriceAmount().floatValue()) / 1.5;
                        result = (int) (count1 * 1000);
                    }
                    pstmt.setString(5, (StringUtils.isBlank(foodCode) ? "8050006" : foodCode));
                    pstmt.setString(6, String.valueOf(Util.dateToJuLian(date)));
                    pstmt.setInt(7, result);
                    pstmt.setInt(8, 1);
                    pstmt.setFloat(9, lists.get(i).getFoodRealAmount().floatValue());
                    pstmt.setString(10, String.valueOf(lists.get(i).getFoodPriceAmount().floatValue()));
                    pstmt.setInt(11, Util.dateToInt());
                    pstmt.setInt(12, Util.dateToJuLian(new Date()));
                    pstmt.setString(13, "EA");

                    pstmt.setString(14, " ");
                    pstmt.setInt(15, 0);
                    pstmt.setString(16, " ");
                    pstmt.setString(17, " ");
                    pstmt.setString(18, " ");

                    pstmt.setString(19, " ");
                    pstmt.setString(20, " ");
                    pstmt.setString(21, " ");
                    pstmt.setString(22, " ");
                    pstmt.setString(23, " ");
                    pstmt.setString(24, " ");
                    pstmt.setDate(25, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setString(26, " ");
                    pstmt.setDate(27, new java.sql.Date(System.currentTimeMillis()));
                    pstmt.setString(28, " ");
                    pstmt.setInt(29, 0);
                    pstmt.setString(30, " ");
                    pstmt.setString(31, " ");
                    pstmt.setString(32, " ");
                    pstmt.setString(33, " ");
                    pstmt.setInt(34, 0);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                conn.commit();
                pstmt.clearBatch();
            }
        } catch (Exception e) {
            try {
                logger.error("importYuanGongCanInfo F58Q508 insert failed rollback", e);
                conn.rollback();
            } catch (Exception e1) {
                logger.error("importYuanGongCanInfo F58Q508  rollback failed", e1);
            }
        } finally {
            try {
                if (res != null) {
                    res.close();
                    res = null;
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                logger.error("importYuanGongCanInfo F58Q508  close resources failed", e);
            }
        }
    }

    @Override
    public List<ShopInfo> queryShopIds(long groupID) {
        try {
            String token = getToken();
            Map<String, String> mapForData = new HashMap<String, String>();
            mapForData.put("groupID", String.valueOf(groupID));
            Map<String, String> mapHeaderForData = new HashMap<String, String>();
            mapHeaderForData.put("Authorization", "bearer " + token);
            ResponseInfo responseForData = null;
            try {
                responseForData = Util.sendHttpPost("http://open-api.hualala.com/order/getShopIds", "UTF-8", mapForData, mapHeaderForData);
            } catch (Exception e1) {
                if (responseForData == null || !responseForData.getHttpStatus().equals("200")) {
                    try {
                        responseForData = Util.sendHttpPost("http://open-api.hualala.com/order/getShopIds", "UTF-8", mapForData, mapHeaderForData);
                    } catch (Exception e2) {
                        if (responseForData == null || !responseForData.getHttpStatus().equals("200")) {
                            logger.error("访问open-api getShopIds failed");
                            return null;
                        }
                    }
                }
            }
            JSONObject jsonForData = JSONObject.parseObject(responseForData.getResponse());
            Map<String, Object> resultMap = new HashMap<>();
            List<ShopInfo> shopInfoList = new ArrayList<>();
            if (jsonForData.getString("code").equals("000")) {
                JSONObject data = jsonForData.getJSONObject("data");
                JSONArray jsonArray = data.getJSONArray("shopIds");
                if (jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        ShopInfo shopInfo = new ShopInfo();
                        Long shopID = jsonArray.getJSONObject(i).getLong("shopID");
                        String shopName = jsonArray.getJSONObject(i).getString("shopName");
                        shopInfo.setShopID(shopID);
                        shopInfo.setShopName(shopName);
                        shopInfoList.add(shopInfo);
                    }
                }
            }
            return shopInfoList;
        } catch (Exception e)
        {
            logger.error("queryShopIds failed", e);
        }
        return null;
    }

}
