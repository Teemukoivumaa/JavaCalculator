package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Calculator");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("calculatorIcon.png")));
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();

        root.addEventFilter(KeyEvent.ANY, keyEvent -> {
            String key = String.valueOf(keyEvent.getCode());
            switch (key) {
                case "ENTER":
                    SampleController controller = new SampleController();
                    controller.test(keyEvent);
                    keyEvent.consume();
                    break;
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
