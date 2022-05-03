package netty.粘包;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: yangcs
 * @Date: 2022/4/25 16:20
 * @Description:
 */
public class ZbMsgEncoder extends MessageToByteEncoder<MsgProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MsgProtocol msgProtocol, ByteBuf byteBuf) throws Exception {
        System.out.println("自定义编码器被调用");
        byteBuf.writeInt(msgProtocol.getLen());
        byteBuf.writeBytes(msgProtocol.getContext());
    }
}
