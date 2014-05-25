package controllers;

import com.amazonaws.*;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.services.s3.model.*;
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
import utilities.Common.MessageType;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.*;

import java.util.*;
import javafx.application.*;

import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.stage.*;
import views.*;

public class MainWindowController implements Initializable{
	@FXML private StackPane stackPane;
	@FXML private Button	backButton,		forwardButton,	homeButton, addButton;
	@FXML private Button	uploadButton,	downloadButton, deleteButton;
	@FXML private VBox		bottomPane;

	@FXML private TableView<S3ObjectSummary>	objectTableView;
	@FXML private TableView<Bucket>				bucketTableView;
	@FXML private TableColumn<S3ObjectSummary, String> oNameCol, oSizeCol, oOwnerCol, oDateModifiedCol, oEtagCol;
	@FXML private TableColumn<Bucket, String> bNameCol, bOwnerCol, bDateCreatedCol;

	private final ObservableList<S3ObjectSummary>	objectList;
	private final ObservableList<Bucket>			bucketList;
	private final FileChooser		fileChooser;
	private final DirectoryChooser	dirChooser;
	private final Client			client;

	private String	bucketName, prefix;
	private boolean isBucketViewFront;

	public MainWindowController(){
		objectList	= FXCollections.observableArrayList();
		bucketList	= FXCollections.observableArrayList();
		client		= Client.instance();
		fileChooser	= new FileChooser();
		dirChooser	= new DirectoryChooser();
		bucketName	= "";
		prefix		= "";
	}

	@Override
	public void initialize(URL url, ResourceBundle rb){
		bucketTableView.setItems(bucketList);
		bucketTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		bucketTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		bucketTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)-> disableButtons());


		objectTableView.setItems(objectList);
		objectTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		objectTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

		objectTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)-> disableButtons());
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
			if(prefix.equals(""))
				switchToBuckets(true);
			else{
				prefix= Common.getParentFile(prefix);
				updateObjectList();
			}
		}
	}

	public void actionToHome(ActionEvent event){
		switchToBuckets(true);
		updateBucketList();
	}

	public void actionDeleteFiles(ActionEvent event){
		boolean isYes= new DialogWindow().showDialog(Common.MessageType.WARNING, "Are you sure want to delete selected items", "Deletion Confirm");

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
					client.getS3Client().listObjects(item.getName()).getObjectSummaries().forEach(i-> {
						client.getS3Client().deleteObject(item.getName(), i.getKey());
					});

					client.getS3Client().deleteBucket(item.getName());
				});

				updateBucketList();
			}
		}
	}

	public void actionAddItem(ActionEvent event){
		String result = new InputDialogWindow().showDialog();
		if (!result.isEmpty()){
			if(isBucketViewFront){
				if(client.getS3Client().doesBucketExist(result)){
					new DialogWindow().showDialog(Common.MessageType.WARNING, "A bucket with this name already exists");
				}else{
					Bucket bucket = client.getS3Client().createBucket(result.toLowerCase());
					updateBucketList();
				}
			}else{
				boolean isConflict	= objectList.stream().anyMatch(item-> result.equals(Common.getFileName(item.getKey())));
				boolean accept		= true;

				if(isConflict)
					accept= new DialogWindow().showDialog(MessageType.WARNING, "Are you want to overwrite existed file");

				if(accept){
					client.getS3Client().putObject(bucketName, result, new File(prefix+ result));
					updateObjectList();
				}
			}
		}
	}

	public void actionDownloadFiles(ActionEvent event){
		File dir= dirChooser.showDialog(Views.instance().getPrimaryStage());
		dirChooser.setTitle("save files to directory...");
		objectTableView.getSelectionModel().getSelectedItems().forEach(item-> downloadFile(item.getKey(), dir.getAbsolutePath()));
	}

	public void actionUploadFiles(ActionEvent event){
		List<File> files = fileChooser.showOpenMultipleDialog(Views.instance().getPrimaryStage());
		files.forEach(file-> uploadFile(file));
	}

	public void actionRefreshList(ActionEvent event){ updateObjectList(); }

	public void actionShowAbout(ActionEvent event){
		new DialogWindow().showDialog("version 1.0 beta", false);
	}

	public void actionDragOver(DragEvent event){
		Dragboard dragboard= event.getDragboard();

		if(dragboard.hasFiles())
			event.acceptTransferModes(TransferMode.COPY);
		else
			event.consume();
	}

	public void actionDragDropped(DragEvent event){
		Dragboard dragboard= event.getDragboard();
		boolean isSuccess= false;

		if(dragboard.hasFiles()){
			dragboard.getFiles().forEach(file-> uploadFile(file));
			isSuccess= true;
		}

		event.setDropCompleted(isSuccess);
		event.consume();
	}

	public void actionDragDetected(MouseEvent event){
//		Dragboard dragboard= objectTableView.startDragAndDrop(TransferMode.COPY);
		ClipboardContent content= new ClipboardContent();

//		List<String> paths= objectTableView.getSelectionModel().getSelectedItems().stream().map(item-> item.getKey()).collect(Collectors.toList());
//		content.putFilesByPath(paths);
//
//		dragboard.setContent(content);

		event.consume();
	}

	public void disableButtons(){
		if(isBucketViewFront){
			backButton.setDisable(true);
			homeButton.setDisable(true);
			forwardButton.setDisable(false);

			TableViewSelectionModel<Bucket> model= bucketTableView.getSelectionModel();

			if(model.isEmpty()){
				deleteButton.setDisable(true);
			}else{
				deleteButton.setDisable(false);
			}
		}else{
			backButton.setDisable(false);
			homeButton.setDisable(false);

			TableViewSelectionModel<S3ObjectSummary> model= objectTableView.getSelectionModel();

			if(model.isEmpty()){
				deleteButton.setDisable(true);
				downloadButton.setDisable(true);
				uploadButton.setDisable(true);
				backButton.setDisable(false);
				homeButton.setDisable(false);
			}else{
				deleteButton.setDisable(false);
			}

//			if(model.getSelectedItem().getKey().endsWith("/"))
//				forwardButton.setDisable(false);
//			else
//				forwardButton.setDisable(true);
		}
	}

	public void updateBucketList(){
		homeButton.setDisable(true);
		forwardButton.setDisable(true);
		backButton.setDisable(true);
		uploadButton.setDisable(true);
		downloadButton.setDisable(true);
		deleteButton.setDisable(true);



		Platform.runLater(()-> {
			bucketList.setAll(client.getBuckets());

			bNameCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getName()));
			bOwnerCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getOwner().getDisplayName()));
			bDateCreatedCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getCreationDate().toString()));
		});
	}

	public void updateObjectList(){
		Platform.runLater(()->{
			objectList.clear();
			homeButton.setDisable(false);
			backButton.setDisable(false);
			uploadButton.setDisable(false);
			downloadButton.setDisable(false);
			forwardButton.setDisable(false);



			if(!bucketName.isEmpty()){
				objectList.addAll(client.getObjectSummaries(bucketName, prefix));

				oNameCol.setCellValueFactory(data-> new SimpleStringProperty(Common.getFileName(data.getValue().getKey())));
				oSizeCol.setCellValueFactory(data-> new SimpleStringProperty(Common.getSizeString(data.getValue().getSize())));
				oOwnerCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getOwner().getDisplayName()));
				oDateModifiedCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getLastModified().toString()));
				oEtagCol.setCellValueFactory(data-> new SimpleStringProperty(data.getValue().getETag()));
			}
		});
	}

	public void downloadFile(String filepath, String parentpath){
		File file= new File(parentpath+ "/"+ filepath);

		if(filepath.endsWith("/")){
			if(!file.exists())
				file.mkdir();

			client.getObjectSummaries(bucketName, prefix, false).stream().
				filter(item-> item.getKey().startsWith(filepath)).
				filter(item-> !item.getKey().equals(filepath)).
				forEach(item-> downloadFile(item.getKey(), parentpath));
		}else{
			if(file.exists())
				return;

			client.getTransferManager().download(bucketName, filepath, file);
		}
	}

	public void uploadFile(File file){
		if(client.getObjectSummaries(bucketName, prefix).stream().anyMatch(item-> item.getKey().endsWith(file.getName()))){
			if(!new DialogWindow().showDialog(MessageType.WARNING, "item '"+ file.getName()+"' already existed within this folder, do you want to overwrite it?", "Overwrite Confirm"))
				return;
		}

		PutObjectRequest request= new PutObjectRequest(bucketName, prefix+ file.getName(), file);
		Upload upload= client.getTransferManager().upload(request);

		request.setGeneralProgressListener(event-> {
			switch(event.getEventCode()){
				case ProgressEvent.COMPLETED_EVENT_CODE:
					updateObjectList();
					break;

				case ProgressEvent.FAILED_EVENT_CODE:
					try{
						AmazonClientException ace= upload.waitForException();
						if(ace!= null)
							throw new InterruptedException(ace.getMessage());
					}catch(InterruptedException ie){ new DialogWindow().showDialog(MessageType.ERROR, ie.getMessage(), "Upload Failed", false); }

					break;
			}
		});
	}

	public void switchToBuckets(boolean toBuckets){
		if(toBuckets){
			bucketTableView.toFront();
			isBucketViewFront= true;

			bucketName	= "";
			prefix		= "";

			Views.instance().getPrimaryStage().setTitle("Buckets");
			updateBucketList();
		}else{
			bucketTableView.toBack();
			isBucketViewFront= false;

			Views.instance().getPrimaryStage().setTitle("Items");
			updateObjectList();
		}
	}
}
