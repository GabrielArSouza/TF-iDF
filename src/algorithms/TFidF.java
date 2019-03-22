package algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import common.Key;

public abstract class TFidF {

	protected final String urlDocuments;
	protected ConcurrentHashMap<Document, Integer> documents;
	protected ConcurrentHashMap<String, Integer> terms;
	protected ConcurrentHashMap<Key, Double> termFrequency;
	protected ConcurrentHashMap<String, Double> inverseDistance;
	protected ConcurrentHashMap<Key, Double> tfIdf;
	
	public TFidF(String url) {
		this.urlDocuments = url;
		this.documents = new ConcurrentHashMap<Document,Integer>();
		this.terms = new ConcurrentHashMap<String, Integer>();
		this.termFrequency = new ConcurrentHashMap<Key,Double>();
		this.inverseDistance = new ConcurrentHashMap<String, Double>();
		this.tfIdf = new ConcurrentHashMap<Key, Double>();
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
		StringBuffer str = new StringBuffer("Term");
		
		// header
		Set<Document> docs = this.documents.keySet();
		for (Document doc : docs)
			str.append(";" + doc.getName());
		str.append(" \n");
	
		Set<String> termsOfTable = terms.keySet();
		Key keyValue;
		for (String s : termsOfTable ) {
			str.append(s);
			for (Document doc : docs) {
				keyValue = new Key(doc.getName(), s);
				str.append(";\"");
				str.append(String.format("%.6f", this.termFrequency.get(keyValue)));
				str.append("\"");
			}
			str.append("\n");	
		}
		
		String filename = "archive/tableTermFrequency.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(str.toString());
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
		
		// header
		Set<Document> docs = this.documents.keySet();
		for (Document doc : docs)
			str.append(";" + doc.getName());
		str.append("\n");
	
		Set<String> termsOfTable = terms.keySet();
		Key keyValue;
		for (String s : termsOfTable ) {
			str.append(s);
			for (Document doc : docs) {
				keyValue = new Key(doc.getName(), s);
				str.append(";\"");
				str.append(String.format("%.6f", tfIdf.get(keyValue)));
				str.append("\"");
			}
			str.append("\n");	
		}
		
		String filename = "archive/TF-idF.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    writer.write(str.toString());
	    writer.close();
	    
	    System.out.println("save TF-idF table in " + filename );
	}
	
	public int getNumberOfDocuments () {
		return this.documents.size();
	}
}
