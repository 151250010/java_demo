package netty.protocol.auth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.protocol.entity.Header;
import netty.protocol.entity.MessageType;
import netty.protocol.entity.NettyMessage;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception{
        channelHandlerContext.writeAndFlush(buildLoginRequest());
    }

    @Override
    public void channelRead(ChannelHandlerContext handlerContext, Object message) {
        NettyMessage nettyMessage = (NettyMessage) message;
        if (nettyMessage.getHeader() != null && nettyMessage.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            byte loginResult = (byte) nettyMessage.getBody(); // 协议规定只返回一个byte
            if (loginResult != (byte) 0) {
                // 握手失败，关闭连接
                handlerContext.close();
            }else {
                System.out.println("Login is OK : " + message);
                handlerContext.fireChannelRead(message);
            }
        }else {
            handlerContext.fireChannelRead(message);
        }
    }

    private NettyMessage buildLoginRequest() {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        nettyMessage.setHeader(header);
        return nettyMessage;
    }
}
