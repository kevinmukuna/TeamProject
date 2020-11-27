package rayTrace;

import rayTrace.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("render.fxml"));
        Parent root = loader.load(getClass().getResource("render.fxml").openStream());
//        Parent root = loader.load();
        Controller controller = (Controller) loader.getController();

        primaryStage.setTitle("Simple Ray Tracing");
        primaryStage.setScene(new Scene(root, 860, 640));
        //

        //controller.setStage(primaryStage);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
