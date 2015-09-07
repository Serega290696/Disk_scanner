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

    private Stage primaryStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BBB");

        System.out.println("A rr");
        System.out.println("??? Oo");
        System.out.println("??? Oo");
        Parent root = FXMLLoader.load(MainController.class.getResource("/fxml2/MainFx.fxml"));
        System.out.println("A rr");
        System.out.println("A rr");

//        XYChart.Series<String, Number> series1 = new XYChart.Series();
//        series1.setName("XYChart.Series 1");

//        series1.getData().add(new XYChart.Data("January", 100));
//        series1.getData().add(new XYChart.Data("February", 200));
//        series1.getData().add(new XYChart.Data("March", 50));

//        XYChart.Series<String, Number> series2 = new XYChart.Series();
//        series2.setName("XYChart.Series 2");
//
//        series2.getData().add(new XYChart.Data("January", 150));
//        series2.getData().add(new XYChart.Data("February", 100));
//        series2.getData().add(new XYChart.Data("March", 60));



        primaryStage.setScene(new Scene(root, null));
        primaryStage.setTitle("Disk space analyzer");
        primaryStage.show();
    }
}
