package com.tbms.util;

public class ExchangeEdge {
    private double rate;
    private String bank;

    public ExchangeEdge(double rate, String bank) {
        this.rate = rate;
        this.bank = bank;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBank() {
        return bank;
    }

    @Override
    public String toString() {
        return "ExchangeEdge{rate=" + rate + ", bank='" + bank + "'}";
    }
}