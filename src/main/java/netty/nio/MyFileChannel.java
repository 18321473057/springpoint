package netty.nio;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: yangcs
 * @Date: 2021/12/28 16:26
 * @Description:  FileChanel 使用
 */
public class MyFileChannel {


    public static void main(String[] args) throws IOException {
//        write();
//        read();
        copy();
    }


    public static void write() throws IOException {
        String str = "hello line,你好--------";
        //缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //放入缓冲区
        byteBuffer.put(str.getBytes()).flip();

        //输出流
        FileOutputStream outputStream = new FileOutputStream("d:\\linexx.txt");
        //管道
        FileChannel channel = outputStream.getChannel();
        channel.write(byteBuffer);
        outputStream.close();
    }

    public static void read() throws IOException {
        File file = new File("d:\\linexx.txt");
        //输出流
        FileInputStream inputStream = new FileInputStream(file);
        //管道
        FileChannel channel = inputStream.getChannel();
        //缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        //放入缓冲区
        int read = channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array(),0,read));
        inputStream.close();
    }

    public static void copy() throws IOException {
        //这个文件流只能读
        FileInputStream inputStream = new FileInputStream("d:\\linexx.txt");
        FileChannel channel = inputStream.getChannel();

        //rw 读写;
        RandomAccessFile  inputStream2 = new RandomAccessFile ("d:\\lineZZ.txt","rw");
        FileChannel channel2 = inputStream2.getChannel();

        //普通写法 1
        ByteBuffer allocate = ByteBuffer.allocate(512);
        while (true){
//            allocate.clear();
            //必须复位, 不然position == limit  读取只会返回0
            allocate.position(0);
            int read = channel.read(allocate);
            if(read == -1){
                break;
            }else {
                allocate.flip();
                channel2.write(allocate);
            }
        }

        //直接调用转换方法2
//        channel2.transferFrom(channel,0,channel.size());




//        关闭流
        inputStream.close();
        inputStream2.close();
    }

}
