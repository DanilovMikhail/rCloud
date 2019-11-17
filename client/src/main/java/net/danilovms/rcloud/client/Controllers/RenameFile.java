package net.danilovms.rcloud.client.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RenameFile {
    @FXML
    VBox vbRenameFile;

    @FXML
    TextField NameFile;

    public void btOK(){
        vbRenameFile.getScene().getWindow().hide();
    }

}
