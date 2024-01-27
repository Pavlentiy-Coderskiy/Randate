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
import javafx.scene.control.Alert;
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
     --- Перекраска кнопки Check 
     --- Баг с одним и тем же пунктом в radioLabel'e
     --- Сделать PopUp более симпотным
     --- Сохранение шрифтов
----------------------------------------*/
public class RanDateController {

    @FXML
    private Button checkAnswerButton, openFileButton, closeButton, minButton;

    @FXML
    private Text isCorrectText, dateLabel, correctAnswerCountText, radioText1, radioText2, radioText3, radioText4;

    @FXML
    private RadioButton rightButton, radioButton1, radioButton2, radioButton3, radioButton4;

    @FXML
    private Pane topPane;

    private double xOffset = 0;
    private double yOffset = 0;
    private File file;
    private Stage stage;
    private Map<String, String> dataMap = new HashMap<String, String>();
    private List<String> eventArray = new ArrayList<>();
    private List<String> dateArray = new ArrayList<>();
    private int correctAnswerCount = eventArray.size();
    private int numberOfChecks = 0;

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
    protected void handleMovementAction(MouseEvent event) {
        Stage stage = (Stage) topPane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    protected void handlePressedAction(MouseEvent event) {
        xOffset = event.getX();
        yOffset = event.getY();
    }

    @FXML
    protected void openFile(ActionEvent event) {
        clearAll();
        dataMap.clear();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file with dates");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("*.txt files",
                "*.txt"));
        file = fileChooser.showOpenDialog(stage);
        // file = new File("C:\\Users\\phuha\\OneDrive\\Рабочий стол\\History
        // datetest.txt");
        readFile();
        correctAnswerCount = dataMap.keySet().size();
        correctAnswerCountText.setText("");
    }

    protected void readFile() {
        if (file != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    String[] splitedString = line.split("--");
                    // !FIXIT
                    if (splitedString.length == 2)
                        dataMap.put(splitedString[0] += " ", splitedString[1]);
                    line = reader.readLine();
                }
                eventArray.addAll(dataMap.values());
                dateArray.addAll(dataMap.keySet());
                reader.close();
            } catch (IOException e) {
                System.out.println(e.getClass().getSimpleName());
            }
            buildQuestion();
        }
    }

    @FXML
    protected void checkAnswer(ActionEvent event) throws InterruptedException {
        if (file != null && rightButton != null) {
            if (rightButton.isSelected()) {
                Thread.sleep(250);
                isCorrectText.setFill(Color.GREEN);
                correctAnswerCountText.setText(correctAnswerCount + "\\" + dataMap.keySet().size());
            } else {
                Thread.sleep(250);
                isCorrectText.setFill(Color.RED);
                correctAnswerCount -= 1;
                correctAnswerCountText.setText(correctAnswerCount + "\\" + dataMap.keySet().size());
            }
            numberOfChecks++;
            readFile();
            rightButton.requestFocus();
            if (numberOfChecks == dataMap.keySet().size()) {
                userScore();
            }
        }
    }

    protected void userScore() {
        int procentOfCorrectAnswer = (correctAnswerCount * 100) / dataMap.keySet().size();
        if (procentOfCorrectAnswer == 100)
            conformationPopup("Count assessment", procentOfCorrectAnswer + "% Correct answers", "Your assessment: 5");
        else if (procentOfCorrectAnswer >= 95)
            conformationPopup("Count assessment", procentOfCorrectAnswer + "% Correct answers", "Your assessment: 5-");
        else if (procentOfCorrectAnswer >= 80)
            conformationPopup("Count assessment", procentOfCorrectAnswer + "% Correct answers", "Your assessment: 4");
        else if (procentOfCorrectAnswer >= 75)
            conformationPopup("Count assessment", procentOfCorrectAnswer + "% Correct answers", "Your assessment: 4-");
        else if (procentOfCorrectAnswer >= 55)
            conformationPopup("Count assessment", procentOfCorrectAnswer + "% Correct answers", "Your assessment: 3");
        else if (procentOfCorrectAnswer >= 53)
            conformationPopup("Count assessment", procentOfCorrectAnswer + "% Correct answers", "Your assessment: 3-");
        else if (procentOfCorrectAnswer <= 55 && procentOfCorrectAnswer != 0)
            conformationPopup("Count assessment", procentOfCorrectAnswer + "% Correct answers", "Your assessment: 2");
        else if (procentOfCorrectAnswer == 0) {
            conformationPopup("Count assessment", procentOfCorrectAnswer + "% Correct answers",
                    "Your assessment: Dima Ignashev");
        }
        clearAll();
        file = null;
        numberOfChecks = 0;
        correctAnswerCount = dataMap.keySet().size();
        correctAnswerCountText.setText("");
    }

    protected void clearAll() {
        radioText1.setText("Text");
        radioText2.setText("Text");
        radioText3.setText("Text");
        radioText4.setText("Text");
        dateLabel.setText("Date:");
        isCorrectText.setFill(Color.WHITE);
    }

    protected void buildQuestion() {
        try {
            Random rand = new Random();
            String date = dateArray.get(rand.nextInt(dateArray.size()));

            List<Text> radioTextArray = new ArrayList<>();
            List<RadioButton> radioButtonsArray = new ArrayList<>();
            Collections.addAll(radioTextArray, radioText1, radioText2, radioText3, radioText4);
            Collections.addAll(radioButtonsArray, radioButton1, radioButton2, radioButton3, radioButton4);

            int randomElement = rand.nextInt(radioButtonsArray.size());
            rightButton = radioButtonsArray.get(randomElement);
            Text rightAnswerText = radioTextArray.get(randomElement);
            rightAnswerText.setText(dataMap.get(date));
            eventArray.remove(dataMap.get(date));
            radioTextArray.remove(rightAnswerText);
            for (Text radioText : radioTextArray) {
                String randomEvent = eventArray.get(rand.nextInt(eventArray.size()));
                radioText.setText(randomEvent);
                eventArray.remove(randomEvent);
            }

            //
            dateLabel.setText("Date: " + date);
            dateArray.clear();
            eventArray.clear();

        } catch (Exception e) {
            errorPopup(e, "File is not Found of file cannot be processed. Please, choose the other file.");
        }
    }

    protected void errorPopup(Exception exception, String instruction) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error: " + exception.getClass().getSimpleName() + "!");
        alert.setHeaderText(exception.getClass().getSimpleName());
        alert.setContentText(instruction);
        alert.show();
    }

    protected void conformationPopup(String title, String content, String header) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();

    }

}