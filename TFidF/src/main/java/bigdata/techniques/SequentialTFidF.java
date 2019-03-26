package bigdata.techniques;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;


public class SequentialTFidF extends TFidF{

	public SequentialTFidF(String url) {
		super(url);
	}

	public void readDocuments() {
		try {
			
			FileReader file = new FileReader(this.urlDocuments);
			BufferedReader readFile = new BufferedReader(file);
			String line = readFile.readLine();
			int count=1;
			while (line != null) {
				line.trim();
				this.documents.put(new Document(line), count);
				line = readFile.readLine();
				count++;
			}
			System.out.println( count +  " files were read" );
			file.close();
		} catch (IOException e1) {
			System.err.println("could not open file " + this.urlDocuments 
					+ " " + e1.getMessage());
		}
	}
	
	public void constructTerms() {
//		System.out.println("Building the terms table...");
		
		Set<Document> docs = documents.keySet();
		for (Document doc : docs) {
			for (String s : doc.getTableTermOccurrence().keySet())
				this.terms.put(s, 1);
		}
		
//		System.out.println("Terms table built");
	}
	
	public void termFrequency() {

//		System.out.println("Building the term frequency table...");
//		
		Set<String> termsOfTable = terms.keySet();
		Set<Document> docs = documents.keySet();

		StringBuffer keyValue = new StringBuffer("");
		for (Document doc : docs) {
			for (String s : termsOfTable) {
						
				keyValue.append(s);
				keyValue.append("+");
				keyValue.append(doc.getName());
				
				double numberOfTimesAppears = (double) doc.numberOfOccurrencesTerm(s);
				if (numberOfTimesAppears != 0.0)
					this.termFrequency.put(keyValue.toString(), numberOfTimesAppears/(double) doc.getNumberOfTerms());
				//System.out.println(numberOfTimesAppears/(double) doc.getNumberOfTerms());
			
				keyValue.delete(0, keyValue.length());
			}
		}
//		
//		System.out.println("The term frequency table was built");
////		
	}
	
	public void inverseDistance() {
		
//		System.out.println("Building the inverse distance table...");
		
		Set<String> termsOfTable = terms.keySet();
		Set<Document> docs = documents.keySet();
		
		double numDocs = (double) this.getNumberOfDocuments();
		int count;
		double value;
		
		for (String term : termsOfTable) {
			count = 0;
	
			for (Document doc : docs) {
				if (doc.numberOfOccurrencesTerm(term)!=0)
					count++;
			}
			value = Math.log(numDocs/(double)count);
			this.inverseDistance.put(term, value);
		}
		
//		System.out.println("The inverse distance table was built");
		
	}
	
	public void tfidfTable() {
		
//		System.out.println("Building the TF-idF table...");
		
		Set<String> termsOfTable = terms.keySet();
		Set<Document> docs = documents.keySet();		
	
		StringBuffer keyValue = new StringBuffer("");
		double value = 0.0;
		double tfValue = 0.0;
		
		for (String s : termsOfTable) {
			for (Document doc : docs) {
				
				keyValue.append(s);
				keyValue.append("+");
				keyValue.append(doc.getName());			
				
				if (this.termFrequency.get(keyValue.toString()) != null) {
					tfValue = this.termFrequency.get(keyValue.toString());
					value = tfValue * this.inverseDistance.get(s);
					this.tfIdf.put(keyValue.toString(), value);
				}
				keyValue.delete(0, keyValue.length());
			}
		}
//		System.out.println("The TF-idF table was built");
	}
		
}
