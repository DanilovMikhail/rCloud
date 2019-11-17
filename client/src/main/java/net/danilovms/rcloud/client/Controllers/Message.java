package net.danilovms.rcloud.client.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Message {
    @FXML
    VBox vbMessage;

    @FXML
    Label lblMessage;

    public void btOK(){
        vbMessage.getScene().getWindow().hide();
    }
}
