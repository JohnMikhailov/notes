package controllers;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Notes");
        MainController mainController = new MainController("jdbc:sqlite:sqlite/notes.db");
        primaryStage.setScene(mainController.createScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
