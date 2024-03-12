package com.zbank.enums;

public enum TransactionDetail {
	ALL(""),
	SUCCESS("SUCCESS"),
	FAILED("FAILED"),
	DEBIT("DEBIT"),
	CREDIT("CREDIT"),;

	private final String value;
	
	private TransactionDetail(String value) {
		this.value =value;
	}
	public String get() {
		return value;
	}
}
