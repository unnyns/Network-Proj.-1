import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MqttBroker
{
  private static int port = 9999;
  private static ServerSocket server;
  private static ArrayList<MqttClient> clients = new ArrayList<MqttClient>();

  public MqttBroker(int Port)
  {
    port = Port;
  }

  static public void main(String[] args)
  {
    server = new ServerSocket(port);
    while(true)
    {
      try
      {
        Socket socket = server.accept();
        System.out.println("Connection: OK");
        MqttClient client = new MqttClient(socket);
        clients.add(client);
        client.start();
      }
      catch(IOException e)
      {
        System.out.println("Something failed.");
        server.close();
        System.exit(0);
      }
    }
  }
}
