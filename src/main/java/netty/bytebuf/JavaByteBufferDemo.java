package netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

public class JavaByteBufferDemo {

    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(88);
        System.out.println("The ");
        String value = "ZengXiHao";
        byteBuffer.put(value.getBytes());
        // need to flip the index,set the limit to current position and the position to 0
        byteBuffer.flip();
        // the remaining of byte buffer is (limit - position)
        byte[] array = new byte[byteBuffer.remaining()];
        byteBuffer.get(array);
        System.out.println("The value of byte array is " + new String(array));

//        System.out.println(Integer.toBinaryString(64));

    }
}
