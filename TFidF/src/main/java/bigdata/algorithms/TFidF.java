package bigdata.algorithms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
//		System.out.println("total of read documents: " + this.documents.size());
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
	
//		System.out.println("printing the term frequency table...");
		StringBuffer str = new StringBuffer("Term; Document; Value \n");
		
		String filename = "archive/tableTermFrequency.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		String[] parser;
		
		for (String s : this.termFrequency.keySet()) {
			
			writer.write(str.toString());
			str.delete(0,str.length());
			
			parser = s.split("\\+");
			str.append(parser[0]);
			str.append(";");
			str.append(parser[1]);
			str.append(";");
			str.append(String.format("%.6f", this.termFrequency.get(s)));
			str.append("\n");
	
		}
	    
		writer.close();	
//	    System.out.println("save Term Frequency table in " + filename );
	}
	
	/**
	 * Operate term frequency with inverse distance frequency 
	 * @throws IOException 
	 */
	private void printTFidF() throws IOException{
		
//		System.out.println("generating TF-idF file");
		StringBuffer str = new StringBuffer("Term; Document; Value \n");
		
		String filename = "archive/TF-idF.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		String[] parser;
		
		for (String s : this.tfIdf.keySet()) {
			
			writer.write(str.toString());
			str.delete(0,str.length());
			
			parser = s.split("\\+");
			str.append(parser[0]);
			str.append(";");
			str.append(parser[1]);
			str.append(";");
			str.append(String.format("%.6f", this.tfIdf.get(s)));
			str.append("\n");
	
		}
		
	    writer.close();
//	    System.out.println("save TF-idF table in " + filename );
	}
	
	public int getNumberOfDocuments () {
		return this.documents.size();
	}
	
	public ConcurrentHashMap<String, Double> getTFidF(){
		return this.tfIdf;
	}
}
