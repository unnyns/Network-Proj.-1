import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.lang.*;

public class Subscriber extends Thread {
    public static int port;
    public static String ip;

    public Subscriber(String Ip, int Port) {
        ip = Ip;
        port = Port;
    }


    public static void main(String[] args) throws IOException {
        String[] splitcommand; //Array used to split the string command
        String topic = ""; //The topic that the user wrote
        Socket socket = null; //Socket which make the connection with the server
        Scanner sc = new Scanner(System.in);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    Thread.sleep(200);
                    System.out.println("Shutting down ...");
                    //some cleaning up code...

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });


        while(true){
            try {
                System.out.println("Enter the port (Ex: 9999)");
                String scanport = sc.nextLine();//Try to take the port
                port = Integer.parseInt(scanport);//Parse the port to have an int
                break;
            } catch (Exception e) {
                System.out.println("Invalid port");//If the user doesn't write a number
            }
        }
        while (true) {
            System.out.println("Subscribe [ServerIp] [Topic] or Exit");

            String command = sc.nextLine().toLowerCase();//Scan the subscribe command

            splitcommand = command.split(" ");//Split the command
            //The command should have 1 or 4 arguments
            if (splitcommand.length != 1 && splitcommand.length != 3) {
                System.out.println("Invalid command");
            } else {
                if (splitcommand[0].equals("exit")) {
                    break; //stop the loop
                } else if (splitcommand[0].equals("subscribe")) {
                    ip = splitcommand[1];
                    topic = splitcommand[2];
                    if (socket == null || socket.isConnected()) {
                        try {
                            socket = new Socket(ip, port); //Try to connect with the server
                            System.out.println("Successfully connect to broker " + socket.getRemoteSocketAddress());
                            //Open the output stream and send the command
                            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                            output.writeUTF("subscribe " + topic);
                            break;
                        } catch (Exception e) {
                            System.out.println("Can't connect to broker: " + e.getMessage());
                        }
                    }
                } else {
                    System.out.println("You can only Subscribe or Exit.");
                }
            }
        }
            if (socket != null) //If the socket is not null we close it
            {
                Scanner scanner = new Scanner(System.in);
                while (socket.isConnected()) //loop waiting for data
                {
                        try {
                            String in;
                            //Open the inout stream and try to receive some data
                            DataInputStream input = new DataInputStream(socket.getInputStream());
                            in = input.readUTF(); //Read the stream
                            if (in != null) //If we get a message
                            {
                                System.out.println(in);//We print the message
                            }
                        } catch (IOException e) {
                        }

                }
                socket.close();
            }
        System.out.println("Exit");
    }
}

//?subscribe 192.168.0.146 test

