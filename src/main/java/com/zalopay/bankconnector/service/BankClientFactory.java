package com.zalopay.bankconnector.service;

import zalopay.bankconnector.bank.client.BankClient;
import zalopay.bankconnector.entity.ActionResult;
import zalopay.bankconnector.enums.ReturnCodeEnum;

public class BankClientFactory {

    private static BankClientFactory INSTANCE = new BankClientFactory();

    public static BankClientFactory getInstance() {
        return INSTANCE;
    }

    public BankClient getBankClient(ActionResult actionResult) {

        actionResult.returnCode = ReturnCodeEnum.SUCCESSFUL.getValue();
        return new SBISClient();
    }

}
