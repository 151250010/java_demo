package netty.httpdemo.json.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import netty.httpdemo.json.entity.Order;
import netty.httpdemo.json.utis.HttpJsonRequestDecoder;
import netty.httpdemo.json.utis.HttpJsonResponseEncoder;

import java.net.InetSocketAddress;

public class HttpJsonServer {

    public void run (int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-decoder", new HttpRequestEncoder()); // http请求解码
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));

                            ch.pipeline().addLast("json-decoder", new HttpJsonRequestDecoder(Order.class));

                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("json-encoder", new HttpJsonResponseEncoder());

                            ch.pipeline().addLast("json-server-handler", new HttpJsonServerHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(port));
            System.out.println("Http 订购服务器启动，网址是： " + "http://localhost:" + port);

            channelFuture.channel().closeFuture().sync();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{

        int port = 8080;
        new HttpJsonServer().run(port);

    }
}
