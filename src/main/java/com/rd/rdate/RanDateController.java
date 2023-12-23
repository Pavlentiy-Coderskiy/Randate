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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

/*TO-DO--------------------------------- 
    Прикрутить еще пару-тройку режимов
    Заполнить отстаток radioText'ов 
    Перекраска кнопки Check /
    Баг с логикой кнопки Check

----------------------------------------*/
public class RanDateController {

    @FXML
    private Button checkAnswerButton, openFileButton;

    @FXML
    private Text isCorrectText, dateLabel, radioText1, radioText2, radioText3, radioText4;

    @FXML
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;

    File file;
    Stage stage;
    RadioButton rightButton;
    Map<String, String> dateMap = new HashMap<String, String>();
    List<String> eventArray = new ArrayList<>();
    List<String> dateArray = new ArrayList<>();

    @FXML
    void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file with dates");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("*.txt files",
                "*.txt"));
        file = fileChooser.showOpenDialog(stage);
        // file = new File("C:\\Users\\phuha\\OneDrive\\Рабочий стол\\History
        // datetest.txt");
        readFile();
    }

    void readFile() {
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
                System.out.println("readFile error");
            }
            raiseQuestion();
        }
    }

    @FXML
    void checkAnswer(ActionEvent event) throws InterruptedException {
        if (file != null) {
            if (rightButton.isSelected()) {
                isCorrectText.setFill(Color.GREEN);
            } else {
                isCorrectText.setFill(Color.RED);
            }
            readFile();
            rightButton.setSelected(true);
        }

    }

    void raiseQuestion() {
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
    }

}