package net.danilovms.rcloud.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.danilovms.rcloud.common.InfoPath;
import net.danilovms.rcloud.common.Objects.*;

import java.io.IOException;
import java.nio.file.*;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private String path = "/Users/MichailD/GitHub/rCloud/FileServer";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof User){
                User authUser = (User) msg;
                if (ManagerDB.Authorization(authUser)){
                    authUser.setAuthorised(true);
                }
                ctx.writeAndFlush(authUser);
            }else if (msg instanceof CommandMessage) {
                CommandMessage cm = (CommandMessage) msg;
                if (cm.getUser().getAuthorised()) {
                    switch (cm.getCommand()) {
                        case "getListFiles": {
                            InfoPath infoPath = new InfoPath(path);
                            if (infoPath.isPathExist()) {
                                cm.setTreesFiles(infoPath.getInfoLocalObjectFile());
                                ctx.writeAndFlush(cm);
                            } else {
                                ctx.writeAndFlush(new ErrorMessage("Путь не найден"));
                            }
                            break;
                        }
                        case "delete": {
                            InfoPath infoPath = new InfoPath(cm.getFullName());
                            if (infoPath.isPathExist()){
                                infoPath.deleteDir(infoPath.getPath());
                                cm.setTreesFiles(infoPath.getInfoLocalObjectFile());
                                ctx.writeAndFlush(cm);
                            }else{
                                ctx.writeAndFlush(new ErrorMessage("Файл не найден."));
                            }

                            break;
                        }
                        case "rename": {
                            Path sourcePath         = Paths.get(this.path + "/" + cm.getFullName());
                            Path destinationPath    = Paths.get(this.path + "/" + cm.getNewFullName());

                            if (Files.exists(sourcePath)) {
                                try {
                                    Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                                    InfoPath infoPath = new InfoPath(path);
                                    if (infoPath.isPathExist()) {
                                        cm.setTreesFiles(infoPath.getInfoLocalObjectFile());
                                        ctx.writeAndFlush(cm);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        }
                    }
                }else {
                    ctx.writeAndFlush(new ErrorMessage("Пользователь не авторизован."));
                }
//                if (Files.exists(Paths.get("server_storage/" + fr.getFilename()))) {
//                    FileMessage fm = new FileMessage(Paths.get("server_storage/" + fr.getFilename()));
//                    ctx.writeAndFlush(fm);
//                }
            }else if (msg instanceof FileMessage){
                FileMessage fm = (FileMessage) msg;
                if (fm.getUser().getAuthorised()) {
                    switch (fm.getCommand()){
                        case "send":
                            Files.write(Paths.get(this.path + "/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
                            InfoPath infoPath = new InfoPath(path);
                            if (infoPath.isPathExist()) {
                                fm.setTreesFiles(infoPath.getInfoLocalObjectFile());
                                ctx.writeAndFlush(fm);
                            }
                            break;
                        case "download":
                            String fullName = this.path + "/" + fm.getFileName();
                            if (Files.exists(Paths.get(fullName))){
                                fm.setData(fullName);
                                ctx.writeAndFlush(fm);
                            }else {
                                ctx.writeAndFlush(new ErrorMessage("Файл не найден."));
                            }
                            break;
                    }
                }else {
                    ctx.writeAndFlush(new ErrorMessage("Пользователь не авторизован."));
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
