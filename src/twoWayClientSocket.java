package src;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import src.dbConnection;

public class twoWayClientSocket {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",65000);
        System.out.println("Client has sent the request\n");

        Connection con=null;
        try{
            con=dbConnection.getConnection();
            System.out.println("Connected to the database");
        } catch (SQLException | ClassNotFoundException e) {
            System.exit(1);
        }

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
        int msg_id=1;
        while (true){
            input = scanner.nextLine();
            pw.println(input);
            pw.flush();

            try{
                String str="insert into msg (msg_id, sender, chat) values (?, 'client', ?)";
                PreparedStatement ps = con.prepareStatement(str);
                ps.setInt(1, msg_id);
                ps.setString(2, input);
                ps.executeUpdate();
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
