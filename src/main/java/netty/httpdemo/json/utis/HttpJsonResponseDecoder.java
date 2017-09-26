package netty.httpdemo.json.utis;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.memcache.binary.FullBinaryMemcacheRequest;

import java.util.List;

public class HttpJsonResponseDecoder extends AbstractHttpJsonDecoder<FullHttpResponse> {

    public HttpJsonResponseDecoder(Class<?> clazz) {
        super(clazz);
    }

    protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) throws Exception {
        System.out.println("The body in HttpJsonResponseDecoder: " + msg.content());
        HttpJsonResponse response = new HttpJsonResponse(msg, decode0(ctx, msg.content()));
        out.add(response);
    }

}
