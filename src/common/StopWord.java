package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class StopWord {
	
	//<! The name with the URL for stop words file
	private String filename;
	
	//<! The number of stop words in table
	private int numberOfStopWords;
	
	//<! The table of stopWords. 
	//<! Key   - The stop word 
	//<! Value - The id of stop word
	private HashMap<String, Integer> tableStopWord;
	
	/**
	 * Construct the object
	 * @param filename The URL for stop words file
	 */
	public StopWord(String filename) {
		this.filename = filename;
		this.numberOfStopWords = 0;
		this.tableStopWord = new HashMap<String, Integer>();
		this.constructTrableStopWords();
	}
	
	/**
	 * Read the stop words file and build a table
	 * of stop words
	 */
	private void constructTrableStopWords() {
		
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
			this.numberOfStopWords = id;
			file.close();
			
		}catch (Exception e) {
			System.err.printf("Erro ao abrir arquivo: %s.\n", e.getMessage());
		}
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

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public HashMap<String, Integer> getTableStopWord() {
		return tableStopWord;
	}

	public void setTableStopWord(HashMap<String, Integer> tableStopWord) {
		this.tableStopWord = tableStopWord;
	}
	
	public int getNumberOfStopWords () {
		return numberOfStopWords;
	}
	
}
