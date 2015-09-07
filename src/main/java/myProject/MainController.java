package myProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by serega on 05.09.2015.
 */
public class MainController extends Application {

//    private Stage primaryStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        this.primaryStage = primaryStage;
//        this.primaryStage.setTitle("Disk space analyzer");

//        System.out.println("Start Fx");
        Parent root = FXMLLoader.load(MainController.class.getResource("/fxml/MainWindow.fxml"));
//        System.out.println("Finish Fx");
primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, null));
        primaryStage.setTitle("Disk space analyzer");
        primaryStage.show();
    }
}
