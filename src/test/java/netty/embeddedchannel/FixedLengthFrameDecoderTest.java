package netty.embeddedchannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

public class FixedLengthFrameDecoderTest {

    @Test
    public void testFrameDecoded(){
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertTrue(channel.writeInbound(input.retain())); // 往channel写入数据，并且标记为已完成状态
        assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        assertEquals(read, buf.readSlice(3));

        buf.release();
    }

    @Test
    public void testFrameDecode2() {

        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        assertFalse(channel.writeInbound(input.readBytes(2))); // EmbeddedChannel.writeInbound 方法返回值是当channel.readInbound 可以从channel中读取数据的时候才会返回true

    }
}