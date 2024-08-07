package com.currencyrate.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exchange_rates", uniqueConstraints = { @UniqueConstraint(columnNames = { "date", "currency" }) })
public class ExchangeRates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String currency;

    String value;

    String date;

	


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
    
    
    
    
}

