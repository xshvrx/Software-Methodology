package modules.controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import models.*;
import modules.utils.Utility;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class SearchPageController implements Initializable {
    @FXML
    Button logoutButton;
    @FXML
    Button searchButton;
    @FXML
    Button createResultAlbum;
    @FXML
    Button clearSelectionButton;
    @FXML
    Button goBackButton;

    @FXML
    ChoiceBox<String> tagFilter;
    @FXML
    ChoiceBox<String> valueFilter;

    @FXML
    DatePicker startDatePicker;
    @FXML
    DatePicker endDatePicker;

    @FXML
    FlowPane resultPane;

    List<Photo> retPhotoList = new ArrayList<Photo>();

    /**
     * @param location
     * @param resources
     */
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        resetSearchPage();
        return;
    }

    
    /** 
     * @param event
     */
    public void search(ActionEvent event) {
        User currentUser = Utility.getCurrentUser();
        List<Photo> photos = currentUser.getPhotos();
        List<Photo> filteredPhotos = new ArrayList<Photo>();

        if (startDatePicker.getValue() == null && endDatePicker == null) {
            filteredPhotos = photos;
        } else if (startDatePicker.getValue() == null) {
            Date endDate = Date.valueOf(endDatePicker.getValue());
            filteredPhotos = photos.stream().filter(p -> p.getDate().before(endDate)).collect(Collectors.toList());
        } else if (endDatePicker.getValue() == null) {
            Date startDate = Date.valueOf(startDatePicker.getValue());
            filteredPhotos = photos.stream().filter(p -> p.getDate().after(startDate)).collect(Collectors.toList());
        } else {
            Date startDate = Date.valueOf(startDatePicker.getValue());
            Date endDate = Date.valueOf(endDatePicker.getValue());
            filteredPhotos = photos.stream().filter(p -> p.getDate().after(startDate) && p.getDate().before(endDate)).collect(Collectors.toList());
        }

        if (tagFilter.getValue() != null && valueFilter.getValue() != null) {
            List<Photo> temp = new ArrayList<Photo>();
            for (Photo photo : filteredPhotos) {
                for (Tag tag : photo.getTags()) {
                    if (tag.getName().equals(tagFilter.getValue()) && tag.getValue().equals(valueFilter.getValue())) {
                        temp.add(photo);
                    }
                }
            }
            filteredPhotos = temp;
        }

        resultPane.getChildren().clear();

        if (filteredPhotos.size() == 0) {
            Utility.sendAlert(AlertType.INFORMATION, "No Results", null, "No photos match the search criteria.");
            return;
        }

        for (Photo photo : filteredPhotos) {
            if (resultPane.getChildren().stream().anyMatch(node -> node.getId().equals(photo.getFile().getAbsolutePath()))) {
                continue;
            }

            ImageView image = new ImageView(photo.getFile().toURI().toString());
            image.setFitHeight(175);
            image.setFitWidth(175);
            image.setId(photo.getFile().getAbsolutePath());
            resultPane.getChildren().add(image);
            retPhotoList.add(photo);
        }
    }
    
    /** 
     * @param event
     * @throws IOException
     */
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


    
    /** 
     * @param event
     */
    public void clear(ActionEvent event) {
        resetSearchPage();
    }


    public void resetSearchPage() {
        resultPane.getChildren().clear();

        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        tagFilter.getItems().clear();
        valueFilter.getItems().clear();

        User currentUser = Utility.getCurrentUser();
        List<Photo> photos = currentUser.getPhotos();
        List<Tag> tags = new ArrayList<Tag>();
        retPhotoList.clear();

        for (Photo photo : photos) {
            for (Tag tag : photo.getTags()) {
                if (!tags.contains(tag)) {
                    tags.add(tag);
                }
            }
        }

        if (tags.size() == 0) {
            tagFilter.disableProperty().set(true);
            valueFilter.disableProperty().set(true);

            return;
        }

        for (Tag tag : tags) {
            List<String> tagItems = tagFilter.getItems();
            List<String> valueItems = valueFilter.getItems();

            if (!tagItems.contains(tag.getName())) {
                tagItems.add(tag.getName());
            }

            if (!valueItems.contains(tag.getValue())) {
                valueItems.add(tag.getValue());
            }
        }
    }

    public void createAlbumWithResults(ActionEvent event) throws IOException {
        if (resultPane.getChildren().size() == 0) {
            Utility.sendAlert(AlertType.ERROR, "No Results", null, "No photos to create album with.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setTitle("Create Album");
        dialog.setContentText("Enter album name:");

        dialog.showAndWait();

        User currentUser = Utility.getCurrentUser();
        String albumName = dialog.getResult();

        if (albumName == null || albumName.equals("")) {
            return;
        }
        
        List<Album> albums = currentUser.getAlbums();
        for (Album a : albums) {
            if (a.getName().equals(albumName)) {
                Utility.sendAlert(AlertType.ERROR, "Error", null, "Album already exists.");
                return;
            }
        }

        AlbumList albumList = new AlbumList();
        Album album = new Album(albumName, currentUser.getID());

        for (Photo photo : retPhotoList) {
            Photo newPhoto = Photo.deepCopy(photo);
            album.addPhoto(newPhoto);
        }

        albumList.addAlbum(album);
        
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Utility.changeScene("UserSubsystem.fxml", stage);

        Utility.sendAlert(AlertType.INFORMATION, "Success", null, "Album created successfully.");
    }

}