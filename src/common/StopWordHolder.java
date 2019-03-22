package common;

/**
 * A singleton to StopWord class 
 * @author gabriel
 */
public class StopWordHolder {
	
	//<! instance of StopWord class
	private static StopWord sw = null;
	
	private StopWordHolder(){}
	
	/**
	 * Get the unique instance of StopWord class
	 * @return The instance of StopWord
	 */
	public static synchronized StopWord getStopWord() {
		if (sw == null)
			sw = new StopWord();
		return sw;
	}
	
}
