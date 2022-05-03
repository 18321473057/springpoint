package netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Author: yangcs
 * @Date: 2022/4/15 14:42
 * @Description:
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter  {
    //通道建立后就会执行
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ctx = "+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好呀, 初次见面", CharsetUtil.UTF_8));
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bf = ByteBuf.class.cast(msg);
        System.out.println(bf.toString(CharsetUtil.UTF_8));
        System.out.println(ctx.channel().remoteAddress());    }
}
