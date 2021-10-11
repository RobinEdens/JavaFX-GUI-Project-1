package Application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  MainController for the Main.fxml file, Primary way that I kept the scenes connected was making sure that
    - the MainController and Subcontrollers all had an equivalent has-a relationship to make sure they would not lose any
    - data on new Controller instantiation whenever the Scene changes
 */

public class MainController extends Controller implements Initializable {
    @FXML Button menuAdd;
    @FXML Button menuEdit;
    @FXML Button menuPrint;
    @FXML Button menuQuit;
    @FXML Button btnColor;

    @FXML ColorPicker colorPick;
    private Subcontroller subAdd, subEdit, subPrint;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize();
        subAdd = new AddController(this);
        subEdit = new EditController(this);
        subPrint = new PrintController(this);
        this.invAlbum = new Inventory();
    }

    // Handles menu selection
    @FXML
    public void handleWindowButtonAction(ActionEvent event) throws IOException {
        Stage stage; FXMLLoader loader;

        if (event.getSource() == btnColor) {
            this.setColor(colorPick.getValue());
            this.applyColor();
        }
        if (event.getSource() == menuAdd) {
            stage = (Stage) menuAdd.getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource(subAdd.getLoader()));
            this.loadScene(stage, loader, this);
        }

        if (event.getSource() == menuEdit) {
            stage = (Stage) menuEdit.getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource(subEdit.getLoader()));
            this.loadScene(stage, loader, this);
        }

        if (event.getSource() == menuPrint) {
            stage = (Stage) menuPrint.getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource(subPrint.getLoader()));
            this.loadScene(stage, loader, this);
        }
        if (event.getSource() == menuQuit) {
            Platform.exit();
            System.exit(0);
        }
    }
}
