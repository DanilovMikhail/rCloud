package net.danilovms.rcloud.client.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.nio.file.*;
import java.util.ArrayList;
import java.util.SplittableRandom;

import jdk.nashorn.internal.ir.IfNode;
import net.danilovms.rcloud.client.Network;
import net.danilovms.rcloud.common.*;
import net.danilovms.rcloud.common.Objects.*;

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

    @FXML
    private void initialize(){

        openLoginWindow();

        if (user == null){
            closeChat();
        } else{

            // устанавливаем тип и значение которое должно хранится в колонке
            setTypeAndValue();

            /////////////////////////////////////////////////////////////////
            //заполнение файлов на форме список локальных файлов
            viewFilesInPath();

            /////////////////////////////////////////////////////////////////
            //заполнение файлов на форме список фалов на сервере
            CommandMessage cm = new CommandMessage(this.user, "getListFiles");
            sendAndWaitAnswerCommandMessage(cm);
        }
    }

    private void openLoginWindow(){
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

    private void openMessageWindow(String text){
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

    private void updateUI(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        } else {
            Platform.runLater(r);
        }
    }

    private void setTypeAndValue(){
        nameLocalFile.setCellValueFactory(new PropertyValueFactory<ObjectFile, String>("name"));
        fullNameLocalFile.setCellValueFactory(new PropertyValueFactory<ObjectFile, String>("fullName"));
        nameServerFile.setCellValueFactory(new PropertyValueFactory<ObjectFile, String>("name"));
        fullNameServerFile.setCellValueFactory(new PropertyValueFactory<ObjectFile, String>("fullName"));

        tfPath.setText(path);
    }

    private void sendAndWaitAnswerCommandMessage(AbstractMessage abstractMessage){
        Network.start();
        Network.sendMsg(abstractMessage);

        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof CommandMessage) {
                        viewFilesInServer(((CommandMessage) am).getTreesFiles());
                    }else if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        if (fm.getCommand().equals("send")) {
                            viewFilesInServer(((FileMessage) am).getTreesFiles());
                        }else{
                            Files.write(Paths.get(this.path + "/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
                        }
                    }else if (am instanceof ErrorMessage){
                        openMessageWindow(((ErrorMessage) am).getTextError());
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

    public void setUser(User user){this.user = user;}

    public User getUser(){return this.user;}

    public void btReloadFilesPath(){
        this.path = tfPath.getText();
        viewFilesInPath();
    }

    public void viewFilesInPath(){
        InfoPath infoPath = new InfoPath(path);
        if (infoPath.isPathExist())
        {
            localFile.setItems(FXCollections.observableList(infoPath.getInfoLocalObjectFile()));
        }else {
            openMessageWindow("Путь не найден");
        }
    }

    public void viewFilesInServer(ArrayList<ObjectFile> arrayServerFile) {
        updateUI(() -> {
            if (arrayServerFile.size() == 0) {
                serverFile.setItems(FXCollections.observableArrayList());
            } else{
                serverFile.setItems(FXCollections.observableList(arrayServerFile));
            }
        });
    }

    //действия с локальными фалами
    public void sendFileToServer(){
        String fullName = localFile.getSelectionModel().getSelectedItem().getFullName();
        if (Files.exists(Paths.get(fullName))) {
            FileMessage fm = new FileMessage(this.user, "send", localFile.getSelectionModel().getSelectedItem().getName());
            fm.setData(fullName);
            sendAndWaitAnswerCommandMessage(fm);
        }else{
            openMessageWindow("Файл не найден.");
        }
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

    //действия с серверными файлами
    public void downloadFileFromServer(){
        FileMessage fm = new FileMessage(this.user, "download", serverFile.getSelectionModel().getSelectedItem().getName());
        sendAndWaitAnswerCommandMessage(fm);
        viewFilesInPath();
    }

    public void deleteFileFromServer(){
        CommandMessage cm = new CommandMessage(this.user, "delete");
        cm.setFullName(serverFile.getSelectionModel().getSelectedItem().getFullName());
        sendAndWaitAnswerCommandMessage(cm);
    }

    public void renameFileFromServer(){
        String oldFileName = serverFile.getSelectionModel().getSelectedItem().getName();
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
                CommandMessage cm = new CommandMessage(this.user, "rename");
                cm.setFullName(oldFileName);
                cm.setNewFullName(newFileName);
                sendAndWaitAnswerCommandMessage(cm);
            }
        }else {
            openMessageWindow("Путь не найден");
        }
    }

    //команды формы
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