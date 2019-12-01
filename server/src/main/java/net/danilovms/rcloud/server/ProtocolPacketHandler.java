package net.danilovms.rcloud.server;

import io.netty.channel.ChannelHandlerContext;

public class ProtocolPacketHandler extends SimpleChannelHandler
{
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e ) throws Exception
    {
        // получим сообщение
        byte[] msg = ((ChannelBuffer)e.getMessage()).array();

        // Соберем его для отправки обработчику
        Packet packet = new Packet();
        packet.setData( msg );
        packet.setSender(session);

        // Поставим пакет в очередь обработки сесссии
        session.addReadPacketQueue( packet );

        // Поставим сессию в очередь обработки логики
        Server.getReader().addSessionToProcess( session );
    }
}
