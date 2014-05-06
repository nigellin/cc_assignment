package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.Common.Views;

public class Program extends Application{
	private HashMap<Views, FXMLLoader> loaders;
	private HashMap<Views, Scene>		scenes;

	public Program(){
		loaders	= new HashMap<>();
		scenes	= new HashMap<>();

		Stream.of(Views.values()).forEach(v-> {
			try{
				loaders.put(v, new FXMLLoader(getClass().getResource(v.getFxmlPath())));
				scenes.put(v, new Scene(loaders.get(v).load()));
			}catch(IOException ex){ ex.printStackTrace(); }
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setScene(scenes.get(Views.Login));
		primaryStage.setResizable(false);
		primaryStage.show();

		primaryStage.setOnCloseRequest(event->{ System.exit(0); });
	}

	public static void main(String args[]){
		launch(args);
	}
}
