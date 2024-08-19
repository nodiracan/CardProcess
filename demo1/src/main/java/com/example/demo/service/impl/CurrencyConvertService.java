package com.example.demo.service.impl;

import com.example.demo.enums.Currency;
import org.springframework.stereotype.Service;


@Service
public class CurrencyConvertService {


    public Long convertCurrency(Long amount, Long exchangeRate) {
        return amount * exchangeRate;
    }
}
