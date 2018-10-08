package action;

import com.csvreader.CsvReader;
import com.opensymphony.xwork2.ActionSupport;
import com.util.Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoll on 2017/10/30.
 */
public class FileUploadAction extends ActionSupport {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadAction.class);

    //注意，file并不是指前端jsp上传过来的文件本身，而是文件上传过来存放在临时文件夹下面的文件
    private File file;

    //提交过来的file的名字
    private String fileFileName;

    //提交过来的file的MIME类型
    private String fileContentType;

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 150000;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public String getFileFileName()
    {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName)
    {
        this.fileFileName = fileFileName;
    }

    public String getFileContentType()
    {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType)
    {
        this.fileContentType = fileContentType;
    }

    public String execute(){

        StringBuilder data = new StringBuilder();
        //检查文件格式
        if(null==file || null==fileFileName.split("\\.")[1] ||
                !fileFileName.split("\\.")[1].equals("csv")){
            ServletActionContext.getRequest().setAttribute("message","请上传csv格式文件");
          return "fail";
        }
        try{
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(file.getAbsolutePath());
            // 读表头
            csvReader.readHeaders();
            while (csvReader.readRecord()) {
                // 读这行数据
                String dt = "<Act>"
                        + "<COMPANY_CODE>" +csvReader.get("COMPANY_CODE") + "</COMPANY_CODE>"
                        + "<STORE_ID>" + csvReader.get("STORE_ID") + "</STORE_ID>"
                        + "<BUSINESS_DATE>" + Util.getSpecialDate(csvReader.get("BUSINESS_DATE")) + "</BUSINESS_DATE>"
                        + "<CHANNEL_TYPE>" + csvReader.get("CHANNEL_TYPE") + "</CHANNEL_TYPE>"
                        + "<GROSS_SALES>" + csvReader.get("GROSS_SALES")  + "</GROSS_SALES>"
                        + "<DISCOUNT>" +csvReader.get("DISCOUNT") + "</DISCOUNT>"
                        + "<SALES_TAX>" + csvReader.get("SALES_TAX") + "</SALES_TAX>"
                        + "<NET_SALES>" + csvReader.get("NET_SALES") + "</NET_SALES>"
                        + "<CUST_COUNT>" + csvReader.get("CUSTOMER") + "</CUST_COUNT>"
                        + "<DOCKET_COUNT>" + csvReader.get("DOCKET")  + "</DOCKET_COUNT>"
                        + "</Act>";
                data.append(dt);
            }

            getPayDataTask(data.toString());

            ServletActionContext.getRequest().setAttribute("message","上传成功");

            return "success";

        }catch (Exception e){

            e.printStackTrace();
            ServletActionContext.getRequest().setAttribute("message","上传失败");
            return "fail";
        }
    }

    public void getPayDataTask(String data) throws Exception{

        String user_id = "mfgc2_uat";
        String user_p = "123456";
        HttpServletRequest request = ServletActionContext. getRequest();

        String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body>"
                + "<LoadSaleActDataXML xmlns=\"http://tempuri.org/\">"
                + "<xmlStringSaleActual>"
                + "<![CDATA["
                + "<SALEACTUALDATA>"
                + data.toString()
                + "</SALEACTUALDATA>"
                + "]]>"
                + "</xmlStringSaleActual>"
                + "<user_id>" + user_id + "</user_id>"
                + "<user_passwd>" + user_p + "</user_passwd>"
                + "</LoadSaleActDataXML>"
                + "</soap:Body>"
                + "</soap:Envelope>";

        logger.info("soapXML= "+soapXML);
        String postUrl = "https://mgscorp-bi-dev.azurewebsites.net/nws/service/websale4client.asmx";
        String soapAction = "http://tempuri.org/LoadSaleActDataXML";
        String retStr = "";
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(postUrl);
        HttpHost targetHost = new HttpHost("mgscorp-bi-dev.azurewebsites.net");
        HttpClientContext context = HttpClientContext.create();
        CloseableHttpResponse response = null;
        String httpStr = null;
        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(soapXML,
                    Charset.forName("UTF-8"));
            stringEntity.setContentType("text/xml");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return ;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return ;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
            logger.info("*******"+httpStr);
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return ;
    }


    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

}
