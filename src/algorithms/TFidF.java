package algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TFidF {

	protected final String urlDocuments;
	protected ConcurrentHashMap<Document, Integer> documents;
	protected ConcurrentHashMap<String, Integer> terms;
	protected ConcurrentHashMap<String, Double> termFrequency;
	protected ConcurrentHashMap<String, Double> inverseDistance;
	protected ConcurrentHashMap<String, Double> tfIdf;
	
	public TFidF(String url) {
		this.urlDocuments = url;
		this.documents = new ConcurrentHashMap<Document,Integer>();
		this.terms = new ConcurrentHashMap<String, Integer>();
		this.termFrequency = new ConcurrentHashMap<String,Double>();
		this.inverseDistance = new ConcurrentHashMap<String, Double>();
		this.tfIdf = new ConcurrentHashMap<String, Double>();
	}
	
	public void run () {
		readDocuments();
		constructTerms();
		termFrequency();
		inverseDistance();
		tfidfTable();
	}
	
	public abstract void readDocuments();
	public abstract void constructTerms();
	public abstract void termFrequency();
	public abstract void inverseDistance();
	public abstract void tfidfTable();

	
	public void printTables() throws IOException {
		this.printTableTermFrequency();
		this.printTFidF();
	}
	
	private void printTableTermFrequency () throws IOException
	{
		System.out.println("genereting the term frequency table...");
		StringBuffer str = new StringBuffer("Term");
		String filename = "archive/tableTermFrequency.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	  
		// header
		Set<Document> docs = this.documents.keySet();
		for (Document doc : docs)
			str.append(";" + doc.getName());
		str.append(" \n");
	
		Set<String> termsOfTable = terms.keySet();
		StringBuffer keyValue = new StringBuffer("");
		for (String s : termsOfTable ) {
			str.append(s);
			for (Document doc : docs) {
				
				keyValue.append(doc.getName());
				keyValue.append(s);
				
				str.append(";\"");
				str.append(String.format("%.6f", this.termFrequency.get(keyValue.toString())));
				str.append("\"");
								
				keyValue.delete(0, keyValue.length());
			}
			str.append("\n");	
			writer.write(str.toString());
			str.delete(0,str.length());
		}
	    writer.close();
	    System.out.println("save Term Frequency table in " + filename );
	}
	
	/**
	 * Operate term frequency with inverse distance frequency 
	 * @throws IOException 
	 */
	private void printTFidF() throws IOException{
		
		System.out.println("generating TF-idF file");
		StringBuffer str = new StringBuffer("Term");
		String filename = "archive/TF-idF.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		
		
		// header
		Set<Document> docs = this.documents.keySet();
		for (Document doc : docs)
			str.append(";" + doc.getName());
		str.append("\n");
	
		Set<String> termsOfTable = terms.keySet();
		StringBuffer keyValue = new StringBuffer("");
		for (String s : termsOfTable ) {
			str.append(s);
			for (Document doc : docs) {
				
				keyValue.append(doc.getName());
				keyValue.append(s);
			
				str.append(";\"");
				str.append(String.format("%.6f", tfIdf.get(keyValue.toString())));
				str.append("\"");
				
				keyValue.delete(0, keyValue.length());
			}
			str.append("\n");
			writer.write(str.toString());
			str.delete(0, str.length());
		}
	    
	    writer.close();
	    System.out.println("save TF-idF table in " + filename );
	}
	
	public int getNumberOfDocuments () {
		return this.documents.size();
	}
}
