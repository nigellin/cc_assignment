package controllers;

import controllers.DialogController;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilities.Common;
import utilities.Common.MessageType;
import views.Views;

public class Dialog extends Stage{
	public Dialog(Stage owner){
		super();

		initOwner(owner);
		initModality(Modality.APPLICATION_MODAL);
	}

	public int showDialog(String message){
		return showDialog(MessageType.NULL, message, "Message Dialog");
	}

	public int showDialog(String message, String title){
		return showDialog(MessageType.NULL, message, title);
	}

	public int showDialog(MessageType type, String message, String title){
		DialogController controller= (DialogController)Views.instance().getController(Common.SceneType.Dialog);
		controller.setMessage(type, message);

		setScene(Views.instance().getScene(Common.SceneType.Dialog));
		setTitle(title);
		showAndWait();

		return controller.getResult();
	}
}
