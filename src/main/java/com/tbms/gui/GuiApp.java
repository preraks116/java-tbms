package com.tbms.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GuiApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Currency Exchange System");

        BorderPane mainLayout = createMainLayout();

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createMainLayout() {
        BorderPane borderPane = new BorderPane();
        // Add components to the layout
        return borderPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
