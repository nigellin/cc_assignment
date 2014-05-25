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

	public static String getFileName(String filepath){
		if(filepath.contains("/"))
			if(filepath.endsWith("/"))
				filepath= filepath.substring(filepath.substring(0, filepath.length()- 1).lastIndexOf("/")+ 1);
			else
				filepath= filepath.substring(filepath.lastIndexOf("/")+ 1);

		return filepath;
	}

	public static String getParentFile(String filepath){
		if(!filepath.contains("/"))
			return "";

		if(filepath.endsWith("/"))
			filepath= filepath.substring(0, filepath.length()- 1);

		return filepath.substring(0, filepath.lastIndexOf("/")+ 1);
	}

	public static boolean isSubFile(String filename, String prefix){
		if(!filename.contains(prefix) || filename.equals(prefix))
			return false;

		String temp= filename.substring(prefix.length());

		if(!temp.contains("/"))
			return true;

		if(temp.endsWith("/") && temp.indexOf("/")== temp.lastIndexOf("/"))
			return true;

		return false;
	}

	public static String getSizeString(double size){
		String suffix= "bytes";

		if(size== 0.0)
			return "--";

		if(size>= 1024){
			size/= 1024;
			suffix= "KB";
		}else if(size>= 1000* 1000){
			size/= 1024* 1024;
			suffix= "MB";
		}else if(size>= Math.pow(1000, 3)){
			size/= Math.pow(1024, 3);
			suffix= "GB";
		}else if(size>= Math.pow(1000, 4)){
			size/= Math.pow(1024, 4);
			suffix= "TB";
		}

		String sizeString= String.format("%.2f", size);

		if(sizeString.endsWith(".00"))
			sizeString= sizeString.substring(1, sizeString.lastIndexOf(".00"));

		return sizeString.concat(" "+ suffix);
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
		Dialog("Dialog Window"),
		InputDialog("Input Dialog Window"),
		MainWindow("Main Window");

		private final String title;
		private SceneType(String t){ title= t; }
		public String getTitle(){ return title; }
		public String getFxmlPath(){ return "/views/"+ name()+ ".fxml"; }
	}

	public static final String[] FILE_EXTENSIONS= {	"*.txt", "*.conf", "*.config", "*.properties", "*.property"};
}
