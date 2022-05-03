package netty.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author: yangcs
 * @Date: 2022/4/13 15:03
 * @Description:
 */
public class NioClient {

    public static void main(String[] args) throws IOException {
        String msg = "你好 , 我来了";
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        if (!socketChannel.connect(new InetSocketAddress("127.0.0.1", 6666))) {
            System.out.println("客户端连接失败!!!!!");
            while (!socketChannel.finishConnect()){
                System.out.println("客户端最终连接失败!!!!!");
            }
        }
        //wrap 包裹  一个字节数组到buffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
        //buffer 写入channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
