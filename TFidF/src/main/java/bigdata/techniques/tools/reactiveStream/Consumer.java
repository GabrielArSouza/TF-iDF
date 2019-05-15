package bigdata.techniques.tools.reactiveStream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

import bigdata.algorithms.Document;

public class Consumer implements Flow.Subscriber<String> {

	private Subscription subscription;
	private ConcurrentHashMap<Document, Integer> documents;
	
	public Consumer (ConcurrentHashMap<Document, Integer> documents)
	{ this.documents = documents; }
	
	@Override
	public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
	}

	@Override
	public void onNext(String url) {
        //System.out.println("Received url: " + url);
        documents.put(new Document(url), 1);
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

}
