package netty.粘包;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @Author: yangcs
 * @Date: 2022/4/25 16:20
 * @Description:
 */
// Void
public class ZbMsgDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("自定义解码器被调用");
        //将字节码转为 MsgProtocol 数据包
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        MsgProtocol msgProtocol = new MsgProtocol(length, bytes);
        list.add(msgProtocol);
    }
}
