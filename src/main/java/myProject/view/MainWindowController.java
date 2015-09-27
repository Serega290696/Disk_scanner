package myProject.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import myProject.model.*;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Created by serega on 05.09.2015.
 */
public class MainWindowController implements Initializable {

    private static final double MENU_TOOLBAR_BUTTON_MAX_WIDTH = 60;
    private static final SettingsConstants SETTINGS = SettingsConstants.SETTINGS;
    public static int maxBarsAmount = 100;

//    public Pane getMainPane() {
//        return mainPane;
//    }

    @FXML
    public Pane mainPane;
    @FXML
    private StackedBarChart<String, Number> mychart;
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
    @FXML
    public Button saveReportButton;

    @FXML
    private Button changeButton;
    @FXML
    private Label infoButton;
    @FXML
    private Label tipLabel;
    @FXML
    private RadioButton isOnCheckBox;
    @FXML
    private Button onOffButton;
    @FXML
    public TableView tableDatesColors;
    @FXML
    public TableColumn<TableDataObject, String> dateColumn;
    @FXML
    public TableColumn<TableDataObject, String> colorColumn;
    private ObservableList<TableDataObject> tableData = FXCollections.observableArrayList();


    private static Stage optionStage = null;
    private static Stage popupStage = null;

    private static DiskAnalyzer diskAnalyzer = new DiskAnalyzer();
    private DataWorker dataWorker = new DataWorker();

    private ArrayList<File> requestHistory = new ArrayList<>();
    private int indexRequest = 0;
    private DiskWorker diskWorker = new DiskWorker();
    //    private File reportFolder = new File("D:\\1-Programming\\1-Projects\\Part1\\BigProjects\\Disk_scanner\\reports");
//    private File reportFolder = new File(SettingsConstants.DEFAULT_REPORTS_FOLDER_2.getAbsolutePath());
    private File reportFolder = (File) SETTINGS.getDEFAULT_REPORTS_FOLDER_2();

    private static Function<Double, Integer> fun1 = a -> (int) (1 + Math.pow(a, 2d) / 100d);

    private static double sizeControlValue;
    private static int MY_CHART_DEFAULT_WIDTH;
    private static Function<Integer, Integer> fun2 = x -> (int) (80d + x * (15 + sizeControlValue / 100d * 400));

    private String startButtonText = "Start analysis!";

    private ReportGenerator rg = new ReportGenerator();
    private Thread analysisThread;
    private static ArrayList<HBox> blocksList = initBlocksList();

    private static ArrayList<HBox> initBlocksList() {
        return new ArrayList<HBox>() {
            {
                HBox box = new HBox();
                Slider slider = new Slider();
                slider.setValue(slider.getMin());
                box.getChildren().add(slider);
                add(box);
            }
        };
    }

    public static boolean isdListIsOn() {
        return dListIsOn;
    }

    private static boolean dListIsOn = true;

    public static Stage getOptionStage() {
        return optionStage;
    }

    public static void setPopupStage(Stage popupStage) {
        MainWindowController.popupStage = popupStage;
    }

    public static Stage getPopupStage() {
        return popupStage;
    }

    public static void setOptionStage(Stage optionStage) {
        MainWindowController.optionStage = optionStage;
    }

    @FXML
    public void showInfo() {
        tipLabel.setVisible(true);
    }

    @FXML
    public void hideInfo() {
        tipLabel.setVisible(false);
    }

    @FXML
    public void showAllTable() {
        tableDatesColors.setPrefHeight(250);
        tableDatesColors.autosize();
    }

    @FXML
    public void hideTable() {
        tableDatesColors.setPrefHeight(62);
        tableDatesColors.autosize();
    }

    @FXML
    public void showSettingsWindow() {
        System.out.println(optionStage);
        if (optionStage != null)
            return;
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
                optionStage = null;
//                MainController.setPrimaryStage();
            });
//            MainController.setPrimaryStage(settings);
            optionStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showPopupWindow() {
        if (popupStage != null)
            return;
        try {
//            mainPane.setDisable(true);
            mainPane.toBack();
            popupStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/PopupWindow.fxml"));
            popupStage.setScene(new Scene(root, null));
            popupStage.setTitle("Popup");
            popupStage.setResizable(true);
            popupStage.setAlwaysOnTop(false);
//            MainController.setPrimaryStage(settings);
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showSaveReportWindow() {
        FileChooser fileChooser = new FileChooser();//Класс работы с диалогом выборки и сохранения
//        fileChooser.setInitialDirectory(SettingsConstants.DEFAULT_REPORTS_FOLDER_2);
        fileChooser.setTitle("Save report");//Заголовок диалога
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("txt (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
//        fileChooser.setInitialDirectory(reportFolder);
//        System.out.println("'" + reportFolder.exists() + " : '" + reportFolder.getAbsolutePath()+"'");
        if (reportFolder.exists() && reportFolder.isDirectory()) {
            fileChooser.setInitialDirectory(reportFolder);
            fileChooser.setInitialFileName("report");
//            System.out.println(reportFolder.exists());
//            System.out.println(reportFolder.isDirectory());
        }
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            rg.setReportFolder(new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4)));
            rg.saveReport(diskAnalyzer, false);
            System.out.println(file.getAbsolutePath());
        }
    }

//    private void saveReport() {
//        if (!(reportFolder.isDirectory() && reportFolder.exists()))
//            System.err.println("Directory was created! " + reportFolder.getAbsoluteFile());
//        fileWorker.write(autoGenerateReportName(), diskAnalyzer.createReport());
//    }

//    private String autoGenerateReportName() {
//        String s;
//        while (new File(s = (reportFolder + "\\" + "report#" + (reportNumber) + ".txt")).exists()) {
//            reportNumber++;
////            System.out.println("\n: " + reportNumber + "\n: ");
//        }
////        System.out.println(new File(reportFolder + "\\" + "report#" + (reportNumber) + ".txt").exists());
//        return s;
//    }

    @FXML
    public void changeAmount() {
        maxBarsAmount = fun1.apply(amountControl.getValue());
    }

    @FXML
    public void changeSize(Event event) {
        sizeControlValue = sizeControl.getValue();
        mychart.setPrefWidth(fun2.apply(diskAnalyzer.getResultList().size()));
//        mychart.setPrefWidth(MY_CHART_DEFAULT_WIDTH);
//        if(fun2.apply(diskAnalyzer.getResultList().size()).)
//        System.out.println(links);
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
        if (diskAnalyzer.isRunning()) {
            System.out.println("RUN");
            diskAnalyzer.cancel();
            startbutton.setText(startButtonText);
            progressAnalysis.setVisible(false);
//            progressAnalysis.setProgress(0d);
            System.out.println("Is run: " + diskAnalyzer.isRunning());
            return;
        } else System.out.println("STOP");
        startButtonText = startbutton.getText();
        startbutton.setText("Stop!");
        progressAnalysis.setVisible(true);
//        File chosenFile = new File(filePathField.getText());

        diskAnalyzer = new DiskAnalyzer();
        diskAnalyzer.setPath(filePathField.getText());
        progressAnalysis.progressProperty().bind(diskAnalyzer.progressProperty());
        diskAnalyzer.setOnSucceeded(event -> {
            update();
        });
        diskAnalyzer.setOnCancelled(event -> {
            System.out.println("***");
            System.out.println(diskAnalyzer.isRunning());
            analysisThread.interrupt();
            System.out.println(diskAnalyzer.isRunning());
            System.out.println("***");
            System.out.println("I cancelled!");
        });
        progressAnalysis.setVisible(true);
        analysisThread = new Thread(diskAnalyzer);
        analysisThread.start();
    }

    public void update() {
        progressAnalysis.setVisible(false);
        File chosenFile = new File(filePathField.getText());

        if (requestHistory.size() > 0) {
            if (!requestHistory.get(0).getAbsoluteFile().toString().equals(chosenFile.getAbsoluteFile().toString())) {
                requestHistory.add(0, chosenFile.getAbsoluteFile());
            }
        } else
            requestHistory.add(0, chosenFile.getAbsoluteFile());
        setUp(chosenFile.getAbsolutePath(), true);
//        Button toolBarButton = setUp(chosenFile.getAbsolutePath());
//        toolBarMenu.getItems().add(toolBarButton);

        xord.getCategories().remove(0, xord.getCategories().size() - 1);
        mychart.setPrefWidth(60d * diskAnalyzer.getResultList().size() * (1d + 3d * (sizeControl.getValue() / 100d)));
//        mychart.setPrefWidth(MY_CHART_DEFAULT_WIDTH);
        mychart.getData().clear();
        if (dListIsOn) {
            int i;
            for (i = 0; i < diskAnalyzer.getResultDatesList().get(0).length; i++) ;
            System.out.println("Length: " + i);
            XYChart.Series series[] = {
                    new XYChart.Series(),
                    new XYChart.Series(),
                    new XYChart.Series(),
                    new XYChart.Series(),
                    new XYChart.Series(),
                    new XYChart.Series(),
                    new XYChart.Series(),
            };
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (int j = 0; j < i; j++) {
                series[j].setName("From: " + diskAnalyzer.getDatesList()[j].format(formatter));
                for (int k = 0; k < diskAnalyzer.getResultDatesList().size(); k++) {
                    ResultFile rf = diskAnalyzer.getResultDatesList().get(k)[j];
                    series[j].getData().add(new XYChart.Data(rf.getName(), rf.getFinallySize()));
                }
//                for (ResultFile[] rf : diskAnalyzer.getResultDatesList().get(j)) {
//                    series[j].getData().add(new XYChart.Data(rf[j].getName(), rf[j].getFinallySize()));
//                }
                mychart.getData().add(series[j]);
                System.out.println(mychart.getData().get(j));
            }
        } else {
            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Size: " + diskAnalyzer.getChosenFileSize() + " bytes = " + dataWorker.convert(diskAnalyzer.getChosenFileSize()));
            for (ResultFile rf : diskAnalyzer.getResultList()) {
                series1.getData().add(new XYChart.Data(rf.getName(), rf.getFinallySize()));
            }
            mychart.getData().addAll(series1);
        }

        if (SETTINGS.isSAVE_REPORTS_AUTOMATICALLY_4()) {
            rg.setReportFolder(new File(reportFolder.getAbsolutePath() + "\\report"));
            rg.saveReport(diskAnalyzer, true);
        }
        startbutton.setText(startButtonText);
        saveReportButton.setDisable(false);
        System.out.println(diskAnalyzer.getState());
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
        if (new File(filePathField.getText()).getParentFile() != null)
            filePathField.setText(new File(filePathField.getText()).getParent());
    }


    private Button setUp(String fullPath, boolean toBegin) {
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
        if (toBegin)
            toolBarMenu.getItems().add((toolBarMenu.getItems().size() >= 5) ? 5 : toolBarMenu.getItems().size() - 1, tmpButton);
        else
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
        filePathField.setText(SETTINGS.getDEFAULT_ANALYZED_FOLDER_1().getAbsolutePath());
        File chosenFile = new File(filePathField.getText());
        progressAnalysis.setVisible(false);
        sizeControl.setValue(SETTINGS.getSLIDER_SIZE_5());
        amountControl.setValue(SETTINGS.getSLIDER_NUMBER_6());
        yord.setLabel("File size (bytes)");
        Arrays.asList(diskWorker.getOftenUsedPaths())
                .stream()
                .forEach((d) -> setUp(d, false)
                );
//        final String tip = "If you want to see";
//        final String tip = "Если у Вас выбран один временной отрезок, то будут отображаться диаграммы только оранжевого цвета. " +
//                "Если Вы выбирите дополнительные временные отрезки, то часть диаграммы изменить цвет на другой. " +
//                "Каждому временому отрезку соответствует свой цвет. " +
//                "Это означает что файлы были созданы (модифицированы) в данном временом отрезке.";
        final String tip = "If you have selected only one time interval, the chart painted only orange.\n" +
                "If you add new time interval, part of the chart changes the color to another.\n" +
                "Each time interval painted to different colors.\n" +
                "This means that the files were created (modified) in a given time interval.";

        tipLabel.setText(tip);
        infoButton.setTooltip(new Tooltip(tip));

        initTable();
        if (SETTINGS.isSTART_ANALYSIS_WITH_APP_START_3())
            startAnalysis();
    }

    @FXML
    private void onOffDList() {
        dListIsOn = !dListIsOn;
        changeButton.setDisable(!dListIsOn);
        infoButton.setDisable(!dListIsOn);
        isOnCheckBox.setDisable(!dListIsOn);
        tableDatesColors.setDisable(!dListIsOn);
        isOnCheckBox.setSelected(dListIsOn);
        if (dListIsOn) {
            onOffButton.setText("   On");
            onOffButton.setStyle("-fx-background-color: darkgreen;");
            onOffButton.applyCss();
        } else {
            onOffButton.setText("   Off");
            onOffButton.setStyle("-fx-background-color: darkred;");
            onOffButton.applyCss();
        }
    }

    private void initTable() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<TableDataObject, String>("dateString"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<TableDataObject, String>("colorString"));

//        TableDataObject t = new TableDataObject();
//        t.setDateString("ddd");
//        t.setColorString("ccc");
//        tableData.add(t);
        String[] colorMass = {"orange", "yellow", "blue", "blue", "blue", "blue", "blue"};
        TableDataObject tFirst = new TableDataObject();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("A "+diskAnalyzer.getDatesList()[0]);
        tFirst.setDateString((diskAnalyzer.getDatesList()[0]).format(formatter) +
                " - " +
                LocalDateTime.now().format(formatter));
        tFirst.setColorString(colorMass[0]);
        tableData.add(tFirst);
        for (int i = 1; i < diskAnalyzer.getDatesList().length; i++) {
            TableDataObject t = new TableDataObject();
            t.setDateString((diskAnalyzer.getDatesList()[i]).format(formatter) +
                    " - " +
                    diskAnalyzer.getDatesList()[i - 1].format(formatter));
            t.setColorString(colorMass[i]);
            tableData.add(t);
        }
        new File("").lastModified();

        Date date = new Date();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();


        System.out.println(tFirst.getDateString());
        tableDatesColors.setItems(tableData);

    }

    public static Function<Double, Integer> getFun1() {
        return fun1;
    }

    public static Function<Integer, Integer> getFun2() {
        return fun2;
    }


    public static void setBlocksList(ArrayList<HBox> l) {
        blocksList = l;
    }

    public static LocalDateTime[] getDates() {
        if(blocksList == null) {
            blocksList = initBlocksList();
        }
        System.out.println(blocksList);
        LocalDateTime list[] = new LocalDateTime[blocksList.size()];
        for (int i = 0; i < blocksList.size(); i++) {
            HBox box = blocksList.get(i);
            list[i] =
                    DiskAnalyzer.getDateOf(
                            ((Slider) box.getChildren().get(0)).getValue()
                    );
        }
        return list;
    }
}
