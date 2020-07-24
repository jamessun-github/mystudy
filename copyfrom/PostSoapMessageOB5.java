package com.jbase.CNMB.TITSOAP;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.java.tools.HttpClientUtil;

/**
 * TODO: Document me!
 *
 * @author jsun
 *
 */

public class PostSoapMessageOB5 {

       
    public String onMessage(String dynArray) throws ParseException {
        long lBefore = System.currentTimeMillis();
        
        String strArray[] = dynArray.split("TITSOAPSPLIT");
        String soapRequestData = strArray[10]; 
        String sUrl = strArray[9];
        String sContentType = strArray[8];
        String timeoutStr = strArray[3];
        String sRetry = strArray[4];


        System.out.println("sUrl [" + sUrl + "]");
        System.out.println("sContentType [" + sContentType + "]");
        System.out.println("timeoutStr [" + timeoutStr + "]");
        System.out.println("Retry1 [" + sRetry + "]");
        System.out.println("soapRequestData [" + soapRequestData + "]");


        int maxTimeToWait;
        maxTimeToWait = 60000;
        if ((timeoutStr != null) && (timeoutStr.length() > 0)) {
            try {
                maxTimeToWait = Integer.valueOf(timeoutStr).intValue() * 1000;
            } catch (NumberFormatException e) {
                // logger.info("Timeout value invalid, using 60000 as default: "
                // + timeoutStr);
                System.out.println("Timeout value invalid, using 60000 as default: " + timeoutStr);
            }
        }
        
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        String strSoapResponse = "";
        try {
            // 创建 HttpPost 对象
            HttpPost httpPost = new HttpPost(sUrl);

            StringEntity stringEntity = new StringEntity(soapRequestData, StandardCharsets.UTF_8); 
            // 设置 HttpPost 请求参数
            httpPost.setEntity(stringEntity);  
            
            // 设置 Content-Type
            httpPost.addHeader("Content-Type", sContentType);

            // 执行 Http Post 请求
            response = HttpClientUtil.getHttpclient().execute(httpPost);
            
            HttpEntity entity = response.getEntity(); // 获取返回实体
            strSoapResponse = EntityUtils.toString(entity, "utf-8");
            System.out.println("Response:" + strSoapResponse); // 获取网页内容
            response.close(); // response关闭
            //httpPost.close(); // httpClient关闭

            } catch (ClientProtocolException e) { // http协议异常
                long lNow = System.currentTimeMillis() - lBefore;
                System.out.println("Operation unSuccessful," + Long.toString(lNow));
                strSoapResponse = "POST_SOAP_ERROR";
                //e.printStackTrace();
            } catch (IOException e) { // io异常
                long lTimeWait = System.currentTimeMillis() - lBefore;
                if (lTimeWait >= maxTimeToWait) {
                    System.out.println("Network TimeOut Error,TimeWait is " + Long.toString(lTimeWait));
                    strSoapResponse = "POST_TIMEOUT";
                } else {
                    long lNow = System.currentTimeMillis() - lBefore;
                    System.out.println("Network Other Error," + Long.toString(lNow));
                    strSoapResponse = "POST_SOAP_ERROR";
                }
                //e.printStackTrace();
                
            }
        return strSoapResponse;
    }
    

}
