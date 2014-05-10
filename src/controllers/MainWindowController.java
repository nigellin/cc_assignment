package controllers;

import com.amazonaws.services.s3.model.S3Object;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainWindowController implements Initializable{
	@FXML private StackPane stackPane;
	@FXML private Button	backButton, forwardButton, parentButton;
	@FXML private VBox		bottomPane;
	@FXML private TableView tableView;
	private final ObservableList<S3Object> data;
	private int currentIndex;

	public MainWindowController(){
		data= FXCollections.observableArrayList();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb){
		currentIndex= 0;
		tableView.setItems(data);
		
	}

	public void actionGoBack(ActionEvent event){

	}

	public void actionGoForward(ActionEvent event){

	}

	public void actionToParentDirectory(ActionEvent event){

	}
}
