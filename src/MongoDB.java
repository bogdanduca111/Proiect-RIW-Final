import java.awt.List;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.text.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class MongoDB {
	public static void ConnectMongo() throws UnknownHostException
	{

		 MongoClient mongoClient = new MongoClient();
		 DB database = mongoClient.getDB("RIW");	
	
	}
	
}
