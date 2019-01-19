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

    public void setRing(int ring, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'set_pixel'," +
                "'ring': " + ring + ",'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/all/json", message);
    }

    public void setRing(String id, int ring, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'set_pixel'," +
                "'ring': " + ring + ",'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/" + id + "/json", message);
    }

    public void setPixel(int pixelId, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'set_pixel'," +
                "'led': " + pixelId + ",'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/all/json", message);
    }

    public void setPixel(String id, int pixelId, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'set_pixel'," +
                "'led': " + pixelId + ",'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/" + id + "/json", message);
    }

    public void setColumn(int row, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'set_column'," +
                "'column': " + row + ", 'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/all/json", message);
    }

    public void setColumn(String id, int row, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'set_column'," +
                "'column': " + row + ", 'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/" + id + "/json", message);
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

    public void color_wipe(int duration, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'color_wipe'," +
                "'duration': " + duration + "," +
                "'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/all/json", message);
    }

    public void color_wipe(String id, int duration, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'color_wipe'," +
                "'duration': " + duration + "," +
                "'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/" + id + "/json", message);
    }

    public void rainbow() throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'animate_rainbow'}").getBytes());

        client.publish("laumio/all/json", message);
    }

    public void rainbow(String id) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'animate_rainbow'}").getBytes());

        client.publish("laumio/" + id + "/json", message);
    }

    public void fill(String id, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'fill', 'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        client.publish("laumio/" + id + "/json", message);
    }

    public void set_all_columns(int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'commands': [{'command': 'set_column','column': 0,'rgb': [" + R + ", " + G + ", " + B + "]},{'command': 'set_column','column': 1,'rgb': [" + R + ", " + G + ", " + B + "]}]}").getBytes());

        System.out.println(new String(message.getPayload()));

        client.publish("laumio/all/json", message);
    }

    public void close() throws MqttException
    {
        client.disconnect();
    }

    public void testAtmo() throws MqttException {

        client.publish("atmosphere/status/advertise", new MqttMessage());
    }

    public void lookForIDs() throws MqttException {
        client.publish("laumio/all/discover", new MqttMessage());
    }
}
