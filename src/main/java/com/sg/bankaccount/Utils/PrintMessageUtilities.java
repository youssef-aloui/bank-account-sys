package com.sg.bankaccount.Utils;

public class PrintMessageUtilities {
	public static final String HEADER = "DATE || OPERATION ||AMOUNT | BALANCE";
	public static final String SEPARATOR = " || ";

	public static String lineSeparator() {
		return System.getProperty("line.separator");
	}
}