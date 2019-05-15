package bigdata.techniques.tools.forkJoin;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

import bigdata.algorithms.Document;

public class ForkJoinReader extends RecursiveAction {

	private static final long serialVersionUID = 1L;

	private static final int SEQUENTIAL_THRESHOLD = 1;
	private List<String> data;
	private ConcurrentHashMap<Document, Integer> documents;
	
	public ForkJoinReader
	(List<String> data, ConcurrentHashMap<Document, Integer> doc) 
	{
		this.data = data;
		this.documents = doc;
	}
	
	@Override
	protected void compute() {	
	
		if (data.size() <= SEQUENTIAL_THRESHOLD) {
			
			computeDirect(data.get(0));
//			System.out.println("read documents in subtask");
		
		}else {
			
			// calculate new range
			int mid = data.size() / 2;
			ForkJoinReader firstSubtask = new ForkJoinReader(data.subList(0, mid),documents);
			ForkJoinReader secondSubtask = new ForkJoinReader(data.subList(mid, data.size()), documents);
			
			firstSubtask.fork();     // queue the first task
			secondSubtask.compute(); // compute the second task
			firstSubtask.join();     // 
		
		}
			
	}
	
	private void computeDirect(String data) {
		this.documents.put(new Document(data), 1);
	}

}
