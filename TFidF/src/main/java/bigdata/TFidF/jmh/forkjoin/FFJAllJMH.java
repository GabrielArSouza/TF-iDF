package bigdata.TFidF.jmh.forkjoin;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import bigdata.TFidF.jmh.Parameters;
import bigdata.common.StopWordHolder;
import bigdata.techniques.ForkJoinTFidF;

public class FFJAllJMH {

//	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Fork(value = Parameters.FORK_VALUE, warmups = Parameters.FORK_WARMUPS)
	@Warmup(iterations = Parameters.WARMUPS_ITERATIONS)
	@Measurement(iterations = Parameters.MEASUREMENT_ITERATIONS)
	public void init() {
		
		String filename = "archive/forRead.txt";
		
		StopWordHolder.getStopWord();
		ForkJoinTFidF tf = new ForkJoinTFidF(filename);
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
