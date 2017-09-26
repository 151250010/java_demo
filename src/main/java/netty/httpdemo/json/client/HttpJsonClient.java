package netty.httpdemo.json.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import netty.httpdemo.json.entity.Order;
import netty.httpdemo.json.utis.HttpJsonRequestEncoder;
import netty.httpdemo.json.utis.HttpJsonResponseDecoder;

import java.net.InetAddress;

public class HttpJsonClient {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new HttpJsonClient().connect(port);
    }

    public void connect(int port) throws Exception {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //先把http response 解析成为FullHttpResponse
                            ch.pipeline().addLast("http-decoder", new HttpResponseDecoder());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));

                            // FullHttpResponse ---> 实体类
                            ch.pipeline().addLast("json-decoder", new HttpJsonResponseDecoder(Order.class));

                            // 生成HttpRequest
                            ch.pipeline().addLast("http-encoder", new HttpRequestEncoder());

                            // Object --> Json 放置到FullHttpRequest之中
                            ch.pipeline().addLast("json-encoder", new HttpJsonRequestEncoder());

                            ch.pipeline().addLast("client-handler", new HttpJsonClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(InetAddress.getLocalHost(),port).sync();

            future.channel().closeFuture().sync();
        }
        finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
