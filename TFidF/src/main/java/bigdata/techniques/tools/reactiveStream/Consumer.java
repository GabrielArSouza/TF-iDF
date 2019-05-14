package bigdata.techniques.tools.reactiveStream;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class Consumer implements Subscriber<String> {

	private Subscription subscription;
	
	@Override
	public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
	}

	@Override
	public void onNext(String url) {
        System.out.println("Received url: " + url);
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
