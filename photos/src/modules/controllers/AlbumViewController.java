package modules.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import models.Album;
import models.Photo;
import modules.utils.Utility;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class AlbumViewController implements Initializable {
    @FXML
    Text windowTitle;

    @FXML
    Button searchButton;
    @FXML
    Button logoutButton;
    @FXML
    Button addPhotoButton;
    @FXML
    Button goBackButton;

    @FXML
    FlowPane photosPane;
    @FXML
    VBox verticalBox;
    @FXML
    AnchorPane anchorPane;

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        refresh();

        verticalBox.widthProperty().addListener((obs, oldVal, newVal) -> {
            anchorPane.setPrefWidth(newVal.doubleValue());
        });
        verticalBox.heightProperty().addListener((obs, oldVal, newVal) -> {
            anchorPane.setPrefHeight(newVal.doubleValue());
        });

        return;
    }


    public void refresh() {
        Album album = Utility.getCurrentAlbum();
        List<Photo> photos = album.getPhotos();

        photosPane.getChildren().clear();

        List<Photo> sortedPhotos = photos.stream().sorted(Comparator.comparing(Photo::getName))
                .collect(Collectors.toList());

        for (Photo photo : sortedPhotos) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/modules/fxml/PhotoCover.fxml"));

                AnchorPane photoCover = loader.load();
                PhotoCoverController controller = loader.getController();
                controller.setPhoto(photo);
                controller.setParentController(this);

                photosPane.getChildren().add(photoCover);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        windowTitle.setText("Album Photos (" + album.getName() + ")");
    }

    
    /** 
     * @param event
     * @throws IOException
     */
    // Adds a new empty album to the list of albums
    public void addPhoto(ActionEvent event) throws IOException {
        // Create a FileChooser object
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select Image");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            Album album = Utility.getCurrentAlbum();
            album.addPhoto(new Photo(album.getID(), selectedFile));

            Utility.sendAlert(AlertType.INFORMATION, "Photo Added", null, "Photo added to album successfully.");
        }

        refresh();
        return;
    }

    
    /** 
     * @param event
     * @throws IOException
     */
    public void searchBar(ActionEvent event) throws IOException {
        Utility.changeScene("SearchPage.fxml", event);
    }

    
    /** 
     * @param event
     * @throws IOException
     */
    // Logs out of the current user
    public void logout(ActionEvent event) throws IOException {
        Utility.setCurrentUser(null);
        Utility.changeScene("LoginPage.fxml", event);
    }


    
    /** 
     * @param event
     * @throws IOException
     */
    public void goBack(ActionEvent event) throws IOException {
        Utility.changeScene("UserSubsystem.fxml", event);
    }
}
