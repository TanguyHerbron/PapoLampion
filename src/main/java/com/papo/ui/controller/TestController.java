package com.papo.ui.controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.papo.lib.Laumio;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestController implements Initializable {
	@FXML private VBox  VBox_Connection;
	@FXML private VBox VBox_Functions;
	@FXML private VBox VBox_Duration;
	@FXML private HBox HBox_Individual;
    @FXML private Button Button_Send;
    @FXML private Button Button_Exit;
    @FXML private Button Button_Connect;
    @FXML private ColorPicker ColorPicker_Color1;
    @FXML private ChoiceBox<String> ChoiceBox_Command;
    @FXML private ListView<String> ListView_Enable;
    @FXML private ListView<String> ListView_Disable;
    @FXML private ProgressBar ProgressBar_Distance;
    @FXML private TextField TextField_IP;
    @FXML private TextField TextField_Port;
    @FXML private Slider Slider_Duration;
    @FXML private Label Label_Log;

    @FXML private Button Button_Prev;
    @FXML private Button Button_Next;
    @FXML private Button Button_Toggle;
    @FXML private Button Button_Stop;
    @FXML private Slider Slider_Volume;
    
    @FXML private CheckBox CheckBox_LED1;
    @FXML private CheckBox CheckBox_LED2;
    @FXML private CheckBox CheckBox_LED3;
    @FXML private CheckBox CheckBox_LED4;
    @FXML private CheckBox CheckBox_LED5;
    @FXML private CheckBox CheckBox_LED6;
    @FXML private CheckBox CheckBox_LED7;
    @FXML private CheckBox CheckBox_LED8;
    @FXML private CheckBox CheckBox_LED9;
    @FXML private CheckBox CheckBox_LED10;
    @FXML private CheckBox CheckBox_LED11;
    @FXML private CheckBox CheckBox_LED12;
    @FXML private CheckBox CheckBox_LED13;
    
    private Set<String> idList;
    
    private Laumio laumio;
	private Color color;
	private List<CheckBox> ledList;

	private Thread policeThread;
    
	public void initialize(URL location, ResourceBundle resources) {
		idList = new HashSet<String>();
		ledList = new ArrayList<CheckBox>();
		
		ledList.add(CheckBox_LED1);
		ledList.add(CheckBox_LED2);
		ledList.add(CheckBox_LED3);
		ledList.add(CheckBox_LED4);
		ledList.add(CheckBox_LED5);
		ledList.add(CheckBox_LED6);
		ledList.add(CheckBox_LED7);
		ledList.add(CheckBox_LED8);
		ledList.add(CheckBox_LED9);
		ledList.add(CheckBox_LED10);
		ledList.add(CheckBox_LED11);
		ledList.add(CheckBox_LED12);
		ledList.add(CheckBox_LED13);
		
		ConnectionInit();
	}
	
	public void ConnectionInit()
	{
		color = new Color(0,0,0,0);
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
			laumio.refreshIDs(new Laumio.IDCallback() {
	                @Override
	                public void onIDReceived(String id) {
	                	int size = idList.size();
	                idList.add(id);
	                if(size < idList.size()) {
	                	AddNewToListDisable(id);
	                }
	                }
	            });
			ListnerInit();
			FunctionsInit();
	}

	public void ListnerInit(){
		
		try {
			laumio.addDisListener(new Laumio.DisCallback() {
				
				@Override
				public void onDistanceChange(float dist) {
					ProgressBar_Distance.setProgress(dist);
				}
			});
			
			laumio.addRemoteListener(new Laumio.RemoteCallback() {
				@Override
				public void onKeyReceived(String key, boolean isOn) {

				    System.out.println(">> " + key + " " + isOn);

				    Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if(isOn)
							{
								Label_Log.setText(key + " as been pressed");
							}
							else
							{
								Label_Log.setText(key + " as been released");
							}
						}
					});
				}
			});

			laumio.addPresListener(new Laumio.PresCallback() {
				@Override
				public void onPresChanged(boolean isSomeoneOnTheThrone) {
					if(isSomeoneOnTheThrone)
					{
						Police();
					}
				}
			});
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void FunctionsInit() {
		VBox_Connection.setVisible(false);
		VBox_Functions.setVisible(true);

		HBox_Individual.setVisible(false);
		//Interface init
		InitButtons();
		InitListView();
		InitChoiceBox();
		ListnerInit();
		InitMedia();
	}

	private void InitMedia()
    {
        Button_Prev.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    laumio.prevMusic();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        Button_Next.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    laumio.nextMusic();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        Button_Stop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    laumio.stopTheFuckingMusic();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        Button_Toggle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    laumio.toggleMusic();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        Slider_Volume.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				try {
					laumio.setVolume(newValue.intValue());
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		});
    }

	public void InitButtons() {
		Button_Send.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				try {
				switch(ChoiceBox_Command.getValue().toString())
				{
				case "Fill":
					color = ColorPicker_Color1.getValue();
						laumio.fill(new HashSet<String>(ListView_Enable.getItems()), (int)Math.floor(color.getRed()*255), (int)Math.floor(color.getGreen()*255), (int)Math.floor(color.getBlue()*255));
				break;
				case "Scanner":
					ScannerAnnimation((int)Math.floor(Slider_Duration.getValue()));
					break;
				case "Rainbow":
					Rainbow();
					break;
				case "Individual":
					Individual();
					break;
				case "Police":
					Police();
					break;
				}
				} catch (MqttException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
        });
		
		Button_Exit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				try {
					Stage stage = (Stage) Button_Exit.getScene().getWindow();
				    stage.close();
					laumio.close();
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
	
	public void InitChoiceBox() {
		ChoiceBox_Command.setItems(FXCollections.observableArrayList(
			    "Fill",
			    "Scanner",
			    "Rainbow",
			    "Individual",
				"Police")
				);
		
		VBox_Duration.setVisible(false);
		
		ChoiceBox_Command.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				switch(ChoiceBox_Command.getItems().get((int) arg2))
				{
				case "Fill":
						SetDurationOff();
						 SetIndividualOff();
					break;
				case "Scanner":
						SetDurationOn();
						 SetIndividualOff();
					break;
				case "Rainbow":
					 SetDurationOff();
					 SetIndividualOff();
					break;

				case "Individual":
					 SetDurationOff();
					 SetIndividualOn();
					break;

				case "Police":
					SetDurationOff();
					SetIndividualOff();
					break;
				}
			}
		    });
	}
	
	public void Individual() throws MqttException
	{

		for(CheckBox item : ledList){
			if(item.isSelected())
			{
				color = ColorPicker_Color1.getValue();
				System.out.println();
				laumio.setPixel(new HashSet<String>(ListView_Enable.getItems()), ledList.indexOf(item), (int)Math.floor(color.getRed()*255), (int)Math.floor(color.getGreen()*255), (int)Math.floor(color.getBlue()*255));
				}
		}
	}

	public void Police()
	{
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					for(int i = 0; i < 10; i++){
						laumio.setPixel(new HashSet<String>(ListView_Enable.getItems()), 9, 255, 255, 255);

						laumio.setColumn(new HashSet<String>(ListView_Enable.getItems()), 0, 255, 0, 0);
						laumio.setColumn(new HashSet<String>(ListView_Enable.getItems()), 1, 0, 0, 255);
						laumio.setColumn(new HashSet<String>(ListView_Enable.getItems()), 2, 255, 0, 0);
						laumio.setColumn(new HashSet<String>(ListView_Enable.getItems()), 3, 0, 0, 255);

						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						laumio.setPixel(new HashSet<String>(ListView_Enable.getItems()), 9, 255, 255, 255);

						laumio.setColumn(new HashSet<String>(ListView_Enable.getItems()), 0, 0, 0, 255);
						laumio.setColumn(new HashSet<String>(ListView_Enable.getItems()), 1, 255, 0, 0);
						laumio.setColumn(new HashSet<String>(ListView_Enable.getItems()), 2, 0, 0, 255);
						laumio.setColumn(new HashSet<String>(ListView_Enable.getItems()), 3, 255, 0, 0);

						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void AddNewToListDisable(String id) {
		ListView_Disable.getItems().add(id);
	}
	
	public void ScannerAnnimation(int duration) throws MqttException{
		color = ColorPicker_Color1.getValue();
		laumio.color_wipe(new HashSet<String>(ListView_Enable.getItems()), duration, (int)Math.floor(color.getRed()*255), (int)Math.floor(color.getGreen()*255), (int)Math.floor(color.getBlue()*255));
	}
	
	public void Rainbow() throws MqttException
	{
		laumio.rainbow(new HashSet<String>(ListView_Enable.getItems()));
	}
	
	public void SetDurationOn()
	{
		if(!VBox_Duration.isVisible())
		{
			VBox_Duration.setVisible(true);
		}
	}
	
	public void SetDurationOff()
	{
		if(VBox_Duration.isVisible())
		{
			VBox_Duration.setVisible(false);
		}
	}
	
	public void SetIndividualOn() {

		if(!HBox_Individual.isVisible())
		{
			HBox_Individual.setVisible(true);
		}
	}
	
	public void SetIndividualOff() {

		if(HBox_Individual.isVisible())
		{
			HBox_Individual.setVisible(false);
		}
	}
	
}
