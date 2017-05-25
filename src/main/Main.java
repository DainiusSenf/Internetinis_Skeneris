package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.DataBaseView;


public class Main extends Application {

    public static Stage thestage;

    @Override
    public void start(Stage primaryStage) {
        thestage = primaryStage;
        Scene scene = DataBaseView.loadMainWindow(false);
        Main.thestage.setTitle("WebCrawler");
        Main.thestage.setScene(scene);
        thestage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


