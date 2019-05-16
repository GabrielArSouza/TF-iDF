package bigdata.techniques.tools.reactiveStream;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;

import bigdata.algorithms.Document;
import bigdata.techniques.tools.mutex.MutexCounter;

public class ProducerDocuments implements Runnable {

	private SubmissionPublisher<Document> publisher;
	private final MutexCounter mtx;
	private final List<Document> documents;
	
	public ProducerDocuments(SubmissionPublisher<Document> publisher,
			MutexCounter mtx, List<Document> documents) 
	{
		super();
		this.publisher = publisher;
		this.mtx = mtx;
		this.documents = documents;
	}

	@Override
	public void run() {
		Integer pos = null;
		while ((pos = mtx.increment()) != null) 
			publisher.submit(documents.get(pos));
	}
	
}
