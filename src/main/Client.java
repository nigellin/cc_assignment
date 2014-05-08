package main;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import java.io.File;
import java.io.IOException;

public class Client{
	private static Client	instance;
	private AmazonS3Client	s3Client;
	private String			name;

	public static Client instance(){
		if(instance== null)
			instance= new Client();

		return instance;
	}

	public AmazonS3Client getS3Client(){ return s3Client; }

	public void setS3Client(File file) throws AmazonS3Exception, IOException{
		s3Client= new AmazonS3Client(new PropertiesCredentials(file));
		name	= s3Client.getS3AccountOwner().getDisplayName();

		s3Client.setRegion(Region.getRegion(Regions.DEFAULT_REGION));
	}

	public String getName(){
		return name;
	}
}
