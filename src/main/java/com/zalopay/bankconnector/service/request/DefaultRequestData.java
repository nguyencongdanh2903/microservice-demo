package com.zalopay.bankconnector.service.request;

import java.util.HashMap;
import java.util.Map;

import zalopay.bankconnector.entity.BaseEntity;
import zalopay.bankconnector.enums.BusinessTypeEnum;

public class DefaultRequestData extends BaseEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 7347315987203564312L;

    public String merchantCode = "";
    public String feeModel = BusinessTypeEnum.B2C.getValue();  // default is B2C, for modification, dive deeper into Convert per transType
    public String description = "";
    public Map<String, Object> extraInfo = new HashMap<>();

}
