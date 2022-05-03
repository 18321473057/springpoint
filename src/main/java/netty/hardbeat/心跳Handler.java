package netty.hardbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author: yangcs
 * @Date: 2022/4/18 15:57
 * @Description:
 */
public class 心跳Handler extends ChannelInboundHandlerAdapter {
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {

        if(event instanceof IdleStateEvent){
            IdleStateEvent stateEvent = IdleStateEvent.class.cast(event);
            String msg = "";
            switch (stateEvent.state()){
                case ALL_IDLE:
                    msg="读写空闲";
                    break;
                case READER_IDLE:
                    msg="读空闲";
                    break;
                case WRITER_IDLE:
                    msg="写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress()+"出现了  "+msg);
        }

    }
}
