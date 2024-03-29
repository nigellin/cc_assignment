package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.stage.*;
import utilities.*;

public class InputDialogController implements Initializable{
	@FXML private TextField inputField;
	@FXML private Text		message;

	@Override
	public void initialize(URL url, ResourceBundle rb){}

	public String getResult(){
		String result= inputField.getText();
		inputField.setText("");
		return result;
	}

	public void setMessage(String msg){
		message.setText(msg);
	}

	public void close(){
		((Stage) message.getScene().getWindow()).close();
	}

	public void actionKeyPressed(KeyEvent event){
		if(event.getCode()== KeyCode.ENTER)
			actionClickedOk(event);
	}

	public void actionClickedOk(Event event){
		if(event instanceof KeyEvent)
			if(((KeyEvent)event).getCode()!= KeyCode.ENTER)
				return;

		if(inputField.getText().isEmpty()){
			new DialogWindow().showDialog(Common.MessageType.WARNING, "the value is required", "Input Warning", false);
		}else
			close();
	}

	public void actionClickedCancel(Event event){
		inputField.setText("");

		if(event instanceof KeyEvent)
			if(((KeyEvent)event).getCode()!= KeyCode.ENTER)
				return;

		close();
	}
}
