package com.zalopay.bankconnector.service.converter;

import com.google.common.base.Strings;
import com.mysql.jdbc.StringUtils;
import com.zalopay.bankconnector.config.BankConnConfig;
import com.zalopay.bankconnector.config.SBISConfigEntity;
import com.zalopay.bankconnector.config.SBISDbConfig;
import com.zalopay.bankconnector.service.ClientUtils;
import com.zalopay.bankconnector.service.request.DefaultRequest;
import com.zalopay.bankconnector.service.request.FundTransferRequestData;
import com.zalopay.bankconnector.service.request.InquiryCardRequestData;
import com.zalopay.bankconnector.service.request.LinkRequestData;
import com.zalopay.bankconnector.service.request.PayByCardRequestData;
import com.zalopay.bankconnector.service.request.PayByTokenRequestData;
import com.zalopay.bankconnector.service.request.QueryStatusRequestData;
import com.zalopay.bankconnector.service.request.RefundRequestData;
import com.zalopay.bankconnector.service.request.RevertRequestData;
import com.zalopay.bankconnector.service.request.UnLinkRequestData;
import com.zalopay.bankconnector.service.request.VerifyOTPRequestData;
import com.zalopay.bankconnector.service.request.WithdrawByCardRequestData;
import com.zalopay.bankconnector.service.request.WithdrawByTokenRequestData;
import com.zalopay.bankconnector.service.request.ZP2BankBaseRequest;
import com.zalopay.bankconnector.service.response.DefaultResponse;
import com.zalopay.bankconnector.service.response.DefaultResponseData;
import com.zalopay.bankconnector.service.response.ZP2BankBaseResponse;
import com.zalopay.bankconnector.service.utils.CardUtil;
import com.zalopay.bankconnector.service.utils.HashUtil;
import com.zalopay.bankconnector.service.utils.MappingCodeUtil;
import com.zalopay.bankconnector.web.rest.vm.ActionResult;
import com.zalopay.bankconnector.web.rest.vm.request.InvokePaymentBankConnectorRequest;
import com.zalopay.bankconnector.web.rest.vm.response.InvokePaymentBankConnectorResponse;

import zalopay.bankconnector.constant.ApiParamsConst;
import zalopay.bankconnector.constant.CoreApiNameConst;
import zalopay.bankconnector.entity.ReturnCodeMappingEntity;
import zalopay.bankconnector.enums.BankTransStatusEnum;
import zalopay.bankconnector.enums.NextActionEnum;
import zalopay.bankconnector.enums.ReturnCodeEnum;
import zalopay.bankconnector.enums.SubTransTypeEnum;
import zalopay.bankconnector.util.ExceptionUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public abstract class ZP2BankCoreMessageConverter implements ZP2BankMessageConverter {

	@Autowired
	private SBISConfigEntity sbisConfig;

	@Autowired
	private BankConnConfig bankConn;

	@Autowired
	private SBISDbConfig sbisDbConfig;
	private static final Logger LOGGER = Logger.getLogger(ZP2BankCoreMessageConverter.class);

	abstract protected void convertAdditionalRequestInfos(InvokePaymentBankConnectorRequest source, DefaultRequest data,
			ActionResult actionResult);

	abstract protected void convertAdditionalResponseInfos(DefaultResponse data,
			InvokePaymentBankConnectorResponse target, ActionResult actionResult);

	@Override
	public ZP2BankBaseRequest convertRequest(InvokePaymentBankConnectorRequest source, ZP2BankBaseRequest target,
			ActionResult actionResult) {

		actionResult.returnCode = ReturnCodeEnum.SUCCESSFUL.getValue();
		actionResult.returnMessage = "convertRequest successful";
		DefaultRequest zp2bankRequest = new DefaultRequest();
		target = zp2bankRequest;

		try {
			SubTransTypeEnum subTransType = SubTransTypeEnum.fromInt(source.dataObj.subTransType);
			target.subTransType = source.dataObj.subTransType;

			switch (subTransType) {
			case LINK:
				zp2bankRequest.dataObj = new LinkRequestData();
				break;
			case UNLINK:
				zp2bankRequest.dataObj = new UnLinkRequestData();
				break;
			case PAY:
				zp2bankRequest.dataObj = new PayByCardRequestData();
				break;
			case PAY_BY_TOKEN:
				zp2bankRequest.dataObj = new PayByTokenRequestData();
				break;
			case WITHDRAW_BY_TOKEN:
				zp2bankRequest.dataObj = new WithdrawByTokenRequestData();
				break;
			case WITHDRAW:
				zp2bankRequest.dataObj = new WithdrawByCardRequestData();
				break;
			case VERIFY_OTP:
				zp2bankRequest.dataObj = new VerifyOTPRequestData();
				break;
			case REFUND:
			case REFUND_BY_WITHDRAW:
			case REFUND_BY_REVERT:
			case REFUND_BY_WITHDRAW_BY_TOKEN:
				zp2bankRequest.dataObj = new RefundRequestData();
				break;
			case REVERT_FOR_PAY:
			case REVERT_FOR_PAY_BY_TOKEN:
			case REVERT_FOR_WITHDRAW:
			case REVERT_FOR_WITHDRAW_BY_TOKEN:
				zp2bankRequest.dataObj = new RevertRequestData();
				break;
			case CHECK_BANK_TRANS:
				zp2bankRequest.dataObj = new QueryStatusRequestData();
				break;
			case DOMESTIC_INQUIRY:
				zp2bankRequest.dataObj = new InquiryCardRequestData();
				break;
			case DOMESTIC_TRANSFER_FUND_2_CARD:
				zp2bankRequest.dataObj = new FundTransferRequestData();
				break;
			default:
				throw new Exception("Can't find proper data object for request!");
			}

			zp2bankRequest.dataObj.merchantCode = sbisConfig.merchantCode;
			zp2bankRequest.clientCode = sbisConfig.caller;

			// retrieve business type from source data
			String businessType = String.valueOf(source.dataObj.extraInfo.get(ApiParamsConst.EXTRAINFO_BUSINESS_TYPE));
			if (!StringUtils.isNullOrEmpty(businessType) && (ApiParamsConst.PAYMENTCHANEL_B2B.equals(businessType)
					|| ApiParamsConst.PAYMENTCHANEL_B2C.equals(businessType))) {
				zp2bankRequest.dataObj.feeModel = businessType;
			}

			zp2bankRequest.clientUserID = source.dataObj.zaloPayID;
			zp2bankRequest.requestID = source.dataObj.traceNo;
			zp2bankRequest.time = source.dataObj.transTime + "";
			convertAdditionalRequestInfos(source, (DefaultRequest) target, actionResult);

			zp2bankRequest.data = zp2bankRequest.dataObj.toJsonString();
			zp2bankRequest.signature = createSignature((DefaultRequest) target, actionResult);

		} catch (Exception e) {
			actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
			actionResult.exception = ExceptionUtil.getExInfo(e);
			actionResult.returnMessage = "Convert request exception";
			LOGGER.error(String.format("Convert request ex: %s", ExceptionUtil.getExInfo(e)));
			return null;
		}
		return target;
	}

	@Override
	public InvokePaymentBankConnectorResponse convertResponse(ZP2BankBaseRequest sourceRequest,
			ZP2BankBaseResponse source, InvokePaymentBankConnectorResponse target, ActionResult actionResult) {

		DefaultResponse bankResponse = (DefaultResponse) source;

		try {
			if (sbisConfig.isVerifySignature) {
				verifySignature(bankResponse, actionResult);
			}
			if (actionResult.returnCode != ReturnCodeEnum.SUCCESSFUL.getValue()) {
				return target;
			}
			if (sbisConfig.isSimulateReturnCode) {
				bankResponse.code = sbisConfig.expectedReturnCode;
			}

			target.dataObj.bankReturnCode = bankResponse.errorCode;

			ReturnCodeMappingEntity returnCodeMapping = MappingCodeUtil.mapBankToGatewayCode(bankConn.connSystemName,
					sbisConfig.bankCode, 9999, bankResponse.errorCode, sbisDbConfig.bankToZPMapCodes);

			int returnCode = returnCodeMapping.returnCode;

			target.dataObj.returnCode = String.valueOf(returnCode);
			target.dataObj.bankReturnMessage = returnCodeMapping.description;
			target.dataObj.bankReturnCode = bankResponse.errorCode;
			if (returnCode == ReturnCodeEnum.SUCCESSFUL.getValue()) {
				target.dataObj.bankTransStatus = BankTransStatusEnum.SUCCESSFUL.getValue();
			} else if (returnCode == ReturnCodeEnum.PROCESSING.getValue()) {
				target.dataObj.bankTransStatus = BankTransStatusEnum.PROCESSING.getValue();
			} else {
				target.dataObj.bankTransStatus = BankTransStatusEnum.FAIL.getValue();
			}

			if (bankResponse.data == null) {
				target.data = target.dataObj.toJsonString();
			}

			try {
				bankResponse.dataObj = ClientUtils.parseJsonResponse(bankResponse.data, DefaultResponseData.class,
						actionResult);
			} catch (Exception parseResponseDataException) {
				actionResult.extraInfo = actionResult.returnMessage;
				actionResult.exception = ExceptionUtil.getExInfo(parseResponseDataException);
				actionResult.returnCode = ReturnCodeEnum.BANK_PARAMETER_INVALID.getValue();
				actionResult.stepResult = "parse response data  ex";
				LOGGER.error(String.format("[%s] parse response data ex: %s", sbisConfig.getPrefixLog(),
						ExceptionUtil.getExInfo(parseResponseDataException)));
				return target;
			}

			if (Arrays.asList(sbisConfig.returnCodeRequireOTPList).contains(bankResponse.errorCode)) {
				target.dataObj.nextAction = NextActionEnum.VERIFY_OTP.getValue();
				target.dataObj.nextActionValue = CoreApiNameConst.VERIFY_OTP;
				target.dataObj.bankTransStatus = BankTransStatusEnum.PROCESSING.getValue();
				target.dataObj.returnCode = ReturnCodeEnum.PROCESSING.getValue() + "";

				if (sbisConfig.useRefTraceNo) {
					target.dataObj.bankTransID = "";
				}
			}

			if (bankResponse.dataObj != null) {
				target.dataObj.bankToken = bankResponse.dataObj.tokenNo;
				if (ReturnCodeEnum.SUCCESSFUL.getValue() == returnCode
						&& !Strings.isNullOrEmpty(bankResponse.accountNo)) {
					target.dataObj.extraInfo.put(ApiParamsConst.EXTRAINFO_KYC_FIELDS,
							String.format("%s|%s", ApiParamsConst.USER_PHONE_NO, ApiParamsConst.USER_PHONE_NO));

					String maskedAccount = CardUtil.simpleMasking(bankResponse.accountNo, 6, 4);

					target.dataObj.extraInfo.put(ApiParamsConst.EXTRAINFO_ACC_ACCOUNT_NO, bankResponse.accountNo);
					target.dataObj.extraInfo.put(ApiParamsConst.EXTRAINFO_ACC_HASHED_ACCOUNT_NO,
							HashUtil.MD5(bankResponse.accountNo));
					target.dataObj.extraInfo.put(ApiParamsConst.EXTRAINFO_ACC_MASKING_ACCOUNT_NO, maskedAccount);
				}
			}
			target.dataObj.bankTransID = bankResponse.responseID;
			convertAdditionalResponseInfos(bankResponse, target, actionResult);
			target.data = target.dataObj.toJsonString();
		} catch (Exception e) {
			actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
			actionResult.exception = ExceptionUtil.getExInfo(e);
			actionResult.returnMessage = "Convert response exception";
			LOGGER.error(String.format("Convert response ex: %s", ExceptionUtil.getExInfo(e)));
			return target;
		}
		return target;
	}

	private String createSignature(DefaultRequest request, ActionResult actionResult) {

		try {

			StringBuilder rawData = new StringBuilder();
			rawData.append(request.requestID).append("|").append(request.clientCode).append("|")
					.append(request.clientUserID).append("|").append(request.time).append("|").append(request.data);

			request.signature = ClientUtils.generateSigData(rawData.toString(), sbisConfig.secretKey, actionResult,
					bankConn.connHashAlg);

			return request.signature;

		} catch (Exception e) {
			actionResult.exception = ExceptionUtil.getExInfo(e);
			actionResult.returnCode = ReturnCodeEnum.ENCRYPT_FAIL.getValue();
			actionResult.returnMessage = "Fail to createSignature";
			return "";
		}
	}

	private boolean verifySignature(DefaultResponse response, ActionResult actionResult) {

		try {

			StringBuilder rawData = new StringBuilder();
			rawData.append(response.time).append("|").append(response.caller).append("|").append(response.requestId)
					.append("|").append(response.operation).append("|").append(response.code).append("|")
					.append(response.data).append(sbisConfig.secretKey);

			boolean isSignature = ClientUtils.verify(rawData.toString(), response.signature, sbisConfig.publicKey,
					actionResult);
			if (actionResult.returnCode != ReturnCodeEnum.SUCCESSFUL.getValue()) {
				actionResult.returnMessage = "Fail to create signature from Bank' response";
				actionResult.extraInfo = String.format("[Bank response = %s ]", response.toJsonString());
				return false;
			}
			if (!isSignature) {
				actionResult.returnCode = ReturnCodeEnum.INVALID_RESP_SIG.getValue();
				actionResult.returnMessage = "Bank response's signature is not match with data";
				actionResult.extraInfo = String.format("[Bank sig = %s ]", response.signature);
				LOGGER.info(String.format("[Bank sig = %s ]", response.signature));
				return false;
			}

			actionResult.returnCode = ReturnCodeEnum.SUCCESSFUL.getValue();
			return true;

		} catch (Exception e) {
			actionResult.exception = ExceptionUtil.getExInfo(e);
			actionResult.returnCode = ReturnCodeEnum.EXCEPTION.getValue();
			actionResult.returnMessage = "Fail to validate signature";
			LOGGER.info(String.format("[Fail to validate signature = %s ]\n[Ex = %s ]", response.signature,
					ExceptionUtil.getExInfo(e)));
			return false;
		}
	}

}
