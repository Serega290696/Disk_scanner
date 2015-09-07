package myProject.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import myProject.model.DataWorker;
import myProject.model.DiskAnalyzer;
import myProject.model.ResultFile;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by serega on 05.09.2015.
 */
public class MainWindowController implements Initializable {

    @FXML
    private BarChart<String, Number> mychart2;
    @FXML
    private CategoryAxis xord;
    @FXML
    private NumberAxis yord;
    @FXML
    private Button startbutton;
    @FXML
    public TextField filepathaaa;
    @FXML
    public Button fileChooser;

    private DiskAnalyzer diskAnalyzer = new DiskAnalyzer();
    private DataWorker dataWorker = new DataWorker();

    @FXML
    public void tt() {
//        filepathaaa.setText(fileChooser.getInitialFileName());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(null); // you could pass a stage reference here if you wanted.
        if (file != null) filepathaaa.setText(file.getAbsolutePath());
    }

    @FXML
    public void startAnalysis() {
        File chosenFile = new File(filepathaaa.getText());
        diskAnalyzer.launch(chosenFile);


//        System.out.println("A: " + xord.getCategories());
        xord.getCategories().remove(0, xord.getCategories().size() - 1);
//        System.out.println("B: " + xord.getCategories());
        xord.setCategories(FXCollections.observableArrayList(
                diskAnalyzer.getResultListFileNames()
        ));

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Size: " + diskAnalyzer.getChosenFileSize() + " bytes = " + dataWorker.convert(diskAnalyzer.getChosenFileSize()));
        for (ResultFile rf : diskAnalyzer.getResultList()) {
            series1.getData().add(new XYChart.Data(rf.getName(), rf.getFinallySize()));
        }
        mychart2.getData().clear();
        mychart2.getData().addAll(series1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File chosenFile = new File(filepathaaa.getText());
        Thread diskAnThread = new Thread(new Runnable() {
            @Override
            public void run() {
                diskAnalyzer.launch(chosenFile);
            }
        });
        diskAnalyzer.isOperationDone = false;
        diskAnThread.start();
//        diskAnalyzer.launch("D:\\Downloads\\example");

        while(!diskAnalyzer.isOperationDone) {

        }
        mychart2.setTitle("Result");
        xord.setLabel("File name");
        yord.setLabel("File size (bytes)");
        xord.setCategories(FXCollections.<String>observableArrayList(
                diskAnalyzer.getResultListFileNames()
        ));
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Size: " + diskAnalyzer.getChosenFileSize() + " bytes = " + dataWorker.convert(diskAnalyzer.getChosenFileSize()));
        for (ResultFile rf : diskAnalyzer.getResultList()) {
            series1.getData().add(new XYChart.Data(rf.getName(), rf.getFinallySize()));
        }
        mychart2.getData().addAll(series1);
    }
}
