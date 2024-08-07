package com.currencyrate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.currencyrate.pojo.ExchangeRateResponse;
import com.currencyrate.pojo.TimeSeriesResponse;
import com.currencyrate.service.ExchangeRateService;


@RestController
@RequestMapping("fx")
public class FxController {
	@Autowired
	private ExchangeRateService exchangeRateService;

	@GetMapping
	public ExchangeRateResponse getFx(@RequestParam(required = false) String target) throws Exception {
		return exchangeRateService.getFx(target);
	}

	@GetMapping(path = "/{targetCurrency}")
	public TimeSeriesResponse getFxTimeSeries(@PathVariable(name = "targetCurrency") String targetCurrency) {
		try {
			return exchangeRateService.getTimeSeriesResponseForCurrency(targetCurrency);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
