package net.danilovms.rcloud.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.handler.codec.ReplayingDecoder;

public class FrameHandler extends ReplayingDecoder<DecoderState>
{
    public enum DecoderState
    {
        READ_LENGTH,
        READ_CONTENT;
    }

    private int length;

    public ProtocolPacketFramer()
    {
        super(DecoderState.READ_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext chc, Channel chnl, ChannelOutboundBuffer cb, DecoderState state ) throws Exception
    {
        switch ( state )
        {
            case READ_LENGTH:
                length = cb.readInt();
                checkpoint( DecoderState.READ_CONTENT );

            case READ_CONTENT:
                ChannelBuffer frame = cb.readBytes( length );
                checkpoint( DecoderState.READ_LENGTH );
                return frame;

            default:
                throw new Error( "Shouldn't reach here." );
        }
    }
}