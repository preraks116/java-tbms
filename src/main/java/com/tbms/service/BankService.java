package com.tbms.service;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.tbms.model.Bank;

import java.util.*;

public class BankService {
    private Map<String, Bank> banks;
    
    // i want define a graph with best exchange rate where each vertex is a bank and each edge is pair of the best exchange rate and the bank that has it

    public BankService() {
        this.banks = new HashMap<>();
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