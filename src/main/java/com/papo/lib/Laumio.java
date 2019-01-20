package com.papo.lib;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import java.util.Set;

public class Laumio implements MqttCallback {

    private MqttClient client;

    private BPCallback bpCallback;
    private RemoteCallback remoteCallback;
    private IDCallback idCallback;
    private DisCallback disCallback;

    public Laumio(String address) throws MqttException
    {
        client = new MqttClient(address, MqttClient.generateClientId());
        client.setCallback(this);
        client.connect();

        client.subscribe("laumio/status/advertise");
    }

    public void listenTo(String topic) throws MqttException
    {
        client.subscribe(topic);
    }

    public void refreshIDs(IDCallback callback) throws MqttException
    {
        idCallback = callback;

        client.publish("laumio/all/discover", new MqttMessage());
    }

    public void addBPListener(BPCallback callback) throws MqttException
    {
        bpCallback = callback;

        client.subscribe("capteur_bp/status");
        client.subscribe("capteur_bp/switch/1/state");
        client.subscribe("capteur_bp/switch/2/state");
        client.subscribe("capteur_bp/switch/3/state");
        client.subscribe("capteur_bp/switch/4/state");
        client.subscribe("capteur_bp/binary_sensor/1/state");
        client.subscribe("capteur_bp/binary_sensor/2/state");
        client.subscribe("capteur_bp/binary_sensor/3/state");
        client.subscribe("capteur_bp/binary_sensor/4/state");
        client.subscribe("capteur_bp/sensor/bp_rssi/state");
        client.subscribe("capteur_bp/sensor/uptime_sensor/state");
    }

    public void addDisListener(DisCallback callback) throws MqttException
    {
        disCallback = callback;

        client.subscribe("distance/value");
    }

    public void addRemoteListener(RemoteCallback callback) throws MqttException
    {
        remoteCallback = callback;

        client.subscribe("remote/power/state");
        client.subscribe("remote/mode/state");
        client.subscribe("remote/mute/state");
        client.subscribe("remote/playp/state");
        client.subscribe("remote/prev/state");
        client.subscribe("remote/next/state");
        client.subscribe("remote/eq/state");
        client.subscribe("remote/minus/state");
        client.subscribe("remote/plus/state");
        client.subscribe("remote/0/state");
        client.subscribe("remote/chg/state");
        client.subscribe("remote/u_sd/state");
        client.subscribe("remote/1/state");
        client.subscribe("remote/2/state");
        client.subscribe("remote/3/state");
        client.subscribe("remote/4/state");
        client.subscribe("remote/5/state");
        client.subscribe("remote/6/state");
        client.subscribe("remote/7/state");
        client.subscribe("remote/8/state");
        client.subscribe("remote/9/state");
    }

    public void parseIDMessage(MqttMessage mqttMessage)
    {
        idCallback.onIDReceived(new String(mqttMessage.getPayload()));
    }

    public void parseRemoteMessage(String topic, MqttMessage mqttMessage)
    {
        String key = topic.substring(topic.indexOf("/") + 1, topic.lastIndexOf("/"));

        remoteCallback.onKeyReceived(key, new String(mqttMessage.getPayload()).equals("ON"));
    }

    public void parseBPMessage(String topic, MqttMessage mqttMessage)
    {
        if(topic.contains("status"))
        {
            bpCallback.onStatusChanged(new String(mqttMessage.getPayload()).equals("online"));
        }

        if(topic.contains("switch"))
        {
            bpCallback.onLedStatusChanged(Integer.parseInt(topic.substring(topic.lastIndexOf("h/"), topic.lastIndexOf("/"))), new String(mqttMessage.getPayload()).equals("ON"));
        }

        if(topic.contains("binary_sensor"))
        {
            bpCallback.onBPStatusChanged(Integer.parseInt(topic.substring(topic.lastIndexOf("r/"), topic.lastIndexOf("/"))), new String(mqttMessage.getPayload()).equals("ON"));
        }

        if(topic.contains("bp_rssi"))
        {
            bpCallback.onRSSIChanged(Float.parseFloat(new String(mqttMessage.getPayload())));
        }

        if(topic.contains("uptime_sensor"))
        {
            bpCallback.onUptimeChanged(Long.parseLong(new String(mqttMessage.getPayload())));
        }
    }

    public void parseDistMessage(String topic, MqttMessage mqttMessage)
    {
        if(topic.contains("value"))
        {
            disCallback.onDistanceChange(Float.parseFloat(new String(mqttMessage.getPayload())));
        }
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

    public void setRing(Set<String> ids, int ring, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'set_pixel'," +
                "'ring': " + ring + ",'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        for(String id : ids)
        {
            client.publish("laumio/" + id + "/json", message);
        }
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

    public void setPixel(Set<String> ids, int pixelId, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'set_pixel'," +
                "'led': " + pixelId + ",'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        for(String id : ids)
        {
            client.publish("laumio/" + id + "/json", message);
        }
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

    public void setColumn(Set<String> ids, int row, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'set_column'," +
                "'column': " + row + ", 'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        for(String id : ids)
        {
            client.publish("laumio/" + id + "/json", message);
        }
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

    public void fill(String id, int R, int G, int B) throws MqttException
    {

    }

    public void fill(Set<String> ids, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'fill', 'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        for(String id : ids)
        {
            client.publish("laumio/" + id + "/json", message);
        }
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

    public void color_wipe(Set<String> ids, int duration, int R, int G, int B) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'color_wipe'," +
                "'duration': " + duration + "," +
                "'rgb': [" +
                R + ", " +
                G + ", " +
                B + "]}").getBytes());

        for(String id : ids)
        {
            client.publish("laumio/" + id + "/json", message);
        }
    }

    public void rainbow() throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'animate_rainbow'}").getBytes());

        client.publish("laumio/all/json", message);
    }

    public void rainbow(Set<String> ids) throws MqttException
    {
        MqttMessage message = new MqttMessage();
        message.setPayload(("{'command': 'animate_rainbow'}").getBytes());

        for(String id : ids)
        {
            client.publish("laumio/" + id + "/json", message);
        }
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

    public void stockTheFuckingMusic() throws MqttException {
        client.publish("music/control/stop", new MqttMessage());
    }

    public void nextMusic() throws MqttException {
        client.publish("music/control/next", new MqttMessage());
    }

    public void connectionLost(Throwable throwable) {

    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        if(s.startsWith("capteur_bp") && bpCallback != null)
        {
            parseBPMessage(s, mqttMessage);
        }
        else if(s.startsWith("remote")) {
            parseRemoteMessage(s, mqttMessage);
        }
        else if(s.equals("laumio/status/advertise")) {
            parseIDMessage(mqttMessage);
        }
        else if(s.startsWith("distance")) {
            parseDistMessage(s, mqttMessage);
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public interface BPCallback {
        void onStatusChanged(boolean isOn);
        void onLedStatusChanged(int ledNumber, boolean isOn);
        void onBPStatusChanged(int bpNumber, boolean isOn);
        void onRSSIChanged(float db);
        void onUptimeChanged(long uptime);
    }

    public interface RemoteCallback {
        void onKeyReceived(String key, boolean isOn);
    }

    public interface IDCallback {
        void onIDReceived(String id);
    }

    public interface DisCallback {
        void onDistanceChange(float dist);
    }
}
