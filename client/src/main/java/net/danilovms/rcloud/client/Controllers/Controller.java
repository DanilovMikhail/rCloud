package net.danilovms.rcloud.client.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import net.danilovms.rcloud.common.*;

public class Controller{
    @FXML
    AnchorPane AnchorPane, apMain;

    @FXML
    TextField tfPath;

    //menu
    @FXML
    MenuItem miCloseWindow, cmSendToServer, cmDeleteLocalFile, cmRenameLocalFile, cmDownload, cmDeleteServerFile, cmRenameServerFile;

    @FXML
    TableView<ObjectFile> localFile, serverFile;

    @FXML
    TableColumn nameLocalFile, fullNameLocalFile, nameServerFile, fullNameServerFile;

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    private User user;
    private String path = "/Users/MichailD/GitHub/rCloud/FileClient";

    final String IP_ADRESS = "localhost";
    final int PORT = 8189;

    @FXML
    private void initialize(){
        openLoginWindow();

        if (user == null){
            closeChat();
        } else{
            // устанавливаем тип и значение которое должно хранится в колонке
            nameLocalFile.setCellValueFactory(new PropertyValueFactory<ObjectFile, String>("name"));
            fullNameLocalFile.setCellValueFactory(new PropertyValueFactory<ObjectFile, String>("fullName"));
            nameServerFile.setCellValueFactory(new PropertyValueFactory<ObjectFile, String>("name"));
            fullNameServerFile.setCellValueFactory(new PropertyValueFactory<ObjectFile, String>("fullName"));

            tfPath.setText(path);
            viewFilesInPath();
        }
    }

    public void openLoginWindow(){
        try {
            Stage stage         = new Stage();
            FXMLLoader loader   = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root         = loader.load();
            Login lc            = (Login) loader.getController();
            lc.backController   = this;

            stage.setTitle("User autorization");
            stage.setScene(new Scene(root, 400 , 150));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openMessageWindow(String text){
        try {
            Stage stage         = new Stage();
            FXMLLoader loader   = new FXMLLoader(getClass().getResource("/fxml/Message.fxml"));
            Parent root         = loader.load();
            Message lc          = (Message) loader.getController();
            lc.lblMessage.setText(text);

            stage.setTitle("Info");
            stage.setScene(new Scene(root, 400 , 100));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user){
        this.user = user;
    }

    public void btReloadFilesPath(){
        this.path = tfPath.getText();
        viewFilesInPath();
    }

    public void viewFilesInPath(){
        InfoPath infoPath = new InfoPath(path);
        if (infoPath.isPathExist())
        {
            localFile.setItems(infoPath.getInfoLocalObjectFile());
        }else {
            openMessageWindow("Путь не найден");
        }
    }

    public void sendFileToServer(){

    }

    public void deleteLocalFile(){
        InfoPath infoPath = new InfoPath(localFile.getSelectionModel().getSelectedItem().getFullName());
        if (infoPath.isPathExist())
        {
            infoPath.deleteDir(infoPath.getPath());
            viewFilesInPath();
        }else {
            openMessageWindow("Путь не найден");
        }
    }

    public void renameLocalFile(){
        String oldFileName = localFile.getSelectionModel().getSelectedItem().getName();
        String newFileName = "";
        if (oldFileName != "") {
            try {
                Stage stage         = new Stage();
                FXMLLoader loader   = new FXMLLoader(getClass().getResource("/fxml/RenameFile.fxml"));
                Parent root         = loader.load();
                RenameFile lc       = (RenameFile) loader.getController();
                lc.NameFile.setText(oldFileName);

                stage.setTitle("Rename file");
                stage.setScene(new Scene(root, 400, 100));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                newFileName = lc.NameFile.getText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (newFileName != "" && !newFileName.equals(oldFileName)){

                Path sourcePath         = Paths.get(this.path + "/" + oldFileName);
                Path destinationPath    = Paths.get(this.path + "/" + newFileName);

                try {
                    Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                }catch (IOException e) {
                    e.printStackTrace();
                }

                viewFilesInPath();
            }
        }else {
            openMessageWindow("Путь не найден");
        }
    }

    public void closeChat()
    {
        try {
            out.writeUTF("/end");
            System.exit(0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}