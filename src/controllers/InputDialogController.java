package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.stage.*;
<<<<<<< HEAD
=======
import utilities.*;
>>>>>>> FETCH_HEAD

public class InputDialogController implements Initializable{
	@FXML private TextField inputField;
	@FXML private Text		message;

	@Override
	public void initialize(URL url, ResourceBundle rb){}

	public String getResult(){
<<<<<<< HEAD
		return inputField.getText();
=======
		String result= inputField.getText();
		inputField.setText("");
		return result;
>>>>>>> FETCH_HEAD
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
<<<<<<< HEAD
		close();
=======
		if(inputField.getText().isEmpty()){
			new DialogWindow().showDialog(Common.MessageType.WARNING, "the value is required", "Input Warning", false);
		}else
			close();
>>>>>>> FETCH_HEAD
	}

	public void actionClickedCancel(ActionEvent event){
		inputField.setText("");
		close();
	}
}
