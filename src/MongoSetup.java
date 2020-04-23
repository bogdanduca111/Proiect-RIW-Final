import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoSetup {
	Mongo  mongo = null;
	DB     database = null;
	String databaseName;
	
	public MongoSetup()
	{
		try {
			setup();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 
	public MongoSetup( String database ) throws UnknownHostException
	{
		setup();
		this.databaseName = database;
	}

	 
	private void setup() throws UnknownHostException
	{
		mongo = new Mongo();
	}
	 
	public String getDatabaseName() { return this.databaseName; }
	public void setDatabaseName( String databaseName ) { this.databaseName = databaseName; }
	public DB getDatabase() { return this.database; }
	
	public DBCollection getCollection( String collection )
	{
		return this.getCollection( this.databaseName, collection );
	}

	
	public DBCollection getCollection( String database, String collection )
	{
		this.database = mongo.getDB( database );
		return this.database.getCollection( collection );
	}
}
