/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zalopay.bankconnector.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zalopay.bankconnector.config.ProxyConfig;
import com.zalopay.bankconnector.config.SBISConfigEntity;
import com.zalopay.bankconnector.constant.ApiNameConst;
import com.zalopay.bankconnector.constant.BankRequestParams;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.response.DefaultResponse;

import zalopay.bankconnector.bank.client.BankClient;
import zalopay.bankconnector.bank.entity.ZP2BankBaseRequest;
import zalopay.bankconnector.bank.entity.ZP2BankBaseResponse;
import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.enums.ReturnCodeEnum;
import zalopay.bankconnector.enums.SubTransTypeEnum;
import zalopay.bankconnector.util.ExceptionUtil;
import zalopay.bankconnector.util.HttpUtils;

/**
 *
 * @author
 */
public class SBISClient implements BankClient {

	private static final Logger sbisLog = Logger.getLogger("sbisLog");

	@Autowired
	private SBISConfigEntity sbisConfig;

	@Autowired
	private ProxyConfig proxy;

	@Override
	public ZP2BankBaseResponse request(ZP2BankBaseRequest bankClientRequest, ActionResult actionResult) {

		DefaultResponse bankResponse = new DefaultResponse();

		try {

			String strRequest = bankClientRequest.toJsonString();
			String strResponse = "";
			DefaultRequest requestData = (DefaultRequest) bankClientRequest;

			SubTransTypeEnum messageType = SubTransTypeEnum.fromInt(requestData.subTransType);

			try {

				actionResult.step = "request to sbis";
				bankClientRequest.bankAPIName = ApiNameConst.getApiNameFromSubTransType(messageType, actionResult);

				String url = new StringBuilder(sbisConfig.url).append(bankClientRequest.bankAPIName).toString();

				sbisLog.info(String.format(
						"sbisClient - Request to sbis for messageType = [%s] requestData [%s] : \nbypassSSL = %s , \nrequest = %s",
						messageType.name(), requestData.toJsonString(), sbisConfig.bypassSSL, strRequest));
				List<NameValuePair> params = new ArrayList<>();
				params.add(new BasicNameValuePair(BankRequestParams.REQUEST_ID, requestData.requestID));
				params.add(new BasicNameValuePair(BankRequestParams.TIME, requestData.time));
				params.add(new BasicNameValuePair(BankRequestParams.CLIENT_CODE, requestData.clientCode));
				params.add(new BasicNameValuePair(BankRequestParams.CLIENT_USER_ID, requestData.clientUserID));
				params.add(new BasicNameValuePair(BankRequestParams.DATA, requestData.data));
				params.add(new BasicNameValuePair(BankRequestParams.SIGNATURE, requestData.signature));

				if (sbisConfig.isProxyUsed) {
					strResponse = ClientUtils.POSTMETHOD(url, params, proxy.proxyHost, proxy.proxyPort,
							proxy.callTimeOutMilis);
				} else {
					strResponse = HttpUtils.POSTMETHOD(url, params);
				}

			} catch (Exception callBankException) {
				actionResult.exception = ExceptionUtil.getExInfo(callBankException);
				actionResult.returnCode = ReturnCodeEnum.CALL_BANK_EXCEPTION.getValue();
				actionResult.stepResult = "call bank ex";
				sbisLog.error(String.format("[%s] call to sbis ex: %s", sbisConfig.getPrefixLog(),
						ExceptionUtil.getExInfo(callBankException)));
				return bankResponse;
			}

			sbisLog.info(String.format(
					"sbisRestClient - Response from sbis for messageType = [%s] request [%s] : \nreponse = [%s]",
					messageType.name(), requestData.toJsonString(), strResponse));

			actionResult.step = "parse response from sbis";
			if ((StringUtils.isBlank(strResponse))) {
				actionResult.returnCode = ReturnCodeEnum.BANK_PARAMETER_INVALID.getValue();
				actionResult.returnMessage = "Bank response is blank";
				return bankResponse;
			}

			try {

				bankResponse = ClientUtils.parseJsonResponse(strResponse, DefaultResponse.class, actionResult);
				sbisLog.info(
						String.format("sbisRestClient - response after parsing = %s", bankResponse.toJsonString()));
			} catch (Exception parseResponseException) {
				actionResult.extraInfo = actionResult.returnMessage;
				actionResult.exception = ExceptionUtil.getExInfo(parseResponseException);
				actionResult.returnCode = ReturnCodeEnum.BANK_PARAMETER_INVALID.getValue();
				actionResult.stepResult = "parse response data  ex";
				sbisLog.error(String.format("[%s] parse response data ex: %s", sbisConfig.getPrefixLog(),
						ExceptionUtil.getExInfo(parseResponseException)));
				return bankResponse;
			}

			bankResponse.accountNo = requestData.accountNo;
			return bankResponse;
		} catch (Exception ex) {
			actionResult.exception = ExceptionUtil.getExInfo(ex);
			actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
			actionResult.stepResult = "request bank ex";
			actionResult.exception = ExceptionUtil.getExInfo(ex);
			sbisLog.error(sbisConfig.getPrefixLog()
					+ String.format("request to sbis error, ex %s", ExceptionUtils.getMessage(ex)));
			return bankResponse;
		}
	}

}
