package netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Author: yangcs
 * @Date: 2022/4/15 14:11
 * @Description:  继承适配器
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * ChannelHandlerContext 上下文对象; 含有管道pipeline, 通道
     * Object : 客户端发送的数据
     * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ctx = "+ ctx);
        ByteBuf bf = ByteBuf.class.cast(msg);
        System.out.println(bf.toString(CharsetUtil.UTF_8));
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好.客户端",CharsetUtil.UTF_8));
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
