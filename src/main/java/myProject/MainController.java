package myProject;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by serega.
 */
public class MainController extends Application {
    @FXML
    private static Stage primaryStage;
    private final static String TITLE = "Disk space analyzer";
    private final String iconPath = "icon.png";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStageT) throws Exception {
        primaryStage = primaryStageT;
        primaryStage.setTitle(TITLE);

        Parent root = FXMLLoader.load(MainController.class.getResource("/fxml/MainWindow.fxml"));
        primaryStage.getIcons().add(new Image("file:" + iconPath));
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, null));
        primaryStage.setTitle(TITLE);
        primaryStage.show();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown");
        }));
    }

}
