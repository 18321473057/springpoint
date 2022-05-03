package netty.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: yangcs
 * @Date: 2021/12/29 9:33
 * @Description:
 */
public class MyMappedByteBuffer {

    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("d:\\linexx.txt", "rw");
        FileChannel channel = raf.getChannel();
        MappedByteBuffer mbb = channel.map(FileChannel.MapMode.READ_WRITE, 0, 15);
        mbb.put(0, (byte) 'H');
        mbb.put(6, (byte) 'L');
        //其实可以以追加
        mbb.put(14, (byte) ';');

        raf.close();
        System.out.println("--------------------");
    }
}
