package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.model.spw.ResponseInfo;
import com.model.spw.YamlInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by feichen on 17/9/29.
 */
public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static final String SIGNATURE_PREFIX = "key"; // 签名前缀
    public static final String SIGNATURE_SUFFIX = "secret"; // 签名后缀

    /**
     * 得到29-Sep-2017形式的日期
     *
     * @param s
     * @return
     */
    public static String getSpecialDate(String s) throws Exception {
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yy", Locale.US);

        //Date  date = new Date();

        String month[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jue", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        Date date = null;
        try {
            date = sdf2.parse(s);
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

            StringBuilder str = new StringBuilder();
            String[] strings = sdf3.format(date).split("-");
            str.append(strings[2]);
            str.append("-");
            str.append(month[Integer.parseInt(strings[1]) - 1]);
            str.append("-");
            str.append(strings[0]);
            return str.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * SHA1 安全加密算法
     *
     * @param encryptStr 待加密字符串
     */
    public static String getSha1(String encryptStr) throws Exception {
        try {
            if (StringUtils.isBlank(encryptStr)) {
                return null;
            }
            //指定sha1算法
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(encryptStr.getBytes());
            //获取字节数组
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            // 转换为全大写
            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * MD5 加密算法
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        String res = "";
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return res;
        }
    }


//    /**
//     * 写入yaml配置文件
//     * </br>
//     */
//    public static void writeYaml(YamlInfo yamlInfo) throws FileNotFoundException {
//        if (yamlInfo == null) {
//            logger.error("util writeYaml failed,case: yamlInfo is null");
//            return;
//        }
//        try {
//            // 获取当前路径
//            // File file = new File(System.getProperty("user.dir"));
//            // get parent dir,main.yml dir
//            //  String ymlDir = file.getParent() + File.separator + "src/main/conf" + File.separator + "main.yml";
//            //File dumpFile = new File("D://soft/main.yml");
//            File dumpFile = new File("/home/hualala/hualala_jde_client/main.yml");
//            Yaml.dump(yamlInfo, dumpFile);
//        } catch (FileNotFoundException e) {
//            logger.error("util writeYaml error", e);
//            throw e;
//        }
//    }

//    /**
//     * 读取yaml配置文件
//     * </br>
//     */
//    public static YamlInfo readYaml() throws FileNotFoundException {
//        try {
//            // 获取当前路径
////      File file = new File(System.getProperty("user.dir"));
////      // get parent dir,main.yml dir
////      String ymlDir = file.getParent() + File.separator + "src/main/conf" + File.separator + "main.yml";
//            //File dumpFile = new File("D://soft/main.yml");
//            File dumpFile = new File("/home/hualala/hualala_jde_client/main.yml");
//            YamlInfo yamlInfo = (YamlInfo) Yaml.loadType(dumpFile, YamlInfo.class);
//            return yamlInfo;
//        } catch (FileNotFoundException e) {
//            logger.error("util readYaml error", e);
//            throw e;
//        }
//    }

    /**
     * 发送http post请求
     * </p>
     */
    public static ResponseInfo sendHttpPost(String url, String encoding, Map<String, String> paramMap,
                                            Map<String, String> headerMap)
            throws Exception {
        CloseableHttpClient client = null;
        HttpPost httpPost = null;
        try {
            //创建httpclient对象
            client = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(75000)
                    .setConnectTimeout(45000).build();//设置请求和传输超时时间
            //创建post方式请求对象
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            if (headerMap != null && headerMap.size() > 0) {
                for (String key : headerMap.keySet()) {
                    httpPost.addHeader(key, headerMap.get(key));
                }
            }
            //装填参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (paramMap != null && paramMap.size() > 0) {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            //设置参数到请求对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
            StringBuilder sb = new StringBuilder();
            sb.append("url: " + url).append(" ").append("请求参数: " + nvps.toString() + " ")
                    .append("返回结果: ");
            //执行请求操作，并拿到结果（同步阻塞）
            CloseableHttpResponse response = client.execute(httpPost);
            //获取结果实体
            HttpEntity entity = response.getEntity();
            Object result = null;
            if (entity != null) {
                //按指定编码转换结果实体为String类型
                result = JSONObject.parse(EntityUtils.toString(entity, encoding));
                sb.append(result.toString());
            } else {
                sb.append("无返回结果!");
            }
            EntityUtils.consume(entity);

            ResponseInfo responseInfo = new ResponseInfo();
            responseInfo.setHttpStatus(response.getStatusLine().getStatusCode() + "");
            responseInfo.setResponse(JSON.toJSONString(result));
            logger.info(sb.toString());
            return responseInfo;
        } catch (Exception e) {
            throw e;
        } finally {
            if (httpPost != null) {
                httpPost.abort();
            }
            if (client != null) {
                client.close();
            }
        }
    }

    //将当前日期转换成JuLian
    public static int dateToJuLian(java.util.Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR) - 1900;
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        return year * 1000 + dayOfYear;
    }

    //juLian 转换成正常日期
    public static java.util.Date juLianToDate(int date) {
        int year = (date / 1000) + 1900;
        int dayOfYear = date % 1000;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

        return calendar.getTime();
    }

    //：如23:59:59转为235959
    public static int dateToInt() {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
//    int day = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        // System.out.println(Integer.valueOf(String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second)));
        return Integer.valueOf(String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second));
    }

    /**
     * 把2018-04-10 转为20180410
     * @param date
     * @return
     */
    public static String convertDate(String date){
        StringBuilder sb = new StringBuilder();
        if(!StringUtils.isBlank(date)){
            String[] dates = date.split("-");
            sb.append(dates[0]+dates[1]+dates[2]);
            return sb.toString();
        }
        return "";

    }
//  public static void main(String[] args) {
//
////    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
////    Date date = null;
////    try {
////      date = (Date) formatter.parse(String.valueOf(20171109));
////    } catch (ParseException e) {
////      e.printStackTrace();
////    }
// //   System.out.println(Util.convertDate("2018-04-10"));
////      Util.dateToInt();
//  }




}
