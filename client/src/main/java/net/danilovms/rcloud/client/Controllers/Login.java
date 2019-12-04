package net.danilovms.rcloud.client.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.danilovms.rcloud.client.Network;
import net.danilovms.rcloud.common.Objects.*;

import java.io.IOException;

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

    public void tryToAuth(ActionEvent actionEvent) {
        String login = loginField.getText();
        char[] pass = passField.getText().toCharArray();

        if (backController.getUser() == null){
            backController.setUser(new User(login, pass));
        }else {
            if (backController.getUser().getAuthorised() == true) {
                return;
            }else{
                backController.getUser().setLogin(login);
                backController.getUser().setPassword(pass);
            }
        }

        Network.start();
        Network.sendMsg(backController.getUser());
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof User){
                        User userInfo = (User) am;
                        responseProcessing(userInfo);
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void responseProcessing(User userInfo) {
        updateUI(() -> {
            if (userInfo.getAuthorised()){
                backController.getUser().setAuthorised(true);
                AnchorPane.getScene().getWindow().hide();
            }else{
                lblAuth.setText("Неправильный логин или пароль!");
            }
        });
    }

    public void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }

    public void goToRegister(){

    }

    public void setLblAuth(String text){
        lblAuth.setText(text);
    }
}
