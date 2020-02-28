import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The class iss
 *
 * @author YuCheng
 * @version 2020/2/28 20:12
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        // 超过时间
        socket.setSoTimeout(3000);
        // 链接本地，端口2000：；超过时间3000ms
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);
        // 链接成功
        System.out.println("已发起服务器链接，并进入后续流程");
        System.out.println("客户端信息：" + socket.getLocalAddress() + "P:" + socket.getLocalPort());
        System.out.println("服务器信息：" + socket.getInetAddress() + "P:" + socket.getPort());
//        System.out.println();
        try {
            // 发送接收数据
            todo(socket);
        } catch(Exception e) {
            System.out.println("异常关闭");
        }
        // 释放资源
        socket.close();
        System.out.println("客户端已退出");

    }
    // 发送数据
    private static void todo(Socket client) throws IOException{
        // 键盘输入流
        InputStream in = System.in;
        // 转换成bufferreader
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        // 得到Socket输出流，并装换成打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);

        // 得到Socket输入流（服务器）
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));


        boolean flag = true;
        do {

            // 键盘读取一行
            String str = input.readLine();
            // 发送到服务器
            socketPrintStream.println(str);

            // 从服务器读取一行
            String echo = socketBufferedReader.readLine();
            // 服务器如果给予bye,说明就是退出操作
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        } while (flag);

        socketPrintStream.close();
        socketBufferedReader.close();
    }
}
