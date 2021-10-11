package Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  Controller for Print.fxml, handles the TableView of all fields of a new song, and allows to iterate the Song array
    - by dynamic buttons added into the table on initialization.
    -
    - As seen in EditController as well, I was having issues where the first time a scene would load, the indexes of the Buttons
    - would always be one more than it should be, which is why there's the checks for both indexOoB and the isFirstLoop() as a
    - quick way to get around it since I was already running short on time
 */
public class PrintController extends Subcontroller {
    @FXML TableView<Album> tblAlbum;
    @FXML TableColumn<Album, String> tcBandName;
    @FXML TableColumn<Album, String> tcAlbumName;
    @FXML TableColumn<Album, String> tcPublisher;
    @FXML TableColumn<Album, LocalDate> tcDate;
    @FXML TableColumn<Album, String> tcLength;
    @FXML TableColumn<Album, Album> tcSongs;

    @FXML Button btnPrint;
    @FXML Text totalAlbums;

    private boolean isFirstLoop;
    List<Button> btnSongs = new ArrayList<>();

    public PrintController(MainController main) {
        super(main);
        super.loader = "Print.fxml";
        this.setFirstLoop(true);
    }
    public PrintController(){
        super();
        super.loader = "Print.fxml";
        this.setFirstLoop(true);
    }

    // Show how many Albums have been added so far by getting value of static attribute of Class
    public void initialize() {
        super.initialize();
        totalAlbums.setText(totalAlbums.getText() + Album.getTotalAlbums());
    }

    public boolean isFirstLoop() {
        return isFirstLoop;
    }

    public void setFirstLoop(boolean firstLoop) {
        isFirstLoop = firstLoop;
    }


    // Populates the table and creates the buttons to get list of Songs
    @FXML
    private void setTable() {
        btnSongs.clear();
        tcBandName.setCellValueFactory(
                new PropertyValueFactory<>("bandName")
        );
        tcAlbumName.setCellValueFactory(
                new PropertyValueFactory<>("albumName")
        );
        tcDate.setCellValueFactory(
                new PropertyValueFactory<>("datePublished")
        );
        tcLength.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().length())));
        tcPublisher.setCellValueFactory(
                new PropertyValueFactory<>("publisher")
        );

        ObservableList<Album> obsAlbum = FXCollections.observableArrayList(getInvAlbum().getAlbumInventory());
        tblAlbum.setItems(obsAlbum);
        tblAlbum.setEditable(true);
        tcBandName.setCellFactory(TextFieldTableCell.forTableColumn());
        tcAlbumName.setCellFactory(TextFieldTableCell.forTableColumn());
        tcDate.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        tcLength.setCellFactory(TextFieldTableCell.forTableColumn());
        tcPublisher.setCellFactory(TextFieldTableCell.forTableColumn());

        // Create buttons for each row  and add to ArrayList which works in parallel with Inventory for retrieval of row.
        tcSongs.setCellFactory(c ->  {
            Button btnSong = new Button("Songs");
            this.btnSongs.add(btnSong);
            btnSong.setOnAction(event -> this.onButtonPress(event));
            TableCell<Album, Album> cell = new TableCell<>() {
                @Override
                public void updateItem(Album album, boolean empty ) {
                    super.updateItem(album, empty);
                    if (empty) {
                        setGraphic(null);
                    }
                    else {
                        setGraphic(btnSong);
                    }
                }
            };
            return cell;});
        tblAlbum.setEditable(false);
    }

    // used as a workaround to issue noted above
    private void clear() {
        this.setFirstLoop(false);
        this.btnSongs.clear();
        this.setTable();
    }

    // If possible, open a PrintSongs window with list of Songs. Couldnt get it to load in SongController so it is technically incomplete
    // even though it does what it needs to, SongController just wouldnt fill its TextArea
    private void onButtonPress (ActionEvent event) {
        try {
            Stage songPopup = new  Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PrintSongs.fxml"));
            Parent root = loader.load();
            SongController control = loader.getController();
            control.setInvAlbum(this.getInvAlbum());
            Scene scene = new Scene(root);
            songPopup.setTitle("Songs on Album");
            songPopup.setScene(scene);

            int tmpIndex = this.btnSongs.indexOf(event.getSource());
            boolean validIndex = ((tmpIndex >= 0) && (tmpIndex < this.btnSongs.size()));

            if (validIndex) {
                int albumIndex = this.btnSongs.indexOf(event.getSource());
                if (isFirstLoop()) {
                    albumIndex--;
                    setFirstLoop(false);
                }
                Album tmpAlbum = this.getInvAlbum().getAlbumInventory().get(albumIndex);
                control.setColor(this.getColor());
                control.applyColor();
                songPopup.show();
                control.populate(tmpAlbum);
            }
            this.clear();
        } catch (IOException exception) {
            err.setTitle("Unable to get Songs");
            err.setContentText("Unable to get song list. The selected Album might be affected");
            err.showAndWait();
        // Didnt happen for me after the validIndex check above but kept it in just to be safe
        } catch (IndexOutOfBoundsException exception) {
            err.setTitle("Whoops");
            err.setContentText("Error with getting indexes of TableView to match with Array. This usually only happens once. Try again and it should work");
            err.showAndWait();
        }
    }
}