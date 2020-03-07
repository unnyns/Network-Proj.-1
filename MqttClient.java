import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class MqttClient extends Thread {
    /*This variable is the id of the client, this id will be the MAC address of
     *the using device*/
    private String id;
    /*Input stream with the server*/
    private DataInputStream input;
    /*Output stream with the server*/
    private DataOutputStream output;
    /*The socket of the client connected to the server*/
    private Socket socket;
    /*The topic on where the client want to be*/
    private String topic;

    private String clientType;

    /*Constructor of the client, this function will get the MAC address of the
     *client, save the socket given in parameter, and open streams from this
     *socket*/
    public MqttClient(Socket Socket) {
        socket = Socket;
        id = socket.getRemoteSocketAddress().toString();
        if (socket != null) {
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("Stream connection failed");
            }

        }
    }

    /*Here is the Thread function, we try to get a command, if it's a subscribe
     *command, a loop begin waiting for any publish in the apropiate topic,
     *if it's a publish, we will search all clients register in the topic
     *and send them the data associate with the command*/
    public void run() {
        String command = "";
        String[] splitcommand = null;
        try {
            command = input.readUTF();
        } catch (IOException e) {
            System.out.println("Impossible to get de command");
        }
        if (command != null) {
            splitcommand = command.split(" ");
        }
        if ((!splitcommand[0].equals("publish") && !splitcommand[0].equals("subscribe")) ||
                (splitcommand.length != 2 && splitcommand.length != 3)) {
            System.out.println("Invalid command");
            System.out.println(command);
            System.out.println(splitcommand[0]);
        }
        if (splitcommand[0].equals("subscribe")) {
            clientType = "Subscriber";
            topic = splitcommand[1];
            System.out.println("Connected with Subscriber : " + socket.getRemoteSocketAddress());
            while (socket.isConnected()){
                try {
                    String e = input.readUTF();
                    if(e.equals("exit")){
                        break;
                    }
                } catch (IOException s) {
                    break;
                }
            }
            } else if (splitcommand[0].equals("publish")) {
            clientType = "Publisher";
            ArrayList<MqttClient> clientList = MqttBroker.getAllClients();
            System.out.println("Get message: " + splitcommand[2] + " from Publisher : " + socket.getRemoteSocketAddress());
            for (MqttClient client : clientList) {
                if (client.topic != null && client.topic.equals(splitcommand[1])) {
                    try {
                        System.out.println("Send to the subscriber " + client.id + ": " + splitcommand[2]);
                        client.output.writeUTF("Message: " + splitcommand[2] + "  from publisher: " + socket.getRemoteSocketAddress());
                    } catch (IOException e) {
                        System.out.println("Can't send message to subscriber : " + e.getMessage());
                    }
                }
            }
        }
        try {
            System.out.println("Close connection with "+ clientType  + " : " + socket.getRemoteSocketAddress());
            MqttBroker.removeClient(this);
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
        }
    }

    /*This function return the MAC address of the client running the program*/
//    public static String getMacAddress() {
//        String result = "";
//        try {
//            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
//                byte[] address = ni.getHardwareAddress();
//                if (address != null) {
//                    for (int i = 0; i < address.length; i++)
//                        result += String.format((i == 0 ? "" : "-") + "%02X", address[i]);
//                    return result;
//                }
//            }
//        } catch (SocketException e) {
//            System.out.println("MAC Adress not found");
//            System.exit(1);
//        }
//        return result;
//    }
}
