package main;

import algorithms.Document;
import algorithms.TermFrequency;

public class Main {
	
	public static void main (String[] args)
	{
		Document d = new Document("/home/gabriel/Códigos/java/TF-iDF/archive/transcripts.csv");
			
		TermFrequency tf = new TermFrequency(d);
		tf.constructTableTermFrequency();
		tf.printTableTermFrequency();
	}
}

