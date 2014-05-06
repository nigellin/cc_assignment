package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import utilities.Common;
import utilities.Common.MessageType;

public class LoginController implements Initializable{
	@FXML private Text			messageText;
	@FXML private TextField		accessKeyField;
	@FXML private PasswordField secretKeyField;

	private final FileChooser	fileChooser;

	public LoginController(){
		fileChooser= new FileChooser();
		fileChooser.getExtensionFilters().add(
			new ExtensionFilter("*.txt, *.conf, *.config, *.property, *.properties",
				"*.txt", "*.conf", "*.config", "*.properties", "*.property"));
		// initialize fileChooser & set allowed file extensions
	}

	@Override
	public void initialize(URL url, ResourceBundle rb){}

	public void actionKeyPressed(KeyEvent event){
		clearMessage();
		// clear all message text;
		
		switch(event.getCode()){
			case ENTER:
				processLogin(null);
				break;
				// invoke login if enter key is pressed
				
			case ESCAPE:
				TextField temp= ((TextField) event.getSource());
				temp.setText("");
				break;
				// clear text if escape key is pressed
		}
	}

	public void processImportKeyFile(ActionEvent event){
		clearMessage();
		File keyFile= fileChooser.showOpenDialog(messageText.getScene().getWindow());

		if(keyFile== null){
			setMessage(MessageType.WARN, "No file selected");
			return;
		}// selected file check, show warn message if it is null

		try{
			Common.instance().setPropertFile(keyFile.getPath());

			String accessKey= Common.instance().getProperty("accessKey");
			String secretKey= Common.instance().getProperty("secretKey");
			// get property values
			
			if(accessKey== null || secretKey== null){
				setMessage(MessageType.ERROR, "Required both accessKey & secretKey values");
				return;
			}// check property values of selected file

			accessKeyField.setText(accessKey);
			secretKeyField.setText(secretKey);
			// set values for keyFields
		}catch(IOException ioe){}

		processLogin(event);
	}

	public void processLogin(ActionEvent event){
		clearMessage();

		if(secretKeyField.getText().isEmpty() || accessKeyField.getText().isEmpty()){
			setMessage(MessageType.ERROR, "Both fields are required");
			return;
		}
		// check secretKeyField & accessKeyField
	}

	private void clearMessage(){
		messageText.setText("");
	}
	// clear message text

	private void setMessage(MessageType type, String message){
		Platform.runLater(()->{
			messageText.setFill(type.getColor());
			messageText.setText(message);
		});
	}
	// set message text
}
// controller for login.fxml
