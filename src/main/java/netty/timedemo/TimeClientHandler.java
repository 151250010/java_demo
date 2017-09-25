package netty.timedemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(TimeClientHandler.class.getName());

    private final ByteBuf firstMessage;


    public TimeClientHandler() {
        byte[] request = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.copiedBuffer(request); // 直接copy
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf response = (ByteBuf) msg;
        byte[] resBytes = new byte[response.readableBytes()];
        response.readBytes(resBytes);
        String responseString = new String(resBytes, "UTF-8");

        System.out.println("Get the response " + responseString);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.warning("Unexpected exception from downstream: " + cause.getMessage());
        ctx.close();
    }
}
