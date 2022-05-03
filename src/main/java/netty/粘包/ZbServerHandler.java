package netty.粘包;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @Author: yangcs
 * @Date: 2022/4/18 10:32
 * @Description:
 */
public class ZbServerHandler extends SimpleChannelInboundHandler<MsgProtocol> {
    Integer count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProtocol s) throws Exception {
        System.out.println(s.getLen() + "-----" + new String(s.getContext(), Charset.forName("utf-8")));
        System.out.println("服务器接收到消息包数量--= " + (++count));


        System.out.println("服务器 开始回执消息");
        String uid = UUID.randomUUID().toString();
        byte[] bytes = uid.getBytes("utf-8");
        ctx.writeAndFlush(new MsgProtocol(bytes.length,bytes));
    }
}
