/*
Rutgers University CS213 Software Methodology
Assignment 1 - Song Library Application
@author Zaeem Zahid
@author Shiv Patel
*/

package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SongLib extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file to view application
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/modules/SongLib.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Song Library");
            primaryStage.show();
        } catch (Exception error) {
            // Catch exceptions to view tracebacks
            error.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
