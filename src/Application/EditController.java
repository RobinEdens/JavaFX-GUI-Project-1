package Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  EditController for the Edit.fxml scene. Handles making sure a valid checkbox/radio button is selected
    - for any deletion or edits to the Inventory
    -
    - Same issue as noted in PrintController with the TableView, every time the scene would load for the first time, the index would
    - include the header, which would throw the count off, which is why the use of isFirstLoop is done since I didnt have time for
    - any further troubleshooting.
 */
public class EditController extends Subcontroller {
    private Subcontroller subChagne;
    ToggleGroup groupRadio = new ToggleGroup();
    List<CheckBox> checkList = new ArrayList<>();
    List<RadioButton> radioList= new ArrayList<>();


    private boolean isFirstLoop;
    private boolean hasChange;

    private int changeIndex;

    @FXML TableView<Album> tblAlbum;
    @FXML TableColumn<Album, String> tcBandName;
    @FXML TableColumn<Album, String> tcAlbumName;
    @FXML TableColumn<Album, Album> tcChange;
    @FXML TableColumn<Album, Album> tcDelete;

    @FXML Button btnDelete;
    @FXML Button btnChange;

    public EditController(MainController main) {
        super(main);
        super.loader = "Edit.fxml";
        this.setFirstLoop(true);
        this.setHasChange(false);
        subChagne = new ChangeController(main);
    }
    public EditController(){
        super();
        super.loader = "Edit.fxml";
        this.setFirstLoop(true);
        this.setHasChange(false);
        subChagne = new ChangeController();
    }

    // Boolean value of whether or not a Radio button is active
    public boolean isHasChange() {
        return hasChange;
    }

    public void setHasChange(boolean hasChange) {
        this.hasChange = hasChange;
    }

    // returns index of where the Album should be stored
    public int getChangeIndex() {
        return changeIndex;
    }

    public void setChangeIndex(int changeIndex) {
        this.changeIndex = changeIndex;
    }

    // mentioned earlier in regards to TableView bug
    public boolean isFirstLoop() {
        return isFirstLoop;
    }

    public void setFirstLoop(boolean firstLoop) {
        isFirstLoop = firstLoop;
    }

    // Populates TableView
    public void setTable() {
        tcBandName.setCellValueFactory(
                new PropertyValueFactory<>("bandName")
        );
        tcAlbumName.setCellValueFactory(
                new PropertyValueFactory<>("albumName")
        );

        ObservableList<Album> obsAlbum = FXCollections.observableArrayList(getInvAlbum().getAlbumInventory());
        tblAlbum.setItems(obsAlbum);
        tblAlbum.setEditable(true);
        tcBandName.setCellFactory(TextFieldTableCell.forTableColumn());
        tcAlbumName.setCellFactory(TextFieldTableCell.forTableColumn());
        // Sets a column full of the radio buttons used for editing an Album
        tcChange.setCellFactory(c ->  {
            RadioButton radioChange = new RadioButton("Change");

            TableCell<Album, Album> cell = new TableCell<>() {
                @Override
                public void updateItem(Album album, boolean empty ) {
                    super.updateItem(album, empty);
                    radioChange.setToggleGroup(groupRadio);
                    if (empty) {
                        setGraphic(null);
                    }
                    else {
                        setGraphic(radioChange);
                    }
                }
            };
            radioList.add(radioChange);
            return cell;});

        // Sets a column full of checkboxes for deleting one or more Albums from the Inventory
        tcDelete.setCellFactory(c ->  {
            CheckBox chkDelete = new CheckBox("Delete");
            checkList.add(chkDelete);
            TableCell<Album, Album> cell = new TableCell<>() {
                @Override
                public void updateItem(Album album, boolean empty ) {
                    super.updateItem(album, empty);
                    if (empty) {
                        setGraphic(null);
                    }
                    else {
                        setGraphic(chkDelete);
                    }
                }
            };
            return cell;});
        tblAlbum.setEditable(false);
    }

    // Refreshes the value to prevent the checklist and radiolist values from getting too big, causing OoB issues with index selection
    public void refreshLab() {
        checkList.clear();
        radioList.clear();

        tblAlbum.getItems().clear();
        tblAlbum.refresh();
        this.setTable();
    }

    // Deletes every item with a checkbox next to it and throws an error if nothing is checked
    @FXML
    private void handleDeleteButtonAction() {
        boolean isChecked = false;

        for (CheckBox check: checkList) {
            isChecked = check.isSelected();
            if (isChecked) break;
        }
        if (isChecked) {
            int iter = 0;

            // Workaround to make sure the Checkboxes are deleted
            for (CheckBox check: checkList) {

            /* For some reason on the instantiation of this class, the first time it would delete loops, it would always be
                one index behind but work fine after. This worked as a quick workaround
             */
                if (this.isFirstLoop()) {
                    this.setFirstLoop(false);
                    continue;
                }
                if (check.isSelected() && iter < this.getInvAlbum().getAlbumInventory().size()) {
                    this.getInvAlbum().delete(iter);

                    // Stops iter increment to accommodate for deleted item and -1 index
                    continue;
                }
                iter++;
            }

        } else {
        err.setTitle("Nothing selected");
        err.setContentText("You did not select anything to delete");
        err.showAndWait();
        }
        this.refreshLab();
    }

    // Verifies that a radio button is toggled and if so, opens a Change.fxml to change the selected Album values
    @FXML
    private void handleChangeButtonAction() throws IOException {
        if (groupRadio.getSelectedToggle() != null) {
            Stage stage = (Stage) btnChange.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(subChagne.getLoader()));
            this.setChangeIndex(radioList.indexOf(groupRadio.getSelectedToggle()) - 1);
            this.setHasChange(true);
            this.loadScene(stage, loader, this);
            this.setHasChange(false);
        } else {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("No Item Selected");
            err.setContentText("You did not select an Album to change");
            err.showAndWait();
        }
        this.refreshLab();
    }
}