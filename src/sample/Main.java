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
        primaryStage.setMaximized(true);
        primaryStage.setTitle("5437 PAINT");
        String css = this.getClass().getResource("style.css").toExternalForm();
        root.getStylesheets().add(css);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        Controller controller = new Controller();
        controller.stageSetter(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
