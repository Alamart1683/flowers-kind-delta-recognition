package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Распознавание 8 видов цветков с помощью нейронной сети на основе дельта-правила");
        primaryStage.setScene(new Scene(root, 748, 422));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
