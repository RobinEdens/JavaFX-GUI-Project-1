package Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  Controller for Change.fxml, which is called on after a valid row is selected from Edit.fxml. Autopopulates
    - the TextFields with the values of the selected Album and creates a new Album based on any changes, which overwrites the
    - passed album in the Inventory class
 */
public class ChangeController extends Subcontroller {

    Album changeAlbum;
    private boolean noSongs;
    int songIndex;

    @FXML Button btnChange;
    @FXML TextField textBandName;
    @FXML TextField textAlbumName;
    @FXML TextField textPublisher;
    @FXML DatePicker dateAlbumReleased;

    @FXML TextField textLength;
    @FXML Text labelALbumLength;

    public ChangeController(MainController main) {
        super(main);
        super.loader = "Change.fxml";
    }
    public ChangeController(){
        super();
        super.loader = "Change.fxml";
    }

    public boolean hasNoSongs() {
        return noSongs;
    }

    // Check to see if user entered songs or if it was a default value for preventing OoB access
    public void setNoSongs(boolean noSongs) {
        this.noSongs = noSongs;
    }

    // Prefills the TextFields with the Album that is being edited
    public void setScene(int index) {
        try {
            this.songIndex = index;
            this.changeAlbum = this.invAlbum.getAlbumInventory().get(songIndex);
            String checkDefault = this.changeAlbum.getTrackList().get(0).getName();
            if (checkDefault.equals("None Specified")) {
                this.setNoSongs(true);
            }
            textBandName.setText(this.changeAlbum.getBandName());
            textAlbumName.setText(this.changeAlbum.getAlbumName());
            textPublisher.setText(this.changeAlbum.getPublisher());
            dateAlbumReleased.setValue(this.changeAlbum.getDatePublished());

            if (this.hasNoSongs()) {
                textLength.setText(String.valueOf(this.changeAlbum.getTrackList().get(0).getLength()));
            }
            textLength.setVisible(this.hasNoSongs());
            labelALbumLength.setVisible(this.hasNoSongs());
        } catch (IndexOutOfBoundsException e) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Unable to select item");
            err.setContentText("Item was unable to be selected. This usually happens when Song values are null" +
                    "due to closing out of the Song window when manually listing songs or when change/delete is" +
                    "pressed without any selection" +
                    "\nGoing back to the main menu and coming back usually fixes the issue but I'd recommend deleting current songs");
            err.showAndWait();
        }
    }

    // Checks if songs were entered by user or if prefilled and changes whether or not the length can be edited based on that
    // (since the length is autodetermined by song length if songs are entered)
    @FXML
    private void handleChangeButtonAction() throws IOException {
        changeAlbum.setBandName(textBandName.getText());
        changeAlbum.setAlbumName(textAlbumName.getText());
        changeAlbum.setPublisher(textPublisher.getText());
        changeAlbum.setDatePublished(dateAlbumReleased.getValue());
        if (hasNoSongs()) {
            try {
                Song tmpSong = new Song("None Specified", Double.parseDouble(textLength.getText()));
                changeAlbum.getTrackList().set(0, tmpSong);
            } catch (NumberFormatException e) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setTitle("String inputted");
                err.setContentText("Integers only for # of Songs/Album Length");
                err.showAndWait();
            }
        }
        this.getInvAlbum().change(songIndex, changeAlbum);
        Stage stage = (Stage) textLength.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader((getClass().getResource("Edit.fxml")));
        this.loadScene(stage, loader, this);
    }
}
