package com.zalopay.bankconnector.config.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zalopay.bankconnector.service.converter.EnrollopMessageConverter;
import com.zalopay.bankconnector.service.converter.FundTransferMessageConverter;
import com.zalopay.bankconnector.service.converter.GenerateQRMessageConverter;
import com.zalopay.bankconnector.service.converter.InquiryCardMessageConverter;
import com.zalopay.bankconnector.service.converter.LinkMessageConverter;
import com.zalopay.bankconnector.service.converter.PayByCardMessageConverter;
import com.zalopay.bankconnector.service.converter.PayByQRMessageConverter;
import com.zalopay.bankconnector.service.converter.PayByTokenMessageConverter;
import com.zalopay.bankconnector.service.converter.PushPaymentMessageConverter;
import com.zalopay.bankconnector.service.converter.QueryStatusMessageConverter;
import com.zalopay.bankconnector.service.converter.RefundMessageConverter;
import com.zalopay.bankconnector.service.converter.RenewTokenMessageConverter;
import com.zalopay.bankconnector.service.converter.RevertMessageConverter;
import com.zalopay.bankconnector.service.converter.UnlinkMessageConverter;
import com.zalopay.bankconnector.service.converter.VerifyOTPMessageConverter;
import com.zalopay.bankconnector.service.converter.WithdrawByCardMessageConverter;
import com.zalopay.bankconnector.service.converter.WithdrawByTokenMessageConverter;
import com.zalopay.bankconnector.service.handler.InvokeBankApiRequestHandler;

@Configuration
public class HandlerContext {

	@Bean(name = "linkHandler")
	public InvokeBankApiRequestHandler getLinkHandler(LinkMessageConverter linkMessageConverter) {

		return new InvokeBankApiRequestHandler(linkMessageConverter);
	}

	@Bean(name = "enrollopHandler")
	public InvokeBankApiRequestHandler getEnrollopHandler(EnrollopMessageConverter enrollopMessageConverter) {

		return new InvokeBankApiRequestHandler(enrollopMessageConverter);
	}

	@Bean(name = "fundTransferHandler")
	public InvokeBankApiRequestHandler getFundTransferHandler(FundTransferMessageConverter fundTransferMessageConverter) {

		return new InvokeBankApiRequestHandler(fundTransferMessageConverter);
	}

	@Bean(name = "generateQRHandler")
	public InvokeBankApiRequestHandler getGenerateQRHandler(GenerateQRMessageConverter generateQRMessageConverter) {

		return new InvokeBankApiRequestHandler(generateQRMessageConverter);
	}

	@Bean(name = "inquiryCardHandler")
	public InvokeBankApiRequestHandler getInquiryCardHandler(InquiryCardMessageConverter inquiryCardMessageConverter) {

		return new InvokeBankApiRequestHandler(inquiryCardMessageConverter);
	}

	@Bean(name = "payByCardHandler")
	public InvokeBankApiRequestHandler getPayByCardHandler(PayByCardMessageConverter payByCardMessageConverter) {

		return new InvokeBankApiRequestHandler(payByCardMessageConverter);
	}

	@Bean(name = "payByQRHandler")
	public InvokeBankApiRequestHandler getPayByQRHandler(PayByQRMessageConverter payByQRMessageConverter) {

		return new InvokeBankApiRequestHandler(payByQRMessageConverter);
	}

	@Bean(name = "payByTokenHandler")
	public InvokeBankApiRequestHandler getPayByTokenHandler(PayByTokenMessageConverter payByTokenMessageConverter) {

		return new InvokeBankApiRequestHandler(payByTokenMessageConverter);
	}

	@Bean(name = "pushPaymentandler")
	public InvokeBankApiRequestHandler getPushPaymentHandler(PushPaymentMessageConverter pushPaymentMessageConverter) {

		return new InvokeBankApiRequestHandler(pushPaymentMessageConverter);
	}

	@Bean(name = "queryStatusHandler")
	public InvokeBankApiRequestHandler getQueryStatusHandler(QueryStatusMessageConverter queryStatusMessageConverter) {

		return new InvokeBankApiRequestHandler(queryStatusMessageConverter);
	}

	@Bean(name = "refundHandler")
	public InvokeBankApiRequestHandler getRefundHandler(RefundMessageConverter refundMessageConverter) {

		return new InvokeBankApiRequestHandler(refundMessageConverter);
	}

	@Bean(name = "renewTokenHandler")
	public InvokeBankApiRequestHandler getRenewTokenHandler(RenewTokenMessageConverter renewTokenMessageConverter) {

		return new InvokeBankApiRequestHandler(renewTokenMessageConverter);
	}

	@Bean(name = "revertHandler")
	public InvokeBankApiRequestHandler getRevertHandler(RevertMessageConverter revertMessageConverter) {

		return new InvokeBankApiRequestHandler(revertMessageConverter);
	}

	@Bean(name = "unlinkHandler")
	public InvokeBankApiRequestHandler getUnlinkHandler(UnlinkMessageConverter unlinkMessageConverter) {

		return new InvokeBankApiRequestHandler(unlinkMessageConverter);
	}

	@Bean(name = "verifyOTPHandler")
	public InvokeBankApiRequestHandler getVerifyOTPHandler(VerifyOTPMessageConverter verifyOTPMessageConverter) {

		return new InvokeBankApiRequestHandler(verifyOTPMessageConverter);
	}

	@Bean(name = "withdrawByCardHandler")
	public InvokeBankApiRequestHandler getWithdrawByCardHandler(WithdrawByCardMessageConverter withdrawByCardMessageConverter) {

		return new InvokeBankApiRequestHandler(withdrawByCardMessageConverter);
	}

	@Bean(name = "withdrawByTokenHandler")
	@Autowired
	public InvokeBankApiRequestHandler getWithdrawByTokenHandler(WithdrawByTokenMessageConverter withdrawByTokenMessageConverter) {

		return new InvokeBankApiRequestHandler(withdrawByTokenMessageConverter);
	}

}
