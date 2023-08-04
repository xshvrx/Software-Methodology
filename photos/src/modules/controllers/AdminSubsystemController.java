package modules.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.FileNotFoundException;
import java.io.IOException;

import modules.utils.Utility;

import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import models.User;
import models.UserList;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class AdminSubsystemController implements Initializable {
    @FXML
    ListView<User> userListView;

    @FXML
    Button createButton;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;

    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;

    @FXML
    Button logoutButton;

    ObservableList<User> obUserList = FXCollections.observableArrayList();

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Read the contents at startup and load them into the list view
            readFile();

            // If the list is not empty, select the first item
            if (!obUserList.isEmpty()) {
                userListView.getSelectionModel().select(0);
                refreshSelection();
            }

            // Add a listener to list view selections
            userListView.getSelectionModel().selectedItemProperty().addListener(this::selectionManager);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return;
    }

    
    /** 
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void selectionManager(ObservableValue<? extends User> observable, User oldValue, User newValue) {
        if (newValue != null) {
            usernameField.setText(newValue.getUsername());
            passwordField.setText(newValue.getPassword());
        } else {
            resetUser();
        }
    }

    
    /** 
     * @throws FileNotFoundException
     */
    // Reading the contents of a file and returning it as a string
    private void readFile() throws FileNotFoundException {
        UserList userList = new UserList();

        for (User user : userList.getUsers()) {
            obUserList.add(user);
        }

        userListView.setItems(obUserList);

        userListView.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getUsername());
                }
            }
        });
    }


    
    /** 
     * @param event
     * @throws IOException
     */
    public void deleteUser(ActionEvent event) throws IOException {
        if (userListView.isDisable()) {
            // Re-enable the buttons on the interface
            userListView.setDisable(false);
            editButton.setDisable(false);
            deleteButton.setText("Delete User");

            // Reset the user list selection
            refreshSelection();
            return;
        }

        if (obUserList.isEmpty()) {
            Utility.sendAlert(AlertType.ERROR, "Error", null, "No users to delete");
            return;
        }

        int selectedIndex = userListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            Utility.sendAlert(AlertType.ERROR, "Error", null, "No user selected");
            return;
        }

        Alert alert = Utility.sendAlert(AlertType.CONFIRMATION, "Confirmation", null,
                "Are you sure you want to delete this user? This action is irreversible");
        if (alert.getResult() == ButtonType.OK) {
            UserList userList = new UserList();
            User selectedUser = userListView.getSelectionModel().getSelectedItem();
            userList.deleteUser(selectedUser.getID());

            obUserList.remove(selectedIndex);
            userListView.setItems(obUserList);

            if (obUserList.isEmpty()) {
                resetUser();
            } else if (selectedIndex < obUserList.size()) {
                userListView.getSelectionModel().select(selectedIndex);
            } else {
                userListView.getSelectionModel().select(selectedIndex - 1);
            }

            Utility.sendAlert(AlertType.INFORMATION, "Success", null, "Successfully deleted user!");
        }
    }


    
    /** 
     * @param event
     * @throws IOException
     */
    public void createUser(ActionEvent event) throws IOException {
        if (userListView.getSelectionModel().getSelectedItem() != null && !userListView.isDisable()) {
            // Disable the buttons on the interface
            userListView.setDisable(true);
            editButton.setDisable(true);
            deleteButton.setText("Cancel Account Creation");
            resetUser();
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Utility.sendAlert(AlertType.ERROR, "Error", null, "Please enter a username and password");
        } else if (username.equals("admin")) {
            Utility.sendAlert(AlertType.ERROR, "Error", null, "You cannot create a user with the username 'admin'!");
        } else {
            User user = new User(username, password);

            boolean alreadyExists = obUserList.stream().anyMatch(s -> s.getUsername().equals(username));

            if (alreadyExists) {
                Utility.sendAlert(AlertType.ERROR, "Error", null, "A user with that username already exists!");
            } else {
                obUserList.add(user);
                userListView.setItems(obUserList);
                userListView.getSelectionModel().select(user);

                UserList userList = new UserList();
                User newUser = new User(username, password);
                userList.addUser(newUser);

                userListView.setDisable(false);
                editButton.setDisable(false);
                deleteButton.setText("Delete User");

                Utility.sendAlert(AlertType.INFORMATION, "Success", null,
                        String.format("Successfully created new user with the username: %s", username));
            }
        }
    }


    public void editUser(ActionEvent event) {
        UserList userList = new UserList();
        User selectedUser = userListView.getSelectionModel().getSelectedItem();

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (selectedUser == null) {
            Utility.sendAlert(AlertType.ERROR, "Error", null, "Please select a user to edit");
        } else if (username.isEmpty() || password.isEmpty()) {
            Utility.sendAlert(AlertType.ERROR, "Error", null, "Please enter a username and password");
        } else {
            if (userList.checkUsername(username) && !selectedUser.getUsername().equals(username)) {
                Utility.sendAlert(AlertType.ERROR, "Error", null, "A user with that username already exists!");
                return;
            }

            selectedUser.setUsername(username);
            selectedUser.setPassword(password);

            userListView.setItems(obUserList);
            userListView.refresh();

            Utility.sendAlert(AlertType.CONFIRMATION, "Success", null, "Successfully edited user!");
        }

        return;
    }


    public void logout(ActionEvent event) throws IOException {
        Utility.changeScene("LoginPage.fxml", event);
    }


    public void resetUser() {
        usernameField.clear();
        passwordField.clear();
    }


    public void sortUserList() {
        obUserList.sort((s1, s2) -> {
            return s1.getUsername().compareToIgnoreCase(s2.getUsername());
        });
    }


    public void refreshSelection() {
        User user = userListView.getSelectionModel().getSelectedItem();
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
    }
}
