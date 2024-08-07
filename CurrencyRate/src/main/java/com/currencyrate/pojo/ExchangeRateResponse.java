package com.currencyrate.pojo;

import java.util.List;


public class ExchangeRateResponse {
    String date;
    String source = "USD";
    List<CurrencyRate> rates;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public List<CurrencyRate> getRates() {
		return rates;
	}
	public void setRates(List<CurrencyRate> rates) {
		this.rates = rates;
	}
    

}
