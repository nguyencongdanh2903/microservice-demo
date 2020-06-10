package com.zalopay.bankconnector.service.handler;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zalopay.bankconnector.config.BankConnConfig;
import com.zalopay.bankconnector.service.BankClient;
import com.zalopay.bankconnector.service.converter.ZP2BankMessageConverter;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.ZP2BankBaseRequest;
import com.zalopay.bankconnector.service.request.ZP2BankDefaultRequest;
import com.zalopay.bankconnector.service.response.DefaultResponse;
import com.zalopay.bankconnector.service.response.ZP2BankBaseResponse;
import com.zalopay.bankconnector.service.selector.BankMIDSelector;
import com.zalopay.bankconnector.web.rest.vm.ActionResult;
import com.zalopay.bankconnector.web.rest.vm.request.InvokePaymentBankConnectorRequest;
import com.zalopay.bankconnector.web.rest.vm.response.InvokePaymentBankConnectorResponse;

import zalopay.bankconnector.amq.AMQueueProducer;
import zalopay.bankconnector.entity.bank.ZP2BankLogEntity;
import zalopay.bankconnector.entity.config.AMQueueConfigEntity;
import zalopay.bankconnector.enums.BankConnectorReturnStatusEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;
import zalopay.bankconnector.util.BankConnectorLogger;
import zalopay.bankconnector.util.CommonUtils;
import zalopay.bankconnector.util.ExceptionUtil;
import zalopay.bankconnector.util.GsonUtils;
import zalopay.bankconnector.util.SignatureUtils;

public class InvokeBankApiRequestHandler {

	private ZP2BankMessageConverter zp2BankMessageConverter;

	@Autowired
	private BankMIDSelector bankMIDSelector;

	private BankClient bankClient;

	@Autowired
	private AMQueueConfigEntity zp2bankLogQueueConfigEntity;

	@Autowired
	private BankConnConfig bankConn;

//	@Autowired
//	public void setZP2BankValidator(ZP2BankValidator zp2BankValidator) {
//
//		this.zp2BankValidator = zp2BankValidator;
//	}
//
//	@Autowired
//	public void setZP2BankMessageConverter(ZP2BankMessageConverter zp2BankMessageConverter) {
//
//		this.zp2BankMessageConverter = zp2BankMessageConverter;
//	}
	@Autowired
	public InvokeBankApiRequestHandler(ZP2BankMessageConverter zp2BankMessageConverter) {

		this.zp2BankMessageConverter = zp2BankMessageConverter;
	}

	public InvokePaymentBankConnectorResponse handle(InvokePaymentBankConnectorRequest paymentRequest) {

		InvokePaymentBankConnectorResponse paymentResponse = new InvokePaymentBankConnectorResponse();
		ActionResult actionResult = new ActionResult(ReturnCodeEnum.EXCEPTION.getValue(),
				"init actionResult, default returnCode = Exception");

		BankConnectorLogger.DEBUG_LOGGER.debug(
				String.format("action=handle_paymentRequest, paymentRequest=%s", paymentRequest.toMaskedString()));

		ZP2BankBaseRequest invokeBankRequest = null;
		ZP2BankBaseResponse invokeBankResponse = null;

		try {

			paymentResponse.transID = String.valueOf(paymentRequest.dataObj.transID);

			/*
			 * 3. convert request to bank client message
			 */

			actionResult.step = "3.1. convert to bank client message";
			invokeBankRequest = new ZP2BankDefaultRequest();
			invokeBankRequest = zp2BankMessageConverter.convertRequest(paymentRequest, invokeBankRequest, actionResult);
			if (actionResult.returnCode != ReturnCodeEnum.SUCCESSFUL.getValue()) {
				actionResult.stepResult = actionResult.returnMessage;
				paymentResponse.setResult(actionResult);
				BankConnectorLogger.ERROR_LOGGER
						.error(String.format("action=convertRequest, transID=%s, actionResult=%s",
								paymentRequest.dataObj.transID, actionResult.toJsonString()));
				makeResponseString(paymentResponse, actionResult);
				return paymentResponse;
			}

			/*
			 * 4. execute bankMIDSelector
			 */

			String selectedBankMID = bankMIDSelector.select(paymentRequest, actionResult);
			if (actionResult.returnCode != ReturnCodeEnum.SUCCESSFUL.getValue()) {
				actionResult.stepResult = actionResult.returnMessage;
				paymentResponse.setResult(actionResult);
				BankConnectorLogger.ERROR_LOGGER
						.error(String.format("action=bankMIDSelector.select, transID=%s, actionResult=%s",
								paymentRequest.dataObj.transID, actionResult.toJsonString()));
				makeResponseString(paymentResponse, actionResult);
				return paymentResponse;
			}
			invokeBankRequest.bankMID = selectedBankMID;

			/*
			 * 5. Invoke Bank Client
			 */

			actionResult.step = "5.1 call Bank Client";
			invokeBankResponse = bankClient.request(invokeBankRequest, actionResult);

			BankConnectorLogger.INFO_LOGGER.info(
					String.format("action=bankClient.request, actionResult=%s", invokeBankResponse.toMaskedString()));

			if (actionResult.returnCode != ReturnCodeEnum.SUCCESSFUL.getValue()) {
				actionResult.stepResult = actionResult.returnMessage;
				paymentResponse.setResult(actionResult);
				makeResponseString(paymentResponse, actionResult);
				return paymentResponse;
			}

			/*
			 * 6. convert bank response to api response message
			 */
			invokeBankResponse.transType = paymentRequest.dataObj.transType;
			invokeBankResponse.subTransType = paymentRequest.dataObj.subTransType;
			invokeBankResponse.requiredOTP = paymentRequest.dataObj.requiredOTP;
			actionResult.step = "6. convert bank response to api response message";
			paymentResponse = zp2BankMessageConverter.convertResponse(invokeBankRequest, invokeBankResponse,
					paymentResponse, actionResult);

			if (actionResult.returnCode != ReturnCodeEnum.SUCCESSFUL.getValue()) {
				actionResult.stepResult = actionResult.returnMessage;
				paymentResponse.setResult(actionResult);
				BankConnectorLogger.ERROR_LOGGER
						.error(String.format("action=convertResponse, actionResult=%s", actionResult.toJsonString()));
				makeResponseString(paymentResponse, actionResult);
				return paymentResponse;
			}

			if (paymentResponse.dataObj != null && StringUtils.isEmpty(paymentResponse.dataObj.bankMID)) {
				paymentResponse.dataObj.bankMID = selectedBankMID;
			}

			/*
			 * 7. make response string
			 */
			actionResult.returnCode = ReturnCodeEnum.SUCCESSFUL.getValue();
			actionResult.step = "7. make response string";
			paymentResponse.setResult(actionResult);
			makeResponseString(paymentResponse, actionResult);
			return paymentResponse;

		} catch (Exception ex) {
			actionResult.stepResult = "bank handle";
			actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
			actionResult.exception = ex.getMessage();
			paymentResponse.setResult(actionResult);
			BankConnectorLogger.ERROR_LOGGER
					.error(String.format("action=handle_paymentRequest, transID=%s, actionResult=%s, ex=%s",
							paymentRequest.dataObj.transID, actionResult.toJsonString(), ExceptionUtil.getExInfo(ex)));
			makeResponseString(paymentResponse, actionResult);
			return paymentResponse;
		} finally {

			/*
			 * save trans: put message to AMQ
			 */
			ZP2BankLogEntity logEntity = createLogEntity(paymentRequest, paymentResponse, invokeBankRequest,
					invokeBankResponse, actionResult);
			enqueueWriteLog(logEntity);

			/*
			 * Log4j if response status FAIL
			 */
			log4jIfFail(paymentRequest, paymentResponse, logEntity, actionResult);
		}
	}

	protected ZP2BankLogEntity addInvokeBankInfosToLogEntity(ZP2BankLogEntity logEntity,
			InvokePaymentBankConnectorRequest paymentRequest, InvokePaymentBankConnectorResponse paymentResponse,
			ZP2BankBaseRequest invokeBankRequest, ZP2BankBaseResponse invokeBankResponse, ActionResult actionResult) {

		if (invokeBankRequest != null) {
			DefaultRequest reqData = (DefaultRequest) invokeBankRequest;
			logEntity.bankAPIName = String.valueOf(reqData.subTransType);
			logEntity.requestID = reqData.requestID;
			logEntity.bankRequest = reqData.toJsonString();
			if (reqData.dataObj != null) {
				logEntity.bankReqData = reqData.dataObj.toJsonString();
			}
		}

		if (invokeBankResponse != null) {
			DefaultResponse repsData = (DefaultResponse) invokeBankResponse;
			logEntity.bankReturnCode = String.valueOf(repsData.code);
			logEntity.bankResponse = repsData.toJsonString();
			if (repsData.dataObj != null) {
				logEntity.bankTransID = repsData.dataObj.bankTransId;
				logEntity.bankRespData = repsData.dataObj.toJsonString();
			}
		}
		return logEntity;
	}

	private void log4jIfFail(InvokePaymentBankConnectorRequest paymentRequest,
			InvokePaymentBankConnectorResponse paymentResponse, ZP2BankLogEntity logEntity, ActionResult actionResult) {

		if (paymentResponse.code != BankConnectorReturnStatusEnum.SUCCESSFUL.getValue()
				|| (!paymentResponse.dataObj.returnCode.equals(ReturnCodeEnum.SUCCESSFUL.getValueAsString())
						&& !paymentResponse.dataObj.returnCode.equals(ReturnCodeEnum.PROCESSING.getValueAsString()))) {

			BankConnectorLogger.DEBUG_LOGGER
					.warn(String.format("paymentRequest=%s, paymentResponse=%s, logEntity=%s, actionResult=%s",
							paymentRequest.toMaskedString(), paymentResponse.toMaskedString(),
							logEntity.toMaskedString(), actionResult.toJsonString()));
		}
	}

	final protected ZP2BankLogEntity createLogEntity(InvokePaymentBankConnectorRequest paymentRequest,
			InvokePaymentBankConnectorResponse paymentResponse, ZP2BankBaseRequest invokeBankRequest,
			ZP2BankBaseResponse invokeBankResponse, ActionResult actionResult) {

		ZP2BankLogEntity logEntity = new ZP2BankLogEntity();

		logEntity.bankConnectorCode = bankConn.connCode;
		try {
			if (paymentRequest != null) {
				logEntity.env = paymentRequest.env;
				logEntity.reqTimeMilisec = paymentRequest.reqTimeMilisec;

				if (paymentRequest.dataObj != null) {
					logEntity.bankCode = paymentRequest.dataObj.bankCode;
					logEntity.zaloPayID = CommonUtils.parseLong(paymentRequest.dataObj.zaloPayID, -1);
					logEntity.transID = paymentRequest.dataObj.transID;
					logEntity.refTransID = paymentRequest.dataObj.refTransID;
					logEntity.bankRefTransID = paymentRequest.dataObj.refBankTransID;
					logEntity.refTraceNo = paymentRequest.dataObj.refTraceNo;
					logEntity.traceNo = paymentRequest.dataObj.traceNo;
					logEntity.pmcID = paymentRequest.dataObj.pmcID;
					logEntity.transType = paymentRequest.dataObj.transType;
					logEntity.subTransType = paymentRequest.dataObj.subTransType;
					logEntity.phone = paymentRequest.dataObj.phone;
					logEntity.transTime = paymentRequest.dataObj.transTime;
					logEntity.OTPValue = paymentRequest.dataObj.otpValue;
					logEntity.amount = paymentRequest.dataObj.amount;
					logEntity.bankMID = paymentRequest.dataObj.bankMID;
					if (paymentRequest.dataObj.paymentInfo != null) {
						logEntity.paymentInfo = GsonUtils.toJsonString(paymentRequest.dataObj.paymentInfo);
					}
					if (paymentRequest.dataObj.userInfo != null) {
						logEntity.userInfo = GsonUtils.toJsonString(paymentRequest.dataObj.userInfo);
					}

					logEntity.linkAfterPay = paymentRequest.dataObj.linkAfterPay;
					logEntity.renewToken = paymentRequest.dataObj.renewToken;
					logEntity.description = paymentRequest.dataObj.description;
					if (paymentRequest.dataObj.extraInfo != null) {
						logEntity.extraInfo = GsonUtils.toJsonString(paymentRequest.dataObj.extraInfo);
					}

					logEntity.userIP = paymentRequest.dataObj.userIP;

					logEntity.reqData = paymentRequest.dataObj.toJsonString();
				}

				logEntity.request = paymentRequest.toJsonString();
			} else {
				logEntity.reqTimeMilisec = System.currentTimeMillis();
			}

			if (invokeBankRequest != null) {
				logEntity.bankMID = invokeBankRequest.bankMID;
				logEntity.bankAPIName = invokeBankRequest.bankAPIName;
				logEntity.bankRequest = invokeBankRequest.rawRequest;
			}

			if (invokeBankResponse != null) {
				logEntity.bankResponse = invokeBankResponse.rawResponse;
			}

			if (paymentResponse != null) {
				logEntity.respTimeMilisec = paymentResponse.respTimeMilisec;
				if (paymentResponse.dataObj != null) {
					logEntity.respData = paymentResponse.dataObj.toJsonString();
					logEntity.returnCode = paymentResponse.dataObj.returnCode;
					logEntity.bankReturnCode = paymentResponse.dataObj.bankReturnCode;
					logEntity.bankReturnMessage = paymentResponse.dataObj.bankReturnMessage;
					logEntity.bankTransID = paymentResponse.dataObj.bankTransID;
					logEntity.bankTransStatus = paymentResponse.dataObj.bankTransStatus;
					logEntity.bankTransTime = paymentResponse.dataObj.bankTransTime;

					logEntity.nextAction = paymentResponse.dataObj.nextAction;
					logEntity.nextActionValue = paymentResponse.dataObj.nextActionValue;
				}
				logEntity.response = paymentResponse.toJsonString();
			}

			logEntity.step = actionResult.step;
			logEntity.stepResult = actionResult.stepResult;
			logEntity.exception = actionResult.exception;
			logEntity.ymd = CommonUtils.parseInt(CommonUtils.getYYYYMMDD(logEntity.reqTimeMilisec), -1);
		} catch (Exception ex) {
			BankConnectorLogger.ERROR_LOGGER.error(String.format("action=createLogEntity, transID=%s, exception=%s",
					logEntity.transID, ExceptionUtil.getExInfo(ex)));
		}

		try {
			addInvokeBankInfosToLogEntity(logEntity, paymentRequest, paymentResponse, invokeBankRequest,
					invokeBankResponse, actionResult);

			if (logEntity.respTimeMilisec <= 0) {
				logEntity.respTimeMilisec = System.currentTimeMillis();
			}
		} catch (Exception ex) {
			BankConnectorLogger.ERROR_LOGGER
					.error(String.format("action=addInvokeBankInfosToLogEntity, transID=%s, exception=%s",
							logEntity.transID, ExceptionUtil.getExInfo(ex)));
		}

		return logEntity;
	}

	/*
	 * response json String with signature
	 */
	private String makeResponseString(InvokePaymentBankConnectorResponse response, ActionResult actionResult) {

		String sigData = String.format("%s|%s|%s", response.transID, response.code, response.data);

		try {
			response.sig = SignatureUtils.createSignature(sigData, bankConn.connHashKey2);
			actionResult.returnCode = ReturnCodeEnum.SUCCESSFUL.getValue();
		} catch (NoSuchAlgorithmException e) {
			actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
			actionResult.returnMessage = "make response string ex: " + ExceptionUtils.getMessage(e);
			BankConnectorLogger.ERROR_LOGGER
					.error(String.format("action=makeResponseString, transID=%s, actionResult=%s", response.transID,
							actionResult.toJsonString()));
		}
		return response.toJsonString();
	}

	private void enqueueWriteLog(ZP2BankLogEntity logEntity) {

		try {
			AMQueueProducer producer = new AMQueueProducer(zp2bankLogQueueConfigEntity);
			producer.enAMQueueTextMessage(logEntity.toJsonString());

			producer = null;
		} catch (Exception ex) {
			BankConnectorLogger.ERROR_LOGGER.debug(String.format("action=enqueueWriteLog, logEntity=%s, exception=%s",
					logEntity.toJsonString(), ExceptionUtil.getExInfo(ex)));
			BankConnectorLogger.ERROR_DATA_AMQUEUE_LOGGER.info(
					String.format("[queue-%s] %s", zp2bankLogQueueConfigEntity.subject, logEntity.toJsonString()));
		}
	}
}
