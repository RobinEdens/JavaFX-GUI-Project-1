package Application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  SongController for use with both the AddSongs and PrintSongs fxml file. Used only by AddController and
    - PrintController
 */
public class SongController extends Subcontroller {
    private Song newSong;
    @FXML Button btnAddSong;
    @FXML Button btnClose;
    @FXML TextField txtName;
    @FXML TextField txtLength;
    @FXML TextArea printSong;

    public Song getNewSong() {
        return newSong;
    }
    public SongController() {
        super();
        super.loader = "PrintSongs.fxml";
        printSong = new TextArea();
    }
    public void initialize() {
        super.initialize();
        this.printSong.setText("Test");
    }

    public void populate(Album album) {
        StringBuilder getSongs = new StringBuilder("");
        for (Song song: album.getTrackList()) {
            getSongs.append(song + "\n" );

        }
        System.out.println(getSongs);
        this.printSong.setEditable(true);
        String tmp = getSongs.toString();
        this.printSong.setText(tmp);
        this.printSong.setEditable(false);
    }

    // for use with AddController
    @FXML
    private void handleAddSongButtonAction() {
        try {
            newSong = new Song(txtName.getText(), Double.parseDouble(txtLength.getText()));
            Stage stage = (Stage)btnAddSong.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Number mismatch");
            err.setContentText("Only enter numbers for length of song as an int or float");
            err.showAndWait();
        }
    }

    // For use with PrintController dynamic buttons
    @FXML
    private void handleCloseButtonAction() {
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }
}
