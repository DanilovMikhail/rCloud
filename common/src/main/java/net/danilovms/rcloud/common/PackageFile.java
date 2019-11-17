package net.danilovms.rcloud.common;

public class PackageFile {
    private int number;
    private String fileName;
    private byte data;

    public PackageFile(int number, String fileName, byte data){
        this.number     = number;
        this.fileName   = fileName;
        this.data       = data;
    }

    public int getNumber() {
        return number;
    }

    public String getFileName() {
        return fileName;
    }
}
