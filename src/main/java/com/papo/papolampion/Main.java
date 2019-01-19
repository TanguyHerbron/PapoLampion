package com.papo.papolampion;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args)
    {
        final Main main = new Main();

        /*new Thread(new Runnable() {
            public void run() {
                main.sub();
            }
        }).start();*/

        main.pub();
    }

    private void sub()
    {
        MqttClient client= null;
        try {
            client = new MqttClient("tcp://mpd.lan:1883", MqttClient.generateClientId());
            client.setCallback( new MQTTCallback() );
            client.connect();
            client.subscribe("laumio/status/advertise");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void pub()
    {
        MqttClient client = null;
        try {
            client = new MqttClient("tcp://mpd.lan:1883", MqttClient.generateClientId());
            client.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload(new byte[]{(byte)0xff, (byte)0x14, (byte)0x93});
            client.publish("laumio/all/fill", message);

            System.out.println("pub");

            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
