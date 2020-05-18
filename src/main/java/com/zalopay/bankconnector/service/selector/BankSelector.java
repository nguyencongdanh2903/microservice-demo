package com.zalopay.bankconnector.service.selector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.zalopay.bankconnector.config.SBISConfigEntity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import zalopay.bankconnector.bank.selector.BankMIDSelector;
import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.entity.connector.InvokePaymentBankConnectorRequest;

/**
 *
 * @author
 */
@Component
public class BankSelector implements BankMIDSelector {

	@Autowired
	private SBISConfigEntity sbisConfig;
	private static final BankSelector INSTANCE = new BankSelector();

	public static BankSelector getInstance() {

		return INSTANCE;
	}

	@Override
	public String select(InvokePaymentBankConnectorRequest paymentRequest, ActionResult actionResult) {

		if (!Strings.isNullOrEmpty(paymentRequest.dataObj.bankMID)) {
			return paymentRequest.dataObj.bankMID;
		}
		return sbisConfig.defaultBankMID;
	}

}
