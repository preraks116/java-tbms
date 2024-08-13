package com.tbms.util;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class CurrencyTablePrinter {
    private Graph<Currency, DefaultWeightedEdge> currencyGraph;

    public String getCurrencyTable(Graph<Currency, DefaultWeightedEdge> currencyGraph) {
        this.currencyGraph = currencyGraph;
        List<Currency> currencies = new ArrayList<>(currencyGraph.vertexSet());
        currencies.sort(Comparator.comparing(Currency::getCurrencyCode));

        StringBuilder table = new StringBuilder();
        appendTableHeader(table, currencies);
        appendTableRows(table, currencies);
        return table.toString();
    }

    private static void appendTableHeader(StringBuilder table, List<Currency> currencies) {
        table.append(String.format("%-10s", ""));
        for (Currency currency : currencies) {
            table.append(String.format("%-10s", currency.getCurrencyCode()));
        }
        table.append("\n");
    }

    private void appendTableRows(StringBuilder table, List<Currency> currencies) {
        for (Currency fromCurrency : currencies) {
            table.append(String.format("%-10s", fromCurrency.getCurrencyCode()));
            for (Currency toCurrency : currencies) {
                if (fromCurrency.equals(toCurrency)) {
                    table.append(String.format("%-10s", "1.0"));
                } else {
                    appendExchangeRate(table, fromCurrency, toCurrency);
                }
            }
            table.append("\n");
        }
    }

    private void appendExchangeRate(StringBuilder table, Currency from, Currency to) {
        try {
            double rate = getExchangeRate(from.getCurrencyCode(), to.getCurrencyCode());
            table.append(String.format("%-10s", CurrencyUtils.formatExchangeRate(rate)));
        } catch (IllegalStateException e) {
            table.append(String.format("%-10s", "N/A"));
        }
    }

    private Currency getCurrencyFromGraph(String currencyCode) {
        Currency currency = CurrencyUtils.validateCurrencyCode(currencyCode);
        if (!currencyGraph.containsVertex(currency)) {
            throw new IllegalArgumentException("Currency does not exist in the graph: " + currencyCode);
        }
        return currency;
    }

    private double getExchangeRate(String fromCode, String toCode) {
        Currency fromCurrency = getCurrencyFromGraph(fromCode);
        Currency toCurrency = getCurrencyFromGraph(toCode);

        DefaultWeightedEdge edge = currencyGraph.getEdge(fromCurrency, toCurrency);
        if (edge == null) {
            throw new IllegalStateException("No exchange rate found between " +
                CurrencyUtils.formatCurrencyPair(fromCode, toCode));
        }
        return 1 / currencyGraph.getEdgeWeight(edge);
    }
}
