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
	private int result;

	@Override
	public void initialize(URL url, ResourceBundle rb){
		result= 0;
	}

	public void actionClickedOk(ActionEvent event){
		result= 1;
		close();
	}

	public void actionClickedCancel(ActionEvent event){
		result= -1;
		close();
	}

	public int getResult(){ return result; }

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
