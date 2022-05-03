package netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @Author: yangcs
 * @Date: 2022/4/15 14:26
 * @Description:
 */
public class ProtobufClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup) //设置线程组
                    .channel(NioSocketChannel.class)// 设置客户端通道的实现类
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                            //加入proto编码器
                            socketChannel.pipeline().addLast("encoder",new ProtobufEncoder());
                            socketChannel.pipeline().addLast(new ProtobufClientHandler());
                        }
                    });
            System.out.println("客户端配置完成");
            ChannelFuture cf = bootstrap.connect("127.0.0.1", 6789).sync();
            cf.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}
