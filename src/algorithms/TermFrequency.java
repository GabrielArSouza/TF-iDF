package algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import common.StopWord;
import sun.rmi.server.InactiveGroupException;

import java.io.IOException;


public class TermFrequency {

	private List<Document> documents;
	private HashMap<String, Integer> terms;
	private Float[][] tableTermFrequency;
	private Float[] inverseDistanceFrequency;
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
		this.constructTables();
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
		this.constructTables();
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
	
	public void constructTables () {
		this.constructTableTermFrequency();
		System.out.println("The Term Frequency table was built");
		this.constructInverseDistanceFrequency();
		System.out.println("The inverse distance Frequency table was built");
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
	
	/**
	 * Construct the inverse distance Frequency table
	 */
	public void constructInverseDistanceFrequency() {
		int numberOfDocuments = this.documents.size();
		Set<String> termsOfTable = terms.keySet();
		
		this.inverseDistanceFrequency = new Float[termsOfTable.size()];
		
		int count, i = 0;
		for (String term : termsOfTable) {
			count = 0;
			
			for ( Document doc : documents ) {
				if (doc.numberOfOccurrencesTerm(term) != 0)
					count ++;
			}
			
			inverseDistanceFrequency[i] = (float) Math.log(
					(float)(numberOfDocuments/count));
			i++;
		}
		
	}
	
	public void printTables () throws IOException {
		
		int numberOfDocuments = this.documents.size();
		System.out.println(numberOfDocuments + " documents found");
		System.out.println(this.terms.keySet().size() + " terms found");
		
		this.printTableTermFrequency();
		this.printInverseDistanceFrequency();
		this.printTFidF();

	    System.out.println("successfully");
	}
	
	public void printTableTermFrequency () throws IOException
	{
		int numberOfDocuments = this.documents.size();
		StringBuffer str = new StringBuffer("Term");
		
		// header
		for (Document doc : this.documents)
			str.append(";" + doc.getName());
		str.append(" \n");
	
		Set<String> termsOfTable = terms.keySet();
		int i=0;
		for (String s : termsOfTable ) {
			str.append(s);
			for (int j=0; j < numberOfDocuments; j++) {
				str.append(";\"");
				str.append(String.format("%.6f", this.tableTermFrequency[j][i]));
				str.append("\"");
			}
			str.append("\n");
			i++;			
		}
		
		String filename = "archive/tableTermFrequency.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(str.toString());
	    writer.close();
	    
	    System.out.println("save Term Frequency table in " + filename );
	}
	
	public void printInverseDistanceFrequency() throws IOException {
		StringBuffer str = new StringBuffer("Term;idf\n");
		
		Set<String> termsOfTable = terms.keySet();

		int count = 0;
		for (String s : termsOfTable) {
			str.append(s);
			str.append(";");
			str.append(String.format("%.6f",this.inverseDistanceFrequency[count]));
			str.append("\n");
			count++;
		}
		
		String filename = "archive/inverseDistanceFrequency.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(str.toString());
	    writer.close();
	
	    System.out.println("save inverse distance Frequency table in " + filename );
	}
	
	/**
	 * Operate term frequency with inverse distance frequency 
	 * @throws IOException 
	 */
	public void printTFidF() throws IOException{
		
		System.out.println("generating TF-idF file");
		int numberOfDocuments = this.documents.size();
		StringBuffer str = new StringBuffer("Term");
		
		// header
		for (Document doc : this.documents)
			str.append(";" + doc.getName());
		str.append("\n");
	
		Set<String> termsOfTable = terms.keySet();
		int i=0;
		for (String s : termsOfTable ) {
			str.append(s);
			for (int j=0; j < numberOfDocuments; j++) {
				str.append(";\"");
				str.append(String.format("%.6f", 
						tableTermFrequency[j][i] * inverseDistanceFrequency[i]));
				str.append("\"");
			}
			str.append("\n");
			i++;			
		}
		
		String filename = "archive/TF-idF.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(str.toString());
	    writer.close();
	    
	    System.out.println("save TF-idF table in " + filename );
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
