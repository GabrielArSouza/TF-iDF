package bigdata.techniques.tools.executor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.common.StopWord;
import bigdata.common.StopWordHolder;
import bigdata.techniques.tools.mutex.MutexCounter;

public class ExecutorConstructTerms implements Runnable  {

	private final MutexCounter mtxCounter;
	private final ArrayList<Document> docs;
	private ConcurrentHashMap<String, Integer> terms;
	
	public ExecutorConstructTerms(MutexCounter mtxCounter,
			ArrayList<Document> docs,
			ConcurrentHashMap<String, Integer> terms)
	{
		this.mtxCounter = mtxCounter;
		this.docs = docs;
		this.terms = terms;
	}

	@Override
	public void run() {
		Integer pos = null;
		StopWord sw = StopWordHolder.getStopWord();
		Document docAux;
		
		while ((pos = mtxCounter.increment()) != null) {
			docAux = this.docs.get(pos);
			for (String s : docAux.getTableTermOccurrence().keySet()) {
				if (!sw.isStopWord(s)) {
					this.terms.put(s, 1);
				}else docAux.incrementStopWords(docAux.numberOfOccurrencesTerm(s));
			}
		}
		
	}

}
