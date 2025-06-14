package src;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class twoWayServerSocket {
    public static void main(String[] args) throws IOException {
        System.out.println("Server has started");

        ServerSocket serverSocket = new ServerSocket(65000);
        System.out.println("Server waiting for client's request");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected\n");

        Scanner sc = new Scanner(System.in);
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
        PrintWriter pw = new PrintWriter(osw);

        new Thread(() -> {
            String msg;
            try{
                while ((msg=br.readLine())!=null){
                    System.out.println("Client:- "+msg);
                }
            } catch (IOException e) {
                System.exit(1);
            }
        }).start();

        System.out.println("=========Start chatting from below==========");

        String input;
        while (true){
            input = sc.nextLine();
            pw.println(input);
            pw.flush();

            if (input.equals("exit")){
                System.exit(1);
            }
        }
    }
}
