package com.jbase.CNMB.TITSOAP;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLException;

//import java.io.IOException;
//import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * TODO: Document me!
 *
 * @author jsun
 *
 */

public class PostSoapMessageOBRetry {

       
    public String onMessage(String dynArray) {
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

        CloseableHttpClient httpclient = null;
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setRetryHandler(new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException arg0, int retryTimes, HttpContext arg2) {
                if (retryTimes > 3) {
                    return false;
                }
                if (arg0 instanceof UnknownHostException || arg0 instanceof ConnectTimeoutException
                        || !(arg0 instanceof SSLException) || arg0 instanceof NoHttpResponseException) {
                    return true;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(arg2);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    return true;
                }
                return false;
            }
        });

        System.out.println("set timeout is:" + maxTimeToWait);
        RequestConfig config=RequestConfig.custom()
                .setConnectTimeout(maxTimeToWait) // 设置连接超时时间 10秒钟
                .setConnectionRequestTimeout(maxTimeToWait)
                .setSocketTimeout(maxTimeToWait) // 设置读取超时时间10秒钟
                //.setResponseTimeout(maxTimeToWait, TimeUnit.MILLISECONDS)
                //.setRetryHandler(myRetryHandler)
                .build();
        httpclient = httpClientBuilder.build();
        
/*        CloseableHttpClient httpClient = HttpClients.custom()
                //.setRetryHandler(myRetryHandler)
                .setDefaultRequestConfig(config)
                .build(); // 创建httpClient实例
*/        
        
        
        HttpPost httpPost = new HttpPost(sUrl); // 创建httpget实例
        httpPost.setConfig(config);
        StringEntity stringEntity = new StringEntity(soapRequestData, StandardCharsets.UTF_8);       
        httpPost.setEntity(stringEntity);        

        // 设置 HttpPost 请求参数       
        httpPost.addHeader("Content-Type", sContentType);
        CloseableHttpResponse response = null;
        String strSoapResponse = "";

        try {
            response = httpclient.execute(httpPost); // 执行http 请求
            HttpEntity entity = response.getEntity(); // 获取返回实体
            strSoapResponse = EntityUtils.toString(entity, "utf-8");
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Response status:" + statusCode);
            System.out.println("Response:" + strSoapResponse); // 获取网页内容
            
            if (statusCode != HttpStatus.SC_OK){                
                strSoapResponse = "POST_SOAP_ERROR";
            }
            response.close(); // response关闭
            httpclient.close(); // httpClient关闭
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
