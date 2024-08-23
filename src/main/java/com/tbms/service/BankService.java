package com.tbms.service;


import com.tbms.model.Bank;

import java.util.*;

public class BankService {
    private Map<String, Bank> banks;

    public BankService() {
        this.banks = new HashMap<>();
    }

    public Map<String, Bank> getBanks() {
        return banks;
    }

    public Set<String> getAllBankNames() {
        return banks.keySet();
    }

    public void addBank(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Bank name cannot be null or empty");
        }
        if (banks.containsKey(name)) {
            throw new IllegalArgumentException("Bank with this name already exists");
        }
        banks.put(name, new Bank(name));
    }

    public void deleteBank(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Bank name cannot be null or empty");
        }
        if (!banks.containsKey(name)) {
            throw new IllegalArgumentException("Bank not found: " + name);
        }
        banks.remove(name);
    }

    public Bank getBank(String name) {
        Bank bank = banks.get(name);
        if (bank == null) {
            throw new IllegalArgumentException("Bank not found: " + name);
        }
        return bank;
    }
}