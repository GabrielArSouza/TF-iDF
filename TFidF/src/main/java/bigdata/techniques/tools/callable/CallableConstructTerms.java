package bigdata.techniques.tools.callable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import bigdata.algorithms.Document;
import bigdata.common.StopWord;
import bigdata.common.StopWordHolder;
import bigdata.techniques.tools.mutex.MutexCounter;

public class CallableConstructTerms implements Callable<Map<String,Integer>> {

	private final MutexCounter mtxCounter;
	private final ArrayList<Document> docs;
	
	public CallableConstructTerms(
			MutexCounter mtxCounter,
			ArrayList<Document> docs) 
	{
		this.mtxCounter = mtxCounter;
		this.docs = docs;
	}
	
	
	
	@Override
	public Map<String, Integer> call() throws Exception {
		
		Integer pos = null;
		Document docAux;
		Map<String, Integer> terms = new HashMap<>();
		StopWord sw = StopWordHolder.getStopWord();
		
		
		while ((pos = mtxCounter.increment()) != null) {
			docAux = this.docs.get(pos);
			for (String s : docAux.getTableTermOccurrence().keySet()) {
				if (!sw.isStopWord(s)) {
					terms.put(s, 1);
				}else docAux.incrementStopWords(docAux.numberOfOccurrencesTerm(s));
			}
		}
		
		return terms;
	}

}
