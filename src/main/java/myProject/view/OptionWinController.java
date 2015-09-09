package myProject.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ResourceBundle;

//import java.awt.*;

/**
 * Created by serega on 08.09.2015.
 */
public class OptionWinController implements Initializable {
    @FXML
    private CheckBox radioStartAppWithWin;
    @FXML
    private CheckBox radioStartAnalysisWithAppStart;
    @FXML
    private Button buttonDefaultFolder;
    @FXML
    private CheckBox radioSaveReportsAuto;
    @FXML
    private Button defaultReportsFolder;
    @FXML
    private Button buttonClearCash;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("IN CNTRL!");
//        cancelButton.setOnAction((e) -> {
//            MainWindowController.getOptionStage().close();
//        });
    }

    @FXML
    public void clearCashAction() {
        MainWindowController.getOptionStage().close();
    }

    @FXML
    public void saveAction() {
        MainWindowController.getOptionStage().close();
//        MainWindowController.().setDisable(false);
    }

    @FXML
    public void cancelAction() {
        MainWindowController.getOptionStage().close();
//        MainWindowController.getMainPane().setDisable(false);
    }
}
