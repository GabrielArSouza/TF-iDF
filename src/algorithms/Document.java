package algorithms;

import java.util.concurrent.ConcurrentHashMap;

public class Document {

	/**
	 * Attributes
	 */	
	private final String url;
	private final int numberOfTerms;
	private final ConcurrentHashMap<String, Integer> tableTermOccurrence;
	private final Parser parser;
	
	/**
	 * Construct 
	 * @param url The url of document
	 */
	public Document ( String url )
	{
		this.url = url;
		this.parser = new Parser(url);
		this.tableTermOccurrence = parser.getProcessedTerms() ;
		this.numberOfTerms = parser.getNumberOfProcessedTerms();
	}
	
	public void printTerms ()
	{
		for (String name: this.tableTermOccurrence.keySet()){
			String key = name.toString();
			String value = this.tableTermOccurrence.get(name).toString();  
			System.out.println(key + " | " + value);  
		}
	}
	
	/**
	 * informs the number of occurrences of a specific term
	 * @param term  The term
	 * @return  The number of occurrences of term
	 */
	public int numberOfOccurrencesTerm (String term)
	{ 
		if (tableTermOccurrence.containsKey(term))
			return this.tableTermOccurrence.get(term);
		else return 0;
	}
	
	/**
	 * Getters and Setters
	 */
	
	public String getUrl() {
		return url;
	}

	public String getName () {
		String name[] = this.url.split("/");
		int size = name.length;
		return name[size-1];
	}

	public int getNumberOfTerms() {
		return numberOfTerms;
	}

	public ConcurrentHashMap<String, Integer> getTableTermOccurrence() {
		return tableTermOccurrence;
	}
}
