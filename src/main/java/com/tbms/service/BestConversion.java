package com.tbms.service;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

import com.tbms.util.ExchangeEdge;

import com.tbms.model.Bank;

import java.util.*;

public class BestConversion {
    private Graph<String, ExchangeEdge> bestExchangeGraph;

    public BestConversion() {
        this.bestExchangeGraph = new DefaultDirectedWeightedGraph<>(ExchangeEdge.class);;
    }

    public void addCurrency(String currency) {
        if (!bestExchangeGraph.containsVertex(currency)) {
            bestExchangeGraph.addVertex(currency);
        }
    }

    public void deleteCurrency(String currency) {
        if (bestExchangeGraph.containsVertex(currency)) {
            bestExchangeGraph.removeVertex(currency);
        }
    }

    public void addRate(String fromCurrency, String toCurrency, double rate, String bank) {
        if (bestExchangeGraph.containsVertex(fromCurrency) && bestExchangeGraph.containsVertex(toCurrency)) {
            ExchangeEdge edge = bestExchangeGraph.getEdge(fromCurrency, toCurrency);
            if (edge == null) {
                edge = new ExchangeEdge(rate, bank);
                bestExchangeGraph.addEdge(fromCurrency, toCurrency, edge);
            } else {
                edge.setRate(rate);
                edge.setBank(bank);
            }
        }
    }

    public void deleteRate(double currentRate, String fromCurrency, String toCurrency, Map<String, Bank> banks) {
        // if it doesnt exist, throw exception
        if (!bestExchangeGraph.containsVertex(fromCurrency)) {
            throw new IllegalArgumentException("Currency does not exist: " + fromCurrency);
        }
        if (!bestExchangeGraph.containsVertex(toCurrency)) {
            throw new IllegalArgumentException("Currency does not exist: " + toCurrency);
        }
        ExchangeEdge edge = bestExchangeGraph.getEdge(fromCurrency, toCurrency);
        if (edge == null) {
            throw new IllegalArgumentException("Exchange rate does not exist: " + fromCurrency + " to " + toCurrency);
        }

        if (edge.getRate() == currentRate) {
            bestExchangeGraph.removeEdge(fromCurrency, toCurrency);

            // find next best rate
            double bestRate = Double.MAX_VALUE;
            String bestBank = "";

            for (Map.Entry<String, Bank> entry : banks.entrySet()) {
                    // get the rate for the currency pair
                    Bank bank2 = entry.getValue();
                    double newRate2 = bank2.getExchangeService().getExchangeRate(fromCurrency, toCurrency);
                    // if the rate is better than the current best rate
                    if (newRate2 < bestRate) {
                        bestRate = newRate2;
                        bestBank = bank2.getName();
                    }
            }

            // update the best rate
            if (bestRate != Double.MAX_VALUE) {
                addRate(fromCurrency, toCurrency, bestRate, bestBank);
            }
        }
    }

    public void getBestConversion(String fromCurrency, String toCurrency) {
            if (!bestExchangeGraph.containsVertex(fromCurrency)) {
                throw new IllegalArgumentException("Currency not found: " + fromCurrency);
            }
            if (!bestExchangeGraph.containsVertex(toCurrency)) {
                throw new IllegalArgumentException("Currency not found: " + toCurrency);
            }
            if (fromCurrency.equals(toCurrency)) {
                throw new IllegalArgumentException("Source and target currencies must be different");
            }

            // find the best path
            DijkstraShortestPath<String, ExchangeEdge> dijkstra = new DijkstraShortestPath<>(bestExchangeGraph);
            GraphPath<String, ExchangeEdge>  path = dijkstra.getPath(fromCurrency, toCurrency);
            List<ExchangeEdge> edgeList = path.getEdgeList();
            List<String> vertexList = path.getVertexList();

            // print the path
            System.out.println("Best exchange rate from " + fromCurrency + " to " + toCurrency + ":");
            // get vertex list and start vertex from graph path
            for (int i = 0; i < vertexList.size() - 1; i++) {
                ExchangeEdge edge = edgeList.get(i);
                System.out.println(edge.getBank() + ": " + vertexList.get(i) + " to " + vertexList.get(i + 1) + " at " + edge.getRate());
            }
    }

}
