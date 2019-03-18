package main;

import algorithms.Document;
import algorithms.TermFrequency;
import common.StopWord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
	
	public static void main (String[] args)
	{
		String filename = "archive/forRead.txt";
		ArrayList<Document> documents = new ArrayList<Document>();
		
		String stopWordFile = "archive/stopWords.txt";
		StopWord sw = new StopWord(stopWordFile);
		System.out.println("read stop words file - " + sw.getNumberOfStopWords()
			+ " stop words loaded");
		
		try {
			FileReader file = new FileReader(filename);
			BufferedReader readFile = new BufferedReader(file);
			String line = readFile.readLine();
			while (line != null) {
				line.trim();
				documents.add(new Document(line, sw));
				System.out.println("read document in path " + line);
				line = readFile.readLine();
			}
			file.close();
		} catch (IOException e1) {
			System.err.println("could not open file " + filename 
					+ " " + e1.getMessage());
		}
		
		TermFrequency tf = new TermFrequency(documents, sw);
		tf.constructTableTermFrequency();
		System.out.println("construct table term frequency");
		
		try {
			tf.printTableTermFrequency();
		}catch(IOException e) {
			System.out.println(e);
		}
		
		System.out.println("Finish");
	}
}

