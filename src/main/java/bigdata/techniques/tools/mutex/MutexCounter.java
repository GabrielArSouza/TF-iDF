package bigdata.techniques.tools.mutex;

//import java.util.concurrent.atomic.AtomicInteger;

public class MutexCounter {
	
	private int count;
	private int limit;
	
	public MutexCounter (int limit) {
		this.count = -1;
		this.limit = limit;
	}
	
	public synchronized Integer increment () {
		Integer value = null;
		if (count < limit ) {
			count++;
			value = count;
		}
		return value;
	}

//	private volatile AtomicInteger count;
//	private final int limit;
//	
//	public MutexCounter (int limit) {
//		this.count = new AtomicInteger(-1);
//		this.limit = limit;
//		
//		System.out.println(limit);
//	}
//	
//	public Integer increment () {
//		
//		Integer value = count.incrementAndGet();
//		if (value < limit) return value;
//		return null;
//	}


}
