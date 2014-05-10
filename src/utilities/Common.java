package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Common{
	private static	Common		instance;
	private final	Properties	properties;

	public Common(){
		properties= new Properties();
	}

	public static Common instance(){
		if(instance== null)
			instance= new Common();

		return instance;
	} // singleton method

	public void setPropertyFile(String filepath) throws IOException{
		this.setPropertFile(new File(filepath));
	}

	public void setPropertFile(File file) throws IOException{
		if(!file.isFile())
			throw new IOException("Invalid file");

		try(InputStream input= new FileInputStream(file)){
			properties.load(input);
		}catch(IOException ioe){
			throw new IOException(ioe.getMessage());
		}
	}// set property file

	public String getProperty(String key){
		return properties.getProperty(key);
	}// get property values

	public enum FileActions{
		DELETE, CREATE, DOWNLOAD, MODIFY;
	}

	public enum MessageType{
		INFO(Color.CORNFLOWERBLUE, "info"),
		ERROR(Color.RED, "close"),
		WARNING(Color.DARKORANGE, "warning"),
		NULL(Color.BLACK, "bubble");

		private final String iconName;
		private final Color color;
		private MessageType(Color c, String i){
			color	= c;
			iconName= i;
		}
		public final Color getColor(){ return color; }
		public final Image getIconImage(){ return new Image("views/icons/"+ iconName+ ".png"); }
	}

	public enum SceneType{
		Authentication("Authentication - import key property file"),
		Dialog("Message Dialog"),
		MainWindow("Main Window");

		private final String title;
		private SceneType(String t){ title= t; }
		public String getTitle(){ return title; }
		public String getFxmlPath(){ return "/views/"+ name()+ ".fxml"; }
	}

	public static final String[] FILE_EXTENSIONS= {	"*.txt", "*.conf", "*.config", "*.properties", "*.property"};
}
