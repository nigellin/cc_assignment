package controllers;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class MainWindowController implements Initializable{
	@FXML private StackPane stackPane;
	@FXML private Button	backButton,		forwardButton,	parentButton;
	@FXML private Button	uploadButton,	downloadButton, deleteButton;
	@FXML private VBox		bottomPane;
	@FXML private TableView tableView;
	@FXML private TableColumn<S3Object, String> nameCol, sizeCol, typeCol,
		ownerCol, dateModifiedCol, etagCol;

	private final ObservableList<S3ObjectSummary> data;
	private final FileChooser fileChooser;

	public MainWindowController(){
		data		= FXCollections.observableArrayList();
		fileChooser	= new FileChooser();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb){
		tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		tableView.setItems(data);

	}

	public void actionGoBack(ActionEvent event){

	}

	public void actionGoForward(ActionEvent event){

	}

	public void actionToParentDirectory(ActionEvent event){

	}

	public void actionDeleteFiles(ActionEvent event){

	}

	public void actionDownloadFiles(ActionEvent event){

	}

	public void actionUploadFiles(ActionEvent event){

	}

	public void actionOpenSettings(ActionEvent event){

	}

	public void actionRefreshList(ActionEvent event){

	}

	public void actionShowAbout(ActionEvent event){
		new DialogWindow().showDialog("version 0.1 alpha", false);
	}

	public void actionDragOver(DragEvent event){
		Dragboard gragboard= event.getDragboard();

	}

	public void actionDragDropped(DragEvent event){
		Dragboard gragboard= event.getDragboard();
		boolean isSuccess= false;
	}
}
