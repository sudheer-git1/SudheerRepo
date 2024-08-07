package com.currencyrate.repo;



import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.currencyrate.model.ExchangeRates;

@Repository
public interface ExchangeRateRepo extends CrudRepository<ExchangeRates,Integer> {
	@Query("from ExchangeRates  where currency=:currency and date=:date")
    public ExchangeRates findDistinctFirstByDateAndCurrency(String date, String currency);
}
