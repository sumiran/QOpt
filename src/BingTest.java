import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

class Term implements Comparable<Term>
{
	public String word;
	int score;
	
	//This makes Arrays.sort output in descending order and prioritises longer words with same score
	@Override
	public int compareTo(Term o) {
		if(o.score != score)
			return o.score - score;
		return (o.word.length() - word.length());
	};
	
	public Term(String word, int score)
	{
		this.word = word;
		this.score = score;
	}
	
	@Override
	public String toString()
	{
		return word+"["+score+"]";
	}
	
}

class StringHelper
{
	//public static String[] englishStopWords = { "a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the" };
	public static ArrayList<String> englishStopWords = new ArrayList<String>(Arrays.asList(new String[] {"a", "about", "above", "across", "after", "again", "against", "all", "almost", "alone", "along", "already", "also", "although", "always", "among", "an", "and", "another", "any", "anybody", "anyone", "anything", "anywhere", "are", "area", "areas", "around", "as", "ask", "asked", "asking", "asks", "at", "away", "b", "back", "backed", "backing", "backs", "be", "became", "because", "become", "becomes", "been", "before", "began", "behind", "being", "beings", "best", "better", "between", "big", "both", "but", "by", "c", "came", "can", "cannot", "case", "cases", "certain", "certainly", "clear", "clearly", "come", "could", "d", "did", "differ", "different", "differently", "do", "does", "done", "down", "down", "downed", "downing", "downs", "during", "e", "each", "early", "either", "end", "ended", "ending", "ends", "enough", "even", "evenly", "ever", "every", "everybody", "everyone", "everything", "everywhere", "f", "face", "faces", "fact", "facts", "far", "felt", "few", "find", "finds", "first", "for", "four", "from", "full", "fully", "further", "furthered", "furthering", "furthers", "g", "gave", "general", "generally", "get", "gets", "give", "given", "gives", "go", "going", "good", "goods", "got", "great", "greater", "greatest", "group", "grouped", "grouping", "groups", "h", "had", "has", "have", "having", "he", "her", "here", "herself", "high", "high", "high", "higher", "highest", "him", "himself", "his", "how", "however", "i", "if", "important", "in", "interest", "interested", "interesting", "interests", "into", "is", "it", "its", "itself", "j", "just", "k", "keep", "keeps", "kind", "knew", "know", "known", "knows", "l", "large", "largely", "last", "later", "latest", "least", "less", "let", "lets", "like", "likely", "long", "longer", "longest", "m", "made", "make", "making", "man", "many", "may", "me", "member", "members", "men", "might", "more", "most", "mostly", "mr", "mrs", "much", "must", "my", "myself", "n", "necessary", "need", "needed", "needing", "needs", "never", "new", "new", "newer", "newest", "next", "no", "nobody", "non", "noone", "not", "nothing", "now", "nowhere", "number", "numbers", "o", "of", "off", "often", "old", "older", "oldest", "on", "once", "one", "only", "open", "opened", "opening", "opens", "or", "order", "ordered", "ordering", "orders", "other", "others", "our", "out", "over", "p", "part", "parted", "parting", "parts", "per", "perhaps", "place", "places", "point", "pointed", "pointing", "points", "possible", "present", "presented", "presenting", "presents", "problem", "problems", "put", "puts", "q", "quite", "r", "rather", "really", "right", "right", "room", "rooms", "s", "said", "same", "saw", "say", "says", "second", "seconds", "see", "seem", "seemed", "seeming", "seems", "sees", "several", "shall", "she", "should", "show", "showed", "showing", "shows", "side", "sides", "since", "small", "smaller", "smallest", "so", "some", "somebody", "someone", "something", "somewhere", "state", "states", "still", "still", "such", "sure", "t", "take", "taken", "than", "that", "the", "their", "them", "then", "there", "therefore", "these", "they", "thing", "things", "think", "thinks", "this", "those", "though", "thought", "thoughts", "three", "through", "thus", "to", "today", "together", "too", "took", "toward", "turn", "turned", "turning", "turns", "two", "u", "under", "until", "up", "upon", "us", "use", "used", "uses", "v", "very", "w", "want", "wanted", "wanting", "wants", "was", "way", "ways", "we", "well", "wells", "went", "were", "what", "when", "where", "whether", "which", "while", "who", "whole", "whose", "why", "will", "with", "within", "without", "work", "worked", "working", "works", "would", "x", "y", "year", "years", "yet", "you", "young", "younger", "youngest", "your", "yours", "z"}));
	public static ArrayList<String> internetStopWords = new ArrayList<String>(Arrays.asList(new String[]{ "http", "www", "index", "html", "encyclopedia", "dictionary", "en", "asp", "aspx", "php", "com", "org", "ie", "page", "utf8", "utf16", "utf32", "utf64", "etc" }));
	
	public static String sanitizeStringBag(String bag, String originalQuery)
	{
		String[] originalQueryTerms = originalQuery.split(" ");
		
		bag = bag.replaceAll("[^a-zA-Z0-9]", " ");
		
		bag = bag.toLowerCase();
		
		for(int i=0;i<englishStopWords.size();i++)
		{
			bag = bag.replaceAll("\\b"+englishStopWords.get(i)+"\\b", " ");
		}
		for(int i=0;i<internetStopWords.size();i++)
		{
			bag = bag.replaceAll("\\b"+internetStopWords.get(i)+"\\b", " ");
		}
		
		for(int i=0;i<originalQueryTerms.length;i++)
		{
			bag = bag.replaceAll("\\b"+originalQueryTerms[i]+"\\b", " ");
		}
		bag = bag.replaceAll("\\s+", " ");
		return bag;
	}
	
	//Matches words with low edit distance and tries to combine plurals as one
	//We assume that false positives by matching large LCSs will be offset by the search algorithm of bing itself (such as "jaguar tar" being autocorrected to "jaguar car"
	public static boolean isSameWord(String s1, String s2)
	{
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		if(s1.equals(s2))
		{
			return true;
		}
		
		if(s1.matches(s2+"s|es")|| s2.matches(s1+"s|es"))
		{
			return true;
		}
		
		if((s2.endsWith("y") && s1.matches((s2.substring(0, s2.length()-2))+"ies")) || (s1.endsWith("y") && s2.matches((s1.substring(0, s1.length()-2))+"ies")))
		{
			return true;
		}
		
		int lcsLength = longestSubstringLength(s1, s2);
		int s1Len = s1.length();
		int s2len = s2.length();
		
		int diff = Math.max(s1Len - lcsLength, s2.length() - lcsLength );
		int maxLen = Math.max(s1Len, s2len);
		
		if(diff < 1 && maxLen > 3)	return true;
		
		if(diff <= (maxLen*1.0)/3)	return true;
		
		return false;
		
	}
	
	public static int longestSubstringLength(String a, String b) {
	    int[][] lengths = new int[a.length()+1][b.length()+1];
	 
	    for (int i = 0; i < a.length(); i++)
	        for (int j = 0; j < b.length(); j++)
	            if (a.charAt(i) == b.charAt(j))
	                lengths[i+1][j+1] = lengths[i][j] + 1;
	            else
	                lengths[i+1][j+1] =
	                    Math.max(lengths[i+1][j], lengths[i][j+1]);
	 
	    int lcsLength = 0;
	    for (int x = a.length(), y = b.length();
	         x != 0 && y != 0; ) {
	        if (lengths[x][y] == lengths[x-1][y])
	            x--;
	        else if (lengths[x][y] == lengths[x][y-1])
	            y--;
	        else {
	            assert a.charAt(x-1) == b.charAt(y-1);
	            lcsLength++;
	            x--;
	            y--;
	        }
	    }
	 
	    return lcsLength;
	}
	
}


public class BingTest {

	static ArrayList<Entry> entries;
	static Document document;
	public static void search(String key) throws IOException, DocumentException
	{
		
		key = key.replaceAll(" ","%20");
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

	public static void analyze() throws Exception
	{
		try
		{
			entries = new ArrayList<Entry>();
			
			Document doc = document;
			Element root = doc.getRootElement();
			Element foo;
			//iterator xml file to fetch data into our class
			for(Iterator<Element> i = root.elementIterator("entry");i.hasNext();)
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
		word = word.toLowerCase();
		for(int i=0;i<list.size();i++)
		{
			if(StringHelper.isSameWord(list.get(i).word, word))
			{
				list.get(i).score += additionalScore;
				return;
			}
		}
		
		list.add(new Term(word, additionalScore));
	}
	
	
	
	public static void main(String args[]) throws Exception {
		
		
		
		System.out.println(StringHelper.longestSubstringLength("car", "cars"));
		System.out.println(StringHelper.longestSubstringLength("encyclopedia", "wikipedia"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Enter search query: ");
		String q = br.readLine();
		q = q.toLowerCase();
		StringHelper.englishStopWords.remove(q);
		StringHelper.internetStopWords.remove(q);
		
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
			
			for(int i=0;i<entries.size();i++)
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
			
			currentP = relevantDocs*1.0/entries.size();
			
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
				for(int i=0;i<relevant.size();i++)
				{
					allRelevantTitlesString += relevant.get(i).Title;
				}
				String[] relevantTitles = StringHelper.sanitizeStringBag(allRelevantTitlesString, q).split(" ");
				
				
				String allRelevantDescriptionsString = "";
				for(int i=0;i<relevant.size();i++)
				{
					allRelevantDescriptionsString += relevant.get(i).Description;
				}
				String[] relevantDescriptions = StringHelper.sanitizeStringBag(allRelevantDescriptionsString, q).split(" ");
				
				
				String allRelevantURLsString = "";
				for(int i=0;i<relevant.size();i++)
				{
					allRelevantURLsString += relevant.get(i).Url;
				}
				String[] relevantURLs = StringHelper.sanitizeStringBag(allRelevantURLsString, q).split(" ");
				
				String allNonRelevantTitlesString = "";
				for(int i=0;i<notRelevant.size();i++)
				{
					allNonRelevantTitlesString += notRelevant.get(i).Title;
				}
				String[] nonRelevantTitles = StringHelper.sanitizeStringBag(allNonRelevantTitlesString, q).split(" ");
				
				
				String allNonRelevantDescriptionsString = "";
				for(int i=0;i<notRelevant.size();i++)
				{
					allNonRelevantDescriptionsString += notRelevant.get(i).Description;
				}
				String[] nonRelevantDescriptions = StringHelper.sanitizeStringBag(allNonRelevantDescriptionsString, q).split(" ");
				
				
				String allNonRelevantURLsString = "";
				for(int i=0;i<notRelevant.size();i++)
				{
					allNonRelevantURLsString += notRelevant.get(i).Url;
				}
				String[] nonRelevantURLs = StringHelper.sanitizeStringBag(allNonRelevantURLsString, q).split(" ");
				
				
				Arrays.sort(relevantTitles);
				Arrays.sort(relevantDescriptions);
				Arrays.sort(relevantURLs);
				Arrays.sort(nonRelevantTitles);
				Arrays.sort(nonRelevantDescriptions);
				Arrays.sort(nonRelevantURLs);
				
				ArrayList<Term> terms = new ArrayList<Term>();
				
				for(int i=0;i<relevantTitles.length;i++)
				{
					String word = relevantTitles[i];
					mergeTermInList(word, terms, 10);
				}
				
				for(int i=0;i<relevantDescriptions.length;i++)
				{
					String word = relevantDescriptions[i];
					mergeTermInList(word, terms, 5);
				}
				
				for(int i=0;i<relevantURLs.length;i++)
				{
					String word = relevantURLs[i];
					mergeTermInList(word, terms, 3);
				}
				
				for(int i=0;i<nonRelevantTitles.length;i++)
				{
					String word = nonRelevantTitles[i];
					mergeTermInList(word, terms, -20);
				}
				
				for(int i=0;i<nonRelevantDescriptions.length;i++)
				{
					String word = nonRelevantDescriptions[i];
					mergeTermInList(word, terms, -10);
				}
				
				for(int i=0;i<nonRelevantURLs.length;i++)
				{
					String word = nonRelevantURLs[i];
					mergeTermInList(word, terms, -6);
				}
				
				Collections.sort(terms);
				
				String newTerm1 = terms.get(0).word;
				String newTerm2 = terms.get(1).word;
				
				q += " " + newTerm1 + " " + newTerm2 ;
			}
		}
		while(currentP < targetP && currentP > 0);
		
		
	}

}