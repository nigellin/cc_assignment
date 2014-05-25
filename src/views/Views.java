package views;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Stream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.Common.SceneType;

public class Views{
	private final HashMap<SceneType, FXMLLoader>	loaders;
	private final HashMap<SceneType, Scene>			scenes;
	private Stage primaryStage;
	private static Views instance;

	public static Views instance(){
		if(instance== null)
			instance= new Views();

		return instance;
	}

	public Views(){
		loaders	= new HashMap<>();
		scenes	= new HashMap<>();
		// initialize hashmap

		Stream.of(SceneType.values()).forEach(type-> {
			try{
				loaders.put(type, new FXMLLoader(getClass().getResource(type.getFxmlPath())));
				scenes.put(type, new Scene(loaders.get(type).load()));
			}catch(IOException ex){ ex.printStackTrace(); }
		});
		// add values into loaders & scenes
	}

	public void switchScene(SceneType type){
		primaryStage.setTitle(type.getTitle());
		primaryStage.setScene(scenes.get(type));
	}

	public Stage getPrimaryStage(){ return primaryStage; }
	public void setPrimaryStage(Stage stage){ primaryStage= stage; }

	public Scene getScene(SceneType type){
		return scenes.get(type);
	}// get scene from hashmap

	public FXMLLoader getFXMLLoader(SceneType type){
		return loaders.get(type);
	}// get fxmlloader from hashmap

	public Object getController(SceneType type){
		return loaders.get(type).getController();
	}// get controller
}
