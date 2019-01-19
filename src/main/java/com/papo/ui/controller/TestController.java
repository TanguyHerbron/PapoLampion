package com.papo.ui.controller;


import java.net.URL;
import java.util.ResourceBundle;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.papo.lib.Laumio;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class TestController implements Initializable {
    @FXML private Button Button_Send;
    @FXML private ColorPicker ColorPicker_Color1;
    @FXML private ChoiceBox ChoiceBox_LaumioID;
    @FXML private ChoiceBox ChoiceBox_Command;
    
    Color color;
    
    private Laumio laumio;
    
	public void initialize(URL location, ResourceBundle resources) {
		try {
			laumio = new Laumio("tcp://mpd.lan:1883", new MqttCallback() {
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
			
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Button_Send.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				color = ColorPicker_Color1.getValue();
				try {
					laumio.fill((int)Math.floor(color.getRed()*255), (int)Math.floor(color.getGreen()*255), (int)Math.floor(color.getBlue()*255));
					System.out.println(color.getRed());
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        });
	}


}
