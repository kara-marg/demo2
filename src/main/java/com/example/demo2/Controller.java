package com.example.demo2;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    private MediaPlayer mediaPlayer;

    private  Media media;
    @FXML
    private MediaView mediaView;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider sceneSlider;
    @FXML
    private Label timeLabel;

    @FXML
    private Button openVideoButton;

    @FXML
    private void playMedia(){
        mediaPlayer.play();
        mediaPlayer.setRate(1);
    }

    @FXML
    private void stopMedia(){
        mediaPlayer.stop();
    }

    @FXML
    private void pauseMedia(){
        mediaPlayer.pause();
    }

    @FXML
    private void slowMedia(){
        mediaPlayer.setRate(0.75);
    }

    @FXML
    private void slow2Media(){
        mediaPlayer.setRate(0.5);
    }

    @FXML
    private void fastMedia(){
        mediaPlayer.setRate(1.5);
    }

    @FXML
    private void fast2Media(){
        mediaPlayer.setRate(2);
    }

    @FXML
    private void exitMedia(){
        System.exit(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser
                .ExtensionFilter("Select file (.mp3), (.mp4)", "*.mp3", "*.mp4");
        fileChooser.getExtensionFilters().add(filter);
        openVideoButton.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                String filePath = file.toURI().toString();
                media = new Media(filePath);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);

                DoubleProperty width = mediaView.fitWidthProperty();
                DoubleProperty height = mediaView.fitHeightProperty();

                width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
                mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                    if (!sceneSlider.isValueChanging()) {
                        sceneSlider.setValue(newValue.toSeconds());
                    }
                });

                mediaPlayer.setOnReady(() -> {
                    volumeSlider.setValue(mediaPlayer.getVolume() * 100);
                    sceneSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
                    addSliderListeners();
                });

                mediaPlayer.play();
            }
        });
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        Duration currentTime = mediaPlayer.getCurrentTime();
                        String formattedTime = formatTime(currentTime);
                        timeLabel.setText(formattedTime);
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    private String formatTime(Duration time) {
        int minutes = (int) time.toMinutes();
        int seconds = (int) time.toSeconds() % 60;
        int hours = minutes / 60;
        minutes %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    private void addSliderListeners(){
        sceneSlider.valueChangingProperty().addListener((observable, oldValue, isChanging) -> {
            if (!isChanging) {
                mediaPlayer.seek(Duration.seconds(sceneSlider.getValue()));
            }
        });

        volumeSlider.valueProperty().addListener(observable -> mediaPlayer.setVolume(volumeSlider.getValue() / 100));
    }

}