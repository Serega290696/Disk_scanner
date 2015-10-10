package myProject.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import myProject.model.SettingsConstants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OptionWinController implements Initializable {
    @FXML
    private CheckBox radioStartAppWithWin;
    @FXML
    private CheckBox radioStartAnalysisWithAppStart;
    @FXML
    private CheckBox radioSaveReportsAuto;
    @FXML
    private Slider sliderSize;
    @FXML
    private Slider sliderNumber;
    @FXML
    private Label sliderSizeL;
    @FXML
    private Label sliderNumberL;

    private SettingsConstants SETTINGS = SettingsConstants.SETTINGS;
    private final String iconPath = "settings_icon.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainWindowController.getOptionStage().getIcons().add(new Image("file:" + iconPath));
        radioStartAppWithWin.setSelected(SETTINGS.isSTART_APP_WITH_WINDOWS_7());
        radioStartAnalysisWithAppStart.setSelected(SETTINGS.isSTART_ANALYSIS_WITH_APP_START_3());

        radioSaveReportsAuto.setSelected(SETTINGS.isSAVE_REPORTS_AUTOMATICALLY_4());

        sliderSize.setValue(SETTINGS.getSLIDER_SIZE_5());
        sliderNumber.setValue(SETTINGS.getSLIDER_NUMBER_6());
        if (SETTINGS.isSTART_APP_WITH_WINDOWS_7()) {
            radioStartAppWithWin.setSelected(true);
            radioStartAppWithWin.setDisable(true);
        }
//        setOnCloseRequest(event -> {
//                    mainPane.setDisable(false);
//                    optionStage = null;
//                };
        updateSliderL();
    }

    @FXML
    public void updateSliderL() {
        String tmpS = sliderSizeL.getText();
        sliderSizeL.setText(
                tmpS.substring(0, tmpS.lastIndexOf('(') + 1) +
                        (int) (15 + sliderSize.getValue() / 100d * 400)
                        + ")"
        );
        tmpS = sliderNumberL.getText();
        sliderNumberL.setText(
                tmpS.substring(0, tmpS.lastIndexOf('(') + 1) +
                        MainWindowController.getFun1().apply(sliderNumber.getValue())
                        + ")"
        );
    }

    @FXML
    public void clearCashAction() {
        exit();
    }

    @FXML
    public void saveAction() {
        SETTINGS.setStartAnalysisWithAppStart3(radioStartAnalysisWithAppStart.isSelected());
        SETTINGS.setSaveReportsAutomatically4(radioSaveReportsAuto.isSelected());
        SETTINGS.setSliderSize5(sliderSize.getValue());
        SETTINGS.setSliderNumber6(sliderNumber.getValue());
        try {
            SETTINGS.setStartAppWithWindows(radioStartAppWithWin.isSelected());
        } catch (IOException e) {
            e.printStackTrace();
        }

        exit();
    }

    @FXML
    public void cancelAction() {
        exit();
    }

    private void exit() {
        MainWindowController.getOptionStage().close();
        MainWindowController.setOptionStage(null);
    }

    @FXML
    public void chooseDefAn() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        if (SETTINGS.getDEFAULT_ANALYZED_FOLDER_1().exists() && SETTINGS.getDEFAULT_ANALYZED_FOLDER_1().isDirectory())
            dirChooser.setInitialDirectory(SETTINGS.getDEFAULT_ANALYZED_FOLDER_1());
        dirChooser.setTitle("Open directory");
        File file = dirChooser.showDialog(null);
        if (file != null) {
            SETTINGS.setDefaultAnalyzedFolder1(file);
        }
    }

    @FXML
    public void chooseDefRep() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        if (SETTINGS.getDEFAULT_REPORTS_FOLDER_2().exists() && SETTINGS.getDEFAULT_REPORTS_FOLDER_2().isDirectory())
            dirChooser.setInitialDirectory(SETTINGS.getDEFAULT_REPORTS_FOLDER_2());
        dirChooser.setTitle("Open directory");
        File file = dirChooser.showDialog(null);
        if (file != null) {
            SETTINGS.setDefaultReportsFolder2(file);
        }
    }
}
