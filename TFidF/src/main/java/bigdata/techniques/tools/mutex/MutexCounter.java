package bigdata.techniques.tools.mutex;

public class MutexCounter {
	
	private int count;
	private int limit;
	
	public MutexCounter (int limit) {
		this.count = -1;
		this.limit = limit;
	}
	
	public synchronized int increment () {
		if (count < limit ) count++;
		return count;
	}
	
	/**
	 * Check if the limit was reached
	 * @return false if the limit was not reached, true otherwise
	 */
	public synchronized boolean isLimit () {
		if (count < limit ) return false;
		return true;
	}
}
