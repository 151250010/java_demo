package netty.protocol.util;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

public class MarshallingEncoder {

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    private Marshaller marshaller;

    public MarshallingEncoder() throws IOException {
        this.marshaller = MarshallingFactory.buildMarshalling();
    }

    protected void encode(Object message, ByteBuf out) throws IOException {
        try{
            int length = out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER); // 先把ByteBuf的前四个字节写入LENTGH_PLACEHOLDER
            ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
            marshaller.start(output);
            marshaller.writeObject(message);
            marshaller.finish();
            out.setInt(length, out.writerIndex() - length - 4);
        }finally {
            marshaller.close();
        }
    }


}
