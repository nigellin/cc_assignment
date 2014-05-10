package main;

import controllers.DialogWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import utilities.Common.*;
import views.*;

public class Program extends Application{
	public Views views;

	public Program(){
		views= Views.instance();
	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setScene(views.getScene(SceneType.MainWindow));
		primaryStage.setTitle(SceneType.MainWindow.getTitle());

		primaryStage.setResizable(false);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(event->{ System.exit(0); });
	}

	public static void main(String args[]){
		launch(args);
		// launch application
	}
}
