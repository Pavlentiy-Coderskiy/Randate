package com.rd.rdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/*TO-DO--------------------------------- 
     --- Прикрутить еще пару-тройку режимов
     --- Заполнить отстаток radioText'ов 
     --- Перекраска кнопки Check /
     --- Баг с перемещением файла из изночальной деректории
     --- Баг с одним и тем же пунктом в radioLabel'e
----------------------------------------*/
public class RanDateController {

    @FXML
    private Button checkAnswerButton, openFileButton, closeButton, minButton;

    @FXML
    private Text isCorrectText, dateLabel, correctAnswerCountText, radioText1, radioText2, radioText3, radioText4;

    @FXML
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;

    @FXML
    private Pane topPane;
    private double xOffset = 0;
    private double yOffset = 0;
    private File file;
    private Stage stage;
    private int correctAnswerCount = 0;
    private RadioButton rightButton;
    private Map<String, String> dateMap = new HashMap<String, String>();
    private List<String> eventArray = new ArrayList<>();
    private List<String> dateArray = new ArrayList<>();

    @FXML
    protected void closeWindow(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void minimizeWndow(ActionEvent event) {
        Stage stage = (Stage) minButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void handleClickAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        xOffset = stage.getX() - event.getSceneX();
        yOffset = stage.getY() - event.getSceneY();
    }

    @FXML
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getSceneX() + xOffset);
        stage.setY(event.getSceneY() + yOffset);

    }

    @FXML
    protected void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file with dates");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("*.txt files",
                "*.txt"));
        file = fileChooser.showOpenDialog(stage);
        // file = new File("C:\\Users\\phuha\\OneDrive\\Рабочий стол\\History
        // datetest.txt");
        readFile();
    }

    protected void readFile() {
        if (file != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    String[] splitedString = line.split("--");
                    dateMap.put(splitedString[0], splitedString[1]);
                    line = reader.readLine();
                }
                eventArray.addAll(dateMap.values());
                dateArray.addAll(dateMap.keySet());
                reader.close();
            } catch (IOException e) {
                System.out.println(e.getClass().getSimpleName());
            }
            makeTheQuestion();
        }
    }

    @FXML
    protected void checkAnswer(ActionEvent event) throws InterruptedException {
        if (file != null) {
            if (rightButton.isSelected()) {
                Thread.sleep(250);
                isCorrectText.setFill(Color.GREEN);
                correctAnswerCount += 1;
                correctAnswerCountText.setText(correctAnswerCount + "\\20");
            } else {
                Thread.sleep(250);
                isCorrectText.setFill(Color.RED);
                correctAnswerCount -= 1;
                correctAnswerCountText.setText(correctAnswerCount + "\\20");
            }
            readFile();
            rightButton.requestFocus();
        }

    }

    protected void makeTheQuestion() {
        try {
            Random rand = new Random();
            String date = dateArray.get(rand.nextInt(dateArray.size()));
            String event = dateMap.get(date);
            List<Text> radioTextArray = new ArrayList<Text>();
            List<RadioButton> radioButtonsArray = new ArrayList<RadioButton>();
            Collections.addAll(radioTextArray, radioText1, radioText2, radioText3, radioText4);
            Collections.addAll(radioButtonsArray, radioButton1, radioButton2, radioButton3, radioButton4);

            int randomElement = rand.nextInt(radioButtonsArray.size());
            rightButton = radioButtonsArray.get(randomElement);
            Text rightText = radioTextArray.get(randomElement);
            radioTextArray.remove(rightText);
            for (Text t : radioTextArray) {
                t.setText(eventArray.get(rand.nextInt(eventArray.size())));
            }

            //
            dateLabel.setText("Date: " + date);
            rightText.setText(event);
            dateArray.clear();
            eventArray.clear();

        } catch (IllegalArgumentException e) {
            errorPopup(e);
        }
    }

    protected void errorPopup(Exception e) { // TODO Error Popup
        System.out.println(e.getClass().getSimpleName());
    }

}