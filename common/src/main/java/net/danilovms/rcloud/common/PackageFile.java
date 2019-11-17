package net.danilovms.rcloud.common;

public class PackageFile {
    private int number;
    private String fileName;
    private byte data;
    private boolean echo;

    public PackageFile(int number, String fileName, byte data, boolean echo){
        this.number     = number;
        this.fileName   = fileName;
        this.data       = data;
        this.echo       = echo;
    }

    public int getNumber() {
        return number;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isEcho() {
        return echo;
    }

    public void setEcho() {
        this.echo   = true;
        this.data   = 0;
    }
}
