package com.zalopay.bankconnector.service.request;

import java.util.HashMap;
import java.util.Map;

public class PayByQRRequestData extends DefaultRequestData {

    /**
     * 
     */
    private static final long serialVersionUID = 1911586797307393693L;

    public Map<String, Object> qrData = new HashMap<>();
    public String amount = "";
    public String fee = "";
    public String currency = "";
    public String consumerPAN = "";
    public String consumerName = "";
}
