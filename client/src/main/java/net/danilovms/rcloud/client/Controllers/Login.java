package net.danilovms.rcloud.client.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.danilovms.rcloud.client.Controllers.Controller;

import net.danilovms.rcloud.common.User;

public class Login {
    @FXML
    AnchorPane AnchorPane, apAuthorization;

    @FXML
    PasswordField passField;

    @FXML
    TextField loginField;

    @FXML
    Button btAuth;

    @FXML
    Label lblAuth;

    public Controller backController;

    public void tryToAuth(ActionEvent actionEvent)
    {
        char[] pass = new char[] {'U', 'n', 'k', 'w', 'o', 'n'};
        backController.setUser(new User("qwerty", pass));
        AnchorPane.getScene().getWindow().hide();
    }

    public void goToRegister(){

    }

    public void setLblAuth(String text){
        lblAuth.setText(text);
    }
}
