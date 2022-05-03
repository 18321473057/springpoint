package netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Author: yangcs
 * @Date: 2022/4/15 14:42
 * @Description:
 */
public class ProtobufClientHandler extends ChannelInboundHandlerAdapter {
    //通道建立后就会执行
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ctx = " + ctx);
        int i = (int) (System.currentTimeMillis() % 2);
        MyFruitsOut.MyDataType fruits;
        if (i == 0) {
            fruits = MyFruitsOut.MyDataType.newBuilder().setDataType(MyFruitsOut.MyDataType.DataType.AppleType)
                    .setApple(MyFruitsOut.Apple.newBuilder().setId(123).setOrderCode("我是苹果").build()).build();
        } else {
            fruits = MyFruitsOut.MyDataType.newBuilder().setDataType(MyFruitsOut.MyDataType.DataType.OrangeTyoe)
                    .setOrange(MyFruitsOut.Orange.newBuilder().setPrice(999).setSku("我是橘子").build()).build();
        }
        ctx.writeAndFlush(fruits);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bf = ByteBuf.class.cast(msg);
        System.out.println(bf.toString(CharsetUtil.UTF_8));
        System.out.println(ctx.channel().remoteAddress());
    }
}
