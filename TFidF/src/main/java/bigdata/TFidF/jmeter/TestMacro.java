package bigdata.TFidF.jmeter;

import java.io.IOException;
import java.io.Serializable;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import bigdata.common.StopWordHolder;
import bigdata.techniques.MutexTFidF;
import bigdata.techniques.SemaphoreTFidF;
import bigdata.techniques.SequentialTFidF;

public class TestMacro extends AbstractJavaSamplerClient implements Serializable {

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		
		SampleResult result = new SampleResult();
		result.sampleStart();
		result.setSampleLabel("Test Sample");
		
		String filename = "/home/gabriel/Códigos/java/TF-iDF/TFidF/archive/forRead.txt";
		StopWordHolder.getStopWord();
		SemaphoreTFidF tf = new SemaphoreTFidF(filename);
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
