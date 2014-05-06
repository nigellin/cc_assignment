package utilities;

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
	}

	public void setPropertFile(String filePath) throws IOException{
		try(InputStream input= new FileInputStream(filePath)){
			properties.load(input);
		}catch(IOException ioe){
			throw new IOException(ioe.getMessage());
		}
	}

	public String getProperty(String key){
		return properties.getProperty(key);
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
