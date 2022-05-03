package netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.Executors;

/**
 * @Author: yangcs
 * @Date: 2022/4/15 13:42
 * @Description:
 */
public class NettyServer {


    public static void main(String[] args) throws InterruptedException {
        //boosGroup处理连接事件
        //workerGroup处理读写事件
        //他们都是无限循环
        EventLoopGroup boosGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //服务器启动对象,配置参数
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(boosGroup, workerGroup) //设置两个线程
                .channel(NioServerSocketChannel.class)// 设置客户端通道的实现类
                .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列连接个数
                .handler(new LoggingHandler(LogLevel.INFO))
                .childOption(ChannelOption.SO_KEEPALIVE, true)  //保持连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    //给 通道设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                }) ; //给woekerGroup的管道设置处理器

        System.out.println("服务器已经准备好了");

        //绑定端口, 同步处理
        ChannelFuture cf = serverBootstrap.bind(6789).sync();
        cf.channel().closeFuture().sync();
    }

}
