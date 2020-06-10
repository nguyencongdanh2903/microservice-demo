package com.zalopay.bankconnector.web.rest.vm;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class BaseEntity{

	static Gson gson;

	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		gson = gsonBuilder.disableHtmlEscaping().create();
	}

	public String toJsonString() {

		return gson.toJson(this);
	}

	@Override
	public String toString() {

		return toJsonString();
	}

	public String removeTabAndNewLine(String str) {

		if (str != null) {
			return str.replaceAll("\r", "").replaceAll("\n", " ").replaceAll("\t", " ").trim();
		} else {
			return "";
		}
	}

	public String formatDate(SimpleDateFormat simpleDateFormat, long time) {

		if (time > 0) {
			return simpleDateFormat.format(new Date(time));
		}
		return "";
	}
}
