package netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @Author: yangcs
 * @Date: 2022/4/13 8:34
 * @Description:  - 将数据写入buffer时, 第一个写满后,可以依次写入;
 * - 从buffer中读取数据时, 第一个读完后, 依次读取第二个;
 */
public class ScatterIngAndGathering {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //获取socket,绑定网络地址
        serverSocketChannel.socket().bind(new InetSocketAddress(7700));

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待连接
        SocketChannel socketChannel = serverSocketChannel.accept();
         while (true){
             //因为是循环, 所以读取前还是先复位吧
             Arrays.asList(byteBuffers).forEach(buf->buf.clear());

             int byteRead = 0;
             while(byteRead < 8){
                 long read = socketChannel.read(byteBuffers);
                 byteRead+=read;
                 System.out.println(byteRead);
                 Arrays.asList(byteBuffers).stream().map(buffer -> "postion = "+buffer.position()+",limit = " + buffer.limit()).forEach(System.out::println);
             }
             Arrays.asList(byteBuffers).forEach(buf->buf.flip());

             //数据回写到客户端
             long  byteWrite = 0;
             while (byteWrite<8){
                 long write = socketChannel.write(byteBuffers);
                 byteWrite+=write;
             }
          }
    }
}
