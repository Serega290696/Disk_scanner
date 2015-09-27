package myProject.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import myProject.model.DiskAnalyzer;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ListSettingsController implements Initializable {

    @FXML
    private Button addButton;
    @FXML
    private Label dateLabel;
    @FXML
    private Pane pane;

    @FXML
    private Separator sep1;
    @FXML
    private Separator sep2;
    @FXML
    private Separator sep3;
    @FXML
    private Separator sep4;
    @FXML
    private Separator sep5;
    @FXML
    private Separator sep6;
    @FXML
    private Separator sep7;

    private Separator[] sep;


    @FXML
    private VBox bigblock;

    private final double MARGIN_X = 15;
    private final double MARGIN_Y = 49;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static ArrayList<LocalDateTime> datesList = new ArrayList<LocalDateTime>() {
        {
            add(LocalDateTime.MIN);
        }
    };
    private static ArrayList<HBox> blocksList = new ArrayList<HBox>() {
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < size(); i++) {
                Slider slider = (Slider) (get(i)).getChildren().get(0);
                CheckBox checkBox = (CheckBox) (get(i)).getChildren().get(1);
                builder.append("Slider: " + slider.getValue() + ".\t\tCheckBox: " + checkBox.isSelected() + "\n");
            }
            return builder.toString();
        }

        @Override
        public boolean add(HBox hBox) {
            datesList.add(DiskAnalyzer.getDateOf(((Slider) hBox.getChildren().get(0)).getValue()));
            return super.add(hBox);
        }

        @Override
        public HBox remove(int index) {
            datesList.remove(index);
            return super.remove(index);
        }
    };

    private boolean bockListIsLoaded = false;

//    @FXML

//    @FXML

//    @FXML

//    private void fillSliders() {


    private void delBlock(final int index) {
        addButton.setVisible(true);
        bigblock.getChildren().remove(index);
        blocksList.remove(index);
        for (int i = 1; i < bigblock.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) bigblock.getChildren().get(i);
//            System.out.println("I = " + i);
            Slider slider = ((Slider) (hBox.getChildren().get(0)));
            CheckBox checkBox = ((CheckBox) (hBox.getChildren().get(1)));
            Button button = ((Button) (hBox.getChildren().get(2)));
            final int finalI = i;
            slider.setOnMouseDragged(event -> {
//                System.out.println("Moved. ");
                if (finalI > 0) {
                    if (slider.getValue() > ((Slider) ((HBox) bigblock.getChildren().get(finalI - 1)).getChildren().get(0)).getValue())
                        slider.setValue(((Slider) ((HBox) bigblock.getChildren().get(finalI - 1)).getChildren().get(0)).getValue());
                }
                if (finalI < bigblock.getChildren().size() - 2) {
                    if (slider.getValue() < ((Slider) ((HBox) bigblock.getChildren().get(finalI)).getChildren().get(0)).getValue())
                        ((Slider) ((HBox) bigblock.getChildren().get(finalI)).getChildren().get(0)).setValue(slider.getValue());
                }
                Separator curSep = sep[finalI];
                curSep.setLayoutX(MARGIN_X + slider.getValue() / slider.getMax() * slider.getWidth() * 0.95);
                curSep.setLayoutY(MARGIN_Y + hBox.getHeight() * finalI + slider.getHeight() / 2 + 6);
                for (int j = finalI + 1; j < bigblock.getChildren().size() - 1; j++) {
                    HBox n = (HBox) bigblock.getChildren().get(j);

                    Slider curSlider = (Slider) n.getChildren().get(0);
                    curSlider.getOnMouseDragged().handle(null);
                }

                dateLabel.setText(
                        "From " +
                                DiskAnalyzer.getDateOf(slider.getValue()).format(formatter) +
                                " to " +
                                DiskAnalyzer.getDateOf(slider.getValue() + 1).format(formatter)
                );
            });
            checkBox.setOnAction(event -> {
                if (checkBox.isSelected())
                    System.out.println(finalI + " show up");
                else System.out.println(finalI + " hidden");
            });
            button.setOnAction(event -> {
                System.out.println("I = " + finalI);
                delBlock(finalI);
            });
        }
        for (int i = 0; i < bigblock.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) bigblock.getChildren().get(i);
            Slider slider = (Slider) hBox.getChildren().get(0);
            sep[i].setLayoutX(MARGIN_X + slider.getValue() / 100 * slider.getWidth() * 0.95);
            sep[i].setLayoutY(MARGIN_Y + hBox.getHeight() * i + slider.getHeight() / 2 + 6);
        }
//        if (index > 0)
//            ((HBox) bigblock.getChildren().get(bigblock.getChildren().size() - 2)).getChildren().get(0).setVisible(false);
        sep[bigblock.getChildren().size() - 1 - 1].setVisible(false);
        updateBlocksList();
    }

    @FXML
    private HBox addblock() {
        boolean isFirst = false;
        if (bigblock.getChildren().size() == 1)
            isFirst = true;
        final int curNumber = bigblock.getChildren().size() - 1;
        System.out.println(curNumber);
        if (curNumber > 6) {
            return null;
        } else if (curNumber == 6) {
            addButton.setVisible(false);
        }

        HBox newBlock = new HBox();
        Slider slider = new Slider(0, 100, 0);
        CheckBox checkBox = new CheckBox("Show");
        Button delB = new Button("Delete");
//        curSlider.getOnMouseDragged().handle(null);
        slider.setMin(0);
        slider.setMax(10);
        slider.setPrefWidth(167);
        slider.setPrefHeight(35);
        checkBox.setPrefWidth(60);
        checkBox.setPrefHeight(35);
        checkBox.setSelected(true);
        if (!isFirst) {
            delB.setPrefWidth(65);
            delB.setPrefHeight(33);
            delB.setFont(new Font(14));
            delB.setTextFill(Color.WHITE);
            delB.getStyleClass().add("secondButton");
        }
        slider.setOnMouseDragged(event -> {
//            System.out.println("Moved. ");
            if (curNumber > 0) {
                if (slider.getValue() > ((Slider) ((HBox) bigblock.getChildren().get(curNumber - 1)).getChildren().get(0)).getValue())
                    slider.setValue(((Slider) ((HBox) bigblock.getChildren().get(curNumber - 1)).getChildren().get(0)).getValue());
            }
            if (curNumber < bigblock.getChildren().size() - 2) {
                if (slider.getValue() < ((Slider) ((HBox) bigblock.getChildren().get(curNumber)).getChildren().get(0)).getValue())
                    ((Slider) ((HBox) bigblock.getChildren().get(curNumber)).getChildren().get(0)).setValue(slider.getValue());
            }
            Separator curSep = sep[curNumber];
            curSep.setLayoutX(MARGIN_X + slider.getValue() / slider.getMax() * slider.getWidth() * 0.95);
            curSep.setLayoutY(MARGIN_Y + newBlock.getHeight() * curNumber + slider.getHeight() / 2 + 6);
            for (int i = curNumber + 1; i < bigblock.getChildren().size() - 1; i++) {
                HBox n = (HBox) bigblock.getChildren().get(i);

                Slider curSlider = (Slider) n.getChildren().get(0);
                curSlider.getOnMouseDragged().handle(null);
            }
            dateLabel.setText(
                    "From " +
                            DiskAnalyzer.getDateOf(slider.getValue()).toString() +
                            " to " +
                            DiskAnalyzer.getDateOf(slider.getValue() + 1).toString()
            );
        });
        checkBox.setOnAction(event -> {
            if (checkBox.isSelected())
                System.out.println(curNumber + " show up");
            else System.out.println(curNumber + " hidden");
        });
        delB.setOnAction(event -> {
            System.out.println(curNumber);
            delBlock(curNumber);
        });

        if (isFirst)
            newBlock.getChildren().addAll(
                    slider,
                    checkBox
            );
        else
            newBlock.getChildren().addAll(
                    slider,
                    checkBox,
                    delB
            );
        if (curNumber > 0) {
//            System.out.println(curNumber - 1);
//            System.out.println(MARGIN_Y + newBlock.getHeight() * curNumber + slider.getHeight() / 2 + 6);
            sep[curNumber - 1].setVisible(true);
            sep[curNumber - 1].setLayoutX(MARGIN_X + slider.getValue() / 100 * slider.getWidth() * 0.95);
            sep[curNumber - 1].setLayoutY(MARGIN_Y + newBlock.getHeight() * curNumber + slider.getHeight() / 2 + 6);
            slider.getOnMouseDragged().handle(null);
        }
        HBox.setMargin(checkBox, new Insets(0, 0, 0, 10));
        HBox.setMargin(delB, new Insets(0, 0, 0, 10));
        newBlock.setVisible(true);
        newBlock.setPrefWidth(311);
        newBlock.setPrefHeight(55);
        if (bockListIsLoaded) {
            addToList(curNumber, newBlock);
        }
        for (Separator s : sep) {
            s.toBack();
        }
        for (int i = 0; i < bigblock.getChildren().size() - 1; i++) {
            HBox hBox = (HBox) bigblock.getChildren().get(i);
            Slider slider2 = (Slider) hBox.getChildren().get(0);
            sep[i].setLayoutX(MARGIN_X + slider2.getValue() / 100 * slider2.getWidth() * 0.95);
            sep[i].setLayoutY(MARGIN_Y + hBox.getHeight() * i + slider2.getHeight() / 2 + 6);
        }
        updateBlocksList();
//        System.out.println(blocksList);
        return newBlock;
    }

    private void addToList(int index, HBox newBlock) {
        bigblock.getChildren().add(index, newBlock);
        if (bockListIsLoaded)
            blocksList.add(newBlock);
        updateBlocksList();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bockListIsLoaded = false;
        sep = new Separator[]{sep1, sep2, sep3, sep4, sep5, sep6, sep7};
        int a = blocksList.size();
        if (blocksList.size() == 0) {
            bockListIsLoaded = true;
            addblock();
            bockListIsLoaded = false;
        } else {
            for (HBox box : blocksList) {
                HBox newBlock = addblock();
                ((Slider) getNode(newBlock, 0)).setValue(
                        ((Slider) getNode(box, 0)).getValue()
                );
                ((CheckBox) getNode(newBlock, 1)).setSelected(
                        ((CheckBox) getNode(box, 1)).isSelected()
                );
                addToList(bigblock.getChildren().size() - 1, newBlock);
            }
        }
        System.out.println(a + " -> " + blocksList.size());
        MainWindowController.getPopupStage().setOnCloseRequest(event -> {
                    updateBlocksList();
                    MainWindowController.setBlocksList(ListSettingsController.getBlocksList());
                    MainWindowController.setPopupStage(null);
                }
        );
        bockListIsLoaded = true;
//        addblock();
    }

    private Node getNode(HBox box, int index) {
        return box.getChildren().get(index);
    }

    public ArrayList<HBox> updateBlocksList() {
        System.out.println("{");
        for (int i = 0; i < bigblock.getChildren().size() - 1; i++) {
            HBox box = (HBox) (bigblock.getChildren().get(i));

            ((Slider) getNode(blocksList.get(i), 0)).setValue(
                    ((Slider) getNode((HBox) bigblock.getChildren().get(i), 0)).getValue()
            );
            ((CheckBox) getNode(blocksList.get(i), 1)).setSelected(
                    ((CheckBox) getNode((HBox) bigblock.getChildren().get(i), 1)).isSelected()
            );
            System.out.println(((Slider) getNode(blocksList.get(i), 0)).getValue());
        }
        System.out.println("}");
//        bigblock.getChildren().get(0);
//        getNode()
        return blocksList;
    }


    public static ArrayList<HBox> getBlocksList() {
        return blocksList;
    }

}
