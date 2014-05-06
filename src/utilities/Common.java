package utilities;

import javafx.scene.paint.Color;

public class Common{
	private static Common instance;

	public Common instance(){
		if(instance== null)
			instance= new Common();

		return instance;
	}

	public enum Views{
		Login;
		public String getFxmlPath(){ return "/views/"+ name()+ ".fxml"; }
	}

	public enum MessageType{
		INFO(Color.CORNFLOWERBLUE),
		ERROR(Color.RED),
		WARN(Color.DARKORANGE),
		NULL(Color.BLACK);

		private final Color color;
		private MessageType(Color c){ color= c; }
		public final Color getColor(){ return color; }
	}
}
