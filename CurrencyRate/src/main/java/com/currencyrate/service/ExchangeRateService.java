package com.currencyrate.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.currencyrate.model.ExchangeRates;
import com.currencyrate.pojo.CurrencyRate;
import com.currencyrate.pojo.ExchangeRateResponse;
import com.currencyrate.pojo.FrankfurterResponse;
import com.currencyrate.pojo.TimeSeriesResponse;
import com.currencyrate.repo.ExchangeRateRepo;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class ExchangeRateService {
	@Autowired
    ExchangeRateRepo exchangeRateRepo;

    static final String ACCEPTABLE_TARGETS_CURRENCY ="EUR,GBP,JPY,CZK";

    public ExchangeRateResponse getFx(String target) throws Exception {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now()));
        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setDate(currentDate);
        List<ExchangeRates> data = new ArrayList<>();
        if (target == null || target.isBlank()) {
            target="EUR,GBP,JPY,CZK";
        }
        for (String t: target.split(",")) {
            List<ExchangeRates> d = new ArrayList<>();
            ExchangeRates res = exchangeRateRepo.findDistinctFirstByDateAndCurrency(currentDate, t);
          
            if (res == null) {
                d = getFxFromApi(t, currentDate);
            } else {
                d.add(res);
            }
            data.addAll(d);
        }
        
        if (data.isEmpty()) {
            data = getFxFromApi(target, currentDate);
        }
        List<CurrencyRate> rates = new ArrayList<>();
        data.forEach(dat -> rates.add(new CurrencyRate(dat.getCurrency(), dat.getValue())));
        response.setRates(rates);
        return response;
    }

    private List<ExchangeRates> getFxFromApi(String target, String date) throws Exception {
    	FrankfurterResponse data = getFrankfurterResponse(target, date);
        List<ExchangeRates> response = new ArrayList<>();
        data.getRates().forEach((k, v) -> {
            ExchangeRates rate = new ExchangeRates();
            rate.setDate(date);
            rate.setCurrency(k);
            rate.setValue(v.toString());
            response.add(rate);
            saveResponseData(date, k, v.toString());
        });
        return response;
    }

    private void saveResponseData(String date, String currency, String value) {
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setDate(date);
        exchangeRates.setCurrency(currency);
        exchangeRates.setValue(value);
        exchangeRateRepo.save(exchangeRates);
    }

    private static FrankfurterResponse getFrankfurterResponse(String target, String date) throws URISyntaxException {
     
    	FrankfurterResponse data = new FrankfurterResponse();
        String uri = String.format("https://api.frankfurter.app/%s?from=USD&to=%s", date == null ? "latest" : date, target);
        HttpRequest request = HttpRequest.newBuilder().uri(new URI(uri)).GET().build();
        try{
        	HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            data = new ObjectMapper().readValue(response.body(), FrankfurterResponse.class);
         

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public TimeSeriesResponse getTimeSeriesResponseForCurrency(String targetCurrency) throws Exception {
        Map<String, CurrencyRate> timeSeries = new HashMap<>();
        for(int i = 0 ; i <= 2; i++){
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now().minus(i, ChronoUnit.DAYS)));
            List<ExchangeRates> data = new ArrayList<>();
            ExchangeRates res = exchangeRateRepo.findDistinctFirstByDateAndCurrency(currentDate, targetCurrency);
            if(res == null){
                data = getFxFromApi(targetCurrency, currentDate);
            } else  {
                data.add(res);
            }

            data.forEach(dat -> {
                CurrencyRate currencyRate = new CurrencyRate(dat.getCurrency(), dat.getValue());
                timeSeries.put(currentDate, currencyRate);
            });

        }

        TimeSeriesResponse timeSeriesResponse = new TimeSeriesResponse();
        timeSeriesResponse.setRates(timeSeries);
        return timeSeriesResponse;
    }


}
