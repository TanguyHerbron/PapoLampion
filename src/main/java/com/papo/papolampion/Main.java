package com.papo.papolampion;

import com.papo.lib.Laumio;
import org.eclipse.paho.client.mqttv3.*;

public class Main {

    public static void main(String[] args)
    {
        try {
            Laumio pub = new Laumio("tcp://mpd.lan:1883", new MqttCallback() {
                public void connectionLost(Throwable throwable) {
                    System.out.println("Disconnected");
                }

                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    System.out.println(">> " + new String(mqttMessage.getPayload()));
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("Message sent");
                }
            });

            int i = 0;

            while(i < 255)
            {
                pub.fill(i, i, i);

                Thread.sleep(100);

                i++;
            }

            pub.close();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
