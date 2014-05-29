package main;

import controllers.*;
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
		views.setPrimaryStage(primaryStage);
		primaryStage.setScene(views.getScene(SceneType.Authentication));
		primaryStage.setTitle(SceneType.Authentication.getTitle());

		primaryStage.setResizable(false);
		primaryStage.show();

		primaryStage.setOnCloseRequest(event->{
			if(new DialogWindow().showDialog(MessageType.WARNING, "Are your sure want to exit?", "Exit Confirmation"))
				System.exit(0);
			else
				event.consume();
		});
	}

	public static void main(String args[]){
		launch(args);
		// launch application
	}
}
