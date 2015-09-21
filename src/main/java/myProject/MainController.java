package myProject;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import myProject.model.HibernateUtil;

/**
 * Created by serega on 05.09.2015.
 */
public class MainController extends Application {
    @FXML
    private static Stage primaryStage;
    private final static String TITLE = "Disk space analyzer";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(TITLE);

        Parent root = FXMLLoader.load(MainController.class.getResource("/fxml/MainWindow.fxml"));

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, null));
        primaryStage.setTitle(TITLE);
        primaryStage.show();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown. Hibernate shutdown.");
            HibernateUtil.shutdown();
        }));
    }

    public static void setPrimaryStage(Stage primaryStage) {
        MainController.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
