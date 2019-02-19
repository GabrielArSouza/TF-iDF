package algorithms;

import java.util.HashMap;

public class Parser {

	//<! Attributes
	
	/**
	 * The map of term. 
	 * Key   - term's name
	 * Value - number of occurrences of the term 
	 */
	private HashMap<String, Integer> term;
	
	private String document;
	
	/**
	 * The constructor
	 * @param document   The document for parser
	 */
	public Parser ( String document )
	{ this.document = document; }

	public void run ()
	{
		// TODO
	}
	
	//<! Getters and Setters
	
	public HashMap<String, Integer> getTerm() {
		return term;
	}

	public void setTerm(HashMap<String, Integer> term) {
		this.term = term;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}
	
	
	
}
