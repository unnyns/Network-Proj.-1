import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

public class MqttClient extends Thread
{
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

  /*Constructor of the client, this function will get the MAC address of the
   *client, save the socket given in parameter, and open streams from this
   *socket*/
  public MqttClient(Socket Socket)
  {
    id = getMacAddress();
    socket = Socket;
    if(socket != null)
    {
      try
      {
        input = new DataInputStream(socket.getInputStream());
  			output = new DataOutputStream(socket.getOutputStream());
      }
      catch(IOException e)
      {
        System.out.println("stream connection failed");
      }

    }
  }
  /*Here is the Thread function, we try to get a command, if it's a subscribe
   *command, a loop begin waiting for any publish in the apropiate topic,
   *if it's a publish, we will search all clients register in the topic
   *and send them the data associate with the command*/
  public void run()
  {
    String command = "";
    String[] splitcommand = null;
    String in = "";
    String[] splitInput =null;
    try
    {
      command = input.readUTF();
    }
    catch(IOException e)
    {
      System.out.println("Impossible to get de command");
    }
    if(command != null)
    {
      splitcommand = command.split(" ");
    }
    if((!splitcommand[0].equals("publish") && !splitcommand[0].equals("subscribe")) ||
      (splitcommand.length != 2 && splitcommand.length != 3))
    {
      System.out.println("(MqttClient) Invalid command");
      System.out.println(command);
      System.out.println(splitcommand[0]);
    }
    if(splitcommand[0].equals("subscribe"))
    {
      topic = splitcommand[1];
      while(socket.isConnected());
    }
    else if(splitcommand[0].equals("publish"))
    {
  		ArrayList<MqttClient> clientList = MqttBroker.getClients();
  		System.out.println("Sending message: "+ splitcommand[2]);
  		for (int i = 0; i < clientList.size(); i++)
  			if(clientList.get(i).topic != null && clientList.get(i).topic.equals(splitcommand[1]))
  			{
          try
          {

            System.out.println("Send to the subscriber " + id +": " + splitcommand[2]);
            clientList.get(i).output.writeUTF(splitcommand[2]);
          }
          catch(IOException e){}
  			}
    }
    try
		{
			input.close();
			output.close();
		}
		catch (IOException e){}
  }

  /*This function return the MAC address of the client running the program*/
  public static String getMacAddress()
  {
      String result = "";
      try
      {
          for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces()))
          {
            byte[] address = ni.getHardwareAddress();
            if (address != null)
            {
                for (int i = 0; i < address.length; i++)
                      result += String.format((i == 0 ? "" : "-") + "%02X", address[i]);
                return result;
            }
          }
      }
      catch (SocketException e)
      {
          System.out.println("MAC Adress not found");
          System.exit(1);
      }
      return result;
  }
}
