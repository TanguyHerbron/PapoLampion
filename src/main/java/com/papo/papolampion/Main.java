package com.papo.papolampion;

import com.papo.lib.Laumio;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Random;

public class Main {

    public static void main(String[] args)
    {
        try {
            Laumio pub = new Laumio("tcp://mpd.lan:1883", new MqttCallback() {
                public void connectionLost(Throwable throwable) {
                    System.out.println("Disconnected");
                }

                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println(">> " + s + " " + new String(mqttMessage.getPayload()));
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    //When a message is sent
                }
            });

            pub.listenTo("laumio/status/advertise");
            pub.listenTo("atmosphere/status");
            pub.listenTo("atmosphere/temperature");
            pub.listenTo("atmosphere/pression");
            pub.listenTo("atmosphere/humidite");
            pub.listenTo("atmosphere/humidite_absolue");

            pub.lookForIDs();
            pub.testAtmo();

            //pub.set_all_columns(10, 255, 0);

            Thread.sleep(1500);

            pub.close();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
