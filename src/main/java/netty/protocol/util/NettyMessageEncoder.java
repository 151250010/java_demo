package netty.protocol.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import netty.protocol.entity.Header;
import netty.protocol.entity.NettyMessage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    private MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {

        if (msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }

        System.out.println("Try to encode the message: " + msg.getBody().toString());

        ByteBuf sendBuf = Unpooled.buffer();
        Header header = msg.getHeader();
        sendBuf.writeInt(header.getCrcCode()); // int crcCode
        sendBuf.writeInt(header.getLength()); // int length
        sendBuf.writeLong(header.getSessionId()); // long sessionId
        sendBuf.writeByte(header.getType()); // byte type
        sendBuf.writeByte(header.getPriority()); // byte priority
        sendBuf.writeInt(header.getAttachment().size()); // int sizeOfAttachment

        header.getAttachment().forEach((name,object)->{
            try {
                byte[] keyArray = name.getBytes("UTF-8");
                sendBuf.writeInt(keyArray.length);
                sendBuf.writeBytes(keyArray);
                marshallingEncoder.encode(object, sendBuf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (msg.getBody() != null) {
            marshallingEncoder.encode(msg.getBody(), sendBuf);
        } else {
            sendBuf.writeInt(0);
            sendBuf.setInt(4, sendBuf.readableBytes());
        }

    }
}
