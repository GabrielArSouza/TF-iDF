package common;

public class Key {

	private String document;
	private String term;
	private StringBuffer result;
	
	public Key (String document, String term) {
		this.document = document;
		this.term = term;
		this.result = new StringBuffer();
		this.result.append(document);
		this.result.append(term);
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
		return result.toString().hashCode();
	}
	
	public void setNewKey(String document, String term) {
		this.document = document;
		this.term = term;
		this.result.delete(0, this.result.length());
		result.append(document);
		result.append(term);
	}
}
