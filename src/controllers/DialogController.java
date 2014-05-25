package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utilities.Common.MessageType;

public class DialogController implements Initializable{
	@FXML private ImageView icon;
	@FXML private Text		messageText;
	@FXML private Button	cancelButton;
	private boolean			result;

	@Override
	public void initialize(URL url, ResourceBundle rb){
		result= false;
	}

	public void actionClickedOk(ActionEvent event){
		result= true;
		close();
	}

	public void actionClickedCancel(ActionEvent event){
		result= false;
		close();
	}

	public boolean getResult(){ return result; }
	public void setResult(boolean result){ this.result= result; }
	
	public void setMessage(MessageType type, String msg){
		Platform.runLater(()->{
			icon.setImage(type.getIconImage());
			messageText.setFill(type.getColor());
			messageText.setText(msg);
		});
	}

	public void close(){
		((Stage)messageText.getScene().getWindow()).close();
	}

	public void requireCancelButton(boolean isRequire){
		cancelButton.setVisible(isRequire);
	}
}
