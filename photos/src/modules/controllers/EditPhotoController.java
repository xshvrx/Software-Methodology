package modules.controllers;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Photo;
import models.PhotoList;
import models.Tag;
import modules.utils.Utility;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class EditPhotoController implements Initializable {
    @FXML
    ListView<Tag> tagListView;

    @FXML
    Text windowTitle;
    @FXML
    Text photoPath;
    @FXML
    Text photoDate;

    @FXML
    TextField photoName;
    @FXML
    TextArea photoCaption;

    @FXML
    Button logoutButton;
    @FXML
    Button goBackButton;
    @FXML
    Button saveChangeButton;
    @FXML
    Button addTagButton;
    @FXML
    Button deleteTagButton;

    @FXML
    ImageView photoView;

    ObservableList<Tag> obTagList = FXCollections.observableArrayList();

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        refresh();

        if (!obTagList.isEmpty()) {
            tagListView.getSelectionModel().select(0);
        }

        tagListView.getSelectionModel().selectedItemProperty().addListener(this::selectionManager);

        return;
    }

    public void refresh() {
        PhotoList photoList = new PhotoList();
        Photo photo = photoList.getPhoto(Utility.getCurrentPhoto().getID());

        windowTitle.setText("Editing " + photo.getName());
        photoName.setText(photo.getName());

        String path = photo.getFile().getPath();
        path = path.replaceAll("\\\\", "/");
        photoPath.setText(path);

        photoDate.setText(photo.getDate().toString());

        photoCaption.setText(photo.getCaption());

        Image image = new Image(photo.getFile().toURI().toString());
        photoView.setImage(image);

        List<Tag> tags = photo.getTags().stream().sorted(Comparator.comparing(Tag::getName))
                .collect(Collectors.toList());

        obTagList.setAll(tags);
        tagListView.setItems(obTagList);

        return;
    }

    
    /** 
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void selectionManager(ObservableValue<? extends Tag> observable, Tag oldValue, Tag newValue) {
        return;
    }

    
    /** 
     * @param event
     * @throws IOException
     */
    public void goBack(ActionEvent event) throws IOException {
        Utility.changeScene("AlbumView.fxml", event);
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
    public void saveChanges(ActionEvent event) throws IOException {
        Photo photo = Utility.getCurrentPhoto();

        if (photoName.getText().trim().isEmpty()) {
            Utility.sendAlert(AlertType.ERROR, "Error", null, "Please enter a name for the photo.");
            return;
        }

        if (photoCaption.getText().trim().isEmpty()) {
            photoCaption.setText("No caption.");
            return;
        }

        PhotoList photoList = new PhotoList();
        photoList.editPhoto(photo.getID(), photo.getAlbum(), photoName.getText().trim(),
                photoCaption.getText().trim(), photo.getTags(), photo.getFile());

        Utility.changeScene("AlbumView.fxml", event);

        Utility.sendAlert(AlertType.INFORMATION, "Success", null, "Photo edited successfully.");

        return;
    }


    public void createTag(ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setTitle("Add Tag");
        dialog.setContentText("Please enter the tag name and value (separated by a colon):");

        java.util.Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String[] tag = result.get().split(":");
            if (tag.length != 2) {
                Utility.sendAlert(AlertType.ERROR, "Error", null, "Please enter a valid tag name and value.");
                return;
            }

            Photo photo = Utility.getCurrentPhoto();
            photo.addTag(new Tag(tag[0], tag[1]));

            PhotoList photoList = new PhotoList();
            photoList.editPhotoTags(photo.getID(), photo.getTags());

            refresh();
        }
    }


    public void deleteTag(ActionEvent event) throws IOException {
        Tag tag = tagListView.getSelectionModel().getSelectedItem();
        if (tag == null) {
            Utility.sendAlert(AlertType.ERROR, "Error", null, "Please select a tag to delete.");
            return;
        }

        Photo photo = Utility.getCurrentPhoto();
        photo.removeTag(tag);

        PhotoList photoList = new PhotoList();
        photoList.editPhotoTags(photo.getID(), photo.getTags());

        Utility.sendAlert(AlertType.INFORMATION, "Success", null, "Tag deleted successfully.");

        refresh();
    }
}
