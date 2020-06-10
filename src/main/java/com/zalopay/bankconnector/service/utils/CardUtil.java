package com.zalopay.bankconnector.service.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class CardUtil {

	private static Pattern cardNoPattern = Pattern.compile("[^0-9](([\\d]{6})([\\d]{6,9})([\\d]{4}))[^0-9]");
	private static Pattern cvvPattern = Pattern.compile("\"(cvv|cvNumber)\":\"([\\d]{3,4})\"");
	private static Pattern cvvIntextPattern = Pattern.compile("\\\\\"(cvv|cvNumber)\\\\\":\\\\\"([\\d]{3,4})\\\\\"");

	public static String masking(String msg) {

		Matcher m = null;

		m = cardNoPattern.matcher(msg);
		while (m.find()) {
			if (checkLuhn(m.group(1))) {
				msg = msg.replace(m.group(1), m.group(2) + m.group(3).replaceAll("\\d", "*") + m.group(4));
			}
		}

		m = cvvPattern.matcher(msg);
		while (m.find()) {
			msg = msg.replace(m.group(0), "\"cvv\":\"***\"");
		}

		m = cvvIntextPattern.matcher(msg);
		while (m.find()) {
			msg = msg.replace(m.group(0), "\\\"cvv\\\":\\\"***\\\"");
		}

		return msg;
	}

	public static boolean checkLuhn(String ccNumber) {

		int sum = 0;
		boolean alternate = false;
		for (int i = ccNumber.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(ccNumber.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		return (sum % 10 == 0);
	}

	public static void main(String[] args) {

		String content;

		try {
			content = new String(Files.readAllBytes(Paths.get("/Users/sondv/Desktop/test.txt")));
			System.out.println(masking(content));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main2(String[] args) {

		System.out.println(simpleMasking("120000", 0, 0));

	}

	public static String simpleMasking(String msg, int sizeF, int sizeL) {

		int length = msg.length();
		if (msg != null && length > (sizeF + sizeL)) {
			return msg.substring(0, sizeF) + createStarString(length - (sizeF + sizeL))
					+ msg.substring(length - sizeL, length);
		}

		return msg;
	}

	public static String createStarString(int length) {

		String rs = "";
		for (int i = length - 1; i >= 0; i--) {
			rs += "*";
		}

		return rs;
	}
}