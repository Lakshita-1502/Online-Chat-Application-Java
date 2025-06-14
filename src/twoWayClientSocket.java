package src;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class twoWayClientSocket {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",65000);
        System.out.println("Client has sent the request\n");

        Scanner scanner = new Scanner(System.in);
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
        PrintWriter pw = new PrintWriter(osw);
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        new Thread (() -> {
            String msg;
            try{
                while ((msg=br.readLine())!=null){
                    System.out.println("Server:- "+msg);
                }
            } catch (IOException e) {
                System.exit(1);
            }
        }).start();

        System.out.println("=========Start chatting from below==========");

        String input;
        while (true){
            input = scanner.nextLine();
            pw.println(input);
            pw.flush();

            if (input.equals("exit")){
                System.exit(1);
            }
        }
    }
}
