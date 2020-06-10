package com.zalopay.bankconnector.web.rest.vm.request;

import zalopay.bankconnector.entity.BaseMessage;
import zalopay.bankconnector.enums.EnvironmentEnum;

public class BankConnectorBaseRequest extends BaseMessage {

    private static final long serialVersionUID = 4249089686154413021L;
    public String clientID = "";
    public String data = "";
    public String sig = "";
    public int env = EnvironmentEnum.LIVE.getValue(); //STG OR LIVE

    public long reqTimeMilisec;

}