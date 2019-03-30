package bigdata.techniques.tools.mutex;

public class MutexCounter {
	
	private volatile int count;
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
}
