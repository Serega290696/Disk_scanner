package myProject.view;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import myProject.model.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Created by serega on 05.09.2015.
 */
public class MainWindowController implements Initializable {

    private static final double MENU_TOOLBAR_BUTTON_MAX_WIDTH = 60;
    public static int maxBarsAmount = 100;

//    public Pane getMainPane() {
//        return mainPane;
//    }

    @FXML
    public Pane mainPane;
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
    private FileWorker fileWorker = new FileWorker();
    private File reportFolder = SettingsConstants.DEFAULT_REPORTS_FOLDER_2;
    private static int reportNumber = 0;

    private static Function<Double, Integer> fun1 = a -> (int) (Math.pow(a, 2d) / 100d);

    private static Function<Double, Integer> fun2 = a -> (int) (60d * 1 * (1d + 3d * (a / 100d)));

    public static Stage getOptionStage() {
        return optionStage;
    }

    @FXML
    public static Stage optionStage;

    @FXML
    public void showSettingsWindow() {
        try {
//            mainPane.setDisable(true);
            mainPane.toBack();
            optionStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/OptionsWindow.fxml"));
            optionStage.setScene(new Scene(root, null));
            optionStage.setTitle("SettingsConstants");
            optionStage.setResizable(false);
            optionStage.setAlwaysOnTop(true);
            optionStage.setOnCloseRequest(event -> {
                mainPane.setDisable(false);
//                MainController.setPrimaryStage();
            });
//            MainController.setPrimaryStage(settings);
            optionStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showSaveReportWindow() {
        FileChooser fileChooser = new FileChooser();//����� ������ � �������� ������� � ����������
        fileChooser.setTitle("Save report");//��������� �������
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
//        fileChooser.setInitialDirectory(reportFolder);
        if (reportFolder.exists() && reportFolder.isDirectory()) {
            fileChooser.setInitialDirectory(reportFolder);
            System.out.println(reportFolder.exists());
            System.out.println(reportFolder.isDirectory());
        }
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            reportFolder = file.getAbsoluteFile();
            saveReport();
        }
    }

    private void saveReport() {
        if (!(reportFolder.isDirectory() && reportFolder.exists()))
            System.err.println("Directory was created! " + reportFolder.getAbsoluteFile());
        fileWorker.write(autoGenerateReportName(), diskAnalyzer.getReport());
    }

    private String autoGenerateReportName() {
        String s;
        while (new File(s = (reportFolder + "\\" + "report#" + (reportNumber) + ".txt")).exists()) {
            reportNumber++;
            System.out.println("\n: " + reportNumber + "\n: ");
        }
        System.out.println(new File(reportFolder + "\\" + "report#" + (reportNumber) + ".txt").exists());
        return s;
    }

    @FXML
    public void changeAmount() {
        maxBarsAmount = fun1.apply(amountControl.getValue());
    }

    @FXML
    public void changeSize(Event event) {
        mychart.setPrefWidth(fun2.apply(sizeControl.getValue()));
        System.out.println(links);
//        System.out.println(sizeControl.getValue());
//        System.out.println(150*mychart.getData().size());
    }

    @FXML
    public void openDirectoryChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        if (new File(filePathField.getText()).exists() && new File(filePathField.getText()).isDirectory())
            dirChooser.setInitialDirectory(new File(filePathField.getText()));
        dirChooser.setTitle("Open directory");
        File file = dirChooser.showDialog(null);//showOpenDialog(null);
        if (file != null) filePathField.setText(file.getAbsolutePath());
    }

    @FXML
    public void startAnalysis() {
        String startButtonText = startbutton.getText();
//        startbutton.setText("Stop!");
        progressAnalysis.setVisible(true);
        File chosenFile = new File(filePathField.getText());

//        Thread analyzerThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                diskAnalyzer.launch(chosenFile);
//            }
//        });
        diskAnalyzer.setOperationDone(false);
        diskAnalyzer.launch(chosenFile);
//        analyzerThread.start();

        if (requestHistory.size() > 0) {
            if (!requestHistory.get(0).getAbsoluteFile().toString().equals(chosenFile.getAbsoluteFile().toString())) {
                requestHistory.add(0, chosenFile.getAbsoluteFile());
            }
        } else
            requestHistory.add(0, chosenFile.getAbsoluteFile());
        setUp(chosenFile.getAbsolutePath());
//        Button toolBarButton = setUp(chosenFile.getAbsolutePath());
//        toolBarMenu.getItems().add(toolBarButton);

        xord.getCategories().remove(0, xord.getCategories().size() - 1);
        mychart.setPrefWidth(60d * diskAnalyzer.getResultList().size() * (1d + 3d * (sizeControl.getValue() / 100d)));
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Size: " + diskAnalyzer.getChosenFileSize() + " bytes = " + dataWorker.convert(diskAnalyzer.getChosenFileSize()));
        for (ResultFile rf : diskAnalyzer.getResultList()) {
            series1.getData().add(new XYChart.Data(rf.getName(), rf.getFinallySize()));
        }
        mychart.getData().clear();
        mychart.getData().addAll(series1);
        if (SettingsConstants.SAVE_REPORTS_AUTOMATICALLY_4)
            saveReport();
//        startbutton.setText(startButtonText);
    }

    @FXML
    public void backAction() {
        if (!(indexRequest < requestHistory.size() - 1)) {
            return;
        }
        indexRequest++;
        filePathField.setText(requestHistory.get(indexRequest).getAbsolutePath());
    }

    @FXML
    public void forwardAction() {
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
            if (tmpFile.exists())
                links.add(tmpFile);
            tmpButton.setText(tmpFile.getName());
        } else if (find(tmpFile.getAbsolutePath()) == null) {
            if (tmpFile.exists())
                links.add(tmpFile);
            tmpButton.setText(tmpFile.getAbsolutePath());
        } else return null;
        tmpButton.setMaxWidth(MENU_TOOLBAR_BUTTON_MAX_WIDTH);
        tmpButton.setId("quickPaste" + (links.size() + 1));
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
        toolBarMenu.getItems().add(tmpButton);
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
            if (f.getAbsoluteFile().toString().equals(s)) {
                return f.getAbsolutePath();
            }
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filePathField.setText(SettingsConstants.DEFAULT_ANALYZED_FOLDER_1.getAbsolutePath());
        File chosenFile = new File(filePathField.getText());
        progressAnalysis.setVisible(true);
        sizeControl.setValue(SettingsConstants.SLIDER_SIZE_5);
        amountControl.setValue(SettingsConstants.SLIDER_NUMBER_6);
        yord.setLabel("File size (bytes)");
        Arrays.asList(diskWorker.getOftenUsedPaths())
                .stream()
                .forEach((d) -> setUp(d)
                );
//        Thread analysisThread = new Thread(() -> {
        //
//        });
//        analysisThread.setPriority(Thread.MIN_PRIORITY);
//        analysisThread.start();
        if (SettingsConstants.START_ANALYSIS_WITH_APP_START_3)
            startAnalysis();
//        startAnalysis();
    }

    public static Function<Double, Integer> getFun1() {
        return fun1;
    }

    public static Function<Double, Integer> getFun2() {
        return fun2;
    }
}
