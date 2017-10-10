package netty.protocol.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import netty.protocol.entity.Header;
import netty.protocol.entity.NettyMessage;

import java.util.HashMap;
import java.util.Map;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    private MarshallingDecoder decoder;

    public NettyMessageDecoder(int maxFrameLength,int lengthFieldOffset,int lengthFiledLength) throws Exception {
        super(maxFrameLength, lengthFieldOffset, lengthFiledLength, -8, 0);
        decoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext channelHandlerContext, ByteBuf in) throws Exception {

        System.out.println("Begin to decode : " + in.toString(CharsetUtil.UTF_8));

        ByteBuf frame = (ByteBuf) super.decode(channelHandlerContext, in);
        if (frame == null) {
            return null;
        }

        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionId(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());

        int size = in.readInt();
        if (size > 0) {
            Map<String, Object> attachMent = new HashMap<>();
            int keySize;
            byte[] keyArray;
            String key;
            for (int i = 0; i < size; i++) {
                keySize = in.readInt();
                keyArray = new byte[keySize];
                in.readBytes(keyArray);
                key = new String(keyArray, "UTF-8");
                attachMent.put(key, decoder.decode(in));
            }
            header.setAttachment(attachMent);
        }

        if (in.readableBytes() > 4) {
            message.setBody(decoder.decode(in));
        }

        message.setHeader(header);
        return message;
    }
}
