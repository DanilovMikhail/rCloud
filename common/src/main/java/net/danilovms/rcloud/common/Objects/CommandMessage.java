package net.danilovms.rcloud.common.Objects;

import java.util.ArrayList;

public class CommandMessage extends AbstractMessage {
    private User user;
    private String command;
    private String fullName;
    private String newFullName;
    private ArrayList<ObjectFile> treesFiles;

    public CommandMessage(User user, String command) {
        this.command    = command;
        this.user       = user;
    }

    public void setTreesFiles(ArrayList<ObjectFile> treesFiles){this.treesFiles = treesFiles;}

    public ArrayList<ObjectFile> getTreesFiles(){return this.treesFiles;}

    public void setFullName(String fullName){this.fullName = fullName;}

    public String getFullName(){return this.fullName;}

    public void setNewFullName(String newFullName){this.newFullName = newFullName;}

    public String getNewFullName(){return this.newFullName;}

    public User getUser(){return this.user;}

    public String getCommand(){return this.command;}
}
