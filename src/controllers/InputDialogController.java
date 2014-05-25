package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class InputDialogController implements Initializable{
	@FXML private TextField inputField;
	@FXML private Text		message;

	@Override
	public void initialize(URL url, ResourceBundle rb){}

	public String getResult(){
		return inputField.getText();
	}

	public void setMessage(String msg){
		message.setText(msg);
	}

	public void close(){
		((Stage) message.getScene().getWindow()).close();
	}

	public void actionKeyPressed(KeyEvent event){
		if(event.getCode()== KeyCode.ENTER)
			close();
	}

	public void actionClickedOk(ActionEvent event){
		close();
	}

	public void actionClickedCancel(ActionEvent event){
		inputField.setText("");
		close();
	}
}
