package controllers;

import java.io.File;
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
import utilities.Common.MessageType;

public class LoginController implements Initializable{
	@FXML private Text			messageText;
	@FXML private TextField		accessKeyField;
	@FXML private PasswordField secretKeyField;

	private final FileChooser	fileChooser;

	public LoginController(){
		fileChooser= new FileChooser();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb){}

	public void actionKeyPressed(KeyEvent event){
		clearMessage();
		switch(event.getCode()){
			case ENTER:
				processLogin(null);
				break;
			case ESCAPE:
				TextField temp= ((TextField) event.getSource());
				temp.setText("");
				break;
		}
	}

	public void processImportKeyFile(ActionEvent event){
		clearMessage();
		File keyFile= fileChooser.showOpenDialog(messageText.getScene().getWindow());

		if(keyFile== null){
			setMessage(MessageType.WARN, "No file selected");
			return;
		}

		processLogin(event);
	}

	public void processLogin(ActionEvent event){
		clearMessage();

		if(secretKeyField.getText().isEmpty() || accessKeyField.getText().isEmpty()){
			setMessage(MessageType.ERROR, "Both fields are required");
			return;
		}
	}

	private void clearMessage(){
		messageText.setText("");
	}

	private void setMessage(MessageType type, String message){
		Platform.runLater(()->{
			messageText.setFill(type.getColor());
			messageText.setText(message);
		});
	}
}
