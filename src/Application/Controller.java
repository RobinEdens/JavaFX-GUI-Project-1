package Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  Superclass of every other Controller, how i was able to to allow the passing of the Inventory between each Controller without
    - a new Controller instance causing the Inventory to be lost. Also allows for the dynamic colors in each scene.
 */
public class Controller {
    @FXML BorderPane pane;
    String color;
    Alert err;

    protected Inventory invAlbum;
    public String loader;

    public Controller(){
        this.color = "-fx-background-color: GRAY";
        this.err = new Alert(Alert.AlertType.ERROR);
    }

    public void initialize () {
        this.applyColor();
    }

    public void applyColor() {
        this.pane.setStyle(color);
    }

    // Primary way that the Controller's pass colors around
    public void setColor (String color ) {
        this.color = color;
    }

    // Overloaded method that parses a java Color object into CSS, which is how the colors are being applied to the Scene
    // Is used to parse the ColorPicker result
    public void setColor (Color color) {
        this.color = "-fx-background-color: rgb(" + color.getRed() *255 + ", " + color.getGreen()*255 + ", " + color.getBlue()*255 + ");";
    }

    public String getColor(){
        return this.color;
    }

    public void setInvAlbum(Inventory invAlbum) {
        this.invAlbum = invAlbum;
    }

    public Inventory getInvAlbum() {
        return invAlbum;
    }

    public String getLoader(){
        return this.loader;
    }

    // Controller method to change scenes between all the other subclasses. A handful of if statements thrown in to handles
    // rare exceptions that do not effect every other Controller/Subcontroller
    protected void loadScene(Stage stage, FXMLLoader loader, Controller previous) throws IOException {
        Scene scene;  Parent root; Controller control;
        root = loader.load();
        control = loader.getController();
        control.setInvAlbum(this.getInvAlbum());
        if (previous.getClass().getName().equals("Application.MainController")) {
           ((Subcontroller)control).setController((MainController)previous);
        }
        if (control.getClass().getName().equals("Application.EditController")){
            ((EditController)control).setTable();
        }
        if (previous.getClass().getName().equals("Application.EditController")
            && control.getClass().getName().equals("Application.ChangeController")) {
            if (((EditController)previous).isHasChange()) {
                ((ChangeController)control).setScene(((EditController)previous).getChangeIndex());
            }
        }
        control.setColor(this.getColor());
        control.applyColor();
        scene = new Scene(root); stage.setScene(scene);
    }
}
