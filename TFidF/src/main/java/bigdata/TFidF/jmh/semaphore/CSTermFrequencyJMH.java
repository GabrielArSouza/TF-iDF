package bigdata.TFidF.jmh.semaphore;

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
import bigdata.techniques.SemaphoreTFidF;

public class CSTermFrequencyJMH {

	//@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.MILLISECONDS)
	@Fork(value = Parameters.FORK_VALUE, warmups = Parameters.FORK_WARMUPS)
	@Warmup(iterations = Parameters.WARMUPS_ITERATIONS)
	@Measurement(iterations = Parameters.MEASUREMENT_ITERATIONS)
	public void init() {
		
		String filename = "archive/forRead.txt";
		
		StopWordHolder.getStopWord();
		SemaphoreTFidF tf = new SemaphoreTFidF(filename);
		tf.readDocuments();
		tf.constructTerms();
		tf.termFrequency();
	}
	
}
