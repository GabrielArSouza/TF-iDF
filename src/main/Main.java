package main;

import algorithms.Document;

public class Main {
	
	public static void main (String[] args)
	{
		Document d = new Document("/home/gabriel/Códigos/java/TF-iDF/archive/transcripts.csv");
		System.out.println(d.getDocumentName());
	}
}

