package techniques;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import algorithms.Document;
import algorithms.TFidF;
import common.Key;

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
				System.out.println("read document in path " + line);
				line = readFile.readLine();
				count++;
			}
			
			file.close();
		} catch (IOException e1) {
			System.err.println("could not open file " + this.urlDocuments 
					+ " " + e1.getMessage());
		}
	}
	
	public void constructTerms() {
		
		Set<Document> docs = documents.keySet();
		for (Document doc : docs) {
			for (String s : doc.getTableTermOccurrence().keySet())
				this.terms.put(s, 1);
		}
	}
	
	public void termFrequency() {

		Set<String> termsOfTable = terms.keySet();
		Set<Document> docs = documents.keySet();

		Key keyValue;
		for (Document doc : docs) {
			for (String s : termsOfTable) {
				keyValue = new Key(doc.getName(), s);
				double numberOfTimesAppears = (double) doc.numberOfOccurrencesTerm(s);
				this.termFrequency.put(keyValue, numberOfTimesAppears/(double) doc.getNumberOfTerms());
			}
		}
		
	}
	
	public void inverseDistance() {
		
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
		
	}
	
	public void tfidfTable() {
		
		Set<String> termsOfTable = terms.keySet();
		Set<Document> docs = documents.keySet();		
	
		Key keyValue;
		double value = 0.0;
		
		for (String s : termsOfTable) {
			for (Document doc : docs) {
				keyValue = new Key(doc.getName(), s);
				value = this.termFrequency.get(keyValue) * this.inverseDistance.get(s);
				this.tfIdf.put(keyValue, value);
			}
		}
	}
}
