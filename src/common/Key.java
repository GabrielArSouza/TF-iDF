package common;

public class Key {

	private final String document;
	private final String term;
	
	public Key (String document, String term) {
		this.document = document;
		this.term = term;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		if (!(o instanceof Key)) return false;
		Key key = (Key) o;
		return document.equals(key.document) && term.equals(key.term);
	}
	
	@Override
	public int hashCode() {
		String result = this.document + this.term;
		return result.hashCode();
	}
	
}
