package controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
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

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

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
	private Object selectedFile;
	static AmazonS3       s3;



	public MainWindowController(){
		objectList	= FXCollections.observableArrayList();
		bucketList	= FXCollections.observableArrayList();
		client		= Client.instance();
		fileChooser	= new FileChooser();
		bucketName	= "";
		
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
    s3  = new AmazonS3Client(credentials);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb){
		bucketTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		bucketTableView.setItems(bucketList);
		objectTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		objectTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		objectTableView.setItems(objectList);
		objectTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
	    objectTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
	        @Override
	        public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
	            //Check whether item is selected and set value of selected item to Label
	            if(objectTableView.getSelectionModel().getSelectedItem() != null) 
	            {    
	               TableViewSelectionModel selectionModel = objectTableView.getSelectionModel();
	               ObservableList selectedCells = selectionModel.getSelectedCells();
	               TablePosition tablePosition = (TablePosition) selectedCells.get(0);

	               
	               selectedFile = objectTableView.getItems().get(objectTableView.getSelectionModel().getSelectedIndex());
//	               System.out.println("Selected Value" + ((S3ObjectSummary) hi).getKey());
	               
	               
	             }
	             }
	         });
	}

	public void processSelectedItems(Event event){
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
		
		s3.deleteObject(String.valueOf(bucketName), String.valueOf(((S3ObjectSummary) selectedFile).getKey()));
		
		updateObjectList();
		
	}

	public void actionDownloadFiles(ActionEvent event){
		FileChooser fileChooser = new FileChooser();
		  
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        
        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        
        if(file != null){
        	File localFile = new File(String.valueOf(((S3ObjectSummary) selectedFile).getKey()));
        	
        	s3.getObject(
    		        new GetObjectRequest(String.valueOf(bucketName), String.valueOf(((S3ObjectSummary) selectedFile).getKey())), localFile
    		        );
            
        }
//    	s3.getObject(
//		        new GetObjectRequest(String.valueOf(bucketName), String.valueOf(((S3ObjectSummary) selectedFile).getKey())),
//		        new File("~/Downloads" + String.valueOf(((S3ObjectSummary) selectedFile).getKey()))		        
//		        );

	}
	


	public void actionUploadFiles(ActionEvent event){
		File file;
		FileChooser fileChooser = new FileChooser();
		 
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Files (*.*)", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
       
        file = fileChooser.showOpenDialog(null);
        
        s3.putObject(String.valueOf(bucketName), file.getName(), file);
        
        updateObjectList();
       
	}

	public void actionOpenSettings(ActionEvent event){

	}

	public void actionRefreshList(ActionEvent event){
		updateObjectList();
	}

	public void actionShowAbout(ActionEvent event){
		new DialogWindow().showDialog("version 0.2 alpha", false);
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
