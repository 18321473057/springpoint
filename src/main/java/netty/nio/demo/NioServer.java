package netty.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: yangcs
 * @Date: 2022/4/13 13:56
 * @Description:
 */
public class NioServer {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //监听端口
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1",6666));
        //非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //注册连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            //无事件发生
            if(selector.select(1000) == 0){
                System.out.println("无事件发生!");
                continue;
            }
            //获取通道的key
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //判断是否连接事件?
                if (key.isAcceptable()){
                    //虽然这个方法是阻塞的, 但是这时候 连接事件已经发生,所以会立刻执行就变成了不阻塞
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //已经连接上, 注册写入事件
                    socketChannel.register(selector, SelectionKey.OP_WRITE, ByteBuffer.allocate(1024));
                }

                if (key.isWritable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int read = socketChannel.read(buffer);
                    System.out.println("客户端发送数据 "+new String(buffer.array()));
                }
//                移除当前事件
                iterator.remove();
            }
        }
    }

}
