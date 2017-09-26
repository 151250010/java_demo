package netty.httpdemo.json.utis;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.util.List;

/**
 * used to encode the http request from client side
 */
public class HttpJsonRequestEncoder extends AbstractHttpJsonEncoder<HttpJsonRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpJsonRequest msg, List<Object> out) throws Exception {

        ByteBuf body = encode0(ctx, msg.getObject()); // 直接获取请求体之中的Json String
        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            // 如果业务侧 没有对请求头部进行自定义，使用默认的请求头
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/do", body);
            HttpHeaders httpHeaders = request.headers();
            httpHeaders.set(HttpHeaders.Names.HOST, InetAddress.getLocalHost().getHostAddress());
            httpHeaders.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            httpHeaders.set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP + ',' + HttpHeaders.Values.DEFLATE);
            httpHeaders.set(HttpHeaders.Names.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            httpHeaders.set(HttpHeaders.Names.ACCEPT_LANGUAGE, "zh");
            httpHeaders.set(HttpHeaders.Names.USER_AGENT, "Netty Json Http Client Side");
            httpHeaders.set(HttpHeaders.Names.ACCEPT, "text/html,text/plain,application/json;q=0.9,*/*;q=0.8");
        }
        HttpUtil.setContentLength(request, body.readableBytes());

        out.add(request); //将请求加入队列，等待下一个处理器进行处理
    }
}
