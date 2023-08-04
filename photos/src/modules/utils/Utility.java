package modules.utils;

import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.Album;
import models.AlbumList;
import models.Photo;
import models.PhotoList;
import models.User;
import models.UserList;

public class Utility {

    /**
     * @param alert_type
     * @param title
     * @param header
     * @param content
     * @return Alert
     */
    public static Alert sendAlert(AlertType alert_type, String title, String header, String content) {
        Alert alert = new Alert(alert_type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

        return alert;
    }

    
    /** 
     * @return User
     */
    public static User getCurrentUser() {
        Preferences prefs = Preferences.userRoot();
        String userId = prefs.get("currentUser", null);

        if (userId == null) {
            return null;
        }

        UserList userList = new UserList();
        User user = userList.getUser(userId);

        return user;
    }

    
    /** 
     * @param user
     */
    public static void setCurrentUser(User user) {
        Preferences prefs = Preferences.userRoot();

        if (user == null) {
            prefs.remove("currentUser");
            return;
        }

        prefs.put("currentUser", user.getID());
    }

    
    /** 
     * @return Album
     */
    public static Album getCurrentAlbum() {
        Preferences prefs = Preferences.userRoot();
        String albumId = prefs.get("currentAlbum", null);

        if (albumId == null) {
            return null;
        }

        AlbumList albumList = new AlbumList();
        Album album = albumList.getAlbum(albumId);

        return album;
    }

    
    /** 
     * @param album
     */
    public static void setCurrentAlbum(Album album) {
        Preferences prefs = Preferences.userRoot();

        if (album == null) {
            prefs.remove("currentAlbum");
            return;
        }

        prefs.put("currentAlbum", album.getID());
    }

    public static Photo getCurrentPhoto() {
        Preferences prefs = Preferences.userRoot();
        String photoId = prefs.get("currentPhoto", null);

        if (photoId == null) {
            return null;
        }

        PhotoList photoList = new PhotoList();
        Photo photo = photoList.getPhoto(photoId);

        return photo;
    }

    public static void setCurrentPhoto(Photo photo) {
        Preferences prefs = Preferences.userRoot();

        if (photo == null) {
            prefs.remove("currentPhoto");
            return;
        }

        prefs.put("currentPhoto", photo.getID());
    }

    public static <T> void changeScene(String fxmlPath, T event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Utility.class.getResource("/modules/fxml/" + fxmlPath));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        if (event instanceof ActionEvent) {
            Stage stage = (Stage) ((Button) ((ActionEvent) event).getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else if (event instanceof Button) {
            Stage stage = (Stage) ((Button) event).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else if (event instanceof MenuItem) {
            Stage stage = (Stage) ((MenuItem) event).getParentPopup().getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else if (event instanceof Stage) {
            Stage stage = (Stage) event;
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Unknown event type given in changeScene function: " + event.getClass().getName());
        }
    }
}
