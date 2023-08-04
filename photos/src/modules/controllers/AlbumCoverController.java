package modules.controllers;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Album;
import models.AlbumList;
import modules.utils.Utility;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class AlbumCoverController implements Initializable {
    @FXML
    Text albumName;
    @FXML
    Text photoCount;

    @FXML
    MenuItem renameAlbumButton;
    @FXML
    MenuItem deleteAlbumButton;

    @FXML
    HBox albumCoverPanel;

    private UserSubsystemController parentController;
    Album currentAlbum;

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        albumCoverPanel.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                albumCoverPanel.setStyle("-fx-background-color: #646B7A;-fx-background-radius: 25px;");
            } else {
                albumCoverPanel.setStyle("-fx-background-color: #4A5366;-fx-background-radius: 25px;");
            }
        });

        return;
    }

    
    /** 
     * @param parentController
     */
    public void setParentController(UserSubsystemController parentController) {
        this.parentController = parentController;
    }

    
    /** 
     * @param album
     */
    public void setAlbum(models.Album album) {
        albumName.setText(album.getName().substring(0, Math.min(album.getName().length(), 17))
                + (album.getName().length() > 17 ? "..." : ""));
        photoCount.setText(String.valueOf(album.getPhotos().size()) + " Photos");

        currentAlbum = album;
        return;
    }

    
    /** 
     * @param event
     * @throws IOException
     */
    public void openAlbum(MouseEvent event) throws IOException {
        Utility.setCurrentAlbum(currentAlbum);

        Stage stage = (Stage) albumCoverPanel.getScene().getWindow();
        Utility.changeScene("AlbumView.fxml", stage);

        return;
    }


    
    /** 
     * @param event
     * @throws IOException
     */
    public void renameAlbum(javafx.event.ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog(currentAlbum.getName());
        dialog.setHeaderText(null);
        dialog.setTitle("Rename Album");
        dialog.setContentText("Enter new name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newName = result.get();

            if (newName.isEmpty()) {
                Utility.sendAlert(AlertType.ERROR, "Error", null, "Album name cannot be empty");
                return;
            }

            AlbumList albumList = new AlbumList();
            albumList.editAlbum(currentAlbum.getID(), newName);

            Utility.sendAlert(AlertType.INFORMATION, "Album Renamed Successfully", null,
                    "Album renamed successfully to " + newName + ".");

            parentController.refresh();
        }

        return;

    }


    public void deleteAlbum(javafx.event.ActionEvent event) throws IOException {
        AlbumList albumList = new AlbumList();
        albumList.deleteAlbum(currentAlbum.getID());

        Utility.setCurrentAlbum(null);
        Utility.sendAlert(AlertType.INFORMATION, "Album Deleted Successfully", null, "Album deleted successfully.");

        parentController.refresh();
    }


    public void editAlbum(javafx.event.ActionEvent event) {
        return;
    }
}
