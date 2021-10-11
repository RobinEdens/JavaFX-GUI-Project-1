package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description: Main method for the first Big Project. It works as a mock inventory for a music store dealing 100% with Album sales.
    -
    - I went a bit overboard with the design process on this one so some of the documentation is a little lighter than I would have liked
    - due to time restraints
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Music Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
