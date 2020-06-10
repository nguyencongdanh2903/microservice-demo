/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zalopay.bankconnector.service;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.zalopay.bankconnector.service.converter.ZP2BankCoreMessageConverter;
import com.zalopay.bankconnector.web.rest.vm.ActionResult;

import zalopay.bankconnector.enums.ReturnCodeEnum;
import zalopay.bankconnector.util.ExceptionUtil;
import zalopay.bankconnector.util.GsonUtils;

/**
 *
 * @author
 */
public class ClientUtils {

    private static final Logger sbisLog = Logger.getLogger("sbisLog");
    private static final String SHA256withRSA = "SHA256withRSA";
    private final static Charset UTF8_CHARSET = Charset.forName("UTF-8");
    
    public static String signData(String reqData, PrivateKey privateKey, ActionResult actionResult) throws NoSuchAlgorithmException {
        try {

            try {
                Signature signature = Signature.getInstance(SHA256withRSA);
                signature.initSign(privateKey);
                signature.update(reqData.getBytes());
                byte[] signatureBytes = signature.sign();

                String result = Hex.encodeHexString(signatureBytes);
                actionResult.returnCode = ReturnCodeEnum.SUCCESSFUL.getValue();
                return result;

            } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
                System.err.println(ExceptionUtil.getExInfo(ex));
                actionResult.exception = ExceptionUtil.getExInfo(ex);
                actionResult.returnCode = ReturnCodeEnum.ENCRYPT_FAIL.getValue();
                actionResult.returnMessage = "Fail to signData";
                return "";
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ZP2BankCoreMessageConverter.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public static boolean verify(String message, String sign, PublicKey publicKey, ActionResult actionResult) {
        try {
            final Signature sig = Signature.getInstance(SHA256withRSA);
            sig.initVerify(publicKey);
            sig.update(message.getBytes(StandardCharsets.UTF_8));

            return sig.verify(Hex.decodeHex(sign.toCharArray()));
        } catch (Exception ex) {
            System.err.println(ExceptionUtil.getExInfo(ex));
            actionResult.exception = ExceptionUtil.getExInfo(ex);
            actionResult.returnCode = ReturnCodeEnum.ENCRYPT_FAIL.getValue();
            actionResult.returnMessage = "Fail to verify data";
            return false;
        }
    }

    public static <T> T parseJsonResponse(String resp, Class<T> clazz, ActionResult actionResult) throws Exception {
        try {
            T boResp = GsonUtils.fromJsonString(resp, clazz);
            actionResult.returnCode = ReturnCodeEnum.SUCCESSFUL.getValue();
            return boResp;
        } catch (Exception ex) {
            actionResult.exception = ExceptionUtil.getExInfo(ex);
            actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
            actionResult.returnMessage = "parse bank data exception";
            sbisLog.error(String.format("parse bank data fro class %s ex: \n data : %s \n ActionResult %s \n Exception: %s", clazz, resp,
                    actionResult.toJsonString(), ExceptionUtil.getExInfo(ex)));
            throw ex;
        }
    }
    
    public static String generateSigData(String data, String key, ActionResult actionResult, String alg) throws Exception{
        MessageDigest mDigest = MessageDigest.getInstance(alg);
        String input = String.format("%s|%s", data, key);
        byte[] shaByteArr = mDigest.digest(encodeUTF8(input));
        return Hex.encodeHexString(shaByteArr);
    }
    private static byte[] encodeUTF8(String string) {
        return string.getBytes(UTF8_CHARSET);
    }
    
    public static String POSTMETHOD(String url, List<NameValuePair> params, String proxyHost, int port, int timeout)
            throws Exception {
        HttpHost proxy = new HttpHost(proxyHost, port);
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setStaleConnectionCheckEnabled(true).setProxy(proxy).build();
        
        try (CloseableHttpClient httpclient =
                HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build()) {
            HttpPost httppost = new HttpPost(url);
            
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            
            // Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    String result = IOUtils.toString(instream, "UTF-8");
                    return result;
                }
            }
        }
        return null;
    }
    

}
