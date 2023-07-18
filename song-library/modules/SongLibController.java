/*
Rutgers University CS213 Software Methodology
Assignment 1 - Song Library Application
@author Zaeem Zahid
@author Shiv Patel
*/

package modules;

import models.Song;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class SongLibController implements Initializable {
    @FXML ListView<Song> songList;
    
    @FXML Button addButton;
    @FXML Button editButton;
    @FXML Button deleteButton;
    
    @FXML TextField titleField;
    @FXML TextField artistField;
    @FXML TextField albumField;
    @FXML TextField yearField;

    ObservableList<Song> obSongList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Read the contents at startup and load them into the list view
            readFile();

            // If the list is not empty, select the first item
            if (!obSongList.isEmpty()) {
                songList.getSelectionModel().select(0);
                refreshSelection();
            }

            // Add a listener to list view selections
            songList.getSelectionModel().selectedItemProperty().addListener(this::selectionManager);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return;
    }

    private void selectionManager(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
        if (newValue != null) {
            titleField.setText(newValue.getTitle());
            artistField.setText(newValue.getArtist());
            albumField.setText(newValue.getAlbum());
            yearField.setText(newValue.getYear());
        } else {
            resetSong();
        }
    }

    // Reading the contents of a file and returning it as a string
    private void readFile() throws FileNotFoundException {
        Path filePath = Paths.get("src/attributes/songs.json");

        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
                Files.write(filePath, "[]".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

        try {
            String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(content);
        
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String artist = jsonObject.getString("artist");
                String album = jsonObject.getString("album");
                String year = jsonObject.getString("year");
                Song song = new Song(title, artist, album, year);

                obSongList.add(song);
            }
            
            songList.setItems(obSongList);
            sortSongList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(ObservableList<Song> songList) {
        JSONArray songArray = new JSONArray();

        for (Song song : songList) {
            JSONObject songObject = new JSONObject();
            songObject.put("title", song.getTitle());
            songObject.put("artist", song.getArtist());
            songObject.put("album", song.getAlbum());
            songObject.put("year", song.getYear());
            songArray.put(songObject);
        }
        
        try (FileWriter writer = new FileWriter("src/attributes/songs.json")) {
            writer.write(songArray.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sortSongList();
    }

    public void deleteSong(ActionEvent event) throws IOException {
        if (songList.isDisable()) {
            // Re-enable the buttons on the interface
            songList.setDisable(false);
            editButton.setDisable(false);
            deleteButton.setText("Delete Song");

            // Reset the song list selection
            refreshSelection();
            return;
        }

        if (obSongList.isEmpty()) {
            sendAlert(AlertType.ERROR, "Error", null, "No songs to delete");
            return;
        }
    
        int selectedIndex = songList.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            sendAlert(AlertType.ERROR, "Error", null, "No song selected");
            return;
        }
    
        Alert alert = sendAlert(AlertType.CONFIRMATION, "Confirmation", null, "Are you sure you want to delete this song?");
        if (alert.getResult() == ButtonType.OK) {
            obSongList.remove(selectedIndex);
            songList.setItems(obSongList);
    
            saveToFile(obSongList);
    
            if (obSongList.isEmpty()) {
                resetSong();
            } else if (selectedIndex < obSongList.size()) {
                songList.getSelectionModel().select(selectedIndex);
            } else {
                songList.getSelectionModel().select(selectedIndex - 1);
            }
    
            sendAlert(AlertType.INFORMATION, "Success", null, "Successfully deleted song from library!");
        }
    }

    public void addSong(ActionEvent event) throws IOException {
        if (songList.getSelectionModel().getSelectedItem() != null && !songList.isDisable()) {
            // Disable the buttons on the interface
            songList.setDisable(true);
            editButton.setDisable(true);
            deleteButton.setText("Cancel Song Addition");
            resetSong();
            return;
        }

        String title = titleField.getText().trim();
        String artist = artistField.getText().trim();
        String album = albumField.getText().trim();
        String year = yearField.getText().trim();

        if (title.isEmpty() || artist.isEmpty()) {
            sendAlert(AlertType.ERROR, "Error", null, "Please enter a title and artist");
        } else if (!year.isEmpty() && !isValidYear(year)) {
            sendAlert(AlertType.ERROR, "Error", null, "Please enter a valid year");
        } else {
            Song song = new Song(title, artist, album, year);

            boolean alreadyExists = obSongList.stream().anyMatch(s -> s.getTitle().equals(title) && s.getArtist().equals(artist));

            if (alreadyExists) {
                sendAlert(AlertType.ERROR, "Error", null, "A song with the same name and artist already exists!");
            } else {
                obSongList.add(song);
                songList.setItems(obSongList);
                songList.getSelectionModel().select(song);
                saveToFile(obSongList);

                songList.setDisable(false);
                editButton.setDisable(false);
                deleteButton.setText("Delete Song");

                sendAlert(AlertType.CONFIRMATION, "Success", null, String.format("Successfully added %s by %s", title, artist));
            }
        }
    }

    public void editSong(ActionEvent event) {
        Song selectedSong = songList.getSelectionModel().getSelectedItem();

        String title = titleField.getText().trim();
        String artist = artistField.getText().trim();
        String album = albumField.getText().trim();
        String year = yearField.getText().trim();

        if (selectedSong == null) {
            sendAlert(AlertType.ERROR, "Error", null, "Please select a song to edit");
        } else if (title.isEmpty() || artist.isEmpty()) {
            sendAlert(AlertType.ERROR, "Error", null, "Please enter a title and artist");
        } else if (!year.isEmpty() && !isValidYear(year)) {
            sendAlert(AlertType.ERROR, "Error", null, "Please enter a valid year");
        } else {
            boolean alreadyExists = obSongList.stream().anyMatch(s -> s.getTitle().equals(title) && s.getArtist().equals(artist));

            if (alreadyExists && !selectedSong.getTitle().equals(title) && !selectedSong.getArtist().equals(artist)) {
                sendAlert(AlertType.ERROR, "Error", null, "A song with the same name and artist already exists!");
                return;
            }
            
            selectedSong.setTitle(title);
            selectedSong.setArtist(artist);
            selectedSong.setAlbum(album);
            selectedSong.setYear(year);
            
            songList.setItems(obSongList);
            songList.refresh();
            saveToFile(obSongList);
            sendAlert(AlertType.CONFIRMATION, "Success", null, "Successfully edited song!");
        }

        return;
    }

    public void resetSong() {
        titleField.clear();
        artistField.clear();
        albumField.clear();
        yearField.clear();
    }

    public Alert sendAlert(AlertType alert_type, String title, String header, String content) {
        Alert alert = new Alert(alert_type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();

        return alert;
    }
    
    private boolean isValidYear(String yearString) {
        try {
            int year = Integer.parseInt(yearString);
            return year >= 0 && year <= 9999;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void sortSongList() {
        obSongList.sort((s1, s2) -> {
            int titleCompare = s1.getTitle().compareToIgnoreCase(s2.getTitle());
            if (titleCompare == 0) {
                return s1.getArtist().compareToIgnoreCase(s2.getArtist());
            }
            return titleCompare;
        });
    }

    public void refreshSelection() {
        Song song = songList.getSelectionModel().getSelectedItem();
        titleField.setText(song.getTitle());
        artistField.setText(song.getArtist());
        albumField.setText(song.getAlbum());
        yearField.setText(song.getYear());
    }
}