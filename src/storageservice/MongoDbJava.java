package storageservice;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDbJava {
	
	public static MongoDatabase getDatabase() {
	    	
	        try {
	        	final MongoClient mongoClient = new MongoClient("localhost", 27017);
	            final MongoDatabase database = mongoClient.getDatabase("Risk");
	            System.out.println("Successful database connection established. \n");
	            return database;
	        } 
	        catch (Exception exception) {
	            System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
	            return null;
	        }
	}

}
