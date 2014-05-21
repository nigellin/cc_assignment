package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import main.Client;
import utilities.Common;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import javafx.stage.*;
import views.*;

public class MainWindowController implements Initializable{
	@FXML private StackPane stackPane;
	@FXML private Button	backButton,		forwardButton,	homeButton;
	@FXML private Button	uploadButton,	downloadButton, deleteButton;
	@FXML private VBox		bottomPane;

	@FXML private TableView<S3ObjectSummary>	objectTableView;
	@FXML private TableView<Bucket>				bucketTableView;
	@FXML private TableColumn<S3ObjectSummary, String> oNameCol, oSizeCol, oTypeCol,
		oOwnerCol, oDateModifiedCol, oEtagCol;
	@FXML private TableColumn<Bucket, String> bNameCol, bTypeCol, bOwnerCol, bDateCreatedCol;

	private final ObservableList<S3ObjectSummary>	objectList;
	private final ObservableList<Bucket>			bucketList;
	private final FileChooser		fileChooser;
	private final Client			client;


	private String	bucketName, prefix;
	private boolean isBucketViewFront;

	public MainWindowController(){
		objectList	= FXCollections.observableArrayList();
		bucketList	= FXCollections.observableArrayList();
		client		= Client.instance();
		fileChooser	= new FileChooser();
		bucketName	= "";
		prefix		= "";
	}

	@Override
	public void initialize(URL url, ResourceBundle rb){
		bucketTableView.setItems(bucketList);
		bucketTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		objectTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		objectTableView.setItems(objectList);
		objectTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		objectTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void processSelectedItems(Event event){
		boolean isSuccess= false;

		TableView temp= isBucketViewFront ? bucketTableView : objectTableView;

		if(temp.getSelectionModel().isEmpty())
			return;

		if(event instanceof KeyEvent){
			if(((KeyEvent) event).getCode()== KeyCode.ENTER)
				isSuccess= true;

		}else if(event instanceof MouseEvent){
			if(((MouseEvent) event).getClickCount()>= 2)
				isSuccess= true;
		}

		if(isSuccess)
			actionGoInto(null);
	}

	public void actionGoInto(ActionEvent event){
		if(isBucketViewFront){
			if(bucketTableView.getSelectionModel().isEmpty())
				return;

			bucketName= bucketTableView.getSelectionModel().getSelectedItem().getName();
			switchToBuckets(false);
		}else{
			if(objectTableView.getSelectionModel().isEmpty())
				return;

			String filename= objectTableView.getSelectionModel().getSelectedItem().getKey();

			if(filename.endsWith("/")){
				prefix+= Common.getFileName(filename);
				updateObjectList();
			}
		}
	}

	public void actionGoOutTo(ActionEvent event){
		if(isBucketViewFront){

		}else{
			if(prefix.equals("")){
				bucketName= "";
				switchToBuckets(true);
			}else{
				prefix= Common.getParentFile(prefix);
				System.out.println(prefix);
				updateObjectList();;
			}
		}
	}

	public void actionToHome(ActionEvent event){
		switchToBuckets(true);
		updateBucketList();
	}

	public void actionDeleteFiles(ActionEvent event){
		boolean isYes= new DialogWindow().showDialog(Common.MessageType.WARNING, "Are you sure want to delete the selected items", "");

		if(!isBucketViewFront){
			if(isYes){
				objectTableView.getSelectionModel().getSelectedItems().forEach(item-> {
					client.getS3Client().deleteObject(bucketName, item.getKey());
				});

				updateObjectList();
			}
		}else{
			if(isYes){
				bucketTableView.getSelectionModel().getSelectedItems().forEach(item->{
					client.getS3Client().deleteBucket(item.getName());
				});

				updateBucketList();
			}
		}
	}

	public void actionDownloadFiles(ActionEvent event){
		objectTableView.getSelectionModel().getSelectedItems().forEach(item-> {
			String filename= Common.getFileName(item.getKey());
			fileChooser.setInitialFileName(filename);

			File file= fileChooser.showSaveDialog(Views.instance().getPrimaryStage());
			client.getTransferManager().download(bucketName, item.getKey(), file);
		});
	}

	public void actionUploadFiles(ActionEvent event){
        File file = fileChooser.showOpenDialog(Views.instance().getPrimaryStage());

		client.getTransferManager().upload(bucketName, file.getName(), file);
		//client.getS3Client().putObject(bucketName, file.getName(), file);

        updateObjectList();
	}

	public void actionRefreshList(ActionEvent event){
		updateObjectList();
	}

	public void actionShowAbout(ActionEvent event){
		new DialogWindow().showDialog("version 1.0 beta", false);
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
			objectList.addAll(client.getObjectSummaries(bucketName, prefix));

			oNameCol.setCellValueFactory(data-> new SimpleStringProperty(Common.getFileName(data.getValue().getKey())));
			//oTypeCol.setCellValueFactory(data-> new SimpleStringProperty(client.getObjectMetadata(bucketName, data.getValue().getKey()).getContentType()));
			oSizeCol.setCellValueFactory(data-> new SimpleStringProperty(Common.getSizeString(data.getValue().getSize())));
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
