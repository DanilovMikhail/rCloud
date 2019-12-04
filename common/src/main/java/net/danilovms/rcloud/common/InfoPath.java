package net.danilovms.rcloud.common;

import net.danilovms.rcloud.common.Objects.ObjectFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class InfoPath{

    private Path path;
    private boolean pathExist;
    private ArrayList<ObjectFile> infoLocalObjectFile = new ArrayList();;

    public InfoPath(String path){
        this.path = Paths.get(path);
        setPathExist(Files.exists(this.path));
    }

    public void setPathExist(boolean pathExist) {
        this.pathExist = pathExist;
    }

    public Path getPath() {
        return this.path;
    }

    public boolean isPathExist(){
        return this.pathExist;
    }

    public ArrayList<ObjectFile> getInfoLocalObjectFile() {
        if (!this.pathExist){
            return null;
        }
        //обход директории
        try {
            Files.walkFileTree(this.path, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (dir.equals(path)){
                        return FileVisitResult.CONTINUE;
                    }else {
                        infoLocalObjectFile.add(new ObjectFile(dir.getFileName().toString(), dir.toRealPath().toString(), true));
                        return FileVisitResult.SKIP_SUBTREE;
                    }
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    infoLocalObjectFile.add(new ObjectFile(file.getFileName().toString(), file.toRealPath().toString(), false));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    infoLocalObjectFile.add(new ObjectFile(file.getFileName().toString(), file.toRealPath().toString(), false));
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return infoLocalObjectFile;
    }

    public void deleteDir(Path dir){
        try {
            Files.delete(dir);
        } catch (IOException e) {
            // Не удалось удалить файл
            e.printStackTrace();
        }

    }
}
