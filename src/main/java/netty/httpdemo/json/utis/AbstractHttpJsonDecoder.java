package netty.httpdemo.json.utis;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

public abstract class AbstractHttpJsonDecoder<T> extends MessageToMessageDecoder<T> {

    private Class<?> clazz;

    protected AbstractHttpJsonDecoder(Class<?> clazz) {
        this.clazz = clazz;
    }

    protected Object decode0(ChannelHandlerContext channelHandlerContext, ByteBuf body) throws Exception{
        String content = body.toString(CharsetUtil.UTF_8);
        System.out.println("The content is: " + content);
        return JSON.parseObject(content, clazz); // 使用fastjson 进行转化
    }

}
