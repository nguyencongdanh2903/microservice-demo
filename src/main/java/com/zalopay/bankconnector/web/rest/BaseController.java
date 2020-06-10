package com.zalopay.bankconnector.web.rest;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zalopay.bankconnector.service.handler.InvokeBankApiRequestHandler;
import com.zalopay.bankconnector.web.rest.vm.request.InvokePaymentBankConnectorRequest;
import com.zalopay.bankconnector.web.rest.vm.response.InvokePaymentBankConnectorResponse;

@Controller
@RequestMapping("api/v1")
public class BaseController {

	@Resource(name = "linkHandler")
	private InvokeBankApiRequestHandler linkHandler;

	@Resource(name = "enrollopHandler")
	private InvokeBankApiRequestHandler enrollopHandler;

	@Resource(name = "fundTransferHandler")
	private InvokeBankApiRequestHandler fundTransferHandler;

	@Resource(name = "generateQRHandler")
	private InvokeBankApiRequestHandler generateQRHandler;

	@Resource(name = "inquiryCardHandler")
	private InvokeBankApiRequestHandler inquiryCardHandler;

	@Resource(name = "payByTokenHandler")
	private InvokeBankApiRequestHandler payByTokenHandler;

	@Resource(name = "payByCardHandler")
	private InvokeBankApiRequestHandler payByCardHandler;

	@Resource(name = "payByQRHandler")
	private InvokeBankApiRequestHandler payByQRHandler;

	@Resource(name = "pushPaymentHandler")
	private InvokeBankApiRequestHandler pushPaymentHandler;

	@Resource(name = "queryStatusHandler")
	private InvokeBankApiRequestHandler queryStatusHandler;

	@Resource(name = "refundHandler")
	private InvokeBankApiRequestHandler refundHandler;

	@Resource(name = "renewTokenHandler")
	private InvokeBankApiRequestHandler renewTokenHandler;

	@Resource(name = "revertHandler")
	private InvokeBankApiRequestHandler revertHandler;

	@Resource(name = "unlinkHandler")
	private InvokeBankApiRequestHandler unlinkHandler;

	@Resource(name = "verifyOTPHandler")
	private InvokeBankApiRequestHandler verifyOTPHandler;

	@Resource(name = "withdrawByCardHandler")
	private InvokeBankApiRequestHandler withdrawByCardHandler;

	@Resource(name = "withdrawByTokenHandler")
	private InvokeBankApiRequestHandler withdrawByTokenHandler;

	@PostMapping("/link")
	public ResponseEntity<InvokePaymentBankConnectorResponse> link(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = linkHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/enrollop")
	public ResponseEntity<InvokePaymentBankConnectorResponse> enrollop(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = enrollopHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/fundTransfer")
	public ResponseEntity<InvokePaymentBankConnectorResponse> fundTransfer(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = fundTransferHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/generateQR")
	public ResponseEntity<InvokePaymentBankConnectorResponse> generateQR(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = generateQRHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/inquiryCard")
	public ResponseEntity<InvokePaymentBankConnectorResponse> inquiryCard(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = inquiryCardHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/payByToken")
	public ResponseEntity<InvokePaymentBankConnectorResponse> payByToken(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = payByTokenHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/payByCard")
	public ResponseEntity<InvokePaymentBankConnectorResponse> payByCard(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = payByCardHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/payByQR")
	public ResponseEntity<InvokePaymentBankConnectorResponse> payByQR(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = payByQRHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/queryStatus")
	public ResponseEntity<InvokePaymentBankConnectorResponse> queryStatus(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = queryStatusHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/refund")
	public ResponseEntity<InvokePaymentBankConnectorResponse> refund(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = refundHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/pushPayment")
	public ResponseEntity<InvokePaymentBankConnectorResponse> pushPayment(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = pushPaymentHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/renewToken")
	public ResponseEntity<InvokePaymentBankConnectorResponse> renewToken(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = renewTokenHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/revert")
	public ResponseEntity<InvokePaymentBankConnectorResponse> revert(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = revertHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/unlink")
	public ResponseEntity<InvokePaymentBankConnectorResponse> unlink(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = unlinkHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/verifyOTP")
	public ResponseEntity<InvokePaymentBankConnectorResponse> verifyOTP(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = verifyOTPHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/withdrawByCard")
	public ResponseEntity<InvokePaymentBankConnectorResponse> withdrawByCard(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = withdrawByCardHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/withdrawByToken")
	public ResponseEntity<InvokePaymentBankConnectorResponse> withdrawByToken(
			@Valid @RequestBody InvokePaymentBankConnectorRequest request) {

		InvokePaymentBankConnectorResponse response = withdrawByTokenHandler.handle(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
