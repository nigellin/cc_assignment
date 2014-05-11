package controllers;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import main.Client;
import utilities.Common;

public class MainWindowController implements Initializable{
	@FXML private StackPane stackPane;
	@FXML private Button	backButton,		forwardButton,	parentButton;
	@FXML private Button	uploadButton,	downloadButton, deleteButton;
	@FXML private VBox		bottomPane;

	@FXML private TableView objectTableView;
	@FXML private TableView bucketTableView;
	@FXML private TableColumn<S3ObjectSummary, String> oNameCol, oSizeCol, oTypeCol,
		oOwnerCol, oDateModifiedCol, oEtagCol;
	@FXML private TableColumn<Bucket, String> bNameCol, bTypeCol, bOwnerCol, bDateCreatedCol;

	private final ObservableList<S3ObjectSummary> objectList;
	private final ObservableList<Bucket> bucketList;
	private final FileChooser	fileChooser;
	private final Client		client;

	private String	bucketName;
	private boolean isBucketViewFront;

	public MainWindowController(){
		objectList	= FXCollections.observableArrayList();
		bucketList	= FXCollections.observableArrayList();
		client		= Client.instance();
		fileChooser	= new FileChooser();
		bucketName	= "";
	}

	@Override
	public void initialize(URL url, ResourceBundle rb){
		bucketTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		bucketTableView.setItems(bucketList);
		objectTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		objectTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		objectTableView.setItems(objectList);
		objectTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void processSelected(Event event){
		boolean isSuccess= false;

		TableView temp= isBucketViewFront? bucketTableView: objectTableView;

		if(temp.getSelectionModel().isEmpty())
			return;

		if(event instanceof KeyEvent){
			if(((KeyEvent) event).getCode()== KeyCode.ENTER)
				isSuccess= true;

		}else if(event instanceof MouseEvent){
			if(((MouseEvent) event).getClickCount()>= 2)
				isSuccess= true;
		}

		if(isSuccess){
			if(isBucketViewFront){
				bucketName= ((Bucket)bucketTableView.getSelectionModel().getSelectedItem()).getName();
				switchToBuckets(false);
			}else{

			}
		}
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

	public void updateBucketList(){
		bucketList.setAll(client.getBuckets());
		bNameCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getName()));
		bTypeCol.setCellValueFactory(data-> new SimpleStringProperty("Bucket"));
		bOwnerCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getOwner().getDisplayName()));
		bDateCreatedCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getCreationDate().toString()));
	}

	public void updateObjectList(){
		objectList.clear();

		if(!bucketName.isEmpty()){
			objectList.addAll(client.getObjectSummaries(bucketName));
			oNameCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getKey()));
			oTypeCol.setCellValueFactory(data-> new SimpleStringProperty(client.getObjectMetadata(bucketName, data.getValue().getKey()).getContentType()));
			oSizeCol.setCellValueFactory(data-> new SimpleStringProperty(Common.toSizeString(data.getValue().getSize())));
			oOwnerCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getOwner().getDisplayName()));
			oDateModifiedCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getLastModified().toString()));
			oEtagCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getETag()));
		}
	}

	public void switchToBuckets(boolean toBuckets){
		if(toBuckets){
			bucketTableView.toFront();
			isBucketViewFront= true;
			updateBucketList();
		}else{
			bucketTableView.toBack();
			isBucketViewFront= false;
			updateObjectList();
		}
	}
}
