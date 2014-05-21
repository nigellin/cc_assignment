package controllers;

import javafx.scene.input.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilities.Common;
import utilities.Common.MessageType;
import utilities.Common.SceneType;
import views.Views;

public class DialogWindow extends Stage{

	public DialogWindow(){
		super();

		initOwner(Views.instance().getPrimaryStage());
		initModality(Modality.APPLICATION_MODAL);

		addEventHandler(KeyEvent.KEY_PRESSED, event->{
			if(event.getCode()== KeyCode.ESCAPE)
				close();
		});
	}

	public boolean showDialog(String message){ return showDialog(message, SceneType.Dialog.getTitle()); }
	public boolean showDialog(String message, String title){ return showDialog(message, title, true); }
	public boolean showDialog(String message, String title, boolean showCancel){
		return showDialog(MessageType.INFO, message, title, showCancel);
	}

	public boolean showDialog(String message, boolean showCancel){
		return showDialog(message, SceneType.Dialog.getTitle(), showCancel);
	}

	public boolean showDialog(MessageType type, String message){ return showDialog(type, message, SceneType.Dialog.getTitle()); }
	public boolean showDialog(MessageType type, String message, String title){ return showDialog(type, message, title, true); }

	public boolean showDialog(MessageType type, String message, String title, boolean showCancel){
		DialogController controller= (DialogController)Views.instance().getController(Common.SceneType.Dialog);
		
		controller.setMessage(type, message);
		controller.requireCancelButton(showCancel);

		setScene(Views.instance().getScene(SceneType.Dialog));
		setTitle(title);
		showAndWait();

		return controller.getResult();
	}
}
