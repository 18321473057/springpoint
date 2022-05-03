package netty.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: yangcs
 * @Date: 2021/12/28 14:23
 * @Description:
 */
public class BioServer {

    /**
     *   telnet 127.0.0.1 6666
     *   ctrl+]
     *   send 1234
     * */

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务启动!");
        while (true) {
            final Socket socket = serverSocket.accept();
            executorService.execute(() -> {
                try {
                    System.out.println("客户端连接,线程信息,id="+Thread.currentThread().getId()+",名字="+Thread.currentThread().getName());
                    run(socket);
                } catch (IOException e) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }


    }

    private static void run(Socket socket) throws IOException {
        byte[] bytes = new byte[1024];
        InputStream inputStream = socket.getInputStream();
        while (true){
            int read = inputStream.read(bytes);
            if(read!= -1){
                System.out.println(new String(bytes,0,read));
            }else{
                break;
            }
        }
    }
}
