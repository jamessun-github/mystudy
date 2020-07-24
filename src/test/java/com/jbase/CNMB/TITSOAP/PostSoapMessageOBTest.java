package com.jbase.CNMB.TITSOAP;

import org.junit.Test;

/**
 * TODO: Document me!
 *
 * @author jsun
 *
 */
public class PostSoapMessageOBTest {
    @Test
    public void testSoap() {
        PostSoapMessageOB soap = new PostSoapMessageOB();

        
        //String sUrl = "http://localhost:8090/CHBTWS/services";
        String sUrl = "http://10.252.241.196:7800/esbws?wsdl";
        String sContentType = "text/xml; charset=utf-8";
        String timeoutStr = "60";
        String sRetry = "3";
        String soapRequestData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:chb=\"http://temenos.com/CHBTWS\">   <soapenv:Header/>   <soapenv:Body>      <chb:callOfs>           <OfsRequest>ENQUIRY.SELECT,,ITFIB.01//HK0010256,ENQ.ITF.LOAN.BAL,ACCOUNT:EQ=256500001201</OfsRequest>      </chb:callOfs>   </soapenv:Body></soapenv:Envelope>";
        //String soapRequestData = "  <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:chb=\"http://temenos.com/CHBTWS\">   <soapenv:Header/>   <soapenv:Body>      <chb:callOfs>           <OfsRequest>ENQUIRY.SELECT,,ITFIB.01//HK0010256,ENQ.ITF.LOAN.BAL,ACCOUNT:EQ=256500001201</OfsRequest>      </chb:callOfs>   </soapenv:Body></soapenv:Envelope>";

        String strDelimit = "TITSOAPSPLIT";
        String dynArray =  strDelimit + strDelimit + strDelimit + timeoutStr + strDelimit + sRetry + strDelimit + strDelimit+strDelimit+strDelimit+ sContentType+ strDelimit+ sUrl + strDelimit+ soapRequestData;

       
        System.out.println("TEST Req = " + dynArray);        
        String result = soap.onMessage(dynArray);
        
        System.out.println("TEST Result = " + result);
        
        

    }
}


