package modules.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import models.User;
import models.UserList;
import modules.utils.Utility;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class LoginPageController implements Initializable {
    @FXML
    Button loginButton;

    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;

    /**
     * @param location
     * @param resources
     */
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        return;
    }

    
    /** 
     * @param event
     * @throws IOException
     */
    public void login(ActionEvent event) throws IOException {
        try {
            UserList userList = new UserList();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.equals("admin")) {
                if (!password.equals("admin")) {
                    Utility.sendAlert(AlertType.ERROR, "Error", null,
                            "The username or password you entered is incorrect.");
                    return;
                } else {
                    Utility.changeScene("AdminSubsystem.fxml", event);
                    return;
                }
            }

            if (userList.checkUsername(username)) {
                User user = userList.getUserByUsername(username);
                if (user.getPassword().equals(password)) {
                    Utility.setCurrentUser(user);
                    Utility.changeScene("UserSubsystem.fxml", event);
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Utility.sendAlert(AlertType.ERROR, "Error", null, "The username or password you entered is incorrect.");
    }
}
