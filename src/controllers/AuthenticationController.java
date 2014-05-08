package controllers;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.Client;
import utilities.Common.MessageType;

public class AuthenticationController implements Initializable{
	@FXML private Text			messageText;
	@FXML private TextField		fileField;
	private final FileChooser	fileChooser;
	private final Client		client;

	public AuthenticationController(){
		client		= Client.instance();
		fileChooser	= new FileChooser();

		fileChooser.getExtensionFilters().add(
			new ExtensionFilter("*.txt,  *.conf[ig],  *.propert[y|ies]",
				"*.txt", "*.conf", "*.config", "*.properties", "*.property"));
		// initialize fileChooser & set allowed file extensions
	}

	@Override
	public void initialize(URL url, ResourceBundle rb){
		fileField.setFocusTraversable(false);
	}

	private void processAuthenticate(File file){
		fileField.setText(file.getAbsolutePath());

		try{
			client.setS3Client(file);
		}catch(Exception e){
			setMessageText(MessageType.ERROR, e instanceof AmazonS3Exception?
				((AmazonS3Exception)e).getErrorMessage(): e.getMessage());
		}
	}

	public void actionBrowseFile(ActionEvent event){
		clearText();
		File file= fileChooser.showOpenDialog(fileField.getScene().getWindow());

		if(file!= null)
			processAuthenticate(file);
		else
			setMessageText(MessageType.WARN, "No file selected");
	}

	public void actionDragOver(DragEvent event){
		clearText();
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
			File file= dragboard.getFiles().get(0);
			processAuthenticate(file);
			isSuccess= true;
		}

		event.setDropCompleted(isSuccess);
		event.consume();
	}

	public void clearText(){
		fileField.clear();
		messageText.setText("");
	}

	public void setMessageText(MessageType type, String msg){
		messageText.setFill(type.getColor());
		messageText.setText(msg);
	}
}
// controller for login.fxml
