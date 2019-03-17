package main;

import algorithms.Document;
import algorithms.TermFrequency;

public class Main {
	
	public static void main (String[] args)
	{
		String filename = "archive/books/Frankenstein.txt";
		Document d = new Document(filename);
		System.out.println("read document in path " + filename);
		
		TermFrequency tf = new TermFrequency(d);
		tf.constructTableTermFrequency();
		System.out.println("construct table term frequency");
		
		tf.printTableTermFrequency();
	}
}

