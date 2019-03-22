package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.ConcurrentHashMap;

public class StopWord {
	
	//<! The name with the URL for stop words file
	private final String filename;
	
	//<! The number of stop words in table
	private final int numberOfStopWords;
	
	//<! The table of stopWords. 
	//<! Key   - The stop word 
	//<! Value - The id of stop word
	private ConcurrentHashMap<String, Integer> tableStopWord;
	
	/**
	 * Construct the object
	 * @param filename The URL for stop words file
	 */
	public StopWord() {
		this.filename = "archive/stopWords.txt";;
		this.tableStopWord = new ConcurrentHashMap<String, Integer>();
		this.numberOfStopWords = this.constructTrableStopWords();
	}
	
	/**
	 * Read the stop words file and build a table
	 * of stop words
	 */
	private int constructTrableStopWords() {
		
		try {
		
			FileReader file = new FileReader(this.filename);
			BufferedReader reader = new BufferedReader(file);
			
			String line = reader.readLine();
			Integer id = 1;
			
			while (line != null) {
				this.tableStopWord.put(line, id);
				line = reader.readLine();
				id++;
			}
			file.close();
			return id;
			
		}catch (Exception e) {
			System.err.printf("Erro ao abrir arquivo: %s.\n", e.getMessage());
		}
		return 0;
	}
	
	/**
	 * verify if a string s is a stop word
	 * @param s  the string
	 * @return true if a stop word, false otherwise
	 */
	public boolean isStopWord (String s) {
		if (this.tableStopWord.containsKey(s)) return true;
		else return false;
	}
	
	// Getters and Setters
	
	public String getFilename() {
		return filename;
	}

	public ConcurrentHashMap<String, Integer> getTableStopWord() {
		return tableStopWord;
	}

	public int getNumberOfStopWords () {
		return numberOfStopWords;
	}
	
}
