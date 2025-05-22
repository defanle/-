package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Server {
    static ArrayList<Socket> list = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(10001);

        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("E:\\Code\\Java\\NetWork\\userinfo.txt");
        properties.load(fis);
        fis.close();

        while (true){
            Socket socket = ss.accept();
            System.out.println("客户端来连接");
            new Thread(new MyRunnable(socket,properties)).start();
        }

    }
}
