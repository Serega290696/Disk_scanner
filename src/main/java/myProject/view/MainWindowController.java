package myProject.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import myProject.model.DataWorker;
import myProject.model.DiskAnalyzer;
import myProject.model.DiskWorker;
import myProject.model.ResultFile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Created by serega on 05.09.2015.
 */
public class MainWindowController implements Initializable {

    private static final double MENU_TOOLBAR_BUTTON_MAX_WIDTH = 60;
    public static int specialOption = 100;
    @FXML
    private BarChart<String, Number> mychart;
    @FXML
    private CategoryAxis xord;
    @FXML
    private NumberAxis yord;
    @FXML
    private Button startbutton;
    @FXML
    public TextField filePathField;
    @FXML
    public Button fileChooser;
    @FXML
    public ProgressBar progressAnalysis;
    @FXML
    public Slider sizeControl;
    @FXML
    public Slider amountControl;
    @FXML
    public ToolBar toolBarMenu;

    private DiskAnalyzer diskAnalyzer = new DiskAnalyzer();
    private DataWorker dataWorker = new DataWorker();

    private ArrayList<File> requestHistory = new ArrayList<>();
    private int indexRequest = 0;
    private DiskWorker diskWorker = new DiskWorker();

    @FXML
    public void changeSize() {
        mychart.setPrefWidth(50 + 15d * diskAnalyzer.getResultList().size() * (1d + 29d * (sizeControl.getValue() / 100d)));
//        System.out.println("A: " + sizeControl.getValue());
    }

    @FXML
    public void changeAmount() {
//        System.out.println("B: " + amountControl.getValue());
        specialOption = (int) (Math.pow(amountControl.getValue(), 2d) / 100d);
    }

    @FXML
    public void openFileChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setInitialDirectory(new File(filePathField.getText()));
        dirChooser.setTitle("Open File");
        File file = dirChooser.showDialog(null);//showOpenDialog(null);
        if (file != null) filePathField.setText(file.getAbsolutePath());
    }

    @FXML
    public void startAnalysis() {
        progressAnalysis.setVisible(true);
//        Thread loadingThread = new Thread(() -> {
//            while (!diskAnalyzer.isOperationDone()) {
//                progressAnalysis.setProgress(diskAnalyzer.processStatus * 100);
//                System.out.println(diskAnalyzer.processStatus);
//            }
//        });
//        loadingThread.start();

//        System.out.format("%S\n", requestHistory.size());
        File chosenFile = new File(filePathField.getText());
//        Thread diskAnalyzerThread = new Thread(() -> {
//            diskAnalyzer.launch(chosenFile);
//        });
//        diskAnalyzerThread.start();
//        try {
//            Thread.sleep(500);
//            progressAnalysis.setProgress(0.2);
//            Thread.sleep(500);
//            progressAnalysis.setProgress(0.4);
//            Thread.sleep(500);
//            progressAnalysis.setProgress(0.5);
//            Thread.sleep(500);
//            progressAnalysis.setProgress(0.15);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        double i = 0;
//        while (i < 100) {
//            i += 0.0001;
//            System.out.println(i);
//            System.out.println(progressAnalysis.getProgress());
//            progressAnalysis.setProgress(i);
//            progressAnalysis.
//        }
        diskAnalyzer.setOperationDone(false);
//        while (!diskAnalyzer.isOperationDone()) {
//            progressAnalysis.setProgress(diskAnalyzer.processStatus * 100);
//            System.out.println(diskAnalyzer.processStatus);
//        }
        diskAnalyzer.launch(chosenFile);
        if (requestHistory.size() > 0) {
            if (!requestHistory.get(0).getAbsoluteFile().toString().equals(chosenFile.getAbsoluteFile().toString())) {
                requestHistory.add(0, chosenFile.getAbsoluteFile());
//                System.out.format("%d ; %S\n", requestHistory.size(), requestHistory.get(0));
            }
        } else
            requestHistory.add(0, chosenFile.getAbsoluteFile());
        Button toolBarButton = setUp(chosenFile.getAbsolutePath());
        toolBarMenu.getItems().add(toolBarButton);

//        System.out.println("A: " + xord.getCategories());
        xord.getCategories().remove(0, xord.getCategories().size() - 1);
//        System.out.println("B: " + xord.getCategories());
//        xord.setCategories(FXCollections.observableArrayList(
//                diskAnalyzer.getResultListFileNames()
//        ));
        mychart.setPrefWidth(60d * diskAnalyzer.getResultList().size() * (1d + 3d * (sizeControl.getValue() / 100d)));
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Size: " + diskAnalyzer.getChosenFileSize() + " bytes = " + dataWorker.convert(diskAnalyzer.getChosenFileSize()));
        for (ResultFile rf : diskAnalyzer.getResultList()) {
            series1.getData().add(new XYChart.Data(rf.getName(), rf.getFinallySize()));
        }
        mychart.getData().clear();
        mychart.getData().addAll(series1);
    }

    @FXML
    public void backAction() {
//        System.out.println(requestHistory);
        if (!(indexRequest < requestHistory.size() - 1)) {
            return;
        }
        indexRequest++;
        filePathField.setText(requestHistory.get(indexRequest).getAbsolutePath());
//        if (!requestHistory.get(0).getAbsoluteFile().toString().equals(filePathField.getText()))
//            requestHistory.add(0, new File(filePathField.getText()));
    }

    @FXML
    public void forwardAction() {
//        System.out.println(requestHistory);
        if (!(indexRequest > 0)) {
            return;
        }
        indexRequest--;
        filePathField.setText(requestHistory.get(indexRequest).getAbsolutePath());
    }

    @FXML
    public void upAction() {
        filePathField.setText(
                filePathField.getText().substring(
                        0,
                        (
                                filePathField.getText().lastIndexOf('\\') - 1 <= 0
                                        ?
                                        filePathField.getText().length()
                                        :
                                        filePathField.getText().lastIndexOf('\\'))
                )
        );
    }


    private Button setUp(String fullPath) {
        File tmpFile = new File(fullPath);
        Button tmpButton;
        if (diskWorker.isDisk(tmpFile))
            tmpButton = new Button(tmpFile.getAbsolutePath());
        else
            tmpButton = new Button(tmpFile.getName());
        tmpButton.setTooltip(new Tooltip(fullPath));
        if (find(tmpFile.getName()) == null && !diskWorker.isDisk(tmpFile)) {
            links.add(tmpFile);
            tmpButton.setText(tmpFile.getName());
        } else if (find(tmpFile.getAbsolutePath()) == null) {
            links.add(tmpFile);
            tmpButton.setText(tmpFile.getAbsolutePath());
        }
//        if(links.)
        tmpButton.setMaxWidth(MENU_TOOLBAR_BUTTON_MAX_WIDTH);
        tmpButton.setId("quickPaste" + (links.size() + 1));
//        tmpButton.setOnAction(event -> quickPasteAction(event));
        tmpButton.setOnAction((event) -> {
            String s = find(tmpButton.getText());
            if (s != null)
                filePathField.setText(s);
        });
        tmpButton.setOnMouseEntered((event) ->
                        tmpButton.setMaxWidth(-1)
        );
        tmpButton.setOnMouseExited((event) ->
                        tmpButton.setMaxWidth(MENU_TOOLBAR_BUTTON_MAX_WIDTH)
        );
        return tmpButton;
    }

    private ArrayList<File> links = new ArrayList<>();

    private String find(String s) {
        for (File f : links) {
            if (f.getAbsoluteFile().getName().equals(s)) {
                return f.getAbsolutePath();
            }
        }
        for (File f : links) {
//            System.out.println(f);
            if (f.getAbsoluteFile().toString().equals(s)) {
                return f.getAbsolutePath();
            }
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File chosenFile = new File(filePathField.getText());
        progressAnalysis.setVisible(true);
        sizeControl.setValue(23);
        yord.setLabel("File size (bytes)");
//        toolBarMenu.getItems().addAll(new DiskWorker().getOftenUsedPaths().new Button("NEW !!!"));
//        toolBarMenu.set
        Arrays.asList(diskWorker.getOftenUsedPaths())
                .stream()
                .forEach((d) -> {
//                            links.add(new File(d));
                            toolBarMenu.getItems().add(setUp(d));
                        }
                );

        startAnalysis();
//        progressAnalysis.setVisible(false);
//        Thread diskAnThread = new Thread(() -> {
//            diskAnalyzer.launch(chosenFile);
//        });
//        diskAnalyzer.isOperationDone = false;
//        diskAnThread.start();
//        diskAnalyzer.launch("D:\\Downloads\\example");
//        diskAnalyzer.launch(chosenFile);

//        while (!diskAnalyzer.isOperationDone) {
//
//        }
//        mychart.setTitle("Result");
//        xord.setLabel("File name");
//        xord.setCategories(FXCollections.<String>observableArrayList(
////                diskAnalyzer.getResultListFileNames()
//            new ArrayList<String>(){{add("AA"); add("BB"); add("CC");}}
//        ));
//        XYChart.Series series1 = new XYChart.Series();
//        series1.setName("Size: " + diskAnalyzer.getChosenFileSize() + " bytes = " + dataWorker.convert(diskAnalyzer.getChosenFileSize()));
//        for (ResultFile rf : diskAnalyzer.getResultList()) {
////            series1.getData().add(new XYChart.Data(rf.getName(), rf.getFinallySize()));
//        }
//        series1.getData().add(new XYChart.Data("AA", 100));
//        series1.getData().add(new XYChart.Data("BB", 150));
//        series1.getData().add(new XYChart.Data("CC", 80));
//        mychart.getData().addAll(series1);
    }

}
