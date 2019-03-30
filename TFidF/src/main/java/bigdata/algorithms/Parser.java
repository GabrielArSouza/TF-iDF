package bigdata.algorithms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.common.StopWord;
import bigdata.common.StopWordHolder;


public class Parser {

	//<! Attributes
	private final String document;
	private int numberOfProcessedTerms;
	private final ConcurrentHashMap<String, Integer> processedTerms;
	
	
	// For tests
	public Parser() {
		this.document = "test";
		this.processedTerms = new ConcurrentHashMap<String, Integer>();
	}
	
	/**
	 * The constructor
	 * @param document   The URL document for parser
	 */
	public Parser ( String document ){ 
		this.document = document; 
		this.numberOfProcessedTerms = 0;
		this.processedTerms = this.processTerms();
	}

	private ConcurrentHashMap<String, Integer> processTerms(){ 
		
		ConcurrentHashMap<String, Integer> terms = new ConcurrentHashMap<String, Integer>();
		
		try {
			
			/**
			 * Read the document
			 */
			FileReader file = new FileReader(this.document);
		    BufferedReader readFile = new BufferedReader(file);
		 
		    String line = readFile.readLine(); // read the first line
		    String[] parsedTerms;
 		    
		    while (line != null) {
		    	
		        // eliminates line punctuation
		    	parsedTerms = line.trim().split("\\p{Punct}");
		        
		    	for (String t : parsedTerms )
		        {
		        	// separate words (terms)
		        	String[] singleTerm = t.trim().split("\\s+");
		        	for (String s : singleTerm)
		        	{
		        		// save the words
		        		if (!s.trim().equals("")) {
		        			this.numberOfProcessedTerms++;
		        			if (terms.containsKey(s))
				        		terms.replace(s, terms.get(s)+1);
				        	else { terms.put(s, new Integer(1));}
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
		
		
		return terms;
	}
	

	//<! Getters and Setters
	
	public String getDocument() {
		return document;
	}

	public int getNumberOfProcessedTerms () {
		return this.numberOfProcessedTerms;
	}
	
	public ConcurrentHashMap<String, Integer> getProcessedTerms(){
		return processedTerms;
	}
}
