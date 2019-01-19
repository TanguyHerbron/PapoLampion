package com.papo.lib;

import org.eclipse.paho.client.mqttv3.*;

public class Laumio {

    private MqttClient client;

    public Laumio(String address, MqttCallback callback) throws MqttException
    {
        client = new MqttClient(address, MqttClient.generateClientId());
        client.setCallback(callback);
        client.connect();
    }

    public void listenTo(String topic) throws MqttException
    {
        client.subscribe(topic);
    }

    public void fill(int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'fill', 'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/all/json", message);
    }

    public void set_pixel() throws MqttException
    {

    }

    public void close() throws MqttException
    {
        client.disconnect();
    }

}
