package netty.粘包;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author: yangcs
 * @Date: 2022/4/18 10:20
 * @Description: 群聊服务
 */
public class ZbServer {

    public static void main(String[] args) throws InterruptedException {
        new ZbServer(19989).run();
    }
    public void run() throws InterruptedException {
        NioEventLoopGroup bossEvent = new NioEventLoopGroup(1);
        NioEventLoopGroup workerEvent = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossEvent, workerEvent)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 2)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ZbServerInitializer());
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
            System.out.println("---------服务启动!!!");
        } finally {
            bossEvent.shutdownGracefully();
            workerEvent.shutdownGracefully();
        }
    }

    private int port;

    public ZbServer(int port) {
        this.port = port;
    }
}
