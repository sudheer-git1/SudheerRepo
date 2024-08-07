package com.currencyrate.pojo;

import java.util.Map;


public class TimeSeriesResponse {
    String source = "USD";
    Map<String, CurrencyRate> rates;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Map<String, CurrencyRate> getRates() {
		return rates;
	}
	public void setRates(Map<String, CurrencyRate> rates) {
		this.rates = rates;
	}
    
}
