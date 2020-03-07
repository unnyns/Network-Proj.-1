import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.lang.*;

public class Subscriber extends Thread {
    private static int port;
    private static String ip;
    private static DataInputStream input;
    private static DataOutputStream output;
    private static Socket socket = null;

    public static void main(String[] args) throws IOException {
        String[] splitCommand; //Array used to split the string command
        String topic = ""; //The topic that the user wrote
        Scanner sc = new Scanner(System.in);

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

            splitCommand = command.split(" ");//Split the command
            //The command should have 1 or 4 arguments
            if (splitCommand.length != 1 && splitCommand.length != 3) {
                System.out.println("Invalid command");
            } else {
                if (splitCommand[0].equals("exit")) {
                    break; //stop the loop
                } else if (splitCommand[0].equals("subscribe")) {
                    ip = splitCommand[1];
                    topic = splitCommand[2];
                    if (socket == null || socket.isConnected()) {
                        try {
                            socket = new Socket(ip, port); //Try to connect with the server
                            System.out.println("Successfully connect to broker " + socket.getRemoteSocketAddress());
                            //Open the output stream and send the command
                            output = new DataOutputStream(socket.getOutputStream());
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
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        try {
                            output.writeUTF("exit");
                            Thread.sleep(1000);
                            System.out.println("Exit");


                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                while (socket.isConnected()) //loop waiting for data
                {
                        try {
                            String in;
                            //Open the inout stream and try to receive some data
                            input = new DataInputStream(socket.getInputStream());
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

