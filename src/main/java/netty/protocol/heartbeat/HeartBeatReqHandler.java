package netty.protocol.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocol.entity.Header;
import netty.protocol.entity.MessageType;
import netty.protocol.entity.NettyMessage;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;

    public void channelRead(ChannelHandlerContext channelHandlerContext, Object message) throws Exception{
        NettyMessage nettyMessage = (NettyMessage) message;
        if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            heartBeat = channelHandlerContext.executor().scheduleAtFixedRate(new HeartBeatTask(channelHandlerContext), 0, 5000, TimeUnit.MILLISECONDS);
        } else if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
            System.out.println("Client receive server heart beat message: ---------->" + message);
        } else {
            channelHandlerContext.fireChannelRead(message);
        }
    }

    private NettyMessage buildHeartBeat(){
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_REQ.value());
        nettyMessage.setHeader(header);
        return nettyMessage;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        channelHandlerContext.fireExceptionCaught(cause);
    }

    private class HeartBeatTask implements Runnable{

        private final ChannelHandlerContext channelHandlerContext;

        private HeartBeatTask(ChannelHandlerContext channelHandlerContext) {
            this.channelHandlerContext = channelHandlerContext;
        }

        @Override
        public void run() {
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("Client send heart beat message to Server!: ---------> " + heartBeat);
            channelHandlerContext.writeAndFlush(heartBeat);
        }
    }
}
