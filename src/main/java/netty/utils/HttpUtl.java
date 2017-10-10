package netty.utils;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMessage;

public class HttpUtl {

    /**
     * Sets the {@code "Content-Length"} header.
     */
    public static void setContentLength(HttpMessage message, long length) {
        message.headers().set(HttpHeaderNames.CONTENT_LENGTH, length);
    }

    public static boolean isKeepAlive(HttpMessage message) {
        CharSequence connection = message.headers().get(HttpHeaderNames.CONNECTION);
        if (connection != null && HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection)) {
            return false;
        }

        if (message.protocolVersion().isKeepAliveDefault()) {
            return !HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection);
        } else {
            return HttpHeaderValues.KEEP_ALIVE.contentEqualsIgnoreCase(connection);
        }
    }

}
