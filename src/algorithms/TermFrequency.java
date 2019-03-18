package algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import common.StopWord;

import java.io.IOException;


public class TermFrequency {

	private List<Document> documents;
	private HashMap<String, Integer> terms;
	private Float[][] tableTermFrequency;
	private StopWord stopWordObject;
	
	/**
	 * Construct the object from a unique document
	 * @param document  the document
	 */
	public TermFrequency (Document document, StopWord sw)
	{
		this.documents = new ArrayList<Document>();
		this.documents.add(document);
		this.stopWordObject = sw;
		
		terms = new HashMap<String, Integer>();
		for (String s : document.getTableTermOccurrence().keySet())
			terms.put(s, 1);
	}
	
	/**
	 * Construct the object from a list of documents
	 * @param documents  the list of documents
	 */
	public TermFrequency (List<Document> documents, StopWord sw)
	{
		this.documents = new ArrayList<Document>();
		this.terms = new HashMap<String, Integer>();
		this.stopWordObject = sw;
		
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
	 * Construct the table of term frequency
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
		System.out.println(numberOfDocuments + " documents found");
		System.out.println(this.terms.keySet().size() + " terms found");
		System.out.println("Running algorithm...");
		String str = "Term";
		
		// header
		for (Document doc : this.documents)
			str += ";" + doc.getName();
		str += " \n";
	
		Set<String> termsOfTable = terms.keySet();
		int i=0;
		for (String s : termsOfTable ) {
			str += s;
			for (int j=0; j < numberOfDocuments; j++)
				str += ";\"" + String.format("%.6f", tableTermFrequency[j][i])+"\"";
			str += "\n";
			i++;			
		}
		
		String filename = "archive/tableTermFrequency.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(str);
	    writer.close();
	    
	    System.out.println("successfully");
	    System.out.println("save table term frequency in " + filename );
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
	
	public StopWord getStopWord () {
		return this.stopWordObject;
	}
	
}
