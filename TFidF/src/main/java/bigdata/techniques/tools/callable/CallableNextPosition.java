package bigdata.techniques.tools.callable;

import java.util.concurrent.Callable;

public class CallableNextPosition implements Callable<Integer>{

	private int count;
	private final int limit;
	
	public CallableNextPosition(int limit) {
		this.limit = limit;
		this.count = -1;
	}
	
	@Override
	public synchronized Integer call() throws Exception {
		Integer value = null;
		if (count < limit ) {
			count++;
			value = count;
		}
		return value;
	}

}
