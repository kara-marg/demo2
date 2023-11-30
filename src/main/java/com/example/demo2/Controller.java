package com.example.demo2;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
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
   /* @FXML
    private void handleButtonAction(ActionEvent event){

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser
                .ExtensionFilter("Select file (.mp3), (.mp4), (.MOV)", "*.mp3","*.mp4","*.MOV");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(null);
        String filePath = file.toURI().toString();

        if (filePath != null){
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

           // mediaView.setFitWidth(500);
           // mediaView.setFitHeight(500);
            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty height = mediaView.fitHeightProperty();

            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });

            sceneSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    mediaPlayer.seek(Duration.seconds(sceneSlider.getValue()));
                }
            });

            sceneSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isChanging) {
                    if (!isChanging) {
                        mediaPlayer.seek(Duration.seconds(sceneSlider.getValue()));
                    }
                }
            });

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    if (!sceneSlider.isValueChanging()) {
                        sceneSlider.setValue(newValue.toSeconds());
                    }
                }
            });


            mediaPlayer.play();
        }

    }
*/

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
                .ExtensionFilter("Select file (.mp3), (.mp4), (.MOV)", "*.mp3", "*.mp4", "*.MOV");
        fileChooser.getExtensionFilters().add(filter);
        openVideoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
                    mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                        @Override
                        public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                            if (!sceneSlider.isValueChanging()) {
                                sceneSlider.setValue(newValue.toSeconds());
                            }
                        }
                    });

                    mediaPlayer.setOnReady(new Runnable() {
                        @Override
                        public void run() {
                            volumeSlider.setValue(mediaPlayer.getVolume() * 100);
                            sceneSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
                            addSliderListeners();
                        }
                    });

                    mediaPlayer.play();
                }
            }
        });


//
//        // Additional code for continuous updating of slider during media playback
//        if (mediaPlayer != null) {
//            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
//                if (!sceneSlider.isValueChanging()) {
//                    sceneSlider.setValue(newValue.toSeconds());
//                }
//            });
//        }
    }

    private void addSliderListeners(){
        // Handle slider drag events
        sceneSlider.valueChangingProperty().addListener((observable, oldValue, isChanging) -> {
            if (!isChanging) {
                mediaPlayer.seek(Duration.seconds(sceneSlider.getValue()));
            }
        });

        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }


}