package src;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import src.dbConnection;

public class twoWayServerSocket {
    public static void main(String[] args) throws IOException {
        System.out.println("Server has started");

        ServerSocket serverSocket = new ServerSocket(65000);
        System.out.println("Server waiting for client's request");

        Socket socket = serverSocket.accept();
        System.out.println("Client connected\n");

        Connection con=null;
        try{
            con=dbConnection.getConnection();
            System.out.println("Connected to database");
        } catch (SQLException | ClassNotFoundException e) {
            System.exit(1);
        }

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
        int msg_id=1;
        while (true){
            input = sc.nextLine();
            pw.println(input);
            pw.flush();

            try{
                String str="insert into msg (msg_id, sender, chat) values (?, 'server', ?)";
                PreparedStatement pst = con.prepareStatement(str);
                pst.setInt(1, msg_id);
                pst.setString(2, input);
                pst.executeUpdate();
                msg_id++;
            } catch (SQLException e) {
                System.exit(1);
            }

            if (input.equals("exit")){
                System.exit(1);
            }
        }
    }
}
