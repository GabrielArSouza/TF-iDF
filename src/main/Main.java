package main;

import algorithms.Document;
import algorithms.TermFrequency;
import common.StopWord;
import common.StopWordHolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.concurrent.*; 

public class Main {
	
	public static void main (String[] args)
	{
		String filename = "archive/forRead.txt";
		ArrayList<Document> documents = new ArrayList<Document>();
		
		StopWord sw = StopWordHolder.getStopWord();
		System.out.println("read stop words file - " + sw.getNumberOfStopWords()
			+ " stop words loaded");
		
		System.out.println("Running algorithm...");
		long startTime = System.nanoTime();
		
		try {
			FileReader file = new FileReader(filename);
			BufferedReader readFile = new BufferedReader(file);
			String line = readFile.readLine();
			while (line != null) {
				line.trim();
				documents.add(new Document(line));
				System.out.println("read document in path " + line);
				line = readFile.readLine();
			}
			file.close();
		} catch (IOException e1) {
			System.err.println("could not open file " + filename 
					+ " " + e1.getMessage());
		}
		
		TermFrequency tf = new TermFrequency(documents, sw);
				
		try {
			tf.printTables();
		}catch(IOException e) {
			System.err.println("Something went wrong " + e.getMessage());
		}
		
		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;
		long convert = TimeUnit.MILLISECONDS.convert(totalTime, TimeUnit.NANOSECONDS);
		
		System.out.println("Finish");
		System.out.println("elapsed time: " + convert + " milisegundos ");
	}
}

