package bigdata.techniques.tools.reactiveStream;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;

import bigdata.techniques.tools.mutex.MutexCounter;

public class ProducerUrls implements Runnable {

	private SubmissionPublisher<String> publisher;
	private final MutexCounter mtx;
	private final List<String> urls;
	
	public ProducerUrls(SubmissionPublisher<String> publisher,
			MutexCounter mtx,
			List<String> urls) 
	{
		this.publisher = publisher;
		this.mtx = mtx;
		this.urls = urls;
	}
	
	@Override
	public void run() {
		Integer pos = null;
		while ((pos = mtx.increment()) != null)
			publisher.submit(urls.get(pos));
	}

}
