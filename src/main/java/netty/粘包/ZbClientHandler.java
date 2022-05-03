package netty.粘包;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @Author: yangcs
 * @Date: 2022/4/18 14:12
 * @Description:
 */
public class ZbClientHandler extends SimpleChannelInboundHandler<MsgProtocol> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 0; i < 5; i++) {
            String msg = "今天好冷哦, 我想吃火锅";
            byte[] bytes = msg.getBytes(Charset.forName("utf-8"));
            int length = msg.getBytes(Charset.forName("utf-8")).length;
            ctx.channel().writeAndFlush(new MsgProtocol(length, bytes));
            System.out.println("--------发送消息----------");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MsgProtocol msg) throws Exception {
        System.out.println("-----------接受回执消息-------------------");
        System.out.println("回执 长度= " + msg.getLen() + ";消息= " + new String(msg.getContext(),"utf-8"));
    }
}
