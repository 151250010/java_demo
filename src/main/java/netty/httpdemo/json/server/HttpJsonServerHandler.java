package netty.httpdemo.json.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import netty.httpdemo.json.entity.Order;
import netty.httpdemo.json.entity.Shipping;
import netty.httpdemo.json.utis.HttpJsonRequest;
import netty.httpdemo.json.utis.HttpJsonResponse;

public class HttpJsonServerHandler extends SimpleChannelInboundHandler<HttpJsonRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpJsonRequest msg) throws Exception {

        Order order = (Order) msg.getObject();
        System.out.println("Http Server receive request: " + order);
        doBusiness(order); // 进行业务逻辑处理

        ChannelFuture future = ctx.writeAndFlush(new HttpJsonResponse(null, order));

        if (!HttpUtil.isKeepAlive(msg.getRequest())) {
            future.addListener(future1 -> ctx.close());
        }
    }

    private void doBusiness(Order order) {

        order.getAddress().setCountry("Japan");
        order.getAddress().setCity("Tokyo");
        order.getUser().setUserId("151250010");
        order.getUser().setUserName("ZengXihao");
        order.setShipping(Shipping.NATIONAL);

    }

    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception{
        throwable.printStackTrace();
        if (channelHandlerContext.channel().isActive()) {
            sendError(channelHandlerContext, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendError(ChannelHandlerContext channelHandlerContext, HttpResponseStatus status) {

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("失败： " + status.toString() + "\r\n", CharsetUtil.UTF_8));

        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
