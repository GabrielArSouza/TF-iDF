package bigdata.techniques.tools.semaphore;

import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.common.StopWord;
import bigdata.common.StopWordHolder;

public class SemaphoreThreadConstructTerms extends Thread {

	private final SemaphoreDocumentController semaphore;
	private ConcurrentHashMap<String, Integer> terms;
	
	public SemaphoreThreadConstructTerms(
			ConcurrentHashMap<String, Integer> terms,
			SemaphoreDocumentController semaphore)
	{
		this.terms = terms;
		this.semaphore = semaphore;
	}
	
	public void run () {
		
		StopWord sw = StopWordHolder.getStopWord();
		Document docAux = null;
			
		try {
			while ((docAux = semaphore.getNext()) != null) {
				
				for (String s : docAux.getTableTermOccurrence().keySet()) {
					if (!sw.isStopWord(s)) {
						this.terms.put(s, 1);
					}else docAux.incrementStopWords(docAux.numberOfOccurrencesTerm(s));
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
