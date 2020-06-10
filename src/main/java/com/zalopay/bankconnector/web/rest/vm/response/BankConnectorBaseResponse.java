package com.zalopay.bankconnector.web.rest.vm.response;

import zalopay.bankconnector.entity.BaseMessage;
import zalopay.bankconnector.entity.config.DisplayMessageManagement;
import zalopay.bankconnector.enums.BankConnectorReturnStatusEnum;

public class BankConnectorBaseResponse extends BaseMessage {

    private static final long serialVersionUID = 7267854325973078057L;

    public String transID = "";
    public int code = BankConnectorReturnStatusEnum.FAIL.getValue();

    public String message = "";
    public String data = "";
    public String sig = "";

    public transient long respTimeMilisec;

    public void mapMessage() {
        message = DisplayMessageManagement.getDisplayMessage(code);
    }

}
