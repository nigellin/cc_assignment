package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javafx.scene.paint.Color;

public class Common{
	private static Common	instance;
	private Properties		properties;

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

	public enum MessageType{
		INFO(Color.CORNFLOWERBLUE),
		ERROR(Color.RED),
		WARN(Color.DARKORANGE),
		NULL(Color.BLACK);

		private final Color color;
		private MessageType(Color c){ color= c; }
		public final Color getColor(){ return color; }
	}// enum for message type, color value was associated
}
