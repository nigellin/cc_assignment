package controllers;

import javafx.stage.Modality;
import javafx.stage.Stage;
import utilities.Common;
import utilities.Common.MessageType;
import views.Views;

public class DialogWindow extends Stage{
	public DialogWindow(Stage owner){
		super();

		initOwner(owner);
		initModality(Modality.APPLICATION_MODAL);
	}

	public int showDialog(String message, boolean showCancel){
		return showDialog(MessageType.NULL, message, Common.SceneType.Dialog.getTitle(), showCancel);
	}

	public int showDialog(String message, String title, boolean showCancel){
		return showDialog(MessageType.NULL, message, title, showCancel);
	}

	public int showDialog(MessageType type, String message, String title, boolean showCancel){
		DialogController controller= (DialogController)Views.instance().getController(Common.SceneType.Dialog);
		controller.setMessage(type, message);
		controller.requireCancelButton(showCancel);

		setScene(Views.instance().getScene(Common.SceneType.Dialog));
		setTitle(title);
		showAndWait();

		return controller.getResult();
	}
}
