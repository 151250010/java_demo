package netty.httpdemo.json.utis;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;

public class HttpJsonResponseEncoder extends AbstractHttpJsonEncoder<HttpJsonResponse> {

    protected void encode(ChannelHandlerContext ctx, HttpJsonResponse msg, List<Object> out) throws Exception {
        ByteBuf body = encode0(ctx, msg.getResult());
        FullHttpResponse response = msg.getResponse();
        if (response == null) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        }else {
            // 重新构造一个 设置response body
            response = new DefaultFullHttpResponse(response.getProtocolVersion(), response.getStatus(), body);
        }

        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/json");
        HttpUtil.setContentLength(response, body.readableBytes());
        out.add(response);
    }
}
