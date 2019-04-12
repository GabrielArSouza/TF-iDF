package bigdata.techniques.tools.semaphore;

import java.util.Stack;
import java.util.concurrent.Semaphore;

public class SemaphoreStringController {
	
	private Semaphore semaphore;
	private Stack<String> urls;
	
	/**
	 * For Tests
	 */
	public SemaphoreStringController() {
		urls = new Stack<String>();
		urls.push("a");
		urls.push("b");
		this.semaphore = new Semaphore(1);
	}
	
	public SemaphoreStringController(Stack<String> urls) {
		this.semaphore = new Semaphore(1);
		this.urls = urls;
	}
	
	public String getNext() throws InterruptedException {
	
		String s = null;
		this.semaphore.acquire();
		// Critical section
		if (!urls.isEmpty()) {
			s = urls.peek();
			urls.pop();
		}
		this.semaphore.release();
	
		return s;
	}

}
