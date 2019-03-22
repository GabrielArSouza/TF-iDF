package common;

public class StopWordHolder {
	
	private static StopWord sw = null;
	private StopWordHolder(){}
	
	public static synchronized StopWord getStopWord() {
		if (sw == null)
			sw = new StopWord();
		return sw;
	}
	
}
