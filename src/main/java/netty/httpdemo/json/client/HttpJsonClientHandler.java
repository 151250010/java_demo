package netty.httpdemo.json.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.httpdemo.json.entity.Order;
import netty.httpdemo.json.utis.HttpJsonRequest;
import netty.httpdemo.json.utis.HttpJsonResponse;

public class HttpJsonClientHandler extends SimpleChannelInboundHandler<HttpJsonResponse> {

    protected void channelRead0(ChannelHandlerContext ctx, HttpJsonResponse msg) throws Exception {
        System.out.println("The client receive response of http header is: " + msg.getResponse().headers().names());
        System.out.println("The client receive response of http body is: " + msg.getResult());
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        HttpJsonRequest request = new HttpJsonRequest(null, Order.createOrder(963852));
        channelHandlerContext.writeAndFlush(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        throwable.printStackTrace();
        channelHandlerContext.close();
    }

}
