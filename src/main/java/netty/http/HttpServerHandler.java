package netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @Author: yangcs
 * @Date: 2022/4/16 13:26
 * @Description:
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //读取时间触发,读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = HttpRequest.class.cast(msg);
            if (new URI(request.uri()).getPath().equals("/favicon.ico")) {
                System.out.println("图标地址~!!!");
                return;
            }
            System.out.println("msg 类型 = " + msg.getClass());
            System.out.println("客户端地址 = " + ctx.channel().remoteAddress());
            ByteBuf byteBuf = Unpooled.copiedBuffer("你好,我是服务器", CharsetUtil.UTF_16);
            //构建httpRespose
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            //"text/plain"
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
            //返回response
            ctx.writeAndFlush(response);
        }

    }
}

