import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

public class MqttClient
{
  private String id;
  private DataInputStream input;
  private DataOutputStream output;
  private Socket socket;

  public MqttClient(Socket Socket)
  {
    id = getMacAddress();
    socket = Socket;
    if(socket != null)
    {
      input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
    }
  }

  public void start()
  {
    String command;
    String[] splitcommand;
    String in;
    String[] splitInput;
    String topic;
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
    if((splitcommand[0] != "publish" && splitcommand[0] != "subscribe") ||
        (splitcommand.length != 3 && splitcommand.length != 4))
    {
      System.out.println("(MqttClient) Invalid command");
    }
    else if(splitcommand[0] == "subscribe")
    {
      topic = splitcommand[2];
      while(socket.isConnected())
      {
        try
        {
          in = input.readUTF();
          splitInput = in.split(" ");
          if(splitInput[0] == "publish" && splitInput.lenght == 4)
          {
            if(splitInput[2] == topic)
              System.out.println("Client: " + id + "Receive: " + splitInput[3]);
          }
        }
        catch(Exception e)
        {
          
        }
      }
    }
  }

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
