import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;



public class Lab1 {
	
	//Laborator1 Ex1
	
	public static void laborator1() throws IOException
	{
		File input=new File("/workspace/emag.html");
		
		Document doc = Jsoup.parse(input,"utf-8");
		
		String title = doc.title();
		System.out.println("Title: "+title);
		
		String keywords = doc.select("meta[name=keywords]").first().attr("content");  
        System.out.println("Primul Meta keyword : " + keywords);  
        
        String description = doc.select("meta[name=description]").first().attr("content");  
        System.out.println("Primul Meta description : " + description);  
        
        String robot = doc.select("meta[name=robots]").first().attr("content");
        System.out.println("Primul Robots: "+robot);
        
        Element link = doc.select("a[href^=https:]").first();
        System.out.println("Primul link: "+link.attr("abs:href"));
        
       System.out.println("Text:");
       String text = doc.body().text();
       System.out.println(text);
	}
	
	//Laborator 1 Ex 2
	
	public static Scanner input;		
	public static Map<String, Integer> TextSplit(File file) 
	{
		Map<String, Integer> map = new HashMap<String,Integer>();
		try {
			input=new Scanner(file);
			while(input.hasNextLine())
			{
				String line=input.nextLine();
				String mystring=line.toLowerCase();
				StringBuilder word=new StringBuilder();
				for(int i=0;i<mystring.length();++i)
				{
					if(Character.isLetter(mystring.charAt(i)))
					{
						word.append(mystring.charAt(i));
					}
					else
					{
						if(word.length()>1)
						{
							if(!map.containsKey(word.toString())) {
								map.put(word.toString(), 1);
							}
							else
							{
								map.put(word.toString(), map.get(word.toString())+1);
							}
							
						}
						word.setLength(0);
					}
				}
			}
			System.out.println(map);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}
	
	//Laborator 2
	
	//parcurgere fisiere
	
	public static boolean CheckIfText(File file)
	{
		boolean exist = false;
		if(file == null)
		{
			return false;
		}
		String nume = file.getName();
		int i = nume.lastIndexOf('.');
		String ext = i>0?nume.substring(i+1) : " ";
		if(ext.equals("txt"))
		{
			exist = true;
		}
		else
		{
			exist = false;
		}
		return exist;
	}
	
	
	
	public static Queue<File> getFile(File path)
	{
		Queue<File> fisiere =  new LinkedList<File>();
		Queue<File> directoare = new LinkedList<File>();
		directoare.add(path);
		
		while(!directoare.isEmpty())
		{
			File current = directoare.poll(); //returneaza primul element si il sterge
			File[] listaDir = current.listFiles();
			
			if(listaDir != null)
			{
				for(File file : listaDir)
				{
					if(file.isDirectory())
					{
						directoare.add(file);
						 //System.out.println("director");
					}
					else if(CheckIfText(file))
					{
						fisiere.add(file);
						//System.out.println("fisier");
					}
				}
			}
			
		}
		return fisiere;
			
	}
	public static boolean StopWords(String words) 
	{
		ArrayList<String> listaCuvinte=new ArrayList<String>();
	    try {
	    	FileReader in = new FileReader("stopwords.txt");
		    BufferedReader br = new BufferedReader(in);
	    	  String line = br.readLine();
	  	    while (line!=null) {
	  	       
	  	    	listaCuvinte.add(line);
	  	        line = br.readLine();
	  	    }
	    }catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
		   for( String i : listaCuvinte)
		    {
		       if(words.equals(i))
		       {
		    		return false;
		       }	
		    }
	    input.close();
		return true;
	}
	
	public static Map<String, Integer> TextSplitPorter(File filePath)
	{
		Map<String, Integer> map = new HashMap<String, Integer>();
		FileInputStream inputStream = null;
		Porter porter=new Porter();
		Scanner sc = null;
		try {
			inputStream = new FileInputStream(filePath);
			sc = new Scanner(inputStream, "UTF-8");
			StringBuilder word = new StringBuilder();

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				for (int i = 0; i < line.length(); ++i) {
					if (Character.isLetter(line.charAt(i))) {
						word.append(line.charAt(i));

					} else {
						if (((word.length() > 1))&&(StopWords(word.toString()))) {
							if (!map.containsKey(porter.stripAffixes(word.toString()))) {
								map.put(porter.stripAffixes(word.toString()), 1);
							} else {
								map.put(porter.stripAffixes(word.toString()), map.get(porter.stripAffixes(word.toString())) + 1);
							}
						}
						word.setLength(0);
					}

					if ((i == (line.length() - 1)) && (word.length() > 1 ))
					{
						if(StopWords(word.toString()))
						{
							if (!map.containsKey(word.toString())) {
								map.put(porter.stripAffixes(word.toString()), 1);
							} else {
								map.put(porter.stripAffixes(word.toString()), map.get(porter.stripAffixes(word.toString())) + 1);
							}
						}
						word.setLength(0);
					}
				}
			}
			System.out.println(map);
			inputStream.close();
			sc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
	//Laborator3 - Indexare

public static Map<String,Map<String,Integer>> inverseMap = new HashMap<String,Map<String,Integer>>();
public static MongoSetup mongoClient=new MongoSetup();
	
	public static void AddIndexFiles(File folder) throws IOException
	{
		
		 
		 HashSet<String>hset=new HashSet<String>();
		 Queue<File> fisiere=new LinkedList<File>();
		 FileWriter out;
		 FileWriter allfiles=new FileWriter("fisiere.txt");
		 FileWriter indexInvers=new FileWriter("inversindex.txt");
		 fisiere=getFile(folder);
		 
		 mongoClient.setDatabaseName("RIW"); 
		 DBCollection collection1 = mongoClient.getCollection("directIndex");
         collection1.drop();
         
		 Map<String,Map<String,Integer>> directMap = new HashMap<String,Map<String,Integer>>();
		 
	
		 for (File f : fisiere)
		 {
			 String dir = f.getParent();
			 File path = new File(dir + "\\index.html"); 
			 if(hset.contains(dir) == true)
			 {
				 out = new FileWriter(path, true);
			 }
			 else
			 {
				 out = new FileWriter(path);
				 hset.add(dir);
			 }
			 
			 HashMap<String, Integer>print = (HashMap<String, Integer>)TextSplitPorter(f);
			 directMap.put(f.getAbsolutePath(), print);
			 
			//MONGODB directIndex                      
	            java.util.List<BasicDBObject> mydb = new ArrayList<BasicDBObject>();
	            for(Entry<String, Integer> entry:print.entrySet()) {                     
	                mydb.add(new BasicDBObject("cuvant",entry.getKey()).append("aparitii", entry.getValue()));
	            }
	            DBObject directIndex = new BasicDBObject("Fisier",f.getAbsolutePath()).append("Cuvinte",mydb);
	            collection1.insert(directIndex);
			 
			 out.write(directMap.toString());
			 out.write(System.lineSeparator());
			 allfiles.write(f.getAbsolutePath()+",");
			 allfiles.write(path.getAbsolutePath());
			 allfiles.write(System.lineSeparator());
			 out.close();
			 
			 
			 //indexare inversa
			 for(String key1 : directMap.keySet())
			 {
				 for(String key2 : directMap.get(key1).keySet())
				 {
					 int c = directMap.get(key1).get(key2);
					 if(inverseMap.containsKey(key2))
					 {
						 
						 inverseMap.get(key2).put(key1, c);
						 
					 }
					 else
					 {
						HashMap<String, Integer> inv = new HashMap<String, Integer>();
						 inv.put(key1, c);
						 inverseMap.put(key2, inv);
					
					 }
				 }
			 }
			 directMap.clear();
		 }
		 
		 for(Entry<String, Map<String, Integer>> entry:inverseMap.entrySet()) 
		 {				
				indexInvers.write((entry.getKey()+" = "+entry.getValue()));
				indexInvers.write(System.lineSeparator());	
		 }
		 
		//mongo inversIndex
		 
         DBCollection collection = mongoClient.getCollection("inversIndex");           
         collection.drop();
         for(Entry<String, Map<String, Integer>> entry:inverseMap.entrySet()) {
            java.util.List<BasicDBObject> inversIndex = new ArrayList<BasicDBObject>();
            for(Entry<String, Integer> entry1 : entry.getValue().entrySet())
            {
                inversIndex.add(new BasicDBObject("Fisier",entry1.getKey()).append("aparitii", entry1.getValue()));
            }
           
            DBObject invers = new BasicDBObject("cuvant",entry.getKey()).append("indexare", inversIndex);
            collection.insert(invers);
         }
		 indexInvers.close();
		 allfiles.close();
	}
	
	//LABORATOR 4 Cautarea booleana
	
	public static boolean verifyIfTermenExists(String values)
	{		
		for(Entry<String, Map<String, Integer>> entry:inverseMap.entrySet()) {
			if(values.equals(entry.getKey()))
			{
				return true;
			}		
		}
		return false;
	}
	
	public static LinkedList<String> ORoperator(String cuv1,String cuv2)
	{
		LinkedList<String> mylist=new LinkedList();
		
			if((cuv1 !=null)&&(cuv2!=null))
			{
				if(verifyIfTermenExists(cuv1)&&verifyIfTermenExists(cuv2))
				{
					
						for(String key1 : inverseMap.keySet())
						{
							for(String key2 : inverseMap.keySet())
							{
								if(key1.equals(cuv1)&&key2.equals(cuv2))
								{																			
									mylist.add(inverseMap.get(key1).keySet().toString());
									mylist.add(inverseMap.get(key2).keySet().toString());		                                                          								
								}
							}							
						}
				}
			}
			return mylist;		
	}

	
	public static LinkedList<String> ANDoperator(String cuv1,String cuv2)
	{
		LinkedList<String> mylist=new LinkedList();
		if((cuv1 !=null)&&(cuv2!=null))
		{
			if(verifyIfTermenExists(cuv1)&&verifyIfTermenExists(cuv2))
			{	
					for(String key1 : inverseMap.keySet())
					{
						for(String key2 : inverseMap.keySet())
						{			
							if(key1.equals(cuv1)&&key2.equals(cuv2))
							{		
								System.out.println(inverseMap.get(key1).keySet());
								System.out.println(inverseMap.get(key2).keySet());
								System.out.println("Rezultatul intersectiei: ");
								
								for(String key3 : inverseMap.get(key1).keySet())
								{
									for(String key4 : inverseMap.get(key2).keySet())
									{
										if(key3==key4)
											mylist.add(key3.toString());
									}	
								}	
							}
						}
					}
				}
		}
		return mylist;			
	}
	

public static void main(String[] args) throws IOException {
		
	   System.out.println("Laborator 1: ");
	   laborator1();		
       File file=new File("text1.txt");
       TextSplit(file);
       
       //Laborator2
       System.out.println("Laborator 2: ");
       File p = new File("D:\\workspace\\labm1\\Folder1");
       Queue<File> fisiere=new LinkedList<File>();
	   fisiere=getFile(p);
	   for(File f : fisiere)	
	   {
			System.out.println(f);
			TextSplitPorter(f);
	   }
		
		//Laborator3
		System.out.println();
		System.out.println("Laborator 3: ");
		AddIndexFiles(p);
       
		//Laborator4
		
		System.out.println("Laborator 4: ");
		System.out.println("Introduceti primul cuvant:");
		
		Scanner scanner1=new Scanner(System.in);
		String cuv1=scanner1.nextLine();
		System.out.println("Introduceti al 2-lea cuvant:");
		Scanner scanner2=new Scanner(System.in);
		String cuv2=scanner2.nextLine();
		
		if((verifyIfTermenExists(cuv1)&&verifyIfTermenExists(cuv2))==true)
		{
			System.out.println("Cuvintele se afla in fisier");
		}
		else
		{
			System.out.println("Cuvintele nu se afla in fisier");
		}
		LinkedList<String>mylist1=new LinkedList<String>();
		LinkedList<String>mylist2=new LinkedList<String>();
		System.out.println("Reuniune:");
		mylist1=ORoperator(cuv1, cuv2);
		System.out.println(mylist1);
		
		System.out.println("Intersectie:");
		mylist2=ANDoperator(cuv1, cuv2);
		System.out.println(mylist2);
		
		
        
     
                   
	}
	
	
}
