package netty.protocol.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocol.entity.Header;
import netty.protocol.entity.MessageType;
import netty.protocol.entity.NettyMessage;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext , Object message) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) message;
        if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            System.out.println("Receive client heart beat message: ---->" + nettyMessage);
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("Send heart beat response message to client: -----> " + heartBeat);
            channelHandlerContext.writeAndFlush(heartBeat);
        } else {
            channelHandlerContext.fireChannelRead(message);
        }
    }

    private NettyMessage buildHeartBeat(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
