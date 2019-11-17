package net.danilovms.rcloud.common;

public class ObjectFile {
    private String name;
    private String fullName;
    private boolean folder;

    public ObjectFile(String name, String fullName, boolean folder){
        this.name = name;
        this.fullName = fullName;
        this.folder = folder;
    }

    public void setName(String name) {this.name = name;}

    public void setFullName(String fullName) {this.fullName = fullName;}

    public String getName() {return name;}

    public String getFullName() {return fullName;}

    public boolean getFolder(){return folder;}
}
