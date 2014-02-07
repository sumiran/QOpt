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

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

class Term implements Comparable<Term>
{
	public String word;
	int score;
	
	public int compareTo(Term o) {
		return score - o.score;
	};
	
	public Term(String word, int score)
	{
		this.word = word;
		this.score = score;
	}
	
}




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

	public static void searchAndReturn(String query) throws Exception {
		search(query);
		analyze();
	}
	
	
	public static void mergeTermInList(String word, ArrayList<Term> list, int additionalScore)
	{
		for(int i=0;i<list.length;i++)
		{
			if(list.get(i).word.equals(word))
			{
				list.get(i).score += additionalScore;
				return;
			}
		}
		
		list.add(new Term(word, additionalScore));
	}
	
	public static String[] stopWords = { "a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the" };
	
	public static void main(String args[]) throws Exception {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Enter search query: ");
		String q = br.readLine();
		double targetP = 0.0; 
		boolean validP = false;
		do
		{
			System.out.print("Enter desired precision@10: ");
			try
			{
				targetP = Double.parseDouble(br.readLine());
				if(targetP > 0.0 && targetP <= 1.0)
				{
					validP = true;
				}
			}
			catch(Exception e)
			{
				
			}
		}
		while(!validP);
		
		double currentP = 0.0;
		
		do
		{
			searchAndReturn(q);
			
			currentP = 0.0;
			int relevantDocs = 0;
			ArrayList<Entry> relevant = new ArrayList<Entry>();
			ArrayList<Entry> notRelevant = new ArrayList<Entry>();
			
			for(int i=0;i<entries.length;i++)
			{
				System.out.println("Result "+(i+1)+": ");
				System.out.println("[");
				System.out.println("\tTitle:   "+entries.get(i).Title);
				System.out.println("\tSummary: "+entries.get(i).Description);
				System.out.println("\tURL:     "+entries.get(i).Url);
				System.out.println("]");
				System.out.println("Relevant? (y/n): ");
				String feedback = br.readLine();
				if(feedback.toLowerCase().charAt(0) == 'y')
				{
					relevantDocs += 1;
					relevant.add(entries.get(i));
				}
				else
				{
					notRelevant.add(entries.get(i));
				}
			}
			
			currentP = relevantDocs/results.length;
			
			/*
URL
Title
Summary


Q = original query
X = set of relevant results
Y = set of irrelevant results

For X and Y, remove all stop words from summary and title, and break URL.
URL: get all subdomains, path names. Remove standard values from path names (index, htm, html, pdf, txt, jpg, jpeg, png, gif)

For each remaining word, calculate "co-relevancy score" based on number of times it appears in X - no of times it appears in Y. Multiply by weight for each occurrence if it is in title as opposed to summary.


append max 2 scores and repeat
			 * */
			if(currentP < targetP)
			{
				String allRelevantTitlesString = "";
				for(int i=0;i<relevant.length;i++)
				{
					allRelevantTitlesString += relevant.get(i).Title;
				}
				
				//allRelevantTitlesString.replaceAll("[,.!;:\"\'\-\+]", " ");
				//remove all stop words
				
				String[] relevantTitles = allRelevantTitlesString.split(" ");
				
				
				String allRelevantDescriptionsString = "";
				for(int i=0;i<relevant.length;i++)
				{
					allRelevantDescriptionsString += relevant.get(i).Description;
				}
				
				//allRelevantTitlesString.replaceAll("[,.!;:\"\'\-\+]", " ");
				//remove all stop words
				
				String[] relevantDescriptions = allRelevantDescriptionsString.split(" ");
				
				
				String allRelevantURLsString = "";
				for(int i=0;i<relevant.length;i++)
				{
					allRelevantURLsString += relevant.get(i).Url;
				}
				
				//allRelevantTitlesString.replaceAll("[,.!;:\"\'\-\+]", " ");
				//remove all stop words
				
				String[] relevantURLs = allRelevantURLsString.split(" ");
				
				String allNonRelevantTitlesString = "";
				for(int i=0;i<nonRelevant.length;i++)
				{
					allNonRelevantTitlesString += nonRelevant.get(i).Title;
				}
				
				//allRelevantTitlesString.replaceAll("[,.!;:\"\'\-\+]", " ");
				//remove all stop words
				
				String[] nonRelevantTitles = allNonRelevantTitlesString.split(" ");
				
				
				String allNonRelevantDescriptionsString = "";
				for(int i=0;i<nonRelevant.length;i++)
				{
					allNonRelevantDescriptionsString += nonRelevant.get(i).Description;
				}
				
				//allRelevantTitlesString.replaceAll("[,.!;:\"\'\-\+]", " ");
				//remove all stop words
				
				String[] nonRelevantDescriptions = allNonRelevantDescriptionsString.split(" ");
				
				
				String allNonRelevantURLsString = "";
				for(int i=0;i<nonRelevant.length;i++)
				{
					allNonRelevantURLsString += nonRelevant.get(i).Url;
				}
				
				//allRelevantTitlesString.replaceAll("[,.!;:\"\'\-\+]", " ");
				//remove all stop words
				
				String[] nonRelevantURLs = allNonRelevantURLsString.split(" ");
				
				
				Arrays.sort(relevantTitles);
				Arrays.sort(relevantDescriptions);
				Arrays.sort(relevantURLs);
				Arrays.sort(nonRelevantTitles);
				Arrays.sort(nonRelevantDescriptions);
				Arrays.sort(allNonRelevantURLsString);
				
				ArrayList<Term> terms = new ArrayList<Term>();
				
				for(int i=0;i<relevantTitles.length;i++)
				{
					String word = relevantTitles[i];
					
					mergeTermInList(word, terms, 10);
				}
				
				for(int i=0;i<relevantDescriptions.length;i++)
				{
					String word = relevantDescriptions[i];
					
					mergeTermInList(word, terms, 3);
				}
				
				for(int i=0;i<relevantURLs.length;i++)
				{
					String word = relevantURLs[i];
					
					mergeTermInList(word, terms, 5);
				}
				
				
				for(int i=0;i<nonRelevantTitles.length;i++)
				{
					String word = nonRelevantTitles[i];
					
					mergeTermInList(word, terms, -20);
				}
				
				for(int i=0;i<nonRelevantDescriptions.length;i++)
				{
					String word = nonRelevantDescriptions[i];
					
					mergeTermInList(word, terms, -6);
				}
				
				for(int i=0;i<nonRelevantURLs.length;i++)
				{
					String word = nonRelevantURLs[i];
					
					mergeTermInList(word, terms, -10);
				}
				
				Arrays.sort(terms);
				
				String newTerm1 = terms.get(0);
				String newTerm2 = terms.get(1);
				
				q += " " + newTerm1 + " " newTerm2 ;
			}
		}
		while(currentP < targetP && currentP > 0);
		
		
	}

}