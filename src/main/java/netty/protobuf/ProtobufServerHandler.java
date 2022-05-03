package netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Author: yangcs
 * @Date: 2022/4/15 14:11
 * @Description:  继承适配器
 */
public class ProtobufServerHandler extends SimpleChannelInboundHandler<MyFruitsOut.MyDataType> {



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyFruitsOut.MyDataType myDataType) throws Exception {
        MyFruitsOut.MyDataType.DataType type = MyFruitsOut.MyDataType.DataType.class.cast(myDataType.getDataType());

        if(type == MyFruitsOut.MyDataType.DataType.AppleType ){
            System.out.println("apple-------------------");
            System.out.println("id = "+myDataType.getApple().getId()+";;;;orderCode = "+myDataType.getApple().getOrderCode());
        }else{
            System.out.println("orange");
            System.out.println("sku = "+myDataType.getOrange().getSku()+";;;;price = "+myDataType.getOrange().getPrice());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好.客户端",CharsetUtil.UTF_8));
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
