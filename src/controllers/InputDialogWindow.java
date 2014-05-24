package controllers;

import javafx.scene.input.*;
import javafx.stage.*;
import utilities.*;
import views.*;

public class InputDialogWindow extends Stage{
	public InputDialogWindow(){
		super();

		initOwner(Views.instance().getPrimaryStage());
		initModality(Modality.APPLICATION_MODAL);
	}

	public String showDialog(){
		return showDialog("Enter a value:");
	}

	public String showDialog(String message){
		return showDialog(message, Common.SceneType.InputDialog.getTitle());
	}

	public String showDialog(String message, String title){
		InputDialogController controller= (InputDialogController) Views.instance().getController(Common.SceneType.InputDialog);

		addEventHandler(KeyEvent.KEY_PRESSED, event->{
			if(event.getCode()== KeyCode.ESCAPE)
				controller.actionClickedCancel(null);
		});

		controller.setMessage(message);
		setScene(Views.instance().getScene(Common.SceneType.InputDialog));
		setTitle(title);
		showAndWait();

		return controller.getResult();
	}
}
