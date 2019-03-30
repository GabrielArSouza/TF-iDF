package bigdata.techniques.tools.semaphore;

import java.util.Stack;
import java.util.concurrent.Semaphore;

import bigdata.algorithms.Document;

public class SemaphoreDocumentController {

	private Semaphore semaphore;
	private Stack<Document> docs;
	
	public SemaphoreDocumentController() {
		this.docs = new Stack<Document>();
		this.docs.push(new Document("a", 100));
		this.docs.push(new Document("b", 100));
		semaphore = new Semaphore(1);
	}
	
	public SemaphoreDocumentController(Stack<Document> docs) {
		this.docs = docs;
		this.semaphore = new Semaphore(1);
	}
	
	/**
	 * Get next Document
	 * @param doc  The Document
	 * @return False if the stack is not empty and a new document was generated,
	 * 		   True if the stack is empty and wasn't possible generate a new document
	 * @throws InterruptedException
	 */
	public Document getNext() throws InterruptedException {
		
		Document doc = null;
		this.semaphore.acquire();
		// Critical section
		if (!docs.isEmpty()) {
			doc = this.docs.peek();
			this.docs.pop();
		}
		this.semaphore.release();
		return doc;
		
	}	
}
