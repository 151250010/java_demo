package netty.httpdemo.json.utis;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

public abstract class AbstractHttpJsonEncoder<T> extends MessageToMessageEncoder<T> {

    /**
     * translate the body to string directly
     * @param channelHandlerContext context
     * @param body the request body
     * @return the ByteBuf of request body String
     * @throws Exception
     */
    protected ByteBuf encode0(ChannelHandlerContext channelHandlerContext,Object body) throws Exception {

        String jsonBody = JSON.toJSONString(body); // 使用fastJosn进行转换
        return Unpooled.copiedBuffer(jsonBody, CharsetUtil.UTF_8);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {

        System.out.println("An exception has encountered : " + throwable.getMessage());
    }
}
