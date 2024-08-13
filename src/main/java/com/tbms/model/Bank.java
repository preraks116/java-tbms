package com.tbms.model;
import com.tbms.service.ExchangeService;

public class Bank {
    private String name;
    private ExchangeService exchangeService;

    public Bank(String name) {
        this.name = name;
        this.exchangeService = new ExchangeService();
    }

    public String getName() {
        return name;
    }

    public ExchangeService getExchangeService() {
        return exchangeService;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Bank bank = (Bank) obj;
        return name.equals(bank.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
