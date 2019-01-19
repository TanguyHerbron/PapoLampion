package com.papo.papolampion;

import com.papo.lib.Laumio;
import org.eclipse.paho.client.mqttv3.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    private static Set<String> lampList;
    private static Laumio pub;

    public static void main(String[] args)
    {
        lampList = new HashSet<String>();

        try {
            pub = new Laumio("tcp://mpd.lan:1883", new MqttCallback() {
                public void connectionLost(Throwable throwable) {
                    System.out.println("Disconnected");
                }

                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println(">> " + s + " " + new String(mqttMessage.getPayload()) );

                    processReceivedMessage(s, mqttMessage);
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //When a message is sent
                }
            });

            pub.listenTo("laumio/status/advertise");
            pub.listenTo("distance/value");
            pub.listenTo("atmosphere/status");
            pub.listenTo("atmosphere/temperature");
            pub.listenTo("atmosphere/pression");
            pub.listenTo("atmosphere/humidite");
            pub.listenTo("atmosphere/humidite_absolue");

            pub.lookForIDs();
            pub.testAtmo();

            pub.fill("Laumio_10805F", 0, 255, 0);

            pub.fill("Laumio_88813D", 0, 255, 0);

            pub.fill("Laumio_CD0522", 0, 255, 0);

            pub.fill("Laumio_1D9486", 0, 255, 0);

            Thread.sleep(200000);

            pub.close();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void processReceivedMessage(String topic, MqttMessage mqttMessage)
    {
        if(topic.equals("laumio/status/advertise"))
        {
            lampList.add(new String(mqttMessage.getPayload()));
        }

        if(topic.equals("distance/value"))
        {
            try {
                pub.fill("Laumio_1D9486", 0, (int) Math.floor(Double.parseDouble(new String(mqttMessage.getPayload())) * 100), 0);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

}
