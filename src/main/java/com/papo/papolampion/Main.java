package com.papo.papolampion;

import com.papo.lib.Laumio;
import org.eclipse.paho.client.mqttv3.*;

import java.util.*;

public class Main {

    private static Set<String> lampList;
    private static Laumio pub;

    private static boolean isOn = true;

    public static void main(String[] args)
    {
        lampList = new HashSet<String>();

        try {
            pub = new Laumio("tcp://mpd.lan:1883");

            pub.refreshIDs(new Laumio.IDCallback() {
                @Override
                public void onIDReceived(String id) {
                    System.out.println("New id received " + id);
                }
            });

            pub.addBPListener(new Laumio.BPCallback() {
                public void onStatusChanged(boolean isOn) {
                    System.out.println("Sensor online " + isOn);
                }

                public void onLedStatusChanged(int ledNumber, boolean isOn) {
                    System.out.println("Led " + ledNumber + " bright : " + isOn);
                }

                public void onBPStatusChanged(int bpNumber, boolean isOn) {
                    System.out.println("BP " + bpNumber + " pressed : " + isOn);
                }

                public void onRSSIChanged(float db) {
                    System.out.println("WiFi : " + db + "db");
                }

                public void onUptimeChanged(long uptime) {
                    System.out.println("System running for " + uptime + " minutes");
                }
            });

            pub.addRemoteListener(new Laumio.RemoteCallback() {
                public void onKeyReceived(String key, boolean isOn) {
                    System.out.println("Key pressed " + key + " pressed " + isOn);
                }
            });

        } catch (MqttException e) {
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
                pub.fill(new HashSet<>(Arrays.asList("Laumio_1D9486")), 0, (int) Math.floor(Double.parseDouble(new String(mqttMessage.getPayload())) * 100), 0);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        if(topic.equals("remote/power/state") && new String(mqttMessage.getPayload()).equals("OFF"))
        {
            try {
                if(isOn)
                {
                    pub.fill(new HashSet<>(Arrays.asList("Laumio_0FC168")), 0, 0, 0);
                }
                else
                {
                    pub.fill(new HashSet<>(Arrays.asList("Laumio_0FC168")), 255, 255, 255);
                }

                isOn = !isOn;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

}
