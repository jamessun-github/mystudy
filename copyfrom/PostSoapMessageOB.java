package com.jbase.CNMB.TITSOAP;

//import java.lang.reflect.Method;
//import java.util.logging.Logger;

//import com.jbase.jremote.JDynArray;
//import com.jbase.jremote.jca.inflow.JRemoteMessageListener;

//External package:
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.util.Iterator;

//import javax.xml.soap.SOAPBody;
//import javax.xml.soap.SOAPElement;
//import javax.xml.soap.SOAPMessage;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpException;

//import org.apache.commons.httpclient.*;


public class PostSoapMessageOB{
    public String TestFunction(String data) {
		System.out.println("Java says: Hello world james");
		return "TestFunction: " + data;
	}

    //private final static Logger logger = Logger.getLogger(PostSoapMessageOB.class.getName());
    /*private static final byte AM = -2;
    private static final byte VM = -3;
    private static final byte SVM = -4;  */

    public String onMessage(String dynArray) {
       // logger.info("onMessage [" + dynArray + "]");

    //if (this._context != null) {
        //String jdynSoapRsp = new String();

        String strArray[] = dynArray.split("TITSOAPSPLIT");

  /*      logger.info("onMessage0 [" + strArray[0] + "]");
        logger.info("onMessage1 [" + strArray[1] + "]");
        logger.info("onMessage2 [" + strArray[2] + "]");
        logger.info("onMessage3 [" + strArray[3] + "]");
        logger.info("onMessage4 [" + strArray[4] + "]");
        logger.info("onMessage5 [" + strArray[5] + "]");
        logger.info("onMessage6 [" + strArray[6] + "]");
        logger.info("onMessage7 [" + strArray[7] + "]");
        logger.info("onMessage8 [" + strArray[8] + "]");
        logger.info("onMessage9 [" + strArray[9] + "]");
        logger.info("onMessage10 [" + strArray[10] + "]");*/

        String soapRequestData = strArray[10];
        //logger.info(soapRequestData);
        String sUrl = strArray[9];
        String sContentType = strArray[8];
        String timeoutStr = strArray[3];
        String sRetry = strArray[4];
        //logger.info("sUrl [" + sUrl + "]");
        //logger.info("sContentType [" + sContentType + "]");
        //logger.info("timeoutStr [" + timeoutStr + "]");
        //logger.info("soapRequestData [" + soapRequestData + "]");

        System.out.println("sUrl [" + sUrl + "]");
        System.out.println("sContentType [" + sContentType + "]");
        System.out.println("timeoutStr [" + timeoutStr + "]");
        System.out.println("Retry1 [" + sRetry + "]");
        System.out.println("soapRequestData [" + soapRequestData + "]");
        
        //String sUrl = "http://10.128.1.120:8080/powerScreening/webservice/amlService?wsdl";
        //String sContentType = "text/xml; charset=utf-8";
        //String timeoutStr = "60";

        
        int maxTimeToWait;
        maxTimeToWait = 60000;
        if ((timeoutStr != null) && (timeoutStr.length() > 0)) {
          try {
            maxTimeToWait = Integer.valueOf(timeoutStr).intValue() * 1000;
          } catch (NumberFormatException e) {
            //logger.info("Timeout value invalid, using 60000 as default: " + timeoutStr);
            System.out.println("Timeout value invalid, using 60000 as default: " + timeoutStr);
          }
        }
        long lBefore = System.currentTimeMillis();

        PostMethod postMethod = new PostMethod(sUrl);

        String strSoapResponse = "";
        try {
        	byte[] b = soapRequestData.getBytes("utf-8");
        	InputStream is = new ByteArrayInputStream(b,0,b.length);
        	RequestEntity re = new InputStreamRequestEntity(is,b.length,sContentType);
        	postMethod.setRequestEntity(re);
// for retry
        	int iRetry = Integer.valueOf(sRetry).intValue();
        	//org.apache.commons.httpclient.DefaultMethodRetryHandler retryhandler = new org.apache.commons.httpclient.DefaultMethodRetryHandler();  
        	org.apache.commons.httpclient.DefaultMethodRetryHandler retryhandler = new org.apache.commons.httpclient.DefaultMethodRetryHandler();
        	retryhandler.setRequestSentRetryEnabled(false);  
        	retryhandler.setRetryCount(iRetry);        	
        	postMethod.setMethodRetryHandler(retryhandler); 
        	
        	HttpClient httpClient = new HttpClient();
        	httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(maxTimeToWait);
        	httpClient.getHttpConnectionManager().getParams().setSoTimeout(maxTimeToWait);
        	//httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        	int statusCode = httpClient.executeMethod(postMethod);

        	if (statusCode == HttpStatus.SC_OK) {
        	    //strSoapResponse = postMethod.getResponseBodyAsString();
                BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String str = "";
                while((str = reader.readLine())!=null){
                    stringBuffer.append(str);
                }
                strSoapResponse = stringBuffer.toString();
        	} else {
        	    strSoapResponse = "POST_SOAP_ERROR";
        	}
        	//logger.info("Response status:"+statusCode+",Msg:"+strSoapResponse);
        	//logger.info("Response status:"+statusCode);
        	System.out.println("Response status:"+statusCode);
            //return jdynSoapRsp;
//} else {
//    returnMq = "No context";
//    logger.info("Operation unSuccessful");
//    throw new Exception(returnMq);
//}
        } catch (HttpException e) {
            //logger.info("Exception while posting", ex);
            long lNow = System.currentTimeMillis() - lBefore;
            //logger.info("Operation unSuccessful,"+Long.toString(lNow));
            System.out.println("Operation unSuccessful,"+Long.toString(lNow));
            return new String("POST_SOAP_ERROR");
        } catch (IOException e) {
            long lTimeWait = System.currentTimeMillis() - lBefore;
            if (lTimeWait >= maxTimeToWait) {
                //logger.info("Network TimeOut Error,TimeWait is "+Long.toString(lTimeWait));
                System.out.println("Network TimeOut Error,TimeWait is "+Long.toString(lTimeWait));
                return new String("POST_TIMEOUT");
            } else {
                //logger.info("Network Other Error");
                System.out.println("Network Other Error");
                return new String("POST_SOAP_ERROR");
            }
        } finally {
            postMethod.releaseConnection();
        }
        return strSoapResponse;
    }


}
