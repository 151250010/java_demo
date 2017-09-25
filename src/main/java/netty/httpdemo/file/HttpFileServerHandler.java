package netty.httpdemo.file;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {

        // 对于解析失败的请求暂不处理

        // 对于非get请求暂不处理

        final String uri = request.getUri();
        final String path = sanitizeUri(uri);

        if (path == null) { // 不允许的文件路径请求暂不处理
            return;
        }

        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                sendListing(channelHandlerContext, file);
            } else {
                sendListing(channelHandlerContext, new File(path + "/"));
            }
            return;
        }

        RandomAccessFile accessFile = new RandomAccessFile(file, "r");
        long fileLength = accessFile.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        // 设置content-length 以及 content-type
        HttpUtil.setContentLength(response, fileLength);
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, fileTypeMap.getContentType(file.getPath()));

        if (HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        channelHandlerContext.write(response);

        ChannelFuture future = channelHandlerContext.writeAndFlush(new ChunkedFile(accessFile, 0, fileLength, 8192), channelHandlerContext.newProgressivePromise());

        future.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long l, long l1) throws Exception {

            }

            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                System.out.println("Transfer Complete!");
            }
        });

        ChannelFuture lastContentFuture = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT); // 发送一个编码结束的空消息体

        if (!HttpUtil.isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void sendListing(ChannelHandlerContext channelHandlerContext, File file) {

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        StringBuilder stringBuilder = new StringBuilder();

        initBuffer(stringBuilder, file);

        ByteBuf byteBuf = Unpooled.copiedBuffer(stringBuilder, CharsetUtil.UTF_8);
        response.content().writeBytes(byteBuf);
        byteBuf.release();

        channelHandlerContext.writeAndFlush(response).addListener((ChannelFutureListener) channelFuture -> System.out.println("Get Directory: " + file.getPath()));
    }

    private void initBuffer(StringBuilder stringBuilder, File file) {
        String dirPath = file.getPath();

        stringBuilder.append("<!DOCTYPE html>\r\n");
        stringBuilder.append("<html><head><title>");
        stringBuilder.append("Listing of: ");
        stringBuilder.append(dirPath);
        stringBuilder.append("</title></head><body>\r\n");

        stringBuilder.append("<h3>Listing of: ");
        stringBuilder.append(dirPath);
        stringBuilder.append("</h3>\r\n");

        stringBuilder.append("<ul>");
        stringBuilder.append("<li><a href=\"../\"></li>\r\n");
        Arrays.stream(file.listFiles())
                .filter(f -> !f.isHidden() && f.canRead())
                .map(File::getName)
                .forEach(name -> {
            stringBuilder.append("<li><a href=\"");
            stringBuilder.append(name);
            stringBuilder.append("\">");
            stringBuilder.append(name);
            stringBuilder.append("</a></li>\r\n");
        });

        stringBuilder.append("</ul></body></html>\r\n");
    }

    private String sanitizeUri(String uri) {
        try{
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        uri = uri.replace('/', File.separatorChar);
        boolean notValid = uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.startsWith(".") || uri.endsWith(".");

        if (notValid) {
            return null;
        }

        return System.getProperty("user.dir") + File.separator + uri;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

}
