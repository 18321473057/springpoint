package netty.粘包;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: yangcs
 * @Date: 2022/4/16 13:28
 * @Description:
 */
public class ZbClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("encoder",new ZbMsgEncoder());
        pipeline.addLast("decoder",new ZbMsgDecoder());
        pipeline.addLast(new ZbClientHandler());
    }
}
