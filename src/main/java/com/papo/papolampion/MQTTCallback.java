package com.papo.papolampion;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTCallback implements MqttCallback {

    public void connectionLost(Throwable throwable) {
        System.out.println("Connexion lost");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        System.out.println("Message received : " + new String(mqttMessage.getPayload()));
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Connexion established");

    }
}
