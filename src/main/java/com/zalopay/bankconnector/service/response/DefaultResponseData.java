package com.zalopay.bankconnector.service.response;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

import zalopay.bankconnector.bank.entity.ZP2BankBaseResponse;

public class DefaultResponseData extends ZP2BankBaseResponse {

    /**
     * 
     */
    public static final long serialVersionUID = -2915375796065867741L;
    
    @SerializedName("transId")
    public String bankTransId = "";
    public String tokenNo = "";
    public int refCode;
    public int type;
    
    public String description = "";
    public Map<String, Object> extraInfo = new HashMap<>();

    @Override
    public String toMaskedString() {
        return null;
    }
}
