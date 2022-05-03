package netty.nio;

import java.nio.IntBuffer;

/**
 * @Author: yangcs
 * @Date: 2022/4/12 16:37
 * @Description:
 */
public class MyBuffer {

    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(5);
        buffer.put(1);
        buffer.put(2);
        buffer.put(3);
        buffer.put(4);
        buffer.put(5);
        //反转, 反转之后才能取出值
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }
    }
}
