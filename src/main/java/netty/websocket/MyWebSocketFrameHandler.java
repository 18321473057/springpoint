package netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 * @Author: yangcs
 * @Date: 2022/4/18 16:48
 * @Description:
 */
public class MyWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息"+msg.text());
        //回复消息
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("服务器时间:"+ LocalDateTime.now()+msg.text()));
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asShortText());
    }
}
