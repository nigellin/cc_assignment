package main;

import javafx.application.Application;
import javafx.stage.Stage;
import views.Views;

public class Program extends Application{
	public Views views;

	public Program(){
		views= Views.instance();
	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setScene(views.getScene(Views.ViewType.Authentication));
		primaryStage.setResizable(false);
		primaryStage.show();

		primaryStage.setOnCloseRequest(event->{ System.exit(0); });
	}

	public static void main(String args[]){
		launch(args);
		// launch application
	}
}
