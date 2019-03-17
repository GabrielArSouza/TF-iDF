package algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;


public class Parser {

	//<! Attributes
	private String document;
	
	/**
	 * The constructor
	 * @param document   The document for parser
	 */
	public Parser ( String document )
	{ this.document = document; }

	/**
	 * Execute the algorithm
	 */
	public HashMap<String, Integer> run ()
	{
		/**
		 * Key - The term
		 * value - number of term occurrences
		 */
		HashMap<String, Integer> term = new HashMap<String, Integer>();
		
		try {
			
			/**
			 * Read the document
			 */
			FileReader file = new FileReader(this.document);
		    BufferedReader readFile = new BufferedReader(file);
		 
		    String line = readFile.readLine(); // read the first line
		    String[] terms;
 		    
		    while (line != null) {
		    	
		        // eliminates line punctuation
		    	terms = line.split("\\p{Punct}");
		        
		    	for (String t : terms)
		        {
		        	// separate words (terms)
		        	String[] singleTerm = t.split("\\p{Blank}");
		        	for (String s : singleTerm)
		        	{
		        		// save the words
		        		if (term.containsKey(s))
			        		term.replace(s, term.get(s)+1);
			        	else
			        	{ term.put(s, new Integer(1));}
		        	}
		        }
		        
		        line = readFile.readLine(); // next line
		    }
		 
		    file.close();
		    
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n",
		    e.getMessage());
		}
		
		return term;
	}
	
	//<! Getters and Setters
	
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}	
}
