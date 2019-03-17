package algorithms;

import java.util.HashMap;

public class Document {

	/**
	 * Attributes
	 */	
	private String url;
	private int numberOfTerms;
	private HashMap<String, Integer> tableTermOccurrence;
	private Parser parser;
	
	/**
	 * Construct 
	 * @param url The url of document
	 */
	public Document ( String url )
	{
		this.url = url;
		this.parser = new Parser(url);
		this.tableTermOccurrence = parser.run();
		
		this.numberOfTerms = 0;
		for (String s : this.tableTermOccurrence.keySet())
			numberOfTerms += this.tableTermOccurrence.get(s);
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
	{ return this.tableTermOccurrence.get(term); }
	
	/**
	 * Getters and Setters
	 */
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getName () {
		String name[] = this.url.split("/");
		int size = name.length;
		return name[size-1];
	}

	public int getNumberOfTerms() {
		return numberOfTerms;
	}

	public void setNumberOfTerms(int numberOfTerms) {
		this.numberOfTerms = numberOfTerms;
	}

	
	public HashMap<String, Integer> getTableTermOccurrence() {
		return tableTermOccurrence;
	}

	public void setTableTermOccurrence(HashMap<String, Integer> tableTermOccurrence) {
		this.tableTermOccurrence = tableTermOccurrence;
	}

	
}
