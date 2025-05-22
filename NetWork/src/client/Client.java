package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",10001);
        System.out.println("服务器已连接成功！");

        while (true){
            System.out.println("===================");
            System.out.println("1、登录");
            System.out.println("2、注册");
            System.out.println("请选择：");
            Scanner sc = new Scanner(System.in);
            String choose = sc.nextLine();
            switch (choose){
                case "1" :
                    login(socket);
                case "2" :
                    System.out.println("选择注册");
                default:
                    System.out.println("没有这个选项");
            }
        }
    }

    private static void login(Socket socket) throws IOException {
        //获取输出流
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //键盘输入
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = sc.nextLine();
        System.out.println("请输入密码：");
        String password = sc.nextLine();

        //拼接
        StringBuffer sb = new StringBuffer();
        sb.append("username=").append(username).append("&password=").append(password);

        //第一次登录操作
        bw.write("login");
        bw.newLine();
        bw.flush();

        //第二次
        bw.write(sb.toString());
        bw.newLine();
        bw.flush();

        //获取输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = br.readLine();
        System.out.println(message);
        //可能出现的情况
        //1.登录成功 2.密码错误 3.用户不存在
        if ("1".equals(message)){
            System.out.println("登录成功，开始聊天");
            //创建一个线程，用来接受服务端发送过来的聊天记录
            new Thread(new ClinentMyrunnable(socket)).start();
            talk2All(bw);
        } else if ("2".equals(message)) {
            System.out.println("密码输入错误！！！");
        } else if ("3".equals(message)) {
            System.out.println("用户不存在");
        }
    }

    private static void talk2All(BufferedWriter bw) throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.print("内容：");
            String str = sc.nextLine();
            //把内容给服务器
            bw.write(str);
            bw.newLine();
            bw.flush();

        }
    }
}
