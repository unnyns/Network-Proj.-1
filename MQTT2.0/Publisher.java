import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.lang.*;

public class Publisher
{
  public static int port;
  public static String ip;

  public Publisher(String Ip, int Port)
  {
    ip = Ip;
    port = Port;
  }

  public static void main(String[] args) throws IOException
  {
    String[] splitcommand;
    String topic;
    String message;
    Scanner sc = new Scanner(System.in);
    Socket socket = null;
    try
    {
      String scanport = sc.nextLine();
      port = Integer.parseInt(scanport);
    }
    catch(Exception e)
    {
      System.out.println("Invalid port");
    }
    while(true)
    {
      System.out.println("Publish [ServerIp] [Topic] [Data] or Exit");
      String command = sc.nextLine().toLowerCase();
      splitcommand = command.split(" ");
      if(splitcommand.length != 1 && splitcommand.length != 4)
      {
        System.out.println("Invalid command");
      }
      else
      {
        if(splitcommand[0].equals("exit"))
        {
          break;
        }
        else if(splitcommand[0].equals("publish"))
        {
          ip = splitcommand[1];
          topic = splitcommand[2];
          message = splitcommand[3];
          try
          {
            socket = new Socket(ip, port);
            if(socket != null)
            {
              System.out.println("Connected");
              DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        			output.writeUTF("publish " + topic + " " + message);
            }
            else
            {
              System.out.println("There is a problem with the connection.");
            }

          }
          catch(Exception e)
          {
            System.out.println("Impossible to connect");
          }
        }
        else
        {
          System.out.println("You can just Publish or Exit here.");
        }
      }
    }
    if(socket != null)
      socket.close();
  }
}
