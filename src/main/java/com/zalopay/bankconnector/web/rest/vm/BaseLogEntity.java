package com.zalopay.bankconnector.web.rest.vm;

public class BaseLogEntity extends BaseEntity {

	public String step = "_";
	public String stepResult = "_";
	public String exception = "_";
	public String requestUrl = "_";
	public String clientIP = "_";
	public long reqTimeMilisec;
	public long respTimeMilisec;
}
