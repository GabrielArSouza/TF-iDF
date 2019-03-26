package bigdata.TFidF;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import bigdata.techniques.MutexTFidF;
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
		long averageTime = 0;
		int numberTestes = 1;
		for (int i=0; i<numberTestes; i++) {
			long startTime = System.nanoTime();
				
//			SequentialTFidF tfSeq = new SequentialTFidF(filename);
//			tfSeq.run();
			
			MutexTFidF tfMtx = new MutexTFidF(filename);
			tfMtx.run();
					
			try {
				tfMtx.printTables();
			}catch(IOException e) {
				System.err.println("Something went wrong " + e.getMessage());
			}
	//		
			long endTime = System.nanoTime();
			long totalTime = endTime - startTime;
			System.out.println("loop" + i + " finished - time: " + totalTime/1000000 + " ms");
			averageTime += totalTime;
		}
		
		averageTime /= (long) numberTestes;
		long convert = TimeUnit.MILLISECONDS.convert(averageTime, TimeUnit.NANOSECONDS);
		
		System.out.println("Finish");
		System.out.println("average elapsed time: " + convert + " milisegundos ");
    }
}
