package netty.protocol.util;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;

import java.io.IOException;

public class ChannelBufferByteInput implements ByteInput {

    private final ByteBuf byteBuf;

    public ChannelBufferByteInput(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public int read() throws IOException {
        if (byteBuf.isReadable()) {
            return byteBuf.readableBytes() & 0xff; // ? why &
        }
        return -1;
    }

    public int read(byte[] bytes) throws IOException {
        return read(bytes, 0, bytes.length);
    }

    public int read(byte[] bytes, int dstIndex, int length) throws IOException {
        int available = available();
        if (available == 0) {
            return -1;
        }

        length = Math.min(available, length);
        byteBuf.readBytes(bytes, dstIndex, length);
        return length;
    }

    public int available() throws IOException {
        return byteBuf.readableBytes();
    }

    public long skip(long bytes) throws IOException {
        int readbale = byteBuf.readableBytes();
        if (readbale < bytes) {
            bytes = readbale;
        }
        byteBuf.readerIndex((int) (byteBuf.readerIndex() + bytes));
        return bytes;
    }

    public void close() throws IOException {
        // nothing to do
    }
}
