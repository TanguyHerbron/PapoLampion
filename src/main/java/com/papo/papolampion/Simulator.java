package com.papo.papolampion;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Simulator {

    private static MqttClient mqttCli;
    private static MqttCallback callback;
    
	public static void main(String[] args) throws MqttException {
		callback = new MqttCallback() {
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				handleMessage(topic, message);
			}
			
			public void deliveryComplete(IMqttDeliveryToken token) {
				// TODO Auto-generated method stub
				
			}
			
			public void connectionLost(Throwable cause) {
				// TODO Auto-generated method stub
				
			}
		};
		mqttCli = new MqttClient("localhost", MqttClient.generateClientId());
		mqttCli.setCallback(callback);
		mqttCli.connect();
		
		
	}
	
	private static void handleMessage(String topic, MqttMessage message)
	{
		
	}
}
