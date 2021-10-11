package Application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  Primary subclass of Controller, in which is used as the superclass for every Controller that is not
    - MainController, primarily due to the need to pass the Controllers and attributes in between back and forth to get around
    - each scene going to a new scene (I could not figure out how swap to a scene already in memory with my current knowledge)
 */
public class Subcontroller extends Controller {
    public MainController main;
    public Scene scene;
    @FXML Button menuBack;

    public Subcontroller(MainController main) {
        super();
        this.setController(main);
        menuBack = new Button();
        this.scene = menuBack.getScene();
        this.invAlbum = new Inventory();
    }
    public Subcontroller(){
        super();
        this.main = new MainController();
        menuBack = new Button();
        this.scene = menuBack.getScene();
        this.invAlbum = new Inventory();
    }

    public void setController(MainController main) {
        this.main = main;
    }

    // If statement to handle the single time a back button call does not take one back to the Main Scene
    @FXML
    public void handleWindowButtonAction() throws IOException {
        if (this.getClass().getName().equals("Application.ChangeController")){
            Stage stage = (Stage) menuBack.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Edit.fxml"));
            this.loadScene(stage, loader, this);
        } else {
            Stage stage = (Stage) menuBack.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            this.loadScene(stage, loader, this);
        }
    }
}
