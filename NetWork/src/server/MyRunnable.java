package server;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class MyRunnable implements Runnable{
    Socket socket;
    Properties properties;

    public MyRunnable(Socket socket,Properties properties){
        this.properties = properties;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                String choose = br.readLine();
                switch (choose){
                    case "login" :
                        login(br);
                    case "register" :
                        System.out.println("注册操作");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void login(BufferedReader br) throws IOException {
        System.out.println("登录操作");
        String userinfo = br.readLine();
        String[] userInfoArr = userinfo.split("&");
        String usernameInput = userInfoArr[0].split("=")[1];
        String passwordInput = userInfoArr[1].split("=")[1];
        System.out.println("用户输入的用户名为："+ usernameInput);
        System.out.println("用户输入的密码为："+ passwordInput);


        if (properties.containsKey(usernameInput)){
            //用户名存在，判断密码
            String rightPassword = properties.get(usernameInput) + "";
            if (rightPassword.equals(passwordInput)){
                //登录成功，开始聊天
                writeMessage2Client(socket,"1");
                Server.list.add(socket);
                talk2All(br,usernameInput);
            }else {
                writeMessager2Client("2");
            }
        }else {
            writeMessage2Client(socket,"3");
        }

    }



    private void talk2All(BufferedReader br, String username) throws IOException {
        while (true){
            String message = br.readLine();
            System.out.println(username+"发送消息："+message);

            for(Socket s : Server.list){
                writeMessage2Client(s,username+"发送消息："+message);
            }
        }
    }
    private void writeMessager2Client(String message) throws IOException {
        //获取输出流
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    private void writeMessage2Client(Socket s, String message) throws IOException {
        //获取输出流
        BufferedWriter bw = new BufferedWriter((new OutputStreamWriter(s.getOutputStream())));
        bw.write(message);
        bw.newLine();
        bw.flush();
    }



}
