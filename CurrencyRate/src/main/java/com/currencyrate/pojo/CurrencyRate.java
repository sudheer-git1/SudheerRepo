package com.currencyrate.pojo;

public class CurrencyRate {
    String target;
    String value;
	public CurrencyRate(String target, String value) {
	
		this.target = target;
		this.value = value;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
    
    
    
}
