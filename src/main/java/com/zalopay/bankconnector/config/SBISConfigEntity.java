/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zalopay.bankconnector.config;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author
 */
@Component
@ConfigurationProperties(prefix = "sbisConfig")
public class SBISConfigEntity {

	public String bankCode = null;
	public String publicKeyStr = null;

	public transient PrivateKey privateKey = null;
	public transient PublicKey publicKey = null;
	public String url = null;
	public boolean bypassSSL = false;
	public String secretKey = null;
	public String merchantCode = null;
	public String caller = null;
	public String version = null;
	public String privateKeyStr = null;
	public boolean isVerifySignature = false;
	public String[] returnCodeRequireOTPList;
	public boolean isSimulateReturnCode = false;
	public String expectedReturnCode = "";
	public String defaultBankMID = "";
	public boolean useRefTraceNo = false;
	public String contentType = null;
	public boolean isProxyUsed = false;

	public SBISConfigEntity() {

	}

	@PostConstruct
	public void readPrivateKey() throws Exception {

		byte[] keyBytes = new byte[privateKeyStr.length()];
		privateKeyStr = privateKeyStr.replaceAll("(-+BEGIN RSA PRIVATE KEY-+\\r?\\n|-+END RSA PRIVATE KEY-+\\r?\\n?)",
				"");
		Base64 decoder = new Base64();
		keyBytes = decoder.decode(privateKeyStr);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		this.privateKey = keyFactory.generatePrivate(spec);
	}

	@PostConstruct
	public void getKey() {

		try {

			Base64 base64 = new Base64();
			byte[] byteKey = base64.decode(publicKeyStr.getBytes());
			javax.security.cert.X509Certificate cert = javax.security.cert.X509Certificate.getInstance(byteKey);

			publicKey = cert.getPublicKey();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public SBISConfigEntity(SBISConfigEntity other) {

	}

	public String getPrefixLog() {

		return String.format("%s: ", "");
	}

}
