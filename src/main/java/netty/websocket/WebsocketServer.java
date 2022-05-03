package netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import netty.hardbeat.心跳Handler;
import netty.simple.NettyServerHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Author: yangcs
 * @Date: 2022/4/18 16:22
 * @Description:
 */
public class WebsocketServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossEvent = new NioEventLoopGroup(1);
        EventLoopGroup workerEvent = new NioEventLoopGroup();
        try {
            //服务器启动对象,配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossEvent, workerEvent)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            //基于http协议, 需要http编码和解码
                            pipeline.addLast(new HttpServerCodec());
                            //以块的方式写
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * http 数据是分段传输的; 所以HttpObjectAggregator 将多个段聚合处理;
                             * 当浏览器发送大量数据时, 就会发送多次http请求;
                             * */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            //websocket 数据是以帧(frame)传递的
                            //浏览器请求格式 ws://localhost:8192/xxx
                            //WebSocketServerProtocolHandler 核心功能将http协议升级为ws协议,保持长链接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            //自定义处理了
                            pipeline.addLast(new MyWebSocketFrameHandler());
                        }
                    });
            //绑定端口, 同步处理
            ChannelFuture cf = bootstrap.bind(6789).sync();
            cf.channel().closeFuture().sync();
        } finally {
            bossEvent.shutdownGracefully();
            workerEvent.shutdownGracefully();
        }

    }
}
