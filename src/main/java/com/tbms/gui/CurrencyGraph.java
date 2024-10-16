package com.tbms.gui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class CurrencyGraph {

    private Pane graphPane;
    private Map<String, Circle> currencyNodes;

    public CurrencyGraph() {
        this.graphPane = new Pane();
        this.currencyNodes = new HashMap<>();
    }

    public Pane generateGraph(Map<String, Map<String, Double>> exchangeRates) {
        graphPane.getChildren().clear();
        currencyNodes.clear();

        // Create nodes for each currency
        int index = 0;
        for (String currency : exchangeRates.keySet()) {
            Circle node = new Circle(20, Color.BLUE);
            node.setCenterX(100 + index * 100);
            node.setCenterY(100);
            currencyNodes.put(currency, node);
            graphPane.getChildren().add(node);
            index++;
        }

        // Create edges for each exchange rate
        for (Map.Entry<String, Map<String, Double>> entry : exchangeRates.entrySet()) {
            String fromCurrency = entry.getKey();
            for (Map.Entry<String, Double> rateEntry : entry.getValue().entrySet()) {
                String toCurrency = rateEntry.getKey();
                Line edge = new Line();
                edge.setStartX(currencyNodes.get(fromCurrency).getCenterX());
                edge.setStartY(currencyNodes.get(fromCurrency).getCenterY());
                edge.setEndX(currencyNodes.get(toCurrency).getCenterX());
                edge.setEndY(currencyNodes.get(toCurrency).getCenterY());
                graphPane.getChildren().add(edge);
            }
        }

        return graphPane;
    }
}
