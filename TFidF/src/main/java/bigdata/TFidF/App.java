package bigdata.TFidF;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import bigdata.techniques.ForkJoinTFidF;
import bigdata.techniques.HybridTFidF;
import bigdata.techniques.MutexTFidF;
//import bigdata.techniques.SemaphoreTFidF;
//import bigdata.techniques.SequentialTFidF;
//import bigdata.algorithms.Document;
import bigdata.common.StopWord;
import bigdata.common.StopWordHolder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		// read reference set
//		HashMap<String, Double> referenceSet = new HashMap<String, Double>();
//		try {
//			FileReader file = new FileReader("archive/referenceTFidF.csv");
//			BufferedReader readFile = new BufferedReader(file);
//			
//			//discard  first line
//			String line = readFile.readLine();
//			line = readFile.readLine();
//			String[] tokens;
//			String key = "";
//			double value = 0.0;
//			System.out.println("reading...");
//			while (line != null) {
//				
//				line.trim();
//				tokens = line.split(";");
//				key = tokens[0] + "+" + tokens[1];
//				value = Double.parseDouble(tokens[2].replaceAll(",", "."));
//				referenceSet.put(key, value);
//				line = readFile.readLine();
//			}
//			file.close();
//		} catch (IOException e1) {
//			System.err.println("could not open file " + e1.getMessage());
//		}
//    		
    	String filename = "archive/forRead.txt";
		
		StopWord sw = StopWordHolder.getStopWord();
		System.out.println("read stop words file - " + sw.getNumberOfStopWords()
			+ " stop words loaded");
		
		System.out.println("Running algorithm...");
		long averageTime = 0;
		int numberTestes = 20;
		for (int i=0; i<numberTestes; i++) {
			long startTime = System.nanoTime();
				
			ForkJoinTFidF tf = new ForkJoinTFidF(filename);
			tf.run();
					
			try {
				tf.printTables();
			}catch(IOException e) {
				System.err.println("Something went wrong " + e.getMessage());
			}
	//		
			long endTime = System.nanoTime();
			long totalTime = endTime - startTime;
			System.out.println("loop" + i + " finished - time: " + totalTime/1000000 + " ms");
			averageTime += totalTime;
		
			System.out.println("Run validation");
			
//			Set<String> keys = referenceSet.keySet();
//			ConcurrentHashMap<String, Double> set = tf.getTFidF();
//			
//			for (String s : keys) {
//				if (set.get(s) == null || (referenceSet.get(s)-set.get(s)> 0.000001)) {
//					System.out.println("Error: Incorrect TFidF");
//					System.out.println("tentei buscar: " + s + " com valor: " + set.get(s) + " mas não achei");
//					System.out.println(referenceSet.get(s));
//					break;
//				}
//			}
//			System.out.println("Test ok!");
		}
		
		averageTime /= (long) numberTestes;
		long convert = TimeUnit.MILLISECONDS.convert(averageTime, TimeUnit.NANOSECONDS);
		
		System.out.println("Finish");
		System.out.println("average elapsed time: " + convert + " milisegundos ");
		
    
    }
}
