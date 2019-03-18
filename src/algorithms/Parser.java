package algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import common.StopWord;


public class Parser {

	//<! Attributes
	private String document;
	private StopWord sw;
	private int numberOfProcessedTerms;
	
	/**
	 * The constructor
	 * @param document   The document for parser
	 * @param sw         The sto words
	 */
	public Parser ( String document, StopWord sw ){ 
		this.document = document; 
		this.sw = sw;
		this.numberOfProcessedTerms = 0;
	}

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
		    	terms = line.trim().split("\\p{Punct}");
		        
		    	for (String t : terms)
		        {
		        	// separate words (terms)
		        	String[] singleTerm = t.trim().split("\\s+");
		        	for (String s : singleTerm)
		        	{
		        		// save the words
		        		if (!sw.isStopWord(s) && !s.trim().equals("")) {
		        			numberOfProcessedTerms++;
		        			if (term.containsKey(s))
				        		term.replace(s, term.get(s)+1);
				        	else
				        	{ term.put(s, new Integer(1));}
		        		}
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
	
	public int getNumberOfProcessedTerms () {
		return this.numberOfProcessedTerms;
	}
}
