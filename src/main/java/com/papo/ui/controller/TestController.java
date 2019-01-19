package com.papo.ui.controller;


import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.value.ChangeListener ;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.papo.lib.Laumio;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.paint.Color;

public class TestController implements Initializable {
    @FXML private Button Button_Send;
    @FXML private ColorPicker ColorPicker_Color1;
    @FXML private ChoiceBox<String> ChoiceBox_LaumioID;
    @FXML private ChoiceBox<String> ChoiceBox_Command;
    @FXML private ListView<String> ListView_Enable;
    @FXML private ListView<String> ListView_Disable;
    @FXML private ProgressBar ProgressBar_Distance;
    
    private Set<String> idList;
    Color color;
    
    private Laumio laumio;
    
	public void initialize(URL location, ResourceBundle resources) {
		
		//ListView_Enable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		idList = new HashSet<String>();
		
		try {
			laumio = new Laumio("tcp://mpd.lan:1883", new MqttCallback() {
			    public void connectionLost(Throwable throwable) {
			        System.out.println("Disconnected");
			    }

			    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
			        //System.out.println(">> " + s + " " + new String(mqttMessage.getPayload()));
			        MessageHandler(s, mqttMessage);
			    }

			    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
			        //When a message is sent
			    }
			});
			
			
	        laumio.listenTo("laumio/status/advertise");
	        laumio.listenTo("distance/value");
	        laumio.lookForIDs();
	        
	        
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListView_Enable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(oldValue != null && oldValue != "")
				{
					EnableItem(newValue);
				}
				ListView_Enable.getSelectionModel().clearSelection();
			}
        }); 
		
		ListView_Disable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub

				if(oldValue != null && oldValue != "")
				{
					DisableItem(newValue);
				}
				ListView_Disable.getSelectionModel().clearSelection();
			}
        }); 
		
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
	
	public void MessageHandler(String topic, MqttMessage mqttMessage)
	{
        if(topic.equals("laumio/status/advertise"))
        {
        	int size =idList.size();
            idList.add(new String(mqttMessage.getPayload()));
            if(size < idList.size()) {
            	AddNewToListDisable(new String(mqttMessage.getPayload()));
            }
            else
            	{
            	if(size > idList.size()) {
                	RemoveInListDisable(new String(mqttMessage.getPayload()));
                }
            	}
        }

        if(topic.equals("distance/value"))
        {
        	ProgressBar_Distance.setProgress((int)Math.floor(Double.parseDouble(new String(mqttMessage.getPayload()))));
        	 //(int)Math.floor(Double.parseDouble(new String(mqttMessage.getPayload())));
        }
	}

	public void AddNewToListEnable(String id) {
		ListView_Enable.getItems().add(id);
	}
	
	public void RemoveInListEnable(String id)
	{
		ListView_Enable.getItems().remove(id);
	}
	
	public void AddNewToListDisable(String id) {
		ListView_Disable.getItems().add(id);
	}
	
	public void RemoveInListDisable(String id)
	{
		ListView_Disable.getItems().remove(id);
	}

	public void	EnableItem(String id)
	{
		RemoveInListEnable(id);
		AddNewToListDisable(id);
	}
	
	public void	DisableItem(String id)
	{
		RemoveInListDisable(id);
		AddNewToListEnable(id);
	}
}
