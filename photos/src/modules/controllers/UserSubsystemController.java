package modules.controllers;

import java.net.URL;
import models.Album;
import models.User;
import modules.utils.Utility;

import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class UserSubsystemController implements Initializable {
    @FXML
    private FlowPane albumCoversPane;
    @FXML
    VBox verticalBox;
    @FXML
    AnchorPane anchorPane;

    @FXML
    Button createAlbumButton;
    @FXML
    Button searchButton;
    @FXML
    Button logoutButton;

    @FXML
    Text windowTitle;

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        User user = Utility.getCurrentUser();
        List<Album> albums = user.getAlbums();

        albumCoversPane.getChildren().clear();

        List<Album> sortedAlbums = albums.stream().sorted(Comparator.comparing(Album::getName))
                .collect(Collectors.toList());

        for (Album album : sortedAlbums) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/modules/fxml/AlbumCover.fxml"));

                AnchorPane albumCover = loader.load();
                AlbumCoverController controller = loader.getController();
                controller.setAlbum(album);
                controller.setParentController(this);

                albumCoversPane.getChildren().add(albumCover);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        windowTitle.setText("User Albums (" + user.getUsername() + ")");
    }

    
    /** 
     * @param event
     * @throws IOException
     */
    public void logout(ActionEvent event) throws IOException {
        Utility.changeScene("LoginPage.fxml", event);
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
    public void createAlbum(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/modules/fxml/SingleInputModal.fxml"));

        javafx.scene.Parent root = loader.load();
        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        javafx.stage.Stage stage = new javafx.stage.Stage();

        stage.setScene(scene);
        SingleInputModalController controller = loader.getController();
        controller.setStage(stage);

        controller.setModalTitle("Album Name");
        controller.setModalDescription("Create a new album to store photos in.");
        controller.setFirstInputTitle("Album Name");
        controller.setFirstInput("Enter album name...");
        controller.setConfirmButtonLabel("Confirm");
        controller.setParentController(this);

        stage.showAndWait();
        return;
    }
}
