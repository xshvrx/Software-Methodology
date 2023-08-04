package modules.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Album;
import models.Photo;
import models.PhotoList;
import models.User;
import modules.utils.Utility;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class PhotoCoverController implements Initializable {
    @FXML
    ImageView photoCover;

    @FXML
    Text photoCaption;
    @FXML
    Text photoName;

    @FXML
    MenuItem editPhotoButton;
    @FXML
    MenuItem deletePhotoButton;
    @FXML
    MenuItem copyPhotoButton;
    @FXML
    MenuItem movePhotoButton;

    private AlbumViewController parentController;
    Photo currentPhoto;

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        photoCover.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                photoCover.setEffect(new BoxBlur(1, 1, 1));
                photoCover.setOpacity(0.5);
            } else {
                photoCover.setEffect(null);
                photoCover.setOpacity(1);
            }
        });

        return;
    }

    
    /** 
     * @param parentController
     */
    public void setParentController(AlbumViewController parentController) {
        this.parentController = parentController;
    }

    
    /** 
     * @param photo
     */
    public void setPhoto(models.Photo photo) {
        photoName.setText(photo.getName().substring(0, Math.min(photo.getName().length(), 17))
                + (photo.getName().length() > 17 ? "..." : ""));
        photoCaption.setText((photo.getName().substring(0, Math.min(photo.getName().length(), 17))
                + (photo.getName().length() > 17 ? "..." : "")) + " - " + photo.getDate().toString());

        photoCover.setImage(new Image(photo.getFile().toURI().toString()));

        currentPhoto = photo;
        return;
    }


    
    /** 
     * @throws IOException
     */
    public void editPhoto() throws IOException {
        Utility.setCurrentPhoto(currentPhoto);

        Stage stage = (Stage) photoCover.getScene().getWindow();
        Utility.changeScene("EditPhoto.fxml", stage);
    }


    
    /** 
     * @throws IOException
     */
    public void openSlideshow() throws IOException {
        Utility.setCurrentPhoto(currentPhoto);

        Stage stage = (Stage) photoCover.getScene().getWindow();
        Utility.changeScene("SlideshowView.fxml", stage);
    }


    public void deletePhoto() throws IOException {
        PhotoList currentPhotoList = new PhotoList();
        currentPhotoList.deletePhoto(currentPhoto.getID());

        Utility.sendAlert(AlertType.INFORMATION, "Success!", null, "Successfully deleted photo!");

        parentController.refresh();
    }


    public void copyPhoto() throws IOException {
        User user = Utility.getCurrentUser();
        List<String> userAlbums = user.getAlbums().stream().map(a -> a.getName()).collect(Collectors.toList());

        ChoiceDialog<String> dialog = new ChoiceDialog<>(userAlbums.get(0), userAlbums);
        dialog.setHeaderText(null);
        dialog.setTitle("Copy Photo");
        dialog.setContentText("Choose an album to copy the photo to:");

        dialog.showAndWait().ifPresent(albumName -> {
            Album album = user.getAlbums().stream().filter(a -> a.getName().equals(albumName)).findFirst().get();

            if (album.getPhotos().stream()
                    .anyMatch(p -> p.getFile().getAbsolutePath().equals(currentPhoto.getFile().getAbsolutePath()))) {
                Utility.sendAlert(AlertType.ERROR, "Error!", null, "Photo is already in this album!");
                return;
            }

            Photo newPhoto = Photo.deepCopy(currentPhoto);

            try {
                album.addPhoto(newPhoto);

                Utility.sendAlert(AlertType.INFORMATION, "Success!", null, "Successfully copied photo!");
            } catch (IOException e) {
                e.printStackTrace();

                Utility.sendAlert(AlertType.ERROR, "Error!", null, "An error occured while copying the photo!");
            }
        });

        parentController.refresh();
    }


    public void movePhoto() throws IOException {
        User user = Utility.getCurrentUser();
        List<String> userAlbums = user.getAlbums().stream().map(a -> a.getName()).collect(Collectors.toList());

        ChoiceDialog<String> dialog = new ChoiceDialog<>(userAlbums.get(0), userAlbums);
        dialog.setHeaderText(null);
        dialog.setTitle("Move Photo");
        dialog.setContentText("Choose an album to move the photo to:");

        dialog.showAndWait().ifPresent(albumName -> {
            Album album = user.getAlbums().stream().filter(a -> a.getName().equals(albumName)).findFirst().get();

            if (album.getPhotos().stream()
                    .anyMatch(p -> p.getFile().getAbsolutePath().equals(currentPhoto.getFile().getAbsolutePath()))) {
                Utility.sendAlert(AlertType.ERROR, "Error!", null, "Photo is already in this album!");
                return;
            }

            Photo newPhoto = Photo.deepCopy(currentPhoto);

            try {
                album.addPhoto(newPhoto);
                PhotoList currentPhotoList = new PhotoList();
                currentPhotoList.deletePhoto(currentPhoto.getID());

                Utility.sendAlert(AlertType.INFORMATION, "Success!", null, "Successfully moved photo!");
            } catch (IOException e) {
                e.printStackTrace();

                Utility.sendAlert(AlertType.ERROR, "Error!", null, "An error occured while moving the photo!");
            }
        });

        parentController.refresh();
    }
}
