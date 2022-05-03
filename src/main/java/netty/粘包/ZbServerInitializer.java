package netty.粘包;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.http.HttpServerHandler;

/**
 * @Author: yangcs
 * @Date: 2022/4/16 13:28
 * @Description:
 */
public class ZbServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("decoder",new ZbMsgDecoder());  //自定义编码, 处理粘包问题
        pipeline.addLast("encoder",new ZbMsgEncoder());  //自定义编码, 处理粘包问题
        pipeline.addLast(new ZbServerHandler());
    }
}
