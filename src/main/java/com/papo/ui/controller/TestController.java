package com.papo.ui.controller;


import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.application.Platform;

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
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestController implements Initializable {
	@FXML private VBox  VBox_Connection;
	@FXML private VBox VBox_Functions;
    @FXML private Button Button_Send;
    @FXML private Button Button_Exit;
    @FXML private Button Button_Connect;
    @FXML private ColorPicker ColorPicker_Color1;
    @FXML private ChoiceBox<String> ChoiceBox_LaumioID;
    @FXML private ChoiceBox<String> ChoiceBox_Command;
    @FXML private ListView<String> ListView_Enable;
    @FXML private ListView<String> ListView_Disable;
    @FXML private ProgressBar ProgressBar_Distance;
    @FXML private TextField TextField_IP;
    @FXML private TextField TextField_Port;
    
    
    private Laumio laumio;
    
	public void initialize(URL location, ResourceBundle resources) {
		ConnectionInit();
	}
	
	public void ConnectionInit()
	{
		VBox_Functions.setVisible(false);
		
		Button_Connect.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				String connectStr = new String("tcp://" + TextField_IP.getText() + ":" + TextField_Port.getText());
				System.out.println(connectStr);
				try {
					Connect(connectStr);
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
        });
	}
	
	public void Connect(String ip) throws MqttException
	{
			laumio = new Laumio(ip);
			ListnerInit();
			FunctionsInit();
	}

	public void ListnerInit() throws MqttException {
        laumio.listenTo("laumio/status/advertise");
        laumio.listenTo("distance/value");
        laumio.lookForIDs();
	}
	
	public void FunctionsInit() {
		VBox_Connection.setVisible(false);
		VBox_Functions.setVisible(true);
		
		//Interface init
		InitButtons();
		InitListView();
	}
	
	public void MessageHandler(String topic, MqttMessage mqttMessage)
	{/*
        if(topic.equals("laumio/status/advertise"))
        {
        	int size = idList.size();
            idList.add(new String(mqttMessage.getPayload()));
            if(size < idList.size()) {
            	AddNewToListDisable(new String(mqttMessage.getPayload()));
            }
        }

        if(topic.equals("distance/value"))
        {
        	ProgressBar_Distance.setProgress(Double.parseDouble(new String(mqttMessage.getPayload())));
        }*/
	}

	public void InitButtons() {
		/*
		Button_Send.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				color = ColorPicker_Color1.getValue();
				try {
					laumio.fill(new HashSet<String>(ListView_Enable.getItems()), (int)Math.floor(color.getRed()*255), (int)Math.floor(color.getGreen()*255), (int)Math.floor(color.getBlue()*255));
					//laumio.fill((int)Math.floor(color.getRed()*255), (int)Math.floor(color.getGreen()*255), (int)Math.floor(color.getBlue()*255));
					System.out.println(color.getRed());
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        });*/
		
		Button_Exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					laumio.close();
					Stage stage = (Stage) Button_Exit.getScene().getWindow();
				    stage.close();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
        });
	}
	
	public void InitListView() {
		ListView_Disable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
		    if (newValue != null) {

		        Platform.runLater(() -> {
		        	ListView_Disable.getSelectionModel().select(-1);
		            ListView_Enable.getItems().add(newValue);
		            ListView_Disable.getItems().remove(newValue);
		        });
		    }
		});
		
		ListView_Enable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
		    if (newValue != null) {

		        Platform.runLater(() -> {
		        	ListView_Enable.getSelectionModel().select(-1);
		        	ListView_Disable.getItems().add(newValue);
		        	ListView_Enable.getItems().remove(newValue);
		        });
		    }
		});
	}
	
	public void AddNewToListDisable(String id) {
		ListView_Disable.getItems().add(id);
	}
	
}
