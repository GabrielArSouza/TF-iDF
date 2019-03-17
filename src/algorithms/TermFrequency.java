package algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.io.IOException;


public class TermFrequency {

	private List<Document> documents;
	private HashMap<String, Integer> terms;
	private Float[][] tableTermFrequency;
	/**
	 * Construct the object from a unique document
	 * @param document  the document
	 */
	public TermFrequency (Document document)
	{
		this.documents = new ArrayList<Document>();
		this.documents.add(document);
		
		terms = new HashMap<String, Integer>();
		for (String s : document.getTableTermOccurrence().keySet())
			terms.put(s, 1);
	}
	
	/**
	 * Construct the object from a list of documents
	 * @param documents  the list of documents
	 */
	public TermFrequency (List<Document> documents)
	{
		this.documents = new ArrayList<Document>();
		this.terms = new HashMap<String, Integer>();
		
		for (Document doc : documents)
		{
			this.documents.add(doc);
			for (String s : doc.getTableTermOccurrence().keySet())
				this.terms.put(s,1);
		}
		
	}

	/**
	 * Add new document in documents list
	 * @param doc  the new document
	 */
	public void addNewDocument (Document doc)
	{
		this.documents.add(doc);
		for (String s : doc.getTableTermOccurrence().keySet())
			this.terms.put(s, 1);
	}
	
	/**
	 * Construc the table of term frequency
	 */
	public void constructTableTermFrequency ()
	{
		int numberOfDocuments = this.documents.size();
		int numberOfTerms = this.terms.keySet().size();
		
		this.tableTermFrequency = new Float[numberOfDocuments][numberOfTerms];
		Document auxDoc;
		
		Set<String> termsOfTable = terms.keySet();
		int j;
		
		for (int i = 0; i < numberOfDocuments; i++)
		{
			auxDoc = documents.get(i);
			j = 0;
			
			for (String s : termsOfTable)
			{
				float numberOfTimesAppears = (float) auxDoc.numberOfOccurrencesTerm(s); 		
				this.tableTermFrequency[i][j] = numberOfTimesAppears / (float) auxDoc.getNumberOfTerms();
				j++;
			}
		}
	}
	
	public void printTableTermFrequency () throws IOException
	{
		int numberOfDocuments = this.documents.size();
		int numberOfTerms = this.terms.keySet().size();
		String str = "";
		
		Set<String> termsOfTable = terms.keySet();
		int j;
		
		for (int i = 0; i < numberOfDocuments; i++)
		{
			str = "|  | " + documents.get(i).getName() + " |\n";
			j = 0;
			
			for (String s : termsOfTable)
			{
				str += "| " + s + " | " + this.tableTermFrequency[i][j] + " |\n";
				j++;
			}
		}
		
		String filename = "archive/output.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(str);
	     
	    writer.close();
	}
		
	
	/**
	 * Getters and Setters
	 */
			
	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public HashMap<String,Integer> getTerms() {
		return terms;
	}

	public void setTerms(HashMap<String, Integer> terms) {
		this.terms = terms;
	}
	
	
}
