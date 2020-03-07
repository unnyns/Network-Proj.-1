import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MqttBroker extends Thread {
    /*TCP port 9999 uses the Transmission Control Protocol. TCP is one of the
      main protocols in TCP/IP networks*/
    private static int port;
    /*The socket server will be initialize with the previous port*/
    private static ServerSocket server;
    /*This list is the one where all clients will be stored*/
    private static ArrayList<MqttClient> clients = new ArrayList<MqttClient>();

    /*This main function will initialize the ServerSocket with the port,
     *and wait for a request. When a request is receive it will create the client
     *with the recieved socket, add it to the list of clients and start the Thread
     */
    static public void main(String[] args) throws IOException {
        port = (int) (Math.random() * ((49152 -  6553) + 1) + 6553 );
        server = new ServerSocket(port);
        System.out.println(InetAddress.getLocalHost());
        System.out.println("Port: " + port);
        while (true) {
            try {
                Socket socket = server.accept();
                MqttClient client = new MqttClient(socket);
                clients.add(client);
                client.start();
            } catch (IOException e) {
                System.out.println("Something failed.");
                server.close();
                System.exit(0);
            }
        }
    }

    /*Getter for the list of clients*/
    public static ArrayList<MqttClient> getAllClients() {
        return clients;
    }


    public static void removeClient(MqttClient client) {
        clients.remove(client);
    }

}
