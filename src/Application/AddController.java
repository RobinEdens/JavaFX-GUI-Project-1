package Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  AddController used in the Add.fxml scene, which allows adding new Albums to the Inventory. Also sets a loop to call and
    - create as many AddSong.fxml scenes as needed to fill up as many user-inputted songs (to make sure it doesnt get too large, the max value it will
    - accept is 15 songs otherwise it will default to that)
 */
public class AddController extends Subcontroller {


    @FXML Text labelAlbumLength;
    @FXML TextField textBandName;
    @FXML TextField textAlbumName;
    @FXML TextField textPublisher;
    @FXML TextField textSongs;
    @FXML TextField textLength;
    @FXML DatePicker dateAlbumReleased;
    @FXML ToggleButton toggleAddSongs;

    @FXML Button btnAdd;

    public AddController(MainController main) {
        super(main);
        super.loader = "Add.fxml";
        this.err = new Alert(Alert.AlertType.ERROR);
    }
    public AddController(){
        super();
        super.loader = "Add.fxml";
        this.err = new Alert(Alert.AlertType.ERROR);
    }

    private void clearFields() {
        dateAlbumReleased.setValue(null);
        toggleAddSongs.setSelected(false);
        textBandName.clear(); textAlbumName.clear();
        textPublisher.clear(); textSongs.clear();
        textBandName.requestFocus();
        this.handleToggleAction();
    }

    private Song createSongPopup()  throws IOException {
        Stage songPopup = new  Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSongs.fxml"));
        Parent root = loader.load();
        SongController control = loader.getController();
        control.setInvAlbum(this.getInvAlbum());
        Scene scene = new Scene(root);
        songPopup.setTitle("Add Song to Album");
        songPopup.setScene(scene);
        control.setColor(this.getColor());
        control.applyColor();
        songPopup.showAndWait();


        if (control.getNewSong() != null) {
            return control.getNewSong();
        } else {
            return new Song("None Specified", 0);
        }
    }

    // Takes the values inputted by the user, verifies that all values are valid and catches any NumberFormatException errors due to invalid input, then if
    // the user decided to add Songs, it iterates a loop to create AddSong scenes equal to the amount of songs in the album, to enter that information.
    @FXML
    private void handleAddButtonAction() throws IOException {
        try {
            Album tmpAlbum;
            ArrayList<Song> tmpSongList = new ArrayList<>();
            if (toggleAddSongs.isSelected()) {
                int numSongs = Integer.parseInt(textSongs.getText());
                 if (numSongs > 15) {
                    err.setTitle("Over 15 songs");
                    err.setContentText("You are adding over 15 songs, setting song limit to 15");
                    err.showAndWait();
                    numSongs = 15;
                }

                for (int iter = 0; iter < numSongs; iter++) {
                    tmpSongList.add(this.createSongPopup());
                }
                tmpAlbum = new Album(textBandName.getText(), textAlbumName.getText(), textPublisher.getText(),
                        tmpSongList, dateAlbumReleased.getValue());
            }
            else {
                tmpSongList.add(new Song("None Specified", Integer.parseInt(textLength.getText())));
                tmpAlbum = new Album(textBandName.getText(), textAlbumName.getText(), textPublisher.getText(),
                        tmpSongList, dateAlbumReleased.getValue());
                textLength.clear();
            }
            this.invAlbum.add(tmpAlbum);
        } catch (NumberFormatException e) {
            err.setTitle("String inputted");
            err.setContentText("Integers only for # of Songs/Album Length");
            err.showAndWait();
        }
        this.clearFields();
    }

    // Show or hides the album length field based on whether or not the length is calculated by input or by summing all the lengths of each Song
    @FXML
    private void handleToggleAction() {
        labelAlbumLength.setVisible(!toggleAddSongs.isSelected());
        textLength.setVisible(!toggleAddSongs.isSelected());
    }
}
