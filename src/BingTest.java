import java.io.*;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;









import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;





//Download and add this library to the build path.
import org.apache.commons.codec.binary.Base64;
import org.dom4j.*;
import org.dom4j.io.*;
public class BingTest {

	static ArrayList<Entry> entries;
	static Document document;
	public static void search(String key) throws IOException, DocumentException
	{
		
		
		String bingUrl = "https://api.datamarket.azure.com/Bing/SearchWeb/Web?Query=%27" + key +"%27&$top=10&$format=Atom";
		//Provide your account key here. 
		
		String accountKey = "E4Zv/pCEQ0AuHsYlNrOuXkZtjbRs2b4gFZYjiiuZj78";
		
		System.out.println(accountKey);
		byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);

		URL url = new URL(bingUrl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
		
		InputStream inputStream = (InputStream) urlConnection.getContent();		
		byte[] contentRaw = new byte[urlConnection.getContentLength()];
		inputStream.read(contentRaw);
		String content = new String(contentRaw);
		
		document = DocumentHelper.parseText(content);
		
		System.out.println(content);
		//save the contents to xml file
		//The content string is the xml/json output from Bing.
	
	}

	public static boolean doc2Xml(Document document,String filename) 
	   { 
	      boolean flag = true; 
	      try 
	      { 

	            OutputFormat format = OutputFormat.createPrettyPrint(); 

	            XMLWriter writer = new XMLWriter(new FileWriter(new File(filename))); 
	            writer.write(document); 
	            writer.close();             
	        }catch(Exception ex) 
	        { 
	            flag = false; 
	            ex.printStackTrace(); 
	        } 
	        return flag;       
	   }
	
	public static void analyze() throws Exception
	{
		try
		{
			entries = new ArrayList<Entry>();
			
			SAXReader reader = new SAXReader();
			Document doc = document;
			Element root = doc.getRootElement();
			Element foo;
			//iterator xml file to fetch data into our class
			for(Iterator i = root.elementIterator("entry");i.hasNext();)
			{
				Entry entry = new Entry();
				foo = (Element) i.next();
				entry.id = foo.elementText("id");
				entry.title = foo.elementText("title");
				entry.updated = foo.elementText("updated");
				Element content = foo.element("content");
				Element properties = content.element("properties");
				entry.ID = properties.elementText("ID");
				entry.Title = properties.elementText("Title");
				entry.Description = properties.elementText("Description");
				entry.DisplayUrl = properties.elementText("DisplayUrl");
				entry.Url = properties.elementText("Url");
				entries.add(entry);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
			
		
	}

	public static void main(String[] args) throws Exception {
		search("bible");
		analyze();
		doc2Xml(document, "result.xml");
		for(Entry e : entries)
		System.out.println(e.toString());
	}

}