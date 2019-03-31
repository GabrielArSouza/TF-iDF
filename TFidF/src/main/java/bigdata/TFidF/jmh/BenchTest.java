package bigdata.TFidF.jmh;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import bigdata.common.StopWord;
import bigdata.common.StopWordHolder;
import bigdata.techniques.MutexTFidF;
import bigdata.techniques.SemaphoreTFidF;
import bigdata.techniques.SequentialTFidF;

public class BenchTest {
	
	@Benchmark
	@Fork(value = 1, warmups = 2)
	@BenchmarkMode(Mode.AverageTime)
	@Warmup(iterations = 5) 
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void init() {
		String filename = "archive/forRead.txt";
		
		StopWordHolder.getStopWord();
		SemaphoreTFidF tf = new SemaphoreTFidF(filename);
		tf.readDocuments();
		tf.constructTerms();
		tf.termFrequency();
		tf.inverseDistance();
		tf.tfidfTable();
		
		try {
			tf.printTables();
		}catch(IOException e) {
			System.err.println("Something went wrong " + e.getMessage());
		}
	}
}
