package de.dcsquare.paho.client.publisher;

import de.dcsquare.paho.client.util.Utils;
import org.eclipse.paho.client.mqttv3.*;
import java.util.Scanner;


public class Publisher {

    public static final String BROKER_URL = "broker_url";
    public static final String TOPIC_MESSAGE = "home/message";
    public static final String message;

    private MqttClient client;


    public Publisher() {

        //Client id = mac adress
        String clientId = Utils.getMacAddress() + "-pub";


        try {

            client = new MqttClient(BROKER_URL, clientId);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void start() {

        try {
            client.connect();
            //Publish data forever
            while (true) {
                publishMessage();
                Thread.sleep(500);
            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void publishMessage() throws MqttException {
        final MqttTopic messageTopic = client.getTopic(TOPIC_MESSAGE);
        Scanner sc = new Scanner(System.in);
        message = sc.nextLine();
        messageTopic.publish(new MqttMessage(message.getBytes()));

        System.out.println("Published data. Topic: " + messageTopic.getName() + "  Message: " + message);
    }

    public static void main(String... args) {
        final Publisher publisher = new Publisher();
        publisher.start();
    }
}
