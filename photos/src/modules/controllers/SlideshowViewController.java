package modules.controllers;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Album;
import models.Photo;
import modules.utils.Utility;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class SlideshowViewController implements Initializable {
    @FXML
    Text windowTitle;
    @FXML
    Text imageProgression;

    @FXML
    Button logoutButton;
    @FXML
    Button goBackButton;

    @FXML
    ImageView previousArrowButton;
    @FXML
    ImageView nextArrowButton;
    @FXML
    ImageView imageView;

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        refresh();

        previousArrowButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                previousArrowButton.setOpacity(0.5);
            } else {
                previousArrowButton.setOpacity(1);
            }
        });

        nextArrowButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                nextArrowButton.setOpacity(0.5);
            } else {
                nextArrowButton.setOpacity(1);
            }
        });

        return;
    }


    public void refresh() {
        Album album = Utility.getCurrentAlbum();
        List<Photo> photos = album.getPhotos();
        List<Photo> sortedPhotos = photos.stream().sorted(Comparator.comparing(Photo::getName))
                .collect(Collectors.toList());

        for (Photo photo : sortedPhotos) {
            if (photo.getID().equals(Utility.getCurrentPhoto().getID())) {
                imageProgression.setText((sortedPhotos.indexOf(photo) + 1) + "/" + sortedPhotos.size());
                imageView.setImage(new Image(photo.getFile().toURI().toString()));
                windowTitle.setText("Album Photos (" + photo.getName() + ")");
            }
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
        Utility.setCurrentPhoto(null);
        Utility.changeScene("AlbumView.fxml", event);
    }


    public void previousImage() {
        Album album = Utility.getCurrentAlbum();
        List<Photo> photos = album.getPhotos();
        List<Photo> sortedPhotos = photos.stream().sorted(Comparator.comparing(Photo::getName))
                .collect(Collectors.toList());

        for (Photo photo : sortedPhotos) {
            if (photo.getID().equals(Utility.getCurrentPhoto().getID())) {
                int currentIndex = sortedPhotos.indexOf(photo);

                if (currentIndex == 0) {
                    currentIndex = sortedPhotos.size();
                }

                int previousIndex = (currentIndex - 1) % sortedPhotos.size();
                Utility.setCurrentPhoto(sortedPhotos.get(previousIndex));

                break;
            }
        }

        refresh();
    }


    public void nextImage() {
        Album album = Utility.getCurrentAlbum();
        List<Photo> photos = album.getPhotos();
        List<Photo> sortedPhotos = photos.stream().sorted(Comparator.comparing(Photo::getName))
                .collect(Collectors.toList());

        for (Photo photo : sortedPhotos) {
            if (photo.getID().equals(Utility.getCurrentPhoto().getID())) {
                int currentIndex = sortedPhotos.indexOf(photo);

                if (currentIndex == sortedPhotos.size() - 1) {
                    currentIndex = -1;
                }

                int nextIndex = (currentIndex + 1) % sortedPhotos.size();
                Utility.setCurrentPhoto(sortedPhotos.get(nextIndex));

                break;
            }
        }

        refresh();
    }
}
