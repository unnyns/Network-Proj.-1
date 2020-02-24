import java.io.*;
import java.net.*;

public class MqttClient
{
  private String IP = "";
  private String topic_name = "";
  private String clientID = "";
  private String port = "";

  public MqttClient(String ip, String clientId)
  {
    String[] split = ip.split(":");
    IP = split[0];
    port = split[1];
    clientID = clientId;
  }

  public Connect()
  {
    try
    {
      Socket sock = new Socket(IP, (int)port);
    }
    catch(UnknownHostException e)
    {

    }
  }

}
