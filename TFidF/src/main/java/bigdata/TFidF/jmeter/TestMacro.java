package bigdata.TFidF.jmeter;

import java.io.IOException;
import java.io.Serializable;


import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import bigdata.common.StopWordHolder;
import bigdata.techniques.ForkJoinTFidF;
import bigdata.techniques.SemaphoreTFidF;
import bigdata.techniques.tools.forkJoin.ForkJoinReader;


public class TestMacro extends AbstractJavaSamplerClient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		
		SampleResult result = new SampleResult();
		result.sampleStart();
		result.setSampleLabel("Test Sample");
		
		String filename = "/home/gabriel/Códigos/java/TF-iDF/TFidF/archive/forRead.txt";
		StopWordHolder.getStopWord();
		ForkJoinTFidF tf = new ForkJoinTFidF(filename);
		tf.run();
		
		try {
			tf.printTables();
		}catch(IOException e) {
			System.err.println("Something went wrong " + e.getMessage());
		}
		
		result.sampleEnd();
		result.setResponseCode("200");
		result.setResponseMessage("OK");
		result.setSuccessful(true);
		
		return result;
	}
}
