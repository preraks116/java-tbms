package com.tbms.service;

import com.tbms.util.CurrencyTablePrinter;

import com.tbms.util.CurrencyUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class ExchangeService {
    private final Graph<Currency, DefaultWeightedEdge> currencyGraph;
    private final CurrencyTablePrinter tablePrinter = new CurrencyTablePrinter();

    public ExchangeService() {
        this.currencyGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    public void addCurrency(String currencyCode) {
        Currency newCurrency = CurrencyUtils.validateCurrencyCode(currencyCode);
        if (currencyGraph.containsVertex(newCurrency)) {
            throw new IllegalArgumentException("Currency already exists: " + currencyCode);
        }
        currencyGraph.addVertex(newCurrency);
    }

    public void deleteCurrency(String currencyCode) {
        Currency currencyToDelete = CurrencyUtils.validateCurrencyCode(currencyCode);
        if (!currencyGraph.containsVertex(currencyToDelete)) {
            throw new IllegalArgumentException("Currency does not exist: " + currencyCode);
        }
        currencyGraph.removeVertex(currencyToDelete);
    }

    public void addExchangeRate(String fromCode, String toCode, double rate) {
        CurrencyUtils.validateExchangeRateInput(fromCode, toCode, rate);
        Currency fromCurrency = getCurrencyFromGraph(fromCode);
        Currency toCurrency = getCurrencyFromGraph(toCode);

        updateExchangeRate(fromCurrency, toCurrency, rate);
        updateExchangeRate(toCurrency, fromCurrency, 1 / rate);
    }

    public void deleteExchangeRate(String fromCode, String toCode) {
        Currency fromCurrency = CurrencyUtils.validateCurrencyCode(fromCode);
        Currency toCurrency = CurrencyUtils.validateCurrencyCode(toCode);
        if (fromCode.equals(toCode)) {
            throw new IllegalArgumentException("Source and target currencies must be different");
        }

        removeExchangeRateEdge(fromCurrency, toCurrency);
        removeExchangeRateEdge(toCurrency, fromCurrency);
    }

    public double getExchangeRate(String fromCode, String toCode) {
        Currency fromCurrency = getCurrencyFromGraph(fromCode);
        Currency toCurrency = getCurrencyFromGraph(toCode);

        DefaultWeightedEdge edge = currencyGraph.getEdge(fromCurrency, toCurrency);
        if (edge == null) {
            return Double.MAX_VALUE;
        }
        return 1 / currencyGraph.getEdgeWeight(edge);
    }

    public String getCurrencyTable() {
        // if there are no currencies in the graph, return an empty table
        if (currencyGraph.vertexSet().isEmpty()) {
            return String.format("No currencies found");
        }
        return tablePrinter.getCurrencyTable(currencyGraph);
    }

    private Currency getCurrencyFromGraph(String currencyCode) {
        Currency currency = CurrencyUtils.validateCurrencyCode(currencyCode);
        if (!currencyGraph.containsVertex(currency)) {
            throw new IllegalArgumentException("Currency does not exist in the graph: " + currencyCode);
        }
        return currency;
    }

    private void updateExchangeRate(Currency from, Currency to, double rate) {
        DefaultWeightedEdge edge = currencyGraph.getEdge(from, to);
        if (edge == null) {
            edge = currencyGraph.addEdge(from, to);
        }
        currencyGraph.setEdgeWeight(edge, 1 / rate); // Store the inverse rate for dijkstra
    }

    private void removeExchangeRateEdge(Currency from, Currency to) {
        DefaultWeightedEdge edge = currencyGraph.getEdge(from, to);
        if (edge == null) {
            throw new IllegalArgumentException("Exchange rate does not exist between " +
                CurrencyUtils.formatCurrencyPair(from.getCurrencyCode(), to.getCurrencyCode()));
        }
        currencyGraph.removeEdge(edge);
    }
}