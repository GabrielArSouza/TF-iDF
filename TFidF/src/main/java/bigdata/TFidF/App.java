package bigdata.TFidF;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import bigdata.techniques.SequentialTFidF;
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
		String filename = "archive/forRead.txt";
		
		StopWord sw = StopWordHolder.getStopWord();
		System.out.println("read stop words file - " + sw.getNumberOfStopWords()
			+ " stop words loaded");
		
		System.out.println("Running algorithm...");
		long startTime = System.nanoTime();
			
		SequentialTFidF tf = new SequentialTFidF(filename);
		tf.run();
				
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
