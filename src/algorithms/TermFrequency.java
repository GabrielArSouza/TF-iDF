package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TermFrequency {

	private List<Document> documents;
	private HashMap<String, Integer> terms;
	
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
