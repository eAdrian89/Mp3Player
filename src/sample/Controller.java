package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.Timer;

public class Controller implements Initializable {

    @FXML
    private Button pauseButton, stopButton, nextButton, repeatButton, shuffleButton,
            libraryButton, playlistButton, muteButton, volDownButton, volUpButton;
    @FXML
    private VBox vBox;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Pane pane;
    @FXML
    private Label label, songTitle, playList;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Slider volumeSlider;


    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs = new ArrayList<File>();

    private int songNumber = 0;

    private Timer timer;
    private TimerTask task;
    private boolean running;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

    }

    public void libraryMedia() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open Mp3 File");
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Mp3 File", "mp3");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(chooser.getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            songs = new ArrayList<>(Arrays.asList(chooser.getSelectedFiles()));
            System.out.println(songs.size());
            System.out.println(songs.toString());
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songTitle.setText(songs.get(songNumber).getName());
            playMedia();
        }

    }

    public void previousMedia() {
        if (songNumber > 0) {
            songNumber--;
            mediaPlayer.stop();

            if (running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songTitle.setText(songs.get(songNumber).getName());
            playMedia();
        } else {
            songNumber = songs.size() - 1;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songTitle.setText(songs.get(songNumber).getName());
            playMedia();
        }

    }

    public void playMedia() {
        beginTimer();
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    public void pauseMedia() {
        cancelTimer();
        mediaPlayer.pause();

    }

    public void stopMedia() {
        progressBar.setProgress(0);
        mediaPlayer.stop();
        cancelTimer();
    }

    public void nextMedia() {
        if (songNumber < songs.size() + 1) {
            songNumber++;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songTitle.setText(songs.get(songNumber).getName());
            playMedia();
        } else {
            songNumber = 0;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songTitle.setText(songs.get(songNumber).getName());
            playMedia();
        }
    }

    public void repeatMedia() {
        //TODO
    }

    public void shuffleMedia() {
        //TODO
    }


    public void muteMedia(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            mediaPlayer.setMute(true);
        }
        if (mouseEvent.getClickCount() == 2) {
            mediaPlayer.setMute(false);
        }

    }

    public void beginTimer() {

        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                System.out.println(current / end);
                progressBar.setProgress(current / end);

                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void cancelTimer() {
        running = false;
        timer.cancel();

    }


}
