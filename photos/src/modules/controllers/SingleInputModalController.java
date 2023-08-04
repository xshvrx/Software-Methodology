package modules.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Album;
import models.AlbumList;
import models.User;
import modules.utils.Utility;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class SingleInputModalController implements Initializable {
    @FXML
    AnchorPane modalPane;
    @FXML
    TextField firstInputField;

    @FXML
    Text firstInputTitle;
    @FXML
    Text modalTitle;
    @FXML
    Text modalDescription;

    @FXML
    Button cancelButton;
    @FXML
    Button confirmButton;

    private Object parentController;

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        return;
    }

    
    /** 
     * @param stage
     */
    public void setStage(Stage stage) {
        stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        stage.initModality(Modality.WINDOW_MODAL);

        javafx.scene.Scene scene = stage.getScene();
        scene.setFill(Color.TRANSPARENT);

    }

    
    /** 
     * @param parentController
     */
    public <T> void setParentController(T parentController) {
        this.parentController = parentController;
    }


    
    /** 
     * @param title
     */
    public void setFirstInputTitle(String title) {
        firstInputTitle.setText(title);
    }


    
    /** 
     * @param title
     */
    public void setModalTitle(String title) {
        modalTitle.setText(title);
    }


    public void setModalDescription(String description) {
        modalDescription.setText(description);
    }


    public void setCancelButtonLabel(String label) {
        cancelButton.setText(label);
    }


    public void setConfirmButtonLabel(String label) {
        confirmButton.setText(label);
    }


    public String getFirstInput() {
        return firstInputField.getText();
    }


    public void setFirstInput(String input) {
        firstInputField.setText(input);
    }


    public void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    public void confirm(ActionEvent event) throws IOException {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();

        if (modalTitle.getText().equals("Album Name")) {
            User user = Utility.getCurrentUser();

            String albumName = firstInputField.getText().trim();
            if (albumName.equals("")) {
                Utility.sendAlert(AlertType.ERROR, "Error", null, "Album name cannot be empty.");
                return;
            }

            AlbumList albumList = new AlbumList();
            Album album = new Album(albumName, user.getID());
            albumList.addAlbum(album);

            ((UserSubsystemController) parentController).refresh();
        }
    }
}
