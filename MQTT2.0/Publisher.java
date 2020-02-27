import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.lang.*;

public class Publisher extends Thread
{
  public static int port;
  public static String ip;

  /*Constructor for the Publisher*/
  public Publisher(String Ip, int Port)
  {
    ip = Ip;
    port = Port;
  }

  public static void main(String[] args) throws IOException
  {
    String[] splitcommand; //Array used to split the string command
    String topic; //The topic that the user wrote
    String message; //The data that the user wrote
    Scanner sc = new Scanner(System.in);
    Socket socket = null; //Socket which make the connection with the server
    try
    {
      System.out.println("Enter the port (Ex: 9999)");
      String scanport = sc.nextLine();//Try to take the port
      port = Integer.parseInt(scanport);//Parse the port to have an int
    }
    catch(Exception e)
    {
      System.out.println("Invalid port"); //If the user doesn't write a number
    }
    while(true)
    {
      System.out.println("Publish [ServerIp] [Topic] [Data] or Exit");
      String command = sc.nextLine().toLowerCase(); //Scan the publish command
      splitcommand = command.split(" "); //Split the command
      //The command should have 1 or 4 arguments
      if(splitcommand.length != 1 && splitcommand.length != 4)
      {
        System.out.println("Invalid command");
      }
      else
      {
        if(splitcommand[0].equals("exit"))
        {
          break; //stop the loop
        }
        else if(splitcommand[0].equals("publish"))
        {
          ip = splitcommand[1];
          topic = splitcommand[2];
          message = splitcommand[3];
          try
          {
            socket = new Socket(ip, port); //Try to connect with the server
            if(socket != null)
            {
              System.out.println("Connected");
              //Open the output stream and send the command
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
          System.out.println("You can only Publish or Exit here.");
        }
      }
    }
    if(socket != null) //If the socket is not null we close it
      socket.close();
  }
}
