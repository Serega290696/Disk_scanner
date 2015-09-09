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
            optionStage.setTitle("Settings");
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
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
        fileChooser.setTitle("Save report");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            fileWorker.write(file, diskAnalyzer.getReport());
        }
    }

    @FXML
    public void changeAmount() {
        maxBarsAmount = (int) (Math.pow(amountControl.getValue(), 2d) / 100d);
    }

    @FXML
    public void changeSize(Event event) {
        mychart.setPrefWidth(60d * diskAnalyzer.getResultList().size() * (1d + 3d * (sizeControl.getValue() / 100d)));
        System.out.println(links);
//        System.out.println(sizeControl.getValue());
//        System.out.println(150*mychart.getData().size());
    }

    @FXML
    public void openDirectoryChooser() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setInitialDirectory(new File(filePathField.getText()));
        dirChooser.setTitle("Open File");
        File file = dirChooser.showDialog(null);//showOpenDialog(null);
        if (file != null) filePathField.setText(file.getAbsolutePath());
    }

    @FXML
    public void startAnalysis() {
        progressAnalysis.setVisible(true);
        File chosenFile = new File(filePathField.getText());
        diskAnalyzer.setOperationDone(false);
        diskAnalyzer.launch(chosenFile);
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
        File chosenFile = new File(filePathField.getText());
        progressAnalysis.setVisible(true);
        sizeControl.setValue(23);
        yord.setLabel("File size (bytes)");
        Arrays.asList(diskWorker.getOftenUsedPaths())
                .stream()
                .forEach((d) -> setUp(d)
                );
        Thread analysisThread = new Thread(() -> {
            startAnalysis();
        });
        analysisThread.setPriority(Thread.MIN_PRIORITY);
        analysisThread.start();
//        startAnalysis();
    }
}
