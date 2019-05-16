package bigdata.techniques.tools.reactiveStream;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

import bigdata.algorithms.Document;

public class ConsumeTermFrequency implements Flow.Subscriber<Document>{

	private Subscription subscription;
	private final ConcurrentHashMap<String, Integer> terms;
	private ConcurrentHashMap<String, Double> termFrequency;
	
	public ConsumeTermFrequency(
			ConcurrentHashMap<String, Integer> terms,
			ConcurrentHashMap<String, Double> termFrequency) 
	{
		super();
		this.terms = terms;
		this.termFrequency = termFrequency;
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
        subscription.request(1);
		
	}

	@Override
	public void onNext(Document item) {
		process(item);
		subscription.request(1);
	}

	@Override
	public void onError(Throwable error) {
		System.out.println("Error occurred: " + error.getMessage());
		
	}

	@Override
	public void onComplete() {
		System.out.println("PrintSubscriber is complete");
		
	}

	private void process(Document doc ) {
	
		StringBuffer keyValue = new StringBuffer("");
		Set<String> termsOfTable = terms.keySet();
		double numberOfTimesAppears = 0;
		
		for (String s : termsOfTable) {
			
			keyValue.append(s);
			keyValue.append("+");
			keyValue.append(doc.getName());
			
			numberOfTimesAppears = (double) doc.numberOfOccurrencesTerm(s);
			if (numberOfTimesAppears != 0.0)
				this.termFrequency.put(keyValue.toString(), numberOfTimesAppears/(double) doc.getNumberOfTerms());
		
			keyValue.delete(0, keyValue.length());
		}
	}
}
