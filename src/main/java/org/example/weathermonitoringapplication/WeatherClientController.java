package org.example.weathermonitoringapplication;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import java.io.IOException;

public class WeatherClientController {

    @FXML private TextField searchField;
    @FXML private ImageView weatherIcon;
    @FXML private Label temperatureLabel;
    @FXML private Label conditionLabel;
    @FXML private ImageView humidityIcon;
    @FXML private Label humidityLabel;
    @FXML private ImageView windIcon;
    @FXML private Label windSpeedLabel;

    @FXML private Label timeLabel1;
    @FXML private Label timeTempLabel1;
    @FXML private Label timeConditionLabel1;

    @FXML private Label timeLabel2;
    @FXML private Label timeTempLabel2;
    @FXML private Label timeConditionLabel2;

    @FXML private Label timeLabel3;
    @FXML private Label timeTempLabel3;
    @FXML private Label timeConditionLabel3;

    @FXML private Label timeLabel4;
    @FXML private Label timeTempLabel4;
    @FXML private Label timeConditionLabel4;

    @FXML private Label timeLabel5;
    @FXML private Label timeTempLabel5;
    @FXML private Label timeConditionLabel5;

    @FXML private Label timeLabel6;
    @FXML private Label timeTempLabel6;
    @FXML private Label timeConditionLabel6;

    @FXML private Label dateTimeLabel;

    private WeatherClient client;

    @FXML
    public void initialize() {
        try {
            resetData();
            client = new WeatherClient("localhost", 12345); // Modify with your server address and port
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetData() {
        temperatureLabel.setText("0");
        conditionLabel.setText("null");
        humidityLabel.setText("Humidity: 0");
        windSpeedLabel.setText("Wind: 0");

        timeLabel1.setText("00:00");
        timeTempLabel1.setText("0");
        timeConditionLabel1.setText("null");

        timeLabel2.setText("00:00");
        timeTempLabel2.setText("0");
        timeConditionLabel2.setText("null");

        timeLabel3.setText("00:00");
        timeTempLabel3.setText("0");
        timeConditionLabel3.setText("null");

        timeLabel4.setText("00:00");
        timeTempLabel4.setText("0");
        timeConditionLabel4.setText("null");

        timeLabel5.setText("00:00");
        timeTempLabel5.setText("0");
        timeConditionLabel5.setText("null");

        timeLabel6.setText("00:00");
        timeTempLabel6.setText("0");
        timeConditionLabel6.setText("null");

        dateTimeLabel.setText("null");

        weatherIcon.setImage(new Image(getClass().getResourceAsStream("/Images/sun.png")));
    }

    @FXML
    private void handleSearchButton(ActionEvent event) {
        String city = searchField.getText().trim(); // Trim extra spaces
        if (city.isEmpty()) {
            showError("Please enter a city name.");
            return;
        }

        try {
            String response = client.requestWeatherData(city);

            // Check if the response contains an error message
            if (response.startsWith("Error")) {
                showError(response);
                resetData();
                return;
            }

            // Split by lines
            String[] lines = response.split("\n");

            // Parse current weather data (first line)
            String[] currentWeatherData = lines[0].split(","); // Assuming first line has weather data separated by commas
            if (currentWeatherData.length == 5) {
                // Update UI with current weather data
                temperatureLabel.setText(currentWeatherData[0]);  // temperature
                conditionLabel.setText(currentWeatherData[1]);    // weather condition
                humidityLabel.setText("Humidity: " + currentWeatherData[2]);  // humidity
                windSpeedLabel.setText("Wind: " + currentWeatherData[3]); // wind speed
                dateTimeLabel.setText("Local Date & Time: " + currentWeatherData[4]);

                // Update weather icons based on condition
                String condition = currentWeatherData[1].toLowerCase();
                if (condition.contains("cloud")) {
                    weatherIcon.setImage(new Image(getClass().getResourceAsStream("/Images/cloud.png")));
                } else if (condition.contains("sunny")) {
                    weatherIcon.setImage(new Image(getClass().getResourceAsStream("/Images/sunny.png")));
                } else {
                    weatherIcon.setImage(new Image(getClass().getResourceAsStream("/Images/sun.png")));
                }
            } else {
                // Handle invalid or incomplete data
                resetData();
                showError("No valid weather data found for this city. Please try again.");
            }

            // Parse forecast data (subsequent lines)
            if (lines.length > 1) {
                // Loop through forecast data and assign to corresponding labels
                for (int i = 1; i <= 6; i++) { // We are considering 6 forecast hours
                    if (i < lines.length) {
                        String[] forecastData = lines[i].split(",");
                        String time = forecastData[0];
                        String temperature = forecastData[1];
                        String condition = forecastData[2];

                        // Assigning the data to the corresponding forecast labels
                        switch (i) {
                            case 1:
                                timeLabel1.setText(time);
                                timeTempLabel1.setText(temperature);
                                timeConditionLabel1.setText(condition);
                                break;
                            case 2:
                                timeLabel2.setText(time);
                                timeTempLabel2.setText(temperature);
                                timeConditionLabel2.setText(condition);
                                break;
                            case 3:
                                timeLabel3.setText(time);
                                timeTempLabel3.setText(temperature);
                                timeConditionLabel3.setText(condition);
                                break;
                            case 4:
                                timeLabel4.setText(time);
                                timeTempLabel4.setText(temperature);
                                timeConditionLabel4.setText(condition);
                                break;
                            case 5:
                                timeLabel5.setText(time);
                                timeTempLabel5.setText(temperature);
                                timeConditionLabel5.setText(condition);
                                break;
                            case 6:
                                timeLabel6.setText(time);
                                timeTempLabel6.setText(temperature);
                                timeConditionLabel6.setText(condition);
                                break;
                        }
                    }
                }
            } else {
                // No forecast data available
                showError("No forecast data available");
            }

        } catch (IOException e) {
            showError("Error connecting to the server.");
            e.printStackTrace();
        }
    }

    private void showError(String errorMessage) {
        // Show error message on the GUI
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
