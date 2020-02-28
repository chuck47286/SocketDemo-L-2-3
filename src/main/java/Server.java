import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The class is
 *
 * @author YuCheng
 * @version 2020/2/28 20:12
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 服务器要监听
        ServerSocket server = new ServerSocket(2000);

        System.out.println("服务器准备就绪");
        System.out.println("服务器信息：" + server.getInetAddress() + "P:" + server.getLocalPort());
//        System.out.println("客户端信息：" + socket.getLocalAddress() + "P:" + socket.getLocalPort());
        // 等待客户端链接
        for (;;) {
            // 得到客户端
            Socket client = server.accept();
            // 客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            // 启动线程
            clientHandler.start();
        }

//        todo(client);
    }
    //  异步的处理类

    /**
     * 客户端消息处理
     */
    private static class ClientHandler extends Thread{
        // 当前的链接
        private Socket socket;
        // 链接需要循环
        private boolean flag = true;
        // 构造方法
        ClientHandler(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {
            super.run();
            // 打印操作(客户端端点)
            System.out.println("新客户端链接：" + socket.getInetAddress() +
                    "P:" + socket.getLocalPort());
            try {
                // 得到打印流，用于数据输出，服务器会回送数据使用
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                // 得到输入流，用于接收数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                do {
                    // 从客户端拿到一条数据
                    String str = socketInput.readLine();
                    if ("bye".equalsIgnoreCase(str)) {
                        flag = false;
                        // 回送
                        socketOutput.println("bye");
                    } else {
                        // 打印到屏幕，并回送一条数据长度
                        System.out.println(str);
                        socketOutput.println("回送：" + str.length());
                    }
                } while(flag);
                socketInput.close();
                socketOutput.close();


            } catch (Exception e) {
                System.out.println("链接异常断开");
            } finally {
                // 链接关闭
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("客户端已退出" +
                        socket.getInetAddress() + "P:" + socket.getPort());
            }
        }
    }
}
