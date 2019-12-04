package net.danilovms.rcloud.common.Objects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileMessage extends AbstractMessage{
    private User user;
    private String fileName;
    private boolean folder;
    private String command;
    private byte[] data;
    private ArrayList<ObjectFile> treesFiles;

    public FileMessage(User user, String command, String fileName){
        this.user       = user;
        this.command    = command;
        this.fileName   = fileName;
    }

    public void setData(String fullName){
        try {
            this.data = Files.readAllBytes(Paths.get(fullName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTreesFiles(ArrayList<ObjectFile> treesFiles){this.treesFiles = treesFiles;}

    public ArrayList<ObjectFile> getTreesFiles(){return this.treesFiles;}

    public byte[] getData() {return this.data;}

    public User getUser(){return this.user;}

    public String getFileName(){return this.fileName;}

    public String getCommand(){return this.command;}
}
