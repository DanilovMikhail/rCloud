package net.danilovms.rcloud.server;

import com.sun.jdi.event.ExceptionEvent;
import io.netty.channel.ChannelHandlerContext;

public class ConnectionHandler extends SimpleChannelHandler
{
    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e ) throws Exception
    {
//        if( необходимые условия фильтрации подключения не выполнены )
//        {
//            // закроем подключение
//            e.getChannel().close();
//        }
    }

    @Override
    public void channelClosed( ChannelHandlerContext ctx, ChannelStateEvent e ) throws Exception
    {
        // Здесь, при закрытии подключения, прописываем закрытие всех связанных ресурсов для корректного завершения.
    }


    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, ExceptionEvent e ) throws Exception
    {
        // Обработка возникающих ошибок
        log.error( "message: {}", e.getCause().getMessage() );
    }

}
