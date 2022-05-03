package netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author: yangcs
 * @Date: 2022/4/16 13:28
 * @Description:
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //netty 提供的编码解码器
        pipeline.addLast("myHttpServerCodec", new HttpServerCodec());
        //提供自定义的handler
        pipeline.addLast("myHttpServerHandler", new HttpServerHandler());
    }
}
