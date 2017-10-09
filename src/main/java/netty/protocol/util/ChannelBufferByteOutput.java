package netty.protocol.util;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

public class ChannelBufferByteOutput implements ByteOutput {

    private final ByteBuf byteBuf;

    public ChannelBufferByteOutput(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public void write(int i) throws IOException {
        byteBuf.writeByte(i);
    }

    public void write(byte[] bytes) throws IOException {
        byteBuf.writeBytes(bytes);
    }

    public void write(byte[] bytes, int i, int i1) throws IOException {
        byteBuf.writeBytes(bytes, i, i1);
    }

    public void close() throws IOException {
        // nothing to do
    }

    public void flush() throws IOException {
        // nothing to do
    }
}
