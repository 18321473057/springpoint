package netty.群聊;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

/**
 * @Author: yangcs
 * @Date: 2022/4/18 10:32
 * @Description:
 */
public class GtoupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //每一个线程都有自己的handler处理器, 要收集所有的channel通道
    //GlobalEventExecutor.INSTANCE 是单例
    private static ChannelGroup cg = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //连接被建立后事件
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //ChannelGroup 可以遍历channel通道,并发送消息;不需要我们遍历
        cg.writeAndFlush(">>>>>>>> [客户端]" + channel.remoteAddress() + "加入聊天/n");
        cg.add(channel);
    }

    //连接断开后事件
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        cg.writeAndFlush(">>>>>>>> [客户端]" + channel.remoteAddress() + "离开了聊天/n");
        //不需要删除吗
        cg.remove(channel);
    }


    //channel 处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    //channel 处于活动状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel channel = ctx.channel();
        cg.forEach(ch -> {
            //不是当前的发送消息的 channel
            if (channel != ch) {
                ch.writeAndFlush("[成员]" + channel.remoteAddress() + "发送了消息" + s);
            } else {
                ch.writeAndFlush("[自己]发送的消息已成功, msg=" + s);
            }
        });
    }

    //异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
        ctx.close();
    }

}
