package com.papo.papolampion;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args)
    {
        final Main main = new Main();

        new Thread(new Runnable() {
            public void run() {
                main.sub();
            }
        }).start();

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
        Byte command = Byte.parseByte("0xFF");
        Byte r = Byte.parseByte("0xFF");
        Byte g = Byte.parseByte("0X14");
        Byte b = Byte.parseByte("0x93");

        MqttClient client = null;
        try {
            client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
            client.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload(new byte[]{r, g, b});
            client.publish("laumio/all/fill", message);
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
