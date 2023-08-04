package application;

import java.io.Serializable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.UserList;

import java.util.prefs.Preferences;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class Photos extends Application implements Serializable {

    /**
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file to view application
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/modules/fxml/LoginPage.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Photo Album");
            primaryStage.show();

            Preferences session = Preferences.userRoot();
            session.clear();

            UserList users = new UserList();
            users.setUpUsers();
        } catch (Exception error) {
            // Catch exceptions to view tracebacks
            error.printStackTrace();
        }
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
