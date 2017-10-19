package netty.embeddedchannel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * a simple fixed length decoder
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int length;

    public FixedLengthFrameDecoder(int length) {
        this.length = length;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        while (in.readableBytes() >= length) {
            ByteBuf byteBuf = in.readBytes(length);
            out.add(byteBuf);
        }
    }
}
