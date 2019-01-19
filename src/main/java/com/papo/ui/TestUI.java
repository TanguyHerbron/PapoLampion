package com.papo.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestUI extends Application{

    public static void main(String[] args) {
        launch(args);
    }
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TestScreen.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Laumio Tester");
        primaryStage.setScene(new Scene(root, 512, 512));
        primaryStage.show();
	}

}
