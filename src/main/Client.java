package main;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.*;
import utilities.*;

public class Client{
	private static Client			instance;
	public static AmazonS3Client	s3Client;
	private TransferManager			transferManager;
	private String					name;
	private ListObjectsRequest		listObject;

	public Client(){ listObject= new ListObjectsRequest(); }

	public static Client instance(){
		if(instance== null)
			instance= new Client();

		return instance;
	}

	public AmazonS3Client getS3Client(){ return s3Client; }

	public void setS3Client(File file) throws AmazonS3Exception, IOException{
		s3Client= new AmazonS3Client(new PropertiesCredentials(file));
		name	= s3Client.getS3AccountOwner().getDisplayName();

		transferManager= new TransferManager(s3Client);

		s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
	}

	public List<Bucket> getBuckets(){ return s3Client.listBuckets(); }

	public List<S3ObjectSummary> getObjectSummaries(String bucketName, String prefix){
		return getObjectSummaries(bucketName, prefix, true);
	}

	public List<S3ObjectSummary> getObjectSummaries(String bucketName, String prefix, boolean noSub){
		Stream<S3ObjectSummary> stream= s3Client.listObjects(bucketName, prefix).getObjectSummaries().stream();

		if(noSub)
			return stream.filter(s-> Common.isSubFile(s.getKey(), prefix)).collect(Collectors.toList());
		else
			return stream.collect(Collectors.toList());
	}

	public ObjectMetadata	getObjectMetadata(String bucketName, String key){ return s3Client.getObjectMetadata(bucketName, key); }
	public S3Object			getObject(String bucketName, String key){ return s3Client.getObject(bucketName, key); }
	public String			getName(){ return name; }
	public TransferManager	getTransferManager(){ return transferManager; }
}
